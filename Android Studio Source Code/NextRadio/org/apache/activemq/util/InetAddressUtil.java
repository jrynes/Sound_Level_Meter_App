package org.apache.activemq.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressUtil {
    public static String getLocalHostName() throws UnknownHostException {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage();
            if (host != null) {
                int colon = host.indexOf(58);
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            throw uhe;
        }
    }
}
