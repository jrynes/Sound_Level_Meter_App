package com.facebook.ads.internal;

import com.facebook.ads.AdError;
import com.facebook.ads.internal.util.C0536s;

/* renamed from: com.facebook.ads.internal.b */
public class C0458b {
    private final AdErrorType f1681a;
    private final String f1682b;

    public C0458b(AdErrorType adErrorType, String str) {
        if (C0536s.m1572a(str)) {
            str = adErrorType.getDefaultErrorMessage();
        }
        this.f1681a = adErrorType;
        this.f1682b = str;
    }

    public AdErrorType m1352a() {
        return this.f1681a;
    }

    public AdError m1353b() {
        return this.f1681a.m1162a() ? new AdError(this.f1681a.getErrorCode(), this.f1682b) : new AdError(AdErrorType.UNKNOWN_ERROR.getErrorCode(), AdErrorType.UNKNOWN_ERROR.getDefaultErrorMessage());
    }
}
