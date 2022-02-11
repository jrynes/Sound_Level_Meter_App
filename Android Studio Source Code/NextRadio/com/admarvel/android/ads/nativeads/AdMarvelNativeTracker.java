package com.admarvel.android.ads.nativeads;

public class AdMarvelNativeTracker {
    private String type;
    private String[] url;

    public String getType() {
        return this.type;
    }

    public String[] getUrl() {
        return this.url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }
}
