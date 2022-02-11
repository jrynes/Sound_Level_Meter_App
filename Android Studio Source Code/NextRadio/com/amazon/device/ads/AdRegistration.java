package com.amazon.device.ads;

import android.content.Context;

public class AdRegistration {
    private static final String LOGTAG;
    private static final AdRegistrationExecutor amazonAdRegistration;

    static {
        LOGTAG = AdRegistration.class.getSimpleName();
        amazonAdRegistration = new AdRegistrationExecutor(LOGTAG);
    }

    private AdRegistration() {
    }

    public static final void setAppKey(String appKey) throws IllegalArgumentException {
        amazonAdRegistration.setAppKey(appKey);
    }

    public static final void enableLogging(boolean enable) {
        amazonAdRegistration.enableLogging(enable);
    }

    public static final void enableTesting(boolean enable) {
        amazonAdRegistration.enableTesting(enable);
    }

    public static final String getVersion() {
        return amazonAdRegistration.getVersion();
    }

    public static final void registerApp(Context context) {
        amazonAdRegistration.registerApp(context);
    }

    static AdRegistrationExecutor getAmazonAdRegistrationExecutor() {
        return amazonAdRegistration;
    }
}
