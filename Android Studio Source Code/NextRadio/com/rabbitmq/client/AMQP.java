package com.rabbitmq.client;

import com.rabbitmq.client.impl.AMQBasicProperties;
import com.rabbitmq.client.impl.ContentHeaderPropertyReader;
import com.rabbitmq.client.impl.ContentHeaderPropertyWriter;
import com.rabbitmq.client.impl.LongStringHelper;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

public interface AMQP {
    public static final int ACCESS_REFUSED = 403;
    public static final int CHANNEL_ERROR = 504;
    public static final int COMMAND_INVALID = 503;
    public static final int CONNECTION_FORCED = 320;
    public static final int CONTENT_TOO_LARGE = 311;
    public static final int FRAME_BODY = 3;
    public static final int FRAME_END = 206;
    public static final int FRAME_ERROR = 501;
    public static final int FRAME_HEADER = 2;
    public static final int FRAME_HEARTBEAT = 8;
    public static final int FRAME_METHOD = 1;
    public static final int FRAME_MIN_SIZE = 4096;
    public static final int INTERNAL_ERROR = 541;
    public static final int INVALID_PATH = 402;
    public static final int NOT_ALLOWED = 530;
    public static final int NOT_FOUND = 404;
    public static final int NOT_IMPLEMENTED = 540;
    public static final int NO_CONSUMERS = 313;
    public static final int NO_ROUTE = 312;
    public static final int PRECONDITION_FAILED = 406;
    public static final int REPLY_SUCCESS = 200;
    public static final int RESOURCE_ERROR = 506;
    public static final int RESOURCE_LOCKED = 405;
    public static final int SYNTAX_ERROR = 502;
    public static final int UNEXPECTED_FRAME = 505;

    public static class Access {

        public interface Request extends Method {

            public static final class Builder {
                private boolean active;
                private boolean exclusive;
                private boolean passive;
                private boolean read;
                private String realm;
                private boolean write;

                public Builder() {
                    this.realm = "/data";
                    this.exclusive = false;
                    this.passive = true;
                    this.active = true;
                    this.write = true;
                    this.read = true;
                }

                public Builder realm(String realm) {
                    this.realm = realm;
                    return this;
                }

                public Builder exclusive(boolean exclusive) {
                    this.exclusive = exclusive;
                    return this;
                }

                public Builder exclusive() {
                    return exclusive(true);
                }

                public Builder passive(boolean passive) {
                    this.passive = passive;
                    return this;
                }

                public Builder passive() {
                    return passive(true);
                }

                public Builder active(boolean active) {
                    this.active = active;
                    return this;
                }

                public Builder active() {
                    return active(true);
                }

                public Builder write(boolean write) {
                    this.write = write;
                    return this;
                }

                public Builder write() {
                    return write(true);
                }

                public Builder read(boolean read) {
                    this.read = read;
                    return this;
                }

                public Builder read() {
                    return read(true);
                }

                public Request build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Access.Request(this.realm, this.exclusive, this.passive, this.active, this.write, this.read);
                }
            }

            boolean getActive();

            boolean getExclusive();

            boolean getPassive();

            boolean getRead();

            String getRealm();

            boolean getWrite();
        }

        public interface RequestOk extends Method {

            public static final class Builder {
                private int ticket;

                public Builder() {
                    this.ticket = AMQP.FRAME_METHOD;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public RequestOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Access.RequestOk(this.ticket);
                }
            }

