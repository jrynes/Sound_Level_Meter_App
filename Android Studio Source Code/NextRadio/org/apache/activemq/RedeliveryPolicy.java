package org.apache.activemq;

import java.io.Serializable;
import java.util.Random;
import org.apache.activemq.filter.DestinationMapEntry;
import org.apache.activemq.util.IntrospectionSupport;

public class RedeliveryPolicy extends DestinationMapEntry implements Cloneable, Serializable {
    public static final int NO_MAXIMUM_REDELIVERIES = -1;
    private static Random randomNumberGenerator;
    protected double backOffMultiplier;
    protected double collisionAvoidanceFactor;
    protected long initialRedeliveryDelay;
    protected int maximumRedeliveries;
    protected long maximumRedeliveryDelay;
    protected long redeliveryDelay;
    protected boolean useCollisionAvoidance;
    protected boolean useExponentialBackOff;

    public RedeliveryPolicy() {
        this.collisionAvoidanceFactor = 0.15d;
        this.maximumRedeliveries = 6;
        this.maximumRedeliveryDelay = -1;
        this.initialRedeliveryDelay = 1000;
        this.backOffMultiplier = 5.0d;
        this.redeliveryDelay = this.initialRedeliveryDelay;
    }

    public RedeliveryPolicy copy() {
        try {
            return (RedeliveryPolicy) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not clone: " + e, e);
        }
    }

    public double getBackOffMultiplier() {
        return this.backOffMultiplier;
    }

    public void setBackOffMultiplier(double backOffMultiplier) {
        this.backOffMultiplier = backOffMultiplier;
    }

    public short getCollisionAvoidancePercent() {
        return (short) ((int) Math.round(this.collisionAvoidanceFactor * 100.0d));
    }

    public void setCollisionAvoidancePercent(short collisionAvoidancePercent) {
        this.collisionAvoidanceFactor = ((double) collisionAvoidancePercent) * 0.01d;
    }

    public long getInitialRedeliveryDelay() {
        return this.initialRedeliveryDelay;
    }

    public void setInitialRedeliveryDelay(long initialRedeliveryDelay) {
        this.initialRedeliveryDelay = initialRedeliveryDelay;
    }

    public long getMaximumRedeliveryDelay() {
        return this.maximumRedeliveryDelay;
    }

    public void setMaximumRedeliveryDelay(long maximumRedeliveryDelay) {
        this.maximumRedeliveryDelay = maximumRedeliveryDelay;
    }

    public int getMaximumRedeliveries() {
        return this.maximumRedeliveries;
    }

    public void setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }

    public long getNextRedeliveryDelay(long previousDelay) {
        long nextDelay;
        if (previousDelay == 0) {
            nextDelay = this.redeliveryDelay;
        } else if (!this.useExponentialBackOff || this.backOffMultiplier <= 1.0d) {
            nextDelay = previousDelay;
        } else {
            nextDelay = (long) (((double) previousDelay) * this.backOffMultiplier);
            if (this.maximumRedeliveryDelay != -1 && nextDelay > this.maximumRedeliveryDelay) {
                nextDelay = Math.max(this.maximumRedeliveryDelay, this.redeliveryDelay);
            }
        }
        if (!this.useCollisionAvoidance) {
            return nextDelay;
        }
        Random random = getRandomNumberGenerator();
        return (long) (((double) nextDelay) + (((double) nextDelay) * ((random.nextBoolean() ? this.collisionAvoidanceFactor : -this.collisionAvoidanceFactor) * random.nextDouble())));
    }

    public boolean isUseCollisionAvoidance() {
        return this.useCollisionAvoidance;
    }

    public void setUseCollisionAvoidance(boolean useCollisionAvoidance) {
        this.useCollisionAvoidance = useCollisionAvoidance;
    }

    public boolean isUseExponentialBackOff() {
        return this.useExponentialBackOff;
    }

    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }

    protected static synchronized Random getRandomNumberGenerator() {
        Random random;
        synchronized (RedeliveryPolicy.class) {
            if (randomNumberGenerator == null) {
                randomNumberGenerator = new Random();
            }
            random = randomNumberGenerator;
        }
        return random;
    }

    public void setRedeliveryDelay(long redeliveryDelay) {
        this.redeliveryDelay = redeliveryDelay;
    }

    public long getRedeliveryDelay() {
        return this.redeliveryDelay;
    }

    public String toString() {
        return IntrospectionSupport.toString(this, DestinationMapEntry.class, null);
    }
}
