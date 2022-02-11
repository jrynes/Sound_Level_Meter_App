package com.amazon.device.ads;

import com.amazon.device.ads.PreferredMarketplaceRetriever.NullPreferredMarketplaceRetriever;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONException;
import org.json.JSONObject;

class Configuration {
    private static final String AAX_MSDK_CONFIG_ENDPOINT = "/msdk/getConfig";
    private static final String AAX_PROD_US_HOSTNAME = "mads.amazon-adsystem.com";
    protected static final String CONFIG_APP_DEFINED_MARKETPLACE = "config-appDefinedMarketplace";
    protected static final String CONFIG_LASTFETCHTIME = "config-lastFetchTime";
    protected static final String CONFIG_TTL = "config-ttl";
    protected static final String CONFIG_VERSION_NAME = "configVersion";
    protected static final int CURRENT_CONFIG_VERSION = 3;
    private static final String LOGTAG;
    protected static final int MAX_CONFIG_TTL = 172800000;
    protected static final int MAX_NO_RETRY_TTL = 300000;
    private static Configuration instance;
    private final AdvertisingIdentifier advertisingIdentifier;
    private String appDefinedMarketplace;
    private final DebugProperties debugProperties;
    private final MobileAdsInfoStore infoStore;
    private boolean isAppDefinedMarketplaceSet;
    private final AtomicBoolean isFetching;
    private boolean isFirstParty;
    private Boolean lastTestModeValue;
    private final List<ConfigurationListener> listeners;
    private final MobileAdsLogger logger;
    private final Metrics metrics;
    private final PermissionChecker permissionChecker;
    private PreferredMarketplaceRetriever pfmRetriever;
    private final Settings settings;
    private final SystemTime systemTime;
    private final ThreadRunner threadRunner;
    private final WebRequestFactory webRequestFactory;

    interface ConfigurationListener {
        void onConfigurationFailure();

        void onConfigurationReady();
    }

    /* renamed from: com.amazon.device.ads.Configuration.1 */
    class C02991 implements Runnable {
        C02991() {
        }

        public void run() {
            Configuration.this.fetchConfigurationOnBackgroundThread();
        }
    }

    public static class ConfigOption {
        public static final ConfigOption AAX_HOSTNAME;
        public static final ConfigOption AD_PREF_URL;
        public static final ConfigOption MADS_HOSTNAME;
        public static final ConfigOption SEND_GEO;
        public static final ConfigOption SIS_DOMAIN;
        public static final ConfigOption SIS_URL;
        public static final ConfigOption TRUNCATE_LAT_LON;
        public static final ConfigOption[] configOptions;
        private final boolean allowEmpty;
        private final Class<?> dataType;
        private final String debugProperty;
        private final String responseKey;
        private final String settingsName;

        static {
            AAX_HOSTNAME = new ConfigOption("config-aaxHostname", String.class, "aaxHostname", DebugProperties.DEBUG_AAX_AD_HOSTNAME);
            SIS_URL = new ConfigOption("config-sisURL", String.class, "sisURL", DebugProperties.DEBUG_SIS_URL);
            AD_PREF_URL = new ConfigOption("config-adPrefURL", String.class, "adPrefURL", DebugProperties.DEBUG_AD_PREF_URL);
            MADS_HOSTNAME = new ConfigOption("config-madsHostname", String.class, "madsHostname", DebugProperties.DEBUG_MADS_HOSTNAME, true);
            SIS_DOMAIN = new ConfigOption("config-sisDomain", String.class, "sisDomain", DebugProperties.DEBUG_SIS_DOMAIN);
            SEND_GEO = new ConfigOption("config-sendGeo", Boolean.class, "sendGeo", DebugProperties.DEBUG_SEND_GEO);
            TRUNCATE_LAT_LON = new ConfigOption("config-truncateLatLon", Boolean.class, "truncateLatLon", DebugProperties.DEBUG_TRUNCATE_LAT_LON);
            configOptions = new ConfigOption[]{AAX_HOSTNAME, SIS_URL, AD_PREF_URL, MADS_HOSTNAME, SIS_DOMAIN, SEND_GEO, TRUNCATE_LAT_LON};
        }

        protected ConfigOption(String settingsName, Class<?> dataType, String responseKey, String debugProperty) {
            this(settingsName, dataType, responseKey, debugProperty, false);
        }

        protected ConfigOption(String settingsName, Class<?> dataType, String responseKey, String debugProperty, boolean allowEmpty) {
            this.settingsName = settingsName;
            this.responseKey = responseKey;
            this.dataType = dataType;
            this.debugProperty = debugProperty;
            this.allowEmpty = allowEmpty;
        }

