package com.onelouder.adlib;

import android.content.SharedPreferences.Editor;

class SharedPreferencesGingerbread {
    SharedPreferencesGingerbread() {
    }

    public static void apply(Editor editor) {
        editor.apply();
    }
}
