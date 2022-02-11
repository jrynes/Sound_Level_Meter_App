package org.apache.activemq.command;

public class DiscoveryEvent implements DataStructure {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 40;
    protected String brokerName;
    protected String serviceName;

    public DiscoveryEvent(String serviceName) {
        this.serviceName = serviceName;
    }

    protected DiscoveryEvent(DiscoveryEvent copy) {
        this.serviceName = copy.serviceName;
        this.brokerName = copy.brokerName;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBrokerName() {
        return this.brokerName;
    }

    public void setBrokerName(String name) {
        this.brokerName = name;
    }

    public boolean isMarshallAware() {
        return false;
    }
}
