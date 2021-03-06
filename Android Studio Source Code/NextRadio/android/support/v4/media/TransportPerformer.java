package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;
import com.google.android.gms.location.places.Place;
import com.rabbitmq.client.impl.AMQImpl.Confirm;
import org.xbill.DNS.WKSRecord.Service;

public abstract class TransportPerformer {
    static final int AUDIOFOCUS_GAIN = 1;
    static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    static final int AUDIOFOCUS_LOSS = -1;
    static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
    static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

    public abstract long onGetCurrentPosition();

    public abstract long onGetDuration();

    public abstract boolean onIsPlaying();

    public abstract void onPause();

    public abstract void onSeekTo(long j);

    public abstract void onStart();

    public abstract void onStop();

    public int onGetBufferPercentage() {
        return 100;
    }

    public int onGetTransportControlFlags() {
        return 60;
    }

    public boolean onMediaButtonDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case Service.FINGER /*79*/:
            case Confirm.INDEX /*85*/:
                if (!onIsPlaying()) {
                    onStart();
                    break;
                }
                onPause();
                break;
            case Place.TYPE_STADIUM /*86*/:
                onStop();
                break;
            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
                onStart();
                break;
            case Service.LOCUS_CON /*127*/:
                onPause();
                break;
        }
        return true;
    }

    public boolean onMediaButtonUp(int keyCode, KeyEvent event) {
        return true;
    }

    public void onAudioFocusChange(int focusChange) {
        int keyCode = 0;
        switch (focusChange) {
            case AUDIOFOCUS_LOSS /*-1*/:
                keyCode = Service.LOCUS_CON;
                break;
        }
        if (keyCode != 0) {
            long now = SystemClock.uptimeMillis();
            onMediaButtonDown(keyCode, new KeyEvent(now, now, 0, keyCode, 0));
            onMediaButtonUp(keyCode, new KeyEvent(now, now, AUDIOFOCUS_GAIN, keyCode, 0));
        }
    }
}
