package org.apache.activemq.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.activemq.transport.stomp.Stomp;

public class StringArrayConverter {
    public static String[] convertToStringArray(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        if (text == null || text.length() == 0) {
            return null;
        }
        StringTokenizer stok = new StringTokenizer(text, Stomp.COMMA);
        List<String> list = new ArrayList();
        while (stok.hasMoreTokens()) {
            list.add(stok.nextToken());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String convertToString(String[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        StringBuffer result = new StringBuffer(String.valueOf(value[0]));
        for (int i = 1; i < value.length; i++) {
            result.append(Stomp.COMMA).append(value[i]);
        }
        return result.toString();
    }
}
