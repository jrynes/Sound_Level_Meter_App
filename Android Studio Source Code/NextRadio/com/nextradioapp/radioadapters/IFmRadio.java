package com.nextradioapp.radioadapters;

import android.content.Context;

public interface IFmRadio {
    int getCurrentFrequency();

    int[] getCurrentRDSAlternativeFrequencies();

    String getCurrentRDSProgramIdentification();

    String getCurrentRDSProgramService();

    String getCurrentRDSRadioText();

    boolean getIsBroadcastRuning();

    boolean getIsBroadcastService();

    boolean getIsMuted();

    boolean getIsPoweredOn();

    boolean getSupportHD();

    void init(Context context);

    boolean isBluAdapter();

    void mute();

    void powerOffAsync();

    void powerOnAsync(int i, int i2);

    void powerOnAsync(int i, int i2, int i3);

    void seekAsync(int i);

    void serviceShuttingDown();

    void setBinder(IFmBind iFmBind);

    void setFrequencyAsync(int i);

    void setFrequencyAsync(int i, int i2);

    void setRadioCallback(IRadioEventListener iRadioEventListener);

    void toggleSpeaker(boolean z);

    void tuneAsync(int i);

    void unmute();
}
