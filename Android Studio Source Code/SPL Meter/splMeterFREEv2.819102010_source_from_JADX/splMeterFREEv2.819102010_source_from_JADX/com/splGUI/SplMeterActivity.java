package com.splGUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

public class SplMeterActivity extends Activity {
    static final int ABOUT_OPTION = 2;
    static final int ERROR_MSG = -1;
    static final int EXIT_OPTION = 3;
    static final int MAXOVER_MSG = 2;
    static final int MY_MSG = 1;
    static int PREFERENCES_GROUP_ID = 0;
    static final int RESET_OPTION = 1;
    static final String VERSION = "2.8";
    private OnClickListener calib_button_handle;
    private OnClickListener calibdown_button_handle;
    private OnClickListener calibup_button_handle;
    private OnClickListener log_button_handle;
    Boolean mCalib;
    Context mContext;
    SplEngine mEngine;
    Boolean mLog;
    Boolean mMax;
    Boolean mMode;
    Button mSplCalibButton;
    ImageButton mSplCalibDownButton;
    ImageButton mSplCalibUpButton;
    TextView mSplDataTV;
    Button mSplLogButton;
    Button mSplMaxButton;
    Button mSplModeButton;
    TextView mSplModeTV;
    Button mSplOnOffButton;
    private OnClickListener max_button_handle;
    public Handler mhandle;
    private OnClickListener mode_button_handle;
    private OnClickListener start_button_handle;

    /* renamed from: com.splGUI.SplMeterActivity.1 */
    class C00011 implements OnClickListener {
        C00011() {
        }

