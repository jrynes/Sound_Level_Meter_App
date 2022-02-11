package com.nextradioapp.nextradio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.adapters.EventListAdapter;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRRecentlyPlayed;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(2130903083)
public class RecentlyPlayedListFragment extends Fragment {
    private static final String TAG = "RecetPlListFragment";
    @ViewById
    View blue_bar_line;
    @ViewById
    LinearLayout emptyLayout;
    @ViewById
    ListView listViewHistoryEvents;
    @ViewById
    LinearLayout listViewLayout;
    private EventListAdapter mEventsListAdapter;
    @ViewById
    LinearLayout progressLayout;
    @ViewById
    TextView unavailableTextView;

    @UiThread
    @Subscribe
    public void NREventsAvailable(NRRecentlyPlayed event) {
        if (isAdded()) {
            Log.d(TAG, "NREventsAvailable - status: " + event.status);
            if (event.status == 0) {
                showEmptyView(true);
            } else if (event.status == 1) {
                showProgressBar();
            } else if (event.status == 2) {
                showEmptyView(false);
            } else {
                showData();
                this.mEventsListAdapter.setData(event.recentlyPlayed);
                this.mEventsListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void CurrentEventAvailable(NRCurrentEvent event) {
        Log.d(TAG, "CurrentEventAvailable");
        if (event != null && event.currentEvent != null && event.currentEvent.stationInfo != null) {
            NextRadioSDKWrapperProvider.getInstance().requestRecentlyPlayed(event.currentEvent.stationInfo.frequencyHz, event.currentEvent.stationInfo.frequencySubChannel);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @AfterViews
    void afterViews() {
        if (NextRadioApplication.isTablet) {
            this.blue_bar_line.setVisibility(8);
        }
        this.mEventsListAdapter = new EventListAdapter(getActivity());
        this.listViewHistoryEvents.setAdapter(this.mEventsListAdapter);
        this.listViewHistoryEvents.setOnItemClickListener(new 1(this));
    }

    public void onResume() {
        super.onPause();
        NextRadioApplication.registerWithBus(this);
        this.progressLayout.setVisibility(0);
        this.listViewLayout.setVisibility(8);
        this.emptyLayout.setVisibility(8);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    private void showProgressBar() {
        this.progressLayout.setVisibility(0);
        this.emptyLayout.setVisibility(8);
        this.listViewLayout.setVisibility(8);
    }

    private void showEmptyView(boolean isUninitialized) {
        if (isAdded()) {
            this.progressLayout.setVisibility(8);
            this.emptyLayout.setVisibility(0);
            this.listViewLayout.setVisibility(8);
            if (isUninitialized) {
                this.unavailableTextView.setText(getString(2131165394));
            } else {
                this.unavailableTextView.setText(getString(2131165282));
            }
        }
    }

    private void showData() {
        if (isAdded()) {
            this.progressLayout.setVisibility(8);
            this.emptyLayout.setVisibility(8);
            this.listViewLayout.setVisibility(0);
        }
    }
}
