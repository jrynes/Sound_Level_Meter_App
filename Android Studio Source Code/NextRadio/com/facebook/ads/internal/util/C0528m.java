package com.facebook.ads.internal.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/* renamed from: com.facebook.ads.internal.util.m */
public class C0528m {

    /* renamed from: com.facebook.ads.internal.util.m.1 */
    static class C05271 implements C0410l {
        final /* synthetic */ int[] f1927a;
        final /* synthetic */ C0410l f1928b;

        C05271(int[] iArr, C0410l c0410l) {
            this.f1927a = iArr;
            this.f1928b = c0410l;
        }

        public void m1557a() {
            int[] iArr = this.f1927a;
            iArr[0] = iArr[0] - 1;
            if (this.f1927a[0] == 0 && this.f1928b != null) {
                this.f1928b.m1093a();
            }
        }
    }

    static Bitmap m1558a(Context context, String str) {
        return BitmapFactory.decodeFile(new File(context.getCacheDir(), String.format("%d.png", new Object[]{Integer.valueOf(str.hashCode())})).getAbsolutePath());
    }

    static void m1559a(Context context, String str, Bitmap bitmap) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(new File(context.getCacheDir(), String.format("%d.png", new Object[]{Integer.valueOf(str.hashCode())})));
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
        }
    }

    public static void m1560a(Context context, List<String> list, C0410l c0410l) {
        int[] iArr = new int[]{list.size()};
        if (iArr[0] != 0) {
            for (String str : list) {
                new C0526k(context).m1554a(new C05271(iArr, c0410l)).execute(new String[]{str});
            }
        } else if (c0410l != null) {
            c0410l.m1093a();
        }
    }
}
