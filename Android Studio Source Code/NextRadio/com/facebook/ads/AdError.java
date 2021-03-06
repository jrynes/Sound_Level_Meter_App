package com.facebook.ads;

import com.facebook.ads.internal.util.C0536s;

public class AdError {
    public static final AdError INTERNAL_ERROR;
    public static final int INTERNAL_ERROR_CODE = 2001;
    public static final AdError LOAD_TOO_FREQUENTLY;
    public static final int LOAD_TOO_FREQUENTLY_ERROR_CODE = 1002;
    @Deprecated
    public static final AdError MISSING_PROPERTIES;
    public static final AdError NETWORK_ERROR;
    public static final int NETWORK_ERROR_CODE = 1000;
    public static final AdError NO_FILL;
    public static final int NO_FILL_ERROR_CODE = 1001;
    public static final AdError SERVER_ERROR;
    public static final int SERVER_ERROR_CODE = 2000;
    private final int f1410a;
    private final String f1411b;

    static {
        NETWORK_ERROR = new AdError(NETWORK_ERROR_CODE, "Network Error");
        NO_FILL = new AdError(NO_FILL_ERROR_CODE, "No Fill");
        LOAD_TOO_FREQUENTLY = new AdError(LOAD_TOO_FREQUENTLY_ERROR_CODE, "Ad was re-loaded too frequently");
        SERVER_ERROR = new AdError(SERVER_ERROR_CODE, "Server Error");
        INTERNAL_ERROR = new AdError(INTERNAL_ERROR_CODE, "Internal Error");
        MISSING_PROPERTIES = new AdError(2002, "Native ad failed to load due to missing properties");
    }

    public AdError(int i, String str) {
        if (C0536s.m1572a(str)) {
            str = "unknown error";
        }
        this.f1410a = i;
        this.f1411b = str;
    }

    public int getErrorCode() {
        return this.f1410a;
    }

    public String getErrorMessage() {
        return this.f1411b;
    }
}
