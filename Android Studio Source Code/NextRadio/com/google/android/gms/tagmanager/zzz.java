package com.google.android.gms.tagmanager;

import android.content.Context;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.net.URLEncoder;

public class zzz implements zzar {
    private static final Object zzbhz;
    private static zzz zzbiM;
    private String zzbiN;
    private String zzbiO;
    private zzas zzbiP;
    private zzcd zzbic;

    static {
        zzbhz = new Object();
    }

    private zzz(Context context) {
        this(zzat.zzaZ(context), new zzcs());
    }

    zzz(zzas com_google_android_gms_tagmanager_zzas, zzcd com_google_android_gms_tagmanager_zzcd) {
        this.zzbiP = com_google_android_gms_tagmanager_zzas;
        this.zzbic = com_google_android_gms_tagmanager_zzcd;
    }

    public static zzar zzaX(Context context) {
        zzar com_google_android_gms_tagmanager_zzar;
        synchronized (zzbhz) {
            if (zzbiM == null) {
                zzbiM = new zzz(context);
            }
            com_google_android_gms_tagmanager_zzar = zzbiM;
        }
        return com_google_android_gms_tagmanager_zzar;
    }

    public boolean zzgc(String str) {
        if (this.zzbic.zzlw()) {
            if (!(this.zzbiN == null || this.zzbiO == null)) {
                try {
                    str = this.zzbiN + "?" + this.zzbiO + "=" + URLEncoder.encode(str, HttpRequest.CHARSET_UTF8);
                    zzbg.m1676v("Sending wrapped url hit: " + str);
                } catch (Throwable e) {
                    zzbg.zzd("Error wrapping URL for testing.", e);
                    return false;
                }
            }
            this.zzbiP.zzgg(str);
            return true;
        }
        zzbg.zzaK("Too many urls sent too quickly with the TagManagerSender, rate limiting invoked.");
        return false;
    }
}
