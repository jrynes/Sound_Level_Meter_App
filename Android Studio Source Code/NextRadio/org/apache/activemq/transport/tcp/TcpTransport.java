package org.apache.activemq.transport.tcp;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import com.admarvel.android.ads.Constants;
import com.rabbitmq.client.ConnectionFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.SocketFactory;
import org.apache.activemq.Service;
import org.apache.activemq.TransportLoggerSupport;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportThreadSupport;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.util.InetAddressUtil;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.wireformat.WireFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.KEYRecord.Flags;

public class TcpTransport extends TransportThreadSupport implements Transport, Service, Runnable {
    private static final Logger LOG;
    protected TimeStampStream buffOut;
    protected boolean closeAsync;
    protected int connectionTimeout;
    protected DataInputStream dataIn;
    protected DataOutputStream dataOut;
    protected boolean diffServChosen;
    protected boolean dynamicManagement;
    protected int ioBufferSize;
    protected int jmxPort;
    private Boolean keepAlive;
    protected final URI localLocation;
    protected String logWriterName;
    protected int minmumWireFormatVersion;
    protected volatile int receiveCounter;
    protected final URI remoteLocation;
    private Thread runnerThread;
    private int soLinger;
    protected int soTimeout;
    protected Socket socket;
    protected int socketBufferSize;
    protected SocketFactory socketFactory;
    private Map<String, Object> socketOptions;
    protected boolean startLogging;
    protected final AtomicReference<CountDownLatch> stoppedLatch;
    private Boolean tcpNoDelay;
    protected boolean trace;
    protected int trafficClass;
    private boolean trafficClassSet;
    protected boolean typeOfServiceChosen;
    protected boolean useLocalHost;
    protected final WireFormat wireFormat;

    class 1 implements Runnable {
        final /* synthetic */ CountDownLatch val$latch;

        1(CountDownLatch countDownLatch) {
            this.val$latch = countDownLatch;
        }

        public void run() {
            TcpTransport.LOG.trace("Closing socket {}", TcpTransport.this.socket);
            try {
                TcpTransport.this.socket.close();
                TcpTransport.LOG.debug("Closed socket {}", TcpTransport.this.socket);
                this.val$latch.countDown();
            } catch (IOException e) {
                if (TcpTransport.LOG.isDebugEnabled()) {
                    TcpTransport.LOG.debug("Caught exception closing socket " + TcpTransport.this.socket + ". This exception will be ignored.", e);
                }
                this.val$latch.countDown();
            } catch (Throwable th) {
                this.val$latch.countDown();
            }
        }
    }

    class 2 extends TcpBufferedInputStream {
        2(InputStream x0, int x1) {
            super(x0, x1);
        }

        public int read() throws IOException {
            TcpTransport tcpTransport = TcpTransport.this;
            tcpTransport.receiveCounter++;
            return super.read();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            TcpTransport tcpTransport = TcpTransport.this;
            tcpTransport.receiveCounter++;
            return super.read(b, off, len);
        }

        public long skip(long n) throws IOException {
            TcpTransport tcpTransport = TcpTransport.this;
            tcpTransport.receiveCounter++;
            return super.skip(n);
        }

        protected void fill() throws IOException {
            TcpTransport tcpTransport = TcpTransport.this;
            tcpTransport.receiveCounter++;
            super.fill();
        }
    }

    static {
        LOG = LoggerFactory.getLogger(TcpTransport.class);
    }

