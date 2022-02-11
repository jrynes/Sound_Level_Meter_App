package org.apache.activemq.transport.tcp;

import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.ActiveMQDestination;

public class QualityOfServiceUtils {
    private static final Map<String, Integer> DIFF_SERV_NAMES;
    private static final int MAX_DIFF_SERV = 63;
    private static final int MAX_TOS = 255;
    private static final int MIN_DIFF_SERV = 0;
    private static final int MIN_TOS = 0;

    static {
        DIFF_SERV_NAMES = new HashMap();
        DIFF_SERV_NAMES.put("CS0", Integer.valueOf(0));
        DIFF_SERV_NAMES.put("CS1", Integer.valueOf(8));
        DIFF_SERV_NAMES.put("CS2", Integer.valueOf(16));
        DIFF_SERV_NAMES.put("CS3", Integer.valueOf(24));
        DIFF_SERV_NAMES.put("CS4", Integer.valueOf(32));
        DIFF_SERV_NAMES.put("CS5", Integer.valueOf(40));
        DIFF_SERV_NAMES.put("CS6", Integer.valueOf(48));
        DIFF_SERV_NAMES.put("CS7", Integer.valueOf(56));
        DIFF_SERV_NAMES.put("AF11", Integer.valueOf(10));
        DIFF_SERV_NAMES.put("AF12", Integer.valueOf(12));
        DIFF_SERV_NAMES.put("AF13", Integer.valueOf(14));
        DIFF_SERV_NAMES.put("AF21", Integer.valueOf(18));
        DIFF_SERV_NAMES.put("AF22", Integer.valueOf(20));
        DIFF_SERV_NAMES.put("AF23", Integer.valueOf(22));
        DIFF_SERV_NAMES.put("AF31", Integer.valueOf(26));
        DIFF_SERV_NAMES.put("AF32", Integer.valueOf(28));
        DIFF_SERV_NAMES.put("AF33", Integer.valueOf(30));
        DIFF_SERV_NAMES.put("AF41", Integer.valueOf(34));
        DIFF_SERV_NAMES.put("AF42", Integer.valueOf(36));
        DIFF_SERV_NAMES.put("AF43", Integer.valueOf(38));
        DIFF_SERV_NAMES.put("EF", Integer.valueOf(46));
    }

    public static int getDSCP(String value) throws IllegalArgumentException {
        int intValue;
        if (DIFF_SERV_NAMES.containsKey(value)) {
            intValue = ((Integer) DIFF_SERV_NAMES.get(value)).intValue();
        } else {
            try {
                intValue = Integer.parseInt(value);
                if (intValue > MAX_DIFF_SERV || intValue < 0) {
                    throw new IllegalArgumentException("Differentiated Services value: " + intValue + " not in legal range [" + 0 + ", " + MAX_DIFF_SERV + "].");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("No such Differentiated Services name: " + value);
            }
        }
        return adjustDSCPForECN(intValue);
    }

    public static int getToS(int value) throws IllegalArgumentException {
        if (value <= MAX_TOS && value >= 0) {
            return value;
        }
        throw new IllegalArgumentException("Type of Service value: " + value + " not in legal range [" + 0 + ", " + MAX_TOS + ActiveMQDestination.PATH_SEPERATOR);
    }

    private static int adjustDSCPForECN(int dscp) throws IllegalArgumentException {
        try {
            return (dscp << 2) | (new Socket().getTrafficClass() & 3);
        } catch (SocketException e) {
            throw new IllegalArgumentException("Setting Differentiated Services not supported: " + e);
        }
    }
}
