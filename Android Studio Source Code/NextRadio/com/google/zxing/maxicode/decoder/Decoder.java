package com.google.zxing.maxicode.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.Map;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public final class Decoder {
    private static final int ALL = 0;
    private static final int EVEN = 1;
    private static final int ODD = 2;
    private final ReedSolomonDecoder rsDecoder;

    public Decoder() {
        this.rsDecoder = new ReedSolomonDecoder(GenericGF.MAXICODE_FIELD_64);
    }

    public DecoderResult decode(BitMatrix bits) throws ChecksumException, FormatException {
        return decode(bits, null);
    }

    public DecoderResult decode(BitMatrix bits, Map<DecodeHintType, ?> map) throws FormatException, ChecksumException {
        byte[] datawords;
        byte[] codewords = new BitMatrixParser(bits).readCodewords();
        correctErrors(codewords, ALL, 10, 10, ALL);
        int mode = codewords[ALL] & 15;
        switch (mode) {
            case ODD /*2*/:
            case Protocol.GGP /*3*/:
            case Type.MF /*4*/:
                correctErrors(codewords, 20, 84, 40, EVEN);
                correctErrors(codewords, 20, 84, 40, ODD);
                datawords = new byte[94];
                break;
            case Service.RJE /*5*/:
                correctErrors(codewords, 20, 68, 56, EVEN);
                correctErrors(codewords, 20, 68, 56, ODD);
                datawords = new byte[78];
                break;
            default:
                throw FormatException.getFormatInstance();
        }
        System.arraycopy(codewords, ALL, datawords, ALL, 10);
        System.arraycopy(codewords, 20, datawords, 10, datawords.length - 10);
        return DecodedBitStreamParser.decode(datawords, mode);
    }

    private void correctErrors(byte[] codewordBytes, int start, int dataCodewords, int ecCodewords, int mode) throws ChecksumException {
        int codewords = dataCodewords + ecCodewords;
        int divisor = mode == 0 ? EVEN : ODD;
        int[] codewordsInts = new int[(codewords / divisor)];
        int i = ALL;
        while (i < codewords) {
            if (mode == 0 || i % ODD == mode - 1) {
                codewordsInts[i / divisor] = codewordBytes[i + start] & Type.ANY;
            }
            i += EVEN;
        }
        try {
            this.rsDecoder.decode(codewordsInts, ecCodewords / divisor);
            i = ALL;
            while (i < dataCodewords) {
                if (mode == 0 || i % ODD == mode - 1) {
                    codewordBytes[i + start] = (byte) codewordsInts[i / divisor];
                }
                i += EVEN;
            }
        } catch (ReedSolomonException e) {
            throw ChecksumException.getChecksumInstance();
        }
    }
}
