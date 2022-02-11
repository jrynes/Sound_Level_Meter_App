package com.nextradioapp.nextradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.nextradioapp.nextradio.ottos.NREventList;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class EventsListFragment_ extends EventsListFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public EventsListFragment_() {
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
            this.contentView_ = inflater.inflate(2130903083, container, false);
        }
        return this.contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_(null);
    }

    public void onViewChanged(HasViews hasViews) {
        this.progressLayout = (LinearLayout) hasViews.findViewById(2131689668);
        this.blue_bar_line = hasViews.findViewById(2131689671);
        this.unavailableTextView = (TextView) hasViews.findViewById(2131689673);
        this.listViewHistoryEvents = (ListView) hasViews.findViewById(2131689670);
        this.emptyLayout = (LinearLayout) hasViews.findViewById(2131689672);
        this.listViewLayout = (LinearLayout) hasViews.findViewById(2131689669);
        afterViews();
    }

    @Subscribe
    public void NREventsAvailable(NREventList event) {
        this.handler_.post(new 1(this, event));
    }
}
