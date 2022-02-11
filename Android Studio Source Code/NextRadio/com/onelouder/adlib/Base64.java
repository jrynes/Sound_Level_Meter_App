package com.onelouder.adlib;

import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.DataArrayResponse;
import org.apache.activemq.command.DataResponse;
import org.apache.activemq.command.DiscoveryEvent;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.FlushCommand;
import org.apache.activemq.command.IntegerResponse;
import org.apache.activemq.command.JournalQueueAck;
import org.apache.activemq.command.JournalTopicAck;
import org.apache.activemq.command.JournalTrace;
import org.apache.activemq.command.JournalTransaction;
import org.apache.activemq.command.LocalTransactionId;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageDispatchNotification;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.PartialCommand;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ReplayCommand;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SubscriptionInfo;
import org.apache.activemq.command.XATransactionId;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.MarshallingSupport;
import org.xbill.DNS.Flags;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final byte[] ALPHABET;
    private static final byte[] DECODABET;
    public static final boolean DECODE = false;
    public static final boolean ENCODE = true;
    private static final byte EQUALS_SIGN = (byte) 61;
    private static final byte EQUALS_SIGN_ENC = (byte) -1;
    private static final byte NEW_LINE = (byte) 10;
    private static final byte[] WEBSAFE_ALPHABET;
    private static final byte[] WEBSAFE_DECODABET;
    private static final byte WHITE_SPACE_ENC = (byte) -5;

    static {
        boolean z;
        if (Base64.class.desiredAssertionStatus()) {
            z = DECODE;
        } else {
            z = ENCODE;
        }
        $assertionsDisabled = z;
        ALPHABET = new byte[]{ReplayCommand.DATA_STRUCTURE_TYPE, (byte) 66, (byte) 67, (byte) 68, (byte) 69, CommandTypes.BYTE_TYPE, CommandTypes.CHAR_TYPE, CommandTypes.SHORT_TYPE, CommandTypes.INTEGER_TYPE, CommandTypes.LONG_TYPE, CommandTypes.DOUBLE_TYPE, CommandTypes.FLOAT_TYPE, CommandTypes.STRING_TYPE, CommandTypes.BOOLEAN_TYPE, CommandTypes.BYTE_ARRAY_TYPE, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, MessageDispatchNotification.DATA_STRUCTURE_TYPE, (byte) 97, (byte) 98, (byte) 99, CommandTypes.ACTIVEMQ_QUEUE, CommandTypes.ACTIVEMQ_TOPIC, CommandTypes.ACTIVEMQ_TEMP_QUEUE, CommandTypes.ACTIVEMQ_TEMP_TOPIC, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, MessageId.DATA_STRUCTURE_TYPE, LocalTransactionId.DATA_STRUCTURE_TYPE, XATransactionId.DATA_STRUCTURE_TYPE, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, ConnectionId.DATA_STRUCTURE_TYPE, SessionId.DATA_STRUCTURE_TYPE, ConsumerId.DATA_STRUCTURE_TYPE, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        WEBSAFE_ALPHABET = new byte[]{ReplayCommand.DATA_STRUCTURE_TYPE, (byte) 66, (byte) 67, (byte) 68, (byte) 69, CommandTypes.BYTE_TYPE, CommandTypes.CHAR_TYPE, CommandTypes.SHORT_TYPE, CommandTypes.INTEGER_TYPE, CommandTypes.LONG_TYPE, CommandTypes.DOUBLE_TYPE, CommandTypes.FLOAT_TYPE, CommandTypes.STRING_TYPE, CommandTypes.BOOLEAN_TYPE, CommandTypes.BYTE_ARRAY_TYPE, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, MessageDispatchNotification.DATA_STRUCTURE_TYPE, (byte) 97, (byte) 98, (byte) 99, CommandTypes.ACTIVEMQ_QUEUE, CommandTypes.ACTIVEMQ_TOPIC, CommandTypes.ACTIVEMQ_TEMP_QUEUE, CommandTypes.ACTIVEMQ_TEMP_TOPIC, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, MessageId.DATA_STRUCTURE_TYPE, LocalTransactionId.DATA_STRUCTURE_TYPE, XATransactionId.DATA_STRUCTURE_TYPE, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, ConnectionId.DATA_STRUCTURE_TYPE, SessionId.DATA_STRUCTURE_TYPE, ConsumerId.DATA_STRUCTURE_TYPE, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, (byte) -9, (byte) 63, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, Stomp.COLON, (byte) 59, PartialCommand.DATA_STRUCTURE_TYPE, EQUALS_SIGN, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, Flags.CD, MarshallingSupport.LIST_TYPE, MarshallingSupport.BIG_STRING_TYPE, ControlCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_MAP_MESSAGE, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, CommandTypes.ACTIVEMQ_OBJECT_MESSAGE, CommandTypes.ACTIVEMQ_STREAM_MESSAGE, CommandTypes.ACTIVEMQ_TEXT_MESSAGE, CommandTypes.ACTIVEMQ_BLOB_MESSAGE, Response.DATA_STRUCTURE_TYPE, ExceptionResponse.DATA_STRUCTURE_TYPE, DataResponse.DATA_STRUCTURE_TYPE, DataArrayResponse.DATA_STRUCTURE_TYPE, IntegerResponse.DATA_STRUCTURE_TYPE, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, DiscoveryEvent.DATA_STRUCTURE_TYPE, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
        WEBSAFE_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, Stomp.COLON, (byte) 59, PartialCommand.DATA_STRUCTURE_TYPE, EQUALS_SIGN, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, Flags.CD, MarshallingSupport.LIST_TYPE, MarshallingSupport.BIG_STRING_TYPE, ControlCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_MAP_MESSAGE, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) -9, CommandTypes.ACTIVEMQ_OBJECT_MESSAGE, CommandTypes.ACTIVEMQ_STREAM_MESSAGE, CommandTypes.ACTIVEMQ_TEXT_MESSAGE, CommandTypes.ACTIVEMQ_BLOB_MESSAGE, Response.DATA_STRUCTURE_TYPE, ExceptionResponse.DATA_STRUCTURE_TYPE, DataResponse.DATA_STRUCTURE_TYPE, DataArrayResponse.DATA_STRUCTURE_TYPE, IntegerResponse.DATA_STRUCTURE_TYPE, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, DiscoveryEvent.DATA_STRUCTURE_TYPE, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    }

    private Base64() {
    }

    private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, byte[] alphabet) {
        int i;
        int i2 = 0;
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
        if (numSigBytes > 2) {
            i2 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i3 | i2;
        switch (numSigBytes) {
            case Zone.PRIMARY /*1*/:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case Zone.SECONDARY /*2*/:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case Protocol.GGP /*3*/:
                destination[destOffset] = alphabet[inBuff >>> 18];
                destination[destOffset + 1] = alphabet[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = alphabet[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = alphabet[inBuff & 63];
                break;
        }
        return destination;
    }

    public static String encode(byte[] source) {
        return encode(source, 0, source.length, ALPHABET, (boolean) ENCODE);
    }

    public static String encodeWebSafe(byte[] source, boolean doPadding) {
        return encode(source, 0, source.length, WEBSAFE_ALPHABET, doPadding);
    }

    public static String encode(byte[] source, int off, int len, byte[] alphabet, boolean doPadding) {
        byte[] outBuff = encode(source, off, len, alphabet, Integer.MAX_VALUE);
        int outLen = outBuff.length;
        while (!doPadding && outLen > 0 && outBuff[outLen - 1] == 61) {
            outLen--;
        }
        return new String(outBuff, 0, outLen);
    }

    public static byte[] encode(byte[] source, int off, int len, byte[] alphabet, int maxLineLength) {
        int len43 = ((len + 2) / 3) * 4;
        byte[] outBuff = new byte[((len43 / maxLineLength) + len43)];
        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        while (d < len2) {
            int inBuff = (((source[d + off] << 24) >>> 8) | ((source[(d + 1) + off] << 24) >>> 16)) | ((source[(d + 2) + off] << 24) >>> 24);
            outBuff[e] = alphabet[inBuff >>> 18];
            outBuff[e + 1] = alphabet[(inBuff >>> 12) & 63];
            outBuff[e + 2] = alphabet[(inBuff >>> 6) & 63];
            outBuff[e + 3] = alphabet[inBuff & 63];
            lineLength += 4;
            if (lineLength == maxLineLength) {
                outBuff[e + 4] = NEW_LINE;
                e++;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, alphabet);
            if (lineLength + 4 == maxLineLength) {
                outBuff[e + 4] = NEW_LINE;
                e++;
            }
            e += 4;
        }
        if ($assertionsDisabled || e == outBuff.length) {
            return outBuff;
        }
        throw new AssertionError();
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, byte[] decodabet) {
        if (source[srcOffset + 2] == EQUALS_SIGN) {
            destination[destOffset] = (byte) ((((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12)) >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == EQUALS_SIGN) {
            outBuff = (((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12)) | ((decodabet[source[srcOffset + 2]] << 24) >>> 18);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            outBuff = ((((decodabet[source[srcOffset]] << 24) >>> 6) | ((decodabet[source[srcOffset + 1]] << 24) >>> 12)) | ((decodabet[source[srcOffset + 2]] << 24) >>> 18)) | ((decodabet[source[srcOffset + 3]] << 24) >>> 24);
            destination[destOffset] = (byte) (outBuff >> 16);
            destination[destOffset + 1] = (byte) (outBuff >> 8);
            destination[destOffset + 2] = (byte) outBuff;
            return 3;
        }
    }

    public static byte[] decode(String s) throws Base64DecoderException {
        byte[] bytes = s.getBytes();
        return decode(bytes, 0, bytes.length);
    }

    public static byte[] decodeWebSafe(String s) throws Base64DecoderException {
        byte[] bytes = s.getBytes();
        return decodeWebSafe(bytes, 0, bytes.length);
    }

    public static byte[] decode(byte[] source) throws Base64DecoderException {
        return decode(source, 0, source.length);
    }

    public static byte[] decodeWebSafe(byte[] source) throws Base64DecoderException {
        return decodeWebSafe(source, 0, source.length);
    }

    public static byte[] decode(byte[] source, int off, int len) throws Base64DecoderException {
        return decode(source, off, len, DECODABET);
    }

    public static byte[] decodeWebSafe(byte[] source, int off, int len) throws Base64DecoderException {
        return decode(source, off, len, WEBSAFE_DECODABET);
    }

    public static byte[] decode(byte[] source, int off, int len, byte[] decodabet) throws Base64DecoderException {
        byte[] out;
        byte[] outBuff = new byte[(((len * 3) / 4) + 2)];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int i = 0;
        int b4Posn = 0;
        while (i < len) {
            int b4Posn2;
            byte sbiCrop = (byte) (source[i + off] & Service.LOCUS_CON);
            byte sbiDecode = decodabet[sbiCrop];
            if (sbiDecode >= -5) {
                if (sbiDecode < -1) {
                    b4Posn2 = b4Posn;
                } else if (sbiCrop == 61) {
                    int bytesLeft = len - i;
                    byte lastByte = (byte) (source[(len - 1) + off] & Service.LOCUS_CON);
                    if (b4Posn == 0 || b4Posn == 1) {
                        throw new Base64DecoderException("invalid padding byte '=' at byte offset " + i);
                    } else if ((b4Posn != 3 || bytesLeft <= 2) && (b4Posn != 4 || bytesLeft <= 1)) {
                        if (!(lastByte == 61 || lastByte == 10)) {
                            throw new Base64DecoderException("encoded value has invalid trailing byte");
                        }
                        if (b4Posn != 0) {
                        } else if (b4Posn != 1) {
                            throw new Base64DecoderException("single trailing character at offset " + (len - 1));
                        } else {
                            b4Posn2 = b4Posn + 1;
                            b4[b4Posn] = EQUALS_SIGN;
                            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
                        }
                        out = new byte[outBuffPosn];
                        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
                        return out;
                    } else {
                        throw new Base64DecoderException("padding byte '=' falsely signals end of encoded value at offset " + i);
                    }
                } else {
                    b4Posn2 = b4Posn + 1;
                    b4[b4Posn] = sbiCrop;
                    if (b4Posn2 == 4) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
                        b4Posn2 = 0;
                    }
                }
                i++;
                b4Posn = b4Posn2;
            } else {
                throw new Base64DecoderException("Bad Base64 input character at " + i + ": " + source[i + off] + "(decimal)");
            }
        }
        if (b4Posn != 0) {
        } else if (b4Posn != 1) {
            b4Posn2 = b4Posn + 1;
            b4[b4Posn] = EQUALS_SIGN;
            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet);
        } else {
            throw new Base64DecoderException("single trailing character at offset " + (len - 1));
        }
        out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }
}
