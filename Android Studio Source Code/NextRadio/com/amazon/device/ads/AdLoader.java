package com.amazon.device.ads;

import com.amazon.device.ads.AdError.ErrorCode;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestStatus;
import com.amazon.device.ads.WebRequest.WebResponse;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.json.JSONObject;

class AdLoader {
    public static final int AD_FAILED = -1;
    public static final int AD_LOAD_DEFERRED = 1;
    public static final int AD_READY_TO_LOAD = 0;
    public static final String DISABLED_APP_SERVER_MESSAGE = "DISABLED_APP";
    private static final String LOGTAG;
    private final AdRequest adRequest;
    private final Assets assets;
    private CompositeMetricsCollector compositeMetricsCollector;
    private final DebugProperties debugProperties;
    private AdError error;
    private final MobileAdsInfoStore infoStore;
    private final MobileAdsLogger logger;
    private final Map<Integer, AdSlot> slots;
    private final SystemTime systemTime;
    private final ThreadRunner threadRunner;
    private int timeout;

    /* renamed from: com.amazon.device.ads.AdLoader.1 */
    class C02881 implements Runnable {
        C02881() {
        }

        public void run() {
            AdLoader.this.fetchAd();
            AdLoader.this.beginFinalizeFetchAd();
        }
    }

    /* renamed from: com.amazon.device.ads.AdLoader.2 */
    class C02892 implements Runnable {
        C02892() {
        }

        public void run() {
            AdLoader.this.finalizeFetchAd();
        }
    }

    protected class AdFetchException extends Exception {
        private static final long serialVersionUID = 1;
        private final AdError adError;

        public AdFetchException(AdError adError) {
            this.adError = adError;
        }

        public AdFetchException(AdError adError, Throwable throwable) {
            super(throwable);
            this.adError = adError;
        }

        public AdError getAdError() {
            return this.adError;
        }
    }

    protected static class AdLoaderFactory {
        protected AdLoaderFactory() {
        }

        public AdLoader createAdLoader(AdRequest adRequest, Map<Integer, AdSlot> slots) {
            return new AdLoader(adRequest, slots);
        }
    }

    static {
        LOGTAG = AdLoader.class.getSimpleName();
    }

    public AdLoader(AdRequest adRequest, Map<Integer, AdSlot> slots) {
        this(adRequest, slots, ThreadUtils.getThreadRunner(), new SystemTime(), Assets.getInstance(), MobileAdsInfoStore.getInstance(), new MobileAdsLoggerFactory(), DebugProperties.getInstance());
    }

