package com.admarvel.android.util;

import android.content.Context;
import android.util.TypedValue;

/* renamed from: com.admarvel.android.util.k */
public class Dips {
    public static float m605a(float f, Context context) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int m606b(float f, Context context) {
        return (int) (Dips.m605a(f, context) + 0.5f);
    }
}
