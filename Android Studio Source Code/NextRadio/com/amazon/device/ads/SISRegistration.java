package com.amazon.device.ads;

import com.amazon.device.ads.ThreadUtils.RunnableExecutor;
import com.amazon.device.ads.ThreadUtils.SingleThreadScheduler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;

class SISRegistration {
    private static final String LOGTAG;
    protected static final long SIS_CHECKIN_INTERVAL = 86400000;
    private static final String SIS_LAST_CHECKIN_PREF_NAME = "amzn-ad-sis-last-checkin";
    private static final SingleThreadScheduler singleThreadScheduler;
    private final AdvertisingIdentifier advertisingIdentifier;
    private final AppEventRegistrationHandler appEventRegistrationHandler;
    private final Configuration configuration;
    private final DebugProperties debugProperties;
    private final RunnableExecutor executor;
    private final MobileAdsInfoStore infoStore;
    private final MobileAdsLogger logger;
    private final Settings settings;
    private final SISRequestFactory sisRequestFactory;
    private final SISRequestorFactory sisRequestorFactory;
    private final SystemTime systemTime;
    private final ThreadVerify threadVerify;

    /* renamed from: com.amazon.device.ads.SISRegistration.1 */
    class C03371 implements Runnable {
        C03371() {
        }

        public void run() {
            SISRegistration.this.waitForConfigurationThenBeginRegistration();
        }
    }

    /* renamed from: com.amazon.device.ads.SISRegistration.2 */
    class C03382 implements ConfigurationListener {
        final /* synthetic */ AtomicBoolean val$canRegister;
        final /* synthetic */ CountDownLatch val$latch;

        C03382(AtomicBoolean atomicBoolean, CountDownLatch countDownLatch) {
            this.val$canRegister = atomicBoolean;
            this.val$latch = countDownLatch;
        }

        public void onConfigurationReady() {
            this.val$canRegister.set(true);
            this.val$latch.countDown();
        }

        public void onConfigurationFailure() {
            SISRegistration.this.getLogger().m645w("Configuration fetching failed so device registration will not proceed.");
            this.val$latch.countDown();
        }
    }

    protected static class RegisterEventsSISRequestorCallback implements SISRequestorCallback {
        private final SISRegistration sisRegistration;

        public RegisterEventsSISRequestorCallback(SISRegistration sisRegistration) {
            this.sisRegistration = sisRegistration;
        }

        public void onSISCallComplete() {
            this.sisRegistration.registerEvents();
        }
    }

    static {
        LOGTAG = SISRegistration.class.getSimpleName();
        singleThreadScheduler = new SingleThreadScheduler();
    }

    public SISRegistration() {
        this(new SISRequestFactory(), new SISRequestorFactory(), new AdvertisingIdentifier(), MobileAdsInfoStore.getInstance(), Configuration.getInstance(), Settings.getInstance(), AppEventRegistrationHandler.getInstance(), new SystemTime(), singleThreadScheduler, new ThreadVerify(), new MobileAdsLoggerFactory(), DebugProperties.getInstance());
    }

    SISRegistration(SISRequestFactory sisRequestFactory, SISRequestorFactory sisRequestorFactory, AdvertisingIdentifier advertisingIdentifier, MobileAdsInfoStore infoStore, Configuration configuration, Settings settings, AppEventRegistrationHandler appEventRegistrationHandler, SystemTime systemTime, RunnableExecutor runnableExecutor, ThreadVerify threadVerify, MobileAdsLoggerFactory loggerFactory, DebugProperties debugProperties) {
        this.sisRequestFactory = sisRequestFactory;
        this.sisRequestorFactory = sisRequestorFactory;
        this.advertisingIdentifier = advertisingIdentifier;
        this.infoStore = infoStore;
        this.configuration = configuration;
        this.settings = settings;
        this.appEventRegistrationHandler = appEventRegistrationHandler;
        this.systemTime = systemTime;
        this.executor = runnableExecutor;
        this.threadVerify = threadVerify;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.debugProperties = debugProperties;
    }

    private MobileAdsLogger getLogger() {
        return this.logger;
    }

    protected boolean canRegister(long currentTime) {
        RegistrationInfo registrationInfo = this.infoStore.getRegistrationInfo();
        if (exceededCheckinInterval(currentTime) || registrationInfo.shouldGetNewSISDeviceIdentifer() || registrationInfo.shouldGetNewSISRegistration() || this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_SHOULD_REGISTER_SIS, Boolean.valueOf(false)).booleanValue()) {
            return true;
        }
        return false;
    }

    protected boolean shouldUpdateDeviceInfo() {
        return this.infoStore.getRegistrationInfo().isRegisteredWithSIS();
    }

    public void registerApp() {
        this.executor.execute(new C03371());
    }

    void waitForConfigurationThenBeginRegistration() {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean canRegister = new AtomicBoolean(false);
        this.configuration.queueConfigurationListener(new C03382(canRegister, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        if (canRegister.get()) {
            registerAppWorker();
        }
    }

    void registerAppWorker() {
        long currentTime = this.systemTime.currentTimeMillis();
        if (this.advertisingIdentifier.getAdvertisingIdentifierInfo().canDo() && canRegister(currentTime)) {
            putLastSISCheckin(currentTime);
            if (shouldUpdateDeviceInfo()) {
                updateDeviceInfo(this.advertisingIdentifier);
            } else {
                register(this.advertisingIdentifier);
            }
        }
    }

    protected boolean exceededCheckinInterval(long now) {
        return now - getLastSISCheckin() > SIS_CHECKIN_INTERVAL;
    }

    protected void register(AdvertisingIdentifier advertisingIdentifier) {
        SISRequest generateDIDRequest = this.sisRequestFactory.createDeviceRequest(SISRequestType.GENERATE_DID).setAdvertisingIdentifier(advertisingIdentifier);
        SISRequestorCallback sisRequestorCallback = new RegisterEventsSISRequestorCallback(this);
        this.sisRequestorFactory.createSISRequestor(sisRequestorCallback, generateDIDRequest).startCallSIS();
    }

    protected void updateDeviceInfo(AdvertisingIdentifier advertisingIdentifier) {
        SISRequest updateDeviceInfoRequest = this.sisRequestFactory.createDeviceRequest(SISRequestType.UPDATE_DEVICE_INFO).setAdvertisingIdentifier(advertisingIdentifier);
        SISRequestorCallback sisRequestorCallback = new RegisterEventsSISRequestorCallback(this);
        this.sisRequestorFactory.createSISRequestor(sisRequestorCallback, updateDeviceInfoRequest).startCallSIS();
    }

    protected long getLastSISCheckin() {
        return this.settings.getLong(SIS_LAST_CHECKIN_PREF_NAME, 0);
    }

    private void putLastSISCheckin(long currentTime) {
        this.settings.putLong(SIS_LAST_CHECKIN_PREF_NAME, currentTime);
    }

    protected void registerEvents() {
        if (this.threadVerify.isOnMainThread()) {
            getLogger().m639e("Registering events must be done on a background thread.");
            return;
        }
        Info advertisingIdentifierInfo = this.advertisingIdentifier.getAdvertisingIdentifierInfo();
        if (advertisingIdentifierInfo.hasSISDeviceIdentifier()) {
            JSONArray appEvents = this.appEventRegistrationHandler.getAppEventsJSONArray();
            if (appEvents != null) {
                SISRegisterEventRequest registerEventRequest = this.sisRequestFactory.createRegisterEventRequest(advertisingIdentifierInfo, appEvents);
                this.sisRequestorFactory.createSISRequestor(registerEventRequest).startCallSIS();
            }
        }
    }
}
