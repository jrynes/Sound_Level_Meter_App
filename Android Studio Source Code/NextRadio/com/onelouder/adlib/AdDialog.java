package com.onelouder.adlib;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;

public class AdDialog extends Activity {
    private boolean bOkClicked;
    private AdPlacement mPlacement;

    /* renamed from: com.onelouder.adlib.AdDialog.1 */
    class C12791 implements OnClickListener {
        C12791() {
        }

        public void onClick(View v) {
            AdDialog.this.finish();
        }
    }

    /* renamed from: com.onelouder.adlib.AdDialog.2 */
    class C12802 implements OnClickListener {
        C12802() {
        }

        public void onClick(View v) {
            AdDialog.this.bOkClicked = true;
            AdDialog.this.mPlacement.displayInterstitial(AdDialog.this);
            AdDialog.this.finish();
        }
    }

    public AdDialog() {
        this.bOkClicked = false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        requestWindowFeature(1);
        String placementid = getIntent().getStringExtra(AdPlacement.EXTRA_1L_PLACEMENT_ID);
        this.mPlacement = PlacementManager.getInstance().getAdPlacement(this, placementid, "interstitial");
        if (this.mPlacement != null) {
            View textView;
            LayoutParams lParams;
            FrameLayout screen = new FrameLayout(this);
            String sTitle = this.mPlacement.getString(this, SettingsJsonConstants.PROMPT_TITLE_KEY);
            String sText = this.mPlacement.getString(this, "text");
            String sOk = this.mPlacement.getString(this, "ok");
            String sCancel = this.mPlacement.getString(this, "cancel");
            LinearLayout dialog = new LinearLayout(this);
            dialog.setOrientation(1);
            dialog.setBackgroundColor(Color.rgb(242, 242, 242));
            screen.addView(dialog, new FrameLayout.LayoutParams(Utils.getDIP(280.0d), -2));
            if (sTitle.length() > 0) {
                textView = new TextView(this);
                textView.setText(sTitle);
                textView.setGravity(16);
                textView.setPadding(Utils.getDIP(16.0d), 0, Utils.getDIP(16.0d), 0);
                textView.setSingleLine();
                textView.setEllipsize(TruncateAt.END);
                textView.setTextSize(1, 19.0f);
                textView.setTextColor(Color.rgb(24, 24, 24));
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                dialog.addView(textView, new LayoutParams(-1, Utils.getDIP(48.0d)));
                textView = new ImageView(this);
                textView.setBackgroundColor(Color.argb(230, 0, 0, 0));
                dialog.addView(textView, new LayoutParams(-1, Utils.getDIP(1.0d)));
            }
            if (sText.length() > 0) {
                textView = new TextView(this);
                textView.setText(sText);
                textView.setTextColor(Color.rgb(24, 24, 24));
                textView.setTextSize(1, 16.0f);
                textView.setPadding(Utils.getDIP(16.0d), 0, Utils.getDIP(16.0d), 0);
                lParams = new LayoutParams(-1, -2);
                lParams.topMargin = Utils.getDIP(16.0d);
                dialog.addView(textView, lParams);
            }
            LinearLayout buttons = new LinearLayout(this);
            buttons.setOrientation(0);
            lParams = new LayoutParams(-1, -2);
            lParams.leftMargin = Utils.getDIP(16.0d);
            lParams.rightMargin = Utils.getDIP(16.0d);
            lParams.topMargin = Utils.getDIP(16.0d);
            lParams.bottomMargin = Utils.getDIP(16.0d);
            dialog.addView(buttons, lParams);
            Button bCancel = new Button(this);
            if (sCancel.length() > 0) {
                bCancel.setText(sCancel);
                bCancel.setTextSize(1, 18.0f);
                bCancel.setTextColor(Color.rgb(24, 24, 24));
            }
            buttons.addView(bCancel, new LayoutParams(-1, Utils.getDIP(48.0d), 1.0f));
            bCancel.setOnClickListener(new C12791());
            Button bOK = new Button(this);
            if (sOk.length() > 0) {
                bOK.setText(sOk);
                bOK.setTextSize(1, 18.0f);
                bOK.setTextColor(Color.rgb(24, 24, 24));
            }
            buttons.addView(bOK, new LayoutParams(-1, Utils.getDIP(48.0d), 1.0f));
            bOK.setOnClickListener(new C12802());
            setContentView(screen, new ViewGroup.LayoutParams(-2, -2));
        }
    }

    public void onDestroy() {
        if (!(this.bOkClicked || this.mPlacement == null || !this.mPlacement.isResetOnCancel())) {
            this.mPlacement.reset(this, 0);
        }
        super.onDestroy();
    }
}
