package com.amazon.device.ads;

import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONObject;

/* compiled from: SISRequests */
class SISUpdateDeviceInfoRequest extends SISDeviceRequest {
    private static final MetricType CALL_METRIC_TYPE;
    private static final String LOGTAG = "SISUpdateDeviceInfoRequest";
    private static final String PATH = "/update_dev_info";

    static {
        CALL_METRIC_TYPE = MetricType.SIS_LATENCY_UPDATE_DEVICE_INFO;
    }

    public SISUpdateDeviceInfoRequest() {
        setCallMetricType(CALL_METRIC_TYPE);
        setLogTag(LOGTAG);
        setPath(PATH);
    }

    public QueryStringParameters getQueryParameters() {
        String adId = DebugProperties.getInstance().getDebugPropertyAsString(DebugProperties.DEBUG_ADID, getAdvertisingIdentifierInfo().getSISDeviceIdentifier());
        QueryStringParameters baseQueryParameters = super.getQueryParameters();
        if (!StringUtils.isNullOrEmpty(adId)) {
            baseQueryParameters.putUrlEncoded("adId", adId);
        }
        return baseQueryParameters;
    }

    public void onResponseReceived(JSONObject payload) {
        String adId = JSONUtils.getStringFromJSON(payload, "adId", Stomp.EMPTY);
        if (JSONUtils.getBooleanFromJSON(payload, "idChanged", false)) {
            Metrics.getInstance().getMetricsCollector().incrementMetric(MetricType.SIS_COUNTER_IDENTIFIED_DEVICE_CHANGED);
        }
        if (adId.length() > 0) {
            MobileAdsInfoStore.getInstance().getRegistrationInfo().putAdId(adId, getAdvertisingIdentifierInfo());
        }
    }
}
