package org.apache.activemq.transport.stomp;

import java.io.IOException;
import org.apache.activemq.command.Response;

interface ResponseHandler {
    void onResponse(ProtocolConverter protocolConverter, Response response) throws IOException;
}
