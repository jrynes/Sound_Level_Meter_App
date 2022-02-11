package com.nextradioapp.nextradio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import android.view.KeyEvent;
import com.google.android.gms.location.places.Place;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.nextradio.services.RadioAdapterService_;
import com.rabbitmq.client.impl.AMQImpl.Confirm;
import org.xbill.DNS.WKSRecord.Service;

public class MusicIntentReceiver extends BroadcastReceiver {
    private static String TAG;

    static {
        TAG = "MusicIntentReceiver";
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            Intent i;
            if (intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
                Log.d(TAG, "MusicIntentReceiver.onReceive() - ACTION_AUDIO_BECOMING_NOISY");
                i = new Intent(context, RadioAdapterService_.class);
                i.setAction(RadioAdapterService.ACTION_STOP);
                context.startService(i);
            } else if (intent.getAction().equals("android.intent.action.MEDIA_BUTTON")) {
                KeyEvent keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT");
                if (keyEvent.getAction() == 0) {
                    Log.d(TAG, "MusicIntentReceiver.onReceive() - ACTION_DOWN, keycode: " + keyEvent.getKeyCode());
                    switch (keyEvent.getKeyCode()) {
                        case Service.FINGER /*79*/:
                        case Confirm.INDEX /*85*/:
                            i = new Intent(context, RadioAdapterService_.class);
                            i.setAction(RadioAdapterService.ACTION_PLAYPAUSE);
                            i.putExtra(RadioAdapterService.CMDNOTIF, 1);
                            i.putExtra("isPause", true);
                            context.startService(i);
                        case Place.TYPE_STADIUM /*86*/:
                            Intent stopIntent = new Intent(context, RadioAdapterService_.class);
                            stopIntent.setAction(RadioAdapterService.ACTION_STOP);
                            context.startService(stopIntent);
                        case Place.TYPE_STORAGE /*87*/:
                            Intent i3 = new Intent(context, RadioAdapterService_.class);
                            i3.setAction(RadioAdapterService.ACTION_SEEKUP);
                            i3.putExtra(RadioAdapterService.CMDNOTIF, 1);
                            i3.putExtra("widgetSeek", true);
                            context.startService(i3);
                        case Place.TYPE_STORE /*88*/:
                            Intent i4 = new Intent(context, RadioAdapterService_.class);
                            i4.setAction(RadioAdapterService.ACTION_SEEKDOWN);
                            i4.putExtra(RadioAdapterService.CMDNOTIF, 1);
                            i4.putExtra("widgetSeek", true);
                            context.startService(i4);
                        case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
                            Intent playIntent = new Intent(context, RadioAdapterService_.class);
                            playIntent.setAction(RadioAdapterService.ACTION_PLAY);
                            context.startService(playIntent);
                        case Service.LOCUS_CON /*127*/:
                            Intent pauseIntent = new Intent(context, RadioAdapterService_.class);
                            pauseIntent.setAction(RadioAdapterService.ACTION_PLAYPAUSE);
                            pauseIntent.putExtra(RadioAdapterService.CMDNOTIF, 1);
                            pauseIntent.putExtra("isPause", true);
                            context.startService(pauseIntent);
                        default:
                    }
                }
            }
        }
    }
}
