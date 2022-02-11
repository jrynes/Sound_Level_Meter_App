package com.onelouder.adlib;

import android.util.Log;

class Diagnostics {
    public static final int DATA_USAGE = 6;
    public static final int DEBUG = 4;
    public static final int ERROR = 1;
    public static final int INFO = 3;
    public static final int NONE = 0;
    public static final int VERBOSE = 5;
    public static final int WARNING = 2;
    private static Object csLock = null;
    public static final String dataUsage = "dataUsage";
    public static final String debug = "debug";
    public static final String error = "error";
    public static final String info = "info";
    private static Diagnostics mInstance = null;
    public static final String verbose = "verbose";
    public static final String warning = "warn";
    private int mLogLevel;

    protected Diagnostics() {
        this.mLogLevel = NONE;
    }

    static {
        csLock = new Object();
        mInstance = null;
    }

    public static Diagnostics getInstance() {
        synchronized (csLock) {
            if (mInstance == null) {
                mInstance = new Diagnostics();
            }
        }
        return mInstance;
    }

    public static void m1957w(String tag, String msg) {
        if (getInstance().isEnabled(WARNING)) {
            Log.w(tag, msg);
        }
    }

    public static void m1958w(String tag, Throwable e) {
        if (getInstance().isEnabled(WARNING)) {
            Log.w(tag, e.toString());
        }
    }

    public static void m1959w(String tag, VirtualMachineError e) {
        if (getInstance().isEnabled(WARNING)) {
            Log.w(tag, e);
        }
    }

    public static void m1952e(String tag, String msg) {
        if (getInstance().isEnabled(ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static void m1953e(String tag, Throwable e) {
        if (getInstance().isEnabled(ERROR)) {
            e.printStackTrace();
        }
    }

    public static void m1954e(String tag, VirtualMachineError e) {
        if (getInstance().isEnabled(ERROR)) {
            Log.w(tag, e);
        }
    }

    public static void m1951d(String tag, String msg) {
        if (getInstance().isEnabled(DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void m1955i(String tag, String msg) {
        if (getInstance().isEnabled(INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void m1956v(String tag, String msg) {
        if (getInstance().isEnabled(VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public void setLogLevel(int level) {
        this.mLogLevel = level;
    }

    public int getLogLevel() {
        return this.mLogLevel;
    }

    public boolean isEnabled(int level) {
        return this.mLogLevel >= level;
    }
}
