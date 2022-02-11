package com.admarvel.android.ads.nativeads;

public class AdMarvelNativeCta {
    private String action;
    private String clickUrl;
    private AdMarvelNativeImage image;
    private String title;

    public String getAction() {
        return this.action;
    }

    public String getClickUrl() {
        return this.clickUrl;
    }

    public AdMarvelNativeImage getImage() {
        return this.image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public void setImage(AdMarvelNativeImage image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
