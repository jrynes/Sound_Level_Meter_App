package com.amazon.device.ads;

import com.amazon.device.ads.JavascriptInteractor.Executor;

interface AdSDKBridge {
    String getJavascript();

    Executor getJavascriptInteractorExecutor();

    String getName();

    SDKEventListener getSDKEventListener();

    boolean hasNativeExecution();
}
