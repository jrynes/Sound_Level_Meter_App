package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import com.admarvel.android.ads.AdMarvelAd.AdType;
import com.admarvel.android.util.AdHistoryDumpUtils;
import com.admarvel.android.util.AdMarvelDownloaderUtilsForBitmapAssets;
import com.admarvel.android.util.AdMarvelThreadExecutorService;
import com.admarvel.android.util.GoogleAdvertisingIdClient;
import com.admarvel.android.util.Logging;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@SuppressLint({"NewApi"})
public class AdMarvelUtils {
    public static final String ADCOLONY_SITEID_ZONEID_MAP = "/adcolony_siteid_zoneid_map";
    protected static final String ADMARVEL_ADAPTER_GUID = "ADMARVELGUID";
    static final int AD_HISTORY_AD_DUMP_DELAY = 1000;
    static final int AD_HISTORY_REDIRECTED_PAGE_DUMP_DELAY = 3000;
    private static AdmarvelOrientationInfo AdmarvelActivityOrientationInfo = null;
    public static final String PATH = "/data/com.admarvel.android.admarvelcachedads";
    private static Map<String, String> adMarvelOptionalFlags;
    private static boolean disableInterstitialProgressBar;
    private static boolean enableLogging;
    static boolean isCustomExpandEnable;
    private static boolean isInitialized;
    private static boolean isLogDumpEnabled;
    public static boolean isRegisteredForActivityLifecylceCallbacks;
    private static boolean notificationBarInFullScreenLaunchEnabled;
    private static String userId;
    private WeakReference<Context> contextReference;

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.1 */
    static class C01771 implements Runnable {
        final /* synthetic */ Activity f237a;

        C01771(Activity activity) {
            this.f237a = activity;
        }

