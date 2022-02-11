package org.apache.activemq.util;

import java.util.List;
import org.apache.activemq.Service;
import org.slf4j.LoggerFactory;

public class ServiceStopper {
    private Throwable firstException;

    public void stop(Service service) {
        if (service != null) {
            try {
                service.stop();
            } catch (Exception e) {
                onException(service, e);
            }
        }
    }

    public void run(Callback stopClosure) {
        try {
            stopClosure.execute();
        } catch (Throwable e) {
            onException(stopClosure, e);
        }
    }

    public void stopServices(List services) {
        for (Service service : services) {
            stop(service);
        }
    }

    public void onException(Object owner, Throwable e) {
        logError(owner, e);
        if (this.firstException == null) {
            this.firstException = e;
        }
    }

    public void throwFirstException() throws Exception {
        if (this.firstException == null) {
            return;
        }
        if (this.firstException instanceof Exception) {
            throw this.firstException;
        } else if (this.firstException instanceof RuntimeException) {
            throw this.firstException;
        } else {
            throw new RuntimeException("Unknown type of exception: " + this.firstException, this.firstException);
        }
    }

    protected void logError(Object service, Throwable e) {
        LoggerFactory.getLogger(service.getClass()).error("Could not stop service: " + service + ". Reason: " + e, e);
    }
}
