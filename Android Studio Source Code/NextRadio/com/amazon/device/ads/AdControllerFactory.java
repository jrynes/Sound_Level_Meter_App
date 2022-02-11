package com.amazon.device.ads;

import android.content.Context;

class AdControllerFactory {
    private static AdController cachedAdController;

    AdControllerFactory() {
    }

    static {
        cachedAdController = null;
    }

    public static void cacheAdController(AdController adController) {
        cachedAdController = adController;
    }

    public static AdController getCachedAdController() {
        return cachedAdController;
    }

    public static AdController removeCachedAdController() {
        AdController currentAdController = cachedAdController;
        cachedAdController = null;
        return currentAdController;
    }

    public AdController buildAdController(Context context, AdSize adSize) {
        try {
            AdController adController = new AdController(context, adSize);
            return adController;
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
