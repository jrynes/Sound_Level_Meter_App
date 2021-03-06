package com.google.zxing.maxicode.decoder;

import com.google.zxing.common.DecoderResult;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConsumerControl;
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
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.PartialCommand;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SubscriptionInfo;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.MarshallingSupport;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

final class DecodedBitStreamParser {
    private static final char ECI = '\ufffa';
    private static final char FS = '\u001c';
    private static final char GS = '\u001d';
    private static final char LATCHA = '\ufff7';
    private static final char LATCHB = '\ufff8';
    private static final char LOCK = '\ufff9';
    private static final NumberFormat NINE_DIGITS;
    private static final char NS = '\ufffb';
    private static final char PAD = '\ufffc';
    private static final char RS = '\u001e';
    private static final String[] SETS;
    private static final char SHIFTA = '\ufff0';
    private static final char SHIFTB = '\ufff1';
    private static final char SHIFTC = '\ufff2';
    private static final char SHIFTD = '\ufff3';
    private static final char SHIFTE = '\ufff4';
    private static final char THREESHIFTA = '\ufff6';
    private static final NumberFormat THREE_DIGITS;
    private static final char TWOSHIFTA = '\ufff5';

    static {
        NINE_DIGITS = new DecimalFormat("000000000");
        THREE_DIGITS = new DecimalFormat("000");
        SETS = new String[]{"\nABCDEFGHIJKLMNOPQRSTUVWXYZ\ufffa\u001c\u001d\u001e\ufffb \ufffc\"#$%&'()*+,-./0123456789:\ufff1\ufff2\ufff3\ufff4\ufff8", "`abcdefghijklmnopqrstuvwxyz\ufffa\u001c\u001d\u001e\ufffb{\ufffc}~\u007f;<=>?[\\]^_ ,./:@!|\ufffc\ufff5\ufff6\ufffc\ufff0\ufff2\ufff3\ufff4\ufff7", "\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\ufffa\u001c\u001d\u001e\u00db\u00dc\u00dd\u00de\u00df\u00aa\u00ac\u00b1\u00b2\u00b3\u00b5\u00b9\u00ba\u00bc\u00bd\u00be\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\ufff7 \ufff9\ufff3\ufff4\ufff8", "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\ufffa\u001c\u001d\u001e\ufffb\u00fb\u00fc\u00fd\u00fe\u00ff\u00a1\u00a8\u00ab\u00af\u00b0\u00b4\u00b7\u00b8\u00bb\u00bf\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\ufff7 \ufff2\ufff9\ufff4\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\ufffa\ufffc\ufffc\u001b\ufffb\u001c\u001d\u001e\u001f\u009f\u00a0\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a9\u00ad\u00ae\u00b6\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\ufff7 \ufff2\ufff3\ufff9\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?"};
    }

    private DecodedBitStreamParser() {
    }

    static DecoderResult decode(byte[] bytes, int mode) {
        StringBuilder result = new StringBuilder(144);
        switch (mode) {
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
                String postcode;
                if (mode == 2) {
                    postcode = new DecimalFormat("0000000000".substring(0, getPostCode2Length(bytes))).format((long) getPostCode2(bytes));
                } else {
                    postcode = getPostCode3(bytes);
                }
                String country = THREE_DIGITS.format((long) getCountry(bytes));
                String service = THREE_DIGITS.format((long) getServiceClass(bytes));
                result.append(getMessage(bytes, 10, 84));
                if (!result.toString().startsWith("[)>\u001e01\u001d")) {
                    result.insert(0, postcode + GS + country + GS + service + GS);
                    break;
                }
                result.insert(9, postcode + GS + country + GS + service + GS);
                break;
            case Type.MF /*4*/:
                result.append(getMessage(bytes, 1, 93));
                break;
            case Service.RJE /*5*/:
                result.append(getMessage(bytes, 1, 77));
                break;
        }
        return new DecoderResult(bytes, result.toString(), null, String.valueOf(mode));
    }

    private static int getBit(int bit, byte[] bytes) {
        bit--;
        if ((bytes[bit / 6] & (1 << (5 - (bit % 6)))) == 0) {
            return 0;
        }
        return 1;
    }

    private static int getInt(byte[] bytes, byte[] x) {
        int val = 0;
        for (int i = 0; i < x.length; i++) {
            val += getBit(x[i], bytes) << ((x.length - i) - 1);
        }
        return val;
    }

    private static int getCountry(byte[] bytes) {
        return getInt(bytes, new byte[]{JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 37, (byte) 38});
    }

