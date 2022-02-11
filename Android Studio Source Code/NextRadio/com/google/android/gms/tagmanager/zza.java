package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Process;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;

public class zza {
    private static zza zzbhA;
    private static Object zzbhz;
    private volatile boolean mClosed;
    private final Context mContext;
    private final Thread zzLM;
    private volatile Info zzPW;
    private volatile long zzbht;
    private volatile long zzbhu;
    private volatile long zzbhv;
    private volatile long zzbhw;
    private final Object zzbhx;
    private zza zzbhy;
    private final zzmq zzqW;

    public interface zza {
        Info zzFV();
    }

    /* renamed from: com.google.android.gms.tagmanager.zza.1 */
    class C10331 implements zza {
        final /* synthetic */ zza zzbhB;

        C10331(zza com_google_android_gms_tagmanager_zza) {
            this.zzbhB = com_google_android_gms_tagmanager_zza;
        }

        public Info zzFV() {
            Info info = null;
            try {
                info = AdvertisingIdClient.getAdvertisingIdInfo(this.zzbhB.mContext);
            } catch (Throwable e) {
                zzbg.zzd("IllegalStateException getting Advertising Id Info", e);
            } catch (Throwable e2) {
                zzbg.zzd("GooglePlayServicesRepairableException getting Advertising Id Info", e2);
            } catch (Throwable e22) {
                zzbg.zzd("IOException getting Ad Id Info", e22);
            } catch (Throwable e222) {
                zzbg.zzd("GooglePlayServicesNotAvailableException getting Advertising Id Info", e222);
            } catch (Throwable e2222) {
                zzbg.zzd("Unknown exception. Could not get the Advertising Id Info.", e2222);
            }
            return info;
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zza.2 */
    class C10342 implements Runnable {
        final /* synthetic */ zza zzbhB;

        C10342(zza com_google_android_gms_tagmanager_zza) {
            this.zzbhB = com_google_android_gms_tagmanager_zza;
        }

        public void run() {
            this.zzbhB.zzFU();
        }
    }

    static {
        zzbhz = new Object();
    }

    private zza(Context context) {
        this(context, null, zzmt.zzsc());
    }

    public zza(Context context, zza com_google_android_gms_tagmanager_zza_zza, zzmq com_google_android_gms_internal_zzmq) {
        this.zzbht = 900000;
        this.zzbhu = 30000;
        this.mClosed = false;
        this.zzbhx = new Object();
        this.zzbhy = new C10331(this);
        this.zzqW = com_google_android_gms_internal_zzmq;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        if (com_google_android_gms_tagmanager_zza_zza != null) {
            this.zzbhy = com_google_android_gms_tagmanager_zza_zza;
        }
        this.zzbhv = this.zzqW.currentTimeMillis();
        this.zzLM = new Thread(new C10342(this));
    }

    private void zzFR() {
        synchronized (this) {
            try {
                zzFS();
                wait(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void zzFS() {
        if (this.zzqW.currentTimeMillis() - this.zzbhv > this.zzbhu) {
            synchronized (this.zzbhx) {
                this.zzbhx.notify();
            }
            this.zzbhv = this.zzqW.currentTimeMillis();
        }
    }

    private void zzFT() {
        if (this.zzqW.currentTimeMillis() - this.zzbhw > 3600000) {
            this.zzPW = null;
        }
    }

    private void zzFU() {
        Process.setThreadPriority(10);
        while (!this.mClosed) {
            Info zzFV = this.zzbhy.zzFV();
            if (zzFV != null) {
                this.zzPW = zzFV;
                this.zzbhw = this.zzqW.currentTimeMillis();
                zzbg.zzaJ("Obtained fresh AdvertisingId info from GmsCore.");
            }
            synchronized (this) {
                notifyAll();
            }
            try {
                synchronized (this.zzbhx) {
                    this.zzbhx.wait(this.zzbht);
                }
            } catch (InterruptedException e) {
                zzbg.zzaJ("sleep interrupted in AdvertiserDataPoller thread; continuing");
            }
        }
    }

    public static zza zzaW(Context context) {
        if (zzbhA == null) {
            synchronized (zzbhz) {
                if (zzbhA == null) {
                    zzbhA = new zza(context);
                    zzbhA.start();
                }
            }
        }
        return zzbhA;
    }

    public boolean isLimitAdTrackingEnabled() {
        if (this.zzPW == null) {
            zzFR();
        } else {
            zzFS();
        }
        zzFT();
        return this.zzPW == null ? true : this.zzPW.isLimitAdTrackingEnabled();
    }

    public void start() {
        this.zzLM.start();
    }

    public String zzFQ() {
        if (this.zzPW == null) {
            zzFR();
        } else {
            zzFS();
        }
        zzFT();
        return this.zzPW == null ? null : this.zzPW.getId();
    }
}
