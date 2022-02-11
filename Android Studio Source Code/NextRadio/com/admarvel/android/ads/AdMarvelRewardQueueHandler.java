package com.admarvel.android.ads;

import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.admarvel.android.ads.j */
class AdMarvelRewardQueueHandler {
    private static AdMarvelRewardQueueHandler f627a;
    private ArrayList<AdMarvelRewardQueueHandler> f628b;

    /* renamed from: com.admarvel.android.ads.j.a */
    class AdMarvelRewardQueueHandler {
        SDKAdNetwork f624a;
        AdMarvelEvent f625b;
        final /* synthetic */ AdMarvelRewardQueueHandler f626c;

        public AdMarvelRewardQueueHandler(AdMarvelRewardQueueHandler adMarvelRewardQueueHandler, SDKAdNetwork sDKAdNetwork, AdMarvelEvent adMarvelEvent) {
            this.f626c = adMarvelRewardQueueHandler;
            this.f624a = sDKAdNetwork;
            this.f625b = adMarvelEvent;
        }
    }

    static {
        f627a = null;
    }

    private AdMarvelRewardQueueHandler() {
        this.f628b = null;
    }

    public static AdMarvelRewardQueueHandler m336a() {
        if (f627a == null) {
            f627a = new AdMarvelRewardQueueHandler();
        }
        return f627a;
    }

    public AdMarvelEvent m337a(SDKAdNetwork sDKAdNetwork) {
        if (this.f628b != null && this.f628b.size() > 0) {
            Iterator it = this.f628b.iterator();
            while (it.hasNext()) {
                AdMarvelRewardQueueHandler adMarvelRewardQueueHandler = (AdMarvelRewardQueueHandler) it.next();
                if (adMarvelRewardQueueHandler.f624a == sDKAdNetwork) {
                    AdMarvelEvent adMarvelEvent = adMarvelRewardQueueHandler.f625b;
                    it.remove();
                    return adMarvelEvent;
                }
            }
        }
        return null;
    }

    public void m338a(SDKAdNetwork sDKAdNetwork, AdMarvelEvent adMarvelEvent) {
        if (this.f628b == null) {
            this.f628b = new ArrayList();
        }
        this.f628b.add(new AdMarvelRewardQueueHandler(this, sDKAdNetwork, adMarvelEvent));
    }
}
