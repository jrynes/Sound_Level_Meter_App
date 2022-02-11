package com.google.android.gms.tagmanager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class zzdb extends zzak {
    private static final String ID;
    private static final String NAME;
    private static final String zzblp;
    private static final String zzblq;
    private static final String zzblr;
    private final Context mContext;
    private Handler mHandler;
    private DataLayer zzbhN;
    private boolean zzbls;
    private boolean zzblt;
    private final HandlerThread zzblu;
    private final Set<String> zzblv;

    private final class zza implements Runnable {
        private final long zzCv;
        private final long zzaNY;
        final /* synthetic */ zzdb zzblA;
        private final String zzblw;
        private final String zzblx;
        private final long zzbly;
        private long zzblz;

        zza(zzdb com_google_android_gms_tagmanager_zzdb, String str, String str2, long j, long j2) {
            this.zzblA = com_google_android_gms_tagmanager_zzdb;
            this.zzblw = str;
            this.zzblx = str2;
            this.zzaNY = j;
            this.zzbly = j2;
            this.zzCv = System.currentTimeMillis();
        }

        public void run() {
            if (this.zzbly <= 0 || this.zzblz < this.zzbly) {
                this.zzblz++;
                if (zzcH()) {
                    long currentTimeMillis = System.currentTimeMillis();
                    this.zzblA.zzbhN.push(DataLayer.mapOf(DataLayer.EVENT_KEY, this.zzblw, "gtm.timerInterval", String.valueOf(this.zzaNY), "gtm.timerLimit", String.valueOf(this.zzbly), "gtm.timerStartTime", String.valueOf(this.zzCv), "gtm.timerCurrentTime", String.valueOf(currentTimeMillis), "gtm.timerElapsedTime", String.valueOf(currentTimeMillis - this.zzCv), "gtm.timerEventNumber", String.valueOf(this.zzblz), "gtm.triggers", this.zzblx));
                }
                this.zzblA.mHandler.postDelayed(this, this.zzaNY);
            } else if (!"0".equals(this.zzblx)) {
                this.zzblA.zzblv.remove(this.zzblx);
            }
        }

        protected boolean zzcH() {
            if (this.zzblA.zzblt) {
                return this.zzblA.zzbls;
            }
            KeyguardManager keyguardManager = (KeyguardManager) this.zzblA.mContext.getSystemService("keyguard");
            PowerManager powerManager = (PowerManager) this.zzblA.mContext.getSystemService("power");
            for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) this.zzblA.mContext.getSystemService("activity")).getRunningAppProcesses()) {
                if (Process.myPid() == runningAppProcessInfo.pid && runningAppProcessInfo.importance == 100 && !keyguardManager.inKeyguardRestrictedInputMode() && powerManager.isScreenOn()) {
                    return true;
                }
            }
            return false;
        }
    }

    static {
        ID = zzad.TIMER_LISTENER.toString();
        NAME = zzae.NAME.toString();
        zzblp = zzae.INTERVAL.toString();
        zzblq = zzae.LIMIT.toString();
        zzblr = zzae.UNIQUE_TRIGGER_ID.toString();
    }

    public zzdb(Context context, DataLayer dataLayer) {
        super(ID, zzblp, NAME);
        this.zzblv = new HashSet();
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzblu = new HandlerThread("Google GTM SDK Timer", 10);
        this.zzblu.start();
        this.mHandler = new Handler(this.zzblu.getLooper());
    }

    public boolean zzFW() {
        return false;
    }

    public com.google.android.gms.internal.zzag.zza zzP(Map<String, com.google.android.gms.internal.zzag.zza> map) {
        long parseLong;
        long parseLong2;
        Object zzg = zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(NAME));
        String zzg2 = zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(zzblr));
        String zzg3 = zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(zzblp));
        String zzg4 = zzdf.zzg((com.google.android.gms.internal.zzag.zza) map.get(zzblq));
        try {
            parseLong = Long.parseLong(zzg3);
        } catch (NumberFormatException e) {
            parseLong = 0;
        }
        try {
            parseLong2 = Long.parseLong(zzg4);
        } catch (NumberFormatException e2) {
            parseLong2 = 0;
        }
        if (parseLong > 0 && !TextUtils.isEmpty(zzg)) {
            if (zzg2 == null || zzg2.isEmpty()) {
                zzg2 = "0";
            }
            if (!this.zzblv.contains(zzg2)) {
                if (!"0".equals(zzg2)) {
                    this.zzblv.add(zzg2);
                }
                this.mHandler.postDelayed(new zza(this, zzg, zzg2, parseLong, parseLong2), parseLong);
            }
        }
        return zzdf.zzHF();
    }
}
