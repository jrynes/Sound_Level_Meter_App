package com.amazon.device.ads;

class MobileAdsLoggerFactory {
    private static MobileAdsLoggerFactory loggerFactory;

    MobileAdsLoggerFactory() {
    }

    static void setLoggerFactory(MobileAdsLoggerFactory loggerFactory) {
        loggerFactory = loggerFactory;
    }

    public MobileAdsLogger createMobileAdsLogger(String logTag) {
        if (loggerFactory != null) {
            return loggerFactory.createMobileAdsLogger(logTag);
        }
        return createMobileAdsLogger(logTag, new LogcatLogger());
    }

    MobileAdsLogger createMobileAdsLogger(String logTag, Logger logger) {
        return new MobileAdsLogger(logger).withLogTag(logTag);
    }
}
