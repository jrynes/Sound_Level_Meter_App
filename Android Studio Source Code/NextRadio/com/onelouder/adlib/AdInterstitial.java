package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import com.onelouder.adlib.AdPlacement.AdPlacementListener;
import java.util.HashMap;

public class AdInterstitial {
    private static final String TAG = "AdInterstitial";
    private static final HashMap<String, String> placements;

    /* renamed from: com.onelouder.adlib.AdInterstitial.1 */
    static class C12811 implements AdPlacementListener {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ AdInterstitialListener val$listener;
        final /* synthetic */ String val$placementid;

        C12811(String str, Activity activity, AdInterstitialListener adInterstitialListener) {
            this.val$placementid = str;
            this.val$activity = activity;
            this.val$listener = adInterstitialListener;
        }

        public void onInterstitialRequestFailed(AdPlacement placement, int errorcode, String errormessage) {
            synchronized (AdInterstitial.placements) {
                Diagnostics.m1957w(AdInterstitial.TAG, "AdPlacementListener.onInterstitialRequestFailed");
                if (placement.getRolloverId() != null) {
                    Diagnostics.m1955i(AdInterstitial.TAG, "setting rollover placement to " + placement.getRolloverId());
                    AdInterstitial.placements.put(this.val$placementid, placement.getRolloverId());
                    AdInterstitial.requestInterstitial(this.val$activity, this.val$placementid, this.val$listener);
                } else {
                    if (this.val$listener != null) {
                        this.val$listener.onInterstitialRequestFailed(errorcode, errormessage);
                    }
                    AdInterstitial.placements.remove(this.val$placementid);
                }
            }
        }

        public void onInterstitialReady(AdPlacement placement) {
            synchronized (AdInterstitial.placements) {
                Diagnostics.m1955i(AdInterstitial.TAG, "AdPlacementListener.onInterstitialReady");
            }
        }
    }

    /* renamed from: com.onelouder.adlib.AdInterstitial.2 */
    static class C12822 implements AdPlacementListener {
        final /* synthetic */ String val$placementid;

        C12822(String str) {
            this.val$placementid = str;
        }

        public void onInterstitialRequestFailed(AdPlacement placement, int errorcode, String errormessage) {
            if (placement.getRolloverId() != null) {
                synchronized (AdInterstitial.placements) {
                    Diagnostics.m1955i(AdInterstitial.TAG, "setting rollover placement to " + placement.getRolloverId());
                    AdInterstitial.placements.put(this.val$placementid, placement.getRolloverId());
                }
            }
        }

        public void onInterstitialReady(AdPlacement placement) {
            synchronized (AdInterstitial.placements) {
                Diagnostics.m1955i(AdInterstitial.TAG, "AdPlacementListener.onInterstitialReady");
            }
        }
    }

    public interface AdInterstitialListener {
        void onInterstitialClosed(HashMap<String, Object> hashMap);

        void onInterstitialDisplayed();

        void onInterstitialReceived();

        void onInterstitialRequestFailed(int i, String str);

        void onSetTargetParams(HashMap<String, String> hashMap);
    }

    static {
        placements = new HashMap();
    }

    public static boolean requestInterstitial(Activity activity, String placementid, AdInterstitialListener listener) {
        Diagnostics.m1951d(TAG, "requestInterstitial placement=" + placementid);
        if (Preferences.getSimplePref((Context) activity, "ads_enabled", true)) {
            String strPlacementid = placementid;
            synchronized (placements) {
                if (placements.containsKey(placementid)) {
                    strPlacementid = (String) placements.get(placementid);
                    Diagnostics.m1951d(TAG, "rollover available, using " + strPlacementid);
                }
            }
            AdPlacement adPlacement = PlacementManager.getInstance().getAdPlacement(activity, strPlacementid, "interstitial");
            if (adPlacement != null && adPlacement.isDefined()) {
                adPlacement.setListener(new C12811(placementid, activity, listener));
                if (!adPlacement.isTimeForNextAd(activity, true)) {
                    return false;
                }
                adPlacement.requestInterstitial(activity, listener);
                return true;
            } else if (listener == null) {
                return false;
            } else {
                listener.onInterstitialRequestFailed(AdPlacement.ERRORCODE_NO_PLACEMENT, null);
                return false;
            }
        }
        Diagnostics.m1957w(TAG, "ads_enabled=false");
        if (listener != null) {
            listener.onInterstitialRequestFailed(AdPlacement.ERRORCODE_ADS_NOT_ENABLED, null);
        }
        return false;
    }

