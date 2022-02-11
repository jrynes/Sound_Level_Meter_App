package com.facebook.ads.internal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.facebook.ads.internal.view.C0554e;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/* renamed from: com.facebook.ads.internal.util.k */
public class C0526k extends AsyncTask<String, Void, Bitmap[]> {
    private static final String f1922a;
    private final Context f1923b;
    private final ImageView f1924c;
    private final C0554e f1925d;
    private C0410l f1926e;

    static {
        f1922a = C0526k.class.getSimpleName();
    }

    public C0526k(Context context) {
        this.f1923b = context;
        this.f1925d = null;
        this.f1924c = null;
    }

    public C0526k(ImageView imageView) {
        this.f1923b = imageView.getContext();
        this.f1925d = null;
        this.f1924c = imageView;
    }

    public C0526k(C0554e c0554e) {
        this.f1923b = c0554e.getContext();
        this.f1925d = c0554e;
        this.f1924c = null;
    }

    public C0526k m1554a(C0410l c0410l) {
        this.f1926e = c0410l;
        return this;
    }

    protected void m1555a(Bitmap[] bitmapArr) {
        if (this.f1924c != null) {
            this.f1924c.setImageBitmap(bitmapArr[0]);
        }
        if (this.f1925d != null) {
            this.f1925d.m1600a(bitmapArr[0], bitmapArr[1]);
        }
        if (this.f1926e != null) {
            this.f1926e.m1093a();
        }
    }

    protected Bitmap[] m1556a(String... strArr) {
        Bitmap a;
        Throwable th;
        Object obj;
        Object obj2;
        Bitmap bitmap;
        String str = null;
        String str2 = strArr[0];
        try {
            a = C0528m.m1558a(this.f1923b, str2);
            if (a == null) {
                try {
                    HttpEntity entity = C0522g.m1542b().execute(new HttpGet(str2)).getEntity();
                    byte[] toByteArray = EntityUtils.toByteArray(entity);
                    a = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
                    entity.consumeContent();
                    C0528m.m1559a(this.f1923b, str2, a);
                } catch (Throwable th2) {
                    th = th2;
                    obj = str;
                    Log.e(f1922a, "Error downloading image: " + str2, th);
                    C0515c.m1515a(C0514b.m1512a(th, str));
                    obj2 = bitmap;
                    bitmap = a;
                    return new Bitmap[]{bitmap, str};
                }
            }
            bitmap = a;
            try {
                if (!(this.f1925d == null || bitmap == null)) {
                    try {
                        C0535r c0535r = new C0535r(bitmap);
                        c0535r.m1571a(Math.round(((float) bitmap.getWidth()) / 40.0f));
                        str = c0535r.m1570a();
                    } catch (Throwable th3) {
                        th = th3;
                        a = bitmap;
                        Log.e(f1922a, "Error downloading image: " + str2, th);
                        C0515c.m1515a(C0514b.m1512a(th, str));
                        obj2 = bitmap;
                        bitmap = a;
                        return new Bitmap[]{bitmap, str};
                    }
                }
            } catch (Throwable th32) {
                th = th32;
                a = bitmap;
                obj = str;
                Log.e(f1922a, "Error downloading image: " + str2, th);
                C0515c.m1515a(C0514b.m1512a(th, str));
                obj2 = bitmap;
                bitmap = a;
                return new Bitmap[]{bitmap, str};
            }
        } catch (Throwable th22) {
            th = th22;
            a = str;
            bitmap = str;
            Log.e(f1922a, "Error downloading image: " + str2, th);
            C0515c.m1515a(C0514b.m1512a(th, str));
            obj2 = bitmap;
            bitmap = a;
            return new Bitmap[]{bitmap, str};
        }
        return new Bitmap[]{bitmap, str};
    }

    protected /* synthetic */ Object doInBackground(Object[] objArr) {
        return m1556a((String[]) objArr);
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        m1555a((Bitmap[]) obj);
    }
}
