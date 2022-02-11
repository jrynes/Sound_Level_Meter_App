package org.apache.activemq.command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.activemq.util.ByteArrayOutputStream;
import org.apache.activemq.util.ByteSequence;
import org.apache.activemq.util.MarshallingSupport;
import org.apache.activemq.wireformat.WireFormat;

public class WireFormatInfo implements Command, MarshallAware {
    public static final byte DATA_STRUCTURE_TYPE = (byte) 1;
    private static final byte[] MAGIC;
    private static final int MAX_PROPERTY_SIZE = 4096;
    private transient Endpoint from;
    protected byte[] magic;
    protected ByteSequence marshalledProperties;
    protected transient Map<String, Object> properties;
    private transient Endpoint to;
    protected int version;

    public WireFormatInfo() {
        this.magic = MAGIC;
    }

    static {
        MAGIC = new byte[]{ReplayCommand.DATA_STRUCTURE_TYPE, (byte) 99, (byte) 116, (byte) 105, (byte) 118, CommandTypes.ACTIVEMQ_TOPIC, CommandTypes.STRING_TYPE, (byte) 81};
    }

    public byte getDataStructureType() {
        return DATA_STRUCTURE_TYPE;
    }

    public boolean isWireFormatInfo() {
        return true;
    }

    public boolean isMarshallAware() {
        return true;
    }

    public byte[] getMagic() {
        return this.magic;
    }

