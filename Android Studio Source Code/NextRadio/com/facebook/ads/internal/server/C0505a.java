package com.facebook.ads.internal.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.AdErrorType;
import com.facebook.ads.internal.C0458b;
import com.facebook.ads.internal.dto.C0464c;
import com.facebook.ads.internal.dto.C0467e;
import com.facebook.ads.internal.dto.C0468f;
import com.facebook.ads.internal.http.C0490a;
import com.facebook.ads.internal.http.C0493c;
import com.facebook.ads.internal.http.C0495d;
import com.facebook.ads.internal.server.C0508c.C0507a;
import com.facebook.ads.internal.util.AdInternalSettings;
import com.facebook.ads.internal.util.C0517d;
import com.facebook.ads.internal.util.C0532p;
import com.facebook.ads.internal.util.C0536s;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.json.JSONException;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.server.a */
public class C0505a {
    private static final C0532p f1864g;
    private static final ThreadPoolExecutor f1865h;
    Map<String, String> f1866a;
    private final C0506b f1867b;
    private C0485a f1868c;
    private C0467e f1869d;
    private C0490a f1870e;
    private final String f1871f;

    /* renamed from: com.facebook.ads.internal.server.a.a */
    public interface C0485a {
        void m1398a(C0458b c0458b);

        void m1399a(C0509d c0509d);
    }

    /* renamed from: com.facebook.ads.internal.server.a.1 */
    class C05001 implements Runnable {
        final /* synthetic */ Context f1855a;
        final /* synthetic */ C0467e f1856b;
        final /* synthetic */ C0505a f1857c;

        C05001(C0505a c0505a, Context context, C0467e c0467e) {
            this.f1857c = c0505a;
            this.f1855a = context;
            this.f1856b = c0467e;
        }

