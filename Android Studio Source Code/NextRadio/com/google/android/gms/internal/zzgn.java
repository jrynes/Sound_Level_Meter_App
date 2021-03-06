package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzjq.zza;
import java.util.concurrent.atomic.AtomicBoolean;

@zzhb
public abstract class zzgn implements zzit<Void>, zza {
    protected final Context mContext;
    protected final zzgr.zza zzGc;
    protected final zzif.zza zzGd;
    protected AdResponseParcel zzGe;
    private Runnable zzGf;
    protected final Object zzGg;
    private AtomicBoolean zzGh;
    protected final zzjp zzpD;

    /* renamed from: com.google.android.gms.internal.zzgn.1 */
    class C08461 implements Runnable {
        final /* synthetic */ zzgn zzGi;

        C08461(zzgn com_google_android_gms_internal_zzgn) {
            this.zzGi = com_google_android_gms_internal_zzgn;
        }

        public void run() {
            if (this.zzGi.zzGh.get()) {
                zzb.m1672e("Timed out waiting for WebView to finish loading.");
                this.zzGi.cancel();
            }
        }
    }

    protected zzgn(Context context, zzif.zza com_google_android_gms_internal_zzif_zza, zzjp com_google_android_gms_internal_zzjp, zzgr.zza com_google_android_gms_internal_zzgr_zza) {
        this.zzGg = new Object();
        this.zzGh = new AtomicBoolean(true);
        this.mContext = context;
        this.zzGd = com_google_android_gms_internal_zzif_zza;
        this.zzGe = this.zzGd.zzLe;
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzGc = com_google_android_gms_internal_zzgr_zza;
    }

    private zzif zzD(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzGd.zzLd;
        return new zzif(adRequestInfoParcel.zzHt, this.zzpD, this.zzGe.zzBQ, i, this.zzGe.zzBR, this.zzGe.zzHV, this.zzGe.orientation, this.zzGe.zzBU, adRequestInfoParcel.zzHw, this.zzGe.zzHT, null, null, null, null, null, this.zzGe.zzHU, this.zzGd.zzrp, this.zzGe.zzHS, this.zzGd.zzKY, this.zzGe.zzHX, this.zzGe.zzHY, this.zzGd.zzKT, null, this.zzGe.zzIj, this.zzGe.zzIk, this.zzGe.zzIl, this.zzGe.zzIm);
    }

    public void cancel() {
        if (this.zzGh.getAndSet(false)) {
            this.zzpD.stopLoading();
            zzr.zzbE().zzi(this.zzpD);
            zzC(-1);
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    protected void zzC(int i) {
        if (i != -2) {
            this.zzGe = new AdResponseParcel(i, this.zzGe.zzBU);
        }
        this.zzpD.zzhO();
        this.zzGc.zzb(zzD(i));
    }

    public void zza(zzjp com_google_android_gms_internal_zzjp, boolean z) {
        zzb.zzaI("WebView finished loading.");
        if (this.zzGh.getAndSet(false)) {
            zzC(z ? zzgc() : -1);
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    public final Void zzga() {
        zzx.zzcD("Webview render task needs to be called on UI thread.");
        this.zzGf = new C08461(this);
        zzir.zzMc.postDelayed(this.zzGf, ((Long) zzbt.zzwY.get()).longValue());
        zzgb();
        return null;
    }

    protected abstract void zzgb();

    protected int zzgc() {
        return -2;
    }

    public /* synthetic */ Object zzgd() {
        return zzga();
    }
}
