package org.apache.activemq.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.activemq.transport.stomp.Stomp;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public final class MarshallingSupport {
    public static final byte BIG_STRING_TYPE = (byte) 13;
    public static final byte BOOLEAN_TYPE = (byte) 1;
    public static final byte BYTE_ARRAY_TYPE = (byte) 10;
    public static final byte BYTE_TYPE = (byte) 2;
    public static final byte CHAR_TYPE = (byte) 3;
    public static final byte DOUBLE_TYPE = (byte) 7;
    public static final byte FLOAT_TYPE = (byte) 8;
    public static final byte INTEGER_TYPE = (byte) 5;
    public static final byte LIST_TYPE = (byte) 12;
    public static final byte LONG_TYPE = (byte) 6;
    public static final byte MAP_TYPE = (byte) 11;
    public static final byte NULL = (byte) 0;
    public static final byte SHORT_TYPE = (byte) 4;
    public static final byte STRING_TYPE = (byte) 9;

    private MarshallingSupport() {
    }

    public static void marshalPrimitiveMap(Map<String, Object> map, DataOutputStream out) throws IOException {
        if (map == null) {
            out.writeInt(-1);
            return;
        }
        out.writeInt(map.size());
        for (String name : map.keySet()) {
            out.writeUTF(name);
            marshalPrimitive(out, map.get(name));
        }
    }

    public static Map<String, Object> unmarshalPrimitiveMap(DataInputStream in) throws IOException {
        return unmarshalPrimitiveMap(in, Integer.MAX_VALUE);
    }

    public static Map<String, Object> unmarshalPrimitiveMap(DataInputStream in, boolean force) throws IOException {
        return unmarshalPrimitiveMap(in, Integer.MAX_VALUE, force);
    }

    public static Map<String, Object> unmarshalPrimitiveMap(DataInputStream in, int maxPropertySize) throws IOException {
        return unmarshalPrimitiveMap(in, maxPropertySize, false);
    }

    public static Map<String, Object> unmarshalPrimitiveMap(DataInputStream in, int maxPropertySize, boolean force) throws IOException {
        int size = in.readInt();
        if (size > maxPropertySize) {
            throw new IOException("Primitive map is larger than the allowed size: " + size);
        } else if (size < 0) {
            return null;
        } else {
            Map<String, Object> rc = new HashMap(size);
            for (int i = 0; i < size; i++) {
                rc.put(in.readUTF(), unmarshalPrimitive(in, force));
            }
            return rc;
        }
    }

    public static void marshalPrimitiveList(List<Object> list, DataOutputStream out) throws IOException {
        out.writeInt(list.size());
        for (Object element : list) {
            marshalPrimitive(out, element);
        }
    }

    public static List<Object> unmarshalPrimitiveList(DataInputStream in) throws IOException {
        return unmarshalPrimitiveList(in, false);
    }

    public static List<Object> unmarshalPrimitiveList(DataInputStream in, boolean force) throws IOException {
        int size = in.readInt();
        List<Object> answer = new ArrayList(size);
        int size2 = size;
        while (true) {
            size = size2 - 1;
            if (size2 <= 0) {
                return answer;
            }
            answer.add(unmarshalPrimitive(in, force));
            size2 = size;
        }
    }

    public static void marshalPrimitive(DataOutputStream out, Object value) throws IOException {
        if (value == null) {
            marshalNull(out);
        } else if (value.getClass() == Boolean.class) {
            marshalBoolean(out, ((Boolean) value).booleanValue());
        } else if (value.getClass() == Byte.class) {
            marshalByte(out, ((Byte) value).byteValue());
        } else if (value.getClass() == Character.class) {
            marshalChar(out, ((Character) value).charValue());
        } else if (value.getClass() == Short.class) {
            marshalShort(out, ((Short) value).shortValue());
        } else if (value.getClass() == Integer.class) {
            marshalInt(out, ((Integer) value).intValue());
        } else if (value.getClass() == Long.class) {
            marshalLong(out, ((Long) value).longValue());
        } else if (value.getClass() == Float.class) {
            marshalFloat(out, ((Float) value).floatValue());
        } else if (value.getClass() == Double.class) {
            marshalDouble(out, ((Double) value).doubleValue());
        } else if (value.getClass() == byte[].class) {
            marshalByteArray(out, (byte[]) value);
        } else if (value.getClass() == String.class) {
            marshalString(out, (String) value);
        } else if (value.getClass() == UTF8Buffer.class) {
            marshalString(out, value.toString());
        } else if (value instanceof Map) {
            out.writeByte(11);
            marshalPrimitiveMap((Map) value, out);
        } else if (value instanceof List) {
            out.writeByte(12);
            marshalPrimitiveList((List) value, out);
        } else {
            throw new IOException("Object is not a primitive: " + value);
        }
    }

    public static Object unmarshalPrimitive(DataInputStream in) throws IOException {
        return unmarshalPrimitive(in, false);
    }

    public static Object unmarshalPrimitive(DataInputStream in, boolean force) throws IOException {
        byte type = in.readByte();
        switch (type) {
            case Tokenizer.EOF /*0*/:
                return null;
            case Zone.PRIMARY /*1*/:
                return in.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
            case Zone.SECONDARY /*2*/:
                return Byte.valueOf(in.readByte());
            case Protocol.GGP /*3*/:
                return Character.valueOf(in.readChar());
            case Type.MF /*4*/:
                return Short.valueOf(in.readShort());
            case Service.RJE /*5*/:
                return Integer.valueOf(in.readInt());
            case Protocol.TCP /*6*/:
                return Long.valueOf(in.readLong());
            case Service.ECHO /*7*/:
                return new Double(in.readDouble());
            case Protocol.EGP /*8*/:
                return new Float(in.readFloat());
            case Service.DISCARD /*9*/:
                if (force) {
                    return in.readUTF();
                }
                return readUTF(in, in.readUnsignedShort());
            case Protocol.BBN_RCC_MON /*10*/:
                Object value = new byte[in.readInt()];
                in.readFully((byte[]) value);
                return value;
            case Service.USERS /*11*/:
                return unmarshalPrimitiveMap(in, true);
            case Protocol.PUP /*12*/:
                return unmarshalPrimitiveList(in, true);
            case Service.DAYTIME /*13*/:
                if (force) {
                    return readUTF8(in);
                }
                return readUTF(in, in.readInt());
            default:
                throw new IOException("Unknown primitive type: " + type);
        }
    }

    public static UTF8Buffer readUTF(DataInputStream in, int length) throws IOException {
        byte[] data = new byte[length];
        in.readFully(data);
        return new UTF8Buffer(data);
    }

    public static void marshalNull(DataOutputStream out) throws IOException {
        out.writeByte(0);
    }

    public static void marshalBoolean(DataOutputStream out, boolean value) throws IOException {
        out.writeByte(1);
        out.writeBoolean(value);
    }

    public static void marshalByte(DataOutputStream out, byte value) throws IOException {
        out.writeByte(2);
        out.writeByte(value);
    }

    public static void marshalChar(DataOutputStream out, char value) throws IOException {
        out.writeByte(3);
        out.writeChar(value);
    }

    public static void marshalShort(DataOutputStream out, short value) throws IOException {
        out.writeByte(4);
        out.writeShort(value);
    }

    public static void marshalInt(DataOutputStream out, int value) throws IOException {
        out.writeByte(5);
        out.writeInt(value);
    }

    public static void marshalLong(DataOutputStream out, long value) throws IOException {
        out.writeByte(6);
        out.writeLong(value);
    }

    public static void marshalFloat(DataOutputStream out, float value) throws IOException {
        out.writeByte(8);
        out.writeFloat(value);
    }

    public static void marshalDouble(DataOutputStream out, double value) throws IOException {
        out.writeByte(7);
        out.writeDouble(value);
    }

    public static void marshalByteArray(DataOutputStream out, byte[] value) throws IOException {
        marshalByteArray(out, value, 0, value.length);
    }

    public static void marshalByteArray(DataOutputStream out, byte[] value, int offset, int length) throws IOException {
        out.writeByte(10);
        out.writeInt(length);
        out.write(value, offset, length);
    }

    public static void marshalString(DataOutputStream out, String s) throws IOException {
        if (s.length() < 8191) {
            out.writeByte(9);
            out.writeUTF(s);
            return;
        }
        out.writeByte(13);
        writeUTF8(out, s);
    }

    public static void writeUTF8(DataOutput dataOut, String text) throws IOException {
        if (text != null) {
            int i;
            int c;
            int strlen = text.length();
            int utflen = 0;
            char[] charr = new char[strlen];
            text.getChars(0, strlen, charr, 0);
            for (i = 0; i < strlen; i++) {
                c = charr[i];
                if (c >= 1 && c <= Service.LOCUS_CON) {
                    utflen++;
                } else if (c > 2047) {
                    utflen += 3;
                } else {
                    utflen += 2;
                }
            }
            byte[] bytearr = new byte[(utflen + 4)];
            int i2 = 0 + 1;
            bytearr[0] = (byte) ((utflen >>> 24) & Type.ANY);
            int i3 = i2 + 1;
            bytearr[i2] = (byte) ((utflen >>> 16) & Type.ANY);
            i2 = i3 + 1;
            bytearr[i3] = (byte) ((utflen >>> 8) & Type.ANY);
            i3 = i2 + 1;
            bytearr[i2] = (byte) ((utflen >>> 0) & Type.ANY);
            i = 0;
            i2 = i3;
            while (i < strlen) {
                c = charr[i];
                if (c >= 1 && c <= Service.LOCUS_CON) {
                    i3 = i2 + 1;
                    bytearr[i2] = (byte) c;
                } else if (c > 2047) {
                    i3 = i2 + 1;
                    bytearr[i2] = (byte) (((c >> 12) & 15) | 224);
                    i2 = i3 + 1;
                    bytearr[i3] = (byte) (((c >> 6) & 63) | Flags.FLAG8);
                    i3 = i2 + 1;
                    bytearr[i2] = (byte) (((c >> 0) & 63) | Flags.FLAG8);
                } else {
                    i3 = i2 + 1;
                    bytearr[i2] = (byte) (((c >> 6) & 31) | 192);
                    i2 = i3 + 1;
                    bytearr[i3] = (byte) (((c >> 0) & 63) | Flags.FLAG8);
                    i3 = i2;
                }
                i++;
                i2 = i3;
            }
            dataOut.write(bytearr);
            return;
        }
        dataOut.writeInt(-1);
    }

    public static String readUTF8(DataInput dataIn) throws IOException {
        int utflen = dataIn.readInt();
        if (utflen <= -1) {
            return null;
        }
        StringBuffer str = new StringBuffer(utflen);
        byte[] bytearr = new byte[utflen];
        int count = 0;
        dataIn.readFully(bytearr, 0, utflen);
        while (count < utflen) {
            int c = bytearr[count] & Type.ANY;
            int char2;
            switch (c >> 4) {
                case Tokenizer.EOF /*0*/:
                case Zone.PRIMARY /*1*/:
                case Zone.SECONDARY /*2*/:
                case Protocol.GGP /*3*/:
                case Type.MF /*4*/:
                case Service.RJE /*5*/:
                case Protocol.TCP /*6*/:
                case Service.ECHO /*7*/:
                    count++;
                    str.append((char) c);
                    break;
                case Protocol.PUP /*12*/:
                case Service.DAYTIME /*13*/:
                    count += 2;
                    if (count <= utflen) {
                        char2 = bytearr[count - 1];
                        if ((char2 & 192) == Flags.FLAG8) {
                            str.append((char) (((c & 31) << 6) | (char2 & 63)));
                            break;
                        }
                        throw new UTFDataFormatException();
                    }
                    throw new UTFDataFormatException();
                case Protocol.EMCON /*14*/:
                    count += 3;
                    if (count <= utflen) {
                        char2 = bytearr[count - 2];
                        int char3 = bytearr[count - 1];
                        if ((char2 & 192) == Flags.FLAG8 && (char3 & 192) == Flags.FLAG8) {
                            str.append((char) ((((c & 15) << 12) | ((char2 & 63) << 6)) | ((char3 & 63) << 0)));
                            break;
                        }
                        throw new UTFDataFormatException();
                    }
                    throw new UTFDataFormatException();
                    break;
                default:
                    throw new UTFDataFormatException();
            }
        }
        return new String(str);
    }

    public static String propertiesToString(Properties props) throws IOException {
        String result = Stomp.EMPTY;
        if (props == null) {
            return result;
        }
        DataByteArrayOutputStream dataOut = new DataByteArrayOutputStream();
        props.store(dataOut, Stomp.EMPTY);
        result = new String(dataOut.getData(), 0, dataOut.size());
        dataOut.close();
        return result;
    }

    public static Properties stringToProperties(String str) throws IOException {
        Properties result = new Properties();
        if (str != null && str.length() > 0) {
            DataByteArrayInputStream dataIn = new DataByteArrayInputStream(str.getBytes());
            result.load(dataIn);
            dataIn.close();
        }
        return result;
    }

    public static String truncate64(String text) {
        if (text.length() > 63) {
            return text.substring(0, 45) + "..." + text.substring(text.length() - 12);
        }
        return text;
    }
}
