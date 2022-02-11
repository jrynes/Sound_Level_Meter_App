package com.admarvel.android.util.p000a;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import com.admarvel.android.ads.AdFetcher.Adtype;
import com.admarvel.android.ads.AdMarvelAd;
import com.admarvel.android.util.p000a.Reflection.Reflection;
import java.util.Map;

/* renamed from: com.admarvel.android.util.a.b */
public class OfflineReflectionUtils {
    public static String m531a(String str, String str2) {
        try {
            Object a = MethodBuilderFactory.m529a(null, "readData").m538a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils")).m539a(String.class, str).m539a(String.class, str2).m540a();
            return (a == null || !(a instanceof String)) ? null : (String) a;
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized void m532a() {
        synchronized (OfflineReflectionUtils.class) {
            try {
                MethodBuilderFactory.m529a(null, "disableNetworkActivity").m538a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils")).m540a();
            } catch (Exception e) {
            }
        }
    }

    public static void m533a(Activity activity, String str) {
        try {
            MethodBuilderFactory.m529a(null, "initializeOfflineSDK").m538a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils")).m539a(Activity.class, activity).m539a(String.class, str).m540a();
        } catch (Exception e) {
        }
    }

    public static synchronized void m534b(Activity activity, String str) {
        synchronized (OfflineReflectionUtils.class) {
            try {
                MethodBuilderFactory.m529a(null, "enableNetworkActivity").m538a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils")).m539a(Activity.class, activity).m539a(String.class, str).m540a();
            } catch (Exception e) {
            }
        }
    }

    public String m535a(Adtype adtype, Context context, String str, int i, String str2, Map<String, Object> map, String str3, String str4, int i2, String str5, boolean z, Handler handler, boolean z2) {
        try {
            Reflection a = MethodBuilderFactory.m529a(Class.forName("com.admarvel.android.offlinesdk.AdMarvelOfflineAdFetcher").newInstance(), "fetchAdFromLocal");
            a.m539a(Adtype.class, adtype);
            a.m539a(Context.class, context);
            a.m539a(String.class, str);
            a.m539a(Integer.TYPE, Integer.valueOf(i));
            a.m539a(String.class, str2);
            a.m539a(Map.class, map);
            a.m539a(String.class, str3);
            a.m539a(String.class, str4);
            a.m539a(Integer.TYPE, Integer.valueOf(i2));
            a.m539a(String.class, str5);
            a.m539a(Boolean.TYPE, Boolean.valueOf(z));
            a.m539a(Handler.class, handler);
            a.m539a(Boolean.TYPE, Boolean.valueOf(z2));
            Object a2 = a.m540a();
            return (a2 == null || !(a2 instanceof String)) ? null : (String) a2;
        } catch (Exception e) {
            return null;
        }
    }

    public void m536a(AdMarvelAd adMarvelAd, Context context, Handler handler) {
        try {
            Reflection a = MethodBuilderFactory.m529a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils").newInstance(), "firePixel");
            a.m539a(AdMarvelAd.class, adMarvelAd);
            a.m539a(Context.class, context);
            a.m539a(Handler.class, handler);
            a.m540a();
        } catch (Exception e) {
        }
    }

    public void m537a(String str, Context context, Handler handler) {
        try {
            Reflection a = MethodBuilderFactory.m529a(Class.forName("com.admarvel.android.offlinesdk.AdmarvelOfflineUtils").newInstance(), "firePixel");
            a.m539a(String.class, str);
            a.m539a(Context.class, context);
            a.m539a(Handler.class, handler);
            a.m540a();
        } catch (Exception e) {
        }
    }
}
