package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.zzx;
import com.rabbitmq.client.impl.AMQConnection;
import java.util.Map;

@zzhb
public class zzeg {
    private final Context mContext;
    private final String zzAY;
    private zzb<zzed> zzAZ;
    private zzb<zzed> zzBa;
    private zze zzBb;
    private int zzBc;
    private final VersionInfoParcel zzpT;
    private final Object zzpV;

    public interface zzb<T> {
        void zze(T t);
    }

    /* renamed from: com.google.android.gms.internal.zzeg.1 */
    class C08131 implements Runnable {
        final /* synthetic */ zze zzBd;
        final /* synthetic */ zzeg zzBe;

        /* renamed from: com.google.android.gms.internal.zzeg.1.1 */
        class C08081 implements com.google.android.gms.internal.zzed.zza {
            final /* synthetic */ zzed zzBf;
            final /* synthetic */ C08131 zzBg;

            /* renamed from: com.google.android.gms.internal.zzeg.1.1.1 */
            class C08071 implements Runnable {
                final /* synthetic */ C08081 zzBh;

                /* renamed from: com.google.android.gms.internal.zzeg.1.1.1.1 */
                class C08061 implements Runnable {
                    final /* synthetic */ C08071 zzBi;

                    C08061(C08071 c08071) {
                        this.zzBi = c08071;
                    }

                    public void run() {
                        this.zzBi.zzBh.zzBf.destroy();
                    }
                }

                C08071(C08081 c08081) {
                    this.zzBh = c08081;
                }

                public void run() {
                    synchronized (this.zzBh.zzBg.zzBe.zzpV) {
                        if (this.zzBh.zzBg.zzBd.getStatus() == -1 || this.zzBh.zzBg.zzBd.getStatus() == 1) {
                            return;
                        }
                        this.zzBh.zzBg.zzBd.reject();
                        zzir.runOnUiThread(new C08061(this));
                        zzin.m1674v("Could not receive loaded message in a timely manner. Rejecting.");
                    }
                }
            }

            C08081(C08131 c08131, zzed com_google_android_gms_internal_zzed) {
                this.zzBg = c08131;
                this.zzBf = com_google_android_gms_internal_zzed;
            }

