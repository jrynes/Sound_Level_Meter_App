package com.mologiq.analytics;

import android.content.Context;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;

/* renamed from: com.mologiq.analytics.j */
final class MologiqDeviceEventsAsyncTask extends AsyncTask<Object, Object, Object> {
    private final WeakReference<Context> f2148a;

    MologiqDeviceEventsAsyncTask(Context context) {
        this.f2148a = new WeakReference(context);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final java.lang.Object doInBackground(java.lang.Object... r19) {
        /*
        r18 = this;
        r4 = 0;
        r0 = r18;
        r2 = r0.f2148a;	 Catch:{ Exception -> 0x02b0 }
        if (r2 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r18;
        r2 = r0.f2148a;	 Catch:{ Exception -> 0x02b0 }
        r2 = r2.get();	 Catch:{ Exception -> 0x02b0 }
        r2 = (android.content.Context) r2;	 Catch:{ Exception -> 0x02b0 }
        r4 = r2;
    L_0x0012:
        if (r4 != 0) goto L_0x0016;
    L_0x0014:
        r2 = 0;
    L_0x0015:
        return r2;
    L_0x0016:
        r8 = com.mologiq.analytics.UserSettings.m1806d(r4);	 Catch:{ Exception -> 0x02b0 }
        r2 = r8.m1813b();	 Catch:{ Exception -> 0x02b0 }
        if (r2 == 0) goto L_0x0022;
    L_0x0020:
        r2 = 0;
        goto L_0x0015;
    L_0x0022:
        r5 = com.mologiq.analytics.UserState.m1852b();	 Catch:{ Exception -> 0x02b0 }
        r5.m1768a(r4);	 Catch:{ Exception -> 0x02b0 }
        r2 = r5.m1770b(r4);	 Catch:{ Exception -> 0x02b0 }
        if (r2 != 0) goto L_0x0040;
    L_0x002f:
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x02b0 }
        r6 = r8.m1827o();	 Catch:{ Exception -> 0x02b0 }
        r2 = r2 - r6;
        r6 = r8.m1825m();	 Catch:{ Exception -> 0x02b0 }
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 <= 0) goto L_0x0294;
    L_0x0040:
        r6 = com.mologiq.analytics.UserSettings.m1806d(r4);	 Catch:{ Throwable -> 0x02a7 }
        r2 = com.mologiq.analytics.DatabaseHandler.m1725a(r4);	 Catch:{ Throwable -> 0x02a7 }
        r3 = r6.m1820h();	 Catch:{ Throwable -> 0x02a7 }
        if (r3 == 0) goto L_0x006e;
    L_0x004e:
        r3 = r2.m1746f();	 Catch:{ Throwable -> 0x02a7 }
        if (r3 == 0) goto L_0x0297;
    L_0x0054:
        r7 = r3.size();	 Catch:{ Throwable -> 0x02a7 }
        if (r7 <= 0) goto L_0x0297;
    L_0x005a:
        r10 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x02a7 }
        r12 = r6.m1828p();	 Catch:{ Throwable -> 0x02a7 }
        r10 = r10 - r12;
        r12 = r6.m1826n();	 Catch:{ Throwable -> 0x02a7 }
        r7 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r7 > 0) goto L_0x0297;
    L_0x006b:
        r5.m1866a(r3);	 Catch:{ Throwable -> 0x02a7 }
    L_0x006e:
        r2 = r2.m1744d();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2 + 1;
        r5.m1862a(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = 0;
        r3 = r6.m1830r();	 Catch:{ Throwable -> 0x02a7 }
        if (r3 == 0) goto L_0x0090;
    L_0x007e:
        r3 = r3.values();	 Catch:{ Throwable -> 0x02a7 }
        r7 = r3.iterator();	 Catch:{ Throwable -> 0x02a7 }
        r3 = r2;
    L_0x0087:
        r2 = r7.hasNext();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 != 0) goto L_0x02b9;
    L_0x008d:
        r5.m1870b(r3);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0090:
        r2 = r4.getPackageManager();	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r3 = r4.getPackageName();	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r7 = 0;
        r2 = r2.getApplicationInfo(r3, r7);	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r3 = r4.getApplicationContext();	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r3 = r3.getPackageManager();	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x00b4;
    L_0x00a7:
        if (r3 == 0) goto L_0x00b4;
    L_0x00a9:
        r2 = r3.getApplicationLabel(r2);	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
        r5.m1869b(r2);	 Catch:{ Exception -> 0x02d9, Throwable -> 0x02a7 }
    L_0x00b4:
        r2 = com.mologiq.analytics.AdvertisingId.m1714a(r4);	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        r3 = r2.m1716a();	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        r2 = r2.m1717b();	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        if (r3 == 0) goto L_0x02e1;
    L_0x00c2:
        r7 = r3.length();	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        if (r7 <= 0) goto L_0x02e1;
    L_0x00c8:
        r5.m1892i(r3);	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        r5.m1867a(r2);	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
    L_0x00ce:
        r2 = android.os.Build.VERSION.RELEASE;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1875c(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = android.os.Build.MODEL;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1880d(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = android.os.Build.DEVICE;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1884e(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = android.os.Build.MANUFACTURER;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1896k(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = android.os.Build.BRAND;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1888g(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = java.util.TimeZone.getDefault();	 Catch:{ Throwable -> 0x02a7 }
        r3 = java.util.Locale.getDefault();	 Catch:{ Throwable -> 0x02a7 }
        r3 = r2.getDisplayName(r3);	 Catch:{ Throwable -> 0x02a7 }
        if (r3 == 0) goto L_0x00f8;
    L_0x00f5:
        r5.m1898l(r3);	 Catch:{ Throwable -> 0x02a7 }
    L_0x00f8:
        r2 = r2.getID();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x0101;
    L_0x00fe:
        r5.m1900m(r2);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0101:
        r2 = java.util.Locale.getDefault();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getCountry();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1901n(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r4 instanceof android.app.Activity;	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x013c;
    L_0x0110:
        r2 = r4.getApplicationContext();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getResources();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getDisplayMetrics();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x013c;
    L_0x011e:
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02a7 }
        r7 = r2.widthPixels;	 Catch:{ Throwable -> 0x02a7 }
        r7 = java.lang.String.valueOf(r7);	 Catch:{ Throwable -> 0x02a7 }
        r3.<init>(r7);	 Catch:{ Throwable -> 0x02a7 }
        r7 = "*";
        r3 = r3.append(r7);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.heightPixels;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r3.append(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1904o(r2);	 Catch:{ Throwable -> 0x02a7 }
    L_0x013c:
        r2 = java.util.Locale.getDefault();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getDisplayLanguage();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1890h(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = "android.permission.ACCESS_WIFI_STATE";
        r2 = com.mologiq.analytics.Utils.m1925a(r4, r2);	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x018a;
    L_0x014f:
        r2 = r6.m1821i();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x018a;
    L_0x0155:
        r3 = new com.mologiq.analytics.v$b;	 Catch:{ Throwable -> 0x02a7 }
        r5.getClass();	 Catch:{ Throwable -> 0x02a7 }
        r3.<init>(r5);	 Catch:{ Throwable -> 0x02a7 }
        r2 = "wifi";
        r2 = r4.getSystemService(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = (android.net.wifi.WifiManager) r2;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getConnectionInfo();	 Catch:{ Throwable -> 0x02a7 }
        r7 = new com.mologiq.analytics.x;	 Catch:{ Throwable -> 0x02a7 }
        r7.<init>();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getSSID();	 Catch:{ Throwable -> 0x02a7 }
        r7.m1929a(r2);	 Catch:{ Throwable -> 0x02a7 }
        r3.m1851a(r7);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1864a(r3);	 Catch:{ Throwable -> 0x02a7 }
        r2 = "phone";
        r2 = r4.getSystemService(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = (android.telephony.TelephonyManager) r2;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getNetworkOperatorName();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1906p(r2);	 Catch:{ Throwable -> 0x02a7 }
    L_0x018a:
        r2 = com.mologiq.analytics.Version.m1682a();	 Catch:{ Throwable -> 0x02a7 }
        r2 = java.lang.String.valueOf(r2);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1886f(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r6.m1819g();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x0232;
    L_0x019b:
        r2 = "location";
        r2 = r4.getSystemService(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = (android.location.LocationManager) r2;	 Catch:{ Throwable -> 0x02a7 }
        r3 = 0;
        r7 = "android.permission.ACCESS_FINE_LOCATION";
        r7 = com.mologiq.analytics.Utils.m1925a(r4, r7);	 Catch:{ Throwable -> 0x02a7 }
        if (r7 == 0) goto L_0x01b2;
    L_0x01ac:
        r3 = "gps";
        r3 = r2.getLastKnownLocation(r3);	 Catch:{ Throwable -> 0x02a7 }
    L_0x01b2:
        if (r3 == 0) goto L_0x02fa;
    L_0x01b4:
        r10 = r3.getLatitude();	 Catch:{ Throwable -> 0x02a7 }
        r12 = r3.getLongitude();	 Catch:{ Throwable -> 0x02a7 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02a7 }
        r2.<init>();	 Catch:{ Throwable -> 0x02a7 }
        r7 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r14 = (double) r7;	 Catch:{ Throwable -> 0x02a7 }
        r14 = r14 * r10;
        r7 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.append(r7);	 Catch:{ Throwable -> 0x02a7 }
        r7 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r14 = (double) r7;	 Catch:{ Throwable -> 0x02a7 }
        r14 = r14 * r12;
        r7 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.append(r7);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x02a7 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02a7 }
        r7.<init>();	 Catch:{ Throwable -> 0x02a7 }
        r14 = r5.m1895k();	 Catch:{ Throwable -> 0x02a7 }
        r9 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r0 = (double) r9;	 Catch:{ Throwable -> 0x02a7 }
        r16 = r0;
        r14 = r14 * r16;
        r9 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r7 = r7.append(r9);	 Catch:{ Throwable -> 0x02a7 }
        r14 = r5.m1897l();	 Catch:{ Throwable -> 0x02a7 }
        r6 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r0 = (double) r6;	 Catch:{ Throwable -> 0x02a7 }
        r16 = r0;
        r14 = r14 * r16;
        r6 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r6 = r7.append(r6);	 Catch:{ Throwable -> 0x02a7 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x02a7 }
        r2 = r6.equals(r2);	 Catch:{ Throwable -> 0x02a7 }
        if (r2 != 0) goto L_0x0214;
    L_0x020e:
        r5.m1861a(r10);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1868b(r12);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0214:
        r2 = r3.getAccuracy();	 Catch:{ Throwable -> 0x02a7 }
        r6 = (double) r2;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1879d(r6);	 Catch:{ Throwable -> 0x02a7 }
        r6 = r3.getAltitude();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1873c(r6);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r3.getSpeed();	 Catch:{ Throwable -> 0x02a7 }
        r6 = (double) r2;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1883e(r6);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r3.getTime();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1863a(r2);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0232:
        r2 = "phone";
        r2 = r4.getSystemService(r2);	 Catch:{ Throwable -> 0x02a7 }
        r2 = (android.telephony.TelephonyManager) r2;	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getNetworkOperator();	 Catch:{ Throwable -> 0x02a7 }
        if (r2 == 0) goto L_0x0268;
    L_0x0240:
        r3 = r2.length();	 Catch:{ Throwable -> 0x02a7 }
        r6 = 4;
        if (r3 <= r6) goto L_0x0268;
    L_0x0247:
        r3 = 0;
        r6 = 3;
        r3 = r2.substring(r3, r6);	 Catch:{ Throwable -> 0x02a7 }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ Throwable -> 0x02a7 }
        r6 = 3;
        r2 = r2.substring(r6);	 Catch:{ Throwable -> 0x02a7 }
        r2 = java.lang.Integer.parseInt(r2);	 Catch:{ Throwable -> 0x02a7 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1908q(r3);	 Catch:{ Throwable -> 0x02a7 }
        r2 = java.lang.String.valueOf(r2);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1910r(r2);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0268:
        r2 = new com.mologiq.analytics.w;	 Catch:{ Exception -> 0x02b0 }
        r2.<init>(r4);	 Catch:{ Exception -> 0x02b0 }
        r2 = r8.m1832t();	 Catch:{ Exception -> 0x02b0 }
        if (r2 != 0) goto L_0x0393;
    L_0x0273:
        r2 = r8.m1814c();	 Catch:{ Exception -> 0x02b0 }
        r3 = "";
        r5 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = 0;
        r2 = com.mologiq.analytics.Utils.m1921a(r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x02b0 }
        if (r2 == 0) goto L_0x0294;
    L_0x0284:
        r3 = r2.length();	 Catch:{ Exception -> 0x02b0 }
        if (r3 <= 0) goto L_0x0294;
    L_0x028a:
        r2 = java.lang.Integer.parseInt(r2);	 Catch:{ Exception -> 0x02b0 }
        r8.m1808a(r2);	 Catch:{ Exception -> 0x02b0 }
        r8.m1770b(r4);	 Catch:{ Exception -> 0x02b0 }
    L_0x0294:
        r2 = 0;
        goto L_0x0015;
    L_0x0297:
        r10 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x02a7 }
        r6.m1812b(r10);	 Catch:{ Throwable -> 0x02a7 }
        r3 = r6.m1833u();	 Catch:{ Throwable -> 0x02a7 }
        r2.m1737a(r4, r3);	 Catch:{ Throwable -> 0x02a7 }
        goto L_0x006e;
    L_0x02a7:
        r2 = move-exception;
        r2 = com.mologiq.analytics.Utils.m1922a(r2);	 Catch:{ Exception -> 0x02b0 }
        com.mologiq.analytics.Utils.m1924a(r2);	 Catch:{ Exception -> 0x02b0 }
        goto L_0x0268;
    L_0x02b0:
        r2 = move-exception;
        r2 = com.mologiq.analytics.Utils.m1922a(r2);
        com.mologiq.analytics.Utils.m1924a(r2);
        goto L_0x0294;
    L_0x02b9:
        r2 = r7.next();	 Catch:{ Throwable -> 0x02a7 }
        r2 = (com.mologiq.analytics.AppUrlInfo) r2;	 Catch:{ Throwable -> 0x02a7 }
        if (r3 != 0) goto L_0x02c6;
    L_0x02c1:
        r3 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x02a7 }
        r3.<init>();	 Catch:{ Throwable -> 0x02a7 }
    L_0x02c6:
        r9 = r2.f2127b;	 Catch:{ Throwable -> 0x02a7 }
        r9 = com.mologiq.analytics.Utils.m1927b(r4, r9);	 Catch:{ Throwable -> 0x02a7 }
        if (r9 == 0) goto L_0x0087;
    L_0x02ce:
        r2 = r2.f2126a;	 Catch:{ Throwable -> 0x02a7 }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Throwable -> 0x02a7 }
        r3.add(r2);	 Catch:{ Throwable -> 0x02a7 }
        goto L_0x0087;
    L_0x02d9:
        r2 = move-exception;
        r2 = "Application name not found";
        com.mologiq.analytics.Utils.m1924a(r2);	 Catch:{ Throwable -> 0x02a7 }
        goto L_0x00b4;
    L_0x02e1:
        r2 = r4.getContentResolver();	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        r3 = "android_id";
        r2 = android.provider.Settings.Secure.getString(r2, r3);	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        r5.m1894j(r2);	 Catch:{ Exception -> 0x02f0, Throwable -> 0x02a7 }
        goto L_0x00ce;
    L_0x02f0:
        r2 = move-exception;
        r2 = com.mologiq.analytics.Utils.m1922a(r2);	 Catch:{ Throwable -> 0x02a7 }
        com.mologiq.analytics.Utils.m1924a(r2);	 Catch:{ Throwable -> 0x02a7 }
        goto L_0x00ce;
    L_0x02fa:
        r3 = 0;
        r7 = "android.permission.ACCESS_FINE_LOCATION";
        r7 = com.mologiq.analytics.Utils.m1925a(r4, r7);	 Catch:{ Throwable -> 0x02a7 }
        if (r7 != 0) goto L_0x030b;
    L_0x0303:
        r7 = "android.permission.ACCESS_COARSE_LOCATION";
        r7 = com.mologiq.analytics.Utils.m1925a(r4, r7);	 Catch:{ Throwable -> 0x02a7 }
        if (r7 == 0) goto L_0x03e5;
    L_0x030b:
        r3 = "network";
        r2 = r2.getLastKnownLocation(r3);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0311:
        if (r2 == 0) goto L_0x0232;
    L_0x0313:
        r10 = r2.getLatitude();	 Catch:{ Throwable -> 0x02a7 }
        r12 = r2.getLongitude();	 Catch:{ Throwable -> 0x02a7 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02a7 }
        r3.<init>();	 Catch:{ Throwable -> 0x02a7 }
        r7 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r14 = (double) r7;	 Catch:{ Throwable -> 0x02a7 }
        r14 = r14 * r10;
        r7 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r3 = r3.append(r7);	 Catch:{ Throwable -> 0x02a7 }
        r7 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r14 = (double) r7;	 Catch:{ Throwable -> 0x02a7 }
        r14 = r14 * r12;
        r7 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r3 = r3.append(r7);	 Catch:{ Throwable -> 0x02a7 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x02a7 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x02a7 }
        r7.<init>();	 Catch:{ Throwable -> 0x02a7 }
        r14 = r5.m1895k();	 Catch:{ Throwable -> 0x02a7 }
        r9 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r0 = (double) r9;	 Catch:{ Throwable -> 0x02a7 }
        r16 = r0;
        r14 = r14 * r16;
        r9 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r7 = r7.append(r9);	 Catch:{ Throwable -> 0x02a7 }
        r14 = r5.m1897l();	 Catch:{ Throwable -> 0x02a7 }
        r6 = r6.m1829q();	 Catch:{ Throwable -> 0x02a7 }
        r0 = (double) r6;	 Catch:{ Throwable -> 0x02a7 }
        r16 = r0;
        r14 = r14 * r16;
        r6 = (int) r14;	 Catch:{ Throwable -> 0x02a7 }
        r6 = r7.append(r6);	 Catch:{ Throwable -> 0x02a7 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x02a7 }
        r3 = r6.equals(r3);	 Catch:{ Throwable -> 0x02a7 }
        if (r3 != 0) goto L_0x0373;
    L_0x036d:
        r5.m1861a(r10);	 Catch:{ Throwable -> 0x02a7 }
        r5.m1868b(r12);	 Catch:{ Throwable -> 0x02a7 }
    L_0x0373:
        r3 = r2.getAccuracy();	 Catch:{ Throwable -> 0x02a7 }
        r6 = (double) r3;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1879d(r6);	 Catch:{ Throwable -> 0x02a7 }
        r6 = r2.getAltitude();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1873c(r6);	 Catch:{ Throwable -> 0x02a7 }
        r3 = r2.getSpeed();	 Catch:{ Throwable -> 0x02a7 }
        r6 = (double) r3;	 Catch:{ Throwable -> 0x02a7 }
        r5.m1883e(r6);	 Catch:{ Throwable -> 0x02a7 }
        r2 = r2.getTime();	 Catch:{ Throwable -> 0x02a7 }
        r5.m1863a(r2);	 Catch:{ Throwable -> 0x02a7 }
        goto L_0x0232;
    L_0x0393:
        r2 = r8.m1817e();	 Catch:{ Exception -> 0x02b0 }
        r6 = new com.mologiq.analytics.h;	 Catch:{ Exception -> 0x02b0 }
        r6.<init>();	 Catch:{ Exception -> 0x02b0 }
        r3 = "1.4.4";
        r6.m1750a(r3);	 Catch:{ Exception -> 0x02b0 }
        r3 = "2015-09-10";
        r6.m1751b(r3);	 Catch:{ Exception -> 0x02b0 }
        r3 = r4.getPackageName();	 Catch:{ Exception -> 0x02b0 }
        if (r3 != 0) goto L_0x03e0;
    L_0x03ac:
        r3 = "";
    L_0x03ae:
        r6.m1752c(r3);	 Catch:{ Exception -> 0x02b0 }
        r6.m1749a(r5);	 Catch:{ Exception -> 0x02b0 }
        r3 = r6.m1748a(r4);	 Catch:{ Exception -> 0x02b0 }
        if (r3 == 0) goto L_0x0294;
    L_0x03ba:
        r5 = r3.length();	 Catch:{ Exception -> 0x02b0 }
        if (r5 <= 0) goto L_0x0294;
    L_0x03c0:
        r5 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = 1;
        r2 = com.mologiq.analytics.Utils.m1921a(r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x02b0 }
        if (r2 == 0) goto L_0x0294;
    L_0x03cb:
        r3 = r2.length();	 Catch:{ Exception -> 0x02b0 }
        if (r3 <= 0) goto L_0x0294;
    L_0x03d1:
        r8.m1811a(r2, r4);	 Catch:{ Exception -> 0x02b0 }
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x02b0 }
        r8.m1809a(r2);	 Catch:{ Exception -> 0x02b0 }
        r8.m1770b(r4);	 Catch:{ Exception -> 0x02b0 }
        goto L_0x0294;
    L_0x03e0:
        r3 = r4.getPackageName();	 Catch:{ Exception -> 0x02b0 }
        goto L_0x03ae;
    L_0x03e5:
        r2 = r3;
        goto L_0x0311;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mologiq.analytics.j.doInBackground(java.lang.Object[]):java.lang.Object");
    }
}
