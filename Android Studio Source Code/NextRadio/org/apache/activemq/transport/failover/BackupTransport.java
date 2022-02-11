package org.apache.activemq.transport.failover;

import java.io.IOException;
import java.net.URI;
import org.apache.activemq.transport.DefaultTransportListener;
import org.apache.activemq.transport.Transport;

class BackupTransport extends DefaultTransportListener {
    private boolean disposed;
    private final FailoverTransport failoverTransport;
    private Transport transport;
    private URI uri;

    BackupTransport(FailoverTransport ft) {
        this.failoverTransport = ft;
    }

    public void onException(IOException error) {
        this.disposed = true;
        if (this.failoverTransport != null) {
            this.failoverTransport.reconnect(false);
        }
    }

    public Transport getTransport() {
        return this.transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
        this.transport.setTransportListener(this);
    }

    public URI getUri() {
        return this.uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isDisposed() {
        return this.disposed || (this.transport != null && this.transport.isDisposed());
    }

    public void setDisposed(boolean disposed) {
        this.disposed = disposed;
    }

    public int hashCode() {
        return this.uri != null ? this.uri.hashCode() : -1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BackupTransport)) {
            return false;
        }
        BackupTransport other = (BackupTransport) obj;
        if ((this.uri != null || other.uri != null) && (this.uri == null || other.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "Backup transport: " + this.uri;
    }
}
