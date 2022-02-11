package com.admarvel.android.ads.nativeads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.admarvel.android.util.Logging;
import java.lang.ref.WeakReference;
import java.net.URL;

public class AdMarvelNativeImage {
    private Bitmap bitmap;
    private Drawable drawableResource;
    private int height;
    private String imageUrl;
    private WeakReference<ImageView> imageViewReference;
    private int width;

    /* renamed from: com.admarvel.android.ads.nativeads.AdMarvelNativeImage.a */
    private class C0264a extends AsyncTask<String, Void, Bitmap> {
        final /* synthetic */ AdMarvelNativeImage f694a;
        private final ImageView f695b;
        private final WeakReference<ImageView> f696c;

        public C0264a(AdMarvelNativeImage adMarvelNativeImage, ImageView imageView) {
            this.f694a = adMarvelNativeImage;
            this.f695b = imageView;
            this.f696c = new WeakReference(imageView);
        }

        protected Bitmap m395a(String... strArr) {
            String str;
            Object e;
            Bitmap bitmap = null;
            try {
                str = strArr[0];
                try {
                    bitmap = BitmapFactory.decodeStream(new URL(str).openStream());
                } catch (Exception e2) {
                    e = e2;
                    Logging.log("Error downloading image: " + str + " " + e);
                    return bitmap;
                }
            } catch (Exception e3) {
                e = e3;
                str = bitmap;
                Logging.log("Error downloading image: " + str + " " + e);
                return bitmap;
            }
            return bitmap;
        }

        protected void m396a(Bitmap bitmap) {
            if (this.f696c.get() != null) {
                ((ImageView) this.f696c.get()).setImageBitmap(bitmap);
            }
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m395a((String[]) x0);
        }

        protected /* synthetic */ void onPostExecute(Object x0) {
            m396a((Bitmap) x0);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private void setImage(Bitmap bitmap) {
        if (this.imageViewReference.get() != null) {
            ((ImageView) this.imageViewReference.get()).setImageBitmap(bitmap);
        }
    }

    public void downloadAndDisplayImage(ImageView imageView) {
        this.imageViewReference = new WeakReference(imageView);
        if (getDrawableResource() != null) {
            setImage(getBitmapFromDrawable(getDrawableResource()));
        } else if (getBitmap() != null) {
            setImage(getBitmap());
        } else {
            String imageUrl = getImageUrl();
            new C0264a(this, imageView).execute(new String[]{imageUrl});
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Drawable getDrawableResource() {
        return this.drawableResource;
    }

    public int getHeight() {
        return this.height;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public int getWidth() {
        return this.width;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setDrawableResource(Drawable drawableResource) {
        this.drawableResource = drawableResource;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImageUrl(String content) {
        this.imageUrl = content;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
