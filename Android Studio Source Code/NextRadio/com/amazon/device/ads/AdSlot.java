package com.amazon.device.ads;

class AdSlot {
    private final AdController adController;
    private AdError adError;
    private final AdTargetingOptions adOptions;
    private boolean deferredLoad;
    private int slotNumber;

    AdSlot(AdController adController, AdTargetingOptions adOptions) {
        this.deferredLoad = false;
        this.adController = adController;
        if (adOptions == null) {
            this.adOptions = new AdTargetingOptions();
        } else {
            this.adOptions = adOptions;
        }
    }

    public AdSlot setDeferredLoad(boolean deferredLoad) {
        this.deferredLoad = deferredLoad;
        return this;
    }

    public AdTargetingOptions getAdTargetingOptions() {
        return this.adOptions;
    }

    public AdSize getRequestedAdSize() {
        return this.adController.getAdSize();
    }

    void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    int getSlotNumber() {
        return this.slotNumber;
    }

    void setAdError(AdError adError) {
        this.adError = adError;
    }

    AdError getAdError() {
        return this.adError;
    }

    String getMaxSize() {
        return this.adController.getMaxSize();
    }

    MetricsCollector getMetricsCollector() {
        return this.adController.getMetricsCollector();
    }

    void setAdData(AdData adData) {
        this.adController.setAdData(adData);
    }

    boolean isFetched() {
        return this.adController.getAdData() != null && this.adController.getAdData().getIsFetched();
    }

    void adFailed(AdError adError) {
        this.adController.adFailed(adError);
    }

    void initializeAd() {
        this.adController.initialize();
    }

    boolean prepareForAdLoad(long loadAdStartTime) {
        return this.adController.prepareForAdLoad(loadAdStartTime, this.deferredLoad);
    }

    boolean isValid() {
        return this.adController.isValid();
    }

    void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.adController.setConnectionInfo(connectionInfo);
    }

    boolean canBeUsed() {
        return this.adController.canBeUsed();
    }
}
