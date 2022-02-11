package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.internal.zzaf.zzj;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrv;
import com.google.android.gms.internal.zzrw;
import com.google.android.gms.tagmanager.zzbf.zza;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.activemq.transport.stomp.Stomp;

class zzcl implements Runnable {
    private final Context mContext;
    private final String zzbhM;
    private volatile String zzbij;
    private final zzrw zzbke;
    private final String zzbkf;
    private zzbf<zzj> zzbkg;
    private volatile zzs zzbkh;
    private volatile String zzbki;

    zzcl(Context context, String str, zzrw com_google_android_gms_internal_zzrw, zzs com_google_android_gms_tagmanager_zzs) {
        this.mContext = context;
        this.zzbke = com_google_android_gms_internal_zzrw;
        this.zzbhM = str;
        this.zzbkh = com_google_android_gms_tagmanager_zzs;
        this.zzbkf = "/r?id=" + str;
        this.zzbij = this.zzbkf;
        this.zzbki = null;
    }

    public zzcl(Context context, String str, zzs com_google_android_gms_tagmanager_zzs) {
        this(context, str, new zzrw(), com_google_android_gms_tagmanager_zzs);
    }

    private boolean zzGX() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        zzbg.m1676v("...no network connectivity");
        return false;
    }

    private void zzGY() {
        if (zzGX()) {
            zzbg.m1676v("Start loading resource from network ...");
            String zzGZ = zzGZ();
            zzrv zzIa = this.zzbke.zzIa();
            try {
                InputStream zzgI = zzIa.zzgI(zzGZ);
                try {
                    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    zzrs.zzb(zzgI, byteArrayOutputStream);
                    zzj zzd = zzj.zzd(byteArrayOutputStream.toByteArray());
                    zzbg.m1676v("Successfully loaded supplemented resource: " + zzd);
                    if (zzd.zzju == null && zzd.zzjt.length == 0) {
                        zzbg.m1676v("No change for container: " + this.zzbhM);
                    }
                    this.zzbkg.zzI(zzd);
                    zzbg.m1676v("Load resource from network finished.");
                } catch (Throwable e) {
                    zzbg.zzd("Error when parsing downloaded resources from url: " + zzGZ + " " + e.getMessage(), e);
                    this.zzbkg.zza(zza.SERVER_ERROR);
                    zzIa.close();
                }
            } catch (FileNotFoundException e2) {
                zzbg.zzaK("No data is retrieved from the given url: " + zzGZ + ". Make sure container_id: " + this.zzbhM + " is correct.");
                this.zzbkg.zza(zza.SERVER_ERROR);
            } catch (Throwable e3) {
                zzbg.zzd("Error when loading resources from url: " + zzGZ + " " + e3.getMessage(), e3);
                this.zzbkg.zza(zza.IO_ERROR);
            } finally {
                zzIa.close();
            }
        } else {
            this.zzbkg.zza(zza.NOT_AVAILABLE);
        }
    }

    public void run() {
        if (this.zzbkg == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.zzbkg.zzGk();
        zzGY();
    }

    String zzGZ() {
        String str = this.zzbkh.zzGm() + this.zzbij + "&v=a65833898";
        if (!(this.zzbki == null || this.zzbki.trim().equals(Stomp.EMPTY))) {
            str = str + "&pv=" + this.zzbki;
        }
        return zzcb.zzGU().zzGV().equals(zza.CONTAINER_DEBUG) ? str + "&gtm_debug=x" : str;
    }

    void zza(zzbf<zzj> com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzaf_zzj) {
        this.zzbkg = com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzaf_zzj;
    }

    void zzfW(String str) {
        if (str == null) {
            this.zzbij = this.zzbkf;
            return;
        }
        zzbg.zzaI("Setting CTFE URL path: " + str);
        this.zzbij = str;
    }

    void zzgl(String str) {
        zzbg.zzaI("Setting previous container version: " + str);
        this.zzbki = str;
    }
}
