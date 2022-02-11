package com.nextradioapp.nextradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ManualTuner_ extends ManualTuner implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public ManualTuner_() {
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
            this.contentView_ = inflater.inflate(2130903085, container, false);
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
        this.blue_bar_line = hasViews.findViewById(2131689671);
        this.seekDownImageButton = (ImageButton) hasViews.findViewById(2131689711);
        this.tuneDownImageButton = (ImageButton) hasViews.findViewById(2131689712);
        this.hyphenTextView = (TextView) hasViews.findViewById(2131689708);
        this.manualTuneCallLettersLayout = (LinearLayout) hasViews.findViewById(2131689706);
        this.manualTunerFrequencyTextView = (TextView) hasViews.findViewById(2131689710);
        this.buttonEnhancedMode = (Button) hasViews.findViewById(2131689518);
        this.seekUpImageButton = (ImageButton) hasViews.findViewById(2131689714);
        this.manualTunerSloganTextView = (TextView) hasViews.findViewById(2131689709);
        this.tuneUpImageButton = (ImageButton) hasViews.findViewById(2131689713);
        this.manualTunerCallLettersTextView = (TextView) hasViews.findViewById(2131689707);
        View view = hasViews.findViewById(2131689712);
        if (view != null) {
            view.setOnClickListener(new 1(this));
        }
        view = hasViews.findViewById(2131689518);
        if (view != null) {
            view.setOnClickListener(new 2(this));
        }
        view = hasViews.findViewById(2131689711);
        if (view != null) {
            view.setOnClickListener(new 3(this));
        }
        view = hasViews.findViewById(2131689713);
        if (view != null) {
            view.setOnClickListener(new 4(this));
        }
        view = hasViews.findViewById(2131689714);
        if (view != null) {
            view.setOnClickListener(new 5(this));
        }
        afterViews();
    }

    @Subscribe
    public void onEventReceived(NRCurrentEvent event) {
        this.handler_.post(new 6(this, event));
    }

    @Subscribe
    public void onDataModeChanged(NRDataMode nrDataMode) {
        this.handler_.post(new 7(this, nrDataMode));
    }
}
