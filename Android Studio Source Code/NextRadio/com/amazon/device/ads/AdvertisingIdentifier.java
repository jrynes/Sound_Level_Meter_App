package com.amazon.device.ads;

import org.apache.activemq.transport.stomp.Stomp;

class AdvertisingIdentifier {
    private static final String GPS_ADVERTISING_IDENTIFIER_SETTING = "gpsAdId";
    private static final String LOGTAG;
    private static final String TRANSITION_MIGRATE = "migrate";
    private static final String TRANSITION_RESET = "reset";
    private static final String TRANSITION_REVERT = "revert";
    private static final String TRANSITION_SETTING = "adIdTransistion";
    private final DebugProperties debugProperties;
    private AdvertisingInfo gpsAdvertisingInfo;
    private final MobileAdsInfoStore infoStore;
    private final MobileAdsLogger logger;
    private final Settings settings;
    private boolean shouldSetCurrentAdvertisingIdentifier;

    static class Info {
        private String advertisingIdentifier;
        private boolean canDo;
        private final DebugProperties debugProperties;
        private boolean limitAdTrackingEnabled;
        private String sisDeviceIdentifier;

        private Info(DebugProperties debugProperties) {
            this.debugProperties = debugProperties;
            this.canDo = true;
        }

        boolean canDo() {
            return this.canDo;
        }

        private Info setCanDo(boolean canDo) {
            this.canDo = canDo;
            return this;
        }

        String getAdvertisingIdentifier() {
            return this.debugProperties.getDebugPropertyAsString(DebugProperties.DEBUG_IDFA, this.advertisingIdentifier);
        }

        private Info setAdvertisingIdentifier(String advertisingIdentifier) {
            this.advertisingIdentifier = advertisingIdentifier;
            return this;
        }

        boolean hasAdvertisingIdentifier() {
            return !StringUtils.isNullOrEmpty(getAdvertisingIdentifier());
        }

        boolean isLimitAdTrackingEnabled() {
            return this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_OPT_OUT, Boolean.valueOf(this.limitAdTrackingEnabled)).booleanValue();
        }

        private Info setLimitAdTrackingEnabled(boolean limitAdTrackingEnabled) {
            this.limitAdTrackingEnabled = limitAdTrackingEnabled;
            return this;
        }

        String getSISDeviceIdentifier() {
            return this.debugProperties.getDebugPropertyAsString(DebugProperties.DEBUG_ADID, this.sisDeviceIdentifier);
        }

        boolean hasSISDeviceIdentifier() {
            return getSISDeviceIdentifier() != null;
        }

        Info setSISDeviceIdentifier(String sisDeviceIdentifier) {
            this.sisDeviceIdentifier = sisDeviceIdentifier;
            return this;
        }
    }

    static {
        LOGTAG = AdvertisingIdentifier.class.getSimpleName();
    }

    public AdvertisingIdentifier() {
        this(Settings.getInstance(), MobileAdsInfoStore.getInstance(), new MobileAdsLoggerFactory(), DebugProperties.getInstance());
    }

    AdvertisingIdentifier(Settings settings, MobileAdsInfoStore infoStore, MobileAdsLoggerFactory loggerFactory, DebugProperties debugProperties) {
        this.shouldSetCurrentAdvertisingIdentifier = true;
        this.settings = settings;
        this.infoStore = infoStore;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.debugProperties = debugProperties;
    }

    String getAndClearTransition() {
        String transition = this.settings.getString(TRANSITION_SETTING, null);
        this.settings.remove(TRANSITION_SETTING);
        return transition;
    }

    AdvertisingIdentifier setShouldSetCurrentAdvertisingIdentifier(boolean shouldSetCurrentAdvertisingIdentifier) {
        this.shouldSetCurrentAdvertisingIdentifier = shouldSetCurrentAdvertisingIdentifier;
        return this;
    }

    Info getAdvertisingIdentifierInfo() {
        if (ThreadUtils.isOnMainThread()) {
            this.logger.m639e("You must obtain the advertising indentifier information on a background thread.");
            return new Info(null).setCanDo(false);
        }
        fetchGooglePlayServicesAdvertisingIdentifierInfo();
        if (this.shouldSetCurrentAdvertisingIdentifier) {
            determineTransition();
        }
        Info info = new Info(null);
        if (getGPSAdvertisingInfo().hasAdvertisingIdentifier()) {
            info.setAdvertisingIdentifier(getGPSAdvertisingInfo().getAdvertisingIdentifier());
            info.setLimitAdTrackingEnabled(getGPSAdvertisingInfo().isLimitAdTrackingEnabled());
            if (this.shouldSetCurrentAdvertisingIdentifier) {
                setCurrentGPSAID(getGPSAdvertisingInfo().getAdvertisingIdentifier());
            }
        }
        RegistrationInfo registrationInfo = this.infoStore.getRegistrationInfo();
        if (registrationInfo.isAdIdCurrent(info)) {
            info.setSISDeviceIdentifier(registrationInfo.getAdId());
            return info;
        }
        registrationInfo.requestNewSISDeviceIdentifier();
        return info;
    }

    private void determineTransition() {
        String transition = null;
        if (isTransitionMigrated()) {
            transition = TRANSITION_MIGRATE;
        } else if (isTransitionReset()) {
            transition = TRANSITION_RESET;
        } else if (isTransitionReverted()) {
            transition = TRANSITION_REVERT;
        }
        if (transition != null) {
            setTransition(transition);
        } else {
            this.logger.m637d("No transition detected.");
        }
    }

    private void setTransition(String transition) {
        this.logger.m638d("Transition: %s", transition);
        this.settings.putString(TRANSITION_SETTING, transition);
    }

    protected void fetchGooglePlayServicesAdvertisingIdentifierInfo() {
        this.gpsAdvertisingInfo = new GooglePlayServices().getAdvertisingIdentifierInfo();
    }

    protected AdvertisingInfo getGPSAdvertisingInfo() {
        return this.gpsAdvertisingInfo;
    }

    private boolean isTransitionMigrated() {
        return this.infoStore.getRegistrationInfo().hasAdId() && RegistrationInfo.isAdIdOriginatedFromNonAdvertisingIdentifier() && !hasCurrentGPSAID() && getGPSAdvertisingInfo().hasAdvertisingIdentifier();
    }

    private boolean isTransitionReset() {
        return hasCurrentGPSAID() && getGPSAdvertisingInfo().hasAdvertisingIdentifier() && !getCurrentGPSAID().equals(getGPSAdvertisingInfo().getAdvertisingIdentifier());
    }

    private boolean isTransitionReverted() {
        return hasCurrentGPSAID() && !getGPSAdvertisingInfo().hasAdvertisingIdentifier();
    }

    private void setCurrentGPSAID(String gpsaid) {
        this.settings.putString(GPS_ADVERTISING_IDENTIFIER_SETTING, gpsaid);
    }

    private String getCurrentGPSAID() {
        return this.settings.getString(GPS_ADVERTISING_IDENTIFIER_SETTING, Stomp.EMPTY);
    }

    private boolean hasCurrentGPSAID() {
        return !StringUtils.isNullOrEmpty(getCurrentGPSAID());
    }
}
