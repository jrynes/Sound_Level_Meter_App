package com.amazon.device.ads;

import android.content.Context;

interface PreferredMarketplaceRetriever {

    public static class NullPreferredMarketplaceRetriever implements PreferredMarketplaceRetriever {
        public String retrievePreferredMarketplace(Context context) {
            return null;
        }
    }

    String retrievePreferredMarketplace(Context context);
}
