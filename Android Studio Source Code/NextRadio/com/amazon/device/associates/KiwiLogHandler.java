package com.amazon.device.associates;

import com.amazon.android.framework.util.KiwiLogger;

/* renamed from: com.amazon.device.associates.y */
final class KiwiLogHandler implements bn {
    private static final KiwiLogger f1394a;

    KiwiLogHandler() {
    }

    static {
        f1394a = new KiwiLogger("In App Purchasing SDK/Real Mode");
    }

    private static String m1030c(String str, String str2) {
        return str + ": " + str2;
    }

    public boolean m1032a() {
        return KiwiLogger.TRACE_ON;
    }

    public boolean m1034b() {
        return KiwiLogger.ERROR_ON;
    }

    public void m1031a(String str, String str2) {
        if (KiwiLogger.TRACE_ON) {
            f1394a.trace(KiwiLogHandler.m1030c(str, str2));
        }
    }

    public void m1033b(String str, String str2) {
        if (KiwiLogger.ERROR_ON) {
            f1394a.error(KiwiLogHandler.m1030c(str, str2));
        }
    }
}
