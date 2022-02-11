package com.admarvel.android.ads;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import java.util.Map;

public abstract class AdMarvelAdapter {
    protected static AdMarvelAdapter createInstance(String className) {
        return (AdMarvelAdapter) createObject(className);
    }

    private static Object createObject(String className) {
        Object obj = null;
        try {
            obj = Class.forName(className).newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e2) {
        } catch (ClassNotFoundException e3) {
        }
        return obj;
    }

    protected abstract void cleanupView(View view);

    public abstract void create(Activity activity);

    public abstract void destroy(View view);

    protected abstract boolean displayInterstitial(Activity activity, boolean z);

    protected abstract void forceCloseFullScreenAd(Activity activity);

    public abstract int getAdAvailablityStatus();

    public abstract int getAdAvailablityStatus(String str, Context context);

    public abstract String getAdNetworkSDKVersion();

    public abstract String getAdapterVersion();

    public abstract String getAdnetworkSDKVersionInfo();

    public abstract void handleBackKeyPressed(Activity activity);

    public abstract void handleClick();

    public abstract void handleConfigChanges(Activity activity, Configuration configuration);

    public abstract void handleImpression();

    public abstract void handleNotice();

    public abstract void initialize(Activity activity, Map<SDKAdNetwork, String> map);

    protected abstract AdMarvelAd loadAd(AdMarvelAd adMarvelAd, AdMarvelXMLReader adMarvelXMLReader);

    public abstract Object loadNativeAd(AdMarvelNativeAd adMarvelNativeAd, AdMarvelXMLReader adMarvelXMLReader);

    public abstract void notifyAddedToListView(View view);

    public abstract void pause(Activity activity, View view);

    public abstract void registerViewForInteraction(View view);

    public abstract void registerViewForInteraction(View[] viewArr);

    protected abstract void requestIntersitialNewAd(AdMarvelInterstitialAdapterListener adMarvelInterstitialAdapterListener, Context context, AdMarvelAd adMarvelAd, Map<String, Object> map, int i, int i2, String str);

    public abstract Object requestNativeAd(AdMarvelAdapterListener adMarvelAdapterListener, AdMarvelNativeAd adMarvelNativeAd);

    protected abstract View requestNewAd(AdMarvelAdapterListener adMarvelAdapterListener, Context context, AdMarvelAd adMarvelAd, Map<String, Object> map, int i, int i2);

    public abstract void resume(Activity activity, View view);

    public abstract void setUserId(String str);

    public abstract void start(Activity activity, View view);

    public abstract void stop(Activity activity, View view);

    public abstract void unregisterView();
}
