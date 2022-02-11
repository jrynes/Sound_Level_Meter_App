package com.amazon.device.associates;

import android.util.Log;

/* compiled from: SandboxLogHandler */
final class am implements bn {
    am() {
    }

    private static String m758a(String str) {
        return "IAP Physical SDK/Sandbox Mode: " + str;
    }

    public boolean m760a() {
        return true;
    }

    public boolean m762b() {
        return true;
    }

    public void m759a(String str, String str2) {
        Log.d(str, m758a(str2));
    }

    public void m761b(String str, String str2) {
        Log.e(str, m758a(str2));
    }
}
