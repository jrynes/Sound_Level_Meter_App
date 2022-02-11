package com.facebook.ads.internal.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView.Type;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.component.C0551b;
import com.facebook.ads.internal.view.component.C0553d;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Arrays;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.facebook.ads.internal.view.b */
public class C0544b extends RelativeLayout {
    private final NativeAdViewAttributes f1961a;
    private final NativeAd f1962b;
    private final DisplayMetrics f1963c;

    /* renamed from: com.facebook.ads.internal.view.b.1 */
    static /* synthetic */ class C05431 {
        static final /* synthetic */ int[] f1960a;

        static {
            f1960a = new int[Type.values().length];
            try {
                f1960a[Type.HEIGHT_400.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1960a[Type.HEIGHT_300.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1960a[Type.HEIGHT_100.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1960a[Type.HEIGHT_120.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public C0544b(Context context, NativeAd nativeAd, Type type, NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        setBackgroundColor(nativeAdViewAttributes.getBackgroundColor());
        this.f1961a = nativeAdViewAttributes;
        this.f1962b = nativeAd;
        this.f1963c = context.getResources().getDisplayMetrics();
        setLayoutParams(new LayoutParams(-1, Math.round(((float) type.getHeight()) * this.f1963c.density)));
        View c0565l = new C0565l(context);
        c0565l.setMinWidth(Math.round(280.0f * this.f1963c.density));
        c0565l.setMaxWidth(Math.round(375.0f * this.f1963c.density));
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13, -1);
        c0565l.setLayoutParams(layoutParams);
        addView(c0565l);
        ViewGroup linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        c0565l.addView(linearLayout);
        switch (C05431.f1960a[type.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                m1584b(linearLayout);
                break;
            case Zone.SECONDARY /*2*/:
                break;
        }
        m1580a(linearLayout);
        m1581a(linearLayout, type);
        View adChoicesView = new AdChoicesView(getContext(), nativeAd);
        LayoutParams layoutParams2 = (LayoutParams) adChoicesView.getLayoutParams();
        layoutParams2.addRule(11);
        layoutParams2.setMargins(Math.round(this.f1963c.density * 4.0f), Math.round(this.f1963c.density * 4.0f), Math.round(this.f1963c.density * 4.0f), Math.round(this.f1963c.density * 4.0f));
        c0565l.addView(adChoicesView);
    }

    private void m1580a(ViewGroup viewGroup) {
        View relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, Math.round(this.f1963c.density * BitmapDescriptorFactory.HUE_CYAN)));
        relativeLayout.setBackgroundColor(this.f1961a.getBackgroundColor());
        View mediaView = new MediaView(getContext());
        relativeLayout.addView(mediaView);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, (int) (this.f1963c.density * BitmapDescriptorFactory.HUE_CYAN));
        layoutParams.addRule(13, -1);
        mediaView.setLayoutParams(layoutParams);
        mediaView.setAutoplay(this.f1961a.getAutoplay());
        mediaView.setNativeAd(this.f1962b);
        viewGroup.addView(relativeLayout);
    }

    private void m1581a(ViewGroup viewGroup, Type type) {
        View c0551b = new C0551b(getContext(), this.f1962b, this.f1961a, m1582a(type), m1583b(type));
        c0551b.setLayoutParams(new LinearLayout.LayoutParams(-1, Math.round(((float) m1583b(type)) * this.f1963c.density)));
        viewGroup.addView(c0551b);
        this.f1962b.registerViewForInteraction(this, Arrays.asList(new View[]{c0551b.getIconView(), c0551b.getCallToActionView()}));
    }

    private boolean m1582a(Type type) {
        return type == Type.HEIGHT_300 || type == Type.HEIGHT_120;
    }

    private int m1583b(Type type) {
        switch (C05431.f1960a[type.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return (type.getHeight() - 180) / 2;
            case Zone.SECONDARY /*2*/:
                return type.getHeight() - 180;
            case Protocol.GGP /*3*/:
            case org.xbill.DNS.Type.MF /*4*/:
                return type.getHeight();
            default:
                return 0;
        }
    }

    private void m1584b(ViewGroup viewGroup) {
        View c0553d = new C0553d(getContext(), this.f1962b, this.f1961a);
        c0553d.setLayoutParams(new LinearLayout.LayoutParams(-1, Math.round(110.0f * this.f1963c.density)));
        viewGroup.addView(c0553d);
    }
}
