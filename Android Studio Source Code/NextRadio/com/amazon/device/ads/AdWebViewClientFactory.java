package com.amazon.device.ads;

import android.content.Context;

class AdWebViewClientFactory {
    private final AndroidBuildInfo androidBuildInfo;
    private final MobileAdsLoggerFactory loggerFactory;
    private final WebUtils2 webUtils;

    public AdWebViewClientFactory(WebUtils2 webUtils, MobileAdsLoggerFactory loggerFactory, AndroidBuildInfo androidBuildInfo) {
        this.webUtils = webUtils;
        this.loggerFactory = loggerFactory;
        this.androidBuildInfo = androidBuildInfo;
    }

    public AdWebViewClient createAdWebViewClient(Context context, AdSDKBridgeList bridgeList, AdControlAccessor adControlAccessor) {
        return new AdWebViewClient(context, bridgeList, adControlAccessor, this.webUtils, this.loggerFactory, this.androidBuildInfo);
    }
}
