package com.admarvel.android.ads;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import com.admarvel.android.ads.AdMarvelActivity.C0140c;
import com.admarvel.android.ads.AdMarvelActivity.C0152l;
import com.admarvel.android.ads.AdMarvelActivity.C0155n;
import com.admarvel.android.ads.AdMarvelActivity.C0156o;
import com.admarvel.android.ads.AdMarvelActivity.C0157p;
import com.admarvel.android.util.Logging;
import java.lang.ref.WeakReference;

public class AdMarvelBrightrollJSInterface {
    private static WeakReference<AdMarvelActivity> adMarvelActivityReference;
    private final WeakReference<AdMarvelInternalWebView> adMarvelInternalWebViewReference;
    private String videoUrl;

    public AdMarvelBrightrollJSInterface(AdMarvelInternalWebView vw) {
        this.adMarvelInternalWebViewReference = new WeakReference(vw);
    }

    public static void updateActivityContext(AdMarvelActivity activity) {
        adMarvelActivityReference = new WeakReference(activity);
    }

    @JavascriptInterface
    public void browseTo(String url) {
        Logging.log("AdMarvelBrightrollJSInterface - browseTo");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                if (Utils.m191a(adMarvelActivity.getBaseContext(), intent)) {
                    adMarvelActivity.startActivity(intent);
                }
                finish();
            }
        }
    }

    @JavascriptInterface
    public void cleanup() {
        Logging.log("AdMarvelBrightrollJSInterface - cleanup");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
                adMarvelActivity.f93d.post(new C0140c(adMarvelActivity));
            }
        }
    }

    @JavascriptInterface
    public void didPixel(String pixel) {
    }

    @JavascriptInterface
    public void enableVideoCloseInBackground() {
        Logging.log("AdMarvelBrightrollJSInterface - setVideoUrl");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            adMarvelActivity.f109t = true;
            return;
        }
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.f573C = true;
            adMarvelInternalWebView.f583M = false;
        }
    }

    @JavascriptInterface
    public void finish() {
        Logging.log("AdMarvelBrightrollJSInterface - finish");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView == null || !adMarvelInternalWebView.m312b()) {
                adMarvelActivity.m41g();
            }
        }
    }

    @JavascriptInterface
    public void hide() {
        Logging.log("AdMarvelBrightrollJSInterface - hide");
        didPixel("Hide");
        finish();
    }

    @JavascriptInterface
    public void load() {
        Logging.log("AdMarvelBrightrollJSInterface - load");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.m311a(true);
            if (adMarvelActivity == null) {
                adMarvelInternalWebView.f582L = true;
                adMarvelInternalWebView.f583M = false;
            } else if (this.videoUrl != null && this.videoUrl.length() > 0) {
                adMarvelActivity.f93d.post(new C0152l(this.videoUrl, adMarvelActivity, adMarvelInternalWebView));
                adMarvelActivity.f99j = false;
            }
        }
    }

    @JavascriptInterface
    public void onBackPressed() {
        Logging.log("AdMarvelBrightrollJSInterface - onBackPressed");
        finish();
    }

    @JavascriptInterface
    public void play() {
        Logging.log("AdMarvelBrightrollJSInterface - play");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && this.videoUrl != null && this.videoUrl.length() > 0) {
                adMarvelActivity.f93d.post(new C0155n(adMarvelActivity, adMarvelInternalWebView));
            }
        }
    }

    @JavascriptInterface
    public void positionVideo(float xRatio, float yRatio, float wRatio, float hRatio) {
        Logging.log("AdMarvelBrightrollJSInterface - positionVideo");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && this.videoUrl != null && this.videoUrl.length() > 0) {
                adMarvelActivity.f93d.post(new C0156o(adMarvelActivity, adMarvelInternalWebView, xRatio, yRatio, wRatio, hRatio));
            }
        }
    }

    @JavascriptInterface
    public void seekTo(float currentTime) {
        Logging.log("AdMarvelBrightrollJSInterface - seekTo");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity != null) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
            if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b() && this.videoUrl != null && this.videoUrl.length() > 0) {
                adMarvelActivity.f93d.post(new C0157p(adMarvelActivity, adMarvelInternalWebView, currentTime));
            }
        }
    }

    @JavascriptInterface
    public void setVideoUrl(String videoUrl) {
        Logging.log("AdMarvelBrightrollJSInterface - setVideoUrl");
        this.videoUrl = videoUrl;
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && !adMarvelInternalWebView.m312b()) {
            adMarvelInternalWebView.f574D = videoUrl;
            adMarvelInternalWebView.f583M = false;
        }
    }

    @JavascriptInterface
    boolean videoIsLoaded() {
        Logging.log("AdMarvelBrightrollJSInterface - videoIsLoaded");
        AdMarvelActivity adMarvelActivity = adMarvelActivityReference != null ? (AdMarvelActivity) adMarvelActivityReference.get() : null;
        if (adMarvelActivity == null) {
            return false;
        }
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.adMarvelInternalWebViewReference.get();
        if (adMarvelInternalWebView != null && adMarvelInternalWebView.m312b()) {
            return false;
        }
        return adMarvelActivity.f97h != null;
    }
}
