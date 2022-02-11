package com.nextradioapp.radioadapters;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

public class FmRadioEmulated implements IFmRadio, IFmLocalTune {
    private static final String TAG = "FmRadioEmulated";
    private SpecialOnAudioFocusChangeListener mAudioFocusListener;
    private AudioManager mAudioManager;
    private IRadioEventListener mCallback;
    public Context mContext;
    private int mCurrRadioPowerOffType;
    private int mFreq;
    private int mFrequencyStep;
    final Handler mHandler;
    private boolean mIsOn;
    private int mLastAudioFocusChange;
    private int mSubChannel;

    public FmRadioEmulated() {
        this.mHandler = new Handler();
        this.mAudioFocusListener = new SpecialOnAudioFocusChangeListener(this);
        this.mFrequencyStep = 200000;
    }

    public void withTuneStep(int step) {
        this.mFrequencyStep = 100000 * step;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void setRadioCallback(IRadioEventListener listener) {
        this.mCallback = listener;
    }

    public boolean getIsPoweredOn() {
        return this.mIsOn;
    }

    private void powerOffTransient() {
        Log.d(TAG, "powerOffTransient");
        this.mCurrRadioPowerOffType = 2;
        this.mIsOn = false;
    }

    private void powerOnTransient() {
        Log.d(TAG, "powerOnTransient");
        this.mIsOn = true;
        this.mCallback.onRadioFrequencyChanged(this.mFreq, 0);
    }

    public void powerOffAsync() {
        Log.d(TAG, "powerOffAsync");
        this.mCurrRadioPowerOffType = 1;
        this.mIsOn = false;
        this.mAudioFocusListener.disabled = true;
        this.mCallback.onRadioPoweredOff(1);
    }

    public void powerOnAsync(int startingFrequency, int region) {
        this.mLastAudioFocusChange = 1;
        if (this.mAudioFocusListener != null) {
            this.mAudioFocusListener.disabled = true;
        }
        this.mAudioFocusListener = new SpecialOnAudioFocusChangeListener(this);
        int result = this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1);
        Log.d(TAG, "requestAudioFocus() result=" + result);
        if (result == 1) {
            this.mIsOn = true;
            this.mCallback.onRadioPoweredOn();
            this.mCallback.onRadioFrequencyChanged(startingFrequency, 0);
        }
    }

    public boolean getIsMuted() {
        return false;
    }

    public void mute() {
    }

    public void unmute() {
    }

    public int getCurrentFrequency() {
        return this.mFreq;
    }

    public void setFrequencyAsync(int frequencyHz) {
        this.mFreq = frequencyHz;
        setFrequencyAsync(this.mFreq, 0);
    }

    public void tuneAsync(int direction) {
        this.mFreq += this.mFrequencyStep * direction;
    }

    public void seekAsync(int direction) {
        Log.d("FMRadioEmulated", "seekAsync - " + direction + " mFreq " + this.mFreq);
        this.mFreq += this.mFrequencyStep * direction;
        this.mCallback.onRadioFrequencyChanged(this.mFreq, 0);
    }

    public String getCurrentRDSRadioText() {
        return null;
    }

    public String getCurrentRDSProgramIdentification() {
        return null;
    }

    public String getCurrentRDSProgramService() {
        return null;
    }

    public int[] getCurrentRDSAlternativeFrequencies() {
        return null;
    }

    public void toggleSpeaker(boolean useSpeaker) {
    }

    public void serviceShuttingDown() {
    }

    public void powerOnAsync(int startingFrequency, int HDSubChannel, int region) {
        this.mFreq = startingFrequency;
        this.mSubChannel = HDSubChannel;
        int result = this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1);
        this.mIsOn = true;
        this.mCallback.onRadioPoweredOn();
        this.mCallback.onRadioFrequencyChanged(startingFrequency, HDSubChannel);
    }

    public boolean getSupportHD() {
        return false;
    }

    public void setFrequencyAsync(int frequencyHz, int subChannel) {
        this.mFreq = frequencyHz;
        this.mSubChannel = subChannel;
        this.mCallback.onRadioFrequencyChanged(this.mFreq, this.mSubChannel);
    }

    public boolean getIsBroadcastService() {
        return false;
    }

    public boolean getIsBroadcastRuning() {
        return false;
    }

    public void setBinder(IFmBind bind) {
    }

    public boolean isBluAdapter() {
        return false;
    }
}
