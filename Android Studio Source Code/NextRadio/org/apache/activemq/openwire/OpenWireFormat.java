package org.apache.activemq.openwire;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.ByteSequenceData;
import org.apache.activemq.util.DataByteArrayInputStream;
import org.apache.activemq.util.DataByteArrayOutputStream;
import org.apache.activemq.wireformat.WireFormat;
import org.xbill.DNS.Type;

public final class OpenWireFormat implements WireFormat {
    public static final long DEFAULT_MAX_FRAME_SIZE = Long.MAX_VALUE;
    public static final int DEFAULT_VERSION = 6;
    public static final int DEFAULT_WIRE_VERSION = 9;
    private static final int MARSHAL_CACHE_FREE_SPACE = 100;
    private static final int MARSHAL_CACHE_SIZE = 16383;
    static final byte NULL_TYPE = (byte) 0;
    private DataByteArrayInputStream bytesIn;
    private DataByteArrayOutputStream bytesOut;
    private boolean cacheEnabled;
    private DataStreamMarshaller[] dataMarshallers;
    private DataStructure[] marshallCache;
    private Map<DataStructure, Short> marshallCacheMap;
    private long maxFrameSize;
    private short nextMarshallCacheEvictionIndex;
    private short nextMarshallCacheIndex;
    private WireFormatInfo preferedWireFormatInfo;
    private boolean sizePrefixDisabled;
    private boolean stackTraceEnabled;
    private boolean tcpNoDelayEnabled;
    private boolean tightEncodingEnabled;
    private DataStructure[] unmarshallCache;
    private int version;

    public OpenWireFormat() {
        this(DEFAULT_VERSION);
    }

    public OpenWireFormat(int i) {
        this.maxFrameSize = DEFAULT_MAX_FRAME_SIZE;
        this.marshallCacheMap = new HashMap();
        this.marshallCache = null;
        this.unmarshallCache = null;
        this.bytesOut = new DataByteArrayOutputStream();
        this.bytesIn = new DataByteArrayInputStream();
        setVersion(i);
    }

    public int hashCode() {
        return (this.sizePrefixDisabled ? AccessibilityNodeInfoCompat.ACTION_CUT : AccessibilityNodeInfoCompat.ACTION_SET_SELECTION) ^ (((this.version ^ (this.cacheEnabled ? 268435456 : 536870912)) ^ (this.stackTraceEnabled ? ViewCompat.MEASURED_STATE_TOO_SMALL : 33554432)) ^ (this.tightEncodingEnabled ? AccessibilityNodeInfoCompat.ACTION_DISMISS : AccessibilityNodeInfoCompat.ACTION_SET_TEXT));
    }

    public OpenWireFormat copy() {
        OpenWireFormat answer = new OpenWireFormat(this.version);
        answer.stackTraceEnabled = this.stackTraceEnabled;
        answer.tcpNoDelayEnabled = this.tcpNoDelayEnabled;
        answer.cacheEnabled = this.cacheEnabled;
        answer.tightEncodingEnabled = this.tightEncodingEnabled;
        answer.sizePrefixDisabled = this.sizePrefixDisabled;
        answer.preferedWireFormatInfo = this.preferedWireFormatInfo;
        return answer;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        OpenWireFormat o = (OpenWireFormat) object;
        if (o.stackTraceEnabled == this.stackTraceEnabled && o.cacheEnabled == this.cacheEnabled && o.version == this.version && o.tightEncodingEnabled == this.tightEncodingEnabled && o.sizePrefixDisabled == this.sizePrefixDisabled) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "OpenWireFormat{version=" + this.version + ", cacheEnabled=" + this.cacheEnabled + ", stackTraceEnabled=" + this.stackTraceEnabled + ", tightEncodingEnabled=" + this.tightEncodingEnabled + ", sizePrefixDisabled=" + this.sizePrefixDisabled + ", maxFrameSize=" + this.maxFrameSize + "}";
    }

