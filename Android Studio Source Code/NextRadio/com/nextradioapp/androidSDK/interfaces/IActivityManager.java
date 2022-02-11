package com.nextradioapp.androidSDK.interfaces;

import android.app.Activity;
import android.app.Application;

public interface IActivityManager {
    Activity getCurrentActivity();

    Application getCurrentApplication();

    void setCurrentActivity(Activity activity);
}
