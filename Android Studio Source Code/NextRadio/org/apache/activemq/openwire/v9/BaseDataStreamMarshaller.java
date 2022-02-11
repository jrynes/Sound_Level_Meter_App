package org.apache.activemq.openwire.v9;

import com.rabbitmq.client.LongString;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.openwire.BooleanStream;
import org.apache.activemq.openwire.DataStreamMarshaller;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.util.ByteSequence;
import org.xbill.DNS.WKSRecord.Service;

public abstract class BaseDataStreamMarshaller implements DataStreamMarshaller {
    public static final Constructor STACK_TRACE_ELEMENT_CONSTRUCTOR;

    public abstract DataStructure createObject();

    public abstract byte getDataStructureType();

    static {
        Constructor constructor = null;
        try {
            constructor = StackTraceElement.class.getConstructor(new Class[]{String.class, String.class, String.class, Integer.TYPE});
        } catch (Throwable th) {
        }
        STACK_TRACE_ELEMENT_CONSTRUCTOR = constructor;
    }

    public int tightMarshal1(OpenWireFormat wireFormat, Object o, BooleanStream bs) throws IOException {
        return 0;
    }

    public void tightMarshal2(OpenWireFormat wireFormat, Object o, DataOutput dataOut, BooleanStream bs) throws IOException {
    }

