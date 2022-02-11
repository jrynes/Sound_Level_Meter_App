package com.onelouder.adlib;

import com.pinsightmediaplus.advertising.IPsmAd;
import com.pinsightmediaplus.advertising.IPsmNotification;

public class SimplePsmNotification implements IPsmNotification {
    public void onRequest(IPsmAd ad) {
    }

    public void onImpression(IPsmAd ad) {
    }

    public void onError(IPsmAd ad, String errorMsg) {
    }

    public boolean onClick(IPsmAd ad, String url) {
        return false;
    }

    public void onChangeSize(IPsmAd ad, int arg1, int arg2, int arg3, int arg4, String arg5) {
    }

    public void onClose(IPsmAd ad, String operation) {
    }

    public void onExpand(IPsmAd ad) {
    }

    public void onResize(IPsmAd ad) {
    }

    public void onResponse(IPsmAd ad, boolean valid) {
    }
}
