package com.admarvel.android.ads;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import com.admarvel.android.ads.AdMarvelWebView.AdMarvelWebView;
import com.admarvel.android.util.Logging;
import java.lang.ref.WeakReference;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

/* renamed from: com.admarvel.android.ads.m */
public class AdMarvelUniversalVideoView extends TextureView implements MediaPlayerControl, AdMarvelWebView {
    private int f637A;
    private OnErrorListener f638B;
    private OnInfoListener f639C;
    private int f640D;
    private boolean f641E;
    private boolean f642F;
    private boolean f643G;
    private Context f644H;
    private int f645I;
    private AdMarvelUniversalVideoView f646J;
    private boolean f647K;
    private boolean f648L;
    private WeakReference<AdMarvelWebView> f649M;
    private OnCompletionListener f650N;
    private OnInfoListener f651O;
    private OnErrorListener f652P;
    private OnBufferingUpdateListener f653Q;
    public SurfaceTexture f654a;
    public MediaPlayer f655b;
    OnVideoSizeChangedListener f656c;
    OnPreparedListener f657d;
    SurfaceTextureListener f658e;
    private String f659f;
    private Uri f660g;
    private Map<String, String> f661h;
    private final int f662i;
    private final int f663j;
    private final int f664k;
    private final int f665l;
    private final int f666m;
    private final int f667n;
    private final int f668o;
    private int f669p;
    private int f670q;
    private int f671r;
    private int f672s;
    private int f673t;
    private int f674u;
    private int f675v;
    private int f676w;
    private MediaController f677x;
    private OnCompletionListener f678y;
    private OnPreparedListener f679z;

    /* renamed from: com.admarvel.android.ads.m.a */
    interface AdMarvelUniversalVideoView {
        void m77a();

        void m78a(AdMarvelUniversalVideoView adMarvelUniversalVideoView);

        void m79b();

        void m80c();

        void m81d();

        void m82e();

        void m83f();

        void m84g();
    }

