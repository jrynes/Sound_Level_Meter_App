package com.facebook.ads;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.C0402a;
import com.facebook.ads.internal.C0458b;
import com.facebook.ads.internal.C0459c;
import com.facebook.ads.internal.C0486h;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.view.C0542a;

public class AdView extends RelativeLayout implements Ad {
    private static final C0459c f1423a;
    private final DisplayMetrics f1424b;
    private final AdSize f1425c;
    private C0486h f1426d;
    private volatile boolean f1427e;
    private AdListener f1428f;
    private ImpressionListener f1429g;
    private View f1430h;
    private boolean f1431i;

    /* renamed from: com.facebook.ads.AdView.1 */
    class C04031 extends C0402a {
        final /* synthetic */ AdView f1422a;

        C04031(AdView adView) {
            this.f1422a = adView;
        }

        public void m1054a() {
            if (this.f1422a.f1426d != null) {
                this.f1422a.f1426d.m1427c();
            }
        }

        public void m1055a(View view) {
            if (view == null) {
                throw new IllegalStateException("Cannot present null view");
            }
            this.f1422a.f1431i = true;
            this.f1422a.f1430h = view;
            this.f1422a.removeAllViews();
            this.f1422a.addView(this.f1422a.f1430h);
            if (this.f1422a.f1430h instanceof C0542a) {
                C0522g.m1536a(this.f1422a.f1424b, this.f1422a.f1430h, this.f1422a.f1425c);
            }
            if (this.f1422a.f1428f != null) {
                this.f1422a.f1428f.onAdLoaded(this.f1422a);
            }
        }

        public void m1056a(C0458b c0458b) {
            if (this.f1422a.f1428f != null) {
                this.f1422a.f1428f.onError(this.f1422a, c0458b.m1353b());
            }
        }

        public void m1057b() {
            if (this.f1422a.f1428f != null) {
                this.f1422a.f1428f.onAdClicked(this.f1422a);
            }
        }

        public void m1058c() {
            if (this.f1422a.f1429g != null) {
                this.f1422a.f1429g.onLoggingImpression(this.f1422a);
            }
            if ((this.f1422a.f1428f instanceof ImpressionListener) && this.f1422a.f1428f != this.f1422a.f1429g) {
                ((ImpressionListener) this.f1422a.f1428f).onLoggingImpression(this.f1422a);
            }
        }
    }

    static {
        f1423a = C0459c.ADS;
    }

    public AdView(Context context, String str, AdSize adSize) {
        super(context);
        this.f1431i = false;
        if (adSize == null || adSize == AdSize.INTERSTITIAL) {
            throw new IllegalArgumentException("adSize");
        }
        this.f1424b = getContext().getResources().getDisplayMetrics();
        this.f1425c = adSize;
        this.f1426d = new C0486h(context, str, C0522g.m1527a(adSize), adSize, f1423a, 1, false);
        this.f1426d.m1423a(new C04031(this));
    }

    public void destroy() {
        if (this.f1426d != null) {
            this.f1426d.m1428d();
            this.f1426d = null;
        }
        removeAllViews();
        this.f1430h = null;
    }

    public void disableAutoRefresh() {
        if (this.f1426d != null) {
            this.f1426d.m1432h();
        }
    }

    public void loadAd() {
        if (!this.f1427e) {
            this.f1426d.m1426b();
            this.f1427e = true;
        } else if (this.f1426d != null) {
            this.f1426d.m1431g();
        }
    }

    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f1430h != null) {
            C0522g.m1536a(this.f1424b, this.f1430h, this.f1425c);
        }
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.f1426d != null) {
            if (i == 0) {
                this.f1426d.m1430f();
            } else if (i == 8) {
                this.f1426d.m1429e();
            }
        }
    }

    public void setAdListener(AdListener adListener) {
        this.f1428f = adListener;
    }

    @Deprecated
    public void setImpressionListener(ImpressionListener impressionListener) {
        this.f1429g = impressionListener;
    }
}
