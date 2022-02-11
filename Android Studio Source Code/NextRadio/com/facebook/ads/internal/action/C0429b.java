package com.facebook.ads.internal.action;

import android.content.Context;
import android.net.Uri;

/* renamed from: com.facebook.ads.internal.action.b */
public class C0429b {
    public static C0428a m1166a(Context context, Uri uri) {
        String authority = uri.getAuthority();
        return "store".equals(authority) ? uri.getQueryParameter("video_url") != null ? null : new C0430c(context, uri) : "open_link".equals(authority) ? new C0431d(context, uri) : null;
    }
}
