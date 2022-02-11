package com.facebook.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.util.C0526k;
import com.facebook.ads.internal.util.C0536s;
import com.facebook.ads.internal.view.C0554e;
import com.facebook.ads.internal.view.video.C0579a;

public class MediaView extends RelativeLayout {
    private final C0554e f1455a;
    private final C0579a f1456b;
    private boolean f1457c;
    private boolean f1458d;

    public MediaView(Context context) {
        this(context, null);
    }

    public MediaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1457c = false;
        this.f1458d = true;
        this.f1455a = new C0554e(context);
        this.f1455a.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.f1455a);
        this.f1456b = new C0579a(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.f1456b.setLayoutParams(layoutParams);
        this.f1456b.setAutoplay(this.f1458d);
        addView(this.f1456b);
    }

    private boolean m1092a(NativeAd nativeAd) {
        return !C0536s.m1572a(nativeAd.m1139a());
    }

    public boolean isAutoplay() {
        return this.f1458d;
    }

    public void setAutoplay(boolean z) {
        this.f1458d = z;
        this.f1456b.setAutoplay(z);
    }

    public void setNativeAd(NativeAd nativeAd) {
        nativeAd.m1143b(true);
        nativeAd.setMediaViewAutoplay(this.f1458d);
        if (this.f1457c) {
            this.f1455a.m1600a(null, null);
            this.f1456b.m1661b();
            this.f1457c = false;
        }
        if (m1092a(nativeAd)) {
            this.f1455a.setVisibility(4);
            this.f1456b.setVisibility(0);
            bringChildToFront(this.f1456b);
            this.f1457c = true;
            try {
                this.f1456b.setVideoPlayReportURI(nativeAd.m1142b());
                this.f1456b.setVideoTimeReportURI(nativeAd.m1144c());
                this.f1456b.setVideoURI(nativeAd.m1139a());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (nativeAd.getAdCoverImage() != null) {
            this.f1456b.m1660a();
            this.f1456b.setVisibility(4);
            this.f1455a.setVisibility(0);
            bringChildToFront(this.f1455a);
            this.f1457c = true;
            new C0526k(this.f1455a).execute(new String[]{nativeAd.getAdCoverImage().getUrl()});
        }
    }
}