        public void run() {
            Utils.m234s(this.f237a);
            if (Version.getAndroidSDKVersion() > 8) {
                GoogleAdvertisingIdClient.m615a();
                GoogleAdvertisingIdClient.m616c(this.f237a);
            }
            Utils.m238u(this.f237a);
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.2 */
    static class C01782 implements Runnable {
        final /* synthetic */ Activity f238a;

        C01782(Activity activity) {
            this.f238a = activity;
        }

        public void run() {
            AdMarvelDownloaderUtilsForBitmapAssets.m561a(this.f238a);
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.3 */
    static /* synthetic */ class C01793 {
        static final /* synthetic */ int[] f239a;

        static {
            f239a = new int[AdmarvelOrientationInfo.values().length];
            try {
                f239a[AdmarvelOrientationInfo.SCREEN_ORIENTATION_PORTRAIT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f239a[AdmarvelOrientationInfo.SCREEN_ORIENTATION_LANDSCAPE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f239a[AdmarvelOrientationInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f239a[AdmarvelOrientationInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f239a[AdmarvelOrientationInfo.SCREEN_ORIENTATION_CURRENT_ACTIVITY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum AdMArvelErrorReason {
        SITE_ID_OR_PARTNER_ID_NOT_PRESENT(201),
        SITE_ID_AND_PARTNER_ID_DO_NOT_MATCH(202),
        BOT_USER_AGENT_FOUND(203),
        NO_BANNER_FOUND(204),
        NO_AD_FOUND(205),
        NO_USER_AGENT_FOUND(AMQP.FRAME_END),
        SITE_ID_NOT_PRESENT(207),
        PARTNER_ID_NOT_PRESENT(208),
        NO_NETWORK_CONNECTIVITY(301),
        NETWORK_CONNECTIVITY_DISRUPTED(302),
        AD_REQUEST_XML_PARSING_EXCEPTION(303),
        AD_REQUEST_IN_PROCESS_EXCEPTION(304),
        AD_UNIT_NOT_ABLE_TO_RENDER(305),
        AD_REQUEST_MISSING_XML_ELEMENTS(306),
        AD_REQUEST_SDK_TYPE_UNSUPPORTED(307),
        AD_UNIT_NOT_ABLE_TO_LOAD(308),
        AD_UNIT_IN_DISPLAY_STATE(309);
        
        private final int f240a;

        private AdMArvelErrorReason(int errorCode) {
            this.f240a = errorCode;
        }

        public int getErrorCode() {
            return this.f240a;
        }
    }

    public enum AdMarvelVideoEvents {
        IMPRESSION,
        START,
        FIRSTQUARTILE,
        MIDPOINT,
        THIRDQUARTILE,
        COMPLETE,
        CLICK,
        CLOSE,
        CUSTOM;

        public static AdMarvelVideoEvents getEnum(String s) {
            if (IMPRESSION.name().equalsIgnoreCase(s)) {
                return IMPRESSION;
            }
            if (START.name().equalsIgnoreCase(s)) {
                return START;
            }
            if (FIRSTQUARTILE.name().equalsIgnoreCase(s)) {
                return FIRSTQUARTILE;
            }
            if (MIDPOINT.name().equalsIgnoreCase(s)) {
                return MIDPOINT;
            }
            if (THIRDQUARTILE.name().equalsIgnoreCase(s)) {
                return THIRDQUARTILE;
            }
            if (COMPLETE.name().equalsIgnoreCase(s)) {
                return COMPLETE;
            }
            if (CLICK.name().equalsIgnoreCase(s)) {
                return CLICK;
            }
            return CLOSE.name().equalsIgnoreCase(s) ? CLOSE : CUSTOM;
        }
    }

    public enum AdmarvelOrientationInfo {
        SCREEN_ORIENTATION_CURRENT_ACTIVITY,
        SCREEN_ORIENTATION_PORTRAIT,
        SCREEN_ORIENTATION_LANDSCAPE,
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
        SCREEN_ORIENTATION_REVERSE_PORTRAIT
    }

    public enum ErrorReason {
        SITE_ID_OR_PARTNER_ID_NOT_PRESENT,
        SITE_ID_AND_PARTNER_ID_DO_NOT_MATCH,
        BOT_USER_AGENT_FOUND,
        NO_BANNER_FOUND,
        NO_AD_FOUND,
        NO_USER_AGENT_FOUND,
        SITE_ID_NOT_PRESENT,
        PARTNER_ID_NOT_PRESENT,
        NO_NETWORK_CONNECTIVITY,
        NETWORK_CONNECTIVITY_DISRUPTED,
        AD_REQUEST_XML_PARSING_EXCEPTION,
        AD_REQUEST_IN_PROCESS_EXCEPTION,
        AD_UNIT_NOT_ABLE_TO_RENDER,
        AD_REQUEST_MISSING_XML_ELEMENTS,
        AD_REQUEST_SDK_TYPE_UNSUPPORTED,
        AD_UNIT_NOT_ABLE_TO_LOAD,
        AD_UNIT_IN_DISPLAY_STATE
    }

    public enum SDKAdNetwork {
        RHYTHM,
        MILLENNIAL,
        ADMARVEL,
        AMAZON,
        ADCOLONY,
        GOOGLEPLAY,
        FACEBOOK,
        INMOBI,
        HEYZAP,
        UNITYADS,
        CHARTBOOST,
        VUNGLE,
        YUME,
        VERVE
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.a */
    private static class C0180a {
        static void m52a(Activity activity) {
            if (activity != null) {
                activity.getWindow().setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL, ViewCompat.MEASURED_STATE_TOO_SMALL);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.b */
    static class C0181b {
        static Integer m53a(Context context) {
            if (AdMarvelUtils.AdmarvelActivityOrientationInfo != null) {
                switch (C01793.f239a[AdMarvelUtils.AdmarvelActivityOrientationInfo.ordinal()]) {
                    case Zone.PRIMARY /*1*/:
                        return Integer.valueOf(1);
                    case Zone.SECONDARY /*2*/:
                        return Integer.valueOf(0);
                    case Protocol.GGP /*3*/:
                        return Integer.valueOf(0);
                    case Type.MF /*4*/:
                        return Integer.valueOf(1);
                    case Service.RJE /*5*/:
                        return Utils.m218k(context);
                }
            }
            return null;
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelUtils.c */
    static class C0182c {
        static Integer m54a(Context context) {
            if (AdMarvelUtils.AdmarvelActivityOrientationInfo != null) {
                switch (C01793.f239a[AdMarvelUtils.AdmarvelActivityOrientationInfo.ordinal()]) {
                    case Zone.PRIMARY /*1*/:
                        return Integer.valueOf(1);
                    case Zone.SECONDARY /*2*/:
                        return Integer.valueOf(0);
                    case Protocol.GGP /*3*/:
                        return Integer.valueOf(8);
                    case Type.MF /*4*/:
                        return Integer.valueOf(9);
                    case Service.RJE /*5*/:
                        return Utils.m218k(context);
                }
            }
            return null;
        }
    }

    static {
        enableLogging = false;
        isLogDumpEnabled = false;
        isCustomExpandEnable = false;
        notificationBarInFullScreenLaunchEnabled = false;
        disableInterstitialProgressBar = false;
        AdmarvelActivityOrientationInfo = null;
        isInitialized = false;
        isRegisteredForActivityLifecylceCallbacks = false;
    }

    public AdMarvelUtils(Context context) {
        this.contextReference = new WeakReference(context);
    }

    public static void appendParams(StringBuilder param, String key, String val) {
        Utils.m188a(param, key, val);
    }

    public static String captureTargetingParams(Map<String, Object> targetingParams, String delimiter) {
        return Utils.m185a((Map) targetingParams, delimiter);
    }

    public static boolean detectDeviceForWebViewCrash() {
        return Utils.m198b();
    }

    public static void disableLogDump() {
        isLogDumpEnabled = false;
    }

    public static void enableCustomExpand(boolean isCustomExpandEnable) {
        isCustomExpandEnable = isCustomExpandEnable;
    }

    public static void enableLogDump() {
        isLogDumpEnabled = true;
    }

    public static void enableLogging(boolean enableLoggingFlag) {
        enableLogging = enableLoggingFlag;
    }

    public static void enableNotificationBarInFullScreenLaunch(boolean flag) {
        notificationBarInFullScreenLaunchEnabled = flag;
    }

    public static String encodeString(String str) {
        return Utils.m205d(str);
    }

    public static void forceCloseFullScreenAd(Activity activity, AdMarvelActivity adMarvelActivity, AdMarvelVideoActivity adMarvelVideoActivity) {
        if (adMarvelActivity != null) {
            adMarvelActivity.finish();
        }
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.finish();
        }
    }

    public static AdMArvelErrorReason getAdMArvelErrorReason(int errorCode) {
        switch (errorCode) {
            case 201:
                return AdMArvelErrorReason.SITE_ID_OR_PARTNER_ID_NOT_PRESENT;
            case 202:
                return AdMArvelErrorReason.SITE_ID_AND_PARTNER_ID_DO_NOT_MATCH;
            case 203:
                return AdMArvelErrorReason.BOT_USER_AGENT_FOUND;
            case 204:
                return AdMArvelErrorReason.NO_BANNER_FOUND;
            case 205:
                return AdMArvelErrorReason.NO_AD_FOUND;
            case AMQP.FRAME_END /*206*/:
                return AdMArvelErrorReason.NO_USER_AGENT_FOUND;
            case 207:
                return AdMArvelErrorReason.SITE_ID_NOT_PRESENT;
            case 208:
                return AdMArvelErrorReason.PARTNER_ID_NOT_PRESENT;
            case 301:
                return AdMArvelErrorReason.NO_NETWORK_CONNECTIVITY;
            case 302:
                return AdMArvelErrorReason.NETWORK_CONNECTIVITY_DISRUPTED;
            case 303:
                return AdMArvelErrorReason.AD_REQUEST_XML_PARSING_EXCEPTION;
            case 304:
                return AdMArvelErrorReason.AD_REQUEST_IN_PROCESS_EXCEPTION;
            case 305:
                return AdMArvelErrorReason.AD_UNIT_NOT_ABLE_TO_RENDER;
            case 306:
                return AdMArvelErrorReason.AD_REQUEST_MISSING_XML_ELEMENTS;
            case 307:
                return AdMArvelErrorReason.AD_REQUEST_SDK_TYPE_UNSUPPORTED;
            case 308:
                return AdMArvelErrorReason.AD_UNIT_NOT_ABLE_TO_LOAD;
            case 309:
                return AdMArvelErrorReason.AD_UNIT_IN_DISPLAY_STATE;
            default:
                return null;
        }
    }

    public static Map<String, String> getAdMarvelOptionalFlags() {
        return adMarvelOptionalFlags;
    }

    static Integer getAdmarvelActivityOrientationInfo(Context context) {
        return Version.getAndroidSDKVersion() >= 9 ? C0182c.m54a(context) : C0181b.m53a(context);
    }

    public static int getAndroidSDKVersion() {
        return Version.getAndroidSDKVersion();
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "???");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String getDeviceConnectivitiy(Context context) {
        return Utils.m181a(context);
    }

    static float getDeviceDensity(Context context) {
        return Utils.m226o(context);
    }

    public static int getDeviceHeight(Context context) {
        return Utils.m224n(context);
    }

    public static int getDeviceWidth(Context context) {
        return Utils.m222m(context);
    }

    public static int getErrorCode(ErrorReason errorReason) {
        return Utils.m177a(errorReason);
    }

    public static ErrorReason getErrorReason(int errorCode) {
        return Utils.m178a(errorCode);
    }

    public static synchronized boolean getPreferenceValueBoolean(Context context, String pref, String key) {
        boolean z;
        synchronized (AdMarvelUtils.class) {
            z = context.getSharedPreferences(pref, 0).getBoolean(key, false);
        }
        return z;
    }

    public static synchronized int getPreferenceValueInt(Context context, String pref, String key) {
        int i;
        synchronized (AdMarvelUtils.class) {
            i = context.getSharedPreferences(pref, 0).getInt(key, ExploreByTouchHelper.INVALID_ID);
        }
        return i;
    }

    public static synchronized long getPreferenceValueLong(Context context, String pref, String key) {
        long j = -2147483648L;
        synchronized (AdMarvelUtils.class) {
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(pref, 0);
                if (sharedPreferences != null) {
                    j = sharedPreferences.getLong(key, -2147483648L);
                }
            }
        }
        return j;
    }

    public static synchronized String getPreferenceValueString(Context context, String pref, String key) {
        String string;
        synchronized (AdMarvelUtils.class) {
            string = context != null ? context.getSharedPreferences(pref, 0).getString(key, "VALUE_NOT_DEFINED") : null;
        }
        return string;
    }

    public static String getSDKVersion() {
        return Version.SDK_VERSION;
    }

    public static String getSDKVersionDate() {
        return Version.SDK_VERSION_DATE;
    }

    public static int getScreenOrientation(Context context) {
        return Utils.m216j(context);
    }

    public static String getSupportedInterfaceOrientations(Activity currentActivity) {
        return Utils.m180a(currentActivity);
    }

    public static String getUserAgent(Context context, Handler handler) {
        return Utils.m241w(context);
    }

    public static String getUserId() {
        return userId;
    }

    public static void initialize(Activity activity, Map<SDKAdNetwork, String> publisherIds) {
        Logging.log("AdMarvelUtils - initialize");
        if (!isInitialized) {
            isInitialized = true;
            AdMarvelInterstitialAds.clearWebViewMap();
            if (activity == null) {
                Logging.log("AdMarvelUtils : initialize - Activity context should not be null");
                return;
            }
            if (Version.getAndroidSDKVersion() >= 11 && AdMarvelView.isEnableHardwareAcceleration()) {
                C0180a.m52a(activity);
            }
            if (Version.getAndroidSDKVersion() >= 14) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarveladcolonyadapter.AdMarvelAdColonyAdapter: initialize");
                } catch (Exception e) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 14) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelinmobiadapter.AdMarvelInmobiAdapter: initialize");
                } catch (Exception e2) {
                }
            }
            if (Version.getAndroidSDKVersion() > 8) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelheyzapadapter.AdMarvelHeyzapAdapter: initialize");
                } catch (Exception e3) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 17) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelunityadsadapter.AdMarvelUnityAdsAdapter: initialize");
                } catch (Exception e4) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 9) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelchartboostadapter.AdMarvelChartboostAdapter: initialize");
                } catch (Exception e5) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 14) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelvungleadapter.AdMarvelVungleAdapter: initialize");
                } catch (Exception e6) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 16) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelyumeadapter.AdMarvelYuMeAdapter: initialize");
                } catch (Exception e7) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 16) {
                try {
                    AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME).initialize(activity, publisherIds);
                    Logging.log("com.admarvel.android.admarvelmillennialadapter.AdMarvelMillennialAdapter: initialize");
                } catch (Exception e8) {
                }
            }
            if (Version.getAndroidSDKVersion() >= 14) {
                activity.getApplication().registerActivityLifecycleCallbacks(AdMarvelActivityLifecycleCallbacksListener.m248a());
                isRegisteredForActivityLifecylceCallbacks = true;
            }
            AdMarvelThreadExecutorService.m597a().m598b().execute(new C01771(activity));
            AdMarvelThreadExecutorService.m597a().m598b().execute(new C01782(activity));
            if (!isLogDumpEnabled()) {
                Utils.m236t((Context) activity);
            }
            if (Version.getAndroidSDKVersion() == 19 || Version.getAndroidSDKVersion() == 20) {
                Utils.m240v(activity.getApplicationContext());
            }
        }
    }

