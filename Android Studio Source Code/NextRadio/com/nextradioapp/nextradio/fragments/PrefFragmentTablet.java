package com.nextradioapp.nextradio.fragments;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.utils.CountryEULAManager;
import org.androidannotations.annotations.EFragment;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment
public class PrefFragmentTablet extends PreferenceFragment {
    private static final String TAG = "PrefFragment";
    private Callback mCallback;
    protected int mVersionTapCount;
    private View view;

    public interface Callback {
        void onNestedPreferenceSelected(int i);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2131034116);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.mVersionTapCount = 0;
        findPreference("lblVersionNumber").setOnPreferenceClickListener(new 1(this));
        findPreference("sendUsLove").setOnPreferenceClickListener(new 2(this));
        findPreference("supportFeedback").setOnPreferenceClickListener(new 3(this));
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PreferenceStorage.NO_DATA_MODE, false)) {
            getPreferenceScreen().removePreference(findPreference("btnShowTutorial"));
        } else {
            findPreference("btnShowTutorial").setOnPreferenceClickListener(new 4(this));
        }
        CountryEULAManager EULAManager = new CountryEULAManager(getActivity());
        findPreference("btnViewEULA").setOnPreferenceClickListener(new 5(this, EULAManager));
        findPreference("btnViewPrivacy").setOnPreferenceClickListener(new 6(this, EULAManager));
        findPreference("btnViewFAQ").setOnPreferenceClickListener(new 7(this, EULAManager));
        String versionName = Stomp.EMPTY;
        try {
            versionName = "v " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        findPreference("screenInterface").setOnPreferenceClickListener(new 8(this));
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("enabledev", false)) {
            findPreference("developer_options").setOnPreferenceClickListener(new 9(this));
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

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            this.mCallback = (Callback) activity;
            return;
        }
        throw new IllegalStateException("Owner must implement URLCallback interface");
    }
}
