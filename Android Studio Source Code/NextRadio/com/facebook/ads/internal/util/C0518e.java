package com.facebook.ads.internal.util;

import java.util.Locale;

/* renamed from: com.facebook.ads.internal.util.e */
public enum C0518e {
    NONE,
    INSTALLED,
    NOT_INSTALLED;

    public static C0518e m1523a(String str) {
        if (C0536s.m1572a(str)) {
            return NONE;
        }
        try {
            return C0518e.valueOf(str.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}
