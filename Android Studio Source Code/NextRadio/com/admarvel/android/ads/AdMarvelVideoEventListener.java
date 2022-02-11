package com.admarvel.android.ads;

import com.admarvel.android.ads.AdMarvelUtils.AdMarvelVideoEvents;
import java.util.Map;

public interface AdMarvelVideoEventListener {
    void onAdMarvelVideoEvent(AdMarvelVideoEvents adMarvelVideoEvents, Map<String, String> map);

    void onAudioStart();

    void onAudioStop();
}
