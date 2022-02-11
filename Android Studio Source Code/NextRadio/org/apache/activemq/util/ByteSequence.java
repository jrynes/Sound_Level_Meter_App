package org.apache.activemq.util;

public class ByteSequence {
    public byte[] data;
    public int length;
    public int offset;

    public ByteSequence(byte[] data) {
        this.data = data;
        this.offset = 0;
        this.length = data.length;
    }

    public ByteSequence(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getLength() {
        return this.length;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void compact() {
        if (this.length != this.data.length) {
            byte[] t = new byte[this.length];
            System.arraycopy(this.data, this.offset, t, 0, this.length);
            this.data = t;
            this.offset = 0;
        }
    }

    public int indexOf(ByteSequence needle, int pos) {
        int max = this.length - needle.length;
        for (int i = pos; i < max; i++) {
            if (matches(needle, i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean matches(ByteSequence needle, int pos) {
        for (int i = 0; i < needle.length; i++) {
            if (this.data[(this.offset + pos) + i] != needle.data[needle.offset + i]) {
                return false;
            }
        }
        return true;
    }

    private byte getByte(int i) {
        return this.data[this.offset + i];
    }

    public final int indexOf(byte value, int pos) {
        for (int i = pos; i < this.length; i++) {
            if (this.data[this.offset + i] == value) {
                return i;
            }
        }
        return -1;
    }
}
