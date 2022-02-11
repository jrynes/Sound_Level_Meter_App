package com.admarvel.android.ads;

import android.content.Context;
import android.os.Build.VERSION;
import com.admarvel.android.util.PostAPI4Version;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import org.apache.activemq.transport.stomp.Stomp;

public class Version {
    public static final String ADMARVEL_API_VERSION = "1.5";
    public static final String SDK_VERSION = "2.6.0";
    public static final String SDK_VERSION_DATE = "2015-10-27";

    static final String getAdNetworkVersionInfo(Context context) {
        AdMarvelAdapter adapterInstance;
        String str = Stomp.EMPTY;
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.ADCOLONY_SDK_FULL_CLASSNAME)) {
                str = str + "AC~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e) {
        }
        try {
            str = str + "AV~" + AdMarvelAnalyticsAdapterInstances.getInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context).getAdapterAnalyticsVersionNumber() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
        } catch (Exception e2) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.AMAZON_SDK_FULL_CLASSNAME)) {
                str = str + "AZ~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e3) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.FACEBOOK_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 8) {
                str = str + "FB~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e4) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.HEYZAP_SDK_FULL_CLASSNAME)) {
                str = str + "HZ~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e5) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.INMOBI_SDK_FULL_CLASSNAME)) {
                str = str + "IM~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e6) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.MILLENNIAL_SDK_FULL_CLASSNAME)) {
                str = str + "MM~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e7) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.RHYTHM_SDK_FULL_CLASSNAME)) {
                str = str + "RM~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e8) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.UNITYADS_SDK_FULL_CLASSNAME)) {
                str = str + "UA~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e9) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.CHARTBOOST_SDK_FULL_CLASSNAME)) {
                str = str + "CB~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e10) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.VUNGLE_SDK_FULL_CLASSNAME)) {
                str = str + "VU~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e11) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.YUME_SDK_FULL_CLASSNAME)) {
                str = str + "YM~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e12) {
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.VERVE_SDK_FULL_CLASSNAME)) {
                str = str + "VR~" + adapterInstance.getAdNetworkSDKVersion() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR;
            }
        } catch (Exception e13) {
        }
        return str;
    }

    public static int getAndroidSDKVersion() {
        return VERSION.RELEASE.contains(ADMARVEL_API_VERSION) ? 3 : PostAPI4Version.m626a();
    }

    static final String getSDKSupported(boolean checkRewardSupport) {
        String str = Stomp.EMPTY;
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.GOOGLEPLAY_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 8 && !checkRewardSupport) {
                str = str + "_googleplay_admob";
            }
        } catch (Exception e) {
        }
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.RHYTHM_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 9 && !checkRewardSupport) {
                str = str + "_rhythm";
            }
        } catch (Exception e2) {
        }
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.MILLENNIAL_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 16 && !checkRewardSupport) {
                str = str + "_millennial";
            }
        } catch (Exception e3) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME) == null || !AdMarvelSDKInstances.m340a(Constants.AMAZON_SDK_FULL_CLASSNAME) || checkRewardSupport)) {
                str = str + "_amazon";
            }
        } catch (Exception e4) {
        }
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.ADCOLONY_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 14) {
                str = str + "_adcolony";
            }
        } catch (Exception e5) {
        }
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.FACEBOOK_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 8 && !checkRewardSupport) {
                str = str + "_facebook";
            }
        } catch (Exception e6) {
        }
        try {
            if (AdMarvelAdapterInstances.getAdapterInstance(Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME) != null && AdMarvelSDKInstances.m340a(Constants.INMOBI_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 14) {
                str = str + "_inmobi";
            }
        } catch (Exception e7) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.HEYZAP_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() <= 8 || checkRewardSupport)) {
                str = str + "_heyzap";
            }
        } catch (Exception e8) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.UNITYADS_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 17)) {
                str = str + "_unityads";
            }
        } catch (Exception e9) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.VUNGLE_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 14)) {
                str = str + "_vungle";
            }
        } catch (Exception e10) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.CHARTBOOST_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 9)) {
                str = str + "_chartboost";
            }
        } catch (Exception e11) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.YUME_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 16 || checkRewardSupport)) {
                str = str + "_yume";
            }
        } catch (Exception e12) {
        }
        try {
            if (!(AdMarvelAdapterInstances.getAdapterInstance(Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME) == null || Class.forName(Constants.VERVE_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 9 || checkRewardSupport)) {
                str = str + "_verve";
            }
        } catch (Exception e13) {
        }
        return str;
    }

    static final String getVersionDump() {
        AdMarvelAdapter adapterInstance;
        String str = Stomp.EMPTY;
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.GOOGLEPLAY_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 8) {
                str = str + "googleplay: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e) {
            str = str + "googleplay: NOT found, admarvel-android-sdk-googleplay-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.RHYTHM_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 9) {
                str = str + "rhythm: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e2) {
            str = str + "rhythm: NOT found, admarvel-android-sdk-rhythm-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.MILLENNIAL_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 16) {
                str = str + "millennial: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e3) {
            str = str + "millennial: NOT found, admarvel-android-sdk-millennial-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.AMAZON_SDK_FULL_CLASSNAME)) {
                str = str + "amazon: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e4) {
            str = str + "amazon: NOT found, admarvel-android-sdk-amazon-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.ADCOLONY_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 14) {
                str = str + "adcolony: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e5) {
            str = str + "adcolony: NOT found, admarvel-android-sdk-adcolony-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.FACEBOOK_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() > 8) {
                str = str + "facebook: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e6) {
            str = str + "facebook: NOT found, admarvel-android-sdk-facebook-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME);
            if (adapterInstance != null && AdMarvelSDKInstances.m340a(Constants.INMOBI_SDK_FULL_CLASSNAME) && getAndroidSDKVersion() >= 14) {
                str = str + "inmobi: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e7) {
            str = str + "inmobi: NOT found, admarvel-android-sdk-inmobi-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME);
            if (!(adapterInstance == null || Class.forName(Constants.HEYZAP_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() <= 8)) {
                str = str + "heyzap: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e8) {
            str = str + "heyzap: NOT found, admarvel-android-sdk-heyzap-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME);
            if (!(adapterInstance == null || Class.forName(Constants.UNITYADS_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 14)) {
                str = str + "unityads: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e9) {
            str = str + "unityads: NOT found, admarvel-android-sdk-unityads-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME);
            if (!(adapterInstance == null || Class.forName(Constants.CHARTBOOST_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 9)) {
                str = str + "chartboost: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e10) {
            str = str + "chartboost: NOT found, admarvel-android-sdk-chartboost-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME);
            if (!(adapterInstance == null || Class.forName(Constants.VUNGLE_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 14)) {
                str = str + "vungle: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e11) {
            str = str + "vungle: NOT found, admarvel-android-sdk-vungle-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME);
            if (!(adapterInstance == null || Class.forName(Constants.YUME_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 16)) {
                str = str + "yume: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
            }
        } catch (Exception e12) {
            str = str + "yume: NOT found, admarvel-android-sdk-yume-adapter.jar NOT in classpath\n";
        }
        try {
            adapterInstance = AdMarvelAdapterInstances.getAdapterInstance(Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME);
            return (adapterInstance == null || Class.forName(Constants.VERVE_SDK_FULL_CLASSNAME) == null || getAndroidSDKVersion() < 9) ? str : str + "verve: found, " + adapterInstance.getAdnetworkSDKVersionInfo() + Stomp.NEWLINE;
        } catch (Exception e13) {
            return str + "yume: NOT found, admarvel-android-sdk-yume-adapter.jar NOT in classpath\n";
        }
    }
}
