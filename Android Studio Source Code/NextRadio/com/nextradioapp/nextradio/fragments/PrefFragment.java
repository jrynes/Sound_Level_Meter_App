package com.nextradioapp.nextradio.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.utils.CountryEULAManager;
import org.androidannotations.annotations.EFragment;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment
public class PrefFragment extends PreferenceFragment {
    public static final int RESTART_SERVICE = 4;
    public static final int START_EMULATED_MODE = 2;
    private static final String TAG = "PrefFragment";
    private EditTextPreference APIOverride;
    private EditTextPreference adGroupPreference;
    private EditTextPreference cachingIDPreference;
    private CheckBoxPreference demoStationsCheckBox;
    protected int mVersionTapCount;
    private CheckBoxPreference notificationCheckBox;
    private String supportUrl;
    private View view;

    public PrefFragment() {
        this.supportUrl = "http://nextradioapp.zendesk.com";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2131034115);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        this.mVersionTapCount = 0;
        findPreference("lblVersionNumber").setOnPreferenceClickListener(new 1(this));
        findPreference("sendUsLove").setOnPreferenceClickListener(new 2(this));
        findPreference("supportFeedback").setOnPreferenceClickListener(new 3(this));
        Preference p;
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceStorage.NO_DATA_MODE, false)) {
            p = new Preference(getActivity());
            p.setTitle(getResources().getString(2131165377));
            p.setSummary(getResources().getString(2131165376));
            p.setOnPreferenceClickListener(new 4(this));
            ((PreferenceScreen) findPreference("screenInterface")).addPreference(p);
        } else {
            p = new Preference(getActivity());
            p.setTitle(getResources().getString(2131165363));
            p.setSummary(getResources().getString(2131165362));
            p.setOnPreferenceClickListener(new 5(this));
            ((PreferenceScreen) findPreference("screenInterface")).addPreference(p);
        }
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceStorage.NO_DATA_MODE, false)) {
            getPreferenceScreen().removePreference(findPreference("btnShowTutorial"));
        } else {
            findPreference("btnShowTutorial").setOnPreferenceClickListener(new 6(this));
        }
        CountryEULAManager EULAManager = new CountryEULAManager(getActivity());
        findPreference("btnViewEULA").setOnPreferenceClickListener(new 7(this, EULAManager));
        findPreference("btnViewPrivacy").setOnPreferenceClickListener(new 8(this, EULAManager));
        findPreference("btnViewFAQ").setOnPreferenceClickListener(new 9(this, EULAManager));
        findPreference("btnStartTester").setOnPreferenceClickListener(new 10(this));
        ListPreference countrySelect = (ListPreference) findPreference(PreferenceStorage.SELECTED_COUNTRY);
        String countryCode = NextRadioSDKWrapperProvider.getInstance().getCountryCode();
        if (countryCode == null) {
            countrySelect.setValueIndex(0);
        } else {
            countrySelect.setValue(countryCode);
        }
        countrySelect.setOnPreferenceChangeListener(new 11(this));
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        String deviceID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PreferenceStorage.DEVICE_ID, DeviceInfo.ORIENTATION_UNKNOWN);
        findPreference(PreferenceStorage.DEVICE_ID).setSummary(deviceID);
        findPreference(PreferenceStorage.DEVICE_ID).setOnPreferenceClickListener(new 12(this, deviceID));
        String versionName = Stomp.EMPTY;
        try {
            versionName = "v " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("enabledev", false)) {
            this.APIOverride = (EditTextPreference) findPreference(PreferenceStorage.OVERRIDE_URL);
            String urlOverride = NextRadioSDKWrapperProvider.getInstance().getUrlOverride();
            if (urlOverride != null) {
                this.APIOverride.setDefaultValue(urlOverride);
            }
            this.APIOverride.setOnPreferenceChangeListener(new 13(this));
            this.adGroupPreference = (EditTextPreference) findPreference(PreferenceStorage.AD_GROUP);
            DeviceRegistrationInfo deviceInfo = NextRadioSDKWrapperProvider.getInstance().getDeviceRegistrationInfo();
            if (!(deviceInfo == null || deviceInfo.adGroup == null || deviceInfo.adGroup.length() <= 0)) {
                this.adGroupPreference.setDefaultValue(urlOverride);
            }
            this.adGroupPreference.setOnPreferenceChangeListener(new 14(this));
            this.cachingIDPreference = (EditTextPreference) findPreference(PreferenceStorage.CACHING_ID);
            if (!(deviceInfo == null || deviceInfo.cachingGroup == null || deviceInfo.cachingGroup.length() <= 0)) {
                this.cachingIDPreference.setDefaultValue(urlOverride);
            }
            this.cachingIDPreference.setOnPreferenceChangeListener(new 15(this));
            this.demoStationsCheckBox = (CheckBoxPreference) findPreference("DemoStationsAllowed");
            this.demoStationsCheckBox.setOnPreferenceChangeListener(new 16(this));
            this.notificationCheckBox = (CheckBoxPreference) findPreference(PreferenceStorage.NOTIFICATION_PREFERENCE);
            this.notificationCheckBox.setOnPreferenceChangeListener(new 17(this));
        } else {
            getPreferenceScreen().removePreference(findPreference("developer_options"));
        }
        Preference lblVersionNumber = findPreference("lblVersionNumber");
        lblVersionNumber.setTitle("Version");
        lblVersionNumber.setSummary(versionName);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
        if (this.view != null) {
            deletePadding();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = super.onCreateView(inflater, container, savedInstanceState);
        if (this.view != null) {
            deletePadding();
        }
        return this.view;
    }

    private void deletePadding() {
        ((ListView) this.view.findViewById(16908298)).setPadding(10, 10, 10, 10);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        super.onPreferenceTreeClick(preferenceScreen, preference);
        if (preference instanceof PreferenceScreen) {
            initializeActionBar((PreferenceScreen) preference);
        }
        return false;
    }

    public void initializeActionBar(PreferenceScreen preferenceScreen) {
        Dialog dialog = preferenceScreen.getDialog();
        if (dialog != null) {
            dialog.getActionBar().setDisplayHomeAsUpEnabled(true);
            View homeBtn = dialog.findViewById(16908332);
            if (homeBtn != null) {
                OnClickListener dismissDialogClickListener = new 18(this, dialog);
                ViewParent homeBtnContainer = homeBtn.getParent();
                if (homeBtnContainer instanceof FrameLayout) {
                    ViewGroup containerParent = (ViewGroup) homeBtnContainer.getParent();
                    if (containerParent instanceof LinearLayout) {
                        ((LinearLayout) containerParent).setOnClickListener(dismissDialogClickListener);
                        return;
                    } else {
                        ((FrameLayout) homeBtnContainer).setOnClickListener(dismissDialogClickListener);
                        return;
                    }
                }
                homeBtn.setOnClickListener(dismissDialogClickListener);
            }
        }
    }
}
