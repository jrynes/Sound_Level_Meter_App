package com.facebook.ads.internal;

import com.rabbitmq.client.AMQP;
import org.xbill.DNS.WKSRecord.Service;

/* renamed from: com.facebook.ads.internal.e */
public enum C0469e {
    UNKNOWN(0),
    WEBVIEW_BANNER_LEGACY(4),
    WEBVIEW_BANNER_50(5),
    WEBVIEW_BANNER_90(6),
    WEBVIEW_BANNER_250(7),
    WEBVIEW_INTERSTITIAL_UNKNOWN(100),
    WEBVIEW_INTERSTITIAL_HORIZONTAL(Service.HOSTNAME),
    WEBVIEW_INTERSTITIAL_VERTICAL(Service.ISO_TSAP),
    WEBVIEW_INTERSTITIAL_TABLET(Service.X400),
    NATIVE_UNKNOWN(AMQP.REPLY_SUCCESS),
    NATIVE_250(201);
    
    private final int f1765l;

    private C0469e(int i) {
        this.f1765l = i;
    }

    public int m1386a() {
        return this.f1765l;
    }
}
