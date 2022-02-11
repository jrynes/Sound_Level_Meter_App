package org.apache.activemq.transport.reliable;

import java.io.IOException;

public class ExceptionIfDroppedReplayStrategy implements ReplayStrategy {
    private int maximumDifference;

    public ExceptionIfDroppedReplayStrategy() {
        this.maximumDifference = 5;
    }

    public ExceptionIfDroppedReplayStrategy(int maximumDifference) {
        this.maximumDifference = 5;
        this.maximumDifference = maximumDifference;
    }

    public boolean onDroppedPackets(ReliableTransport transport, int expectedCounter, int actualCounter, int nextAvailableCounter) throws IOException {
        int difference = actualCounter - expectedCounter;
        long count = (long) Math.abs(difference);
        if (count <= ((long) this.maximumDifference)) {
            return difference > 0;
        } else {
            throw new IOException("Packets dropped on: " + transport + " count: " + count + " expected: " + expectedCounter + " but was: " + actualCounter);
        }
    }

    public void onReceivedPacket(ReliableTransport transport, long expectedCounter) {
    }

    public int getMaximumDifference() {
        return this.maximumDifference;
    }

    public void setMaximumDifference(int maximumDifference) {
        this.maximumDifference = maximumDifference;
    }
}
