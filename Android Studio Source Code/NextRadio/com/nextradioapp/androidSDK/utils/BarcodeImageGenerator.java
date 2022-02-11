package com.nextradioapp.androidSDK.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Contents.Type;
import com.google.zxing.client.android.Intents.Encode;
import com.google.zxing.client.android.Intents.SearchBookContents;
import com.google.zxing.client.android.encode.QRCodeEncoder;
import org.apache.activemq.transport.stomp.Stomp;

public class BarcodeImageGenerator {
    public static Bitmap generateBarcodeBitmap(Activity context, String codeData, String codeType) {
        Bitmap bitmap = null;
        Intent intent = new Intent();
        if (!(codeData == null || codeData.equals(Stomp.EMPTY))) {
            BarcodeFormat barcodeType = BarcodeFormat.QR_CODE;
            if (codeData != null) {
                if (codeData.endsWith("UPCA") || codeType.equals("UPC-A")) {
                    barcodeType = BarcodeFormat.UPC_A;
                } else if (codeData.endsWith("UPA5")) {
                    barcodeType = BarcodeFormat.UPC_A;
                } else if (codeData.endsWith(SearchBookContents.ISBN) || codeType.equals(SearchBookContents.ISBN)) {
                    barcodeType = BarcodeFormat.EAN_13;
                } else if (codeData.endsWith("DMX0")) {
                    barcodeType = BarcodeFormat.DATA_MATRIX;
                }
            }
            intent.putExtra(Encode.DATA, codeData);
            intent.putExtra(Encode.FORMAT, barcodeType.toString());
            intent.putExtra(Encode.TYPE, Type.TEXT);
            intent.setAction(Encode.ACTION);
            try {
                bitmap = new QRCodeEncoder(context, intent, 240, false).encodeAsBitmap();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }
}
