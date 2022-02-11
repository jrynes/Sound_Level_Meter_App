package com.facebook.ads.internal.view.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.InterstitialAdActivity.Type;
import com.facebook.ads.NativeAdVideoActivity;
import com.facebook.ads.internal.adapters.C0437e;
import com.facebook.ads.internal.adapters.C0437e.C0415a;
import com.facebook.ads.internal.util.C0435u;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0515c;
import com.facebook.ads.internal.util.C0531o;
import com.facebook.ads.internal.view.video.support.C0568b;
import com.facebook.ads.internal.view.video.support.C0581a;
import com.google.android.gms.common.api.CommonStatusCodes;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;

/* renamed from: com.facebook.ads.internal.view.video.a */
public class C0579a extends FrameLayout {
    private C0581a f2036a;
    private C0437e f2037b;
    private C0578c f2038c;
    private String f2039d;
    private String f2040e;
    private String f2041f;
    private boolean f2042g;
    private boolean f2043h;
    private int f2044i;
    private Handler f2045j;
    private Handler f2046k;
    private Runnable f2047l;
    private Runnable f2048m;
    private float f2049n;

    /* renamed from: com.facebook.ads.internal.view.video.a.1 */
    class C05691 implements C0568b {
        final /* synthetic */ C0579a f2018a;
        final /* synthetic */ C0579a f2019b;

        C05691(C0579a c0579a, C0579a c0579a2) {
            this.f2019b = c0579a;
            this.f2018a = c0579a2;
        }

