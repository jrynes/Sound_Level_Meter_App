package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import org.apache.activemq.transport.stomp.Stomp;

class WebViewFactory {
    private static WebViewFactory instance;
    private final AndroidBuildInfo buildInfo;
    private final MobileAdsCookieManager cookieManager;
    private final DebugProperties debugProperties;
    private final MobileAdsInfoStore infoStore;
    private boolean isWebViewCheckedAndOk;
    private final MobileAdsLoggerFactory loggerFactory;
    private final WebViewConstructor webViewConstructor;
    private final WebViewDatabaseFactory webViewDatabaseFactory;

    static class MobileAdsCookieManager {
        private boolean cookieSyncManagerCreated;

        MobileAdsCookieManager() {
            this.cookieSyncManagerCreated = false;
        }

        public void createCookieSyncManager(Context context) {
            if (!this.cookieSyncManagerCreated) {
                CookieSyncManager.createInstance(context);
                this.cookieSyncManagerCreated = true;
            }
        }

        public void setCookie(String url, String cookie) {
            CookieManager.getInstance().setCookie(url, cookie);
        }

        public boolean isCookieSyncManagerCreated() {
            return this.cookieSyncManagerCreated;
        }

        public void startSync() {
            CookieSyncManager.getInstance().startSync();
        }

        public void stopSync() {
            CookieSyncManager.getInstance().stopSync();
        }
    }

    static class WebViewConstructor {
        WebViewConstructor() {
        }

        public WebView createWebView(Context context) {
            return new WebView(context);
        }
    }

    static class WebViewDatabaseFactory {
        WebViewDatabaseFactory() {
        }

        public WebViewDatabase getWebViewDatabase(Context context) {
            return WebViewDatabase.getInstance(context);
        }
    }

    static {
        instance = new WebViewFactory();
    }

    protected WebViewFactory() {
        this(MobileAdsInfoStore.getInstance(), new MobileAdsLoggerFactory(), DebugProperties.getInstance(), new MobileAdsCookieManager(), new AndroidBuildInfo(), new WebViewDatabaseFactory(), new WebViewConstructor());
    }

    WebViewFactory(MobileAdsInfoStore infoStore, MobileAdsLoggerFactory loggerFactory, DebugProperties debugProperties, MobileAdsCookieManager cookieManager, AndroidBuildInfo buildInfo, WebViewDatabaseFactory webViewDatabaseFactory, WebViewConstructor webViewConstructor) {
        this.isWebViewCheckedAndOk = false;
        this.infoStore = infoStore;
        this.loggerFactory = loggerFactory;
        this.debugProperties = debugProperties;
        this.cookieManager = cookieManager;
        this.buildInfo = buildInfo;
        this.webViewDatabaseFactory = webViewDatabaseFactory;
        this.webViewConstructor = webViewConstructor;
        shouldDebugWebViews();
    }

    public static final WebViewFactory getInstance() {
        return instance;
    }

    public synchronized WebView createWebView(Context context) {
        WebView webView;
        webView = this.webViewConstructor.createWebView(context);
        this.infoStore.getDeviceInfo().setUserAgentString(webView.getSettings().getUserAgentString());
        webView.getSettings().setUserAgentString(this.infoStore.getDeviceInfo().getUserAgentString());
        this.cookieManager.createCookieSyncManager(context);
        updateAdIdCookie();
        return webView;
    }

    private void updateAdIdCookie() {
        if (this.cookieManager.isCookieSyncManagerCreated()) {
            String adId = this.infoStore.getRegistrationInfo().getAdId();
            if (adId == null) {
                adId = Stomp.EMPTY;
            }
            this.cookieManager.setCookie("http://amazon-adsystem.com", "ad-id=" + adId + "; Domain=.amazon-adsystem.com");
        }
    }

    public boolean isWebViewOk(Context context) {
        if (AndroidTargetUtils.isAtOrBelowAndroidAPI(this.buildInfo, 8) && !this.isWebViewCheckedAndOk) {
            if (this.webViewDatabaseFactory.getWebViewDatabase(context) == null) {
                return false;
            }
            SQLiteDatabase cdb = null;
            try {
                cdb = context.openOrCreateDatabase("webviewCache.db", 0, null);
                if (cdb != null) {
                    cdb.close();
                }
                this.isWebViewCheckedAndOk = true;
            } catch (SQLiteException e) {
                boolean isDatabaseLocked = isDatabaseLocked(e);
                if (cdb == null) {
                    return isDatabaseLocked;
                }
                cdb.close();
                return isDatabaseLocked;
            } catch (Throwable th) {
                if (cdb != null) {
                    cdb.close();
                }
            }
        }
        return true;
    }

    private boolean isDatabaseLocked(SQLiteException e) {
        if (AndroidTargetUtils.isAtLeastAndroidAPI(this.buildInfo, 11)) {
            return AndroidTargetUtils.isInstanceOfSQLiteDatabaseLockedException(e);
        }
        return doesExceptionContainLockedDatabaseMessage(e);
    }

    private static boolean doesExceptionContainLockedDatabaseMessage(Exception e) {
        String lockedDatabaseMessage = "database is locked";
        return (e == null || e.getMessage() == null) ? false : e.getMessage().contains("database is locked");
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public boolean setJavaScriptEnabledForWebView(boolean enable, WebView webView, String logtag) {
        try {
            webView.getSettings().setJavaScriptEnabled(enable);
            return true;
        } catch (NullPointerException e) {
            this.loggerFactory.createMobileAdsLogger(logtag).m645w("Could not set JavaScriptEnabled because a NullPointerException was encountered.");
            return false;
        }
    }

    private void shouldDebugWebViews() {
        if (this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_WEBVIEWS, Boolean.valueOf(false)).booleanValue()) {
            AndroidTargetUtils.enableWebViewDebugging(true);
        }
    }
}
