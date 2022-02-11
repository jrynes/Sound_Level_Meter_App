package com.facebook.ads.internal.view.video.support;

import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.view.video.support.a */
public class C0581a extends FrameLayout {
    private C0583e f2051a;
    private C0585f f2052b;
    private View f2053c;
    private Uri f2054d;
    private Context f2055e;

    /* renamed from: com.facebook.ads.internal.view.video.support.a.1 */
    static /* synthetic */ class C05801 {
        static final /* synthetic */ int[] f2050a;

        static {
            f2050a = new int[C0585f.values().length];
            try {
                f2050a[C0585f.TEXTURE_VIEW.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f2050a[C0585f.VIDEO_VIEW.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public C0581a(Context context) {
        super(context);
        this.f2055e = context;
        this.f2053c = m1665b(context);
        this.f2051a = m1664a(context);
        addView(this.f2053c);
    }

    private C0583e m1664a(Context context) {
        if (VERSION.SDK_INT >= 14) {
            this.f2052b = C0585f.TEXTURE_VIEW;
            Object c0584d = new C0584d(context);
            c0584d.m1670a(this.f2053c, this.f2054d);
            addView(c0584d);
            return c0584d;
        }
        this.f2052b = C0585f.VIDEO_VIEW;
        c0584d = new C0586g(context);
        addView(c0584d);
        return c0584d;
    }

    private View m1665b(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        view.setLayoutParams(new LayoutParams(-1, -1));
        return view;
    }

    public void m1666a() {
        this.f2051a.start();
    }

    public void m1667b() {
        this.f2051a.pause();
    }

    public int getCurrentPosition() {
        return this.f2051a.getCurrentPosition();
    }

    public View getPlaceholderView() {
        return this.f2053c;
    }

    public C0585f getVideoImplType() {
        return this.f2052b;
    }

    public void setFrameVideoViewListener(C0568b c0568b) {
        this.f2051a.setFrameVideoViewListener(c0568b);
    }

    public void setVideoImpl(C0585f c0585f) {
        removeAllViews();
        if (c0585f == C0585f.TEXTURE_VIEW && VERSION.SDK_INT < 14) {
            c0585f = C0585f.VIDEO_VIEW;
        }
        this.f2052b = c0585f;
        Object c0584d;
        switch (C05801.f2050a[c0585f.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                c0584d = new C0584d(this.f2055e);
                c0584d.m1670a(this.f2053c, this.f2054d);
                addView(c0584d);
                this.f2051a = c0584d;
                break;
            case Zone.SECONDARY /*2*/:
                c0584d = new C0586g(this.f2055e);
                c0584d.m1671a(this.f2053c, this.f2054d);
                addView(c0584d);
                this.f2051a = c0584d;
                break;
        }
        addView(this.f2053c);
        m1666a();
    }

    public void setup(Uri uri) {
        this.f2054d = uri;
        this.f2051a.m1668a(this.f2053c, uri);
    }
}
