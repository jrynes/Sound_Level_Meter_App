package com.facebook.ads.internal.dto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.facebook.ads.internal.C0473f;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0522g;
import com.facebook.ads.internal.util.C0522g.C0521a;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.facebook.ads.internal.dto.f */
public class C0468f {
    public static final String f1737a;
    public static String f1738b;
    public static String f1739c;
    public static String f1740d;
    public static String f1741e;
    public static String f1742f;
    public static int f1743g;
    public static String f1744h;
    public static int f1745i;
    public static String f1746j;
    public static int f1747k;
    public static String f1748l;
    public static String f1749m;
    public static String f1750n;
    public static boolean f1751o;
    private static boolean f1752p;

    static {
        f1752p = false;
        f1737a = VERSION.RELEASE;
        f1738b = Stomp.EMPTY;
        f1739c = Stomp.EMPTY;
        f1740d = Stomp.EMPTY;
        f1741e = Stomp.EMPTY;
        f1742f = Stomp.EMPTY;
        f1743g = 0;
        f1744h = Stomp.EMPTY;
        f1745i = 0;
        f1746j = Stomp.EMPTY;
        f1747k = 0;
        f1748l = Stomp.EMPTY;
        f1749m = Stomp.EMPTY;
        f1750n = Stomp.EMPTY;
        f1751o = false;
    }

    public static synchronized void m1382a(Context context) {
        synchronized (C0468f.class) {
            if (!f1752p) {
                f1752p = true;
                C0468f.m1384c(context);
            }
            C0468f.m1385d(context);
        }
    }

    public static void m1383b(Context context) {
        if (f1752p) {
            try {
                C0521a a;
                C0473f a2;
                SharedPreferences sharedPreferences = context.getSharedPreferences("SDKIDFA", 0);
                if (sharedPreferences.contains("attributionId")) {
                    f1749m = sharedPreferences.getString("attributionId", Stomp.EMPTY);
                }
                if (sharedPreferences.contains("advertisingId")) {
                    f1750n = sharedPreferences.getString("advertisingId", Stomp.EMPTY);
                    f1751o = sharedPreferences.getBoolean("limitAdTracking", f1751o);
                }
                try {
                    a = C0522g.m1528a(context.getContentResolver());
                } catch (Throwable e) {
                    C0515c.m1515a(C0514b.m1512a(e, "Error retrieving attribution id from fb4a"));
                    a = null;
                }
                if (a != null) {
                    String str = a.f1910a;
                    if (str != null) {
                        f1749m = str;
                    }
                }
                try {
                    a2 = C0473f.m1391a(context, a);
                } catch (Throwable e2) {
                    C0515c.m1515a(C0514b.m1512a(e2, "Error retrieving advertising id from Google Play Services"));
                    a2 = null;
                }
                if (a2 != null) {
                    String a3 = a2.m1393a();
                    Boolean valueOf = Boolean.valueOf(a2.m1394b());
                    if (a3 != null) {
                        f1750n = a3;
                        f1751o = valueOf.booleanValue();
                    }
                }
                Editor edit = sharedPreferences.edit();
                edit.putString("attributionId", f1749m);
                edit.putString("advertisingId", f1750n);
                edit.putBoolean("limitAdTracking", f1751o);
                edit.apply();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private static void m1384c(Context context) {
        String networkOperatorName;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            f1740d = packageInfo.packageName;
            f1742f = packageInfo.versionName;
            f1743g = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        try {
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
            if (applicationLabel != null && applicationLabel.length() > 0) {
                f1741e = applicationLabel.toString();
            }
        } catch (NameNotFoundException e2) {
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            networkOperatorName = telephonyManager.getNetworkOperatorName();
            if (networkOperatorName != null && networkOperatorName.length() > 0) {
                f1744h = networkOperatorName;
            }
        }
        networkOperatorName = Build.MANUFACTURER;
        if (networkOperatorName != null && networkOperatorName.length() > 0) {
            f1738b = networkOperatorName;
        }
        networkOperatorName = Build.MODEL;
        if (networkOperatorName != null && networkOperatorName.length() > 0) {
            f1739c = Build.MODEL;
        }
    }

    private static void m1385d(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                f1745i = activeNetworkInfo.getType();
                f1746j = activeNetworkInfo.getTypeName();
                f1747k = activeNetworkInfo.getSubtype();
                f1748l = activeNetworkInfo.getSubtypeName();
            }
        } catch (Exception e) {
        }
    }
}
