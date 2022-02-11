package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzaf.zzj;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class zzcm implements zze {
    private boolean mClosed;
    private final Context mContext;
    private final String zzbhM;
    private String zzbij;
    private zzbf<zzj> zzbkg;
    private zzs zzbkh;
    private final ScheduledExecutorService zzbkj;
    private final zza zzbkk;
    private ScheduledFuture<?> zzbkl;

    interface zzb {
        ScheduledExecutorService zzHb();
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcm.1 */
    class C10381 implements zzb {
        final /* synthetic */ zzcm zzbkm;

        C10381(zzcm com_google_android_gms_tagmanager_zzcm) {
            this.zzbkm = com_google_android_gms_tagmanager_zzcm;
        }

        public ScheduledExecutorService zzHb() {
            return Executors.newSingleThreadScheduledExecutor();
        }
    }

    interface zza {
        zzcl zza(zzs com_google_android_gms_tagmanager_zzs);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcm.2 */
    class C10392 implements zza {
        final /* synthetic */ zzcm zzbkm;

        C10392(zzcm com_google_android_gms_tagmanager_zzcm) {
            this.zzbkm = com_google_android_gms_tagmanager_zzcm;
        }

        public zzcl zza(zzs com_google_android_gms_tagmanager_zzs) {
            return new zzcl(this.zzbkm.mContext, this.zzbkm.zzbhM, com_google_android_gms_tagmanager_zzs);
        }
    }

    public zzcm(Context context, String str, zzs com_google_android_gms_tagmanager_zzs) {
        this(context, str, com_google_android_gms_tagmanager_zzs, null, null);
    }

    zzcm(Context context, String str, zzs com_google_android_gms_tagmanager_zzs, zzb com_google_android_gms_tagmanager_zzcm_zzb, zza com_google_android_gms_tagmanager_zzcm_zza) {
        this.zzbkh = com_google_android_gms_tagmanager_zzs;
        this.mContext = context;
        this.zzbhM = str;
        if (com_google_android_gms_tagmanager_zzcm_zzb == null) {
            com_google_android_gms_tagmanager_zzcm_zzb = new C10381(this);
        }
        this.zzbkj = com_google_android_gms_tagmanager_zzcm_zzb.zzHb();
        if (com_google_android_gms_tagmanager_zzcm_zza == null) {
            this.zzbkk = new C10392(this);
        } else {
            this.zzbkk = com_google_android_gms_tagmanager_zzcm_zza;
        }
    }

    private synchronized void zzHa() {
        if (this.mClosed) {
            throw new IllegalStateException("called method after closed");
        }
    }

    private zzcl zzgm(String str) {
        zzcl zza = this.zzbkk.zza(this.zzbkh);
        zza.zza(this.zzbkg);
        zza.zzfW(this.zzbij);
        zza.zzgl(str);
        return zza;
    }

    public synchronized void release() {
        zzHa();
        if (this.zzbkl != null) {
            this.zzbkl.cancel(false);
        }
        this.zzbkj.shutdown();
        this.mClosed = true;
    }

    public synchronized void zza(zzbf<zzj> com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzaf_zzj) {
        zzHa();
        this.zzbkg = com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzaf_zzj;
    }

    public synchronized void zzf(long j, String str) {
        zzbg.m1676v("loadAfterDelay: containerId=" + this.zzbhM + " delay=" + j);
        zzHa();
        if (this.zzbkg == null) {
            throw new IllegalStateException("callback must be set before loadAfterDelay() is called.");
        }
        if (this.zzbkl != null) {
            this.zzbkl.cancel(false);
        }
        this.zzbkl = this.zzbkj.schedule(zzgm(str), j, TimeUnit.MILLISECONDS);
    }

    public synchronized void zzfW(String str) {
        zzHa();
        this.zzbij = str;
    }
}
