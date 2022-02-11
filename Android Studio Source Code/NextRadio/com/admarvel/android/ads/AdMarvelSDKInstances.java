package com.admarvel.android.ads;

import java.util.HashMap;
import java.util.Map;

/* renamed from: com.admarvel.android.ads.k */
class AdMarvelSDKInstances {
    private static final Map<String, Boolean> f629a;

    static {
        f629a = AdMarvelSDKInstances.m339a();
    }

    static Map<String, Boolean> m339a() {
        Map<String, Boolean> hashMap = new HashMap();
        try {
            hashMap.put(Constants.GOOGLEPLAY_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.GOOGLEPLAY_SDK_FULL_CLASSNAME)));
        } catch (Exception e) {
        }
        try {
            hashMap.put(Constants.RHYTHM_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.RHYTHM_SDK_FULL_CLASSNAME)));
        } catch (Exception e2) {
        }
        try {
            hashMap.put(Constants.MILLENNIAL_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.MILLENNIAL_SDK_FULL_CLASSNAME)));
        } catch (Exception e3) {
        }
        try {
            hashMap.put(Constants.AMAZON_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.AMAZON_SDK_FULL_CLASSNAME)));
        } catch (Exception e4) {
        }
        try {
            hashMap.put(Constants.ADCOLONY_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.ADCOLONY_SDK_FULL_CLASSNAME)));
        } catch (Exception e5) {
        }
        try {
            hashMap.put(Constants.FACEBOOK_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.FACEBOOK_SDK_FULL_CLASSNAME)));
        } catch (Exception e6) {
        }
        try {
            hashMap.put(Constants.INMOBI_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.INMOBI_SDK_FULL_CLASSNAME)));
        } catch (Exception e7) {
        }
        try {
            hashMap.put(Constants.HEYZAP_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.HEYZAP_SDK_FULL_CLASSNAME)));
        } catch (Exception e8) {
        }
        try {
            hashMap.put(Constants.UNITYADS_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.UNITYADS_SDK_FULL_CLASSNAME)));
        } catch (Exception e9) {
        }
        try {
            hashMap.put(Constants.VUNGLE_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.VUNGLE_SDK_FULL_CLASSNAME)));
        } catch (Exception e10) {
        }
        try {
            hashMap.put(Constants.YUME_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.YUME_SDK_FULL_CLASSNAME)));
        } catch (Exception e11) {
        }
        try {
            hashMap.put(Constants.CHARTBOOST_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.CHARTBOOST_SDK_FULL_CLASSNAME)));
        } catch (Exception e12) {
        }
        try {
            hashMap.put(Constants.VERVE_SDK_FULL_CLASSNAME, Boolean.valueOf(AdMarvelSDKInstances.m341b(Constants.VERVE_SDK_FULL_CLASSNAME)));
        } catch (Exception e13) {
        }
        return hashMap;
    }

    static boolean m340a(String str) {
        return ((Boolean) f629a.get(str)).booleanValue();
    }

    public static boolean m341b(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