    public TcpTransport(WireFormat wireFormat, SocketFactory socketFactory, URI remoteLocation, URI localLocation) throws UnknownHostException, IOException {
        this.connectionTimeout = Constants.LOADING_INTERVAL;
        this.socketBufferSize = AccessibilityNodeInfoCompat.ACTION_CUT;
        this.ioBufferSize = Flags.FLAG2;
        this.closeAsync = true;
        this.buffOut = null;
        this.trafficClass = 0;
        this.trafficClassSet = false;
        this.diffServChosen = false;
        this.typeOfServiceChosen = false;
        this.trace = false;
        this.logWriterName = TransportLoggerSupport.defaultLogWriterName;
        this.dynamicManagement = false;
        this.startLogging = true;
        this.jmxPort = 1099;
        this.useLocalHost = false;
        this.stoppedLatch = new AtomicReference();
        this.soLinger = ExploreByTouchHelper.INVALID_ID;
        this.wireFormat = wireFormat;
        this.socketFactory = socketFactory;
        try {
            this.socket = socketFactory.createSocket();
        } catch (SocketException e) {
            this.socket = null;
        }
        this.remoteLocation = remoteLocation;
        this.localLocation = localLocation;
        setDaemon(false);
    }

    public TcpTransport(WireFormat wireFormat, Socket socket) throws IOException {
        this.connectionTimeout = Constants.LOADING_INTERVAL;
        this.socketBufferSize = AccessibilityNodeInfoCompat.ACTION_CUT;
        this.ioBufferSize = Flags.FLAG2;
        this.closeAsync = true;
        this.buffOut = null;
        this.trafficClass = 0;
        this.trafficClassSet = false;
        this.diffServChosen = false;
        this.typeOfServiceChosen = false;
        this.trace = false;
        this.logWriterName = TransportLoggerSupport.defaultLogWriterName;
        this.dynamicManagement = false;
        this.startLogging = true;
        this.jmxPort = 1099;
        this.useLocalHost = false;
        this.stoppedLatch = new AtomicReference();
        this.soLinger = ExploreByTouchHelper.INVALID_ID;
        this.wireFormat = wireFormat;
        this.socket = socket;
        this.remoteLocation = null;
        this.localLocation = null;
        setDaemon(true);
    }

    public void oneway(Object command) throws IOException {
        checkStarted();
        this.wireFormat.marshal(command, this.dataOut);
        this.dataOut.flush();
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append(Stomp.EMPTY);
        r0 = this.socket.isConnected() ? "tcp://" + this.socket.getInetAddress() + Headers.SEPERATOR + this.socket.getPort() + "@" + this.socket.getLocalPort() : this.localLocation != null ? this.localLocation : this.remoteLocation;
        return append.append(r0).toString();
    }

    public void run() {
        LOG.trace("TCP consumer thread for " + this + " starting");
        this.runnerThread = Thread.currentThread();
        while (!isStopped()) {
            try {
                doRun();
            } catch (IOException e) {
                ((CountDownLatch) this.stoppedLatch.get()).countDown();
                onException(e);
                ((CountDownLatch) this.stoppedLatch.get()).countDown();
                return;
            } catch (Throwable th) {
                Throwable th2 = th;
                ((CountDownLatch) this.stoppedLatch.get()).countDown();
            }
        }
        ((CountDownLatch) this.stoppedLatch.get()).countDown();
    }

    protected void doRun() throws IOException {
        try {
            doConsume(readCommand());
        } catch (SocketTimeoutException e) {
        } catch (InterruptedIOException e2) {
        }
    }

    protected Object readCommand() throws IOException {
        return this.wireFormat.unmarshal(this.dataIn);
    }

    public String getDiffServ() {
        return Integer.toString(this.trafficClass);
    }

    public void setDiffServ(String diffServ) throws IllegalArgumentException {
        this.trafficClass = QualityOfServiceUtils.getDSCP(diffServ);
        this.diffServChosen = true;
    }

    public int getTypeOfService() {
        return this.trafficClass;
    }

