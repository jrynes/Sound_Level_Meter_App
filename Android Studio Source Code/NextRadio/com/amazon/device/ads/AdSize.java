package com.amazon.device.ads;

import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.apache.activemq.transport.stomp.StompSubscription;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class AdSize {
    private static final String LOGTAG;
    public static final AdSize SIZE_1024x50;
    public static final AdSize SIZE_300x250;
    public static final AdSize SIZE_300x50;
    public static final AdSize SIZE_320x50;
    public static final AdSize SIZE_600x90;
    public static final AdSize SIZE_728x90;
    public static final AdSize SIZE_AUTO;
    public static final AdSize SIZE_AUTO_NO_SCALE;
    static final AdSize SIZE_INTERSTITIAL;
    static final AdSize SIZE_MODELESS_INTERSTITIAL;
    private int gravity;
    private int height;
    private final MobileAdsLogger logger;
    private Modality modality;
    private Scaling scaling;
    private SizeType type;
    private int width;

    /* renamed from: com.amazon.device.ads.AdSize.1 */
    static /* synthetic */ class C02901 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdSize$SizeType;

        static {
            $SwitchMap$com$amazon$device$ads$AdSize$SizeType = new int[SizeType.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdSize$SizeType[SizeType.EXPLICIT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdSize$SizeType[SizeType.AUTO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdSize$SizeType[SizeType.INTERSTITIAL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private enum Modality {
        MODAL,
        MODELESS
    }

    private enum Scaling {
        CAN_UPSCALE,
        NO_UPSCALE
    }

    enum SizeType {
        EXPLICIT,
        AUTO,
        INTERSTITIAL
    }

    static {
        LOGTAG = AdSize.class.getSimpleName();
        SIZE_300x50 = new AdSize(300, 50);
        SIZE_320x50 = new AdSize((int) AMQP.CONNECTION_FORCED, 50);
        SIZE_300x250 = new AdSize(300, (int) Type.TSIG);
        SIZE_600x90 = new AdSize((int) SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT, 90);
        SIZE_728x90 = new AdSize(728, 90);
        SIZE_1024x50 = new AdSize((int) Flags.FLAG5, 50);
        SIZE_AUTO = new AdSize(SizeType.AUTO);
        SIZE_AUTO_NO_SCALE = new AdSize(SizeType.AUTO, Scaling.NO_UPSCALE);
        SIZE_INTERSTITIAL = new AdSize(SizeType.INTERSTITIAL, Modality.MODAL);
        SIZE_MODELESS_INTERSTITIAL = new AdSize(SizeType.INTERSTITIAL);
    }

    public AdSize(int width, int height) {
        this.gravity = 17;
        this.type = SizeType.EXPLICIT;
        this.modality = Modality.MODELESS;
        this.scaling = Scaling.CAN_UPSCALE;
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        initialize(width, height);
    }

    AdSize(String width, String height) {
        this.gravity = 17;
        this.type = SizeType.EXPLICIT;
        this.modality = Modality.MODELESS;
        this.scaling = Scaling.CAN_UPSCALE;
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        initialize(NumberUtils.parseInt(width, 0), NumberUtils.parseInt(height, 0));
    }

    AdSize(SizeType type) {
        this.gravity = 17;
        this.type = SizeType.EXPLICIT;
        this.modality = Modality.MODELESS;
        this.scaling = Scaling.CAN_UPSCALE;
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.type = type;
    }

    AdSize(SizeType type, Modality modality) {
        this(type);
        this.modality = modality;
    }

    AdSize(SizeType type, Scaling scaling) {
        this(type);
        this.scaling = scaling;
    }

    private AdSize deepClone() {
        AdSize adSize = new AdSize(this.type);
        adSize.width = this.width;
        adSize.height = this.height;
        adSize.gravity = this.gravity;
        adSize.modality = this.modality;
        adSize.scaling = this.scaling;
        return adSize;
    }

    private void initialize(int width, int height) {
        if (width <= 0 || height <= 0) {
            String msg = "The width and height must be positive integers.";
            this.logger.m639e(msg);
            throw new IllegalArgumentException(msg);
        }
        this.width = width;
        this.height = height;
        this.type = SizeType.EXPLICIT;
    }

    public AdSize newGravity(int gravity) {
        AdSize adSize = deepClone();
        adSize.gravity = gravity;
        return adSize;
    }

    public int getGravity() {
        return this.gravity;
    }

    public String toString() {
        switch (C02901.$SwitchMap$com$amazon$device$ads$AdSize$SizeType[this.type.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return getAsSizeString(this.width, this.height);
            case Zone.SECONDARY /*2*/:
                return StompSubscription.AUTO_ACK;
            case Protocol.GGP /*3*/:
                return "interstitial";
            default:
                return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof AdSize) {
            AdSize other = (AdSize) obj;
            if (this.type.equals(other.type)) {
                if (!this.type.equals(SizeType.EXPLICIT)) {
                    return true;
                }
                if (this.width == other.width && this.height == other.height) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isAuto() {
        return this.type == SizeType.AUTO;
    }

    boolean isModal() {
        return Modality.MODAL.equals(this.modality);
    }

    SizeType getSizeType() {
        return this.type;
    }

    public boolean canUpscale() {
        return Scaling.CAN_UPSCALE.equals(this.scaling);
    }

    static String getAsSizeString(int w, int h) {
        return Integer.toString(w) + "x" + Integer.toString(h);
    }
}
