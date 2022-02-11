package com.amazon.device.ads;

import android.os.Build;
import android.os.Build.VERSION;

class AndroidBuildInfo {
    private String make;
    private String model;
    private String osVersion;
    private int sdkInt;

    AndroidBuildInfo() {
        this.make = Build.MANUFACTURER;
        this.model = Build.MODEL;
        this.osVersion = VERSION.RELEASE;
        this.sdkInt = VERSION.SDK_INT;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public int getSDKInt() {
        return this.sdkInt;
    }
}
