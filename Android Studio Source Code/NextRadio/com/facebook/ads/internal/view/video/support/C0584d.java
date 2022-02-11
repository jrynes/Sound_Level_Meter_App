package com.facebook.ads.internal.view.video.support;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;

@TargetApi(14)
/* renamed from: com.facebook.ads.internal.view.video.support.d */
public class C0584d extends TextureView implements OnBufferingUpdateListener, OnPreparedListener, SurfaceTextureListener, C0583e {
    private static final String f2057i;
    private View f2058a;
    private Uri f2059b;
    private C0568b f2060c;
    private Surface f2061d;
    private MediaPlayer f2062e;
    private boolean f2063f;
    private boolean f2064g;
    private int f2065h;

    static {
        f2057i = C0584d.class.getSimpleName();
    }

    public C0584d(Context context) {
        super(context);
    }

    private void m1669a() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getContext(), this.f2059b);
            mediaPlayer.setSurface(this.f2061d);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnInfoListener(new C0582c(this.f2058a));
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepareAsync();
            this.f2062e = mediaPlayer;
        } catch (Exception e) {
            mediaPlayer.release();
            Log.e(f2057i, "Cannot prepare media player with SurfaceTexture: " + e);
        }
    }

    public void m1670a(View view, Uri uri) {
        this.f2058a = view;
        this.f2059b = uri;
        setSurfaceTextureListener(this);
    }

    public int getCurrentPosition() {
        return this.f2062e != null ? this.f2062e.getCurrentPosition() : 0;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        if (this.f2060c != null) {
            this.f2060c.m1638a(mediaPlayer);
        }
        if (this.f2064g) {
            mediaPlayer.start();
            this.f2064g = false;
        }
        if (this.f2065h > 0) {
            if (this.f2065h >= this.f2062e.getDuration()) {
                this.f2065h = 0;
            }
            this.f2062e.seekTo(this.f2065h);
            this.f2065h = 0;
        }
        this.f2063f = true;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.f2061d = new Surface(surfaceTexture);
        m1669a();
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void pause() {
        if (this.f2062e != null) {
            this.f2065h = this.f2062e.getCurrentPosition();
            this.f2062e.stop();
            this.f2062e.reset();
            this.f2062e.release();
        }
        this.f2062e = null;
        this.f2063f = false;
        this.f2064g = false;
    }

    public void setFrameVideoViewListener(C0568b c0568b) {
        this.f2060c = c0568b;
    }

    public void start() {
        if (this.f2063f) {
            this.f2062e.start();
        } else {
            this.f2064g = true;
        }
        if (isAvailable()) {
            onSurfaceTextureAvailable(getSurfaceTexture(), 0, 0);
        }
    }
}
