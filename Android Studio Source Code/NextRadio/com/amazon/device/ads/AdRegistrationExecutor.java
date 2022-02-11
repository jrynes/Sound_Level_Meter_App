package com.amazon.device.ads;

import android.content.Context;

class AdRegistrationExecutor {
    private final MobileAdsInfoStore infoStore;
    private volatile boolean isInitialized;
    private final MobileAdsLogger logger;
    private final MobileAdsLoggerFactory loggerFactory;
    private final PermissionChecker permissionChecker;
    private final Settings settings;

    public AdRegistrationExecutor(String logtag) {
        this(logtag, MobileAdsInfoStore.getInstance(), Settings.getInstance(), new MobileAdsLoggerFactory(), new PermissionChecker());
    }

    AdRegistrationExecutor(String logtag, MobileAdsInfoStore infoStore, Settings settings, MobileAdsLoggerFactory loggerFactory, PermissionChecker permissionChecker) {
        this.isInitialized = false;
        this.infoStore = infoStore;
        this.settings = settings;
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(logtag);
        this.permissionChecker = permissionChecker;
    }

    public void setAppKey(String appKey) throws IllegalArgumentException {
        this.infoStore.getRegistrationInfo().putAppKey(appKey);
    }

    public void enableLogging(boolean enable) {
        this.logger.enableLoggingWithSetterNotification(enable);
    }

    public void enableTesting(boolean enable) {
        this.settings.putTransientBoolean("testingEnabled", enable);
        this.logger.logSetterNotification("Test mode", Boolean.valueOf(enable));
    }

    public String getVersion() {
        return Version.getSDKVersion();
    }

    public void registerApp(Context context) {
        if (this.permissionChecker.hasInternetPermission(context)) {
            initializeAds(context);
            this.infoStore.register();
            return;
        }
        this.logger.m639e("Network task cannot commence because the INTERNET permission is missing from the app's manifest.");
    }

    public void initializeAds(Context context) {
        if (!this.isInitialized) {
            this.infoStore.contextReceived(context);
            this.infoStore.getDeviceInfo().setUserAgentManager(new AdUserAgentManager());
            this.isInitialized = true;
        }
    }

    MobileAdsInfoStore getMobileAdsInfoStore() {
        return this.infoStore;
    }

    Settings getSettings() {
        return this.settings;
    }

    PermissionChecker getPermissionChecker() {
        return this.permissionChecker;
    }

    MobileAdsLogger getLogger() {
        return this.logger;
    }

    MobileAdsLoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }
}
