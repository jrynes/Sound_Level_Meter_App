package com.onelouder.adlib;

import android.content.SharedPreferences.Editor;

class SharedPreferencesFroyo {
    SharedPreferencesFroyo() {
    }

    public static void apply(Editor editor) {
        editor.commit();
    }
}
