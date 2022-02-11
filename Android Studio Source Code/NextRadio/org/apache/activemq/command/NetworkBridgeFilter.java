package org.apache.activemq.command;

import java.util.Arrays;
import javax.jms.JMSException;
import org.apache.activemq.filter.BooleanExpression;
import org.apache.activemq.filter.MessageEvaluationContext;
import org.apache.activemq.util.JMSExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkBridgeFilter implements DataStructure, BooleanExpression {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 91;
    static final Logger LOG;
    transient ConsumerInfo consumerInfo;
    protected BrokerId networkBrokerId;
    protected int networkTTL;

    static {
        LOG = LoggerFactory.getLogger(NetworkBridgeFilter.class);
    }

    public NetworkBridgeFilter(ConsumerInfo consumerInfo, BrokerId networkBrokerId, int networkTTL) {
        this.networkBrokerId = networkBrokerId;
        this.networkTTL = networkTTL;
        this.consumerInfo = consumerInfo;
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public boolean matches(MessageEvaluationContext mec) throws JMSException {
        try {
            Message message = mec.getMessage();
            return message != null && matchesForwardingFilter(message, mec);
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        return matches(message) ? Boolean.TRUE : Boolean.FALSE;
    }

    protected boolean matchesForwardingFilter(Message message, MessageEvaluationContext mec) {
        if (!contains(message.getBrokerPath(), this.networkBrokerId)) {
            if ((message.getBrokerPath() == null ? 0 : message.getBrokerPath().length) < this.networkTTL) {
                if (message.isAdvisory()) {
                    if (this.consumerInfo == null || !this.consumerInfo.isNetworkSubscription()) {
                        if (message.getDataStructure() != null && message.getDataStructure().getDataStructureType() == 5) {
                            ConsumerInfo info = (ConsumerInfo) message.getDataStructure();
                            if ((info.getBrokerPath() == null ? 0 : info.getBrokerPath().length) >= this.networkTTL) {
                                if (!LOG.isTraceEnabled()) {
                                    return false;
                                }
                                LOG.trace("ConsumerInfo advisory restricted to " + this.networkTTL + " network hops ignoring: " + message);
                                return false;
                            } else if (contains(info.getBrokerPath(), this.networkBrokerId)) {
                                LOG.trace("ConsumerInfo advisory all ready routed once through target broker (" + this.networkBrokerId + "), path: " + Arrays.toString(info.getBrokerPath()) + " - ignoring: " + message);
                                return false;
                            }
                        }
                    } else if (!LOG.isTraceEnabled()) {
                        return false;
                    } else {
                        LOG.trace("not propagating advisory to network sub: " + this.consumerInfo.getConsumerId() + ", message: " + message);
                        return false;
                    }
                }
                return true;
            } else if (!LOG.isTraceEnabled()) {
                return false;
            } else {
                LOG.trace("Message restricted to " + this.networkTTL + " network hops ignoring: " + message);
                return false;
            }
        } else if (!LOG.isTraceEnabled()) {
            return false;
        } else {
            LOG.trace("Message all ready routed once through target broker (" + this.networkBrokerId + "), path: " + Arrays.toString(message.getBrokerPath()) + " - ignoring: " + message);
            return false;
        }
    }

    public static boolean contains(BrokerId[] brokerPath, BrokerId brokerId) {
        if (!(brokerPath == null || brokerId == null)) {
            for (Object equals : brokerPath) {
                if (brokerId.equals(equals)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getNetworkTTL() {
        return this.networkTTL;
    }

    public void setNetworkTTL(int networkTTL) {
        this.networkTTL = networkTTL;
    }

    public BrokerId getNetworkBrokerId() {
        return this.networkBrokerId;
    }

    public void setNetworkBrokerId(BrokerId remoteBrokerPath) {
        this.networkBrokerId = remoteBrokerPath;
    }
}
