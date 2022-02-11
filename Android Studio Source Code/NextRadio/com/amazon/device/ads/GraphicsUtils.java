package com.amazon.device.ads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images.Media;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.KEYRecord;

class GraphicsUtils {
    private static final String LOGTAG;
    private final MobileAdsLogger logger;

    GraphicsUtils() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    static {
        LOGTAG = GraphicsUtils.class.getSimpleName();
    }

    public Bitmap createBitmapImage(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        InputStream inputStream2 = new BufferedInputStream(inputStream, KEYRecord.FLAG_NOAUTH);
        inputStream2.mark(KEYRecord.FLAG_NOAUTH);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream2);
        try {
            inputStream2.close();
        } catch (IOException e) {
            this.logger.m639e("IOException while trying to close the input stream.");
        }
        inputStream = inputStream2;
        return bitmap;
    }

    public String insertImageInMediaStore(Context context, Bitmap bitmap, String name, String description) {
        return Media.insertImage(context.getContentResolver(), bitmap, name, description);
    }
}