    public static boolean isInterstitialProgressBarDisabled() {
        return disableInterstitialProgressBar;
    }

    public static boolean isLogDumpEnabled() {
        return isLogDumpEnabled;
    }

    public static boolean isLoggingEnabled() {
        return enableLogging;
    }

    public static final boolean isNetworkAvailable(Context context) {
        return Utils.m220l(context);
    }

    public static boolean isNotificationBarInFullScreenLaunchEnabled() {
        return notificationBarInFullScreenLaunchEnabled;
    }

    public static boolean isTabletDevice(Context context) {
        if (context == null) {
            return false;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (displayMetrics.widthPixels < SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT) {
            return false;
        }
        float f = ((float) displayMetrics.widthPixels) / displayMetrics.xdpi;
        float f2 = ((float) displayMetrics.heightPixels) / displayMetrics.ydpi;
        Double valueOf = Double.valueOf(Math.sqrt((double) ((f2 * f2) + (f * f))));
        Logging.log("Device Screen Size : " + valueOf);
        return valueOf.doubleValue() > 6.5d;
    }

    public static void lockAdMarvelActivityOrientation(AdmarvelOrientationInfo screenOrientaionInfo) {
        Logging.log("AdMarvelUtils - lockAdMarvelActivityOrientation");
        AdmarvelActivityOrientationInfo = screenOrientaionInfo;
    }

    static void pause(Activity activity) {
        try {
            AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).pause(activity, null);
        } catch (Exception e) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME).pause(activity, null);
        } catch (Exception e2) {
        }
    }

    public static Object readObjectFromFile(String fileName, Context context) {
        Throwable e;
        File dir = context.getDir("adm_assets", 0);
        if (fileName == null) {
            return null;
        }
        File file = new File(dir, fileName);
        if (!file.exists()) {
            return null;
        }
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            try {
                HashMap hashMap = (HashMap) objectInputStream.readObject();
                objectInputStream.close();
                if (objectInputStream == null) {
                    return hashMap;
                }
                try {
                    objectInputStream.close();
                    return hashMap;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return hashMap;
                }
            } catch (StreamCorruptedException e3) {
                e = e3;
                try {
                    Logging.log(Log.getStackTraceString(e));
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th) {
                    e = th;
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    throw e;
                }
            } catch (FileNotFoundException e5) {
                e = e5;
                Logging.log(Log.getStackTraceString(e));
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e42) {
                        e42.printStackTrace();
                    }
                }
                return null;
            } catch (IOException e6) {
                e = e6;
                Logging.log(Log.getStackTraceString(e));
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e422) {
                        e422.printStackTrace();
                    }
                }
                return null;
            } catch (ClassNotFoundException e7) {
                e = e7;
                Logging.log(Log.getStackTraceString(e));
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e4222) {
                        e4222.printStackTrace();
                    }
                }
                return null;
            }
        } catch (StreamCorruptedException e8) {
            e = e8;
            objectInputStream = null;
            Logging.log(Log.getStackTraceString(e));
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            return null;
        } catch (FileNotFoundException e9) {
            e = e9;
            objectInputStream = null;
            Logging.log(Log.getStackTraceString(e));
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            return null;
        } catch (IOException e10) {
            e = e10;
            objectInputStream = null;
            Logging.log(Log.getStackTraceString(e));
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            return null;
        } catch (ClassNotFoundException e11) {
            e = e11;
            objectInputStream = null;
            Logging.log(Log.getStackTraceString(e));
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            return null;
        } catch (Throwable th2) {
            e = th2;
            objectInputStream = null;
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            throw e;
        }
    }

    public static String reportAdMarvelAdHistory(int count, Context context) {
        String str = Stomp.EMPTY;
        return (isNetworkAvailable(context) && isLogDumpEnabled) ? AdHistoryDumpUtils.m550b(context).m556a(count) : str;
    }

    public static String reportAdMarvelAdHistory(Context context) {
        String str = Stomp.EMPTY;
        return (isNetworkAvailable(context) && isLogDumpEnabled) ? AdHistoryDumpUtils.m550b(context).m556a(20) : str;
    }

    static void resume(Activity activity) {
        try {
            AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME).resume(activity, null);
        } catch (Exception e) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME).resume(activity, null);
        } catch (Exception e2) {
        }
        try {
            AdMarvelAdapterInstances.getInstance(ADMARVEL_ADAPTER_GUID, Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME).resume(activity, null);
        } catch (Exception e3) {
        }
    }

    public static void setAdMarvelOptionalFlags(Map<String, String> adMarvelOptionalFlags) {
        adMarvelOptionalFlags = adMarvelOptionalFlags;
    }

    static synchronized AdMarvelAd setAdStatus(AdMarvelAd adMarvelAd, String adStatus) {
        synchronized (AdMarvelUtils.class) {
            adMarvelAd.setAdType(AdType.ERROR);
            adMarvelAd.setErrorCode(306);
            adMarvelAd.setErrorReason(adStatus);
        }
        return adMarvelAd;
    }

    public static void setFullScreenloadingTimeout(int timeInSeconds) {
        if (timeInSeconds > 0) {
            Constants.WAIT_FOR_INTERSTITIAL = (long) (timeInSeconds * AD_HISTORY_AD_DUMP_DELAY);
        } else {
            Logging.log("setFullScreenloadingTimeout :- time cannot be less than zero");
        }
    }

    public static void setInterstitialProgressBarDisabled(boolean flag) {
        disableInterstitialProgressBar = flag;
    }

    public static synchronized void setPreferenceValueBoolean(Context context, String pref, String key, boolean value) {
        synchronized (AdMarvelUtils.class) {
            if (context != null) {
                Editor edit = context.getSharedPreferences(pref, 0).edit();
                edit.putBoolean(key, value);
                edit.commit();
            }
        }
    }

    public static synchronized void setPreferenceValueInt(Context context, String pref, String key, int value) {
        synchronized (AdMarvelUtils.class) {
            if (context != null) {
                Editor edit = context.getSharedPreferences(pref, 0).edit();
                edit.putInt(key, value);
                edit.commit();
            }
        }
    }

    public static synchronized void setPreferenceValueLong(Context context, String pref, String key, long value) {
        synchronized (AdMarvelUtils.class) {
            if (context != null) {
                Editor edit = context.getSharedPreferences(pref, 0).edit();
                edit.putLong(key, value);
                edit.commit();
            }
        }
    }

    public static synchronized void setPreferenceValueString(Context context, String pref, String key, String value) {
        synchronized (AdMarvelUtils.class) {
            if (context != null) {
                Editor edit = context.getSharedPreferences(pref, 0).edit();
                edit.putString(key, value);
                edit.commit();
            }
        }
    }

    public static void setUserId(String userId) {
        userId = userId;
    }

    @Deprecated
    public static void uninitialize(Activity activity) {
        Logging.log("AdMarvelUtils - uninitialize : API is now depricated");
    }

    public static void writeObjectToFile(Context context, Object object, String fileName) {
        File dir = context.getDir("adm_assets", 0);
        if (object != null) {
            try {
                OutputStream fileOutputStream = new FileOutputStream(new File(dir, fileName));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                Logging.log(e.getMessage());
            } catch (IOException e2) {
                Logging.log(e2.getMessage());
            }
        }
    }

    void firePixel(AdMarvelAd adMarvelAd) {
        new Utils((Context) this.contextReference.get()).m244a(adMarvelAd);
    }

    void firePixel(String pixelUrl) {
        new Utils((Context) this.contextReference.get()).m245a(pixelUrl);
    }

    public String getUserAgent() {
        return Utils.m241w((Context) this.contextReference.get());
    }
}
