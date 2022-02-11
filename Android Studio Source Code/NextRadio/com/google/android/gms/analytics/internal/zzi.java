package com.google.android.gms.analytics.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.stats.zzb;
import java.util.Collections;
import org.xbill.DNS.WKSRecord.Service;

public class zzi extends zzd {
    private final zza zzQH;
    private zzac zzQI;
    private final zzt zzQJ;
    private zzaj zzQK;

    /* renamed from: com.google.android.gms.analytics.internal.zzi.1 */
    class C06611 extends zzt {
        final /* synthetic */ zzi zzQL;

        C06611(zzi com_google_android_gms_analytics_internal_zzi, zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzQL = com_google_android_gms_analytics_internal_zzi;
            super(com_google_android_gms_analytics_internal_zzf);
        }

        public void run() {
            this.zzQL.zzjJ();
        }
    }

    protected class zza implements ServiceConnection {
        final /* synthetic */ zzi zzQL;
        private volatile zzac zzQM;
        private volatile boolean zzQN;

        /* renamed from: com.google.android.gms.analytics.internal.zzi.zza.1 */
        class C06621 implements Runnable {
            final /* synthetic */ zzac zzQO;
            final /* synthetic */ zza zzQP;

            C06621(zza com_google_android_gms_analytics_internal_zzi_zza, zzac com_google_android_gms_analytics_internal_zzac) {
                this.zzQP = com_google_android_gms_analytics_internal_zzi_zza;
                this.zzQO = com_google_android_gms_analytics_internal_zzac;
            }

            public void run() {
                if (!this.zzQP.zzQL.isConnected()) {
                    this.zzQP.zzQL.zzbe("Connected to service after a timeout");
                    this.zzQP.zzQL.zza(this.zzQO);
                }
            }
        }

        /* renamed from: com.google.android.gms.analytics.internal.zzi.zza.2 */
        class C06632 implements Runnable {
            final /* synthetic */ zza zzQP;
            final /* synthetic */ ComponentName zzQQ;

            C06632(zza com_google_android_gms_analytics_internal_zzi_zza, ComponentName componentName) {
                this.zzQP = com_google_android_gms_analytics_internal_zzi_zza;
                this.zzQQ = componentName;
            }

            public void run() {
                this.zzQP.zzQL.onServiceDisconnected(this.zzQQ);
            }
        }

        protected zza(zzi com_google_android_gms_analytics_internal_zzi) {
            this.zzQL = com_google_android_gms_analytics_internal_zzi;
        }