        public void onClick(View v) {
            AnimationSet set = new AnimationSet(true);
            Animation animation = new TranslateAnimation(SplMeterActivity.RESET_OPTION, 0.0f, SplMeterActivity.RESET_OPTION, 0.01f, SplMeterActivity.RESET_OPTION, 0.0f, SplMeterActivity.RESET_OPTION, 0.09f);
            animation.setDuration(100);
            set.addAnimation(animation);
            SplMeterActivity.this.mSplCalibDownButton.startAnimation(animation);
            SplMeterActivity.this.mEngine.calibDown();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.2 */
    class C00022 implements OnClickListener {
        C00022() {
        }

        public void onClick(View v) {
            AnimationSet set = new AnimationSet(true);
            Animation animation = new TranslateAnimation(SplMeterActivity.RESET_OPTION, 0.0f, SplMeterActivity.RESET_OPTION, 0.01f, SplMeterActivity.RESET_OPTION, 0.0f, SplMeterActivity.RESET_OPTION, -0.09f);
            animation.setDuration(100);
            set.addAnimation(animation);
            SplMeterActivity.this.mSplCalibUpButton.startAnimation(animation);
            SplMeterActivity.this.mEngine.calibUp();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.3 */
    class C00033 implements OnClickListener {
        C00033() {
        }

        public void onClick(View v) {
            if (SplMeterActivity.this.mLog.booleanValue()) {
                SplMeterActivity.this.mSplLogButton.setTextColor(Color.parseColor("#6D7B8D"));
                SplMeterActivity.this.mLog = Boolean.valueOf(false);
                SplMeterActivity.this.handle_log(false);
            } else {
                SplMeterActivity.this.mSplLogButton.setTextColor(-16777216);
                SplMeterActivity.this.mLog = Boolean.valueOf(true);
                SplMeterActivity.this.handle_log(true);
            }
            SplMeterActivity.this.handle_mode_display();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.4 */
    class C00044 implements OnClickListener {
        C00044() {
        }

        public void onClick(View v) {
            if (SplMeterActivity.this.mCalib.booleanValue()) {
                SplMeterActivity.this.mSplCalibButton.setTextColor(Color.parseColor("#6D7B8D"));
                SplMeterActivity.this.mSplCalibUpButton.setVisibility(4);
                SplMeterActivity.this.mSplCalibDownButton.setVisibility(4);
                SplMeterActivity.this.mSplMaxButton.setVisibility(0);
                SplMeterActivity.this.mSplLogButton.setVisibility(0);
                SplMeterActivity.this.mCalib = Boolean.valueOf(false);
                SplMeterActivity.this.mEngine.storeCalibvalue();
                Toast.makeText(SplMeterActivity.this.mContext, "Calibration Saved.", 0).show();
            } else {
                SplMeterActivity.this.mSplCalibButton.setTextColor(-16777216);
                SplMeterActivity.this.mSplCalibUpButton.setVisibility(0);
                SplMeterActivity.this.mSplCalibDownButton.setVisibility(0);
                SplMeterActivity.this.mSplMaxButton.setVisibility(4);
                SplMeterActivity.this.mSplLogButton.setVisibility(4);
                SplMeterActivity.this.mCalib = Boolean.valueOf(true);
            }
            SplMeterActivity.this.handle_mode_display();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.5 */
    class C00055 implements OnClickListener {
        C00055() {
        }

        public void onClick(View v) {
            Object obj = "SLOW";
            CharSequence charSequence = "FAST";
            String str;
            if (SplMeterActivity.this.mMode.booleanValue()) {
                str = "SLOW";
                SplMeterActivity.this.mSplModeButton.setText(obj);
                SplMeterActivity.this.mMode = Boolean.valueOf(false);
                str = "FAST";
                SplMeterActivity.this.setMeterMode(charSequence);
            } else {
                str = "FAST";
                SplMeterActivity.this.mSplModeButton.setText(charSequence);
                SplMeterActivity.this.mMode = Boolean.valueOf(true);
                str = "SLOW";
                SplMeterActivity.this.setMeterMode(obj);
            }
            SplMeterActivity.this.handle_mode_display();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.6 */
    class C00066 implements OnClickListener {
        C00066() {
        }

        public void onClick(View v) {
            CharSequence charSequence = "ON";
            CharSequence charSequence2 = "";
            String str = "ON";
            if (SplMeterActivity.this.mSplOnOffButton.getText().equals(charSequence)) {
                SplMeterActivity.this.mSplOnOffButton.setText("OFF");
                SplMeterActivity.this.mSplOnOffButton.setTextColor(-16777216);
                SplMeterActivity.this.mSplModeTV.setText("FAST");
                SplMeterActivity.this.mSplModeTV.setBackgroundResource(C0000R.drawable.lcd4);
                str = "";
                SplMeterActivity.this.mSplDataTV.setText(charSequence2);
                SplMeterActivity.this.mSplDataTV.setTextColor(SplMeterActivity.ERROR_MSG);
                SplMeterActivity.this.mSplDataTV.setBackgroundResource(C0000R.drawable.lcd2);
                SplMeterActivity.this.mSplCalibButton.setVisibility(0);
                SplMeterActivity.this.mSplLogButton.setVisibility(0);
                SplMeterActivity.this.mSplMaxButton.setVisibility(0);
                SplMeterActivity.this.mSplModeButton.setVisibility(0);
                SplMeterActivity.this.mSplModeButton.setText("SLOW");
                SplMeterActivity.this.start_meter();
                return;
            }
            SplMeterActivity.this.stop_meter();
            str = "ON";
            SplMeterActivity.this.mSplOnOffButton.setText(charSequence);
            SplMeterActivity.this.mSplOnOffButton.setTextColor(Color.parseColor("#6D7B8D"));
            str = "";
            SplMeterActivity.this.mSplModeTV.setText(charSequence2);
            SplMeterActivity.this.mSplModeTV.setBackgroundColor(-7829368);
            str = "";
            SplMeterActivity.this.mSplDataTV.setText(charSequence2);
            SplMeterActivity.this.mSplDataTV.setTextColor(-7829368);
            SplMeterActivity.this.mSplDataTV.setBackgroundColor(-7829368);
            SplMeterActivity.this.mSplCalibButton.setVisibility(4);
            SplMeterActivity.this.mSplLogButton.setVisibility(4);
            SplMeterActivity.this.mSplMaxButton.setVisibility(4);
            SplMeterActivity.this.mSplModeButton.setVisibility(4);
            SplMeterActivity.this.mSplCalibUpButton.setVisibility(4);
            SplMeterActivity.this.mSplCalibDownButton.setVisibility(4);
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.7 */
    class C00077 implements OnClickListener {
        C00077() {
        }

        public void onClick(View v) {
            SplMeterActivity.this.mSplMaxButton.setTextColor(-16777216);
            SplMeterActivity.this.display_max();
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.8 */
    class C00088 extends Handler {
        C00088() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SplMeterActivity.ERROR_MSG /*-1*/:
                    Toast.makeText(SplMeterActivity.this.mContext, "Error " + msg.obj, SplMeterActivity.RESET_OPTION).show();
                    SplMeterActivity.this.stop_meter();
                case SplMeterActivity.RESET_OPTION /*1*/:
                    SplMeterActivity.this.mSplDataTV.setText(" " + msg.obj);
                case SplMeterActivity.MAXOVER_MSG /*2*/:
                    SplMeterActivity.this.mMax = Boolean.valueOf(false);
                    SplMeterActivity.this.handle_mode_display();
                    SplMeterActivity.this.mSplMaxButton.setTextColor(Color.parseColor("#6D7B8D"));
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /* renamed from: com.splGUI.SplMeterActivity.9 */
    class C00099 implements DialogInterface.OnClickListener {
        C00099() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SplMeterActivity.this.mEngine.stop_engine();
            SplMeterActivity.this.mEngine.reset();
            SplMeterActivity.this.mEngine = new SplEngine(SplMeterActivity.this.mhandle, SplMeterActivity.this.mContext);
            SplMeterActivity.this.mEngine.start_engine();
            Toast.makeText(SplMeterActivity.this.mContext, "Calibration reset", 0).show();
        }
    }

    public SplMeterActivity() {
        this.mSplModeTV = null;
        this.mSplDataTV = null;
        this.mSplMaxButton = null;
        this.mSplOnOffButton = null;
        this.mSplModeButton = null;
        this.mSplCalibButton = null;
        this.mSplLogButton = null;
        this.mSplCalibUpButton = null;
        this.mSplCalibDownButton = null;
        this.mMode = Boolean.valueOf(false);
        this.mCalib = Boolean.valueOf(false);
        this.mLog = Boolean.valueOf(false);
        this.mMax = Boolean.valueOf(false);
        this.mEngine = null;
        this.mContext = this;
        this.calibdown_button_handle = new C00011();
        this.calibup_button_handle = new C00022();
        this.log_button_handle = new C00033();
        this.calib_button_handle = new C00044();
        this.mode_button_handle = new C00055();
        this.start_button_handle = new C00066();
        this.max_button_handle = new C00077();
        this.mhandle = new C00088();
    }

    static {
        PREFERENCES_GROUP_ID = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        CharSequence charSequence = "";
        super.onCreate(savedInstanceState);
        setContentView(C0000R.layout.main);
        getWindow().addFlags(128);
        setTitle("SPL Meter FREE v2.8");
        this.mSplModeTV = (TextView) findViewById(C0000R.id.splModeTV);
        this.mSplModeTV.setBackgroundColor(-7829368);
        String str = "";
        this.mSplModeTV.setText(charSequence);
        this.mSplDataTV = (TextView) findViewById(C0000R.id.splTV);
        this.mSplDataTV.setBackgroundColor(-7829368);
        str = "";
        this.mSplDataTV.setText(charSequence);
        this.mSplDataTV.setGravity(5);
        this.mSplOnOffButton = (Button) findViewById(C0000R.id.splOnOffB);
        this.mSplOnOffButton.setOnClickListener(this.start_button_handle);
        this.mSplMaxButton = (Button) findViewById(C0000R.id.splMaxB);
        this.mSplMaxButton.setOnClickListener(this.max_button_handle);
        this.mSplModeButton = (Button) findViewById(C0000R.id.splModeB);
        this.mSplModeButton.setOnClickListener(this.mode_button_handle);
        this.mSplCalibButton = (Button) findViewById(C0000R.id.splCalibB);
        this.mSplCalibButton.setOnClickListener(this.calib_button_handle);
        this.mSplLogButton = (Button) findViewById(C0000R.id.splLogB);
        this.mSplLogButton.setOnClickListener(this.log_button_handle);
        this.mSplCalibUpButton = (ImageButton) findViewById(C0000R.id.splCalibUpB);
        this.mSplCalibUpButton.setOnClickListener(this.calibup_button_handle);
        this.mSplCalibDownButton = (ImageButton) findViewById(C0000R.id.splCalibDownB);
        this.mSplCalibDownButton.setOnClickListener(this.calibdown_button_handle);
        this.mSplOnOffButton.setText("OFF");
        this.mSplOnOffButton.setTextColor(-16777216);
        this.mSplModeTV.setText("FAST");
        this.mSplModeTV.setBackgroundResource(C0000R.drawable.lcd4);
        str = "";
        this.mSplDataTV.setText(charSequence);
        this.mSplDataTV.setBackgroundResource(C0000R.drawable.lcd2);
        this.mSplCalibButton.setVisibility(0);
        this.mSplLogButton.setVisibility(0);
        this.mSplMaxButton.setVisibility(0);
        this.mSplModeButton.setVisibility(0);
        start_meter();
    }

    public void onResume() {
        getWindow().addFlags(128);
        super.onResume();
    }

    protected void onPause() {
        getWindow().clearFlags(128);
        stop_meter();
        finish();
        super.onDestroy();
    }

    public void onStop() {
        getWindow().clearFlags(128);
        stop_meter();
        finish();
        super.onDestroy();
    }

    public void handle_log(boolean set) {
        String str = "_";
        if (set) {
            this.mEngine.startLogging();
            return;
        }
        this.mEngine.stopLogging();
        Date today = new Date();
        String str2 = "_";
        str2 = "_";
        Toast.makeText(this.mContext, "Log saved to /sdcard/splmeter_" + today.getDate() + str + today.getMonth() + str + (today.getYear() + 1900) + ".xls", 0).show();
    }

    public void display_max() {
        this.mMax = Boolean.valueOf(true);
        handle_mode_display();
        this.mEngine.showMaxValue();
    }

    private void handle_mode_display() {
        String modeString = "";
        if (this.mMode.booleanValue()) {
            modeString = "SLOW";
        } else {
            modeString = "FAST";
        }
        if (this.mCalib.booleanValue()) {
            modeString = new StringBuilder(String.valueOf(modeString)).append("    CALIB").toString();
        } else {
            modeString = new StringBuilder(String.valueOf(modeString)).append("              ").toString();
        }
        if (this.mLog.booleanValue()) {
            modeString = new StringBuilder(String.valueOf(modeString)).append("    LOG").toString();
        } else {
            modeString = new StringBuilder(String.valueOf(modeString)).append("          ").toString();
        }
        if (this.mMax.booleanValue()) {
            modeString = "MAX";
        }
        this.mSplModeTV.setText(modeString);
    }

    public void setMeterMode(String mode) {
        this.mEngine.setMode(mode);
    }

    public void start_meter() {
        this.mCalib = Boolean.valueOf(false);
        this.mMax = Boolean.valueOf(false);
        this.mLog = Boolean.valueOf(false);
        this.mMode = Boolean.valueOf(false);
        this.mEngine = new SplEngine(this.mhandle, this.mContext);
        this.mEngine.start_engine();
    }

    public void stop_meter() {
        this.mEngine.stop_engine();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(PREFERENCES_GROUP_ID, RESET_OPTION, 0, "RESET").setIcon(17301580);
        menu.add(PREFERENCES_GROUP_ID, MAXOVER_MSG, 0, "HELP").setIcon(17301568);
        menu.add(PREFERENCES_GROUP_ID, EXIT_OPTION, 0, "EXIT").setIcon(17301560);
        return true;
    }

    public void reset_meter() {
        AlertDialog alertDialog = new Builder(this).create();
        alertDialog.setTitle("Reset SPL Meter");
        alertDialog.setMessage("Do you want to reset the SPL Meter?");
        alertDialog.setButton("OK", new C00099());
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public void show_about() {
        AlertDialog alertDialog = new Builder(this).create();
        alertDialog.setTitle("SPL Meter 2.8");
        alertDialog.setMessage("Hashir N A \nhashir@mobware4u.com \nwww.mobware4u.com\n\nHOW TO CALIBRATE\n\nThe SPL Meter works with the phone's built in microphone, the headset or the bluetooth headset. You can adjust the calibration if you have a professional calibrated spl meter to compare it to.\n\nTHINGS REQUIRED\n1. a professional calibrated spl meter ( reference meter )\n2. a way to generate pink or white noise. You may use any available software for this purpose.\n\nTHE PROCESS\n1. Press the CALIB button on the SPL Meter app.\n2. Set your reference meter to have the same settings as the spl meter. ( For eg. SLOW db SPL on both the meters )\n3. Play pink or white noise through your system to get a reading on the reference meter.\n4. Now adjust the spl meter app using the up and down arrows to match the reading on the reference meter.\n5. Press the CALIB button again to save the settings.\n\nUse Menu-Reset to reset the calibrations if required.\n\nHOW TO SAVE\n\nPress the LOG button to enable logging(saving) of the readings.\nPress LOG button again to stop logging.\nThe log would be saved as a xls file in /sdcard\n\nMAX button-to display the maximum value observed so far.\n\nSLOW button-toggle between fast and slow modes.");
        alertDialog.setIcon(C0000R.drawable.icon);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case RESET_OPTION /*1*/:
                reset_meter();
                break;
            case MAXOVER_MSG /*2*/:
                show_about();
                break;
            case EXIT_OPTION /*3*/:
                stop_meter();
                super.onDestroy();
                finish();
                getWindow().clearFlags(128);
                break;
        }
        return true;
    }
}
