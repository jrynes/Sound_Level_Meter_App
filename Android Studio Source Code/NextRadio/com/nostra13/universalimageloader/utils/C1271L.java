package com.nostra13.universalimageloader.utils;

import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

/* renamed from: com.nostra13.universalimageloader.utils.L */
public final class C1271L {
    private static volatile boolean DISABLED = false;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    static {
        DISABLED = false;
    }

    private C1271L() {
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void m1945d(String message, Object... args) {
        C1271L.log(3, null, message, args);
    }

    public static void m1949i(String message, Object... args) {
        C1271L.log(4, null, message, args);
    }

    public static void m1950w(String message, Object... args) {
        C1271L.log(5, null, message, args);
    }

    public static void m1947e(Throwable ex) {
        C1271L.log(6, ex, null, new Object[0]);
    }

    public static void m1946e(String message, Object... args) {
        C1271L.log(6, null, message, args);
    }

    public static void m1948e(Throwable ex, String message, Object... args) {
        C1271L.log(6, ex, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            String log;
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (ex == null) {
                log = message;
            } else {
                String logMessage;
                if (message == null) {
                    logMessage = ex.getMessage();
                } else {
                    logMessage = message;
                }
                String logBody = Log.getStackTraceString(ex);
                log = String.format(LOG_FORMAT, new Object[]{logMessage, logBody});
            }
            Log.println(priority, ImageLoader.TAG, log);
        }
    }
}
