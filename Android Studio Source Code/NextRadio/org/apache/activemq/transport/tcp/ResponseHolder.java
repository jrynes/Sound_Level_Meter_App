package org.apache.activemq.transport.tcp;

import org.apache.activemq.command.Response;

public class ResponseHolder {
    protected Object lock;
    protected boolean notified;
    protected Response response;

    public ResponseHolder() {
        this.lock = new Object();
    }

    public void setResponse(Response r) {
        synchronized (this.lock) {
            this.response = r;
            this.notified = true;
            this.lock.notify();
        }
    }

    public Response getResponse() {
        return getResponse(0);
    }

    public Response getResponse(int timeout) {
        synchronized (this.lock) {
            if (!this.notified) {
                try {
                    this.lock.wait((long) timeout);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return this.response;
    }

    public void close() {
        synchronized (this.lock) {
            this.notified = true;
            this.lock.notifyAll();
        }
    }
}
