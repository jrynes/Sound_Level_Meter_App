package org.apache.activemq.command;

public interface Endpoint {
    BrokerId getBrokerId();

    BrokerInfo getBrokerInfo();

    String getName();

    void setBrokerInfo(BrokerInfo brokerInfo);
}
