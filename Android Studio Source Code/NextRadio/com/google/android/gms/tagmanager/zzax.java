package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;

public class zzax {
    private static String zzbjg;
    static Map<String, String> zzbjh;

    static {
        zzbjh = new HashMap();
    }

    public static String zzU(String str, String str2) {
        return str2 == null ? str.length() > 0 ? str : null : Uri.parse("http://hostname/?" + str).getQueryParameter(str2);
    }

    public static String zzf(Context context, String str, String str2) {
        String str3 = (String) zzbjh.get(str);
        if (str3 == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("gtm_click_referrers", 0);
            str3 = sharedPreferences != null ? sharedPreferences.getString(str, Stomp.EMPTY) : Stomp.EMPTY;
            zzbjh.put(str, str3);
        }
        return zzU(str3, str2);
    }

    public static void zzgh(String str) {
        synchronized (zzax.class) {
            zzbjg = str;
        }
    }

    public static String zzm(Context context, String str) {
        if (zzbjg == null) {
            synchronized (zzax.class) {
                if (zzbjg == null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("gtm_install_referrer", 0);
                    if (sharedPreferences != null) {
                        zzbjg = sharedPreferences.getString("referrer", Stomp.EMPTY);
                    } else {
                        zzbjg = Stomp.EMPTY;
                    }
                }
            }
        }
        return zzU(zzbjg, str);
    }

    public static void zzn(Context context, String str) {
        String zzU = zzU(str, "conv");
        if (zzU != null && zzU.length() > 0) {
            zzbjh.put(zzU, str);
            zzcv.zzb(context, "gtm_click_referrers", zzU, str);
        }
    }
}
