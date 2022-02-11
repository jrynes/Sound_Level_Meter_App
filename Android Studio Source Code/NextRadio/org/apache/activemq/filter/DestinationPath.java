package org.apache.activemq.filter;

import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.Message;

public final class DestinationPath {
    protected static final char SEPARATOR = '.';

    private DestinationPath() {
    }

    public static String[] getDestinationPaths(String subject) {
        List<String> list = new ArrayList();
        int previous = 0;
        int lastIndex = subject.length() - 1;
        while (true) {
            int idx = subject.indexOf(46, previous);
            if (idx < 0) {
                list.add(subject.substring(previous, lastIndex + 1));
                String[] answer = new String[list.size()];
                list.toArray(answer);
                return answer;
            }
            list.add(subject.substring(previous, idx));
            previous = idx + 1;
        }
    }

    public static String[] getDestinationPaths(Message message) throws JMSException {
        return getDestinationPaths(message.getDestination());
    }

    public static String[] getDestinationPaths(ActiveMQDestination destination) {
        return getDestinationPaths(destination.getPhysicalName());
    }

    public static String toString(String[] paths) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            if (i > 0) {
                buffer.append(SEPARATOR);
            }
            String path = paths[i];
            if (path == null) {
                buffer.append(DestinationFilter.ANY_CHILD);
            } else {
                buffer.append(path);
            }
        }
        return buffer.toString();
    }
}
