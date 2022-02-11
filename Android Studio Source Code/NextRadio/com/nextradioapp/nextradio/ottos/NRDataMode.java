package com.nextradioapp.nextradio.ottos;

public class NRDataMode {
    public boolean mIsDataModeOff;
    public boolean mIsFreshRequest;

    public String toString() {
        return "NRDataMode current mode:" + this.mIsDataModeOff + " FreshRequest " + this.mIsFreshRequest;
    }
}
