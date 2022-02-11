package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public class ReplayCommand extends BaseCommand {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 65;
    private int firstAckNumber;
    private int firstNakNumber;
    private int lastAckNumber;
    private int lastNakNumber;
    private String producerId;

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public String getProducerId() {
        return this.producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public int getFirstAckNumber() {
        return this.firstAckNumber;
    }

    public void setFirstAckNumber(int firstSequenceNumber) {
        this.firstAckNumber = firstSequenceNumber;
    }

    public int getLastAckNumber() {
        return this.lastAckNumber;
    }

    public void setLastAckNumber(int lastSequenceNumber) {
        this.lastAckNumber = lastSequenceNumber;
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return null;
    }

    public int getFirstNakNumber() {
        return this.firstNakNumber;
    }

    public void setFirstNakNumber(int firstNakNumber) {
        this.firstNakNumber = firstNakNumber;
    }

    public int getLastNakNumber() {
        return this.lastNakNumber;
    }

    public void setLastNakNumber(int lastNakNumber) {
        this.lastNakNumber = lastNakNumber;
    }

    public String toString() {
        return "ReplayCommand {commandId = " + getCommandId() + ", firstNakNumber = " + getFirstNakNumber() + ", lastNakNumber = " + getLastNakNumber() + "}";
    }
}
