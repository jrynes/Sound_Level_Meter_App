package org.apache.activemq.transport.stomp;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.Locale;
import org.apache.activemq.command.MessageId;

public interface Stomp {
    public static final byte BREAK = (byte) 10;
    public static final byte COLON = (byte) 58;
    public static final byte[] COLON_ESCAPE_SEQ;
    public static final String COMMA = ",";
    public static final String DEFAULT_HEART_BEAT = "0,0";
    public static final String DEFAULT_VERSION = "1.0";
    public static final String EMPTY = "";
    public static final String END = "end";
    public static final byte ESCAPE = (byte) 92;
    public static final byte[] ESCAPE_ESCAPE_SEQ;
    public static final String FALSE = "false";
    public static final String NEWLINE = "\n";
    public static final byte[] NEWLINE_ESCAPE_SEQ;
    public static final String NULL = "\u0000";
    public static final String[] SUPPORTED_PROTOCOL_VERSIONS;
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TRUE = "true";
    public static final String V1_0 = "1.0";
    public static final String V1_1 = "1.1";
    public static final String V1_2 = "1.2";

    public interface Commands {
        public static final String ABORT = "ABORT";
        public static final String ABORT_TRANSACTION = "ABORT";
        public static final String ACK = "ACK";
        public static final String BEGIN = "BEGIN";
        public static final String BEGIN_TRANSACTION = "BEGIN";
        public static final String COMMIT = "COMMIT";
        public static final String COMMIT_TRANSACTION = "COMMIT";
        public static final String CONNECT = "CONNECT";
        public static final String DISCONNECT = "DISCONNECT";
        public static final String KEEPALIVE = "KEEPALIVE";
        public static final String NACK = "NACK";
        public static final String SEND = "SEND";
        public static final String STOMP = "STOMP";
        public static final String SUBSCRIBE = "SUB";
        public static final String UNSUBSCRIBE = "UNSUB";
    }

    public interface Headers {
        public static final String AMQ_MESSAGE_TYPE = "amq-msg-type";
        public static final String CONTENT_LENGTH = "content-length";
        public static final String CONTENT_TYPE = "content-type";
        public static final String RECEIPT_REQUESTED = "receipt";
        public static final String SEPERATOR = ":";
        public static final String TRANSACTION = "transaction";
        public static final String TRANSFORMATION = "transformation";
        public static final String TRANSFORMATION_ERROR = "transformation-error";

        public interface Ack {
            public static final String ACK_ID = "id";
            public static final String MESSAGE_ID = "message-id";
            public static final String SUBSCRIPTION = "subscription";
        }

        public interface Connect {
            public static final String ACCEPT_VERSION = "accept-version";
            public static final String CLIENT_ID = "client-id";
            public static final String HEART_BEAT = "heart-beat";
            public static final String HOST = "host";
            public static final String LOGIN = "login";
            public static final String PASSCODE = "passcode";
            public static final String REQUEST_ID = "request-id";
        }

        public interface Connected {
            public static final String HEART_BEAT = "heart-beat";
            public static final String RESPONSE_ID = "response-id";
            public static final String SERVER = "server";
            public static final String SESSION = "session";
            public static final String VERSION = "version";
        }

        public interface Error {
            public static final String MESSAGE = "message";
        }

        public interface Message {
            public static final String ACK_ID = "ack";
            public static final String BROWSER = "browser";
            public static final String CORRELATION_ID = "correlation-id";
            public static final String DESTINATION = "destination";
            public static final String EXPIRATION_TIME = "expires";
            public static final String MESSAGE_ID = "message-id";
            public static final String ORIGINAL_DESTINATION = "original-destination";
            public static final String PERSISTENT = "persistent";
            public static final String PRORITY = "priority";
            public static final String REDELIVERED = "redelivered";
            public static final String REPLY_TO = "reply-to";
            public static final String SUBSCRIPTION = "subscription";
            public static final String TIMESTAMP = "timestamp";
            public static final String TYPE = "type";
            public static final String USERID = "JMSXUserID";
        }

        public interface Response {
            public static final String RECEIPT_ID = "receipt-id";
        }

        public interface Send {
            public static final String CORRELATION_ID = "correlation-id";
            public static final String DESTINATION = "destination";
            public static final String EXPIRATION_TIME = "expires";
            public static final String PERSISTENT = "persistent";
            public static final String PRIORITY = "priority";
            public static final String REPLY_TO = "reply-to";
            public static final String TYPE = "type";
        }

        public interface Subscribe {
            public static final String ACK_MODE = "ack";
            public static final String BROWSER = "browser";
            public static final String DESTINATION = "destination";
            public static final String ID = "id";
            public static final String SELECTOR = "selector";

            public interface AckModeValues {
                public static final String AUTO = "auto";
                public static final String CLIENT = "client";
                public static final String INDIVIDUAL = "client-individual";
            }
        }

        public interface Unsubscribe {
            public static final String DESTINATION = "destination";
            public static final String ID = "id";
        }
    }

    public interface Responses {
        public static final String CONNECTED = "CONNECTED";
        public static final String ERROR = "ERROR";
        public static final String MESSAGE = "MESSAGE";
        public static final String RECEIPT = "RECEIPT";
    }

    public enum Transformations {
        JMS_BYTE,
        JMS_XML,
        JMS_JSON,
        JMS_OBJECT_XML,
        JMS_OBJECT_JSON,
        JMS_MAP_XML,
        JMS_MAP_JSON,
        JMS_ADVISORY_XML,
        JMS_ADVISORY_JSON;

        public String toString() {
            return name().replaceAll(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR, "-").toLowerCase(Locale.ENGLISH);
        }

        public static Transformations getValue(String value) {
            return valueOf(value.replaceAll("-", EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR).toUpperCase(Locale.ENGLISH));
        }
    }

    static {
        ESCAPE_ESCAPE_SEQ = new byte[]{ESCAPE, ESCAPE};
        COLON_ESCAPE_SEQ = new byte[]{ESCAPE, (byte) 99};
        NEWLINE_ESCAPE_SEQ = new byte[]{ESCAPE, MessageId.DATA_STRUCTURE_TYPE};
        SUPPORTED_PROTOCOL_VERSIONS = new String[]{V1_2, V1_1, V1_0};
    }
}
