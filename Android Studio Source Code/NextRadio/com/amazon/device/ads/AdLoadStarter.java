package com.amazon.device.ads;

import android.annotation.SuppressLint;
import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.activemq.ActiveMQPrefetchPolicy;

class AdLoadStarter {
    private static final String LOGTAG;
    private final AdLoaderFactory adLoaderFactory;
    private final AdRequestBuilder adRequestBuilder;
    private final AdvertisingIdentifier advertisingIdentifier;
    private final Configuration configuration;
    private final MobileAdsInfoStore infoStore;
    private final MobileAdsLogger logger;
    private final Settings settings;
    private final SystemTime systemTime;
    private final ThreadRunner threadRunner;

    /* renamed from: com.amazon.device.ads.AdLoadStarter.1 */
    class C02871 extends StartUpWaiter {
        final /* synthetic */ ArrayList val$requestAdSlots;
        final /* synthetic */ AdTargetingOptions val$requestOptions;
        final /* synthetic */ int val$timeout;

        /* renamed from: com.amazon.device.ads.AdLoadStarter.1.1 */
        class C02861 implements Runnable {
            C02861() {
            }

            public void run() {
                AdLoadStarter.this.failAds(new AdError(ErrorCode.NETWORK_ERROR, "The configuration was unable to be loaded"), C02871.this.val$requestAdSlots);
            }
        }

        C02871(Settings x0, Configuration x1, int i, AdTargetingOptions adTargetingOptions, ArrayList arrayList) {
            this.val$timeout = i;
            this.val$requestOptions = adTargetingOptions;
            this.val$requestAdSlots = arrayList;
            super(x0, x1);
        }

        protected void startUpReady() {
            AdLoadStarter.this.infoStore.register();
            AdLoadStarter.this.beginFetchAds(this.val$timeout, this.val$requestOptions, this.val$requestAdSlots);
        }

        protected void startUpFailed() {
            AdLoadStarter.this.threadRunner.execute(new C02861(), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
        }
    }

    static {
        LOGTAG = AdLoadStarter.class.getSimpleName();
    }

    public AdLoadStarter() {
        this(new AdLoaderFactory(), new AdvertisingIdentifier(), ThreadUtils.getThreadRunner(), MobileAdsInfoStore.getInstance(), Settings.getInstance(), Configuration.getInstance(), new MobileAdsLoggerFactory(), new SystemTime(), new AdRequestBuilder());
    }

    AdLoadStarter(AdLoaderFactory adLoaderFactory, AdvertisingIdentifier advertisingIdentifier, ThreadRunner threadRunner, MobileAdsInfoStore infoStore, Settings settings, Configuration configuration, MobileAdsLoggerFactory loggerFactory, SystemTime systemTime, AdRequestBuilder adRequestBuilder) {
        this.adLoaderFactory = adLoaderFactory;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.advertisingIdentifier = advertisingIdentifier;
        this.infoStore = infoStore;
        this.settings = settings;
        this.configuration = configuration;
        this.threadRunner = threadRunner;
        this.systemTime = systemTime;
        this.adRequestBuilder = adRequestBuilder;
    }

    public void loadAds(int timeout, AdTargetingOptions requestOptions, AdSlot... adSlots) {
        if (!isNoRetry(adSlots)) {
            long loadAdStartTime = this.systemTime.nanoTime();
            ArrayList<AdSlot> requestAdSlots = new ArrayList();
            for (AdSlot adSlot : adSlots) {
                if (adSlot.prepareForAdLoad(loadAdStartTime)) {
                    requestAdSlots.add(adSlot);
                }
            }
            new C02871(this.settings, this.configuration, timeout, requestOptions, requestAdSlots).start();
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private void beginFetchAds(int timeout, AdTargetingOptions requestOptions, List<AdSlot> adSlots) {
        Info advertisingIdentifierInfo = this.advertisingIdentifier.getAdvertisingIdentifierInfo();
        if (advertisingIdentifierInfo.canDo()) {
            if (requestOptions == null) {
                requestOptions = new AdTargetingOptions();
            }
            AdRequest request = this.adRequestBuilder.withAdTargetingOptions(requestOptions).withAdvertisingIdentifierInfo(advertisingIdentifierInfo).build();
            HashMap<Integer, AdSlot> goodAdSlots = new HashMap();
            int slotNumber = 1;
            for (AdSlot slot : adSlots) {
                if (slot.isValid()) {
                    slot.setSlotNumber(slotNumber);
                    goodAdSlots.put(Integer.valueOf(slotNumber), slot);
                    request.putSlot(slot);
                    slotNumber++;
                }
            }
            if (goodAdSlots.size() > 0) {
                AdLoader adLoader = this.adLoaderFactory.createAdLoader(request, goodAdSlots);
                adLoader.setTimeout(timeout);
                adLoader.beginFetchAd();
                return;
            }
            return;
        }
        failAds(new AdError(ErrorCode.INTERNAL_ERROR, "An internal request was not made on a background thread."), adSlots);
    }

    private void failAds(AdError adError, List<AdSlot> adSlots) {
        int adFailCount = 0;
        for (AdSlot slot : adSlots) {
            if (slot.getSlotNumber() != -1) {
                slot.adFailed(adError);
                adFailCount++;
            }
        }
        if (adFailCount > 0) {
            this.logger.m640e("%s; code: %s", adError.getMessage(), adError.getCode());
        }
    }

    private boolean isNoRetry(AdSlot[] adSlots) {
        int noRetryTtlRemainingMillis = this.infoStore.getNoRetryTtlRemainingMillis();
        if (noRetryTtlRemainingMillis <= 0) {
            return false;
        }
        ErrorCode errorCode;
        int noRetryTtlRemainingSecs = noRetryTtlRemainingMillis / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        String errorMessage = "SDK Message: ";
        if (this.infoStore.getIsAppDisabled()) {
            errorMessage = errorMessage + AdLoader.DISABLED_APP_SERVER_MESSAGE;
            errorCode = ErrorCode.INTERNAL_ERROR;
        } else {
            errorMessage = errorMessage + "no results. Try again in " + noRetryTtlRemainingSecs + " seconds.";
            errorCode = ErrorCode.NO_FILL;
        }
        failAds(new AdError(errorCode, errorMessage), new ArrayList(Arrays.asList(adSlots)));
        return true;
    }
}
