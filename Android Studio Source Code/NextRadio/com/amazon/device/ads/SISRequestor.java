package com.amazon.device.ads;

import android.support.v4.app.NotificationCompat;
import com.amazon.device.ads.Configuration.ConfigOption;
import com.amazon.device.ads.WebRequest.HttpMethod;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import java.util.Map.Entry;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONObject;

class SISRequestor {
    protected static final String API_LEVEL_ENDPOINT = "/api3";
    private final SISRequestorCallback sisRequestorCallback;
    private final SISRequest[] sisRequests;
    private final WebRequestFactory webRequestFactory;

    static class SISRequestorFactory {
        SISRequestorFactory() {
        }

        public SISRequestor createSISRequestor(SISRequest... sisRequests) {
            return createSISRequestor(null, sisRequests);
        }

        public SISRequestor createSISRequestor(SISRequestorCallback sisRequestorCallback, SISRequest... sisRequests) {
            return new SISRequestor(sisRequestorCallback, sisRequests);
        }
    }

    public SISRequestor(SISRequest... sisRequests) {
        this(null, sisRequests);
    }

    public SISRequestor(SISRequestorCallback sisRequestorCallback, SISRequest... sisRequests) {
        this(new WebRequestFactory(), sisRequestorCallback, sisRequests);
    }

    SISRequestor(WebRequestFactory webRequestFactory, SISRequestorCallback sisRequestorCallback, SISRequest... sisRequests) {
        this.webRequestFactory = webRequestFactory;
        this.sisRequestorCallback = sisRequestorCallback;
        this.sisRequests = sisRequests;
    }

    public void startCallSIS() {
        callSIS();
        SISRequestorCallback sisRequestorCallback = getSisRequestorCallback();
        if (sisRequestorCallback != null) {
            sisRequestorCallback.onSISCallComplete();
        }
    }

    protected void callSIS() {
        for (SISRequest sisRequest : this.sisRequests) {
            callSIS(sisRequest);
        }
    }

    protected void callSIS(SISRequest sisRequest) {
        try {
            JSONObject jsonPayload = getWebRequest(sisRequest).makeCall().getResponseReader().readAsJSON();
            if (jsonPayload != null) {
                int rcode = JSONUtils.getIntegerFromJSON(jsonPayload, "rcode", 0);
                String msg = JSONUtils.getStringFromJSON(jsonPayload, NotificationCompat.CATEGORY_MESSAGE, Stomp.EMPTY);
                if (rcode == 1) {
                    sisRequest.getLogger().m642i("Result - code: %d, msg: %s", Integer.valueOf(rcode), msg);
                    sisRequest.onResponseReceived(jsonPayload);
                    return;
                }
                sisRequest.getLogger().m646w("Result - code: %d, msg: %s", Integer.valueOf(rcode), msg);
            }
        } catch (WebRequestException e) {
        }
    }

    protected WebRequest getWebRequest(SISRequest sisRequest) {
        WebRequest request = this.webRequestFactory.createWebRequest();
        request.setExternalLogTag(sisRequest.getLogTag());
        request.setHttpMethod(HttpMethod.POST);
        request.setHost(getHostname());
        request.setPath(getEndpoint(sisRequest));
        request.enableLog(true);
        if (sisRequest.getPostParameters() != null) {
            for (Entry<String, String> postParameter : sisRequest.getPostParameters().entrySet()) {
                request.putPostParameter((String) postParameter.getKey(), (String) postParameter.getValue());
            }
        }
        QueryStringParameters queryStringParameters = sisRequest.getQueryParameters();
        queryStringParameters.putUrlEncoded("appId", MobileAdsInfoStore.getInstance().getRegistrationInfo().getAppKey());
        queryStringParameters.putUrlEncoded("sdkVer", Version.getSDKVersion());
        request.setQueryStringParameters(queryStringParameters);
        request.setMetricsCollector(Metrics.getInstance().getMetricsCollector());
        request.setServiceCallLatencyMetric(sisRequest.getCallMetricType());
        return request;
    }

    protected static String getHostname() {
        String hostname = Configuration.getInstance().getString(ConfigOption.SIS_URL);
        if (hostname == null) {
            return hostname;
        }
        int endpointIndex = hostname.indexOf(ReadOnlyContext.SEPARATOR);
        if (endpointIndex > -1) {
            return hostname.substring(0, endpointIndex);
        }
        return hostname;
    }

    protected static String getEndpoint(SISRequest sisRequest) {
        String endpoint = Configuration.getInstance().getString(ConfigOption.SIS_URL);
        if (endpoint != null) {
            int endpointIndex = endpoint.indexOf(ReadOnlyContext.SEPARATOR);
            if (endpointIndex > -1) {
                endpoint = endpoint.substring(endpointIndex);
            } else {
                endpoint = Stomp.EMPTY;
            }
        }
        return endpoint + API_LEVEL_ENDPOINT + sisRequest.getPath();
    }

    protected SISRequestorCallback getSisRequestorCallback() {
        return this.sisRequestorCallback;
    }
}