    public int getVersion() {
        return this.version;
    }

    public synchronized ByteSequence marshal(Object command) throws IOException {
        ByteSequence sequence;
        if (this.cacheEnabled) {
            runMarshallCacheEvictionSweep();
        }
        sequence = null;
        if (null == null) {
            if (command != null) {
                DataStructure c = (DataStructure) command;
                byte type = c.getDataStructureType();
                DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
                if (dsm == null) {
                    throw new IOException("Unknown data type: " + type);
                } else if (this.tightEncodingEnabled) {
                    BooleanStream bs = new BooleanStream();
                    size = (1 + dsm.tightMarshal1(this, c, bs)) + bs.marshalledSize();
                    this.bytesOut.restart(size);
                    if (!this.sizePrefixDisabled) {
                        this.bytesOut.writeInt(size);
                    }
                    this.bytesOut.writeByte(type);
                    bs.marshal(this.bytesOut);
                    dsm.tightMarshal2(this, c, this.bytesOut, bs);
                    sequence = this.bytesOut.toByteSequence();
                } else {
                    this.bytesOut.restart();
                    if (!this.sizePrefixDisabled) {
                        this.bytesOut.writeInt(0);
                    }
                    this.bytesOut.writeByte(type);
                    dsm.looseMarshal(this, c, this.bytesOut);
                    sequence = this.bytesOut.toByteSequence();
                    if (!this.sizePrefixDisabled) {
                        size = sequence.getLength() - 4;
                        int pos = sequence.offset;
                        ByteSequenceData.writeIntBig(sequence, size);
                        sequence.offset = pos;
                    }
                }
            } else {
                this.bytesOut.restart(5);
                this.bytesOut.writeInt(1);
                this.bytesOut.writeByte(0);
                sequence = this.bytesOut.toByteSequence();
            }
        }
        return sequence;
    }

    public synchronized Object unmarshal(ByteSequence sequence) throws IOException {
        this.bytesIn.restart(sequence);
        if (!this.sizePrefixDisabled) {
            int size = this.bytesIn.readInt();
            if (sequence.getLength() - 4 != size) {
            }
            if (((long) size) > this.maxFrameSize) {
                throw new IOException("Frame size of " + (size / AccessibilityNodeInfoCompat.ACTION_DISMISS) + " MB larger than max allowed " + (this.maxFrameSize / 1048576) + " MB");
            }
        }
        return doUnmarshal(this.bytesIn);
    }

    public synchronized void marshal(Object o, DataOutput dataOut) throws IOException {
        if (this.cacheEnabled) {
            runMarshallCacheEvictionSweep();
        }
        if (o != null) {
            DataStructure c = (DataStructure) o;
            byte type = c.getDataStructureType();
            DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
            if (dsm == null) {
                throw new IOException("Unknown data type: " + type);
            } else if (this.tightEncodingEnabled) {
                BooleanStream bs = new BooleanStream();
                int size = (1 + dsm.tightMarshal1(this, c, bs)) + bs.marshalledSize();
                if (!this.sizePrefixDisabled) {
                    dataOut.writeInt(size);
                }
                dataOut.writeByte(type);
                bs.marshal(dataOut);
                dsm.tightMarshal2(this, c, dataOut, bs);
            } else {
                DataOutput looseOut = dataOut;
                if (!this.sizePrefixDisabled) {
                    this.bytesOut.restart();
                    looseOut = this.bytesOut;
                }
                looseOut.writeByte(type);
                dsm.looseMarshal(this, c, looseOut);
                if (!this.sizePrefixDisabled) {
                    ByteSequence sequence = this.bytesOut.toByteSequence();
                    dataOut.writeInt(sequence.getLength());
                    dataOut.write(sequence.getData(), sequence.getOffset(), sequence.getLength());
                }
            }
        } else {
            if (!this.sizePrefixDisabled) {
                dataOut.writeInt(1);
            }
            dataOut.writeByte(0);
        }
    }

