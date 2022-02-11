package org.apache.activemq.management;

import javax.jms.Destination;
import org.apache.activemq.util.IndentPrinter;

public class JMSConsumerStatsImpl extends JMSEndpointStatsImpl {
    private String origin;

    public JMSConsumerStatsImpl(JMSSessionStatsImpl sessionStats, Destination destination) {
        super(sessionStats);
        if (destination != null) {
            this.origin = destination.toString();
        }
    }

    public JMSConsumerStatsImpl(CountStatisticImpl messageCount, CountStatisticImpl pendingMessageCount, CountStatisticImpl expiredMessageCount, TimeStatisticImpl messageWaitTime, TimeStatisticImpl messageRateTime, String origin) {
        super(messageCount, pendingMessageCount, expiredMessageCount, messageWaitTime, messageRateTime);
        this.origin = origin;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("consumer ");
        buffer.append(this.origin);
        buffer.append(" { ");
        buffer.append(super.toString());
        buffer.append(" }");
        return buffer.toString();
    }

    public void dump(IndentPrinter out) {
        out.printIndent();
        out.print("consumer ");
        out.print(this.origin);
        out.println(" {");
        out.incrementIndent();
        super.dump(out);
        out.decrementIndent();
        out.printIndent();
        out.println("}");
    }
}
