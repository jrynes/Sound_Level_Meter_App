package com.admarvel.android.ads.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import com.admarvel.android.ads.AdMarvelUtils;
import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* renamed from: com.admarvel.android.ads.nativeads.d */
public class AdMarvelVisibilityDetector {
    private WeakReference<View> f710a;
    private AdMarvelVisibilityDetector f711b;
    private WeakReference<Context> f712c;
    private AdMarvelVisibilityDetector f713d;
    private int f714e;
    private ScheduledThreadPoolExecutor f715f;

    /* renamed from: com.admarvel.android.ads.nativeads.d.a */
    interface AdMarvelVisibilityDetector {
        void m391a(boolean z);
    }

    /* renamed from: com.admarvel.android.ads.nativeads.d.b */
    private static class AdMarvelVisibilityDetector implements Runnable {
        private AdMarvelVisibilityDetector f706a;
        private WeakReference<Context> f707b;
        private View f708c;
        private AdMarvelVisibilityDetector f709d;

        public AdMarvelVisibilityDetector(AdMarvelVisibilityDetector adMarvelVisibilityDetector, Context context, View view, AdMarvelVisibilityDetector adMarvelVisibilityDetector2) {
            this.f706a = adMarvelVisibilityDetector;
            this.f707b = new WeakReference(context);
            this.f708c = view;
            this.f709d = adMarvelVisibilityDetector2;
        }

        public void run() {
            int i = 0;
            if (this.f706a != null && this.f709d != null) {
                Context context = (Context) this.f707b.get();
                if (context != null && !Thread.currentThread().isInterrupted()) {
                    int[] iArr = new int[]{-1, -1};
                    this.f708c.getLocationInWindow(iArr);
                    int height = this.f708c.getHeight() > 0 ? this.f708c.getHeight() / 2 : 0;
                    int height2 = this.f708c.getHeight() > 0 ? this.f708c.getHeight() / 2 : 0;
                    int width = this.f708c.getWidth() > 0 ? this.f708c.getWidth() / 2 : 0;
                    int width2 = this.f708c.getWidth() > 0 ? this.f708c.getWidth() / 2 : 0;
                    int a = (this.f709d.f714e == ExploreByTouchHelper.INVALID_ID || this.f709d.f714e <= 0) ? 0 : this.f709d.f714e;
                    if (this.f708c.getHeight() > 0 && this.f708c.getWidth() > 0 && height + (iArr[1] - a) >= 0 && iArr[1] + height2 < AdMarvelUtils.getDeviceHeight(context) && iArr[0] + width >= 0 && iArr[0] + width2 < AdMarvelUtils.getDeviceWidth(context)) {
                        i = 1;
                    }
                    if (!Thread.currentThread().isInterrupted() && r2 != 0) {
                        if (this.f706a != null) {
                            this.f706a.m391a(true);
                        }
                        if (this.f709d != null) {
                            this.f709d.m414c();
                        }
                    }
                }
            }
        }
    }

    public AdMarvelVisibilityDetector(AdMarvelVisibilityDetector adMarvelVisibilityDetector, Context context) {
        this.f714e = ExploreByTouchHelper.INVALID_ID;
        this.f711b = adMarvelVisibilityDetector;
        this.f712c = new WeakReference(context);
        m415d();
    }

    private void m412b() {
        m414c();
        Context context = this.f712c != null ? (Context) this.f712c.get() : null;
        if (context != null) {
            View view = this.f710a != null ? (View) this.f710a.get() : null;
            if (view != null) {
                this.f713d = null;
                this.f715f = null;
                this.f713d = new AdMarvelVisibilityDetector(this.f711b, context, view, this);
                this.f715f = new ScheduledThreadPoolExecutor(1);
                this.f715f.scheduleWithFixedDelay(this.f713d, 0, 500, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void m414c() {
        if (this.f713d != null && this.f715f != null) {
            this.f715f.remove(this.f713d);
            this.f715f.shutdown();
            this.f715f.purge();
            this.f713d = null;
        }
    }

    private void m415d() {
        if (this.f714e == ExploreByTouchHelper.INVALID_ID || this.f714e < 0) {
            Context context = this.f712c != null ? (Context) this.f712c.get() : null;
            if (context != null) {
                ViewGroup viewGroup = (ViewGroup) ((Activity) context).getWindow().findViewById(16908290);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int measuredHeight = displayMetrics.heightPixels - viewGroup.getMeasuredHeight();
                if (measuredHeight >= 0 && this.f714e == ExploreByTouchHelper.INVALID_ID) {
                    this.f714e = measuredHeight;
                    return;
                }
                return;
            }
            return;
        }
        measuredHeight = this.f714e;
    }

    View m416a() {
        return this.f710a != null ? (View) this.f710a.get() : null;
    }

    void m417a(View view) {
        this.f710a = new WeakReference(view);
        m412b();
    }
}
