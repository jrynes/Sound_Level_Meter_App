package com.facebook.ads;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.facebook.ads.NativeAdView.Type;
import java.util.ArrayList;
import java.util.List;

public class NativeAdScrollView extends LinearLayout {
    public static final int DEFAULT_INSET = 20;
    public static final int DEFAULT_MAX_ADS = 10;
    private final Context f1514a;
    private final NativeAdsManager f1515b;
    private final AdViewProvider f1516c;
    private final Type f1517d;
    private final int f1518e;
    private final C0424b f1519f;
    private final NativeAdViewAttributes f1520g;

    public interface AdViewProvider {
        View createView(NativeAd nativeAd, int i);

        void destroyView(NativeAd nativeAd, View view);
    }

    /* renamed from: com.facebook.ads.NativeAdScrollView.a */
    private class C0423a extends PagerAdapter {
        final /* synthetic */ NativeAdScrollView f1511a;
        private List<NativeAd> f1512b;

        public C0423a(NativeAdScrollView nativeAdScrollView) {
            this.f1511a = nativeAdScrollView;
            this.f1512b = new ArrayList();
        }

        public void m1145a() {
            this.f1512b.clear();
            int min = Math.min(this.f1511a.f1518e, this.f1511a.f1515b.getUniqueNativeAdCount());
            for (int i = 0; i < min; i++) {
                NativeAd nextNativeAd = this.f1511a.f1515b.nextNativeAd();
                nextNativeAd.m1141a(true);
                this.f1512b.add(nextNativeAd);
            }
            notifyDataSetChanged();
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            if (i < this.f1512b.size()) {
                if (this.f1511a.f1517d != null) {
                    ((NativeAd) this.f1512b.get(i)).unregisterView();
                } else {
                    this.f1511a.f1516c.destroyView((NativeAd) this.f1512b.get(i), (View) obj);
                }
            }
            viewGroup.removeView((View) obj);
        }

        public int getCount() {
            return this.f1512b.size();
        }

        public int getItemPosition(Object obj) {
            int indexOf = this.f1512b.indexOf(obj);
            return indexOf >= 0 ? indexOf : -2;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View render = this.f1511a.f1517d != null ? NativeAdView.render(this.f1511a.f1514a, (NativeAd) this.f1512b.get(i), this.f1511a.f1517d, this.f1511a.f1520g) : this.f1511a.f1516c.createView((NativeAd) this.f1512b.get(i), i);
            viewGroup.addView(render);
            return render;
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    /* renamed from: com.facebook.ads.NativeAdScrollView.b */
    private class C0424b extends ViewPager {
        final /* synthetic */ NativeAdScrollView f1513a;

        public C0424b(NativeAdScrollView nativeAdScrollView, Context context) {
            this.f1513a = nativeAdScrollView;
            super(context);
        }

        protected void onMeasure(int i, int i2) {
            int i3 = 0;
            for (int i4 = 0; i4 < getChildCount(); i4++) {
                View childAt = getChildAt(i4);
                childAt.measure(i, MeasureSpec.makeMeasureSpec(0, 0));
                int measuredHeight = childAt.getMeasuredHeight();
                if (measuredHeight > i3) {
                    i3 = measuredHeight;
                }
            }
            super.onMeasure(i, MeasureSpec.makeMeasureSpec(i3, 1073741824));
        }
    }

    public NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, AdViewProvider adViewProvider) {
        this(context, nativeAdsManager, adViewProvider, null, null, DEFAULT_MAX_ADS);
    }

    public NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, AdViewProvider adViewProvider, int i) {
        this(context, nativeAdsManager, adViewProvider, null, null, i);
    }

    private NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, AdViewProvider adViewProvider, Type type, NativeAdViewAttributes nativeAdViewAttributes, int i) {
        super(context);
        if (!nativeAdsManager.isLoaded()) {
            throw new IllegalStateException("NativeAdsManager not loaded");
        } else if (type == null && adViewProvider == null) {
            throw new IllegalArgumentException("Must provide one of AdLayoutProperties or a CustomAdView");
        } else {
            this.f1514a = context;
            this.f1515b = nativeAdsManager;
            this.f1520g = nativeAdViewAttributes;
            this.f1516c = adViewProvider;
            this.f1517d = type;
            this.f1518e = i;
            PagerAdapter c0423a = new C0423a(this);
            this.f1519f = new C0424b(this, context);
            this.f1519f.setAdapter(c0423a);
            setInset(DEFAULT_INSET);
            c0423a.m1145a();
            addView(this.f1519f);
        }
    }

    public NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, Type type) {
        this(context, nativeAdsManager, null, type, new NativeAdViewAttributes(), DEFAULT_MAX_ADS);
    }

    public NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, Type type, NativeAdViewAttributes nativeAdViewAttributes) {
        this(context, nativeAdsManager, null, type, nativeAdViewAttributes, DEFAULT_MAX_ADS);
    }

    public NativeAdScrollView(Context context, NativeAdsManager nativeAdsManager, Type type, NativeAdViewAttributes nativeAdViewAttributes, int i) {
        this(context, nativeAdsManager, null, type, nativeAdViewAttributes, i);
    }

    public void setInset(int i) {
        if (i > 0) {
            DisplayMetrics displayMetrics = this.f1514a.getResources().getDisplayMetrics();
            int round = Math.round(((float) i) * displayMetrics.density);
            this.f1519f.setPadding(round, 0, round, 0);
            this.f1519f.setPageMargin(Math.round(displayMetrics.density * ((float) (i / 2))));
            this.f1519f.setClipToPadding(false);
        }
    }
}
