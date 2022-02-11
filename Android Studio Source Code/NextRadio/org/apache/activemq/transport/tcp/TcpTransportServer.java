package org.apache.activemq.transport.tcp;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.AdError;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.net.ServerSocketFactory;
import org.apache.activemq.Service;
import org.apache.activemq.TransportLoggerSupport;
import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.openwire.OpenWireFormatFactory;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportServerThreadSupport;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.InetAddressUtil;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.ServiceListener;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.util.ServiceSupport;
import org.apache.activemq.util.ThreadPoolUtils;
import org.apache.activemq.wireformat.WireFormat;
import org.apache.activemq.wireformat.WireFormatFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpTransportServer extends TransportServerThreadSupport implements ServiceListener {
    private static final Logger LOG;
    protected int backlog;
    protected int connectionTimeout;
    protected int currentTransportCount;
    protected boolean dynamicManagement;
    protected String logWriterName;
    protected long maxInactivityDuration;
    protected long maxInactivityDurationInitalDelay;
    protected int maximumConnections;
    protected int minmumWireFormatVersion;
    protected ServerSocket serverSocket;
    protected final ServerSocketFactory serverSocketFactory;
    protected int soTimeout;
    protected int socketBufferSize;
    protected Thread socketHandlerThread;
    protected BlockingQueue<Socket> socketQueue;
    protected boolean startLogging;
    protected boolean trace;
    protected final TcpTransportFactory transportFactory;
    protected boolean useQueueForAccept;
    protected WireFormatFactory wireFormatFactory;

    class 1 implements Runnable {
        1() {
        }

        public void run() {
            while (!TcpTransportServer.this.isStopped() && !TcpTransportServer.this.isStopping()) {
                try {
                    Socket sock = (Socket) TcpTransportServer.this.socketQueue.poll(1, TimeUnit.SECONDS);
                    if (sock != null) {
                        TcpTransportServer.this.handleSocket(sock);
                    }
                } catch (InterruptedException e) {
                    TcpTransportServer.LOG.info("socketQueue interuppted - stopping");
                    if (!TcpTransportServer.this.isStopping()) {
                        TcpTransportServer.this.onAcceptError(e);
                        return;
                    }
                    return;
                }
            }
        }
    }

    static {
        LOG = LoggerFactory.getLogger(TcpTransportServer.class);
    }

    public TcpTransportServer(TcpTransportFactory transportFactory, URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        super(location);
        this.backlog = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.wireFormatFactory = new OpenWireFormatFactory();
        this.maxInactivityDuration = 30000;
        this.maxInactivityDurationInitalDelay = ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION;
        this.useQueueForAccept = true;
        this.trace = false;
        this.soTimeout = 0;
        this.socketBufferSize = AccessibilityNodeInfoCompat.ACTION_CUT;
        this.connectionTimeout = Constants.LOADING_INTERVAL;
        this.logWriterName = TransportLoggerSupport.defaultLogWriterName;
        this.dynamicManagement = false;
        this.startLogging = true;
        this.socketQueue = new LinkedBlockingQueue();
        this.maximumConnections = Integer.MAX_VALUE;
        this.currentTransportCount = 0;
        this.transportFactory = transportFactory;
        this.serverSocketFactory = serverSocketFactory;
    }

    public void bind() throws IOException {
        URI bind = getBindLocation();
        String host = bind.getHost();
        if (host == null || host.length() == 0) {
            host = ConnectionFactory.DEFAULT_HOST;
        }
        InetAddress addr = InetAddress.getByName(host);
        try {
            this.serverSocket = this.serverSocketFactory.createServerSocket(bind.getPort(), this.backlog, addr);
            configureServerSocket(this.serverSocket);
            try {
                setConnectURI(new URI(bind.getScheme(), bind.getUserInfo(), resolveHostName(this.serverSocket, addr), this.serverSocket.getLocalPort(), bind.getPath(), bind.getQuery(), bind.getFragment()));
            } catch (URISyntaxException e) {
                try {
                    setConnectURI(new URI(bind.getScheme(), bind.getUserInfo(), addr.getHostAddress(), this.serverSocket.getLocalPort(), bind.getPath(), bind.getQuery(), bind.getFragment()));
                } catch (Exception e2) {
                    throw IOExceptionSupport.create(e2);
                }
            }
        } catch (Exception e3) {
            throw IOExceptionSupport.create("Failed to bind to server socket: " + bind + " due to: " + e3, e3);
        }
    }

    private void configureServerSocket(ServerSocket socket) throws SocketException {
        socket.setSoTimeout(AdError.SERVER_ERROR_CODE);
        if (this.transportOptions != null) {
            IntrospectionSupport.setProperties(socket, this.transportOptions);
        }
    }

    public WireFormatFactory getWireFormatFactory() {
        return this.wireFormatFactory;
    }

    public void setWireFormatFactory(WireFormatFactory wireFormatFactory) {
        this.wireFormatFactory = wireFormatFactory;
    }

    public void setBrokerInfo(BrokerInfo brokerInfo) {
    }

    public long getMaxInactivityDuration() {
        return this.maxInactivityDuration;
    }

    public void setMaxInactivityDuration(long maxInactivityDuration) {
        this.maxInactivityDuration = maxInactivityDuration;
    }

    public long getMaxInactivityDurationInitalDelay() {
        return this.maxInactivityDurationInitalDelay;
    }

    public void setMaxInactivityDurationInitalDelay(long maxInactivityDurationInitalDelay) {
        this.maxInactivityDurationInitalDelay = maxInactivityDurationInitalDelay;
    }

    public int getMinmumWireFormatVersion() {
        return this.minmumWireFormatVersion;
    }

    public void setMinmumWireFormatVersion(int minmumWireFormatVersion) {
        this.minmumWireFormatVersion = minmumWireFormatVersion;
    }

    public boolean isTrace() {
        return this.trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public String getLogWriterName() {
        return this.logWriterName;
    }

    public void setLogWriterName(String logFormat) {
        this.logWriterName = logFormat;
    }

    public boolean isDynamicManagement() {
        return this.dynamicManagement;
    }

    public void setDynamicManagement(boolean useJmx) {
        this.dynamicManagement = useJmx;
    }

    public boolean isStartLogging() {
        return this.startLogging;
    }

    public void setStartLogging(boolean startLogging) {
        this.startLogging = startLogging;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public boolean isUseQueueForAccept() {
        return this.useQueueForAccept;
    }

    public void setUseQueueForAccept(boolean useQueueForAccept) {
        this.useQueueForAccept = useQueueForAccept;
    }

    public void run() {
        while (!isStopped()) {
            try {
                Socket socket = this.serverSocket.accept();
                if (socket != null) {
                    if (isStopped() || getAcceptListener() == null) {
                        socket.close();
                    } else if (this.useQueueForAccept) {
                        this.socketQueue.put(socket);
                    } else {
                        handleSocket(socket);
                    }
                }
            } catch (SocketTimeoutException e) {
            } catch (Exception e2) {
                if (!isStopping()) {
                    onAcceptError(e2);
                } else if (!isStopped()) {
                    LOG.warn("run()", e2);
                    onAcceptError(e2);
                }
            }
        }
    }

    protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
        return new TcpTransport(format, socket);
    }

    public String toString() {
        return Stomp.EMPTY + getBindLocation();
    }

    protected String resolveHostName(ServerSocket socket, InetAddress bindAddress) throws UnknownHostException {
        if (!socket.isBound()) {
            return bindAddress.getCanonicalHostName();
        }
        if (socket.getInetAddress().isAnyLocalAddress()) {
            return InetAddressUtil.getLocalHostName();
        }
        return socket.getInetAddress().getCanonicalHostName();
    }

    protected void doStart() throws Exception {
        if (this.useQueueForAccept) {
            this.socketHandlerThread = new Thread(null, new 1(), "ActiveMQ Transport Server Thread Handler: " + toString(), getStackSize());
            this.socketHandlerThread.setDaemon(true);
            this.socketHandlerThread.setPriority(8);
            this.socketHandlerThread.start();
        }
        super.doStart();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        super.doStop(stopper);
        if (this.serverSocket != null) {
            this.serverSocket.close();
        }
    }

    public InetSocketAddress getSocketAddress() {
        return (InetSocketAddress) this.serverSocket.getLocalSocketAddress();
    }

    protected final void handleSocket(Socket socket) {
        try {
            if (this.currentTransportCount >= this.maximumConnections) {
                throw new ExceededMaximumConnectionsException("Exceeded the maximum number of allowed client connections. See the 'maximumConnections' property on the TCP transport configuration URI in the ActiveMQ configuration file (e.g., activemq.xml)");
            }
            HashMap<String, Object> options = new HashMap();
            options.put("maxInactivityDuration", Long.valueOf(this.maxInactivityDuration));
            options.put("maxInactivityDurationInitalDelay", Long.valueOf(this.maxInactivityDurationInitalDelay));
            options.put("minmumWireFormatVersion", Integer.valueOf(this.minmumWireFormatVersion));
            options.put("trace", Boolean.valueOf(this.trace));
            options.put("soTimeout", Integer.valueOf(this.soTimeout));
            options.put("socketBufferSize", Integer.valueOf(this.socketBufferSize));
            options.put("connectionTimeout", Integer.valueOf(this.connectionTimeout));
            options.put("logWriterName", this.logWriterName);
            options.put("dynamicManagement", Boolean.valueOf(this.dynamicManagement));
            options.put("startLogging", Boolean.valueOf(this.startLogging));
            options.putAll(this.transportOptions);
            WireFormat format = this.wireFormatFactory.createWireFormat();
            Transport transport = createTransport(socket, format);
            if (transport instanceof ServiceSupport) {
                ((ServiceSupport) transport).addServiceListener(this);
            }
            getAcceptListener().onAccept(this.transportFactory.serverConfigure(transport, format, options));
        } catch (SocketTimeoutException e) {
        } catch (Exception e2) {
            if (!isStopping()) {
                onAcceptError(e2);
            } else if (!isStopped()) {
                LOG.warn("run()", e2);
                onAcceptError(e2);
            }
        }
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getSocketBufferSize() {
        return this.socketBufferSize;
    }

    public void setSocketBufferSize(int socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaximumConnections() {
        return this.maximumConnections;
    }

    public void setMaximumConnections(int maximumConnections) {
        this.maximumConnections = maximumConnections;
    }

    public void started(Service service) {
        this.currentTransportCount++;
    }

    public void stopped(Service service) {
        this.currentTransportCount--;
    }

    public boolean isSslServer() {
        return false;
    }
}
