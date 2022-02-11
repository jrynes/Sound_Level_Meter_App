package com.nextradioapp.nextradio.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NavigationDrawerFragment_ extends NavigationDrawerFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public NavigationDrawerFragment_() {
        this.onViewChangedNotifier_ = new OnViewChangedNotifier();
        this.handler_ = new Handler(Looper.getMainLooper());
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (this.contentView_ == null) {
            return null;
        }
        return this.contentView_.findViewById(id);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (this.contentView_ == null) {
            this.contentView_ = inflater.inflate(2130903095, container, false);
        }
        return this.contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        Resources resources_ = getActivity().getResources();
        this.navigationdrawerfrag_view_recent_activity = resources_.getString(2131165318);
        this.navigationdrawerfrag_tuner_only_mode = resources_.getString(2131165316);
        this.navigationdrawerfrag_more = resources_.getString(2131165310);
        this.navigationdrawerfrag_recent_stations = resources_.getString(2131165314);
        this.navigationdrawerfrag_MORE = resources_.getString(2131165309);
        this.navigationdrawerfrag_radio_guide = resources_.getString(2131165313);
        this.nav_recentActivity = resources_.getStringArray(2131230727);
        this.nav_radioGuide = resources_.getStringArray(2131230726);
        this.nav_more = resources_.getStringArray(2131230725);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_(null);
    }

    public void onViewChanged(HasViews hasViews) {
        this.listViewNavigation = (ListView) hasViews.findViewById(2131689734);
        afterViews();
    }

    @Subscribe
    public void userPickedStation(NRRadioAction tune) {
        super.userPickedStation(tune);
    }

    @Subscribe
    public void NRDataMode(NRDataMode nrDataMode) {
        this.handler_.post(new 1(this, nrDataMode));
    }

    @Subscribe
    public void navigationChosen(NRNavigationEvent event) {
        this.handler_.post(new 2(this, event));
    }

    @Subscribe
    public void NRStationsAvailable(NRStationList stations) {
        this.handler_.post(new 3(this, stations));
    }
}
