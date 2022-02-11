package org.apache.activemq.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;

public class StringToListOfActiveMQDestinationConverter {
    public static List<ActiveMQDestination> convertToActiveMQDestination(Object value) {
        List<ActiveMQDestination> list = null;
        if (value != null) {
            String text = value.toString();
            if (text.startsWith("[") && text.endsWith("]")) {
                String[] array = text.substring(1, text.length() - 1).split(Stomp.COMMA);
                list = new ArrayList();
                for (String item : array) {
                    list.add(ActiveMQDestination.createDestination(item.trim(), (byte) 1));
                }
            }
        }
        return list;
    }

    public static String convertFromActiveMQDestination(Object value) {
        if (value == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("[");
        if (value instanceof List) {
            List list = (List) value;
            for (int i = 0; i < list.size(); i++) {
                ActiveMQDestination e = list.get(i);
                if (e instanceof ActiveMQDestination) {
                    sb.append(e);
                    if (i < list.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
        }
        sb.append("]");
        if (sb.length() > 2) {
            return sb.toString();
        }
        return null;
    }
}
