package com.amazon.device.ads;

import android.util.Log;

/* compiled from: Logger */
class LogcatLogger implements Logger {
    private String logTag;

    LogcatLogger() {
    }

    public LogcatLogger withLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }

    public void m634i(String message) {
        Log.i(this.logTag, message);
    }

    public void m635v(String message) {
        Log.v(this.logTag, message);
    }

    public void m632d(String message) {
        Log.d(this.logTag, message);
    }

    public void m636w(String message) {
        Log.w(this.logTag, message);
    }

    public void m633e(String message) {
        Log.e(this.logTag, message);
    }
}
