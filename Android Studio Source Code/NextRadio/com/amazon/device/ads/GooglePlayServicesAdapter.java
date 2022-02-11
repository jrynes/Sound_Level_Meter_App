package com.amazon.device.ads;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;

class GooglePlayServicesAdapter {
    private static final String LOGTAG;
    private final MobileAdsLogger logger;

    GooglePlayServicesAdapter() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
    }

    static {
        LOGTAG = GooglePlayServicesAdapter.class.getSimpleName();
    }

    public AdvertisingInfo getAdvertisingIdentifierInfo() {
        try {
            Info info = AdvertisingIdClient.getAdvertisingIdInfo(MobileAdsInfoStore.getInstance().getApplicationContext());
            this.logger.m643v("The Google Play Services Advertising Identifier was successfully retrieved.");
            String advertisingId = info.getId();
            return new AdvertisingInfo().setAdvertisingIdentifier(advertisingId).setLimitAdTrackingEnabled(info.isLimitAdTrackingEnabled());
        } catch (IllegalStateException e) {
            this.logger.m639e("The Google Play Services Advertising Id API was called from a non-background thread.");
            return new AdvertisingInfo();
        } catch (IOException e2) {
            this.logger.m639e("Retrieving the Google Play Services Advertising Identifier caused an IOException.");
            return new AdvertisingInfo();
        } catch (GooglePlayServicesNotAvailableException e3) {
            this.logger.m643v("Retrieving the Google Play Services Advertising Identifier caused a GooglePlayServicesNotAvailableException.");
            return AdvertisingInfo.createNotAvailable();
        } catch (GooglePlayServicesRepairableException e4) {
            this.logger.m643v("Retrieving the Google Play Services Advertising Identifier caused a GooglePlayServicesRepairableException.");
            return new AdvertisingInfo();
        }
    }
}
