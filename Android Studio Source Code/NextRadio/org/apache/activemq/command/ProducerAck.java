package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ProducerAck extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 19;
    protected ProducerId producerId;
    protected int size;

    public ProducerAck(ProducerId producerId, int size) {
        this.producerId = producerId;
        this.size = size;
    }

    public void copy(ProducerAck copy) {
        super.copy(copy);
        copy.producerId = this.producerId;
        copy.size = this.size;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processProducerAck(this);
    }

    public ProducerId getProducerId() {
        return this.producerId;
    }

    public void setProducerId(ProducerId producerId) {
        this.producerId = producerId;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
