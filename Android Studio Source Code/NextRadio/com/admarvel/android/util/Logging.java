package com.admarvel.android.util;

import android.util.Log;
import com.admarvel.android.ads.AdMarvelUtils;

public class Logging {
    private static final String LOGTAG = "admarvel";

    public static void log(String message) {
        if (Log.isLoggable(LOGTAG, 2) || AdMarvelUtils.isLoggingEnabled()) {
            Log.d(LOGTAG, message);
        }
    }
}
