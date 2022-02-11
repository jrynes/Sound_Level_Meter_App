package com.facebook.ads.internal.adapters;

import com.facebook.ads.internal.server.AdPlacementType;

/* renamed from: com.facebook.ads.internal.adapters.h */
public enum C0441h {
    ANBANNER(C0445i.class, C0440g.AN, AdPlacementType.BANNER),
    ANINTERSTITIAL(C0447j.class, C0440g.AN, AdPlacementType.INTERSTITIAL),
    ANNATIVE(C0449k.class, C0440g.AN, AdPlacementType.NATIVE);
    
    public Class<?> f1587d;
    public String f1588e;
    public C0440g f1589f;
    public AdPlacementType f1590g;

    private C0441h(Class<?> cls, C0440g c0440g, AdPlacementType adPlacementType) {
        this.f1587d = cls;
        this.f1589f = c0440g;
        this.f1590g = adPlacementType;
    }
}
