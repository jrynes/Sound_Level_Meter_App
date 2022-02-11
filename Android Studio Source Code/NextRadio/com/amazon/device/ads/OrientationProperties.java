package com.amazon.device.ads;

import java.util.Locale;

class OrientationProperties {
    private static final String FORMAT = "{\"allowOrientationChange\":%s,\"forceOrientation\":\"%s\"}";
    private Boolean allowOrientationChange;
    private ForceOrientation forceOrientation;

    OrientationProperties() {
        this.allowOrientationChange = Boolean.valueOf(true);
        this.forceOrientation = ForceOrientation.NONE;
    }

    public Boolean isAllowOrientationChange() {
        return this.allowOrientationChange;
    }

    public void setAllowOrientationChange(Boolean allowOrientationChange) {
        this.allowOrientationChange = allowOrientationChange;
    }

    public ForceOrientation getForceOrientation() {
        return this.forceOrientation;
    }

    public void setForceOrientation(ForceOrientation forceOrientation) {
        this.forceOrientation = forceOrientation;
    }

    public String toString() {
        return String.format(Locale.US, FORMAT, new Object[]{this.allowOrientationChange.toString(), this.forceOrientation.toString()});
    }
}
