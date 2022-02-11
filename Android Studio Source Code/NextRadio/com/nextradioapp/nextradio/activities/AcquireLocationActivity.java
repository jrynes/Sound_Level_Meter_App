package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.nextradioapp.core.interfaces.IPostalCodeInfoListener;
import com.nextradioapp.core.objects.Location;
import com.nextradioapp.core.objects.PostalCodeInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.nextradioapp.nextradio.receivers.LocationReceiver;
import com.nextradioapp.utils.PermissionUtil;
import com.nextradioapp.utils.TelephonyUtils;
import com.squareup.otto.Subscribe;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;

@EActivity(2130903041)
public class AcquireLocationActivity extends Activity implements OnRequestPermissionsResultCallback, OnItemSelectedListener {
    public static final int OTHER_POSITION = 6;
    public static final int PERU_POSITION = 5;
    private static final String TAG = "AcquireLocationActivity";
    public static boolean noStationAvailable;
    public static int validationCount;
    @ViewById
    FrameLayout FrameLayout2;
    @ViewById
    TextView acquireLocationCloseButton;
    @ViewById
    TextView acquiringLocationTextView;
    @ViewById
    Button btnBasicTuner;
    @ViewById
    Button btnGetLocationZip;
    private String[] countryNames;
    private String[] countryPrefix;
    @ViewById
    View dummyView;
    @ViewById
    EditText editText;
    @ViewById
    ImageView imageView;
    private boolean isLocationMixPanelRecorded;
    private boolean isManualEntry;
    @ViewById
    LinearLayout layoutNetworkConnectivity;
    @ViewById
    LinearLayout linearLayoutNoStations;
    @ViewById
    LinearLayout linearLayoutUpdatingStations;
    private Location mCurrLoc;
    private boolean mHasInitialized;
    @ViewById
    LinearLayout manualEntry;
    private IPostalCodeInfoListener postalCodeListener;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    ProgressBar progressBarLoadingZip;
    @ViewById
    RadioButton radioButton;
    private int selectedCountryIndex;
    private int selectedItemPos;
    @ViewById
    Spinner spinnerCountry;
    @ViewById
    TextView textView;
    @ViewById
    TextView txtCountryNotAvailable;
    @ViewById
    TextView txtErrorMessage;
    @ViewById
    TextView txtNoStationListing;

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.1 */
    class C11571 implements Runnable {
        C11571() {
        }

