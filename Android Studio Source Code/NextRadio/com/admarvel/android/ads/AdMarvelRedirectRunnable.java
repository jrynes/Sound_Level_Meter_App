package com.admarvel.android.ads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.webkit.URLUtil;
import com.admarvel.android.ads.Utils.C0250s;
import com.admarvel.android.util.Logging;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

public class AdMarvelRedirectRunnable implements Runnable {
    private final String GUID;
    private final String WEBVIEW_GUID;
    private final AdMarvelAd adMarvelAd;
    private final String adType;
    private final boolean isClickRedirectEnabled;
    private final boolean isOfflineSDK;
    private final boolean isPostitialView;
    private final WeakReference<Context> mContextReference;
    private final String url;

    /* renamed from: com.admarvel.android.ads.AdMarvelRedirectRunnable.1 */
    class C01761 implements Runnable {
        final /* synthetic */ Context f234a;
        final /* synthetic */ Intent f235b;
        final /* synthetic */ AdMarvelRedirectRunnable f236c;

        C01761(AdMarvelRedirectRunnable adMarvelRedirectRunnable, Context context, Intent intent) {
            this.f236c = adMarvelRedirectRunnable;
            this.f234a = context;
            this.f235b = intent;
        }

        public void run() {
            this.f234a.startActivity(this.f235b);
        }
    }

    public AdMarvelRedirectRunnable(String url, Context context, AdMarvelAd ad, String adType, String guid, boolean isClickRedirectEnabled, boolean isOfflineSDK, boolean isPostitialView, String WEBVIEW_GUID) {
        this.url = url;
        this.mContextReference = new WeakReference(context);
        this.adMarvelAd = ad;
        this.adType = adType;
        this.GUID = guid;
        this.isClickRedirectEnabled = isClickRedirectEnabled;
        this.isOfflineSDK = isOfflineSDK;
        this.isPostitialView = isPostitialView;
        this.WEBVIEW_GUID = WEBVIEW_GUID;
    }

