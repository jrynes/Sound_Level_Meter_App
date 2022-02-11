package com.rabbitmq.client.impl;

import com.rabbitmq.client.Command;
import com.rabbitmq.client.Method;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AMQCommand implements Command {
    private static final int EMPTY_CONTENT_BODY_FRAME_SIZE = 8;
    private final CommandAssembler assembler;

    public AMQCommand() {
        this(null, null, null);
    }

    public AMQCommand(Method method) {
        this(method, null, null);
    }

    public AMQCommand(Method method, AMQContentHeader contentHeader, byte[] body) {
        this.assembler = new CommandAssembler((Method) method, contentHeader, body);
    }

    public Method getMethod() {
        return this.assembler.getMethod();
    }

    public AMQContentHeader getContentHeader() {
        return this.assembler.getContentHeader();
    }

    public byte[] getContentBody() {
        return this.assembler.getContentBody();
    }

    public boolean handleFrame(Frame f) throws IOException {
        return this.assembler.handleFrame(f);
    }

    public void transmit(AMQChannel channel) throws IOException {
        int channelNumber = channel.getChannelNumber();
        AMQConnection connection = channel.getConnection();
        synchronized (this.assembler) {
            Method m = this.assembler.getMethod();
            connection.writeFrame(m.toFrame(channelNumber));
            if (m.hasContent()) {
                byte[] body = this.assembler.getContentBody();
                connection.writeFrame(this.assembler.getContentHeader().toFrame(channelNumber, (long) body.length));
                int frameMax = connection.getFrameMax();
                int bodyPayloadMax = frameMax == 0 ? body.length : frameMax - 8;
                for (int offset = 0; offset < body.length; offset += bodyPayloadMax) {
                    int fragmentLength;
                    int remaining = body.length - offset;
                    if (remaining < bodyPayloadMax) {
                        fragmentLength = remaining;
                    } else {
                        fragmentLength = bodyPayloadMax;
                    }
                    connection.writeFrame(Frame.fromBodyFragment(channelNumber, body, offset, fragmentLength));
                }
            }
        }
        connection.flush();
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean suppressBody) {
        String str;
        synchronized (this.assembler) {
            str = '{' + this.assembler.getMethod() + ", " + this.assembler.getContentHeader() + ", " + contentBodyStringBuilder(this.assembler.getContentBody(), suppressBody) + '}';
        }
        return str;
    }

    private static StringBuilder contentBodyStringBuilder(byte[] body, boolean suppressBody) {
        if (!suppressBody) {
            return new StringBuilder().append('\"').append(new String(body, HttpRequest.CHARSET_UTF8)).append('\"');
        }
        try {
            return new StringBuilder().append(body.length).append(" bytes of payload");
        } catch (Exception e) {
            return new StringBuilder().append('|').append(body.length).append('|');
        }
    }

    public static void checkPreconditions() {
        checkEmptyContentBodyFrameSize();
    }

    private static void checkEmptyContentBodyFrameSize() {
        Frame f = new Frame(3, 0, new byte[0]);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            f.writeTo(new DataOutputStream(s));
            int actualLength = s.toByteArray().length;
            if (EMPTY_CONTENT_BODY_FRAME_SIZE != actualLength) {
                throw new AssertionError("Internal error: expected EMPTY_CONTENT_BODY_FRAME_SIZE(8) is not equal to computed value: " + actualLength);
            }
        } catch (IOException e) {
            throw new AssertionError("IOException while checking EMPTY_CONTENT_BODY_FRAME_SIZE");
        }
    }
}