    public Object unmarshal(DataInput dis) throws IOException {
        DataInput dataIn = dis;
        if (!this.sizePrefixDisabled) {
            int size = dis.readInt();
            if (((long) size) > this.maxFrameSize) {
                throw new IOException("Frame size of " + (size / AccessibilityNodeInfoCompat.ACTION_DISMISS) + " MB larger than max allowed " + (this.maxFrameSize / 1048576) + " MB");
            }
        }
        return doUnmarshal(dataIn);
    }

    public int tightMarshal1(Object o, BooleanStream bs) throws IOException {
        if (o == null) {
            return 1;
        }
        DataStructure c = (DataStructure) o;
        byte type = c.getDataStructureType();
        DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
        if (dsm != null) {
            return (1 + dsm.tightMarshal1(this, c, bs)) + bs.marshalledSize();
        }
        throw new IOException("Unknown data type: " + type);
    }

    public void tightMarshal2(Object o, DataOutput ds, BooleanStream bs) throws IOException {
        if (this.cacheEnabled) {
            runMarshallCacheEvictionSweep();
        }
        if (o != null) {
            DataStructure c = (DataStructure) o;
            byte type = c.getDataStructureType();
            DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
            if (dsm == null) {
                throw new IOException("Unknown data type: " + type);
            }
            ds.writeByte(type);
            bs.marshal(ds);
            dsm.tightMarshal2(this, c, ds, bs);
        }
    }

    public void setVersion(int version) {
        String mfName = "org.apache.activemq.openwire.v" + version + ".MarshallerFactory";
        try {
            try {
                this.dataMarshallers = (DataStreamMarshaller[]) Class.forName(mfName, false, getClass().getClassLoader()).getMethod("createMarshallerMap", new Class[]{OpenWireFormat.class}).invoke(null, new Object[]{this});
                this.version = version;
            } catch (Throwable e) {
                IllegalArgumentException illegalArgumentException = (IllegalArgumentException) new IllegalArgumentException("Invalid version: " + version + ", " + mfName + " does not properly implement the createMarshallerMap method.").initCause(e);
            }
        } catch (ClassNotFoundException e2) {
            throw ((IllegalArgumentException) new IllegalArgumentException("Invalid version: " + version + ", could not load " + mfName).initCause(e2));
        }
    }

    public Object doUnmarshal(DataInput dis) throws IOException {
        byte dataType = dis.readByte();
        if (dataType == null) {
            return null;
        }
        DataStreamMarshaller dsm = this.dataMarshallers[dataType & Type.ANY];
        if (dsm == null) {
            throw new IOException("Unknown data type: " + dataType);
        }
        DataStructure data = dsm.createObject();
        if (this.tightEncodingEnabled) {
            BooleanStream bs = new BooleanStream();
            bs.unmarshal(dis);
            dsm.tightUnmarshal(this, data, dis, bs);
            return data;
        }
        dsm.looseUnmarshal(this, data, dis);
        return data;
    }

    public int tightMarshalNestedObject1(DataStructure o, BooleanStream bs) throws IOException {
        boolean z = true;
        bs.writeBoolean(o != null);
        if (o == null) {
            return 0;
        }
        if (o.isMarshallAware()) {
            ByteSequence sequence = null;
            if (sequence == null) {
                z = false;
            }
            bs.writeBoolean(z);
            if (sequence != null) {
                return sequence.getLength() + 1;
            }
        }
        byte type = o.getDataStructureType();
        DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
        if (dsm != null) {
            return dsm.tightMarshal1(this, o, bs) + 1;
        }
        throw new IOException("Unknown data type: " + type);
    }

    public void tightMarshalNestedObject2(DataStructure o, DataOutput ds, BooleanStream bs) throws IOException {
        if (bs.readBoolean()) {
            byte type = o.getDataStructureType();
            ds.writeByte(type);
            if (o.isMarshallAware() && bs.readBoolean()) {
                throw new IOException("Corrupted stream");
            }
            DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
            if (dsm == null) {
                throw new IOException("Unknown data type: " + type);
            }
            dsm.tightMarshal2(this, o, ds, bs);
        }
    }

