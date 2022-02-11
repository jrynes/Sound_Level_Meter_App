package org.apache.activemq.util;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BitArray implements Serializable {
    private static final long[] BIT_VALUES;
    static final int BYTE_SIZE = 8;
    static final int INT_SIZE = 32;
    static final int LONG_SIZE = 64;
    static final int SHORT_SIZE = 16;
    private static final long serialVersionUID = 1;
    private long bits;
    private int length;

    static {
        BIT_VALUES = new long[]{serialVersionUID, 2, 4, 8, 16, 32, 64, 128, 256, 512, PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM, PlaybackStateCompat.ACTION_PLAY_FROM_URI, PlaybackStateCompat.ACTION_PREPARE, PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID, PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH, PlaybackStateCompat.ACTION_PREPARE_FROM_URI, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};
    }

    public int length() {
        return this.length;
    }

    public long getBits() {
        return this.bits;
    }

    public boolean set(int index, boolean flag) {
        this.length = Math.max(this.length, index + 1);
        boolean oldValue = (this.bits & BIT_VALUES[index]) != 0;
        if (flag) {
            this.bits |= BIT_VALUES[index];
        } else if (oldValue) {
            this.bits &= BIT_VALUES[index] ^ -1;
        }
        return oldValue;
    }

    public boolean get(int index) {
        return (this.bits & BIT_VALUES[index]) != 0;
    }

    public void reset() {
        this.bits = 0;
    }

    public void reset(long bits) {
        this.bits = bits;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeToStream(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readFromStream(in);
    }

    public void writeToStream(DataOutput dataOut) throws IOException {
        dataOut.writeByte(this.length);
        if (this.length <= BYTE_SIZE) {
            dataOut.writeByte((byte) ((int) this.bits));
        } else if (this.length <= SHORT_SIZE) {
            dataOut.writeShort((short) ((int) this.bits));
        } else if (this.length <= INT_SIZE) {
            dataOut.writeInt((int) this.bits);
        } else {
            dataOut.writeLong(this.bits);
        }
    }

    public void readFromStream(DataInput dataIn) throws IOException {
        this.length = dataIn.readByte();
        if (this.length <= BYTE_SIZE) {
            this.bits = (long) dataIn.readByte();
        } else if (this.length <= SHORT_SIZE) {
            this.bits = (long) dataIn.readShort();
        } else if (this.length <= INT_SIZE) {
            this.bits = (long) dataIn.readInt();
        } else {
            this.bits = dataIn.readLong();
        }
    }
}
