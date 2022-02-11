package com.admarvel.android.ads;

import android.app.Activity;

/* renamed from: com.admarvel.android.ads.t */
class AdMarvelVideoActivity {
    static void m495a(Activity activity, String str) {
        if (str.equalsIgnoreCase("LandscapeRight")) {
            activity.setRequestedOrientation(8);
        } else if (str.equalsIgnoreCase("PortraitUpsideDown")) {
            activity.setRequestedOrientation(9);
        }
    }
}
