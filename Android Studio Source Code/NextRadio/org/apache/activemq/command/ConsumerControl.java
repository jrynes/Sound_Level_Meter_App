package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ConsumerControl extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 17;
    protected boolean close;
    protected ConsumerId consumerId;
    protected ActiveMQDestination destination;
    protected boolean flush;
    protected int prefetch;
    protected boolean start;
    protected boolean stop;

    public ActiveMQDestination getDestination() {
        return this.destination;
    }

    public void setDestination(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processConsumerControl(this);
    }

    public boolean isClose() {
        return this.close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public ConsumerId getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(ConsumerId consumerId) {
        this.consumerId = consumerId;
    }

    public int getPrefetch() {
        return this.prefetch;
    }

    public void setPrefetch(int prefetch) {
        this.prefetch = prefetch;
    }

    public boolean isFlush() {
        return this.flush;
    }

    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    public boolean isStart() {
        return this.start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isStop() {
        return this.stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
