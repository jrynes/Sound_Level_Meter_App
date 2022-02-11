package org.apache.activemq;

import java.io.IOException;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;

public class TransportLoggerSupport {
    public static String defaultLogWriterName;
    public static final SPI spi;

    public interface SPI {
        Transport createTransportLogger(Transport transport) throws IOException;

        Transport createTransportLogger(Transport transport, String str, boolean z, boolean z2, int i) throws IOException;
    }

    static {
        SPI temp;
        defaultLogWriterName = MulticastDiscoveryAgent.DEFAULT_HOST_STR;
        try {
            temp = (SPI) TransportLoggerSupport.class.getClassLoader().loadClass("org.apache.activemq.transport.TransportLoggerFactorySPI").newInstance();
        } catch (Throwable th) {
            temp = null;
        }
        spi = temp;
    }

    public static Transport createTransportLogger(Transport transport) throws IOException {
        if (spi != null) {
            return spi.createTransportLogger(transport);
        }
        return transport;
    }

    public static Transport createTransportLogger(Transport transport, String logWriterName, boolean dynamicManagement, boolean startLogging, int jmxPort) throws IOException {
        if (spi != null) {
            return spi.createTransportLogger(transport, logWriterName, dynamicManagement, startLogging, jmxPort);
        }
        return transport;
    }
}
