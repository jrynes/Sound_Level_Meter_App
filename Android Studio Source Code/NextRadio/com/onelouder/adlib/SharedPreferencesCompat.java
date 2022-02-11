package com.onelouder.adlib;

import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;

class SharedPreferencesCompat {
    SharedPreferencesCompat() {
    }

    public static void apply(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            SharedPreferencesGingerbread.apply(editor);
        } else {
            SharedPreferencesFroyo.apply(editor);
        }
    }
}
