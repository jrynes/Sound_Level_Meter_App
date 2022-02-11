package com.nextradioapp.nextradio.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nextradioapp.core.interfaces.IPostalCodeInfoListener;
import com.nextradioapp.core.objects.Location;
import com.nextradioapp.core.objects.PostalCodeInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.activities.AcquireLocationActivity;
import com.nextradioapp.nextradio.activities.StationsActivity;
import com.nextradioapp.nextradio.activities.TabletFragContainerActivity;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.nextradioapp.utils.TelephonyUtils;
import com.nextradioapp.utils.ToasterStrudle;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment(2130903071)
public class LocationDialogFragment extends DialogFragment implements OnItemSelectedListener {
    private static final String TAG = "LocationDialogFragment";
    @ViewById
    FrameLayout FrameLayout1;
    @ViewById
    Button btnDialogBasicTuner;
    @ViewById
    Button btnDialogGetLocationZip;
    @ViewById
    Button btnNetworkTryAgain;
    private String[] countryNames;
    private String[] countryPrefix;
    @ViewById
    EditText editText;
    private boolean isManualEntry;
    @ViewById
    LinearLayout linearLayoutUpdatingStations;
    private Location mCurrLoc;
    private boolean mDoRefresh;
    private OnNoStationAvailListener mListener;
    private Thread mStationUpdateThread;
    @ViewById
    LinearLayout manualEntry;
    @ViewById
    TextView messageTextView3;
    @ViewById
    LinearLayout networkUnavailable;
    @ViewById
    LinearLayout noStationsLinearLayout;
    private IPostalCodeInfoListener postalCodeListener;
    @ViewById
    View progressBarLoadingZip;
    @ViewById
    RadioButton radioButton;
    private int selectedCountryIndex;
    private int selectedItemPos;
    @ViewById
    Spinner spinnerDialogCountry;
    @ViewById
    TextView stationUpdateProgressText;
    private boolean stationsRequested;
    @ViewById
    TextView titleTextView4;
    @ViewById
    TextView txtDialogCountryNotAvailable;
    @ViewById
    TextView txtErrorMessage;
    @ViewById
    TextView txtNoStations;

    public interface OnNoStationAvailListener {
        void onNoStationFound();
    }

    public LocationDialogFragment() {
        this.postalCodeListener = new 1(this);
    }

