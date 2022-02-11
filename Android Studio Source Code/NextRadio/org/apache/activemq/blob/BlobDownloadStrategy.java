package org.apache.activemq.blob;

import java.io.IOException;
import java.io.InputStream;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public interface BlobDownloadStrategy {
    void deleteFile(ActiveMQBlobMessage activeMQBlobMessage) throws IOException, JMSException;

    InputStream getInputStream(ActiveMQBlobMessage activeMQBlobMessage) throws IOException, JMSException;
}
