package com.facebook.ads.internal;

import com.facebook.ads.AdError;
import com.mixpanel.android.java_websocket.framing.CloseFrame;
import org.apache.activemq.ActiveMQPrefetchPolicy;

public enum AdErrorType {
    UNKNOWN_ERROR(-1, "unknown error", false),
    NETWORK_ERROR(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH, "Network Error", true),
    NO_FILL(CloseFrame.GOING_AWAY, "No Fill", true),
    LOAD_TOO_FREQUENTLY(CloseFrame.PROTOCOL_ERROR, "Ad was re-loaded too frequently", true),
    INVALID_PARAMETERS(CloseFrame.REFUSE, "Ad was requested with invalid parameters", true),
    SERVER_ERROR(AdError.SERVER_ERROR_CODE, "Server Error", true),
    INTERNAL_ERROR(AdError.INTERNAL_ERROR_CODE, "Internal Error", true),
    START_BEFORE_INIT(2004, "initAd must be called before startAd", true),
    AD_REQUEST_FAILED(1111, "Facebook Ads SDK request for ads failed", false),
    AD_REQUEST_TIMEOUT(1112, "Facebook Ads SDK request for ads timed out", false),
    PARSER_FAILURE(1201, "Failed to parse Facebook Ads SDK delivery response", false),
    UNKNOWN_RESPONSE(1202, "Unknown Facebook Ads SDK delivery response type", false),
    ERROR_MESSAGE(1203, "Facebook Ads SDK delivery response Error message", true),
    NO_AD_PLACEMENT(1302, "Facebook Ads SDK returned no ad placements", false);
    
    private final int f1550a;
    private final String f1551b;
    private final boolean f1552c;

    private AdErrorType(int i, String str, boolean z) {
        this.f1550a = i;
        this.f1551b = str;
        this.f1552c = z;
    }

    public static AdErrorType adErrorTypeFromCode(int i) {
        for (AdErrorType adErrorType : values()) {
            if (adErrorType.getErrorCode() == i) {
                return adErrorType;
            }
        }
        return UNKNOWN_ERROR;
    }

    boolean m1162a() {
        return this.f1552c;
    }

    public AdError getAdError(String str) {
        return new C0458b(this, str).m1353b();
    }

    public C0458b getAdErrorWrapper(String str) {
        return new C0458b(this, str);
    }

    public String getDefaultErrorMessage() {
        return this.f1551b;
    }

    public int getErrorCode() {
        return this.f1550a;
    }
}
