package com.google.zxing;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.encoder.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Map;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public final class MultiFormatWriter implements Writer {

    /* renamed from: com.google.zxing.MultiFormatWriter.1 */
    static /* synthetic */ class C10591 {
        static final /* synthetic */ int[] $SwitchMap$com$google$zxing$BarcodeFormat;

        static {
            $SwitchMap$com$google$zxing$BarcodeFormat = new int[BarcodeFormat.values().length];
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.EAN_8.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.EAN_13.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.UPC_A.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.QR_CODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.CODE_39.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.CODE_128.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.ITF.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.PDF_417.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.CODABAR.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return encode(contents, format, width, height, null);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        Writer writer;
        switch (C10591.$SwitchMap$com$google$zxing$BarcodeFormat[format.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                writer = new EAN8Writer();
                break;
            case Zone.SECONDARY /*2*/:
                writer = new EAN13Writer();
                break;
            case Protocol.GGP /*3*/:
                writer = new UPCAWriter();
                break;
            case Type.MF /*4*/:
                writer = new QRCodeWriter();
                break;
            case Service.RJE /*5*/:
                writer = new Code39Writer();
                break;
            case Protocol.TCP /*6*/:
                writer = new Code128Writer();
                break;
            case Service.ECHO /*7*/:
                writer = new ITFWriter();
                break;
            case Protocol.EGP /*8*/:
                writer = new PDF417Writer();
                break;
            case Service.DISCARD /*9*/:
                writer = new CodaBarWriter();
                break;
            default:
                throw new IllegalArgumentException("No encoder available for format " + format);
        }
        return writer.encode(contents, format, width, height, hints);
    }
}