        String getSettingsName() {
            return this.settingsName;
        }

        String getResponseKey() {
            return this.responseKey;
        }

        Class<?> getDataType() {
            return this.dataType;
        }

        String getDebugProperty() {
            return this.debugProperty;
        }

        boolean getAllowEmpty() {
            return this.allowEmpty;
        }
    }

    static {
        LOGTAG = Configuration.class.getSimpleName();
        instance = new Configuration();
    }

    protected Configuration() {
        this(new MobileAdsLoggerFactory(), new PermissionChecker(), new WebRequestFactory(), DebugProperties.getInstance(), Settings.getInstance(), MobileAdsInfoStore.getInstance(), new SystemTime(), Metrics.getInstance(), new AdvertisingIdentifier(), ThreadUtils.getThreadRunner());
    }

    Configuration(MobileAdsLoggerFactory loggerFactory, PermissionChecker permissionChecker, WebRequestFactory webRequestFactory, DebugProperties debugProperties, Settings settings, MobileAdsInfoStore infoStore, SystemTime systemTime, Metrics metrics, AdvertisingIdentifier advertisingIdentifier, ThreadRunner threadRunner) {
        this.appDefinedMarketplace = null;
        this.isAppDefinedMarketplaceSet = false;
        this.listeners = new ArrayList(5);
        this.isFetching = new AtomicBoolean(false);
        this.lastTestModeValue = null;
        this.isFirstParty = false;
        this.pfmRetriever = new NullPreferredMarketplaceRetriever();
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.permissionChecker = permissionChecker;
        this.webRequestFactory = webRequestFactory;
        this.debugProperties = debugProperties;
        this.settings = settings;
        this.infoStore = infoStore;
        this.systemTime = systemTime;
        this.metrics = metrics;
        this.advertisingIdentifier = advertisingIdentifier;
        this.threadRunner = threadRunner;
    }

    public static final Configuration getInstance() {
        return instance;
    }

    public String getAppDefinedMarketplace() {
        return this.appDefinedMarketplace;
    }

    public void setAppDefinedMarketplace(String appDefinedMarketplace) {
        this.appDefinedMarketplace = appDefinedMarketplace;
        this.isAppDefinedMarketplaceSet = true;
    }

    public void setIsFirstParty(boolean isFirstParty) {
        this.isFirstParty = isFirstParty;
    }

    boolean isFirstParty() {
        return this.isFirstParty;
    }

    public boolean hasValue(ConfigOption configOption) {
        return !StringUtils.isNullOrWhiteSpace(getString(configOption));
    }

    public String getString(ConfigOption configOption) {
        String value = this.debugProperties.getDebugPropertyAsString(configOption.getDebugProperty(), null);
        if (value == null) {
            return this.settings.getString(configOption.getSettingsName(), null);
        }
        return value;
    }

    public boolean getBoolean(ConfigOption configOption) {
        return getBooleanWithDefault(configOption, false);
    }

    public boolean getBooleanWithDefault(ConfigOption configOption, boolean defaultValue) {
        if (this.debugProperties.containsDebugProperty(configOption.getDebugProperty())) {
            return this.debugProperties.getDebugPropertyAsBoolean(configOption.getDebugProperty(), Boolean.valueOf(defaultValue)).booleanValue();
        }
        return this.settings.getBoolean(configOption.getSettingsName(), defaultValue);
    }

