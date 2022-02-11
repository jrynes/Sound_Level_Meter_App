package com.facebook.ads.internal;

import android.text.TextUtils;
import com.facebook.ads.internal.util.AdInternalSettings;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;

/* renamed from: com.facebook.ads.internal.d */
public enum C0460d {
    APP_AD(0),
    LINK_AD(1),
    APP_AD_V2(2),
    LINK_AD_V2(3),
    APP_ENGAGEMENT_AD(4),
    AD_CHOICES(5),
    JS_TRIGGER(6),
    JS_TRIGGER_NO_AUTO_IMP_LOGGING(7),
    VIDEO_AD(8),
    INLINE_VIDEO_AD(9),
    BANNER_TO_INTERSTITIAL(10),
    NATIVE_CLOSE_BUTTON(11);
    
    private static final C0460d[] f1699n;
    private static final String f1700o;
    private static final String f1701p;
    private final int f1703m;

    static {
        f1699n = new C0460d[]{LINK_AD_V2, APP_ENGAGEMENT_AD, AD_CHOICES, JS_TRIGGER_NO_AUTO_IMP_LOGGING, NATIVE_CLOSE_BUTTON};
        JSONArray jSONArray = new JSONArray();
        C0460d[] c0460dArr = f1699n;
        int length = c0460dArr.length;
        int i;
        while (i < length) {
            jSONArray.put(c0460dArr[i].m1357a());
            i++;
        }
        f1700o = jSONArray.toString();
        f1701p = TextUtils.join(Stomp.COMMA, f1699n);
    }

    private C0460d(int i) {
        this.f1703m = i;
    }

    public static String m1355b() {
        return AdInternalSettings.shouldUseLiveRailEndpoint() ? f1701p : f1700o;
    }

    public static String m1356c() {
        return f1701p;
    }

    int m1357a() {
        return this.f1703m;
    }

    public String toString() {
        return String.valueOf(this.f1703m);
    }
}