            int getTicket();
        }
    }

    public static class Basic {

        public interface Ack extends Method {

            public static final class Builder {
                private long deliveryTag;
                private boolean multiple;

                public Builder() {
                    this.deliveryTag = 0;
                    this.multiple = false;
                }

                public Builder deliveryTag(long deliveryTag) {
                    this.deliveryTag = deliveryTag;
                    return this;
                }

                public Builder multiple(boolean multiple) {
                    this.multiple = multiple;
                    return this;
                }

                public Builder multiple() {
                    return multiple(true);
                }

                public Ack build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Ack(this.deliveryTag, this.multiple);
                }
            }

            long getDeliveryTag();

            boolean getMultiple();
        }

        public interface Cancel extends Method {

            public static final class Builder {
                private String consumerTag;
                private boolean nowait;

                public Builder() {
                    this.nowait = false;
                }

                public Builder consumerTag(String consumerTag) {
                    this.consumerTag = consumerTag;
                    return this;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Cancel build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Cancel(this.consumerTag, this.nowait);
                }
            }

            String getConsumerTag();

            boolean getNowait();
        }

        public interface CancelOk extends Method {

            public static final class Builder {
                private String consumerTag;

                public Builder consumerTag(String consumerTag) {
                    this.consumerTag = consumerTag;
                    return this;
                }

                public CancelOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.CancelOk(this.consumerTag);
                }
            }

            String getConsumerTag();
        }

        public interface Consume extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private String consumerTag;
                private boolean exclusive;
                private boolean noAck;
                private boolean noLocal;
                private boolean nowait;
                private String queue;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.consumerTag = Stomp.EMPTY;
                    this.noLocal = false;
                    this.noAck = false;
                    this.exclusive = false;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder consumerTag(String consumerTag) {
                    this.consumerTag = consumerTag;
                    return this;
                }

                public Builder noLocal(boolean noLocal) {
                    this.noLocal = noLocal;
                    return this;
                }

                public Builder noLocal() {
                    return noLocal(true);
                }

                public Builder noAck(boolean noAck) {
                    this.noAck = noAck;
                    return this;
                }

                public Builder noAck() {
                    return noAck(true);
                }

                public Builder exclusive(boolean exclusive) {
                    this.exclusive = exclusive;
                    return this;
                }

                public Builder exclusive() {
                    return exclusive(true);
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Consume build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Consume(this.ticket, this.queue, this.consumerTag, this.noLocal, this.noAck, this.exclusive, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            String getConsumerTag();

            boolean getExclusive();

            boolean getNoAck();

            boolean getNoLocal();

            boolean getNowait();

            String getQueue();

            int getTicket();
        }

        public interface ConsumeOk extends Method {

            public static final class Builder {
                private String consumerTag;

                public Builder consumerTag(String consumerTag) {
                    this.consumerTag = consumerTag;
                    return this;
                }

                public ConsumeOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.ConsumeOk(this.consumerTag);
                }
            }

            String getConsumerTag();
        }

        public interface Deliver extends Method {

            public static final class Builder {
                private String consumerTag;
                private long deliveryTag;
                private String exchange;
                private boolean redelivered;
                private String routingKey;

                public Builder() {
                    this.redelivered = false;
                }

                public Builder consumerTag(String consumerTag) {
                    this.consumerTag = consumerTag;
                    return this;
                }

                public Builder deliveryTag(long deliveryTag) {
                    this.deliveryTag = deliveryTag;
                    return this;
                }

                public Builder redelivered(boolean redelivered) {
                    this.redelivered = redelivered;
                    return this;
                }

                public Builder redelivered() {
                    return redelivered(true);
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Deliver build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Deliver(this.consumerTag, this.deliveryTag, this.redelivered, this.exchange, this.routingKey);
                }
            }

            String getConsumerTag();

            long getDeliveryTag();

            String getExchange();

            boolean getRedelivered();

            String getRoutingKey();
        }

        public interface Get extends Method {

            public static final class Builder {
                private boolean noAck;
                private String queue;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.noAck = false;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder noAck(boolean noAck) {
                    this.noAck = noAck;
                    return this;
                }

                public Builder noAck() {
                    return noAck(true);
                }

                public Get build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Get(this.ticket, this.queue, this.noAck);
                }
            }

            boolean getNoAck();

            String getQueue();

            int getTicket();
        }

        public interface GetEmpty extends Method {

            public static final class Builder {
                private String clusterId;

                public Builder() {
                    this.clusterId = Stomp.EMPTY;
                }

                public Builder clusterId(String clusterId) {
                    this.clusterId = clusterId;
                    return this;
                }

                public GetEmpty build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.GetEmpty(this.clusterId);
                }
            }

            String getClusterId();
        }

        public interface GetOk extends Method {

            public static final class Builder {
                private long deliveryTag;
                private String exchange;
                private int messageCount;
                private boolean redelivered;
                private String routingKey;

                public Builder() {
                    this.redelivered = false;
                }

                public Builder deliveryTag(long deliveryTag) {
                    this.deliveryTag = deliveryTag;
                    return this;
                }

                public Builder redelivered(boolean redelivered) {
                    this.redelivered = redelivered;
                    return this;
                }

                public Builder redelivered() {
                    return redelivered(true);
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder messageCount(int messageCount) {
                    this.messageCount = messageCount;
                    return this;
                }

                public GetOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.GetOk(this.deliveryTag, this.redelivered, this.exchange, this.routingKey, this.messageCount);
                }
            }

            long getDeliveryTag();

            String getExchange();

            int getMessageCount();

            boolean getRedelivered();

            String getRoutingKey();
        }

        public interface Nack extends Method {

            public static final class Builder {
                private long deliveryTag;
                private boolean multiple;
                private boolean requeue;

                public Builder() {
                    this.deliveryTag = 0;
                    this.multiple = false;
                    this.requeue = true;
                }

                public Builder deliveryTag(long deliveryTag) {
                    this.deliveryTag = deliveryTag;
                    return this;
                }

                public Builder multiple(boolean multiple) {
                    this.multiple = multiple;
                    return this;
                }

                public Builder multiple() {
                    return multiple(true);
                }

                public Builder requeue(boolean requeue) {
                    this.requeue = requeue;
                    return this;
                }

                public Builder requeue() {
                    return requeue(true);
                }

                public Nack build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Nack(this.deliveryTag, this.multiple, this.requeue);
                }
            }

            long getDeliveryTag();

            boolean getMultiple();

            boolean getRequeue();
        }

        public interface Publish extends Method {

            public static final class Builder {
                private String exchange;
                private boolean immediate;
                private boolean mandatory;
                private String routingKey;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.exchange = Stomp.EMPTY;
                    this.routingKey = Stomp.EMPTY;
                    this.mandatory = false;
                    this.immediate = false;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder mandatory(boolean mandatory) {
                    this.mandatory = mandatory;
                    return this;
                }

                public Builder mandatory() {
                    return mandatory(true);
                }

                public Builder immediate(boolean immediate) {
                    this.immediate = immediate;
                    return this;
                }

                public Builder immediate() {
                    return immediate(true);
                }

                public Publish build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Publish(this.ticket, this.exchange, this.routingKey, this.mandatory, this.immediate);
                }
            }

            String getExchange();

            boolean getImmediate();

            boolean getMandatory();

            String getRoutingKey();

            int getTicket();
        }

        public interface Qos extends Method {

            public static final class Builder {
                private boolean global;
                private int prefetchCount;
                private int prefetchSize;

                public Builder() {
                    this.prefetchSize = 0;
                    this.prefetchCount = 0;
                    this.global = false;
                }

                public Builder prefetchSize(int prefetchSize) {
                    this.prefetchSize = prefetchSize;
                    return this;
                }

                public Builder prefetchCount(int prefetchCount) {
                    this.prefetchCount = prefetchCount;
                    return this;
                }

                public Builder global(boolean global) {
                    this.global = global;
                    return this;
                }

                public Builder global() {
                    return global(true);
                }

                public Qos build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Qos(this.prefetchSize, this.prefetchCount, this.global);
                }
            }

            boolean getGlobal();

            int getPrefetchCount();

            int getPrefetchSize();
        }

        public interface QosOk extends Method {

            public static final class Builder {
                public QosOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.QosOk();
                }
            }
        }

        public interface Recover extends Method {

            public static final class Builder {
                private boolean requeue;

                public Builder() {
                    this.requeue = false;
                }

                public Builder requeue(boolean requeue) {
                    this.requeue = requeue;
                    return this;
                }

                public Builder requeue() {
                    return requeue(true);
                }

                public Recover build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Recover(this.requeue);
                }
            }

            boolean getRequeue();
        }

        public interface RecoverAsync extends Method {

            public static final class Builder {
                private boolean requeue;

                public Builder() {
                    this.requeue = false;
                }

                public Builder requeue(boolean requeue) {
                    this.requeue = requeue;
                    return this;
                }

                public Builder requeue() {
                    return requeue(true);
                }

                public RecoverAsync build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.RecoverAsync(this.requeue);
                }
            }

            boolean getRequeue();
        }

        public interface RecoverOk extends Method {

            public static final class Builder {
                public RecoverOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.RecoverOk();
                }
            }
        }

        public interface Reject extends Method {

            public static final class Builder {
                private long deliveryTag;
                private boolean requeue;

                public Builder() {
                    this.requeue = true;
                }

                public Builder deliveryTag(long deliveryTag) {
                    this.deliveryTag = deliveryTag;
                    return this;
                }

                public Builder requeue(boolean requeue) {
                    this.requeue = requeue;
                    return this;
                }

                public Builder requeue() {
                    return requeue(true);
                }

                public Reject build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Reject(this.deliveryTag, this.requeue);
                }
            }

            long getDeliveryTag();

            boolean getRequeue();
        }

        public interface Return extends Method {

            public static final class Builder {
                private String exchange;
                private int replyCode;
                private String replyText;
                private String routingKey;

                public Builder() {
                    this.replyText = Stomp.EMPTY;
                }

                public Builder replyCode(int replyCode) {
                    this.replyCode = replyCode;
                    return this;
                }

                public Builder replyText(String replyText) {
                    this.replyText = replyText;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Return build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Basic.Return(this.replyCode, this.replyText, this.exchange, this.routingKey);
                }
            }

            String getExchange();

            int getReplyCode();

            String getReplyText();

            String getRoutingKey();
        }
    }

    public static class BasicProperties extends AMQBasicProperties {
        private String appId;
        private String clusterId;
        private String contentEncoding;
        private String contentType;
        private String correlationId;
        private Integer deliveryMode;
        private String expiration;
        private Map<String, Object> headers;
        private String messageId;
        private Integer priority;
        private String replyTo;
        private Date timestamp;
        private String type;
        private String userId;

        public static final class Builder {
            private String appId;
            private String clusterId;
            private String contentEncoding;
            private String contentType;
            private String correlationId;
            private Integer deliveryMode;
            private String expiration;
            private Map<String, Object> headers;
            private String messageId;
            private Integer priority;
            private String replyTo;
            private Date timestamp;
            private String type;
            private String userId;

            public Builder contentType(String contentType) {
                this.contentType = contentType;
                return this;
            }

            public Builder contentEncoding(String contentEncoding) {
                this.contentEncoding = contentEncoding;
                return this;
            }

            public Builder headers(Map<String, Object> headers) {
                this.headers = headers;
                return this;
            }

            public Builder deliveryMode(Integer deliveryMode) {
                this.deliveryMode = deliveryMode;
                return this;
            }

            public Builder priority(Integer priority) {
                this.priority = priority;
                return this;
            }

            public Builder correlationId(String correlationId) {
                this.correlationId = correlationId;
                return this;
            }

            public Builder replyTo(String replyTo) {
                this.replyTo = replyTo;
                return this;
            }

            public Builder expiration(String expiration) {
                this.expiration = expiration;
                return this;
            }

            public Builder messageId(String messageId) {
                this.messageId = messageId;
                return this;
            }

            public Builder timestamp(Date timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public Builder type(String type) {
                this.type = type;
                return this;
            }

            public Builder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public Builder appId(String appId) {
                this.appId = appId;
                return this;
            }

            public Builder clusterId(String clusterId) {
                this.clusterId = clusterId;
                return this;
            }

            public BasicProperties build() {
                return new BasicProperties(this.contentType, this.contentEncoding, this.headers, this.deliveryMode, this.priority, this.correlationId, this.replyTo, this.expiration, this.messageId, this.timestamp, this.type, this.userId, this.appId, this.clusterId);
            }
        }

        public BasicProperties(String contentType, String contentEncoding, Map<String, Object> headers, Integer deliveryMode, Integer priority, String correlationId, String replyTo, String expiration, String messageId, Date timestamp, String type, String userId, String appId, String clusterId) {
            this.contentType = contentType;
            this.contentEncoding = contentEncoding;
            this.headers = headers == null ? null : Collections.unmodifiableMap(new HashMap(headers));
            this.deliveryMode = deliveryMode;
            this.priority = priority;
            this.correlationId = correlationId;
            this.replyTo = replyTo;
            this.expiration = expiration;
            this.messageId = messageId;
            this.timestamp = timestamp;
            this.type = type;
            this.userId = userId;
            this.appId = appId;
            this.clusterId = clusterId;
        }

        public BasicProperties(DataInputStream in) throws IOException {
            super(in);
            ContentHeaderPropertyReader reader = new ContentHeaderPropertyReader(in);
            boolean contentType_present = reader.readPresence();
            boolean contentEncoding_present = reader.readPresence();
            boolean headers_present = reader.readPresence();
            boolean deliveryMode_present = reader.readPresence();
            boolean priority_present = reader.readPresence();
            boolean correlationId_present = reader.readPresence();
            boolean replyTo_present = reader.readPresence();
            boolean expiration_present = reader.readPresence();
            boolean messageId_present = reader.readPresence();
            boolean timestamp_present = reader.readPresence();
            boolean type_present = reader.readPresence();
            boolean userId_present = reader.readPresence();
            boolean appId_present = reader.readPresence();
            boolean clusterId_present = reader.readPresence();
            reader.finishPresence();
            this.contentType = contentType_present ? reader.readShortstr() : null;
            this.contentEncoding = contentEncoding_present ? reader.readShortstr() : null;
            this.headers = headers_present ? reader.readTable() : null;
            this.deliveryMode = deliveryMode_present ? Integer.valueOf(reader.readOctet()) : null;
            this.priority = priority_present ? Integer.valueOf(reader.readOctet()) : null;
            this.correlationId = correlationId_present ? reader.readShortstr() : null;
            this.replyTo = replyTo_present ? reader.readShortstr() : null;
            this.expiration = expiration_present ? reader.readShortstr() : null;
            this.messageId = messageId_present ? reader.readShortstr() : null;
            this.timestamp = timestamp_present ? reader.readTimestamp() : null;
            this.type = type_present ? reader.readShortstr() : null;
            this.userId = userId_present ? reader.readShortstr() : null;
            this.appId = appId_present ? reader.readShortstr() : null;
            this.clusterId = clusterId_present ? reader.readShortstr() : null;
        }

        public int getClassId() {
            return 60;
        }

        public String getClassName() {
            return "basic";
        }

        public Builder builder() {
            return new Builder().contentType(this.contentType).contentEncoding(this.contentEncoding).headers(this.headers).deliveryMode(this.deliveryMode).priority(this.priority).correlationId(this.correlationId).replyTo(this.replyTo).expiration(this.expiration).messageId(this.messageId).timestamp(this.timestamp).type(this.type).userId(this.userId).appId(this.appId).clusterId(this.clusterId);
        }

        public String getContentType() {
            return this.contentType;
        }

        @Deprecated
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentEncoding() {
            return this.contentEncoding;
        }

        @Deprecated
        public void setContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
        }

        public Map<String, Object> getHeaders() {
            return this.headers;
        }

        @Deprecated
        public void setHeaders(Map<String, Object> headers) {
            this.headers = headers == null ? null : Collections.unmodifiableMap(new HashMap(headers));
        }

        public Integer getDeliveryMode() {
            return this.deliveryMode;
        }

        @Deprecated
        public void setDeliveryMode(Integer deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Integer getPriority() {
            return this.priority;
        }

        @Deprecated
        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getCorrelationId() {
            return this.correlationId;
        }

        @Deprecated
        public void setCorrelationId(String correlationId) {
            this.correlationId = correlationId;
        }

        public String getReplyTo() {
            return this.replyTo;
        }

        @Deprecated
        public void setReplyTo(String replyTo) {
            this.replyTo = replyTo;
        }

        public String getExpiration() {
            return this.expiration;
        }

        @Deprecated
        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getMessageId() {
            return this.messageId;
        }

        @Deprecated
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public Date getTimestamp() {
            return this.timestamp;
        }

        @Deprecated
        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getType() {
            return this.type;
        }

        @Deprecated
        public void setType(String type) {
            this.type = type;
        }

        public String getUserId() {
            return this.userId;
        }

        @Deprecated
        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAppId() {
            return this.appId;
        }

        @Deprecated
        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getClusterId() {
            return this.clusterId;
        }

        @Deprecated
        public void setClusterId(String clusterId) {
            this.clusterId = clusterId;
        }

        public void writePropertiesTo(ContentHeaderPropertyWriter writer) throws IOException {
            boolean z;
            boolean z2 = true;
            writer.writePresence(this.contentType != null);
            if (this.contentEncoding != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.headers != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.deliveryMode != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.priority != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.correlationId != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.replyTo != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.expiration != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.messageId != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.timestamp != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.type != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.userId != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.appId != null) {
                z = true;
            } else {
                z = false;
            }
            writer.writePresence(z);
            if (this.clusterId == null) {
                z2 = false;
            }
            writer.writePresence(z2);
            writer.finishPresence();
            if (this.contentType != null) {
                writer.writeShortstr(this.contentType);
            }
            if (this.contentEncoding != null) {
                writer.writeShortstr(this.contentEncoding);
            }
            if (this.headers != null) {
                writer.writeTable(this.headers);
            }
            if (this.deliveryMode != null) {
                writer.writeOctet(this.deliveryMode);
            }
            if (this.priority != null) {
                writer.writeOctet(this.priority);
            }
            if (this.correlationId != null) {
                writer.writeShortstr(this.correlationId);
            }
            if (this.replyTo != null) {
                writer.writeShortstr(this.replyTo);
            }
            if (this.expiration != null) {
                writer.writeShortstr(this.expiration);
            }
            if (this.messageId != null) {
                writer.writeShortstr(this.messageId);
            }
            if (this.timestamp != null) {
                writer.writeTimestamp(this.timestamp);
            }
            if (this.type != null) {
                writer.writeShortstr(this.type);
            }
            if (this.userId != null) {
                writer.writeShortstr(this.userId);
            }
            if (this.appId != null) {
                writer.writeShortstr(this.appId);
            }
            if (this.clusterId != null) {
                writer.writeShortstr(this.clusterId);
            }
        }

        public void appendPropertyDebugStringTo(StringBuilder acc) {
            acc.append("(content-type=").append(this.contentType).append(", content-encoding=").append(this.contentEncoding).append(", headers=").append(this.headers).append(", delivery-mode=").append(this.deliveryMode).append(", priority=").append(this.priority).append(", correlation-id=").append(this.correlationId).append(", reply-to=").append(this.replyTo).append(", expiration=").append(this.expiration).append(", message-id=").append(this.messageId).append(", timestamp=").append(this.timestamp).append(", type=").append(this.type).append(", user-id=").append(this.userId).append(", app-id=").append(this.appId).append(", cluster-id=").append(this.clusterId).append(")");
        }
    }

    public static class Channel {

        public interface Close extends Method {

            public static final class Builder {
                private int classId;
                private int methodId;
                private int replyCode;
                private String replyText;

                public Builder() {
                    this.replyText = Stomp.EMPTY;
                }

                public Builder replyCode(int replyCode) {
                    this.replyCode = replyCode;
                    return this;
                }

                public Builder replyText(String replyText) {
                    this.replyText = replyText;
                    return this;
                }

                public Builder classId(int classId) {
                    this.classId = classId;
                    return this;
                }

                public Builder methodId(int methodId) {
                    this.methodId = methodId;
                    return this;
                }

                public Close build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.Close(this.replyCode, this.replyText, this.classId, this.methodId);
                }
            }

            int getClassId();

            int getMethodId();

            int getReplyCode();

            String getReplyText();
        }

        public interface CloseOk extends Method {

            public static final class Builder {
                public CloseOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.CloseOk();
                }
            }
        }

        public interface Flow extends Method {

            public static final class Builder {
                private boolean active;

                public Builder active(boolean active) {
                    this.active = active;
                    return this;
                }

                public Builder active() {
                    return active(true);
                }

                public Flow build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.Flow(this.active);
                }
            }

            boolean getActive();
        }

        public interface FlowOk extends Method {

            public static final class Builder {
                private boolean active;

                public Builder active(boolean active) {
                    this.active = active;
                    return this;
                }

                public Builder active() {
                    return active(true);
                }

                public FlowOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.FlowOk(this.active);
                }
            }

            boolean getActive();
        }

        public interface Open extends Method {

            public static final class Builder {
                private String outOfBand;

                public Builder() {
                    this.outOfBand = Stomp.EMPTY;
                }

                public Builder outOfBand(String outOfBand) {
                    this.outOfBand = outOfBand;
                    return this;
                }

                public Open build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.Open(this.outOfBand);
                }
            }

            String getOutOfBand();
        }

        public interface OpenOk extends Method {

            public static final class Builder {
                private LongString channelId;

                public Builder() {
                    this.channelId = LongStringHelper.asLongString(Stomp.EMPTY);
                }

                public Builder channelId(LongString channelId) {
                    this.channelId = channelId;
                    return this;
                }

                public Builder channelId(String channelId) {
                    return channelId(LongStringHelper.asLongString(channelId));
                }

                public OpenOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Channel.OpenOk(this.channelId);
                }
            }

            LongString getChannelId();
        }
    }

    public static class Confirm {

        public interface Select extends Method {

            public static final class Builder {
                private boolean nowait;

                public Builder() {
                    this.nowait = false;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Select build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Confirm.Select(this.nowait);
                }
            }

            boolean getNowait();
        }

        public interface SelectOk extends Method {

            public static final class Builder {
                public SelectOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Confirm.SelectOk();
                }
            }
        }
    }

    public static class Connection {

        public interface Close extends Method {

            public static final class Builder {
                private int classId;
                private int methodId;
                private int replyCode;
                private String replyText;

                public Builder() {
                    this.replyText = Stomp.EMPTY;
                }

                public Builder replyCode(int replyCode) {
                    this.replyCode = replyCode;
                    return this;
                }

                public Builder replyText(String replyText) {
                    this.replyText = replyText;
                    return this;
                }

                public Builder classId(int classId) {
                    this.classId = classId;
                    return this;
                }

                public Builder methodId(int methodId) {
                    this.methodId = methodId;
                    return this;
                }

                public Close build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.Close(this.replyCode, this.replyText, this.classId, this.methodId);
                }
            }

            int getClassId();

            int getMethodId();

            int getReplyCode();

            String getReplyText();
        }

        public interface CloseOk extends Method {

            public static final class Builder {
                public CloseOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.CloseOk();
                }
            }
        }

        public interface Open extends Method {

            public static final class Builder {
                private String capabilities;
                private boolean insist;
                private String virtualHost;

                public Builder() {
                    this.virtualHost = ReadOnlyContext.SEPARATOR;
                    this.capabilities = Stomp.EMPTY;
                    this.insist = false;
                }

                public Builder virtualHost(String virtualHost) {
                    this.virtualHost = virtualHost;
                    return this;
                }

                public Builder capabilities(String capabilities) {
                    this.capabilities = capabilities;
                    return this;
                }

                public Builder insist(boolean insist) {
                    this.insist = insist;
                    return this;
                }

                public Builder insist() {
                    return insist(true);
                }

                public Open build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.Open(this.virtualHost, this.capabilities, this.insist);
                }
            }

            String getCapabilities();

            boolean getInsist();

            String getVirtualHost();
        }

        public interface OpenOk extends Method {

            public static final class Builder {
                private String knownHosts;

                public Builder() {
                    this.knownHosts = Stomp.EMPTY;
                }

                public Builder knownHosts(String knownHosts) {
                    this.knownHosts = knownHosts;
                    return this;
                }

                public OpenOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.OpenOk(this.knownHosts);
                }
            }

            String getKnownHosts();
        }

        public interface Secure extends Method {

            public static final class Builder {
                private LongString challenge;

                public Builder challenge(LongString challenge) {
                    this.challenge = challenge;
                    return this;
                }

                public Builder challenge(String challenge) {
                    return challenge(LongStringHelper.asLongString(challenge));
                }

                public Secure build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.Secure(this.challenge);
                }
            }

            LongString getChallenge();
        }

        public interface SecureOk extends Method {

            public static final class Builder {
                private LongString response;

                public Builder response(LongString response) {
                    this.response = response;
                    return this;
                }

                public Builder response(String response) {
                    return response(LongStringHelper.asLongString(response));
                }

                public SecureOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.SecureOk(this.response);
                }
            }

            LongString getResponse();
        }

        public interface Start extends Method {

            public static final class Builder {
                private LongString locales;
                private LongString mechanisms;
                private Map<String, Object> serverProperties;
                private int versionMajor;
                private int versionMinor;

                public Builder() {
                    this.versionMajor = 0;
                    this.versionMinor = 9;
                    this.mechanisms = LongStringHelper.asLongString("PLAIN");
                    this.locales = LongStringHelper.asLongString("en_US");
                }

                public Builder versionMajor(int versionMajor) {
                    this.versionMajor = versionMajor;
                    return this;
                }

                public Builder versionMinor(int versionMinor) {
                    this.versionMinor = versionMinor;
                    return this;
                }

                public Builder serverProperties(Map<String, Object> serverProperties) {
                    this.serverProperties = serverProperties;
                    return this;
                }

                public Builder mechanisms(LongString mechanisms) {
                    this.mechanisms = mechanisms;
                    return this;
                }

                public Builder mechanisms(String mechanisms) {
                    return mechanisms(LongStringHelper.asLongString(mechanisms));
                }

                public Builder locales(LongString locales) {
                    this.locales = locales;
                    return this;
                }

                public Builder locales(String locales) {
                    return locales(LongStringHelper.asLongString(locales));
                }

                public Start build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.Start(this.versionMajor, this.versionMinor, this.serverProperties, this.mechanisms, this.locales);
                }
            }

            LongString getLocales();

            LongString getMechanisms();

            Map<String, Object> getServerProperties();

            int getVersionMajor();

            int getVersionMinor();
        }

        public interface StartOk extends Method {

            public static final class Builder {
                private Map<String, Object> clientProperties;
                private String locale;
                private String mechanism;
                private LongString response;

                public Builder() {
                    this.mechanism = "PLAIN";
                    this.locale = "en_US";
                }

                public Builder clientProperties(Map<String, Object> clientProperties) {
                    this.clientProperties = clientProperties;
                    return this;
                }

                public Builder mechanism(String mechanism) {
                    this.mechanism = mechanism;
                    return this;
                }

                public Builder response(LongString response) {
                    this.response = response;
                    return this;
                }

                public Builder response(String response) {
                    return response(LongStringHelper.asLongString(response));
                }

                public Builder locale(String locale) {
                    this.locale = locale;
                    return this;
                }

                public StartOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.StartOk(this.clientProperties, this.mechanism, this.response, this.locale);
                }
            }

            Map<String, Object> getClientProperties();

            String getLocale();

            String getMechanism();

            LongString getResponse();
        }

        public interface Tune extends Method {

            public static final class Builder {
                private int channelMax;
                private int frameMax;
                private int heartbeat;

                public Builder() {
                    this.channelMax = 0;
                    this.frameMax = 0;
                    this.heartbeat = 0;
                }

                public Builder channelMax(int channelMax) {
                    this.channelMax = channelMax;
                    return this;
                }

                public Builder frameMax(int frameMax) {
                    this.frameMax = frameMax;
                    return this;
                }

                public Builder heartbeat(int heartbeat) {
                    this.heartbeat = heartbeat;
                    return this;
                }

                public Tune build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.Tune(this.channelMax, this.frameMax, this.heartbeat);
                }
            }

            int getChannelMax();

            int getFrameMax();

            int getHeartbeat();
        }

        public interface TuneOk extends Method {

            public static final class Builder {
                private int channelMax;
                private int frameMax;
                private int heartbeat;

                public Builder() {
                    this.channelMax = 0;
                    this.frameMax = 0;
                    this.heartbeat = 0;
                }

                public Builder channelMax(int channelMax) {
                    this.channelMax = channelMax;
                    return this;
                }

                public Builder frameMax(int frameMax) {
                    this.frameMax = frameMax;
                    return this;
                }

                public Builder heartbeat(int heartbeat) {
                    this.heartbeat = heartbeat;
                    return this;
                }

                public TuneOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Connection.TuneOk(this.channelMax, this.frameMax, this.heartbeat);
                }
            }

            int getChannelMax();

            int getFrameMax();

            int getHeartbeat();
        }
    }

    public static class Exchange {

        public interface Bind extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private String destination;
                private boolean nowait;
                private String routingKey;
                private String source;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.routingKey = Stomp.EMPTY;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder destination(String destination) {
                    this.destination = destination;
                    return this;
                }

                public Builder source(String source) {
                    this.source = source;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Bind build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.Bind(this.ticket, this.destination, this.source, this.routingKey, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            String getDestination();

            boolean getNowait();

            String getRoutingKey();

            String getSource();

            int getTicket();
        }

        public interface BindOk extends Method {

            public static final class Builder {
                public BindOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.BindOk();
                }
            }
        }

        public interface Declare extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private boolean autoDelete;
                private boolean durable;
                private String exchange;
                private boolean internal;
                private boolean nowait;
                private boolean passive;
                private int ticket;
                private String type;

                public Builder() {
                    this.ticket = 0;
                    this.type = "direct";
                    this.passive = false;
                    this.durable = false;
                    this.autoDelete = false;
                    this.internal = false;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder type(String type) {
                    this.type = type;
                    return this;
                }

                public Builder passive(boolean passive) {
                    this.passive = passive;
                    return this;
                }

                public Builder passive() {
                    return passive(true);
                }

                public Builder durable(boolean durable) {
                    this.durable = durable;
                    return this;
                }

                public Builder durable() {
                    return durable(true);
                }

                public Builder autoDelete(boolean autoDelete) {
                    this.autoDelete = autoDelete;
                    return this;
                }

                public Builder autoDelete() {
                    return autoDelete(true);
                }

                public Builder internal(boolean internal) {
                    this.internal = internal;
                    return this;
                }

                public Builder internal() {
                    return internal(true);
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Declare build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.Declare(this.ticket, this.exchange, this.type, this.passive, this.durable, this.autoDelete, this.internal, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            boolean getAutoDelete();

            boolean getDurable();

            String getExchange();

            boolean getInternal();

            boolean getNowait();

            boolean getPassive();

            int getTicket();

            String getType();
        }

        public interface DeclareOk extends Method {

            public static final class Builder {
                public DeclareOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.DeclareOk();
                }
            }
        }

        public interface Delete extends Method {

            public static final class Builder {
                private String exchange;
                private boolean ifUnused;
                private boolean nowait;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.ifUnused = false;
                    this.nowait = false;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder ifUnused(boolean ifUnused) {
                    this.ifUnused = ifUnused;
                    return this;
                }

                public Builder ifUnused() {
                    return ifUnused(true);
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Delete build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.Delete(this.ticket, this.exchange, this.ifUnused, this.nowait);
                }
            }

            String getExchange();

            boolean getIfUnused();

            boolean getNowait();

            int getTicket();
        }

        public interface DeleteOk extends Method {

            public static final class Builder {
                public DeleteOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.DeleteOk();
                }
            }
        }

        public interface Unbind extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private String destination;
                private boolean nowait;
                private String routingKey;
                private String source;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.routingKey = Stomp.EMPTY;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder destination(String destination) {
                    this.destination = destination;
                    return this;
                }

                public Builder source(String source) {
                    this.source = source;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Unbind build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.Unbind(this.ticket, this.destination, this.source, this.routingKey, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            String getDestination();

            boolean getNowait();

            String getRoutingKey();

            String getSource();

            int getTicket();
        }

        public interface UnbindOk extends Method {

            public static final class Builder {
                public UnbindOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Exchange.UnbindOk();
                }
            }
        }
    }

    public static class PROTOCOL {
        public static final int MAJOR = 0;
        public static final int MINOR = 9;
        public static final int PORT = 5672;
        public static final int REVISION = 1;
    }

    public static class Queue {

        public interface Bind extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private String exchange;
                private boolean nowait;
                private String queue;
                private String routingKey;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.routingKey = Stomp.EMPTY;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Bind build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.Bind(this.ticket, this.queue, this.exchange, this.routingKey, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            String getExchange();

            boolean getNowait();

            String getQueue();

            String getRoutingKey();

            int getTicket();
        }

        public interface BindOk extends Method {

            public static final class Builder {
                public BindOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.BindOk();
                }
            }
        }

        public interface Declare extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private boolean autoDelete;
                private boolean durable;
                private boolean exclusive;
                private boolean nowait;
                private boolean passive;
                private String queue;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.passive = false;
                    this.durable = false;
                    this.exclusive = false;
                    this.autoDelete = false;
                    this.nowait = false;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder passive(boolean passive) {
                    this.passive = passive;
                    return this;
                }

                public Builder passive() {
                    return passive(true);
                }

                public Builder durable(boolean durable) {
                    this.durable = durable;
                    return this;
                }

                public Builder durable() {
                    return durable(true);
                }

                public Builder exclusive(boolean exclusive) {
                    this.exclusive = exclusive;
                    return this;
                }

                public Builder exclusive() {
                    return exclusive(true);
                }

                public Builder autoDelete(boolean autoDelete) {
                    this.autoDelete = autoDelete;
                    return this;
                }

                public Builder autoDelete() {
                    return autoDelete(true);
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Declare build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.Declare(this.ticket, this.queue, this.passive, this.durable, this.exclusive, this.autoDelete, this.nowait, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            boolean getAutoDelete();

            boolean getDurable();

            boolean getExclusive();

            boolean getNowait();

            boolean getPassive();

            String getQueue();

            int getTicket();
        }

        public interface DeclareOk extends Method {

            public static final class Builder {
                private int consumerCount;
                private int messageCount;
                private String queue;

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder messageCount(int messageCount) {
                    this.messageCount = messageCount;
                    return this;
                }

                public Builder consumerCount(int consumerCount) {
                    this.consumerCount = consumerCount;
                    return this;
                }

                public DeclareOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.DeclareOk(this.queue, this.messageCount, this.consumerCount);
                }
            }

            int getConsumerCount();

            int getMessageCount();

            String getQueue();
        }

        public interface Delete extends Method {

            public static final class Builder {
                private boolean ifEmpty;
                private boolean ifUnused;
                private boolean nowait;
                private String queue;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.ifUnused = false;
                    this.ifEmpty = false;
                    this.nowait = false;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder ifUnused(boolean ifUnused) {
                    this.ifUnused = ifUnused;
                    return this;
                }

                public Builder ifUnused() {
                    return ifUnused(true);
                }

                public Builder ifEmpty(boolean ifEmpty) {
                    this.ifEmpty = ifEmpty;
                    return this;
                }

                public Builder ifEmpty() {
                    return ifEmpty(true);
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Delete build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.Delete(this.ticket, this.queue, this.ifUnused, this.ifEmpty, this.nowait);
                }
            }

            boolean getIfEmpty();

            boolean getIfUnused();

            boolean getNowait();

            String getQueue();

            int getTicket();
        }

        public interface DeleteOk extends Method {

            public static final class Builder {
                private int messageCount;

                public Builder messageCount(int messageCount) {
                    this.messageCount = messageCount;
                    return this;
                }

                public DeleteOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.DeleteOk(this.messageCount);
                }
            }

            int getMessageCount();
        }

        public interface Purge extends Method {

            public static final class Builder {
                private boolean nowait;
                private String queue;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.nowait = false;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder nowait(boolean nowait) {
                    this.nowait = nowait;
                    return this;
                }

                public Builder nowait() {
                    return nowait(true);
                }

                public Purge build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.Purge(this.ticket, this.queue, this.nowait);
                }
            }

            boolean getNowait();

            String getQueue();

            int getTicket();
        }

        public interface PurgeOk extends Method {

            public static final class Builder {
                private int messageCount;

                public Builder messageCount(int messageCount) {
                    this.messageCount = messageCount;
                    return this;
                }

                public PurgeOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.PurgeOk(this.messageCount);
                }
            }

            int getMessageCount();
        }

        public interface Unbind extends Method {

            public static final class Builder {
                private Map<String, Object> arguments;
                private String exchange;
                private String queue;
                private String routingKey;
                private int ticket;

                public Builder() {
                    this.ticket = 0;
                    this.queue = Stomp.EMPTY;
                    this.routingKey = Stomp.EMPTY;
                    this.arguments = null;
                }

                public Builder ticket(int ticket) {
                    this.ticket = ticket;
                    return this;
                }

                public Builder queue(String queue) {
                    this.queue = queue;
                    return this;
                }

                public Builder exchange(String exchange) {
                    this.exchange = exchange;
                    return this;
                }

                public Builder routingKey(String routingKey) {
                    this.routingKey = routingKey;
                    return this;
                }

                public Builder arguments(Map<String, Object> arguments) {
                    this.arguments = arguments;
                    return this;
                }

                public Unbind build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.Unbind(this.ticket, this.queue, this.exchange, this.routingKey, this.arguments);
                }
            }

            Map<String, Object> getArguments();

            String getExchange();

            String getQueue();

            String getRoutingKey();

            int getTicket();
        }

        public interface UnbindOk extends Method {

            public static final class Builder {
                public UnbindOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Queue.UnbindOk();
                }
            }
        }
    }

    public static class Tx {

        public interface Commit extends Method {

            public static final class Builder {
                public Commit build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.Commit();
                }
            }
        }

        public interface CommitOk extends Method {

            public static final class Builder {
                public CommitOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.CommitOk();
                }
            }
        }

        public interface Rollback extends Method {

            public static final class Builder {
                public Rollback build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.Rollback();
                }
            }
        }

        public interface RollbackOk extends Method {

            public static final class Builder {
                public RollbackOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.RollbackOk();
                }
            }
        }

        public interface Select extends Method {

            public static final class Builder {
                public Select build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.Select();
                }
            }
        }

        public interface SelectOk extends Method {

            public static final class Builder {
                public SelectOk build() {
                    return new com.rabbitmq.client.impl.AMQImpl.Tx.SelectOk();
                }
            }
        }
    }
}
