package org.apache.activemq.command;

import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import java.io.IOException;
import org.apache.activemq.state.CommandVisitor;
import org.xbill.DNS.WKSRecord.Service;

public class RemoveInfo extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 12;
    protected long lastDeliveredSequenceId;
    protected DataStructure objectId;

    public RemoveInfo(DataStructure objectId) {
        this.objectId = objectId;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public DataStructure getObjectId() {
        return this.objectId;
    }

    public void setObjectId(DataStructure objectId) {
        this.objectId = objectId;
    }

    public long getLastDeliveredSequenceId() {
        return this.lastDeliveredSequenceId;
    }

    public void setLastDeliveredSequenceId(long lastDeliveredSequenceId) {
        this.lastDeliveredSequenceId = lastDeliveredSequenceId;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        switch (this.objectId.getDataStructureType()) {
            case Nack.INDEX /*120*/:
                return visitor.processRemoveConnection((ConnectionId) this.objectId, this.lastDeliveredSequenceId);
            case Service.ERPC /*121*/:
                return visitor.processRemoveSession((SessionId) this.objectId, this.lastDeliveredSequenceId);
            case (byte) 122:
                return visitor.processRemoveConsumer((ConsumerId) this.objectId, this.lastDeliveredSequenceId);
            case Service.NTP /*123*/:
                return visitor.processRemoveProducer((ProducerId) this.objectId);
            default:
                throw new IOException("Unknown remove command type: " + this.objectId.getDataStructureType());
        }
    }

    public boolean isConnectionRemove() {
        return this.objectId.getDataStructureType() == 120;
    }

    public boolean isSessionRemove() {
        return this.objectId.getDataStructureType() == 121;
    }

    public boolean isConsumerRemove() {
        return this.objectId.getDataStructureType() == 122;
    }

    public boolean isProducerRemove() {
        return this.objectId.getDataStructureType() == 123;
    }
}
