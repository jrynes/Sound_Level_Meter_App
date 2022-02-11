package org.apache.activemq.command;

import org.apache.activemq.transport.stomp.Stomp;

public class BaseEndpoint implements Endpoint {
    private BrokerInfo brokerInfo;
    private String name;

    public BaseEndpoint(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        String brokerText = Stomp.EMPTY;
        BrokerId brokerId = getBrokerId();
        if (brokerId != null) {
            brokerText = " broker: " + brokerId;
        }
        return "Endpoint[name:" + this.name + brokerText + "]";
    }

    public BrokerId getBrokerId() {
        if (this.brokerInfo != null) {
            return this.brokerInfo.getBrokerId();
        }
        return null;
    }

    public BrokerInfo getBrokerInfo() {
        return this.brokerInfo;
    }

    public void setBrokerInfo(BrokerInfo brokerInfo) {
        this.brokerInfo = brokerInfo;
    }
}