    public static boolean isInterstitialRequested(Context context, String placementid) {
        Diagnostics.m1951d(TAG, "isInterstitialRequested placement=" + placementid);
        String strPlacementid = placementid;
        synchronized (placements) {
            if (placements.containsKey(placementid)) {
                strPlacementid = (String) placements.get(placementid);
                Diagnostics.m1951d(TAG, "rollover available, using " + strPlacementid);
            }
        }
        AdPlacement adPlacement = PlacementManager.getInstance().getAdPlacement(context, strPlacementid, "interstitial");
        if (adPlacement != null) {
            return adPlacement.isInterstitialRequested();
        }
        return false;
    }

    public static boolean isInterstitialReady(Context context, String placementid) {
        Diagnostics.m1951d(TAG, "isInterstitialReady placement=" + placementid);
        String strPlacementid = placementid;
        synchronized (placements) {
            if (placements.containsKey(placementid)) {
                strPlacementid = (String) placements.get(placementid);
                Diagnostics.m1951d(TAG, "rollover available, using " + strPlacementid);
            }
        }
        AdPlacement adPlacement = PlacementManager.getInstance().getAdPlacement(context, strPlacementid, "interstitial");
        if (adPlacement != null) {
            return adPlacement.isInterstitialReady();
        }
        return false;
    }

    public static void displayInterstitial(Activity activity, String placementid) {
        Diagnostics.m1951d(TAG, "displayInterstitial placement=" + placementid);
        if (Preferences.getSimplePref((Context) activity, "ads_enabled", true)) {
            String strPlacementid = placementid;
            synchronized (placements) {
                if (placements.containsKey(placementid)) {
                    strPlacementid = (String) placements.get(placementid);
                    Diagnostics.m1951d(TAG, "rollover available, using " + strPlacementid);
                }
            }
            AdPlacement adPlacement = PlacementManager.getInstance().getAdPlacement(activity, strPlacementid, "interstitial");
            if (adPlacement != null && adPlacement.isDefined()) {
                adPlacement.displayInterstitial(activity);
                synchronized (placements) {
                    placements.remove(placementid);
                }
                return;
            }
            return;
        }
        Diagnostics.m1957w(TAG, "ads_enabled=false");
    }

    public static void onLaunch(Activity activity, AdInterstitialListener listener) {
        Diagnostics.m1951d(TAG, "onLaunch");
        handleInterstitial(activity, "launch", listener);
    }

    @Deprecated
    public static void onScreenChange(Activity activity, AdInterstitialListener listener) {
        Diagnostics.m1952e(TAG, "onScreenChange - DEPRECATED - please refer to documentation on prefered way to implement screen change interstitials.");
        handleInterstitial(activity, "screenchange", listener);
    }

    private static void handleInterstitial(Activity activity, String placementid, AdInterstitialListener listener) {
        if (Preferences.getSimplePref((Context) activity, "ads_enabled", true)) {
            String strPlacementid = placementid;
            synchronized (placements) {
                if (placements.containsKey(placementid)) {
                    strPlacementid = (String) placements.get(placementid);
                    Diagnostics.m1951d(TAG, "rollover available, using " + strPlacementid);
                }
            }
            AdPlacement adPlacement = PlacementManager.getInstance().getAdPlacement(activity, strPlacementid, "interstitial");
            if (adPlacement != null && adPlacement.isDefined()) {
                adPlacement.setListener(new C12822(placementid));
                if (adPlacement.isTimeForNextAd(activity, true)) {
                    if (adPlacement.isInterstitialReady()) {
                        adPlacement.displayInterstitial(activity);
                        synchronized (placements) {
                            placements.remove(placementid);
                        }
                        return;
                    } else if (!adPlacement.isInterstitialRequested()) {
                        adPlacement.requestInterstitial(activity, listener);
                        return;
                    } else {
                        return;
                    }
                } else if (adPlacement.isAlmostTimeForNextAd(activity) && !adPlacement.isInterstitialRequested()) {
                    adPlacement.requestInterstitial(activity, listener);
                    return;
                } else {
                    return;
                }
            } else if (listener != null) {
                listener.onInterstitialRequestFailed(AdPlacement.ERRORCODE_NO_PLACEMENT, null);
                return;
            } else {
                return;
            }
        }
        Diagnostics.m1957w(TAG, "ads_enabled=false");
        if (listener != null) {
            listener.onInterstitialRequestFailed(AdPlacement.ERRORCODE_ADS_NOT_ENABLED, null);
        }
    }
}
