package com.facebook.ads;

import android.content.Context;
import android.view.View;
import com.facebook.ads.internal.view.C0544b;
import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import org.apache.activemq.ActiveMQPrefetchPolicy;

public class NativeAdView {

    public enum Type {
        HEIGHT_100(-1, 100),
        HEIGHT_120(-1, Nack.INDEX),
        HEIGHT_300(-1, 300),
        HEIGHT_400(-1, 400);
        
        private final int f1522a;
        private final int f1523b;

        private Type(int i, int i2) {
            this.f1522a = i;
            this.f1523b = i2;
        }

        public int getHeight() {
            return this.f1523b;
        }

        public int getValue() {
            switch (this.f1523b) {
                case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                    return 1;
                case Nack.INDEX /*120*/:
                    return 2;
                case 300:
                    return 3;
                case 400:
                    return 4;
                default:
                    return -1;
            }
        }

        public int getWidth() {
            return this.f1522a;
        }
    }

    public static View render(Context context, NativeAd nativeAd, Type type) {
        return render(context, nativeAd, type, null);
    }

    public static View render(Context context, NativeAd nativeAd, Type type, NativeAdViewAttributes nativeAdViewAttributes) {
        if (nativeAd.isNativeConfigEnabled()) {
            nativeAdViewAttributes = nativeAd.getAdViewAttributes();
        } else if (nativeAdViewAttributes == null) {
            nativeAdViewAttributes = new NativeAdViewAttributes();
        }
        nativeAd.m1140a(type);
        return new C0544b(context, nativeAd, type, nativeAdViewAttributes);
    }
}
