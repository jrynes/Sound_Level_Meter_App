package com.rabbitmq.client;

import com.rabbitmq.client.impl.AMQConnection;
import com.rabbitmq.client.impl.FrameHandler;
import com.rabbitmq.client.impl.SocketFrameHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class ConnectionFactory implements Cloneable {
    public static final int DEFAULT_AMQP_OVER_SSL_PORT = 5671;
    public static final int DEFAULT_AMQP_PORT = 5672;
    public static final int DEFAULT_CHANNEL_MAX = 0;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 0;
    public static final int DEFAULT_FRAME_MAX = 0;
    public static final int DEFAULT_HEARTBEAT = 0;
    public static final String DEFAULT_HOST = "localhost";
    @Deprecated
    public static final int DEFAULT_NUM_CONSUMER_THREADS = 5;
    public static final String DEFAULT_PASS = "guest";
    private static final String DEFAULT_SSL_PROTOCOL = "SSLv3";
    public static final String DEFAULT_USER = "guest";
    public static final String DEFAULT_VHOST = "/";
    public static final int USE_DEFAULT_PORT = -1;
    private Map<String, Object> _clientProperties;
    private int connectionTimeout;
    private SocketFactory factory;
    private String host;
    private String password;
    private int port;
    private int requestedChannelMax;
    private int requestedFrameMax;
    private int requestedHeartbeat;
    private SaslConfig saslConfig;
    private String username;
    private String virtualHost;

    public ConnectionFactory() {
        this.username = DEFAULT_USER;
        this.password = DEFAULT_USER;
        this.virtualHost = DEFAULT_VHOST;
        this.host = DEFAULT_HOST;
        this.port = USE_DEFAULT_PORT;
        this.requestedChannelMax = DEFAULT_HEARTBEAT;
        this.requestedFrameMax = DEFAULT_HEARTBEAT;
        this.requestedHeartbeat = DEFAULT_HEARTBEAT;
        this.connectionTimeout = DEFAULT_HEARTBEAT;
        this._clientProperties = AMQConnection.defaultClientProperties();
        this.factory = SocketFactory.getDefault();
        this.saslConfig = DefaultSaslConfig.PLAIN;
    }

    @Deprecated
    public int getNumConsumerThreads() {
        return DEFAULT_NUM_CONSUMER_THREADS;
    }

    @Deprecated
    public void setNumConsumerThreads(int numConsumerThreads) {
        throw new IllegalArgumentException("setNumConsumerThreads not supported -- create explicit ExecutorService instead.");
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private int portOrDefault(int port) {
        if (port != USE_DEFAULT_PORT) {
            return port;
        }
        if (isSSL()) {
            return DEFAULT_AMQP_OVER_SSL_PORT;
        }
        return DEFAULT_AMQP_PORT;
    }

    public int getPort() {
        return portOrDefault(this.port);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return this.virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public void setUri(URI uri) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        if (!"amqp".equals(uri.getScheme().toLowerCase())) {
            if ("amqps".equals(uri.getScheme().toLowerCase())) {
                setPort(DEFAULT_AMQP_OVER_SSL_PORT);
                useSslProtocol();
            } else {
                throw new IllegalArgumentException("Wrong scheme in AMQP URI: " + uri.getScheme());
            }
        }
        String host = uri.getHost();
        if (host != null) {
            setHost(host);
        }
        int port = uri.getPort();
        if (port != USE_DEFAULT_PORT) {
            setPort(port);
        }
        String userInfo = uri.getRawUserInfo();
        if (userInfo != null) {
            String[] userPass = userInfo.split(Headers.SEPERATOR);
            if (userPass.length > 2) {
                throw new IllegalArgumentException("Bad user info in AMQP URI: " + userInfo);
            }
            setUsername(uriDecode(userPass[DEFAULT_HEARTBEAT]));
            if (userPass.length == 2) {
                setPassword(uriDecode(userPass[1]));
            }
        }
        String path = uri.getRawPath();
        if (path != null && path.length() > 0) {
            if (path.indexOf(47, 1) != USE_DEFAULT_PORT) {
                throw new IllegalArgumentException("Multiple segments in path of AMQP URI: " + path);
            }
            setVirtualHost(uriDecode(uri.getPath().substring(1)));
        }
    }

    public void setUri(String uriString) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        setUri(new URI(uriString));
    }

    private String uriDecode(String s) {
        try {
            return URLDecoder.decode(s.replace("+", "%2B"), "US-ASCII");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRequestedChannelMax() {
        return this.requestedChannelMax;
    }

    public void setRequestedChannelMax(int requestedChannelMax) {
        this.requestedChannelMax = requestedChannelMax;
    }

    public int getRequestedFrameMax() {
        return this.requestedFrameMax;
    }

    public void setRequestedFrameMax(int requestedFrameMax) {
        this.requestedFrameMax = requestedFrameMax;
    }

    public int getRequestedHeartbeat() {
        return this.requestedHeartbeat;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setRequestedHeartbeat(int requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public Map<String, Object> getClientProperties() {
        return this._clientProperties;
    }

    public void setClientProperties(Map<String, Object> clientProperties) {
        this._clientProperties = clientProperties;
    }

    public SaslConfig getSaslConfig() {
        return this.saslConfig;
    }

    public void setSaslConfig(SaslConfig saslConfig) {
        this.saslConfig = saslConfig;
    }

    public SocketFactory getSocketFactory() {
        return this.factory;
    }

    public void setSocketFactory(SocketFactory factory) {
        this.factory = factory;
    }

    public boolean isSSL() {
        return getSocketFactory() instanceof SSLSocketFactory;
    }

    public void useSslProtocol() throws NoSuchAlgorithmException, KeyManagementException {
        useSslProtocol(DEFAULT_SSL_PROTOCOL);
    }

    public void useSslProtocol(String protocol) throws NoSuchAlgorithmException, KeyManagementException {
        useSslProtocol(protocol, new NullTrustManager());
    }

    public void useSslProtocol(String protocol, TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext c = SSLContext.getInstance(protocol);
        c.init(null, new TrustManager[]{trustManager}, null);
        useSslProtocol(c);
    }

    public void useSslProtocol(SSLContext context) {
        setSocketFactory(context.getSocketFactory());
    }

    protected FrameHandler createFrameHandler(Address addr) throws IOException {
        String hostName = addr.getHost();
        int portNumber = portOrDefault(addr.getPort());
        Socket socket = null;
        try {
            socket = this.factory.createSocket();
            configureSocket(socket);
            socket.connect(new InetSocketAddress(hostName, portNumber), this.connectionTimeout);
            return createFrameHandler(socket);
        } catch (IOException ioe) {
            quietTrySocketClose(socket);
            throw ioe;
        }
    }

    private static void quietTrySocketClose(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }

    protected FrameHandler createFrameHandler(Socket sock) throws IOException {
        return new SocketFrameHandler(sock);
    }

    protected void configureSocket(Socket socket) throws IOException {
        socket.setTcpNoDelay(true);
    }

    public Connection newConnection(Address[] addrs) throws IOException {
        return newConnection(null, addrs);
    }

    public Connection newConnection(ExecutorService executor, Address[] addrs) throws IOException {
        IOException lastException = null;
        Address[] arr$ = addrs;
        int len$ = arr$.length;
        int i$ = DEFAULT_HEARTBEAT;
        while (i$ < len$) {
            try {
                AMQConnection conn = new AMQConnection(this.username, this.password, createFrameHandler(arr$[i$]), executor, this.virtualHost, getClientProperties(), this.requestedFrameMax, this.requestedChannelMax, this.requestedHeartbeat, this.saslConfig);
                conn.start();
                return conn;
            } catch (IOException e) {
                lastException = e;
                i$++;
            }
        }
        if (lastException == null) {
            IOException iOException = new IOException("failed to connect");
        }
        throw lastException;
    }

    public Connection newConnection() throws IOException {
        return newConnection(null, new Address[]{new Address(getHost(), getPort())});
    }

    public Connection newConnection(ExecutorService executor) throws IOException {
        return newConnection(executor, new Address[]{new Address(getHost(), getPort())});
    }

    public ConnectionFactory clone() {
        try {
            return (ConnectionFactory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
