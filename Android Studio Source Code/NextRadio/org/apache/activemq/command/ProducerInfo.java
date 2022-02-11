package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ProducerInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 6;
    protected BrokerId[] brokerPath;
    protected ActiveMQDestination destination;
    protected boolean dispatchAsync;
    protected ProducerId producerId;
    protected int windowSize;

    public ProducerInfo(ProducerId producerId) {
        this.producerId = producerId;
    }

    public ProducerInfo(SessionInfo sessionInfo, long producerId) {
        this.producerId = new ProducerId(sessionInfo.getSessionId(), producerId);
    }

    public ProducerInfo copy() {
        ProducerInfo info = new ProducerInfo();
        copy(info);
        return info;
    }

    public void copy(ProducerInfo info) {
        super.copy(info);
        info.producerId = this.producerId;
        info.destination = this.destination;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public ProducerId getProducerId() {
        return this.producerId;
    }

    public void setProducerId(ProducerId producerId) {
        this.producerId = producerId;
    }

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public RemoveInfo createRemoveCommand() {
        RemoveInfo command = new RemoveInfo(getProducerId());
        command.setResponseRequired(isResponseRequired());
        return command;
    }

    public BrokerId[] getBrokerPath() {
        return this.brokerPath;
    }

    public void setBrokerPath(BrokerId[] brokerPath) {
        this.brokerPath = brokerPath;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processAddProducer(this);
    }

    public boolean isDispatchAsync() {
        return this.dispatchAsync;
    }

    public void setDispatchAsync(boolean dispatchAsync) {
        this.dispatchAsync = dispatchAsync;
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }
}
