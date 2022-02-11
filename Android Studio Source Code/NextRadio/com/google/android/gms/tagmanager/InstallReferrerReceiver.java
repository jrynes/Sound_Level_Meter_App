package com.google.android.gms.tagmanager;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.CampaignTrackingService;

public final class InstallReferrerReceiver extends CampaignTrackingReceiver {
    protected void zzaV(String str) {
        zzax.zzgh(str);
    }

    protected Class<? extends CampaignTrackingService> zziB() {
        return InstallReferrerService.class;
    }
}
