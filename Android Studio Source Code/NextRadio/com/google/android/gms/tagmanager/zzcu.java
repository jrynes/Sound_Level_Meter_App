package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

class zzcu extends zzct {
    private static final Object zzbkP;
    private static zzcu zzbla;
    private boolean connected;
    private Handler handler;
    private Context zzbkQ;
    private zzau zzbkR;
    private volatile zzas zzbkS;
    private int zzbkT;
    private boolean zzbkU;
    private boolean zzbkV;
    private boolean zzbkW;
    private zzav zzbkX;
    private zzbl zzbkY;
    private boolean zzbkZ;

    /* renamed from: com.google.android.gms.tagmanager.zzcu.1 */
    class C10461 implements zzav {
        final /* synthetic */ zzcu zzblb;

        C10461(zzcu com_google_android_gms_tagmanager_zzcu) {
            this.zzblb = com_google_android_gms_tagmanager_zzcu;
        }

        public void zzax(boolean z) {
            this.zzblb.zzd(z, this.zzblb.connected);
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcu.2 */
    class C10472 implements Callback {
        final /* synthetic */ zzcu zzblb;

        C10472(zzcu com_google_android_gms_tagmanager_zzcu) {
            this.zzblb = com_google_android_gms_tagmanager_zzcu;
        }

        public boolean handleMessage(Message msg) {
            if (1 == msg.what && zzcu.zzbkP.equals(msg.obj)) {
                this.zzblb.dispatch();
                if (this.zzblb.zzbkT > 0 && !this.zzblb.zzbkZ) {
                    this.zzblb.handler.sendMessageDelayed(this.zzblb.handler.obtainMessage(1, zzcu.zzbkP), (long) this.zzblb.zzbkT);
                }
            }
            return true;
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcu.3 */
    class C10483 implements Runnable {
        final /* synthetic */ zzcu zzblb;

        C10483(zzcu com_google_android_gms_tagmanager_zzcu) {
            this.zzblb = com_google_android_gms_tagmanager_zzcu;
        }

        public void run() {
            this.zzblb.zzbkR.dispatch();
        }
    }

    static {
        zzbkP = new Object();
    }

    private zzcu() {
        this.zzbkT = 1800000;
        this.zzbkU = true;
        this.zzbkV = false;
        this.connected = true;
        this.zzbkW = true;
        this.zzbkX = new C10461(this);
        this.zzbkZ = false;
    }

    public static zzcu zzHo() {
        if (zzbla == null) {
            zzbla = new zzcu();
        }
        return zzbla;
    }

    private void zzHp() {
        this.zzbkY = new zzbl(this);
        this.zzbkY.zzba(this.zzbkQ);
    }

    private void zzHq() {
        this.handler = new Handler(this.zzbkQ.getMainLooper(), new C10472(this));
        if (this.zzbkT > 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(1, zzbkP), (long) this.zzbkT);
        }
    }

    public synchronized void dispatch() {
        if (this.zzbkV) {
            this.zzbkS.zzj(new C10483(this));
        } else {
            zzbg.m1676v("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.zzbkU = true;
        }
    }

    synchronized zzau zzHr() {
        if (this.zzbkR == null) {
            if (this.zzbkQ == null) {
                throw new IllegalStateException("Cant get a store unless we have a context");
            }
            this.zzbkR = new zzby(this.zzbkX, this.zzbkQ);
        }
        if (this.handler == null) {
            zzHq();
        }
        this.zzbkV = true;
        if (this.zzbkU) {
            dispatch();
            this.zzbkU = false;
        }
        if (this.zzbkY == null && this.zzbkW) {
            zzHp();
        }
        return this.zzbkR;
    }

    synchronized void zza(Context context, zzas com_google_android_gms_tagmanager_zzas) {
        if (this.zzbkQ == null) {
            this.zzbkQ = context.getApplicationContext();
            if (this.zzbkS == null) {
                this.zzbkS = com_google_android_gms_tagmanager_zzas;
            }
        }
    }

    public synchronized void zzay(boolean z) {
        zzd(this.zzbkZ, z);
    }

    synchronized void zzd(boolean z, boolean z2) {
        if (!(this.zzbkZ == z && this.connected == z2)) {
            if (z || !z2) {
                if (this.zzbkT > 0) {
                    this.handler.removeMessages(1, zzbkP);
                }
            }
            if (!z && z2 && this.zzbkT > 0) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, zzbkP), (long) this.zzbkT);
            }
            StringBuilder append = new StringBuilder().append("PowerSaveMode ");
            String str = (z || !z2) ? "initiated." : "terminated.";
            zzbg.m1676v(append.append(str).toString());
            this.zzbkZ = z;
            this.connected = z2;
        }
    }

    public synchronized void zzjg() {
        if (!this.zzbkZ && this.connected && this.zzbkT > 0) {
            this.handler.removeMessages(1, zzbkP);
            this.handler.sendMessage(this.handler.obtainMessage(1, zzbkP));
        }
    }
}