        public void onServiceConnected(android.content.ComponentName r5, android.os.IBinder r6) {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.google.android.gms.analytics.internal.zzi.zza.onServiceConnected(android.content.ComponentName, android.os.IBinder):void. bs: [B:3:0x0008, B:9:0x0015]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:57)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r4 = this;
            r0 = "AnalyticsServiceConnection.onServiceConnected";
            com.google.android.gms.common.internal.zzx.zzcD(r0);
            monitor-enter(r4);
            if (r6 != 0) goto L_0x0014;
        L_0x0008:
            r0 = r4.zzQL;	 Catch:{ all -> 0x005a }
            r1 = "Service connected with null binder";	 Catch:{ all -> 0x005a }
            r0.zzbh(r1);	 Catch:{ all -> 0x005a }
            r4.notifyAll();	 Catch:{ all -> 0x0046 }
            monitor-exit(r4);	 Catch:{ all -> 0x0046 }
        L_0x0013:
            return;
        L_0x0014:
            r0 = 0;
            r1 = r6.getInterfaceDescriptor();	 Catch:{ RemoteException -> 0x0051 }
            r2 = "com.google.android.gms.analytics.internal.IAnalyticsService";	 Catch:{ RemoteException -> 0x0051 }
            r2 = r2.equals(r1);	 Catch:{ RemoteException -> 0x0051 }
            if (r2 == 0) goto L_0x0049;	 Catch:{ RemoteException -> 0x0051 }
        L_0x0021:
            r0 = com.google.android.gms.analytics.internal.zzac.zza.zzaf(r6);	 Catch:{ RemoteException -> 0x0051 }
            r1 = r4.zzQL;	 Catch:{ RemoteException -> 0x0051 }
            r2 = "Bound to IAnalyticsService interface";	 Catch:{ RemoteException -> 0x0051 }
            r1.zzbd(r2);	 Catch:{ RemoteException -> 0x0051 }
        L_0x002c:
            if (r0 != 0) goto L_0x005f;
        L_0x002e:
            r0 = com.google.android.gms.common.stats.zzb.zzrP();	 Catch:{ IllegalArgumentException -> 0x007c }
            r1 = r4.zzQL;	 Catch:{ IllegalArgumentException -> 0x007c }
            r1 = r1.getContext();	 Catch:{ IllegalArgumentException -> 0x007c }
            r2 = r4.zzQL;	 Catch:{ IllegalArgumentException -> 0x007c }
            r2 = r2.zzQH;	 Catch:{ IllegalArgumentException -> 0x007c }
            r0.zza(r1, r2);	 Catch:{ IllegalArgumentException -> 0x007c }
        L_0x0041:
            r4.notifyAll();	 Catch:{ all -> 0x0046 }
            monitor-exit(r4);	 Catch:{ all -> 0x0046 }
            goto L_0x0013;	 Catch:{ all -> 0x0046 }
        L_0x0046:
            r0 = move-exception;	 Catch:{ all -> 0x0046 }
            monitor-exit(r4);	 Catch:{ all -> 0x0046 }
            throw r0;
        L_0x0049:
            r2 = r4.zzQL;	 Catch:{ RemoteException -> 0x0051 }
            r3 = "Got binder with a wrong descriptor";	 Catch:{ RemoteException -> 0x0051 }
            r2.zze(r3, r1);	 Catch:{ RemoteException -> 0x0051 }
            goto L_0x002c;
        L_0x0051:
            r1 = move-exception;
            r1 = r4.zzQL;	 Catch:{ all -> 0x005a }
            r2 = "Service connect failed to get IAnalyticsService";	 Catch:{ all -> 0x005a }
            r1.zzbh(r2);	 Catch:{ all -> 0x005a }
            goto L_0x002c;
        L_0x005a:
            r0 = move-exception;
            r4.notifyAll();	 Catch:{ all -> 0x0046 }
            throw r0;	 Catch:{ all -> 0x0046 }
        L_0x005f:
            r1 = r4.zzQN;	 Catch:{ all -> 0x005a }
            if (r1 != 0) goto L_0x0079;	 Catch:{ all -> 0x005a }
        L_0x0063:
            r1 = r4.zzQL;	 Catch:{ all -> 0x005a }
            r2 = "onServiceConnected received after the timeout limit";	 Catch:{ all -> 0x005a }
            r1.zzbg(r2);	 Catch:{ all -> 0x005a }
            r1 = r4.zzQL;	 Catch:{ all -> 0x005a }
            r1 = r1.zzjo();	 Catch:{ all -> 0x005a }
            r2 = new com.google.android.gms.analytics.internal.zzi$zza$1;	 Catch:{ all -> 0x005a }
            r2.<init>(r4, r0);	 Catch:{ all -> 0x005a }
            r1.zzf(r2);	 Catch:{ all -> 0x005a }
            goto L_0x0041;	 Catch:{ all -> 0x005a }
        L_0x0079:
            r4.zzQM = r0;	 Catch:{ all -> 0x005a }
            goto L_0x0041;
        L_0x007c:
            r0 = move-exception;
            goto L_0x0041;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzi.zza.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        public void onServiceDisconnected(ComponentName name) {
            zzx.zzcD("AnalyticsServiceConnection.onServiceDisconnected");
            this.zzQL.zzjo().zzf(new C06632(this, name));
        }

