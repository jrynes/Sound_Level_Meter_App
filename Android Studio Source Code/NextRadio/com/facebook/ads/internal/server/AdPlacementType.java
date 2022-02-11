package com.facebook.ads.internal.server;

import com.facebook.ads.internal.util.C0536s;
import java.util.Locale;

public enum AdPlacementType {
    UNKNOWN(DeviceInfo.ORIENTATION_UNKNOWN),
    BANNER("banner"),
    INTERSTITIAL("interstitial"),
    NATIVE("native");
    
    private String f1854a;

    private AdPlacementType(String str) {
        this.f1854a = str;
    }

    public static AdPlacementType fromString(String str) {
        if (C0536s.m1572a(str)) {
            return UNKNOWN;
        }
        try {
            return valueOf(str.toUpperCase(Locale.US));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public String toString() {
        return this.f1854a;
    }
}
