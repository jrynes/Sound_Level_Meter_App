package com.amazon.device.ads;

import org.apache.activemq.transport.stomp.Stomp;

class Version {
    private static String buildVersion;
    private static String devBuild;
    private static String prefixVersion;
    private static String sdkVersion;
    private static String userAgentPrefixVersion;
    private static String userAgentSDKVersion;

    Version() {
    }

    static {
        buildVersion = "5.6.20";
        prefixVersion = "amznAdSDK-android-";
        sdkVersion = null;
        userAgentPrefixVersion = "AmazonAdSDK-Android/";
        userAgentSDKVersion = null;
        devBuild = "(DEV)";
    }

    public static String getRawSDKVersion() {
        String rawVersion = buildVersion;
        if (rawVersion == null || rawVersion.equals(Stomp.EMPTY)) {
            return devBuild;
        }
        if (rawVersion.endsWith("x")) {
            return rawVersion + devBuild;
        }
        return rawVersion;
    }

    public static String getSDKVersion() {
        if (sdkVersion == null) {
            sdkVersion = prefixVersion + getRawSDKVersion();
        }
        return sdkVersion;
    }

    protected static void setSDKVersion(String newSdkVersion) {
        sdkVersion = newSdkVersion;
    }

    public static String getUserAgentSDKVersion() {
        if (userAgentSDKVersion == null) {
            userAgentSDKVersion = userAgentPrefixVersion + getRawSDKVersion();
        }
        return userAgentSDKVersion;
    }
}
