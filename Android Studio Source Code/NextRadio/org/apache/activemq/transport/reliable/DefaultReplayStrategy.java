package org.apache.activemq.transport.reliable;

import java.io.IOException;

public class DefaultReplayStrategy implements ReplayStrategy {
    private int maximumDifference;

    public DefaultReplayStrategy() {
        this.maximumDifference = 5;
    }

    public DefaultReplayStrategy(int maximumDifference) {
        this.maximumDifference = 5;
        this.maximumDifference = maximumDifference;
    }

    public boolean onDroppedPackets(ReliableTransport transport, int expectedCounter, int actualCounter, int nextAvailableCounter) throws IOException {
        int difference = actualCounter - expectedCounter;
        if (((long) Math.abs(difference)) > ((long) this.maximumDifference)) {
            int upperLimit = actualCounter - 1;
            if (upperLimit < expectedCounter) {
                upperLimit = expectedCounter;
            }
            transport.requestReplay(expectedCounter, upperLimit);
        }
        return difference > 0;
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
