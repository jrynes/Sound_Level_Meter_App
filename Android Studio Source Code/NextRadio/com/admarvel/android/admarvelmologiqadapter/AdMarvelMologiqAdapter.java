package com.admarvel.android.admarvelmologiqadapter;

import android.content.Context;
import com.admarvel.android.ads.AdMarvelAnalyticsAdapter;
import com.admarvel.android.ads.Version;
import com.mologiq.analytics.MologiqAnalytics;
import java.lang.ref.WeakReference;
import java.util.Map;

public class AdMarvelMologiqAdapter extends AdMarvelAnalyticsAdapter {
    private final WeakReference contextReference;

    public AdMarvelMologiqAdapter(Context context) {
        super(context);
        this.contextReference = new WeakReference(context);
    }

    public void enableAppInstallCheck(boolean z) {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).enableAppInstallCheck(context, z);
        }
    }

    public String getAdapterAnalyticsVersion() {
        return "1.4.4 2015-09-10";
    }

    public String getAdapterAnalyticsVersionNumber() {
        return com.mologiq.analytics.Version.VERSION;
    }

    public Map getEnhancedTargetParams(String str, Map map) {
        Context context = (Context) this.contextReference.get();
        return context != null ? Version.getAndroidSDKVersion() >= 8 ? MologiqAnalytics.getInstance(context).getEnhancedTargetParams(str, map) : map : null;
    }

    public void onAdClick(String str, int i, Map map, String str2, String str3) {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onAdClick(str, i, map, str2, str3);
        }
    }

    public void onFailedToReceiveAd(String str, int i, Map map, String str2) {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onFailedToReceiveAd(str, i, map, str2);
        }
    }

    public void onReceiveAd(String str, int i, Map map, String str2) {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onReceiveAd(str, i, map, str2);
        }
    }

    public void pause() {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onPause();
        }
    }

    public void resume() {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onResume();
        }
    }

    public void start() {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onStart();
        }
    }

    public void stop() {
        Context context = (Context) this.contextReference.get();
        if (context != null && Version.getAndroidSDKVersion() >= 8) {
            MologiqAnalytics.getInstance(context).onStop();
        }
    }
}
