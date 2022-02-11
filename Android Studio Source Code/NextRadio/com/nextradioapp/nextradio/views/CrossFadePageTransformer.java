package com.nextradioapp.nextradio.views;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.nineoldandroids.view.ViewHelper;

public class CrossFadePageTransformer implements PageTransformer {
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        View backgroundView = page.findViewById(2131689807);
        View text_head = page.findViewById(2131689809);
        View text_content = page.findViewById(2131689810);
        View welcomeImage01 = page.findViewById(2131689808);
        View welcomeImage02 = page.findViewById(2131689811);
        View welcomeImage03 = page.findViewById(2131689812);
        View welcomeImage04 = page.findViewById(2131689813);
        if (0.0f <= position && position < 1.0f) {
            ViewHelper.setTranslationX(page, ((float) pageWidth) * (-position));
        }
        if (GroundOverlayOptions.NO_DIMENSION < position && position < 0.0f) {
            ViewHelper.setTranslationX(page, ((float) pageWidth) * (-position));
        }
        if (position > GroundOverlayOptions.NO_DIMENSION && position < 1.0f && position != 0.0f) {
            if (backgroundView != null) {
                ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));
            }
            if (text_head != null) {
                ViewHelper.setTranslationX(text_head, ((float) pageWidth) * position);
                ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
            }
            if (text_content != null) {
                ViewHelper.setTranslationX(text_content, ((float) pageWidth) * position);
                ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
            }
            if (welcomeImage01 != null) {
                ViewHelper.setTranslationX(welcomeImage01, ((float) (pageWidth / 2)) * position);
                ViewHelper.setAlpha(welcomeImage01, 1.0f - Math.abs(position));
            }
            if (welcomeImage02 != null) {
                ViewHelper.setTranslationX(welcomeImage02, ((float) (pageWidth / 2)) * position);
                ViewHelper.setAlpha(welcomeImage02, 1.0f - Math.abs(position));
            }
            if (welcomeImage03 != null) {
                ViewHelper.setTranslationX(welcomeImage03, ((float) (pageWidth / 2)) * position);
                ViewHelper.setAlpha(welcomeImage03, 1.0f - Math.abs(position));
            }
            if (welcomeImage04 != null) {
                ViewHelper.setTranslationX(welcomeImage04, ((float) (pageWidth / 2)) * position);
                ViewHelper.setAlpha(welcomeImage04, 1.0f - Math.abs(position));
            }
        }
    }
}
