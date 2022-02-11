package com.amazon.device.ads;

import android.app.Activity;
import com.amazon.device.ads.SDKEvent.SDKEventType;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

class MRAIDAdSDKEventListener implements SDKEventListener {
    private static final String LOGTAG;
    private final MobileAdsLogger logger;
    private MRAIDAdSDKBridge mraidAdSDKBridge;

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKEventListener.1 */
    static /* synthetic */ class C03261 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$AdState;
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType;

        static {
            $SwitchMap$com$amazon$device$ads$AdState = new int[AdState.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.EXPANDED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.SHOWING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$AdState[AdState.RENDERED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType = new int[SDKEventType.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.RENDERED.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.VISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.CLOSED.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.RESIZED.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.HIDDEN.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.DESTROYED.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[SDKEventType.BRIDGE_ADDED.ordinal()] = 7;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    static {
        LOGTAG = MRAIDAdSDKEventListener.class.getSimpleName();
    }

    MRAIDAdSDKEventListener(MRAIDAdSDKBridge adBridge) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.mraidAdSDKBridge = adBridge;
    }

    public void onSDKEvent(SDKEvent sdkEvent, AdControlAccessor adControlAccessor) {
        this.logger.m637d(sdkEvent.getEventType().toString());
        switch (C03261.$SwitchMap$com$amazon$device$ads$SDKEvent$SDKEventType[sdkEvent.getEventType().ordinal()]) {
            case Zone.PRIMARY /*1*/:
                handleReadyEvent(adControlAccessor);
            case Zone.SECONDARY /*2*/:
                handleVisibleEvent(adControlAccessor);
            case Protocol.GGP /*3*/:
                handleClosedEvent(adControlAccessor);
            case Type.MF /*4*/:
                this.mraidAdSDKBridge.reportSizeChangeEvent();
            case Service.RJE /*5*/:
            case Protocol.TCP /*6*/:
                adControlAccessor.injectJavascript("mraidBridge.stateChange('hidden');");
                adControlAccessor.injectJavascript("mraidBridge.viewableChange('false');");
            case Service.ECHO /*7*/:
                handleBridgeAddedEvent(sdkEvent, adControlAccessor);
            default:
        }
    }

    private void handleBridgeAddedEvent(SDKEvent sdkEvent, AdControlAccessor adControlAccessor) {
        String bridgeName = sdkEvent.getParameter(SDKEvent.BRIDGE_NAME);
        if (bridgeName != null && bridgeName.equals(this.mraidAdSDKBridge.getName())) {
            switch (C03261.$SwitchMap$com$amazon$device$ads$AdState[adControlAccessor.getAdState().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                case Zone.SECONDARY /*2*/:
                    handleReadyEvent(adControlAccessor);
                    handleVisibleEvent(adControlAccessor);
                case Protocol.GGP /*3*/:
                    handleReadyEvent(adControlAccessor);
                default:
            }
        }
    }

    private void handleReadyEvent(AdControlAccessor adControlAccessor) {
        adControlAccessor.injectJavascript("mraidBridge.ready();");
    }

    private void handleVisibleEvent(AdControlAccessor adControlAccessor) {
        adControlAccessor.setOriginalOrientation((Activity) adControlAccessor.getContext());
        Size size = adControlAccessor.getMaxSize();
        this.mraidAdSDKBridge.updateExpandProperties(size.getWidth(), size.getHeight());
        Position currentPosition = adControlAccessor.getCurrentPosition();
        this.mraidAdSDKBridge.updateDefaultPosition(currentPosition.getSize().getWidth(), currentPosition.getSize().getHeight(), currentPosition.getX(), currentPosition.getY());
        this.mraidAdSDKBridge.orientationPropertyChange();
        adControlAccessor.injectJavascript("mraidBridge.stateChange('default');");
        adControlAccessor.injectJavascript("mraidBridge.viewableChange('true');");
    }

    private void handleClosedEvent(AdControlAccessor adControlAccessor) {
        if (adControlAccessor.getAdState().equals(AdState.EXPANDED)) {
            this.mraidAdSDKBridge.collapseExpandedAd(adControlAccessor);
            if (((Activity) adControlAccessor.getContext()).getRequestedOrientation() != adControlAccessor.getOriginalOrientation()) {
                ((Activity) adControlAccessor.getContext()).setRequestedOrientation(adControlAccessor.getOriginalOrientation());
            }
        } else if (adControlAccessor.getAdState().equals(AdState.SHOWING)) {
            if (((Activity) adControlAccessor.getContext()).getRequestedOrientation() != adControlAccessor.getOriginalOrientation()) {
                ((Activity) adControlAccessor.getContext()).setRequestedOrientation(adControlAccessor.getOriginalOrientation());
            }
            adControlAccessor.injectJavascript("mraidBridge.stateChange('hidden');");
            adControlAccessor.injectJavascript("mraidBridge.viewableChange('false');");
        }
    }
}
