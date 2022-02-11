package com.nextradioapp.nextradio.fragments;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.activities.AcquireLocationActivity;
import com.nextradioapp.nextradio.activities.AcquireLocationActivity_;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment(2130903085)
public class ManualTuner extends Fragment {
    private static final String COLOMBIA = "CO";
    @ViewById
    View blue_bar_line;
    @ViewById
    Button buttonEnhancedMode;
    @ViewById
    TextView hyphenTextView;
    private boolean mIsDataOff;
    @ViewById
    LinearLayout manualTuneCallLettersLayout;
    @ViewById
    TextView manualTunerCallLettersTextView;
    @ViewById
    TextView manualTunerFrequencyTextView;
    @ViewById
    TextView manualTunerSloganTextView;
    @ViewById
    ImageButton seekDownImageButton;
    @ViewById
    ImageButton seekUpImageButton;
    @ViewById
    ImageButton tuneDownImageButton;
    @ViewById
    ImageButton tuneUpImageButton;

    @Click({2131689518})
    void restoreFULLPOWER() {
        NextRadioSDKWrapperProvider.setNoDataMode(false, getActivity());
        startActivity(new Intent(getActivity(), AcquireLocationActivity_.class));
        getActivity().finish();
    }

    @Click({2131689711})
    void setSeekDownButton_click() {
        NextRadioApplication.postToBus(this, new 1(this));
    }

    @Click({2131689712})
    void setTuneDownButton_click() {
        NextRadioApplication.postToBus(this, new 2(this));
    }

    @Click({2131689713})
    void setTuneUpButton_click() {
        NextRadioApplication.postToBus(this, new 3(this));
    }

    @Click({2131689714})
    void setSeekUpButton_click() {
        NextRadioApplication.postToBus(this, new 4(this));
    }

    public static ManualTuner newInstance(String param1, String param2) {
        ManualTuner fragment = new ManualTuner();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public ManualTuner() {
        this.mIsDataOff = false;
    }

    @UiThread
    @Subscribe
    public void onDataModeChanged(NRDataMode nrDataMode) {
        if (!isAdded()) {
            return;
        }
        if (nrDataMode.mIsDataModeOff) {
            this.manualTuneCallLettersLayout.setVisibility(8);
            if (!NextRadioApplication.isTablet) {
                this.buttonEnhancedMode.setVisibility(0);
            }
            Log.i("Manual Tune", "Data Mode OFF-XX");
            this.mIsDataOff = true;
            return;
        }
        this.manualTuneCallLettersLayout.setVisibility(0);
        if (!NextRadioApplication.isTablet) {
            this.buttonEnhancedMode.setVisibility(8);
        }
        this.mIsDataOff = false;
        Log.i("Manual Tune", "Data Mode ON");
    }

    @UiThread
    @Subscribe
    public void onEventReceived(NRCurrentEvent event) {
        if (isAdded() && event != null && event.currentEvent != null) {
            Log.i("Manual Tune", "Clearing Frequency2222");
            if (event.currentEvent.stationInfo.getStationType() == 1 || event.currentEvent.stationInfo.getStationType() == 0) {
                this.hyphenTextView.setVisibility(8);
            } else if (!this.mIsDataOff) {
                this.hyphenTextView.setVisibility(0);
            }
            this.manualTunerFrequencyTextView.setText(event.currentEvent.stationInfo.frequencyMHz());
            this.manualTunerCallLettersTextView.setText(event.currentEvent.stationInfo.callLetters != null ? event.currentEvent.stationInfo.callLetters : Stomp.EMPTY);
            this.manualTunerSloganTextView.setText(event.currentEvent.stationInfo.slogan != null ? event.currentEvent.stationInfo.slogan : Stomp.EMPTY);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @AfterViews
    void afterViews() {
        String country = NextRadioSDKWrapperProvider.getInstance().getCountryCode();
        if (country != null && country.equals(COLOMBIA)) {
            this.seekDownImageButton.setVisibility(8);
            this.seekUpImageButton.setVisibility(8);
        }
        if (NextRadioApplication.isTablet) {
            this.blue_bar_line.setVisibility(8);
        }
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
        if (AcquireLocationActivity.noStationAvailable) {
            displayAlert(getResources().getString(2131165250), getResources().getString(2131165320));
        }
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    private void displayAlert(String title, String msg) {
        new Builder(getActivity(), 2131427334).setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton(17039370, new 5(this)).setIcon(17301543).show();
    }
}
