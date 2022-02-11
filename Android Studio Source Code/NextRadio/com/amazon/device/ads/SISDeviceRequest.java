package com.amazon.device.ads;

import com.amazon.device.ads.Configuration.ConfigOption;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.HashMap;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: SISRequests */
abstract class SISDeviceRequest implements SISRequest {
    private AdvertisingIdentifier advertisingIdentifier;
    private Info advertisingIdentifierInfo;
    private MetricType callMetricType;
    private String logTag;
    private final MobileAdsLogger logger;
    private String path;

    SISDeviceRequest() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(this.logTag);
    }

    public SISDeviceRequest setLogTag(String logTag) {
        this.logTag = logTag;
        this.logger.withLogTag(logTag);
        return this;
    }

    public SISDeviceRequest setCallMetricType(MetricType callMetricType) {
        this.callMetricType = callMetricType;
        return this;
    }

    public SISDeviceRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public SISDeviceRequest setAdvertisingIdentifier(AdvertisingIdentifier advertisingIdentifier) {
        this.advertisingIdentifier = advertisingIdentifier;
        this.advertisingIdentifierInfo = this.advertisingIdentifier.getAdvertisingIdentifierInfo();
        return this;
    }

    public String getLogTag() {
        return this.logTag;
    }

    public MetricType getCallMetricType() {
        return this.callMetricType;
    }

    public String getPath() {
        return this.path;
    }

    public QueryStringParameters getQueryParameters() {
        QueryStringParameters queryStringParameters = new QueryStringParameters();
        queryStringParameters.putUrlEncoded("dt", MobileAdsInfoStore.getInstance().getDeviceInfo().getDeviceType());
        queryStringParameters.putUrlEncoded(SettingsJsonConstants.APP_KEY, MobileAdsInfoStore.getInstance().getRegistrationInfo().getAppName());
        queryStringParameters.putUrlEncoded("aud", Configuration.getInstance().getString(ConfigOption.SIS_DOMAIN));
        queryStringParameters.putUrlEncoded("ua", WebUtils.getURLEncodedString(MobileAdsInfoStore.getInstance().getDeviceInfo().getUserAgentString()));
        queryStringParameters.putUrlEncoded("dinfo", WebUtils.getURLEncodedString(getDInfoProperty()));
        queryStringParameters.putUrlEncoded("pkg", WebUtils.getURLEncodedString(MobileAdsInfoStore.getInstance().getAppInfo().getPackageInfoJSONString()));
        if (this.advertisingIdentifierInfo.hasAdvertisingIdentifier()) {
            queryStringParameters.putUrlEncoded("idfa", this.advertisingIdentifierInfo.getAdvertisingIdentifier());
            queryStringParameters.putUrlEncoded("oo", convertOptOutBooleanToStringInt(this.advertisingIdentifierInfo.isLimitAdTrackingEnabled()));
        } else {
            DeviceInfo deviceInfo = MobileAdsInfoStore.getInstance().getDeviceInfo();
            queryStringParameters.putUrlEncoded("sha1_mac", deviceInfo.getMacSha1());
            queryStringParameters.putUrlEncoded("sha1_serial", deviceInfo.getSerialSha1());
            queryStringParameters.putUrlEncoded("sha1_udid", deviceInfo.getUdidSha1());
            queryStringParameters.putUrlEncodedIfTrue("badMac", Stomp.TRUE, deviceInfo.isMacBad());
            queryStringParameters.putUrlEncodedIfTrue("badSerial", Stomp.TRUE, deviceInfo.isSerialBad());
            queryStringParameters.putUrlEncodedIfTrue("badUdid", Stomp.TRUE, deviceInfo.isUdidBad());
        }
        String adIdTransition = this.advertisingIdentifier.getAndClearTransition();
        queryStringParameters.putUrlEncodedIfTrue("aidts", adIdTransition, adIdTransition != null);
        return queryStringParameters;
    }

    private static String convertOptOutBooleanToStringInt(boolean optOut) {
        return optOut ? "1" : "0";
    }

    protected Info getAdvertisingIdentifierInfo() {
        return this.advertisingIdentifierInfo;
    }

    public static String getDInfoProperty() {
        DeviceInfo deviceInfo = MobileAdsInfoStore.getInstance().getDeviceInfo();
        return String.format("{\"make\":\"%s\",\"model\":\"%s\",\"os\":\"%s\",\"osVersion\":\"%s\"}", new Object[]{deviceInfo.getMake(), deviceInfo.getModel(), deviceInfo.getOS(), deviceInfo.getOSVersion()});
    }

    public HashMap<String, String> getPostParameters() {
        return null;
    }

    public MobileAdsLogger getLogger() {
        return this.logger;
    }
}
