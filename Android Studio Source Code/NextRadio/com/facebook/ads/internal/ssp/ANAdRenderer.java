package com.facebook.ads.internal.ssp;

import android.content.Context;
import android.view.View;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.C0460d;
import com.facebook.ads.internal.C0469e;
import com.facebook.ads.internal.adapters.C0450l;
import com.facebook.ads.internal.view.C0549c;
import org.json.JSONObject;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class ANAdRenderer {

    /* renamed from: com.facebook.ads.internal.ssp.ANAdRenderer.1 */
    static /* synthetic */ class C05111 {
        static final /* synthetic */ int[] f1880a;

        static {
            f1880a = new int[AdSize.values().length];
            try {
                f1880a[AdSize.INTERSTITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1880a[AdSize.RECTANGLE_HEIGHT_250.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1880a[AdSize.BANNER_HEIGHT_90.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1880a[AdSize.BANNER_HEIGHT_50.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public interface Listener {
        void onAdClick();

        void onAdClose();

        void onAdError(Throwable th);

        void onAdImpression();
    }

    public static String getSupportedCapabilities() {
        return C0460d.m1356c();
    }

    public static int getTemplateID(int i, int i2) {
        AdSize fromWidthAndHeight = AdSize.fromWidthAndHeight(i, i2);
        if (fromWidthAndHeight == null) {
            return C0469e.UNKNOWN.m1386a();
        }
        switch (C05111.f1880a[fromWidthAndHeight.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return C0469e.WEBVIEW_INTERSTITIAL_UNKNOWN.m1386a();
            case Zone.SECONDARY /*2*/:
                return C0469e.WEBVIEW_BANNER_250.m1386a();
            case Protocol.GGP /*3*/:
                return C0469e.WEBVIEW_BANNER_90.m1386a();
            case Type.MF /*4*/:
                return C0469e.WEBVIEW_BANNER_50.m1386a();
            default:
                return C0469e.WEBVIEW_BANNER_LEGACY.m1386a();
        }
    }

    public static View renderAd(Context context, JSONObject jSONObject, int i, int i2, int i3, Listener listener) {
        try {
            return new C0549c(context, C0450l.m1279a(jSONObject), i3, listener);
        } catch (Throwable th) {
            listener.onAdError(th);
            return null;
        }
    }
}
