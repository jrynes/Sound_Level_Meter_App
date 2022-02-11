package com.amazon.device.ads;

import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestStatus;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

class Metrics {
    private static final String LOGTAG;
    private static final boolean TYPED_METRIC = true;
    private static Metrics instance;
    private final MobileAdsLogger logger;
    private MetricsCollector metricsCollector;

    interface MetricsSubmitter {
        String getInstrumentationPixelUrl();

        MetricsCollector getMetricsCollector();

        void resetMetricsCollector();
    }

    /* renamed from: com.amazon.device.ads.Metrics.1 */
    class C03271 implements Runnable {
        final /* synthetic */ WebRequest val$webRequest;

        C03271(WebRequest webRequest) {
            this.val$webRequest = webRequest;
        }

        public void run() {
            this.val$webRequest.enableLog(Metrics.TYPED_METRIC);
            try {
                this.val$webRequest.makeCall();
            } catch (WebRequestException e) {
                switch (C03282.$SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus[e.getStatus().ordinal()]) {
                    case Zone.PRIMARY /*1*/:
                        Metrics.this.getLogger().m640e("Unable to submit metrics for ad due to an Invalid Client Protocol, msg: %s", e.getMessage());
                        return;
                    case Zone.SECONDARY /*2*/:
                        Metrics.this.getLogger().m640e("Unable to submit metrics for ad due to Network Failure, msg: %s", e.getMessage());
                        return;
                    case Protocol.GGP /*3*/:
                        Metrics.this.getLogger().m640e("Unable to submit metrics for ad due to a Malformed Pixel URL, msg: %s", e.getMessage());
                        break;
                    case Type.MF /*4*/:
                        break;
                    default:
                        return;
                }
                Metrics.this.getLogger().m640e("Unable to submit metrics for ad because of unsupported character encoding, msg: %s", e.getMessage());
            }
        }
    }

    /* renamed from: com.amazon.device.ads.Metrics.2 */
    static /* synthetic */ class C03282 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus;

