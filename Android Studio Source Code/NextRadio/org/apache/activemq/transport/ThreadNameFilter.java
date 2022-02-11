package org.apache.activemq.transport;

import java.io.IOException;

public class ThreadNameFilter extends TransportFilter {
    public ThreadNameFilter(Transport next) {
        super(next);
    }

    public void oneway(Object command) throws IOException {
        String address = this.next != null ? this.next.getRemoteAddress() : null;
        if (address != null) {
            String name = Thread.currentThread().getName();
            try {
                Thread.currentThread().setName(name + " - SendTo:" + address);
                super.oneway(command);
            } finally {
                Thread.currentThread().setName(name);
            }
        } else {
            super.oneway(command);
        }
    }
}
