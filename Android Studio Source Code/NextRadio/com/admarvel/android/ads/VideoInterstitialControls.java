package com.admarvel.android.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.admarvel.android.ads.AdMarvelVideoActivity.C0205n;
import com.admarvel.android.util.AdMarvelBitmapDrawableUtils;
import com.admarvel.android.util.Logging;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

/* renamed from: com.admarvel.android.ads.u */
class VideoInterstitialControls extends LinearLayout {
    static int f924a;
    public boolean f925b;
    VideoInterstitialControls f926c;
    final String f927d;
    AdMarvelXMLReader f928e;
    AdMarvelXMLElement f929f;
    private final WeakReference<AdMarvelVideoActivity> f930g;
    private final WeakReference<AdMarvelUniversalVideoView> f931h;
    private boolean f932i;
    private boolean f933j;

    /* renamed from: com.admarvel.android.ads.u.a */
    private class VideoInterstitialControls extends AsyncTask<String, Void, Bitmap> {
        ImageView f907a;
        final /* synthetic */ VideoInterstitialControls f908b;

        public VideoInterstitialControls(VideoInterstitialControls videoInterstitialControls, ImageView imageView) {
            this.f908b = videoInterstitialControls;
            this.f907a = imageView;
        }

        protected Bitmap m496a(String... strArr) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(strArr[0]).openStream());
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
            return bitmap;
        }

        protected void m497a(Bitmap bitmap) {
            this.f907a.setImageBitmap(bitmap);
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m496a((String[]) x0);
        }

        protected /* synthetic */ void onPostExecute(Object x0) {
            m497a((Bitmap) x0);
        }
    }

    /* renamed from: com.admarvel.android.ads.u.b */
    private class VideoInterstitialControls implements Runnable {
        final /* synthetic */ VideoInterstitialControls f909a;
        private final WeakReference<ImageView> f910b;
        private String f911c;

        public VideoInterstitialControls(VideoInterstitialControls videoInterstitialControls, ImageView imageView, String str) {
            this.f909a = videoInterstitialControls;
            this.f910b = new WeakReference(imageView);
            this.f911c = str;
        }

        public void run() {
            if (this.f910b.get() != null) {
                new VideoInterstitialControls(this.f909a, (ImageView) this.f910b.get()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{this.f911c});
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.u.c */
    public enum VideoInterstitialControls {
        PauseVideo,
        StopVideo,
        CloseVideo
    }

    /* renamed from: com.admarvel.android.ads.u.d */
    class VideoInterstitialControls extends RelativeLayout {
        public String f918a;
        public String f919b;
        public String f920c;
        public VideoInterstitialControls f921d;
        public List<String> f922e;
        final /* synthetic */ VideoInterstitialControls f923f;

        /* renamed from: com.admarvel.android.ads.u.d.1 */
        class VideoInterstitialControls implements OnClickListener {
            final /* synthetic */ VideoInterstitialControls f916a;
            final /* synthetic */ VideoInterstitialControls f917b;

            VideoInterstitialControls(VideoInterstitialControls videoInterstitialControls, VideoInterstitialControls videoInterstitialControls2) {
                this.f917b = videoInterstitialControls;
                this.f916a = videoInterstitialControls2;
            }

            public void onClick(View view) {
                this.f917b.f923f.m501a(view);
            }
        }

        public VideoInterstitialControls(VideoInterstitialControls videoInterstitialControls, Context context, AdMarvelXMLElement adMarvelXMLElement, LayoutParams layoutParams) {
            this.f923f = videoInterstitialControls;
            super(context);
            this.f918a = null;
            this.f919b = null;
            this.f920c = null;
            this.f921d = VideoInterstitialControls.PauseVideo;
            AdMarvelXMLElement adMarvelXMLElement2 = adMarvelXMLElement.getChildren().get(locationTracking.action) != null ? (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get(locationTracking.action)).get(0) : null;
            if (adMarvelXMLElement2 != null) {
                String str = (String) adMarvelXMLElement2.getAttributes().get(Send.TYPE);
                this.f920c = str;
                if ("open_url".equalsIgnoreCase(str)) {
                    this.f918a = adMarvelXMLElement2.getData();
                    str = (String) adMarvelXMLElement2.getAttributes().get("videoOpenUrlBehavior");
                    if (str != null) {
                        if (str.equalsIgnoreCase("pauseVideo")) {
                            this.f921d = VideoInterstitialControls.PauseVideo;
                        } else if (str.equalsIgnoreCase("stopVideo")) {
                            this.f921d = VideoInterstitialControls.StopVideo;
                        } else if (str.equalsIgnoreCase("closeVideo")) {
                            this.f921d = VideoInterstitialControls.CloseVideo;
                        }
                    }
                } else if ("done".equalsIgnoreCase(str)) {
                    videoInterstitialControls.f932i = true;
                } else if ("play_movie".equalsIgnoreCase(str)) {
                    this.f919b = adMarvelXMLElement2.getData();
                }
            }
            AdMarvelXMLElement adMarvelXMLElement3 = adMarvelXMLElement.getChildren().get("clickTracking") != null ? (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get("clickTracking")).get(0) : null;
            if (adMarvelXMLElement3 != null) {
                adMarvelXMLElement3 = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement3.getChildren().get("pixels")).get(0);
                if (adMarvelXMLElement3 != null && adMarvelXMLElement3.getChildren().containsKey("pixel")) {
                    ArrayList arrayList = (ArrayList) adMarvelXMLElement3.getChildren().get("pixel");
                    this.f922e = new ArrayList();
                    this.f922e.clear();
                    for (int i = 0; i < arrayList.size(); i++) {
                        this.f922e.add(((AdMarvelXMLElement) arrayList.get(i)).getData());
                    }
                }
            }
            setLayoutParams(layoutParams);
            setClickable(true);
            addStatesFromChildren();
            setGravity(17);
            setOnClickListener(new VideoInterstitialControls(this, videoInterstitialControls));
        }
    }

    static {
        f924a = 100001;
    }

    VideoInterstitialControls(AdMarvelUniversalVideoView adMarvelUniversalVideoView, AdMarvelVideoActivity adMarvelVideoActivity, Context context, String str, String str2) {
        String str3 = null;
        int i = 0;
        super(context);
        this.f925b = false;
        this.f933j = false;
        this.f930g = new WeakReference(adMarvelVideoActivity);
        this.f931h = new WeakReference(adMarvelUniversalVideoView);
        this.f932i = false;
        this.f927d = str2;
        setId(f924a);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.gravity = 16;
        setLayoutParams(layoutParams);
        setGravity(16);
        View horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setVerticalScrollBarEnabled(false);
        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(0, -1);
        layoutParams2.weight = 1.0f;
        horizontalScrollView.setLayoutParams(layoutParams2);
        addView(horizontalScrollView);
        View linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -1));
        horizontalScrollView.addView(linearLayout);
        this.f928e = new AdMarvelXMLReader();
        this.f928e.parseXMLString(str);
        this.f929f = this.f928e.getParsedXMLData();
        if (this.f929f != null && this.f929f.getChildren().containsKey("toolbar")) {
            AdMarvelXMLElement adMarvelXMLElement = (AdMarvelXMLElement) ((ArrayList) this.f929f.getChildren().get("toolbar")).get(0);
            String str4 = "#3E3E3E";
            String str5 = adMarvelXMLElement == null ? null : (String) adMarvelXMLElement.getAttributes().get("background");
            Drawable shapeDrawable = new ShapeDrawable(new RectShape());
            if (str5 != null) {
                shapeDrawable.getPaint().setColor(Color.parseColor(str5));
            } else {
                shapeDrawable.getPaint().setColor(Color.parseColor(str4));
            }
            if (adMarvelXMLElement != null) {
                str3 = (String) adMarvelXMLElement.getAttributes().get("alpha");
            }
            shapeDrawable.setAlpha(str3 != null ? Integer.parseInt(str3) : 175);
            setBackgroundDrawable(shapeDrawable);
            if (adMarvelXMLElement != null && adMarvelXMLElement.getChildren().containsKey("item")) {
                ArrayList arrayList = (ArrayList) adMarvelXMLElement.getChildren().get("item");
                while (i < arrayList.size()) {
                    AdMarvelXMLElement adMarvelXMLElement2 = (AdMarvelXMLElement) arrayList.get(i);
                    str3 = (String) adMarvelXMLElement2.getAttributes().get(Send.TYPE);
                    if ("SystemItem".equalsIgnoreCase(str3)) {
                        m500a(context, adMarvelXMLElement2, linearLayout);
                    } else if ("Title".equalsIgnoreCase(str3)) {
                        m506b(context, adMarvelXMLElement2, linearLayout);
                    } else if ("Image".equalsIgnoreCase(str3)) {
                        m509c(context, adMarvelXMLElement2, linearLayout);
                    } else if ("Timer".equalsIgnoreCase(str3) && adMarvelVideoActivity.f329k > 0) {
                        this.f933j = true;
                    } else if ("Toggle".equalsIgnoreCase(str3)) {
                        m511d(context, adMarvelXMLElement2, linearLayout);
                    }
                    i++;
                }
            }
        }
        layoutParams2 = new RelativeLayout.LayoutParams(-1, (int) TypedValue.applyDimension(1, 40.0f, getResources().getDisplayMetrics()));
        layoutParams2.addRule(12);
        layoutParams2.addRule(3, AdMarvelInternalWebView.f569a);
        setLayoutParams(layoutParams2);
        if (this.f933j) {
            m499a(context);
        }
    }

    private float m498a(float f) {
        return TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private void m499a(Context context) {
        new LayoutParams(-2, -1).gravity = 3;
        int applyDimension = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, (int) TypedValue.applyDimension(1, 36.0f, getResources().getDisplayMetrics()));
        layoutParams.addRule(5);
        layoutParams.setMargins(applyDimension, 0, applyDimension, 0);
        View linearLayout = new LinearLayout(context);
        linearLayout.setVisibility(4);
        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(-2, -1);
        layoutParams2.gravity = 5;
        linearLayout.setOrientation(0);
        layoutParams.addRule(15);
        linearLayout.setLayoutParams(layoutParams2);
        linearLayout.setTag(this.f927d + "TIMER_BUTTON_LAYOUT");
        linearLayout.setClickable(false);
        linearLayout.addStatesFromChildren();
        linearLayout.setGravity(5);
        View textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams3 = new LayoutParams(-2, -1);
        layoutParams3.setMargins((int) m498a(5.0f), 0, (int) m498a(5.0f), 0);
        textView.setTextColor(Color.parseColor("#0e78b9"));
        textView.setLayoutParams(layoutParams);
        textView.setGravity(16);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setSingleLine(true);
        View imageView = new ImageView(context);
        AdMarvelBitmapDrawableUtils.getBitMapDrawable("time", getContext(), imageView);
        ViewGroup.LayoutParams layoutParams4 = new LayoutParams((int) m498a(32.0f), (int) m498a(32.0f));
        layoutParams4.gravity = 16;
        linearLayout.addView(textView, layoutParams3);
        linearLayout.addView(imageView, layoutParams4);
        addView(linearLayout);
    }

    private void m500a(Context context, AdMarvelXMLElement adMarvelXMLElement, ViewGroup viewGroup) {
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = 3;
        int applyDimension = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
        float applyDimension2 = TypedValue.applyDimension(1, 32.0f, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int) applyDimension2, (int) applyDimension2);
        layoutParams2.addRule(5);
        layoutParams2.setMargins(applyDimension, 0, applyDimension, 0);
        Drawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(new LinearGradient(0.0f, 0.0f, 0.0f, m498a(40.0f), Color.parseColor("#AAFFFFFF"), Color.parseColor("#AA3E3E3E"), TileMode.REPEAT));
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable);
        VideoInterstitialControls videoInterstitialControls = new VideoInterstitialControls(this, context, adMarvelXMLElement, layoutParams);
        if (videoInterstitialControls.f920c != null) {
            View imageView = new ImageView(context);
            imageView.setLayoutParams(layoutParams2);
            if (videoInterstitialControls.f920c.equalsIgnoreCase("done")) {
                AdMarvelBitmapDrawableUtils.getBitMapDrawable("close", getContext(), imageView);
                ViewGroup.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) m498a(36.0f), (int) m498a(36.0f));
                layoutParams.gravity = 53;
                videoInterstitialControls.addView(imageView, layoutParams3);
                imageView.setTag(Constants.ADM_VIDEO_SYSTEM_ICON_DONE_BUTTON);
                m516a(videoInterstitialControls);
                return;
            }
            videoInterstitialControls.setBackgroundDrawable(stateListDrawable);
            layoutParams2.addRule(15);
            AdMarvelBitmapDrawableUtils.getBitMapDrawable(videoInterstitialControls.f920c, getContext(), imageView);
            videoInterstitialControls.addView(imageView);
            viewGroup.addView(videoInterstitialControls);
        }
    }

    private void m501a(View view) {
        if (view != null && (view instanceof VideoInterstitialControls)) {
            VideoInterstitialControls videoInterstitialControls = (VideoInterstitialControls) view;
            AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
            if (adMarvelVideoActivity != null) {
                adMarvelVideoActivity.onUserInteraction();
                if (videoInterstitialControls.f922e != null) {
                    adMarvelVideoActivity.m131a(videoInterstitialControls.f922e);
                }
            }
            if (view.getParent() instanceof ViewSwitcher) {
                ViewSwitcher viewSwitcher = (ViewSwitcher) view.getParent();
                if (viewSwitcher != null) {
                    if (viewSwitcher.getDisplayedChild() == 0) {
                        viewSwitcher.showNext();
                    } else {
                        viewSwitcher.showPrevious();
                    }
                }
            }
            if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("open_url")) {
                m507b(videoInterstitialControls);
            } else if (videoInterstitialControls.f920c == null || !videoInterstitialControls.f920c.equalsIgnoreCase("play_movie")) {
                if (videoInterstitialControls.f920c == null || !videoInterstitialControls.f920c.equalsIgnoreCase("done")) {
                    if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("stop")) {
                        m510d();
                    } else if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("replay")) {
                        m508c();
                    } else if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("pause")) {
                        m504a(true);
                    } else if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("resume")) {
                        m512e();
                    } else if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("mute")) {
                        m513f();
                    } else if (videoInterstitialControls.f920c != null && videoInterstitialControls.f920c.equalsIgnoreCase("unmute")) {
                        m514g();
                    }
                } else if (adMarvelVideoActivity != null) {
                    adMarvelVideoActivity.m146j();
                }
            } else if (videoInterstitialControls.f919b != null) {
                m503a(videoInterstitialControls.f919b);
            }
        }
    }

    private void m503a(String str) {
        if (str != null) {
            AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
            if (adMarvelVideoActivity != null) {
                adMarvelVideoActivity.m135b(str);
            }
        }
    }

    private void m504a(boolean z) {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.m132a(z);
        }
    }

    @SuppressLint({"NewApi"})
    private void m506b(Context context, AdMarvelXMLElement adMarvelXMLElement, ViewGroup viewGroup) {
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = 3;
        int applyDimension = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, (int) TypedValue.applyDimension(1, 36.0f, getResources().getDisplayMetrics()));
        layoutParams2.addRule(5);
        layoutParams2.setMargins(applyDimension, 0, applyDimension, 0);
        VideoInterstitialControls videoInterstitialControls = new VideoInterstitialControls(this, context, adMarvelXMLElement, layoutParams);
        layoutParams2.addRule(15);
        Drawable shapeDrawable = new ShapeDrawable(new RectShape());
        Drawable shapeDrawable2 = new ShapeDrawable(new RectShape());
        shapeDrawable2.getPaint().setColor(Color.parseColor("#000000FF"));
        shapeDrawable.getPaint().setColor(Color.parseColor("#AA3E3E3E"));
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable);
        stateListDrawable.addState(new int[]{-16842913}, shapeDrawable2);
        stateListDrawable.addState(new int[]{16842913}, shapeDrawable);
        Drawable stateListDrawable2 = new StateListDrawable();
        stateListDrawable2.addState(new int[]{16842919}, shapeDrawable);
        if (videoInterstitialControls.f920c != null) {
            String str = (String) adMarvelXMLElement.getAttributes().get(SettingsJsonConstants.PROMPT_TITLE_KEY);
            View textView = new TextView(context);
            textView.setLayoutParams(layoutParams2);
            textView.setBackgroundColor(-16776961);
            textView.setGravity(17);
            layoutParams2.addRule(15);
            textView.setDuplicateParentStateEnabled(true);
            textView.setClickable(false);
            textView.setSingleLine();
            textView.setTextColor(Color.parseColor("#84c043"));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextSize(17.0f);
            String str2 = (String) adMarvelXMLElement.getAttributes().get("color");
            String str3 = (String) adMarvelXMLElement.getAttributes().get("textSize");
            if (str2 != null && str2.length() > 0) {
                textView.setTextColor(Color.parseColor(str2));
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                if (str3 != null) {
                    try {
                        textView.setTextSize((float) Integer.parseInt(str3));
                    } catch (Exception e) {
                        textView.setTextSize(18.0f);
                        Logging.log("Exception in setting Done button text size" + e.getMessage());
                    }
                }
            }
            if (str != null && str.length() > 0) {
                textView.setText(str);
            }
            if (Version.getAndroidSDKVersion() >= 16) {
                textView.setBackground(stateListDrawable);
            } else {
                textView.setBackgroundDrawable(stateListDrawable);
            }
            videoInterstitialControls.setBackgroundDrawable(stateListDrawable2);
            if (videoInterstitialControls.f920c.equalsIgnoreCase("done")) {
                layoutParams2.setMargins(0, 0, 0, 0);
                textView.setLayoutParams(layoutParams2);
                textView.setTag(Constants.ADM_VIDEO_CUSTOM_DONE_BUTTON);
                videoInterstitialControls.addView(textView);
                m516a(videoInterstitialControls);
                return;
            }
            videoInterstitialControls.addView(textView);
            viewGroup.addView(videoInterstitialControls, layoutParams2);
        }
    }

    private void m507b(VideoInterstitialControls videoInterstitialControls) {
        if (videoInterstitialControls != null) {
            AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
            if (adMarvelVideoActivity != null) {
                if (videoInterstitialControls.f921d == VideoInterstitialControls.PauseVideo) {
                    m504a(false);
                } else if (videoInterstitialControls.f921d == VideoInterstitialControls.StopVideo) {
                    m510d();
                } else if (videoInterstitialControls.f921d == VideoInterstitialControls.CloseVideo) {
                    adMarvelVideoActivity.m146j();
                }
                if (videoInterstitialControls.f918a != null && videoInterstitialControls.f918a.length() > 0) {
                    Intent intent;
                    if (AdMarvelInterstitialAds.getEnableClickRedirect()) {
                        adMarvelVideoActivity.f322d = true;
                        intent = new Intent(adMarvelVideoActivity, AdMarvelActivity.class);
                        intent.addFlags(268435456);
                        intent.putExtra(SettingsJsonConstants.APP_URL_KEY, videoInterstitialControls.f918a);
                        intent.putExtra("isInterstitial", false);
                        intent.putExtra("isInterstitialClick", false);
                        intent.putExtra("GUID", this.f927d);
                        adMarvelVideoActivity.startActivity(intent);
                    }
                    intent = new Intent();
                    intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
                    intent.putExtra("callback", Promotion.ACTION_CLICK);
                    intent.putExtra("WEBVIEW_GUID", adMarvelVideoActivity.f330l);
                    intent.putExtra(SettingsJsonConstants.APP_URL_KEY, videoInterstitialControls.f918a);
                    adMarvelVideoActivity.sendBroadcast(intent);
                }
            }
        }
    }

    private void m508c() {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.m149m();
        }
    }

    private void m509c(Context context, AdMarvelXMLElement adMarvelXMLElement, ViewGroup viewGroup) {
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = 3;
        int applyDimension = (int) TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics());
        float applyDimension2 = TypedValue.applyDimension(1, 32.0f, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int) applyDimension2, (int) applyDimension2);
        layoutParams2.addRule(5);
        layoutParams2.setMargins(applyDimension, 0, applyDimension, 0);
        Drawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(new LinearGradient(0.0f, 0.0f, 0.0f, m498a(40.0f), Color.parseColor("#AAFFFFFF"), Color.parseColor("#AA3E3E3E"), TileMode.REPEAT));
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable);
        VideoInterstitialControls videoInterstitialControls = new VideoInterstitialControls(this, context, adMarvelXMLElement, layoutParams);
        if (videoInterstitialControls.f920c != null) {
            String str = (String) adMarvelXMLElement.getAttributes().get(Constants.NATIVE_AD_IMAGE_ELEMENT);
            String str2 = (String) adMarvelXMLElement.getAttributes().get("image_high");
            String str3 = (String) adMarvelXMLElement.getAttributes().get("image_retina");
            Context context2 = (AdMarvelVideoActivity) this.f930g.get();
            if (context2 != null) {
                float o = Utils.m226o(context2);
                if (o > 1.0f) {
                    if (o <= 1.0f || o >= 2.0f) {
                        if (str3 != null && str3.length() > 0) {
                            str = str3;
                        }
                    } else if (str2 != null && str2.length() > 0) {
                        str = str2;
                    }
                }
                layoutParams2.width = (int) m498a(32.0f);
                layoutParams2.height = (int) m498a(32.0f);
                layoutParams2.addRule(13);
                View imageView = new ImageView(context);
                imageView.setLayoutParams(layoutParams2);
                if (Version.getAndroidSDKVersion() >= 11) {
                    context2.m144h().post(new VideoInterstitialControls(this, imageView, str));
                } else {
                    new VideoInterstitialControls(this, imageView).execute(new String[]{str});
                }
                if (videoInterstitialControls.f920c.equalsIgnoreCase("done")) {
                    ViewGroup.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams((int) m498a(36.0f), (int) m498a(36.0f));
                    layoutParams.gravity = 53;
                    videoInterstitialControls.addView(imageView, layoutParams3);
                    imageView.setTag(Constants.ADM_VIDEO_IMAGE_ICON_DONE_BUTTON);
                    m516a(videoInterstitialControls);
                    return;
                }
                videoInterstitialControls.setBackgroundDrawable(stateListDrawable);
                videoInterstitialControls.addView(imageView);
                viewGroup.addView(videoInterstitialControls);
            }
        }
    }

    private void m510d() {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.m147k();
        }
    }

    private void m511d(Context context, AdMarvelXMLElement adMarvelXMLElement, ViewGroup viewGroup) {
        try {
            ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -1);
            layoutParams.gravity = 3;
            View viewSwitcher = new ViewSwitcher(context);
            viewSwitcher.setLayoutParams(layoutParams);
            viewSwitcher.addStatesFromChildren();
            if (adMarvelXMLElement != null && adMarvelXMLElement.getChildren().containsKey("subitem")) {
                this.f925b = true;
                ArrayList arrayList = (ArrayList) adMarvelXMLElement.getChildren().get("subitem");
                for (int i = 0; i < arrayList.size(); i++) {
                    AdMarvelXMLElement adMarvelXMLElement2 = (AdMarvelXMLElement) arrayList.get(i);
                    String str = (String) adMarvelXMLElement2.getAttributes().get(Send.TYPE);
                    if ("SystemItem".equalsIgnoreCase(str)) {
                        m500a(context, adMarvelXMLElement2, viewSwitcher);
                    } else if ("Title".equalsIgnoreCase(str)) {
                        m506b(context, adMarvelXMLElement2, viewSwitcher);
                    } else if ("Image".equalsIgnoreCase(str)) {
                        m509c(context, adMarvelXMLElement2, viewSwitcher);
                    }
                }
            }
            if (viewSwitcher.getChildCount() == 2) {
                viewGroup.addView(viewSwitcher);
            }
        } catch (Exception e) {
            Logging.log("Exception in addToggleButton API" + e.getMessage());
        }
    }

    private void m512e() {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.m148l();
        }
    }

    private void m513f() {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) this.f931h.get();
        if (!(adMarvelUniversalVideoView == null || adMarvelVideoActivity == null)) {
            adMarvelUniversalVideoView.m385d();
            Intent intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
            intent.putExtra("callback", "audiostop");
            intent.putExtra("WEBVIEW_GUID", adMarvelVideoActivity.f330l);
            adMarvelVideoActivity.sendBroadcast(intent);
        }
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.f328j = C0205n.Mute;
        }
    }

    private void m514g() {
        AdMarvelVideoActivity adMarvelVideoActivity = (AdMarvelVideoActivity) this.f930g.get();
        AdMarvelUniversalVideoView adMarvelUniversalVideoView = (AdMarvelUniversalVideoView) this.f931h.get();
        if (!(adMarvelUniversalVideoView == null || adMarvelVideoActivity == null)) {
            adMarvelUniversalVideoView.m386e();
            Intent intent = new Intent();
            intent.setAction(AdMarvelInterstitialAds.CUSTOM_INTERSTITIAL_AD_LISTENER_INTENT);
            intent.putExtra("callback", "audiostart");
            intent.putExtra("WEBVIEW_GUID", adMarvelVideoActivity.f330l);
            adMarvelVideoActivity.sendBroadcast(intent);
        }
        if (adMarvelVideoActivity != null) {
            adMarvelVideoActivity.f328j = C0205n.UnMute;
        }
    }

    public VideoInterstitialControls m515a() {
        return this.f926c;
    }

    public void m516a(VideoInterstitialControls videoInterstitialControls) {
        this.f926c = videoInterstitialControls;
    }

    public boolean m517b() {
        return this.f932i;
    }
}
