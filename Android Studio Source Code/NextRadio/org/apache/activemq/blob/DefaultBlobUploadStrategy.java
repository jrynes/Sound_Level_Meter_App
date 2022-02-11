package org.apache.activemq.blob;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public class DefaultBlobUploadStrategy extends DefaultStrategy implements BlobUploadStrategy {
    public DefaultBlobUploadStrategy(BlobTransferPolicy transferPolicy) {
        super(transferPolicy);
    }

    public URL uploadFile(ActiveMQBlobMessage message, File file) throws JMSException, IOException {
        return uploadStream(message, new FileInputStream(file));
    }

    public URL uploadStream(ActiveMQBlobMessage message, InputStream fis) throws JMSException, IOException {
        URL url = createMessageURL(message);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpRequest.METHOD_PUT);
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(this.transferPolicy.getBufferSize());
        OutputStream os = connection.getOutputStream();
        byte[] buf = new byte[this.transferPolicy.getBufferSize()];
        int c = fis.read(buf);
        while (c != -1) {
            os.write(buf, 0, c);
            os.flush();
            c = fis.read(buf);
        }
        os.close();
        fis.close();
        if (isSuccessfulCode(connection.getResponseCode())) {
            return url;
        }
        throw new IOException("PUT was not successful: " + connection.getResponseCode() + " " + connection.getResponseMessage());
    }
}