    public void tightUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn, BooleanStream bs) throws IOException {
    }

    public int tightMarshalLong1(OpenWireFormat wireFormat, long o, BooleanStream bs) throws IOException {
        if (o == 0) {
            bs.writeBoolean(false);
            bs.writeBoolean(false);
            return 0;
        } else if ((-65536 & o) == 0) {
            bs.writeBoolean(false);
            bs.writeBoolean(true);
            return 2;
        } else if ((-4294967296L & o) == 0) {
            bs.writeBoolean(true);
            bs.writeBoolean(false);
            return 4;
        } else {
            bs.writeBoolean(true);
            bs.writeBoolean(true);
            return 8;
        }
    }

    public void tightMarshalLong2(OpenWireFormat wireFormat, long o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            if (bs.readBoolean()) {
                dataOut.writeLong(o);
            } else {
                dataOut.writeInt((int) o);
            }
        } else if (bs.readBoolean()) {
            dataOut.writeShort((int) o);
        }
    }

    public long tightUnmarshalLong(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            if (bs.readBoolean()) {
                return dataIn.readLong();
            }
            return toLong(dataIn.readInt());
        } else if (bs.readBoolean()) {
            return toLong(dataIn.readShort());
        } else {
            return 0;
        }
    }

    protected long toLong(short value) {
        return 65535 & ((long) value);
    }

    protected long toLong(int value) {
        return LongString.MAX_LENGTH & ((long) value);
    }

    protected DataStructure tightUnmarsalNestedObject(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        return wireFormat.tightUnmarshalNestedObject(dataIn, bs);
    }

    protected int tightMarshalNestedObject1(OpenWireFormat wireFormat, DataStructure o, BooleanStream bs) throws IOException {
        return wireFormat.tightMarshalNestedObject1(o, bs);
    }

    protected void tightMarshalNestedObject2(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut, BooleanStream bs) throws IOException {
        wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
    }

    protected DataStructure tightUnmarsalCachedObject(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (!wireFormat.isCacheEnabled()) {
            return wireFormat.tightUnmarshalNestedObject(dataIn, bs);
        }
        if (!bs.readBoolean()) {
            return wireFormat.getFromUnmarshallCache(dataIn.readShort());
        }
        short index = dataIn.readShort();
        DataStructure object = wireFormat.tightUnmarshalNestedObject(dataIn, bs);
        wireFormat.setInUnmarshallCache(index, object);
        return object;
    }

    protected int tightMarshalCachedObject1(OpenWireFormat wireFormat, DataStructure o, BooleanStream bs) throws IOException {
        if (!wireFormat.isCacheEnabled()) {
            return wireFormat.tightMarshalNestedObject1(o, bs);
        }
        Short index = wireFormat.getMarshallCacheIndex(o);
        bs.writeBoolean(index == null);
        if (index != null) {
            return 2;
        }
        int rc = wireFormat.tightMarshalNestedObject1(o, bs);
        wireFormat.addToMarshallCache(o);
        return rc + 2;
    }

    protected void tightMarshalCachedObject2(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            Short index = wireFormat.getMarshallCacheIndex(o);
            if (bs.readBoolean()) {
                dataOut.writeShort(index.shortValue());
                wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
                return;
            }
            dataOut.writeShort(index.shortValue());
            return;
        }
        wireFormat.tightMarshalNestedObject2(o, dataOut, bs);
    }

    protected Throwable tightUnmarsalThrowable(OpenWireFormat wireFormat, DataInput dataIn, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        }
        Throwable o = createThrowable(tightUnmarshalString(dataIn, bs), tightUnmarshalString(dataIn, bs));
        if (!wireFormat.isStackTraceEnabled()) {
            return o;
        }
        if (STACK_TRACE_ELEMENT_CONSTRUCTOR != null) {
            StackTraceElement[] ss = new StackTraceElement[dataIn.readShort()];
            for (int i = 0; i < ss.length; i++) {
                try {
                    ss[i] = (StackTraceElement) STACK_TRACE_ELEMENT_CONSTRUCTOR.newInstance(new Object[]{tightUnmarshalString(dataIn, bs), tightUnmarshalString(dataIn, bs), tightUnmarshalString(dataIn, bs), Integer.valueOf(dataIn.readInt())});
                } catch (IOException e) {
                    throw e;
                } catch (Throwable th) {
                }
            }
            o.setStackTrace(ss);
        } else {
            short size = dataIn.readShort();
            for (short i2 = (short) 0; i2 < size; i2++) {
                tightUnmarshalString(dataIn, bs);
                tightUnmarshalString(dataIn, bs);
                tightUnmarshalString(dataIn, bs);
                dataIn.readInt();
            }
        }
        o.initCause(tightUnmarsalThrowable(wireFormat, dataIn, bs));
        return o;
    }

    private Throwable createThrowable(String className, String message) {
        try {
            return (Throwable) Class.forName(className, false, BaseDataStreamMarshaller.class.getClassLoader()).getConstructor(new Class[]{String.class}).newInstance(new Object[]{message});
        } catch (Throwable th) {
            return new Throwable(className + ": " + message);
        }
    }

    protected int tightMarshalThrowable1(OpenWireFormat wireFormat, Throwable o, BooleanStream bs) throws IOException {
        if (o == null) {
            bs.writeBoolean(false);
            return 0;
        }
        bs.writeBoolean(true);
        int rc = (0 + tightMarshalString1(o.getClass().getName(), bs)) + tightMarshalString1(o.getMessage(), bs);
        if (!wireFormat.isStackTraceEnabled()) {
            return rc;
        }
        rc += 2;
        StackTraceElement[] stackTrace = o.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            rc = (((rc + tightMarshalString1(element.getClassName(), bs)) + tightMarshalString1(element.getMethodName(), bs)) + tightMarshalString1(element.getFileName(), bs)) + 4;
        }
        return rc + tightMarshalThrowable1(wireFormat, o.getCause(), bs);
    }

    protected void tightMarshalThrowable2(OpenWireFormat wireFormat, Throwable o, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            tightMarshalString2(o.getClass().getName(), dataOut, bs);
            tightMarshalString2(o.getMessage(), dataOut, bs);
            if (wireFormat.isStackTraceEnabled()) {
                StackTraceElement[] stackTrace = o.getStackTrace();
                dataOut.writeShort(stackTrace.length);
                for (StackTraceElement element : stackTrace) {
                    tightMarshalString2(element.getClassName(), dataOut, bs);
                    tightMarshalString2(element.getMethodName(), dataOut, bs);
                    tightMarshalString2(element.getFileName(), dataOut, bs);
                    dataOut.writeInt(element.getLineNumber());
                }
                tightMarshalThrowable2(wireFormat, o.getCause(), dataOut, bs);
            }
        }
    }

    protected String tightUnmarshalString(DataInput dataIn, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        }
        if (!bs.readBoolean()) {
            return dataIn.readUTF();
        }
        byte[] data = new byte[dataIn.readShort()];
        dataIn.readFully(data);
        return new String(data, 0);
    }

    protected int tightMarshalString1(String value, BooleanStream bs) throws IOException {
        boolean z;
        if (value != null) {
            z = true;
        } else {
            z = false;
        }
        bs.writeBoolean(z);
        if (value == null) {
            return 0;
        }
        int strlen = value.length();
        int utflen = 0;
        char[] charr = new char[strlen];
        boolean isOnlyAscii = true;
        value.getChars(0, strlen, charr, 0);
        for (int i = 0; i < strlen; i++) {
            int c = charr[i];
            if (c >= 1 && c <= Service.LOCUS_CON) {
                utflen++;
            } else if (c > 2047) {
                utflen += 3;
                isOnlyAscii = false;
            } else {
                isOnlyAscii = false;
                utflen += 2;
            }
        }
        if (utflen >= ActiveMQPrefetchPolicy.MAX_PREFETCH_SIZE) {
            throw new IOException("Encountered a String value that is too long to encode.");
        }
        bs.writeBoolean(isOnlyAscii);
        return utflen + 2;
    }

    protected void tightMarshalString2(String value, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return;
        }
        if (bs.readBoolean()) {
            dataOut.writeShort(value.length());
            dataOut.writeBytes(value);
            return;
        }
        dataOut.writeUTF(value);
    }

    protected int tightMarshalObjectArray1(OpenWireFormat wireFormat, DataStructure[] objects, BooleanStream bs) throws IOException {
        int i = 0;
        if (objects != null) {
            bs.writeBoolean(true);
            i = 0 + 2;
            for (DataStructure tightMarshalNestedObject1 : objects) {
                i += tightMarshalNestedObject1(wireFormat, tightMarshalNestedObject1, bs);
            }
        } else {
            bs.writeBoolean(false);
        }
        return i;
    }

    protected void tightMarshalObjectArray2(OpenWireFormat wireFormat, DataStructure[] objects, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeShort(objects.length);
            for (DataStructure tightMarshalNestedObject2 : objects) {
                tightMarshalNestedObject2(wireFormat, tightMarshalNestedObject2, dataOut, bs);
            }
        }
    }

    protected int tightMarshalConstByteArray1(byte[] data, BooleanStream bs, int i) throws IOException {
        return i;
    }

    protected void tightMarshalConstByteArray2(byte[] data, DataOutput dataOut, BooleanStream bs, int i) throws IOException {
        dataOut.write(data, 0, i);
    }

    protected byte[] tightUnmarshalConstByteArray(DataInput dataIn, BooleanStream bs, int i) throws IOException {
        byte[] data = new byte[i];
        dataIn.readFully(data);
        return data;
    }

    protected int tightMarshalByteArray1(byte[] data, BooleanStream bs) throws IOException {
        boolean z;
        if (data != null) {
            z = true;
        } else {
            z = false;
        }
        bs.writeBoolean(z);
        if (data != null) {
            return data.length + 4;
        }
        return 0;
    }

    protected void tightMarshalByteArray2(byte[] data, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeInt(data.length);
            dataOut.write(data);
        }
    }

    protected byte[] tightUnmarshalByteArray(DataInput dataIn, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        }
        byte[] rc = new byte[dataIn.readInt()];
        dataIn.readFully(rc);
        return rc;
    }

    protected int tightMarshalByteSequence1(ByteSequence data, BooleanStream bs) throws IOException {
        boolean z;
        if (data != null) {
            z = true;
        } else {
            z = false;
        }
        bs.writeBoolean(z);
        if (data != null) {
            return data.getLength() + 4;
        }
        return 0;
    }

    protected void tightMarshalByteSequence2(ByteSequence data, DataOutput dataOut, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            dataOut.writeInt(data.getLength());
            dataOut.write(data.getData(), data.getOffset(), data.getLength());
        }
    }

    protected ByteSequence tightUnmarshalByteSequence(DataInput dataIn, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        }
        int size = dataIn.readInt();
        byte[] t = new byte[size];
        dataIn.readFully(t);
        return new ByteSequence(t, 0, size);
    }

    public void looseMarshal(OpenWireFormat wireFormat, Object o, DataOutput dataOut) throws IOException {
    }

    public void looseUnmarshal(OpenWireFormat wireFormat, Object o, DataInput dataIn) throws IOException {
    }

    public void looseMarshalLong(OpenWireFormat wireFormat, long o, DataOutput dataOut) throws IOException {
        dataOut.writeLong(o);
    }

    public long looseUnmarshalLong(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        return dataIn.readLong();
    }

    protected DataStructure looseUnmarsalNestedObject(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        return wireFormat.looseUnmarshalNestedObject(dataIn);
    }

    protected void looseMarshalNestedObject(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut) throws IOException {
        wireFormat.looseMarshalNestedObject(o, dataOut);
    }

    protected DataStructure looseUnmarsalCachedObject(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        if (!wireFormat.isCacheEnabled()) {
            return wireFormat.looseUnmarshalNestedObject(dataIn);
        }
        if (!dataIn.readBoolean()) {
            return wireFormat.getFromUnmarshallCache(dataIn.readShort());
        }
        short index = dataIn.readShort();
        DataStructure object = wireFormat.looseUnmarshalNestedObject(dataIn);
        wireFormat.setInUnmarshallCache(index, object);
        return object;
    }

    protected void looseMarshalCachedObject(OpenWireFormat wireFormat, DataStructure o, DataOutput dataOut) throws IOException {
        if (wireFormat.isCacheEnabled()) {
            Short index = wireFormat.getMarshallCacheIndex(o);
            dataOut.writeBoolean(index == null);
            if (index == null) {
                dataOut.writeShort(wireFormat.addToMarshallCache(o).shortValue());
                wireFormat.looseMarshalNestedObject(o, dataOut);
                return;
            }
            dataOut.writeShort(index.shortValue());
            return;
        }
        wireFormat.looseMarshalNestedObject(o, dataOut);
    }

    protected Throwable looseUnmarsalThrowable(OpenWireFormat wireFormat, DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        }
        Throwable o = createThrowable(looseUnmarshalString(dataIn), looseUnmarshalString(dataIn));
        if (!wireFormat.isStackTraceEnabled()) {
            return o;
        }
        if (STACK_TRACE_ELEMENT_CONSTRUCTOR != null) {
            StackTraceElement[] ss = new StackTraceElement[dataIn.readShort()];
            for (int i = 0; i < ss.length; i++) {
                try {
                    ss[i] = (StackTraceElement) STACK_TRACE_ELEMENT_CONSTRUCTOR.newInstance(new Object[]{looseUnmarshalString(dataIn), looseUnmarshalString(dataIn), looseUnmarshalString(dataIn), Integer.valueOf(dataIn.readInt())});
                } catch (IOException e) {
                    throw e;
                } catch (Throwable th) {
                }
            }
            o.setStackTrace(ss);
        } else {
            short size = dataIn.readShort();
            for (short i2 = (short) 0; i2 < size; i2++) {
                looseUnmarshalString(dataIn);
                looseUnmarshalString(dataIn);
                looseUnmarshalString(dataIn);
                dataIn.readInt();
            }
        }
        o.initCause(looseUnmarsalThrowable(wireFormat, dataIn));
        return o;
    }

    protected void looseMarshalThrowable(OpenWireFormat wireFormat, Throwable o, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(o != null);
        if (o != null) {
            looseMarshalString(o.getClass().getName(), dataOut);
            looseMarshalString(o.getMessage(), dataOut);
            if (wireFormat.isStackTraceEnabled()) {
                StackTraceElement[] stackTrace = o.getStackTrace();
                dataOut.writeShort(stackTrace.length);
                for (StackTraceElement element : stackTrace) {
                    looseMarshalString(element.getClassName(), dataOut);
                    looseMarshalString(element.getMethodName(), dataOut);
                    looseMarshalString(element.getFileName(), dataOut);
                    dataOut.writeInt(element.getLineNumber());
                }
                looseMarshalThrowable(wireFormat, o.getCause(), dataOut);
            }
        }
    }

    protected String looseUnmarshalString(DataInput dataIn) throws IOException {
        if (dataIn.readBoolean()) {
            return dataIn.readUTF();
        }
        return null;
    }

    protected void looseMarshalString(String value, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(value != null);
        if (value != null) {
            dataOut.writeUTF(value);
        }
    }

    protected void looseMarshalObjectArray(OpenWireFormat wireFormat, DataStructure[] objects, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(objects != null);
        if (objects != null) {
            dataOut.writeShort(objects.length);
            for (DataStructure looseMarshalNestedObject : objects) {
                looseMarshalNestedObject(wireFormat, looseMarshalNestedObject, dataOut);
            }
        }
    }

    protected void looseMarshalConstByteArray(OpenWireFormat wireFormat, byte[] data, DataOutput dataOut, int i) throws IOException {
        dataOut.write(data, 0, i);
    }

    protected byte[] looseUnmarshalConstByteArray(DataInput dataIn, int i) throws IOException {
        byte[] data = new byte[i];
        dataIn.readFully(data);
        return data;
    }

    protected void looseMarshalByteArray(OpenWireFormat wireFormat, byte[] data, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(data != null);
        if (data != null) {
            dataOut.writeInt(data.length);
            dataOut.write(data);
        }
    }

    protected byte[] looseUnmarshalByteArray(DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        }
        byte[] rc = new byte[dataIn.readInt()];
        dataIn.readFully(rc);
        return rc;
    }

    protected void looseMarshalByteSequence(OpenWireFormat wireFormat, ByteSequence data, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(data != null);
        if (data != null) {
            dataOut.writeInt(data.getLength());
            dataOut.write(data.getData(), data.getOffset(), data.getLength());
        }
    }

    protected ByteSequence looseUnmarshalByteSequence(DataInput dataIn) throws IOException {
        if (!dataIn.readBoolean()) {
            return null;
        }
        int size = dataIn.readInt();
        byte[] t = new byte[size];
        dataIn.readFully(t);
        return new ByteSequence(t, 0, size);
    }
}
