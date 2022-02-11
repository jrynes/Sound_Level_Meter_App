package org.apache.activemq.blob;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class BlobTransferPolicy {
    private String brokerUploadUrl;
    private int bufferSize;
    private String defaultUploadUrl;
    private BlobDownloadStrategy downloadStrategy;
    private BlobUploadStrategy uploadStrategy;
    private String uploadUrl;

    public BlobTransferPolicy() {
        this.defaultUploadUrl = "http://localhost:8080/uploads/";
        this.bufferSize = AccessibilityNodeInfoCompat.ACTION_SET_SELECTION;
    }

    public BlobTransferPolicy copy() {
        BlobTransferPolicy that = new BlobTransferPolicy();
        that.defaultUploadUrl = this.defaultUploadUrl;
        that.brokerUploadUrl = this.brokerUploadUrl;
        that.uploadUrl = this.uploadUrl;
        that.bufferSize = this.bufferSize;
        that.uploadStrategy = this.uploadStrategy;
        that.downloadStrategy = this.downloadStrategy;
        return that;
    }

    public String getUploadUrl() {
        if (this.uploadUrl == null) {
            this.uploadUrl = getBrokerUploadUrl();
            if (this.uploadUrl == null) {
                this.uploadUrl = getDefaultUploadUrl();
            }
        }
        return this.uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getBrokerUploadUrl() {
        return this.brokerUploadUrl;
    }

    public void setBrokerUploadUrl(String brokerUploadUrl) {
        this.brokerUploadUrl = brokerUploadUrl;
    }

    public String getDefaultUploadUrl() {
        return this.defaultUploadUrl;
    }

    public void setDefaultUploadUrl(String defaultUploadUrl) {
        this.defaultUploadUrl = defaultUploadUrl;
    }

    public BlobUploadStrategy getUploadStrategy() {
        if (this.uploadStrategy == null) {
            this.uploadStrategy = createUploadStrategy();
        }
        return this.uploadStrategy;
    }

    public BlobDownloadStrategy getDownloadStrategy() {
        if (this.downloadStrategy == null) {
            this.downloadStrategy = createDownloadStrategy();
        }
        return this.downloadStrategy;
    }

    public void setUploadStrategy(BlobUploadStrategy uploadStrategy) {
        this.uploadStrategy = uploadStrategy;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    protected BlobUploadStrategy createUploadStrategy() {
        try {
            URL url = new URL(getUploadUrl());
            if (url.getProtocol().equalsIgnoreCase("FTP")) {
                return new FTPBlobUploadStrategy(this);
            }
            if (url.getProtocol().equalsIgnoreCase("FILE")) {
                return new FileSystemBlobStrategy(this);
            }
            return new DefaultBlobUploadStrategy(this);
        } catch (MalformedURLException e) {
            return new DefaultBlobUploadStrategy(this);
        } catch (URISyntaxException e2) {
            return new DefaultBlobUploadStrategy(this);
        }
    }

    protected BlobDownloadStrategy createDownloadStrategy() {
        try {
            URL url = new URL(getUploadUrl());
            if (url.getProtocol().equalsIgnoreCase("FTP")) {
                return new FTPBlobDownloadStrategy(this);
            }
            if (url.getProtocol().equalsIgnoreCase("FILE")) {
                return new FileSystemBlobStrategy(this);
            }
            return new DefaultBlobDownloadStrategy(this);
        } catch (MalformedURLException e) {
            return new DefaultBlobDownloadStrategy(this);
        } catch (URISyntaxException e2) {
            return new DefaultBlobDownloadStrategy(this);
        }
    }
}