    @Click({2131689620})
    void btnLocationZip_Click() {
        MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "ZipCode Prompt", "ZipCode");
        if (this.editText.getText().toString().length() >= AcquireLocationActivity.validationCount) {
            this.progressBarLoadingZip.setVisibility(0);
            this.txtErrorMessage.setVisibility(4);
            this.radioButton.setVisibility(4);
            String zipTarget = this.editText.getText().toString().trim().replace(" ", "%20");
            hideKeyBoard(this.editText);
            NextRadioSDKWrapperProvider.getInstance().requestPostalCodeInfo(zipTarget, this.countryPrefix[this.selectedCountryIndex], this.postalCodeListener);
        }
    }

    @Click({2131689625})
    void basicTuner_Click() {
        if (NextRadioApplication.isTablet) {
            ((TabletFragContainerActivity) getActivity()).showManualTunerFromDialog();
        } else {
            ((StationsActivity) getActivity()).showManualTunerFromDialog();
        }
        dismiss();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnNoStationAvailListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Click({2131689509})
    void btnNetworkTryAgain_click() {
        if (this.btnNetworkTryAgain.getText().toString().equalsIgnoreCase("OK")) {
            dismiss();
            this.mListener.onNoStationFound();
            return;
        }
        showUpdating();
        this.mStationUpdateThread = new Thread(new 2(this));
        this.mStationUpdateThread.start();
    }

    private boolean isGpsEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService("location");
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled("gps");
        } catch (Exception e) {
        }
        try {
            network_enabled = lm.isProviderEnabled("network");
        } catch (Exception e2) {
        }
        return gps_enabled && network_enabled;
    }

    @UiThread
    public void showErrorMessage(String s) {
        if (isAdded()) {
            this.progressBarLoadingZip.setVisibility(8);
            this.radioButton.setVisibility(4);
            this.txtErrorMessage.setText(s);
            this.txtErrorMessage.setVisibility(0);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, 16973939);
        this.isManualEntry = false;
        if (getArguments() != null) {
            this.mDoRefresh = getArguments().getBoolean("DoRefresh", true);
        } else {
            this.mDoRefresh = true;
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(32);
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:" + this.mDoRefresh);
        this.stationsRequested = false;
        if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") == 0) {
            this.mDoRefresh = true;
            disableManualEntry();
        }
        NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
        NextRadioApplication.registerWithBus(this);
        if (this.mDoRefresh) {
            if (isGpsEnabled()) {
                this.mStationUpdateThread = new Thread(new 3(this));
                this.mStationUpdateThread.start();
            } else {
                showManualEntry();
            }
        }
        disableManualEntry();
    }

    private void disableManualEntry() {
        this.txtErrorMessage.setVisibility(8);
        this.manualEntry.setVisibility(4);
        this.networkUnavailable.setVisibility(8);
        this.linearLayoutUpdatingStations.setVisibility(0);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    @Subscribe
    public void onStationsUpdated(NRStationList stations) {
        Log.d(TAG, "onStationsUpdated:" + stations.errorCode);
        if (!isAdded()) {
            return;
        }
        if (stations != null && stations.errorCode == 5) {
            showNoStationsError();
        } else if (stations.errorCode == 2 || stations.errorCode == 1) {
            showManualEntry();
        } else if (stations.errorCode == 3) {
            if (!this.stationsRequested) {
                showUpdating();
                NextRadioSDKWrapperProvider.getInstance().requestStations(true);
                this.stationsRequested = true;
            } else if (isNetworkConnectionAvailable()) {
                if (isAdded()) {
                    showNetworkError(getString(2131165320), getResources().getString(2131165338));
                }
            } else if (isAdded()) {
                showNetworkError(getString(2131165319), getString(2131165427));
            }
        } else if (this.stationsRequested && stations != null && stations.stationList != null) {
            if (!this.isManualEntry) {
                MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.VIEW, "Location Services", "ZipCode");
            }
            if (stations.updatedFromNetwork) {
                showUpdatedToast();
            }
            this.stationsRequested = false;
            dismiss();
        }
    }

    @UiThread
    public void showUpdatedToast() {
        if (getActivity() != null) {
            ToasterStrudle.displayToast(getActivity(), 0, getString(2131165222));
        }
    }

    @UiThread
    public void showNetworkError(String msg, String btnText) {
        if (isAdded()) {
            Log.d(TAG, "showNetworkError");
            this.progressBarLoadingZip.setVisibility(8);
            this.manualEntry.setVisibility(8);
            this.networkUnavailable.setVisibility(0);
            this.linearLayoutUpdatingStations.setVisibility(8);
            this.editText.getText().clear();
            setHeight();
            this.messageTextView3.setText(msg);
            this.btnNetworkTryAgain.setText(btnText);
        }
    }

    private void setHeight() {
        try {
            if (isAdded()) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getMetrics(outMetrics);
                getDialog().getWindow().setLayout(-2, (int) (BitmapDescriptorFactory.HUE_MAGENTA * outMetrics.density));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void showNoStationsError() {
        if (isAdded()) {
            Log.d(TAG, "Show No Stations available");
            this.noStationsLinearLayout.setVisibility(0);
            this.manualEntry.setVisibility(8);
            this.titleTextView4.setVisibility(8);
            this.progressBarLoadingZip.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
            this.btnDialogBasicTuner.setVisibility(0);
        }
    }

    @UiThread
    public void showManualEntry() {
        if (isAdded()) {
            this.isManualEntry = true;
            MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.VIEW, "Zip Code Prompt", "ZipCode");
            Log.d(TAG, "showManualEntry");
            this.manualEntry.setVisibility(0);
            this.networkUnavailable.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
        }
    }

    @UiThread
    public void showUpdating() {
        if (isAdded()) {
            this.manualEntry.setVisibility(8);
            this.networkUnavailable.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(0);
        }
    }

    @AfterViews
    public void onAfterViews() {
        showUpdating();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), 2131230733, 17367048);
        adapter.setDropDownViewResource(17367049);
        this.spinnerDialogCountry.setAdapter(adapter);
        this.spinnerDialogCountry.setOnItemSelectedListener(this);
        this.spinnerDialogCountry.setSelection(TelephonyUtils.getCountryCodeIndex(getActivity()));
        this.countryPrefix = getResources().getStringArray(2131230735);
        this.countryNames = getResources().getStringArray(2131230733);
        this.progressBarLoadingZip.setVisibility(8);
        this.editText.setText(getActivity().getSharedPreferences("locationDialogPrompt", 0).getString("lastLocationZip", Stomp.EMPTY));
        this.radioButton.setOnClickListener(new 4(this));
    }

    @UiThread
    public void updateRadioButton(PostalCodeInfo postalCodeInfo) {
        if (postalCodeInfo == null || postalCodeInfo.city == null) {
            Toast.makeText(getActivity(), getResources().getString(2131165219), 0).show();
            return;
        }
        this.progressBarLoadingZip.setVisibility(8);
        this.txtErrorMessage.setVisibility(8);
        this.mCurrLoc = postalCodeInfo.getLocation();
        this.radioButton.setVisibility(0);
        if (postalCodeInfo.state == null || postalCodeInfo.state.length() <= 0) {
            this.radioButton.setText(postalCodeInfo.city + ", " + ((String) this.spinnerDialogCountry.getItemAtPosition(this.selectedCountryIndex)));
        } else {
            this.radioButton.setText(postalCodeInfo.city + ", " + postalCodeInfo.state);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.d(TAG, "Country position: " + pos);
        this.selectedItemPos = pos;
        if (pos == 6) {
            this.editText.setVisibility(8);
            this.FrameLayout1.setVisibility(8);
            this.txtDialogCountryNotAvailable.setVisibility(0);
            this.txtErrorMessage.setVisibility(8);
            this.noStationsLinearLayout.setVisibility(0);
            this.txtNoStations.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
            this.btnDialogBasicTuner.setVisibility(0);
            this.btnDialogGetLocationZip.setVisibility(8);
            hideKeyBoard(this.editText);
        } else {
            if (pos == 5) {
                AcquireLocationActivity.validationCount = 3;
                this.editText.setHint(getResources().getString(2131165276));
            } else {
                AcquireLocationActivity.validationCount = 5;
                this.editText.setHint(getResources().getString(2131165299));
            }
            this.editText.setVisibility(0);
            this.FrameLayout1.setVisibility(0);
            this.txtNoStations.setVisibility(0);
            this.txtDialogCountryNotAvailable.setVisibility(8);
            this.txtNoStations.setVisibility(0);
            this.noStationsLinearLayout.setVisibility(8);
            this.btnDialogGetLocationZip.setVisibility(0);
        }
        this.selectedCountryIndex = pos;
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void setPeruMsg() {
        if (this.selectedItemPos == 5) {
            AcquireLocationActivity.validationCount = 3;
            this.editText.setHint(getResources().getString(2131165276));
            return;
        }
        AcquireLocationActivity.validationCount = 5;
        this.editText.setHint(getResources().getString(2131165299));
    }

    boolean isNetworkConnectionAvailable() {
        NetworkInfo info = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        State network = info.getState();
        if (network == State.CONNECTED || network == State.CONNECTING) {
            return true;
        }
        return false;
    }

    private void hideKeyBoard(EditText editText) {
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