    public void setTypeOfService(int typeOfService) {
        this.trafficClass = QualityOfServiceUtils.getToS(typeOfService);
        this.typeOfServiceChosen = true;
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

    public int getJmxPort() {
        return this.jmxPort;
    }

    public void setJmxPort(int jmxPort) {
        this.jmxPort = jmxPort;
    }

    public int getMinmumWireFormatVersion() {
        return this.minmumWireFormatVersion;
    }

    public void setMinmumWireFormatVersion(int minmumWireFormatVersion) {
        this.minmumWireFormatVersion = minmumWireFormatVersion;
    }

    public boolean isUseLocalHost() {
        return this.useLocalHost;
    }

    public void setUseLocalHost(boolean useLocalHost) {
        this.useLocalHost = useLocalHost;
    }

    public int getSocketBufferSize() {
        return this.socketBufferSize;
    }

    public void setSocketBufferSize(int socketBufferSize) {
        this.socketBufferSize = socketBufferSize;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Boolean getKeepAlive() {
        return this.keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public int getSoLinger() {
        return this.soLinger;
    }

    public Boolean getTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getIoBufferSize() {
        return this.ioBufferSize;
    }

    public void setIoBufferSize(int ioBufferSize) {
        this.ioBufferSize = ioBufferSize;
    }

    public boolean isCloseAsync() {
        return this.closeAsync;
    }

    public void setCloseAsync(boolean closeAsync) {
        this.closeAsync = closeAsync;
    }

    protected String resolveHostName(String host) throws UnknownHostException {
        if (!isUseLocalHost()) {
            return host;
        }
        String localName = InetAddressUtil.getLocalHostName();
        if (localName == null || !localName.equals(host)) {
            return host;
        }
        return ConnectionFactory.DEFAULT_HOST;
    }

    protected void initialiseSocket(Socket sock) throws SocketException, IllegalArgumentException {
        if (this.socketOptions != null) {
            IntrospectionSupport.setProperties(this.socket, this.socketOptions);
        }
        try {
            sock.setReceiveBufferSize(this.socketBufferSize);
            sock.setSendBufferSize(this.socketBufferSize);
        } catch (SocketException se) {
            LOG.warn("Cannot set socket buffer size = " + this.socketBufferSize);
            LOG.debug("Cannot set socket buffer size. Reason: " + se, se);
        }
        sock.setSoTimeout(this.soTimeout);
        if (this.keepAlive != null) {
            sock.setKeepAlive(this.keepAlive.booleanValue());
        }
        if (this.soLinger > -1) {
            sock.setSoLinger(true, this.soLinger);
        } else if (this.soLinger == -1) {
            sock.setSoLinger(false, 0);
        }
        if (this.tcpNoDelay != null) {
            sock.setTcpNoDelay(this.tcpNoDelay.booleanValue());
        }
        if (!this.trafficClassSet) {
            this.trafficClassSet = setTrafficClass(sock);
        }
    }

    protected void doStart() throws Exception {
        connect();
        this.stoppedLatch.set(new CountDownLatch(1));
        super.doStart();
    }

    protected void connect() throws Exception {
        if (this.socket == null && this.socketFactory == null) {
            throw new IllegalStateException("Cannot connect if the socket or socketFactory have not been set");
        }
        InetSocketAddress localAddress = null;
        InetSocketAddress remoteAddress = null;
        if (this.localLocation != null) {
            localAddress = new InetSocketAddress(InetAddress.getByName(this.localLocation.getHost()), this.localLocation.getPort());
        }
        if (this.remoteLocation != null) {
            remoteAddress = new InetSocketAddress(resolveHostName(this.remoteLocation.getHost()), this.remoteLocation.getPort());
        }
        this.trafficClassSet = setTrafficClass(this.socket);
        if (this.socket != null) {
            if (localAddress != null) {
                this.socket.bind(localAddress);
            }
            if (remoteAddress != null) {
                if (this.connectionTimeout >= 0) {
                    this.socket.connect(remoteAddress, this.connectionTimeout);
                } else {
                    this.socket.connect(remoteAddress);
                }
            }
        } else if (localAddress != null) {
            this.socket = this.socketFactory.createSocket(remoteAddress.getAddress(), remoteAddress.getPort(), localAddress.getAddress(), localAddress.getPort());
        } else {
            this.socket = this.socketFactory.createSocket(remoteAddress.getAddress(), remoteAddress.getPort());
        }
        initialiseSocket(this.socket);
        initializeStreams();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stopping transport " + this);
        }
        if (this.socket == null) {
            return;
        }
        if (this.closeAsync) {
            CountDownLatch latch = new CountDownLatch(1);
            TaskRunnerFactory taskRunnerFactory = new TaskRunnerFactory();
            taskRunnerFactory.execute(new 1(latch));
            try {
                latch.await(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                taskRunnerFactory.shutdownNow();
            }
            return;
        }
        LOG.trace("Closing socket {}", this.socket);
        try {
            this.socket.close();
            LOG.debug("Closed socket {}", this.socket);
        } catch (IOException e2) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Caught exception closing socket " + this.socket + ". This exception will be ignored.", e2);
            }
        }
    }

    public void stop() throws Exception {
        super.stop();
        CountDownLatch countDownLatch = (CountDownLatch) this.stoppedLatch.get();
        if (countDownLatch != null && Thread.currentThread() != this.runnerThread) {
            countDownLatch.await(1, TimeUnit.SECONDS);
        }
    }

    protected void initializeStreams() throws Exception {
        this.dataIn = new DataInputStream(new 2(this.socket.getInputStream(), this.ioBufferSize));
        TcpBufferedOutputStream outputStream = new TcpBufferedOutputStream(this.socket.getOutputStream(), this.ioBufferSize);
        this.dataOut = new DataOutputStream(outputStream);
        this.buffOut = outputStream;
    }

    protected void closeStreams() throws IOException {
        if (this.dataOut != null) {
            this.dataOut.close();
        }
        if (this.dataIn != null) {
            this.dataIn.close();
        }
    }

    public void setSocketOptions(Map<String, Object> socketOptions) {
        this.socketOptions = new HashMap(socketOptions);
    }

    public String getRemoteAddress() {
        if (this.socket == null) {
            return null;
        }
        SocketAddress address = this.socket.getRemoteSocketAddress();
        if (address instanceof InetSocketAddress) {
            return "tcp://" + ((InetSocketAddress) address).getAddress().getHostAddress() + Headers.SEPERATOR + ((InetSocketAddress) address).getPort();
        }
        return Stomp.EMPTY + this.socket.getRemoteSocketAddress();
    }

    public <T> T narrow(Class<T> target) {
        if (target == Socket.class) {
            return target.cast(this.socket);
        }
        if (target == TimeStampStream.class) {
            return target.cast(this.buffOut);
        }
        return super.narrow(target);
    }

    public int getReceiveCounter() {
        return this.receiveCounter;
    }

    private boolean setTrafficClass(Socket sock) throws SocketException, IllegalArgumentException {
        if (sock == null) {
            return false;
        }
        if (!this.diffServChosen && !this.typeOfServiceChosen) {
            return false;
        }
        if (this.diffServChosen && this.typeOfServiceChosen) {
            throw new IllegalArgumentException("Cannot set both the  Differentiated Services and Type of Services transport  options on the same connection.");
        }
        sock.setTrafficClass(this.trafficClass);
        int resultTrafficClass = sock.getTrafficClass();
        if (this.trafficClass == resultTrafficClass) {
            this.diffServChosen = false;
            this.typeOfServiceChosen = false;
            return true;
        } else if ((this.trafficClass >> 2) != (resultTrafficClass >> 2) || (this.trafficClass & 3) == (resultTrafficClass & 3)) {
            LOG.warn("Attempted to set the Traffic Class to " + this.trafficClass + " but the result Traffic Class was " + resultTrafficClass + ". Please check that your system " + "supports java.net.setTrafficClass.");
            return false;
        } else {
            LOG.warn("Attempted to set the Traffic Class to " + this.trafficClass + " but the result Traffic Class was " + resultTrafficClass + ". Please check that your system " + "allows you to set the ECN bits (the first two bits).");
            return false;
        }
    }

    public WireFormat getWireFormat() {
        return this.wireFormat;
    }
}
