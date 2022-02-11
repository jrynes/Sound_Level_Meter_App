package org.apache.activemq.filter;

import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.util.JMSExceptionSupport;

public abstract class DestinationFilter implements BooleanExpression {
    public static final String ANY_CHILD = "*";
    public static final String ANY_DESCENDENT = ">";

    public abstract boolean matches(ActiveMQDestination activeMQDestination);

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        return matches(message) ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        try {
            if (message.isDropped()) {
                return false;
            }
            return matches(message.getMessage().getDestination());
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public static DestinationFilter parseFilter(ActiveMQDestination destination) {
        if (destination.isComposite()) {
            return new CompositeDestinationFilter(destination);
        }
        String[] paths = DestinationPath.getDestinationPaths(destination);
        int idx = paths.length - 1;
        if (idx >= 0) {
            if (paths[idx].equals(ANY_DESCENDENT)) {
                return new PrefixDestinationFilter(paths, destination.getDestinationType());
            }
            int idx2;
            do {
                idx2 = idx;
                if (idx2 >= 0) {
                    idx = idx2 - 1;
                }
            } while (!paths[idx2].equals(ANY_CHILD));
            return new WildcardDestinationFilter(paths, destination.getDestinationType());
        }
        return new SimpleDestinationFilter(destination);
    }
}
