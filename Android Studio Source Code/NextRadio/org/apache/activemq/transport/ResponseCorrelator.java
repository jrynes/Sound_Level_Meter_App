package org.apache.activemq.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.Response;
import org.apache.activemq.util.IntSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseCorrelator extends TransportFilter {
    private static final Logger LOG;
    private final boolean debug;
    private IOException error;
    private final Map<Integer, FutureResponse> requestMap;
    private IntSequenceGenerator sequenceGenerator;

    static {
        LOG = LoggerFactory.getLogger(ResponseCorrelator.class);
    }

    public ResponseCorrelator(Transport next) {
        this(next, new IntSequenceGenerator());
    }

    public ResponseCorrelator(Transport next, IntSequenceGenerator sequenceGenerator) {
        super(next);
        this.requestMap = new HashMap();
        this.debug = LOG.isDebugEnabled();
        this.sequenceGenerator = sequenceGenerator;
    }

    public void oneway(Object o) throws IOException {
        Command command = (Command) o;
        command.setCommandId(this.sequenceGenerator.getNextSequenceId());
        command.setResponseRequired(false);
        this.next.oneway(command);
    }

    public FutureResponse asyncRequest(Object o, ResponseCallback responseCallback) throws IOException {
        Command command = (Command) o;
        command.setCommandId(this.sequenceGenerator.getNextSequenceId());
        command.setResponseRequired(true);
        FutureResponse future = new FutureResponse(responseCallback);
        synchronized (this.requestMap) {
            IOException priorError = this.error;
            if (priorError == null) {
                this.requestMap.put(new Integer(command.getCommandId()), future);
            }
        }
        if (priorError != null) {
            future.set(new ExceptionResponse(priorError));
            throw priorError;
        }
        this.next.oneway(command);
        return future;
    }

    public Object request(Object command) throws IOException {
        return asyncRequest(command, null).getResult();
    }

    public Object request(Object command, int timeout) throws IOException {
        return asyncRequest(command, null).getResult(timeout);
    }

    public void onCommand(Object o) {
        if (o instanceof Command) {
            Command command = (Command) o;
            if (command.isResponse()) {
                FutureResponse future;
                Response response = (Response) command;
                synchronized (this.requestMap) {
                    future = (FutureResponse) this.requestMap.remove(Integer.valueOf(response.getCorrelationId()));
                }
                if (future != null) {
                    future.set(response);
                    return;
                } else if (this.debug) {
                    LOG.debug("Received unexpected response: {" + command + "}for command id: " + response.getCorrelationId());
                    return;
                } else {
                    return;
                }
            }
            getTransportListener().onCommand(command);
            return;
        }
        throw new ClassCastException("Object cannot be converted to a Command,  Object: " + o);
    }

    public void onException(IOException error) {
        dispose(error);
        super.onException(error);
    }

    public void stop() throws Exception {
        dispose(new IOException("Stopped."));
        super.stop();
    }

    private void dispose(IOException error) {
        ArrayList<FutureResponse> requests = null;
        synchronized (this.requestMap) {
            if (this.error == null) {
                this.error = error;
                ArrayList<FutureResponse> requests2 = new ArrayList(this.requestMap.values());
                try {
                    this.requestMap.clear();
                    requests = requests2;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    requests = requests2;
                    throw th2;
                }
            }
            try {
                if (requests != null) {
                    Iterator<FutureResponse> iter = requests.iterator();
                    while (iter.hasNext()) {
                        ((FutureResponse) iter.next()).set(new ExceptionResponse(error));
                    }
                }
            } catch (Throwable th3) {
                th2 = th3;
                throw th2;
            }
        }
    }

    public IntSequenceGenerator getSequenceGenerator() {
        return this.sequenceGenerator;
    }

    public String toString() {
        return this.next.toString();
    }
}
