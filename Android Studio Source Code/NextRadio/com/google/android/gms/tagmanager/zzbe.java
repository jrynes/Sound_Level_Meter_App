package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzmq;

class zzbe implements zzcd {
    private final long zzSP;
    private final int zzSQ;
    private double zzSR;
    private long zzSS;
    private final Object zzST;
    private final String zzSU;
    private final long zzbjt;
    private final zzmq zzqW;

    public zzbe(int i, long j, long j2, String str, zzmq com_google_android_gms_internal_zzmq) {
        this.zzST = new Object();
        this.zzSQ = i;
        this.zzSR = (double) this.zzSQ;
        this.zzSP = j;
        this.zzbjt = j2;
        this.zzSU = str;
        this.zzqW = com_google_android_gms_internal_zzmq;
    }

    public boolean zzlw() {
        boolean z = false;
        synchronized (this.zzST) {
            long currentTimeMillis = this.zzqW.currentTimeMillis();
            if (currentTimeMillis - this.zzSS < this.zzbjt) {
                zzbg.zzaK("Excessive " + this.zzSU + " detected; call ignored.");
            } else {
                if (this.zzSR < ((double) this.zzSQ)) {
                    double d = ((double) (currentTimeMillis - this.zzSS)) / ((double) this.zzSP);
                    if (d > 0.0d) {
                        this.zzSR = Math.min((double) this.zzSQ, d + this.zzSR);
                    }
                }
                this.zzSS = currentTimeMillis;
                if (this.zzSR >= 1.0d) {
                    this.zzSR -= 1.0d;
                    z = true;
                } else {
                    zzbg.zzaK("Excessive " + this.zzSU + " detected; call ignored.");
                }
            }
        }
        return z;
    }
}
