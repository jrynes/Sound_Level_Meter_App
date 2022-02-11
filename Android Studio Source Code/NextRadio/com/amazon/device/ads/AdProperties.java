package com.amazon.device.ads;

import org.json.JSONArray;

public class AdProperties {
    public static final int CAN_EXPAND1 = 1003;
    public static final int CAN_EXPAND2 = 1004;
    public static final int CAN_PLAY_AUDIO1 = 1001;
    public static final int CAN_PLAY_AUDIO2 = 1002;
    public static final int CAN_PLAY_VIDEO = 1014;
    public static final int HTML = 1007;
    public static final int INTERSTITIAL = 1008;
    private static final String LOGTAG;
    public static final int MRAID1 = 1016;
    public static final int MRAID2 = 1017;
    public static final int REQUIRES_TRANSPARENCY = 1031;
    public static final int VIDEO_INTERSTITIAL = 1030;
    private AdType adType_;
    private boolean canExpand_;
    private boolean canPlayAudio_;
    private boolean canPlayVideo_;
    private final MobileAdsLogger logger;

    public enum AdType {
        IMAGE_BANNER("Image Banner"),
        MRAID_1("MRAID 1.0"),
        MRAID_2("MRAID 2.0"),
        INTERSTITIAL("Interstitial", "i"),
        MODELESS_INTERSTITIAL("Modeless Interstitial", "mi");
        
        private final String adTypeMetricTag;
        private final String type;

        private AdType(String type) {
            this(r2, r3, type, null);
        }

        private AdType(String type, String metricTag) {
            this.type = type;
            this.adTypeMetricTag = metricTag;
        }

        String getAdTypeMetricTag() {
            return this.adTypeMetricTag;
        }

        public String toString() {
            return this.type;
        }
    }

    static {
        LOGTAG = AdProperties.class.getSimpleName();
    }

    AdProperties(JSONArray creativeTypes) {
        this(creativeTypes, new MobileAdsLoggerFactory());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    AdProperties(org.json.JSONArray r9, com.amazon.device.ads.MobileAdsLoggerFactory r10) {
        /*
        r8 = this;
        r7 = 1;
        r6 = 0;
        r8.<init>();
        r8.canExpand_ = r6;
        r8.canPlayAudio_ = r6;
        r8.canPlayVideo_ = r6;
        r2 = LOGTAG;
        r2 = r10.createMobileAdsLogger(r2);
        r8.logger = r2;
        if (r9 == 0) goto L_0x0057;
    L_0x0015:
        r1 = 0;
    L_0x0016:
        r2 = r9.length();
        if (r1 >= r2) goto L_0x0057;
    L_0x001c:
        r2 = r9.getInt(r1);	 Catch:{ JSONException -> 0x002a }
        switch(r2) {
            case 1001: goto L_0x0026;
            case 1002: goto L_0x0026;
            case 1003: goto L_0x003b;
            case 1004: goto L_0x003b;
            case 1005: goto L_0x0023;
            case 1006: goto L_0x0023;
            case 1007: goto L_0x003f;
            case 1008: goto L_0x0044;
            case 1009: goto L_0x0023;
            case 1010: goto L_0x0023;
            case 1011: goto L_0x0023;
            case 1012: goto L_0x0023;
            case 1013: goto L_0x0023;
            case 1014: goto L_0x0049;
            case 1015: goto L_0x0023;
            case 1016: goto L_0x004d;
            case 1017: goto L_0x0052;
            default: goto L_0x0023;
        };	 Catch:{ JSONException -> 0x002a }
    L_0x0023:
        r1 = r1 + 1;
        goto L_0x0016;
    L_0x0026:
        r2 = 1;
        r8.canPlayAudio_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x002a:
        r0 = move-exception;
        r2 = r8.logger;
        r3 = "Unable to parse creative type: %s";
        r4 = new java.lang.Object[r7];
        r5 = r0.getMessage();
        r4[r6] = r5;
        r2.m646w(r3, r4);
        goto L_0x0023;
    L_0x003b:
        r2 = 1;
        r8.canExpand_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x003f:
        r2 = com.amazon.device.ads.AdProperties.AdType.IMAGE_BANNER;	 Catch:{ JSONException -> 0x002a }
        r8.adType_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x0044:
        r2 = com.amazon.device.ads.AdProperties.AdType.INTERSTITIAL;	 Catch:{ JSONException -> 0x002a }
        r8.adType_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x0049:
        r2 = 1;
        r8.canPlayVideo_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x004d:
        r2 = com.amazon.device.ads.AdProperties.AdType.MRAID_1;	 Catch:{ JSONException -> 0x002a }
        r8.adType_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x0052:
        r2 = com.amazon.device.ads.AdProperties.AdType.MRAID_2;	 Catch:{ JSONException -> 0x002a }
        r8.adType_ = r2;	 Catch:{ JSONException -> 0x002a }
        goto L_0x0023;
    L_0x0057:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.ads.AdProperties.<init>(org.json.JSONArray, com.amazon.device.ads.MobileAdsLoggerFactory):void");
    }

    void setAdType(AdType adType) {
        this.adType_ = adType;
    }

    public AdType getAdType() {
        return this.adType_;
    }

    void setCanExpand(boolean canExpand) {
        this.canExpand_ = canExpand;
    }

    public boolean canExpand() {
        return this.canExpand_;
    }

    void setCanPlayAudio(boolean canPlayAudio) {
        this.canPlayAudio_ = canPlayAudio;
    }

    public boolean canPlayAudio() {
        return this.canPlayAudio_;
    }

    void setCanPlayVideo(boolean canPlayVideo) {
        this.canPlayVideo_ = canPlayVideo;
    }

    public boolean canPlayVideo() {
        return this.canPlayVideo_;
    }
}
