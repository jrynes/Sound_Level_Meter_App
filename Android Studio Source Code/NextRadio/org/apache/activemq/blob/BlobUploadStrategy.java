package org.apache.activemq.blob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public interface BlobUploadStrategy {
    URL uploadFile(ActiveMQBlobMessage activeMQBlobMessage, File file) throws JMSException, IOException;

    URL uploadStream(ActiveMQBlobMessage activeMQBlobMessage, InputStream inputStream) throws JMSException, IOException;
}
