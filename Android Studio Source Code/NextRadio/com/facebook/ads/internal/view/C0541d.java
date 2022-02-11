package com.facebook.ads.internal.view;

import android.content.Context;
import android.webkit.WebView;

/* renamed from: com.facebook.ads.internal.view.d */
public class C0541d extends WebView {
    private boolean f1957a;

    public C0541d(Context context) {
        super(context);
    }

    public boolean m1576b() {
        return this.f1957a;
    }

    public void destroy() {
        this.f1957a = true;
        super.destroy();
    }
}
