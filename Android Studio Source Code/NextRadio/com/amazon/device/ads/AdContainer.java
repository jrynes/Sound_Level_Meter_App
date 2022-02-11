package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import io.fabric.sdk.android.services.network.HttpRequest;

@SuppressLint({"ViewConstructor"})
class AdContainer extends FrameLayout implements Destroyable {
    private static final String CONTENT_DESCRIPTION_AD_CONTAINER = "adContainerObject";
    private boolean allowClicks;
    private String baseUrl;
    private boolean disableHardwareAcceleration;
    private String html;
    private final NativeCloseButton nativeCloseButton;
    private PreloadCallback preloadCallback;
    private boolean shouldPreload;
    private ViewManager viewManager;

    static class AdContainerFactory {
        AdContainerFactory() {
        }

        public AdContainer createAdContainer(Context context, AdCloser adCloser) {
            return new AdContainer(context, adCloser);
        }
    }

    public AdContainer(Context context, AdCloser adCloser) {
        this(context, adCloser, new ViewManagerFactory(), null);
    }

    AdContainer(Context context, AdCloser adCloser, ViewManagerFactory viewManagerFactory, NativeCloseButton nativeCloseButton) {
        super(context);
        this.disableHardwareAcceleration = false;
        this.allowClicks = true;
        this.viewManager = viewManagerFactory.withViewGroup(this).createViewManager();
        setContentDescription(CONTENT_DESCRIPTION_AD_CONTAINER);
        if (nativeCloseButton == null) {
            this.nativeCloseButton = new NativeCloseButton(this, adCloser);
        } else {
            this.nativeCloseButton = nativeCloseButton;
        }
    }

    public void initialize() throws IllegalStateException {
        this.viewManager.disableHardwareAcceleration(this.disableHardwareAcceleration);
        this.viewManager.initialize();
    }

    public void setAdWebViewClient(AdWebViewClient adWebViewClient) {
        this.viewManager.setWebViewClient(adWebViewClient);
    }

    public void disableHardwareAcceleration(boolean shouldDisable) {
        this.disableHardwareAcceleration = shouldDisable;
        if (this.viewManager != null) {
            this.viewManager.disableHardwareAcceleration(this.disableHardwareAcceleration);
        }
    }

    public void destroy() {
        this.viewManager.destroy();
    }

    public void injectJavascript(String javascript, boolean preload) {
        this.viewManager.loadUrl("javascript:" + javascript, preload, null);
    }

    public void setViewHeight(int height) {
        this.viewManager.setHeight(height);
    }

    public void setViewLayoutParams(int width, int height, int gravity) {
        this.viewManager.setLayoutParams(width, height, gravity);
    }

    public boolean canShowViews() {
        return this.viewManager.canShowViews();
    }

    public void loadHtml(String baseUrl, String html, boolean shouldPreload, PreloadCallback callback) {
        this.baseUrl = baseUrl;
        this.html = html;
        this.shouldPreload = shouldPreload;
        this.preloadCallback = callback;
        if (baseUrl != null) {
            this.viewManager.loadDataWithBaseURL(baseUrl, html, WebRequest.CONTENT_TYPE_HTML, HttpRequest.CHARSET_UTF8, null, shouldPreload, callback);
            return;
        }
        this.viewManager.loadData(html, WebRequest.CONTENT_TYPE_HTML, HttpRequest.CHARSET_UTF8, shouldPreload, callback);
    }

    public void removePreviousInterfaces() {
        this.viewManager.removePreviousInterfaces();
    }

    public void addJavascriptInterface(Object jsif, boolean shouldPreload, String interfaceName) {
        this.viewManager.addJavascriptInterface(jsif, shouldPreload, interfaceName);
    }

    public void reload() {
        loadHtml(this.baseUrl, this.html, this.shouldPreload, this.preloadCallback);
    }

    public boolean isCurrentView(View view) {
        return this.viewManager.isCurrentView(view);
    }

    public void stashView() {
        this.viewManager.stashView();
    }

    public boolean popView() {
        return this.viewManager.popView();
    }

    public void enableNativeCloseButton(boolean showImage, RelativePosition position) {
        this.nativeCloseButton.enable(showImage, position);
    }

    public void removeNativeCloseButton() {
        this.nativeCloseButton.remove();
    }

    public void showNativeCloseButtonImage(boolean show) {
        this.nativeCloseButton.showImage(show);
    }

    public void listenForKey(OnKeyListener keyListener) {
        this.viewManager.listenForKey(keyListener);
    }

    public void setAllowClicks(boolean allowClicks) {
        this.allowClicks = allowClicks;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !this.allowClicks;
    }
}
