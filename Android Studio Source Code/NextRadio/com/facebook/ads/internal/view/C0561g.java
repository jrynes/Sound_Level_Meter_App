package com.facebook.ads.internal.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.facebook.ads.internal.util.C0435u;
import com.facebook.ads.internal.util.C0531o;
import com.google.android.gms.common.api.CommonStatusCodes;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;

/* renamed from: com.facebook.ads.internal.view.g */
public class C0561g extends LinearLayout {
    private VideoView f2002a;
    private String f2003b;
    private String f2004c;
    private boolean f2005d;
    private int f2006e;
    private Handler f2007f;
    private Handler f2008g;

    /* renamed from: com.facebook.ads.internal.view.g.a */
    private static final class C0559a extends C0435u<C0561g> {
        public C0559a(C0561g c0561g) {
            super(c0561g);
        }

        public void run() {
            C0561g c0561g = (C0561g) m1184a();
            if (c0561g == null) {
                return;
            }
            if (c0561g.f2002a.getCurrentPosition() > CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS) {
                new C0531o().execute(new String[]{c0561g.getVideoPlayReportURI()});
                return;
            }
            c0561g.f2007f.postDelayed(this, 250);
        }
    }

    /* renamed from: com.facebook.ads.internal.view.g.b */
    private static final class C0560b extends C0435u<C0561g> {
        public C0560b(C0561g c0561g) {
            super(c0561g);
        }

        public void run() {
            C0561g c0561g = (C0561g) m1184a();
            if (c0561g != null) {
                int currentPosition = c0561g.f2002a.getCurrentPosition();
                if (currentPosition > c0561g.f2006e) {
                    c0561g.f2006e = currentPosition;
                }
                c0561g.f2008g.postDelayed(this, 250);
            }
        }
    }

    public C0561g(Context context) {
        super(context);
        m1625c();
    }

    private void m1625c() {
        MediaController mediaController = new MediaController(getContext());
        this.f2002a = new VideoView(getContext());
        mediaController.setAnchorView(this);
        this.f2002a.setMediaController(mediaController);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(11, -1);
        layoutParams.addRule(9, -1);
        layoutParams.addRule(10, -1);
        layoutParams.addRule(12, -1);
        layoutParams.addRule(13);
        this.f2002a.setLayoutParams(layoutParams);
        addView(this.f2002a);
        this.f2008g = new Handler();
        this.f2008g.postDelayed(new C0560b(this), 250);
        this.f2007f = new Handler();
        this.f2007f.postDelayed(new C0559a(this), 250);
    }

    private void m1627d() {
        if (!this.f2005d && getVideoTimeReportURI() != null) {
            Map hashMap = new HashMap();
            hashMap.put("time", Integer.toString(this.f2006e / ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH));
            hashMap.put("inline", "0");
            new C0531o(hashMap).execute(new String[]{getVideoTimeReportURI()});
            this.f2005d = true;
            this.f2006e = 0;
        }
    }

    public void m1628a() {
        this.f2002a.start();
    }

    public void m1629b() {
        if (this.f2002a != null) {
            this.f2002a.stopPlayback();
        }
    }

    public String getVideoPlayReportURI() {
        return this.f2003b;
    }

    public String getVideoTimeReportURI() {
        return this.f2004c;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        m1627d();
    }

    public void setVideoPlayReportURI(String str) {
        this.f2003b = str;
    }

    public void setVideoTimeReportURI(String str) {
        this.f2004c = str;
    }

    public void setVideoURI(Uri uri) {
        if (uri != null) {
            this.f2002a.setVideoURI(uri);
        }
    }

    public void setVideoURI(String str) {
        if (str != null) {
            setVideoURI(Uri.parse(str));
        }
    }
}