        public zzac zzjK() {
            zzac com_google_android_gms_analytics_internal_zzac = null;
            this.zzQL.zzjk();
            Intent intent = new Intent("com.google.android.gms.analytics.service.START");
            intent.setComponent(new ComponentName(AdvertisingInfoServiceStrategy.GOOGLE_PLAY_SERVICES_INTENT_PACKAGE_NAME, "com.google.android.gms.analytics.service.AnalyticsService"));
            Context context = this.zzQL.getContext();
            intent.putExtra("app_package_name", context.getPackageName());
            zzb zzrP = zzb.zzrP();
            synchronized (this) {
                this.zzQM = null;
                this.zzQN = true;
                boolean zza = zzrP.zza(context, intent, this.zzQL.zzQH, (int) Service.PWDGEN);
                this.zzQL.zza("Bind to service requested", Boolean.valueOf(zza));
                if (zza) {
                    try {
                        wait(this.zzQL.zzjn().zzkN());
                    } catch (InterruptedException e) {
                        this.zzQL.zzbg("Wait for service connect was interrupted");
                    }
                    this.zzQN = false;
                    com_google_android_gms_analytics_internal_zzac = this.zzQM;
                    this.zzQM = null;
                    if (com_google_android_gms_analytics_internal_zzac == null) {
                        this.zzQL.zzbh("Successfully bound to service but never got onServiceConnected callback");
                    }
                } else {
                    this.zzQN = false;
                }
            }
            return com_google_android_gms_analytics_internal_zzac;
        }
    }

    protected zzi(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.zzQK = new zzaj(com_google_android_gms_analytics_internal_zzf.zzjl());
        this.zzQH = new zza(this);
        this.zzQJ = new C06611(this, com_google_android_gms_analytics_internal_zzf);
    }

    private void onDisconnect() {
        zziH().zzjf();
    }

    private void onServiceDisconnected(ComponentName name) {
        zzjk();
        if (this.zzQI != null) {
            this.zzQI = null;
            zza("Disconnected from device AnalyticsService", name);
            onDisconnect();
        }
    }

    private void zza(zzac com_google_android_gms_analytics_internal_zzac) {
        zzjk();
        this.zzQI = com_google_android_gms_analytics_internal_zzac;
        zzjI();
        zziH().onServiceConnected();
    }

    private void zzjI() {
        this.zzQK.start();
        this.zzQJ.zzt(zzjn().zzkM());
    }

    private void zzjJ() {
        zzjk();
        if (isConnected()) {
            zzbd("Inactivity, disconnecting from device AnalyticsService");
            disconnect();
        }
    }

    public boolean connect() {
        zzjk();
        zzjv();
        if (this.zzQI != null) {
            return true;
        }
        zzac zzjK = this.zzQH.zzjK();
        if (zzjK == null) {
            return false;
        }
        this.zzQI = zzjK;
        zzjI();
        return true;
    }

    public void disconnect() {
        zzjk();
        zzjv();
        try {
            zzb.zzrP().zza(getContext(), this.zzQH);
        } catch (IllegalStateException e) {
        } catch (IllegalArgumentException e2) {
        }
        if (this.zzQI != null) {
            this.zzQI = null;
            onDisconnect();
        }
    }

    public boolean isConnected() {
        zzjk();
        zzjv();
        return this.zzQI != null;
    }

    public boolean zzb(zzab com_google_android_gms_analytics_internal_zzab) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzab);
        zzjk();
        zzjv();
        zzac com_google_android_gms_analytics_internal_zzac = this.zzQI;
        if (com_google_android_gms_analytics_internal_zzac == null) {
            return false;
        }
        try {
            com_google_android_gms_analytics_internal_zzac.zza(com_google_android_gms_analytics_internal_zzab.zzn(), com_google_android_gms_analytics_internal_zzab.zzlr(), com_google_android_gms_analytics_internal_zzab.zzlt() ? zzjn().zzkF() : zzjn().zzkG(), Collections.emptyList());
            zzjI();
            return true;
        } catch (RemoteException e) {
            zzbd("Failed to send hits to AnalyticsService");
            return false;
        }
    }

    protected void zziJ() {
    }

    public boolean zzjH() {
        zzjk();
        zzjv();
        zzac com_google_android_gms_analytics_internal_zzac = this.zzQI;
        if (com_google_android_gms_analytics_internal_zzac == null) {
            return false;
        }
        try {
            com_google_android_gms_analytics_internal_zzac.zzjc();
            zzjI();
            return true;
        } catch (RemoteException e) {
            zzbd("Failed to clear hits from AnalyticsService");
            return false;
        }
    }
}
