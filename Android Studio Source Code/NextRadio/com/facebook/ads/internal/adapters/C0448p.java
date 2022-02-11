package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.server.AdPlacementType;
import java.util.Map;

/* renamed from: com.facebook.ads.internal.adapters.p */
public abstract class C0448p implements AdAdapter {
    public abstract void m1224a(int i);

    public abstract void m1225a(Context context, C0457q c0457q, Map<String, Object> map);

    public abstract void m1226a(Map<String, Object> map);

    public abstract boolean m1227a();

    public abstract void m1228b(Map<String, Object> map);

    public abstract boolean m1229b();

    public abstract boolean m1230c();

    public abstract boolean m1231d();

    public abstract int m1232e();

    public abstract int m1233f();

    public abstract int m1234g();

    public final AdPlacementType getPlacementType() {
        return AdPlacementType.NATIVE;
    }

    public abstract Image m1235h();

    public abstract Image m1236i();

    public abstract NativeAdViewAttributes m1237j();

    public abstract String m1238k();

    public abstract String m1239l();

    public abstract String m1240m();

    public abstract String m1241n();

    public abstract String m1242o();

    public abstract Rating m1243p();

    public abstract String m1244q();

    public abstract Image m1245r();

    public abstract String m1246s();

    public abstract String m1247t();

    public abstract String m1248u();

    public abstract String m1249v();

    public abstract boolean m1250w();
}
