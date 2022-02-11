package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.Tracker;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.utils.DeviceNotification;
import java.util.Locale;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(2130903108)
public class RadioNotSupportedActivity extends Activity {
    private static final String TAG = "RadioNotSupported";
    String defaultSupportCountry;
    int mClicks;
    private Tracker mGATracker;
    private SharedPreferences mSharedPreferences;
    String nextRadioSupportLink;
    @ViewById
    LinearLayout otherCountryMsgLayout;
    @ViewById
    Button signUpForUpdates;
    @ViewById
    TextView txtNoRadioDescription;
    @ViewById
    Button txtNoRadioLink;

    /* renamed from: com.nextradioapp.nextradio.activities.RadioNotSupportedActivity.1 */
    class C11781 implements Runnable {
        C11781() {
        }

        public void run() {
            try {
                NextRadioAndroid.getInstance().register((NextRadioApplication) RadioNotSupportedActivity.this.getApplicationContext(), "none");
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        }
    }

    public RadioNotSupportedActivity() {
        this.nextRadioSupportLink = "http://nextradioapp.com/latam/whynotme";
        this.defaultSupportCountry = "US";
        this.mClicks = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "unsupported phone", "FM not available");
    }

    @AfterViews
    public void afterViews() {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        trackScreenToGoogleAnalytics("RadioNotSupportedActivity");
        new Thread(new C11781()).start();
        String countryCode = ((TelephonyManager) getSystemService("phone")).getSimCountryIso();
        if (countryCode == null || countryCode.length() == 0) {
            countryCode = Locale.getDefault().getCountry();
        }
        String language = Locale.getDefault().getLanguage();
        boolean isDeviceUS = countryCode.equalsIgnoreCase(this.defaultSupportCountry);
        Log.d(TAG, "countryCode:" + countryCode);
        Log.d(TAG, "getLanguage:" + language);
        if ((isDeviceUS && language.equalsIgnoreCase("en")) || (isDeviceUS && language.equalsIgnoreCase("es"))) {
            displayUsCountryMsg();
        } else {
            displayOtherCountryMsg();
        }
    }

    private void displayOtherCountryMsg() {
        findViewById(2131689774).setVisibility(8);
        findViewById(2131689769).setVisibility(8);
        this.txtNoRadioLink.setVisibility(8);
        this.signUpForUpdates.setVisibility(0);
        this.otherCountryMsgLayout.setVisibility(0);
        this.txtNoRadioDescription.setText(getResources().getString(2131165385));
    }

    private void displayUsCountryMsg() {
        if (NextRadioApplication.isCurrentOSAndroidM() && new DeviceNotification(this).getBrandName().contains("motorola")) {
            findViewById(2131689774).setVisibility(0);
            findViewById(2131689766).setVisibility(8);
        }
    }

    @Click({2131689773})
    void btnOtherCountrySignUp_Click() {
        browseIntent(this.nextRadioSupportLink);
    }

    @Click({2131689772})
    void btnRadioLink_Click() {
        browseIntent("http://nextradioapp.com/whynotme");
    }

    @Click({2131689653})
    void btnRadioSkip_Click() {
        this.mClicks++;
        if (this.mSharedPreferences.getBoolean("skip_radio_check", false)) {
            finish();
            if (NextRadioApplication.isTablet) {
                startActivity(new Intent(this, TabletFragContainerActivity_.class));
            } else {
                startActivity(new Intent(this, StationsActivity_.class));
            }
        } else if (this.mClicks >= 40) {
            this.mSharedPreferences.edit().putBoolean("skip_radio_check", true).commit();
        }
    }

    private void browseIntent(String link) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(link));
            intent.setFlags(268435456);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Exception Caught", e.toString());
        }
    }

    private void trackScreenToGoogleAnalytics(String screenName) {
        ((NextRadioApplication) getApplication()).trackScreen(screenName);
    }
}
