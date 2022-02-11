package com.amazon.device.associates;

import android.content.Intent;

/* renamed from: com.amazon.device.associates.f */
public final class DASResponseReceiver {
    private static final String f1333a;

    static {
        f1333a = DASResponseReceiver.class.getSimpleName();
    }

    public void m956a(Intent intent, bb bbVar) {
        bj bkVar = new bk();
        Log.m1018c(f1333a, "Intent action receiveed by dummy response receiver:" + intent.getAction());
        bkVar.m681a(null, intent, bbVar);
    }
}
