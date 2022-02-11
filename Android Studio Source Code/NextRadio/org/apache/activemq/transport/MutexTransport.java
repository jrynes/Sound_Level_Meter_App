package org.apache.activemq.transport;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class MutexTransport extends TransportFilter {
    private boolean syncOnCommand;
    private final ReentrantLock writeLock;

    public MutexTransport(Transport next) {
        super(next);
        this.writeLock = new ReentrantLock();
        this.syncOnCommand = false;
    }

    public MutexTransport(Transport next, boolean syncOnCommand) {
        super(next);
        this.writeLock = new ReentrantLock();
        this.syncOnCommand = syncOnCommand;
    }

    public void onCommand(Object command) {
        if (this.syncOnCommand) {
            this.writeLock.lock();
            try {
                this.transportListener.onCommand(command);
            } finally {
                this.writeLock.unlock();
            }
        } else {
            this.transportListener.onCommand(command);
        }
    }

    public FutureResponse asyncRequest(Object command, ResponseCallback responseCallback) throws IOException {
        this.writeLock.lock();
        try {
            FutureResponse asyncRequest = this.next.asyncRequest(command, null);
            return asyncRequest;
        } finally {
            this.writeLock.unlock();
        }
    }

    public void oneway(Object command) throws IOException {
        this.writeLock.lock();
        try {
            this.next.oneway(command);
        } finally {
            this.writeLock.unlock();
        }
    }

    public Object request(Object command) throws IOException {
        this.writeLock.lock();
        try {
            Object request = this.next.request(command);
            return request;
        } finally {
            this.writeLock.unlock();
        }
    }

    public Object request(Object command, int timeout) throws IOException {
        this.writeLock.lock();
        try {
            Object request = this.next.request(command, timeout);
            return request;
        } finally {
            this.writeLock.unlock();
        }
    }

    public String toString() {
        return this.next.toString();
    }

    public boolean isSyncOnCommand() {
        return this.syncOnCommand;
    }

    public void setSyncOnCommand(boolean syncOnCommand) {
        this.syncOnCommand = syncOnCommand;
    }
}
