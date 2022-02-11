package org.apache.activemq.transport;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.activemq.command.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureResponse {
    private static final Logger LOG;
    private final ResponseCallback responseCallback;
    private final ArrayBlockingQueue<Response> responseSlot;

    static {
        LOG = LoggerFactory.getLogger(FutureResponse.class);
    }

    public FutureResponse(ResponseCallback responseCallback) {
        this.responseSlot = new ArrayBlockingQueue(1);
        this.responseCallback = responseCallback;
    }

    public Response getResult() throws IOException {
        try {
            return (Response) this.responseSlot.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Operation interupted: " + e, e);
            }
            throw new InterruptedIOException("Interrupted.");
        }
    }

    public Response getResult(int timeout) throws IOException {
        try {
            Response result = (Response) this.responseSlot.poll((long) timeout, TimeUnit.MILLISECONDS);
            if (result != null || timeout <= 0) {
                return result;
            }
            throw new RequestTimedOutIOException();
        } catch (InterruptedException e) {
            throw new InterruptedIOException("Interrupted.");
        }
    }

    public void set(Response result) {
        if (this.responseSlot.offer(result) && this.responseCallback != null) {
            this.responseCallback.onCompletion(this);
        }
    }
}
