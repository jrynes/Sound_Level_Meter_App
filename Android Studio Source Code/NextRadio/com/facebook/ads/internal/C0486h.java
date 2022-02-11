package com.facebook.ads.internal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.adapters.BannerAdapter;
import com.facebook.ads.internal.adapters.BannerAdapterListener;
import com.facebook.ads.internal.adapters.C0439f;
import com.facebook.ads.internal.adapters.C0448p;
import com.facebook.ads.internal.adapters.C0457q;
import com.facebook.ads.internal.adapters.InterstitialAdapter;
import com.facebook.ads.internal.adapters.InterstitialAdapterListener;
import com.facebook.ads.internal.dto.C0461a;
import com.facebook.ads.internal.dto.C0464c;
import com.facebook.ads.internal.dto.C0465d;
import com.facebook.ads.internal.dto.C0467e;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.server.C0505a;
import com.facebook.ads.internal.server.C0505a.C0485a;
import com.facebook.ads.internal.server.C0509d;
import com.facebook.ads.internal.util.C0435u;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0536s;
import com.mixpanel.android.java_websocket.framing.CloseFrame;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.ThreadPoolUtils;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.h */
public class C0486h implements C0485a {
    private static final String f1791b;
    protected C0402a f1792a;
    private final Context f1793c;
    private final String f1794d;
    private final C0505a f1795e;
    private final Handler f1796f;
    private final Runnable f1797g;
    private final Runnable f1798h;
    private volatile boolean f1799i;
    private boolean f1800j;
    private volatile boolean f1801k;
    private AdAdapter f1802l;
    private View f1803m;
    private C0464c f1804n;
    private C0467e f1805o;
    private C0469e f1806p;
    private C0459c f1807q;
    private AdSize f1808r;
    private int f1809s;
    private final C0484c f1810t;
    private boolean f1811u;

    /* renamed from: com.facebook.ads.internal.h.1 */
    class C04751 implements Runnable {
        final /* synthetic */ BannerAdapter f1777a;
        final /* synthetic */ C0486h f1778b;

        C04751(C0486h c0486h, BannerAdapter bannerAdapter) {
            this.f1778b = c0486h;
            this.f1777a = bannerAdapter;
        }

