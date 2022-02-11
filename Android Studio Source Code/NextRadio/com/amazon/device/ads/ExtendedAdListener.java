package com.amazon.device.ads;

import android.graphics.Rect;

public interface ExtendedAdListener extends AdListener {
    void onAdExpired(Ad ad);

    void onAdResized(Ad ad, Rect rect);
}
