package com.facebook.ads.internal.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.facebook.ads.internal.adapters.C0432a;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;

/* renamed from: com.facebook.ads.internal.util.f */
public class C0519f {
    public static Collection<String> m1524a(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        Set hashSet = new HashSet();
        for (int i = 0; i < jSONArray.length(); i++) {
            hashSet.add(jSONArray.optString(i));
        }
        return hashSet;
    }

    public static boolean m1525a(Context context, C0432a c0432a) {
        C0518e a = c0432a.m1177a();
        if (a == null || a == C0518e.NONE) {
            return false;
        }
        Collection<String> c = c0432a.m1179c();
        if (c == null || c.isEmpty()) {
            return false;
        }
        for (String a2 : c) {
            if (C0519f.m1526a(context, a2)) {
                int i = 1;
                break;
            }
        }
        boolean z = false;
        if (a == C0518e.INSTALLED) {
            int i2 = 1;
        } else {
            boolean z2 = false;
        }
        if (i != i2) {
            return false;
        }
        if (C0536s.m1572a(c0432a.m1178b())) {
            return true;
        }
        new C0531o().execute(new String[]{a2});
        return false;
    }

    public static boolean m1526a(Context context, String str) {
        if (C0536s.m1572a(str)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
