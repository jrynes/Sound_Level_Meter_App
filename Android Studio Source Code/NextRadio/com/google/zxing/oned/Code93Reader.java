package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.rabbitmq.client.AMQP;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public final class Code93Reader extends OneDReader {
    private static final char[] ALPHABET;
    private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
    private static final int ASTERISK_ENCODING;
    private static final int[] CHARACTER_ENCODINGS;

    static {
        ALPHABET = ALPHABET_STRING.toCharArray();
        CHARACTER_ENCODINGS = new int[]{276, 328, 324, 322, 296, 292, 290, 336, 274, 266, 424, 420, 418, AMQP.NOT_FOUND, AMQP.INVALID_PATH, 394, 360, 356, 354, 308, 282, 344, 332, 326, 300, 278, 436, 434, 428, 422, AMQP.PRECONDITION_FAILED, 410, 364, 358, 310, 314, 302, 468, 466, 458, 366, 374, 430, 294, 474, 470, 306, 350};
        ASTERISK_ENCODING = CHARACTER_ENCODINGS[47];
    }

    public Result decodeRow(int rowNumber, BitArray row, Map<DecodeHintType, ?> map) throws NotFoundException, ChecksumException, FormatException {
        int[] start = findAsteriskPattern(row);
        int nextStart = row.getNextSet(start[1]);
        int end = row.getSize();
        StringBuilder stringBuilder = new StringBuilder(20);
        int[] counters = new int[6];
        char decodedChar;
        do {
            OneDReader.recordPattern(row, nextStart, counters);
            int pattern = toPattern(counters);
            if (pattern < 0) {
                throw NotFoundException.getNotFoundInstance();
            }
            decodedChar = patternToChar(pattern);
            stringBuilder.append(decodedChar);
            int lastStart = nextStart;
            for (int counter : counters) {
                nextStart += counter;
            }
            nextStart = row.getNextSet(nextStart);
        } while (decodedChar != '*');
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        if (nextStart == end || !row.get(nextStart)) {
            throw NotFoundException.getNotFoundInstance();
        } else if (stringBuilder.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
        } else {
            checkChecksums(stringBuilder);
            stringBuilder.setLength(stringBuilder.length() - 2);
            String resultString = decodeExtended(stringBuilder);
            float right = ((float) (nextStart + lastStart)) / 2.0f;
            r22 = new ResultPoint[2];
            r22[0] = new ResultPoint(((float) (start[1] + start[0])) / 2.0f, (float) rowNumber);
            r22[1] = new ResultPoint(right, (float) rowNumber);
            return new Result(resultString, null, r22, BarcodeFormat.CODE_93);
        }
    }

    private static int[] findAsteriskPattern(BitArray row) throws NotFoundException {
        int width = row.getSize();
        int rowOffset = row.getNextSet(0);
        int counterPosition = 0;
        int[] counters = new int[6];
        int patternStart = rowOffset;
        boolean isWhite = false;
        int patternLength = counters.length;
        for (int i = rowOffset; i < width; i++) {
            if ((row.get(i) ^ isWhite) != 0) {
                counters[counterPosition] = counters[counterPosition] + 1;
            } else {
                if (counterPosition != patternLength - 1) {
                    counterPosition++;
                } else if (toPattern(counters) == ASTERISK_ENCODING) {
                    return new int[]{patternStart, i};
                } else {
                    patternStart += counters[0] + counters[1];
                    System.arraycopy(counters, 2, counters, 0, patternLength - 2);
                    counters[patternLength - 2] = 0;
                    counters[patternLength - 1] = 0;
                    counterPosition--;
                }
                counters[counterPosition] = 1;
                if (isWhite) {
                    isWhite = false;
                } else {
                    isWhite = true;
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static int toPattern(int[] counters) {
        int i;
        int sum = 0;
        for (int i2 : counters) {
            sum += i2;
        }
        int pattern = 0;
        for (i = 0; i < max; i++) {
            int scaledShifted = ((counters[i] << 8) * 9) / sum;
            int scaledUnshifted = scaledShifted >> 8;
            if ((scaledShifted & Type.ANY) > Service.LOCUS_CON) {
                scaledUnshifted++;
            }
            if (scaledUnshifted < 1 || scaledUnshifted > 4) {
                return -1;
            }
            if ((i & 1) == 0) {
                for (int j = 0; j < scaledUnshifted; j++) {
                    pattern = (pattern << 1) | 1;
                }
            } else {
                pattern <<= scaledUnshifted;
            }
        }
        return pattern;
    }

    private static char patternToChar(int pattern) throws NotFoundException {
        for (int i = 0; i < CHARACTER_ENCODINGS.length; i++) {
            if (CHARACTER_ENCODINGS[i] == pattern) {
                return ALPHABET[i];
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static String decodeExtended(CharSequence encoded) throws FormatException {
        int length = encoded.length();
        StringBuilder decoded = new StringBuilder(length);
        int i = 0;
        while (i < length) {
            char c = encoded.charAt(i);
            if (c < 'a' || c > 'd') {
                decoded.append(c);
            } else {
                char next = encoded.charAt(i + 1);
                char decodedChar = '\u0000';
                switch (c) {
                    case Service.SWIFT_RVF /*97*/:
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next - 64);
                            break;
                        }
                        throw FormatException.getFormatInstance();
                        break;
                    case Service.TACNEWS /*98*/:
                        if (next < 'A' || next > 'E') {
                            if (next >= 'F' && next <= 'W') {
                                decodedChar = (char) (next - 11);
                                break;
                            }
                            throw FormatException.getFormatInstance();
                        }
                        decodedChar = (char) (next - 38);
                        break;
                        break;
                    case Service.METAGRAM /*99*/:
                        if (next >= 'A' && next <= 'O') {
                            decodedChar = (char) (next - 32);
                            break;
                        } else if (next == 'Z') {
                            decodedChar = ':';
                            break;
                        } else {
                            throw FormatException.getFormatInstance();
                        }
                        break;
                    case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                        if (next >= 'A' && next <= 'Z') {
                            decodedChar = (char) (next + 32);
                            break;
                        }
                        throw FormatException.getFormatInstance();
                }
                decoded.append(decodedChar);
                i++;
            }
            i++;
        }
        return decoded.toString();
    }

    private static void checkChecksums(CharSequence result) throws ChecksumException {
        int length = result.length();
        checkOneChecksum(result, length - 2, 20);
        checkOneChecksum(result, length - 1, 15);
    }

    private static void checkOneChecksum(CharSequence result, int checkPosition, int weightMax) throws ChecksumException {
        int weight = 1;
        int total = 0;
        for (int i = checkPosition - 1; i >= 0; i--) {
            total += ALPHABET_STRING.indexOf(result.charAt(i)) * weight;
            weight++;
            if (weight > weightMax) {
                weight = 1;
            }
        }
        if (result.charAt(checkPosition) != ALPHABET[total % 47]) {
            throw ChecksumException.getChecksumInstance();
        }
    }
}
