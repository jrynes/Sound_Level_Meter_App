package com.nextradioapp.nextradio.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.adapters.StationsListAdapter;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment(2130903088)
public class StationsListFragment extends Fragment {
    public static final int FRAGMENT_TYPE_STATION_LIST_FAVORITES = 3;
    public static final int FRAGMENT_TYPE_STATION_LIST_FREQUENCY = 2;
    public static final int FRAGMENT_TYPE_STATION_LIST_GENRE = 1;
    @ViewById
    View blue_bar_line;
    @ViewById
    Button btnUpdateStations;
    public int fragment_type;
    private String lastGenre;
    @ViewById
    View layoutFavoriteDescription;
    @ViewById
    ListView listviewStations;
    private StationsListAdapter mStationsListAdapter;
    @ViewById
    TextView txtEmptyStations;

    public StationsListFragment() {
        this.fragment_type = FRAGMENT_TYPE_STATION_LIST_GENRE;
        this.lastGenre = Stomp.EMPTY;
    }

    @UiThread
    public void showEmptyFavorites() {
        if (isAdded()) {
            this.txtEmptyStations.setVisibility(0);
            this.txtEmptyStations.setText(Stomp.EMPTY);
            this.layoutFavoriteDescription.setVisibility(0);
        }
    }

    @UiThread
    public void showEmptyStations() {
        if (isAdded()) {
            this.txtEmptyStations.setVisibility(0);
            this.txtEmptyStations.setText(getString(2131165417));
            this.btnUpdateStations.setVisibility(0);
            this.btnUpdateStations.setOnClickListener(new 1(this));
        }
    }

    @UiThread
    @Subscribe
    public void NRStationsAvailable(NRStationList event) {
        Log.d("StationsListFragment", "NRStationsAvailable");
        if (isAdded()) {
            this.btnUpdateStations.setVisibility(8);
            this.txtEmptyStations.setVisibility(8);
            this.layoutFavoriteDescription.setVisibility(8);
            if (event != null && event.errorCode != 4) {
                if (event.stationList == null || event.stationList.size() == 0) {
                    switch (this.fragment_type) {
                        case FRAGMENT_TYPE_STATION_LIST_GENRE /*1*/:
                        case FRAGMENT_TYPE_STATION_LIST_FREQUENCY /*2*/:
                            showEmptyStations();
                            return;
                        case FRAGMENT_TYPE_STATION_LIST_FAVORITES /*3*/:
                            showEmptyFavorites();
                            return;
                        default:
                            return;
                    }
                }
                ArrayList<StationInfo> newStations = new ArrayList();
                Iterator i$;
                StationInfo station;
                switch (this.fragment_type) {
                    case FRAGMENT_TYPE_STATION_LIST_GENRE /*1*/:
                        i$ = event.stationList.iterator();
                        while (i$.hasNext()) {
                            station = (StationInfo) i$.next();
                            if (station.getStationType() != 0) {
                                newStations.add(station);
                            }
                        }
                        Collections.sort(newStations, new 2(this));
                        this.mStationsListAdapter.clear();
                        i$ = newStations.iterator();
                        while (i$.hasNext()) {
                            station = (StationInfo) i$.next();
                            if (station.genre.equals(this.lastGenre)) {
                                this.mStationsListAdapter.addGenreItem(station);
                            } else {
                                this.mStationsListAdapter.addGenreHeader(station.genre);
                                this.mStationsListAdapter.addGenreItem(station);
                                this.lastGenre = station.genre;
                            }
                        }
                        if (this.mStationsListAdapter.getCount() <= 0) {
                            showEmptyStations();
                        }
                        this.mStationsListAdapter.notifyDataSetChanged();
                    case FRAGMENT_TYPE_STATION_LIST_FREQUENCY /*2*/:
                        i$ = event.stationList.iterator();
                        while (i$.hasNext()) {
                            station = (StationInfo) i$.next();
                            if (station.getStationType() != 0) {
                                newStations.add(station);
                            }
                        }
                        Collections.sort(newStations, new 3(this));
                        this.mStationsListAdapter.clear();
                        i$ = newStations.iterator();
                        while (i$.hasNext()) {
                            this.mStationsListAdapter.addFrequencyItem((StationInfo) i$.next());
                        }
                        this.mStationsListAdapter.notifyDataSetChanged();
                        if (this.mStationsListAdapter.getCount() <= 0) {
                            showEmptyStations();
                        }
                    case FRAGMENT_TYPE_STATION_LIST_FAVORITES /*3*/:
                        newStations = event.stationList;
                        Collections.sort(newStations, new 4(this));
                        this.listviewStations.setOnItemLongClickListener(new 5(this));
                        this.mStationsListAdapter.clear();
                        i$ = newStations.iterator();
                        while (i$.hasNext()) {
                            station = (StationInfo) i$.next();
                            if (station.isFavorited) {
                                this.mStationsListAdapter.addGenreItem(station);
                            }
                        }
                        if (this.mStationsListAdapter.getCount() <= 0) {
                            showEmptyFavorites();
                        }
                        this.mStationsListAdapter.notifyDataSetChanged();
                    default:
                }
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragment_type = getArguments().getInt("FRAGMENT_TYPE", FRAGMENT_TYPE_STATION_LIST_GENRE);
        return null;
    }

    @AfterViews
    void afterViews() {
        if (NextRadioApplication.isTablet) {
            this.blue_bar_line.setVisibility(8);
        }
        this.mStationsListAdapter = new StationsListAdapter(getActivity());
        this.listviewStations.setAdapter(this.mStationsListAdapter);
        this.listviewStations.setOnItemClickListener(new 6(this));
    }

    public void onResume() {
        super.onPause();
        NextRadioApplication.registerWithBus(this);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }
}