        static {
            $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus = new int[WebRequestStatus.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus[WebRequestStatus.INVALID_CLIENT_PROTOCOL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus[WebRequestStatus.NETWORK_FAILURE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus[WebRequestStatus.MALFORMED_URL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$WebRequestStatus[WebRequestStatus.UNSUPPORTED_ENCODING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    enum MetricType {
        AD_LATENCY_TOTAL("tl", Metrics.TYPED_METRIC),
        AD_LATENCY_TOTAL_SUCCESS("tsl", Metrics.TYPED_METRIC),
        AD_LATENCY_TOTAL_FAILURE("tfl", Metrics.TYPED_METRIC),
        AD_LOAD_LATENCY_LOADAD_TO_FETCH_THREAD_REQUEST_START("llfsl", Metrics.TYPED_METRIC),
        AD_LOAD_LATENCY_FETCH_THREAD_SPIN_UP("lfsul"),
        AD_LOAD_LATENCY_FETCH_THREAD_START_TO_AAX_GET_AD_START("lfsasl"),
        AD_LOAD_LATENCY_AAX_GET_AD_END_TO_FETCH_THREAD_END("laefel"),
        AD_LOAD_LATENCY_FINALIZE_FETCH_SPIN_UP("lffsul"),
        AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_RENDER_START("lffsrsl", Metrics.TYPED_METRIC),
        AD_LOAD_LATENCY_FINALIZE_FETCH_START_TO_FAILURE("lffsfl", Metrics.TYPED_METRIC),
        AD_LOAD_LATENCY_CREATE_AAX_GET_AD_URL("lcaul"),
        ASSETS_CREATED_LATENCY("lacl"),
        ASSETS_ENSURED_LATENCY("lael"),
        ASSETS_FAILED("af"),
        AD_LOADED_TO_AD_SHOW_TIME("alast"),
        AD_SHOW_LATENCY("lsa"),
        AD_SHOW_DURATION("sd", Metrics.TYPED_METRIC),
        AD_LAYOUT_INITIALIZATION("ali"),
        AAX_LATENCY_GET_AD("al"),
        AD_LOAD_FAILED("lf"),
        AD_LOAD_FAILED_ON_AAX_CALL_TIMEOUT("lfat"),
        AD_LOAD_FAILED_ON_PRERENDERING_TIMEOUT("lfpt", Metrics.TYPED_METRIC),
        AD_COUNTER_IDENTIFIED_DEVICE(Name.MARK),
        AD_COUNTER_RENDERING_FATAL("rf", Metrics.TYPED_METRIC),
        AD_LATENCY_RENDER("rl", Metrics.TYPED_METRIC),
        AD_LATENCY_RENDER_FAILED("rlf", Metrics.TYPED_METRIC),
        AD_COUNTER_FAILED_DUE_TO_NO_RETRY("nrtf"),
        AD_NO_RETRY_TTL_RECEIVED("nrtr"),
        AD_COUNTER_AUTO_AD_SIZE("aas"),
        AD_COUNTER_PARENT_VIEW_MISSING("pvm"),
        ADLAYOUT_HEIGHT_ZERO("ahz"),
        VIEWPORT_SCALE("vs"),
        AD_COUNTER_RESHOWN("rs", Metrics.TYPED_METRIC),
        AD_FAILED_UNKNOWN_WEBVIEW_ISSUE("fuwi"),
        AD_FAILED_NULL_LAYOUT_PARAMS("fnlp"),
        AD_FAILED_LAYOUT_NOT_RUN("flnr"),
        AD_FAILED_INVALID_AUTO_AD_SIZE("faas"),
        SIS_COUNTER_IDENTIFIED_DEVICE_CHANGED("sid"),
        SIS_LATENCY_REGISTER("srl"),
        SIS_LATENCY_UPDATE_DEVICE_INFO("sul"),
        SIS_LATENCY_REGISTER_EVENT("srel"),
        CONFIG_DOWNLOAD_ERROR("cde"),
        CONFIG_DOWNLOAD_LATENCY("cdt"),
        CONFIG_PARSE_ERROR("cpe"),
        AAX_CONFIG_DOWNLOAD_LATENCY("acl"),
        AAX_CONFIG_DOWNLOAD_FAILED("acf"),
        CUSTOM_RENDER_HANDLED("crh"),
        TLS_ENABLED("tls"),
        WIFI_PRESENT("wifi"),
        CARRIER_NAME("car"),
        CONNECTION_TYPE("ct"),
        AD_IS_INTERSTITIAL("i"),
        INTERSTITIAL_AD_ACTIVITY_FAILED("iaaf"),
        RENDER_REQUIREMENT_CHECK_FAILURE("rrcfc", Metrics.TYPED_METRIC),
        EXPIRED_AD_CALL("eac", Metrics.TYPED_METRIC),
        AD_ASPECT_RATIO_LESS_THAN_SCREEN_ASPECT_RATIO("rarfc", Metrics.TYPED_METRIC),
        SET_ORIENTATION_FAILURE("rsofc", Metrics.TYPED_METRIC),
        AD_EXPIRED_BEFORE_SHOWING("aebs", Metrics.TYPED_METRIC);
        
        private final String aaxName;
        private final boolean isAdTypeSpecific;

        private MetricType(String aaxName) {
            this(r2, r3, aaxName, false);
        }

        private MetricType(String aaxName, boolean isAdTypeSpecific) {
            this.aaxName = aaxName;
            this.isAdTypeSpecific = isAdTypeSpecific;
        }

        public String getAaxName() {
            return this.aaxName;
        }

        public boolean isAdTypeSpecific() {
            return this.isAdTypeSpecific;
        }
    }

    static {
        LOGTAG = Metrics.class.getSimpleName();
        instance = new Metrics();
    }

    Metrics() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.metricsCollector = new MetricsCollector();
    }

    public static Metrics getInstance() {
        return instance;
    }

    public MetricsCollector getMetricsCollector() {
        return this.metricsCollector;
    }

    private MobileAdsLogger getLogger() {
        return this.logger;
    }

    public void submitAndResetMetrics(MetricsSubmitter submitter) {
        getLogger().m637d("METRIC Submit and Reset");
        AdMetrics adMetrics = new AdMetrics(submitter);
        if (adMetrics.canSubmit()) {
            MetricsCollector metricsCollector = this.metricsCollector;
            this.metricsCollector = new MetricsCollector();
            adMetrics.addGlobalMetrics(metricsCollector);
            sendMetrics(adMetrics.getAaxWebRequestAndResetAdMetrics());
            return;
        }
        submitter.resetMetricsCollector();
    }

    private void sendMetrics(WebRequest webRequest) {
        ThreadUtils.scheduleRunnable(new C03271(webRequest));
    }
}
