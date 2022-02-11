package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelActivity.C0146i;
import com.admarvel.android.ads.AdMarvelActivity.C0158q;
import com.admarvel.android.ads.AdMarvelUtils.SDKAdNetwork;
import com.admarvel.android.ads.AdMarvelWebView.AdMarvelWebView;
import com.admarvel.android.util.AdMarvelBitmapDrawableUtils;
import com.admarvel.android.util.Logging;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import org.apache.activemq.transport.stomp.Stomp;

@SuppressLint({"NewApi"})
public class AdMarvelMediationActivity extends Activity {
    private static String f224i;
    private static boolean f225j;
    final Handler f226a;
    private RelativeLayout f227b;
    private AdMarvelAd f228c;
    private SDKAdNetwork f229d;
    private String f230e;
    private AdMarvelAdapter f231f;
    private boolean f232g;
    private String f233h;

    /* renamed from: com.admarvel.android.ads.AdMarvelMediationActivity.a */
    static class C0173a extends LinearLayout {
        boolean f213a;

        /* renamed from: com.admarvel.android.ads.AdMarvelMediationActivity.a.1 */
        class C01721 implements OnClickListener {
            final /* synthetic */ Context f211a;
            final /* synthetic */ C0173a f212b;

            C01721(C0173a c0173a, Context context) {
                this.f212b = c0173a;
                this.f211a = context;
            }

            public void onClick(View v) {
                if (this.f211a instanceof AdMarvelMediationActivity) {
                    ((AdMarvelMediationActivity) this.f211a).m48a();
                }
            }
        }

        public C0173a(Context context, boolean z) {
            super(context);
            this.f213a = z;
            m45a(context);
        }

