package com.facebook.ads.internal.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/* renamed from: com.facebook.ads.internal.view.o */
public class C0567o extends View {
    private C0413n f2017a;

    public C0567o(Context context, C0413n c0413n) {
        super(context);
        this.f2017a = c0413n;
        setLayoutParams(new LayoutParams(0, 0));
    }

    public void onWindowVisibilityChanged(int i) {
        if (this.f2017a != null) {
            this.f2017a.m1100a(i);
        }
    }
}