    protected boolean shouldFetch() {
        if (hasAppDefinedMarketplaceChanged() || this.settings.getInt(CONFIG_VERSION_NAME, 0) != CURRENT_CONFIG_VERSION) {
            return true;
        }
        long currentTime = this.systemTime.currentTimeMillis();
        long lastFetchTime = this.settings.getLong(CONFIG_LASTFETCHTIME, 0);
        long ttl = this.settings.getLong(CONFIG_TTL, 172800000);
        if (lastFetchTime == 0) {
            this.logger.m637d("No configuration found. A new configuration will be retrieved.");
            return true;
        } else if (currentTime - lastFetchTime > ttl) {
            this.logger.m637d("The configuration has expired. A new configuration will be retrieved.");
            return true;
        } else if (this.lastTestModeValue != null && this.lastTestModeValue.booleanValue() != this.settings.getBoolean("testingEnabled", false)) {
            this.logger.m637d("The testing mode has changed. A new configuration will be retrieved.");
            return true;
        } else if (this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_SHOULD_FETCH_CONFIG, Boolean.valueOf(false)).booleanValue()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasAppDefinedMarketplaceChanged() {
        String storedAppDefinedMarketplace = this.settings.getString(CONFIG_APP_DEFINED_MARKETPLACE, null);
        if (!this.isAppDefinedMarketplaceSet) {
            return false;
        }
        this.isAppDefinedMarketplaceSet = false;
        if (this.appDefinedMarketplace != null && !this.appDefinedMarketplace.equals(storedAppDefinedMarketplace)) {
            this.settings.putLongWithNoFlush(CONFIG_LASTFETCHTIME, 0);
            this.settings.putStringWithNoFlush(CONFIG_APP_DEFINED_MARKETPLACE, this.appDefinedMarketplace);
            this.settings.flush();
            this.infoStore.getRegistrationInfo().requestNewSISDeviceIdentifier();
            this.logger.m637d("New application-defined marketplace set. A new configuration will be retrieved.");
            return true;
        } else if (storedAppDefinedMarketplace == null || this.appDefinedMarketplace != null) {
            return false;
        } else {
            this.settings.remove(CONFIG_APP_DEFINED_MARKETPLACE);
            this.infoStore.getRegistrationInfo().requestNewSISDeviceIdentifier();
            this.logger.m637d("Application-defined marketplace removed. A new configuration will be retrieved.");
            return true;
        }
    }

    protected boolean isFetching() {
        return this.isFetching.get();
    }

    protected void setIsFetching(boolean isFetching) {
        this.isFetching.set(isFetching);
    }

    public synchronized void queueConfigurationListener(ConfigurationListener listener) {
        queueConfigurationListener(listener, true);
    }

    public synchronized void queueConfigurationListener(ConfigurationListener listener, boolean allowFetch) {
        if (isFetching()) {
            this.listeners.add(listener);
        } else if (shouldFetch()) {
            this.listeners.add(listener);
            if (allowFetch) {
                this.logger.m637d("Starting configuration fetching...");
                setIsFetching(true);
                beginFetch();
            }
        } else {
            listener.onConfigurationReady();
        }
    }

    protected void beginFetch() {
        this.threadRunner.execute(new C02991(), ExecutionStyle.SCHEDULE, ExecutionThread.BACKGROUND_THREAD);
    }

    protected synchronized void onFetchSuccess() {
        setIsFetching(false);
        for (ConfigurationListener listener : getAndClearListeners()) {
            listener.onConfigurationReady();
        }
    }

    protected synchronized void onFetchFailure() {
        this.metrics.getMetricsCollector().incrementMetric(MetricType.AAX_CONFIG_DOWNLOAD_FAILED);
        setIsFetching(false);
        for (ConfigurationListener listener : getAndClearListeners()) {
            listener.onConfigurationFailure();
        }
    }

    protected synchronized ConfigurationListener[] getAndClearListeners() {
        ConfigurationListener[] toCall;
        toCall = (ConfigurationListener[]) this.listeners.toArray(new ConfigurationListener[this.listeners.size()]);
        this.listeners.clear();
        return toCall;
    }

    protected ConfigOption[] getConfigOptions() {
        return ConfigOption.configOptions;
    }

    protected void setLastTestModeValue(boolean testMode) {
        this.lastTestModeValue = Boolean.valueOf(testMode);
    }

    protected void fetchConfigurationOnBackgroundThread() {
        this.logger.m637d("In configuration fetcher background thread.");
        if (this.permissionChecker.hasInternetPermission(this.infoStore.getApplicationContext())) {
            Info advertisingIdentifierInfo = createAdvertisingIdentifierInfo();
            if (advertisingIdentifierInfo.canDo()) {
                try {
                    JSONObject json = createWebRequest(advertisingIdentifierInfo).makeCall().getResponseReader().readAsJSON();
                    try {
                        for (ConfigOption configOption : getConfigOptions()) {
                            if (!json.isNull(configOption.getResponseKey())) {
                                writeSettingFromConfigOption(configOption, json);
                            } else if (configOption.getAllowEmpty()) {
                                this.settings.removeWithNoFlush(configOption.getSettingsName());
                            } else {
                                throw new Exception("The configuration value for " + configOption.getResponseKey() + " must be present and not null.");
                            }
                        }
                        if (json.isNull("ttl")) {
                            throw new Exception("The configuration value must be present and not null.");
                        }
                        long ttl = NumberUtils.convertToMillisecondsFromSeconds((long) json.getInt("ttl"));
                        if (ttl > 172800000) {
                            ttl = 172800000;
                        }
                        this.settings.putLongWithNoFlush(CONFIG_TTL, ttl);
                        this.settings.putLongWithNoFlush(CONFIG_LASTFETCHTIME, this.systemTime.currentTimeMillis());
                        this.settings.putIntWithNoFlush(CONFIG_VERSION_NAME, CURRENT_CONFIG_VERSION);
                        this.settings.flush();
                        this.logger.m637d("Configuration fetched and saved.");
                        onFetchSuccess();
                        return;
                    } catch (JSONException e) {
                        this.logger.m640e("Unable to parse JSON response: %s", e.getMessage());
                        onFetchFailure();
                        return;
                    } catch (Exception e2) {
                        this.logger.m640e("Unexpected error during parsing: %s", e2.getMessage());
                        onFetchFailure();
                        return;
                    }
                } catch (WebRequestException e3) {
                    onFetchFailure();
                    return;
                }
            }
            onFetchFailure();
            return;
        }
        this.logger.m639e("Network task cannot commence because the INTERNET permission is missing from the app's manifest.");
        onFetchFailure();
    }

    private void writeSettingFromConfigOption(ConfigOption configOption, JSONObject json) throws Exception {
        if (configOption.getDataType().equals(String.class)) {
            String value = json.getString(configOption.getResponseKey());
            if (configOption.getAllowEmpty() || !StringUtils.isNullOrWhiteSpace(value)) {
                this.settings.putStringWithNoFlush(configOption.getSettingsName(), value);
                return;
            }
            throw new IllegalArgumentException("The configuration value must not be empty or contain only white spaces.");
        } else if (configOption.getDataType().equals(Boolean.class)) {
            this.settings.putBooleanWithNoFlush(configOption.getSettingsName(), json.getBoolean(configOption.getResponseKey()));
        } else {
            throw new IllegalArgumentException("Undefined configuration option type.");
        }
    }

    protected WebRequest createWebRequest(Info advertisingIdentifierInfo) {
        WebRequest request = this.webRequestFactory.createJSONGetWebRequest();
        request.setExternalLogTag(LOGTAG);
        request.enableLog(true);
        request.setHost(this.debugProperties.getDebugPropertyAsString(DebugProperties.DEBUG_AAX_CONFIG_HOSTNAME, AAX_PROD_US_HOSTNAME));
        request.setPath(AAX_MSDK_CONFIG_ENDPOINT);
        request.setMetricsCollector(this.metrics.getMetricsCollector());
        request.setServiceCallLatencyMetric(MetricType.AAX_CONFIG_DOWNLOAD_LATENCY);
        request.setUseSecure(this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_AAX_CONFIG_USE_SECURE, Boolean.valueOf(true)).booleanValue());
        RegistrationInfo registrationInfo = this.infoStore.getRegistrationInfo();
        DeviceInfo deviceInfo = this.infoStore.getDeviceInfo();
        request.putUnencodedQueryParameter("appId", registrationInfo.getAppKey());
        request.putUnencodedQueryParameter("dinfo", deviceInfo.getDInfoProperty().toString());
        request.putUnencodedQueryParameter("adId", advertisingIdentifierInfo.getSISDeviceIdentifier());
        request.putUnencodedQueryParameter("sdkVer", Version.getSDKVersion());
        request.putUnencodedQueryParameter("fp", Boolean.toString(this.isFirstParty));
        request.putUnencodedQueryParameter("mkt", this.settings.getString(CONFIG_APP_DEFINED_MARKETPLACE, null));
        request.putUnencodedQueryParameter("pfm", getPreferredMarketplace());
        boolean testingEnabled = this.settings.getBoolean("testingEnabled", false);
        setLastTestModeValue(testingEnabled);
        if (testingEnabled) {
            request.putUnencodedQueryParameter("testMode", Stomp.TRUE);
        }
        request.setAdditionalQueryParamsString(this.debugProperties.getDebugPropertyAsString(DebugProperties.DEBUG_AAX_CONFIG_PARAMS, null));
        return request;
    }

    Info createAdvertisingIdentifierInfo() {
        boolean z = false;
        AdvertisingIdentifier advertisingIdentifier = this.advertisingIdentifier;
        if (this.settings.getInt(CONFIG_VERSION_NAME, 0) != 0) {
            z = true;
        }
        advertisingIdentifier.setShouldSetCurrentAdvertisingIdentifier(z);
        return this.advertisingIdentifier.getAdvertisingIdentifierInfo();
    }

    public void setPreferredMarketplaceRetriever(PreferredMarketplaceRetriever pfmRetriever) {
        this.pfmRetriever = pfmRetriever;
    }

    PreferredMarketplaceRetriever getPreferredMarketplaceRetriever() {
        return this.pfmRetriever;
    }

    private String getPreferredMarketplace() {
        return this.pfmRetriever.retrievePreferredMarketplace(MobileAdsInfoStore.getInstance().getApplicationContext());
    }
}
