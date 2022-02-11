package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import org.xbill.DNS.WKSRecord.Service;

public class zzan extends zzd {
    protected boolean zzPi;
    protected int zzRB;
    protected String zzSE;
    protected String zzSF;
    protected int zzSH;
    protected boolean zzTv;
    protected boolean zzTw;
    protected boolean zzTx;

    public zzan(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
    }

    private static int zzby(String str) {
        String toLowerCase = str.toLowerCase();
        return Diagnostics.verbose.equals(toLowerCase) ? 0 : Diagnostics.info.equals(toLowerCase) ? 1 : "warning".equals(toLowerCase) ? 2 : Diagnostics.error.equals(toLowerCase) ? 3 : -1;
    }

    public int getLogLevel() {
        zzjv();
        return this.zzRB;
    }

    void zza(zzaa com_google_android_gms_analytics_internal_zzaa) {
        int zzby;
        zzbd("Loading global XML config values");
        if (com_google_android_gms_analytics_internal_zzaa.zzlf()) {
            String zzlg = com_google_android_gms_analytics_internal_zzaa.zzlg();
            this.zzSE = zzlg;
            zzb("XML config - app name", zzlg);
        }
        if (com_google_android_gms_analytics_internal_zzaa.zzlh()) {
            zzlg = com_google_android_gms_analytics_internal_zzaa.zzli();
            this.zzSF = zzlg;
            zzb("XML config - app version", zzlg);
        }
        if (com_google_android_gms_analytics_internal_zzaa.zzlj()) {
            zzby = zzby(com_google_android_gms_analytics_internal_zzaa.zzlk());
            if (zzby >= 0) {
                this.zzRB = zzby;
                zza("XML config - log level", Integer.valueOf(zzby));
            }
        }
        if (com_google_android_gms_analytics_internal_zzaa.zzll()) {
            zzby = com_google_android_gms_analytics_internal_zzaa.zzlm();
            this.zzSH = zzby;
            this.zzTw = true;
            zzb("XML config - dispatch period (sec)", Integer.valueOf(zzby));
        }
        if (com_google_android_gms_analytics_internal_zzaa.zzln()) {
            boolean zzlo = com_google_android_gms_analytics_internal_zzaa.zzlo();
            this.zzPi = zzlo;
            this.zzTx = true;
            zzb("XML config - dry run", Boolean.valueOf(zzlo));
        }
    }

    protected void zziJ() {
        zzmd();
    }

    public String zzlg() {
        zzjv();
        return this.zzSE;
    }

    public String zzli() {
        zzjv();
        return this.zzSF;
    }

    public boolean zzlj() {
        zzjv();
        return this.zzTv;
    }

    public boolean zzll() {
        zzjv();
        return this.zzTw;
    }

    public boolean zzln() {
        zzjv();
        return this.zzTx;
    }

    public boolean zzlo() {
        zzjv();
        return this.zzPi;
    }

    public int zzmc() {
        zzjv();
        return this.zzSH;
    }

    protected void zzmd() {
        ApplicationInfo applicationInfo;
        Context context = getContext();
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), Service.PWDGEN);
        } catch (NameNotFoundException e) {
            zzd("PackageManager doesn't know about the app package", e);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            zzbg("Couldn't get ApplicationInfo to load global config");
            return;
        }
        Bundle bundle = applicationInfo.metaData;
        if (bundle != null) {
            int i = bundle.getInt("com.google.android.gms.analytics.globalConfigResource");
            if (i > 0) {
                zzaa com_google_android_gms_analytics_internal_zzaa = (zzaa) new zzz(zzji()).zzah(i);
                if (com_google_android_gms_analytics_internal_zzaa != null) {
                    zza(com_google_android_gms_analytics_internal_zzaa);
                }
            }
        }
    }
}
