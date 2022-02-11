package com.amazon.device.ads;

import java.io.InputStream;
import org.json.JSONObject;

class ResponseReader {
    private static final String LOGTAG;
    private boolean enableLog;
    private final MobileAdsLogger logger;
    private final InputStream stream;

    static {
        LOGTAG = ResponseReader.class.getSimpleName();
    }

    ResponseReader(InputStream stream) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.enableLog = false;
        this.stream = stream;
    }

    public InputStream getInputStream() {
        return this.stream;
    }

    public void enableLog(boolean enabled) {
        this.enableLog = enabled;
    }

    public void setExternalLogTag(String externalLogTag) {
        if (externalLogTag == null) {
            this.logger.withLogTag(LOGTAG);
        } else {
            this.logger.withLogTag(LOGTAG + " " + externalLogTag);
        }
    }

    public String readAsString() {
        String response = StringUtils.readStringFromInputStream(this.stream);
        if (this.enableLog) {
            this.logger.m638d("Response Body: %s", response);
        }
        return response;
    }

    public JSONObject readAsJSON() {
        return JSONUtils.getJSONObjectFromString(readAsString());
    }
}
