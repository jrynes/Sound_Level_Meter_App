package com.nostra13.universalimageloader.core.assist;

import android.graphics.Bitmap;
import android.view.View;

public class SyncImageLoadingListener extends SimpleImageLoadingListener {
    private Bitmap loadedImage;

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        this.loadedImage = loadedImage;
    }

    public Bitmap getLoadedBitmap() {
        return this.loadedImage;
    }
}
