package com.google.android.gms.ads.internal.client;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.client.zzb.zza;
import com.google.android.gms.ads.internal.reward.client.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;

public class zzal extends zza {
    private zzd zzvb;

    /* renamed from: com.google.android.gms.ads.internal.client.zzal.1 */
    class C05951 implements Runnable {
        final /* synthetic */ zzal zzvc;

        C05951(zzal com_google_android_gms_ads_internal_client_zzal) {
            this.zzvc = com_google_android_gms_ads_internal_client_zzal;
        }

        public void run() {
            if (this.zzvc.zzvb != null) {
                try {
                    this.zzvc.zzvb.onRewardedVideoAdFailedToLoad(1);
                } catch (Throwable e) {
                    zzb.zzd("Could not notify onRewardedVideoAdFailedToLoad event.", e);
                }
            }
        }
    }

    public void destroy() throws RemoteException {
    }

    public boolean isLoaded() throws RemoteException {
        return false;
    }

    public void pause() throws RemoteException {
    }

    public void resume() throws RemoteException {
    }

    public void setUserId(String userId) throws RemoteException {
    }

    public void show() throws RemoteException {
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) throws RemoteException {
        zzb.m1672e("This app is using a lightweight version of the Google Mobile Ads SDK that requires the latest Google Play services to be installed, but Google Play services is either missing or out of date.");
        com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new C05951(this));
    }

    public void zza(zzd com_google_android_gms_ads_internal_reward_client_zzd) throws RemoteException {
        this.zzvb = com_google_android_gms_ads_internal_reward_client_zzd;
    }
}
