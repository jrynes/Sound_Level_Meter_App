package com.nextradioapp.nextradio;

import android.content.Context;
import android.content.Intent;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.nextradio.services.RadioAdapterService_;

public class IntentBuilder {
    public static Intent playPause(Context context) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_PLAYPAUSE);
        intent.putExtra("isPause", true);
        return intent;
    }

    public static Intent seek(Context context, int direction, boolean fromWidget) {
        if (direction > 0) {
            Intent intent = new Intent(context, RadioAdapterService_.class);
            intent.setAction(RadioAdapterService.ACTION_SEEKUP);
            intent.putExtra("widgetSeek", fromWidget);
            return intent;
        }
        intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_SEEKDOWN);
        intent.putExtra("widgetSeek", fromWidget);
        return intent;
    }

    public static Intent tune(Context context, int direction) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_TUNE);
        intent.putExtra("direction", direction);
        return intent;
    }

    public static Intent setFreq(Context context, int freqHz) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_SET_FREQ);
        intent.putExtra("frequencyHz", freqHz);
        return intent;
    }

    public static Intent turnOff(Context context, boolean isQuitting) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.putExtra("isQuitting", isQuitting);
        intent.setAction(RadioAdapterService.ACTION_STOP);
        return intent;
    }

    public static Intent turnOn(Context context) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_PLAY);
        return intent;
    }

    public static Intent toggleSpeakerOutput(Context context, boolean toggle) {
        Intent intent = new Intent(context, RadioAdapterService_.class);
        intent.setAction(RadioAdapterService.ACTION_TOGGLE_SPEAKER_OUTPUT);
        intent.putExtra("speakerToggle", toggle);
        return intent;
    }
}
