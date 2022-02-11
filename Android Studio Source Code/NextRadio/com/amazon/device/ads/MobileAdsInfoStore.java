package com.amazon.device.ads;

import android.content.Context;
import java.io.File;
import org.apache.activemq.ActiveMQPrefetchPolicy;

class MobileAdsInfoStore {
    private static MobileAdsInfoStore instance;
    private AppInfo appInfo;
    protected Context applicationContext;
    private boolean contextReceived;
    private final DebugProperties debugProperties;
    private DeviceInfo deviceInfo;
    private File filesDirectory;
    private boolean isAppDisabled;
    private boolean isRegistered;
    private long noRetryTtlExpiresMillis;
    private int noRetryTtlMillis;
    private RegistrationInfo registrationInfo;
    private final Settings settings;
    private SISRegistration sisRegistration;

    static {
        instance = new MobileAdsInfoStore(Settings.getInstance(), DebugProperties.getInstance());
    }

    protected MobileAdsInfoStore(Settings settings, DebugProperties debugProperties) {
        this.isAppDisabled = false;
        this.settings = settings;
        this.debugProperties = debugProperties;
        this.debugProperties.readDebugProperties();
        this.registrationInfo = new RegistrationInfo();
    }

    public static MobileAdsInfoStore getInstance() {
        return instance;
    }

    static void setMobileAdsInfoStore(MobileAdsInfoStore infoStore) {
        instance = infoStore;
    }

    public AppInfo getAppInfo() {
        return this.appInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return this.deviceInfo;
    }

    public RegistrationInfo getRegistrationInfo() {
        return this.registrationInfo;
    }

    public synchronized void contextReceived(Context context) {
        if (!this.contextReceived) {
            this.contextReceived = true;
            setApplicationContextFromContext(context);
            setFilesDirectory(context);
            this.settings.contextReceived(context);
            createAppInfo(context);
            this.deviceInfo = createDeviceInfo(context);
            createSISRegistration();
        }
    }

    public boolean isContextReceived() {
        return this.contextReceived;
    }

    protected void setFilesDirectory(Context context) {
        this.filesDirectory = context.getFilesDir();
    }

    protected void createAppInfo(Context context) {
        this.appInfo = new AppInfo(context);
    }

    protected DeviceInfo createDeviceInfo(Context context) {
        return new DeviceInfo(context, new BasicUserAgentManager());
    }

    public void register() {
        getSISRegistration().registerApp();
        this.isRegistered = true;
    }

    protected void createSISRegistration() {
        this.sisRegistration = new SISRegistration();
    }

    public SISRegistration getSISRegistration() {
        return this.sisRegistration;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    public int getNoRetryTtlRemainingMillis() {
        if (this.noRetryTtlMillis == 0 || this.noRetryTtlExpiresMillis == 0) {
            return 0;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime < this.noRetryTtlExpiresMillis) {
            return (int) (this.noRetryTtlExpiresMillis - currentTime);
        }
        this.noRetryTtlMillis = 0;
        this.noRetryTtlExpiresMillis = 0;
        return 0;
    }

    public void setNoRetryTtl(int noRetryTtlSeconds) {
        int maxNoRetryTtlSeconds = this.debugProperties.getDebugPropertyAsInteger(DebugProperties.DEBUG_NORETRYTTL_MAX, Integer.valueOf(300000)).intValue();
        if (maxNoRetryTtlSeconds < noRetryTtlSeconds) {
            noRetryTtlSeconds = maxNoRetryTtlSeconds;
        }
        if (noRetryTtlSeconds == 0) {
            this.noRetryTtlMillis = 0;
            this.noRetryTtlExpiresMillis = 0;
            return;
        }
        this.noRetryTtlMillis = noRetryTtlSeconds * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        this.noRetryTtlExpiresMillis = System.currentTimeMillis() + ((long) this.noRetryTtlMillis);
    }

    public boolean getIsAppDisabled() {
        return this.isAppDisabled;
    }

    public void setIsAppDisabled(boolean isAppDisabled) {
        this.isAppDisabled = isAppDisabled;
    }

    public File getFilesDir() {
        return this.filesDirectory;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    protected void setApplicationContextFromContext(Context context) {
        this.applicationContext = context.getApplicationContext();
    }
}
