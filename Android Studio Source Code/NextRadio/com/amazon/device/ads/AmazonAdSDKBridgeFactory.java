package com.amazon.device.ads;

public class AmazonAdSDKBridgeFactory implements AdSDKBridgeFactory {
    public AdSDKBridge createAdSDKBridge(AdControlAccessor accessor) {
        return new AmazonAdSDKBridge(accessor, new JavascriptInteractor());
    }
}