    private static int getServiceClass(byte[] bytes) {
        return getInt(bytes, new byte[]{SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, Stomp.COLON, (byte) 59, PartialCommand.DATA_STRUCTURE_TYPE, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, JournalQueueAck.DATA_STRUCTURE_TYPE});
    }

    private static int getPostCode2Length(byte[] bytes) {
        return getInt(bytes, new byte[]{(byte) 39, DiscoveryEvent.DATA_STRUCTURE_TYPE, (byte) 41, (byte) 42, ExceptionResponse.DATA_STRUCTURE_TYPE, DataResponse.DATA_STRUCTURE_TYPE});
    }

    private static int getPostCode2(byte[] bytes) {
        return getInt(bytes, new byte[]{DataArrayResponse.DATA_STRUCTURE_TYPE, IntegerResponse.DATA_STRUCTURE_TYPE, (byte) 35, (byte) 36, CommandTypes.ACTIVEMQ_MAP_MESSAGE, CommandTypes.ACTIVEMQ_OBJECT_MESSAGE, CommandTypes.ACTIVEMQ_STREAM_MESSAGE, CommandTypes.ACTIVEMQ_TEXT_MESSAGE, CommandTypes.ACTIVEMQ_BLOB_MESSAGE, Response.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, MarshallingSupport.BIG_STRING_TYPE, ControlCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, (byte) 7, (byte) 8, (byte) 9, (byte) 10, Flags.CD, MarshallingSupport.LIST_TYPE, (byte) 1, (byte) 2});
    }

    private static String getPostCode3(byte[] bytes) {
        return String.valueOf(new char[]{SETS[0].charAt(getInt(bytes, new byte[]{(byte) 39, DiscoveryEvent.DATA_STRUCTURE_TYPE, (byte) 41, (byte) 42, ExceptionResponse.DATA_STRUCTURE_TYPE, DataResponse.DATA_STRUCTURE_TYPE})), SETS[0].charAt(getInt(bytes, new byte[]{DataArrayResponse.DATA_STRUCTURE_TYPE, IntegerResponse.DATA_STRUCTURE_TYPE, (byte) 35, (byte) 36, CommandTypes.ACTIVEMQ_MAP_MESSAGE, CommandTypes.ACTIVEMQ_OBJECT_MESSAGE})), SETS[0].charAt(getInt(bytes, new byte[]{CommandTypes.ACTIVEMQ_STREAM_MESSAGE, CommandTypes.ACTIVEMQ_TEXT_MESSAGE, CommandTypes.ACTIVEMQ_BLOB_MESSAGE, Response.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE})), SETS[0].charAt(getInt(bytes, new byte[]{MessageDispatch.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, MarshallingSupport.BIG_STRING_TYPE, ControlCommand.DATA_STRUCTURE_TYPE})), SETS[0].charAt(getInt(bytes, new byte[]{FlushCommand.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, (byte) 7, (byte) 8})), SETS[0].charAt(getInt(bytes, new byte[]{(byte) 9, (byte) 10, Flags.CD, MarshallingSupport.LIST_TYPE, (byte) 1, (byte) 2}))});
    }

    private static String getMessage(byte[] bytes, int start, int len) {
        StringBuilder sb = new StringBuilder();
        int shift = -1;
        int set = 0;
        int lastset = 0;
        int i = start;
        while (i < start + len) {
            int shift2;
            char c = SETS[set].charAt(bytes[i]);
            switch (c) {
                case '\ufff0':
                case '\ufff1':
                case '\ufff2':
                case '\ufff3':
                case '\ufff4':
                    lastset = set;
                    set = c - 65520;
                    shift2 = 1;
                    break;
                case '\ufff5':
                    lastset = set;
                    set = 0;
                    shift2 = 2;
                    break;
                case '\ufff6':
                    lastset = set;
                    set = 0;
                    shift2 = 3;
                    break;
                case '\ufff7':
                    set = 0;
                    shift2 = -1;
                    break;
                case '\ufff8':
                    set = 1;
                    shift2 = -1;
                    break;
                case '\ufff9':
                    shift2 = -1;
                    break;
                case '\ufffb':
                    i++;
                    i++;
                    i++;
                    i++;
                    i++;
                    sb.append(NINE_DIGITS.format((long) (((((bytes[i] << 24) + (bytes[i] << 18)) + (bytes[i] << 12)) + (bytes[i] << 6)) + bytes[i])));
                    shift2 = shift;
                    break;
                default:
                    sb.append(c);
                    shift2 = shift;
                    break;
            }
            shift = shift2 - 1;
            if (shift2 == 0) {
                set = lastset;
            }
            i++;
        }
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == PAD) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
