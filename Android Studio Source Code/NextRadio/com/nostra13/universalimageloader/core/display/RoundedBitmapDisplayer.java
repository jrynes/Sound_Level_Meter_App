package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.C1271L;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class RoundedBitmapDisplayer implements BitmapDisplayer {
    private final int roundPixels;

    /* renamed from: com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer.1 */
    static /* synthetic */ class C12661 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public RoundedBitmapDisplayer(int roundPixels) {
        this.roundPixels = roundPixels;
    }

    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (imageAware instanceof ImageViewAware) {
            Bitmap roundedBitmap = roundCorners(bitmap, (ImageViewAware) imageAware, this.roundPixels);
            imageAware.setImageBitmap(roundedBitmap);
            return roundedBitmap;
        }
        throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
    }

    public static Bitmap roundCorners(Bitmap bitmap, ImageViewAware imageAware, int roundPixels) {
        ImageView imageView = imageAware.getWrappedView();
        if (imageView == null) {
            C1271L.m1950w("View is collected probably. Can't round bitmap corners without view properties.", new Object[0]);
            return bitmap;
        }
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageAware.getWidth();
        int vh = imageAware.getHeight();
        if (vw <= 0) {
            vw = bw;
        }
        if (vh <= 0) {
            vh = bh;
        }
        ScaleType scaleType = imageView.getScaleType();
        if (scaleType == null) {
            return bitmap;
        }
        Rect srcRect;
        Rect destRect;
        int width;
        int height;
        Bitmap roundBitmap;
        int x;
        int y;
        switch (C12661.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                int destHeight;
                int destWidth;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int) (((float) bw) / (((float) bh) / ((float) destHeight)));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int) (((float) bh) / (((float) bw) / ((float) destWidth)));
                }
                x = (vw - destWidth) / 2;
                y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case Service.RJE /*5*/:
                int srcWidth;
                int srcHeight;
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    srcWidth = bw;
                    srcHeight = (int) (((float) vh) * (((float) bw) / ((float) vw)));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int) (((float) vw) * (((float) bh) / ((float) vh)));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = srcWidth;
                height = srcHeight;
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case Protocol.TCP /*6*/:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case Service.ECHO /*7*/:
            case Protocol.EGP /*8*/:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
            default:
                if (((float) vw) / ((float) vh) > ((float) bw) / ((float) bh)) {
                    width = (int) (((float) bw) / (((float) bh) / ((float) vh)));
                    height = vh;
                } else {
                    width = vw;
                    height = (int) (((float) bh) / (((float) bw) / ((float) vw)));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
        }
        try {
            roundBitmap = getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width, height);
        } catch (OutOfMemoryError e) {
            C1271L.m1948e(e, "Can't create bitmap with rounded corners. Not enough memory.", new Object[0]);
            roundBitmap = bitmap;
        }
        return roundBitmap;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect, Rect destRect, int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        RectF destRectF = new RectF(destRect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRoundRect(destRectF, (float) roundPixels, (float) roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);
        return output;
    }
}
