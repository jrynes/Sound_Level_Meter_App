package com.amazon.device.ads;

class AdEvent {
    public static final String POSITION_ON_SCREEN = "positionOnScreen";
    private final AdEventType adEventType;
    private String customType;
    private final ParameterMap parameters;

    public enum AdEventType {
        EXPANDED,
        CLOSED,
        CLICKED,
        RESIZED,
        OTHER
    }

    public AdEvent(AdEventType adEventType) {
        this.parameters = new ParameterMap();
        this.adEventType = adEventType;
    }

    AdEventType getAdEventType() {
        return this.adEventType;
    }

    void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getCustomType() {
        return this.customType;
    }

    AdEvent setParameter(String key, Object value) {
        this.parameters.setParameter(key, value);
        return this;
    }

    public ParameterMap getParameters() {
        return this.parameters;
    }
}
