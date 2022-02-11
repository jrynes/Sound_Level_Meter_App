package com.google.android.gms.internal;

import android.os.Handler;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;
import java.util.LinkedList;
import java.util.List;

@zzhb
class zzdw {
    private final List<zza> zzpH;

    interface zza {
        void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException;
    }

    /* renamed from: com.google.android.gms.internal.zzdw.1 */
    class C07791 extends com.google.android.gms.ads.internal.client.zzq.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.1.1 */
        class C07741 implements zza {
            final /* synthetic */ C07791 zzAd;

            C07741(C07791 c07791) {
                this.zzAd = c07791;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzpK != null) {
                    com_google_android_gms_internal_zzdx.zzpK.onAdClosed();
                }
                zzr.zzbN().zzee();
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.1.2 */
        class C07752 implements zza {
            final /* synthetic */ C07791 zzAd;
            final /* synthetic */ int zzAe;

            C07752(C07791 c07791, int i) {
                this.zzAd = c07791;
                this.zzAe = i;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzpK != null) {
                    com_google_android_gms_internal_zzdx.zzpK.onAdFailedToLoad(this.zzAe);
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.1.3 */
        class C07763 implements zza {
            final /* synthetic */ C07791 zzAd;

            C07763(C07791 c07791) {
                this.zzAd = c07791;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzpK != null) {
                    com_google_android_gms_internal_zzdx.zzpK.onAdLeftApplication();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.1.4 */
        class C07774 implements zza {
            final /* synthetic */ C07791 zzAd;

            C07774(C07791 c07791) {
                this.zzAd = c07791;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzpK != null) {
                    com_google_android_gms_internal_zzdx.zzpK.onAdLoaded();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.1.5 */
        class C07785 implements zza {
            final /* synthetic */ C07791 zzAd;

            C07785(C07791 c07791) {
                this.zzAd = c07791;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzpK != null) {
                    com_google_android_gms_internal_zzdx.zzpK.onAdOpened();
                }
            }
        }

        C07791(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void onAdClosed() throws RemoteException {
            this.zzAc.zzpH.add(new C07741(this));
        }

        public void onAdFailedToLoad(int errorCode) throws RemoteException {
            this.zzAc.zzpH.add(new C07752(this, errorCode));
            zzin.m1674v("Pooled interstitial failed to load.");
        }

        public void onAdLeftApplication() throws RemoteException {
            this.zzAc.zzpH.add(new C07763(this));
        }

        public void onAdLoaded() throws RemoteException {
            this.zzAc.zzpH.add(new C07774(this));
            zzin.m1674v("Pooled interstitial loaded.");
        }

        public void onAdOpened() throws RemoteException {
            this.zzAc.zzpH.add(new C07785(this));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.2 */
    class C07812 extends com.google.android.gms.ads.internal.client.zzw.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.2.1 */
        class C07801 implements zza {
            final /* synthetic */ String val$name;
            final /* synthetic */ String zzAf;
            final /* synthetic */ C07812 zzAg;

            C07801(C07812 c07812, String str, String str2) {
                this.zzAg = c07812;
                this.val$name = str;
                this.zzAf = str2;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAq != null) {
                    com_google_android_gms_internal_zzdx.zzAq.onAppEvent(this.val$name, this.zzAf);
                }
            }
        }

        C07812(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void onAppEvent(String name, String info) throws RemoteException {
            this.zzAc.zzpH.add(new C07801(this, name, info));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.3 */
    class C07833 extends com.google.android.gms.internal.zzgd.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.3.1 */
        class C07821 implements zza {
            final /* synthetic */ zzgc zzAh;
            final /* synthetic */ C07833 zzAi;

            C07821(C07833 c07833, zzgc com_google_android_gms_internal_zzgc) {
                this.zzAi = c07833;
                this.zzAh = com_google_android_gms_internal_zzgc;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAr != null) {
                    com_google_android_gms_internal_zzdx.zzAr.zza(this.zzAh);
                }
            }
        }

        C07833(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void zza(zzgc com_google_android_gms_internal_zzgc) throws RemoteException {
            this.zzAc.zzpH.add(new C07821(this, com_google_android_gms_internal_zzgc));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.4 */
    class C07854 extends com.google.android.gms.internal.zzcf.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.4.1 */
        class C07841 implements zza {
            final /* synthetic */ zzce zzAj;
            final /* synthetic */ C07854 zzAk;

            C07841(C07854 c07854, zzce com_google_android_gms_internal_zzce) {
                this.zzAk = c07854;
                this.zzAj = com_google_android_gms_internal_zzce;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAs != null) {
                    com_google_android_gms_internal_zzdx.zzAs.zza(this.zzAj);
                }
            }
        }

        C07854(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void zza(zzce com_google_android_gms_internal_zzce) throws RemoteException {
            this.zzAc.zzpH.add(new C07841(this, com_google_android_gms_internal_zzce));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.5 */
    class C07875 extends com.google.android.gms.ads.internal.client.zzp.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.5.1 */
        class C07861 implements zza {
            final /* synthetic */ C07875 zzAl;

            C07861(C07875 c07875) {
                this.zzAl = c07875;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAt != null) {
                    com_google_android_gms_internal_zzdx.zzAt.onAdClicked();
                }
            }
        }

        C07875(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void onAdClicked() throws RemoteException {
            this.zzAc.zzpH.add(new C07861(this));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.6 */
    class C07956 extends com.google.android.gms.ads.internal.reward.client.zzd.zza {
        final /* synthetic */ zzdw zzAc;

        /* renamed from: com.google.android.gms.internal.zzdw.6.1 */
        class C07881 implements zza {
            final /* synthetic */ C07956 zzAm;

            C07881(C07956 c07956) {
                this.zzAm = c07956;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdLoaded();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.2 */
        class C07892 implements zza {
            final /* synthetic */ C07956 zzAm;

            C07892(C07956 c07956) {
                this.zzAm = c07956;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdOpened();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.3 */
        class C07903 implements zza {
            final /* synthetic */ C07956 zzAm;

            C07903(C07956 c07956) {
                this.zzAm = c07956;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoStarted();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.4 */
        class C07914 implements zza {
            final /* synthetic */ C07956 zzAm;

            C07914(C07956 c07956) {
                this.zzAm = c07956;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdClosed();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.5 */
        class C07925 implements zza {
            final /* synthetic */ C07956 zzAm;
            final /* synthetic */ com.google.android.gms.ads.internal.reward.client.zza zzAn;

            C07925(C07956 c07956, com.google.android.gms.ads.internal.reward.client.zza com_google_android_gms_ads_internal_reward_client_zza) {
                this.zzAm = c07956;
                this.zzAn = com_google_android_gms_ads_internal_reward_client_zza;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.zza(this.zzAn);
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.6 */
        class C07936 implements zza {
            final /* synthetic */ C07956 zzAm;

            C07936(C07956 c07956) {
                this.zzAm = c07956;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdLeftApplication();
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzdw.6.7 */
        class C07947 implements zza {
            final /* synthetic */ int zzAe;
            final /* synthetic */ C07956 zzAm;

            C07947(C07956 c07956, int i) {
                this.zzAm = c07956;
                this.zzAe = i;
            }

            public void zzb(zzdx com_google_android_gms_internal_zzdx) throws RemoteException {
                if (com_google_android_gms_internal_zzdx.zzAu != null) {
                    com_google_android_gms_internal_zzdx.zzAu.onRewardedVideoAdFailedToLoad(this.zzAe);
                }
            }
        }

        C07956(zzdw com_google_android_gms_internal_zzdw) {
            this.zzAc = com_google_android_gms_internal_zzdw;
        }

        public void onRewardedVideoAdClosed() throws RemoteException {
            this.zzAc.zzpH.add(new C07914(this));
        }

        public void onRewardedVideoAdFailedToLoad(int errorCode) throws RemoteException {
            this.zzAc.zzpH.add(new C07947(this, errorCode));
        }

        public void onRewardedVideoAdLeftApplication() throws RemoteException {
            this.zzAc.zzpH.add(new C07936(this));
        }

        public void onRewardedVideoAdLoaded() throws RemoteException {
            this.zzAc.zzpH.add(new C07881(this));
        }

        public void onRewardedVideoAdOpened() throws RemoteException {
            this.zzAc.zzpH.add(new C07892(this));
        }

        public void onRewardedVideoStarted() throws RemoteException {
            this.zzAc.zzpH.add(new C07903(this));
        }

        public void zza(com.google.android.gms.ads.internal.reward.client.zza com_google_android_gms_ads_internal_reward_client_zza) throws RemoteException {
            this.zzAc.zzpH.add(new C07925(this, com_google_android_gms_ads_internal_reward_client_zza));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzdw.7 */
    class C07967 implements Runnable {
        final /* synthetic */ zzdw zzAc;
        final /* synthetic */ zza zzAo;
        final /* synthetic */ zzdx zzAp;

        C07967(zzdw com_google_android_gms_internal_zzdw, zza com_google_android_gms_internal_zzdw_zza, zzdx com_google_android_gms_internal_zzdx) {
            this.zzAc = com_google_android_gms_internal_zzdw;
            this.zzAo = com_google_android_gms_internal_zzdw_zza;
            this.zzAp = com_google_android_gms_internal_zzdx;
        }

        public void run() {
            try {
                this.zzAo.zzb(this.zzAp);
            } catch (Throwable e) {
                zzb.zzd("Could not propagate interstitial ad event.", e);
            }
        }
    }

    zzdw() {
        this.zzpH = new LinkedList();
    }

    void zza(zzdx com_google_android_gms_internal_zzdx) {
        Handler handler = zzir.zzMc;
        for (zza c07967 : this.zzpH) {
            handler.post(new C07967(this, c07967, com_google_android_gms_internal_zzdx));
        }
    }

    void zzc(zzk com_google_android_gms_ads_internal_zzk) {
        com_google_android_gms_ads_internal_zzk.zza(new C07791(this));
        com_google_android_gms_ads_internal_zzk.zza(new C07812(this));
        com_google_android_gms_ads_internal_zzk.zza(new C07833(this));
        com_google_android_gms_ads_internal_zzk.zza(new C07854(this));
        com_google_android_gms_ads_internal_zzk.zza(new C07875(this));
        com_google_android_gms_ads_internal_zzk.zza(new C07956(this));
    }
}
