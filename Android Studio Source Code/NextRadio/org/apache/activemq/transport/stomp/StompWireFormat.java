package org.apache.activemq.transport.stomp;

import com.google.android.gms.location.places.Place;
import com.rabbitmq.client.impl.AMQImpl.Basic.Recover;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp.Commands;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.wireformat.WireFormat;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class StompWireFormat implements WireFormat {
    private static final byte[] END_OF_FRAME;
    private static final int MAX_COMMAND_LENGTH = 1024;
    private static final int MAX_DATA_LENGTH = 104857600;
    private static final int MAX_HEADERS = 1000;
    private static final int MAX_HEADER_LENGTH = 10240;
    private static final byte[] NO_DATA;
    private String stompVersion;
    private int version;

    public StompWireFormat() {
        this.version = 1;
        this.stompVersion = Stomp.V1_0;
    }

    static {
        NO_DATA = new byte[0];
        END_OF_FRAME = new byte[]{(byte) 0, (byte) 10};
    }

    public ByteSequence marshal(Object command) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        marshal(command, dos);
        dos.close();
        return baos.toByteSequence();
    }

    public Object unmarshal(ByteSequence packet) throws IOException {
        return unmarshal(new DataInputStream(new ByteArrayInputStream(packet)));
    }

    public void marshal(Object command, DataOutput os) throws IOException {
        StompFrame stomp = (StompFrame) command;
        if (stomp.getAction().equals(Commands.KEEPALIVE)) {
            os.write(10);
            return;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(stomp.getAction());
        buffer.append(Stomp.NEWLINE);
        for (Entry<String, String> entry : stomp.getHeaders().entrySet()) {
            buffer.append((String) entry.getKey());
            buffer.append(Headers.SEPERATOR);
            buffer.append(encodeHeader((String) entry.getValue()));
            buffer.append(Stomp.NEWLINE);
        }
        buffer.append(Stomp.NEWLINE);
        os.write(buffer.toString().getBytes(HttpRequest.CHARSET_UTF8));
        os.write(stomp.getContent());
        os.write(END_OF_FRAME);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object unmarshal(java.io.DataInput r12) throws java.io.IOException {
        /*
        r11 = this;
        r0 = r11.parseAction(r12);	 Catch:{ ProtocolException -> 0x003c }
        r6 = r11.parseHeaders(r12);	 Catch:{ ProtocolException -> 0x003c }
        r4 = NO_DATA;	 Catch:{ ProtocolException -> 0x003c }
        r8 = "content-length";
        r3 = r6.get(r8);	 Catch:{ ProtocolException -> 0x003c }
        r3 = (java.lang.String) r3;	 Catch:{ ProtocolException -> 0x003c }
        r8 = "SEND";
        r8 = r0.equals(r8);	 Catch:{ ProtocolException -> 0x003c }
        if (r8 != 0) goto L_0x0022;
    L_0x001a:
        r8 = "MESSAGE";
        r8 = r0.equals(r8);	 Catch:{ ProtocolException -> 0x003c }
        if (r8 == 0) goto L_0x0043;
    L_0x0022:
        if (r3 == 0) goto L_0x0043;
    L_0x0024:
        r7 = r11.parseContentLength(r3);	 Catch:{ ProtocolException -> 0x003c }
        r4 = new byte[r7];	 Catch:{ ProtocolException -> 0x003c }
        r12.readFully(r4);	 Catch:{ ProtocolException -> 0x003c }
        r8 = r12.readByte();	 Catch:{ ProtocolException -> 0x003c }
        if (r8 == 0) goto L_0x006f;
    L_0x0033:
        r8 = new org.apache.activemq.transport.stomp.ProtocolException;	 Catch:{ ProtocolException -> 0x003c }
        r9 = "content-length bytes were read and there was no trailing null byte";
        r10 = 1;
        r8.<init>(r9, r10);	 Catch:{ ProtocolException -> 0x003c }
        throw r8;	 Catch:{ ProtocolException -> 0x003c }
    L_0x003c:
        r5 = move-exception;
        r8 = new org.apache.activemq.transport.stomp.StompFrameError;
        r8.<init>(r5);
    L_0x0042:
        return r8;
    L_0x0043:
        r2 = 0;
    L_0x0044:
        r1 = r12.readByte();	 Catch:{ ProtocolException -> 0x003c }
        if (r1 == 0) goto L_0x0066;
    L_0x004a:
        if (r2 != 0) goto L_0x0055;
    L_0x004c:
        r2 = new org.apache.activemq.util.ByteArrayOutputStream;	 Catch:{ ProtocolException -> 0x003c }
        r2.<init>();	 Catch:{ ProtocolException -> 0x003c }
    L_0x0051:
        r2.write(r1);	 Catch:{ ProtocolException -> 0x003c }
        goto L_0x0044;
    L_0x0055:
        r8 = r2.size();	 Catch:{ ProtocolException -> 0x003c }
        r9 = 104857600; // 0x6400000 float:3.6111186E-35 double:5.1806538E-316;
        if (r8 <= r9) goto L_0x0051;
    L_0x005d:
        r8 = new org.apache.activemq.transport.stomp.ProtocolException;	 Catch:{ ProtocolException -> 0x003c }
        r9 = "The maximum data length was exceeded";
        r10 = 1;
        r8.<init>(r9, r10);	 Catch:{ ProtocolException -> 0x003c }
        throw r8;	 Catch:{ ProtocolException -> 0x003c }
    L_0x0066:
        if (r2 == 0) goto L_0x006f;
    L_0x0068:
        r2.close();	 Catch:{ ProtocolException -> 0x003c }
        r4 = r2.toByteArray();	 Catch:{ ProtocolException -> 0x003c }
    L_0x006f:
        r8 = new org.apache.activemq.transport.stomp.StompFrame;	 Catch:{ ProtocolException -> 0x003c }
        r8.<init>(r0, r6, r4);	 Catch:{ ProtocolException -> 0x003c }
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.stomp.StompWireFormat.unmarshal(java.io.DataInput):java.lang.Object");
    }

    private String readLine(DataInput in, int maxLength, String errorMessage) throws IOException {
        ByteSequence sequence = readHeaderLine(in, maxLength, errorMessage);
        return new String(sequence.getData(), sequence.getOffset(), sequence.getLength(), HttpRequest.CHARSET_UTF8).trim();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.activemq.util.ByteSequence readHeaderLine(java.io.DataInput r7, int r8, java.lang.String r9) throws java.io.IOException {
        /*
        r6 = this;
        r1 = new org.apache.activemq.util.ByteArrayOutputStream;
        r1.<init>(r8);
    L_0x0005:
        r0 = r7.readByte();
        r4 = 10;
        if (r0 == r4) goto L_0x0021;
    L_0x000d:
        r4 = r1.size();
        if (r4 <= r8) goto L_0x001d;
    L_0x0013:
        r1.close();
        r4 = new org.apache.activemq.transport.stomp.ProtocolException;
        r5 = 1;
        r4.<init>(r9, r5);
        throw r4;
    L_0x001d:
        r1.write(r0);
        goto L_0x0005;
    L_0x0021:
        r1.close();
        r2 = r1.toByteSequence();
        r4 = r6.stompVersion;
        r5 = "1.0";
        r4 = r4.equals(r5);
        if (r4 != 0) goto L_0x003c;
    L_0x0032:
        r4 = r6.stompVersion;
        r5 = "1.2";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0051;
    L_0x003c:
        r3 = r2.getLength();
        if (r3 <= 0) goto L_0x0051;
    L_0x0042:
        r4 = r2.data;
        r5 = r3 + -1;
        r4 = r4[r5];
        r5 = 13;
        if (r4 != r5) goto L_0x0051;
    L_0x004c:
        r4 = r3 + -1;
        r2.setLength(r4);
    L_0x0051:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.stomp.StompWireFormat.readHeaderLine(java.io.DataInput, int, java.lang.String):org.apache.activemq.util.ByteSequence");
    }

    protected String parseAction(DataInput in) throws IOException {
        String action;
        do {
            action = readLine(in, MAX_COMMAND_LENGTH, "The maximum command length was exceeded");
            if (action == null) {
                throw new IOException("connection was closed");
            }
            action = action.trim();
        } while (action.length() <= 0);
        return action;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.util.HashMap<java.lang.String, java.lang.String> parseHeaders(java.io.DataInput r15) throws java.io.IOException {
        /*
        r14 = this;
        r13 = 1;
        r2 = new java.util.HashMap;
        r9 = 25;
        r2.<init>(r9);
    L_0x0008:
        r9 = 10240; // 0x2800 float:1.4349E-41 double:5.059E-320;
        r10 = "The maximum header length was exceeded";
        r3 = r14.readHeaderLine(r15, r9, r10);
        if (r3 == 0) goto L_0x0099;
    L_0x0012:
        r9 = r3.length;
        if (r9 <= r13) goto L_0x0099;
    L_0x0016:
        r9 = r2.size();
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r9 <= r10) goto L_0x0026;
    L_0x001e:
        r9 = new org.apache.activemq.transport.stomp.ProtocolException;
        r10 = "The maximum number of headers was exceeded";
        r9.<init>(r10, r13);
        throw r9;
    L_0x0026:
        r1 = new org.apache.activemq.util.ByteArrayInputStream;	 Catch:{ Exception -> 0x0042 }
        r1.<init>(r3);	 Catch:{ Exception -> 0x0042 }
        r7 = new org.apache.activemq.util.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0042 }
        r9 = r3.length;	 Catch:{ Exception -> 0x0042 }
        r7.<init>(r9);	 Catch:{ Exception -> 0x0042 }
        r6 = -1;
    L_0x0033:
        r6 = r1.read();	 Catch:{ Exception -> 0x0042 }
        r9 = -1;
        if (r6 == r9) goto L_0x0062;
    L_0x003a:
        r9 = 58;
        if (r6 == r9) goto L_0x0062;
    L_0x003e:
        r7.write(r6);	 Catch:{ Exception -> 0x0042 }
        goto L_0x0033;
    L_0x0042:
        r0 = move-exception;
        r9 = new org.apache.activemq.transport.stomp.ProtocolException;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Unable to parser header line [";
        r10 = r10.append(r11);
        r10 = r10.append(r3);
        r11 = "]";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r9.<init>(r10, r13);
        throw r9;
    L_0x0062:
        r5 = r7.toByteSequence();	 Catch:{ Exception -> 0x0042 }
        r4 = new java.lang.String;	 Catch:{ Exception -> 0x0042 }
        r9 = r5.getData();	 Catch:{ Exception -> 0x0042 }
        r10 = r5.getOffset();	 Catch:{ Exception -> 0x0042 }
        r11 = r5.getLength();	 Catch:{ Exception -> 0x0042 }
        r12 = "UTF-8";
        r4.<init>(r9, r10, r11, r12);	 Catch:{ Exception -> 0x0042 }
        r8 = r14.decodeHeader(r1);	 Catch:{ Exception -> 0x0042 }
        r9 = r14.stompVersion;	 Catch:{ Exception -> 0x0042 }
        r10 = "1.0";
        r9 = r9.equals(r10);	 Catch:{ Exception -> 0x0042 }
        if (r9 == 0) goto L_0x008b;
    L_0x0087:
        r8 = r8.trim();	 Catch:{ Exception -> 0x0042 }
    L_0x008b:
        r9 = r2.containsKey(r4);	 Catch:{ Exception -> 0x0042 }
        if (r9 != 0) goto L_0x0094;
    L_0x0091:
        r2.put(r4, r8);	 Catch:{ Exception -> 0x0042 }
    L_0x0094:
        r7.close();	 Catch:{ Exception -> 0x0042 }
        goto L_0x0008;
    L_0x0099:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.stomp.StompWireFormat.parseHeaders(java.io.DataInput):java.util.HashMap<java.lang.String, java.lang.String>");
    }

    protected int parseContentLength(String contentLength) throws ProtocolException {
        try {
            int length = Integer.parseInt(contentLength.trim());
            if (length <= MAX_DATA_LENGTH) {
                return length;
            }
            throw new ProtocolException("The maximum data length was exceeded", true);
        } catch (NumberFormatException e) {
            throw new ProtocolException("Specified content-length is not a valid integer", true);
        }
    }

    private String encodeHeader(String header) throws IOException {
        String result = header;
        if (this.stompVersion.equals(Stomp.V1_0)) {
            return result;
        }
        byte[] utf8buf = header.getBytes(HttpRequest.CHARSET_UTF8);
        ByteArrayOutputStream stream = new ByteArrayOutputStream(utf8buf.length);
        for (byte val : utf8buf) {
            switch (val) {
                case Protocol.BBN_RCC_MON /*10*/:
                    stream.write(Stomp.NEWLINE_ESCAPE_SEQ);
                    break;
                case Place.TYPE_LOCKSMITH /*58*/:
                    stream.write(Stomp.COLON_ESCAPE_SEQ);
                    break;
                case Place.TYPE_TRAIN_STATION /*92*/:
                    stream.write(Stomp.ESCAPE_ESCAPE_SEQ);
                    break;
                default:
                    stream.write(val);
                    break;
            }
        }
        return new String(stream.toByteArray(), HttpRequest.CHARSET_UTF8);
    }

    private String decodeHeader(InputStream header) throws IOException {
        ByteArrayOutputStream decoded = new ByteArrayOutputStream();
        PushbackInputStream stream = new PushbackInputStream(header);
        while (true) {
            int value = stream.read();
            if (value == -1) {
                return new String(decoded.toByteArray(), HttpRequest.CHARSET_UTF8);
            }
            if (value == 92) {
                int next = stream.read();
                if (next != -1) {
                    switch (next) {
                        case Place.TYPE_TRAIN_STATION /*92*/:
                            decoded.write(92);
                            break;
                        case Service.METAGRAM /*99*/:
                            decoded.write(58);
                            break;
                        case Recover.INDEX /*110*/:
                            decoded.write(10);
                            break;
                        default:
                            stream.unread(next);
                            decoded.write(value);
                            break;
                    }
                }
                decoded.write(value);
            } else {
                decoded.write(value);
            }
        }
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStompVersion() {
        return this.stompVersion;
    }

    public void setStompVersion(String stompVersion) {
        this.stompVersion = stompVersion;
    }
}
