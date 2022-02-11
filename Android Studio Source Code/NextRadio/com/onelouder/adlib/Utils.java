package com.onelouder.adlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.onelouder.adlib.AdvertisingId.AdvertisingIdListener;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@SuppressLint({"InlinedApi"})
class Utils {
    public static final Date MinimumDate;
    public static final String PREF_KEY_ANDROID_AD_ID = "android-adid";
    private static final String TAG = "Utils";
    private static final SimpleDateFormat gFormatDate;
    private static final SimpleDateFormat gFormatTS;
    private static String mAdvertisingId;
    private static Integer mDensityDPI;
    private static String mDeviceId;
    private static String mGUID;
    private static Boolean mIsDefaultDensity;
    private static Boolean mIsHighDensity;
    private static Boolean mIsLarge;
    private static Boolean mIsLowDensity;
    private static Boolean mIsMediumDensity;
    private static Boolean mIsTvDensity;
    private static Boolean mIsXHighDensity;
    private static Boolean mIsXLarge;
    private static Boolean mIsXXHighDensity;
    private static String mPhoneNumber;
    private static String mSESSIONID;
    private static Double mScale;
    private static Integer mYDPI;

    /* renamed from: com.onelouder.adlib.Utils.1 */
    static class C13121 implements AdvertisingIdListener {
        final /* synthetic */ Runnable val$afterChecked;
        final /* synthetic */ Context val$context;

        C13121(Context context, Runnable runnable) {
            this.val$context = context;
            this.val$afterChecked = runnable;
        }

        public void onAdIdReady(String adid, boolean limitTracking) {
            String value = Utils.getAdvertisingId(this.val$context);
            if (!(adid == null || adid.equals(value))) {
                Preferences.setSimplePref(this.val$context, Utils.PREF_KEY_ANDROID_AD_ID, adid);
                Preferences.setMobileConsumerId(this.val$context, Stomp.EMPTY);
                Utils.mAdvertisingId = adid;
                Diagnostics.m1951d(Utils.TAG, "mAdvertisingId=" + adid);
            }
            if (Preferences.getSimplePref(this.val$context, "limit-ad-tracking", false) != limitTracking) {
                Preferences.setSimplePref(this.val$context, "limit-ad-tracking", limitTracking);
                Preferences.setMobileConsumerId(this.val$context, Stomp.EMPTY);
            }
            if (this.val$afterChecked != null) {
                this.val$afterChecked.run();
            }
        }

        public void onAdIdError(String error) {
            Diagnostics.m1952e(Utils.TAG, "onAdIdError " + error);
            if (this.val$afterChecked != null) {
                this.val$afterChecked.run();
            }
        }
    }

    Utils() {
    }

    static {
        MinimumDate = new Date(0);
        gFormatTS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        gFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        mAdvertisingId = null;
        mScale = null;
        mIsLarge = null;
        mIsXLarge = null;
        mYDPI = null;
        mDensityDPI = null;
        mIsDefaultDensity = null;
        mIsLowDensity = null;
        mIsMediumDensity = null;
        mIsTvDensity = null;
        mIsHighDensity = null;
        mIsXHighDensity = null;
        mIsXXHighDensity = null;
    }

