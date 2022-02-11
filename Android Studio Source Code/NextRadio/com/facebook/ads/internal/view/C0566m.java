package com.facebook.ads.internal.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.internal.view.C0557h.C0406a;

/* renamed from: com.facebook.ads.internal.view.m */
public class C0566m implements C0557h {
    private C0561g f2016a;

    public C0566m(InterstitialAdActivity interstitialAdActivity, C0406a c0406a) {
        this.f2016a = new C0561g(interstitialAdActivity);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        this.f2016a.setLayoutParams(layoutParams);
        c0406a.m1080a(this.f2016a);
    }

    public void m1633a() {
    }

    public void m1634a(Intent intent, Bundle bundle) {
        String stringExtra = intent.getStringExtra(InterstitialAdActivity.VIDEO_URL);
        String stringExtra2 = intent.getStringExtra(InterstitialAdActivity.VIDEO_PLAY_REPORT_URL);
        String stringExtra3 = intent.getStringExtra(InterstitialAdActivity.VIDEO_TIME_REPORT_URL);
        this.f2016a.setVideoPlayReportURI(stringExtra2);
        this.f2016a.setVideoTimeReportURI(stringExtra3);
        this.f2016a.setVideoURI(stringExtra);
        this.f2016a.m1628a();
    }

    public void m1635a(Bundle bundle) {
    }

    public void m1636b() {
    }

    public void m1637c() {
        this.f2016a.m1629b();
    }
}
