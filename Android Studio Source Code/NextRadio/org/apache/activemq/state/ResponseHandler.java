package org.apache.activemq.state;

import org.apache.activemq.command.Command;

public interface ResponseHandler {
    void onResponse(Command command);
}
