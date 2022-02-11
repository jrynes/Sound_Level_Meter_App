package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0531o;
import com.facebook.ads.internal.util.C0536s;

/* renamed from: com.facebook.ads.internal.action.a */
public abstract class C0428a {
    public abstract C0513a m1163a();

    protected void m1164a(Context context, Uri uri) {
        if (!C0536s.m1572a(uri.getQueryParameter("native_click_report_url"))) {
            new C0531o().execute(new String[]{r0});
            C0522g.m1535a(context, "Click logged");
        }
    }

    public abstract void m1165b();
}