    public void run() {
        Context context = (Context) this.mContextReference.get();
        if (context != null) {
            Intent intent;
            if (this.isClickRedirectEnabled) {
                try {
                    String offlinekeyUrl;
                    if (this.url != null && this.isOfflineSDK && !Utils.m211g(this.url)) {
                        if (URLUtil.isNetworkUrl(this.url) && Utils.m193a(context, this.url, this.isPostitialView)) {
                            if (this.adType.equals("banner")) {
                                if (AdMarvelWebView.m451a(this.GUID) != null) {
                                    AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, this.url);
                                    return;
                                }
                                return;
                            } else if (this.adType.equals("interstitial")) {
                                intent = new Intent();
                                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                                intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                                intent.putExtra("callback", Promotion.ACTION_CLICK);
                                context.sendBroadcast(intent);
                                return;
                            }
                        }
                        offlinekeyUrl = this.adMarvelAd.getOfflinekeyUrl();
                        if (Utils.m193a(context, offlinekeyUrl.substring(0, offlinekeyUrl.lastIndexOf(ReadOnlyContext.SEPARATOR)) + ReadOnlyContext.SEPARATOR + this.url, this.isPostitialView)) {
                            if (this.adType.equals("banner")) {
                                if (AdMarvelWebView.m451a(this.GUID) != null) {
                                    AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, this.url);
                                    return;
                                }
                                return;
                            } else if (this.adType.equals("interstitial")) {
                                intent = new Intent();
                                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                                intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                                intent.putExtra("callback", Promotion.ACTION_CLICK);
                                context.sendBroadcast(intent);
                                return;
                            }
                        }
                    } else if (!(this.url == null || Utils.m211g(this.url) || !Utils.m193a(context, this.url, this.isPostitialView))) {
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                        }
                        if (this.adType.equals("banner")) {
                            if (AdMarvelWebView.m451a(this.GUID) != null) {
                                AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, this.url);
                                return;
                            }
                            return;
                        } else if (this.adType.equals("interstitial")) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            context.sendBroadcast(intent);
                            return;
                        }
                    }
                    if (this.url != null && Utils.m179a(this.url, "admarvelsdk") != C0250s.NONE) {
                        if (this.adType.equals("banner")) {
                            if (AdMarvelWebView.m451a(this.GUID) != null) {
                                AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, Utils.m184a(this.url, "admarvelsdk", Stomp.EMPTY, Utils.m179a(this.url, "admarvelsdk"), context));
                            }
                        } else if (this.adType.equals("interstitial")) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, Utils.m184a(this.url, "admarvelsdk", Stomp.EMPTY, Utils.m179a(this.url, "admarvelsdk"), context));
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            context.sendBroadcast(intent);
                        }
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                            return;
                        }
                        return;
                    } else if (this.url != null && Utils.m179a(this.url, "admarvelinternal") != C0250s.NONE) {
                        if (this.adType.equals("banner")) {
                            if (AdMarvelWebView.m451a(this.GUID) != null) {
                                AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, Utils.m184a(this.url, "admarvelinternal", Stomp.EMPTY, Utils.m179a(this.url, "admarvelinternal"), context));
                            }
                        } else if (this.adType.equals("interstitial")) {
                            intent = new Intent();
                            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                            intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, Utils.m184a(this.url, "admarvelinternal", Stomp.EMPTY, Utils.m179a(this.url, "admarvelinternal"), context));
                            intent.putExtra("callback", Promotion.ACTION_CLICK);
                            context.sendBroadcast(intent);
                        }
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                            return;
                        }
                        return;
                    } else if (Utils.m220l(context) && this.url != null && Utils.m179a(this.url, "admarvelvideo") != C0250s.NONE) {
                        offlinekeyUrl = Utils.m184a(this.url, "admarvelvideo", "http://", Utils.m179a(this.url, "admarvelvideo"), context);
                        r2 = new Intent("android.intent.action.VIEW");
                        r2.addFlags(268435456);
                        r2.setDataAndType(Uri.parse(offlinekeyUrl), "video/*");
                        if (Utils.m191a(context, r2)) {
                            context.startActivity(r2);
                        }
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                        }
                    } else if (Utils.m220l(context) && this.url != null && Utils.m179a(this.url, "admarvelexternal") != C0250s.NONE) {
                        intent = new Intent("android.intent.action.VIEW", Uri.parse(Utils.m184a(this.url, "admarvelexternal", Stomp.EMPTY, Utils.m179a(this.url, "admarvelexternal"), context)));
                        intent.addFlags(268435456);
                        if (Utils.m191a(context, intent)) {
                            context.startActivity(intent);
                        }
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                        }
                    } else if (Utils.m220l(context) && this.url != null && Utils.m179a(this.url, "admarvelcustomvideo") != C0250s.NONE) {
                        r2 = new Intent(context, AdMarvelVideoActivity.class);
                        r2.addFlags(268435456);
                        if (!this.adType.equals("native")) {
                            this.adMarvelAd.removeNonStringEntriesTargetParam();
                            try {
                                r1 = new ByteArrayOutputStream();
                                r3 = new ObjectOutputStream(r1);
                                r3.writeObject(this.adMarvelAd);
                                r3.close();
                                r2.putExtra("serialized_admarvelad", r1.toByteArray());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            r2.putExtra("xml", this.adMarvelAd.getXml());
                            r2.putExtra(locationTracking.source, this.adMarvelAd.getSource());
                        }
                        r2.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                        r2.putExtra("isCustomUrl", true);
                        r2.putExtra("GUID", this.GUID);
                        try {
                            new Handler(Looper.getMainLooper()).post(new C01761(this, context, r2));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                        }
                    } else if (Utils.m220l(context) && this.url != null && this.url.length() > 0) {
                        if (this.adType.equals("banner")) {
                            intent = this.isPostitialView ? new Intent(context, AdMarvelPostitialActivity.class) : new Intent(context, AdMarvelActivity.class);
                            intent.addFlags(268435456);
                            if (this.isPostitialView) {
                                intent.addFlags(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
                            }
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                            intent.putExtra("isInterstitial", false);
                            intent.putExtra("isInterstitialClick", false);
                            intent.putExtra("xml", this.adMarvelAd.getXml());
                            r2 = intent;
                        } else {
                            intent = new Intent(context, AdMarvelActivity.class);
                            intent.addFlags(268435456);
                            intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                            intent.putExtra("isInterstitial", false);
                            if (this.adType.equals("native")) {
                                intent.putExtra("isInterstitialClick", false);
                                r2 = intent;
                            } else {
                                intent.putExtra("isInterstitialClick", true);
                                intent.putExtra("xml", this.adMarvelAd.getXml());
                                r2 = intent;
                            }
                        }
                        r2.putExtra("GUID", this.GUID);
                        r2.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                        if (!this.adType.equals("native")) {
                            this.adMarvelAd.removeNonStringEntriesTargetParam();
                            try {
                                r1 = new ByteArrayOutputStream();
                                r3 = new ObjectOutputStream(r1);
                                r3.writeObject(this.adMarvelAd);
                                r3.close();
                                r2.putExtra("serialized_admarvelad", r1.toByteArray());
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                            if (this.adMarvelAd.getSource() != null) {
                                r2.putExtra(locationTracking.source, this.adMarvelAd.getSource());
                            }
                        }
                        context.startActivity(r2);
                        if (!this.adType.equals("native")) {
                            new Utils(context).m247c(this.adMarvelAd.getXml());
                        }
                    }
                } catch (Exception e22) {
                    Logging.log(e22.getMessage() + "Exception in RedirectRunnable ");
                }
            }
            if (this.adType.equals("banner")) {
                if (AdMarvelWebView.m451a(this.GUID) != null) {
                    AdMarvelWebView.m451a(this.GUID).m152a(this.adMarvelAd, this.url);
                }
            } else if (this.adType.equals("interstitial")) {
                intent = new Intent();
                intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                intent.putExtra("WEBVIEW_GUID", this.WEBVIEW_GUID);
                intent.putExtra(SettingsJsonConstants.APP_URL_KEY, this.url);
                intent.putExtra("callback", Promotion.ACTION_CLICK);
                context.sendBroadcast(intent);
            }
        }
    }
}
