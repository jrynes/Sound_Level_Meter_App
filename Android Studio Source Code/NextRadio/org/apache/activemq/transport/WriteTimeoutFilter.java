package org.apache.activemq.transport;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.activemq.transport.tcp.TimeStampStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteTimeoutFilter extends TransportFilter {
    private static final Logger LOG;
    protected static AtomicInteger messageCounter;
    protected static long sleep;
    protected static TimeoutThread timeoutThread;
    protected static ConcurrentLinkedQueue<WriteTimeoutFilter> writers;
    protected long writeTimeout;

    protected static class TimeoutThread extends Thread {
        static AtomicInteger instance;
        boolean run;

        static {
            instance = new AtomicInteger(0);
        }

        public TimeoutThread() {
            this.run = true;
            setName("WriteTimeoutFilter-Timeout-" + instance.incrementAndGet());
            setDaemon(true);
            setPriority(1);
            start();
        }

        public void run() {
            while (this.run) {
                try {
                    if (!interrupted()) {
                        Iterator<WriteTimeoutFilter> filters = WriteTimeoutFilter.writers.iterator();
                        while (this.run && filters.hasNext()) {
                            WriteTimeoutFilter filter = (WriteTimeoutFilter) filters.next();
                            if (filter.getWriteTimeout() > 0) {
                                long writeStart = filter.getWriter().getWriteTimestamp();
                                long delta = (!filter.getWriter().isWriting() || writeStart <= 0) ? -1 : System.currentTimeMillis() - writeStart;
                                if (delta > filter.getWriteTimeout()) {
                                    WriteTimeoutFilter.deRegisterWrite(filter, true, null);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(WriteTimeoutFilter.getSleep());
                    } catch (InterruptedException e) {
                    }
                } catch (Throwable t) {
                    if (!false) {
                        WriteTimeoutFilter.LOG.error("WriteTimeout thread unable validate existing sockets.", t);
                    }
                }
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(WriteTimeoutFilter.class);
        writers = new ConcurrentLinkedQueue();
        messageCounter = new AtomicInteger(0);
        timeoutThread = new TimeoutThread();
        sleep = 5000;
    }

    public WriteTimeoutFilter(Transport next) {
        super(next);
        this.writeTimeout = -1;
    }

    public void oneway(Object command) throws IOException {
        try {
            registerWrite(this);
            super.oneway(command);
            deRegisterWrite(this, false, null);
        } catch (IOException x) {
            throw x;
        } catch (Throwable th) {
            deRegisterWrite(this, false, null);
        }
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public static long getSleep() {
        return sleep;
    }

    public static void setSleep(long sleep) {
        sleep = sleep;
    }

    protected TimeStampStream getWriter() {
        return (TimeStampStream) this.next.narrow(TimeStampStream.class);
    }

    protected Socket getSocket() {
        return (Socket) this.next.narrow(Socket.class);
    }

    protected static void registerWrite(WriteTimeoutFilter filter) {
        writers.add(filter);
    }

    protected static boolean deRegisterWrite(WriteTimeoutFilter filter, boolean fail, IOException iox) {
        boolean result = writers.remove(filter);
        if (result && fail) {
            String message = "Forced write timeout for:" + filter.getNext().getRemoteAddress();
            LOG.warn(message);
            Socket sock = filter.getSocket();
            if (sock == null) {
                LOG.error("Destination socket is null, unable to close socket.(" + message + ")");
            } else {
                try {
                    sock.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    public void start() throws Exception {
        super.start();
    }

    public void stop() throws Exception {
        super.stop();
    }
}