        public void run() {
            C0468f.m1383b(this.f1855a);
            this.f1857c.f1866a = this.f1856b.m1381e();
            try {
                this.f1857c.f1870e = new C0490a(this.f1855a, this.f1856b.f1732e);
                this.f1857c.f1870e.m1437a(this.f1857c.f1871f, new C0495d(this.f1857c.f1866a), this.f1857c.m1492b());
            } catch (Exception e) {
                this.f1857c.m1486a(AdErrorType.AD_REQUEST_FAILED.getAdErrorWrapper(e.getMessage()));
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.server.a.2 */
    class C05012 extends C0493c {
        final /* synthetic */ C0505a f1858a;

        C05012(C0505a c0505a) {
            this.f1858a = c0505a;
        }

        public void m1481a() {
        }

        public void m1482a(int i, String str) {
            C0517d.m1520b(this.f1858a.f1869d);
            this.f1858a.f1870e = null;
            this.f1858a.m1490a(str);
        }

        public void m1483a(Throwable th, String str) {
            C0517d.m1520b(this.f1858a.f1869d);
            this.f1858a.f1870e = null;
            try {
                C0508c a = this.f1858a.f1867b.m1505a(str);
                if (a.m1506a() == C0507a.ERROR) {
                    String c = ((C0510e) a).m1508c();
                    C0505a c0505a = this.f1858a;
                    AdErrorType adErrorType = AdErrorType.ERROR_MESSAGE;
                    if (c != null) {
                        str = c;
                    }
                    c0505a.m1486a(adErrorType.getAdErrorWrapper(str));
                    return;
                }
            } catch (JSONException e) {
            }
            this.f1858a.m1486a(new C0458b(AdErrorType.NETWORK_ERROR, th.getMessage()));
        }
    }

    /* renamed from: com.facebook.ads.internal.server.a.3 */
    class C05023 implements Runnable {
        final /* synthetic */ C0509d f1859a;
        final /* synthetic */ C0505a f1860b;

        C05023(C0505a c0505a, C0509d c0509d) {
            this.f1860b = c0505a;
            this.f1859a = c0509d;
        }

        public void run() {
            this.f1860b.f1868c.m1399a(this.f1859a);
        }
    }

    /* renamed from: com.facebook.ads.internal.server.a.4 */
    class C05034 implements Runnable {
        final /* synthetic */ C0458b f1861a;
        final /* synthetic */ C0505a f1862b;

        C05034(C0505a c0505a, C0458b c0458b) {
            this.f1862b = c0505a;
            this.f1861a = c0458b;
        }

        public void run() {
            this.f1862b.f1868c.m1398a(this.f1861a);
        }
    }

    /* renamed from: com.facebook.ads.internal.server.a.5 */
    static /* synthetic */ class C05045 {
        static final /* synthetic */ int[] f1863a;

        static {
            f1863a = new int[C0507a.values().length];
            try {
                f1863a[C0507a.ADS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1863a[C0507a.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    static {
        f1864g = new C0532p();
        f1865h = (ThreadPoolExecutor) Executors.newCachedThreadPool(f1864g);
    }

    public C0505a() {
        this.f1867b = C0506b.m1501a();
        String urlPrefix = AdSettings.getUrlPrefix();
        if (AdInternalSettings.shouldUseLiveRailEndpoint()) {
            if (C0536s.m1572a(urlPrefix)) {
                urlPrefix = "https://ad6.liverail.com/";
            } else {
                urlPrefix = String.format("https://ad6.%s.liverail.com/", new Object[]{urlPrefix});
            }
            this.f1871f = urlPrefix;
            return;
        }
        if (C0536s.m1572a(urlPrefix)) {
            urlPrefix = "https://graph.facebook.com/network_ads_common/";
        } else {
            urlPrefix = String.format("https://graph.%s.facebook.com/network_ads_common/", new Object[]{urlPrefix});
        }
        this.f1871f = urlPrefix;
    }

    private void m1486a(C0458b c0458b) {
        if (this.f1868c != null) {
            new Handler(Looper.getMainLooper()).post(new C05034(this, c0458b));
        }
        m1498a();
    }

    private void m1489a(C0509d c0509d) {
        if (this.f1868c != null) {
            new Handler(Looper.getMainLooper()).post(new C05023(this, c0509d));
        }
        m1498a();
    }

    private void m1490a(String str) {
        try {
            C0508c a = this.f1867b.m1505a(str);
            C0464c b = a.m1507b();
            if (b != null) {
                C0517d.m1517a(b.m1361a().m1368c(), this.f1869d);
            }
            switch (C05045.f1863a[a.m1506a().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    C0509d c0509d = (C0509d) a;
                    if (b != null && b.m1361a().m1369d()) {
                        C0517d.m1518a(str, this.f1869d);
                    }
                    m1489a(c0509d);
                    return;
                case Zone.SECONDARY /*2*/:
                    String c = ((C0510e) a).m1508c();
                    AdErrorType adErrorType = AdErrorType.ERROR_MESSAGE;
                    if (c != null) {
                        str = c;
                    }
                    m1486a(adErrorType.getAdErrorWrapper(str));
                    return;
                default:
                    m1486a(AdErrorType.UNKNOWN_RESPONSE.getAdErrorWrapper(str));
                    return;
            }
        } catch (Exception e) {
            m1486a(AdErrorType.PARSER_FAILURE.getAdErrorWrapper(e.getMessage()));
        }
        m1486a(AdErrorType.PARSER_FAILURE.getAdErrorWrapper(e.getMessage()));
    }

    private boolean m1491a(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean z = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return z;
    }

    private C0493c m1492b() {
        return new C05012(this);
    }

    public void m1498a() {
        if (this.f1870e != null) {
            this.f1870e.m1440a(true);
            this.f1870e = null;
        }
    }

    public void m1499a(Context context, C0467e c0467e) {
        m1498a();
        if (m1491a(context)) {
            this.f1869d = c0467e;
            if (C0517d.m1519a(c0467e)) {
                String c = C0517d.m1521c(c0467e);
                if (c != null) {
                    m1490a(c);
                    return;
                } else {
                    m1486a(AdErrorType.LOAD_TOO_FREQUENTLY.getAdErrorWrapper(null));
                    return;
                }
            }
            f1865h.submit(new C05001(this, context, c0467e));
            return;
        }
        m1486a(new C0458b(AdErrorType.NETWORK_ERROR, "No network connection"));
    }

    public void m1500a(C0485a c0485a) {
        this.f1868c = c0485a;
    }
}
