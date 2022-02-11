package com.amazon.device.associates;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PackageNativeData */
class ad {
    private static ad f1164f;
    private final String f1165a;
    private final String f1166b;
    private final String f1167c;
    private final String f1168d;
    private final String f1169e;
    private JSONObject f1170g;

    static {
        f1164f = null;
    }

    protected static final synchronized ad m710a(Context context) {
        ad adVar;
        synchronized (ad.class) {
            if (f1164f == null) {
                f1164f = new ad(context);
            }
            adVar = f1164f;
        }
        return adVar;
    }

    private ad(Context context) {
        PackageInfo packageInfo;
        this.f1170g = new JSONObject();
        this.f1165a = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        this.f1166b = (String) packageManager.getApplicationLabel(context.getApplicationInfo());
        PackageInfo packageInfo2 = null;
        try {
            packageInfo = packageManager.getPackageInfo(this.f1165a, 0);
        } catch (NameNotFoundException e) {
            packageInfo = packageInfo2;
        }
        this.f1168d = packageInfo != null ? packageInfo.versionName : Stomp.EMPTY;
        this.f1167c = packageInfo != null ? Integer.toString(packageInfo.versionCode) : Stomp.EMPTY;
        try {
            this.f1170g.put("lbl", this.f1166b);
            this.f1170g.put("pn", this.f1165a);
            if (!this.f1167c.equals(Stomp.EMPTY)) {
                this.f1170g.put("v", this.f1167c);
            }
            if (!this.f1168d.equals(Stomp.EMPTY)) {
                this.f1170g.put("vn", this.f1168d);
            }
        } catch (JSONException e2) {
        }
        this.f1169e = this.f1170g.toString();
    }

    protected JSONObject m711a() {
        if (this.f1170g != null) {
            return this.f1170g;
        }
        return null;
    }
}
