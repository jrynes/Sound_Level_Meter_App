package com.nextradioapp.nextradio.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.adapters.StationsGridAdapter;
import com.nextradioapp.nextradio.ottos.NRSecondaryEvent;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(2130903087)
public class StationsGridFragment extends Fragment {
    private static final String TAG = "StationsGridFragment";
    @ViewById
    View blue_bar_line;
    @ViewById
    Button btnUpdateStations;
    @ViewById
    GridView gridViewStations;
    private Context mContext;
    private StationsGridAdapter mGridAdapter;
    private boolean mStoppedSubscriptions;
    @ViewById
    ImageView shadow_imageView2;
    @ViewById
    TextView txtEmptyStations;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
        NextRadioSDKWrapperProvider.getInstance().stopSecondaryEventSubscription();
        this.mStoppedSubscriptions = true;
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
    }

    @UiThread
    public void showEmptyStations() {
        Log.d(TAG, "showEmptyStations");
        if (isAdded()) {
            this.txtEmptyStations.setVisibility(0);
            this.txtEmptyStations.setText(getString(2131165417));
            this.btnUpdateStations.setVisibility(0);
            this.btnUpdateStations.setOnClickListener(new 1(this));
        }
    }

    @UiThread
    @Subscribe
    public void NRStationsAvailable(NRStationList newStationList) {
        if (isAdded()) {
            this.txtEmptyStations.setVisibility(8);
            this.btnUpdateStations.setVisibility(8);
            if (newStationList != null && newStationList.errorCode != 4) {
                if (newStationList.stationList == null || newStationList.stationList.size() == 0) {
                    showEmptyStations();
                    return;
                }
                int i;
                ArrayList<StationInfo> newStations = new ArrayList();
                Iterator i$ = newStationList.stationList.iterator();
                while (i$.hasNext()) {
                    StationInfo station = (StationInfo) i$.next();
                    if (station.getStationType() != 0) {
                        newStations.add(station);
                    }
                }
                Collections.sort(newStations, new 2(this));
                if (!(this.mStoppedSubscriptions || this.mGridAdapter == null || this.mGridAdapter.getStations() == null)) {
                    boolean stationsChanged = false;
                    i = 0;
                    while (i < newStations.size()) {
                        if (i >= this.mGridAdapter.getStations().size()) {
                            stationsChanged = true;
                            break;
                        } else if (!((StationInfo) this.mGridAdapter.getStations().get(i)).areSame((StationInfo) newStations.get(i))) {
                            stationsChanged = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (!stationsChanged) {
                        return;
                    }
                }
                this.mGridAdapter = new StationsGridAdapter(this.mContext, newStations);
                this.gridViewStations.setAdapter(this.mGridAdapter);
                this.gridViewStations.setOnItemClickListener(new 3(this));
                NextRadioSDKWrapperProvider.getInstance().stopSecondaryEventSubscription();
                NextRadioSDKWrapperProvider.getInstance().startSecondarySubscriptions(newStations);
                this.mStoppedSubscriptions = false;
                for (i = 0; i < newStations.size(); i++) {
                    if (((StationInfo) newStations.get(i)).getStationType() == 3) {
                        NextRadioSDKWrapperProvider.getInstance().getCurrentEventSecondary(((StationInfo) newStations.get(i)).publicStationID);
                    }
                }
            }
        }
    }

    @AfterViews
    void afterViews() {
        if (NextRadioApplication.isTablet) {
            this.blue_bar_line.setVisibility(8);
            this.shadow_imageView2.setVisibility(8);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @UiThread
    @Subscribe
    public void NRSecondaryEventAvailable(NRSecondaryEvent event) {
        this.mGridAdapter.updateEvent(event.data);
    }
}
