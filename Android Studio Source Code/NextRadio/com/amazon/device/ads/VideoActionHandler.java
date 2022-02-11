package com.amazon.device.ads;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import com.amazon.device.ads.AdActivity.IAdActivityAdapter;
import com.amazon.device.ads.AdVideoPlayer.AdVideoPlayerListener;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;

class VideoActionHandler implements IAdActivityAdapter {
    private Activity activity;
    private RelativeLayout layout;
    private AdVideoPlayer player;

    /* renamed from: com.amazon.device.ads.VideoActionHandler.1 */
    class C03451 implements AdVideoPlayerListener {
        C03451() {
        }

        public void onError() {
            VideoActionHandler.this.activity.finish();
        }

        public void onComplete() {
            VideoActionHandler.this.activity.finish();
        }
    }

    VideoActionHandler() {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        Bundle data = this.activity.getIntent().getExtras();
        this.layout = new RelativeLayout(this.activity);
        this.layout.setLayoutParams(new LayoutParams(-1, -1));
        this.activity.setContentView(this.layout);
        initPlayer(data);
        this.player.playVideo();
    }

    private void setPlayerListener(AdVideoPlayer player) {
        player.setListener(new C03451());
    }

    private void initPlayer(Bundle data) {
        this.player = new AdVideoPlayer(this.activity);
        this.player.setPlayData(data.getString(SettingsJsonConstants.APP_URL_KEY));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
        lp.addRule(13);
        this.player.setLayoutParams(lp);
        this.player.setViewGroup(this.layout);
        setPlayerListener(this.player);
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStop() {
        this.player.releasePlayer();
        this.player = null;
        this.activity.finish();
    }

    public void preOnCreate() {
        this.activity.requestWindowFeature(1);
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public boolean onBackPressed() {
        return false;
    }
}
