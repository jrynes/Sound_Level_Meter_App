package com.facebook.ads.internal.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.internal.util.C0514b.C0513a;

/* renamed from: com.facebook.ads.internal.action.d */
public class C0431d extends C0428a {
    private static final String f1556a;
    private final Context f1557b;
    private final Uri f1558c;

    static {
        f1556a = C0431d.class.getSimpleName();
    }

    public C0431d(Context context, Uri uri) {
        this.f1557b = context;
        this.f1558c = uri;
    }

    public C0513a m1175a() {
        return C0513a.OPEN_LINK;
    }

    public void m1176b() {
        m1164a(this.f1557b, this.f1558c);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.f1558c.getQueryParameter("link")));
        intent.addFlags(268435456);
        try {
            this.f1557b.startActivity(intent);
        } catch (Throwable e) {
            Log.d(f1556a, "Failed to open market url: " + this.f1558c.toString(), e);
        }
    }
}
