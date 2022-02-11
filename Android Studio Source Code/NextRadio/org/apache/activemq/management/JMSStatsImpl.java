package org.apache.activemq.management;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.util.IndentPrinter;

public class JMSStatsImpl extends StatsImpl {
    private List<ActiveMQConnection> connections;

    public JMSStatsImpl() {
        this.connections = new CopyOnWriteArrayList();
    }

    public JMSConnectionStatsImpl[] getConnections() {
        Object[] connectionArray = this.connections.toArray();
        int size = connectionArray.length;
        JMSConnectionStatsImpl[] answer = new JMSConnectionStatsImpl[size];
        for (int i = 0; i < size; i++) {
            answer[i] = connectionArray[i].getConnectionStats();
        }
        return answer;
    }

    public void addConnection(ActiveMQConnection connection) {
        this.connections.add(connection);
    }

    public void removeConnection(ActiveMQConnection connection) {
        this.connections.remove(connection);
    }

    public void dump(IndentPrinter out) {
        out.printIndent();
        out.println("factory {");
        out.incrementIndent();
        JMSConnectionStatsImpl[] array = getConnections();
        for (JMSConnectionStatsImpl connectionStat : array) {
            connectionStat.dump(out);
        }
        out.decrementIndent();
        out.printIndent();
        out.println("}");
        out.flush();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (JMSConnectionStatsImpl enabled2 : getConnections()) {
            enabled2.setEnabled(enabled);
        }
    }
}
