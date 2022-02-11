package com.facebook.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.NativeAdView.Type;
import com.facebook.ads.internal.C0402a;
import com.facebook.ads.internal.C0458b;
import com.facebook.ads.internal.C0459c;
import com.facebook.ads.internal.C0469e;
import com.facebook.ads.internal.C0486h;
import com.facebook.ads.internal.adapters.C0417c;
import com.facebook.ads.internal.adapters.C0437e;
import com.facebook.ads.internal.adapters.C0437e.C0415a;
import com.facebook.ads.internal.adapters.C0448p;
import com.facebook.ads.internal.adapters.C0456o;
import com.facebook.ads.internal.dto.C0465d;
import com.facebook.ads.internal.util.C0410l;
import com.facebook.ads.internal.util.C0526k;
import com.facebook.ads.internal.util.C0528m;
import com.facebook.ads.internal.view.C0413n;
import com.facebook.ads.internal.view.C0567o;
import com.facebook.ads.internal.view.video.C0579a;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.json.JSONObject;

public class NativeAd implements Ad {
    private static final C0459c f1488a;
    private static final String f1489b;
    private static WeakHashMap<View, WeakReference<NativeAd>> f1490c;
    private final Context f1491d;
    private final String f1492e;
    private AdListener f1493f;
    private ImpressionListener f1494g;
    private C0486h f1495h;
    private volatile boolean f1496i;
    private C0448p f1497j;
    private C0465d f1498k;
    private View f1499l;
    private List<View> f1500m;
    private OnTouchListener f1501n;
    private C0437e f1502o;
    private C0456o f1503p;
    private C0420a f1504q;
    private C0421b f1505r;
    private C0567o f1506s;
    private Type f1507t;
    private boolean f1508u;
    private boolean f1509v;
    private boolean f1510w;

    /* renamed from: com.facebook.ads.NativeAd.1 */
    class C04121 extends C0402a {
        final /* synthetic */ EnumSet f1461a;
        final /* synthetic */ NativeAd f1462b;

        /* renamed from: com.facebook.ads.NativeAd.1.1 */
        class C04111 implements C0410l {
            final /* synthetic */ C0448p f1459a;
            final /* synthetic */ C04121 f1460b;

            C04111(C04121 c04121, C0448p c0448p) {
                this.f1460b = c04121;
                this.f1459a = c0448p;
            }

            public void m1094a() {
                this.f1460b.f1462b.f1497j = this.f1459a;
                this.f1460b.f1462b.m1132g();
                if (this.f1460b.f1462b.f1493f != null) {
                    this.f1460b.f1462b.f1493f.onAdLoaded(this.f1460b.f1462b);
                }
            }
        }

        C04121(NativeAd nativeAd, EnumSet enumSet) {
            this.f1462b = nativeAd;
            this.f1461a = enumSet;
        }

        public void m1095a() {
            if (this.f1462b.f1495h != null) {
                this.f1462b.f1495h.m1427c();
            }
        }

        public void m1096a(C0448p c0448p) {
            if (c0448p != null) {
                List arrayList = new ArrayList(2);
                if (this.f1461a.contains(MediaCacheFlag.ICON) && c0448p.m1235h() != null) {
                    arrayList.add(c0448p.m1235h().getUrl());
                }
                if (this.f1461a.contains(MediaCacheFlag.IMAGE) && c0448p.m1236i() != null) {
                    arrayList.add(c0448p.m1236i().getUrl());
                }
                C0528m.m1560a(this.f1462b.f1491d, arrayList, new C04111(this, c0448p));
            }
        }

        public void m1097a(C0458b c0458b) {
            if (this.f1462b.f1493f != null) {
                this.f1462b.f1493f.onError(this.f1462b, c0458b.m1353b());
            }
        }

        public void m1098b() {
            if (this.f1462b.f1493f != null) {
                this.f1462b.f1493f.onAdClicked(this.f1462b);
            }
        }

