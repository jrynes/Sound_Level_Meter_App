package com.amazon.device.ads;

import com.amazon.device.ads.Configuration.ConfigOption;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xbill.DNS.Zone;

/* compiled from: SISRequests */
class SISRegisterEventRequest implements SISRequest {
    private static final MetricType CALL_METRIC_TYPE;
    private static final String LOGTAG = "SISRegisterEventRequest";
    private static final String PATH = "/register_event";
    private final Info advertisingIdentifierInfo;
    private final JSONArray appEvents;
    private final MobileAdsLogger logger;

    /* renamed from: com.amazon.device.ads.SISRegisterEventRequest.1 */
    static /* synthetic */ class SISRequests {
        static final /* synthetic */ int[] f1065x8e4bf040;

        static {
            f1065x8e4bf040 = new int[SISRequestType.values().length];
            try {
                f1065x8e4bf040[SISRequestType.GENERATE_DID.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1065x8e4bf040[SISRequestType.UPDATE_DEVICE_INFO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1065x8e4bf040[SISRequestType.REGISTER_EVENT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* compiled from: SISRequests */
    static class SISRequestFactory {
        SISRequestFactory() {
        }

        public SISDeviceRequest createDeviceRequest(SISRequestType requestType) {
            switch (SISRequests.f1065x8e4bf040[requestType.ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    return new SISGenerateDIDRequest();
                case Zone.SECONDARY /*2*/:
                    return new SISUpdateDeviceInfoRequest();
                default:
                    throw new IllegalArgumentException("SISRequestType " + requestType + " is not a SISDeviceRequest");
            }
        }

        public SISRegisterEventRequest createRegisterEventRequest(Info advertisingIdentifierInfo, JSONArray appEvents) {
            return new SISRegisterEventRequest(advertisingIdentifierInfo, appEvents);
        }
    }

    /* compiled from: SISRequests */
    enum SISRequestType {
        GENERATE_DID,
        UPDATE_DEVICE_INFO,
        REGISTER_EVENT
    }

    static {
        CALL_METRIC_TYPE = MetricType.SIS_LATENCY_REGISTER_EVENT;
    }

    public SISRegisterEventRequest(Info advertisingIdentifierInfo, JSONArray appEvents) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.advertisingIdentifierInfo = advertisingIdentifierInfo;
        this.appEvents = appEvents;
    }

    public String getLogTag() {
        return LOGTAG;
    }

    public MetricType getCallMetricType() {
        return CALL_METRIC_TYPE;
    }

    public String getPath() {
        return PATH;
    }

    public QueryStringParameters getQueryParameters() {
        QueryStringParameters queryStringParameters = new QueryStringParameters();
        queryStringParameters.putUrlEncoded("adId", this.advertisingIdentifierInfo.getSISDeviceIdentifier());
        queryStringParameters.putUrlEncoded("dt", MobileAdsInfoStore.getInstance().getDeviceInfo().getDeviceType());
        RegistrationInfo registrationInfo = MobileAdsInfoStore.getInstance().getRegistrationInfo();
        queryStringParameters.putUrlEncoded(SettingsJsonConstants.APP_KEY, registrationInfo.getAppName());
        queryStringParameters.putUrlEncoded("appId", registrationInfo.getAppKey());
        queryStringParameters.putUrlEncoded("aud", Configuration.getInstance().getString(ConfigOption.SIS_DOMAIN));
        return queryStringParameters;
    }

    public HashMap<String, String> getPostParameters() {
        HashMap<String, String> eventsMap = new HashMap();
        eventsMap.put("events", this.appEvents.toString());
        return eventsMap;
    }

    public void onResponseReceived(JSONObject payload) {
        int statusCode = JSONUtils.getIntegerFromJSON(payload, "rcode", 0);
        if (statusCode == 1) {
            this.logger.m637d("Application events registered successfully.");
            AppEventRegistrationHandler.getInstance().onAppEventsRegistered();
            return;
        }
        this.logger.m637d("Application events not registered. rcode:" + statusCode);
    }

    public MobileAdsLogger getLogger() {
        return this.logger;
    }
}
