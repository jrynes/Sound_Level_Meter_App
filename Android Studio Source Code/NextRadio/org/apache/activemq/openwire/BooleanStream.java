package org.apache.activemq.openwire;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;

public final class BooleanStream {
    static final /* synthetic */ boolean $assertionsDisabled;
    short arrayLimit;
    short arrayPos;
    byte bytePos;
    byte[] data;

    static {
        $assertionsDisabled = !BooleanStream.class.desiredAssertionStatus();
    }

    public BooleanStream() {
        this.data = new byte[48];
    }

    public boolean readBoolean() throws IOException {
        if ($assertionsDisabled || this.arrayPos <= this.arrayLimit) {
            boolean rc;
            if (((this.data[this.arrayPos] >> this.bytePos) & 1) != 0) {
                rc = true;
            } else {
                rc = false;
            }
            this.bytePos = (byte) (this.bytePos + 1);
            if (this.bytePos >= 8) {
                this.bytePos = (byte) 0;
                this.arrayPos = (short) (this.arrayPos + 1);
            }
            return rc;
        }
        throw new AssertionError();
    }

    public void writeBoolean(boolean value) throws IOException {
        if (this.bytePos == null) {
            this.arrayLimit = (short) (this.arrayLimit + 1);
            if (this.arrayLimit >= this.data.length) {
                byte[] d = new byte[(this.data.length * 2)];
                System.arraycopy(this.data, 0, d, 0, this.data.length);
                this.data = d;
            }
        }
        if (value) {
            byte[] bArr = this.data;
            short s = this.arrayPos;
            bArr[s] = (byte) (bArr[s] | (1 << this.bytePos));
        }
        this.bytePos = (byte) (this.bytePos + 1);
        if (this.bytePos >= 8) {
            this.bytePos = (byte) 0;
            this.arrayPos = (short) (this.arrayPos + 1);
        }
    }

    public void marshal(DataOutput dataOut) throws IOException {
        if (this.arrayLimit < (short) 64) {
            dataOut.writeByte(this.arrayLimit);
        } else if (this.arrayLimit < (short) 256) {
            dataOut.writeByte(192);
            dataOut.writeByte(this.arrayLimit);
        } else {
            dataOut.writeByte(Flags.FLAG8);
            dataOut.writeShort(this.arrayLimit);
        }
        dataOut.write(this.data, 0, this.arrayLimit);
        clear();
    }

    public void marshal(ByteBuffer dataOut) {
        if (this.arrayLimit < (short) 64) {
            dataOut.put((byte) this.arrayLimit);
        } else if (this.arrayLimit < (short) 256) {
            dataOut.put((byte) -64);
            dataOut.put((byte) this.arrayLimit);
        } else {
            dataOut.put(Byte.MIN_VALUE);
            dataOut.putShort(this.arrayLimit);
        }
        dataOut.put(this.data, 0, this.arrayLimit);
    }

    public void unmarshal(DataInput dataIn) throws IOException {
        this.arrayLimit = (short) (dataIn.readByte() & Type.ANY);
        if (this.arrayLimit == (short) 192) {
            this.arrayLimit = (short) (dataIn.readByte() & Type.ANY);
        } else if (this.arrayLimit == (short) 128) {
            this.arrayLimit = dataIn.readShort();
        }
        if (this.data.length < this.arrayLimit) {
            this.data = new byte[this.arrayLimit];
        }
        dataIn.readFully(this.data, 0, this.arrayLimit);
        clear();
    }

    public void clear() {
        this.arrayPos = (short) 0;
        this.bytePos = (byte) 0;
    }

    public int marshalledSize() {
        if (this.arrayLimit < (short) 64) {
            return this.arrayLimit + 1;
        }
        if (this.arrayLimit < (short) 256) {
            return this.arrayLimit + 2;
        }
        return this.arrayLimit + 3;
    }
}
