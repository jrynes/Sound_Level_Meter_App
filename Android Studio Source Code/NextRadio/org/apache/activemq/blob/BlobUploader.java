package org.apache.activemq.blob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public class BlobUploader {
    private final BlobTransferPolicy blobTransferPolicy;
    private File file;
    private InputStream in;

    public BlobUploader(BlobTransferPolicy blobTransferPolicy, InputStream in) {
        this.blobTransferPolicy = blobTransferPolicy.copy();
        this.in = in;
    }

    public BlobUploader(BlobTransferPolicy blobTransferPolicy, File file) {
        this.blobTransferPolicy = blobTransferPolicy.copy();
        this.file = file;
    }

    public URL upload(ActiveMQBlobMessage message) throws JMSException, IOException {
        if (this.file != null) {
            return getStrategy().uploadFile(message, this.file);
        }
        return getStrategy().uploadStream(message, this.in);
    }

    public BlobTransferPolicy getBlobTransferPolicy() {
        return this.blobTransferPolicy;
    }

    public BlobUploadStrategy getStrategy() {
        return getBlobTransferPolicy().getUploadStrategy();
    }
}
