package com.facebook.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0522g.C0521a;
import com.facebook.ads.internal.util.C0536s;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class AdSettings {
    public static final boolean DEBUG = false;
    static volatile boolean f1412a;
    private static final String f1413b;
    private static final Collection<String> f1414c;
    private static final Collection<String> f1415d;
    private static String f1416e;
    private static boolean f1417f;
    private static String f1418g;

    static {
        f1413b = AdSettings.class.getSimpleName();
        f1416e = null;
        f1417f = false;
        f1418g = null;
        f1414c = new HashSet();
        f1415d = new HashSet();
        f1415d.add(CommonUtils.SDK);
        f1415d.add(CommonUtils.GOOGLE_SDK);
        f1415d.add("vbox86p");
        f1415d.add("vbox86tp");
        f1412a = false;
    }

    private static void m1044a(String str) {
        if (!f1412a) {
            f1412a = true;
            Log.d(f1413b, "Test mode device hash: " + str);
            Log.d(f1413b, "When testing your app with Facebook's ad units you must specify the device hashed ID to ensure the delivery of test ads, add the following code before loading an ad: AdSettings.addTestDevice(\"" + str + "\");");
        }
    }

    public static void addTestDevice(String str) {
        f1414c.add(str);
    }

    public static void addTestDevices(Collection<String> collection) {
        f1414c.addAll(collection);
    }

    public static void clearTestDevices() {
        f1414c.clear();
    }

    public static String getUrlPrefix() {
        return f1416e;
    }

    public static boolean isChildDirected() {
        return f1417f;
    }

    public static boolean isTestMode(Context context) {
        if (f1415d.contains(Build.PRODUCT)) {
            return true;
        }
        if (f1418g == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("FBAdPrefs", 0);
            f1418g = sharedPreferences.getString("deviceIdHash", null);
            if (C0536s.m1572a(f1418g)) {
                C0521a a = C0522g.m1528a(context.getContentResolver());
                if (!C0536s.m1572a(a.f1911b)) {
                    f1418g = C0536s.m1573b(a.f1911b);
                } else if (C0536s.m1572a(a.f1910a)) {
                    f1418g = C0536s.m1573b(UUID.randomUUID().toString());
                } else {
                    f1418g = C0536s.m1573b(a.f1910a);
                }
                sharedPreferences.edit().putString("deviceIdHash", f1418g).apply();
            }
        }
        if (f1414c.contains(f1418g)) {
            return true;
        }
        m1044a(f1418g);
        return false;
    }

    public static void setIsChildDirected(boolean z) {
        f1417f = z;
    }

    public static void setUrlPrefix(String str) {
        f1416e = str;
    }
}
