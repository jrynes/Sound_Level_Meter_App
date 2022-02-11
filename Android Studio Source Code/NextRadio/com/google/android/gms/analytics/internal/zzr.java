package com.google.android.gms.analytics.internal;

import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zznf;
import java.util.HashSet;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

public class zzr {
    private final zzf zzOK;
    private Set<Integer> zzRA;
    private volatile Boolean zzRy;
    private String zzRz;

    protected zzr(zzf com_google_android_gms_analytics_internal_zzf) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzf);
        this.zzOK = com_google_android_gms_analytics_internal_zzf;
    }

    public long zzkA() {
        return ((Long) zzy.zzRV.get()).longValue();
    }

    public long zzkB() {
        return ((Long) zzy.zzRW.get()).longValue();
    }

    public int zzkC() {
        return ((Integer) zzy.zzRX.get()).intValue();
    }

    public int zzkD() {
        return ((Integer) zzy.zzRY.get()).intValue();
    }

    public long zzkE() {
        return (long) ((Integer) zzy.zzSl.get()).intValue();
    }

    public String zzkF() {
        return (String) zzy.zzSa.get();
    }

    public String zzkG() {
        return (String) zzy.zzRZ.get();
    }

    public String zzkH() {
        return (String) zzy.zzSb.get();
    }

    public String zzkI() {
        return (String) zzy.zzSc.get();
    }

    public zzm zzkJ() {
        return zzm.zzbm((String) zzy.zzSe.get());
    }

    public zzo zzkK() {
        return zzo.zzbn((String) zzy.zzSf.get());
    }

    public Set<Integer> zzkL() {
        String str = (String) zzy.zzSk.get();
        if (this.zzRA == null || this.zzRz == null || !this.zzRz.equals(str)) {
            String[] split = TextUtils.split(str, Stomp.COMMA);
            Set hashSet = new HashSet();
            for (String parseInt : split) {
                try {
                    hashSet.add(Integer.valueOf(Integer.parseInt(parseInt)));
                } catch (NumberFormatException e) {
                }
            }
            this.zzRz = str;
            this.zzRA = hashSet;
        }
        return this.zzRA;
    }

    public long zzkM() {
        return ((Long) zzy.zzSt.get()).longValue();
    }

    public long zzkN() {
        return ((Long) zzy.zzSu.get()).longValue();
    }

    public long zzkO() {
        return ((Long) zzy.zzSx.get()).longValue();
    }

    public int zzkP() {
        return ((Integer) zzy.zzRO.get()).intValue();
    }

    public int zzkQ() {
        return ((Integer) zzy.zzRQ.get()).intValue();
    }

    public String zzkR() {
        return "google_analytics_v4.db";
    }

    public String zzkS() {
        return "google_analytics2_v4.db";
    }

    public long zzkT() {
        return 86400000;
    }

    public int zzkU() {
        return ((Integer) zzy.zzSn.get()).intValue();
    }

    public int zzkV() {
        return ((Integer) zzy.zzSo.get()).intValue();
    }

    public long zzkW() {
        return ((Long) zzy.zzSp.get()).longValue();
    }

    public long zzkX() {
        return ((Long) zzy.zzSy.get()).longValue();
    }

    public boolean zzkr() {
        return zzd.zzakE;
    }

    public boolean zzks() {
        if (this.zzRy == null) {
            synchronized (this) {
                if (this.zzRy == null) {
                    ApplicationInfo applicationInfo = this.zzOK.getContext().getApplicationInfo();
                    String zzi = zznf.zzi(this.zzOK.getContext(), Process.myPid());
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        boolean z = str != null && str.equals(zzi);
                        this.zzRy = Boolean.valueOf(z);
                    }
                    if ((this.zzRy == null || !this.zzRy.booleanValue()) && "com.google.android.gms.analytics".equals(zzi)) {
                        this.zzRy = Boolean.TRUE;
                    }
                    if (this.zzRy == null) {
                        this.zzRy = Boolean.TRUE;
                        this.zzOK.zzjm().zzbh("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzRy.booleanValue();
    }

    public boolean zzkt() {
        return ((Boolean) zzy.zzRK.get()).booleanValue();
    }

    public int zzku() {
        return ((Integer) zzy.zzSd.get()).intValue();
    }

    public int zzkv() {
        return ((Integer) zzy.zzSh.get()).intValue();
    }

    public int zzkw() {
        return ((Integer) zzy.zzSi.get()).intValue();
    }

    public int zzkx() {
        return ((Integer) zzy.zzSj.get()).intValue();
    }

    public long zzky() {
        return ((Long) zzy.zzRS.get()).longValue();
    }

    public long zzkz() {
        return ((Long) zzy.zzRR.get()).longValue();
    }
}
