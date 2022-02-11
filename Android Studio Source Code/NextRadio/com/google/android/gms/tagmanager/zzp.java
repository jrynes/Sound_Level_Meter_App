package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzaf.zzj;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import com.google.android.gms.internal.zzrr;
import org.apache.activemq.transport.stomp.Stomp;

public class zzp extends com.google.android.gms.common.api.internal.zzb<ContainerHolder> {
    private final Context mContext;
    private final Looper zzagr;
    private final String zzbhM;
    private long zzbhR;
    private final TagManager zzbhY;
    private final zzd zzbib;
    private final zzcd zzbic;
    private final int zzbid;
    private zzf zzbie;
    private zzrr zzbif;
    private volatile zzo zzbig;
    private volatile boolean zzbih;
    private zzj zzbii;
    private String zzbij;
    private zze zzbik;
    private zza zzbil;
    private final zzmq zzqW;

    interface zze extends Releasable {
        void zza(zzbf<zzj> com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzaf_zzj);

        void zzf(long j, String str);

        void zzfW(String str);
    }

    interface zzf extends Releasable {
        void zzGl();

        void zza(zzbf<com.google.android.gms.internal.zzrq.zza> com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzrq_zza);

        void zzb(com.google.android.gms.internal.zzrq.zza com_google_android_gms_internal_zzrq_zza);

        com.google.android.gms.internal.zzrs.zzc zzke(int i);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzp.1 */
    class C10531 {
    }

    /* renamed from: com.google.android.gms.tagmanager.zzp.2 */
    class C10542 implements com.google.android.gms.tagmanager.zzo.zza {
        final /* synthetic */ zzp zzbim;

        C10542(zzp com_google_android_gms_tagmanager_zzp) {
            this.zzbim = com_google_android_gms_tagmanager_zzp;
        }

        public String zzGd() {
            return this.zzbim.zzGd();
        }

        public void zzGf() {
            zzbg.zzaK("Refresh ignored: container loaded as default only.");
        }

        public void zzfT(String str) {
            this.zzbim.zzfT(str);
        }
    }

    interface zza {
        boolean zzb(Container container);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzp.3 */
    class C10553 implements zza {
        final /* synthetic */ zzp zzbim;
        final /* synthetic */ boolean zzbin;

        C10553(zzp com_google_android_gms_tagmanager_zzp, boolean z) {
            this.zzbim = com_google_android_gms_tagmanager_zzp;
            this.zzbin = z;
        }

        public boolean zzb(Container container) {
            return this.zzbin ? container.getLastRefreshTime() + 43200000 >= this.zzbim.zzqW.currentTimeMillis() : !container.isDefault();
        }
    }

    private class zzb implements zzbf<com.google.android.gms.internal.zzrq.zza> {
        final /* synthetic */ zzp zzbim;

        private zzb(zzp com_google_android_gms_tagmanager_zzp) {
            this.zzbim = com_google_android_gms_tagmanager_zzp;
        }

        public void zzGk() {
        }

        public /* synthetic */ void zzI(Object obj) {
            zza((com.google.android.gms.internal.zzrq.zza) obj);
        }

        public void zza(com.google.android.gms.internal.zzrq.zza com_google_android_gms_internal_zzrq_zza) {
            zzj com_google_android_gms_internal_zzaf_zzj;
            if (com_google_android_gms_internal_zzrq_zza.zzbme != null) {
                com_google_android_gms_internal_zzaf_zzj = com_google_android_gms_internal_zzrq_zza.zzbme;
            } else {
                com.google.android.gms.internal.zzaf.zzf com_google_android_gms_internal_zzaf_zzf = com_google_android_gms_internal_zzrq_zza.zzju;
                com_google_android_gms_internal_zzaf_zzj = new zzj();
                com_google_android_gms_internal_zzaf_zzj.zzju = com_google_android_gms_internal_zzaf_zzf;
                com_google_android_gms_internal_zzaf_zzj.zzjt = null;
                com_google_android_gms_internal_zzaf_zzj.zzjv = com_google_android_gms_internal_zzaf_zzf.version;
            }
            this.zzbim.zza(com_google_android_gms_internal_zzaf_zzj, com_google_android_gms_internal_zzrq_zza.zzbmd, true);
        }

        public void zza(com.google.android.gms.tagmanager.zzbf.zza com_google_android_gms_tagmanager_zzbf_zza) {
            if (!this.zzbim.zzbih) {
                this.zzbim.zzak(0);
            }
        }
    }

    private class zzc implements zzbf<zzj> {
        final /* synthetic */ zzp zzbim;

        private zzc(zzp com_google_android_gms_tagmanager_zzp) {
            this.zzbim = com_google_android_gms_tagmanager_zzp;
        }

        public void zzGk() {
        }

        public /* synthetic */ void zzI(Object obj) {
            zzb((zzj) obj);
        }

