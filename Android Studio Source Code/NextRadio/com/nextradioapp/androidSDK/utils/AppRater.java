package com.nextradioapp.androidSDK.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.nextradioapp.androidSDK.C1136R;

public class AppRater {
    private static final String APP_PNAME = "com.nextradioapp.nextradio";
    private static final String APP_TITLE = "NextRadio";
    private static final int DAYS_UNTIL_PROMPT = 3;
    private static final int LAUNCHES_UNTIL_PROMPT = 6;

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.1 */
    static class C11501 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Editor val$editor;
        final /* synthetic */ Context val$mContext;

        C11501(Editor editor, Context context, Dialog dialog) {
            this.val$editor = editor;
            this.val$mContext = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            if (this.val$editor != null) {
                this.val$editor.putBoolean("dontshowagain", true);
                this.val$editor.commit();
            }
            this.val$mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.nextradioapp.nextradio")));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.2 */
    static class C11512 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Context val$mContext;

        C11512(Context context, Dialog dialog) {
            this.val$mContext = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            Editor editor = this.val$mContext.getSharedPreferences("apprater", 0).edit();
            editor.putLong("date_firstlaunch", System.currentTimeMillis() - 172800000);
            editor.putBoolean("remindmelater", false);
            editor.commit();
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.3 */
    static class C11523 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Editor val$editor;

        C11523(Editor editor, Dialog dialog) {
            this.val$editor = editor;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            if (this.val$editor != null) {
                this.val$editor.putBoolean("dontshowagain", true);
                this.val$editor.commit();
            }
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.4 */
    static class C11534 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Context val$mContext;

        C11534(Context context, Dialog dialog) {
            this.val$mContext = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.nextradioapp.nextradio")));
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.5 */
    static class C11545 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Context val$mContext;

        C11545(Context context, Dialog dialog) {
            this.val$mContext = context;
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            Editor editor = this.val$mContext.getSharedPreferences("apprater", 0).edit();
            editor.putLong("date_firstlaunch", System.currentTimeMillis() - 172800000);
            editor.putBoolean("remindmelater", false);
            editor.commit();
            this.val$dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.utils.AppRater.6 */
    static class C11556 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;

        C11556(Dialog dialog) {
            this.val$dialog = dialog;
        }

        public void onClick(View v) {
            this.val$dialog.dismiss();
        }
    }

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        Editor editor = prefs.edit();
        editor.putLong("launch_count", prefs.getLong("launch_count", 0) + 1);
        editor.putBoolean("remindmelater", false);
        if (Long.valueOf(prefs.getLong("date_firstlaunch", 0)).longValue() == 0) {
            editor.putLong("date_firstlaunch", Long.valueOf(System.currentTimeMillis()).longValue());
        }
        editor.commit();
    }

    public static void showRateDialog(Context mContext, Editor editor) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        Long date_firstLaunch = Long.valueOf(prefs.getLong("date_firstlaunch", 0));
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        if (!prefs.getBoolean("dontshowagain", false) && !prefs.getBoolean("remindmelater", false) && launch_count >= 6 && System.currentTimeMillis() >= date_firstLaunch.longValue() + 259200000) {
            Dialog dialog = new Dialog(mContext);
            dialog.setTitle("Send us some love!");
            dialog.setContentView(C1136R.layout.dialog_apprater);
            ((Button) dialog.findViewById(C1136R.id.btnRateNextRadio)).setOnClickListener(new C11501(editor, mContext, dialog));
            ((Button) dialog.findViewById(C1136R.id.btnRemindMeLater)).setOnClickListener(new C11512(mContext, dialog));
            ((Button) dialog.findViewById(C1136R.id.btnNoThanks)).setOnClickListener(new C11523(editor, dialog));
            dialog.show();
        }
    }

    public static void showRateDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Send us some love!");
        dialog.setContentView(C1136R.layout.dialog_apprater);
        ((Button) dialog.findViewById(C1136R.id.btnRateNextRadio)).setOnClickListener(new C11534(mContext, dialog));
        ((Button) dialog.findViewById(C1136R.id.btnRemindMeLater)).setOnClickListener(new C11545(mContext, dialog));
        ((Button) dialog.findViewById(C1136R.id.btnNoThanks)).setOnClickListener(new C11556(dialog));
        dialog.show();
    }
}