    /* renamed from: com.admarvel.android.ads.m.1 */
    class AdMarvelUniversalVideoView implements OnVideoSizeChangedListener {
        final /* synthetic */ AdMarvelUniversalVideoView f630a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f630a = adMarvelUniversalVideoView;
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            try {
                this.f630a.f671r = mp.getVideoWidth();
                this.f630a.f672s = mp.getVideoHeight();
                if (this.f630a.f671r != 0 && this.f630a.f672s != 0) {
                    if (this.f630a.f648L) {
                        this.f630a.getSurfaceTexture().setDefaultBufferSize(this.f630a.f671r, this.f630a.f672s);
                        this.f630a.requestLayout();
                        return;
                    }
                    this.f630a.m384c();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.m.2 */
    class AdMarvelUniversalVideoView implements OnPreparedListener {
        final /* synthetic */ AdMarvelUniversalVideoView f631a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f631a = adMarvelUniversalVideoView;
        }

        public void onPrepared(MediaPlayer mp) {
            Logging.log("MediaPreparedListener : onPrepared");
            this.f631a.f669p = 2;
            this.f631a.f641E = this.f631a.f642F = this.f631a.f643G = true;
            if (this.f631a.f679z != null) {
                this.f631a.f679z.onPrepared(this.f631a.f655b);
            }
            if (this.f631a.f677x != null) {
                this.f631a.f677x.setEnabled(true);
            }
            this.f631a.f671r = mp.getVideoWidth();
            this.f631a.f672s = mp.getVideoHeight();
            int f = this.f631a.f640D;
            if (f != 0) {
                this.f631a.seekTo(f);
            }
            if (this.f631a.f671r == 0 || this.f631a.f672s == 0 || this.f631a.getSurfaceTexture() == null) {
                if (this.f631a.f670q == 3) {
                    this.f631a.start();
                }
            } else if (this.f631a.f648L) {
                try {
                    this.f631a.getSurfaceTexture().setDefaultBufferSize(this.f631a.f671r, this.f631a.f672s);
                    this.f631a.requestLayout();
                    if (this.f631a.f675v != this.f631a.f671r || this.f631a.f676w != this.f631a.f672s) {
                        return;
                    }
                    if (this.f631a.f670q == 3) {
                        this.f631a.start();
                        if (this.f631a.f677x != null) {
                            this.f631a.f677x.show();
                        }
                    } else if (!this.f631a.isPlaying()) {
                        if ((f != 0 || this.f631a.getCurrentPosition() > 0) && this.f631a.f677x != null) {
                            this.f631a.f677x.show(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.f631a.m384c();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.m.3 */
    class AdMarvelUniversalVideoView implements OnCompletionListener {
        final /* synthetic */ AdMarvelUniversalVideoView f632a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f632a = adMarvelUniversalVideoView;
        }

        public void onCompletion(MediaPlayer mp) {
            try {
                Log.d(this.f632a.f659f, "onCompletion: ");
                if (this.f632a.f669p != 5) {
                    this.f632a.f669p = 5;
                    this.f632a.f670q = 5;
                    if (this.f632a.f677x != null) {
                        this.f632a.f677x.hide();
                    }
                    if (this.f632a.f678y != null) {
                        this.f632a.f678y.onCompletion(this.f632a.f655b);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.admarvel.android.ads.m.4 */
    class AdMarvelUniversalVideoView implements OnInfoListener {
        final /* synthetic */ AdMarvelUniversalVideoView f633a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f633a = adMarvelUniversalVideoView;
        }

        public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
            if (this.f633a.f639C != null) {
                this.f633a.f639C.onInfo(mp, arg1, arg2);
            }
            return true;
        }
    }

    /* renamed from: com.admarvel.android.ads.m.5 */
    class AdMarvelUniversalVideoView implements OnErrorListener {
        final /* synthetic */ AdMarvelUniversalVideoView f634a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f634a = adMarvelUniversalVideoView;
        }

        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.d(this.f634a.f659f, "Error: " + framework_err + Stomp.COMMA + impl_err);
            this.f634a.f669p = -1;
            this.f634a.f670q = -1;
            if (this.f634a.f677x != null) {
                this.f634a.f677x.hide();
            }
            return (this.f634a.f638B == null || this.f634a.f638B.onError(this.f634a.f655b, framework_err, impl_err)) ? true : true;
        }
    }

    /* renamed from: com.admarvel.android.ads.m.6 */
    class AdMarvelUniversalVideoView implements OnBufferingUpdateListener {
        final /* synthetic */ AdMarvelUniversalVideoView f635a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f635a = adMarvelUniversalVideoView;
        }

        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            this.f635a.f637A = percent;
        }
    }

    /* renamed from: com.admarvel.android.ads.m.7 */
    class AdMarvelUniversalVideoView implements SurfaceTextureListener {
        final /* synthetic */ AdMarvelUniversalVideoView f636a;

        AdMarvelUniversalVideoView(AdMarvelUniversalVideoView adMarvelUniversalVideoView) {
            this.f636a = adMarvelUniversalVideoView;
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Logging.log("onSurfaceTextureAvailable");
            try {
                this.f636a.f654a = surface;
                this.f636a.f675v = width;
                this.f636a.f676w = height;
                if (this.f636a.f655b != null) {
                    this.f636a.f655b.setSurface(new Surface(surface));
                }
                if (this.f636a.f669p != 0) {
                    this.f636a.m370k();
                }
                if (this.f636a.f648L && this.f636a.f646J != null) {
                    this.f636a.f646J.m80c();
                }
                this.f636a.f647K = false;
            } catch (Exception e) {
                e.printStackTrace();
                if (this.f636a.f646J != null) {
                    this.f636a.f646J.m84g();
                }
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Logging.log("onSurfaceTextureDestroyed");
            if (!this.f636a.f648L) {
                this.f636a.f647K = true;
                if (this.f636a.f649M != null && this.f636a.f649M.get() != null && ((AdMarvelWebView) this.f636a.f649M.get()).aa && this.f636a.m389h()) {
                    if (surface != null) {
                        surface.release();
                    }
                    if (this.f636a.f677x != null) {
                        this.f636a.f677x.hide();
                    }
                    this.f636a.m388g();
                    this.f636a.f647K = false;
                }
            }
            return false;
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Logging.log("onSurfaceTextureSizeChanged");
            try {
                if (this.f636a.f655b != null) {
                    this.f636a.f671r = this.f636a.f655b.getVideoWidth();
                    this.f636a.f672s = this.f636a.f655b.getVideoHeight();
                    if (this.f636a.f671r != 0 && this.f636a.f672s != 0) {
                        surface.setDefaultBufferSize(this.f636a.f671r, this.f636a.f672s);
                        this.f636a.requestLayout();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (this.f636a.f646J != null) {
                    this.f636a.f646J.m84g();
                }
            }
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }

    public AdMarvelUniversalVideoView(Context context) {
        super(context);
        this.f659f = "AdMarvelUniversalVideoView";
        this.f662i = -1;
        this.f663j = 0;
        this.f664k = 1;
        this.f665l = 2;
        this.f666m = 3;
        this.f667n = 4;
        this.f668o = 5;
        this.f645I = 0;
        this.f647K = false;
        this.f649M = null;
        this.f656c = new AdMarvelUniversalVideoView(this);
        this.f657d = new AdMarvelUniversalVideoView(this);
        this.f650N = new AdMarvelUniversalVideoView(this);
        this.f651O = new AdMarvelUniversalVideoView(this);
        this.f652P = new AdMarvelUniversalVideoView(this);
        this.f653Q = new AdMarvelUniversalVideoView(this);
        this.f658e = new AdMarvelUniversalVideoView(this);
        this.f648L = true;
        m366i();
    }

    public AdMarvelUniversalVideoView(Context context, boolean z, String str, AdMarvelWebView adMarvelWebView) {
        super(context);
        this.f659f = "AdMarvelUniversalVideoView";
        this.f662i = -1;
        this.f663j = 0;
        this.f664k = 1;
        this.f665l = 2;
        this.f666m = 3;
        this.f667n = 4;
        this.f668o = 5;
        this.f645I = 0;
        this.f647K = false;
        this.f649M = null;
        this.f656c = new AdMarvelUniversalVideoView(this);
        this.f657d = new AdMarvelUniversalVideoView(this);
        this.f650N = new AdMarvelUniversalVideoView(this);
        this.f651O = new AdMarvelUniversalVideoView(this);
        this.f652P = new AdMarvelUniversalVideoView(this);
        this.f653Q = new AdMarvelUniversalVideoView(this);
        this.f658e = new AdMarvelUniversalVideoView(this);
        try {
            this.f644H = context;
            this.f648L = z;
            m366i();
            this.f660g = Uri.parse(str);
            this.f649M = new WeakReference(adMarvelWebView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m346a(Uri uri, Map<String, String> map) {
        try {
            this.f660g = uri;
            this.f661h = map;
            this.f640D = 0;
            m370k();
            requestLayout();
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m350b(boolean z) {
        try {
            if (this.f655b != null) {
                this.f655b.reset();
                this.f655b.release();
                this.f655b = null;
                this.f669p = 0;
                if (z) {
                    this.f670q = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m366i() {
        this.f644H = getContext();
        this.f671r = 0;
        this.f672s = 0;
        if (this.f658e != null) {
            setSurfaceTextureListener(this.f658e);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        if (this.f655b == null) {
            this.f669p = 0;
            this.f670q = 0;
        }
    }

    private void m368j() {
        try {
            if (this.f655b != null) {
                this.f655b.setOnPreparedListener(this.f657d);
                this.f655b.setOnVideoSizeChangedListener(this.f656c);
                this.f655b.setOnCompletionListener(this.f650N);
                this.f655b.setOnErrorListener(this.f652P);
                this.f655b.setOnInfoListener(this.f651O);
                this.f655b.setOnBufferingUpdateListener(this.f653Q);
                this.f637A = 0;
                this.f655b.setScreenOnWhilePlaying(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m370k() {
        if (this.f660g != null) {
            if (Utils.m232r(this.f644H)) {
                if (this.f655b == null) {
                    try {
                        this.f655b = new MediaPlayer();
                        m368j();
                        this.f655b.setDataSource(this.f644H, this.f660g, this.f661h);
                        this.f655b.setAudioStreamType(3);
                        this.f655b.prepareAsync();
                        this.f669p = 1;
                        m372l();
                        return;
                    } catch (Throwable e) {
                        Log.w(this.f659f, "Unable to open content: " + this.f660g, e);
                        this.f669p = -1;
                        this.f670q = -1;
                        this.f652P.onError(this.f655b, 1, 0);
                        return;
                    } catch (Throwable e2) {
                        Log.w(this.f659f, "Unable to open content: " + this.f660g, e2);
                        this.f669p = -1;
                        this.f670q = -1;
                        this.f652P.onError(this.f655b, 1, 0);
                        return;
                    }
                }
                m368j();
            } else if (this.f646J != null) {
                this.f646J.m83f();
            }
        }
    }

    private void m372l() {
        try {
            if (this.f655b != null && this.f677x != null) {
                this.f677x.setMediaPlayer(this);
                this.f677x.setAnchorView(getParent() instanceof View ? (View) getParent() : this);
                this.f677x.setEnabled(m376n());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void m374m() {
        try {
            if (this.f677x == null) {
                return;
            }
            if (this.f677x.isShowing()) {
                this.f677x.hide();
            } else {
                this.f677x.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean m376n() {
        return (this.f655b == null || this.f669p == -1 || this.f669p == 0 || this.f669p == 1) ? false : true;
    }

    public void m380a() {
        try {
            if (this.f655b != null) {
                this.f655b.stop();
                this.f655b.reset();
                this.f655b.release();
                this.f655b = null;
                this.f669p = 0;
                this.f670q = 0;
                if (this.f646J != null) {
                    this.f646J.m79b();
                }
            }
        } catch (Throwable e) {
            if (this.f646J != null) {
                this.f646J.m84g();
            }
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public void m381a(int i, int i2, int i3, int i4) {
        this.f671r = i;
        this.f672s = i2;
        this.f673t = i3;
        this.f674u = i4;
    }

    public void m382a(boolean z) {
        if (z && this.f649M != null && this.f649M.get() != null && ((AdMarvelWebView) this.f649M.get()).aa && this.f647K && m389h()) {
            this.f654a = null;
            if (this.f677x != null) {
                this.f677x.hide();
            }
            m388g();
            this.f647K = false;
        }
    }

    public void m383b() {
        m350b(false);
    }

    public void m384c() {
        float f = getContext().getResources().getDisplayMetrics().density;
        LayoutParams layoutParams = (this.f671r <= 0 || this.f672s <= 0) ? new RelativeLayout.LayoutParams(-2, -2) : new RelativeLayout.LayoutParams((int) (((float) this.f671r) * f), (int) (((float) this.f672s) * f));
        if (this.f673t >= 0) {
            if (this.f674u >= 0) {
                layoutParams.leftMargin = (int) (((float) this.f673t) * f);
                layoutParams.topMargin = (int) (f * ((float) this.f674u));
            } else {
                layoutParams.leftMargin = (int) (f * ((float) this.f673t));
                layoutParams.addRule(15);
            }
        } else if (this.f674u >= 0) {
            layoutParams.topMargin = (int) (f * ((float) this.f674u));
            layoutParams.addRule(14);
        } else {
            layoutParams.addRule(13);
        }
        setLayoutParams(layoutParams);
    }

    public boolean canPause() {
        return this.f641E;
    }

    public boolean canSeekBackward() {
        return this.f642F;
    }

    public boolean canSeekForward() {
        return this.f643G;
    }

    public void m385d() {
        if (m376n() && this.f655b != null) {
            this.f655b.setVolume(0.0f, 0.0f);
        }
    }

    public void m386e() {
        if (m376n() && this.f655b != null) {
            this.f655b.setVolume(1.0f, 1.0f);
        }
    }

    public void m387f() {
        try {
            Log.d(this.f659f, "stopVideo: ");
            if (this.f655b != null) {
                this.f669p = 4;
                this.f670q = 4;
                this.f655b.pause();
                this.f655b.seekTo(0);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    void m388g() {
        Logging.log("AdMarvelUniversalVideoView : cleanUpVideoView - VideoView Destroyed");
        m350b(true);
        if (this.f646J != null) {
            this.f646J.m78a(this);
        }
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int getBufferPercentage() {
        return this.f655b != null ? this.f637A : 0;
    }

    public int getCurrentPosition() {
        return (!m376n() || this.f655b == null) ? 0 : this.f655b.getCurrentPosition();
    }

    public int getCurrentPositionToDisplay() {
        try {
            if (!m376n()) {
                return this.f645I;
            }
            int currentPosition = this.f655b.getCurrentPosition();
            this.f645I = currentPosition;
            return currentPosition;
        } catch (Exception e) {
            e.printStackTrace();
            this.f645I = 0;
            return 0;
        }
    }

    public int getDuration() {
        return (!m376n() || this.f655b == null) ? -1 : this.f655b.getDuration();
    }

    public int getLastCurrentPosition() {
        return this.f669p;
    }

    boolean m389h() {
        boolean z = false;
        AdMarvelWebView adMarvelWebView = (AdMarvelWebView) this.f649M.get();
        if (adMarvelWebView == null) {
            return true;
        }
        int[] iArr = new int[]{-1, -1};
        adMarvelWebView.getLocationInWindow(iArr);
        int height = adMarvelWebView.getHeight() > 0 ? adMarvelWebView.getHeight() / 2 : 0;
        int i = (adMarvelWebView.f891w == ExploreByTouchHelper.INVALID_ID || adMarvelWebView.f891w <= 0) ? 0 : adMarvelWebView.f891w;
        if (iArr[1] > 0 && (iArr[1] - i) + height >= 0 && height + iArr[1] < Utils.m224n(adMarvelWebView.getContext())) {
            z = true;
        }
        return z;
    }

    public boolean isPlaying() {
        return m376n() && this.f655b.isPlaying();
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AdMarvelUniversalVideoView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AdMarvelUniversalVideoView.class.getName());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (m376n() && z && this.f677x != null) {
            if (keyCode == 79 || keyCode == 85) {
                try {
                    if (this.f655b.isPlaying()) {
                        pause();
                        this.f677x.show();
                        return true;
                    }
                    start();
                    this.f677x.hide();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (keyCode == TransportMediator.KEYCODE_MEDIA_PLAY) {
                if (this.f655b.isPlaying()) {
                    return true;
                }
                start();
                this.f677x.hide();
                return true;
            } else if (keyCode != 86 && keyCode != Service.LOCUS_CON) {
                m374m();
            } else if (!this.f655b.isPlaying()) {
                return true;
            } else {
                pause();
                this.f677x.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int defaultSize = AdMarvelUniversalVideoView.getDefaultSize(this.f671r, widthMeasureSpec);
            int defaultSize2 = AdMarvelUniversalVideoView.getDefaultSize(this.f672s, heightMeasureSpec);
            if (this.f671r > 0 && this.f672s > 0) {
                int mode = MeasureSpec.getMode(widthMeasureSpec);
                int size = MeasureSpec.getSize(widthMeasureSpec);
                int mode2 = MeasureSpec.getMode(heightMeasureSpec);
                defaultSize2 = MeasureSpec.getSize(heightMeasureSpec);
                if (mode == 1073741824 && mode2 == 1073741824) {
                    if (this.f671r * defaultSize2 < this.f672s * size) {
                        defaultSize = (this.f671r * defaultSize2) / this.f672s;
                    } else if (this.f671r * defaultSize2 > this.f672s * size) {
                        defaultSize2 = (this.f672s * size) / this.f671r;
                        defaultSize = size;
                    } else {
                        defaultSize = size;
                    }
                } else if (mode == 1073741824) {
                    defaultSize = (this.f672s * size) / this.f671r;
                    if (mode2 != ExploreByTouchHelper.INVALID_ID || defaultSize <= defaultSize2) {
                        defaultSize2 = defaultSize;
                        defaultSize = size;
                    } else {
                        defaultSize = size;
                    }
                } else if (mode2 == 1073741824) {
                    defaultSize = (this.f671r * defaultSize2) / this.f672s;
                    if (mode == ExploreByTouchHelper.INVALID_ID && defaultSize > size) {
                        defaultSize = size;
                    }
                } else {
                    int i = this.f671r;
                    defaultSize = this.f672s;
                    if (mode2 != ExploreByTouchHelper.INVALID_ID || defaultSize <= defaultSize2) {
                        defaultSize2 = defaultSize;
                        defaultSize = i;
                    } else {
                        defaultSize = (this.f671r * defaultSize2) / this.f672s;
                    }
                    if (mode == ExploreByTouchHelper.INVALID_ID && r1 > size) {
                        defaultSize2 = (this.f672s * size) / this.f671r;
                        defaultSize = size;
                    }
                }
            }
            setMeasuredDimension(defaultSize, defaultSize2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (m376n() && this.f677x != null) {
            m374m();
        }
        if (this.f646J != null) {
            this.f646J.m82e();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (m376n() && this.f677x != null) {
            m374m();
        }
        return false;
    }

    public void pause() {
        try {
            if (m376n() && this.f655b != null && this.f655b.isPlaying()) {
                this.f655b.pause();
                this.f669p = 4;
                if (this.f646J != null) {
                    this.f646J.m77a();
                }
                this.f645I = getCurrentPosition();
            }
            this.f670q = 4;
        } catch (Throwable e) {
            if (this.f646J != null) {
                this.f646J.m84g();
            }
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public void seekTo(int msec) {
        try {
            if (m376n()) {
                this.f655b.seekTo(msec);
                this.f640D = 0;
                return;
            }
            this.f640D = msec;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setListener(AdMarvelUniversalVideoView listener) {
        this.f646J = listener;
    }

    public void setMediaController(MediaController controller) {
        try {
            if (this.f677x != null) {
                this.f677x.hide();
            }
            this.f677x = controller;
            m372l();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnCompletionListener(OnCompletionListener l) {
        this.f678y = l;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.f638B = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        this.f639C = l;
    }

    public void setOnPreparedListener(OnPreparedListener l) {
        this.f679z = l;
    }

    public void setVideoPath(String path) {
        try {
            setVideoURI(Uri.parse(path));
        } catch (Throwable e) {
            if (this.f646J != null) {
                this.f646J.m84g();
            }
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public void setVideoURI(Uri uri) {
        try {
            m346a(uri, null);
        } catch (Throwable e) {
            if (this.f646J != null) {
                this.f646J.m84g();
            }
            Logging.log(Log.getStackTraceString(e));
        }
    }

    public void start() {
        try {
            if (m376n() && this.f655b != null) {
                if (this.f645I <= 0 || this.f670q != 4) {
                    if (this.f646J != null) {
                        this.f646J.m81d();
                    }
                } else if (this.f646J != null) {
                    this.f646J.m80c();
                }
                this.f655b.start();
                this.f669p = 3;
            }
            this.f670q = 3;
        } catch (Throwable e) {
            if (this.f646J != null) {
                this.f646J.m84g();
            }
            Logging.log(Log.getStackTraceString(e));
        }
    }
}
