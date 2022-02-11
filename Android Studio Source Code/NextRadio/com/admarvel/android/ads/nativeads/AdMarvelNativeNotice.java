package com.admarvel.android.ads.nativeads;

import android.widget.ImageView;
import java.lang.ref.WeakReference;

public class AdMarvelNativeNotice {
    private String clickUrl;
    private AdMarvelNativeImage image;
    private WeakReference<ImageView> imageViewReference;
    private String title;

    public String getClickUrl() {
        return this.clickUrl;
    }

    public AdMarvelNativeImage getImage() {
        return this.image;
    }

    public String getTitle() {
        return this.title;
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
