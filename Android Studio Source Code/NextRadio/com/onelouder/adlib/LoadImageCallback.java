package com.onelouder.adlib;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class LoadImageCallback {
    private boolean cancelled;

    public abstract void error(int i);

    public abstract void existing(Bitmap bitmap);

    public abstract Runnable getImageRunnable(String str);

    public abstract String getUrl();

    public abstract void ready(Bitmap bitmap);

    public boolean isHighPriority() {
        return false;
    }

    public Rect getScaledRect() {
        return null;
    }

    public boolean requestScaledImage() {
        return false;
    }

    public synchronized void cancel() {
        this.cancelled = true;
    }

    public synchronized boolean isCanceled() {
        return this.cancelled;
    }
}