    AdLoader(AdRequest adRequest, Map<Integer, AdSlot> slots, ThreadRunner threadRunner, SystemTime systemTime, Assets assets, MobileAdsInfoStore infoStore, MobileAdsLoggerFactory loggerFactory, DebugProperties debugProperties) {
        this.timeout = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        this.error = null;
        this.compositeMetricsCollector = null;
        this.adRequest = adRequest;
        this.slots = slots;
        this.threadRunner = threadRunner;
        this.systemTime = systemTime;
        this.assets = assets;
        this.infoStore = infoStore;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.debugProperties = debugProperties;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void beginFetchAd() {
        getCompositeMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_LOADAD_TO_FETCH_THREAD_REQUEST_START);
        getCompositeMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_FETCH_THREAD_SPIN_UP);
        startFetchAdThread();
    }

    protected void startFetchAdThread() {
        this.threadRunner.execute(new C02881(), ExecutionStyle.SCHEDULE, ExecutionThread.BACKGROUND_THREAD);
    }

    private void beginFinalizeFetchAd() {
        this.threadRunner.execute(new C02892(), ExecutionStyle.SCHEDULE, ExecutionThread.MAIN_THREAD);
    }

    protected void fetchAd() {
        getCompositeMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_FETCH_THREAD_SPIN_UP);
        getCompositeMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_FETCH_THREAD_START_TO_AAX_GET_AD_START);
        String str;
        if (this.assets.ensureAssetsCreated()) {
            try {
                WebResponse response = fetchResponseFromNetwork();
                if (response.isHttpStatusCodeOK()) {
                    JSONObject jsonResponse = response.getResponseReader().readAsJSON();
                    if (jsonResponse == null) {
                        str = "Unable to parse response";
                        this.error = new AdError(ErrorCode.INTERNAL_ERROR, "Unable to parse response");
                        this.logger.m639e("Unable to parse response");
                        setErrorForAllSlots(this.error);
                        return;
                    }
                    parseResponse(jsonResponse);
                    getCompositeMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_AAX_GET_AD_END_TO_FETCH_THREAD_END);
                    getCompositeMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_SPIN_UP);
                    return;
                }
                str = response.getHttpStatusCode() + " - " + response.getHttpStatus();
                this.error = new AdError(ErrorCode.NETWORK_ERROR, str);
                this.logger.m639e(str);
                setErrorForAllSlots(this.error);
                return;
            } catch (AdFetchException e) {
                this.error = e.getAdError();
                this.logger.m639e(e.getAdError().getMessage());
                setErrorForAllSlots(this.error);
                return;
            }
        }
        str = "Unable to create the assets needed to display ads";
        this.error = new AdError(ErrorCode.REQUEST_ERROR, "Unable to create the assets needed to display ads");
        this.logger.m639e("Unable to create the assets needed to display ads");
        setErrorForAllSlots(this.error);
    }

    private WebRequest getAdRequest() throws AdFetchException {
        getCompositeMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_CREATE_AAX_GET_AD_URL);
        WebRequest request = this.adRequest.getWebRequest();
        getCompositeMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_CREATE_AAX_GET_AD_URL);
        return request;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseResponse(org.json.JSONObject r43) {
        /*
        r42 = this;
        r0 = r42;
        r0 = r0.systemTime;
        r37 = r0;
        r16 = r37.currentTimeMillis();
        r37 = "status";
        r38 = 0;
        r0 = r43;
        r1 = r37;
        r2 = r38;
        r36 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r35 = new java.util.HashSet;
        r0 = r42;
        r0 = r0.slots;
        r37 = r0;
        r37 = r37.keySet();
        r0 = r35;
        r1 = r37;
        r0.<init>(r1);
        r6 = r42.getAdError(r43);
        r37 = "errorCode";
        r38 = "No Ad Received";
        r0 = r43;
        r1 = r37;
        r2 = r38;
        r19 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r0 = r42;
        r0 = r0.adRequest;
        r37 = r0;
        r38 = "instrPixelURL";
        r39 = 0;
        r0 = r43;
        r1 = r38;
        r2 = r39;
        r38 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r37.setInstrumentationPixelURL(r38);
        if (r36 == 0) goto L_0x024c;
    L_0x0056:
        r37 = "ok";
        r37 = r36.equals(r37);
        if (r37 == 0) goto L_0x024c;
    L_0x005e:
        r37 = "ads";
        r0 = r43;
        r1 = r37;
        r11 = com.amazon.device.ads.JSONUtils.getJSONArrayFromJSON(r0, r1);
        r24 = 0;
    L_0x006a:
        r37 = r11.length();
        r0 = r24;
        r1 = r37;
        if (r0 >= r1) goto L_0x024c;
    L_0x0074:
        r0 = r24;
        r33 = com.amazon.device.ads.JSONUtils.getJSONObjectFromJSONArray(r11, r0);
        if (r33 != 0) goto L_0x007f;
    L_0x007c:
        r24 = r24 + 1;
        goto L_0x006a;
    L_0x007f:
        r37 = "slotId";
        r38 = -1;
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r34 = com.amazon.device.ads.JSONUtils.getIntegerFromJSON(r0, r1, r2);
        r0 = r42;
        r0 = r0.slots;
        r37 = r0;
        r38 = java.lang.Integer.valueOf(r34);
        r9 = r37.get(r38);
        r9 = (com.amazon.device.ads.AdSlot) r9;
        if (r9 == 0) goto L_0x007c;
    L_0x009f:
        r37 = java.lang.Integer.valueOf(r34);
        r0 = r35;
        r1 = r37;
        r0.remove(r1);
        r37 = "instrPixelURL";
        r0 = r42;
        r0 = r0.adRequest;
        r38 = r0;
        r38 = r38.getInstrumentationPixelURL();
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r8 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r5 = new com.amazon.device.ads.AdData;
        r5.<init>();
        r5.setInstrumentationPixelUrl(r8);
        r37 = "impPixelURL";
        r38 = 0;
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r26 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r0 = r26;
        r5.setImpressionPixelUrl(r0);
        r37 = r9.getRequestedAdSize();
        r37 = r37.isAuto();
        if (r37 == 0) goto L_0x00ee;
    L_0x00e5:
        r37 = r9.getMetricsCollector();
        r38 = com.amazon.device.ads.Metrics.MetricType.AD_COUNTER_AUTO_AD_SIZE;
        r37.incrementMetric(r38);
    L_0x00ee:
        r37 = "html";
        r38 = "";
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r12 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        r37 = "creativeTypes";
        r0 = r33;
        r1 = r37;
        r15 = com.amazon.device.ads.JSONUtils.getJSONArrayFromJSON(r0, r1);
        r13 = new java.util.HashSet;
        r13.<init>();
        if (r15 == 0) goto L_0x014b;
    L_0x010d:
        r27 = 0;
    L_0x010f:
        r37 = r15.length();
        r0 = r27;
        r1 = r37;
        if (r0 >= r1) goto L_0x014b;
    L_0x0119:
        r37 = 0;
        r0 = r27;
        r1 = r37;
        r14 = com.amazon.device.ads.JSONUtils.getIntegerFromJSONArray(r15, r0, r1);
        r4 = com.amazon.device.ads.AAXCreative.getCreativeType(r14);
        if (r4 == 0) goto L_0x012f;
    L_0x0129:
        r13.add(r4);
    L_0x012c:
        r27 = r27 + 1;
        goto L_0x010f;
    L_0x012f:
        r0 = r42;
        r0 = r0.logger;
        r37 = r0;
        r38 = "%d is not a recognized creative type.";
        r39 = 1;
        r0 = r39;
        r0 = new java.lang.Object[r0];
        r39 = r0;
        r40 = 0;
        r41 = java.lang.Integer.valueOf(r14);
        r39[r40] = r41;
        r37.m646w(r38, r39);
        goto L_0x012c;
    L_0x014b:
        r37 = com.amazon.device.ads.AAXCreative.containsPrimaryCreativeType(r13);
        if (r37 != 0) goto L_0x016e;
    L_0x0151:
        r28 = "No valid creative types found";
        r37 = new com.amazon.device.ads.AdError;
        r38 = com.amazon.device.ads.AdError.ErrorCode.INTERNAL_ERROR;
        r39 = "No valid creative types found";
        r37.<init>(r38, r39);
        r0 = r37;
        r9.setAdError(r0);
        r0 = r42;
        r0 = r0.logger;
        r37 = r0;
        r38 = "No valid creative types found";
        r37.m639e(r38);
        goto L_0x007c;
    L_0x016e:
        r37 = "size";
        r38 = "";
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r30 = com.amazon.device.ads.JSONUtils.getStringFromJSON(r0, r1, r2);
        if (r30 == 0) goto L_0x01a7;
    L_0x017e:
        r37 = "9999x9999";
        r0 = r30;
        r1 = r37;
        r37 = r0.equals(r1);
        if (r37 != 0) goto L_0x0196;
    L_0x018a:
        r37 = "interstitial";
        r0 = r30;
        r1 = r37;
        r37 = r0.equals(r1);
        if (r37 == 0) goto L_0x01a7;
    L_0x0196:
        r37 = com.amazon.device.ads.AAXCreative.INTERSTITIAL;
        r0 = r37;
        r37 = r13.contains(r0);
        if (r37 != 0) goto L_0x01a7;
    L_0x01a0:
        r37 = com.amazon.device.ads.AAXCreative.INTERSTITIAL;
        r0 = r37;
        r13.add(r0);
    L_0x01a7:
        r10 = 0;
        r7 = 0;
        r37 = com.amazon.device.ads.AAXCreative.INTERSTITIAL;
        r0 = r37;
        r37 = r13.contains(r0);
        if (r37 != 0) goto L_0x0209;
    L_0x01b3:
        r31 = 0;
        if (r30 == 0) goto L_0x01f1;
    L_0x01b7:
        r37 = "x";
        r0 = r30;
        r1 = r37;
        r32 = r0.split(r1);
    L_0x01c1:
        if (r32 == 0) goto L_0x01d0;
    L_0x01c3:
        r0 = r32;
        r0 = r0.length;
        r37 = r0;
        r38 = 2;
        r0 = r37;
        r1 = r38;
        if (r0 == r1) goto L_0x01f4;
    L_0x01d0:
        r31 = 1;
    L_0x01d2:
        if (r31 == 0) goto L_0x0209;
    L_0x01d4:
        r28 = "Server returned an invalid ad size";
        r37 = new com.amazon.device.ads.AdError;
        r38 = com.amazon.device.ads.AdError.ErrorCode.INTERNAL_ERROR;
        r39 = "Server returned an invalid ad size";
        r37.<init>(r38, r39);
        r0 = r37;
        r9.setAdError(r0);
        r0 = r42;
        r0 = r0.logger;
        r37 = r0;
        r38 = "Server returned an invalid ad size";
        r37.m639e(r38);
        goto L_0x007c;
    L_0x01f1:
        r32 = 0;
        goto L_0x01c1;
    L_0x01f4:
        r37 = 0;
        r37 = r32[r37];	 Catch:{ NumberFormatException -> 0x0205 }
        r10 = java.lang.Integer.parseInt(r37);	 Catch:{ NumberFormatException -> 0x0205 }
        r37 = 1;
        r37 = r32[r37];	 Catch:{ NumberFormatException -> 0x0205 }
        r7 = java.lang.Integer.parseInt(r37);	 Catch:{ NumberFormatException -> 0x0205 }
        goto L_0x01d2;
    L_0x0205:
        r18 = move-exception;
        r31 = 1;
        goto L_0x01d2;
    L_0x0209:
        r37 = "cacheTTL";
        r38 = -1;
        r0 = r33;
        r1 = r37;
        r2 = r38;
        r20 = com.amazon.device.ads.JSONUtils.getLongFromJSON(r0, r1, r2);
        r38 = -1;
        r37 = (r20 > r38 ? 1 : (r20 == r38 ? 0 : -1));
        if (r37 <= 0) goto L_0x0228;
    L_0x021d:
        r38 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r38 = r38 * r20;
        r22 = r16 + r38;
        r0 = r22;
        r5.setExpirationTimeMillis(r0);
    L_0x0228:
        r29 = new com.amazon.device.ads.AdProperties;
        r0 = r29;
        r0.<init>(r15);
        r5.setHeight(r7);
        r5.setWidth(r10);
        r5.setCreative(r12);
        r5.setCreativeTypes(r13);
        r0 = r29;
        r5.setProperties(r0);
        r37 = 1;
        r0 = r37;
        r5.setFetched(r0);
        r9.setAdData(r5);
        goto L_0x007c;
    L_0x024c:
        r25 = r35.iterator();
    L_0x0250:
        r37 = r25.hasNext();
        if (r37 == 0) goto L_0x02ba;
    L_0x0256:
        r34 = r25.next();
        r34 = (java.lang.Integer) r34;
        r0 = r42;
        r0 = r0.slots;
        r37 = r0;
        r0 = r37;
        r1 = r34;
        r37 = r0.get(r1);
        r37 = (com.amazon.device.ads.AdSlot) r37;
        r0 = r37;
        r0.setAdError(r6);
        r5 = new com.amazon.device.ads.AdData;
        r5.<init>();
        r0 = r42;
        r0 = r0.adRequest;
        r37 = r0;
        r37 = r37.getInstrumentationPixelURL();
        r0 = r37;
        r5.setInstrumentationPixelUrl(r0);
        r0 = r42;
        r0 = r0.slots;
        r37 = r0;
        r0 = r37;
        r1 = r34;
        r37 = r0.get(r1);
        r37 = (com.amazon.device.ads.AdSlot) r37;
        r0 = r37;
        r0.setAdData(r5);
        r0 = r42;
        r0 = r0.logger;
        r37 = r0;
        r38 = "%s; code: %s";
        r39 = 2;
        r0 = r39;
        r0 = new java.lang.Object[r0];
        r39 = r0;
        r40 = 0;
        r41 = r6.getMessage();
        r39[r40] = r41;
        r40 = 1;
        r39[r40] = r19;
        r37.m646w(r38, r39);
        goto L_0x0250;
    L_0x02ba:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.ads.AdLoader.parseResponse(org.json.JSONObject):void");
    }

    protected AdError getAdError(JSONObject jsonResponse) {
        int noRetryTtlSeconds = retrieveNoRetryTtlSeconds(jsonResponse);
        this.infoStore.setNoRetryTtl(noRetryTtlSeconds);
        String errorMsg = JSONUtils.getStringFromJSON(jsonResponse, "errorMessage", "No Ad Received");
        this.infoStore.setIsAppDisabled(errorMsg.equalsIgnoreCase(DISABLED_APP_SERVER_MESSAGE));
        String msg = "Server Message: " + errorMsg;
        if (noRetryTtlSeconds > 0) {
            getCompositeMetricsCollector().publishMetricInMilliseconds(MetricType.AD_NO_RETRY_TTL_RECEIVED, (long) (noRetryTtlSeconds * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
        }
        if (noRetryTtlSeconds > 0 && !this.infoStore.getIsAppDisabled()) {
            return new AdError(ErrorCode.NO_FILL, msg + ". Try again in " + noRetryTtlSeconds + " seconds");
        } else if (errorMsg.equals("no results")) {
            return new AdError(ErrorCode.NO_FILL, msg);
        } else {
            return new AdError(ErrorCode.INTERNAL_ERROR, msg);
        }
    }

    private void setErrorForAllSlots(AdError error) {
        for (AdSlot slot : this.slots.values()) {
            slot.setAdError(error);
        }
    }

    protected int retrieveNoRetryTtlSeconds(JSONObject jsonResponse) {
        return this.debugProperties.getDebugPropertyAsInteger(DebugProperties.DEBUG_NORETRYTTL, Integer.valueOf(JSONUtils.getIntegerFromJSON(jsonResponse, "noretryTTL", AD_READY_TO_LOAD))).intValue();
    }

    protected void finalizeFetchAd() {
        for (Entry<Integer, AdSlot> entry : this.slots.entrySet()) {
            AdSlot slot = (AdSlot) entry.getValue();
            if (slot.canBeUsed()) {
                slot.getMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_SPIN_UP);
                if (slot.isFetched()) {
                    slot.getMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_RENDER_START);
                    slot.initializeAd();
                } else {
                    slot.getMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_FAILURE);
                    if (slot.getAdError() != null) {
                        slot.adFailed(slot.getAdError());
                    } else {
                        slot.adFailed(new AdError(ErrorCode.INTERNAL_ERROR, "Unknown error occurred."));
                    }
                }
            } else {
                this.logger.m645w("Ad object was destroyed before ad fetching could be finalized. Ad fetching has been aborted.");
            }
        }
    }

    protected WebResponse fetchResponseFromNetwork() throws AdFetchException {
        WebRequest request = getAdRequest();
        request.setMetricsCollector(getCompositeMetricsCollector());
        request.setServiceCallLatencyMetric(MetricType.AAX_LATENCY_GET_AD);
        request.setTimeout(this.timeout);
        request.setDisconnectEnabled(false);
        getCompositeMetricsCollector().stopMetric(MetricType.AD_LOAD_LATENCY_FETCH_THREAD_START_TO_AAX_GET_AD_START);
        getCompositeMetricsCollector().incrementMetric(MetricType.TLS_ENABLED);
        try {
            WebResponse response = request.makeCall();
            getCompositeMetricsCollector().startMetric(MetricType.AD_LOAD_LATENCY_AAX_GET_AD_END_TO_FETCH_THREAD_END);
            return response;
        } catch (WebRequestException e) {
            AdError error;
            if (e.getStatus() == WebRequestStatus.NETWORK_FAILURE) {
                error = new AdError(ErrorCode.NETWORK_ERROR, "Could not contact Ad Server");
            } else if (e.getStatus() == WebRequestStatus.NETWORK_TIMEOUT) {
                error = new AdError(ErrorCode.NETWORK_TIMEOUT, "Connection to Ad Server timed out");
            } else {
                error = new AdError(ErrorCode.INTERNAL_ERROR, e.getMessage());
            }
            throw new AdFetchException(error);
        }
    }

    private MetricsCollector getCompositeMetricsCollector() {
        if (this.compositeMetricsCollector == null) {
            ArrayList<MetricsCollector> collectors = new ArrayList();
            for (Entry<Integer, AdSlot> entry : this.slots.entrySet()) {
                collectors.add(((AdSlot) entry.getValue()).getMetricsCollector());
            }
            this.compositeMetricsCollector = new CompositeMetricsCollector(collectors);
        }
        return this.compositeMetricsCollector;
    }
}
