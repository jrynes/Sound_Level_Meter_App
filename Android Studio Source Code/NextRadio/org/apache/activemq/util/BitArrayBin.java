package org.apache.activemq.util;

import java.io.Serializable;
import java.util.LinkedList;

public class BitArrayBin implements Serializable {
    private static final long serialVersionUID = 1;
    private int firstIndex;
    private long lastInOrderBit;
    private LinkedList<BitArray> list;
    private int maxNumberOfArrays;

    public BitArrayBin(int windowSize) {
        this.firstIndex = -1;
        this.lastInOrderBit = -1;
        this.maxNumberOfArrays = ((windowSize + 1) / 64) + 1;
        this.maxNumberOfArrays = Math.max(this.maxNumberOfArrays, 1);
        this.list = new LinkedList();
        for (int i = 0; i < this.maxNumberOfArrays; i++) {
            this.list.add(null);
        }
    }

    public boolean setBit(long index, boolean value) {
        BitArray ba = getBitArray(index);
        if (ba == null) {
            return false;
        }
        int offset = getOffset(index);
        if (offset >= 0) {
            return ba.set(offset, value);
        }
        return false;
    }

    public boolean isInOrder(long index) {
        boolean result;
        if (this.lastInOrderBit == -1) {
            result = true;
        } else {
            result = this.lastInOrderBit + serialVersionUID == index;
        }
        this.lastInOrderBit = index;
        return result;
    }

    public boolean getBit(long index) {
        boolean answer = index >= ((long) this.firstIndex);
        BitArray ba = getBitArray(index);
        if (ba != null) {
            int offset = getOffset(index);
            if (offset >= 0) {
                return ba.get(offset);
            }
        }
        answer = true;
        return answer;
    }

    private BitArray getBitArray(long index) {
        int bin = getBin(index);
        if (bin < 0) {
            return null;
        }
        if (bin >= this.maxNumberOfArrays) {
            for (int overShoot = (bin - this.maxNumberOfArrays) + 1; overShoot > 0; overShoot--) {
                this.list.removeFirst();
                this.firstIndex += 64;
                this.list.add(new BitArray());
            }
            bin = this.maxNumberOfArrays - 1;
        }
        BitArray answer = (BitArray) this.list.get(bin);
        if (answer != null) {
            return answer;
        }
        answer = new BitArray();
        this.list.set(bin, answer);
        return answer;
    }

    private int getBin(long index) {
        if (this.firstIndex < 0) {
            this.firstIndex = (int) (index - (index % 64));
            return 0;
        } else if (this.firstIndex >= 0) {
            return (int) ((index - ((long) this.firstIndex)) / 64);
        } else {
            return 0;
        }
    }

    private int getOffset(long index) {
        if (this.firstIndex >= 0) {
            return (int) ((index - ((long) this.firstIndex)) - ((long) (getBin(index) * 64)));
        }
        return 0;
    }

    public long getLastSetIndex() {
        if (this.firstIndex < 0) {
            return -1;
        }
        long result = (long) this.firstIndex;
        for (int lastBitArrayIndex = this.maxNumberOfArrays - 1; lastBitArrayIndex >= 0; lastBitArrayIndex--) {
            BitArray last = (BitArray) this.list.get(lastBitArrayIndex);
            if (last != null) {
                return (result + ((long) (last.length() - 1))) + ((long) (lastBitArrayIndex * 64));
            }
        }
        return result;
    }
}
