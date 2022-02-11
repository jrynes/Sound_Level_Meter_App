package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorWorker implements Runnable {
    private static final AtomicInteger NEXT_ID;
    final int id;
    private final ConcurrentLinkedQueue<Runnable> ioTasks;
    final SelectorManager manager;
    private final int maxChannelsPerWorker;
    final AtomicInteger retainCounter;
    final Selector selector;

    class 1 implements Runnable {
        final /* synthetic */ SelectorSelection val$s;

        1(SelectorSelection selectorSelection) {
            this.val$s = selectorSelection;
        }

        public void run() {
            try {
                this.val$s.onSelect();
                this.val$s.enable();
            } catch (Throwable e) {
                this.val$s.onError(e);
            }
        }
    }

    static {
        NEXT_ID = new AtomicInteger();
    }

    public SelectorWorker(SelectorManager manager) throws IOException {
        this.id = NEXT_ID.getAndIncrement();
        this.retainCounter = new AtomicInteger(1);
        this.ioTasks = new ConcurrentLinkedQueue();
        this.manager = manager;
        this.selector = Selector.open();
        this.maxChannelsPerWorker = manager.getMaxChannelsPerWorker();
        manager.getSelectorExecutor().execute(this);
    }

    void retain() {
        if (this.retainCounter.incrementAndGet() == this.maxChannelsPerWorker) {
            this.manager.onWorkerFullEvent(this);
        }
    }

    void release() {
        int use = this.retainCounter.decrementAndGet();
        if (use == 0) {
            this.manager.onWorkerEmptyEvent(this);
        } else if (use == this.maxChannelsPerWorker - 1) {
            this.manager.onWorkerNotFullEvent(this);
        }
    }

    boolean isReleased() {
        return this.retainCounter.get() == 0;
    }

    public void addIoTask(Runnable work) {
        this.ioTasks.add(work);
        this.selector.wakeup();
    }

    private void processIoTasks() {
        while (true) {
            Runnable task = (Runnable) this.ioTasks.poll();
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    public void run() {
        SelectionKey key;
        String origName = Thread.currentThread().getName();
        Iterator i;
        SelectorSelection s;
        try {
            Thread.currentThread().setName("Selector Worker: " + this.id);
            while (!isReleased()) {
                processIoTasks();
                if (this.selector.select(10) != 0) {
                    i = this.selector.selectedKeys().iterator();
                    while (i.hasNext()) {
                        key = (SelectionKey) i.next();
                        i.remove();
                        s = (SelectorSelection) key.attachment();
                        if (key.isValid()) {
                            key.interestOps(0);
                        }
                        this.manager.getChannelExecutor().execute(new 1(s));
                    }
                    continue;
                }
            }
            try {
                this.manager.onWorkerEmptyEvent(this);
                this.selector.close();
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
            Thread.currentThread().setName(origName);
        } catch (Throwable e) {
            try {
                e.printStackTrace();
                for (SelectionKey key2 : this.selector.keys()) {
                    ((SelectorSelection) key2.attachment()).onError(e);
                }
            } finally {
                try {
                    this.manager.onWorkerEmptyEvent(this);
                    this.selector.close();
                } catch (IOException ignore2) {
                    ignore2.printStackTrace();
                }
                Thread.currentThread().setName(origName);
            }
        }
    }
}
