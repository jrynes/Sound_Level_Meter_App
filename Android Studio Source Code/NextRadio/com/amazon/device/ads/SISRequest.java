package com.amazon.device.ads;

import java.util.HashMap;
import org.json.JSONObject;

/* compiled from: SISRequestor */
interface SISRequest {
    MetricType getCallMetricType();

    String getLogTag();

    MobileAdsLogger getLogger();

    String getPath();

    HashMap<String, String> getPostParameters();

    QueryStringParameters getQueryParameters();

    void onResponseReceived(JSONObject jSONObject);
}