    public DataStructure tightUnmarshalNestedObject(DataInput dis, BooleanStream bs) throws IOException {
        if (!bs.readBoolean()) {
            return null;
        }
        byte dataType = dis.readByte();
        DataStreamMarshaller dsm = this.dataMarshallers[dataType & Type.ANY];
        if (dsm == null) {
            throw new IOException("Unknown data type: " + dataType);
        }
        DataStructure data = dsm.createObject();
        if (data.isMarshallAware() && bs.readBoolean()) {
            dis.readInt();
            dis.readByte();
            BooleanStream bs2 = new BooleanStream();
            bs2.unmarshal(dis);
            dsm.tightUnmarshal(this, data, dis, bs2);
            return data;
        }
        dsm.tightUnmarshal(this, data, dis, bs);
        return data;
    }

    public DataStructure looseUnmarshalNestedObject(DataInput dis) throws IOException {
        if (!dis.readBoolean()) {
            return null;
        }
        byte dataType = dis.readByte();
        DataStreamMarshaller dsm = this.dataMarshallers[dataType & Type.ANY];
        if (dsm == null) {
            throw new IOException("Unknown data type: " + dataType);
        }
        DataStructure data = dsm.createObject();
        dsm.looseUnmarshal(this, data, dis);
        return data;
    }

    public void looseMarshalNestedObject(DataStructure o, DataOutput dataOut) throws IOException {
        dataOut.writeBoolean(o != null);
        if (o != null) {
            byte type = o.getDataStructureType();
            dataOut.writeByte(type);
            DataStreamMarshaller dsm = this.dataMarshallers[type & Type.ANY];
            if (dsm == null) {
                throw new IOException("Unknown data type: " + type);
            }
            dsm.looseMarshal(this, o, dataOut);
        }
    }

    public void runMarshallCacheEvictionSweep() {
        while (this.marshallCacheMap.size() > this.marshallCache.length - 100) {
            this.marshallCacheMap.remove(this.marshallCache[this.nextMarshallCacheEvictionIndex]);
            this.marshallCache[this.nextMarshallCacheEvictionIndex] = null;
            this.nextMarshallCacheEvictionIndex = (short) (this.nextMarshallCacheEvictionIndex + 1);
            if (this.nextMarshallCacheEvictionIndex >= this.marshallCache.length) {
                this.nextMarshallCacheEvictionIndex = (short) 0;
            }
        }
    }

    public Short getMarshallCacheIndex(DataStructure o) {
        return (Short) this.marshallCacheMap.get(o);
    }

    public Short addToMarshallCache(DataStructure o) {
        short i = this.nextMarshallCacheIndex;
        this.nextMarshallCacheIndex = (short) (i + 1);
        if (this.nextMarshallCacheIndex >= this.marshallCache.length) {
            this.nextMarshallCacheIndex = (short) 0;
        }
        if (this.marshallCacheMap.size() >= this.marshallCache.length) {
            return new Short((short) -1);
        }
        this.marshallCache[i] = o;
        Short index = new Short(i);
        this.marshallCacheMap.put(o, index);
        return index;
    }

    public void setInUnmarshallCache(short index, DataStructure o) {
        if (index != (short) -1) {
            this.unmarshallCache[index] = o;
        }
    }

    public DataStructure getFromUnmarshallCache(short index) {
        return this.unmarshallCache[index];
    }

    public void setStackTraceEnabled(boolean b) {
        this.stackTraceEnabled = b;
    }

    public boolean isStackTraceEnabled() {
        return this.stackTraceEnabled;
    }

    public boolean isTcpNoDelayEnabled() {
        return this.tcpNoDelayEnabled;
    }

