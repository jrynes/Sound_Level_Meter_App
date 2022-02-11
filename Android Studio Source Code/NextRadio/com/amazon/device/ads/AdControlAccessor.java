package com.amazon.device.ads;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

class AdControlAccessor {
    private final AdController adController;

    public AdControlAccessor(AdController adController) {
        this.adController = adController;
    }

    public void stashView() {
        this.adController.stashView();
    }

    public boolean popView() {
        return this.adController.popView();
    }

    public void fireAdEvent(AdEvent event) {
        this.adController.fireAdEvent(event);
    }

    public void injectJavascript(String javascript) {
        this.adController.injectJavascript(javascript, false);
    }

    public void injectJavascriptPreload(String javascript) {
        this.adController.injectJavascript(javascript, true);
    }

    public void preloadHtml(String baseUrl, String html, PreloadCallback callback) {
        this.adController.preloadHtml(baseUrl, html, callback);
    }

    public void loadHtml(String baseUrl, String html) {
        this.adController.loadHtml(baseUrl, html);
    }

    public void loadHtml(String baseUrl, String html, boolean shouldPreload, PreloadCallback callback) {
        this.adController.loadHtml(baseUrl, html, shouldPreload, callback);
    }

    public void preloadUrl(String url, PreloadCallback callback) {
        this.adController.preloadUrl(url, callback);
    }

    public void loadUrl(String url) {
        this.adController.loadUrl(url);
    }

    public void openUrl(String url) {
        this.adController.openUrl(url);
    }

    public boolean closeAd() {
        return this.adController.closeAd();
    }

    public void enableCloseButton(boolean showImage) {
        enableCloseButton(showImage, null);
    }

    public void enableCloseButton(boolean showImage, RelativePosition position) {
        this.adController.enableNativeCloseButton(showImage, position);
    }

    public void removeCloseButton() {
        this.adController.removeNativeCloseButton();
    }

    public void showNativeCloseButtonImage(boolean showNativeCloseButtonImage) {
        this.adController.showNativeCloseButtonImage(showNativeCloseButtonImage);
    }

    public Context getContext() {
        return this.adController.getContext();
    }

    public int getViewWidth() {
        return this.adController.getViewWidth();
    }

    public int getViewHeight() {
        return this.adController.getViewHeight();
    }

    public void moveViewToViewGroup(ViewGroup newViewGroup, LayoutParams layoutParams, boolean isModal) {
        this.adController.moveViewToViewGroup(newViewGroup, layoutParams, isModal);
    }

    public void moveViewBackToParent(LayoutParams layoutParams) {
        this.adController.moveViewBackToParent(layoutParams);
    }

    public ViewGroup getViewParentIfExpanded() {
        return this.adController.getViewParentIfExpanded();
    }

    public void setExpanded(boolean isExpanded) {
        this.adController.setExpanded(isExpanded);
    }

    public AdState getAdState() {
        return this.adController.getAdState();
    }

    public Position getCurrentPosition() {
        return this.adController.getAdPosition();
    }

    public Size getMaxSize() {
        return this.adController.getMaxExpandableSize();
    }

    public Size getScreenSize() {
        return this.adController.getScreenSize();
    }

    public boolean isInterstitial() {
        return this.adController.isInterstitial();
    }

    public void addSDKEventListener(SDKEventListener eventListener) {
        this.adController.addSDKEventListener(eventListener);
    }

    public void addJavascriptInterface(Object jsif, boolean shouldPreload, String interfaceName) {
        this.adController.addJavascriptInterface(jsif, shouldPreload, interfaceName);
    }

    public void reload() {
        this.adController.reload();
    }

    public void setOriginalOrientation(Activity activity) {
        this.adController.setOriginalOrientation(activity);
    }

    public int getOriginalOrientation() {
        return this.adController.getOriginalOrientation();
    }

    public void overrideBackButton(boolean override) {
        this.adController.overrideBackButton(override);
    }

    public boolean isVisible() {
        return this.adController.isVisible();
    }

    public boolean isModal() {
        return this.adController.isModal();
    }

    public int getWindowWidth() {
        return this.adController.getWindowWidth();
    }

    public int getWindowHeight() {
        return this.adController.getWindowHeight();
    }

    public int getAdWidth() {
        return this.adController.getAdData().getWidth();
    }

    public int getAdHeight() {
        return this.adController.getAdData().getHeight();
    }

    public double getScalingMultiplier() {
        return this.adController.getScalingMultiplier();
    }

    public void orientationChangeAttemptedWhenNotAllowed() {
        this.adController.orientationChangeAttemptedWhenNotAllowed();
    }
}
