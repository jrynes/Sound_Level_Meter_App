package com.nextradioapp.androidSDK.data;

import android.graphics.Bitmap;

public interface IImageDownloadCallback {
    void onBitmapReady(Bitmap bitmap);

    void onDownloadingFromInternet();

    void onError();
}
