package com.onelouder.adlib;

import android.app.Activity;
import android.view.View;
import java.util.HashMap;

public interface I1LouderAdProxy {
    void destroy();

    void displayInterstitial(Activity activity);

    int getAnimationDelay();

    int getHeight();

    View getProxiedView();

    int getVisibility();

    void invalidate();

    boolean isExpanded();

    boolean isFullScreen();

    boolean isInterstitialReady();

    boolean isInterstitialRequested();

    void onAddedToListView();

    void pause();

    void requestAd(HashMap<String, Object> hashMap);

    void requestInterstitial(Activity activity, HashMap<String, Object> hashMap);

    void resume();

    void setAdPlacement(AdPlacement adPlacement);

    void setAdViewParent(AdView adView);

    void setVisibility(int i);

    void start();

    void stop();
}
