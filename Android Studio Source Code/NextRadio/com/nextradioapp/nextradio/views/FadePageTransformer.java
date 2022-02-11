package com.nextradioapp.nextradio.views;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import com.google.android.gms.maps.model.GroundOverlayOptions;

public class FadePageTransformer implements PageTransformer {
    public void transformPage(View view, float position) {
        view.setTranslationX(((float) view.getWidth()) * (-position));
        if (position <= GroundOverlayOptions.NO_DIMENSION || position >= 1.0f) {
            view.setAlpha(0.0f);
            view.setClickable(false);
        } else if (position == 0.0f) {
            view.setAlpha(1.0f);
            view.setClickable(true);
        } else {
            view.setAlpha(1.0f - Math.abs(position));
        }
    }
}
