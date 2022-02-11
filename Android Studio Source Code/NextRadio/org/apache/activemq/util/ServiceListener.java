package org.apache.activemq.util;

import org.apache.activemq.Service;

public interface ServiceListener {
    void started(Service service);

    void stopped(Service service);
}
