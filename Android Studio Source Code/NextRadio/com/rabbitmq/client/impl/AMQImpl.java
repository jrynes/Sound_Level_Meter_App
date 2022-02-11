package com.rabbitmq.client.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.LongString;
import com.rabbitmq.client.UnexpectedMethodError;
import com.rabbitmq.client.UnknownClassOrMethodId;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class AMQImpl implements AMQP {

    public static class Access {
        public static final int INDEX = 30;

        public static class Request extends Method implements com.rabbitmq.client.AMQP.Access.Request {
            public static final int INDEX = 10;
            private final boolean active;
            private final boolean exclusive;
            private final boolean passive;
            private final boolean read;
            private final String realm;
            private final boolean write;

            public String getRealm() {
                return this.realm;
            }

            public boolean getExclusive() {
                return this.exclusive;
            }

            public boolean getPassive() {
                return this.passive;
            }

            public boolean getActive() {
                return this.active;
            }

            public boolean getWrite() {
                return this.write;
            }

            public boolean getRead() {
                return this.read;
            }

            public Request(String realm, boolean exclusive, boolean passive, boolean active, boolean write, boolean read) {
                if (realm == null) {
                    throw new IllegalStateException("Invalid configuration: 'realm' must be non-null.");
                }
                this.realm = realm;
                this.exclusive = exclusive;
                this.passive = passive;
                this.active = active;
                this.write = write;
                this.read = read;
            }

            public Request(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit());
            }

            public int protocolClassId() {
                return Access.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "access.request";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(realm=").append(this.realm).append(", exclusive=").append(this.exclusive).append(", passive=").append(this.passive).append(", active=").append(this.active).append(", write=").append(this.write).append(", read=").append(this.read).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.realm);
                writer.writeBit(this.exclusive);
                writer.writeBit(this.passive);
                writer.writeBit(this.active);
                writer.writeBit(this.write);
                writer.writeBit(this.read);
            }
        }

        public static class RequestOk extends Method implements com.rabbitmq.client.AMQP.Access.RequestOk {
            public static final int INDEX = 11;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public RequestOk(int ticket) {
                this.ticket = ticket;
            }

            public RequestOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort());
            }

            public int protocolClassId() {
                return Access.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "access.request-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
            }
        }
    }

    public static class Basic {
        public static final int INDEX = 60;

        public static class Ack extends Method implements com.rabbitmq.client.AMQP.Basic.Ack {
            public static final int INDEX = 80;
            private final long deliveryTag;
            private final boolean multiple;

            public long getDeliveryTag() {
                return this.deliveryTag;
            }

            public boolean getMultiple() {
                return this.multiple;
            }

            public Ack(long deliveryTag, boolean multiple) {
                this.deliveryTag = deliveryTag;
                this.multiple = multiple;
            }

            public Ack(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLonglong(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.ack";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(delivery-tag=").append(this.deliveryTag).append(", multiple=").append(this.multiple).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLonglong(this.deliveryTag);
                writer.writeBit(this.multiple);
            }
        }

        public static class Cancel extends Method implements com.rabbitmq.client.AMQP.Basic.Cancel {
            public static final int INDEX = 30;
            private final String consumerTag;
            private final boolean nowait;

            public String getConsumerTag() {
                return this.consumerTag;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Cancel(String consumerTag, boolean nowait) {
                if (consumerTag == null) {
                    throw new IllegalStateException("Invalid configuration: 'consumerTag' must be non-null.");
                }
                this.consumerTag = consumerTag;
                this.nowait = nowait;
            }

            public Cancel(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.cancel";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(consumer-tag=").append(this.consumerTag).append(", nowait=").append(this.nowait).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.consumerTag);
                writer.writeBit(this.nowait);
            }
        }

        public static class CancelOk extends Method implements com.rabbitmq.client.AMQP.Basic.CancelOk {
            public static final int INDEX = 31;
            private final String consumerTag;

            public String getConsumerTag() {
                return this.consumerTag;
            }

            public CancelOk(String consumerTag) {
                if (consumerTag == null) {
                    throw new IllegalStateException("Invalid configuration: 'consumerTag' must be non-null.");
                }
                this.consumerTag = consumerTag;
            }

            public CancelOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.cancel-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(consumer-tag=").append(this.consumerTag).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.consumerTag);
            }
        }

        public static class Consume extends Method implements com.rabbitmq.client.AMQP.Basic.Consume {
            public static final int INDEX = 20;
            private final Map<String, Object> arguments;
            private final String consumerTag;
            private final boolean exclusive;
            private final boolean noAck;
            private final boolean noLocal;
            private final boolean nowait;
            private final String queue;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public String getConsumerTag() {
                return this.consumerTag;
            }

            public boolean getNoLocal() {
                return this.noLocal;
            }

            public boolean getNoAck() {
                return this.noAck;
            }

            public boolean getExclusive() {
                return this.exclusive;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Consume(int ticket, String queue, String consumerTag, boolean noLocal, boolean noAck, boolean exclusive, boolean nowait, Map<String, Object> arguments) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                } else if (consumerTag == null) {
                    throw new IllegalStateException("Invalid configuration: 'consumerTag' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.queue = queue;
                    this.consumerTag = consumerTag;
                    this.noLocal = noLocal;
                    this.noAck = noAck;
                    this.exclusive = exclusive;
                    this.nowait = nowait;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Consume(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.consume";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", consumer-tag=").append(this.consumerTag).append(", no-local=").append(this.noLocal).append(", no-ack=").append(this.noAck).append(", exclusive=").append(this.exclusive).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeShortstr(this.consumerTag);
                writer.writeBit(this.noLocal);
                writer.writeBit(this.noAck);
                writer.writeBit(this.exclusive);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class ConsumeOk extends Method implements com.rabbitmq.client.AMQP.Basic.ConsumeOk {
            public static final int INDEX = 21;
            private final String consumerTag;

            public String getConsumerTag() {
                return this.consumerTag;
            }

            public ConsumeOk(String consumerTag) {
                if (consumerTag == null) {
                    throw new IllegalStateException("Invalid configuration: 'consumerTag' must be non-null.");
                }
                this.consumerTag = consumerTag;
            }

            public ConsumeOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.consume-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(consumer-tag=").append(this.consumerTag).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.consumerTag);
            }
        }

        public static class Deliver extends Method implements com.rabbitmq.client.AMQP.Basic.Deliver {
            public static final int INDEX = 60;
            private final String consumerTag;
            private final long deliveryTag;
            private final String exchange;
            private final boolean redelivered;
            private final String routingKey;

            public String getConsumerTag() {
                return this.consumerTag;
            }

            public long getDeliveryTag() {
                return this.deliveryTag;
            }

            public boolean getRedelivered() {
                return this.redelivered;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public Deliver(String consumerTag, long deliveryTag, boolean redelivered, String exchange, String routingKey) {
                if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (consumerTag == null) {
                    throw new IllegalStateException("Invalid configuration: 'consumerTag' must be non-null.");
                } else {
                    this.consumerTag = consumerTag;
                    this.deliveryTag = deliveryTag;
                    this.redelivered = redelivered;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                }
            }

            public Deliver(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr(), rdr.readLonglong(), rdr.readBit(), rdr.readShortstr(), rdr.readShortstr());
            }

            public int protocolClassId() {
                return INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.deliver";
            }

            public boolean hasContent() {
                return true;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(consumer-tag=").append(this.consumerTag).append(", delivery-tag=").append(this.deliveryTag).append(", redelivered=").append(this.redelivered).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.consumerTag);
                writer.writeLonglong(this.deliveryTag);
                writer.writeBit(this.redelivered);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
            }
        }

        public static class Get extends Method implements com.rabbitmq.client.AMQP.Basic.Get {
            public static final int INDEX = 70;
            private final boolean noAck;
            private final String queue;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public boolean getNoAck() {
                return this.noAck;
            }

            public Get(int ticket, String queue, boolean noAck) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                }
                this.ticket = ticket;
                this.queue = queue;
                this.noAck = noAck;
            }

            public Get(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.get";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", no-ack=").append(this.noAck).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeBit(this.noAck);
            }
        }

        public static class GetEmpty extends Method implements com.rabbitmq.client.AMQP.Basic.GetEmpty {
            public static final int INDEX = 72;
            private final String clusterId;

            public String getClusterId() {
                return this.clusterId;
            }

            public GetEmpty(String clusterId) {
                if (clusterId == null) {
                    throw new IllegalStateException("Invalid configuration: 'clusterId' must be non-null.");
                }
                this.clusterId = clusterId;
            }

            public GetEmpty(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.get-empty";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(cluster-id=").append(this.clusterId).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.clusterId);
            }
        }

        public static class GetOk extends Method implements com.rabbitmq.client.AMQP.Basic.GetOk {
            public static final int INDEX = 71;
            private final long deliveryTag;
            private final String exchange;
            private final int messageCount;
            private final boolean redelivered;
            private final String routingKey;

            public long getDeliveryTag() {
                return this.deliveryTag;
            }

            public boolean getRedelivered() {
                return this.redelivered;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public int getMessageCount() {
                return this.messageCount;
            }

            public GetOk(long deliveryTag, boolean redelivered, String exchange, String routingKey, int messageCount) {
                if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.deliveryTag = deliveryTag;
                    this.redelivered = redelivered;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                    this.messageCount = messageCount;
                }
            }

            public GetOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLonglong(), rdr.readBit(), rdr.readShortstr(), rdr.readShortstr(), rdr.readLong());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.get-ok";
            }

            public boolean hasContent() {
                return true;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(delivery-tag=").append(this.deliveryTag).append(", redelivered=").append(this.redelivered).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(", message-count=").append(this.messageCount).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLonglong(this.deliveryTag);
                writer.writeBit(this.redelivered);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
                writer.writeLong(this.messageCount);
            }
        }

        public static class Nack extends Method implements com.rabbitmq.client.AMQP.Basic.Nack {
            public static final int INDEX = 120;
            private final long deliveryTag;
            private final boolean multiple;
            private final boolean requeue;

            public long getDeliveryTag() {
                return this.deliveryTag;
            }

            public boolean getMultiple() {
                return this.multiple;
            }

            public boolean getRequeue() {
                return this.requeue;
            }

            public Nack(long deliveryTag, boolean multiple, boolean requeue) {
                this.deliveryTag = deliveryTag;
                this.multiple = multiple;
                this.requeue = requeue;
            }

            public Nack(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLonglong(), rdr.readBit(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.nack";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(delivery-tag=").append(this.deliveryTag).append(", multiple=").append(this.multiple).append(", requeue=").append(this.requeue).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLonglong(this.deliveryTag);
                writer.writeBit(this.multiple);
                writer.writeBit(this.requeue);
            }
        }

        public static class Publish extends Method implements com.rabbitmq.client.AMQP.Basic.Publish {
            public static final int INDEX = 40;
            private final String exchange;
            private final boolean immediate;
            private final boolean mandatory;
            private final String routingKey;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public boolean getMandatory() {
                return this.mandatory;
            }

            public boolean getImmediate() {
                return this.immediate;
            }

            public Publish(int ticket, String exchange, String routingKey, boolean mandatory, boolean immediate) {
                if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                    this.mandatory = mandatory;
                    this.immediate = immediate;
                }
            }

            public Publish(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.publish";
            }

            public boolean hasContent() {
                return true;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(", mandatory=").append(this.mandatory).append(", immediate=").append(this.immediate).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
                writer.writeBit(this.mandatory);
                writer.writeBit(this.immediate);
            }
        }

        public static class Qos extends Method implements com.rabbitmq.client.AMQP.Basic.Qos {
            public static final int INDEX = 10;
            private final boolean global;
            private final int prefetchCount;
            private final int prefetchSize;

            public int getPrefetchSize() {
                return this.prefetchSize;
            }

            public int getPrefetchCount() {
                return this.prefetchCount;
            }

            public boolean getGlobal() {
                return this.global;
            }

            public Qos(int prefetchSize, int prefetchCount, boolean global) {
                this.prefetchSize = prefetchSize;
                this.prefetchCount = prefetchCount;
                this.global = global;
            }

            public Qos(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLong(), rdr.readShort(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.qos";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(prefetch-size=").append(this.prefetchSize).append(", prefetch-count=").append(this.prefetchCount).append(", global=").append(this.global).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLong(this.prefetchSize);
                writer.writeShort(this.prefetchCount);
                writer.writeBit(this.global);
            }
        }

        public static class QosOk extends Method implements com.rabbitmq.client.AMQP.Basic.QosOk {
            public static final int INDEX = 11;

            public QosOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.qos-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Recover extends Method implements com.rabbitmq.client.AMQP.Basic.Recover {
            public static final int INDEX = 110;
            private final boolean requeue;

            public boolean getRequeue() {
                return this.requeue;
            }

            public Recover(boolean requeue) {
                this.requeue = requeue;
            }

            public Recover(MethodArgumentReader rdr) throws IOException {
                this(rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.recover";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(requeue=").append(this.requeue).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeBit(this.requeue);
            }
        }

        public static class RecoverAsync extends Method implements com.rabbitmq.client.AMQP.Basic.RecoverAsync {
            public static final int INDEX = 100;
            private final boolean requeue;

            public boolean getRequeue() {
                return this.requeue;
            }

            public RecoverAsync(boolean requeue) {
                this.requeue = requeue;
            }

            public RecoverAsync(MethodArgumentReader rdr) throws IOException {
                this(rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.recover-async";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(requeue=").append(this.requeue).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeBit(this.requeue);
            }
        }

        public static class RecoverOk extends Method implements com.rabbitmq.client.AMQP.Basic.RecoverOk {
            public static final int INDEX = 111;

            public RecoverOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.recover-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Reject extends Method implements com.rabbitmq.client.AMQP.Basic.Reject {
            public static final int INDEX = 90;
            private final long deliveryTag;
            private final boolean requeue;

            public long getDeliveryTag() {
                return this.deliveryTag;
            }

            public boolean getRequeue() {
                return this.requeue;
            }

            public Reject(long deliveryTag, boolean requeue) {
                this.deliveryTag = deliveryTag;
                this.requeue = requeue;
            }

            public Reject(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLonglong(), rdr.readBit());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.reject";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(delivery-tag=").append(this.deliveryTag).append(", requeue=").append(this.requeue).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLonglong(this.deliveryTag);
                writer.writeBit(this.requeue);
            }
        }

        public static class Return extends Method implements com.rabbitmq.client.AMQP.Basic.Return {
            public static final int INDEX = 50;
            private final String exchange;
            private final int replyCode;
            private final String replyText;
            private final String routingKey;

            public int getReplyCode() {
                return this.replyCode;
            }

            public String getReplyText() {
                return this.replyText;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public Return(int replyCode, String replyText, String exchange, String routingKey) {
                if (replyText == null) {
                    throw new IllegalStateException("Invalid configuration: 'replyText' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.replyCode = replyCode;
                    this.replyText = replyText;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                }
            }

            public Return(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readShortstr());
            }

            public int protocolClassId() {
                return Basic.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "basic.return";
            }

            public boolean hasContent() {
                return true;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(reply-code=").append(this.replyCode).append(", reply-text=").append(this.replyText).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.replyCode);
                writer.writeShortstr(this.replyText);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
            }
        }
    }

    public static class Channel {
        public static final int INDEX = 20;

        public static class Close extends Method implements com.rabbitmq.client.AMQP.Channel.Close {
            public static final int INDEX = 40;
            private final int classId;
            private final int methodId;
            private final int replyCode;
            private final String replyText;

            public int getReplyCode() {
                return this.replyCode;
            }

            public String getReplyText() {
                return this.replyText;
            }

            public int getClassId() {
                return this.classId;
            }

            public int getMethodId() {
                return this.methodId;
            }

            public Close(int replyCode, String replyText, int classId, int methodId) {
                if (replyText == null) {
                    throw new IllegalStateException("Invalid configuration: 'replyText' must be non-null.");
                }
                this.replyCode = replyCode;
                this.replyText = replyText;
                this.classId = classId;
                this.methodId = methodId;
            }

            public Close(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShort(), rdr.readShort());
            }

            public int protocolClassId() {
                return Channel.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.close";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(reply-code=").append(this.replyCode).append(", reply-text=").append(this.replyText).append(", class-id=").append(this.classId).append(", method-id=").append(this.methodId).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.replyCode);
                writer.writeShortstr(this.replyText);
                writer.writeShort(this.classId);
                writer.writeShort(this.methodId);
            }
        }

        public static class CloseOk extends Method implements com.rabbitmq.client.AMQP.Channel.CloseOk {
            public static final int INDEX = 41;

            public CloseOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Channel.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.close-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Flow extends Method implements com.rabbitmq.client.AMQP.Channel.Flow {
            public static final int INDEX = 20;
            private final boolean active;

            public boolean getActive() {
                return this.active;
            }

            public Flow(boolean active) {
                this.active = active;
            }

            public Flow(MethodArgumentReader rdr) throws IOException {
                this(rdr.readBit());
            }

            public int protocolClassId() {
                return INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.flow";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(active=").append(this.active).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeBit(this.active);
            }
        }

        public static class FlowOk extends Method implements com.rabbitmq.client.AMQP.Channel.FlowOk {
            public static final int INDEX = 21;
            private final boolean active;

            public boolean getActive() {
                return this.active;
            }

            public FlowOk(boolean active) {
                this.active = active;
            }

            public FlowOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readBit());
            }

            public int protocolClassId() {
                return Channel.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.flow-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(active=").append(this.active).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeBit(this.active);
            }
        }

        public static class Open extends Method implements com.rabbitmq.client.AMQP.Channel.Open {
            public static final int INDEX = 10;
            private final String outOfBand;

            public String getOutOfBand() {
                return this.outOfBand;
            }

            public Open(String outOfBand) {
                if (outOfBand == null) {
                    throw new IllegalStateException("Invalid configuration: 'outOfBand' must be non-null.");
                }
                this.outOfBand = outOfBand;
            }

            public Open(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr());
            }

            public int protocolClassId() {
                return Channel.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.open";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(out-of-band=").append(this.outOfBand).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.outOfBand);
            }
        }

        public static class OpenOk extends Method implements com.rabbitmq.client.AMQP.Channel.OpenOk {
            public static final int INDEX = 11;
            private final LongString channelId;

            public LongString getChannelId() {
                return this.channelId;
            }

            public OpenOk(LongString channelId) {
                if (channelId == null) {
                    throw new IllegalStateException("Invalid configuration: 'channelId' must be non-null.");
                }
                this.channelId = channelId;
            }

            public OpenOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLongstr());
            }

            public int protocolClassId() {
                return Channel.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "channel.open-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(channel-id=").append(this.channelId).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLongstr(this.channelId);
            }
        }
    }

    public static class Confirm {
        public static final int INDEX = 85;

        public static class Select extends Method implements com.rabbitmq.client.AMQP.Confirm.Select {
            public static final int INDEX = 10;
            private final boolean nowait;

            public boolean getNowait() {
                return this.nowait;
            }

            public Select(boolean nowait) {
                this.nowait = nowait;
            }

            public Select(MethodArgumentReader rdr) throws IOException {
                this(rdr.readBit());
            }

            public int protocolClassId() {
                return Confirm.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "confirm.select";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(nowait=").append(this.nowait).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeBit(this.nowait);
            }
        }

        public static class SelectOk extends Method implements com.rabbitmq.client.AMQP.Confirm.SelectOk {
            public static final int INDEX = 11;

            public SelectOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Confirm.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "confirm.select-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }
    }

    public static class Connection {
        public static final int INDEX = 10;

        public static class Close extends Method implements com.rabbitmq.client.AMQP.Connection.Close {
            public static final int INDEX = 50;
            private final int classId;
            private final int methodId;
            private final int replyCode;
            private final String replyText;

            public int getReplyCode() {
                return this.replyCode;
            }

            public String getReplyText() {
                return this.replyText;
            }

            public int getClassId() {
                return this.classId;
            }

            public int getMethodId() {
                return this.methodId;
            }

            public Close(int replyCode, String replyText, int classId, int methodId) {
                if (replyText == null) {
                    throw new IllegalStateException("Invalid configuration: 'replyText' must be non-null.");
                }
                this.replyCode = replyCode;
                this.replyText = replyText;
                this.classId = classId;
                this.methodId = methodId;
            }

            public Close(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShort(), rdr.readShort());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.close";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(reply-code=").append(this.replyCode).append(", reply-text=").append(this.replyText).append(", class-id=").append(this.classId).append(", method-id=").append(this.methodId).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.replyCode);
                writer.writeShortstr(this.replyText);
                writer.writeShort(this.classId);
                writer.writeShort(this.methodId);
            }
        }

        public static class CloseOk extends Method implements com.rabbitmq.client.AMQP.Connection.CloseOk {
            public static final int INDEX = 51;

            public CloseOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.close-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Open extends Method implements com.rabbitmq.client.AMQP.Connection.Open {
            public static final int INDEX = 40;
            private final String capabilities;
            private final boolean insist;
            private final String virtualHost;

            public String getVirtualHost() {
                return this.virtualHost;
            }

            public String getCapabilities() {
                return this.capabilities;
            }

            public boolean getInsist() {
                return this.insist;
            }

            public Open(String virtualHost, String capabilities, boolean insist) {
                if (virtualHost == null) {
                    throw new IllegalStateException("Invalid configuration: 'virtualHost' must be non-null.");
                } else if (capabilities == null) {
                    throw new IllegalStateException("Invalid configuration: 'capabilities' must be non-null.");
                } else {
                    this.virtualHost = virtualHost;
                    this.capabilities = capabilities;
                    this.insist = insist;
                }
            }

            public Open(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr(), rdr.readShortstr(), rdr.readBit());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.open";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(virtual-host=").append(this.virtualHost).append(", capabilities=").append(this.capabilities).append(", insist=").append(this.insist).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.virtualHost);
                writer.writeShortstr(this.capabilities);
                writer.writeBit(this.insist);
            }
        }

        public static class OpenOk extends Method implements com.rabbitmq.client.AMQP.Connection.OpenOk {
            public static final int INDEX = 41;
            private final String knownHosts;

            public String getKnownHosts() {
                return this.knownHosts;
            }

            public OpenOk(String knownHosts) {
                if (knownHosts == null) {
                    throw new IllegalStateException("Invalid configuration: 'knownHosts' must be non-null.");
                }
                this.knownHosts = knownHosts;
            }

            public OpenOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.open-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(known-hosts=").append(this.knownHosts).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.knownHosts);
            }
        }

        public static class Secure extends Method implements com.rabbitmq.client.AMQP.Connection.Secure {
            public static final int INDEX = 20;
            private final LongString challenge;

            public LongString getChallenge() {
                return this.challenge;
            }

            public Secure(LongString challenge) {
                if (challenge == null) {
                    throw new IllegalStateException("Invalid configuration: 'challenge' must be non-null.");
                }
                this.challenge = challenge;
            }

            public Secure(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLongstr());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.secure";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(challenge=").append(this.challenge).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLongstr(this.challenge);
            }
        }

        public static class SecureOk extends Method implements com.rabbitmq.client.AMQP.Connection.SecureOk {
            public static final int INDEX = 21;
            private final LongString response;

            public LongString getResponse() {
                return this.response;
            }

            public SecureOk(LongString response) {
                if (response == null) {
                    throw new IllegalStateException("Invalid configuration: 'response' must be non-null.");
                }
                this.response = response;
            }

            public SecureOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLongstr());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.secure-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(response=").append(this.response).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLongstr(this.response);
            }
        }

        public static class Start extends Method implements com.rabbitmq.client.AMQP.Connection.Start {
            public static final int INDEX = 10;
            private final LongString locales;
            private final LongString mechanisms;
            private final Map<String, Object> serverProperties;
            private final int versionMajor;
            private final int versionMinor;

            public int getVersionMajor() {
                return this.versionMajor;
            }

            public int getVersionMinor() {
                return this.versionMinor;
            }

            public Map<String, Object> getServerProperties() {
                return this.serverProperties;
            }

            public LongString getMechanisms() {
                return this.mechanisms;
            }

            public LongString getLocales() {
                return this.locales;
            }

            public Start(int versionMajor, int versionMinor, Map<String, Object> serverProperties, LongString mechanisms, LongString locales) {
                if (mechanisms == null) {
                    throw new IllegalStateException("Invalid configuration: 'mechanisms' must be non-null.");
                } else if (locales == null) {
                    throw new IllegalStateException("Invalid configuration: 'locales' must be non-null.");
                } else {
                    this.versionMajor = versionMajor;
                    this.versionMinor = versionMinor;
                    this.serverProperties = serverProperties == null ? null : Collections.unmodifiableMap(new HashMap(serverProperties));
                    this.mechanisms = mechanisms;
                    this.locales = locales;
                }
            }

            public Start(MethodArgumentReader rdr) throws IOException {
                this(rdr.readOctet(), rdr.readOctet(), rdr.readTable(), rdr.readLongstr(), rdr.readLongstr());
            }

            public int protocolClassId() {
                return INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.start";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(version-major=").append(this.versionMajor).append(", version-minor=").append(this.versionMinor).append(", server-properties=").append(this.serverProperties).append(", mechanisms=").append(this.mechanisms).append(", locales=").append(this.locales).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeOctet(this.versionMajor);
                writer.writeOctet(this.versionMinor);
                writer.writeTable(this.serverProperties);
                writer.writeLongstr(this.mechanisms);
                writer.writeLongstr(this.locales);
            }
        }

        public static class StartOk extends Method implements com.rabbitmq.client.AMQP.Connection.StartOk {
            public static final int INDEX = 11;
            private final Map<String, Object> clientProperties;
            private final String locale;
            private final String mechanism;
            private final LongString response;

            public Map<String, Object> getClientProperties() {
                return this.clientProperties;
            }

            public String getMechanism() {
                return this.mechanism;
            }

            public LongString getResponse() {
                return this.response;
            }

            public String getLocale() {
                return this.locale;
            }

            public StartOk(Map<String, Object> clientProperties, String mechanism, LongString response, String locale) {
                if (locale == null) {
                    throw new IllegalStateException("Invalid configuration: 'locale' must be non-null.");
                } else if (response == null) {
                    throw new IllegalStateException("Invalid configuration: 'response' must be non-null.");
                } else if (mechanism == null) {
                    throw new IllegalStateException("Invalid configuration: 'mechanism' must be non-null.");
                } else {
                    this.clientProperties = clientProperties == null ? null : Collections.unmodifiableMap(new HashMap(clientProperties));
                    this.mechanism = mechanism;
                    this.response = response;
                    this.locale = locale;
                }
            }

            public StartOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readTable(), rdr.readShortstr(), rdr.readLongstr(), rdr.readShortstr());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.start-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(client-properties=").append(this.clientProperties).append(", mechanism=").append(this.mechanism).append(", response=").append(this.response).append(", locale=").append(this.locale).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeTable(this.clientProperties);
                writer.writeShortstr(this.mechanism);
                writer.writeLongstr(this.response);
                writer.writeShortstr(this.locale);
            }
        }

        public static class Tune extends Method implements com.rabbitmq.client.AMQP.Connection.Tune {
            public static final int INDEX = 30;
            private final int channelMax;
            private final int frameMax;
            private final int heartbeat;

            public int getChannelMax() {
                return this.channelMax;
            }

            public int getFrameMax() {
                return this.frameMax;
            }

            public int getHeartbeat() {
                return this.heartbeat;
            }

            public Tune(int channelMax, int frameMax, int heartbeat) {
                this.channelMax = channelMax;
                this.frameMax = frameMax;
                this.heartbeat = heartbeat;
            }

            public Tune(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readLong(), rdr.readShort());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.tune";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(channel-max=").append(this.channelMax).append(", frame-max=").append(this.frameMax).append(", heartbeat=").append(this.heartbeat).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.channelMax);
                writer.writeLong(this.frameMax);
                writer.writeShort(this.heartbeat);
            }
        }

        public static class TuneOk extends Method implements com.rabbitmq.client.AMQP.Connection.TuneOk {
            public static final int INDEX = 31;
            private final int channelMax;
            private final int frameMax;
            private final int heartbeat;

            public int getChannelMax() {
                return this.channelMax;
            }

            public int getFrameMax() {
                return this.frameMax;
            }

            public int getHeartbeat() {
                return this.heartbeat;
            }

            public TuneOk(int channelMax, int frameMax, int heartbeat) {
                this.channelMax = channelMax;
                this.frameMax = frameMax;
                this.heartbeat = heartbeat;
            }

            public TuneOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readLong(), rdr.readShort());
            }

            public int protocolClassId() {
                return Connection.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "connection.tune-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(channel-max=").append(this.channelMax).append(", frame-max=").append(this.frameMax).append(", heartbeat=").append(this.heartbeat).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.channelMax);
                writer.writeLong(this.frameMax);
                writer.writeShort(this.heartbeat);
            }
        }
    }

    public interface MethodVisitor {
        Object visit(Request request) throws IOException;

        Object visit(RequestOk requestOk) throws IOException;

        Object visit(Ack ack) throws IOException;

        Object visit(Cancel cancel) throws IOException;

        Object visit(CancelOk cancelOk) throws IOException;

        Object visit(Consume consume) throws IOException;

        Object visit(ConsumeOk consumeOk) throws IOException;

        Object visit(Deliver deliver) throws IOException;

        Object visit(Get get) throws IOException;

        Object visit(GetEmpty getEmpty) throws IOException;

        Object visit(GetOk getOk) throws IOException;

        Object visit(Nack nack) throws IOException;

        Object visit(Publish publish) throws IOException;

        Object visit(Qos qos) throws IOException;

        Object visit(QosOk qosOk) throws IOException;

        Object visit(Recover recover) throws IOException;

        Object visit(RecoverAsync recoverAsync) throws IOException;

        Object visit(RecoverOk recoverOk) throws IOException;

        Object visit(Reject reject) throws IOException;

        Object visit(Return returnR) throws IOException;

        Object visit(Close close) throws IOException;

        Object visit(CloseOk closeOk) throws IOException;

        Object visit(Flow flow) throws IOException;

        Object visit(FlowOk flowOk) throws IOException;

        Object visit(Open open) throws IOException;

        Object visit(OpenOk openOk) throws IOException;

        Object visit(Select select) throws IOException;

        Object visit(SelectOk selectOk) throws IOException;

        Object visit(Close close) throws IOException;

        Object visit(CloseOk closeOk) throws IOException;

        Object visit(Open open) throws IOException;

        Object visit(OpenOk openOk) throws IOException;

        Object visit(Secure secure) throws IOException;

        Object visit(SecureOk secureOk) throws IOException;

        Object visit(Start start) throws IOException;

        Object visit(StartOk startOk) throws IOException;

        Object visit(Tune tune) throws IOException;

        Object visit(TuneOk tuneOk) throws IOException;

        Object visit(Bind bind) throws IOException;

        Object visit(BindOk bindOk) throws IOException;

        Object visit(Declare declare) throws IOException;

        Object visit(DeclareOk declareOk) throws IOException;

        Object visit(Delete delete) throws IOException;

        Object visit(DeleteOk deleteOk) throws IOException;

        Object visit(Unbind unbind) throws IOException;

        Object visit(UnbindOk unbindOk) throws IOException;

        Object visit(Bind bind) throws IOException;

        Object visit(BindOk bindOk) throws IOException;

        Object visit(Declare declare) throws IOException;

        Object visit(DeclareOk declareOk) throws IOException;

        Object visit(Delete delete) throws IOException;

        Object visit(DeleteOk deleteOk) throws IOException;

        Object visit(Purge purge) throws IOException;

        Object visit(PurgeOk purgeOk) throws IOException;

        Object visit(Unbind unbind) throws IOException;

        Object visit(UnbindOk unbindOk) throws IOException;

        Object visit(Commit commit) throws IOException;

        Object visit(CommitOk commitOk) throws IOException;

        Object visit(Rollback rollback) throws IOException;

        Object visit(RollbackOk rollbackOk) throws IOException;

        Object visit(Select select) throws IOException;

        Object visit(SelectOk selectOk) throws IOException;
    }

    public static class DefaultMethodVisitor implements MethodVisitor {
        public Object visit(Start x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(StartOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Secure x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(SecureOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Tune x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(TuneOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Open x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(OpenOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Close x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(CloseOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Open x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(OpenOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Flow x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(FlowOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Close x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(CloseOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Request x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(RequestOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Declare x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(DeclareOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Delete x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(DeleteOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Bind x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(BindOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Unbind x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(UnbindOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Declare x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(DeclareOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Bind x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(BindOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Purge x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(PurgeOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Delete x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(DeleteOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Unbind x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(UnbindOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Qos x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(QosOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Consume x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(ConsumeOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Cancel x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(CancelOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Publish x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Return x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Deliver x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Get x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(GetOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(GetEmpty x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Ack x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Reject x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(RecoverAsync x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Recover x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(RecoverOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Nack x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Select x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(SelectOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Commit x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(CommitOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Rollback x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(RollbackOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(Select x) throws IOException {
            throw new UnexpectedMethodError(x);
        }

        public Object visit(SelectOk x) throws IOException {
            throw new UnexpectedMethodError(x);
        }
    }

    public static class Exchange {
        public static final int INDEX = 40;

        public static class Bind extends Method implements com.rabbitmq.client.AMQP.Exchange.Bind {
            public static final int INDEX = 30;
            private final Map<String, Object> arguments;
            private final String destination;
            private final boolean nowait;
            private final String routingKey;
            private final String source;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getDestination() {
                return this.destination;
            }

            public String getSource() {
                return this.source;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Bind(int ticket, String destination, String source, String routingKey, boolean nowait, Map<String, Object> arguments) {
                if (source == null) {
                    throw new IllegalStateException("Invalid configuration: 'source' must be non-null.");
                } else if (destination == null) {
                    throw new IllegalStateException("Invalid configuration: 'destination' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.destination = destination;
                    this.source = source;
                    this.routingKey = routingKey;
                    this.nowait = nowait;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Bind(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.bind";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", destination=").append(this.destination).append(", source=").append(this.source).append(", routing-key=").append(this.routingKey).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.destination);
                writer.writeShortstr(this.source);
                writer.writeShortstr(this.routingKey);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class BindOk extends Method implements com.rabbitmq.client.AMQP.Exchange.BindOk {
            public static final int INDEX = 31;

            public BindOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.bind-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Declare extends Method implements com.rabbitmq.client.AMQP.Exchange.Declare {
            public static final int INDEX = 10;
            private final Map<String, Object> arguments;
            private final boolean autoDelete;
            private final boolean durable;
            private final String exchange;
            private final boolean internal;
            private final boolean nowait;
            private final boolean passive;
            private final int ticket;
            private final String type;

            public int getTicket() {
                return this.ticket;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getType() {
                return this.type;
            }

            public boolean getPassive() {
                return this.passive;
            }

            public boolean getDurable() {
                return this.durable;
            }

            public boolean getAutoDelete() {
                return this.autoDelete;
            }

            public boolean getInternal() {
                return this.internal;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Declare(int ticket, String exchange, String type, boolean passive, boolean durable, boolean autoDelete, boolean internal, boolean nowait, Map<String, Object> arguments) {
                if (type == null) {
                    throw new IllegalStateException("Invalid configuration: 'type' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.exchange = exchange;
                    this.type = type;
                    this.passive = passive;
                    this.durable = durable;
                    this.autoDelete = autoDelete;
                    this.internal = internal;
                    this.nowait = nowait;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Declare(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.declare";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", exchange=").append(this.exchange).append(", type=").append(this.type).append(", passive=").append(this.passive).append(", durable=").append(this.durable).append(", auto-delete=").append(this.autoDelete).append(", internal=").append(this.internal).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.type);
                writer.writeBit(this.passive);
                writer.writeBit(this.durable);
                writer.writeBit(this.autoDelete);
                writer.writeBit(this.internal);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class DeclareOk extends Method implements com.rabbitmq.client.AMQP.Exchange.DeclareOk {
            public static final int INDEX = 11;

            public DeclareOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.declare-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Delete extends Method implements com.rabbitmq.client.AMQP.Exchange.Delete {
            public static final int INDEX = 20;
            private final String exchange;
            private final boolean ifUnused;
            private final boolean nowait;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getExchange() {
                return this.exchange;
            }

            public boolean getIfUnused() {
                return this.ifUnused;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Delete(int ticket, String exchange, boolean ifUnused, boolean nowait) {
                if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                }
                this.ticket = ticket;
                this.exchange = exchange;
                this.ifUnused = ifUnused;
                this.nowait = nowait;
            }

            public Delete(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readBit(), rdr.readBit());
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.delete";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", exchange=").append(this.exchange).append(", if-unused=").append(this.ifUnused).append(", nowait=").append(this.nowait).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.exchange);
                writer.writeBit(this.ifUnused);
                writer.writeBit(this.nowait);
            }
        }

        public static class DeleteOk extends Method implements com.rabbitmq.client.AMQP.Exchange.DeleteOk {
            public static final int INDEX = 21;

            public DeleteOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.delete-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Unbind extends Method implements com.rabbitmq.client.AMQP.Exchange.Unbind {
            public static final int INDEX = 40;
            private final Map<String, Object> arguments;
            private final String destination;
            private final boolean nowait;
            private final String routingKey;
            private final String source;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getDestination() {
                return this.destination;
            }

            public String getSource() {
                return this.source;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Unbind(int ticket, String destination, String source, String routingKey, boolean nowait, Map<String, Object> arguments) {
                if (source == null) {
                    throw new IllegalStateException("Invalid configuration: 'source' must be non-null.");
                } else if (destination == null) {
                    throw new IllegalStateException("Invalid configuration: 'destination' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.destination = destination;
                    this.source = source;
                    this.routingKey = routingKey;
                    this.nowait = nowait;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Unbind(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.unbind";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", destination=").append(this.destination).append(", source=").append(this.source).append(", routing-key=").append(this.routingKey).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.destination);
                writer.writeShortstr(this.source);
                writer.writeShortstr(this.routingKey);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class UnbindOk extends Method implements com.rabbitmq.client.AMQP.Exchange.UnbindOk {
            public static final int INDEX = 51;

            public UnbindOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Exchange.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "exchange.unbind-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }
    }

    public static class Queue {
        public static final int INDEX = 50;

        public static class Bind extends Method implements com.rabbitmq.client.AMQP.Queue.Bind {
            public static final int INDEX = 20;
            private final Map<String, Object> arguments;
            private final String exchange;
            private final boolean nowait;
            private final String queue;
            private final String routingKey;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Bind(int ticket, String queue, String exchange, String routingKey, boolean nowait, Map<String, Object> arguments) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.queue = queue;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                    this.nowait = nowait;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Bind(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readShortstr(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.bind";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class BindOk extends Method implements com.rabbitmq.client.AMQP.Queue.BindOk {
            public static final int INDEX = 21;

            public BindOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.bind-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Declare extends Method implements com.rabbitmq.client.AMQP.Queue.Declare {
            public static final int INDEX = 10;
            private final Map<String, Object> arguments;
            private final boolean autoDelete;
            private final boolean durable;
            private final boolean exclusive;
            private final boolean nowait;
            private final boolean passive;
            private final String queue;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public boolean getPassive() {
                return this.passive;
            }

            public boolean getDurable() {
                return this.durable;
            }

            public boolean getExclusive() {
                return this.exclusive;
            }

            public boolean getAutoDelete() {
                return this.autoDelete;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Declare(int ticket, String queue, boolean passive, boolean durable, boolean exclusive, boolean autoDelete, boolean nowait, Map<String, Object> arguments) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                }
                this.ticket = ticket;
                this.queue = queue;
                this.passive = passive;
                this.durable = durable;
                this.exclusive = exclusive;
                this.autoDelete = autoDelete;
                this.nowait = nowait;
                this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
            }

            public Declare(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readBit(), rdr.readTable());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.declare";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", passive=").append(this.passive).append(", durable=").append(this.durable).append(", exclusive=").append(this.exclusive).append(", auto-delete=").append(this.autoDelete).append(", nowait=").append(this.nowait).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeBit(this.passive);
                writer.writeBit(this.durable);
                writer.writeBit(this.exclusive);
                writer.writeBit(this.autoDelete);
                writer.writeBit(this.nowait);
                writer.writeTable(this.arguments);
            }
        }

        public static class DeclareOk extends Method implements com.rabbitmq.client.AMQP.Queue.DeclareOk {
            public static final int INDEX = 11;
            private final int consumerCount;
            private final int messageCount;
            private final String queue;

            public String getQueue() {
                return this.queue;
            }

            public int getMessageCount() {
                return this.messageCount;
            }

            public int getConsumerCount() {
                return this.consumerCount;
            }

            public DeclareOk(String queue, int messageCount, int consumerCount) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                }
                this.queue = queue;
                this.messageCount = messageCount;
                this.consumerCount = consumerCount;
            }

            public DeclareOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShortstr(), rdr.readLong(), rdr.readLong());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.declare-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(queue=").append(this.queue).append(", message-count=").append(this.messageCount).append(", consumer-count=").append(this.consumerCount).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShortstr(this.queue);
                writer.writeLong(this.messageCount);
                writer.writeLong(this.consumerCount);
            }
        }

        public static class Delete extends Method implements com.rabbitmq.client.AMQP.Queue.Delete {
            public static final int INDEX = 40;
            private final boolean ifEmpty;
            private final boolean ifUnused;
            private final boolean nowait;
            private final String queue;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public boolean getIfUnused() {
                return this.ifUnused;
            }

            public boolean getIfEmpty() {
                return this.ifEmpty;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Delete(int ticket, String queue, boolean ifUnused, boolean ifEmpty, boolean nowait) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                }
                this.ticket = ticket;
                this.queue = queue;
                this.ifUnused = ifUnused;
                this.ifEmpty = ifEmpty;
                this.nowait = nowait;
            }

            public Delete(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readBit(), rdr.readBit(), rdr.readBit());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.delete";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", if-unused=").append(this.ifUnused).append(", if-empty=").append(this.ifEmpty).append(", nowait=").append(this.nowait).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeBit(this.ifUnused);
                writer.writeBit(this.ifEmpty);
                writer.writeBit(this.nowait);
            }
        }

        public static class DeleteOk extends Method implements com.rabbitmq.client.AMQP.Queue.DeleteOk {
            public static final int INDEX = 41;
            private final int messageCount;

            public int getMessageCount() {
                return this.messageCount;
            }

            public DeleteOk(int messageCount) {
                this.messageCount = messageCount;
            }

            public DeleteOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLong());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.delete-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(message-count=").append(this.messageCount).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLong(this.messageCount);
            }
        }

        public static class Purge extends Method implements com.rabbitmq.client.AMQP.Queue.Purge {
            public static final int INDEX = 30;
            private final boolean nowait;
            private final String queue;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public boolean getNowait() {
                return this.nowait;
            }

            public Purge(int ticket, String queue, boolean nowait) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                }
                this.ticket = ticket;
                this.queue = queue;
                this.nowait = nowait;
            }

            public Purge(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readBit());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.purge";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", nowait=").append(this.nowait).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeBit(this.nowait);
            }
        }

        public static class PurgeOk extends Method implements com.rabbitmq.client.AMQP.Queue.PurgeOk {
            public static final int INDEX = 31;
            private final int messageCount;

            public int getMessageCount() {
                return this.messageCount;
            }

            public PurgeOk(int messageCount) {
                this.messageCount = messageCount;
            }

            public PurgeOk(MethodArgumentReader rdr) throws IOException {
                this(rdr.readLong());
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.purge-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(message-count=").append(this.messageCount).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeLong(this.messageCount);
            }
        }

        public static class Unbind extends Method implements com.rabbitmq.client.AMQP.Queue.Unbind {
            public static final int INDEX = 50;
            private final Map<String, Object> arguments;
            private final String exchange;
            private final String queue;
            private final String routingKey;
            private final int ticket;

            public int getTicket() {
                return this.ticket;
            }

            public String getQueue() {
                return this.queue;
            }

            public String getExchange() {
                return this.exchange;
            }

            public String getRoutingKey() {
                return this.routingKey;
            }

            public Map<String, Object> getArguments() {
                return this.arguments;
            }

            public Unbind(int ticket, String queue, String exchange, String routingKey, Map<String, Object> arguments) {
                if (queue == null) {
                    throw new IllegalStateException("Invalid configuration: 'queue' must be non-null.");
                } else if (routingKey == null) {
                    throw new IllegalStateException("Invalid configuration: 'routingKey' must be non-null.");
                } else if (exchange == null) {
                    throw new IllegalStateException("Invalid configuration: 'exchange' must be non-null.");
                } else {
                    this.ticket = ticket;
                    this.queue = queue;
                    this.exchange = exchange;
                    this.routingKey = routingKey;
                    this.arguments = arguments == null ? null : Collections.unmodifiableMap(new HashMap(arguments));
                }
            }

            public Unbind(MethodArgumentReader rdr) throws IOException {
                this(rdr.readShort(), rdr.readShortstr(), rdr.readShortstr(), rdr.readShortstr(), rdr.readTable());
            }

            public int protocolClassId() {
                return INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.unbind";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("(ticket=").append(this.ticket).append(", queue=").append(this.queue).append(", exchange=").append(this.exchange).append(", routing-key=").append(this.routingKey).append(", arguments=").append(this.arguments).append(")");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
                writer.writeShort(this.ticket);
                writer.writeShortstr(this.queue);
                writer.writeShortstr(this.exchange);
                writer.writeShortstr(this.routingKey);
                writer.writeTable(this.arguments);
            }
        }

        public static class UnbindOk extends Method implements com.rabbitmq.client.AMQP.Queue.UnbindOk {
            public static final int INDEX = 51;

            public UnbindOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Queue.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "queue.unbind-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }
    }

    public static class Tx {
        public static final int INDEX = 90;

        public static class Commit extends Method implements com.rabbitmq.client.AMQP.Tx.Commit {
            public static final int INDEX = 20;

            public Commit(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.commit";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class CommitOk extends Method implements com.rabbitmq.client.AMQP.Tx.CommitOk {
            public static final int INDEX = 21;

            public CommitOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.commit-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Rollback extends Method implements com.rabbitmq.client.AMQP.Tx.Rollback {
            public static final int INDEX = 30;

            public Rollback(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.rollback";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class RollbackOk extends Method implements com.rabbitmq.client.AMQP.Tx.RollbackOk {
            public static final int INDEX = 31;

            public RollbackOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.rollback-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class Select extends Method implements com.rabbitmq.client.AMQP.Tx.Select {
            public static final int INDEX = 10;

            public Select(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.select";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }

        public static class SelectOk extends Method implements com.rabbitmq.client.AMQP.Tx.SelectOk {
            public static final int INDEX = 11;

            public SelectOk(MethodArgumentReader rdr) throws IOException {
                this();
            }

            public int protocolClassId() {
                return Tx.INDEX;
            }

            public int protocolMethodId() {
                return INDEX;
            }

            public String protocolMethodName() {
                return "tx.select-ok";
            }

            public boolean hasContent() {
                return false;
            }

            public Object visit(MethodVisitor visitor) throws IOException {
                return visitor.visit(this);
            }

            public void appendArgumentDebugStringTo(StringBuilder acc) {
                acc.append("()");
            }

            public void writeArgumentsTo(MethodArgumentWriter writer) throws IOException {
            }
        }
    }

    public static Method readMethodFrom(DataInputStream in) throws IOException {
        int classId = in.readShort();
        int methodId = in.readShort();
        switch (classId) {
            case Protocol.BBN_RCC_MON /*10*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Start(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new StartOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Secure(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new SecureOk(new MethodArgumentReader(new ValueReader(in)));
                    case Protocol.NETBLT /*30*/:
                        return new Tune(new MethodArgumentReader(new ValueReader(in)));
                    case Service.MSG_AUTH /*31*/:
                        return new TuneOk(new MethodArgumentReader(new ValueReader(in)));
                    case Delete.INDEX /*40*/:
                        return new Open(new MethodArgumentReader(new ValueReader(in)));
                    case Service.GRAPHICS /*41*/:
                        return new OpenOk(new MethodArgumentReader(new ValueReader(in)));
                    case Type.NSEC3 /*50*/:
                        return new Close(new MethodArgumentReader(new ValueReader(in)));
                    case Service.LA_MAINT /*51*/:
                        return new CloseOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Service.FTP_DATA /*20*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Open(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new OpenOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Flow(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new FlowOk(new MethodArgumentReader(new ValueReader(in)));
                    case Delete.INDEX /*40*/:
                        return new Close(new MethodArgumentReader(new ValueReader(in)));
                    case Service.GRAPHICS /*41*/:
                        return new CloseOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Protocol.NETBLT /*30*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Request(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new RequestOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Delete.INDEX /*40*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Declare(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new DeclareOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Delete(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new DeleteOk(new MethodArgumentReader(new ValueReader(in)));
                    case Protocol.NETBLT /*30*/:
                        return new Bind(new MethodArgumentReader(new ValueReader(in)));
                    case Service.MSG_AUTH /*31*/:
                        return new BindOk(new MethodArgumentReader(new ValueReader(in)));
                    case Delete.INDEX /*40*/:
                        return new Unbind(new MethodArgumentReader(new ValueReader(in)));
                    case Service.LA_MAINT /*51*/:
                        return new UnbindOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Type.NSEC3 /*50*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Declare(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new DeclareOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Bind(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new BindOk(new MethodArgumentReader(new ValueReader(in)));
                    case Protocol.NETBLT /*30*/:
                        return new Purge(new MethodArgumentReader(new ValueReader(in)));
                    case Service.MSG_AUTH /*31*/:
                        return new PurgeOk(new MethodArgumentReader(new ValueReader(in)));
                    case Delete.INDEX /*40*/:
                        return new Delete(new MethodArgumentReader(new ValueReader(in)));
                    case Service.GRAPHICS /*41*/:
                        return new DeleteOk(new MethodArgumentReader(new ValueReader(in)));
                    case Type.NSEC3 /*50*/:
                        return new Unbind(new MethodArgumentReader(new ValueReader(in)));
                    case Service.LA_MAINT /*51*/:
                        return new UnbindOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Basic.INDEX /*60*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Qos(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new QosOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Consume(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new ConsumeOk(new MethodArgumentReader(new ValueReader(in)));
                    case Protocol.NETBLT /*30*/:
                        return new Cancel(new MethodArgumentReader(new ValueReader(in)));
                    case Service.MSG_AUTH /*31*/:
                        return new CancelOk(new MethodArgumentReader(new ValueReader(in)));
                    case Delete.INDEX /*40*/:
                        return new Publish(new MethodArgumentReader(new ValueReader(in)));
                    case Type.NSEC3 /*50*/:
                        return new Return(new MethodArgumentReader(new ValueReader(in)));
                    case Basic.INDEX /*60*/:
                        return new Deliver(new MethodArgumentReader(new ValueReader(in)));
                    case Get.INDEX /*70*/:
                        return new Get(new MethodArgumentReader(new ValueReader(in)));
                    case Service.NETRJS_1 /*71*/:
                        return new GetOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.NETRJS_2 /*72*/:
                        return new GetEmpty(new MethodArgumentReader(new ValueReader(in)));
                    case Ack.INDEX /*80*/:
                        return new Ack(new MethodArgumentReader(new ValueReader(in)));
                    case Tx.INDEX /*90*/:
                        return new Reject(new MethodArgumentReader(new ValueReader(in)));
                    case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                        return new RecoverAsync(new MethodArgumentReader(new ValueReader(in)));
                    case Recover.INDEX /*110*/:
                        return new Recover(new MethodArgumentReader(new ValueReader(in)));
                    case Service.SUNRPC /*111*/:
                        return new RecoverOk(new MethodArgumentReader(new ValueReader(in)));
                    case Nack.INDEX /*120*/:
                        return new Nack(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Confirm.INDEX /*85*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Select(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new SelectOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
            case Tx.INDEX /*90*/:
                switch (methodId) {
                    case Protocol.BBN_RCC_MON /*10*/:
                        return new Select(new MethodArgumentReader(new ValueReader(in)));
                    case Service.USERS /*11*/:
                        return new SelectOk(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP_DATA /*20*/:
                        return new Commit(new MethodArgumentReader(new ValueReader(in)));
                    case Service.FTP /*21*/:
                        return new CommitOk(new MethodArgumentReader(new ValueReader(in)));
                    case Protocol.NETBLT /*30*/:
                        return new Rollback(new MethodArgumentReader(new ValueReader(in)));
                    case Service.MSG_AUTH /*31*/:
                        return new RollbackOk(new MethodArgumentReader(new ValueReader(in)));
                    default:
                        break;
                }
        }
        throw new UnknownClassOrMethodId(classId, methodId);
    }

    public static AMQContentHeader readContentHeaderFrom(DataInputStream in) throws IOException {
        int classId = in.readShort();
        switch (classId) {
            case Basic.INDEX /*60*/:
                return new BasicProperties(in);
            default:
                throw new UnknownClassOrMethodId(classId);
        }
    }
}
