package org.apache.activemq.util;

import com.google.android.gms.location.places.Place;
import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream {
    byte[] buffer;
    int size;

    public ByteArrayOutputStream() {
        this(Place.TYPE_SUBPREMISE);
    }

    public ByteArrayOutputStream(int capacity) {
        this.buffer = new byte[capacity];
    }

    public void write(int b) {
        int newsize = this.size + 1;
        checkCapacity(newsize);
        this.buffer[this.size] = (byte) b;
        this.size = newsize;
    }

    public void write(byte[] b, int off, int len) {
        int newsize = this.size + len;
        checkCapacity(newsize);
        System.arraycopy(b, off, this.buffer, this.size, len);
        this.size = newsize;
    }

    private void checkCapacity(int minimumCapacity) {
        if (minimumCapacity > this.buffer.length) {
            byte[] b = new byte[Math.max(this.buffer.length << 1, minimumCapacity)];
            System.arraycopy(this.buffer, 0, b, 0, this.size);
            this.buffer = b;
        }
    }

    public void reset() {
        this.size = 0;
    }

    public ByteSequence toByteSequence() {
        return new ByteSequence(this.buffer, 0, this.size);
    }

    public byte[] toByteArray() {
        byte[] rc = new byte[this.size];
        System.arraycopy(this.buffer, 0, rc, 0, this.size);
        return rc;
    }

    public int size() {
        return this.size;
    }

    public boolean endsWith(byte[] array) {
        int i = 0;
        int start = this.size - array.length;
        if (start < 0) {
            return false;
        }
        while (start < this.size) {
            int start2 = start + 1;
            int i2 = i + 1;
            if (this.buffer[start] != array[i]) {
                start = start2;
                i = i2;
                return false;
            }
            start = start2;
            i = i2;
        }
        return true;
    }
}
