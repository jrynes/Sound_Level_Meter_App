package com.facebook.ads.internal.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.ads.AdError;
import org.apache.activemq.transport.stomp.Stomp.Headers;

/* renamed from: com.facebook.ads.internal.adapters.d */
public class C0434d extends BroadcastReceiver {
    private String f1562a;
    private Context f1563b;
    private InterstitialAdapterListener f1564c;
    private InterstitialAdapter f1565d;

    public C0434d(Context context, String str, InterstitialAdapter interstitialAdapter, InterstitialAdapterListener interstitialAdapterListener) {
        this.f1563b = context;
        this.f1562a = str;
        this.f1564c = interstitialAdapterListener;
        this.f1565d = interstitialAdapter;
    }

    public void m1182a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.ads.interstitial.impression.logged:" + this.f1562a);
        intentFilter.addAction("com.facebook.ads.interstitial.displayed:" + this.f1562a);
        intentFilter.addAction("com.facebook.ads.interstitial.dismissed:" + this.f1562a);
        intentFilter.addAction("com.facebook.ads.interstitial.clicked:" + this.f1562a);
        intentFilter.addAction("com.facebook.ads.interstitial.error:" + this.f1562a);
        LocalBroadcastManager.getInstance(this.f1563b).registerReceiver(this, intentFilter);
    }

    public void m1183b() {
        try {
            LocalBroadcastManager.getInstance(this.f1563b).unregisterReceiver(this);
        } catch (Exception e) {
        }
    }

    public void onReceive(Context context, Intent intent) {
        Object obj = intent.getAction().split(Headers.SEPERATOR)[0];
        if (this.f1564c != null && obj != null) {
            if ("com.facebook.ads.interstitial.clicked".equals(obj)) {
                this.f1564c.onInterstitialAdClicked(this.f1565d, intent.getStringExtra("com.facebook.ads.interstitial.ad.click.url"), intent.getBooleanExtra("com.facebook.ads.interstitial.ad.player.handles.click", true));
            } else if ("com.facebook.ads.interstitial.dismissed".equals(obj)) {
                this.f1564c.onInterstitialAdDismissed(this.f1565d);
            } else if ("com.facebook.ads.interstitial.displayed".equals(obj)) {
                this.f1564c.onInterstitialAdDisplayed(this.f1565d);
            } else if ("com.facebook.ads.interstitial.impression.logged".equals(obj)) {
                this.f1564c.onInterstitialLoggingImpression(this.f1565d);
            } else if ("com.facebook.ads.interstitial.error".equals(obj)) {
                this.f1564c.onInterstitialError(this.f1565d, AdError.INTERNAL_ERROR);
            }
        }
    }
}
