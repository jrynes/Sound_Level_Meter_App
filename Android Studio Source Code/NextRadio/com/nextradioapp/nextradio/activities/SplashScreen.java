package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mixpanel.android.mpmetrics.MPConfig;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.RegistrationIntentService;
import com.nextradioapp.nextradio.adapters.TutorialSlidePagerAdapter;
import com.nextradioapp.nextradio.mixpanel.MipEvents;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.receivers.LocationReceiver;
import com.nextradioapp.nextradio.services.UIService_;
import com.nextradioapp.nextradio.views.CrossFadePageTransformer;
import com.nextradioapp.nextradio.views.FadePageTransformer;
import com.nextradioapp.radioadapters.AdapterListing;
import com.nextradioapp.radioadapters.FmRadioEmulated;
import com.nextradioapp.utils.CountryEULAManager;
import com.nextradioapp.utils.DateUtils;
import com.nextradioapp.utils.DeviceNotification;
import com.nextradioapp.utils.PermissionUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONObject;
import org.xbill.DNS.Zone;

public class SplashScreen extends FragmentActivity implements OnRequestPermissionsResultCallback {
    public static int NUM_PAGES = 0;
    private static final String TAG = "SplashScreen";
    String action;
    private boolean allowAudioPermission;
    private boolean allowLocationPermission;
    private boolean allowStoragePermission;
    private RelativeLayout blue_device_bg_layout;
    private LinearLayout circles;
    private Button done;
    private boolean isCurrentOSAndroidM;
    private boolean isFromTerms;
    private boolean isOpaque;
    private TimerTask mTimerTask;
    private ImageButton next;
    private FrameLayout normal_splash_layout;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Button skip;
    private ImageView splash_imageView1;
    private int tempPosition;
    private int tempVal;
    private RelativeLayout tutorial_bottom_background_layout;
    private RelativeLayout tutorial_view;
    String what;

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.1 */
    class C11821 extends TimerTask {
        C11821() {
        }

