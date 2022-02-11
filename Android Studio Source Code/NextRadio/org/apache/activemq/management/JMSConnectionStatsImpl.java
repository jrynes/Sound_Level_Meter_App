package org.apache.activemq.management;

import java.util.List;
import org.apache.activemq.util.IndentPrinter;

public class JMSConnectionStatsImpl extends StatsImpl {
    private List sessions;
    private boolean transactional;

    public JMSConnectionStatsImpl(List sessions, boolean transactional) {
        this.sessions = sessions;
        this.transactional = transactional;
    }

    public JMSSessionStatsImpl[] getSessions() {
        Object[] sessionArray = this.sessions.toArray();
        int size = sessionArray.length;
        JMSSessionStatsImpl[] answer = new JMSSessionStatsImpl[size];
        for (int i = 0; i < size; i++) {
            answer[i] = sessionArray[i].getSessionStats();
        }
        return answer;
    }

    public void reset() {
        super.reset();
        for (JMSSessionStatsImpl reset : getSessions()) {
            reset.reset();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (JMSSessionStatsImpl enabled2 : getSessions()) {
            enabled2.setEnabled(enabled);
        }
    }

    public boolean isTransactional() {
        return this.transactional;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("connection{ ");
        JMSSessionStatsImpl[] array = getSessions();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(Integer.toString(i));
            buffer.append(" = ");
            buffer.append(array[i]);
        }
        buffer.append(" }");
        return buffer.toString();
    }

    public void dump(IndentPrinter out) {
        out.printIndent();
        out.println("connection {");
        out.incrementIndent();
        JMSSessionStatsImpl[] array = getSessions();
        for (JMSSessionStatsImpl sessionStat : array) {
            out.printIndent();
            out.println("session {");
            out.incrementIndent();
            sessionStat.dump(out);
            out.decrementIndent();
            out.printIndent();
            out.println("}");
        }
        out.decrementIndent();
        out.printIndent();
        out.println("}");
        out.flush();
    }
}
