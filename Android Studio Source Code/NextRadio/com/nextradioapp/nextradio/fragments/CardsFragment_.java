package com.nextradioapp.nextradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.ViewAnimator;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDrawerEvent;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class CardsFragment_ extends CardsFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public CardsFragment_() {
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
            this.contentView_ = inflater.inflate(2130903081, container, false);
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
        this.scrollView = (ScrollView) hasViews.findViewById(2131689487);
        this.viewAnimatorNowPlaying = (ViewAnimator) hasViews.findViewById(2131689595);
        afterViews();
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        super.onEventChanged(event);
    }

    @Subscribe
    public void onDrawerOpened(NRDrawerEvent drawerEvent) {
        super.onDrawerOpened(drawerEvent);
    }

    public void updateCards(NextRadioEventInfo nextRadioEvent) {
        this.handler_.post(new 1(this, nextRadioEvent));
    }

    public void updateViews(NextRadioEventInfo event) {
        this.handler_.post(new 2(this, event));
    }
}
