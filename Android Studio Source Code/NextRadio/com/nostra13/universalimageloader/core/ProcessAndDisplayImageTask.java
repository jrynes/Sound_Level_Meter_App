package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.C1271L;

class ProcessAndDisplayImageTask implements Runnable {
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private final Bitmap bitmap;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;

    public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = engine;
        this.bitmap = bitmap;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
    }

    public void run() {
        if (this.engine.configuration.writeLogs) {
            C1271L.m1945d(LOG_POSTPROCESS_IMAGE, this.imageLoadingInfo.memoryCacheKey);
        }
        DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(this.imageLoadingInfo.options.getPostProcessor().process(this.bitmap), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE);
        displayBitmapTask.setLoggingEnabled(this.engine.configuration.writeLogs);
        if (this.imageLoadingInfo.options.isSyncLoading()) {
            displayBitmapTask.run();
        } else {
            this.handler.post(displayBitmapTask);
        }
    }
}
