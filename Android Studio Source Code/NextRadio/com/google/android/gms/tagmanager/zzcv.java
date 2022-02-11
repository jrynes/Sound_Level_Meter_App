package com.google.android.gms.tagmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;

class zzcv {

    /* renamed from: com.google.android.gms.tagmanager.zzcv.1 */
    static class C10491 implements Runnable {
        final /* synthetic */ Editor zzblc;

        C10491(Editor editor) {
            this.zzblc = editor;
        }

        public void run() {
            this.zzblc.commit();
        }
    }

    static void zza(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            new Thread(new C10491(editor)).start();
        }
    }

    @SuppressLint({"CommitPrefEdits"})
    static void zzb(Context context, String str, String str2, String str3) {
        Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putString(str2, str3);
        zza(edit);
    }
}
