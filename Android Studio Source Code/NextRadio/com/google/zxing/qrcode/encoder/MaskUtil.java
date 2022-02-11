package com.google.zxing.qrcode.encoder;

import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

final class MaskUtil {
    private MaskUtil() {
    }

    static int applyMaskPenaltyRule1(ByteMatrix matrix) {
        return applyMaskPenaltyRule1Internal(matrix, true) + applyMaskPenaltyRule1Internal(matrix, false);
    }

    static int applyMaskPenaltyRule2(ByteMatrix matrix) {
        int penalty = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int y = 0;
        while (y < height - 1) {
            int x = 0;
            while (x < width - 1) {
                byte value = array[y][x];
                if (value == array[y][x + 1] && value == array[y + 1][x] && value == array[y + 1][x + 1]) {
                    penalty += 3;
                }
                x++;
            }
            y++;
        }
        return penalty;
    }

    static int applyMaskPenaltyRule3(ByteMatrix matrix) {
        int penalty = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int y = 0;
        while (y < height) {
            int x = 0;
            while (x < width) {
                if (x + 6 < width && array[y][x] == (byte) 1 && array[y][x + 1] == null && array[y][x + 2] == (byte) 1 && array[y][x + 3] == (byte) 1 && array[y][x + 4] == (byte) 1 && array[y][x + 5] == null && array[y][x + 6] == (byte) 1 && ((x + 10 < width && array[y][x + 7] == null && array[y][x + 8] == null && array[y][x + 9] == null && array[y][x + 10] == null) || (x - 4 >= 0 && array[y][x - 1] == null && array[y][x - 2] == null && array[y][x - 3] == null && array[y][x - 4] == null))) {
                    penalty += 40;
                }
                if (y + 6 < height && array[y][x] == (byte) 1 && array[y + 1][x] == null && array[y + 2][x] == (byte) 1 && array[y + 3][x] == (byte) 1 && array[y + 4][x] == (byte) 1 && array[y + 5][x] == null && array[y + 6][x] == (byte) 1 && ((y + 10 < height && array[y + 7][x] == null && array[y + 8][x] == null && array[y + 9][x] == null && array[y + 10][x] == null) || (y - 4 >= 0 && array[y - 1][x] == null && array[y - 2][x] == null && array[y - 3][x] == null && array[y - 4][x] == null))) {
                    penalty += 40;
                }
                x++;
            }
            y++;
        }
        return penalty;
    }

    static int applyMaskPenaltyRule4(ByteMatrix matrix) {
        int numDarkCells = 0;
        byte[][] array = matrix.getArray();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (array[y][x] == 1) {
                    numDarkCells++;
                }
            }
        }
        return (Math.abs((int) ((100.0d * (((double) numDarkCells) / ((double) (matrix.getHeight() * matrix.getWidth())))) - 50.0d)) / 5) * 10;
    }

    static boolean getDataMaskBit(int maskPattern, int x, int y) {
        if (QRCode.isValidMaskPattern(maskPattern)) {
            int intermediate;
            int temp;
            switch (maskPattern) {
                case Tokenizer.EOF /*0*/:
                    intermediate = (y + x) & 1;
                    break;
                case Zone.PRIMARY /*1*/:
                    intermediate = y & 1;
                    break;
                case Zone.SECONDARY /*2*/:
                    intermediate = x % 3;
                    break;
                case Protocol.GGP /*3*/:
                    intermediate = (y + x) % 3;
                    break;
                case Type.MF /*4*/:
                    intermediate = ((y >>> 1) + (x / 3)) & 1;
                    break;
                case Service.RJE /*5*/:
                    temp = y * x;
                    intermediate = (temp & 1) + (temp % 3);
                    break;
                case Protocol.TCP /*6*/:
                    temp = y * x;
                    intermediate = ((temp & 1) + (temp % 3)) & 1;
                    break;
                case Service.ECHO /*7*/:
                    intermediate = (((y * x) % 3) + ((y + x) & 1)) & 1;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid mask pattern: " + maskPattern);
            }
            if (intermediate == 0) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("Invalid mask pattern");
    }

    private static int applyMaskPenaltyRule1Internal(ByteMatrix matrix, boolean isHorizontal) {
        int penalty = 0;
        int numSameBitCells = 0;
        int prevBit = -1;
        int iLimit = isHorizontal ? matrix.getHeight() : matrix.getWidth();
        int jLimit = isHorizontal ? matrix.getWidth() : matrix.getHeight();
        byte[][] array = matrix.getArray();
        int i = 0;
        while (i < iLimit) {
            int j = 0;
            while (j < jLimit) {
                int bit = isHorizontal ? array[i][j] : array[j][i];
                if (bit == prevBit) {
                    numSameBitCells++;
                    if (numSameBitCells == 5) {
                        penalty += 3;
                    } else if (numSameBitCells > 5) {
                        penalty++;
                    }
                } else {
                    numSameBitCells = 1;
                    prevBit = bit;
                }
                j++;
            }
            numSameBitCells = 0;
            i++;
        }
        return penalty;
    }
}
