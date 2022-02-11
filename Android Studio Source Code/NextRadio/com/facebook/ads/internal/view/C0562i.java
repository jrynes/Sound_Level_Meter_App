package com.facebook.ads.internal.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;

/* renamed from: com.facebook.ads.internal.view.i */
public abstract class C0562i {
    public static LinearLayout m1630a(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes) {
        LinearLayout linearLayout = new LinearLayout(context);
        Rating adStarRating = nativeAd.getAdStarRating();
        if (adStarRating == null || adStarRating.getValue() < 3.0d) {
            View c0564k = new C0564k(context);
            c0564k.setText(nativeAd.getAdSocialContext());
            C0562i.m1632b(c0564k, nativeAdViewAttributes);
            linearLayout.addView(c0564k);
        } else {
            View ratingBar = new RatingBar(context, null, 16842877);
            ratingBar.setLayoutParams(new LayoutParams(-2, -2));
            ratingBar.setStepSize(0.1f);
            ratingBar.setIsIndicator(true);
            ratingBar.setNumStars((int) adStarRating.getScale());
            ratingBar.setRating((float) adStarRating.getValue());
            linearLayout.addView(ratingBar);
        }
        return linearLayout;
    }

    public static void m1631a(TextView textView, NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getTitleTextColor());
        textView.setTextSize((float) nativeAdViewAttributes.getTitleTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
    }

    public static void m1632b(TextView textView, NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getDescriptionTextColor());
        textView.setTextSize((float) nativeAdViewAttributes.getDescriptionTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface());
    }
}
