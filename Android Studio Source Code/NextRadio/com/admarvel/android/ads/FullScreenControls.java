package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.admarvel.android.util.AdMarvelBitmapDrawableUtils;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;

/* renamed from: com.admarvel.android.ads.r */
public class FullScreenControls extends LinearLayout {
    static int f903a;
    final String f904b;
    private final WeakReference<Activity> f905c;
    private boolean f906d;

    /* renamed from: com.admarvel.android.ads.r.1 */
    class FullScreenControls implements OnClickListener {
        final /* synthetic */ FullScreenControls f895a;

        FullScreenControls(FullScreenControls fullScreenControls) {
            this.f895a = fullScreenControls;
        }

        public void onClick(View v) {
            Activity activity = (Activity) this.f895a.f905c.get();
            if (activity != null && (activity instanceof AdMarvelActivity)) {
                AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
                if (adMarvelActivity.m37c()) {
                    Intent intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                    intent.putExtra("WEBVIEW_GUID", adMarvelActivity.f95f);
                    adMarvelActivity.getApplicationContext().sendBroadcast(intent);
                    intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                    intent.putExtra("WEBVIEW_GUID", adMarvelActivity.f95f);
                    intent.putExtra("callback", "close");
                    adMarvelActivity.sendBroadcast(intent);
                } else {
                    adMarvelActivity.finish();
                }
                if (AdMarvelInternalWebView.m288d(this.f895a.f904b) != null) {
                    AdMarvelInternalWebView.m288d(this.f895a.f904b).m15a(this.f895a.f904b);
                }
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.r.2 */
    class FullScreenControls implements OnClickListener {
        final /* synthetic */ FullScreenControls f896a;

        FullScreenControls(FullScreenControls fullScreenControls) {
            this.f896a = fullScreenControls;
        }

        public void onClick(View v) {
            this.f896a.m488b();
        }
    }

    /* renamed from: com.admarvel.android.ads.r.3 */
    class FullScreenControls implements OnClickListener {
        final /* synthetic */ FullScreenControls f897a;

        FullScreenControls(FullScreenControls fullScreenControls) {
            this.f897a = fullScreenControls;
        }

        public void onClick(View v) {
            this.f897a.m490c();
        }
    }

    /* renamed from: com.admarvel.android.ads.r.4 */
    class FullScreenControls implements OnClickListener {
        final /* synthetic */ FullScreenControls f898a;

        FullScreenControls(FullScreenControls fullScreenControls) {
            this.f898a = fullScreenControls;
        }

        public void onClick(View v) {
            this.f898a.m492d();
        }
    }

    /* renamed from: com.admarvel.android.ads.r.a */
    public class FullScreenControls extends Button {
        final /* synthetic */ FullScreenControls f902a;

        /* renamed from: com.admarvel.android.ads.r.a.a */
        protected class FullScreenControls extends LayerDrawable {
            protected int f899a;
            protected ColorFilter f900b;
            final /* synthetic */ FullScreenControls f901c;

            public FullScreenControls(FullScreenControls fullScreenControls, Drawable drawable) {
                this.f901c = fullScreenControls;
                super(new Drawable[]{drawable});
                this.f899a = 100;
                this.f900b = new LightingColorFilter(3768514, 1);
            }

            public boolean isStateful() {
                return true;
            }

            protected boolean onStateChange(int[] states) {
                Object obj = null;
                Object obj2 = null;
                for (int i : states) {
                    if (i == 16842910) {
                        obj2 = 1;
                    } else if (i == 16842919) {
                        int i2 = 1;
                    }
                }
                if (obj2 != null && r0 != null) {
                    setColorFilter(this.f900b);
                } else if (obj2 != null) {
                    setColorFilter(null);
                    setAlpha(Type.ANY);
                } else if (obj2 == null) {
                    setColorFilter(null);
                    setAlpha(this.f899a);
                } else {
                    setColorFilter(null);
                }
                invalidateSelf();
                return super.onStateChange(states);
            }
        }

        public FullScreenControls(FullScreenControls fullScreenControls, Context context) {
            this.f902a = fullScreenControls;
            super(context);
        }

        public void setBackgroundDrawable(Drawable d) {
            if (d != null) {
                super.setBackgroundDrawable(new FullScreenControls(this, d));
            }
        }
    }

    static {
        f903a = 100001;
    }

    @SuppressLint({"NewApi"})
    FullScreenControls(Activity activity, Context context, String str, boolean z) {
        super(context);
        this.f906d = false;
        this.f905c = new WeakReference(activity);
        this.f904b = str;
        this.f906d = z;
        setId(f903a);
        setGravity(17);
        setOrientation(1);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(12);
        setLayoutParams(layoutParams);
        LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
        Drawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setColor(Color.parseColor("#33B5E5"));
        Drawable clipDrawable = new ClipDrawable(shapeDrawable, 3, 1);
        View progressBar = new ProgressBar(context, null, 16842872);
        progressBar.setLayoutParams(layoutParams2);
        progressBar.setTag(this.f904b + "PROGRESS_BAR");
        progressBar.setBackgroundColor(0);
        progressBar.setProgressDrawable(clipDrawable);
        progressBar.setVisibility(8);
        addView(progressBar);
        if (this.f906d) {
            float applyDimension = TypedValue.applyDimension(1, 34.0f, getResources().getDisplayMetrics());
            layoutParams2 = new LinearLayout.LayoutParams(-2, m486a(36.0f));
            layoutParams2.weight = 25.0f;
            layoutParams2.gravity = 17;
            LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) applyDimension, (int) applyDimension);
            layoutParams3.addRule(13);
            progressBar = new FullScreenControls(this, context);
            progressBar.setTag(this.f904b + "CLOSE_BUTTON");
            progressBar.setLayoutParams(layoutParams3);
            View relativeLayout = new RelativeLayout(context);
            relativeLayout.setTag(this.f904b + "CLOSE_BUTTON_LAYOUT");
            relativeLayout.setLayoutParams(layoutParams2);
            relativeLayout.addView(progressBar);
            progressBar.setOnClickListener(new FullScreenControls(this));
            AdMarvelBitmapDrawableUtils.getBitMapDrawable("done", context, progressBar);
            progressBar = new FullScreenControls(this, context);
            progressBar.setTag(this.f904b + "BACK_BUTTON");
            progressBar.setOnClickListener(new FullScreenControls(this));
            progressBar.setLayoutParams(layoutParams3);
            progressBar.setEnabled(false);
            AdMarvelBitmapDrawableUtils.getBitMapDrawable("backward", getContext(), progressBar);
            View relativeLayout2 = new RelativeLayout(context);
            relativeLayout2.setTag(this.f904b + "BACK_BUTTON_LAYOUT");
            relativeLayout2.setLayoutParams(layoutParams2);
            relativeLayout2.addView(progressBar);
            progressBar = new FullScreenControls(this, context);
            progressBar.setTag(this.f904b + "FORWARD_BUTTON");
            progressBar.setLayoutParams(layoutParams3);
            progressBar.setEnabled(false);
            View relativeLayout3 = new RelativeLayout(context);
            progressBar.setOnClickListener(new FullScreenControls(this));
            AdMarvelBitmapDrawableUtils.getBitMapDrawable("resume", getContext(), progressBar);
            relativeLayout3.setTag(this.f904b + "FORWARD_BUTTON_LAYOUT");
            relativeLayout3.setLayoutParams(layoutParams2);
            relativeLayout3.addView(progressBar);
            progressBar = new FullScreenControls(this, context);
            progressBar.setTag(this.f904b + "BROWSER_BUTTON");
            progressBar.setOnClickListener(new FullScreenControls(this));
            progressBar.setLayoutParams(layoutParams3);
            AdMarvelBitmapDrawableUtils.getBitMapDrawable("open_url", getContext(), progressBar);
            View relativeLayout4 = new RelativeLayout(context);
            relativeLayout4.setTag(this.f904b + "BROWSER_BUTTON_LAYOUT");
            relativeLayout4.setLayoutParams(layoutParams2);
            relativeLayout4.addView(progressBar);
            applyDimension = TypedValue.applyDimension(1, 40.0f, getResources().getDisplayMetrics());
            Drawable shapeDrawable2 = new ShapeDrawable(new RectShape());
            shapeDrawable2.getPaint().setColor(Color.parseColor("#FF1D1C1D"));
            LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, (int) applyDimension);
            progressBar = new LinearLayout(context);
            progressBar.setGravity(17);
            progressBar.setTag(this.f904b + "BUTTON_BAR_LAYOUT");
            progressBar.setLayoutParams(layoutParams4);
            progressBar.setOrientation(0);
            progressBar.setWeightSum(100.0f);
            progressBar.setBackgroundDrawable(shapeDrawable2);
            progressBar.addView(relativeLayout);
            progressBar.addView(relativeLayout2);
            progressBar.addView(relativeLayout3);
            progressBar.addView(relativeLayout4);
            addView(progressBar);
        }
    }

