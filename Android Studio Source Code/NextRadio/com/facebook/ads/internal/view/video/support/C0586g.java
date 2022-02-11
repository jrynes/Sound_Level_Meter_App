package com.facebook.ads.internal.view.video.support;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

/* renamed from: com.facebook.ads.internal.view.video.support.g */
public class C0586g extends VideoView implements OnPreparedListener, C0583e {
    private View f2069a;
    private Uri f2070b;
    private C0568b f2071c;

    public C0586g(Context context) {
        super(context);
    }

    public void m1671a(View view, Uri uri) {
        this.f2069a = view;
        this.f2070b = uri;
        setOnPreparedListener(this);
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnInfoListener(new C0582c(this.f2069a));
        if (this.f2071c != null) {
            this.f2071c.m1638a(mediaPlayer);
        }
    }

    public void pause() {
        this.f2069a.setVisibility(0);
        stopPlayback();
    }

    public void setFrameVideoViewListener(C0568b c0568b) {
        this.f2071c = c0568b;
    }

    public void start() {
        setVideoURI(this.f2070b);
        super.start();
    }
}