        public void run() {
            boolean bEULAAgreed = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this).getBoolean(PreferenceStorage.EULA_AGREEMENT, false);
            CountryEULAManager eula = new CountryEULAManager(SplashScreen.this);
            if (!bEULAAgreed && eula.renderTermsScreen()) {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, TermsAndPrivacyActivity.class));
                SplashScreen.this.finish();
            } else if (PreferenceManager.getDefaultSharedPreferences(SplashScreen.this).getBoolean("didTutorial", false)) {
                SplashScreen.this.displayNextActivity();
            } else {
                SplashScreen.this.runOnUiThread(new 1(this));
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.2 */
    class C11832 implements OnClickListener {
        C11832() {
        }

        public void onClick(View v) {
            SplashScreen.this.endTutorial();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.3 */
    class C11843 implements OnClickListener {
        C11843() {
        }

        public void onClick(View v) {
            if (!SplashScreen.this.isCurrentOSAndroidM) {
                SplashScreen.this.pager.setCurrentItem(SplashScreen.this.pager.getCurrentItem() + 1, true);
            } else if (SplashScreen.this.tempPosition != 1 && SplashScreen.this.tempPosition != 2) {
                SplashScreen.this.pager.setCurrentItem(SplashScreen.this.pager.getCurrentItem() + 1, true);
            } else if (SplashScreen.this.tempVal >= 1) {
                SplashScreen.this.setPermissionDisplay(SplashScreen.this.tempPosition);
            } else {
                SplashScreen.this.pager.setCurrentItem(SplashScreen.this.pager.getCurrentItem() + 1, true);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.4 */
    class C11854 implements OnClickListener {
        C11854() {
        }

        public void onClick(View v) {
            SplashScreen.this.endTutorial();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.5 */
    class C11865 implements OnPageChangeListener {
        C11865() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (SplashScreen.this.isCurrentOSAndroidM) {
                if ((SplashScreen.this.allowLocationPermission || position != 1) && (SplashScreen.this.allowStoragePermission || position != 2)) {
                    SplashScreen.this.tempVal = 0;
                } else {
                    SplashScreen.this.tempVal = SplashScreen.this.tempVal + 1;
                }
            }
            if (position != SplashScreen.NUM_PAGES - 2 || positionOffset <= 0.0f) {
                if (!SplashScreen.this.isOpaque) {
                    SplashScreen.this.pager.setBackgroundColor(SplashScreen.this.getResources().getColor(2131558447));
                    SplashScreen.this.isOpaque = true;
                }
            } else if (SplashScreen.this.isOpaque) {
                SplashScreen.this.pager.setBackgroundColor(0);
                SplashScreen.this.isOpaque = false;
            }
        }

        public void onPageSelected(int position) {
            if (SplashScreen.this.isCurrentOSAndroidM) {
                SplashScreen.this.tempPosition = position;
                if (SplashScreen.this.tempVal <= 1) {
                    SplashScreen.this.setIndicator(position);
                    SplashScreen.this.changeBottomPageColor(position);
                    SplashScreen.this.enableTutorialButtons(position);
                    return;
                } else if (position != 2 || SplashScreen.this.allowLocationPermission) {
                    SplashScreen.this.tempPosition = 2;
                    SplashScreen.this.setPermissionDisplay(SplashScreen.this.tempPosition);
                    return;
                } else {
                    SplashScreen.this.tempPosition = 1;
                    SplashScreen.this.setPermissionDisplay(SplashScreen.this.tempPosition);
                    return;
                }
            }
            SplashScreen.this.setIndicator(position);
            SplashScreen.this.changeBottomPageColor(position);
            SplashScreen.this.enableTutorialButtons(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.6 */
    class C11876 implements DialogInterface.OnClickListener {
        final /* synthetic */ int val$permissionType;

        C11876(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            if (this.val$permissionType == 1) {
                NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
                PermissionUtil.saveLocationPermissionDeniedDate(SplashScreen.this, sdf.format(new Date()));
            } else if (this.val$permissionType == 2) {
                NextRadioApplication.getInstance().changeImageCache(false);
                PermissionUtil.saveStoragePermissionDeniedDate(SplashScreen.this, sdf.format(new Date()));
            }
            dialog.dismiss();
            SplashScreen.this.pager.setCurrentItem(SplashScreen.this.pager.getCurrentItem() + 1, true);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.SplashScreen.7 */
    class C11887 implements DialogInterface.OnClickListener {
        final /* synthetic */ int val$permissionType;

        C11887(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.val$permissionType == 1) {
                ActivityCompat.requestPermissions(SplashScreen.this, PermissionUtil.PERMISSIONS_LOCATION, 1);
            } else if (this.val$permissionType == 2) {
                ActivityCompat.requestPermissions(SplashScreen.this, PermissionUtil.PERMISSIONS_STORAGE, 2);
            }
        }
    }

    public SplashScreen() {
        this.isOpaque = true;
        this.tempVal = 0;
        this.tempPosition = 0;
        this.allowStoragePermission = true;
        this.isCurrentOSAndroidM = false;
        this.action = Stomp.EMPTY;
        this.what = Stomp.EMPTY;
    }

    static {
        NUM_PAGES = 5;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903113);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        if (VERSION.SDK_INT >= 23) {
            this.isCurrentOSAndroidM = true;
        }
        ImageView englishSplash = (ImageView) findViewById(2131689786);
        ImageView spanishSplash = (ImageView) findViewById(2131689787);
        if (Locale.getDefault().getLanguage().equals("es")) {
            spanishSplash.setVisibility(0);
            englishSplash.setVisibility(8);
            this.splash_imageView1 = spanishSplash;
        } else {
            this.splash_imageView1 = englishSplash;
        }
        this.tutorial_view = (RelativeLayout) findViewById(2131689525);
        this.blue_device_bg_layout = (RelativeLayout) findViewById(2131689788);
        this.normal_splash_layout = (FrameLayout) findViewById(2131689785);
        this.pager = (ViewPager) findViewById(2131689522);
        scheduleLocationAlarm();
        launchTutorials();
        if (checkPlayServices()) {
            startService(new Intent(this, RegistrationIntentService.class));
        }
    }

    protected void onResume() {
        super.onResume();
        checkPermissionsAccepted();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("didTutorial", false) && this.mTimerTask == null) {
            launchStationsActivityIntent();
        }
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
        }
        this.mTimerTask = null;
        super.onStop();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            Log.d(TAG, "onNewIntent intent:NULL");
            return;
        }
        this.action = intent.getAction();
        this.what = intent.getStringExtra("what");
        if (this.action == null) {
            Log.d(TAG, "onNewIntent action:NULL");
        } else if (this.what != null && !this.what.equals("nowplaying")) {
            Log.d(TAG, "onNewIntent what:" + this.what);
        }
    }

    private void scheduleLocationAlarm() {
        if (!this.isCurrentOSAndroidM) {
            LocationReceiver.getInstance().scheduleAlarms(this);
            this.isCurrentOSAndroidM = false;
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationReceiver.getInstance().scheduleAlarms(this);
        }
    }

    private void launchStationsActivityIntent() {
        Intent intent;
        if (NextRadioApplication.isTablet) {
            intent = new Intent(this, TabletFragContainerActivity_.class);
        } else {
            intent = new Intent(this, StationsActivity_.class);
        }
        if (this.action.contentEquals("nowplaying")) {
            intent.setAction(this.action);
        }
        if (this.action.contentEquals("nowplaying") && this.what.contentEquals("nowplaying")) {
            intent.putExtra("what", this.what);
        }
        startActivity(intent);
        if (!isFinishing()) {
            finish();
        }
    }

    private void tutorialsLaunched() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("didTutorial", true).apply();
    }

    private void setBlueDeviceBgLayout() {
        if (new DeviceNotification(this).getDeviceName().contains("BLU")) {
            this.blue_device_bg_layout.setVisibility(0);
            this.normal_splash_layout.setVisibility(8);
            this.splash_imageView1.setVisibility(8);
            return;
        }
        this.splash_imageView1.setVisibility(0);
        this.blue_device_bg_layout.setVisibility(8);
        this.normal_splash_layout.setVisibility(0);
    }

    private void initDirectLaunch() {
        boolean isServiceRunning = false;
        setBlueDeviceBgLayout();
        this.tutorial_view.setVisibility(8);
        try {
            for (RunningServiceInfo service : ((ActivityManager) getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
                if (UIService_.class.getName().equals(service.service.getClassName())) {
                    isServiceRunning = true;
                }
            }
            if (MPConfig.DEBUG) {
                Log.d(TAG, "getBrandName: " + new DeviceNotification(this).getBrandName());
            }
            if (AdapterListing.getFMRadioImplementationClass(this, true) == FmRadioEmulated.class || new DeviceNotification(this).isDeviceRestricted() || (NextRadioApplication.isCurrentOSAndroidM() && new DeviceNotification(this).getBrandName().equalsIgnoreCase("motorola"))) {
                updateFirstTimeMixPanel(false);
                finish();
                startActivity(new Intent(this, RadioNotSupportedActivity_.class));
            } else if (isServiceRunning) {
                launchStationsActivityIntent();
            } else {
                this.mTimerTask = new C11821();
                new Timer().schedule(this.mTimerTask, 2000);
            }
        } catch (Exception e) {
            launchStationsActivityIntent();
        }
    }

    private void updateFirstTimeMixPanel(boolean isSupported) {
        if (!MixPanelHelper.getInstance(this).isFirstTimeAppOpen()) {
            MixPanelHelper.getInstance(this).firstTimeAppOpen(true);
            JSONObject props = new JSONObject();
            try {
                props.put(MipProperties.VIEW, "First Open");
                props.put(MipProperties.SUPPORTED_DEVICE, isSupported);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MixPanelHelper.getInstance(this).recordMIPJsonObject(props, MipEvents.APP);
        }
    }

    private void displayNextActivity() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("didAcquireLocation", false)) {
            launchStationsActivityIntent();
            return;
        }
        startActivity(new Intent(this, AcquireLocationActivity_.class));
        finish();
    }

    private void initTutorial() {
        MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "tutorial screen", MipEvents.TUTORIAL);
        this.tutorial_view.setVisibility(0);
        this.blue_device_bg_layout.setVisibility(8);
        this.splash_imageView1.setVisibility(8);
        this.tutorial_bottom_background_layout = (RelativeLayout) findViewById(2131689526);
        this.tutorial_bottom_background_layout.setBackgroundColor(getResources().getColor(2131558401));
        this.skip = (Button) Button.class.cast(findViewById(2131689527));
        this.skip.setOnClickListener(new C11832());
        this.next = (ImageButton) ImageButton.class.cast(findViewById(2131689530));
        this.next.setOnClickListener(new C11843());
        this.done = (Button) Button.class.cast(findViewById(2131689529));
        this.done.setOnClickListener(new C11854());
        this.pager = (ViewPager) findViewById(2131689522);
        this.pagerAdapter = new TutorialSlidePagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(this.pagerAdapter);
        if (NextRadioApplication.isTablet) {
            this.pager.setPageTransformer(true, new FadePageTransformer());
        } else {
            this.pager.setPageTransformer(true, new CrossFadePageTransformer());
        }
        this.pager.addOnPageChangeListener(new C11865());
        buildCircles();
    }

    private void setPermissionDisplay(int position) {
        this.tempVal = 0;
        this.allowLocationPermission = true;
        this.pager.setCurrentItem(position, true);
        displayPermissions(position);
        setIndicator(position);
        changeBottomPageColor(position);
        enableTutorialButtons(position);
    }

    private void launchTutorials() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.isFromTerms = extras.getBoolean("fromTerms");
        }
        if (!this.isFromTerms) {
            initDirectLaunch();
        } else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("didTutorial", false)) {
            displayNextActivity();
        } else {
            initTutorial();
        }
    }

    private void checkPermissionsAccepted() {
        boolean z;
        boolean z2 = true;
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            z = true;
        } else {
            z = false;
        }
        this.allowLocationPermission = z;
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            z2 = false;
        }
        this.allowStoragePermission = z2;
    }

    private void displayPermissions(int index) {
        if (index == 1) {
            showLocation(this);
        } else if (index == 2) {
            this.allowStoragePermission = true;
            showStoragePermission(this);
        }
    }

    private void enableTutorialButtons(int position) {
        if (position == NUM_PAGES - 2) {
            this.skip.setVisibility(8);
            this.next.setVisibility(8);
            this.done.setVisibility(0);
        } else if (position < NUM_PAGES - 2) {
            this.skip.setVisibility(0);
            this.next.setVisibility(0);
            this.done.setVisibility(8);
        } else if (position == NUM_PAGES - 1) {
            endTutorial();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.pager != null) {
            this.pager.clearOnPageChangeListeners();
        }
    }

    private void changeBottomPageColor(int index) {
        int colorCode = 2131558405;
        if (index == 0) {
            colorCode = 2131558401;
        } else if (index == 1) {
            colorCode = 2131558402;
        } else if (index == 2) {
            colorCode = 2131558403;
        } else if (index == 3) {
            colorCode = 2131558405;
        } else if (index == 4) {
            colorCode = 2131558405;
        }
        this.tutorial_bottom_background_layout.setBackgroundColor(getResources().getColor(colorCode));
    }

    private void buildCircles() {
        this.circles = (LinearLayout) LinearLayout.class.cast(findViewById(2131689528));
        int padding = (int) ((5.0f * getResources().getDisplayMetrics().density) + 0.5f);
        for (int i = 0; i < NUM_PAGES - 1; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(2130837671);
            circle.setLayoutParams(new LayoutParams(-2, -2));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            this.circles.addView(circle);
        }
        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < NUM_PAGES) {
            for (int i = 0; i < NUM_PAGES - 1; i++) {
                ImageView circle = (ImageView) this.circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(2131558438));
                } else {
                    circle.setColorFilter(getResources().getColor(17170445));
                }
            }
        }
    }

    private void endTutorial() {
        tutorialsLaunched();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("didInitTutorials", true).apply();
        displayNextActivity();
    }

    public void onBackPressed() {
        if (this.pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
        }
    }

    private void showLocation(Activity mActivity) {
        this.allowStoragePermission = false;
        this.allowAudioPermission = false;
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            try {
                if (!DateUtils.getCurrentDate().equals(PermissionUtil.getLocationPermissionDeniedDate(this))) {
                    requestLocationPermission(mActivity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void requestLocationPermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION")) {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_LOCATION, 1);
        } else {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_LOCATION, 1);
        }
    }

    private void showStoragePermission(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            try {
                if (!DateUtils.getCurrentDate().equals(PermissionUtil.getStoragePermissionStateDeniedDate(this))) {
                    requestStoragePermission(mActivity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void requestStoragePermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_STORAGE, 2);
        } else {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_STORAGE, 2);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Zone.PRIMARY /*1*/:
                if (grantResults[0] == 0) {
                    NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
                    LocationReceiver.getInstance().scheduleAlarms(this);
                    this.pager.setCurrentItem(this.pager.getCurrentItem() + 1, true);
                } else if (PermissionUtil.isLocationPermissionAccepted(this)) {
                    NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
                    PermissionUtil.saveLocationPermissionDeniedDate(this, DateUtils.getCurrentDate());
                } else {
                    PermissionUtil.saveLocationPermissionState(this, true);
                    displayAlert(getResources().getString(2131165340), getResources().getString(2131165341), 1);
                }
            case Zone.SECONDARY /*2*/:
                if (grantResults[0] == 0) {
                    NextRadioApplication.getInstance().changeImageCache(true);
                    this.pager.setCurrentItem(this.pager.getCurrentItem() + 1, true);
                } else if (PermissionUtil.isStoragePermissionAccepted(this)) {
                    NextRadioApplication.getInstance().changeImageCache(false);
                    PermissionUtil.saveStoragePermissionDeniedDate(this, DateUtils.getCurrentDate());
                } else {
                    PermissionUtil.saveStoragePermissionState(this, true);
                    displayAlert(getResources().getString(2131165343), getResources().getString(2131165344), 2);
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void displayAlert(String title, String msg, int permissionType) {
        new Builder(this, 2131427334).setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton(17039370, new C11887(permissionType)).setNegativeButton(17039369, new C11876(permissionType)).setIcon(17301543).show();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode == 0) {
            return true;
        }
        if (apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.getErrorDialog(this, resultCode, 9000).show();
        } else {
            Log.i(TAG, "This device is not supported.");
            finish();
        }
        return false;
    }
}
