package com.amazon.device.ads;

import android.content.Context;

interface UserAgentManager {
    String getUserAgentString();

    void populateUserAgentString(Context context);

    void setUserAgentString(String str);
}
