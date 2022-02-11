package com.facebook.ads;

import android.content.Context;
import android.view.View;
import com.facebook.ads.internal.C0402a;
import com.facebook.ads.internal.C0458b;
import com.facebook.ads.internal.C0459c;
import com.facebook.ads.internal.C0486h;
import com.facebook.ads.internal.util.C0522g;

public class InterstitialAd implements Ad {
    private static final C0459c f1433a;
    private final Context f1434b;
    private final String f1435c;
    private C0486h f1436d;
    private boolean f1437e;
    private boolean f1438f;
    private InterstitialAdListener f1439g;
    private ImpressionListener f1440h;

    /* renamed from: com.facebook.ads.InterstitialAd.1 */
    class C04041 extends C0402a {
        final /* synthetic */ InterstitialAd f1432a;

        C04041(InterstitialAd interstitialAd) {
            this.f1432a = interstitialAd;
        }

        public void m1067a() {
            this.f1432a.f1437e = true;
            if (this.f1432a.f1439g != null) {
                this.f1432a.f1439g.onAdLoaded(this.f1432a);
            }
        }

        public void m1068a(View view) {
        }

        public void m1069a(C0458b c0458b) {
            if (this.f1432a.f1439g != null) {
                this.f1432a.f1439g.onError(this.f1432a, c0458b.m1353b());
            }
        }

        public void m1070b() {
            if (this.f1432a.f1439g != null) {
                this.f1432a.f1439g.onAdClicked(this.f1432a);
            }
        }

        public void m1071c() {
            if (this.f1432a.f1440h != null) {
                this.f1432a.f1440h.onLoggingImpression(this.f1432a);
            }
            if ((this.f1432a.f1439g instanceof ImpressionListener) && this.f1432a.f1439g != this.f1432a.f1440h) {
                ((ImpressionListener) this.f1432a.f1439g).onLoggingImpression(this.f1432a);
            }
        }

        public void m1072d() {
            if (this.f1432a.f1439g != null) {
                this.f1432a.f1439g.onInterstitialDisplayed(this.f1432a);
            }
        }

        public void m1073e() {
            this.f1432a.f1438f = false;
            if (this.f1432a.f1436d != null) {
                this.f1432a.f1436d.m1428d();
                this.f1432a.f1436d = null;
            }
            if (this.f1432a.f1439g != null) {
                this.f1432a.f1439g.onInterstitialDismissed(this.f1432a);
            }
        }
    }

    static {
        f1433a = C0459c.ADS;
    }

    public InterstitialAd(Context context, String str) {
        this.f1434b = context;
        this.f1435c = str;
    }

    public void destroy() {
        if (this.f1436d != null) {
            this.f1436d.m1428d();
            this.f1436d = null;
        }
    }

    public boolean isAdLoaded() {
        return this.f1437e;
    }

    public void loadAd() {
        this.f1437e = false;
        if (this.f1438f) {
            throw new IllegalStateException("InterstitialAd cannot be loaded while being displayed. Make sure your adapter calls adapterListener.onInterstitialDismissed().");
        }
        if (this.f1436d != null) {
            this.f1436d.m1428d();
            this.f1436d = null;
        }
        AdSize adSize = AdSize.INTERSTITIAL;
        this.f1436d = new C0486h(this.f1434b, this.f1435c, C0522g.m1527a(AdSize.INTERSTITIAL), adSize, f1433a, 1, true);
        this.f1436d.m1423a(new C04041(this));
        this.f1436d.m1426b();
    }

    public void setAdListener(InterstitialAdListener interstitialAdListener) {
        this.f1439g = interstitialAdListener;
    }

    @Deprecated
    public void setImpressionListener(ImpressionListener impressionListener) {
        this.f1440h = impressionListener;
    }

    public boolean show() {
        if (this.f1437e) {
            this.f1436d.m1427c();
            this.f1438f = true;
            this.f1437e = false;
            return true;
        } else if (this.f1439g == null) {
            return false;
        } else {
            this.f1439g.onError(this, AdError.INTERNAL_ERROR);
            return false;
        }
    }
}
