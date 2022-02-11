package com.google.android.gms.analytics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzaf;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzrp;

public class CampaignTrackingService extends Service {
    private static Boolean zzOO;
    private Handler mHandler;

    /* renamed from: com.google.android.gms.analytics.CampaignTrackingService.1 */
    class C06471 implements Runnable {
        final /* synthetic */ int zzOP;
        final /* synthetic */ zzaf zzOR;
        final /* synthetic */ CampaignTrackingService zzOZ;
        final /* synthetic */ Handler zzt;

        C06471(CampaignTrackingService campaignTrackingService, zzaf com_google_android_gms_analytics_internal_zzaf, Handler handler, int i) {
            this.zzOZ = campaignTrackingService;
            this.zzOR = com_google_android_gms_analytics_internal_zzaf;
            this.zzt = handler;
            this.zzOP = i;
        }

        public void run() {
            this.zzOZ.zza(this.zzOR, this.zzt, this.zzOP);
        }
    }

    /* renamed from: com.google.android.gms.analytics.CampaignTrackingService.2 */
    class C06482 implements Runnable {
        final /* synthetic */ int zzOP;
        final /* synthetic */ zzaf zzOR;
        final /* synthetic */ CampaignTrackingService zzOZ;
        final /* synthetic */ Handler zzt;

        C06482(CampaignTrackingService campaignTrackingService, zzaf com_google_android_gms_analytics_internal_zzaf, Handler handler, int i) {
            this.zzOZ = campaignTrackingService;
            this.zzOR = com_google_android_gms_analytics_internal_zzaf;
            this.zzt = handler;
            this.zzOP = i;
        }

        public void run() {
            this.zzOZ.zza(this.zzOR, this.zzt, this.zzOP);
        }
    }

    /* renamed from: com.google.android.gms.analytics.CampaignTrackingService.3 */
    class C06493 implements Runnable {
        final /* synthetic */ int zzOP;
        final /* synthetic */ zzaf zzOR;
        final /* synthetic */ CampaignTrackingService zzOZ;

        C06493(CampaignTrackingService campaignTrackingService, int i, zzaf com_google_android_gms_analytics_internal_zzaf) {
            this.zzOZ = campaignTrackingService;
            this.zzOP = i;
            this.zzOR = com_google_android_gms_analytics_internal_zzaf;
        }

        public void run() {
            boolean stopSelfResult = this.zzOZ.stopSelfResult(this.zzOP);
            if (stopSelfResult) {
                this.zzOR.zza("Install campaign broadcast processed", Boolean.valueOf(stopSelfResult));
            }
        }
    }

    private Handler getHandler() {
        Handler handler = this.mHandler;
        if (handler != null) {
            return handler;
        }
        handler = new Handler(getMainLooper());
        this.mHandler = handler;
        return handler;
    }

    public static boolean zzZ(Context context) {
        zzx.zzz(context);
        if (zzOO != null) {
            return zzOO.booleanValue();
        }
        boolean zza = zzam.zza(context, CampaignTrackingService.class);
        zzOO = Boolean.valueOf(zza);
        return zza;
    }

    private void zziz() {
        try {
            synchronized (CampaignTrackingReceiver.zzqy) {
                zzrp com_google_android_gms_internal_zzrp = CampaignTrackingReceiver.zzOM;
                if (com_google_android_gms_internal_zzrp != null && com_google_android_gms_internal_zzrp.isHeld()) {
                    com_google_android_gms_internal_zzrp.release();
                }
            }
        } catch (SecurityException e) {
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public void onCreate() {
        super.onCreate();
        zzf.zzaa(this).zzjm().zzbd("CampaignTrackingService is starting up");
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public void onDestroy() {
        zzf.zzaa(this).zzjm().zzbd("CampaignTrackingService is shutting down");
        super.onDestroy();
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public int onStartCommand(Intent intent, int flags, int startId) {
        zziz();
        zzf zzaa = zzf.zzaa(this);
        zzaf zzjm = zzaa.zzjm();
        String str = null;
        if (zzaa.zzjn().zzkr()) {
            zzjm.zzbh("Unexpected installation campaign (package side)");
        } else {
            str = intent.getStringExtra("referrer");
        }
        Handler handler = getHandler();
        if (TextUtils.isEmpty(str)) {
            if (!zzaa.zzjn().zzkr()) {
                zzjm.zzbg("No campaign found on com.android.vending.INSTALL_REFERRER \"referrer\" extra");
            }
            zzaa.zzjo().zzf(new C06471(this, zzjm, handler, startId));
        } else {
            int zzkv = zzaa.zzjn().zzkv();
            if (str.length() > zzkv) {
                zzjm.zzc("Campaign data exceed the maximum supported size and will be clipped. size, limit", Integer.valueOf(str.length()), Integer.valueOf(zzkv));
                str = str.substring(0, zzkv);
            }
            zzjm.zza("CampaignTrackingService called. startId, campaign", Integer.valueOf(startId), str);
            zzaa.zziH().zza(str, new C06482(this, zzjm, handler, startId));
        }
        return 2;
    }

    protected void zza(zzaf com_google_android_gms_analytics_internal_zzaf, Handler handler, int i) {
        handler.post(new C06493(this, i, com_google_android_gms_analytics_internal_zzaf));
    }
}
