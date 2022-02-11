package org.apache.activemq.transport.stomp;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.Endpoint;
import org.apache.activemq.command.Response;
import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;
import org.apache.activemq.util.MarshallingSupport;

public class StompFrame implements Command {
    public static final byte[] NO_DATA;
    private String action;
    private byte[] content;
    private Map<String, String> headers;
    private transient Object transportContext;

    static {
        NO_DATA = new byte[0];
    }

    public StompFrame(String command) {
        this(command, null, null);
    }

    public StompFrame(String command, Map<String, String> headers) {
        this(command, headers, null);
    }

    public StompFrame(String command, Map<String, String> headers, byte[] data) {
        this.headers = new HashMap();
        this.content = NO_DATA;
        this.transportContext = null;
        this.action = command;
        if (headers != null) {
            this.headers = headers;
        }
        if (data != null) {
            this.content = data;
        }
    }

    public StompFrame() {
        this.headers = new HashMap();
        this.content = NO_DATA;
        this.transportContext = null;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String command) {
        this.action = command;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getBody() {
        try {
            return new String(this.content, HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            return new String(this.content);
        }
    }

    public void setContent(byte[] data) {
        this.content = data;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getCommandId() {
        return 0;
    }

    public Endpoint getFrom() {
        return null;
    }

    public Endpoint getTo() {
        return null;
    }

    public boolean isBrokerInfo() {
        return false;
    }

    public boolean isMessage() {
        return false;
    }

    public boolean isMessageAck() {
        return false;
    }

    public boolean isMessageDispatch() {
        return false;
    }

    public boolean isMessageDispatchNotification() {
        return false;
    }

    public boolean isResponse() {
        return false;
    }

    public boolean isResponseRequired() {
        return false;
    }

    public boolean isShutdownInfo() {
        return false;
    }

    public boolean isConnectionControl() {
        return false;
    }

    public boolean isWireFormatInfo() {
        return false;
    }

    public void setCommandId(int value) {
    }

    public void setFrom(Endpoint from) {
    }

    public void setResponseRequired(boolean responseRequired) {
    }

    public void setTo(Endpoint to) {
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return null;
    }

    public byte getDataStructureType() {
        return (byte) 0;
    }

    public boolean isMarshallAware() {
        return false;
    }

    public String toString() {
        return format(true);
    }

    public String format() {
        return format(false);
    }

    public String format(boolean forLogging) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getAction());
        buffer.append(Stomp.NEWLINE);
        for (Entry<String, String> entry : getHeaders().entrySet()) {
            buffer.append((String) entry.getKey());
            buffer.append(Headers.SEPERATOR);
            if (forLogging && ((String) entry.getKey()).toString().toLowerCase(Locale.ENGLISH).contains(Connect.PASSCODE)) {
                buffer.append("*****");
            } else {
                buffer.append((String) entry.getValue());
            }
            buffer.append(Stomp.NEWLINE);
        }
        buffer.append(Stomp.NEWLINE);
        if (getContent() != null) {
            try {
                String contentString = new String(getContent(), HttpRequest.CHARSET_UTF8);
                if (forLogging) {
                    contentString = MarshallingSupport.truncate64(contentString);
                }
                buffer.append(contentString);
            } catch (Throwable th) {
                buffer.append(Arrays.toString(getContent()));
            }
        }
        buffer.append('\u0000');
        return buffer.toString();
    }

    public Object getTransportContext() {
        return this.transportContext;
    }

    public void setTransportContext(Object transportContext) {
        this.transportContext = transportContext;
    }
}
