package com.admarvel.android.ads;

import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import com.admarvel.android.ads.AdMarvelUtils.ErrorReason;
import java.util.Map;

public interface AdMarvelAdapterListener {
    void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvents, Map<String, String> map);

    void onAudioStart();

    void onAudioStop();

    void onClickAd(String str);

    void onClose();

    void onExpand();

    void onFailedToReceiveAd(int i, ErrorReason errorReason);

    void onReceiveAd();

    void onReceiveNativeAd(Object obj);
}
