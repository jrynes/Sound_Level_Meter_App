package com.nextradioapp.nextradio.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.activities.AcquireLocationActivity;
import com.nextradioapp.nextradio.adapters.NavigationListAdapter;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

@EFragment(2130903095)
public class NavigationDrawerFragment extends Fragment {
    private static final String PREF_SELECTION = "navigation_drawer_selected_pos";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String TAG = "Navigation";
    CheckBox cb;
    @ViewById
    ListView listViewNavigation;
    private int mCurrentSelection;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mFragmentContainerView;
    private boolean mFromSavedInstanceState;
    private NavigationListAdapter mListAdapter;
    private boolean mUserLearnedDrawer;
    @StringArrayRes
    String[] nav_more;
    @StringArrayRes
    String[] nav_radioGuide;
    @StringArrayRes
    String[] nav_recentActivity;
    @StringRes
    String navigationdrawerfrag_MORE;
    @StringRes
    String navigationdrawerfrag_more;
    @StringRes
    String navigationdrawerfrag_radio_guide;
    @StringRes
    String navigationdrawerfrag_recent_stations;
    @StringRes
    String navigationdrawerfrag_tuner_only_mode;
    @StringRes
    String navigationdrawerfrag_view_recent_activity;

    public NavigationDrawerFragment() {
        this.mCurrentSelection = 1;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUserLearnedDrawer = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(PREF_USER_LEARNED_DRAWER, false);
    }

    private void setNormalView() {
        if (isAdded()) {
            this.mListAdapter.removeAll();
            this.mListAdapter.addHeader(this.navigationdrawerfrag_recent_stations);
            this.mListAdapter.addRecentStations();
            this.mListAdapter.addHeader(this.navigationdrawerfrag_radio_guide);
            this.mListAdapter.addItem(getString(2131165409), 0);
            this.mListAdapter.addItem(getString(2131165408), 1);
            this.mListAdapter.addItem(getString(2131165406), 2);
            this.mListAdapter.addItem(getString(2131165405), 3);
            this.mListAdapter.addHeader(this.navigationdrawerfrag_view_recent_activity);
            this.mListAdapter.addItem(getString(2131165404), 7);
            this.mListAdapter.addItem(getString(2131165407), 8);
            this.mListAdapter.addItem(getString(2131165412), 9);
            this.mListAdapter.addHeader(this.navigationdrawerfrag_more);
            this.mListAdapter.addItem_outputToSpeaker(getString(2131165311), 10);
            this.mListAdapter.addItem(getString(2131165317), 6);
            this.mListAdapter.addItem(getString(2131165403), 4);
            this.mListAdapter.addItem(getString(2131165315), 5);
            this.mListAdapter.addItem(getString(2131165312), 11);
            this.mListAdapter.notifyDataSetChanged();
        }
    }

    private void setNoData() {
        this.mListAdapter.removeAll();
        this.mListAdapter.addHeader(this.navigationdrawerfrag_tuner_only_mode);
        this.mListAdapter.addItem(getString(2131165409), 0);
        this.mListAdapter.addItem(getString(2131165403), 4);
        this.mListAdapter.addHeader(this.navigationdrawerfrag_more);
        this.mListAdapter.addItem_outputToSpeaker(getString(2131165311), 10);
        this.mListAdapter.addItem(getString(2131165315), 5);
        this.mListAdapter.addItem(getString(2131165312), 11);
        this.mListAdapter.notifyDataSetChanged();
    }

    @AfterViews
    public void afterViews() {
        this.mListAdapter = new NavigationListAdapter(getActivity());
        setNormalView();
        this.listViewNavigation.setAdapter(this.mListAdapter);
        this.listViewNavigation.setOnItemClickListener(new 1(this));
        this.mCurrentSelection = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(PREF_SELECTION, 1);
        if (AcquireLocationActivity.noStationAvailable) {
            this.mCurrentSelection = 4;
        }
        selectItem(this.mCurrentSelection);
    }

    @UiThread
    @Subscribe
    public void navigationChosen(NRNavigationEvent event) {
        selectItem(event.chosenView);
    }

    @Subscribe
    public void userPickedStation(NRRadioAction tune) {
        if (tune.shouldResumeNowPlaying) {
            this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
        }
    }

    @UiThread
    @Subscribe
    public void NRDataMode(NRDataMode nrDataMode) {
        if (isAdded() && this.mListAdapter != null) {
            if (nrDataMode.mIsDataModeOff) {
                setNoData();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (sp == null) {
                    this.mCurrentSelection = 1;
                } else {
                    this.mCurrentSelection = sp.getInt(PREF_SELECTION, 1);
                }
                selectItem(this.mCurrentSelection);
                return;
            }
            setNormalView();
        }
    }

    @UiThread
    @Subscribe
    public void NRStationsAvailable(NRStationList stations) {
        if (stations != null && stations.stationList != null) {
            this.mListAdapter.setStations(stations.stationList);
            this.mListAdapter.notifyDataSetChanged();
        }
    }

    public void onResume() {
        super.onPause();
        NextRadioApplication.registerWithBus(this);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        this.mFragmentContainerView = getActivity().findViewById(fragmentId);
        this.mDrawerLayout = drawerLayout;
        this.mDrawerLayout.setDrawerShadow(2130837653, (int) GravityCompat.START);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        this.mDrawerToggle = new 2(this, getActivity(), this.mDrawerLayout, 2130837668, 2131165411, 2131165410);
        if (!(this.mUserLearnedDrawer || this.mFromSavedInstanceState)) {
            this.mDrawerLayout.openDrawer(this.mFragmentContainerView);
        }
        this.mDrawerLayout.post(new 3(this));
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
    }

    private boolean isCheckable(int selection) {
        if (NextRadioApplication.isTablet && (selection == 10 || selection == 11 || selection == 6)) {
            return false;
        }
        if (NextRadioApplication.isTablet || (selection != 10 && selection != 11 && selection != 6 && selection != 5)) {
            return true;
        }
        return false;
    }

    public void preSelectItem(int selection) {
        this.mCurrentSelection = selection;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (isCheckable(selection)) {
            sp.edit().putInt(PREF_SELECTION, selection).commit();
            for (int i = 0; i < this.listViewNavigation.getCount(); i++) {
                if (this.mListAdapter.getItemSelectionType(i) == selection) {
                    this.listViewNavigation.setItemChecked(i, true);
                } else {
                    this.listViewNavigation.setItemChecked(i, false);
                }
            }
        }
    }

    private void selectItem(int selection) {
        this.mCurrentSelection = selection;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int i;
        if (isCheckable(selection)) {
            sp.edit().putInt(PREF_SELECTION, selection).commit();
            for (i = 0; i < this.listViewNavigation.getCount(); i++) {
                if (this.mListAdapter.getItemSelectionType(i) == selection) {
                    this.listViewNavigation.setItemChecked(i, true);
                } else {
                    this.listViewNavigation.setItemChecked(i, false);
                }
            }
            return;
        }
        for (i = 0; i < this.listViewNavigation.getCount(); i++) {
            if (this.mListAdapter.getItemSelectionType(i) == selection) {
                this.listViewNavigation.setItemChecked(i, false);
            }
        }
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, this.mCurrentSelection);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        showGlobalContextActionBar();
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(0);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    public int getSelectionPosition(int selection) {
        return this.mListAdapter.getSelectionPosition(selection);
    }
}
