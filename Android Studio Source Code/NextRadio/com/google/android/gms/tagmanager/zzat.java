package com.google.android.gms.tagmanager;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

class zzat extends Thread implements zzas {
    private static zzat zzbjb;
    private volatile boolean mClosed;
    private final Context mContext;
    private volatile boolean zzRE;
    private final LinkedBlockingQueue<Runnable> zzbja;
    private volatile zzau zzbjc;

    /* renamed from: com.google.android.gms.tagmanager.zzat.1 */
    class C10351 implements Runnable {
        final /* synthetic */ zzas zzbjd;
        final /* synthetic */ long zzbje;
        final /* synthetic */ zzat zzbjf;
        final /* synthetic */ String zzzP;

        C10351(zzat com_google_android_gms_tagmanager_zzat, zzas com_google_android_gms_tagmanager_zzas, long j, String str) {
            this.zzbjf = com_google_android_gms_tagmanager_zzat;
            this.zzbjd = com_google_android_gms_tagmanager_zzas;
            this.zzbje = j;
            this.zzzP = str;
        }

        public void run() {
            if (this.zzbjf.zzbjc == null) {
                zzcu zzHo = zzcu.zzHo();
                zzHo.zza(this.zzbjf.mContext, this.zzbjd);
                this.zzbjf.zzbjc = zzHo.zzHr();
            }
            this.zzbjf.zzbjc.zzg(this.zzbje, this.zzzP);
        }
    }

    private zzat(Context context) {
        super("GAThread");
        this.zzbja = new LinkedBlockingQueue();
        this.zzRE = false;
        this.mClosed = false;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        start();
    }

    static zzat zzaZ(Context context) {
        if (zzbjb == null) {
            zzbjb = new zzat(context);
        }
        return zzbjb;
    }

    private String zzd(Throwable th) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        th.printStackTrace(printStream);
        printStream.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    public void run() {
        while (!this.mClosed) {
            try {
                Runnable runnable = (Runnable) this.zzbja.take();
                if (!this.zzRE) {
                    runnable.run();
                }
            } catch (InterruptedException e) {
                zzbg.zzaJ(e.toString());
            } catch (Throwable th) {
                zzbg.m1675e("Error on Google TagManager Thread: " + zzd(th));
                zzbg.m1675e("Google TagManager is shutting down.");
                this.zzRE = true;
            }
        }
    }

    public void zzgg(String str) {
        zzk(str, System.currentTimeMillis());
    }

    public void zzj(Runnable runnable) {
        this.zzbja.add(runnable);
    }

    void zzk(String str, long j) {
        zzj(new C10351(this, this, j, str));
    }
}
