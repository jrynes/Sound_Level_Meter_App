package org.apache.activemq.transport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;
import org.apache.activemq.util.FactoryFinder;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.URISupport;
import org.apache.activemq.wireformat.WireFormat;
import org.apache.activemq.wireformat.WireFormatFactory;

public abstract class TransportFactory {
    private static final String THREAD_NAME_FILTER = "threadName";
    private static final ConcurrentHashMap<String, TransportFactory> TRANSPORT_FACTORYS;
    private static final FactoryFinder TRANSPORT_FACTORY_FINDER;
    private static final FactoryFinder WIREFORMAT_FACTORY_FINDER;
    private static final String WRITE_TIMEOUT_FILTER = "soWriteTimeout";

    public abstract TransportServer doBind(URI uri) throws IOException;

    static {
        TRANSPORT_FACTORY_FINDER = new FactoryFinder("META-INF/services/org/apache/activemq/transport/");
        WIREFORMAT_FACTORY_FINDER = new FactoryFinder("META-INF/services/org/apache/activemq/wireformat/");
        TRANSPORT_FACTORYS = new ConcurrentHashMap();
    }

    public Transport doConnect(URI location, Executor ex) throws Exception {
        return doConnect(location);
    }

    public Transport doCompositeConnect(URI location, Executor ex) throws Exception {
        return doCompositeConnect(location);
    }

    public static Transport connect(URI location) throws Exception {
        return findTransportFactory(location).doConnect(location);
    }

    public static Transport connect(URI location, Executor ex) throws Exception {
        return findTransportFactory(location).doConnect(location, ex);
    }

    public static Transport compositeConnect(URI location) throws Exception {
        return findTransportFactory(location).doCompositeConnect(location);
    }

    public static Transport compositeConnect(URI location, Executor ex) throws Exception {
        return findTransportFactory(location).doCompositeConnect(location, ex);
    }

    public static TransportServer bind(URI location) throws IOException {
        return findTransportFactory(location).doBind(location);
    }

    public Transport doConnect(URI location) throws Exception {
        try {
            Map<String, String> options = new HashMap(URISupport.parseParameters(location));
            WireFormat wf = createWireFormat(options);
            Transport rc = configure(createTransport(location, wf), wf, options);
            if (options.isEmpty()) {
                return rc;
            }
            throw new IllegalArgumentException("Invalid connect parameters: " + options);
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    public Transport doCompositeConnect(URI location) throws Exception {
        try {
            Map<String, String> options = new HashMap(URISupport.parseParameters(location));
            WireFormat wf = createWireFormat(options);
            Transport rc = compositeConfigure(createTransport(location, wf), wf, options);
            if (options.isEmpty()) {
                return rc;
            }
            throw new IllegalArgumentException("Invalid connect parameters: " + options);
        } catch (Exception e) {
            throw IOExceptionSupport.create(e);
        }
    }

    public static void registerTransportFactory(String scheme, TransportFactory tf) {
        TRANSPORT_FACTORYS.put(scheme, tf);
    }

    protected Transport createTransport(URI location, WireFormat wf) throws MalformedURLException, UnknownHostException, IOException {
        throw new IOException("createTransport() method not implemented!");
    }

    public static TransportFactory findTransportFactory(URI location) throws IOException {
        String scheme = location.getScheme();
        if (scheme == null) {
            throw new IOException("Transport not scheme specified: [" + location + "]");
        }
        TransportFactory tf = (TransportFactory) TRANSPORT_FACTORYS.get(scheme);
        if (tf != null) {
            return tf;
        }
        try {
            tf = (TransportFactory) TRANSPORT_FACTORY_FINDER.newInstance(scheme);
            TRANSPORT_FACTORYS.put(scheme, tf);
            return tf;
        } catch (Throwable e) {
            IOException create = IOExceptionSupport.create("Transport scheme NOT recognized: [" + scheme + "]", e);
        }
    }

    protected WireFormat createWireFormat(Map<String, String> options) throws IOException {
        return createWireFormatFactory(options).createWireFormat();
    }

    protected WireFormatFactory createWireFormatFactory(Map<String, String> options) throws IOException {
        String wireFormat = (String) options.remove("wireFormat");
        if (wireFormat == null) {
            wireFormat = getDefaultWireFormatType();
        }
        try {
            WireFormatFactory wff = (WireFormatFactory) WIREFORMAT_FACTORY_FINDER.newInstance(wireFormat);
            IntrospectionSupport.setProperties(wff, options, "wireFormat.");
            return wff;
        } catch (Throwable e) {
            IOException create = IOExceptionSupport.create("Could not create wire format factory for: " + wireFormat + ", reason: " + e, e);
        }
    }

    protected String getDefaultWireFormatType() {
        return MulticastDiscoveryAgent.DEFAULT_HOST_STR;
    }

    public Transport configure(Transport transport, WireFormat wf, Map options) throws Exception {
        return new ResponseCorrelator(new MutexTransport(compositeConfigure(transport, wf, options)));
    }

    public Transport serverConfigure(Transport transport, WireFormat format, HashMap options) throws Exception {
        if (options.containsKey(THREAD_NAME_FILTER)) {
            transport = new ThreadNameFilter(transport);
        }
        return new MutexTransport(compositeConfigure(transport, format, options));
    }

    public Transport compositeConfigure(Transport transport, WireFormat format, Map options) {
        if (options.containsKey(WRITE_TIMEOUT_FILTER)) {
            Transport transport2 = new WriteTimeoutFilter(transport);
            String soWriteTimeout = (String) options.remove(WRITE_TIMEOUT_FILTER);
            if (soWriteTimeout != null) {
                ((WriteTimeoutFilter) transport2).setWriteTimeout(Long.parseLong(soWriteTimeout));
            }
            transport = transport2;
        }
        IntrospectionSupport.setProperties(transport, options);
        return transport;
    }

    protected String getOption(Map options, String key, String def) {
        String rc = (String) options.remove(key);
        if (rc == null) {
            return def;
        }
        return rc;
    }
}
