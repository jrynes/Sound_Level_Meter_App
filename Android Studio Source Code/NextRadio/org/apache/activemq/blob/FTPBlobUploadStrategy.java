package org.apache.activemq.blob;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.commons.net.ftp.FTPClient;

public class FTPBlobUploadStrategy extends FTPStrategy implements BlobUploadStrategy {
    public FTPBlobUploadStrategy(BlobTransferPolicy transferPolicy) throws MalformedURLException {
        super(transferPolicy);
    }

    public URL uploadFile(ActiveMQBlobMessage message, File file) throws JMSException, IOException {
        return uploadStream(message, new FileInputStream(file));
    }

    public URL uploadStream(ActiveMQBlobMessage message, InputStream in) throws JMSException, IOException {
        FTPClient ftp = createFTP();
        try {
            String url;
            String path = this.url.getPath();
            String workingDir = path.substring(0, path.lastIndexOf(ReadOnlyContext.SEPARATOR));
            String filename = message.getMessageId().toString().replaceAll(Headers.SEPERATOR, EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
            ftp.setFileType(2);
            if (ftp.changeWorkingDirectory(workingDir)) {
                url = this.url.toString();
            } else {
                url = this.url.toString().replaceFirst(this.url.getPath(), Stomp.EMPTY) + ReadOnlyContext.SEPARATOR;
            }
            if (ftp.storeFile(filename, in)) {
                URL url2 = new URL(url + filename);
                return url2;
            }
            throw new JMSException("FTP store failed: " + ftp.getReplyString());
        } finally {
            ftp.quit();
            ftp.disconnect();
        }
    }
}