    private int m486a(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private void m488b() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) ((RelativeLayout) getParent()).findViewWithTag(this.f904b + "WEBVIEW");
        if (adMarvelInternalWebView.canGoBack()) {
            adMarvelInternalWebView.goBack();
            m494a();
        }
    }

    private void m490c() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) ((RelativeLayout) getParent()).findViewWithTag(this.f904b + "WEBVIEW");
        if (adMarvelInternalWebView.canGoForward()) {
            adMarvelInternalWebView.goForward();
            m494a();
        }
    }

    private void m492d() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(((AdMarvelInternalWebView) ((RelativeLayout) getParent()).findViewWithTag(this.f904b + "WEBVIEW")).getUrl()));
        intent.addFlags(268435456);
        Activity activity = (Activity) this.f905c.get();
        if (activity != null && (activity instanceof AdMarvelActivity)) {
            AdMarvelActivity adMarvelActivity = (AdMarvelActivity) activity;
            if (Utils.m191a(getContext(), intent)) {
                adMarvelActivity.startActivity(intent);
            }
            if (adMarvelActivity.m37c()) {
                Intent intent2 = new Intent();
                intent2.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_STATE_INTENT);
                intent.putExtra("WEBVIEW_GUID", adMarvelActivity.f95f);
                adMarvelActivity.sendBroadcast(intent2);
                intent2 = new Intent();
                intent2.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                intent2.putExtra("callback", "close");
                intent.putExtra("WEBVIEW_GUID", adMarvelActivity.f95f);
                adMarvelActivity.sendBroadcast(intent2);
                adMarvelActivity.m36b();
            }
            if (adMarvelActivity.m34a() > 3) {
                adMarvelActivity.finish();
            } else {
                adMarvelActivity.finish();
            }
        }
    }

    void m494a() {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) ((RelativeLayout) getParent()).findViewWithTag(this.f904b + "WEBVIEW");
        if (this.f906d) {
            FullScreenControls fullScreenControls = (FullScreenControls) findViewWithTag(this.f904b + "BACK_BUTTON_LAYOUT").findViewWithTag(this.f904b + "BACK_BUTTON");
            FullScreenControls fullScreenControls2 = (FullScreenControls) findViewWithTag(this.f904b + "FORWARD_BUTTON_LAYOUT").findViewWithTag(this.f904b + "FORWARD_BUTTON");
            if (adMarvelInternalWebView.canGoBack()) {
                fullScreenControls.setEnabled(true);
            } else {
                fullScreenControls.setEnabled(false);
            }
            if (adMarvelInternalWebView.canGoForward()) {
                fullScreenControls2.setEnabled(true);
            } else {
                fullScreenControls2.setEnabled(false);
            }
            Map adMarvelOptionalFlags = AdMarvelUtils.getAdMarvelOptionalFlags();
            fullScreenControls = (FullScreenControls) findViewWithTag(this.f904b + "BROWSER_BUTTON_LAYOUT").findViewWithTag(this.f904b + "BROWSER_BUTTON");
            String url = adMarvelInternalWebView.getUrl();
            if (url == null || !URLUtil.isNetworkUrl(url)) {
                fullScreenControls.setEnabled(false);
            } else {
                fullScreenControls.setEnabled(true);
            }
            if (adMarvelOptionalFlags != null && adMarvelOptionalFlags.containsKey("AP_TOOL_DISABLE_EXTERNAL_BROWSER_ICON") && ((String) adMarvelOptionalFlags.get("AP_TOOL_DISABLE_EXTERNAL_BROWSER_ICON")).equals(Stomp.TRUE)) {
                fullScreenControls.setEnabled(false);
                fullScreenControls.setVisibility(8);
            }
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == 0) {
            m494a();
        }
    }
}
