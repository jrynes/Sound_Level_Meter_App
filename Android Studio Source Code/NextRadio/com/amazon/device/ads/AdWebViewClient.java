package com.amazon.device.ads;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

class AdWebViewClient extends WebViewClient {
    protected static final String AAX_REDIRECT_BETA = "aax-beta.integ.amazon.com";
    protected static final String AAX_REDIRECT_GAMMA = "aax-us-east.amazon-adsystem.com";
    protected static final String AAX_REDIRECT_PROD = "aax-us-east.amazon-adsystem.com";
    public static final String AMAZON_MOBILE = "amazonmobile";
    protected static final String CORNERSTONE_BEST_ENDPOINT_BETA = "d16g-cornerstone-bes.integ.amazon.com";
    protected static final String CORNERSTONE_BEST_ENDPOINT_PROD = "pda-bes.amazon.com";
    public static final String GEO = "geo";
    public static final String GOOGLE_STREETVIEW = "google.streetview";
    private static final String LOGTAG;
    public static final String MAILTO = "mailto";
    public static final String SMS = "sms";
    public static final String TELEPHONE = "tel";
    public static final String VOICEMAIL = "voicemail";
    protected static final HashSet<String> intentSchemes;
    protected static Set<String> redirectHosts;
    private final AdControlAccessor adControlAccessor;
    private final AndroidBuildInfo androidBuildInfo;
    private final AdSDKBridgeList bridgeList;
    private final Context context;
    private AdWebViewClientListener listener;
    private final MobileAdsLogger logger;
    private final MobileAdsLoggerFactory loggerFactory;
    private CopyOnWriteArrayList<String> resourceList;
    private final HashMap<String, UrlExecutor> urlExecutors;
    private final WebUtils2 webUtils;

    interface AdWebViewClientListener {
        void onLoadResource(WebView webView, String str);

        void onPageFinished(WebView webView, String str);

        void onPageStarted(WebView webView, String str);

        void onReceivedError(WebView webView, int i, String str, String str2);
    }

    /* renamed from: com.amazon.device.ads.AdWebViewClient.1 */
    class C02941 implements Runnable {
        C02941() {
        }

        public void run() {
            AdWebViewClient.this.adControlAccessor.reload();
        }
    }

    interface UrlExecutor {
        boolean execute(String str);
    }

    static class AmazonMobileExecutor implements UrlExecutor {
        private final Context context;
        private final AmazonDeviceLauncher launcher;
        private final MobileAdsLogger logger;
        private final WebUtils2 webUtils;

        AmazonMobileExecutor(Context context) {
            this(context, new MobileAdsLoggerFactory(), new AmazonDeviceLauncher(), new WebUtils2());
        }

        AmazonMobileExecutor(Context context, MobileAdsLoggerFactory loggerFactory, AmazonDeviceLauncher launcher, WebUtils2 webUtils) {
            this.context = context;
            this.logger = loggerFactory.createMobileAdsLogger(AdWebViewClient.LOGTAG);
            this.launcher = launcher;
            this.webUtils = webUtils;
        }

        public boolean execute(String url) {
            specialUrlClicked(url);
            return true;
        }

        public void specialUrlClicked(String url) {
            List<String> intents;
            this.logger.m637d("Executing AmazonMobile Intent");
            Uri uri = Uri.parse(url);
            try {
                intents = uri.getQueryParameters("intent");
            } catch (UnsupportedOperationException e) {
                intents = null;
            }
            if (intents != null && intents.size() > 0) {
                for (String intent : intents) {
                    if (launchExternalActivity(intent)) {
                        return;
                    }
                }
                handleApplicationDefinedSpecialURL(url);
            } else if (!this.launcher.isWindowshopPresent(this.context)) {
                handleApplicationDefinedSpecialURL(url);
            } else if (uri.getHost().equals("shopping")) {
                String action = uri.getQueryParameter("app-action");
                if (action != null && action.length() != 0) {
                    if (action.equals(ProductAction.ACTION_DETAIL)) {
                        String asin = uri.getQueryParameter("asin");
                        if (asin != null && asin.length() != 0) {
                            this.launcher.launchWindowshopDetailPage(this.context, asin);
                        }
                    } else if (action.equals("search")) {
                        String keyword = uri.getQueryParameter("keyword");
                        if (keyword != null && keyword.length() != 0) {
                            this.launcher.launchWindowshopSearchPage(this.context, keyword);
                        }
                    } else if (action.equals("webview")) {
                        handleApplicationDefinedSpecialURL(url);
                    }
                }
            }
        }

        protected void handleApplicationDefinedSpecialURL(String url) {
            this.logger.m642i("Special url clicked, but was not handled by SDK. Url: %s", url);
        }

        protected boolean launchExternalActivity(String url) {
            return this.webUtils.launchActivityForIntentLink(url, this.context);
        }
    }

    static class DefaultExecutor implements UrlExecutor {
        private final Context context;