        public void m1099c() {
            throw new IllegalStateException("Native ads manager their own impressions.");
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.2 */
    class C04142 implements C0413n {
        final /* synthetic */ NativeAd f1463a;

        C04142(NativeAd nativeAd) {
            this.f1463a = nativeAd;
        }

        public void m1101a(int i) {
            if (this.f1463a.f1497j != null) {
                this.f1463a.f1497j.m1224a(i);
            }
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.3 */
    class C04163 extends C0415a {
        final /* synthetic */ NativeAd f1464a;

        C04163(NativeAd nativeAd) {
            this.f1464a = nativeAd;
        }

        public void m1104a() {
            this.f1464a.f1503p.m1343a(this.f1464a.f1499l);
            this.f1464a.f1503p.m1344a(this.f1464a.f1507t);
            this.f1464a.f1503p.m1346a(this.f1464a.f1508u);
            this.f1464a.f1503p.m1348b(this.f1464a.f1509v);
            this.f1464a.f1503p.m1349c(this.f1464a.f1510w);
            this.f1464a.f1503p.m1180a();
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.4 */
    class C04184 extends C0417c {
        final /* synthetic */ NativeAd f1465a;

        C04184(NativeAd nativeAd) {
            this.f1465a = nativeAd;
        }

        public boolean m1110a() {
            return true;
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.5 */
    class C04195 extends C0417c {
        final /* synthetic */ String f1466a;
        final /* synthetic */ NativeAd f1467b;

        C04195(NativeAd nativeAd, String str) {
            this.f1467b = nativeAd;
            this.f1466a = str;
        }

        public boolean m1111b() {
            return true;
        }

        public String m1112c() {
            return this.f1466a;
        }
    }

    public static class Image {
        private final String f1468a;
        private final int f1469b;
        private final int f1470c;

        private Image(String str, int i, int i2) {
            this.f1468a = str;
            this.f1469b = i;
            this.f1470c = i2;
        }

        public static Image fromJSONObject(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            String optString = jSONObject.optString(SettingsJsonConstants.APP_URL_KEY);
            return optString != null ? new Image(optString, jSONObject.optInt(SettingsJsonConstants.ICON_WIDTH_KEY, 0), jSONObject.optInt(SettingsJsonConstants.ICON_HEIGHT_KEY, 0)) : null;
        }

        public int getHeight() {
            return this.f1470c;
        }

        public String getUrl() {
            return this.f1468a;
        }

        public int getWidth() {
            return this.f1469b;
        }
    }

    public enum MediaCacheFlag {
        NONE(0),
        ICON(1),
        IMAGE(2);
        
        public static final EnumSet<MediaCacheFlag> ALL;
        private final long f1472a;

        static {
            ALL = EnumSet.allOf(MediaCacheFlag.class);
        }

        private MediaCacheFlag(long j) {
            this.f1472a = j;
        }

        public long getCacheFlagValue() {
            return this.f1472a;
        }
    }

    public static class Rating {
        private final double f1473a;
        private final double f1474b;

        private Rating(double d, double d2) {
            this.f1473a = d;
            this.f1474b = d2;
        }

        public static Rating fromJSONObject(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            double optDouble = jSONObject.optDouble(Constants.NATIVE_AD_VALUE_ELEMENT, 0.0d);
            double optDouble2 = jSONObject.optDouble("scale", 0.0d);
            return (optDouble == 0.0d || optDouble2 == 0.0d) ? null : new Rating(optDouble, optDouble2);
        }

        public double getScale() {
            return this.f1474b;
        }

        public double getValue() {
            return this.f1473a;
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.a */
    private class C0420a implements OnClickListener, OnTouchListener {
        final /* synthetic */ NativeAd f1475a;
        private int f1476b;
        private int f1477c;
        private int f1478d;
        private int f1479e;
        private float f1480f;
        private float f1481g;
        private int f1482h;
        private int f1483i;
        private boolean f1484j;

        private C0420a(NativeAd nativeAd) {
            this.f1475a = nativeAd;
        }

        public Map<String, Object> m1113a() {
            Map<String, Object> hashMap = new HashMap();
            hashMap.put("clickX", Integer.valueOf(this.f1476b));
            hashMap.put("clickY", Integer.valueOf(this.f1477c));
            hashMap.put(SettingsJsonConstants.ICON_WIDTH_KEY, Integer.valueOf(this.f1478d));
            hashMap.put(SettingsJsonConstants.ICON_HEIGHT_KEY, Integer.valueOf(this.f1479e));
            hashMap.put("adPositionX", Float.valueOf(this.f1480f));
            hashMap.put("adPositionY", Float.valueOf(this.f1481g));
            hashMap.put("visibleWidth", Integer.valueOf(this.f1483i));
            hashMap.put("visibleHeight", Integer.valueOf(this.f1482h));
            return hashMap;
        }

        public void onClick(View view) {
            if (this.f1475a.f1493f != null) {
                this.f1475a.f1493f.onAdClicked(this.f1475a);
            }
            if (!this.f1484j) {
                Log.e("FBAudienceNetworkLog", "No touch data recorded, please ensure touch events reach the ad View by returning false if you intercept the event.");
            }
            Map a = m1113a();
            if (this.f1475a.f1507t != null) {
                a.put("nti", String.valueOf(this.f1475a.f1507t.getValue()));
            }
            if (this.f1475a.f1508u) {
                a.put("nhs", String.valueOf(this.f1475a.f1508u));
            }
            this.f1475a.f1497j.m1228b(a);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && this.f1475a.f1499l != null) {
                this.f1478d = this.f1475a.f1499l.getWidth();
                this.f1479e = this.f1475a.f1499l.getHeight();
                int[] iArr = new int[2];
                this.f1475a.f1499l.getLocationInWindow(iArr);
                this.f1480f = (float) iArr[0];
                this.f1481g = (float) iArr[1];
                Rect rect = new Rect();
                this.f1475a.f1499l.getGlobalVisibleRect(rect);
                this.f1483i = rect.width();
                this.f1482h = rect.height();
                int[] iArr2 = new int[2];
                view.getLocationInWindow(iArr2);
                this.f1476b = (((int) motionEvent.getX()) + iArr2[0]) - iArr[0];
                this.f1477c = (iArr2[1] + ((int) motionEvent.getY())) - iArr[1];
                this.f1484j = true;
            }
            return this.f1475a.f1501n != null && this.f1475a.f1501n.onTouch(view, motionEvent);
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.b */
    private class C0421b extends BroadcastReceiver {
        final /* synthetic */ NativeAd f1485a;
        private boolean f1486b;

        private C0421b(NativeAd nativeAd) {
            this.f1485a = nativeAd;
        }

        public void m1114a() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.facebook.ads.native.impression:" + this.f1485a.f1497j.m1244q());
            intentFilter.addAction("com.facebook.ads.native.click:" + this.f1485a.f1497j.m1244q());
            LocalBroadcastManager.getInstance(this.f1485a.f1491d).registerReceiver(this, intentFilter);
            this.f1486b = true;
        }

        public void m1115b() {
            if (this.f1486b) {
                try {
                    LocalBroadcastManager.getInstance(this.f1485a.f1491d).unregisterReceiver(this);
                } catch (Exception e) {
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            Object obj = intent.getAction().split(Headers.SEPERATOR)[0];
            if ("com.facebook.ads.native.impression".equals(obj)) {
                this.f1485a.f1503p.m1180a();
            } else if ("com.facebook.ads.native.click".equals(obj)) {
                Map hashMap = new HashMap();
                hashMap.put("mil", Boolean.valueOf(true));
                this.f1485a.f1497j.m1228b(hashMap);
            }
        }
    }

    /* renamed from: com.facebook.ads.NativeAd.c */
    private class C0422c extends C0417c {
        final /* synthetic */ NativeAd f1487a;

        private C0422c(NativeAd nativeAd) {
            this.f1487a = nativeAd;
        }

        public boolean m1116a() {
            return false;
        }

        public void m1117d() {
            if (this.f1487a.f1494g != null) {
                this.f1487a.f1494g.onLoggingImpression(this.f1487a);
            }
            if ((this.f1487a.f1493f instanceof ImpressionListener) && this.f1487a.f1493f != this.f1487a.f1494g) {
                ((ImpressionListener) this.f1487a.f1493f).onLoggingImpression(this.f1487a);
            }
        }

        public void m1118e() {
        }
    }

    static {
        f1488a = C0459c.ADS;
        f1489b = NativeAd.class.getSimpleName();
        f1490c = new WeakHashMap();
    }

    public NativeAd(Context context, C0448p c0448p, C0465d c0465d) {
        this(context, null);
        this.f1498k = c0465d;
        this.f1496i = true;
        this.f1497j = c0448p;
    }

    public NativeAd(Context context, String str) {
        this.f1500m = new ArrayList();
        this.f1491d = context;
        this.f1492e = str;
    }

    NativeAd(NativeAd nativeAd) {
        this(nativeAd.f1491d, null);
        this.f1498k = nativeAd.f1498k;
        this.f1496i = true;
        this.f1497j = nativeAd.f1497j;
    }

    private void m1121a(View view) {
        this.f1500m.add(view);
        view.setOnClickListener(this.f1504q);
        view.setOnTouchListener(this.f1504q);
    }

    private void m1122a(List<View> list, View view) {
        list.add(view);
        if ((view instanceof ViewGroup) && !(view instanceof C0579a)) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                m1122a((List) list, viewGroup.getChildAt(i));
            }
        }
    }

    private int m1125d() {
        return this.f1498k != null ? this.f1498k.m1370e() : this.f1497j != null ? this.f1497j.m1233f() : (this.f1495h == null || this.f1495h.m1422a() == null) ? 0 : this.f1495h.m1422a().m1371f();
    }

    public static void downloadAndDisplayImage(Image image, ImageView imageView) {
        if (image != null && imageView != null) {
            new C0526k(imageView).execute(new String[]{image.getUrl()});
        }
    }

    private int m1127e() {
        return this.f1498k != null ? this.f1498k.m1372g() : this.f1497j != null ? this.f1497j.m1234g() : (this.f1495h == null || this.f1495h.m1422a() == null) ? ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH : this.f1495h.m1422a().m1372g();
    }

    private void m1130f() {
        for (View view : this.f1500m) {
            view.setOnClickListener(null);
            view.setOnTouchListener(null);
        }
        this.f1500m.clear();
    }

    private void m1132g() {
        if (this.f1497j != null && this.f1497j.m1227a()) {
            this.f1505r = new C0421b();
            this.f1505r.m1114a();
            this.f1503p = new C0456o(this.f1491d, new C04184(this), this.f1497j);
        }
    }

    private int getMinViewabilityPercentage() {
        return this.f1498k != null ? this.f1498k.m1370e() : (this.f1495h == null || this.f1495h.m1422a() == null) ? 1 : this.f1495h.m1422a().m1370e();
    }

    private void logExternalClick(String str) {
        Map hashMap = new HashMap();
        hashMap.put("eil", Boolean.valueOf(true));
        hashMap.put("eil_source", str);
        this.f1497j.m1228b(hashMap);
    }

    private void logExternalImpression() {
        this.f1503p.m1180a();
    }

    private void registerExternalLogReceiver(String str) {
        this.f1503p = new C0456o(this.f1491d, new C04195(this, str), this.f1497j);
    }

    String m1139a() {
        return !isAdLoaded() ? null : this.f1497j.m1247t();
    }

    void m1140a(Type type) {
        this.f1507t = type;
    }

    void m1141a(boolean z) {
        this.f1508u = z;
    }

    String m1142b() {
        return !isAdLoaded() ? null : this.f1497j.m1248u();
    }

    void m1143b(boolean z) {
        this.f1509v = z;
    }

    String m1144c() {
        return !isAdLoaded() ? null : this.f1497j.m1249v();
    }

    public void destroy() {
        if (this.f1505r != null) {
            this.f1505r.m1115b();
            this.f1505r = null;
        }
        if (this.f1495h != null) {
            this.f1495h.m1428d();
            this.f1495h = null;
        }
    }

    public String getAdBody() {
        return !isAdLoaded() ? null : this.f1497j.m1240m();
    }

    public String getAdCallToAction() {
        return !isAdLoaded() ? null : this.f1497j.m1241n();
    }

    public Image getAdChoicesIcon() {
        return !isAdLoaded() ? null : this.f1497j.m1245r();
    }

    public String getAdChoicesLinkUrl() {
        return !isAdLoaded() ? null : this.f1497j.m1246s();
    }

    public Image getAdCoverImage() {
        return !isAdLoaded() ? null : this.f1497j.m1236i();
    }

    public Image getAdIcon() {
        return !isAdLoaded() ? null : this.f1497j.m1235h();
    }

    public String getAdSocialContext() {
        return !isAdLoaded() ? null : this.f1497j.m1242o();
    }

    public Rating getAdStarRating() {
        return !isAdLoaded() ? null : this.f1497j.m1243p();
    }

    public String getAdSubtitle() {
        return !isAdLoaded() ? null : this.f1497j.m1239l();
    }

    public String getAdTitle() {
        return !isAdLoaded() ? null : this.f1497j.m1238k();
    }

    public NativeAdViewAttributes getAdViewAttributes() {
        return !isAdLoaded() ? null : this.f1497j.m1237j();
    }

    public String getId() {
        return !isAdLoaded() ? null : this.f1497j.m1244q();
    }

    public boolean isAdLoaded() {
        return this.f1497j != null;
    }

    public boolean isNativeConfigEnabled() {
        return isAdLoaded() && this.f1497j.m1230c();
    }

    public void loadAd() {
        loadAd(EnumSet.of(MediaCacheFlag.NONE));
    }

    public void loadAd(EnumSet<MediaCacheFlag> enumSet) {
        if (this.f1496i) {
            throw new IllegalStateException("loadAd cannot be called more than once");
        }
        this.f1496i = true;
        this.f1495h = new C0486h(this.f1491d, this.f1492e, C0469e.NATIVE_UNKNOWN, null, f1488a, 1, true);
        this.f1495h.m1423a(new C04121(this, enumSet));
        this.f1495h.m1426b();
    }

    public void registerViewForInteraction(View view) {
        List arrayList = new ArrayList();
        m1122a(arrayList, view);
        registerViewForInteraction(view, arrayList);
    }

    public void registerViewForInteraction(View view, List<View> list) {
        if (view == null) {
            throw new IllegalArgumentException("Must provide a View");
        } else if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("Invalid set of clickable views");
        } else if (isAdLoaded()) {
            if (this.f1499l != null) {
                Log.w(f1489b, "Native Ad was already registered with a View. Auto unregistering and proceeding.");
                unregisterView();
            }
            if (f1490c.containsKey(view)) {
                Log.w(f1489b, "View already registered with a NativeAd. Auto unregistering and proceeding.");
                ((NativeAd) ((WeakReference) f1490c.get(view)).get()).unregisterView();
            }
            this.f1504q = new C0420a();
            this.f1499l = view;
            if (view instanceof ViewGroup) {
                this.f1506s = new C0567o(view.getContext(), new C04142(this));
                ((ViewGroup) view).addView(this.f1506s);
            }
            for (View a : list) {
                m1121a(a);
            }
            this.f1503p = new C0456o(this.f1491d, new C0422c(), this.f1497j);
            this.f1503p.m1345a((List) list);
            this.f1502o = new C0437e(this.f1491d, this.f1499l, getMinViewabilityPercentage(), new C04163(this));
            this.f1502o.m1196a(m1125d());
            this.f1502o.m1198b(m1127e());
            this.f1502o.m1195a();
            f1490c.put(view, new WeakReference(this));
        } else {
            Log.e(f1489b, "Ad not loaded");
        }
    }

    public void setAdListener(AdListener adListener) {
        this.f1493f = adListener;
    }

    @Deprecated
    public void setImpressionListener(ImpressionListener impressionListener) {
        this.f1494g = impressionListener;
    }

    public void setMediaViewAutoplay(boolean z) {
        this.f1510w = z;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.f1501n = onTouchListener;
    }

    public void unregisterView() {
        if (this.f1499l != null) {
            if (f1490c.containsKey(this.f1499l) && ((WeakReference) f1490c.get(this.f1499l)).get() == this) {
                if ((this.f1499l instanceof ViewGroup) && this.f1506s != null) {
                    ((ViewGroup) this.f1499l).removeView(this.f1506s);
                    this.f1506s = null;
                }
                f1490c.remove(this.f1499l);
                m1130f();
                this.f1499l = null;
                if (this.f1502o != null) {
                    this.f1502o.m1197b();
                    this.f1502o = null;
                }
                this.f1503p = null;
                return;
            }
            throw new IllegalStateException("View not registered with this NativeAd");
        }
    }
}
