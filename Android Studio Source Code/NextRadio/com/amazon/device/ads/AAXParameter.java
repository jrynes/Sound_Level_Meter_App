package com.amazon.device.ads;

import android.content.Context;
import android.location.Location;
import com.amazon.device.ads.Configuration.ConfigOption;
import com.amazon.device.ads.Parsers.IntegerParser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class AAXParameter<T> {
    static final AAXParameter<String> ADVERTISING_IDENTIFIER;
    static final AAXParameter<String> APP_KEY;
    static final AAXParameter<String> CHANNEL;
    static final AAXParameter<JSONObject> DEVICE_INFO;
    static final AAXParameter<Long> FLOOR_PRICE;
    static final AAXParameter<String> GEOLOCATION;
    private static final String LOGTAG;
    static final AAXParameter<String> MAX_SIZE;
    static final AAXParameter<String> MD5_UDID;
    static final AAXParameter<Boolean> OPT_OUT;
    static final AAXParameter<JSONObject> PACKAGE_INFO;
    static final AAXParameter<String> PAGE_TYPE;
    static final AAXParameter<JSONArray> PUBLISHER_ASINS;
    static final PublisherKeywordsParameter PUBLISHER_KEYWORDS;
    static final AAXParameter<String> SDK_VERSION;
    static final AAXParameter<String> SHA1_UDID;
    static final AAXParameter<String> SIS_DEVICE_IDENTIFIER;
    static final AAXParameter<String> SIZE;
    static final AAXParameter<String> SLOT;
    static final AAXParameter<JSONArray> SLOTS;
    static final AAXParameter<Integer> SLOT_ID;
    static final AAXParameter<String> SLOT_POSITION;
    static final AAXParameter<JSONArray> SUPPORTED_MEDIA_TYPES;
    static final AAXParameter<Boolean> TEST;
    static final AAXParameter<String> USER_AGENT;
    static final AAXParameter<JSONObject> VIDEO_OPTIONS;
    private final String debugName;
    private final String name;

    static class StringParameter extends AAXParameter<String> {
        StringParameter(String name, String debugProperty) {
            super(name, debugProperty);
        }

        protected String parseFromString(String value) {
            return value;
        }

        protected String getFromDebugProperties() {
            return DebugProperties.getInstance().getDebugPropertyAsString(getDebugName(), null);
        }
    }

    static class AdvertisingIdentifierParameter extends StringParameter {
        AdvertisingIdentifierParameter() {
            super("idfa", DebugProperties.DEBUG_IDFA);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            if (parameterData.adRequest.getAdvertisingIdentifierInfo().hasAdvertisingIdentifier()) {
                return parameterData.adRequest.getAdvertisingIdentifierInfo().getAdvertisingIdentifier();
            }
            return null;
        }
    }

    static class AppKeyParameter extends StringParameter {
        AppKeyParameter() {
            super("appId", DebugProperties.DEBUG_APPID);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return MobileAdsInfoStore.getInstance().getRegistrationInfo().getAppKey();
        }
    }

    static class BooleanParameter extends AAXParameter<Boolean> {
        BooleanParameter(String name, String debugProperty) {
            super(name, debugProperty);
        }

        protected Boolean parseFromString(String value) {
            return Boolean.valueOf(Boolean.parseBoolean(value));
        }

        protected Boolean getFromDebugProperties() {
            return DebugProperties.getInstance().getDebugPropertyAsBoolean(getDebugName(), null);
        }
    }

    static class JSONObjectParameter extends AAXParameter<JSONObject> {
        private final MobileAdsLogger logger;

        JSONObjectParameter(String name, String debugProperty) {
            super(name, debugProperty);
            this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(AAXParameter.LOGTAG);
        }

        protected JSONObject parseFromString(String value) {
            try {
                return new JSONObject(value);
            } catch (JSONException e) {
                this.logger.m640e("Unable to parse the following value into a JSONObject: %s", getName());
                return null;
            }
        }

        protected JSONObject getFromDebugProperties() {
            return parseFromString(DebugProperties.getInstance().getDebugPropertyAsString(getDebugName(), null));
        }
    }

    static class DeviceInfoParameter extends JSONObjectParameter {
        DeviceInfoParameter() {
            super("dinfo", DebugProperties.DEBUG_DINFO);
        }

        protected JSONObject getDerivedValue(ParameterData parameterData) {
            return MobileAdsInfoStore.getInstance().getDeviceInfo().toJsonObject(parameterData.adRequest.getOrientation());
        }
    }

    static class LongParameter extends AAXParameter<Long> {
        LongParameter(String name, String debugProperty) {
            super(name, debugProperty);
        }

        protected Long parseFromString(String value) {
            return Long.valueOf(Long.parseLong(value));
        }

        protected Long getFromDebugProperties() {
            return DebugProperties.getInstance().getDebugPropertyAsLong(getDebugName(), null);
        }
    }

    static class FloorPriceParameter extends LongParameter {
        FloorPriceParameter() {
            super("ec", DebugProperties.DEBUG_ECPM);
        }

        protected Long getDerivedValue(ParameterData parameterData) {
            if (parameterData.loiSlot.getAdTargetingOptions().hasFloorPrice()) {
                return Long.valueOf(parameterData.loiSlot.getAdTargetingOptions().getFloorPrice());
            }
            return null;
        }
    }

    static class GeoLocationParameter extends StringParameter {
        private final Configuration configuration;
        private final Context context;

        GeoLocationParameter() {
            this(Configuration.getInstance(), MobileAdsInfoStore.getInstance().getApplicationContext());
        }

        GeoLocationParameter(Configuration configuration, Context context) {
            super("geoloc", DebugProperties.DEBUG_GEOLOC);
            this.configuration = configuration;
            this.context = context;
        }

        protected String getDerivedValue(ParameterData parameterData) {
            if (!this.configuration.getBoolean(ConfigOption.SEND_GEO) || !parameterData.getAdRequest().getAdTargetingOptions().isGeoLocationEnabled()) {
                return null;
            }
            Location location = new AdLocation(this.context).getLocation();
            if (location == null) {
                return null;
            }
            return location.getLatitude() + Stomp.COMMA + location.getLongitude();
        }
    }

    static class IntegerParameter extends AAXParameter<Integer> {
        IntegerParameter(String name, String debugProperty) {
            super(name, debugProperty);
        }

        protected Integer parseFromString(String value) {
            return Integer.valueOf(Integer.parseInt(value));
        }

        protected Integer getFromDebugProperties() {
            return DebugProperties.getInstance().getDebugPropertyAsInteger(getDebugName(), null);
        }
    }

    static class JSONArrayParameter extends AAXParameter<JSONArray> {
        private final MobileAdsLogger logger;

        JSONArrayParameter(String name, String debugProperty) {
            super(name, debugProperty);
            this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(AAXParameter.LOGTAG);
        }

        protected JSONArray parseFromString(String value) {
            try {
                return new JSONArray(value);
            } catch (JSONException e) {
                this.logger.m640e("Unable to parse the following value into a JSONArray: %s", getName());
                return null;
            }
        }

        protected JSONArray getFromDebugProperties() {
            return parseFromString(DebugProperties.getInstance().getDebugPropertyAsString(getDebugName(), null));
        }
    }

    static class MD5UDIDParameter extends StringParameter {
        MD5UDIDParameter() {
            super("md5_udid", DebugProperties.DEBUG_MD5UDID);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            if (parameterData.adRequest.getAdvertisingIdentifierInfo().hasAdvertisingIdentifier()) {
                return null;
            }
            return MobileAdsInfoStore.getInstance().getDeviceInfo().getUdidMd5();
        }
    }

    static class MaxSizeParameter extends StringParameter {
        MaxSizeParameter() {
            super("mxsz", DebugProperties.DEBUG_MXSZ);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return parameterData.loiSlot.getAdSlot().getMaxSize();
        }
    }

    static class OptOutParameter extends BooleanParameter {
        OptOutParameter() {
            super("oo", DebugProperties.DEBUG_OPT_OUT);
        }

        protected Boolean getDerivedValue(ParameterData parameterData) {
            if (parameterData.adRequest.getAdvertisingIdentifierInfo().hasAdvertisingIdentifier()) {
                return Boolean.valueOf(parameterData.adRequest.getAdvertisingIdentifierInfo().isLimitAdTrackingEnabled());
            }
            return null;
        }
    }

    static class PackageInfoParameter extends JSONObjectParameter {
        PackageInfoParameter() {
            super("pkg", DebugProperties.DEBUG_PKG);
        }

        protected JSONObject getDerivedValue(ParameterData parameterData) {
            return MobileAdsInfoStore.getInstance().getAppInfo().getPackageInfoJSON();
        }
    }

    static class ParameterData {
        private AdRequest adRequest;
        private AdTargetingOptions adTargetingOptions;
        private Map<String, String> advancedOptions;
        private LOISlot loiSlot;
        private Map<String, String> temporaryOptions;

        public ParameterData() {
            this.temporaryOptions = new HashMap();
        }

        ParameterData setAdRequest(AdRequest adRequest) {
            this.adRequest = adRequest;
            return this;
        }

        ParameterData setAdvancedOptions(Map<String, String> advancedOptions) {
            this.advancedOptions = advancedOptions;
            return this;
        }

        ParameterData setLOISlot(LOISlot loiSlot) {
            this.loiSlot = loiSlot;
            return this;
        }

        AdRequest getAdRequest() {
            return this.adRequest;
        }

        ParameterData setAdTargetingOptions(AdTargetingOptions adTargetingOptions) {
            this.adTargetingOptions = adTargetingOptions;
            return this;
        }
    }

    static class PublisherKeywordsParameter extends JSONArrayParameter {
        PublisherKeywordsParameter() {
            super("pk", DebugProperties.DEBUG_PK);
        }

        protected JSONArray applyPostParameterProcessing(JSONArray value, ParameterData parameterData) {
            if (value == null) {
                value = new JSONArray();
            }
            Iterator i$ = parameterData.adTargetingOptions.getInternalPublisherKeywords().iterator();
            while (i$.hasNext()) {
                value.put((String) i$.next());
            }
            return value;
        }
    }

    static class SDKVersionParameter extends StringParameter {
        SDKVersionParameter() {
            super("adsdk", DebugProperties.DEBUG_VER);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return Version.getSDKVersion();
        }
    }

    static class SHA1UDIDParameter extends StringParameter {
        SHA1UDIDParameter() {
            super("sha1_udid", DebugProperties.DEBUG_SHA1UDID);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            if (parameterData.adRequest.getAdvertisingIdentifierInfo().hasAdvertisingIdentifier()) {
                return null;
            }
            return MobileAdsInfoStore.getInstance().getDeviceInfo().getUdidSha1();
        }
    }

    static class SISDeviceIdentifierParameter extends StringParameter {
        SISDeviceIdentifierParameter() {
            super("ad-id", DebugProperties.DEBUG_ADID);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return parameterData.adRequest.getAdvertisingIdentifierInfo().getSISDeviceIdentifier();
        }
    }

    static class SizeParameter extends StringParameter {
        SizeParameter() {
            super("sz", DebugProperties.DEBUG_SIZE);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return parameterData.loiSlot.getAdSlot().getRequestedAdSize().toString();
        }
    }

    static class SlotIdParameter extends IntegerParameter {
        SlotIdParameter() {
            super("slotId", DebugProperties.DEBUG_SLOT_ID);
        }

        protected Integer getDerivedValue(ParameterData parameterData) {
            return Integer.valueOf(parameterData.loiSlot.getAdSlot().getSlotNumber());
        }
    }

    static class SlotParameter extends StringParameter {
        SlotParameter() {
            super("slot", DebugProperties.DEBUG_SLOT);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return parameterData.adRequest.getOrientation();
        }
    }

    static class SupportedMediaTypesParameter extends JSONArrayParameter {
        public SupportedMediaTypesParameter() {
            super("supportedMediaTypes", DebugProperties.DEBUG_SUPPORTED_MEDIA_TYPES);
        }

        protected JSONArray getDerivedValue(ParameterData parameterData) {
            JSONArray value = new JSONArray();
            addDisplay(parameterData, value);
            addVideo(parameterData, value);
            return value;
        }

        private void addDisplay(ParameterData parameterData, JSONArray json) {
            boolean displayAdsEnabled = parameterData.loiSlot.getAdTargetingOptions().isDisplayAdsEnabled();
            if (parameterData.advancedOptions.containsKey("enableDisplayAds")) {
                displayAdsEnabled = Boolean.parseBoolean((String) parameterData.advancedOptions.remove("enableDisplayAds"));
            }
            if (displayAdsEnabled) {
                json.put("DISPLAY");
            }
        }

        private void addVideo(ParameterData parameterData, JSONArray json) {
            if (new VideoAdsEnabledChecker(parameterData).isVideoAdsEnabled()) {
                json.put("VIDEO");
            }
        }
    }

    static class TestParameter extends BooleanParameter {
        TestParameter() {
            super("isTest", DebugProperties.DEBUG_TEST);
        }

        protected Boolean getDerivedValue(ParameterData parameterData) {
            return Settings.getInstance().getBoolean("testingEnabled", null);
        }
    }

    static class UserAgentParameter extends StringParameter {
        UserAgentParameter() {
            super("ua", DebugProperties.DEBUG_UA);
        }

        protected String getDerivedValue(ParameterData parameterData) {
            return MobileAdsInfoStore.getInstance().getDeviceInfo().getUserAgentString();
        }
    }

    private static class VideoAdsEnabledChecker {
        private final ParameterData parameterData;

        public VideoAdsEnabledChecker(ParameterData parameterData) {
            this.parameterData = parameterData;
        }

        public boolean isVideoAdsEnabled() {
            if (!this.parameterData.loiSlot.getAdTargetingOptions().isVideoEnabledSettable()) {
                return false;
            }
            if (this.parameterData.advancedOptions.containsKey("enableVideoAds")) {
                String advancedOption = (String) this.parameterData.advancedOptions.remove("enableVideoAds");
                this.parameterData.temporaryOptions.put("enableVideoAds", advancedOption);
                return Boolean.parseBoolean(advancedOption);
            } else if (this.parameterData.temporaryOptions.containsKey("enableVideoAds")) {
                return Boolean.parseBoolean((String) this.parameterData.temporaryOptions.get("enableVideoAds"));
            } else {
                return this.parameterData.loiSlot.getAdTargetingOptions().isVideoAdsEnabled();
            }
        }
    }

    static class VideoOptionsParameter extends JSONObjectParameter {
        private static final int MAXIMUM_DURATION_DEFAULT = 30000;
        private static final int MINIMUM_DURATION_DEFAULT = 0;

        public VideoOptionsParameter() {
            super("video", DebugProperties.DEBUG_VIDEO_OPTIONS);
        }

        protected JSONObject getDerivedValue(ParameterData parameterData) {
            JSONObject value = null;
            if (new VideoAdsEnabledChecker(parameterData).isVideoAdsEnabled()) {
                value = new JSONObject();
                int minVideoAdDuration = 0;
                if (parameterData.advancedOptions.containsKey("minVideoAdDuration")) {
                    minVideoAdDuration = new IntegerParser().setDefaultValue(0).setParseErrorLogTag(AAXParameter.LOGTAG).setParseErrorLogMessage("The minVideoAdDuration advanced option could not be parsed properly.").parse((String) parameterData.advancedOptions.remove("minVideoAdDuration"));
                }
                JSONUtils.put(value, "minAdDuration", minVideoAdDuration);
                int maxVideoAdDuration = MAXIMUM_DURATION_DEFAULT;
                if (parameterData.advancedOptions.containsKey("maxVideoAdDuration")) {
                    maxVideoAdDuration = new IntegerParser().setDefaultValue(MAXIMUM_DURATION_DEFAULT).setParseErrorLogTag(AAXParameter.LOGTAG).setParseErrorLogMessage("The maxVideoAdDuration advanced option could not be parsed properly.").parse((String) parameterData.advancedOptions.remove("maxVideoAdDuration"));
                }
                JSONUtils.put(value, "maxAdDuration", maxVideoAdDuration);
            }
            return value;
        }
    }

    protected abstract T getFromDebugProperties();

    protected abstract T parseFromString(String str);

    static {
        LOGTAG = AAXParameter.class.getSimpleName();
        APP_KEY = new AppKeyParameter();
        CHANNEL = new StringParameter("c", DebugProperties.DEBUG_CHANNEL);
        PUBLISHER_KEYWORDS = new PublisherKeywordsParameter();
        PUBLISHER_ASINS = new JSONArrayParameter("pa", DebugProperties.DEBUG_PA);
        USER_AGENT = new UserAgentParameter();
        SDK_VERSION = new SDKVersionParameter();
        GEOLOCATION = new GeoLocationParameter();
        DEVICE_INFO = new DeviceInfoParameter();
        PACKAGE_INFO = new PackageInfoParameter();
        TEST = new TestParameter();
        SIS_DEVICE_IDENTIFIER = new SISDeviceIdentifierParameter();
        SHA1_UDID = new SHA1UDIDParameter();
        MD5_UDID = new MD5UDIDParameter();
        SLOTS = new JSONArrayParameter("slots", DebugProperties.DEBUG_SLOTS);
        ADVERTISING_IDENTIFIER = new AdvertisingIdentifierParameter();
        OPT_OUT = new OptOutParameter();
        SIZE = new SizeParameter();
        PAGE_TYPE = new StringParameter("pt", DebugProperties.DEBUG_PT);
        SLOT = new SlotParameter();
        SLOT_POSITION = new StringParameter("sp", DebugProperties.DEBUG_SP);
        MAX_SIZE = new MaxSizeParameter();
        SLOT_ID = new SlotIdParameter();
        FLOOR_PRICE = new FloorPriceParameter();
        SUPPORTED_MEDIA_TYPES = new SupportedMediaTypesParameter();
        VIDEO_OPTIONS = new VideoOptionsParameter();
    }

    AAXParameter(String name, String debugName) {
        this.name = name;
        this.debugName = debugName;
    }

    String getName() {
        return this.name;
    }

    protected String getDebugName() {
        return this.debugName;
    }

    protected boolean hasDebugPropertiesValue() {
        return DebugProperties.getInstance().containsDebugProperty(this.debugName);
    }

    T getValue(ParameterData parameterData) {
        T value;
        if (hasDebugPropertiesValue()) {
            value = getFromDebugProperties();
        } else if (parameterData.advancedOptions.containsKey(this.name)) {
            value = parseFromString((String) parameterData.advancedOptions.remove(this.name));
        } else {
            value = getDerivedValue(parameterData);
        }
        value = applyPostParameterProcessing(value, parameterData);
        if ((value instanceof String) && StringUtils.isNullOrWhiteSpace((String) value)) {
            return null;
        }
        return value;
    }

    protected T getDerivedValue(ParameterData parameterData) {
        return null;
    }

    protected T applyPostParameterProcessing(T value, ParameterData parameterData) {
        return value;
    }
}