        public DefaultExecutor(Context context) {
            this.context = context;
        }

        public boolean execute(String url) {
            WebUtils.launchActivityForIntentLink(url, this.context);
            return true;
        }
    }

    static {
        LOGTAG = AdWebViewClient.class.getSimpleName();
        intentSchemes = new HashSet();
        intentSchemes.add(TELEPHONE);
        intentSchemes.add(VOICEMAIL);
        intentSchemes.add(SMS);
        intentSchemes.add(MAILTO);
        intentSchemes.add(GEO);
        intentSchemes.add(GOOGLE_STREETVIEW);
        redirectHosts = new HashSet();
        redirectHosts.add(AAX_REDIRECT_PROD);
        redirectHosts.add(AAX_REDIRECT_PROD);
        redirectHosts.add(AAX_REDIRECT_BETA);
        redirectHosts.add(CORNERSTONE_BEST_ENDPOINT_PROD);
        redirectHosts.add(CORNERSTONE_BEST_ENDPOINT_BETA);
    }

    public AdWebViewClient(Context context, AdSDKBridgeList bridgeList, AdControlAccessor adControlAccessor, WebUtils2 webUtils, MobileAdsLoggerFactory loggerFactory, AndroidBuildInfo androidBuildInfo) {
        this.resourceList = new CopyOnWriteArrayList();
        this.context = context;
        this.urlExecutors = new HashMap();
        this.bridgeList = bridgeList;
        this.adControlAccessor = adControlAccessor;
        this.webUtils = webUtils;
        this.loggerFactory = loggerFactory;
        this.logger = this.loggerFactory.createMobileAdsLogger(LOGTAG);
        this.androidBuildInfo = androidBuildInfo;
        setupUrlExecutors();
    }

    public void setListener(AdWebViewClientListener listener) {
        this.listener = listener;
    }

    private void setupUrlExecutors() {
        this.urlExecutors.put(AMAZON_MOBILE, new AmazonMobileExecutor(this.context, this.loggerFactory, new AmazonDeviceLauncher(), this.webUtils));
        DefaultExecutor intentExecutor = new DefaultExecutor(this.context);
        Iterator i$ = intentSchemes.iterator();
        while (i$.hasNext()) {
            putUrlExecutor((String) i$.next(), intentExecutor);
        }
    }

    public void putUrlExecutor(String scheme, UrlExecutor executor) {
        this.urlExecutors.put(scheme, executor);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        this.logger.m640e("Error: %s", description);
        super.onReceivedError(view, errorCode, description, failingUrl);
        this.listener.onReceivedError(view, errorCode, description, failingUrl);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return openUrl(url);
    }

    public boolean openUrl(String url) {
        boolean ret = true;
        if (redirectHosts.contains(Uri.parse(url).getHost()) && !isHoneycombVersion()) {
            ret = false;
        }
        if (interpretScheme(url, getScheme(url))) {
            return true;
        }
        return ret;
    }

    protected String getScheme(String url) {
        return this.webUtils.getScheme(url);
    }

    protected boolean interpretScheme(String url, String scheme) {
        if (scheme == null) {
            return false;
        }
        if (scheme.equals("about") && url.equalsIgnoreCase("about:blank")) {
            return false;
        }
        if (this.urlExecutors.containsKey(scheme)) {
            return ((UrlExecutor) this.urlExecutors.get(scheme)).execute(url);
        }
        this.logger.m638d("Scheme %s unrecognized. Launching as intent.", scheme);
        return this.webUtils.launchActivityForIntentLink(url, this.context);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.listener.onPageStarted(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        this.logger.m638d("Page Finished %s", url);
        if (!checkResources()) {
            if (this.listener == null) {
                this.logger.m645w("Call to onPageFinished() ignored because listener is null.");
            } else {
                this.listener.onPageFinished(view, url);
            }
        }
    }

    public void onLoadResource(WebView view, String url) {
        this.resourceList.add(url);
        this.logger.m638d("Loading resource: %s", url);
        this.listener.onLoadResource(view, url);
    }

    private boolean checkResources() {
        boolean shouldReload = false;
        Iterator it = this.resourceList.iterator();
        while (it.hasNext()) {
            Set<AdSDKBridgeFactory> bridges = BridgeSelector.getInstance().getBridgeFactoriesForResourceLoad((String) it.next());
            if (bridges.size() > 0) {
                for (AdSDKBridgeFactory factory : bridges) {
                    AdSDKBridge bridge = factory.createAdSDKBridge(this.adControlAccessor);
                    if (!this.bridgeList.contains(bridge)) {
                        shouldReload = true;
                        this.bridgeList.addBridge(bridge);
                    }
                }
            }
        }
        if (shouldReload) {
            ThreadUtils.executeOnMainThread(new C02941());
        }
        return shouldReload;
    }

    boolean isHoneycombVersion() {
        return AndroidTargetUtils.isBetweenAndroidAPIs(this.androidBuildInfo, 11, 13);
    }
}
