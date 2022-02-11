package com.amazon.device.ads;

class MraidAdSDKBridgeFactory implements AdSDKBridgeFactory {
    MraidAdSDKBridgeFactory() {
    }

    public AdSDKBridge createAdSDKBridge(AdControlAccessor adControlAccessor) {
        return new MRAIDAdSDKBridge(adControlAccessor, new JavascriptInteractor());
    }
}
