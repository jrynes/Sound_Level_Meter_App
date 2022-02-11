package com.nextradioapp.nextradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDrawerEvent;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NowPlayingBarFragment_ extends NowPlayingBarFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public NowPlayingBarFragment_() {
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
            this.contentView_ = inflater.inflate(2130903086, container, false);
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
        this.btnShare = hasViews.findViewById(2131689547);
        this.textViewSmallStation = (TextView) hasViews.findViewById(2131689598);
        this.imageBtnPause = (ImageView) hasViews.findViewById(2131689604);
        this.imageBtnPlay = (ImageView) hasViews.findViewById(2131689605);
        this.imageViewSmall = (ImageView) hasViews.findViewById(2131689597);
        this.textViewSmallEvent = (TextView) hasViews.findViewById(2131689600);
        this.viewAnimatorNowPlaying = (ViewAnimator) hasViews.findViewById(2131689595);
        this.btnDislike = hasViews.findViewById(2131689548);
        this.imageButtonHeartOn = (ImageView) hasViews.findViewById(2131689602);
        this.scrollView = (ScrollView) hasViews.findViewById(2131689487);
        this.btnLike = hasViews.findViewById(2131689546);
        this.textViewSmallStationPart2 = (TextView) hasViews.findViewById(2131689599);
        this.progressBar = (ProgressBar) hasViews.findViewById(2131689492);
        this.imageButtonHeartOff = (ImageView) hasViews.findViewById(2131689603);
        this.playing_bar_layout = (LinearLayout) hasViews.findViewById(2131689716);
        View view = hasViews.findViewById(2131689604);
        if (view != null) {
            view.setOnClickListener(new 1(this));
        }
        view = hasViews.findViewById(2131689605);
        if (view != null) {
            view.setOnClickListener(new 2(this));
        }
        view = hasViews.findViewById(2131689603);
        if (view != null) {
            view.setOnClickListener(new 3(this));
        }
        view = hasViews.findViewById(2131689602);
        if (view != null) {
            view.setOnClickListener(new 4(this));
        }
        afterViews();
    }

    @Subscribe
    public void dataChange(NRDataMode dataMode) {
        this.handler_.post(new 5(this, dataMode));
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        super.onEventChanged(event);
    }

    @Subscribe
    public void onDrawerOpened(NRDrawerEvent drawerEvent) {
        super.onDrawerOpened(drawerEvent);
    }

    @Subscribe
    public void radioResult(NRRadioResult result) {
        this.handler_.post(new 6(this, result));
    }

    public void updateCards(NextRadioEventInfo nextRadioEvent) {
        this.handler_.post(new 7(this, nextRadioEvent));
    }

    public void updateViews(NextRadioEventInfo event) {
        this.handler_.post(new 8(this, event));
    }
}
