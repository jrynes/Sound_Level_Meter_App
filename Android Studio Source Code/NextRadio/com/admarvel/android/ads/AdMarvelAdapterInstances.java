package com.admarvel.android.ads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdMarvelAdapterInstances {
    private static Map<String, Map<String, AdMarvelAdapter>> adMarvelAdapterInstances;

    static {
        adMarvelAdapterInstances = new ConcurrentHashMap();
        adMarvelAdapterInstances.put("ADMARVELGUID", createInstances());
    }

    public static synchronized void buildAdMarvelAdapterInstances(String keyGUID) {
        synchronized (AdMarvelAdapterInstances.class) {
            adMarvelAdapterInstances.put(keyGUID, createInstances());
        }
    }

    private static Map<String, AdMarvelAdapter> createInstances() {
        Map<String, AdMarvelAdapter> hashMap = new HashMap();
        try {
            hashMap.put(Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.RHYTHM_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e) {
        }
        try {
            hashMap.put(Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.MILLENNIAL_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e2) {
        }
        try {
            hashMap.put(Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.AMAZON_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e3) {
        }
        try {
            hashMap.put(Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.ADCOLONY_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e4) {
        }
        try {
            hashMap.put(Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.GOOGLEPLAY_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e5) {
        }
        try {
            hashMap.put(Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.FACEBOOK_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e6) {
        }
        try {
            hashMap.put(Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.INMOBI_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e7) {
        }
        try {
            hashMap.put(Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.HEYZAP_SDK_APAPTER_FULL_CLASSNAME));
        } catch (Exception e8) {
        }
        try {
            hashMap.put(Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.UNITYADS_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e9) {
        }
        try {
            hashMap.put(Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e10) {
        }
        try {
            hashMap.put(Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.VUNGLE_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e11) {
        }
        try {
            hashMap.put(Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e12) {
        }
        try {
            hashMap.put(Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME, AdMarvelAdapter.createInstance(Constants.VERVE_SDK_ADAPTER_FULL_CLASSNAME));
        } catch (Exception e13) {
        }
        return hashMap;
    }

    protected static synchronized void destroyAdMarvelAdapterInstances(String keyGUID) {
        synchronized (AdMarvelAdapterInstances.class) {
            adMarvelAdapterInstances.remove(keyGUID);
        }
    }

    public static Map<String, Map<String, AdMarvelAdapter>> getAdMarvelAdapterInstancesForAdMarvelView() {
        return adMarvelAdapterInstances;
    }

    public static AdMarvelAdapter getAdapterInstance(String keyAdapterClassName) {
        Map map = (Map) adMarvelAdapterInstances.get("ADMARVELGUID");
        return map != null ? (AdMarvelAdapter) map.get(keyAdapterClassName) : null;
    }

    public static AdMarvelAdapter getInstance(String keyGUID, String keyAdapterClassName) {
        Map map = (Map) adMarvelAdapterInstances.get(keyGUID);
        return map != null ? (AdMarvelAdapter) map.get(keyAdapterClassName) : null;
    }
}
