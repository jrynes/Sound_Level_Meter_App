package org.apache.activemq.broker.region.policy;

import java.util.List;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.filter.DestinationMap;
import org.apache.activemq.filter.DestinationMapEntry;

public class RedeliveryPolicyMap extends DestinationMap {
    private RedeliveryPolicy defaultEntry;

    public RedeliveryPolicy getEntryFor(ActiveMQDestination destination) {
        RedeliveryPolicy answer = (RedeliveryPolicy) chooseValue(destination);
        if (answer == null) {
            return getDefaultEntry();
        }
        return answer;
    }

    public void setRedeliveryPolicyEntries(List entries) {
        super.setEntries(entries);
    }

    public RedeliveryPolicy getDefaultEntry() {
        return this.defaultEntry;
    }

    public void setDefaultEntry(RedeliveryPolicy defaultEntry) {
        this.defaultEntry = defaultEntry;
    }

    protected Class<? extends DestinationMapEntry> getEntryClass() {
        return RedeliveryPolicy.class;
    }
}