        public void run() {
            this.f1778b.m1401a(this.f1777a);
            this.f1778b.m1419n();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.2 */
    class C04762 implements BannerAdapterListener {
        final /* synthetic */ Runnable f1779a;
        final /* synthetic */ C0486h f1780b;

        C04762(C0486h c0486h, Runnable runnable) {
            this.f1780b = c0486h;
            this.f1779a = runnable;
        }

        public void onBannerAdClicked(BannerAdapter bannerAdapter) {
            this.f1780b.m1416k();
            this.f1780b.f1792a.m1050b();
        }

        public void onBannerAdExpanded(BannerAdapter bannerAdapter) {
            this.f1780b.m1416k();
            this.f1780b.m1421p();
        }

        public void onBannerAdLoaded(BannerAdapter bannerAdapter, View view) {
            this.f1780b.m1416k();
            this.f1780b.f1796f.removeCallbacks(this.f1779a);
            AdAdapter f = this.f1780b.f1802l;
            this.f1780b.f1802l = bannerAdapter;
            this.f1780b.f1803m = view;
            if (this.f1780b.f1801k) {
                this.f1780b.f1792a.m1047a(view);
                this.f1780b.m1401a(f);
                this.f1780b.m1420o();
                return;
            }
            this.f1780b.f1792a.m1046a();
        }

        public void onBannerAdMinimized(BannerAdapter bannerAdapter) {
            this.f1780b.m1416k();
            this.f1780b.m1420o();
        }

        public void onBannerError(BannerAdapter bannerAdapter, AdError adError) {
            this.f1780b.m1416k();
            this.f1780b.f1796f.removeCallbacks(this.f1779a);
            this.f1780b.m1401a((AdAdapter) bannerAdapter);
            this.f1780b.m1419n();
        }

        public void onBannerLoggingImpression(BannerAdapter bannerAdapter) {
            this.f1780b.m1416k();
            this.f1780b.f1792a.m1051c();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.3 */
    class C04773 implements Runnable {
        final /* synthetic */ InterstitialAdapter f1781a;
        final /* synthetic */ C0486h f1782b;

        C04773(C0486h c0486h, InterstitialAdapter interstitialAdapter) {
            this.f1782b = c0486h;
            this.f1781a = interstitialAdapter;
        }

        public void run() {
            this.f1782b.m1401a(this.f1781a);
            this.f1782b.m1419n();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.4 */
    class C04784 implements InterstitialAdapterListener {
        final /* synthetic */ Runnable f1783a;
        final /* synthetic */ C0486h f1784b;

        C04784(C0486h c0486h, Runnable runnable) {
            this.f1784b = c0486h;
            this.f1783a = runnable;
        }

        public void onInterstitialAdClicked(InterstitialAdapter interstitialAdapter, String str, boolean z) {
            this.f1784b.m1416k();
            this.f1784b.f1792a.m1050b();
            Object obj = !C0536s.m1572a(str) ? 1 : null;
            if (z && obj != null) {
                Intent intent = new Intent("android.intent.action.VIEW");
                if (!(this.f1784b.f1805o.f1731d instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                intent.setData(Uri.parse(str));
                this.f1784b.f1805o.f1731d.startActivity(intent);
            }
        }

        public void onInterstitialAdDismissed(InterstitialAdapter interstitialAdapter) {
            this.f1784b.m1416k();
            this.f1784b.f1792a.m1053e();
        }

        public void onInterstitialAdDisplayed(InterstitialAdapter interstitialAdapter) {
            this.f1784b.m1416k();
            this.f1784b.f1792a.m1052d();
        }

        public void onInterstitialAdLoaded(InterstitialAdapter interstitialAdapter) {
            this.f1784b.m1416k();
            this.f1784b.f1796f.removeCallbacks(this.f1783a);
            this.f1784b.f1802l = interstitialAdapter;
            this.f1784b.f1792a.m1046a();
            this.f1784b.m1420o();
        }

        public void onInterstitialError(InterstitialAdapter interstitialAdapter, AdError adError) {
            this.f1784b.m1416k();
            this.f1784b.f1796f.removeCallbacks(this.f1783a);
            this.f1784b.m1401a((AdAdapter) interstitialAdapter);
            this.f1784b.m1419n();
        }

        public void onInterstitialLoggingImpression(InterstitialAdapter interstitialAdapter) {
            this.f1784b.m1416k();
            this.f1784b.f1792a.m1051c();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.5 */
    class C04795 implements Runnable {
        final /* synthetic */ C0448p f1785a;
        final /* synthetic */ C0486h f1786b;

        C04795(C0486h c0486h, C0448p c0448p) {
            this.f1786b = c0486h;
            this.f1785a = c0448p;
        }

        public void run() {
            this.f1786b.m1401a(this.f1785a);
            this.f1786b.m1419n();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.6 */
    class C04806 implements C0457q {
        final /* synthetic */ Runnable f1787a;
        final /* synthetic */ C0486h f1788b;

        C04806(C0486h c0486h, Runnable runnable) {
            this.f1788b = c0486h;
            this.f1787a = runnable;
        }

        public void m1396a(C0448p c0448p) {
            this.f1788b.m1416k();
            this.f1788b.f1796f.removeCallbacks(this.f1787a);
            this.f1788b.f1802l = c0448p;
            this.f1788b.f1792a.m1046a();
        }

        public void m1397a(C0448p c0448p, AdError adError) {
            this.f1788b.m1416k();
            this.f1788b.f1796f.removeCallbacks(this.f1787a);
            this.f1788b.m1401a((AdAdapter) c0448p);
            this.f1788b.m1419n();
        }
    }

    /* renamed from: com.facebook.ads.internal.h.7 */
    static /* synthetic */ class C04817 {
        static final /* synthetic */ int[] f1789a;

        static {
            f1789a = new int[AdPlacementType.values().length];
            try {
                f1789a[AdPlacementType.INTERSTITIAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1789a[AdPlacementType.BANNER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1789a[AdPlacementType.NATIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.h.a */
    private static final class C0482a extends C0435u<C0486h> {
        public C0482a(C0486h c0486h) {
            super(c0486h);
        }

        public void run() {
            C0486h c0486h = (C0486h) m1184a();
            if (c0486h != null) {
                c0486h.f1799i = false;
                c0486h.m1418m();
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.h.b */
    private static final class C0483b extends C0435u<C0486h> {
        public C0483b(C0486h c0486h) {
            super(c0486h);
        }

        public void run() {
            C0486h c0486h = (C0486h) m1184a();
            if (c0486h != null) {
                c0486h.m1420o();
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.h.c */
    private class C0484c extends BroadcastReceiver {
        final /* synthetic */ C0486h f1790a;

        private C0484c(C0486h c0486h) {
            this.f1790a = c0486h;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                this.f1790a.m1421p();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                this.f1790a.m1420o();
            }
        }
    }

    static {
        f1791b = C0486h.class.getSimpleName();
    }

    public C0486h(Context context, String str, C0469e c0469e, AdSize adSize, C0459c c0459c, int i, boolean z) {
        this.f1793c = context;
        this.f1794d = str;
        this.f1806p = c0469e;
        this.f1808r = adSize;
        this.f1807q = c0459c;
        this.f1809s = i;
        this.f1810t = new C0484c();
        this.f1795e = new C0505a();
        this.f1795e.m1500a((C0485a) this);
        this.f1796f = new Handler();
        this.f1797g = new C0482a(this);
        this.f1798h = new C0483b(this);
        this.f1800j = z;
        m1414i();
    }

    private void m1401a(AdAdapter adAdapter) {
        if (adAdapter != null) {
            adAdapter.onDestroy();
        }
    }

    private void m1414i() {
        if (!this.f1800j) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.f1793c.registerReceiver(this.f1810t, intentFilter);
            this.f1811u = true;
        }
    }

    private void m1415j() {
        if (this.f1811u) {
            try {
                this.f1793c.unregisterReceiver(this.f1810t);
                this.f1811u = false;
            } catch (Throwable e) {
                C0515c.m1515a(C0514b.m1512a(e, "Error unregistering screen state receiever"));
            }
        }
    }

    private void m1416k() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Adapter listener must be called on the main thread.");
        }
    }

    private AdPlacementType m1417l() {
        return this.f1808r == null ? AdPlacementType.NATIVE : this.f1808r == AdSize.INTERSTITIAL ? AdPlacementType.INTERSTITIAL : AdPlacementType.BANNER;
    }

    private void m1418m() {
        this.f1805o = new C0467e(this.f1793c, this.f1794d, this.f1808r, this.f1806p, this.f1807q, this.f1809s, AdSettings.isTestMode(this.f1793c));
        this.f1795e.m1499a(this.f1793c, this.f1805o);
    }

    private void m1419n() {
        C0464c c0464c = this.f1804n;
        C0461a c = c0464c.m1364c();
        if (c == null) {
            this.f1792a.m1049a(AdErrorType.NO_FILL.getAdErrorWrapper(Stomp.EMPTY));
            m1420o();
            return;
        }
        String str = c.f1705b;
        AdAdapter a = C0439f.m1200a(str, c0464c.m1361a().m1366a());
        if (a == null) {
            Log.e(f1791b, "Adapter does not exist: " + str);
            m1419n();
        } else if (m1417l() != a.getPlacementType()) {
            this.f1792a.m1049a(AdErrorType.INTERNAL_ERROR.getAdErrorWrapper(Stomp.EMPTY));
        } else {
            Map hashMap = new HashMap();
            C0465d a2 = c0464c.m1361a();
            hashMap.put(MPDbAdapter.KEY_DATA, c.f1706c);
            hashMap.put("definition", a2);
            if (this.f1805o == null) {
                this.f1792a.m1049a(AdErrorType.UNKNOWN_ERROR.getAdErrorWrapper("environment is empty"));
            }
            Runnable c04773;
            switch (C04817.f1789a[a.getPlacementType().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    InterstitialAdapter interstitialAdapter = (InterstitialAdapter) a;
                    c04773 = new C04773(this, interstitialAdapter);
                    this.f1796f.postDelayed(c04773, ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
                    interstitialAdapter.loadInterstitialAd(this.f1793c, new C04784(this, c04773), hashMap);
                case Zone.SECONDARY /*2*/:
                    BannerAdapter bannerAdapter = (BannerAdapter) a;
                    c04773 = new C04751(this, bannerAdapter);
                    this.f1796f.postDelayed(c04773, ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
                    bannerAdapter.loadBannerAd(this.f1793c, this.f1808r, new C04762(this, c04773), hashMap);
                case Protocol.GGP /*3*/:
                    C0448p c0448p = (C0448p) a;
                    c04773 = new C04795(this, c0448p);
                    this.f1796f.postDelayed(c04773, ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
                    c0448p.m1225a(this.f1793c, new C04806(this, c04773), hashMap);
                default:
                    Log.e(f1791b, "attempt unexpected adapter type");
            }
        }
    }

    private void m1420o() {
        if (!this.f1800j && !this.f1799i) {
            switch (C04817.f1789a[m1417l().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    if (!C0522g.m1539a(this.f1793c)) {
                        this.f1796f.postDelayed(this.f1798h, 1000);
                        break;
                    }
                    break;
                case Zone.SECONDARY /*2*/:
                    int e = this.f1804n == null ? 1 : this.f1804n.m1361a().m1370e();
                    if (!(this.f1803m == null || C0522g.m1540a(this.f1793c, this.f1803m, e))) {
                        this.f1796f.postDelayed(this.f1798h, 1000);
                        return;
                    }
                default:
                    return;
            }
            long b = this.f1804n == null ? 30000 : this.f1804n.m1361a().m1367b();
            if (b > 0) {
                this.f1796f.postDelayed(this.f1797g, b);
                this.f1799i = true;
            }
        }
    }

    private void m1421p() {
        if (this.f1799i) {
            this.f1796f.removeCallbacks(this.f1797g);
            this.f1799i = false;
        }
    }

    public C0465d m1422a() {
        return this.f1804n == null ? null : this.f1804n.m1361a();
    }

    public void m1423a(C0402a c0402a) {
        this.f1792a = c0402a;
    }

    public void m1424a(C0458b c0458b) {
        this.f1792a.m1049a(c0458b);
        if (!this.f1800j && !this.f1799i) {
            switch (c0458b.m1352a().getErrorCode()) {
                case ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH /*1000*/:
                case CloseFrame.PROTOCOL_ERROR /*1002*/:
                    switch (C04817.f1789a[m1417l().ordinal()]) {
                        case Zone.SECONDARY /*2*/:
                            this.f1796f.postDelayed(this.f1797g, 30000);
                            this.f1799i = true;
                        default:
                    }
                default:
            }
        }
    }

    public void m1425a(C0509d c0509d) {
        C0464c b = c0509d.m1507b();
        if (b == null || b.m1361a() == null) {
            throw new IllegalStateException("invalid placement in response");
        }
        this.f1804n = b;
        m1419n();
    }

    public void m1426b() {
        m1418m();
    }

    public void m1427c() {
        if (this.f1802l == null) {
            throw new IllegalStateException("no adapter ready to start");
        } else if (this.f1801k) {
            throw new IllegalStateException("ad already started");
        } else {
            this.f1801k = true;
            switch (C04817.f1789a[this.f1802l.getPlacementType().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    ((InterstitialAdapter) this.f1802l).show();
                case Zone.SECONDARY /*2*/:
                    if (this.f1803m != null) {
                        this.f1792a.m1047a(this.f1803m);
                        m1420o();
                    }
                case Protocol.GGP /*3*/:
                    C0448p c0448p = (C0448p) this.f1802l;
                    if (c0448p.m1250w()) {
                        this.f1792a.m1048a(c0448p);
                        return;
                    }
                    throw new IllegalStateException("ad is not ready or already displayed");
                default:
                    Log.e(f1791b, "start unexpected adapter type");
            }
        }
    }

    public void m1428d() {
        m1415j();
        if (this.f1801k) {
            m1421p();
            m1401a(this.f1802l);
            this.f1803m = null;
            this.f1801k = false;
        }
    }

    public void m1429e() {
        if (this.f1801k) {
            m1421p();
        }
    }

    public void m1430f() {
        if (this.f1801k) {
            m1420o();
        }
    }

    public void m1431g() {
        m1421p();
        m1418m();
    }

    public void m1432h() {
        this.f1800j = true;
        m1421p();
    }
}
