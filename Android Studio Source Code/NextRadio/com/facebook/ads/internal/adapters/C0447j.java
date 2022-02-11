package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.InterstitialAdActivity.Type;
import com.facebook.ads.internal.util.C0519f;
import com.facebook.ads.internal.util.C0523h;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.j */
public class C0447j extends InterstitialAdapter {
    private final String f1604a;
    private Context f1605b;
    private boolean f1606c;
    private int f1607d;
    private int f1608e;
    private boolean f1609f;
    private C0434d f1610g;
    private InterstitialAdapterListener f1611h;
    private boolean f1612i;
    private C0450l f1613j;
    private WebView f1614k;

    /* renamed from: com.facebook.ads.internal.adapters.j.1 */
    class C04461 extends WebChromeClient {
        final /* synthetic */ C0447j f1603a;

        C04461(C0447j c0447j) {
            this.f1603a = c0447j;
        }

        public void onProgressChanged(WebView webView, int i) {
            if (i == 100 && this.f1603a.f1611h != null) {
                this.f1603a.f1611h.onInterstitialAdLoaded(this.f1603a);
            }
        }
    }

    public C0447j() {
        this.f1604a = UUID.randomUUID().toString();
        this.f1612i = false;
    }

    private void m1223a(C0450l c0450l) {
        if (c0450l != null) {
            this.f1614k = new WebView(this.f1605b.getApplicationContext());
            this.f1614k.setWebChromeClient(new C04461(this));
            this.f1614k.loadDataWithBaseURL(C0523h.m1543a(), c0450l.m1285d(), WebRequest.CONTENT_TYPE_HTML, "utf-8", null);
        }
    }

    public void loadInterstitialAd(Context context, InterstitialAdapterListener interstitialAdapterListener, Map<String, Object> map) {
        this.f1605b = context;
        this.f1611h = interstitialAdapterListener;
        this.f1613j = C0450l.m1279a((JSONObject) map.get(MPDbAdapter.KEY_DATA));
        if (C0519f.m1525a(context, this.f1613j)) {
            interstitialAdapterListener.onInterstitialError(this, AdError.NO_FILL);
            return;
        }
        this.f1610g = new C0434d(context, this.f1604a, this, this.f1611h);
        this.f1610g.m1182a();
        this.f1612i = true;
        Map h = this.f1613j.m1289h();
        if (h.containsKey("is_tablet")) {
            this.f1606c = Boolean.parseBoolean((String) h.get("is_tablet"));
        }
        if (h.containsKey("ad_height")) {
            this.f1607d = Integer.parseInt((String) h.get("ad_height"));
        }
        if (h.containsKey("ad_width")) {
            this.f1608e = Integer.parseInt((String) h.get("ad_width"));
        }
        if (h.containsKey("native_close")) {
            this.f1609f = Boolean.valueOf((String) h.get("native_close")).booleanValue();
        }
        if (h.containsKey("preloadMarkup") && Boolean.parseBoolean((String) h.get("preloadMarkup"))) {
            m1223a(this.f1613j);
        } else if (this.f1611h != null) {
            this.f1611h.onInterstitialAdLoaded(this);
        }
    }

    public void onDestroy() {
        if (this.f1610g != null) {
            this.f1610g.m1183b();
        }
        if (this.f1614k != null) {
            C0523h.m1547a(this.f1614k);
            this.f1614k.destroy();
            this.f1614k = null;
        }
    }

    public boolean show() {
        if (this.f1612i) {
            Intent intent = new Intent(this.f1605b, InterstitialAdActivity.class);
            this.f1613j.m1282a(intent);
            Display defaultDisplay = ((WindowManager) this.f1605b.getSystemService("window")).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            intent.putExtra("displayRotation", defaultDisplay.getRotation());
            intent.putExtra("displayWidth", displayMetrics.widthPixels);
            intent.putExtra("displayHeight", displayMetrics.heightPixels);
            intent.putExtra("isTablet", this.f1606c);
            intent.putExtra("adHeight", this.f1607d);
            intent.putExtra("adWidth", this.f1608e);
            intent.putExtra("adInterstitialUniqueId", this.f1604a);
            intent.putExtra("useNativeCloseButton", this.f1609f);
            intent.putExtra(InterstitialAdActivity.VIEW_TYPE, Type.DISPLAY);
            intent.addFlags(268435456);
            this.f1605b.startActivity(intent);
            return true;
        }
        if (this.f1611h != null) {
            this.f1611h.onInterstitialError(this, AdError.INTERNAL_ERROR);
        }
        return false;
    }
}
