package com.facebook.ads.internal.view.component;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.C0562i;
import com.facebook.ads.internal.view.C0563j;

/* renamed from: com.facebook.ads.internal.view.component.a */
public class C0550a extends LinearLayout {
    private C0563j f1977a;
    private int f1978b;

    public C0550a(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        setOrientation(1);
        setVerticalGravity(16);
        this.f1977a = new C0563j(getContext(), 2);
        this.f1977a.setMinTextSize((float) (nativeAdViewAttributes.getTitleTextSize() - 2));
        this.f1977a.setText(nativeAd.getAdTitle());
        C0562i.m1631a(this.f1977a, nativeAdViewAttributes);
        this.f1977a.setLayoutParams(new LayoutParams(-2, -2));
        addView(this.f1977a);
        this.f1978b = Math.min(nativeAd.getAdTitle().length(), 21);
        addView(C0562i.m1630a(context, nativeAd, nativeAdViewAttributes));
    }

    public int getMinVisibleTitleCharacters() {
        return this.f1978b;
    }

    public TextView getTitleTextView() {
        return this.f1977a;
    }
}
