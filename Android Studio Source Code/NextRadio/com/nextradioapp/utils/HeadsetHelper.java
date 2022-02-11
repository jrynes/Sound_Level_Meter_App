package com.nextradioapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.ToggleButton;
import com.facebook.ads.AdError;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.nextradio.views.FlipView;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class HeadsetHelper {
    private static HeadsetHelper headsetHelper;
    private final String TAG;
    private Handler delayHandler;
    private Runnable delayRunnable;
    public Dialog dialog;
    private FlipView flipView;
    private AnimationDrawable frameAnimation;
    private ImageView frame_anim_imageView;
    private Handler handler;
    private int headPhonesConnectedCount;
    private int headPhonesDisconnectedCount;
    private Runnable headPhonesRunnable;
    private int interval;
    private boolean isHeadsPhonePluggedIn;
    private AlertDialog mAirplaneModeAlertDialog;
    private Activity mContext;
    private SharedPreferences preferences;
    private RelativeLayout progress_layout;

    public HeadsetHelper() {
        this.TAG = "HeadsetHelper";
        this.handler = new Handler();
        this.delayHandler = new Handler();
        this.interval = AdError.SERVER_ERROR_CODE;
    }

    public static HeadsetHelper getInstance() {
        if (headsetHelper == null) {
            headsetHelper = new HeadsetHelper();
        }
        return headsetHelper;
    }

    public void flipViews() {
        if (this.flipView == null || this.flipView.isFlipped() || this.preferences.getBoolean("isViewFlipped", false)) {
            dismissDialog();
            return;
        }
        this.preferences.edit().putBoolean("isViewFlipped", true).apply();
        this.flipView.showNext();
    }

    public void dismissDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    public boolean isDialogShowing() {
        if (this.dialog != null) {
            return this.dialog.isShowing();
        }
        return false;
    }

    public void setDialog(Dialog mDialog) {
        this.dialog = mDialog;
    }

    public void showWarning(Activity context) {
        this.mContext = context;
        MixPanelHelper.getInstance(context).recordMIPEvent("View", "Headphones required", "Head phones");
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.preferences = context.getSharedPreferences(RadioAdapterService.PREFS, 4);
        try {
            View view = context.getLayoutInflater().inflate(2130903070, null);
            this.dialog = new Dialog(context, 2131427349);
            this.dialog.setContentView(view);
            this.dialog.setCancelable(true);
            this.dialog.getWindow().setLayout(-1, -2);
            if (!this.dialog.isShowing()) {
                this.dialog.show();
            }
            this.flipView = (FlipView) view.findViewById(2131689615);
            this.flipView.setOnClickListener(null);
            Button got_it_button = (Button) view.findViewById(2131689645);
            this.frame_anim_imageView = (ImageView) view.findViewById(2131689637);
            this.progress_layout = (RelativeLayout) view.findViewById(2131689638);
            Switch speaker_switchButton = (Switch) view.findViewById(2131689642);
            ToggleButton speaker_toggleButton = (ToggleButton) view.findViewById(2131689643);
            this.frame_anim_imageView.setBackgroundResource(2130837658);
            this.frameAnimation = (AnimationDrawable) this.frame_anim_imageView.getBackground();
            if (VERSION.SDK_INT > 20) {
                speaker_switchButton.setVisibility(0);
                speaker_toggleButton.setVisibility(8);
                switchControl(speaker_switchButton, audioManager);
            } else {
                speaker_switchButton.setVisibility(8);
                speaker_toggleButton.setVisibility(0);
                toggleControl(speaker_toggleButton, audioManager);
            }
            got_it_button.setOnClickListener(new 1(this));
        } catch (Exception ex) {
            this.dialog = null;
            ex.printStackTrace();
        }
    }

    private void switchControl(Switch speaker_toggleButton, AudioManager audioManager) {
        if (this.preferences.getBoolean("outputToSpeaker", false)) {
            speaker_toggleButton.setChecked(true);
        } else {
            speaker_toggleButton.setChecked(false);
        }
        speaker_toggleButton.setOnCheckedChangeListener(new 2(this, audioManager));
    }

    private void toggleControl(ToggleButton speaker_toggleButton, AudioManager audioManager) {
        if (this.preferences.getBoolean("outputToSpeaker", false)) {
            speaker_toggleButton.setChecked(false);
        } else {
            speaker_toggleButton.setChecked(true);
        }
        speaker_toggleButton.setOnCheckedChangeListener(new 3(this, audioManager));
    }

    private void startAnimation(boolean stop) {
        if (!stop && this.frameAnimation != null) {
            this.progress_layout.setVisibility(0);
            this.frame_anim_imageView.setVisibility(8);
            this.frameAnimation.stop();
        } else if (this.frameAnimation != null) {
            this.progress_layout.setVisibility(8);
            this.frame_anim_imageView.setVisibility(0);
            this.frameAnimation.start();
        }
    }

    private void setSpeakerOut(boolean isChecked, SharedPreferences sp, AudioManager audioManager) {
        if (isChecked) {
            sp.edit().putBoolean("outputToSpeaker", true).apply();
            audioManager.setSpeakerphoneOn(true);
            audioManager.setWiredHeadsetOn(false);
            NextRadioApplication.postToBus(this, new 4(this, sp));
            return;
        }
        sp.edit().putBoolean("outputToSpeaker", false).apply();
        audioManager.setSpeakerphoneOn(false);
        NextRadioApplication.postToBus(this, new 5(this, sp));
    }

    public static boolean isHeadphonesPluggedIn(Context context) {
        return ((AudioManager) context.getSystemService("audio")).isWiredHeadsetOn();
    }

    public static boolean isAirplaneModeOn(Context context) {
        return System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public void showAirplaneModeWarning(Context context) {
        MixPanelHelper.getInstance(context).recordMIPEvent("View", "Airplane mode", "Airplane mode");
        try {
            if (this.mAirplaneModeAlertDialog == null) {
                Builder builder = new Builder(context);
                builder.setTitle(context.getString(2131165271));
                builder.setMessage(context.getString(2131165270));
                builder.setPositiveButton(context.getString(2131165338), null);
                this.mAirplaneModeAlertDialog = builder.create();
            }
            if (!this.mAirplaneModeAlertDialog.isShowing()) {
                this.mAirplaneModeAlertDialog.show();
            }
        } catch (Exception ex) {
            this.mAirplaneModeAlertDialog = null;
            ex.printStackTrace();
        }
    }

    public void displayHeadPhoneWarning(int state, Activity activity) {
        this.mContext = activity;
        switch (state) {
            case Tokenizer.EOF /*0*/:
                this.isHeadsPhonePluggedIn = false;
                if (!isDialogShowing()) {
                    showWarning(activity);
                    startAnimation(true);
                    return;
                }
                return;
            case Zone.PRIMARY /*1*/:
                delayEveryInput(0, activity);
                return;
            case Zone.SECONDARY /*2*/:
                delayEveryInput(1, activity);
                break;
            case Protocol.GGP /*3*/:
                break;
            default:
                Log.d("HeadsetHelper", "I have no idea what the headset state is");
                return;
        }
        if (isAirplaneModeOn(activity)) {
            showAirplaneModeWarning(activity);
        }
    }

    private void delayEveryInput(int state, Activity activity) {
        switch (state) {
            case Tokenizer.EOF /*0*/:
                if (this.delayRunnable != null) {
                    this.delayHandler.removeCallbacks(this.delayRunnable);
                }
                this.delayRunnable = new 6(this, activity);
                this.delayHandler.postDelayed(this.delayRunnable, 500);
            case Zone.PRIMARY /*1*/:
                if (this.delayRunnable != null) {
                    this.delayHandler.removeCallbacks(this.delayRunnable);
                }
                this.delayRunnable = new 7(this, activity);
                this.delayHandler.postDelayed(this.delayRunnable, 500);
            default:
        }
    }

    private void getStableState() {
        this.handler.postDelayed(this.headPhonesRunnable, (long) this.interval);
    }

    private void startRunnable(Activity activity) {
        startAnimation(false);
        if (this.headPhonesRunnable != null) {
            this.handler.removeCallbacks(this.headPhonesRunnable);
        }
        this.headPhonesRunnable = new 8(this, activity);
    }

    private void startFmRadio() {
        if (RadioAdapterService.mRadioAdapter != null && RadioAdapterService.mRadioAdapter.getIsPoweredOn()) {
            return;
        }
        if (NextRadioApplication.getInstance().getStationInfo() != null) {
            NextRadioApplication.postToBus(this, new 9(this, NextRadioApplication.getInstance().getStationInfo()));
            NextRadioApplication.getInstance().setStationInfo(null);
            return;
        }
        NextRadioApplication.postToBus(this, new 10(this));
    }

    private void stopRadio() {
        NextRadioApplication.postToBus(this, new 11(this));
    }
}
