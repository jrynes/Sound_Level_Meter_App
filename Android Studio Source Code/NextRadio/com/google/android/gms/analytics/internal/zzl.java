package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.analytics.AnalyticsService;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.CampaignTrackingService;
import com.google.android.gms.analytics.zza;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzkd;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzpq;
import com.google.android.gms.internal.zzpr;
import com.google.android.gms.measurement.zzc;
import com.google.android.gms.measurement.zzg;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp.Headers;

class zzl extends zzd {
    private boolean mStarted;
    private final zzj zzQY;
    private final zzah zzQZ;
    private final zzag zzRa;
    private final zzi zzRb;
    private long zzRc;
    private final zzt zzRd;
    private final zzt zzRe;
    private final zzaj zzRf;
    private long zzRg;
    private boolean zzRh;

    /* renamed from: com.google.android.gms.analytics.internal.zzl.1 */
    class C06641 extends zzt {
        final /* synthetic */ zzl zzRi;

        C06641(zzl com_google_android_gms_analytics_internal_zzl, zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzRi = com_google_android_gms_analytics_internal_zzl;
            super(com_google_android_gms_analytics_internal_zzf);
        }

        public void run() {
            this.zzRi.zzjV();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzl.2 */
    class C06652 extends zzt {
        final /* synthetic */ zzl zzRi;

        C06652(zzl com_google_android_gms_analytics_internal_zzl, zzf com_google_android_gms_analytics_internal_zzf) {
            this.zzRi = com_google_android_gms_analytics_internal_zzl;
            super(com_google_android_gms_analytics_internal_zzf);
        }

        public void run() {
            this.zzRi.zzjW();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzl.3 */
    class C06663 implements Runnable {
        final /* synthetic */ zzl zzRi;

        C06663(zzl com_google_android_gms_analytics_internal_zzl) {
            this.zzRi = com_google_android_gms_analytics_internal_zzl;
        }

        public void run() {
            this.zzRi.zzjU();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzl.4 */
    class C06674 implements zzw {
        final /* synthetic */ zzl zzRi;

        C06674(zzl com_google_android_gms_analytics_internal_zzl) {
            this.zzRi = com_google_android_gms_analytics_internal_zzl;
        }

        public void zzc(Throwable th) {
            this.zzRi.zzkb();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzl.5 */
    class C06685 implements Runnable {
        final /* synthetic */ zzw zzQi;
        final /* synthetic */ zzl zzRi;
        final /* synthetic */ long zzRj;

        C06685(zzl com_google_android_gms_analytics_internal_zzl, zzw com_google_android_gms_analytics_internal_zzw, long j) {
            this.zzRi = com_google_android_gms_analytics_internal_zzl;
            this.zzQi = com_google_android_gms_analytics_internal_zzw;
            this.zzRj = j;
        }

        public void run() {
            this.zzRi.zza(this.zzQi, this.zzRj);
        }
    }

    protected zzl(zzf com_google_android_gms_analytics_internal_zzf, zzg com_google_android_gms_analytics_internal_zzg) {
        super(com_google_android_gms_analytics_internal_zzf);
        zzx.zzz(com_google_android_gms_analytics_internal_zzg);
        this.zzRc = Long.MIN_VALUE;
        this.zzRa = com_google_android_gms_analytics_internal_zzg.zzk(com_google_android_gms_analytics_internal_zzf);
        this.zzQY = com_google_android_gms_analytics_internal_zzg.zzm(com_google_android_gms_analytics_internal_zzf);
        this.zzQZ = com_google_android_gms_analytics_internal_zzg.zzn(com_google_android_gms_analytics_internal_zzf);
        this.zzRb = com_google_android_gms_analytics_internal_zzg.zzo(com_google_android_gms_analytics_internal_zzf);
        this.zzRf = new zzaj(zzjl());
        this.zzRd = new C06641(this, com_google_android_gms_analytics_internal_zzf);
        this.zzRe = new C06652(this, com_google_android_gms_analytics_internal_zzf);
    }

    private void zza(zzh com_google_android_gms_analytics_internal_zzh, zzpr com_google_android_gms_internal_zzpr) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzh);
        zzx.zzz(com_google_android_gms_internal_zzpr);
        zza com_google_android_gms_analytics_zza = new zza(zzji());
        com_google_android_gms_analytics_zza.zzaS(com_google_android_gms_analytics_internal_zzh.zzjE());
        com_google_android_gms_analytics_zza.enableAdvertisingIdCollection(com_google_android_gms_analytics_internal_zzh.zzjF());
        zzc zziy = com_google_android_gms_analytics_zza.zziy();
        zzke com_google_android_gms_internal_zzke = (zzke) zziy.zzf(zzke.class);
        com_google_android_gms_internal_zzke.zzaX(MPDbAdapter.KEY_DATA);
        com_google_android_gms_internal_zzke.zzI(true);
        zziy.zzb(com_google_android_gms_internal_zzpr);
        zzkd com_google_android_gms_internal_zzkd = (zzkd) zziy.zzf(zzkd.class);
        zzpq com_google_android_gms_internal_zzpq = (zzpq) zziy.zzf(zzpq.class);
        for (Entry entry : com_google_android_gms_analytics_internal_zzh.zzn().entrySet()) {
            String str = (String) entry.getKey();
            String str2 = (String) entry.getValue();
            if ("an".equals(str)) {
                com_google_android_gms_internal_zzpq.setAppName(str2);
            } else if ("av".equals(str)) {
                com_google_android_gms_internal_zzpq.setAppVersion(str2);
            } else if ("aid".equals(str)) {
                com_google_android_gms_internal_zzpq.setAppId(str2);
            } else if ("aiid".equals(str)) {
                com_google_android_gms_internal_zzpq.setAppInstallerId(str2);
            } else if ("uid".equals(str)) {
                com_google_android_gms_internal_zzke.setUserId(str2);
            } else {
                com_google_android_gms_internal_zzkd.set(str, str2);
            }
        }
        zzb("Sending installation campaign to", com_google_android_gms_analytics_internal_zzh.zzjE(), com_google_android_gms_internal_zzpr);
        zziy.zzM(zzjq().zzlF());
        zziy.zzAy();
    }

    private boolean zzbk(String str) {
        return getContext().checkCallingOrSelfPermission(str) == 0;
    }

    private void zzjT() {
        Context context = zzji().getContext();
        if (!AnalyticsReceiver.zzY(context)) {
            zzbg("AnalyticsReceiver is not registered or is disabled. Register the receiver for reliable dispatching on non-Google Play devices. See http://goo.gl/8Rd3yj for instructions.");
        } else if (!AnalyticsService.zzZ(context)) {
            zzbh("AnalyticsService is not registered or is disabled. Analytics service at risk of not starting. See http://goo.gl/8Rd3yj for instructions.");
        }
        if (!CampaignTrackingReceiver.zzY(context)) {
            zzbg("CampaignTrackingReceiver is not registered, not exported or is disabled. Installation campaign tracking is not possible. See http://goo.gl/8Rd3yj for instructions.");
        } else if (!CampaignTrackingService.zzZ(context)) {
            zzbg("CampaignTrackingService is not registered or is disabled. Installation campaign tracking is not possible. See http://goo.gl/8Rd3yj for instructions.");
        }
    }

    private void zzjV() {
        zzb(new C06674(this));
    }

    private void zzjW() {
        try {
            this.zzQY.zzjN();
            zzkb();
        } catch (SQLiteException e) {
            zzd("Failed to delete stale hits", e);
        }
        this.zzRe.zzt(zzjn().zzkT());
    }

    private boolean zzkc() {
        return this.zzRh ? false : (!zzjn().zzkr() || zzjn().zzks()) && zzki() > 0;
    }

    private void zzkd() {
        zzv zzjp = zzjp();
        if (zzjp.zzlb() && !zzjp.zzbw()) {
            long zzjO = zzjO();
            if (zzjO != 0 && Math.abs(zzjl().currentTimeMillis() - zzjO) <= zzjn().zzkB()) {
                zza("Dispatch alarm scheduled (ms)", Long.valueOf(zzjn().zzkA()));
                zzjp.zzlc();
            }
        }
    }

    private void zzke() {
        zzkd();
        long zzki = zzki();
        long zzlH = zzjq().zzlH();
        if (zzlH != 0) {
            zzlH = zzki - Math.abs(zzjl().currentTimeMillis() - zzlH);
            if (zzlH <= 0) {
                zzlH = Math.min(zzjn().zzky(), zzki);
            }
        } else {
            zzlH = Math.min(zzjn().zzky(), zzki);
        }
        zza("Dispatch scheduled (ms)", Long.valueOf(zzlH));
        if (this.zzRd.zzbw()) {
            this.zzRd.zzu(Math.max(1, zzlH + this.zzRd.zzkY()));
            return;
        }
        this.zzRd.zzt(zzlH);
    }

    private void zzkf() {
        zzkg();
        zzkh();
    }

    private void zzkg() {
        if (this.zzRd.zzbw()) {
            zzbd("All hits dispatched or no network/service. Going to power save mode");
        }
        this.zzRd.cancel();
    }

    private void zzkh() {
        zzv zzjp = zzjp();
        if (zzjp.zzbw()) {
            zzjp.cancel();
        }
    }

    protected void onServiceConnected() {
        zzjk();
        if (!zzjn().zzkr()) {
            zzjY();
        }
    }

    void start() {
        zzjv();
        zzx.zza(!this.mStarted, (Object) "Analytics backend already started");
        this.mStarted = true;
        if (!zzjn().zzkr()) {
            zzjT();
        }
        zzjo().zzf(new C06663(this));
    }

    public void zzJ(boolean z) {
        zzkb();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long zza(com.google.android.gms.analytics.internal.zzh r6, boolean r7) {
        /*
        r5 = this;
        com.google.android.gms.common.internal.zzx.zzz(r6);
        r5.zzjv();
        r5.zzjk();
        r0 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0049 }
        r0.beginTransaction();	 Catch:{ SQLiteException -> 0x0049 }
        r0 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0049 }
        r2 = r6.zzjD();	 Catch:{ SQLiteException -> 0x0049 }
        r1 = r6.getClientId();	 Catch:{ SQLiteException -> 0x0049 }
        r0.zza(r2, r1);	 Catch:{ SQLiteException -> 0x0049 }
        r0 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0049 }
        r2 = r6.zzjD();	 Catch:{ SQLiteException -> 0x0049 }
        r1 = r6.getClientId();	 Catch:{ SQLiteException -> 0x0049 }
        r4 = r6.zzjE();	 Catch:{ SQLiteException -> 0x0049 }
        r0 = r0.zza(r2, r1, r4);	 Catch:{ SQLiteException -> 0x0049 }
        if (r7 != 0) goto L_0x0042;
    L_0x002f:
        r6.zzn(r0);	 Catch:{ SQLiteException -> 0x0049 }
    L_0x0032:
        r2 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0049 }
        r2.zzb(r6);	 Catch:{ SQLiteException -> 0x0049 }
        r2 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0049 }
        r2.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x0049 }
        r2 = r5.zzQY;	 Catch:{ SQLiteException -> 0x005e }
        r2.endTransaction();	 Catch:{ SQLiteException -> 0x005e }
    L_0x0041:
        return r0;
    L_0x0042:
        r2 = 1;
        r2 = r2 + r0;
        r6.zzn(r2);	 Catch:{ SQLiteException -> 0x0049 }
        goto L_0x0032;
    L_0x0049:
        r0 = move-exception;
        r1 = "Failed to update Analytics property";
        r5.zze(r1, r0);	 Catch:{ all -> 0x0065 }
        r0 = -1;
        r2 = r5.zzQY;	 Catch:{ SQLiteException -> 0x0057 }
        r2.endTransaction();	 Catch:{ SQLiteException -> 0x0057 }
        goto L_0x0041;
    L_0x0057:
        r2 = move-exception;
        r3 = "Failed to end transaction";
        r5.zze(r3, r2);
        goto L_0x0041;
    L_0x005e:
        r2 = move-exception;
        r3 = "Failed to end transaction";
        r5.zze(r3, r2);
        goto L_0x0041;
    L_0x0065:
        r0 = move-exception;
        r1 = r5.zzQY;	 Catch:{ SQLiteException -> 0x006c }
        r1.endTransaction();	 Catch:{ SQLiteException -> 0x006c }
    L_0x006b:
        throw r0;
    L_0x006c:
        r1 = move-exception;
        r2 = "Failed to end transaction";
        r5.zze(r2, r1);
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzl.zza(com.google.android.gms.analytics.internal.zzh, boolean):long");
    }

    public void zza(zzab com_google_android_gms_analytics_internal_zzab) {
        zzx.zzz(com_google_android_gms_analytics_internal_zzab);
        zzg.zzjk();
        zzjv();
        if (this.zzRh) {
            zzbe("Hit delivery not possible. Missing network permissions. See http://goo.gl/8Rd3yj for instructions");
        } else {
            zza("Delivering hit", com_google_android_gms_analytics_internal_zzab);
        }
        zzab zzf = zzf(com_google_android_gms_analytics_internal_zzab);
        zzjX();
        if (this.zzRb.zzb(zzf)) {
            zzbe("Hit sent to the device AnalyticsService for delivery");
        } else if (zzjn().zzkr()) {
            zzjm().zza(zzf, "Service unavailable on package side");
        } else {
            try {
                this.zzQY.zzc(zzf);
                zzkb();
            } catch (SQLiteException e) {
                zze("Delivery failed to save hit to a database", e);
                zzjm().zza(zzf, "deliver: failed to insert hit to database");
            }
        }
    }

    public void zza(zzw com_google_android_gms_analytics_internal_zzw, long j) {
        zzg.zzjk();
        zzjv();
        long j2 = -1;
        long zzlH = zzjq().zzlH();
        if (zzlH != 0) {
            j2 = Math.abs(zzjl().currentTimeMillis() - zzlH);
        }
        zzb("Dispatching local hits. Elapsed time since last dispatch (ms)", Long.valueOf(j2));
        if (!zzjn().zzkr()) {
            zzjX();
        }
        try {
            if (zzjZ()) {
                zzjo().zzf(new C06685(this, com_google_android_gms_analytics_internal_zzw, j));
                return;
            }
            zzjq().zzlI();
            zzkb();
            if (com_google_android_gms_analytics_internal_zzw != null) {
                com_google_android_gms_analytics_internal_zzw.zzc(null);
            }
            if (this.zzRg != j) {
                this.zzRa.zzlA();
            }
        } catch (Throwable th) {
            zze("Local dispatch failed", th);
            zzjq().zzlI();
            zzkb();
            if (com_google_android_gms_analytics_internal_zzw != null) {
                com_google_android_gms_analytics_internal_zzw.zzc(th);
            }
        }
    }

    public void zzb(zzw com_google_android_gms_analytics_internal_zzw) {
        zza(com_google_android_gms_analytics_internal_zzw, this.zzRg);
    }

    public void zzbl(String str) {
        zzx.zzcM(str);
        zzjk();
        zzjj();
        zzpr zza = zzam.zza(zzjm(), str);
        if (zza == null) {
            zzd("Parsing failed. Ignoring invalid campaign data", str);
            return;
        }
        CharSequence zzlJ = zzjq().zzlJ();
        if (str.equals(zzlJ)) {
            zzbg("Ignoring duplicate install campaign");
        } else if (TextUtils.isEmpty(zzlJ)) {
            zzjq().zzbp(str);
            if (zzjq().zzlG().zzv(zzjn().zzkW())) {
                zzd("Campaign received too late, ignoring", zza);
                return;
            }
            zzb("Received installation campaign", zza);
            for (zzh zza2 : this.zzQY.zzr(0)) {
                zza(zza2, zza);
            }
        } else {
            zzd("Ignoring multiple install campaigns. original, new", zzlJ, str);
        }
    }

    protected void zzc(zzh com_google_android_gms_analytics_internal_zzh) {
        zzjk();
        zzb("Sending first hit to property", com_google_android_gms_analytics_internal_zzh.zzjE());
        if (!zzjq().zzlG().zzv(zzjn().zzkW())) {
            String zzlJ = zzjq().zzlJ();
            if (!TextUtils.isEmpty(zzlJ)) {
                zzpr zza = zzam.zza(zzjm(), zzlJ);
                zzb("Found relevant installation campaign", zza);
                zza(com_google_android_gms_analytics_internal_zzh, zza);
            }
        }
    }

    zzab zzf(zzab com_google_android_gms_analytics_internal_zzab) {
        if (!TextUtils.isEmpty(com_google_android_gms_analytics_internal_zzab.zzlv())) {
            return com_google_android_gms_analytics_internal_zzab;
        }
        Pair zzlN = zzjq().zzlK().zzlN();
        if (zzlN == null) {
            return com_google_android_gms_analytics_internal_zzab;
        }
        Long l = (Long) zzlN.second;
        String str = l + Headers.SEPERATOR + ((String) zzlN.first);
        Map hashMap = new HashMap(com_google_android_gms_analytics_internal_zzab.zzn());
        hashMap.put("_m", str);
        return zzab.zza(this, com_google_android_gms_analytics_internal_zzab, hashMap);
    }

    protected void zziJ() {
        this.zzQY.zza();
        this.zzQZ.zza();
        this.zzRb.zza();
    }

    public long zzjO() {
        zzg.zzjk();
        zzjv();
        try {
            return this.zzQY.zzjO();
        } catch (SQLiteException e) {
            zze("Failed to get min/max hit times from local store", e);
            return 0;
        }
    }

    protected void zzjU() {
        zzjv();
        zzjq().zzlF();
        if (!zzbk("android.permission.ACCESS_NETWORK_STATE")) {
            zzbh("Missing required android.permission.ACCESS_NETWORK_STATE. Google Analytics disabled. See http://goo.gl/8Rd3yj for instructions");
            zzkj();
        }
        if (!zzbk("android.permission.INTERNET")) {
            zzbh("Missing required android.permission.INTERNET. Google Analytics disabled. See http://goo.gl/8Rd3yj for instructions");
            zzkj();
        }
        if (AnalyticsService.zzZ(getContext())) {
            zzbd("AnalyticsService registered in the app manifest and enabled");
        } else if (zzjn().zzkr()) {
            zzbh("Device AnalyticsService not registered! Hits will not be delivered reliably.");
        } else {
            zzbg("AnalyticsService not registered in the app manifest. Hits might not be delivered reliably. See http://goo.gl/8Rd3yj for instructions.");
        }
        if (!(this.zzRh || zzjn().zzkr() || this.zzQY.isEmpty())) {
            zzjX();
        }
        zzkb();
    }

    protected void zzjX() {
        if (!this.zzRh && zzjn().zzkt() && !this.zzRb.isConnected()) {
            if (this.zzRf.zzv(zzjn().zzkO())) {
                this.zzRf.start();
                zzbd("Connecting to service");
                if (this.zzRb.connect()) {
                    zzbd("Connected to service");
                    this.zzRf.clear();
                    onServiceConnected();
                }
            }
        }
    }

    public void zzjY() {
        zzg.zzjk();
        zzjv();
        zzjj();
        if (!zzjn().zzkt()) {
            zzbg("Service client disabled. Can't dispatch local hits to device AnalyticsService");
        }
        if (!this.zzRb.isConnected()) {
            zzbd("Service not connected");
        } else if (!this.zzQY.isEmpty()) {
            zzbd("Dispatching local hits to device AnalyticsService");
            while (true) {
                try {
                    break;
                    List zzp = this.zzQY.zzp((long) zzjn().zzkC());
                    if (zzp.isEmpty()) {
                        zzab com_google_android_gms_analytics_internal_zzab;
                        while (true) {
                            if (!zzp.isEmpty()) {
                                com_google_android_gms_analytics_internal_zzab = (zzab) zzp.get(0);
                                if (this.zzRb.zzb(com_google_android_gms_analytics_internal_zzab)) {
                                    zzkb();
                                    return;
                                }
                                zzp.remove(com_google_android_gms_analytics_internal_zzab);
                                try {
                                    this.zzQY.zzq(com_google_android_gms_analytics_internal_zzab.zzlq());
                                } catch (SQLiteException e) {
                                    zze("Failed to remove hit that was send for delivery", e);
                                    zzkf();
                                    return;
                                }
                            }
                        }
                        List zzp2 = this.zzQY.zzp((long) zzjn().zzkC());
                        if (zzp2.isEmpty()) {
                            while (true) {
                                if (zzp2.isEmpty()) {
                                    com_google_android_gms_analytics_internal_zzab = (zzab) zzp2.get(0);
                                    if (this.zzRb.zzb(com_google_android_gms_analytics_internal_zzab)) {
                                        zzp2.remove(com_google_android_gms_analytics_internal_zzab);
                                        this.zzQY.zzq(com_google_android_gms_analytics_internal_zzab.zzlq());
                                    } else {
                                        zzkb();
                                        return;
                                    }
                                }
                            }
                        }
                        zzkb();
                        return;
                    }
                    zzkb();
                    return;
                } catch (SQLiteException e2) {
                    zze("Failed to read hits from store", e2);
                    zzkf();
                    return;
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean zzjZ() {
        /*
        r12 = this;
        r1 = 1;
        r2 = 0;
        com.google.android.gms.measurement.zzg.zzjk();
        r12.zzjv();
        r0 = "Dispatching a batch of local hits";
        r12.zzbd(r0);
        r0 = r12.zzRb;
        r0 = r0.isConnected();
        if (r0 != 0) goto L_0x0032;
    L_0x0015:
        r0 = r12.zzjn();
        r0 = r0.zzkr();
        if (r0 != 0) goto L_0x0032;
    L_0x001f:
        r0 = r1;
    L_0x0020:
        r3 = r12.zzQZ;
        r3 = r3.zzlB();
        if (r3 != 0) goto L_0x0034;
    L_0x0028:
        if (r0 == 0) goto L_0x0036;
    L_0x002a:
        if (r1 == 0) goto L_0x0036;
    L_0x002c:
        r0 = "No network or service available. Will retry later";
        r12.zzbd(r0);
    L_0x0031:
        return r2;
    L_0x0032:
        r0 = r2;
        goto L_0x0020;
    L_0x0034:
        r1 = r2;
        goto L_0x0028;
    L_0x0036:
        r0 = r12.zzjn();
        r0 = r0.zzkC();
        r1 = r12.zzjn();
        r1 = r1.zzkD();
        r0 = java.lang.Math.max(r0, r1);
        r6 = (long) r0;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = 0;
    L_0x0052:
        r0 = r12.zzQY;	 Catch:{ all -> 0x01ee }
        r0.beginTransaction();	 Catch:{ all -> 0x01ee }
        r3.clear();	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x00d3 }
        r8 = r0.zzp(r6);	 Catch:{ SQLiteException -> 0x00d3 }
        r0 = r8.isEmpty();	 Catch:{ SQLiteException -> 0x00d3 }
        if (r0 == 0) goto L_0x0083;
    L_0x0066:
        r0 = "Store is empty, nothing to dispatch";
        r12.zzbd(r0);	 Catch:{ SQLiteException -> 0x00d3 }
        r12.zzkf();	 Catch:{ SQLiteException -> 0x00d3 }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x0079 }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x0079 }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x0079 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x0079 }
        goto L_0x0031;
    L_0x0079:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x0083:
        r0 = "Hits loaded from store. count";
        r1 = r8.size();	 Catch:{ SQLiteException -> 0x00d3 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ SQLiteException -> 0x00d3 }
        r12.zza(r0, r1);	 Catch:{ SQLiteException -> 0x00d3 }
        r1 = r8.iterator();	 Catch:{ all -> 0x01ee }
    L_0x0094:
        r0 = r1.hasNext();	 Catch:{ all -> 0x01ee }
        if (r0 == 0) goto L_0x00f3;
    L_0x009a:
        r0 = r1.next();	 Catch:{ all -> 0x01ee }
        r0 = (com.google.android.gms.analytics.internal.zzab) r0;	 Catch:{ all -> 0x01ee }
        r10 = r0.zzlq();	 Catch:{ all -> 0x01ee }
        r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x0094;
    L_0x00a8:
        r0 = "Database contains successfully uploaded hit";
        r1 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x01ee }
        r3 = r8.size();	 Catch:{ all -> 0x01ee }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x01ee }
        r12.zzd(r0, r1, r3);	 Catch:{ all -> 0x01ee }
        r12.zzkf();	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x00c8 }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x00c8 }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x00c8 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x00c8 }
        goto L_0x0031;
    L_0x00c8:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x00d3:
        r0 = move-exception;
        r1 = "Failed to read hits from persisted store";
        r12.zzd(r1, r0);	 Catch:{ all -> 0x01ee }
        r12.zzkf();	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x00e8 }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x00e8 }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x00e8 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x00e8 }
        goto L_0x0031;
    L_0x00e8:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x00f3:
        r0 = r12.zzRb;	 Catch:{ all -> 0x01ee }
        r0 = r0.isConnected();	 Catch:{ all -> 0x01ee }
        if (r0 == 0) goto L_0x0205;
    L_0x00fb:
        r0 = r12.zzjn();	 Catch:{ all -> 0x01ee }
        r0 = r0.zzkr();	 Catch:{ all -> 0x01ee }
        if (r0 != 0) goto L_0x0205;
    L_0x0105:
        r0 = "Service connected, sending hits to the service";
        r12.zzbd(r0);	 Catch:{ all -> 0x01ee }
    L_0x010a:
        r0 = r8.isEmpty();	 Catch:{ all -> 0x01ee }
        if (r0 != 0) goto L_0x0205;
    L_0x0110:
        r0 = 0;
        r0 = r8.get(r0);	 Catch:{ all -> 0x01ee }
        r0 = (com.google.android.gms.analytics.internal.zzab) r0;	 Catch:{ all -> 0x01ee }
        r1 = r12.zzRb;	 Catch:{ all -> 0x01ee }
        r1 = r1.zzb(r0);	 Catch:{ all -> 0x01ee }
        if (r1 != 0) goto L_0x0148;
    L_0x011f:
        r0 = r4;
    L_0x0120:
        r4 = r12.zzQZ;	 Catch:{ all -> 0x01ee }
        r4 = r4.zzlB();	 Catch:{ all -> 0x01ee }
        if (r4 == 0) goto L_0x0199;
    L_0x0128:
        r4 = r12.zzQZ;	 Catch:{ all -> 0x01ee }
        r9 = r4.zzq(r8);	 Catch:{ all -> 0x01ee }
        r10 = r9.iterator();	 Catch:{ all -> 0x01ee }
        r4 = r0;
    L_0x0133:
        r0 = r10.hasNext();	 Catch:{ all -> 0x01ee }
        if (r0 == 0) goto L_0x018d;
    L_0x0139:
        r0 = r10.next();	 Catch:{ all -> 0x01ee }
        r0 = (java.lang.Long) r0;	 Catch:{ all -> 0x01ee }
        r0 = r0.longValue();	 Catch:{ all -> 0x01ee }
        r4 = java.lang.Math.max(r4, r0);	 Catch:{ all -> 0x01ee }
        goto L_0x0133;
    L_0x0148:
        r10 = r0.zzlq();	 Catch:{ all -> 0x01ee }
        r4 = java.lang.Math.max(r4, r10);	 Catch:{ all -> 0x01ee }
        r8.remove(r0);	 Catch:{ all -> 0x01ee }
        r1 = "Hit sent do device AnalyticsService for delivery";
        r12.zzb(r1, r0);	 Catch:{ all -> 0x01ee }
        r1 = r12.zzQY;	 Catch:{ SQLiteException -> 0x016d }
        r10 = r0.zzlq();	 Catch:{ SQLiteException -> 0x016d }
        r1.zzq(r10);	 Catch:{ SQLiteException -> 0x016d }
        r0 = r0.zzlq();	 Catch:{ SQLiteException -> 0x016d }
        r0 = java.lang.Long.valueOf(r0);	 Catch:{ SQLiteException -> 0x016d }
        r3.add(r0);	 Catch:{ SQLiteException -> 0x016d }
        goto L_0x010a;
    L_0x016d:
        r0 = move-exception;
        r1 = "Failed to remove hit that was send for delivery";
        r12.zze(r1, r0);	 Catch:{ all -> 0x01ee }
        r12.zzkf();	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x0182 }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x0182 }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x0182 }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x0182 }
        goto L_0x0031;
    L_0x0182:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x018d:
        r8.removeAll(r9);	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01b6 }
        r0.zzo(r9);	 Catch:{ SQLiteException -> 0x01b6 }
        r3.addAll(r9);	 Catch:{ SQLiteException -> 0x01b6 }
        r0 = r4;
    L_0x0199:
        r4 = r3.isEmpty();	 Catch:{ all -> 0x01ee }
        if (r4 == 0) goto L_0x01d6;
    L_0x019f:
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01ab }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x01ab }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01ab }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x01ab }
        goto L_0x0031;
    L_0x01ab:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x01b6:
        r0 = move-exception;
        r1 = "Failed to remove successfully uploaded hits";
        r12.zze(r1, r0);	 Catch:{ all -> 0x01ee }
        r12.zzkf();	 Catch:{ all -> 0x01ee }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01cb }
        r0.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x01cb }
        r0 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01cb }
        r0.endTransaction();	 Catch:{ SQLiteException -> 0x01cb }
        goto L_0x0031;
    L_0x01cb:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x01d6:
        r4 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01e3 }
        r4.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x01e3 }
        r4 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01e3 }
        r4.endTransaction();	 Catch:{ SQLiteException -> 0x01e3 }
        r4 = r0;
        goto L_0x0052;
    L_0x01e3:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x01ee:
        r0 = move-exception;
        r1 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01fa }
        r1.setTransactionSuccessful();	 Catch:{ SQLiteException -> 0x01fa }
        r1 = r12.zzQY;	 Catch:{ SQLiteException -> 0x01fa }
        r1.endTransaction();	 Catch:{ SQLiteException -> 0x01fa }
        throw r0;
    L_0x01fa:
        r0 = move-exception;
        r1 = "Failed to commit local dispatch transaction";
        r12.zze(r1, r0);
        r12.zzkf();
        goto L_0x0031;
    L_0x0205:
        r0 = r4;
        goto L_0x0120;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzl.zzjZ():boolean");
    }

    public void zzjc() {
        zzg.zzjk();
        zzjv();
        if (!zzjn().zzkr()) {
            zzbd("Delete all hits from local store");
            try {
                this.zzQY.zzjL();
                this.zzQY.zzjM();
                zzkb();
            } catch (SQLiteException e) {
                zzd("Failed to delete hits from store", e);
            }
        }
        zzjX();
        if (this.zzRb.zzjH()) {
            zzbd("Device service unavailable. Can't clear hits stored on the device service.");
        }
    }

    public void zzjf() {
        zzg.zzjk();
        zzjv();
        zzbd("Service disconnected");
    }

    void zzjh() {
        zzjk();
        this.zzRg = zzjl().currentTimeMillis();
    }

    public void zzka() {
        zzg.zzjk();
        zzjv();
        zzbe("Sync dispatching local hits");
        long j = this.zzRg;
        if (!zzjn().zzkr()) {
            zzjX();
        }
        do {
            try {
            } catch (Throwable th) {
                zze("Sync local dispatch failed", th);
                zzkb();
                return;
            }
        } while (zzjZ());
        zzjq().zzlI();
        zzkb();
        if (this.zzRg != j) {
            this.zzRa.zzlA();
        }
    }

    public void zzkb() {
        zzji().zzjk();
        zzjv();
        if (!zzkc()) {
            this.zzRa.unregister();
            zzkf();
        } else if (this.zzQY.isEmpty()) {
            this.zzRa.unregister();
            zzkf();
        } else {
            boolean z;
            if (((Boolean) zzy.zzSs.get()).booleanValue()) {
                z = true;
            } else {
                this.zzRa.zzly();
                z = this.zzRa.isConnected();
            }
            if (z) {
                zzke();
                return;
            }
            zzkf();
            zzkd();
        }
    }

    public long zzki() {
        if (this.zzRc != Long.MIN_VALUE) {
            return this.zzRc;
        }
        return zziI().zzll() ? ((long) zziI().zzmc()) * 1000 : zzjn().zzkz();
    }

    public void zzkj() {
        zzjv();
        zzjk();
        this.zzRh = true;
        this.zzRb.disconnect();
        zzkb();
    }

    public void zzs(long j) {
        zzg.zzjk();
        zzjv();
        if (j < 0) {
            j = 0;
        }
        this.zzRc = j;
        zzkb();
    }
}
