package com.facebook.ads.internal.view.component;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.C0562i;

/* renamed from: com.facebook.ads.internal.view.component.d */
public class C0553d extends LinearLayout {
    public C0553d(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setVerticalGravity(16);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(Math.round(displayMetrics.density * 15.0f), Math.round(displayMetrics.density * 15.0f), Math.round(displayMetrics.density * 15.0f), Math.round(displayMetrics.density * 15.0f));
        linearLayout.setLayoutParams(layoutParams);
        addView(linearLayout);
        View textView = new TextView(getContext());
        textView.setText(nativeAd.getAdSubtitle());
        C0562i.m1631a(textView, nativeAdViewAttributes);
        textView.setEllipsize(TruncateAt.END);
        textView.setSingleLine(true);
        linearLayout.addView(textView);
        textView = new TextView(getContext());
        textView.setText(nativeAd.getAdBody());
        C0562i.m1632b(textView, nativeAdViewAttributes);
        textView.setEllipsize(TruncateAt.END);
        textView.setMaxLines(2);
        linearLayout.addView(textView);
    }
}
