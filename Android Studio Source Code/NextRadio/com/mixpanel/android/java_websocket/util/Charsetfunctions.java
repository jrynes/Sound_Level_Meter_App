package com.mixpanel.android.java_websocket.util;

import com.mixpanel.android.java_websocket.exceptions.InvalidDataException;
import com.mixpanel.android.java_websocket.framing.CloseFrame;
import io.fabric.sdk.android.services.network.UrlUtils;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import org.apache.activemq.transport.stomp.Stomp;

public class Charsetfunctions {
    public static CodingErrorAction codingErrorAction;

    static {
        codingErrorAction = CodingErrorAction.REPORT;
    }

    public static byte[] utf8Bytes(String s) {
        try {
            return s.getBytes(UrlUtils.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] asciiBytes(String s) {
        try {
            return s.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String stringAscii(byte[] bytes) {
        return stringAscii(bytes, 0, bytes.length);
    }

    public static String stringAscii(byte[] bytes, int offset, int length) {
        try {
            return new String(bytes, offset, length, "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String stringUtf8(byte[] bytes) throws InvalidDataException {
        return stringUtf8(ByteBuffer.wrap(bytes));
    }

    public static String stringUtf8(ByteBuffer bytes) throws InvalidDataException {
        CharsetDecoder decode = Charset.forName(UrlUtils.UTF8).newDecoder();
        decode.onMalformedInput(codingErrorAction);
        decode.onUnmappableCharacter(codingErrorAction);
        try {
            bytes.mark();
            String s = decode.decode(bytes).toString();
            bytes.reset();
            return s;
        } catch (Throwable e) {
            throw new InvalidDataException((int) CloseFrame.NO_UTF8, e);
        }
    }

    public static void main(String[] args) throws InvalidDataException {
        stringUtf8(utf8Bytes(Stomp.NULL));
        stringAscii(asciiBytes(Stomp.NULL));
    }
}
