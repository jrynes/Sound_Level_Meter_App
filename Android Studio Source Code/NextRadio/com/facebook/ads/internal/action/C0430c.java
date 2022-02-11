package com.facebook.ads.internal.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import com.facebook.ads.internal.util.C0514b.C0513a;
import com.facebook.ads.internal.util.C0519f;
import com.facebook.ads.internal.util.C0524i;
import com.facebook.ads.internal.util.C0536s;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.action.c */
public class C0430c extends C0428a {
    private static final String f1553a;
    private final Context f1554b;
    private final Uri f1555c;

    static {
        f1553a = C0430c.class.getSimpleName();
    }

    public C0430c(Context context, Uri uri) {
        this.f1554b = context;
        this.f1555c = uri;
    }

    private Intent m1167a(C0524i c0524i) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        if (!(C0536s.m1572a(c0524i.m1550a()) || C0536s.m1572a(c0524i.m1551b()))) {
            intent.setComponent(new ComponentName(c0524i.m1550a(), c0524i.m1551b()));
        }
        if (!C0536s.m1572a(c0524i.m1552c())) {
            intent.setData(Uri.parse(c0524i.m1552c()));
        }
        return intent;
    }

    private Intent m1168b(C0524i c0524i) {
        if (C0536s.m1572a(c0524i.m1550a())) {
            return null;
        }
        if (!C0519f.m1526a(this.f1554b, c0524i.m1550a())) {
            return null;
        }
        String c = c0524i.m1552c();
        if (!C0536s.m1572a(c) && (c.startsWith("tel:") || c.startsWith("telprompt:"))) {
            return new Intent("android.intent.action.CALL", Uri.parse(c));
        }
        PackageManager packageManager = this.f1554b.getPackageManager();
        if (C0536s.m1572a(c0524i.m1551b()) && C0536s.m1572a(c)) {
            return packageManager.getLaunchIntentForPackage(c0524i.m1550a());
        }
        Intent a = m1167a(c0524i);
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(a, AccessibilityNodeInfoCompat.ACTION_CUT);
        if (a.getComponent() == null) {
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                if (resolveInfo.activityInfo.packageName.equals(c0524i.m1550a())) {
                    a.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                    break;
                }
            }
        }
        return (queryIntentActivities.isEmpty() || a.getComponent() == null) ? null : a;
    }

    private List<C0524i> m1169f() {
        String queryParameter = this.f1555c.getQueryParameter("appsite_data");
        if (C0536s.m1572a(queryParameter) || "[]".equals(queryParameter)) {
            return null;
        }
        List<C0524i> arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(queryParameter).optJSONArray(AbstractSpiCall.ANDROID_CLIENT_TYPE);
            if (optJSONArray == null) {
                return arrayList;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                C0524i a = C0524i.m1549a(optJSONArray.optJSONObject(i));
                if (a != null) {
                    arrayList.add(a);
                }
            }
            return arrayList;
        } catch (Throwable e) {
            Log.w(f1553a, "Error parsing appsite_data", e);
            return arrayList;
        }
    }

    public C0513a m1170a() {
        return C0513a.OPEN_STORE;
    }

    public void m1171b() {
        m1164a(this.f1554b, this.f1555c);
        List<Intent> d = m1173d();
        if (d != null) {
            for (Intent startActivity : d) {
                try {
                    this.f1554b.startActivity(startActivity);
                    return;
                } catch (Throwable e) {
                    Log.d(f1553a, "Failed to open app intent, falling back", e);
                }
            }
        }
        m1174e();
    }

    protected Uri m1172c() {
        String queryParameter = this.f1555c.getQueryParameter("store_url");
        if (!C0536s.m1572a(queryParameter)) {
            return Uri.parse(queryParameter);
        }
        queryParameter = this.f1555c.getQueryParameter("store_id");
        return Uri.parse(String.format("market://details?id=%s", new Object[]{queryParameter}));
    }

    protected List<Intent> m1173d() {
        List<C0524i> f = m1169f();
        List<Intent> arrayList = new ArrayList();
        if (f != null) {
            for (C0524i b : f) {
                Intent b2 = m1168b(b);
                if (b2 != null) {
                    arrayList.add(b2);
                }
            }
        }
        return arrayList;
    }

    public void m1174e() {
        Intent intent = new Intent("android.intent.action.VIEW", m1172c());
        intent.addFlags(268435456);
        try {
            this.f1554b.startActivity(intent);
        } catch (Throwable e) {
            Log.d(f1553a, "Failed to open market url: " + this.f1555c.toString(), e);
            String queryParameter = this.f1555c.getQueryParameter("store_url_web_fallback");
            if (queryParameter != null && queryParameter.length() > 0) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(queryParameter));
                intent2.addFlags(268435456);
                try {
                    this.f1554b.startActivity(intent2);
                } catch (Throwable e2) {
                    Log.d(f1553a, "Failed to open fallback url: " + queryParameter, e2);
                }
            }
        }
    }
}