        public void zza(com.google.android.gms.tagmanager.zzbf.zza com_google_android_gms_tagmanager_zzbf_zza) {
            synchronized (this.zzbim) {
                if (!this.zzbim.isReady()) {
                    if (this.zzbim.zzbig != null) {
                        this.zzbim.zza(this.zzbim.zzbig);
                    } else {
                        this.zzbim.zza(this.zzbim.zzbn(Status.zzagF));
                    }
                }
            }
            this.zzbim.zzak(3600000);
        }

        public void zzb(zzj com_google_android_gms_internal_zzaf_zzj) {
            synchronized (this.zzbim) {
                if (com_google_android_gms_internal_zzaf_zzj.zzju == null) {
                    if (this.zzbim.zzbii.zzju == null) {
                        zzbg.m1675e("Current resource is null; network resource is also null");
                        this.zzbim.zzak(3600000);
                        return;
                    }
                    com_google_android_gms_internal_zzaf_zzj.zzju = this.zzbim.zzbii.zzju;
                }
                this.zzbim.zza(com_google_android_gms_internal_zzaf_zzj, this.zzbim.zzqW.currentTimeMillis(), false);
                zzbg.m1676v("setting refresh time to current time: " + this.zzbim.zzbhR);
                if (!this.zzbim.zzGj()) {
                    this.zzbim.zza(com_google_android_gms_internal_zzaf_zzj);
                }
            }
        }
    }

    private class zzd implements com.google.android.gms.tagmanager.zzo.zza {
        final /* synthetic */ zzp zzbim;

        private zzd(zzp com_google_android_gms_tagmanager_zzp) {
            this.zzbim = com_google_android_gms_tagmanager_zzp;
        }

        public String zzGd() {
            return this.zzbim.zzGd();
        }

        public void zzGf() {
            if (this.zzbim.zzbic.zzlw()) {
                this.zzbim.zzak(0);
            }
        }

        public void zzfT(String str) {
            this.zzbim.zzfT(str);
        }
    }

    zzp(Context context, TagManager tagManager, Looper looper, String str, int i, zzf com_google_android_gms_tagmanager_zzp_zzf, zze com_google_android_gms_tagmanager_zzp_zze, zzrr com_google_android_gms_internal_zzrr, zzmq com_google_android_gms_internal_zzmq, zzcd com_google_android_gms_tagmanager_zzcd) {
        super(looper == null ? Looper.getMainLooper() : looper);
        this.mContext = context;
        this.zzbhY = tagManager;
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        this.zzagr = looper;
        this.zzbhM = str;
        this.zzbid = i;
        this.zzbie = com_google_android_gms_tagmanager_zzp_zzf;
        this.zzbik = com_google_android_gms_tagmanager_zzp_zze;
        this.zzbif = com_google_android_gms_internal_zzrr;
        this.zzbib = new zzd();
        this.zzbii = new zzj();
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzbic = com_google_android_gms_tagmanager_zzcd;
        if (zzGj()) {
            zzfT(zzcb.zzGU().zzGW());
        }
    }

    public zzp(Context context, TagManager tagManager, Looper looper, String str, int i, zzs com_google_android_gms_tagmanager_zzs) {
        this(context, tagManager, looper, str, i, new zzcn(context, str), new zzcm(context, str, com_google_android_gms_tagmanager_zzs), new zzrr(context), zzmt.zzsc(), new zzbe(30, 900000, 5000, "refreshing", zzmt.zzsc()));
        this.zzbif.zzgB(com_google_android_gms_tagmanager_zzs.zzGm());
    }

    private boolean zzGj() {
        zzcb zzGU = zzcb.zzGU();
        return (zzGU.zzGV() == zza.CONTAINER || zzGU.zzGV() == zza.CONTAINER_DEBUG) && this.zzbhM.equals(zzGU.getContainerId());
    }

