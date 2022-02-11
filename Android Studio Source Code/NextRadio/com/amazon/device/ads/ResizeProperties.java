package com.amazon.device.ads;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONObject;

class ResizeProperties {
    private final Boolean allowOffscreenDefault;
    private final String customClosePositionDefault;
    private final JSONObject json;

    public ResizeProperties() {
        this.json = new JSONObject();
        this.customClosePositionDefault = "top-right";
        this.allowOffscreenDefault = Boolean.TRUE;
        getClass();
        JSONUtils.put(this.json, "customClosePosition", "top-right");
        JSONUtils.put(this.json, "allowOffscreen", this.allowOffscreenDefault.booleanValue());
    }

    public int getWidth() {
        return JSONUtils.getIntegerFromJSON(this.json, SettingsJsonConstants.ICON_WIDTH_KEY, 0);
    }

    public void setWidth(int width) {
        JSONUtils.put(this.json, SettingsJsonConstants.ICON_WIDTH_KEY, width);
    }

    public int getHeight() {
        return JSONUtils.getIntegerFromJSON(this.json, SettingsJsonConstants.ICON_HEIGHT_KEY, 0);
    }

    public void setHeight(int height) {
        JSONUtils.put(this.json, SettingsJsonConstants.ICON_HEIGHT_KEY, height);
    }

    public int getOffsetX() {
        return JSONUtils.getIntegerFromJSON(this.json, "offsetX", 0);
    }

    public void setOffsetX(int offsetX) {
        JSONUtils.put(this.json, "offsetX", offsetX);
    }

    public int getOffsetY() {
        return JSONUtils.getIntegerFromJSON(this.json, "offsetY", 0);
    }

    public void setOffsetY(int offsetY) {
        JSONUtils.put(this.json, "offsetY", offsetY);
    }

    public String getCustomClosePosition() {
        getClass();
        return JSONUtils.getStringFromJSON(this.json, "customClosePosition", "top-right");
    }

    public void setCustomClosePosition(String customClosePosition) {
        if (customClosePosition != null) {
            JSONUtils.put(this.json, "customClosePosition", customClosePosition);
        }
    }

    public Boolean getAllowOffscreen() {
        return Boolean.valueOf(JSONUtils.getBooleanFromJSON(this.json, "allowOffscreen", this.allowOffscreenDefault.booleanValue()));
    }

    public void setAllowOffscreen(Boolean allowOffscreen) {
        if (allowOffscreen != null) {
            JSONUtils.put(this.json, "allowOffscreen", allowOffscreen.booleanValue());
        }
    }

    public JSONObject toJSONObject() {
        return this.json;
    }
}
