package com.nextradioapp.nextradio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.nextradioapp.core.objects.PostalCodeInfo;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class LocationDialogFragment_ extends LocationDialogFragment implements HasViews, OnViewChangedListener {
    private View contentView_;
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    public LocationDialogFragment_() {
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
            this.contentView_ = inflater.inflate(2130903071, container, false);
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
        this.linearLayoutUpdatingStations = (LinearLayout) hasViews.findViewById(2131689490);
        this.noStationsLinearLayout = (LinearLayout) hasViews.findViewById(2131689622);
        this.editText = (EditText) hasViews.findViewById(2131689497);
        this.txtDialogCountryNotAvailable = (TextView) hasViews.findViewById(2131689623);
        this.txtNoStations = (TextView) hasViews.findViewById(2131689624);
        this.messageTextView3 = (TextView) hasViews.findViewById(2131689618);
        this.titleTextView4 = (TextView) hasViews.findViewById(2131689616);
        this.btnNetworkTryAgain = (Button) hasViews.findViewById(2131689509);
        this.FrameLayout1 = (FrameLayout) hasViews.findViewById(2131689485);
        this.manualEntry = (LinearLayout) hasViews.findViewById(2131689494);
        this.networkUnavailable = (LinearLayout) hasViews.findViewById(2131689617);
        this.spinnerDialogCountry = (Spinner) hasViews.findViewById(2131689619);
        this.btnDialogGetLocationZip = (Button) hasViews.findViewById(2131689620);
        this.txtErrorMessage = (TextView) hasViews.findViewById(2131689502);
        this.stationUpdateProgressText = (TextView) hasViews.findViewById(2131689621);
        this.radioButton = (RadioButton) hasViews.findViewById(2131689501);
        this.progressBarLoadingZip = hasViews.findViewById(2131689503);
        this.btnDialogBasicTuner = (Button) hasViews.findViewById(2131689625);
        View view = hasViews.findViewById(2131689620);
        if (view != null) {
            view.setOnClickListener(new 1(this));
        }
        view = hasViews.findViewById(2131689625);
        if (view != null) {
            view.setOnClickListener(new 2(this));
        }
        view = hasViews.findViewById(2131689509);
        if (view != null) {
            view.setOnClickListener(new 3(this));
        }
        onAfterViews();
    }

    @Subscribe
    public void onStationsUpdated(NRStationList stations) {
        super.onStationsUpdated(stations);
    }

    public void updateRadioButton(PostalCodeInfo postalCodeInfo) {
        this.handler_.post(new 4(this, postalCodeInfo));
    }

    public void showNoStationsError() {
        this.handler_.post(new 5(this));
    }

    public void showUpdatedToast() {
        this.handler_.post(new 6(this));
    }

    public void showUpdating() {
        this.handler_.post(new 7(this));
    }

    public void showErrorMessage(String s) {
        this.handler_.post(new 8(this, s));
    }

    public void showNetworkError(String msg, String btnText) {
        this.handler_.post(new 9(this, msg, btnText));
    }

    public void showManualEntry() {
        this.handler_.post(new 10(this));
    }
}
