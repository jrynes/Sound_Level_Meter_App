package com.admarvel.android.ads.nativeads;

import android.view.View;
import android.view.View.OnClickListener;

/* renamed from: com.admarvel.android.ads.nativeads.a */
public class AdMarvelClickDetector {
    private AdMarvelClickDetector f699a;
    private OnClickListener f700b;
    private OnClickListener f701c;

    /* renamed from: com.admarvel.android.ads.nativeads.a.a */
    interface AdMarvelClickDetector {
        void m393a(View view, String str);
    }

    /* renamed from: com.admarvel.android.ads.nativeads.a.1 */
    class AdMarvelClickDetector implements OnClickListener {
        final /* synthetic */ AdMarvelClickDetector f697a;

        AdMarvelClickDetector(AdMarvelClickDetector adMarvelClickDetector) {
            this.f697a = adMarvelClickDetector;
        }

        public void onClick(View v) {
            if (this.f697a.f699a != null) {
                this.f697a.f699a.m393a(v, AdMarvelNativeAd.ADMARVEL_HANDLE_NOTICE_CLICK_EVENT);
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.a.2 */
    class AdMarvelClickDetector implements OnClickListener {
        final /* synthetic */ AdMarvelClickDetector f698a;

        AdMarvelClickDetector(AdMarvelClickDetector adMarvelClickDetector) {
            this.f698a = adMarvelClickDetector;
        }

        public void onClick(View v) {
            if (this.f698a.f699a != null) {
                this.f698a.f699a.m393a(v, AdMarvelNativeAd.ADMARVEL_HANDLE_CLICK_EVENT);
            }
        }
    }

    public AdMarvelClickDetector(AdMarvelClickDetector adMarvelClickDetector) {
        this.f700b = new AdMarvelClickDetector(this);
        this.f701c = new AdMarvelClickDetector(this);
        this.f699a = adMarvelClickDetector;
    }

    void m398a(View view) {
        if (view != null) {
            view.setOnClickListener(null);
        }
    }

    void m399a(View[] viewArr, String str) {
        int i = 0;
        if (str != null && viewArr != null && viewArr.length > 0) {
            if (str.trim().equalsIgnoreCase(AdMarvelNativeAd.ADMARVEL_HANDLE_CLICK_EVENT)) {
                while (i < viewArr.length) {
                    try {
                        viewArr[i].setOnClickListener(this.f701c);
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            } else if (str.trim().equalsIgnoreCase(AdMarvelNativeAd.ADMARVEL_HANDLE_NOTICE_CLICK_EVENT)) {
                while (i < viewArr.length) {
                    try {
                        viewArr[i].setOnClickListener(this.f700b);
                        i++;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}
