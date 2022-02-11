package com.amazon.device.associates;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import org.json.JSONObject;

/* compiled from: DeviceData */
final class bq {
    private static bq f1280a;
    private final String f1281b;
    private final String f1282c;
    private final String f1283d;
    private final String f1284e;
    private JSONObject f1285f;

    static {
        f1280a = null;
    }

    protected static final synchronized bq m902a(Context context) {
        bq bqVar;
        synchronized (bq.class) {
            if (f1280a == null) {
                f1280a = new bq();
            }
            bqVar = f1280a;
        }
        return bqVar;
    }

    private bq() {
        this.f1285f = new JSONObject();
        this.f1281b = "Android";
        this.f1282c = Build.MODEL;
        this.f1283d = Build.MANUFACTURER;
        this.f1284e = VERSION.RELEASE;
        try {
            this.f1285f = new JSONObject();
            this.f1285f.put("os", this.f1281b);
            this.f1285f.put("model", this.f1282c);
            this.f1285f.put("make", this.f1283d);
            this.f1285f.put("osVersion", this.f1284e);
        } catch (Exception e) {
        }
    }

    protected JSONObject m903a() {
        if (this.f1285f != null) {
            return this.f1285f;
        }
        return null;
    }
}
