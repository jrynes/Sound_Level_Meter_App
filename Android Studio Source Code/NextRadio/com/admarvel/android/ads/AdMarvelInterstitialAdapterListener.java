package com.admarvel.android.ads;

import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import java.util.Map;

public interface AdMarvelInterstitialAdapterListener extends AdMarvelRewardInterstitialAdapterListener {
    void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvents, Map<String, String> map);

    void onAudioStart();

    void onAudioStop();

    void onClickInterstitialAd(String str);

    void onCloseInterstitialAd();

    void onFailedToReceiveInterstitialAd(SDKAdNetwork sDKAdNetwork, String str, int i, ErrorReason errorReason, AdMarvelAd adMarvelAd);

    void onInterstitialDisplayed();

    void onReceiveInterstitialAd(SDKAdNetwork sDKAdNetwork, String str, AdMarvelAd adMarvelAd);

    void onRequestInterstitialAd();

    void onReward(boolean z, SDKAdNetwork sDKAdNetwork, String str);
}
