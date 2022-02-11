package org.apache.activemq.transport.stomp;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp.Commands;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;
import org.apache.activemq.transport.stomp.Stomp.Responses;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.DataByteArrayInputStream;
import org.apache.activemq.util.MarshallingSupport;

public class StompCodec {
    static final byte[] crlfcrlf;
    String action;
    boolean awaitingCommandStart;
    int contentLength;
    ByteArrayOutputStream currentCommand;
    HashMap<String, String> headers;
    int previousByte;
    boolean processedHeaders;
    int readLength;
    TcpTransport transport;
    String version;

    static {
        crlfcrlf = new byte[]{MarshallingSupport.BIG_STRING_TYPE, (byte) 10, MarshallingSupport.BIG_STRING_TYPE, (byte) 10};
    }

    public StompCodec(TcpTransport transport) {
        this.currentCommand = new ByteArrayOutputStream();
        this.processedHeaders = false;
        this.contentLength = -1;
        this.readLength = 0;
        this.previousByte = -1;
        this.awaitingCommandStart = true;
        this.version = Stomp.V1_0;
        this.transport = transport;
    }

    public void parse(ByteArrayInputStream input, int readSize) throws Exception {
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i < readSize) {
                int b = input.read();
                if (!this.processedHeaders && this.previousByte == 0 && b == 0) {
                    i = i2;
                } else {
                    if (this.processedHeaders) {
                        if (this.contentLength != -1) {
                            int i3 = this.readLength;
                            this.readLength = i3 + 1;
                            if (i3 == this.contentLength) {
                                processCommand();
                                this.readLength = 0;
                            } else {
                                this.currentCommand.write(b);
                            }
                        } else if (b == 0) {
                            processCommand();
                        } else {
                            this.currentCommand.write(b);
                        }
                    } else if (this.awaitingCommandStart && b == 10) {
                        i = i2;
                    } else {
                        this.awaitingCommandStart = false;
                        this.currentCommand.write(b);
                        if (b == 10 && (this.previousByte == 10 || this.currentCommand.endsWith(crlfcrlf))) {
                            StompWireFormat wf = (StompWireFormat) this.transport.getWireFormat();
                            DataByteArrayInputStream data = new DataByteArrayInputStream(this.currentCommand.toByteArray());
                            this.action = wf.parseAction(data);
                            this.headers = wf.parseHeaders(data);
                            try {
                                String contentLengthHeader = (String) this.headers.get(Headers.CONTENT_LENGTH);
                                if ((this.action.equals(Commands.SEND) || this.action.equals(Responses.MESSAGE)) && contentLengthHeader != null) {
                                    this.contentLength = wf.parseContentLength(contentLengthHeader);
                                    this.processedHeaders = true;
                                    this.currentCommand.reset();
                                } else {
                                    this.contentLength = -1;
                                    this.processedHeaders = true;
                                    this.currentCommand.reset();
                                }
                            } catch (ProtocolException e) {
                            }
                        }
                    }
                    this.previousByte = b;
                    i = i2;
                }
            } else {
                return;
            }
        }
    }

    protected void processCommand() throws Exception {
        this.transport.doConsume(new StompFrame(this.action, this.headers, this.currentCommand.toByteArray()));
        this.processedHeaders = false;
        this.awaitingCommandStart = true;
        this.currentCommand.reset();
        this.contentLength = -1;
    }

    public static String detectVersion(Map<String, String> headers) throws ProtocolException {
        String accepts = (String) headers.get(Connect.ACCEPT_VERSION);
        if (accepts == null) {
            accepts = Stomp.V1_0;
        }
        HashSet<String> acceptsVersions = new HashSet(Arrays.asList(accepts.trim().split(Stomp.COMMA)));
        acceptsVersions.retainAll(Arrays.asList(Stomp.SUPPORTED_PROTOCOL_VERSIONS));
        if (!acceptsVersions.isEmpty()) {
            return (String) Collections.max(acceptsVersions);
        }
        throw new ProtocolException("Invalid Protocol version[" + accepts + "], supported versions are: " + Arrays.toString(Stomp.SUPPORTED_PROTOCOL_VERSIONS), true);
    }
}
