package com.nextradioapp.nextradio.test;

import android.util.Log;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class AutomatedTestLogger {
    static String logText;
    private static String superTag;

    static {
        superTag = AutomatedTestFragment.TAG;
        logText = new String();
    }

    public static void NewLine() {
        logText += Stomp.NEWLINE;
        Log.d(superTag, Stomp.EMPTY);
    }

    public static void Log(String tag, String msg) {
        logText += Stomp.NEWLINE + tag + Headers.SEPERATOR + msg;
        Log.d(superTag, tag + Headers.SEPERATOR + msg);
    }

    public static void Error(String tag, String msg) {
        logText += "\nERROR:" + tag + Headers.SEPERATOR + msg;
        Log.e(superTag, tag + Headers.SEPERATOR + msg);
    }
}