            public void zzeo() {
                zzir.zzMc.postDelayed(new C08071(this), (long) zza.zzBn);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.1.2 */
        class C08092 implements zzdf {
            final /* synthetic */ zzed zzBf;
            final /* synthetic */ C08131 zzBg;

            C08092(C08131 c08131, zzed com_google_android_gms_internal_zzed) {
                this.zzBg = c08131;
                this.zzBf = com_google_android_gms_internal_zzed;
            }

            public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
                synchronized (this.zzBg.zzBe.zzpV) {
                    if (this.zzBg.zzBd.getStatus() == -1 || this.zzBg.zzBd.getStatus() == 1) {
                        return;
                    }
                    this.zzBg.zzBe.zzBc = 0;
                    this.zzBg.zzBe.zzAZ.zze(this.zzBf);
                    this.zzBg.zzBd.zzh(this.zzBf);
                    this.zzBg.zzBe.zzBb = this.zzBg.zzBd;
                    zzin.m1674v("Successfully loaded JS Engine.");
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.1.3 */
        class C08103 implements zzdf {
            final /* synthetic */ zzed zzBf;
            final /* synthetic */ C08131 zzBg;
            final /* synthetic */ zzja zzBj;

            C08103(C08131 c08131, zzed com_google_android_gms_internal_zzed, zzja com_google_android_gms_internal_zzja) {
                this.zzBg = c08131;
                this.zzBf = com_google_android_gms_internal_zzed;
                this.zzBj = com_google_android_gms_internal_zzja;
            }

            public void zza(zzjp com_google_android_gms_internal_zzjp, Map<String, String> map) {
                synchronized (this.zzBg.zzBe.zzpV) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaJ("JS Engine is requesting an update");
                    if (this.zzBg.zzBe.zzBc == 0) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaJ("Starting reload.");
                        this.zzBg.zzBe.zzBc = 2;
                        this.zzBg.zzBe.zzeq();
                    }
                    this.zzBf.zzb("/requestReload", (zzdf) this.zzBj.get());
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.1.4 */
        class C08124 implements Runnable {
            final /* synthetic */ zzed zzBf;
            final /* synthetic */ C08131 zzBg;

            /* renamed from: com.google.android.gms.internal.zzeg.1.4.1 */
            class C08111 implements Runnable {
                final /* synthetic */ C08124 zzBk;

                C08111(C08124 c08124) {
                    this.zzBk = c08124;
                }

                public void run() {
                    this.zzBk.zzBf.destroy();
                }
            }

            C08124(C08131 c08131, zzed com_google_android_gms_internal_zzed) {
                this.zzBg = c08131;
                this.zzBf = com_google_android_gms_internal_zzed;
            }

            public void run() {
                synchronized (this.zzBg.zzBe.zzpV) {
                    if (this.zzBg.zzBd.getStatus() == -1 || this.zzBg.zzBd.getStatus() == 1) {
                        return;
                    }
                    this.zzBg.zzBd.reject();
                    zzir.runOnUiThread(new C08111(this));
                    zzin.m1674v("Could not receive loaded message in a timely manner. Rejecting.");
                }
            }
        }

        C08131(zzeg com_google_android_gms_internal_zzeg, zze com_google_android_gms_internal_zzeg_zze) {
            this.zzBe = com_google_android_gms_internal_zzeg;
            this.zzBd = com_google_android_gms_internal_zzeg_zze;
        }

        public void run() {
            zzed zza = this.zzBe.zza(this.zzBe.mContext, this.zzBe.zzpT);
            zza.zza(new C08081(this, zza));
            zza.zza("/jsLoaded", new C08092(this, zza));
            zzja com_google_android_gms_internal_zzja = new zzja();
            zzdf c08103 = new C08103(this, zza, com_google_android_gms_internal_zzja);
            com_google_android_gms_internal_zzja.set(c08103);
            zza.zza("/requestReload", c08103);
            if (this.zzBe.zzAY.endsWith(".js")) {
                zza.zzZ(this.zzBe.zzAY);
            } else if (this.zzBe.zzAY.startsWith("<html>")) {
                zza.zzab(this.zzBe.zzAY);
            } else {
                zza.zzaa(this.zzBe.zzAY);
            }
            zzir.zzMc.postDelayed(new C08124(this, zza), (long) zza.zzBm);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzeg.2 */
    class C08142 implements com.google.android.gms.internal.zzji.zzc<zzed> {
        final /* synthetic */ zzeg zzBe;
        final /* synthetic */ zze zzBl;

        C08142(zzeg com_google_android_gms_internal_zzeg, zze com_google_android_gms_internal_zzeg_zze) {
            this.zzBe = com_google_android_gms_internal_zzeg;
            this.zzBl = com_google_android_gms_internal_zzeg_zze;
        }

        public void zza(zzed com_google_android_gms_internal_zzed) {
            synchronized (this.zzBe.zzpV) {
                this.zzBe.zzBc = 0;
                if (!(this.zzBe.zzBb == null || this.zzBl == this.zzBe.zzBb)) {
                    zzin.m1674v("New JS engine is loaded, marking previous one as destroyable.");
                    this.zzBe.zzBb.zzeu();
                }
                this.zzBe.zzBb = this.zzBl;
            }
        }

        public /* synthetic */ void zze(Object obj) {
            zza((zzed) obj);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzeg.3 */
    class C08153 implements com.google.android.gms.internal.zzji.zza {
        final /* synthetic */ zzeg zzBe;
        final /* synthetic */ zze zzBl;

        C08153(zzeg com_google_android_gms_internal_zzeg, zze com_google_android_gms_internal_zzeg_zze) {
            this.zzBe = com_google_android_gms_internal_zzeg;
            this.zzBl = com_google_android_gms_internal_zzeg_zze;
        }

        public void run() {
            synchronized (this.zzBe.zzpV) {
                this.zzBe.zzBc = 1;
                zzin.m1674v("Failed loading new engine. Marking new engine destroyable.");
                this.zzBl.zzeu();
            }
        }
    }

    static class zza {
        static int zzBm;
        static int zzBn;

        static {
            zzBm = 60000;
            zzBn = AMQConnection.HANDSHAKE_TIMEOUT;
        }
    }

    public static class zzc<T> implements zzb<T> {
        public void zze(T t) {
        }
    }

    public static class zzd extends zzjj<zzeh> {
        private final zze zzBo;
        private boolean zzBp;
        private final Object zzpV;

        /* renamed from: com.google.android.gms.internal.zzeg.zzd.1 */
        class C08161 implements com.google.android.gms.internal.zzji.zzc<zzeh> {
            final /* synthetic */ zzd zzBq;

            C08161(zzd com_google_android_gms_internal_zzeg_zzd) {
                this.zzBq = com_google_android_gms_internal_zzeg_zzd;
            }

            public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                zzin.m1674v("Ending javascript session.");
                ((zzei) com_google_android_gms_internal_zzeh).zzew();
            }

            public /* synthetic */ void zze(Object obj) {
                zzd((zzeh) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.zzd.2 */
        class C08172 implements com.google.android.gms.internal.zzji.zzc<zzeh> {
            final /* synthetic */ zzd zzBq;

            C08172(zzd com_google_android_gms_internal_zzeg_zzd) {
                this.zzBq = com_google_android_gms_internal_zzeg_zzd;
            }

            public void zzd(zzeh com_google_android_gms_internal_zzeh) {
                zzin.m1674v("Releasing engine reference.");
                this.zzBq.zzBo.zzet();
            }

            public /* synthetic */ void zze(Object obj) {
                zzd((zzeh) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.zzd.3 */
        class C08183 implements com.google.android.gms.internal.zzji.zza {
            final /* synthetic */ zzd zzBq;

            C08183(zzd com_google_android_gms_internal_zzeg_zzd) {
                this.zzBq = com_google_android_gms_internal_zzeg_zzd;
            }

            public void run() {
                this.zzBq.zzBo.zzet();
            }
        }

        public zzd(zze com_google_android_gms_internal_zzeg_zze) {
            this.zzpV = new Object();
            this.zzBo = com_google_android_gms_internal_zzeg_zze;
        }

        public void release() {
            synchronized (this.zzpV) {
                if (this.zzBp) {
                    return;
                }
                this.zzBp = true;
                zza(new C08161(this), new com.google.android.gms.internal.zzji.zzb());
                zza(new C08172(this), new C08183(this));
            }
        }
    }

    public static class zze extends zzjj<zzed> {
        private zzb<zzed> zzBa;
        private boolean zzBr;
        private int zzBs;
        private final Object zzpV;

        /* renamed from: com.google.android.gms.internal.zzeg.zze.1 */
        class C08191 implements com.google.android.gms.internal.zzji.zzc<zzed> {
            final /* synthetic */ zzd zzBt;
            final /* synthetic */ zze zzBu;

            C08191(zze com_google_android_gms_internal_zzeg_zze, zzd com_google_android_gms_internal_zzeg_zzd) {
                this.zzBu = com_google_android_gms_internal_zzeg_zze;
                this.zzBt = com_google_android_gms_internal_zzeg_zzd;
            }

            public void zza(zzed com_google_android_gms_internal_zzed) {
                zzin.m1674v("Getting a new session for JS Engine.");
                this.zzBt.zzh(com_google_android_gms_internal_zzed.zzen());
            }

            public /* synthetic */ void zze(Object obj) {
                zza((zzed) obj);
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.zze.2 */
        class C08202 implements com.google.android.gms.internal.zzji.zza {
            final /* synthetic */ zzd zzBt;
            final /* synthetic */ zze zzBu;

            C08202(zze com_google_android_gms_internal_zzeg_zze, zzd com_google_android_gms_internal_zzeg_zzd) {
                this.zzBu = com_google_android_gms_internal_zzeg_zze;
                this.zzBt = com_google_android_gms_internal_zzeg_zzd;
            }

            public void run() {
                zzin.m1674v("Rejecting reference for JS Engine.");
                this.zzBt.reject();
            }
        }

        /* renamed from: com.google.android.gms.internal.zzeg.zze.3 */
        class C08223 implements com.google.android.gms.internal.zzji.zzc<zzed> {
            final /* synthetic */ zze zzBu;

            /* renamed from: com.google.android.gms.internal.zzeg.zze.3.1 */
            class C08211 implements Runnable {
                final /* synthetic */ zzed zzBv;
                final /* synthetic */ C08223 zzBw;

                C08211(C08223 c08223, zzed com_google_android_gms_internal_zzed) {
                    this.zzBw = c08223;
                    this.zzBv = com_google_android_gms_internal_zzed;
                }

                public void run() {
                    this.zzBw.zzBu.zzBa.zze(this.zzBv);
                    this.zzBv.destroy();
                }
            }

            C08223(zze com_google_android_gms_internal_zzeg_zze) {
                this.zzBu = com_google_android_gms_internal_zzeg_zze;
            }

            public void zza(zzed com_google_android_gms_internal_zzed) {
                zzir.runOnUiThread(new C08211(this, com_google_android_gms_internal_zzed));
            }

            public /* synthetic */ void zze(Object obj) {
                zza((zzed) obj);
            }
        }

        public zze(zzb<zzed> com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed) {
            this.zzpV = new Object();
            this.zzBa = com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed;
            this.zzBr = false;
            this.zzBs = 0;
        }

        public zzd zzes() {
            zzd com_google_android_gms_internal_zzeg_zzd = new zzd(this);
            synchronized (this.zzpV) {
                zza(new C08191(this, com_google_android_gms_internal_zzeg_zzd), new C08202(this, com_google_android_gms_internal_zzeg_zzd));
                zzx.zzab(this.zzBs >= 0);
                this.zzBs++;
            }
            return com_google_android_gms_internal_zzeg_zzd;
        }

        protected void zzet() {
            boolean z = true;
            synchronized (this.zzpV) {
                if (this.zzBs < 1) {
                    z = false;
                }
                zzx.zzab(z);
                zzin.m1674v("Releasing 1 reference for JS Engine");
                this.zzBs--;
                zzev();
            }
        }

        public void zzeu() {
            boolean z = true;
            synchronized (this.zzpV) {
                if (this.zzBs < 0) {
                    z = false;
                }
                zzx.zzab(z);
                zzin.m1674v("Releasing root reference. JS Engine will be destroyed once other references are released.");
                this.zzBr = true;
                zzev();
            }
        }

        protected void zzev() {
            synchronized (this.zzpV) {
                zzx.zzab(this.zzBs >= 0);
                if (this.zzBr && this.zzBs == 0) {
                    zzin.m1674v("No reference is left (including root). Cleaning up engine.");
                    zza(new C08223(this), new com.google.android.gms.internal.zzji.zzb());
                } else {
                    zzin.m1674v("There are still references to the engine. Not destroying.");
                }
            }
        }
    }

    public zzeg(Context context, VersionInfoParcel versionInfoParcel, String str) {
        this.zzpV = new Object();
        this.zzBc = 1;
        this.zzAY = str;
        this.mContext = context.getApplicationContext();
        this.zzpT = versionInfoParcel;
        this.zzAZ = new zzc();
        this.zzBa = new zzc();
    }

    public zzeg(Context context, VersionInfoParcel versionInfoParcel, String str, zzb<zzed> com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed, zzb<zzed> com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed2) {
        this(context, versionInfoParcel, str);
        this.zzAZ = com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed;
        this.zzBa = com_google_android_gms_internal_zzeg_zzb_com_google_android_gms_internal_zzed2;
    }

    private zze zzep() {
        zze com_google_android_gms_internal_zzeg_zze = new zze(this.zzBa);
        zzir.runOnUiThread(new C08131(this, com_google_android_gms_internal_zzeg_zze));
        return com_google_android_gms_internal_zzeg_zze;
    }

    protected zzed zza(Context context, VersionInfoParcel versionInfoParcel) {
        return new zzef(context, versionInfoParcel, null);
    }

    protected zze zzeq() {
        zze zzep = zzep();
        zzep.zza(new C08142(this, zzep), new C08153(this, zzep));
        return zzep;
    }

    public zzd zzer() {
        zzd zzes;
        synchronized (this.zzpV) {
            if (this.zzBb == null || this.zzBb.getStatus() == -1) {
                this.zzBc = 2;
                this.zzBb = zzeq();
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 0) {
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 1) {
                this.zzBc = 2;
                zzeq();
                zzes = this.zzBb.zzes();
            } else if (this.zzBc == 2) {
                zzes = this.zzBb.zzes();
            } else {
                zzes = this.zzBb.zzes();
            }
        }
        return zzes;
    }
}
