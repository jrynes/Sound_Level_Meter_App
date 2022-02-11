package com.amazon.device.ads;

class AdListenerExecutorFactory {
    private final MobileAdsLoggerFactory loggerFactory;

    public AdListenerExecutorFactory(MobileAdsLoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    protected MobileAdsLoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    public AdListenerExecutor createAdListenerExecutor(AdListener adListener) {
        if (adListener instanceof ExtendedAdListener) {
            return new ExtendedAdListenerExecutor((ExtendedAdListener) adListener, this.loggerFactory);
        }
        return new AdListenerExecutor(adListener, this.loggerFactory);
    }
}