        public void run() {
            try {
                Thread.sleep(500);
                NextRadioSDKWrapperProvider.register((NextRadioApplication) AcquireLocationActivity.this.getApplicationContext());
                NextRadioSDKWrapperProvider.getInstance().requestStations(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.2 */
    class C11582 implements IPostalCodeInfoListener {
        C11582() {
        }

        public void onPostalCodeInfoReceived(PostalCodeInfo postalInfo) {
            if (postalInfo.country.equals(AcquireLocationActivity.this.countryPrefix[AcquireLocationActivity.this.selectedCountryIndex])) {
                AcquireLocationActivity.this.updateRadioButton(postalInfo);
                return;
            }
            AcquireLocationActivity.this.showErrorMessage(AcquireLocationActivity.this.getString(2131165215).replace("^1", AcquireLocationActivity.this.countryNames[AcquireLocationActivity.this.selectedCountryIndex]));
        }

        public void onNetworkUnavailable() {
            AcquireLocationActivity.this.showErrorMessage(AcquireLocationActivity.this.getString(2131165219));
        }

        public void onInvalidZip() {
            String msg = Stomp.EMPTY;
            if (AcquireLocationActivity.this.selectedItemPos == AcquireLocationActivity.PERU_POSITION) {
                msg = AcquireLocationActivity.this.getResources().getString(2131165297);
            } else {
                msg = AcquireLocationActivity.this.getResources().getString(2131165301);
            }
            AcquireLocationActivity.this.showErrorMessage(msg);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.3 */
    class C11593 implements Runnable {
        C11593() {
        }

        public void run() {
            try {
                Thread.sleep(1000);
                NextRadioSDKWrapperProvider.getInstance().requestStations(true);
            } catch (InterruptedException e) {
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.4 */
    class C11604 implements TextWatcher {
        C11604() {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() >= AcquireLocationActivity.PERU_POSITION) {
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.5 */
    class C11615 implements OnClickListener {
        C11615() {
        }

        public void onClick(View view) {
            AcquireLocationActivity.this.showUpdating();
            NextRadioSDKWrapperProvider.getInstance().setLocation(AcquireLocationActivity.this.mCurrLoc);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.6 */
    class C11626 implements DialogInterface.OnClickListener {
        C11626() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
            PermissionUtil.saveLocationPermissionDeniedDate(AcquireLocationActivity.this, sdf.format(new Date()));
            AcquireLocationActivity.this.initBackGround();
            dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity.7 */
    class C11637 implements DialogInterface.OnClickListener {
        C11637() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ActivityCompat.requestPermissions(AcquireLocationActivity.this, PermissionUtil.PERMISSIONS_LOCATION, 1);
        }
    }

    public AcquireLocationActivity() {
        this.postalCodeListener = new C11582();
    }

    static {
        validationCount = PERU_POSITION;
    }

    @Click({2131689498})
    void btnLocationZip_Click() {
        MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.TAP, "Zip Code Prompt", "ZipCode");
        if (this.editText.getText() != null && this.editText.getText().toString().length() >= PERU_POSITION) {
            this.progressBarLoadingZip.setVisibility(0);
            this.txtErrorMessage.setVisibility(4);
            this.radioButton.setVisibility(4);
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
            NextRadioSDKWrapperProvider.getInstance().requestPostalCodeInfo(this.editText.getText().toString().trim().replace(" ", "%20"), this.countryPrefix[this.selectedCountryIndex], this.postalCodeListener);
        }
    }

    @Click({2131689506})
    void basicTuner_Click() {
        Intent i;
        if (NextRadioApplication.isTablet) {
            i = new Intent(this, TabletFragContainerActivity_.class);
        } else {
            i = new Intent(this, StationsActivity_.class);
        }
        i.putExtra(TabletFragContainerActivity.SHOW_MANUAL_TUNER, true);
        startActivity(i);
        finish();
    }

    @Click({2131689509})
    void btnNetworkTryAgain_click() {
        if (isNetworkConnectionAvailable()) {
            showUpdating();
            new Thread(new C11571()).start();
            return;
        }
        Toast.makeText(this, getResources().getString(2131165219), 0).show();
    }

    @Click({2131689510})
    void setAcquireLocationCloseButton_click() {
        doneWithThisPage();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            initBackGround();
        } else {
            showLocation(this);
        }
    }

    private void initBackGround() {
        doSDKInitInBackground();
    }

    @Background
    protected void doSDKInitInBackground() {
        ((NextRadioApplication) getApplicationContext()).initSDK();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623936, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 2131689837) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.d(TAG, "Country position: " + pos);
        this.selectedItemPos = pos;
        if (pos == OTHER_POSITION) {
            this.editText.setVisibility(8);
            this.linearLayoutNoStations.setVisibility(0);
            this.txtCountryNotAvailable.setVisibility(0);
            this.txtNoStationListing.setVisibility(8);
            this.btnBasicTuner.setVisibility(0);
            this.btnGetLocationZip.setVisibility(8);
            this.txtErrorMessage.setVisibility(8);
            this.FrameLayout2.setVisibility(8);
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
        } else {
            setPeruMsg();
            this.editText.setVisibility(0);
            this.txtNoStationListing.setVisibility(0);
            this.linearLayoutNoStations.setVisibility(8);
            this.btnGetLocationZip.setVisibility(0);
            this.txtCountryNotAvailable.setVisibility(8);
            this.btnBasicTuner.setVisibility(8);
            this.FrameLayout2.setVisibility(0);
        }
        this.selectedCountryIndex = pos;
    }

    private void setPeruMsg() {
        if (this.selectedItemPos == PERU_POSITION) {
            validationCount = 3;
            this.editText.setHint(getResources().getString(2131165276));
            return;
        }
        validationCount = PERU_POSITION;
        this.editText.setHint(getResources().getString(2131165299));
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @UiThread
    @Subscribe
    public void onInitCompleted(NRInitCompleted initEvent) {
        Log.d(TAG, "onInitCompleted " + initEvent.statusCode);
        if (initEvent.statusCode == NRInitCompleted.STATUS_CODE_REGISTER_FAILED) {
            this.layoutNetworkConnectivity.setVisibility(0);
            this.manualEntry.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
            showErrorMessage(getString(2131165219));
            this.mHasInitialized = false;
        } else if (initEvent.statusCode == NRInitCompleted.STATUS_CODE_SUCCESS) {
            this.mHasInitialized = true;
            getStations();
        }
    }

    @UiThread
    @Subscribe
    public void onStationsUpdated(NRStationList stations) {
        Log.d(TAG, "onStationsUpdated:" + stations.errorCode);
        if (stations != null && stations.errorCode == PERU_POSITION) {
            showNoStations();
        } else if (stations.errorCode == 1 || stations.errorCode == 2) {
            if (this.mHasInitialized) {
                showManualEntry();
                return;
            }
            this.layoutNetworkConnectivity.setVisibility(0);
            this.manualEntry.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
            showErrorMessage(getString(2131165219));
        } else if (stations.errorCode == 3) {
            if (isNetworkConnectionAvailable()) {
                if (this.mHasInitialized) {
                    this.mHasInitialized = false;
                    doFakeWork();
                } else {
                    noStationAvailable = true;
                    doneWithThisPage();
                }
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
                return;
            }
            this.layoutNetworkConnectivity.setVisibility(0);
            this.manualEntry.setVisibility(8);
            this.linearLayoutUpdatingStations.setVisibility(8);
            showErrorMessage(getString(2131165219));
        } else if (stations != null && stations.stationList != null) {
            if (!this.isManualEntry) {
                MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Location Services", "ZipCode");
            }
            if (stations.updatedFromNetwork) {
                noStationAvailable = false;
                doneWithThisPage();
                return;
            }
            NextRadioSDKWrapperProvider.getInstance().requestStations(true);
        }
    }

    private void doneWithThisPage() {
        Intent i;
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("didAcquireLocation", true).commit();
        Log.d(TAG, "acquireLocationCloseButton");
        if (NextRadioApplication.isTablet) {
            i = new Intent(this, TabletFragContainerActivity_.class);
        } else {
            i = new Intent(this, StationsActivity_.class);
        }
        startActivity(i);
        finish();
    }

    private void doFakeWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getStations();
    }

    @UiThread
    public void showManualEntry() {
        if (!this.isLocationMixPanelRecorded) {
            this.isLocationMixPanelRecorded = true;
            this.isManualEntry = true;
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Zip Code Prompt", "ZipCode");
        }
        this.progressBarLoadingZip.setVisibility(8);
        Log.d(TAG, "showManualEntry");
        this.manualEntry.setVisibility(0);
        this.linearLayoutUpdatingStations.setVisibility(8);
        this.layoutNetworkConnectivity.setVisibility(8);
        this.txtErrorMessage.setVisibility(8);
        if (this.editText.requestFocus()) {
            ((InputMethodManager) getSystemService("input_method")).showSoftInput(this.editText, 1);
        }
    }

    @UiThread
    public void showErrorMessage(String msg) {
        this.progressBarLoadingZip.setVisibility(8);
        this.radioButton.setVisibility(4);
        this.txtErrorMessage.setText(msg);
        this.txtErrorMessage.setVisibility(0);
    }

    @UiThread
    public void showNoStations() {
        this.manualEntry.setVisibility(8);
        this.acquiringLocationTextView.setVisibility(8);
        this.linearLayoutUpdatingStations.setVisibility(8);
        this.linearLayoutNoStations.setVisibility(0);
        this.btnBasicTuner.setVisibility(0);
    }

    @UiThread
    public void updateRadioButton(PostalCodeInfo postalCodeInfo) {
        this.progressBarLoadingZip.setVisibility(8);
        this.txtErrorMessage.setVisibility(8);
        this.mCurrLoc = postalCodeInfo.getLocation();
        this.radioButton.setVisibility(0);
        if (postalCodeInfo.state == null || postalCodeInfo.state.length() <= 0) {
            this.radioButton.setText(postalCodeInfo.city + ", " + ((String) this.spinnerCountry.getItemAtPosition(this.selectedCountryIndex)));
        } else {
            this.radioButton.setText(postalCodeInfo.city + ", " + postalCodeInfo.state);
        }
    }

    protected void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
    }

    private void getStations() {
        if (isNetworkConnectionAvailable()) {
            showUpdating();
            new Thread(new C11593()).start();
            this.dummyView.requestFocus();
            return;
        }
        showErrorMessage("Internet connection unavailable");
    }

    boolean isNetworkConnectionAvailable() {
        NetworkInfo info = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        State network = info.getState();
        if (network == State.CONNECTED || network == State.CONNECTING) {
            return true;
        }
        return false;
    }

    @AfterViews
    public void onAfterViews() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 2131230733, 17367048);
        adapter.setDropDownViewResource(17367049);
        this.spinnerCountry.setOnItemSelectedListener(this);
        this.spinnerCountry.setAdapter(adapter);
        this.spinnerCountry.setSelection(TelephonyUtils.getCountryCodeIndex(this));
        this.countryPrefix = getResources().getStringArray(2131230735);
        this.countryNames = getResources().getStringArray(2131230733);
        this.progressBarLoadingZip.setVisibility(8);
        this.layoutNetworkConnectivity.setVisibility(8);
        ((NextRadioApplication) getApplication()).trackScreen("AquireLocationActivity");
        this.editText.addTextChangedListener(new C11604());
        this.radioButton.setOnClickListener(new C11615());
    }

    @UiThread
    public void showUpdating() {
        this.layoutNetworkConnectivity.setVisibility(8);
        this.manualEntry.setVisibility(8);
        this.textView.setVisibility(0);
        this.txtErrorMessage.setVisibility(8);
        this.linearLayoutUpdatingStations.setVisibility(0);
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromInputMethod(this.editText.getWindowToken(), 0);
    }

    private void showLocation(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                if (sdf.format(new Date()).equals(PermissionUtil.getLocationPermissionDeniedDate(this))) {
                    initBackGround();
                } else {
                    requestLocationPermission(mActivity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void requestLocationPermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION")) {
            displayAlert(getResources().getString(2131165340), getResources().getString(2131165341));
        } else {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_LOCATION, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (requestCode != 1) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults[0] == 0) {
            NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
            LocationReceiver.getInstance().scheduleAlarms(this);
            initBackGround();
        } else if (PermissionUtil.isLocationPermissionAccepted(this)) {
            NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
            PermissionUtil.saveLocationPermissionDeniedDate(this, sdf.format(new Date()));
            initBackGround();
        } else {
            PermissionUtil.saveLocationPermissionState(this, true);
            showLocation(this);
        }
    }

    private void displayAlert(String title, String msg) {
        new Builder(this, 2131427334).setTitle(title).setMessage(msg).setPositiveButton(17039370, new C11637()).setNegativeButton(17039369, new C11626()).setIcon(17301543).show();
    }
}
