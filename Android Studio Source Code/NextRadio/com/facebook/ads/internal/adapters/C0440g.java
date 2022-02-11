package com.facebook.ads.internal.adapters;

import java.util.Locale;

/* renamed from: com.facebook.ads.internal.adapters.g */
public enum C0440g {
    UNKNOWN,
    AN;

    public static C0440g m1203a(String str) {
        if (str == null) {
            return UNKNOWN;
        }
        try {
            return (C0440g) Enum.valueOf(C0440g.class, str.toUpperCase(Locale.getDefault()));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
