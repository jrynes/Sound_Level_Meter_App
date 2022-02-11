package com.onelouder.adlib;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.activemq.transport.stomp.Stomp;

public class PlacementManager {
    private static final String TAG = "PlacementManager";
    private static PlacementManager _Instance;
    private static final Object cslock;
    private static long mAdConfigInterval;
    private static final HashMap<String, AdPlacement> placements;
    Runnable checkAdViewsRunnable;

    /* renamed from: com.onelouder.adlib.PlacementManager.1 */
    class C13001 implements Runnable {
        C13001() {
        }

        public void run() {
            Diagnostics.m1951d(PlacementManager.TAG, "checkAdViewsRunnable.run()");
            PlacementManager.this.invalidateAllRecycledAds();
        }
    }

    static {
        placements = new HashMap();
        _Instance = null;
        cslock = new Object();
        mAdConfigInterval = 3600000;
    }

    private PlacementManager() {
        this.checkAdViewsRunnable = new C13001();
    }

    public static PlacementManager getInstance() {
        PlacementManager placementManager;
        synchronized (cslock) {
            if (_Instance == null) {
                _Instance = new PlacementManager();
            }
            placementManager = _Instance;
        }
        return placementManager;
    }

    public void invalidateAllRecycledAds() {
        try {
            Diagnostics.m1951d(TAG, "invalidateAllRecycledAds()");
            Handler handler = AdView.getStaticHandler();
            if (handler != null) {
                handler.removeCallbacks(this.checkAdViewsRunnable);
            }
            for (String key : placements.keySet()) {
                AdPlacement adPlacement = (AdPlacement) placements.get(key);
                if (adPlacement != null && adPlacement.isRecycleable() && adPlacement.ispaused()) {
                    adPlacement.setAdProxy(null);
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public void pauseFromClose(Context context, String key, int duration) {
        try {
            Diagnostics.m1951d(TAG, "pauseFromClose(), key=" + key);
            for (String id : Preferences.getSimplePref(context, "all-placement-ids", Stomp.EMPTY).split(Stomp.COMMA)) {
                if (id.length() != 0) {
                    String[] arr = id.split("-");
                    if (arr.length == 2) {
                        AdPlacement placement = getAdPlacement(context, arr[0], arr[1]);
                        if (!(placement == null || placement.getReset() == null || !placement.getReset().equals(key))) {
                            placement.pauseFromClose(context, duration);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public void resetInterstitials(Context context, String key) {
        try {
            Diagnostics.m1951d(TAG, "resetInterstitials(), key=" + key);
            for (String id : Preferences.getSimplePref(context, "all-placement-ids", Stomp.EMPTY).split(Stomp.COMMA)) {
                if (id.length() != 0) {
                    String[] arr = id.split("-");
                    if (arr.length == 2) {
                        AdPlacement placement = getAdPlacement(context, arr[0], arr[1]);
                        if (!(placement == null || placement.getReset() == null || !placement.getReset().equals(key))) {
                            placement.reset(context, System.currentTimeMillis());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    void resetPlacementCache(Context context) {
        Diagnostics.m1951d(TAG, "resetPlacementCache()");
        for (String key : placements.keySet()) {
            AdPlacement adPlacement = (AdPlacement) placements.get(key);
            if (adPlacement != null) {
                adPlacement.refresh(context);
            }
        }
    }

    AdPlacement getAdPlacement(Context context, String placementId, String type) {
        if (type == null || type.length() == 0) {
            Diagnostics.m1952e(TAG, "getAdPlacement, null or empty type");
            return null;
        }
        checkAdTargetUpdate(context);
        StringBuilder prefkey = new StringBuilder();
        if (placementId == null) {
            placementId = "global";
        }
        prefkey.append(placementId);
        prefkey.append("-");
        prefkey.append(type);
        AdPlacement adPlacement = null;
        String key = prefkey.toString();
        if (placements.containsKey(key)) {
            adPlacement = (AdPlacement) placements.get(key);
            if (adPlacement != null && adPlacement.isCloneable()) {
                adPlacement = null;
            }
        }
        String allPlacementsString = Preferences.getSimplePref(context, "all-placement-ids", Stomp.EMPTY);
        if (allPlacementsString.length() > 0 && !Arrays.asList(allPlacementsString.split(Stomp.COMMA)).contains(key)) {
            Diagnostics.m1957w(TAG, "no ad definition in last config update for placement=" + key);
            return null;
        } else if (adPlacement != null) {
            return adPlacement;
        } else {
            String ad_definition = Preferences.getSimplePref(context, key, null);
            if (ad_definition == null) {
                Diagnostics.m1957w(TAG, "no ad definition for placement=" + key);
                return null;
            }
            adPlacement = new AdPlacement(placementId, ad_definition, AdView.getStaticHandler());
            placements.put(key, adPlacement);
            adPlacement.setType(type);
            return adPlacement;
        }
    }

    void verifyAdViews() {
        Handler handler = AdView.getStaticHandler();
        if (handler != null) {
            handler.removeCallbacks(this.checkAdViewsRunnable);
            handler.postDelayed(this.checkAdViewsRunnable, (long) 300000);
        }
    }

    protected static boolean checkAdConfigUpdate(Context context, boolean force) {
        String appname = Preferences.getSimplePref(context, "ads-product-name", Stomp.EMPTY);
        if (appname.length() > 0) {
            long remaining = mAdConfigInterval - (System.currentTimeMillis() - Preferences.getSimplePref(context, "last-adconfig-update", 0));
            if (remaining <= 0) {
                remaining = 0;
            }
            if (force || remaining == 0) {
                Preferences.setSimplePref(context, "last-adconfig-update", System.currentTimeMillis());
                UpdateAdsConfig updateAdsConfig = new UpdateAdsConfig(context, appname);
            }
            return true;
        }
        Log.e("1LAdView", "Product name not set.  Please call AdView.setProductInfo() with the name assigned to this app and its current version.");
        return false;
    }

    protected static void checkAdTargetUpdate(Context context) {
        if (!checkAdConfigUpdate(context, false)) {
            return;
        }
        long remaining;
        if (Preferences.getMobileConsumerId(context).length() == 0 && Preferences.getSimplePref(context, AdView.PREF_KEY_ANDROID_ID_CHEKED, false)) {
            remaining = 300000 - (System.currentTimeMillis() - Preferences.getSimplePref(context, "last-mobileconsumer-request", 0));
            if (remaining <= 0) {
                remaining = 0;
            }
            if (remaining == 0) {
                Preferences.setSimplePref(context, "last-mobileconsumer-request", System.currentTimeMillis());
                RegisterMobileConsumer registerMobileConsumer = new RegisterMobileConsumer(context);
                return;
            }
            return;
        }
        long nextAdTargetRequest = Preferences.getAdTargetingUpdate(context);
        if (nextAdTargetRequest != 0) {
            remaining = nextAdTargetRequest - System.currentTimeMillis();
            if (remaining <= 0) {
                remaining = 0;
            }
            if (remaining == 0) {
                Preferences.setAdTargetingUpdate(context, 0);
                RequestMobileAdTarget requestMobileAdTarget = new RequestMobileAdTarget(context);
            }
        }
    }
}
