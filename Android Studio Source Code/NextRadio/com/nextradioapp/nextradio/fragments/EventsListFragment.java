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
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.adapters.EventListAdapter;
import com.nextradioapp.nextradio.ottos.NREventList;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(2130903083)
public class EventsListFragment extends Fragment {
    public static final int FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS = 1;
    public static final int FRAGMENT_TYPE_EVENTS_LIST_HISTORY = 2;
    private static NextRadioAndroid SDK;
    @ViewById
    View blue_bar_line;
    @ViewById
    LinearLayout emptyLayout;
    public int fragment_type;
    @ViewById
    ListView listViewHistoryEvents;
    @ViewById
    LinearLayout listViewLayout;
    private EventListAdapter mEventsListAdapter;
    @ViewById
    LinearLayout progressLayout;
    @ViewById
    TextView unavailableTextView;

    public EventsListFragment() {
        this.fragment_type = FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS;
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
    }

    static {
        SDK = NextRadioAndroid.getInstance();
    }

    @UiThread
    @Subscribe
    public void NREventsAvailable(NREventList event) {
        if (isAdded()) {
            Log.d("EventsListFragment", "NREventsAvailable");
            if (event.eventList == null) {
                setViews(-1);
                return;
            }
            this.emptyLayout.setVisibility(8);
            this.listViewLayout.setVisibility(0);
            this.progressLayout.setVisibility(8);
            switch (this.fragment_type) {
                case FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS /*1*/:
                    ArrayList<NextRadioEventInfo> favoriteList = SDK.getSavedEvents();
                    this.mEventsListAdapter.setData(favoriteList);
                    this.mEventsListAdapter.notifyDataSetChanged();
                    if (favoriteList.size() < FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS) {
                        setViews(FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS);
                    }
                case FRAGMENT_TYPE_EVENTS_LIST_HISTORY /*2*/:
                    this.mEventsListAdapter.setData(event.eventList);
                    this.mEventsListAdapter.notifyDataSetChanged();
                    if (event.eventList.size() < FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS) {
                        setViews(FRAGMENT_TYPE_EVENTS_LIST_HISTORY);
                    }
                default:
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragment_type = getArguments().getInt("FRAGMENT_TYPE", FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS);
        return null;
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    @AfterViews
    void afterViews() {
        if (NextRadioApplication.isTablet) {
            this.blue_bar_line.setVisibility(8);
        }
        this.mEventsListAdapter = new EventListAdapter(getActivity());
        this.listViewHistoryEvents.setAdapter(this.mEventsListAdapter);
        this.listViewHistoryEvents.setOnItemClickListener(new 1(this));
        this.listViewHistoryEvents.setOnItemLongClickListener(new 2(this));
    }

    private void setViews(int fragment_type) {
        this.emptyLayout.setVisibility(0);
        this.listViewLayout.setVisibility(8);
        this.progressLayout.setVisibility(8);
        if (fragment_type == FRAGMENT_TYPE_EVENTS_LIST_BOOKMARKS) {
            this.unavailableTextView.setText(getString(2131165279));
        } else if (fragment_type == FRAGMENT_TYPE_EVENTS_LIST_HISTORY) {
            this.unavailableTextView.setText(getString(2131165281));
        } else {
            this.unavailableTextView.setText(getString(2131165280));
        }
    }
}
