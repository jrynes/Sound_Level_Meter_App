package com.amazon.device.ads;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONObject;

class ExpandProperties {
    private int height;
    private Boolean isModal;
    private Boolean useCustomClose;
    private int width;

    ExpandProperties() {
        this.useCustomClose = Boolean.FALSE;
        this.isModal = Boolean.TRUE;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Boolean getUseCustomClose() {
        return this.useCustomClose;
    }

    public void setUseCustomClose(Boolean useCustomClose) {
        this.useCustomClose = useCustomClose;
    }

    public Boolean getIsModal() {
        return this.isModal;
    }

    public void setIsModal(Boolean isModal) {
        this.isModal = isModal;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        JSONUtils.put(json, SettingsJsonConstants.ICON_WIDTH_KEY, this.width);
        JSONUtils.put(json, SettingsJsonConstants.ICON_HEIGHT_KEY, this.height);
        JSONUtils.put(json, "useCustomClose", this.useCustomClose.booleanValue());
        JSONUtils.put(json, "isModal", this.isModal.booleanValue());
        return json;
    }
}
