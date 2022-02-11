package com.mologiq.analytics;

import android.os.Build.VERSION;

public class Version {
    public static final String DATE = "2015-09-10";
    public static final String VERSION = "1.4.4";

    static int m1682a() {
        return VERSION.RELEASE.contains(com.admarvel.android.ads.Version.ADMARVEL_API_VERSION) ? 3 : VERSION.SDK_INT;
    }
}
