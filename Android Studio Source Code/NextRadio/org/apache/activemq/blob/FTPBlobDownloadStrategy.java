package org.apache.activemq.blob;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.commons.net.ftp.FTPClient;

public class FTPBlobDownloadStrategy extends FTPStrategy implements BlobDownloadStrategy {

    class 1 extends FilterInputStream {
        final /* synthetic */ FTPClient val$ftp;

        1(InputStream x0, FTPClient fTPClient) {
            this.val$ftp = fTPClient;
            super(x0);
        }

        public void close() throws IOException {
            this.in.close();
            this.val$ftp.quit();
            this.val$ftp.disconnect();
        }
    }

    public FTPBlobDownloadStrategy(BlobTransferPolicy transferPolicy) throws MalformedURLException {
        super(transferPolicy);
    }

    public InputStream getInputStream(ActiveMQBlobMessage message) throws IOException, JMSException {
        this.url = message.getURL();
        FTPClient ftp = createFTP();
        String path = this.url.getPath();
        String workingDir = path.substring(0, path.lastIndexOf(ReadOnlyContext.SEPARATOR));
        String file = path.substring(path.lastIndexOf(ReadOnlyContext.SEPARATOR) + 1);
        ftp.changeWorkingDirectory(workingDir);
        ftp.setFileType(2);
        return new 1(ftp.retrieveFileStream(file), ftp);
    }

    public void deleteFile(ActiveMQBlobMessage message) throws IOException, JMSException {
        this.url = message.getURL();
        FTPClient ftp = createFTP();
        try {
            if (!ftp.deleteFile(this.url.getPath())) {
                throw new JMSException("Delete file failed: " + ftp.getReplyString());
            }
        } finally {
            ftp.quit();
            ftp.disconnect();
        }
    }
}