        public void m1639a(MediaPlayer mediaPlayer) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.setVolume(this.f2019b.getVolume(), this.f2019b.getVolume());
                mediaPlayer.setLooping(false);
                if (this.f2018a.getAutoplay()) {
                    this.f2018a.m1662c();
                } else {
                    this.f2018a.m1663d();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.2 */
    class C05702 extends C0415a {
        final /* synthetic */ C0579a f2020a;
        final /* synthetic */ C0579a f2021b;

        C05702(C0579a c0579a, C0579a c0579a2) {
            this.f2021b = c0579a;
            this.f2020a = c0579a2;
        }

        public void m1640a() {
            if (this.f2020a.getAutoplay()) {
                this.f2021b.m1662c();
            } else {
                this.f2021b.m1663d();
            }
        }

        public void m1641b() {
            this.f2021b.m1663d();
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.3 */
    class C05713 implements OnTouchListener {
        final /* synthetic */ C0579a f2022a;
        final /* synthetic */ C0579a f2023b;

        C05713(C0579a c0579a, C0579a c0579a2) {
            this.f2023b = c0579a;
            this.f2022a = c0579a2;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 1 || this.f2022a.getVideoURI() == null) {
                return false;
            }
            this.f2023b.m1659g();
            return true;
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.4 */
    class C05724 implements OnClickListener {
        final /* synthetic */ C0579a f2024a;
        final /* synthetic */ C0579a f2025b;

        C05724(C0579a c0579a, C0579a c0579a2) {
            this.f2025b = c0579a;
            this.f2024a = c0579a2;
        }

        public void onClick(View view) {
            if (this.f2024a.getVideoURI() != null) {
                this.f2025b.m1659g();
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.5 */
    class C05735 implements OnTouchListener {
        final /* synthetic */ C0579a f2026a;

        C05735(C0579a c0579a) {
            this.f2026a = c0579a;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 1) {
                return false;
            }
            if (this.f2026a.f2038c.m1647c()) {
                this.f2026a.m1662c();
                return true;
            }
            this.f2026a.m1663d();
            return true;
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.6 */
    class C05746 implements OnClickListener {
        final /* synthetic */ C0579a f2027a;

        C05746(C0579a c0579a) {
            this.f2027a = c0579a;
        }

        public void onClick(View view) {
            if (this.f2027a.f2038c.m1647c()) {
                this.f2027a.m1662c();
            } else {
                this.f2027a.m1663d();
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.a */
    private static final class C0575a extends C0435u<C0579a> {
        public C0575a(C0579a c0579a) {
            super(c0579a);
        }

        public void run() {
            C0579a c0579a = (C0579a) m1184a();
            if (c0579a != null) {
                if (c0579a.f2036a.getCurrentPosition() > CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS) {
                    new C0531o().execute(new String[]{c0579a.getVideoPlayReportURI()});
                    return;
                }
                c0579a.f2045j.postDelayed(this, 250);
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.b */
    private static final class C0576b extends C0435u<C0579a> {
        public C0576b(C0579a c0579a) {
            super(c0579a);
        }

        public void run() {
            C0579a c0579a = (C0579a) m1184a();
            if (c0579a != null && c0579a != null) {
                int currentPosition = c0579a.f2036a.getCurrentPosition();
                if (currentPosition > c0579a.f2044i) {
                    c0579a.f2044i = currentPosition;
                }
                c0579a.f2046k.postDelayed(this, 250);
            }
        }
    }

    /* renamed from: com.facebook.ads.internal.view.video.a.c */
    class C0578c extends Button {
        final /* synthetic */ C0579a f2029a;
        private Paint f2030b;
        private Path f2031c;
        private Path f2032d;
        private Path f2033e;
        private int f2034f;
        private boolean f2035g;

        /* renamed from: com.facebook.ads.internal.view.video.a.c.1 */
        class C05771 extends Paint {
            final /* synthetic */ C0578c f2028a;

            C05771(C0578c c0578c) {
                this.f2028a = c0578c;
                setStyle(Style.FILL_AND_STROKE);
                setStrokeCap(Cap.ROUND);
                setStrokeWidth(PDF417.PREFERRED_RATIO);
                setAntiAlias(true);
                setColor(-1);
            }
        }

        public C0578c(C0579a c0579a, Context context) {
            this.f2029a = c0579a;
            super(context);
            m1642a();
        }

        private void m1642a() {
            this.f2034f = 60;
            this.f2031c = new Path();
            this.f2032d = new Path();
            this.f2033e = new Path();
            this.f2030b = new C05771(this);
            m1646b();
            setClickable(true);
            setBackgroundColor(0);
        }

        private void m1644a(boolean z) {
            this.f2035g = z;
            refreshDrawableState();
            invalidate();
        }

        private void m1646b() {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (((float) this.f2034f) * displayMetrics.density), (int) (displayMetrics.density * ((float) this.f2034f)));
            layoutParams.addRule(9);
            layoutParams.addRule(12);
            setLayoutParams(layoutParams);
        }

        private boolean m1647c() {
            return this.f2035g;
        }

        protected void onDraw(@NonNull Canvas canvas) {
            if (canvas.isHardwareAccelerated() && VERSION.SDK_INT < 17) {
                setLayerType(1, null);
            }
            float max = ((float) Math.max(canvas.getWidth(), canvas.getHeight())) / 100.0f;
            if (m1647c()) {
                this.f2033e.rewind();
                this.f2033e.moveTo(26.5f * max, 15.5f * max);
                this.f2033e.lineTo(26.5f * max, 84.5f * max);
                this.f2033e.lineTo(82.5f * max, 50.5f * max);
                this.f2033e.lineTo(26.5f * max, max * 15.5f);
                this.f2033e.close();
                canvas.drawPath(this.f2033e, this.f2030b);
            } else {
                this.f2031c.rewind();
                this.f2031c.moveTo(29.0f * max, 21.0f * max);
                this.f2031c.lineTo(29.0f * max, 79.0f * max);
                this.f2031c.lineTo(45.0f * max, 79.0f * max);
                this.f2031c.lineTo(45.0f * max, 21.0f * max);
                this.f2031c.lineTo(29.0f * max, 21.0f * max);
                this.f2031c.close();
                this.f2032d.rewind();
                this.f2032d.moveTo(55.0f * max, 21.0f * max);
                this.f2032d.lineTo(55.0f * max, 79.0f * max);
                this.f2032d.lineTo(71.0f * max, 79.0f * max);
                this.f2032d.lineTo(71.0f * max, 21.0f * max);
                this.f2032d.lineTo(55.0f * max, max * 21.0f);
                this.f2032d.close();
                canvas.drawPath(this.f2031c, this.f2030b);
                canvas.drawPath(this.f2032d, this.f2030b);
            }
            super.onDraw(canvas);
        }
    }

    public C0579a(Context context) {
        super(context);
        m1656e();
    }

    private void m1649a(Context context, Intent intent) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        intent.putExtra("displayRotation", defaultDisplay.getRotation());
        intent.putExtra("displayWidth", displayMetrics.widthPixels);
        intent.putExtra("displayHeight", displayMetrics.heightPixels);
        intent.putExtra("useNativeCloseButton", true);
        intent.putExtra(InterstitialAdActivity.VIEW_TYPE, Type.VIDEO);
        intent.putExtra(InterstitialAdActivity.VIDEO_URL, getVideoURI());
        intent.putExtra(InterstitialAdActivity.VIDEO_PLAY_REPORT_URL, getVideoPlayReportURI());
        intent.putExtra(InterstitialAdActivity.VIDEO_TIME_REPORT_URL, getVideoTimeReportURI());
        intent.putExtra(InterstitialAdActivity.PREDEFINED_ORIENTATION_KEY, 13);
        intent.addFlags(268435456);
    }

    private boolean m1651a(Class<?> cls) {
        try {
            Context context = getContext();
            Intent intent = new Intent(context, cls);
            m1649a(context, intent);
            context.startActivity(intent);
            return true;
        } catch (Throwable e) {
            C0515c.m1515a(C0514b.m1512a(e, "Error occurred while loading fullscreen video activity."));
            return false;
        }
    }

    private void m1656e() {
        this.f2049n = 0.0f;
        View relativeLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        relativeLayout.setGravity(17);
        relativeLayout.setLayoutParams(layoutParams);
        setBackgroundColor(0);
        Context context = getContext();
        this.f2036a = new C0581a(context);
        this.f2036a.setBackgroundColor(0);
        LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.addRule(10, -1);
        layoutParams2.addRule(12, -1);
        layoutParams2.addRule(11, -1);
        layoutParams2.addRule(9, -1);
        this.f2036a.setLayoutParams(layoutParams2);
        this.f2036a.setFrameVideoViewListener(new C05691(this, this));
        relativeLayout.addView(this.f2036a);
        addView(relativeLayout);
        this.f2046k = new Handler();
        this.f2047l = new C0576b(this);
        this.f2046k.postDelayed(this.f2047l, 250);
        this.f2045j = new Handler();
        this.f2048m = new C0575a(this);
        this.f2045j.postDelayed(this.f2048m, 250);
        this.f2037b = new C0437e(context, this, 50, true, new C05702(this, this));
        this.f2037b.m1196a(0);
        this.f2037b.m1198b((int) org.xbill.DNS.Type.TSIG);
        this.f2037b.m1195a();
        setOnTouchListenerInternal(new C05713(this, this));
        setOnClickListenerInternal(new C05724(this, this));
        this.f2038c = new C0578c(this, context);
        this.f2038c.setLayoutParams(new FrameLayout.LayoutParams(100, 100, 80));
        this.f2038c.setOnTouchListener(new C05735(this));
        this.f2038c.setOnClickListener(new C05746(this));
        addView(this.f2038c);
    }

    private void m1658f() {
        if (!this.f2043h && getVideoTimeReportURI() != null) {
            Map hashMap = new HashMap();
            hashMap.put("time", Integer.toString(this.f2044i / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
            hashMap.put("inline", "1");
            new C0531o(hashMap).execute(new String[]{getVideoTimeReportURI()});
            this.f2043h = true;
            this.f2044i = 0;
        }
    }

    private void m1659g() {
        if (!m1651a(NativeAdVideoActivity.class)) {
            m1651a(InterstitialAdActivity.class);
        }
    }

    private void setOnClickListenerInternal(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
    }

    private void setOnTouchListenerInternal(OnTouchListener onTouchListener) {
        super.setOnTouchListener(onTouchListener);
    }

    public void m1660a() {
        if (this.f2044i > 0) {
            m1658f();
            this.f2044i = 0;
        }
    }

    public void m1661b() {
        this.f2039d = null;
    }

    public void m1662c() {
        this.f2038c.m1644a(false);
        this.f2036a.m1666a();
    }

    public void m1663d() {
        this.f2038c.m1644a(true);
        this.f2036a.m1667b();
    }

    public boolean getAutoplay() {
        return this.f2042g;
    }

    public String getVideoPlayReportURI() {
        return this.f2040e;
    }

    public String getVideoTimeReportURI() {
        return this.f2041f;
    }

    public String getVideoURI() {
        return this.f2039d;
    }

    float getVolume() {
        return this.f2049n;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f2037b.m1195a();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        m1658f();
        this.f2037b.m1197b();
    }

    public void setAutoplay(boolean z) {
        this.f2042g = z;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
    }

    public void setVideoPlayReportURI(String str) {
        this.f2040e = str;
    }

    public void setVideoTimeReportURI(String str) {
        m1660a();
        this.f2041f = str;
    }

    public void setVideoURI(String str) {
        this.f2039d = str;
        if (str != null) {
            this.f2036a.setup(Uri.parse(str));
            if (this.f2042g) {
                this.f2036a.m1666a();
            }
        }
    }

    void setVolume(float f) {
        this.f2049n = f;
    }
}
