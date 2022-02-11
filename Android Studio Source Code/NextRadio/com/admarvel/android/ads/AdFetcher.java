package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import com.admarvel.android.util.AdMarvelObfuscator;
import com.admarvel.android.util.GoogleAdvertisingIdClient;
import com.admarvel.android.util.Logging;
import com.admarvel.android.util.OptionalUtils;
import com.admarvel.android.util.p000a.OfflineReflectionUtils;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.json.JSONObject;

public class AdFetcher {
    private static final String ADMARVEL_ENDPOINT = "http://ads.admarvel.com/fam/androidGetAd.php";
    public static final String SDK_SUPPORTED;
    public static final String SDK_SUPPORTED_REWARD;
    public static String SDK_VERSION_INFO;
    public static String lastAdRequestPostString;
    private String googleAdID;
    private JSONObject requestJson;
    private int userOptOutPreference;

    public enum Adtype {
        BANNER,
        INTERSTITIAL,
        NATIVE
    }

    /* renamed from: com.admarvel.android.ads.AdFetcher.a */
    static class C0132a {
        @SuppressLint({"InlinedApi", "NewApi"})
        public static String m12a() {
            int i = 0;
            try {
                String[] strArr = Build.SUPPORTED_32_BIT_ABIS;
                StringBuilder stringBuilder;
                int length;
                if (strArr.length > 0) {
                    stringBuilder = new StringBuilder();
                    length = strArr.length;
                    while (i < length) {
                        stringBuilder.append(strArr[i]).append(Stomp.COMMA);
                        i++;
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    return stringBuilder.toString();
                }
                strArr = Build.SUPPORTED_64_BIT_ABIS;
                stringBuilder = new StringBuilder();
                length = strArr.length;
                while (i < length) {
                    stringBuilder.append(strArr[i]).append(Stomp.COMMA);
                    i++;
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                return stringBuilder.toString();
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                return null;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdFetcher.b */
    static class C0133b {
        @TargetApi(4)
        public static String m13a() {
            try {
                return Build.CPU_ABI;
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                return null;
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdFetcher.c */
    static class C0134c {
        @TargetApi(8)
        public static String m14a() {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String str = Build.CPU_ABI;
                String str2 = Build.CPU_ABI2;
                if ("x86".equals(str) || "x86".equals(str2)) {
                    stringBuilder.append("x86");
                }
                if ("armeabi-v7a".equals(str) || "armeabi-v7a".equals(str2)) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(Stomp.COMMA).append("armeabi-v7a");
                    } else {
                        stringBuilder.append("armeabi-v7a");
                    }
                }
                if ("armeabi".equals(str) || "armeabi".equals(str2)) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(Stomp.COMMA).append("armeabi");
                    } else {
                        stringBuilder.append("armeabi");
                    }
                }
                return stringBuilder.toString();
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                return null;
            }
        }
    }

    static {
        SDK_SUPPORTED = Version.getSDKSupported(false);
        SDK_SUPPORTED_REWARD = Version.getSDKSupported(true);
        SDK_VERSION_INFO = null;
    }

    public AdFetcher() {
        this.googleAdID = "VALUE_NOT_DEFINED";
        this.userOptOutPreference = ExploreByTouchHelper.INVALID_ID;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String buildParamString(com.admarvel.android.ads.AdFetcher.Adtype r10, android.content.Context r11, int r12, java.lang.String r13, java.util.Map<java.lang.String, java.lang.Object> r14, java.lang.String r15, java.lang.String r16, int r17, java.lang.String r18, boolean r19, boolean r20, boolean r21, java.util.Map<java.lang.String, java.lang.String> r22, java.lang.String r23, boolean r24, int r25, int r26, int r27, int r28, int r29, java.lang.String r30, boolean r31, boolean r32) {
        /*
        r9 = this;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r1 = "||";
        r5 = com.admarvel.android.ads.Utils.m185a(r14, r1);	 Catch:{ Exception -> 0x03f4 }
        r1 = "site_id";
        r0 = r16;
        com.admarvel.android.ads.Utils.m188a(r4, r1, r0);	 Catch:{ Exception -> 0x03f4 }
        r1 = "partner_id";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r15);	 Catch:{ Exception -> 0x03f4 }
        r1 = "timeout";
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r2 = java.lang.String.valueOf(r2);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "version";
        r2 = "1.5";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "language";
        r2 = "java";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "format";
        r2 = "android";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "sdk_version";
        r2 = "2.6.0";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "sdk_version_date";
        r2 = "2015-10-27";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        if (r21 == 0) goto L_0x03e9;
    L_0x0047:
        r1 = "sdk_supported";
        r2 = "";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x004e:
        r1 = "device_model";
        r2 = android.os.Build.MODEL;	 Catch:{ Exception -> 0x0400 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0400 }
        r1 = "device_name";
        r2 = android.os.Build.ID;	 Catch:{ Exception -> 0x0400 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0400 }
        r1 = "device_systemversion";
        r2 = android.os.Build.VERSION.RELEASE;	 Catch:{ Exception -> 0x0400 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0400 }
    L_0x0063:
        r1 = "device_api_version";
        r2 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ Exception -> 0x03f4 }
        r2 = java.lang.String.valueOf(r2);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "retrynum";
        r2 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x040a }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x040a }
    L_0x0079:
        r1 = "excluded_banners";
        r0 = r18;
        com.admarvel.android.ads.Utils.m188a(r4, r1, r0);	 Catch:{ Exception -> 0x03f4 }
        if (r30 == 0) goto L_0x008f;
    L_0x0082:
        r1 = r30.length();	 Catch:{ Exception -> 0x03f4 }
        if (r1 <= 0) goto L_0x008f;
    L_0x0088:
        r1 = "record_request_for_banner";
        r0 = r30;
        com.admarvel.android.ads.Utils.m188a(r4, r1, r0);	 Catch:{ Exception -> 0x03f4 }
    L_0x008f:
        r1 = 2;
        if (r12 != r1) goto L_0x0414;
    L_0x0092:
        r1 = "device_orientation";
        r2 = "landscape";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x041d }
    L_0x0099:
        r1 = "device_connectivity";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r13);	 Catch:{ Exception -> 0x03f4 }
        r3 = com.admarvel.android.ads.Utils.m222m(r11);	 Catch:{ Exception -> 0x03f4 }
        if (r3 <= 0) goto L_0x00b6;
    L_0x00a4:
        r1 = "resolution_width";
        r2 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "max_image_width";
        r2 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x00b6:
        r6 = com.admarvel.android.ads.Utils.m224n(r11);	 Catch:{ Exception -> 0x03f4 }
        if (r6 <= 0) goto L_0x00ce;
    L_0x00bc:
        r1 = "resolution_height";
        r2 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "max_image_height";
        r2 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x00ce:
        r1 = "device_density";
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x03f4 }
        r2.<init>();	 Catch:{ Exception -> 0x03f4 }
        r7 = "";
        r2 = r2.append(r7);	 Catch:{ Exception -> 0x03f4 }
        r7 = com.admarvel.android.ads.Utils.m226o(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = r2.append(r7);	 Catch:{ Exception -> 0x03f4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "device_os";
        r2 = "Android";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = com.admarvel.android.ads.AdFetcher.Adtype.BANNER;	 Catch:{ Exception -> 0x03f4 }
        r1 = r10.equals(r1);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0430;
    L_0x00f9:
        if (r31 == 0) goto L_0x0427;
    L_0x00fb:
        if (r32 == 0) goto L_0x0427;
    L_0x00fd:
        r1 = "adtype";
        r2 = "banner|native";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x0104:
        r1 = 2;
        if (r12 != r1) goto L_0x0452;
    L_0x0107:
        r1 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x045f }
        r2 = r1;
    L_0x010c:
        r1 = 2;
        if (r12 != r1) goto L_0x0459;
    L_0x010f:
        r1 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x045f }
    L_0x0113:
        r3 = "device_details";
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x045f }
        r6.<init>();	 Catch:{ Exception -> 0x045f }
        r7 = "brand:";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x045f }
        r7 = android.os.Build.BRAND;	 Catch:{ Exception -> 0x045f }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x045f }
        r7 = ",model:";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x045f }
        r7 = android.os.Build.MODEL;	 Catch:{ Exception -> 0x045f }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x045f }
        r7 = ",width:";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x045f }
        r2 = r6.append(r2);	 Catch:{ Exception -> 0x045f }
        r6 = ",height:";
        r2 = r2.append(r6);	 Catch:{ Exception -> 0x045f }
        r1 = r2.append(r1);	 Catch:{ Exception -> 0x045f }
        r2 = ",os:";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x045f }
        r2 = android.os.Build.VERSION.RELEASE;	 Catch:{ Exception -> 0x045f }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x045f }
        r2 = ",ua:";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x045f }
        r2 = com.admarvel.android.ads.Utils.m241w(r11);	 Catch:{ Exception -> 0x045f }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x045f }
        r1 = r1.toString();	 Catch:{ Exception -> 0x045f }
        com.admarvel.android.ads.Utils.m188a(r4, r3, r1);	 Catch:{ Exception -> 0x045f }
    L_0x0167:
        r1 = com.admarvel.android.ads.Utils.m189a();	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0469;
    L_0x016d:
        r1 = "isKindle";
        r2 = "true";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x0174:
        r1 = com.admarvel.android.ads.Utils.m201c();	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0472;
    L_0x017a:
        r1 = "jb";
        r2 = "1";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x0181:
        r1 = com.admarvel.android.ads.AdMarvelUtils.isCustomExpandEnable;	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x018c;
    L_0x0185:
        r1 = "custom_expand_allowed";
        r2 = "1";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x018c:
        r1 = com.admarvel.android.ads.Utils.m229p(r11);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x01ae;
    L_0x0192:
        r2 = r1.length;	 Catch:{ Exception -> 0x03f4 }
        r3 = 2;
        if (r2 != r3) goto L_0x01ae;
    L_0x0196:
        r2 = "battery_value";
        r3 = 0;
        r3 = r1[r3];	 Catch:{ Exception -> 0x03f4 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r2, r3);	 Catch:{ Exception -> 0x03f4 }
        r2 = "charging";
        r3 = 1;
        r1 = r1[r3];	 Catch:{ Exception -> 0x03f4 }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x01ae:
        r1 = com.admarvel.android.ads.AdMarvelUtils.getAppName(r11);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x01bf;
    L_0x01b4:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x01bf;
    L_0x01ba:
        r2 = "app_name";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x01bf:
        if (r11 == 0) goto L_0x047b;
    L_0x01c1:
        r1 = r11.getPackageName();	 Catch:{ Exception -> 0x03f4 }
    L_0x01c5:
        if (r1 == 0) goto L_0x01d2;
    L_0x01c7:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x01d2;
    L_0x01cd:
        r2 = "app_identifier";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x01d2:
        r1 = r11 instanceof android.app.Activity;	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0235;
    L_0x01d6:
        r0 = r11;
        r0 = (android.app.Activity) r0;	 Catch:{ Exception -> 0x03f4 }
        r1 = r0;
        if (r1 == 0) goto L_0x0235;
    L_0x01dc:
        r1 = r1.getWindow();	 Catch:{ Exception -> 0x0499 }
        r2 = 16908290; // 0x1020002 float:2.3877235E-38 double:8.353805E-317;
        r1 = r1.findViewById(r2);	 Catch:{ Exception -> 0x0499 }
        r1 = (android.view.ViewGroup) r1;	 Catch:{ Exception -> 0x0499 }
        r2 = 0;
        r3 = java.lang.Boolean.valueOf(r2);	 Catch:{ Exception -> 0x0499 }
        r2 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ Exception -> 0x0499 }
        r6 = 11;
        if (r2 < r6) goto L_0x022c;
    L_0x01f6:
        r2 = com.admarvel.android.ads.AdFetcher.Adtype.BANNER;	 Catch:{ Exception -> 0x0499 }
        r2 = r10.equals(r2);	 Catch:{ Exception -> 0x0499 }
        if (r2 == 0) goto L_0x048a;
    L_0x01fe:
        r2 = android.view.ViewGroup.class;
        r6 = "isHardwareAccelerated";
        r7 = 0;
        r7 = new java.lang.Class[r7];	 Catch:{ Exception -> 0x047e }
        r6 = r2.getMethod(r6, r7);	 Catch:{ Exception -> 0x047e }
        r2 = 0;
        r2 = (java.lang.Object[]) r2;	 Catch:{ Exception -> 0x047e }
        r1 = r6.invoke(r1, r2);	 Catch:{ Exception -> 0x047e }
        if (r1 == 0) goto L_0x0599;
    L_0x0212:
        r2 = r1 instanceof java.lang.Boolean;	 Catch:{ Exception -> 0x047e }
        if (r2 == 0) goto L_0x0599;
    L_0x0216:
        r1 = (java.lang.Boolean) r1;	 Catch:{ Exception -> 0x047e }
    L_0x0218:
        r2 = r1.booleanValue();	 Catch:{ Exception -> 0x0593 }
        if (r2 == 0) goto L_0x022b;
    L_0x021e:
        if (r19 == 0) goto L_0x022b;
    L_0x0220:
        r2 = com.admarvel.android.ads.Utils.m198b();	 Catch:{ Exception -> 0x0593 }
        if (r2 == 0) goto L_0x022b;
    L_0x0226:
        r2 = 0;
        r1 = java.lang.Boolean.valueOf(r2);	 Catch:{ Exception -> 0x0593 }
    L_0x022b:
        r3 = r1;
    L_0x022c:
        r1 = "hardware_accelerated";
        r2 = r3.toString();	 Catch:{ Exception -> 0x0499 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0499 }
    L_0x0235:
        if (r14 == 0) goto L_0x024c;
    L_0x0237:
        r1 = "ANDROID_ADVERTISING_ID";
        r1 = r14.get(r1);	 Catch:{ Exception -> 0x03f4 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x024c;
    L_0x0241:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x024c;
    L_0x0247:
        r2 = "android_advertising_id";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x024c:
        r1 = com.admarvel.android.ads.AdMarvelUtils.isTabletDevice(r11);	 Catch:{ Exception -> 0x03f4 }
        if (r1 != 0) goto L_0x04a3;
    L_0x0252:
        r1 = "auto-scaling";
        r2 = "true";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "is_tablet";
        r2 = "false";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x0260:
        r1 = com.admarvel.android.ads.Utils.m207e(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "unknown-network";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x0279;
    L_0x026c:
        r2 = "null";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x0279;
    L_0x0274:
        r2 = "carriername";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x0279:
        r1 = com.admarvel.android.ads.Utils.m208f(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x0292;
    L_0x0285:
        r2 = "null";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x0292;
    L_0x028d:
        r2 = "simcode";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x0292:
        r1 = com.admarvel.android.ads.Utils.m204d(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02ab;
    L_0x029e:
        r2 = "null";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02ab;
    L_0x02a6:
        r2 = "networkcode";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x02ab:
        r1 = com.admarvel.android.ads.Utils.m200c(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "unknown-network";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02c4;
    L_0x02b7:
        r2 = "null";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02c4;
    L_0x02bf:
        r2 = "networkcarriername";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x02c4:
        r1 = com.admarvel.android.ads.Utils.m210g(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "roaming";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
        r1 = com.admarvel.android.ads.Utils.m214i(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02de;
    L_0x02d9:
        r2 = "netwrokcarriercountryiso";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x02de:
        r1 = com.admarvel.android.ads.Utils.m212h(r11);	 Catch:{ Exception -> 0x03f4 }
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x03f4 }
        if (r2 != 0) goto L_0x02ef;
    L_0x02ea:
        r2 = "carriercountryiso";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x02ef:
        r1 = "";
        r1 = com.admarvel.android.ads.Utils.m195b(r11);	 Catch:{ Exception -> 0x04b3 }
        r2 = "";
        r2 = r1.equals(r2);	 Catch:{ Exception -> 0x04b3 }
        if (r2 != 0) goto L_0x0302;
    L_0x02fd:
        r2 = "radiotech";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x04b3 }
    L_0x0302:
        if (r21 == 0) goto L_0x030b;
    L_0x0304:
        r1 = "force_pixel_track";
        r2 = "true";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x030b:
        r1 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ Exception -> 0x03f4 }
        r2 = 21;
        if (r1 < r2) goto L_0x04bd;
    L_0x0313:
        r1 = com.admarvel.android.ads.AdFetcher.C0132a.m12a();	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0324;
    L_0x0319:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x0324;
    L_0x031f:
        r2 = "device_arc";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x0324:
        if (r24 == 0) goto L_0x0514;
    L_0x0326:
        r1 = "reward";
        r2 = "1";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        if (r23 == 0) goto L_0x04f3;
    L_0x032f:
        r1 = r23.length();	 Catch:{ Exception -> 0x03f4 }
        if (r1 <= 0) goto L_0x04f3;
    L_0x0335:
        r1 = "reward_user_id";
        r0 = r23;
        com.admarvel.android.ads.Utils.m188a(r4, r1, r0);	 Catch:{ Exception -> 0x03f4 }
    L_0x033c:
        r1 = "||";
        r0 = r22;
        r1 = com.admarvel.android.ads.Utils.m197b(r0, r1);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0351;
    L_0x0346:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x0351;
    L_0x034c:
        r2 = "reward_params";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
    L_0x0351:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x0368;
    L_0x0355:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        r2 = "adcolony";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x0368;
    L_0x035f:
        r1 = "adcsdk";
        r2 = java.lang.Integer.toString(r25);	 Catch:{ Exception -> 0x050a }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x050a }
    L_0x0368:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x037f;
    L_0x036c:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        r2 = "unityads";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x037f;
    L_0x0376:
        r1 = "uadsdk";
        r2 = java.lang.Integer.toString(r26);	 Catch:{ Exception -> 0x050a }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x050a }
    L_0x037f:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x0396;
    L_0x0383:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        r2 = "vungle";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x0396;
    L_0x038d:
        r1 = "vusdk";
        r2 = java.lang.Integer.toString(r27);	 Catch:{ Exception -> 0x050a }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x050a }
    L_0x0396:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x03ad;
    L_0x039a:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x050a }
        r2 = "chartboost";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x050a }
        if (r1 == 0) goto L_0x03ad;
    L_0x03a4:
        r1 = "cbsdk";
        r2 = java.lang.Integer.toString(r28);	 Catch:{ Exception -> 0x050a }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x050a }
    L_0x03ad:
        r1 = SDK_VERSION_INFO;	 Catch:{ Exception -> 0x03f4 }
        if (r1 != 0) goto L_0x03b7;
    L_0x03b1:
        r1 = com.admarvel.android.ads.Version.getAdNetworkVersionInfo(r11);	 Catch:{ Exception -> 0x03f4 }
        SDK_VERSION_INFO = r1;	 Catch:{ Exception -> 0x03f4 }
    L_0x03b7:
        r1 = SDK_VERSION_INFO;	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x03d7;
    L_0x03bb:
        r1 = SDK_VERSION_INFO;	 Catch:{ Exception -> 0x03f4 }
        r1 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r1 <= 0) goto L_0x03d7;
    L_0x03c3:
        r1 = "plgn";
        r2 = SDK_VERSION_INFO;	 Catch:{ Exception -> 0x03f4 }
        r3 = 0;
        r6 = SDK_VERSION_INFO;	 Catch:{ Exception -> 0x03f4 }
        r6 = r6.length();	 Catch:{ Exception -> 0x03f4 }
        r6 = r6 + -1;
        r2 = r2.substring(r3, r6);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
    L_0x03d7:
        if (r5 == 0) goto L_0x03e4;
    L_0x03d9:
        r1 = r5.length();	 Catch:{ Exception -> 0x03f4 }
        if (r1 <= 0) goto L_0x03e4;
    L_0x03df:
        r1 = "target_params";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r5);	 Catch:{ Exception -> 0x03f4 }
    L_0x03e4:
        r1 = r4.toString();	 Catch:{ Exception -> 0x03f4 }
    L_0x03e8:
        return r1;
    L_0x03e9:
        if (r24 == 0) goto L_0x03f7;
    L_0x03eb:
        r1 = "sdk_supported";
        r2 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x004e;
    L_0x03f4:
        r1 = move-exception;
        r1 = 0;
        goto L_0x03e8;
    L_0x03f7:
        r1 = "sdk_supported";
        r2 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x004e;
    L_0x0400:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0063;
    L_0x040a:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0079;
    L_0x0414:
        r1 = "device_orientation";
        r2 = "portrait";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x041d }
        goto L_0x0099;
    L_0x041d:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0099;
    L_0x0427:
        r1 = "adtype";
        r2 = "banner";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0104;
    L_0x0430:
        r1 = com.admarvel.android.ads.AdFetcher.Adtype.INTERSTITIAL;	 Catch:{ Exception -> 0x03f4 }
        r1 = r10.equals(r1);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0441;
    L_0x0438:
        r1 = "adtype";
        r2 = "interstitial";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0104;
    L_0x0441:
        r1 = com.admarvel.android.ads.AdFetcher.Adtype.NATIVE;	 Catch:{ Exception -> 0x03f4 }
        r1 = r10.equals(r1);	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0104;
    L_0x0449:
        r1 = "adtype";
        r2 = "native";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0104;
    L_0x0452:
        r1 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x045f }
        r2 = r1;
        goto L_0x010c;
    L_0x0459:
        r1 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x045f }
        goto L_0x0113;
    L_0x045f:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0167;
    L_0x0469:
        r1 = "isKindle";
        r2 = "false";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0174;
    L_0x0472:
        r1 = "jb";
        r2 = "0";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0181;
    L_0x047b:
        r1 = 0;
        goto L_0x01c5;
    L_0x047e:
        r1 = move-exception;
        r2 = r3;
    L_0x0480:
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x0499 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x0499 }
        r3 = r2;
        goto L_0x022c;
    L_0x048a:
        r1 = com.admarvel.android.ads.AdFetcher.Adtype.INTERSTITIAL;	 Catch:{ Exception -> 0x0499 }
        r1 = r10.equals(r1);	 Catch:{ Exception -> 0x0499 }
        if (r1 == 0) goto L_0x022c;
    L_0x0492:
        r1 = 1;
        r3 = java.lang.Boolean.valueOf(r1);	 Catch:{ Exception -> 0x0499 }
        goto L_0x022c;
    L_0x0499:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0235;
    L_0x04a3:
        r1 = "auto-scaling";
        r2 = "false";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        r1 = "is_tablet";
        r2 = "true";
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0260;
    L_0x04b3:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0302;
    L_0x04bd:
        r1 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ Exception -> 0x03f4 }
        r2 = 8;
        if (r1 < r2) goto L_0x04e0;
    L_0x04c5:
        r1 = com.admarvel.android.ads.Version.getAndroidSDKVersion();	 Catch:{ Exception -> 0x03f4 }
        r2 = 20;
        if (r1 > r2) goto L_0x04e0;
    L_0x04cd:
        r1 = com.admarvel.android.ads.AdFetcher.C0134c.m14a();	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0324;
    L_0x04d3:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x0324;
    L_0x04d9:
        r2 = "device_arc";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0324;
    L_0x04e0:
        r1 = com.admarvel.android.ads.AdFetcher.C0133b.m13a();	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x0324;
    L_0x04e6:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x0324;
    L_0x04ec:
        r2 = "device_arc";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x0324;
    L_0x04f3:
        r1 = "UNIQUE_ID";
        r1 = r14.get(r1);	 Catch:{ Exception -> 0x03f4 }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x03f4 }
        if (r1 == 0) goto L_0x033c;
    L_0x04fd:
        r2 = r1.length();	 Catch:{ Exception -> 0x03f4 }
        if (r2 <= 0) goto L_0x033c;
    L_0x0503:
        r2 = "reward_user_id";
        com.admarvel.android.ads.Utils.m188a(r4, r2, r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x033c;
    L_0x050a:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x03ad;
    L_0x0514:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x052b;
    L_0x0518:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        r2 = "adcolony";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x052b;
    L_0x0522:
        r1 = "adcsdk";
        r2 = java.lang.Integer.toString(r25);	 Catch:{ Exception -> 0x0589 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0589 }
    L_0x052b:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0542;
    L_0x052f:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        r2 = "unityads";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0542;
    L_0x0539:
        r1 = "uadsdk";
        r2 = java.lang.Integer.toString(r26);	 Catch:{ Exception -> 0x0589 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0589 }
    L_0x0542:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0559;
    L_0x0546:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        r2 = "vungle";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0559;
    L_0x0550:
        r1 = "vusdk";
        r2 = java.lang.Integer.toString(r27);	 Catch:{ Exception -> 0x0589 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0589 }
    L_0x0559:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0570;
    L_0x055d:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        r2 = "chartboost";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x0570;
    L_0x0567:
        r1 = "cbsdk";
        r2 = java.lang.Integer.toString(r28);	 Catch:{ Exception -> 0x0589 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0589 }
    L_0x0570:
        r1 = SDK_SUPPORTED_REWARD;	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x03ad;
    L_0x0574:
        r1 = SDK_SUPPORTED;	 Catch:{ Exception -> 0x0589 }
        r2 = "yume";
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0589 }
        if (r1 == 0) goto L_0x03ad;
    L_0x057e:
        r1 = "ymsdk";
        r2 = java.lang.Integer.toString(r29);	 Catch:{ Exception -> 0x0589 }
        com.admarvel.android.ads.Utils.m188a(r4, r1, r2);	 Catch:{ Exception -> 0x0589 }
        goto L_0x03ad;
    L_0x0589:
        r1 = move-exception;
        r1 = android.util.Log.getStackTraceString(r1);	 Catch:{ Exception -> 0x03f4 }
        com.admarvel.android.util.Logging.log(r1);	 Catch:{ Exception -> 0x03f4 }
        goto L_0x03ad;
    L_0x0593:
        r2 = move-exception;
        r8 = r2;
        r2 = r1;
        r1 = r8;
        goto L_0x0480;
    L_0x0599:
        r1 = r3;
        goto L_0x0218;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.ads.AdFetcher.buildParamString(com.admarvel.android.ads.AdFetcher$Adtype, android.content.Context, int, java.lang.String, java.util.Map, java.lang.String, java.lang.String, int, java.lang.String, boolean, boolean, boolean, java.util.Map, java.lang.String, boolean, int, int, int, int, int, java.lang.String, boolean, boolean):java.lang.String");
    }

    private String fetchAdFromLocal(Adtype adtype, Context context, String androidId, int orientation, String deviceConnectivity, Map<String, Object> targetParams, String partnerId, String siteId, int retryNum, String excludedBanners, boolean setSoftwareLayer) {
        return new OfflineReflectionUtils().m535a(adtype, context, androidId, orientation, deviceConnectivity, targetParams, partnerId, siteId, retryNum, excludedBanners, false, new Handler(), setSoftwareLayer);
    }

    public String fetchAd(Adtype adtype, Context context, String androidId, int orientation, String deviceConnectivity, Map<String, Object> targetParams, String partnerId, String siteId, int retryNum, String excludedBanners, boolean setSoftwareLayer, boolean enableAutoScaling, boolean isBannerFetchModel, AdMarvelNetworkHandler adMarvelNetworkHandler, Map<String, String> rewardParams, String userId, boolean isRewardInterstitial, int adColonyAvailabilityStatus, int unityAdsAvailabilityStatus, int vungleAvailabilityStatus, int chartboostAvailabilityStatus, int yumeAvailabilityStatus, String recordRequestForBanner, boolean isBannerNativeRequest, boolean isBannerNativeListenersAvailable) {
        String generateRequestParams = generateRequestParams(adtype, context, androidId, orientation, deviceConnectivity, targetParams, partnerId, siteId, retryNum, excludedBanners, setSoftwareLayer, enableAutoScaling, isBannerFetchModel, adMarvelNetworkHandler, rewardParams, userId, isRewardInterstitial, adColonyAvailabilityStatus, unityAdsAvailabilityStatus, vungleAvailabilityStatus, chartboostAvailabilityStatus, yumeAvailabilityStatus, recordRequestForBanner, isBannerNativeRequest, isBannerNativeListenersAvailable);
        if (generateRequestParams == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Utils.m188a(stringBuilder, "v", Version.SDK_VERSION);
            Utils.m188a(stringBuilder, "p", generateRequestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdMarvelHttpPost adMarvelHttpPost = new AdMarvelHttpPost();
        adMarvelHttpPost.setEndpointUrl(ADMARVEL_ENDPOINT);
        if (stringBuilder.toString().length() > 0) {
            adMarvelHttpPost.setPostString(stringBuilder.toString());
        }
        Map hashMap = new HashMap();
        hashMap.put(HttpRequest.HEADER_USER_AGENT, Utils.m241w(context));
        hashMap.put(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
        hashMap.put("Accept-Language", Utils.m203d());
        adMarvelHttpPost.setHttpHeaders(hashMap);
        if (adMarvelNetworkHandler == null) {
            adMarvelNetworkHandler = new AdMarvelNetworkHandlerImpl();
        }
        generateRequestParams = adMarvelNetworkHandler.executeNetworkCall(adMarvelHttpPost);
        if (isRewardInterstitial) {
            try {
                generateRequestParams = new AdMarvelObfuscator().m577b(generateRequestParams);
            } catch (Exception e2) {
                generateRequestParams = null;
                Logging.log("Admarvel XML Response: Invalid RewardInterstitial Ad response  ");
            }
        }
        if (generateRequestParams == null || generateRequestParams.length() <= 0) {
            Logging.log("Admarvel XML Response: Invalid response");
            return generateRequestParams;
        }
        Logging.log("Admarvel XML Response:" + new String(generateRequestParams));
        return generateRequestParams;
    }

    public String fetchOfflineAd(Adtype adtype, Context context, String androidId, int orientation, String deviceConnectivity, Map<String, Object> targetParams, String partnerId, String siteId, int retryNum, String excludedBanners, boolean setSoftwareLayer, boolean enableAutoScaling) {
        if (context == null) {
            return null;
        }
        Map hashMap = new HashMap();
        if (targetParams != null) {
            synchronized (targetParams) {
                hashMap.putAll(targetParams);
            }
        }
        try {
            hashMap.put("RESPONSE_TYPE", "xml_with_xhtml");
            if (Version.getAndroidSDKVersion() > 8) {
                GoogleAdvertisingIdClient c = GoogleAdvertisingIdClient.m616c(context);
                this.googleAdID = c.m622b(context);
                this.userOptOutPreference = c.m619a(context);
                if (this.googleAdID == null || this.googleAdID.equals("VALUE_NOT_DEFINED")) {
                    hashMap.put("ANDROID_ID", OptionalUtils.getId(context));
                } else {
                    hashMap.put("ANDROID_ADVERTISING_ID", this.googleAdID);
                    hashMap.put("ANDROID_ADVERTISING_ID_OPT_OUT", Integer.toString(this.userOptOutPreference));
                }
            } else {
                hashMap.put("ANDROID_ID", OptionalUtils.getId(context));
            }
            if (androidId == null) {
                androidId = this.googleAdID;
            }
            if (hashMap.get("UNIQUE_ID") == null) {
                if (this.googleAdID == null || this.googleAdID.equals("VALUE_NOT_DEFINED")) {
                    hashMap.put("UNIQUE_ID", OptionalUtils.getId(context));
                } else {
                    hashMap.put("UNIQUE_ID", this.googleAdID);
                }
            }
            return fetchAdFromLocal(adtype, context, androidId, orientation, deviceConnectivity, targetParams, partnerId, siteId, retryNum, excludedBanners, setSoftwareLayer);
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return null;
        }
    }

    public String generateRequestParams(Adtype adtype, Context context, String androidId, int orientation, String deviceConnectivity, Map<String, Object> targetParams, String partnerId, String siteId, int retryNum, String excludedBanners, boolean setSoftwareLayer, boolean enableAutoScaling, boolean isBannerFetchModel, AdMarvelNetworkHandler adMarvelNetworkHandler, Map<String, String> rewardParams, String userId, boolean isRewardInterstitial, int adColonyAvailabilityStatus, int unityAdsAvailabilityStatus, int vungleAvailabilityStatus, int chartboostAvailabilityStatus, int yumeAvailabilityStatus, String recordRequestForBanner, boolean isBannerNativeRequest, boolean isBannerNativeListenersAvailable) {
        Map hashMap = new HashMap();
        if (context == null) {
            return null;
        }
        if (targetParams != null) {
            synchronized (targetParams) {
                hashMap.putAll(targetParams);
            }
        }
        try {
            hashMap.put("RESPONSE_TYPE", "xml_with_xhtml");
            if (Version.getAndroidSDKVersion() > 8) {
                GoogleAdvertisingIdClient c = GoogleAdvertisingIdClient.m616c(context);
                this.googleAdID = c.m622b(context);
                this.userOptOutPreference = c.m619a(context);
                if (this.googleAdID == null || this.googleAdID.equals("VALUE_NOT_DEFINED")) {
                    hashMap.put("ANDROID_ID", OptionalUtils.getId(context));
                } else {
                    hashMap.put("ANDROID_ADVERTISING_ID", this.googleAdID);
                    hashMap.put("ANDROID_ADVERTISING_ID_OPT_OUT", Integer.toString(this.userOptOutPreference));
                }
            } else {
                hashMap.put("ANDROID_ID", OptionalUtils.getId(context));
            }
            if (hashMap.get("UNIQUE_ID") == null) {
                if (this.googleAdID == null || this.googleAdID.equals("VALUE_NOT_DEFINED")) {
                    hashMap.put("UNIQUE_ID", OptionalUtils.getId(context));
                } else {
                    hashMap.put("UNIQUE_ID", this.googleAdID);
                }
            }
            String str = Stomp.EMPTY;
            String buildParamString = buildParamString(adtype, context, orientation, deviceConnectivity, hashMap, partnerId, siteId, retryNum, excludedBanners, setSoftwareLayer, enableAutoScaling, isBannerFetchModel, rewardParams, userId, isRewardInterstitial, adColonyAvailabilityStatus, unityAdsAvailabilityStatus, vungleAvailabilityStatus, chartboostAvailabilityStatus, yumeAvailabilityStatus, recordRequestForBanner, isBannerNativeRequest, isBannerNativeListenersAvailable);
            if (buildParamString == null) {
                return null;
            }
            try {
                Logging.log("postString: " + URLDecoder.decode(buildParamString, HttpRequest.CHARSET_UTF8));
            } catch (Exception e) {
                Logging.log("postString: " + buildParamString);
            }
            lastAdRequestPostString = buildParamString;
            try {
                this.requestJson = new JSONObject();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = Calendar.getInstance().getTime();
                Long valueOf = Long.valueOf(System.currentTimeMillis());
                this.requestJson.put(MPDbAdapter.KEY_DATA, buildParamString);
                this.requestJson.put(Message.TIMESTAMP, String.valueOf(valueOf));
                this.requestJson.put(Constants.UTC, simpleDateFormat.format(time));
                this.requestJson.put(Constants.LOCAL, simpleDateFormat2.format(time));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return new AdMarvelObfuscator().m576a(buildParamString);
        } catch (Throwable e3) {
            Logging.log(Log.getStackTraceString(e3));
            return null;
        }
    }

    public JSONObject getRequestJson() {
        return this.requestJson;
    }
}
