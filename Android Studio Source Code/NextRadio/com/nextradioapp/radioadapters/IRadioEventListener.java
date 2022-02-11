package com.nextradioapp.radioadapters;

public interface IRadioEventListener {
    void onBroadcastDisabled();

    void onBroadcastEnabled();

    void onRadioFrequencyChanged(int i, int i2);

    void onRadioPoweredOff(int i);

    void onRadioPoweredOn();

    void onRadioRDSAlternativeFrequenciesChanged(int[] iArr);

    void onRadioRDSProgramIdentificationChanged(String str);

    void onRadioRDSProgramServiceChanged(String str);

    void onRadioRDSRadioTextChanged(String str);

    void onSignalStrengthChanged(int i);
}