    public void setTcpNoDelayEnabled(boolean tcpNoDelayEnabled) {
        this.tcpNoDelayEnabled = tcpNoDelayEnabled;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        if (cacheEnabled) {
            this.marshallCache = new DataStructure[MARSHAL_CACHE_SIZE];
            this.unmarshallCache = new DataStructure[MARSHAL_CACHE_SIZE];
        }
        this.cacheEnabled = cacheEnabled;
    }

    public boolean isTightEncodingEnabled() {
        return this.tightEncodingEnabled;
    }

    public void setTightEncodingEnabled(boolean tightEncodingEnabled) {
        this.tightEncodingEnabled = tightEncodingEnabled;
    }

    public boolean isSizePrefixDisabled() {
        return this.sizePrefixDisabled;
    }

    public void setSizePrefixDisabled(boolean prefixPacketSize) {
        this.sizePrefixDisabled = prefixPacketSize;
    }

    public void setPreferedWireFormatInfo(WireFormatInfo info) {
        this.preferedWireFormatInfo = info;
    }

    public WireFormatInfo getPreferedWireFormatInfo() {
        return this.preferedWireFormatInfo;
    }

    public long getMaxFrameSize() {
        return this.maxFrameSize;
    }

    public void setMaxFrameSize(long maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    public void renegotiateWireFormat(WireFormatInfo info) throws IOException {
        boolean z = true;
        if (this.preferedWireFormatInfo == null) {
            throw new IllegalStateException("Wireformat cannot not be renegotiated.");
        }
        setVersion(min(this.preferedWireFormatInfo.getVersion(), info.getVersion()));
        info.setVersion(getVersion());
        setMaxFrameSize(min(this.preferedWireFormatInfo.getMaxFrameSize(), info.getMaxFrameSize()));
        info.setMaxFrameSize(getMaxFrameSize());
        boolean z2 = info.isStackTraceEnabled() && this.preferedWireFormatInfo.isStackTraceEnabled();
        this.stackTraceEnabled = z2;
        info.setStackTraceEnabled(this.stackTraceEnabled);
        if (info.isTcpNoDelayEnabled() && this.preferedWireFormatInfo.isTcpNoDelayEnabled()) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.tcpNoDelayEnabled = z2;
        info.setTcpNoDelayEnabled(this.tcpNoDelayEnabled);
        if (info.isCacheEnabled() && this.preferedWireFormatInfo.isCacheEnabled()) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.cacheEnabled = z2;
        info.setCacheEnabled(this.cacheEnabled);
        if (info.isTightEncodingEnabled() && this.preferedWireFormatInfo.isTightEncodingEnabled()) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.tightEncodingEnabled = z2;
        info.setTightEncodingEnabled(this.tightEncodingEnabled);
        if (!(info.isSizePrefixDisabled() && this.preferedWireFormatInfo.isSizePrefixDisabled())) {
            z = false;
        }
        this.sizePrefixDisabled = z;
        info.setSizePrefixDisabled(this.sizePrefixDisabled);
        if (this.cacheEnabled) {
            int size = Math.min(this.preferedWireFormatInfo.getCacheSize(), info.getCacheSize());
            info.setCacheSize(size);
            if (size == 0) {
                size = MARSHAL_CACHE_SIZE;
            }
            this.marshallCache = new DataStructure[size];
            this.unmarshallCache = new DataStructure[size];
            this.nextMarshallCacheIndex = (short) 0;
            this.nextMarshallCacheEvictionIndex = (short) 0;
            this.marshallCacheMap = new HashMap();
            return;
        }
        this.marshallCache = null;
        this.unmarshallCache = null;
        this.nextMarshallCacheIndex = (short) 0;
        this.nextMarshallCacheEvictionIndex = (short) 0;
        this.marshallCacheMap = null;
    }

    protected int min(int version1, int version2) {
        return ((version1 >= version2 || version1 <= 0) && version2 > 0) ? version2 : version1;
    }

    protected long min(long version1, long version2) {
        return ((version1 >= version2 || version1 <= 0) && version2 > 0) ? version2 : version1;
    }
}
