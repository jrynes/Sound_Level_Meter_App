package com.facebook.ads.internal.view.video.support;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.view.View;

/* renamed from: com.facebook.ads.internal.view.video.support.c */
public class C0582c implements OnInfoListener {
    private View f2056a;

    public C0582c(View view) {
        this.f2056a = view;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (i != 3) {
            return false;
        }
        this.f2056a.setVisibility(8);
        return true;
    }
}
