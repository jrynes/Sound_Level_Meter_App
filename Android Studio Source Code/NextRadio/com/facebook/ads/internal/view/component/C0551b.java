package com.facebook.ads.internal.view.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.C0562i;
import com.facebook.ads.internal.view.C0564k;

/* renamed from: com.facebook.ads.internal.view.component.b */
public class C0551b extends LinearLayout {
    private ImageView f1979a;
    private C0550a f1980b;
    private TextView f1981c;
    private LinearLayout f1982d;

    public C0551b(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes, boolean z, int i) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        setVerticalGravity(16);
        setOrientation(1);
        View linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(16);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density));
        linearLayout.setLayoutParams(layoutParams);
        addView(linearLayout);
        this.f1982d = new LinearLayout(getContext());
        layoutParams = new LinearLayout.LayoutParams(-1, 0);
        this.f1982d.setOrientation(0);
        this.f1982d.setGravity(16);
        layoutParams.weight = PDF417.PREFERRED_RATIO;
        this.f1982d.setLayoutParams(layoutParams);
        linearLayout.addView(this.f1982d);
        this.f1979a = new C0552c(getContext());
        int a = m1596a(z, i);
        LayoutParams layoutParams2 = new LinearLayout.LayoutParams(Math.round(((float) a) * displayMetrics.density), Math.round(((float) a) * displayMetrics.density));
        layoutParams2.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        this.f1979a.setLayoutParams(layoutParams2);
        NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), this.f1979a);
        this.f1982d.addView(this.f1979a);
        View linearLayout2 = new LinearLayout(getContext());
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        this.f1982d.addView(linearLayout2);
        this.f1980b = new C0550a(getContext(), nativeAd, nativeAdViewAttributes);
        layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams2.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        layoutParams2.weight = 0.5f;
        this.f1980b.setLayoutParams(layoutParams2);
        linearLayout2.addView(this.f1980b);
        this.f1981c = new TextView(getContext());
        this.f1981c.setPadding(Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density));
        this.f1981c.setText(nativeAd.getAdCallToAction());
        this.f1981c.setTextColor(nativeAdViewAttributes.getButtonTextColor());
        this.f1981c.setTextSize(14.0f);
        this.f1981c.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
        this.f1981c.setMaxLines(2);
        this.f1981c.setEllipsize(TruncateAt.END);
        this.f1981c.setGravity(17);
        Drawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(nativeAdViewAttributes.getButtonColor());
        gradientDrawable.setCornerRadius(displayMetrics.density * 5.0f);
        gradientDrawable.setStroke(1, nativeAdViewAttributes.getButtonBorderColor());
        this.f1981c.setBackgroundDrawable(gradientDrawable);
        LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams3.weight = 0.25f;
        this.f1981c.setLayoutParams(layoutParams3);
        linearLayout2.addView(this.f1981c);
        if (z) {
            View c0564k = new C0564k(getContext());
            c0564k.setText(nativeAd.getAdBody());
            C0562i.m1632b(c0564k, nativeAdViewAttributes);
            c0564k.setMinTextSize((float) (nativeAdViewAttributes.getDescriptionTextSize() - 1));
            layoutParams = new LinearLayout.LayoutParams(-1, 0);
            layoutParams.weight = 1.0f;
            c0564k.setLayoutParams(layoutParams);
            c0564k.setGravity(80);
            linearLayout.addView(c0564k);
        }
    }

    private int m1596a(boolean z, int i) {
        return (int) (((double) (i - 30)) * (3.0d / ((double) ((z ? 1 : 0) + 3))));
    }

    public TextView getCallToActionView() {
        return this.f1981c;
    }

    public ImageView getIconView() {
        return this.f1979a;
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextView titleTextView = this.f1980b.getTitleTextView();
        if (titleTextView.getLayout().getLineEnd(titleTextView.getLineCount() - 1) < this.f1980b.getMinVisibleTitleCharacters()) {
            this.f1982d.removeView(this.f1979a);
            super.onMeasure(i, i2);
        }
    }
}
