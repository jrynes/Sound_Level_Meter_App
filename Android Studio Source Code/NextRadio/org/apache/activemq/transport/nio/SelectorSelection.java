package org.apache.activemq.transport.nio;

import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.nio.SelectorManager.Listener;

public final class SelectorSelection {
    private AtomicBoolean closed;
    private int interest;
    private SelectionKey key;
    private final Listener listener;
    private final SelectorWorker worker;

    class 1 implements Runnable {
        final /* synthetic */ SocketChannel val$socketChannel;
        final /* synthetic */ SelectorWorker val$worker;

        1(SocketChannel socketChannel, SelectorWorker selectorWorker) {
            this.val$socketChannel = socketChannel;
            this.val$worker = selectorWorker;
        }

        public void run() {
            try {
                SelectorSelection.this.key = this.val$socketChannel.register(this.val$worker.selector, 0, SelectorSelection.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class 2 implements Runnable {
        2() {
        }

        public void run() {
            try {
                SelectorSelection.this.key.interestOps(SelectorSelection.this.interest);
            } catch (CancelledKeyException e) {
            }
        }
    }

    class 3 implements Runnable {
        3() {
        }

        public void run() {
            try {
                SelectorSelection.this.key.interestOps(0);
            } catch (CancelledKeyException e) {
            }
        }
    }

    class 4 implements Runnable {
        4() {
        }

        public void run() {
            try {
                SelectorSelection.this.key.cancel();
            } catch (CancelledKeyException e) {
            }
            SelectorSelection.this.worker.release();
        }
    }

    public SelectorSelection(SelectorWorker worker, SocketChannel socketChannel, Listener listener) throws ClosedChannelException {
        this.closed = new AtomicBoolean();
        this.worker = worker;
        this.listener = listener;
        worker.addIoTask(new 1(socketChannel, worker));
    }

    public void setInterestOps(int ops) {
        this.interest = ops;
    }

    public void enable() {
        this.worker.addIoTask(new 2());
    }

    public void disable() {
        this.worker.addIoTask(new 3());
    }

    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.worker.addIoTask(new 4());
        }
    }

    public void onSelect() {
        this.listener.onSelect(this);
    }

    public void onError(Throwable e) {
        this.listener.onError(this, e);
    }
}
