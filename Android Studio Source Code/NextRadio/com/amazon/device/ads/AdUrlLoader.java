package com.amazon.device.ads;

import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import com.amazon.device.ads.WebRequest.WebResponse;
import io.fabric.sdk.android.services.network.HttpRequest;

class AdUrlLoader {
    private static final String LOGTAG;
    private final AdControlAccessor adControlAccessor;
    private final AdWebViewClient adWebViewClient;
    private final DeviceInfo deviceInfo;
    private final MobileAdsLogger logger;
    private final ThreadRunner threadRunner;
    private final WebRequestFactory webRequestFactory;
    private final WebUtils2 webUtils;

    /* renamed from: com.amazon.device.ads.AdUrlLoader.1 */
    class C02911 implements Runnable {
        final /* synthetic */ PreloadCallback val$callback;
        final /* synthetic */ boolean val$shouldPreload;
        final /* synthetic */ String val$url;

        C02911(String str, boolean z, PreloadCallback preloadCallback) {
            this.val$url = str;
            this.val$shouldPreload = z;
            this.val$callback = preloadCallback;
        }

        public void run() {
            AdUrlLoader.this.loadUrlInThread(this.val$url, this.val$shouldPreload, this.val$callback);
        }
    }

    /* renamed from: com.amazon.device.ads.AdUrlLoader.2 */
    class C02922 implements Runnable {
        final /* synthetic */ String val$body;
        final /* synthetic */ PreloadCallback val$callback;
        final /* synthetic */ boolean val$shouldPreload;
        final /* synthetic */ String val$url;

        C02922(String str, String str2, boolean z, PreloadCallback preloadCallback) {
            this.val$url = str;
            this.val$body = str2;
            this.val$shouldPreload = z;
            this.val$callback = preloadCallback;
        }

        public void run() {
            AdUrlLoader.this.adControlAccessor.loadHtml(this.val$url, this.val$body, this.val$shouldPreload, this.val$callback);
        }
    }

    static {
        LOGTAG = AdUrlLoader.class.getSimpleName();
    }

    public AdUrlLoader(ThreadRunner threadRunner, AdWebViewClient adWebViewClient, WebRequestFactory webRequestFactory, AdControlAccessor adControlAccessor, WebUtils2 webUtils, MobileAdsLoggerFactory loggerFactory, DeviceInfo deviceInfo) {
        this.threadRunner = threadRunner;
        this.adWebViewClient = adWebViewClient;
        this.webRequestFactory = webRequestFactory;
        this.adControlAccessor = adControlAccessor;
        this.webUtils = webUtils;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.deviceInfo = deviceInfo;
    }

    public void putUrlExecutorInAdWebViewClient(String scheme, UrlExecutor executor) {
        this.adWebViewClient.putUrlExecutor(scheme, executor);
    }

    public void setAdWebViewClientListener(AdWebViewClientListener adWebViewClientListener) {
        this.adWebViewClient.setListener(adWebViewClientListener);
    }

    public AdWebViewClient getAdWebViewClient() {
        return this.adWebViewClient;
    }

    public void loadUrl(String url, boolean shouldPreload, PreloadCallback callback) {
        String scheme = this.webUtils.getScheme(url);
        if (scheme.equals("http") || scheme.equals("https")) {
            this.threadRunner.execute(new C02911(url, shouldPreload, callback), ExecutionStyle.RUN_ASAP, ExecutionThread.BACKGROUND_THREAD);
        } else {
            openUrl(url);
        }
    }

    private void loadUrlInThread(String url, boolean shouldPreload, PreloadCallback callback) {
        WebRequest webRequest = this.webRequestFactory.createWebRequest();
        webRequest.setExternalLogTag(LOGTAG);
        webRequest.enableLogUrl(true);
        webRequest.setUrlString(url);
        webRequest.putHeader(HttpRequest.HEADER_USER_AGENT, this.deviceInfo.getUserAgentString());
        WebResponse response = null;
        try {
            response = webRequest.makeCall();
        } catch (WebRequestException e) {
            this.logger.m640e("Could not load URL (%s) into AdContainer: %s", url, e.getMessage());
        }
        if (response != null) {
            String body = response.getResponseReader().readAsString();
            if (body != null) {
                this.threadRunner.execute(new C02922(url, body, shouldPreload, callback), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
                return;
            }
            this.logger.m640e("Could not load URL (%s) into AdContainer.", url);
        }
    }

    public void openUrl(String url) {
        this.adWebViewClient.openUrl(url);
    }
}
