package com.facebook.ads.internal.dto;

import com.facebook.ads.internal.C0469e;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.dto.b */
public enum C0463b {
    UNKNOWN,
    BANNER,
    INTERSTITIAL,
    NATIVE;

    /* renamed from: com.facebook.ads.internal.dto.b.1 */
    static /* synthetic */ class C04621 {
        static final /* synthetic */ int[] f1707a;

        static {
            f1707a = new int[C0469e.values().length];
            try {
                f1707a[C0469e.NATIVE_UNKNOWN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1707a[C0469e.WEBVIEW_BANNER_50.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1707a[C0469e.WEBVIEW_BANNER_90.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1707a[C0469e.WEBVIEW_BANNER_LEGACY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f1707a[C0469e.WEBVIEW_BANNER_250.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f1707a[C0469e.WEBVIEW_INTERSTITIAL_HORIZONTAL.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f1707a[C0469e.WEBVIEW_INTERSTITIAL_VERTICAL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f1707a[C0469e.WEBVIEW_INTERSTITIAL_TABLET.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f1707a[C0469e.WEBVIEW_INTERSTITIAL_UNKNOWN.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public static C0463b m1360a(C0469e c0469e) {
        switch (C04621.f1707a[c0469e.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return NATIVE;
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
            case Type.MF /*4*/:
            case Service.RJE /*5*/:
                return BANNER;
            case Protocol.TCP /*6*/:
            case Service.ECHO /*7*/:
            case Protocol.EGP /*8*/:
            case Service.DISCARD /*9*/:
                return INTERSTITIAL;
            default:
                return UNKNOWN;
        }
    }
}
