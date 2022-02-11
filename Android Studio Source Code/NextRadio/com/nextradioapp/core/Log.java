package com.nextradioapp.core;

import com.nextradioapp.core.dependencies.ILogger;

public class Log {
    public static ILogger instance;

    static {
        instance = new SystemLogger();
    }

    public static void m1934d(String tag, String message) {
        instance.d(tag, message);
    }

    public static void m1935e(String tagVis, String message) {
        instance.e(tagVis, message);
    }

    public static void m1936w(String tag, String string) {
        instance.w(tag, string);
    }
}
