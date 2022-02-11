package org.androidannotations.api;

import android.os.Build.VERSION;
import com.admarvel.android.ads.Version;

public class SdkVersionHelper {
    public static int getSdkInt() {
        if (VERSION.RELEASE.startsWith(Version.ADMARVEL_API_VERSION)) {
            return 3;
        }
        return HelperInternal.access$000();
    }
}
