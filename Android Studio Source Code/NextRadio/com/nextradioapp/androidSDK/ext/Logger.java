package com.nextradioapp.androidSDK.ext;

import android.util.Log;
import com.nextradioapp.core.dependencies.ILogger;

public class Logger implements ILogger {
    public void m1931d(String tag, String message) {
        Log.d(tag, message);
    }

    public void m1932e(String tag, String message) {
        Log.e(tag, message);
    }

    public void m1933w(String tag, String message) {
        Log.w(tag, message);
    }
}
