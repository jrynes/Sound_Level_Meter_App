package org.apache.activemq;

import javax.jms.ExceptionListener;

public interface AsyncCallback extends ExceptionListener {
    void onSuccess();
}
