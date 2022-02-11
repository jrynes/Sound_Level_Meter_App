package org.apache.activemq.blob;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public class DefaultBlobDownloadStrategy extends DefaultStrategy implements BlobDownloadStrategy {
    public DefaultBlobDownloadStrategy(BlobTransferPolicy transferPolicy) {
        super(transferPolicy);
    }

    public InputStream getInputStream(ActiveMQBlobMessage message) throws IOException, JMSException {
        URL value = message.getURL();
        if (value == null) {
            return null;
        }
        return value.openStream();
    }

    public void deleteFile(ActiveMQBlobMessage message) throws IOException, JMSException {
        HttpURLConnection connection = (HttpURLConnection) createMessageURL(message).openConnection();
        connection.setRequestMethod(HttpRequest.METHOD_DELETE);
        connection.connect();
        connection.disconnect();
        if (!isSuccessfulCode(connection.getResponseCode())) {
            throw new IOException("DELETE was not successful: " + connection.getResponseCode() + " " + connection.getResponseMessage());
        }
    }
}