        private void m45a(Context context) {
            setBackgroundColor(0);
            LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.m176a(50.0f, context), Utils.m176a(50.0f, context));
            layoutParams.weight = 1.0f;
            layoutParams.width = 0;
            layoutParams.gravity = 17;
            setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.weight = 25.0f;
            layoutParams2.gravity = 17;
            float applyDimension = TypedValue.applyDimension(1, 36.0f, getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) applyDimension, (int) applyDimension);
            layoutParams3.addRule(13);
            m46a(context, layoutParams3, layoutParams, this.f213a);
        }

        private void m46a(Context context, RelativeLayout.LayoutParams layoutParams, LinearLayout.LayoutParams layoutParams2, boolean z) {
            View imageView = new ImageView(context);
            imageView.setLayoutParams(layoutParams);
            imageView.setTag("BTN_CLOSE_IMAGE");
            if (z) {
                imageView.setBackgroundColor(0);
            } else {
                AdMarvelBitmapDrawableUtils.getBitMapDrawable("close", context, imageView);
            }
            View relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(layoutParams2);
            relativeLayout.addView(imageView);
            relativeLayout.setOnClickListener(new C01721(this, context));
            addView(relativeLayout);
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelMediationActivity.b */
    static class C0174b implements Runnable {
        private final WeakReference<AdMarvelMediationActivity> f214a;

        public C0174b(AdMarvelMediationActivity adMarvelMediationActivity) {
            this.f214a = new WeakReference(adMarvelMediationActivity);
        }

        public void run() {
            AdMarvelMediationActivity adMarvelMediationActivity = (AdMarvelMediationActivity) this.f214a.get();
            if (adMarvelMediationActivity != null) {
                adMarvelMediationActivity.m48a();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.AdMarvelMediationActivity.c */
    static class C0175c implements Runnable {
        private int f215a;
        private int f216b;
        private int f217c;
        private int f218d;
        private String f219e;
        private boolean f220f;
        private boolean f221g;
        private String f222h;
        private AdMarvelMediationActivity f223i;

        public C0175c(int i, int i2, int i3, int i4, String str, boolean z, boolean z2, String str2, AdMarvelMediationActivity adMarvelMediationActivity) {
            this.f215a = 0;
            this.f216b = 0;
            this.f217c = 0;
            this.f218d = 0;
            this.f215a = i3;
            this.f217c = i;
            this.f218d = i2;
            this.f216b = i4;
            this.f219e = str;
            this.f220f = z;
            this.f221g = z2;
            this.f222h = str2;
            this.f223i = adMarvelMediationActivity;
        }

        public void run() {
            this.f223i.m49a(this.f217c, this.f218d, this.f215a, this.f216b, this.f219e, this.f220f, this.f221g, this.f222h);
        }
    }

    static {
        f224i = Stomp.EMPTY;
        f225j = false;
    }

    public AdMarvelMediationActivity() {
        this.f231f = null;
        this.f226a = new Handler();
    }

    private static void m47a(LinearLayout linearLayout, RelativeLayout.LayoutParams layoutParams, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        layoutParams.rightMargin = 0;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.bottomMargin = 0;
        if (i3 == -1) {
            i3 = i5;
        }
        if (i4 == -1) {
            i4 = i6;
        }
        int i8;
        if (str == null || str.length() <= 0) {
            layoutParams.addRule(11);
            layoutParams.addRule(10);
            i8 = (i + i3) - i5;
            if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("top-right".equals(str)) {
            layoutParams.addRule(10);
            layoutParams.addRule(11);
            i8 = (i + i3) - i5;
            if (i == 0 && i8 > 0) {
                layoutParams.rightMargin = i8 / 2;
            } else if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("top-left".equals(str)) {
            layoutParams.addRule(9);
            layoutParams.addRule(10);
            if (i < 0) {
                layoutParams.leftMargin = Math.abs(i);
            } else if (i == 0) {
                i8 = (i3 - i5) / 2;
                if (i8 > 0) {
                    layoutParams.leftMargin = i8;
                }
            } else {
                layoutParams.leftMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        } else if ("bottom-right".equals(str)) {
            layoutParams.addRule(11);
            layoutParams.addRule(12);
            i8 = (i + i3) - i5;
            if (i == 0 && i8 > 0) {
                layoutParams.rightMargin = i8 / 2;
            } else if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
        } else if ("bottom-left".equals(str)) {
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            if (i < 0) {
                layoutParams.leftMargin = Math.abs(i);
            } else if (i == 0) {
                i8 = (i3 - i5) / 2;
                if (i8 > 0) {
                    layoutParams.leftMargin = i8;
                }
            } else {
                layoutParams.leftMargin = 0;
            }
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
        } else if ("center".equals(str)) {
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
            layoutParams.addRule(15);
            if (i2 < 0) {
                i8 = (i4 / 2) + i2;
                if (i8 < 0) {
                    layoutParams.topMargin = Math.abs(i8) + (i4 / 2);
                    layoutParams.addRule(10);
                } else {
                    layoutParams.topMargin = 0;
                    layoutParams.addRule(15);
                }
            } else {
                i8 = i6 - ((i4 / 2) + i2);
                if (i8 < 0) {
                    layoutParams.bottomMargin = Math.abs(i8) + (i4 / 2);
                    layoutParams.addRule(12);
                } else {
                    layoutParams.bottomMargin = 0;
                    layoutParams.addRule(15);
                }
            }
        } else if ("bottom-center".equals(str)) {
            i8 = i6 - (i2 + i4);
            if (i8 < 0) {
                layoutParams.bottomMargin = Math.abs(i8);
            } else {
                layoutParams.bottomMargin = 0;
            }
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
        } else if ("top-center".equals(str)) {
            layoutParams.addRule(14);
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
            if (i + i3 <= i5) {
                if (i <= 0) {
                    layoutParams.leftMargin = (((i + i3) / 2) + Math.abs(i)) - (i7 / 2);
                } else {
                    layoutParams.leftMargin = (i3 / 2) - (i7 / 2);
                }
            } else if (i <= 0) {
                layoutParams.leftMargin = ((i5 / 2) + Math.abs(i)) - (i7 / 2);
            } else {
                layoutParams.leftMargin = ((i5 - i) / 2) - (i7 / 2);
            }
        } else {
            layoutParams.addRule(11);
            layoutParams.addRule(10);
            i8 = (i + i3) - i5;
            if (i8 > 0) {
                layoutParams.rightMargin = i8;
            } else {
                layoutParams.rightMargin = 0;
            }
            if (i2 < 0) {
                layoutParams.topMargin = Math.abs(i2);
            } else {
                layoutParams.topMargin = 0;
            }
        }
        linearLayout.setLayoutParams(layoutParams);
    }

    void m48a() {
        try {
            ViewGroup viewGroup = (ViewGroup) getWindow().findViewById(16908290);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) viewGroup.findViewWithTag(f224i + "INTERNAL");
            RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewWithTag(f224i + "EXPAND_LAYOUT");
            if (relativeLayout != null) {
                if (this.f232g) {
                    LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(f224i + "BTN_CLOSE");
                    if (linearLayout != null) {
                        relativeLayout.removeView(linearLayout);
                    }
                }
                relativeLayout.removeView(adMarvelInternalWebView);
            }
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) viewGroup.findViewWithTag(f224i + "EXPAND_BG");
            if (!(relativeLayout == null || adMarvelWebView == null)) {
                adMarvelWebView.removeView(relativeLayout);
                adMarvelWebView.m439a();
                viewGroup.removeView(adMarvelWebView);
            }
            if (adMarvelInternalWebView != null) {
                AdMarvelWebView.m456a(f224i, adMarvelInternalWebView);
            }
            viewGroup.invalidate();
            viewGroup.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("GUID", f224i);
        intent.setAction("com.admarvel.expandadclose");
        getApplicationContext().sendBroadcast(intent);
        f225j = false;
        finish();
    }

    void m49a(int i, int i2, int i3, int i4, String str, boolean z, boolean z2, String str2) {
        this.f232g = z;
        ViewGroup viewGroup = (ViewGroup) getWindow().findViewById(16908290);
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) viewGroup.findViewWithTag(f224i + "INTERNAL");
        if (adMarvelInternalWebView == null) {
            m48a();
        } else if (adMarvelInternalWebView.m312b()) {
            m48a();
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) viewGroup.findViewWithTag(f224i + "EXPAND_LAYOUT");
            if (relativeLayout == null) {
                m48a();
                return;
            }
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) viewGroup.findViewWithTag(f224i + "EXPAND_BG");
            if (adMarvelWebView == null) {
                m48a();
                return;
            }
            adMarvelInternalWebView.m308a((Context) this);
            adMarvelWebView.setFocusableInTouchMode(true);
            adMarvelWebView.requestFocus();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = i3;
                layoutParams.height = i4;
                layoutParams.leftMargin = i;
                layoutParams.topMargin = i2;
                if (i != 0) {
                    layoutParams.gravity = 0;
                }
            }
            if (adMarvelInternalWebView != null) {
                adMarvelInternalWebView.m307a(i, i2, i3, i4);
            }
            if (z) {
                LinearLayout linearLayout = (LinearLayout) viewGroup.findViewWithTag(f224i + "BTN_CLOSE");
                if (linearLayout != null) {
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                    int measuredHeight = viewGroup.getMeasuredHeight();
                    m47a(linearLayout, layoutParams2, str, i, i2, i3, i4, viewGroup.getMeasuredWidth(), measuredHeight, (int) TypedValue.applyDimension(1, BitmapDescriptorFactory.HUE_ORANGE, getResources().getDisplayMetrics()));
                    linearLayout.removeAllViews();
                    linearLayout.addView(new C0173a(this, z2));
                } else {
                    return;
                }
            }
            if (str2 != null && str2.length() > 0) {
                m51a(str2);
            }
            viewGroup.invalidate();
            viewGroup.requestLayout();
            if (AdMarvelWebView.m451a(f224i) != null) {
                AdMarvelWebView.m451a(f224i).m151a();
            }
        }
    }

    void m50a(Bundle bundle) {
        int i = bundle.getInt(SettingsJsonConstants.ICON_WIDTH_KEY);
        int i2 = bundle.getInt(SettingsJsonConstants.ICON_HEIGHT_KEY);
        int i3 = bundle.getInt("x");
        int i4 = bundle.getInt("y");
        f224i = bundle.getString("GUID");
        Boolean valueOf = Boolean.valueOf(bundle.getBoolean("allowCenteringOfExpandedAd"));
        this.f232g = bundle.getBoolean("enableCloseButton");
        Boolean valueOf2 = Boolean.valueOf(bundle.getBoolean("isMultiLayerExpandedState"));
        String string = bundle.getString("closeButtonPosition");
        Boolean valueOf3 = Boolean.valueOf(bundle.getBoolean("closeAreaEnabled"));
        this.f233h = bundle.getString("orientationState");
        if (this.f233h != null && this.f233h.length() > 0) {
            m51a(this.f233h);
        }
        byte[] byteArray = bundle.getByteArray("serialized_admarvelad");
        if (byteArray != null) {
            try {
                this.f228c = (AdMarvelAd) new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
                m48a();
            }
        }
        View d = AdMarvelWebView.m465d(f224i);
        if (d == null) {
            m48a();
        } else if (d == null || !d.m312b()) {
            d.f597f = true;
            d.m308a((Context) this);
            View adMarvelWebView = new AdMarvelWebView(this, this.f228c.isHoverAd(), this.f228c.isAppInteractionAllowedForExpandableAds());
            adMarvelWebView.setTag(f224i + "EXPAND_BG");
            adMarvelWebView.setFocusableInTouchMode(true);
            adMarvelWebView.requestFocus();
            LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            View relativeLayout = new RelativeLayout(this);
            relativeLayout.setTag(f224i + "EXPAND_LAYOUT");
            LayoutParams layoutParams2 = new FrameLayout.LayoutParams(i, i2);
            if (i3 == 0 && valueOf.booleanValue()) {
                layoutParams2.gravity = 1;
            } else if (Version.getAndroidSDKVersion() < 11) {
                layoutParams2.gravity = 48;
            }
            relativeLayout.setGravity(1);
            layoutParams2.leftMargin = i3;
            layoutParams2.topMargin = i4;
            if (d != null) {
                d.m307a(i3, i4, i, i2);
            }
            relativeLayout.addView(d);
            adMarvelWebView.addView(relativeLayout, layoutParams2);
            relativeLayout.bringToFront();
            setContentView(adMarvelWebView, layoutParams);
            if (this.f232g && !valueOf2.booleanValue()) {
                d = new LinearLayout(this);
                d.setBackgroundColor(0);
                d.setTag(f224i + "BTN_CLOSE");
                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(Utils.m176a(50.0f, (Context) this), Utils.m176a(50.0f, (Context) this));
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                m47a(d, layoutParams3, string, i3, i4, i, i2, displayMetrics.widthPixels, displayMetrics.heightPixels, (int) TypedValue.applyDimension(1, BitmapDescriptorFactory.HUE_ORANGE, getResources().getDisplayMetrics()));
                d.addView(new C0173a(this, valueOf3.booleanValue()));
                relativeLayout.addView(d);
            }
            if (AdMarvelWebView.m451a(f224i) != null) {
                AdMarvelWebView.m451a(f224i).m151a();
            }
        } else {
            m48a();
        }
    }

    public void m51a(String str) {
        int i;
        Logging.log("DisableActivityOrientation");
        if (Version.getAndroidSDKVersion() < 9) {
            i = getResources().getConfiguration().orientation;
        } else {
            C0146i c0146i = new C0146i(this);
            c0146i.run();
            int i2 = 0;
            i = ExploreByTouchHelper.INVALID_ID;
            while (i == ExploreByTouchHelper.INVALID_ID && i2 < 20) {
                i2++;
                i = c0146i.m17a();
            }
        }
        if (str != null) {
            if (Version.getAndroidSDKVersion() < 9) {
                if (str.equalsIgnoreCase("Portrait")) {
                    setRequestedOrientation(1);
                } else if (str.equalsIgnoreCase("LandscapeLeft")) {
                    setRequestedOrientation(0);
                } else if (!str.equalsIgnoreCase("Current")) {
                } else {
                    if (i == 1) {
                        setRequestedOrientation(1);
                    } else if (i == 2) {
                        setRequestedOrientation(0);
                    }
                }
            } else if (str.equalsIgnoreCase("Portrait")) {
                setRequestedOrientation(1);
            } else if (str.equalsIgnoreCase("LandscapeLeft")) {
                setRequestedOrientation(0);
            } else if (!str.equalsIgnoreCase("Current")) {
                this.f226a.post(new C0158q(this, str));
            } else if (i == 0) {
                this.f226a.post(new C0158q(this, "Portrait"));
            } else if (i == 1) {
                this.f226a.post(new C0158q(this, "LandscapeLeft"));
            } else {
                this.f226a.post(new C0158q(this, "none"));
            }
        } else if (Version.getAndroidSDKVersion() < 9) {
            if (i == 1) {
                setRequestedOrientation(1);
            } else if (i == 2) {
                setRequestedOrientation(0);
            }
        } else if (i == 0) {
            this.f226a.post(new C0158q(this, "Portrait"));
        } else if (i == 1) {
            this.f226a.post(new C0158q(this, "LandscapeLeft"));
        } else {
            this.f226a.post(new C0158q(this, "none"));
        }
    }

    public void onBackPressed() {
        if (f225j) {
            m48a();
        } else if (this.f231f != null && this.f227b != null) {
            this.f231f.handleBackKeyPressed(this);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.f231f != null) {
            this.f231f.handleConfigChanges(this, newConfig);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if ("expand".equals(extras.getString("expandAdType"))) {
                f225j = true;
                m50a(extras);
                return;
            }
            f225j = false;
            this.f227b = new RelativeLayout(this);
            if (this.f227b != null) {
                LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                if (layoutParams != null) {
                    this.f227b.setLayoutParams(layoutParams);
                }
                this.f227b.setGravity(1);
                this.f227b.setBackgroundColor(0);
                setContentView(this.f227b);
            }
            this.f230e = extras.getString(Constants.NATIVE_AD_KEY_ELEMENT);
            String string = extras.getString("SDKAdNetwork");
            if (string.equalsIgnoreCase("yume")) {
                this.f229d = SDKAdNetwork.YUME;
            } else if (string.equalsIgnoreCase("chartboost")) {
                this.f229d = SDKAdNetwork.CHARTBOOST;
            } else {
                Logging.log(" AdMarvelMediation Activity : OnCreate : Invalid Adnetwork Name");
                finish();
            }
            byte[] byteArray = extras.getByteArray("serialized_admarvelad");
            if (byteArray != null) {
                try {
                    this.f228c = (AdMarvelAd) new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
                } catch (Throwable e) {
                    Logging.log(Log.getStackTraceString(e));
                    finish();
                }
            }
            string = extras.getString("adapterWebviewGUID");
            if (this.f229d == SDKAdNetwork.YUME) {
                try {
                    this.f231f = AdMarvelAdapterInstances.getInstance(string, Constants.YUME_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e2) {
                    Logging.log(e2.getMessage());
                    finish();
                }
            } else if (this.f229d == SDKAdNetwork.CHARTBOOST) {
                try {
                    this.f231f = AdMarvelAdapterInstances.getInstance(string, Constants.CHARTBOOST_SDK_ADAPTER_FULL_CLASSNAME);
                } catch (Exception e22) {
                    Logging.log(e22.getMessage());
                    finish();
                }
            }
            if (this.f231f == null || this.f228c == null) {
                Logging.log("AdMarvelMediationActivity: OnCreate : Unable to play ad ");
                finish();
                return;
            }
            this.f231f.create(this);
            this.f231f.displayInterstitial(this, this.f228c.isRewardInterstitial());
            return;
        }
        Logging.log("AdMarvelMediationActivity: OnCreate: Unable to get EXTRAS ");
        finish();
    }

    protected void onDestroy() {
        if (!(this.f231f == null || this.f227b == null)) {
            this.f231f.destroy(this.f227b);
        }
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        if (f225j) {
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) ((ViewGroup) getWindow().findViewById(16908290)).findViewWithTag(f224i + "INTERNAL");
            if (adMarvelInternalWebView == null) {
                m48a();
            } else {
                adMarvelInternalWebView.m316f();
            }
        } else if (this.f231f != null && this.f227b != null) {
            this.f231f.pause(this, this.f227b);
        }
    }

    protected void onResume() {
        super.onResume();
        if (f225j) {
            ViewGroup viewGroup = (ViewGroup) getWindow().findViewById(16908290);
            AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) viewGroup.findViewWithTag(f224i + "INTERNAL");
            AdMarvelWebView adMarvelWebView = (AdMarvelWebView) viewGroup.findViewWithTag(f224i + "EXPAND_BG");
            if (adMarvelWebView != null) {
                adMarvelWebView.setFocusableInTouchMode(true);
                adMarvelWebView.requestFocus();
            }
            if (adMarvelInternalWebView == null) {
                m48a();
                return;
            }
            adMarvelInternalWebView.m317g();
            if (Version.getAndroidSDKVersion() >= 11) {
                ac.m254a(adMarvelInternalWebView);
            } else {
                ad.m256a(adMarvelInternalWebView);
            }
        } else if (this.f231f != null && this.f227b != null) {
            this.f231f.resume(this, this.f227b);
        }
    }

    protected void onStart() {
        super.onStart();
        if (this.f231f != null && this.f227b != null) {
            this.f231f.start(this, this.f227b);
        }
    }

    protected void onStop() {
        if (!(this.f231f == null || this.f227b == null)) {
            this.f231f.stop(this, this.f227b);
        }
        super.onStop();
    }
}