    public void setMagic(byte[] magic) {
        this.magic = magic;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ByteSequence getMarshalledProperties() {
        return this.marshalledProperties;
    }

    public void setMarshalledProperties(ByteSequence marshalledProperties) {
        this.marshalledProperties = marshalledProperties;
    }

    public Endpoint getFrom() {
        return this.from;
    }

    public void setFrom(Endpoint from) {
        this.from = from;
    }

    public Endpoint getTo() {
        return this.to;
    }

    public void setTo(Endpoint to) {
        this.to = to;
    }

    public Object getProperty(String name) throws IOException {
        if (this.properties == null) {
            if (this.marshalledProperties == null) {
                return null;
            }
            this.properties = unmarsallProperties(this.marshalledProperties);
        }
        return this.properties.get(name);
    }

    public Map<String, Object> getProperties() throws IOException {
        if (this.properties == null) {
            if (this.marshalledProperties == null) {
                return Collections.EMPTY_MAP;
            }
            this.properties = unmarsallProperties(this.marshalledProperties);
        }
        return Collections.unmodifiableMap(this.properties);
    }

    public void clearProperties() {
        this.marshalledProperties = null;
        this.properties = null;
    }

    public void setProperty(String name, Object value) throws IOException {
        lazyCreateProperties();
        this.properties.put(name, value);
    }

    protected void lazyCreateProperties() throws IOException {
        if (this.properties != null) {
            return;
        }
        if (this.marshalledProperties == null) {
            this.properties = new HashMap();
            return;
        }
        this.properties = unmarsallProperties(this.marshalledProperties);
        this.marshalledProperties = null;
    }

    private Map<String, Object> unmarsallProperties(ByteSequence marshalledProperties) throws IOException {
        return MarshallingSupport.unmarshalPrimitiveMap(new DataInputStream(new ByteArrayInputStream(marshalledProperties)), (int) MAX_PROPERTY_SIZE);
    }

    public void beforeMarshall(WireFormat wireFormat) throws IOException {
        if (this.marshalledProperties == null && this.properties != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(baos);
            MarshallingSupport.marshalPrimitiveMap(this.properties, os);
            os.close();
            this.marshalledProperties = baos.toByteSequence();
        }
    }

    public void afterMarshall(WireFormat wireFormat) throws IOException {
    }

    public void beforeUnmarshall(WireFormat wireFormat) throws IOException {
    }

    public void afterUnmarshall(WireFormat wireFormat) throws IOException {
    }

    public boolean isValid() {
        return this.magic != null && Arrays.equals(this.magic, MAGIC);
    }

    public void setResponseRequired(boolean responseRequired) {
    }

    public boolean isCacheEnabled() throws IOException {
        return Boolean.TRUE == getProperty("CacheEnabled");
    }

    public void setCacheEnabled(boolean cacheEnabled) throws IOException {
        setProperty("CacheEnabled", cacheEnabled ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isStackTraceEnabled() throws IOException {
        return Boolean.TRUE == getProperty("StackTraceEnabled");
    }

    public void setStackTraceEnabled(boolean stackTraceEnabled) throws IOException {
        setProperty("StackTraceEnabled", stackTraceEnabled ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isTcpNoDelayEnabled() throws IOException {
        return Boolean.TRUE == getProperty("TcpNoDelayEnabled");
    }

    public void setTcpNoDelayEnabled(boolean tcpNoDelayEnabled) throws IOException {
        setProperty("TcpNoDelayEnabled", tcpNoDelayEnabled ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isSizePrefixDisabled() throws IOException {
        return Boolean.TRUE == getProperty("SizePrefixDisabled");
    }

    public void setSizePrefixDisabled(boolean prefixPacketSize) throws IOException {
        setProperty("SizePrefixDisabled", prefixPacketSize ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isTightEncodingEnabled() throws IOException {
        return Boolean.TRUE == getProperty("TightEncodingEnabled");
    }

    public void setTightEncodingEnabled(boolean tightEncodingEnabled) throws IOException {
        setProperty("TightEncodingEnabled", tightEncodingEnabled ? Boolean.TRUE : Boolean.FALSE);
    }

    public long getMaxInactivityDuration() throws IOException {
        Long l = (Long) getProperty("MaxInactivityDuration");
        return l == null ? 0 : l.longValue();
    }

    public void setMaxInactivityDuration(long maxInactivityDuration) throws IOException {
        setProperty("MaxInactivityDuration", new Long(maxInactivityDuration));
    }

    public long getMaxInactivityDurationInitalDelay() throws IOException {
        Long l = (Long) getProperty("MaxInactivityDurationInitalDelay");
        return l == null ? 0 : l.longValue();
    }

    public void setMaxInactivityDurationInitalDelay(long maxInactivityDurationInitalDelay) throws IOException {
        setProperty("MaxInactivityDurationInitalDelay", new Long(maxInactivityDurationInitalDelay));
    }

    public long getMaxFrameSize() throws IOException {
        Long l = (Long) getProperty("MaxFrameSize");
        return l == null ? 0 : l.longValue();
    }

    public void setMaxFrameSize(long maxFrameSize) throws IOException {
        setProperty("MaxFrameSize", new Long(maxFrameSize));
    }

    public int getCacheSize() throws IOException {
        Integer i = (Integer) getProperty("CacheSize");
        return i == null ? 0 : i.intValue();
    }

    public void setCacheSize(int cacheSize) throws IOException {
        setProperty("CacheSize", new Integer(cacheSize));
    }

    public Response visit(CommandVisitor visitor) throws Exception {
        return visitor.processWireFormat(this);
    }

    public String toString() {
        Map<String, Object> p = null;
        try {
            p = getProperties();
        } catch (IOException e) {
        }
        return "WireFormatInfo { version=" + this.version + ", properties=" + p + ", magic=" + toString(this.magic) + "}";
    }

    private String toString(byte[] data) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                sb.append(ActiveMQDestination.COMPOSITE_SEPERATOR);
            }
            sb.append((char) data[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public void setCommandId(int value) {
    }

    public int getCommandId() {
        return 0;
    }

    public boolean isResponseRequired() {
        return false;
    }

    public boolean isResponse() {
        return false;
    }

    public boolean isBrokerInfo() {
        return false;
    }

    public boolean isMessageDispatch() {
        return false;
    }

    public boolean isMessage() {
        return false;
    }

    public boolean isMessageAck() {
        return false;
    }

    public boolean isMessageDispatchNotification() {
        return false;
    }

    public boolean isShutdownInfo() {
        return false;
    }

    public boolean isConnectionControl() {
        return false;
    }

    public void setCachedMarshalledForm(WireFormat wireFormat, ByteSequence data) {
    }

    public ByteSequence getCachedMarshalledForm(WireFormat wireFormat) {
        return null;
    }
}
