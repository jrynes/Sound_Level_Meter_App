package com.facebook.ads;

import com.rabbitmq.client.AMQP;
import org.xbill.DNS.Type;

public enum AdSize {
    BANNER_320_50(AMQP.CONNECTION_FORCED, 50),
    INTERSTITIAL(0, 0),
    BANNER_HEIGHT_50(-1, 50),
    BANNER_HEIGHT_90(-1, 90),
    RECTANGLE_HEIGHT_250(-1, Type.TSIG);
    
    private final int f1420a;
    private final int f1421b;

    private AdSize(int i, int i2) {
        this.f1420a = i;
        this.f1421b = i2;
    }

    private static boolean m1045a(AdSize adSize, int i, int i2) {
        return adSize != null && adSize.f1420a == i && adSize.f1421b == i2;
    }

    public static AdSize fromWidthAndHeight(int i, int i2) {
        for (AdSize adSize : (AdSize[]) AdSize.class.getEnumConstants()) {
            if (m1045a(adSize, i, i2)) {
                return adSize;
            }
        }
        return null;
    }

    public int getHeight() {
        return this.f1421b;
    }

    public int getWidth() {
        return this.f1420a;
    }
}
