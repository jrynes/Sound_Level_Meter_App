package com.mologiq.analytics;

import android.content.Context;
import org.json.JSONObject;

/* renamed from: com.mologiq.analytics.m */
abstract class PersistentObject {
    private final String f2151a;

    PersistentObject(String str) {
        this.f2151a = str;
    }

    protected abstract String m1767a();

    final void m1768a(Context context) {
        try {
            String a = DatabaseHandler.m1725a(context).m1735a(this.f2151a);
            if (a != null && a.length() > 0) {
                m1769a(a);
            }
        } catch (Throwable th) {
            Utils.m1924a(Utils.m1922a(th));
        }
    }

    protected abstract void m1769a(String str);

    final boolean m1770b(Context context) {
        boolean z;
        Throwable th;
        try {
            String a = DatabaseHandler.m1725a(context).m1735a(this.f2151a);
            if (a != null && a.length() > 0) {
                JSONObject jSONObject = new JSONObject(a).getJSONObject("core");
                if (jSONObject != null && jSONObject.toString().equals(m1767a())) {
                    z = false;
                    if (z) {
                        try {
                            m1771c(context);
                        } catch (Throwable th2) {
                            th = th2;
                            Utils.m1924a(Utils.m1922a(th));
                            return z;
                        }
                    }
                    return z;
                }
            }
            z = true;
            if (z) {
                m1771c(context);
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            z = true;
            th = th4;
            Utils.m1924a(Utils.m1922a(th));
            return z;
        }
        return z;
    }

    abstract void m1771c(Context context);
}
