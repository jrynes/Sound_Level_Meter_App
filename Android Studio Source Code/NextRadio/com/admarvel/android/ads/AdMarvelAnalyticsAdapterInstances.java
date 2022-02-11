package com.admarvel.android.ads;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

public class AdMarvelAnalyticsAdapterInstances {
    private static Map<String, AdMarvelAnalyticsAdapter> instances;

    static {
        instances = null;
    }

    static Map<String, AdMarvelAnalyticsAdapter> createInstances(Context context) {
        Map<String, AdMarvelAnalyticsAdapter> hashMap = new HashMap();
        try {
            hashMap.put(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, AdMarvelAnalyticsAdapter.createInstance(Constants.MOLOGIQ_ANALYTICS_ADAPTER_FULL_CLASSNAME, context));
        } catch (Exception e) {
        }
        return hashMap;
    }

    public static AdMarvelAnalyticsAdapter getInstance(String key, Context context) {
        if (instances == null) {
            instances = createInstances(context);
        }
        return (AdMarvelAnalyticsAdapter) instances.get(key);
    }
}
