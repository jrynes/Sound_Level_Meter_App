package org.apache.activemq.blob;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class FileSystemBlobStrategy implements BlobUploadStrategy, BlobDownloadStrategy {
    private final BlobTransferPolicy policy;
    private File rootFile;

    public FileSystemBlobStrategy(BlobTransferPolicy policy) throws MalformedURLException, URISyntaxException {
        this.policy = policy;
        createRootFolder();
    }

    protected void createRootFolder() throws MalformedURLException, URISyntaxException {
        this.rootFile = new File(new URL(this.policy.getUploadUrl()).toURI());
        if (!this.rootFile.exists()) {
            this.rootFile.mkdirs();
        } else if (!this.rootFile.isDirectory()) {
            throw new IllegalArgumentException("Given url is not a directory " + this.rootFile);
        }
    }

    public URL uploadFile(ActiveMQBlobMessage message, File file) throws JMSException, IOException {
        return uploadStream(message, new FileInputStream(file));
    }

    public URL uploadStream(ActiveMQBlobMessage message, InputStream in) throws JMSException, IOException {
        File f = getFile(message);
        FileOutputStream out = new FileOutputStream(f);
        byte[] buffer = new byte[this.policy.getBufferSize()];
        int c = in.read(buffer);
        while (c != -1) {
            out.write(buffer, 0, c);
            out.flush();
            c = in.read(buffer);
        }
        out.flush();
        out.close();
        return f.toURI().toURL();
    }

    public void deleteFile(ActiveMQBlobMessage message) throws IOException, JMSException {
        File f = getFile(message);
        if (f.exists() && !f.delete()) {
            throw new IOException("Unable to delete file " + f);
        }
    }

    public InputStream getInputStream(ActiveMQBlobMessage message) throws IOException, JMSException {
        return new FileInputStream(getFile(message));
    }

    protected File getFile(ActiveMQBlobMessage message) throws JMSException, IOException {
        if (message.getURL() != null) {
            try {
                return new File(message.getURL().toURI());
            } catch (URISyntaxException e) {
                new IOException("Unable to open file for message " + message).initCause(e);
            }
        }
        return new File(this.rootFile, message.getJMSMessageID().replaceAll(Headers.SEPERATOR, EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR));
    }
}
