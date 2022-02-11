package org.apache.activemq.command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.blob.BlobDownloader;
import org.apache.activemq.blob.BlobUploader;
import org.apache.activemq.util.JMSExceptionSupport;

public class ActiveMQBlobMessage extends ActiveMQMessage implements BlobMessage {
    public static final String BINARY_MIME_TYPE = "application/octet-stream";
    public static final byte DATA_STRUCTURE_TYPE = (byte) 29;
    private transient BlobDownloader blobDownloader;
    private transient BlobUploader blobUploader;
    private boolean deletedByBroker;
    private String mimeType;
    private String name;
    private String remoteBlobUrl;
    private transient URL url;

    public Message copy() {
        ActiveMQBlobMessage copy = new ActiveMQBlobMessage();
        copy(copy);
        return copy;
    }

    private void copy(ActiveMQBlobMessage copy) {
        super.copy(copy);
        copy.setRemoteBlobUrl(getRemoteBlobUrl());
        copy.setMimeType(getMimeType());
        copy.setDeletedByBroker(isDeletedByBroker());
        copy.setBlobUploader(getBlobUploader());
        copy.setName(getName());
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getRemoteBlobUrl() {
        return this.remoteBlobUrl;
    }

    public void setRemoteBlobUrl(String remoteBlobUrl) {
        this.remoteBlobUrl = remoteBlobUrl;
        this.url = null;
    }

    public String getMimeType() {
        if (this.mimeType == null) {
            return BINARY_MIME_TYPE;
        }
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeletedByBroker() {
        return this.deletedByBroker;
    }

    public void setDeletedByBroker(boolean deletedByBroker) {
        this.deletedByBroker = deletedByBroker;
    }

    public String getJMSXMimeType() {
        return getMimeType();
    }

    public InputStream getInputStream() throws IOException, JMSException {
        if (this.blobDownloader == null) {
            return null;
        }
        return this.blobDownloader.getInputStream(this);
    }

    public URL getURL() throws JMSException {
        if (this.url == null && this.remoteBlobUrl != null) {
            try {
                this.url = new URL(this.remoteBlobUrl);
            } catch (Exception e) {
                throw JMSExceptionSupport.create(e);
            }
        }
        return this.url;
    }

    public void setURL(URL url) {
        this.url = url;
        this.remoteBlobUrl = url != null ? url.toExternalForm() : null;
    }

    public BlobUploader getBlobUploader() {
        return this.blobUploader;
    }

    public void setBlobUploader(BlobUploader blobUploader) {
        this.blobUploader = blobUploader;
    }

    public BlobDownloader getBlobDownloader() {
        return this.blobDownloader;
    }

    public void setBlobDownloader(BlobDownloader blobDownloader) {
        this.blobDownloader = blobDownloader;
    }

    public void onSend() throws JMSException {
        super.onSend();
        if (this.blobUploader != null) {
            try {
                setURL(this.blobUploader.upload(this));
            } catch (Exception e) {
                throw JMSExceptionSupport.create(e);
            }
        }
    }

    public void deleteFile() throws IOException, JMSException {
        this.blobDownloader.deleteFile(this);
    }
}