    static int getResourseIdByName(String packageName, String className, String name) {
        int id = 0;
        try {
            Class[] classes = Class.forName(packageName + ".R").getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (Throwable th) {
        }
        return id;
    }

    static Bitmap decodePurgeableByteArray(byte[] bytes) {
        Bitmap bitmap = null;
        if (bytes != null) {
            try {
                Options options = new Options();
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inPreferredConfig = Config.RGB_565;
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                if (bitmap == null) {
                    Diagnostics.m1957w(TAG, "BitmapFactory.decodeByteArray returned null");
                }
            } catch (VirtualMachineError e) {
                Diagnostics.m1959w(TAG, e);
            } catch (Throwable e2) {
                Diagnostics.m1953e(TAG, e2);
            }
        }
        return bitmap;
    }

    static int ParseInteger(String s) {
        int val = 0;
        if (s != null) {
            try {
                if (s.length() > 0) {
                    val = Integer.parseInt(s);
                }
            } catch (Throwable e) {
                Diagnostics.m1958w(TAG, e);
            }
        }
        return val;
    }

    static Date ParseTimestamp(String s) {
        Date val = MinimumDate;
        if (s == null) {
            return val;
        }
        try {
            if (s.length() <= 0) {
                return val;
            }
            if (s.length() == 10) {
                return gFormatDate.parse(s);
            }
            return gFormatTS.parse(s);
        } catch (Throwable e) {
            Diagnostics.m1958w(TAG, e);
            return val;
        } catch (Throwable e2) {
            Diagnostics.m1958w(TAG, e2);
            return val;
        }
    }

    static boolean isSameDay(long dt1, long dt2) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dt1);
        int day1 = cal.get(6);
        int year1 = cal.get(1);
        cal.setTimeInMillis(dt2);
        int day2 = cal.get(6);
        int year2 = cal.get(1);
        if (day1 == day2 && year1 == year2) {
            return true;
        }
        return false;
    }

    static boolean isToday(long dt) {
        return isSameDay(System.currentTimeMillis(), dt);
    }

    private static boolean checkReadPhoneStatePermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0;
    }

    private static boolean checkAccessWifiStatePermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0;
    }

    private static boolean checkAccessNetworkStatePermission(Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0;
    }

    static boolean isNetworkConnected(Context context) {
        if (!checkAccessNetworkStatePermission(context)) {
            return true;
        }
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }

    static String getVersionName(Context context) {
        StringBuilder version = new StringBuilder();
        try {
            version.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
        return version.toString();
    }

    static String getCarrier(Context context) {
        String carrier = null;
        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService("phone");
            if (tMgr != null) {
                carrier = tMgr.getNetworkOperatorName();
                if (carrier == null || carrier.length() == 0) {
                    carrier = tMgr.getNetworkOperator();
                }
            }
        } catch (Exception e) {
        }
        return carrier;
    }

    static String getPhoneNumber(Context context) {
        if (mPhoneNumber == null) {
            if (checkReadPhoneStatePermission(context)) {
                try {
                    TelephonyManager tMgr = (TelephonyManager) context.getSystemService("phone");
                    if (tMgr != null) {
                        mPhoneNumber = tMgr.getLine1Number();
                    }
                } catch (Exception e) {
                }
            }
            if (mPhoneNumber == null) {
                mPhoneNumber = Stomp.EMPTY;
            }
        }
        return mPhoneNumber;
    }

    static String getDeviceId(Context context) {
        if (mDeviceId == null && checkReadPhoneStatePermission(context)) {
            try {
                TelephonyManager tMgr = (TelephonyManager) context.getSystemService("phone");
                if (tMgr != null) {
                    mDeviceId = tMgr.getDeviceId();
                }
            } catch (Exception e) {
            }
        }
        return mDeviceId;
    }

    static String getAdvertisingId(Context context) {
        if (mAdvertisingId == null && context != null) {
            mAdvertisingId = Preferences.getSimplePref(context, PREF_KEY_ANDROID_AD_ID, null);
        }
        return mAdvertisingId;
    }

    static String getLimitAdTrackingEnabled(Context context) {
        if (Preferences.getSimplePref(context, "limit-ad-tracking", false)) {
            return Stomp.TRUE;
        }
        return Stomp.FALSE;
    }

    static void checkAndroidId(Context context, Runnable afterChecked) {
        if (VERSION.SDK_INT >= 9) {
            AdvertisingId.getAdvertisingId(context, new C13121(context, afterChecked));
        } else if (afterChecked != null) {
            Preferences.setSimplePref(context, PREF_KEY_ANDROID_AD_ID, Stomp.EMPTY);
            afterChecked.run();
        }
    }

    static String getGUID(Context context) {
        if (mGUID == null && context != null) {
            mGUID = Preferences.getSimplePref(context, "android-guid", null);
            if (mGUID == null) {
                mGUID = UUID.randomUUID().toString();
                Preferences.setSimplePref(context, "android-guid", mGUID);
            }
        }
        return mGUID;
    }

    static String getSESSIONID(Context context) {
        if (mSESSIONID == null && context != null) {
            mSESSIONID = Preferences.getSimplePref(context, "android-sessionid", null);
            if (mSESSIONID == null) {
                mSESSIONID = UUID.randomUUID().toString();
                Preferences.setSimplePref(context, "android-sessionid", mSESSIONID);
            }
        }
        return mSESSIONID;
    }

    public static void resetSESSIONID(Context context) {
        mSESSIONID = UUID.randomUUID().toString();
        Preferences.setSimplePref(context, "android-sessionid", mSESSIONID);
    }

    public static int getPhoneType(Context context) {
        int phoneType = 0;
        try {
            phoneType = ((TelephonyManager) context.getSystemService("phone")).getPhoneType();
        } catch (Exception e) {
        }
        return phoneType;
    }

    static String getWifiMacAddress(Context context) {
        String macAddr = null;
        if (checkAccessWifiStatePermission(context)) {
            try {
                macAddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (Exception e) {
            }
        }
        return macAddr;
    }

    static int getConnectionType(Context context) {
        if (!checkAccessNetworkStatePermission(context)) {
            return 0;
        }
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return 0;
            }
            if (info.getType() == 1) {
                Diagnostics.m1951d("getConnectionType", "info.getType() == ConnectivityManager.TYPE_WIFI, sending CXN=3");
                return 3;
            } else if (info.getType() == 6) {
                Diagnostics.m1951d("getConnectionType", "info.getType() == ConnectivityManager.TYPE_WIMAX, sending CXN=4");
                return 4;
            } else {
                switch (info.getSubtype()) {
                    case Zone.PRIMARY /*1*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_GPRS, ~ 100 kbps, sending CXN=1");
                        return 1;
                    case Zone.SECONDARY /*2*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_IDEN, ~ 50-100 kbps, sending CXN=1");
                        return 1;
                    case Protocol.GGP /*3*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_UMTS, ~ 400-7000 kbps, sending CXN=2");
                        return 2;
                    case Type.MF /*4*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_CDMA, ~ 14-64 kbps, sending CXN=1");
                        return 1;
                    case Service.RJE /*5*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_EVDO_0, ~ 400-1000 kbps, sending CXN=2");
                        return 2;
                    case Protocol.TCP /*6*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_EVDO_A, ~ 600-1400 kbps, sending CXN=2");
                        return 2;
                    case Service.ECHO /*7*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_1xRTT, ~ 50-100 kbps, sending CXN=1");
                        return 1;
                    case Protocol.EGP /*8*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_HSDPA, ~ 2-14 Mbps, sending CXN=2");
                        return 2;
                    case Service.DISCARD /*9*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_HSUPA, ~ 1-23 Mbps, sending CXN=2");
                        return 2;
                    case Protocol.BBN_RCC_MON /*10*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_HSPA, ~ 700-1700 kbps, sending CXN=2");
                        return 2;
                    case Service.USERS /*11*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_IDEN, ~25 kbps, sending CXN=1");
                        return 1;
                    case Protocol.PUP /*12*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_EVDO_B, ~ 5 Mbps, sending CXN=2");
                        return 2;
                    case Service.DAYTIME /*13*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_LTE, ~ 10+ Mbps sending CXN=4");
                        return 4;
                    case Protocol.EMCON /*14*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_EHRPD, ~ 1-2 Mbps, sending CXN=2");
                        return 2;
                    case Protocol.XNET /*15*/:
                        Diagnostics.m1951d("getConnectionType", "TelephonyManager.NETWORK_TYPE_HSPAP, ~ 10-20 Mbps, sending CXN=2");
                        return 2;
                    default:
                        return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    static String getCountry(Context context) {
        String country = Stomp.EMPTY;
        try {
            country = context.getResources().getConfiguration().locale.getCountry();
            if (country == null || country.length() == 0) {
                TelephonyManager tMgr = (TelephonyManager) context.getSystemService("phone");
                if (tMgr != null) {
                    country = tMgr.getNetworkCountryIso();
                }
            }
        } catch (Exception e) {
        }
        return country;
    }

    static String getLanguage(Context context) {
        String language = Stomp.EMPTY;
        try {
            language = Locale.getDefault().getLanguage().toLowerCase();
        } catch (Exception e) {
        }
        return language;
    }

    static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    static boolean isPortrait() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.heightPixels > dm.widthPixels;
    }

    static boolean isLandscape() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.widthPixels > dm.heightPixels;
    }

    private static double getScale() {
        if (mScale == null) {
            mScale = Double.valueOf((double) Resources.getSystem().getDisplayMetrics().density);
        }
        return mScale.doubleValue();
    }

    static int getDIP(double p) {
        return (int) Math.round(getScale() * p);
    }

    static boolean isTabletLayout(Context context) {
        return isLargeLayout(context) || isXLargeLayout(context);
    }

    static boolean isLargeLayout(Context context) {
        boolean z = false;
        if (mIsLarge == null) {
            if (context == null) {
                return false;
            }
            if ((context.getResources().getConfiguration().screenLayout & 15) == 3) {
                z = true;
            }
            mIsLarge = Boolean.valueOf(z);
        }
        return mIsLarge.booleanValue();
    }

    static boolean isXLargeLayout(Context context) {
        boolean z = false;
        if (mIsXLarge == null) {
            if (context == null) {
                return false;
            }
            if ((context.getResources().getConfiguration().screenLayout & 15) == 4) {
                z = true;
            }
            mIsXLarge = Boolean.valueOf(z);
        }
        return mIsXLarge.booleanValue();
    }

    static int getYDPI(Context context) {
        if (mYDPI == null) {
            if (context == null) {
                return 0;
            }
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
            mYDPI = Integer.valueOf((int) metrics.ydpi);
        }
        return mYDPI.intValue();
    }

    static int getDensity(Context context) {
        if (mDensityDPI == null) {
            if (context == null) {
                return 0;
            }
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
            mDensityDPI = Integer.valueOf(metrics.densityDpi);
        }
        return mDensityDPI.intValue();
    }

    static boolean isDefaultDensity(Context context) {
        if (mIsDefaultDensity == null) {
            mIsDefaultDensity = Boolean.valueOf(getDensity(context) == 160);
        }
        return mIsDefaultDensity.booleanValue();
    }

    static boolean isLowDensity(Context context) {
        if (mIsLowDensity == null) {
            mIsLowDensity = Boolean.valueOf(getDensity(context) == Nack.INDEX);
        }
        return mIsLowDensity.booleanValue();
    }

    static boolean isMediumDensity(Context context) {
        if (mIsMediumDensity == null) {
            mIsMediumDensity = Boolean.valueOf(getDensity(context) == 160);
        }
        return mIsMediumDensity.booleanValue();
    }

    static boolean isTvDensity(Context context) {
        if (mIsTvDensity == null) {
            mIsTvDensity = Boolean.valueOf(getDensity(context) == 213);
        }
        return mIsTvDensity.booleanValue();
    }

    static boolean isHighDensity(Context context) {
        if (mIsHighDensity == null) {
            mIsHighDensity = Boolean.valueOf(getDensity(context) == 240);
        }
        return mIsHighDensity.booleanValue();
    }

    static boolean isXHighDensity(Context context) {
        if (mIsXHighDensity == null) {
            mIsXHighDensity = Boolean.valueOf(getDensity(context) == AMQP.CONNECTION_FORCED);
        }
        return mIsXHighDensity.booleanValue();
    }

    static boolean isXXHighDensity(Context context) {
        if (mIsXXHighDensity == null) {
            mIsXXHighDensity = Boolean.valueOf(getDensity(context) == 480);
        }
        return mIsXXHighDensity.booleanValue();
    }

    static void slideOutUp(View v, AnimationListener listener, long startoffset) {
        TranslateAnimation outanim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, GroundOverlayOptions.NO_DIMENSION);
        if (startoffset > 0) {
            outanim.setStartOffset(startoffset);
        }
        outanim.setInterpolator(v.getContext(), 17432581);
        outanim.setDuration(150);
        if (listener != null) {
            outanim.setAnimationListener(listener);
        }
        v.startAnimation(outanim);
        v.setVisibility(8);
    }

    static void slideOutDown(View v, AnimationListener listener, long startoffset) {
        if (v != null) {
            TranslateAnimation outanim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
            if (startoffset > 0) {
                outanim.setStartOffset(startoffset);
            }
            outanim.setInterpolator(v.getContext(), 17432581);
            outanim.setDuration(150);
            if (listener != null) {
                outanim.setAnimationListener(listener);
            }
            v.startAnimation(outanim);
            v.setVisibility(8);
        }
    }

    static void slideInUp(View v, AnimationListener listener, long startoffset) {
        if (v != null) {
            TranslateAnimation outanim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
            if (startoffset > 0) {
                outanim.setStartOffset(startoffset);
            }
            outanim.setInterpolator(v.getContext(), 17432581);
            outanim.setDuration(150);
            if (listener != null) {
                outanim.setAnimationListener(listener);
            }
            v.startAnimation(outanim);
            v.setVisibility(0);
        }
    }

    static void slideInDown(View v, AnimationListener listener, long startoffset) {
        TranslateAnimation outanim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, GroundOverlayOptions.NO_DIMENSION, 1, 0.0f);
        if (startoffset > 0) {
            outanim.setStartOffset(startoffset);
        }
        outanim.setInterpolator(v.getContext(), 17432581);
        outanim.setDuration(150);
        if (listener != null) {
            outanim.setAnimationListener(listener);
        }
        v.startAnimation(outanim);
        v.setVisibility(0);
    }

    static void fadeIn(View v, AnimationListener listener, long startoffset, int duration) {
        try {
            AlphaAnimation inanim = new AlphaAnimation(0.0f, 1.0f);
            if (startoffset > 0) {
                inanim.setStartOffset(startoffset);
            }
            inanim.setDuration((long) duration);
            if (listener != null) {
                inanim.setAnimationListener(listener);
            }
            v.setVisibility(0);
            v.startAnimation(inanim);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
    }

    static void fadeOut(View v, AnimationListener listener, long startoffset, int duration) {
        try {
            AlphaAnimation outanim = new AlphaAnimation(1.0f, 0.0f);
            if (startoffset > 0) {
                outanim.setStartOffset(startoffset);
            }
            outanim.setDuration(150);
            if (listener != null) {
                outanim.setAnimationListener(listener);
            }
            v.startAnimation(outanim);
            v.setVisibility(8);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
    }

    public static void disableMMSDKLogging() {
        try {
            Class.forName("com.millennialmedia.android.MMLog").getDeclaredMethod("setLogLevel", new Class[]{Integer.TYPE}).invoke(null, new Object[]{Integer.valueOf(10)});
            Diagnostics.m1956v(TAG, "MMSDK log level set to 10");
        } catch (Exception e) {
            Diagnostics.m1956v(TAG, "Unable to set MMSDK log level");
        }
    }
}