    private synchronized void zza(zzj com_google_android_gms_internal_zzaf_zzj) {
        if (this.zzbie != null) {
            com.google.android.gms.internal.zzrq.zza com_google_android_gms_internal_zzrq_zza = new com.google.android.gms.internal.zzrq.zza();
            com_google_android_gms_internal_zzrq_zza.zzbmd = this.zzbhR;
            com_google_android_gms_internal_zzrq_zza.zzju = new com.google.android.gms.internal.zzaf.zzf();
            com_google_android_gms_internal_zzrq_zza.zzbme = com_google_android_gms_internal_zzaf_zzj;
            this.zzbie.zzb(com_google_android_gms_internal_zzrq_zza);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void zza(com.google.android.gms.internal.zzaf.zzj r9, long r10, boolean r12) {
        /*
        r8 = this;
        r6 = 43200000; // 0x2932e00 float:2.1626111E-37 double:2.1343636E-316;
        monitor-enter(r8);
        if (r12 == 0) goto L_0x000c;
    L_0x0006:
        r0 = r8.zzbih;	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r8);
        return;
    L_0x000c:
        r0 = r8.isReady();	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x0016;
    L_0x0012:
        r0 = r8.zzbig;	 Catch:{ all -> 0x006a }
        if (r0 != 0) goto L_0x0016;
    L_0x0016:
        r8.zzbii = r9;	 Catch:{ all -> 0x006a }
        r8.zzbhR = r10;	 Catch:{ all -> 0x006a }
        r0 = 0;
        r2 = 43200000; // 0x2932e00 float:2.1626111E-37 double:2.1343636E-316;
        r4 = r8.zzbhR;	 Catch:{ all -> 0x006a }
        r4 = r4 + r6;
        r6 = r8.zzqW;	 Catch:{ all -> 0x006a }
        r6 = r6.currentTimeMillis();	 Catch:{ all -> 0x006a }
        r4 = r4 - r6;
        r2 = java.lang.Math.min(r2, r4);	 Catch:{ all -> 0x006a }
        r0 = java.lang.Math.max(r0, r2);	 Catch:{ all -> 0x006a }
        r8.zzak(r0);	 Catch:{ all -> 0x006a }
        r0 = new com.google.android.gms.tagmanager.Container;	 Catch:{ all -> 0x006a }
        r1 = r8.mContext;	 Catch:{ all -> 0x006a }
        r2 = r8.zzbhY;	 Catch:{ all -> 0x006a }
        r2 = r2.getDataLayer();	 Catch:{ all -> 0x006a }
        r3 = r8.zzbhM;	 Catch:{ all -> 0x006a }
        r4 = r10;
        r6 = r9;
        r0.<init>(r1, r2, r3, r4, r6);	 Catch:{ all -> 0x006a }
        r1 = r8.zzbig;	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x006d;
    L_0x0049:
        r1 = new com.google.android.gms.tagmanager.zzo;	 Catch:{ all -> 0x006a }
        r2 = r8.zzbhY;	 Catch:{ all -> 0x006a }
        r3 = r8.zzagr;	 Catch:{ all -> 0x006a }
        r4 = r8.zzbib;	 Catch:{ all -> 0x006a }
        r1.<init>(r2, r3, r0, r4);	 Catch:{ all -> 0x006a }
        r8.zzbig = r1;	 Catch:{ all -> 0x006a }
    L_0x0056:
        r1 = r8.isReady();	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x000a;
    L_0x005c:
        r1 = r8.zzbil;	 Catch:{ all -> 0x006a }
        r0 = r1.zzb(r0);	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x000a;
    L_0x0064:
        r0 = r8.zzbig;	 Catch:{ all -> 0x006a }
        r8.zza(r0);	 Catch:{ all -> 0x006a }
        goto L_0x000a;
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x006d:
        r1 = r8.zzbig;	 Catch:{ all -> 0x006a }
        r1.zza(r0);	 Catch:{ all -> 0x006a }
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzp.zza(com.google.android.gms.internal.zzaf$zzj, long, boolean):void");
    }

    private synchronized void zzak(long j) {
        if (this.zzbik == null) {
            zzbg.zzaK("Refresh requested, but no network load scheduler.");
        } else {
            this.zzbik.zzf(j, this.zzbii.zzjv);
        }
    }

    private void zzaw(boolean z) {
        this.zzbie.zza(new zzb());
        this.zzbik.zza(new zzc());
        com.google.android.gms.internal.zzrs.zzc zzke = this.zzbie.zzke(this.zzbid);
        if (zzke != null) {
            this.zzbig = new zzo(this.zzbhY, this.zzagr, new Container(this.mContext, this.zzbhY.getDataLayer(), this.zzbhM, 0, zzke), this.zzbib);
        }
        this.zzbil = new C10553(this, z);
        if (zzGj()) {
            this.zzbik.zzf(0, Stomp.EMPTY);
        } else {
            this.zzbie.zzGl();
        }
    }

    synchronized String zzGd() {
        return this.zzbij;
    }

    public void zzGg() {
        com.google.android.gms.internal.zzrs.zzc zzke = this.zzbie.zzke(this.zzbid);
        if (zzke != null) {
            zza(new zzo(this.zzbhY, this.zzagr, new Container(this.mContext, this.zzbhY.getDataLayer(), this.zzbhM, 0, zzke), new C10542(this)));
        } else {
            String str = "Default was requested, but no default container was found";
            zzbg.m1675e(str);
            zza(zzbn(new Status(10, str, null)));
        }
        this.zzbik = null;
        this.zzbie = null;
    }

    public void zzGh() {
        zzaw(false);
    }

    public void zzGi() {
        zzaw(true);
    }

    protected ContainerHolder zzbn(Status status) {
        if (this.zzbig != null) {
            return this.zzbig;
        }
        if (status == Status.zzagF) {
            zzbg.m1675e("timer expired: setting result to failure");
        }
        return new zzo(status);
    }

    protected /* synthetic */ Result zzc(Status status) {
        return zzbn(status);
    }

    synchronized void zzfT(String str) {
        this.zzbij = str;
        if (this.zzbik != null) {
            this.zzbik.zzfW(str);
        }
    }
}
