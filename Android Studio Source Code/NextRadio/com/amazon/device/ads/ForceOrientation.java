package com.amazon.device.ads;

import java.util.Locale;

enum ForceOrientation {
    PORTRAIT,
    LANDSCAPE,
    NONE;

    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
