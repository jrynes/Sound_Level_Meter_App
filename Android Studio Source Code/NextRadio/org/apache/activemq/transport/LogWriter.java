package org.apache.activemq.transport;

import java.io.IOException;
import org.slf4j.Logger;

public interface LogWriter {
    void initialMessage(Logger logger);

    void logAsyncRequest(Logger logger, Object obj);

    void logOneWay(Logger logger, Object obj);

    void logReceivedCommand(Logger logger, Object obj);

    void logReceivedException(Logger logger, IOException iOException);

    void logRequest(Logger logger, Object obj);

    void logResponse(Logger logger, Object obj);
}
