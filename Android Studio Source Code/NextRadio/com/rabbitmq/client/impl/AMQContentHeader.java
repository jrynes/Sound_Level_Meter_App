package com.rabbitmq.client.impl;

import com.rabbitmq.client.ContentHeader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.activemq.filter.DestinationFilter;

public abstract class AMQContentHeader implements ContentHeader {
    private long bodySize;

    public abstract void writePropertiesTo(ContentHeaderPropertyWriter contentHeaderPropertyWriter) throws IOException;

    protected AMQContentHeader() {
        this.bodySize = 0;
    }

    protected AMQContentHeader(DataInputStream in) throws IOException {
        in.readShort();
        this.bodySize = in.readLong();
    }

    public long getBodySize() {
        return this.bodySize;
    }

    private void writeTo(DataOutputStream out, long bodySize) throws IOException {
        out.writeShort(0);
        out.writeLong(bodySize);
        writePropertiesTo(new ContentHeaderPropertyWriter(out));
    }

    public void appendPropertyDebugStringTo(StringBuilder acc) {
        acc.append("(?)");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#contentHeader<").append(getClassName()).append(DestinationFilter.ANY_DESCENDENT);
        appendPropertyDebugStringTo(sb);
        return sb.toString();
    }

    public Frame toFrame(int channelNumber, long bodySize) throws IOException {
        Frame frame = new Frame(2, channelNumber);
        DataOutputStream bodyOut = frame.getOutputStream();
        bodyOut.writeShort(getClassId());
        writeTo(bodyOut, bodySize);
        return frame;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
