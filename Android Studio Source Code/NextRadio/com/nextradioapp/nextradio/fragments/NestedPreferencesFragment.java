package com.nextradioapp.nextradio.fragments;

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
import android.view.ViewGroup;
import android.widget.ListView;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;

public class NestedPreferencesFragment extends PreferenceFragment {
    public static final int NESTED_SCREEN_1_KEY = 1;
    public static final int NESTED_SCREEN_2_KEY = 2;
    private static final String TAG_KEY = "NESTED_KEY";
    private View view;

    public static NestedPreferencesFragment newInstance(int key) {
        NestedPreferencesFragment fragment = new NestedPreferencesFragment();
        Bundle args = new Bundle();
        args.putInt(TAG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setIcon(2130837670);
        }
        checkPreferenceResource();
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

    private void checkPreferenceResource() {
        switch (getArguments().getInt(TAG_KEY)) {
            case NESTED_SCREEN_1_KEY /*1*/:
                addPreferencesFromResource(2131034114);
                intiInterfaceData();
            case NESTED_SCREEN_2_KEY /*2*/:
                addPreferencesFromResource(2131034113);
                initDevData();
            default:
        }
    }

    private void intiInterfaceData() {
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceStorage.NO_DATA_MODE, false)) {
            Preference p = new Preference(getActivity());
            p.setTitle(getResources().getString(2131165377));
            p.setSummary(getResources().getString(2131165376));
            p.setOnPreferenceClickListener(new 1(this));
            ((PreferenceScreen) findPreference("screenInterface")).addPreference(p);
            return;
        }
        p = new Preference(getActivity());
        p.setTitle(getResources().getString(2131165363));
        p.setSummary(getResources().getString(2131165362));
        p.setOnPreferenceClickListener(new 2(this));
        ((PreferenceScreen) findPreference("screenInterface")).addPreference(p);
    }

    private void initDevData() {
        EditTextPreference APIOverride = (EditTextPreference) findPreference(PreferenceStorage.OVERRIDE_URL);
        String urlOverride = NextRadioSDKWrapperProvider.getInstance().getUrlOverride();
        if (urlOverride != null) {
            APIOverride.setDefaultValue(urlOverride);
        }
        APIOverride.setOnPreferenceChangeListener(new 3(this));
        EditTextPreference adGroupPreference = (EditTextPreference) findPreference(PreferenceStorage.AD_GROUP);
        DeviceRegistrationInfo deviceInfo = NextRadioSDKWrapperProvider.getInstance().getDeviceRegistrationInfo();
        if (!(deviceInfo == null || deviceInfo.adGroup == null || deviceInfo.adGroup.length() <= 0)) {
            adGroupPreference.setDefaultValue(urlOverride);
        }
        adGroupPreference.setOnPreferenceChangeListener(new 4(this));
        EditTextPreference cachingIDPreference = (EditTextPreference) findPreference(PreferenceStorage.CACHING_ID);
        if (!(deviceInfo == null || deviceInfo.cachingGroup == null || deviceInfo.cachingGroup.length() <= 0)) {
            cachingIDPreference.setDefaultValue(urlOverride);
        }
        cachingIDPreference.setOnPreferenceChangeListener(new 5(this));
        ((CheckBoxPreference) findPreference("DemoStationsAllowed")).setOnPreferenceChangeListener(new 6(this));
        findPreference("btnStartTester").setOnPreferenceClickListener(new 7(this));
        ListPreference countrySelect = (ListPreference) findPreference(PreferenceStorage.SELECTED_COUNTRY);
        String countryCode = NextRadioSDKWrapperProvider.getInstance().getCountryCode();
        if (countryCode == null) {
            countrySelect.setValueIndex(0);
        } else {
            countrySelect.setValue(countryCode);
        }
        countrySelect.setOnPreferenceChangeListener(new 8(this));
        String deviceID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PreferenceStorage.DEVICE_ID, DeviceInfo.ORIENTATION_UNKNOWN);
        findPreference(PreferenceStorage.DEVICE_ID).setSummary(deviceID);
        findPreference(PreferenceStorage.DEVICE_ID).setOnPreferenceClickListener(new 9(this, deviceID));
    }
}
