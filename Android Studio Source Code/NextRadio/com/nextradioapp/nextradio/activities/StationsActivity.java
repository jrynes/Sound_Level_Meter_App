package com.nextradioapp.nextradio.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.tagmanager.DataLayer;
import com.nextradioapp.androidSDK.data.schema.Tables.impressionReporting;
import com.nextradioapp.androidSDK.utils.AppRater;
import com.nextradioapp.core.interfaces.IEventReceivedListener;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.fragments.EventDialogFragment;
import com.nextradioapp.nextradio.fragments.EventsListFragment_;
import com.nextradioapp.nextradio.fragments.LocationDialogFragment;
import com.nextradioapp.nextradio.fragments.LocationDialogFragment.OnNoStationAvailListener;
import com.nextradioapp.nextradio.fragments.LocationDialogFragment_;
import com.nextradioapp.nextradio.fragments.ManualTuner_;
import com.nextradioapp.nextradio.fragments.NavigationDrawerFragment;
import com.nextradioapp.nextradio.fragments.NowPlayingBarFragment;
import com.nextradioapp.nextradio.fragments.NowPlayingBarFragment.OnStorageRequestPermission;
import com.nextradioapp.nextradio.fragments.RecentlyPlayedListFragment_;
import com.nextradioapp.nextradio.fragments.StationsGridFragment_;
import com.nextradioapp.nextradio.fragments.StationsListFragment_;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDialogEvent;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.nextradioapp.nextradio.receivers.LocationReceiver;
import com.nextradioapp.nextradio.services.HeadsetStateReceiver;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.nextradio.views.NowPlayingTextView;
import com.nextradioapp.radioadapters.AdapterListing;
import com.nextradioapp.utils.DateUtils;
import com.nextradioapp.utils.HeadsetHelper;
import com.nextradioapp.utils.PermissionUtil;
import com.onelouder.adlib.AdView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

@EActivity(2130903044)
public class StationsActivity extends Activity implements OnRequestPermissionsResultCallback, OnNoStationAvailListener, OnStorageRequestPermission {
    private static final int ADS_ATT = 3;
    private static final int ADS_DOUBLECLICK = 1;
    private static final int ADS_PINSIGHT = 2;
    private static final int ADS_UNINITIALIZED = -1;
    private static String DOUBLECLICK_ID = null;
    private static String PINSIGHT_PRODUCT_ID = null;
    public static final String PREF_DISPLAYED_FRAGMENT = "PREF_DISPLAYED_FRAGMENT";
    public static final String SHOW_MANUAL_TUNER = "manualTuner";
    public static final String TAG = "StationsActivity";
    public static boolean stopLiveGuideVisualRecording;
    @ViewById
    public FrameLayout adviewHolder;
    private boolean bShowManualTuner;
    @ViewById
    public LinearLayout container;
    private boolean doNotRefreshIfPermissionDenied;
    @ViewById
    public DrawerLayout drawer_layout;
    private boolean isShareEventSelected;
    private boolean isSharePermissionRequested;
    public AdView mAdview;
    private int mChosenActivity;
    public Fragment mCurrFragment;
    private int mCurrFragmentID;
    private DeviceRegistrationInfo mDeviceRegistrationInfo;
    private int mDoubleClick;
    private ActionBarDrawerToggle mDrawerToggle;
    private Handler mHandler;
    private Menu mMainMenu;
    @FragmentById(2131689520)
    public NavigationDrawerFragment mNavigationDrawerFragment;
    private boolean mNoDataMode;
    private Runnable mPinsightKillerRunnable;
    private PublisherAdView mPublisherAdview;
    private HeadsetStateReceiver receiver;
    @ViewById
    public SlidingUpPanelLayout sliding_layout;

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.1 */
    class C11891 implements Runnable {
        C11891() {
        }

        public void run() {
            Log.d("StationsActivityAdview", "triggering adview killer");
            StationsActivity.this.destroyAdBanner();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.2 */
    class C11902 extends ActionBarDrawerToggle {
        C11902(Activity x0, DrawerLayout x1, int x2, int x3, int x4) {
            super(x0, x1, x2, x3, x4);
        }

        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
        }

        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.3 */
    class C11913 implements PanelSlideListener {
        C11913() {
        }

        public void onPanelSlide(View panel, float slideOffset) {
        }

        public void onPanelExpanded(View panel) {
            StationsActivity.stopLiveGuideVisualRecording = true;
            StationsActivity.this.mMainMenu.findItem(2131689839).setVisible(false);
            StationsActivity.this.mMainMenu.findItem(2131689838).setVisible(true);
            Log.d("Panel", "Expanded");
            NextRadioApplication.postToBus(this, new 1(this));
            StationsActivity.this.trackScreenToGoogleAnalytics("Now Playing");
            StationsActivity.this.destroyAdBanner();
        }

        public void onPanelAnchored(View view) {
            Log.d("Panel", "Anchored");
        }

        public void onPanelCollapsed(View panel) {
            StationsActivity.stopLiveGuideVisualRecording = false;
            Log.d("Panel", "Collapsed");
            StationsActivity.this.mMainMenu.findItem(2131689839).setVisible(true);
            StationsActivity.this.mMainMenu.findItem(2131689838).setVisible(false);
            StationsActivity.this.enableAdBanner();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.4 */
    class C11924 implements Comparator<StationInfo> {
        C11924() {
        }

        public int compare(StationInfo arg0, StationInfo arg1) {
            if (arg0.frequencyHz > arg1.frequencyHz) {
                return StationsActivity.ADS_DOUBLECLICK;
            }
            if (arg0.frequencyHz == arg1.frequencyHz) {
                return 0;
            }
            return StationsActivity.ADS_UNINITIALIZED;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.5 */
    class C11935 implements IEventReceivedListener {
        final /* synthetic */ String val$what;

        C11935(String str) {
            this.val$what = str;
        }

        public void onEventReceived(NextRadioEventInfo eventInfo) {
            ArrayList<EventAction> actions = NextRadioSDKWrapperProvider.getInstance().getEventActions(eventInfo);
            if (actions == null) {
                return;
            }
            if (this.val$what.equals("share")) {
                StationsActivity.this.getIntent().putExtra("what", Stomp.EMPTY);
                NextRadioApplication.mCurrentActivity = StationsActivity.this;
                NowPlayingBarFragment mDisplayFragment = (NowPlayingBarFragment) StationsActivity.this.getFragmentManager().findFragmentById(2131689515);
                if (mDisplayFragment != null && mDisplayFragment.isInLayout()) {
                    mDisplayFragment.displayEventAction(false);
                    return;
                }
                return;
            }
            Iterator i$ = actions.iterator();
            while (i$.hasNext()) {
                EventAction eventAction = (EventAction) i$.next();
                if (eventAction.getType().equals(this.val$what)) {
                    try {
                        Log.d(StationsActivity.TAG, "starting action...");
                        eventAction.start(4);
                        RadioAdapterService.isBuyThisSongSelected = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.6 */
    class C11946 extends NRNavigationEvent {
        C11946() {
            this.chosenView = 6;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.7 */
    class C11957 implements OnClickListener {
        final /* synthetic */ int val$permissionType;

        C11957(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.val$permissionType == StationsActivity.ADS_DOUBLECLICK) {
                NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
                PermissionUtil.saveLocationPermissionDeniedDate(StationsActivity.this, DateUtils.getCurrentDate());
            } else if (this.val$permissionType == StationsActivity.ADS_PINSIGHT) {
                NextRadioApplication.getInstance().changeImageCache(false);
                PermissionUtil.saveStoragePermissionDeniedDate(StationsActivity.this, DateUtils.getCurrentDate());
            }
            dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity.8 */
    class C11968 implements OnClickListener {
        final /* synthetic */ int val$permissionType;

        C11968(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            StationsActivity.this.doNotRefreshIfPermissionDenied = true;
            if (this.val$permissionType == StationsActivity.ADS_DOUBLECLICK) {
                ActivityCompat.requestPermissions(StationsActivity.this, PermissionUtil.PERMISSIONS_LOCATION, StationsActivity.ADS_DOUBLECLICK);
            } else if (this.val$permissionType == StationsActivity.ADS_PINSIGHT) {
                ActivityCompat.requestPermissions(StationsActivity.this, PermissionUtil.PERMISSIONS_STORAGE, StationsActivity.ADS_PINSIGHT);
            }
        }
    }

    public StationsActivity() {
        this.mMainMenu = null;
        this.mNoDataMode = false;
        this.bShowManualTuner = false;
        this.isShareEventSelected = false;
        this.mChosenActivity = ADS_UNINITIALIZED;
        this.mCurrFragmentID = 0;
        this.mDoubleClick = ADS_UNINITIALIZED;
        this.mPinsightKillerRunnable = new C11891();
    }

    static {
        PINSIGHT_PRODUCT_ID = "nextradio";
        DOUBLECLICK_ID = "/44520455/";
    }

    public Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        return this.mHandler;
    }

    @UiThread
    @Subscribe
    public void onNRNavigationEvent(NRNavigationEvent event) {
        Log.d(TAG, "onNRNavigationEvent:" + event);
        if (event.chosenView == 11) {
            Log.d(TAG, "Application Shutdown");
            NRRadioAction radioAction = new NRRadioAction();
            radioAction.action = ADS_PINSIGHT;
            radioAction.isQuitting = true;
            NextRadioApplication.postToBus(this, radioAction);
            finish();
        } else if (this.mCurrFragmentID == event.chosenView) {
            this.sliding_layout.collapsePane();
        } else {
            displayFragment(event.chosenView);
        }
    }

    @UiThread
    @Subscribe
    public void onNRDataMode(NRDataMode nrDataMode) {
        this.mNoDataMode = nrDataMode.mIsDataModeOff;
        NRNavigationEvent nrNavigationEvent;
        if (nrDataMode.mIsDataModeOff) {
            destroyAdBanner();
            this.sliding_layout.setSlidingEnabled(false);
            if (nrDataMode.mIsFreshRequest) {
                nrNavigationEvent = new NRNavigationEvent();
                nrNavigationEvent.chosenView = 4;
                NextRadioApplication.postToBus(this, nrNavigationEvent);
                NextRadioSDKWrapperProvider.setDataModeReceived();
            }
        } else if (nrDataMode.mIsFreshRequest) {
            enableAdBanner();
            this.sliding_layout.setSlidingEnabled(true);
            nrNavigationEvent = new NRNavigationEvent();
            nrNavigationEvent.chosenView = ADS_DOUBLECLICK;
            NextRadioApplication.postToBus(this, nrNavigationEvent);
            NextRadioSDKWrapperProvider.setDataModeReceived();
        }
    }

    private void setMenu(String activityText) {
        setMenuText(activityText);
        if (this.sliding_layout.isExpanded()) {
            this.mMainMenu.findItem(2131689838).setVisible(true);
            this.mMainMenu.findItem(2131689839).setVisible(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(2131623938, menu);
        this.mMainMenu = menu;
        String activityText = Stomp.EMPTY;
        if (this.mChosenActivity != ADS_UNINITIALIZED) {
            switch (this.mChosenActivity) {
                case Tokenizer.EOF /*0*/:
                    activityText = getString(2131165409);
                    break;
                case ADS_DOUBLECLICK /*1*/:
                    activityText = getString(2131165408);
                    break;
                case ADS_PINSIGHT /*2*/:
                    activityText = getString(2131165406);
                    break;
                case ADS_ATT /*3*/:
                    activityText = getString(2131165405);
                    break;
                case Type.MF /*4*/:
                    activityText = getResources().getString(2131165403);
                    break;
                case Service.ECHO /*7*/:
                    activityText = getString(2131165404);
                    break;
                case Protocol.EGP /*8*/:
                    activityText = getString(2131165407);
                    break;
                case Service.DISCARD /*9*/:
                    activityText = getString(2131165412);
                    break;
            }
        }
        setMenu(activityText);
        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuText(String menuText) {
        ((TextView) ((NowPlayingTextView) MenuItemCompat.getActionView(this.mMainMenu.findItem(2131689839))).findViewById(2131689675)).setText(menuText);
    }

    private void registerHeadPhoneStateReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        this.receiver = new HeadsetStateReceiver();
        registerReceiver(this.receiver, intentFilter);
    }

    private void unRegisterHeadPhoneStateReceiver() {
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
        }
    }

    public void displayFragment(int fragmentID) {
        Log.d(TAG, "displayFragment " + fragmentID);
        Bundle bundle;
        if (fragmentID == 0) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "My Favorites");
            trackScreenToGoogleAnalytics("My Favorites");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new StationsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_ATT);
            this.mCurrFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = 0;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165409));
            }
        } else if (fragmentID == ADS_DOUBLECLICK) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Live Guide");
            trackScreenToGoogleAnalytics("Live Guide");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new StationsGridFragment_();
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = ADS_DOUBLECLICK;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165408));
            }
        } else if (fragmentID == ADS_PINSIGHT) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Genre List");
            trackScreenToGoogleAnalytics("Genre List");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new StationsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_DOUBLECLICK);
            this.mCurrFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = ADS_PINSIGHT;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165406));
            }
        } else if (fragmentID == ADS_ATT) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Frequency List");
            trackScreenToGoogleAnalytics("Frequency List");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new StationsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_PINSIGHT);
            this.mCurrFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = ADS_ATT;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165405));
            }
        } else if (fragmentID == 6) {
            new LocationDialogFragment_().show(getFragmentManager().beginTransaction(), "locationDialog");
        } else if (fragmentID == 7) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "My Likes");
            trackScreenToGoogleAnalytics("Bookmarks");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new EventsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_DOUBLECLICK);
            this.mCurrFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = 7;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165404));
            }
        } else if (fragmentID == 8) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "History");
            trackScreenToGoogleAnalytics("History");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new EventsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_PINSIGHT);
            this.mCurrFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = 8;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165407));
            }
        } else if (fragmentID == 9) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Recently Played");
            trackScreenToGoogleAnalytics("Recently Played");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new RecentlyPlayedListFragment_();
            this.mCurrFragment.setArguments(new Bundle());
            getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
            this.mChosenActivity = 9;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165412));
            }
        } else if (fragmentID == 4) {
            showManualTuner();
        } else if (fragmentID == 5) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Settings");
            trackScreenToGoogleAnalytics("Settings");
            startActivity(new Intent(this, PrefActivity.class));
        }
        this.sliding_layout.collapsePane();
    }

    public void showManualTunerFromDialog() {
        this.sliding_layout.collapsePane();
        this.drawer_layout.closeDrawer((int) ADS_ATT);
        this.mNavigationDrawerFragment.preSelectItem(4);
        this.bShowManualTuner = true;
        showManualTuner();
    }

    private void showManualTuner() {
        if (this.bShowManualTuner) {
            getSharedPreferences("locationDialogPrompt", 0).edit().putLong("locationDialogPromptTimeMillis", System.currentTimeMillis()).commit();
        }
        MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Basic Tuner");
        trackScreenToGoogleAnalytics("Basic Tuner");
        this.mCurrFragmentID = 4;
        saveFragmentAsLastViewed(4);
        this.mCurrFragment = new ManualTuner_();
        getFragmentManager().beginTransaction().replace(2131689524, this.mCurrFragment).commit();
        this.mChosenActivity = 4;
        if (this.mMainMenu != null) {
            setMenuText(getString(2131165403));
        }
    }

    private void trackScreenToGoogleAnalytics(String s) {
        ((NextRadioApplication) getApplication()).trackScreen(s);
    }

    private void saveFragmentAsLastViewed(int fragmentID) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(PREF_DISPLAYED_FRAGMENT, fragmentID).apply();
    }

    public void onDestroy() {
        destroyAdBanner();
        NextRadioSDKWrapperProvider.getInstance().setLocationUpdateInterval(false);
        LocationReceiver.getInstance().cancelAlarm(this);
        unRegisterHeadPhoneStateReceiver();
        MixPanelHelper.getInstance(this).flush();
        super.onDestroy();
    }

    private void destroyAdBanner() {
        if (this.mAdview != null) {
            getHandler().removeCallbacks(this.mPinsightKillerRunnable);
            this.mAdview.removeAllViews();
            Log.d("StationsActivityAdview", "adview.destroy()");
            this.mAdview.destroy();
            this.adviewHolder.removeAllViews();
            this.mAdview = null;
        }
        if (this.mPublisherAdview != null) {
            getHandler().removeCallbacks(this.mPinsightKillerRunnable);
            this.mPublisherAdview.removeAllViews();
            Log.d("StationsActivityDCAds", "DCAdview.destroy()");
            this.mPublisherAdview.destroy();
            this.adviewHolder.removeAllViews();
            this.mPublisherAdview = null;
        }
    }

    private synchronized void enableAdBanner() {
        if (this.mDoubleClick == ADS_UNINITIALIZED) {
            Log.d(TAG, "Banner Unitialzied");
        } else if (this.mDoubleClick == ADS_DOUBLECLICK || this.mDoubleClick == ADS_ATT) {
            if (this.mPublisherAdview == null && !this.mNoDataMode) {
                Log.d(TAG, "Double Click ads initialized");
                String s = "no Ad Group set";
                if (this.mDeviceRegistrationInfo == null) {
                    s = "mDeviceReg Info is null";
                } else if (this.mDeviceRegistrationInfo.adGroup == null || this.mDeviceRegistrationInfo.adGroup.length() < ADS_DOUBLECLICK) {
                    s = "AdGroup string is null";
                } else {
                    s = this.mDeviceRegistrationInfo.adGroup;
                }
                Log.d(TAG, "Ad Group: " + s);
                this.mPublisherAdview = new PublisherAdView(this);
                this.mPublisherAdview.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                PublisherAdView publisherAdView = this.mPublisherAdview;
                AdSize[] adSizeArr = new AdSize[ADS_DOUBLECLICK];
                adSizeArr[0] = AdSize.BANNER;
                publisherAdView.setAdSizes(adSizeArr);
                this.mPublisherAdview.setAdUnitId(DOUBLECLICK_ID + this.mDeviceRegistrationInfo.adGroup);
                params = new LayoutParams(ADS_UNINITIALIZED, -2);
                params.addRule(13);
                this.adviewHolder.addView(this.mPublisherAdview, params);
                PublisherAdRequest adRequest = new Builder().build();
                try {
                    if (Process.myUid() == ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) {
                        Log.d(TAG, "don't display adds");
                    } else {
                        Log.d(TAG, "double click adRequest");
                        this.mPublisherAdview.loadAd(adRequest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getHandler().removeCallbacks(this.mPinsightKillerRunnable);
                getHandler().postDelayed(this.mPinsightKillerRunnable, 300000);
            }
        } else if (this.mAdview == null && !this.mNoDataMode) {
            try {
                if (Process.myUid() == ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) {
                    Log.d(TAG, "don't display adds");
                } else {
                    this.mAdview = new AdView(this);
                    this.mAdview.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    Log.d(TAG, "Pinsight ads initialized");
                    Log.d("StationsActivityAdview", "AdView.setProductInfo(this, PINSIGHT_PRODUCT_ID);");
                    AdView.setProductInfo(getApplicationContext(), PINSIGHT_PRODUCT_ID);
                    AdView.startActivity(this);
                    Log.d("StationsActivityAdview", "adview.setPlacementId(null)");
                    this.mAdview.setPlacementId(null);
                    params = new LayoutParams(ADS_UNINITIALIZED, -2);
                    params.addRule(13);
                    this.adviewHolder.addView(this.mAdview, params);
                    Log.d("StationsActivityAdview", "adview.resume()");
                    this.mAdview.resume();
                    getHandler().removeCallbacks(this.mPinsightKillerRunnable);
                    getHandler().postDelayed(this.mPinsightKillerRunnable, 300000);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @AfterViews
    public void afterViews() {
        this.mNavigationDrawerFragment.setUp(2131689520, (DrawerLayout) findViewById(2131689511));
        if (this.bShowManualTuner) {
            this.mNavigationDrawerFragment.preSelectItem(4);
        }
        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
        }
        this.sliding_layout.setDragView(findViewById(2131689596));
        this.mDrawerToggle = new C11902(this, this.drawer_layout, 2130837668, 2131165411, 2131165410);
        this.sliding_layout.setPanelSlideListener(new C11913());
        int fragmentIDToDisplay = PreferenceManager.getDefaultSharedPreferences(this).getInt(PREF_DISPLAYED_FRAGMENT, ADS_DOUBLECLICK);
        if (this.bShowManualTuner) {
            fragmentIDToDisplay = 4;
            this.sliding_layout.collapsePane();
            this.drawer_layout.closeDrawer((int) ADS_ATT);
        }
        this.mCurrFragmentID = fragmentIDToDisplay;
        displayFragment(fragmentIDToDisplay);
    }

    protected void onStart() {
        super.onStart();
        Log.d("StationsActivityAdview", "AdView.startActivity(this)");
        AdView.startActivity(this);
        NextRadioApplication.registerWithBus(this);
    }

    protected void onStop() {
        super.onStop();
        Log.d("StationsActivityAdview", "AdView.stopActivity(this)");
        AdView.stopActivity(this);
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onBackPressed() {
        if (this.drawer_layout.isDrawerOpen((int) ADS_ATT)) {
            this.drawer_layout.closeDrawer((int) ADS_ATT);
        } else if (this.sliding_layout.isExpanded()) {
            this.sliding_layout.collapsePane();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe
    public void onNRRadioAction(NRRadioAction tune) {
        if (tune.shouldResumeNowPlaying && !this.mNoDataMode) {
            this.sliding_layout.expandPane();
        }
    }

    @UiThread
    @Subscribe
    public void renderDialogFragment(NRDialogEvent dialogEvent) {
        MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Card");
        EventDialogFragment.getInstance(dialogEvent).show(getFragmentManager(), DataLayer.EVENT_KEY);
    }

    @UiThread
    @Subscribe
    public void onStationsAutoUpdating(NRStationList stationListEvent) {
        if (stationListEvent == null) {
            Log.d(TAG, "+  stationListEvent null");
        } else if (stationListEvent.errorCode == 4) {
            Log.d(TAG, "+  stationListEvent.errorCode " + stationListEvent.errorCode);
        } else {
            if (stationListEvent.errorCode != 0) {
                Log.d(TAG, "+  stationListEvent.errorCode " + stationListEvent.errorCode);
                Log.d(TAG, "+  onStationsAutoUpdating stationListEvent.failedToGetLocation");
                long currentTime = System.currentTimeMillis();
                long lastTimeAsked = getSharedPreferences("locationDialogPrompt", 0).getLong("locationDialogPromptTimeMillis", 0);
                if (!stationListEvent.wasForced && currentTime - lastTimeAsked < 82800000) {
                    Log.d(TAG, "+  skip automatically displaying location dialog, hasn't been 24 hours since last time it was shown");
                    return;
                } else if (!AcquireLocationActivity.noStationAvailable) {
                    if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                        NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
                    }
                    getSharedPreferences("locationDialogPrompt", 0).edit().putLong("locationDialogPromptTimeMillis", System.currentTimeMillis()).commit();
                    LocationDialogFragment test = (LocationDialogFragment) getFragmentManager().findFragmentByTag("locationDialog");
                    if (test != null && test.isVisible()) {
                        Log.d(TAG, "+- dialog already showing, doing nothing.");
                    } else if (!NextRadioSDKWrapperProvider.getInstance().getNoDataMode()) {
                        Log.d(TAG, "+- show location dialog.");
                        LocationDialogFragment locationDialog = new LocationDialogFragment_();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("DoRefresh", false);
                        locationDialog.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (!(isFinishing() || this.bShowManualTuner)) {
                            try {
                                locationDialog.show(ft, "locationDialog");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (stationListEvent.stationList != null && stationListEvent.stationList.size() > 0) {
                Collections.sort(stationListEvent.stationList, new C11924());
                String CSVString = Stomp.EMPTY;
                Iterator i$ = stationListEvent.stationList.iterator();
                while (i$.hasNext()) {
                    StationInfo station = (StationInfo) i$.next();
                    if (station.isFavorited) {
                        CSVString = CSVString + station.frequencyHz + Stomp.COMMA;
                    }
                }
                if (CSVString.length() > 0) {
                    CSVString = CSVString.substring(0, CSVString.length() + ADS_UNINITIALIZED);
                }
                Log.d(TAG, "widget CSV:" + CSVString);
                getSharedPreferences(RadioAdapterService.PREFS, 4).edit().putString("csvFavoriteStations", CSVString).commit();
            }
        }
    }

    @UiThread
    @Subscribe
    public void headsetDisconnected(NRRadioAvailabilityEvent radioAvailabilityEvent) {
        HeadsetHelper.getInstance().displayHeadPhoneWarning(radioAvailabilityEvent.status, this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerHeadPhoneStateReceiver();
        this.bShowManualTuner = getIntent().getBooleanExtra(SHOW_MANUAL_TUNER, false);
        doSDKInitInBackground();
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        AppRater.app_launched(this);
        AppRater.showRateDialog(this, getSharedPreferences("apprater", 0).edit());
        if (NextRadioSDKWrapperProvider.getInstance().hasInitialized()) {
            setDeviceRegistrationInfo();
        }
    }

    private void setDeviceRegistrationInfo() {
        this.mDeviceRegistrationInfo = NextRadioSDKWrapperProvider.getInstance().getDeviceRegistrationInfo();
        if (this.mDeviceRegistrationInfo.adGroup.equals("Default")) {
            this.mDoubleClick = ADS_DOUBLECLICK;
        } else if (this.mDeviceRegistrationInfo.adGroup.equals("ATT")) {
            this.mDoubleClick = ADS_ATT;
        } else {
            this.mDoubleClick = ADS_PINSIGHT;
        }
    }

    @Background
    protected void doSDKInitInBackground() {
        ((NextRadioApplication) getApplicationContext()).initSDK();
    }

    @UiThread
    @Subscribe
    public void onInitCompleted(NRInitCompleted action) {
        if (action.statusCode == NRInitCompleted.STATUS_CODE_SUCCESS) {
            setDeviceRegistrationInfo();
            enableAdBanner();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 82) {
            return super.onKeyUp(keyCode, event);
        }
        if (this.drawer_layout.isDrawerOpen((int) ADS_ATT)) {
            this.drawer_layout.closeDrawer((int) ADS_ATT);
        } else {
            this.drawer_layout.openDrawer((int) ADS_ATT);
        }
        return true;
    }

    protected void onPause() {
        super.onPause();
        Log.d("StationsActivityAdview", "adview.pause()");
        destroyAdBanner();
        if (!isRadioPowerOn()) {
            NextRadioSDKWrapperProvider.getInstance().setLocationUpdateInterval(false);
        }
    }

    protected void onResume() {
        super.onResume();
        RadioAdapterService.isPermissionRevoked = false;
        if (AcquireLocationActivity.noStationAvailable) {
            this.mChosenActivity = 4;
            displayFragment(this.mChosenActivity);
        } else {
            getSharedPreferences(RadioAdapterService.PREFS, 4).edit().putBoolean("useFavoritesForSeek", PreferenceManager.getDefaultSharedPreferences(this).getBoolean("useFavoritesForSeek", false)).apply();
            checkRegister();
            displayTutorial();
            NextRadioSDKWrapperProvider.getInstance().setLocationUpdateInterval(true);
        }
        if (!(this.sliding_layout.isExpanded() || this.mNoDataMode)) {
            Log.d("StationsActivityAdview", "adview.resume()");
            enableAdBanner();
        }
        checkLanguageUpdate();
    }

    private void displayTutorial() {
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("didTutorial", false)) {
            startActivity(new Intent(this, NewTutorialActivity.class));
        }
        if (this.doNotRefreshIfPermissionDenied) {
            this.doNotRefreshIfPermissionDenied = false;
        } else {
            showLocation(this);
        }
    }

    @Background
    public void checkRegister() {
        NextRadioSDKWrapperProvider.getInstance().register((NextRadioApplication) getApplicationContext(), AdapterListing.getFMRadioImplementationString(this));
    }

    @OptionsItem({16908332})
    void onHomeMenuButtonPushed(MenuItem item) {
        this.mDrawerToggle.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        openShareEvent(intent);
    }

    private void openShareEvent(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "onNewIntent intent:NULL");
            return;
        }
        String campaignId = intent.getStringExtra("campaign_id");
        if (!(campaignId == null || campaignId.isEmpty())) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.CAMPAIGN_ID, campaignId, "campaign_open");
        }
        String action = intent.getAction();
        String eventUFID = intent.getStringExtra(impressionReporting.ufid);
        String what = intent.getStringExtra("what");
        if (action == null) {
            Log.d(TAG, "onNewIntent action:NULL");
        } else if (what != null && !what.equals("nowplaying")) {
            Log.d(TAG, "onNewIntent what:" + what + " ufid:" + eventUFID);
            NextRadioSDKWrapperProvider.getInstance().getEvent(eventUFID, new C11935(what));
        }
    }

    private void checkLanguageUpdate() {
        String currentLang = Locale.getDefault().getLanguage();
        if (getCurrentPhoneLanguageCode().length() == 0) {
            saveCurrentPhoneLanguageCode(currentLang);
        } else if (!currentLang.equals(getCurrentPhoneLanguageCode())) {
            saveCurrentPhoneLanguageCode(currentLang);
            NextRadioApplication.postToBus(this, new C11946());
        }
    }

    private void saveCurrentPhoneLanguageCode(String currentLanguage) {
        Editor editor = getPreferences(0).edit();
        editor.putString("localeLanguage", currentLanguage);
        editor.commit();
    }

    private String getCurrentPhoneLanguageCode() {
        return getPreferences(0).getString("localeLanguage", Stomp.EMPTY);
    }

    public void onNoStationFound() {
        this.mNavigationDrawerFragment.preSelectItem(4);
        displayFragment(4);
    }

    private boolean isRadioPowerOn() {
        if (RadioAdapterService.mRadioAdapter != null) {
            return RadioAdapterService.mRadioAdapter.getIsPoweredOn();
        }
        return false;
    }

    private void showLocation(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            try {
                if (DateUtils.getCurrentDate().equals(PermissionUtil.getLocationPermissionDeniedDate(this))) {
                    showStoragePermission(this);
                    return;
                } else {
                    requestLocationPermission(mActivity);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
        showStoragePermission(this);
    }

    private void requestLocationPermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.ACCESS_FINE_LOCATION")) {
            displayAlert(getResources().getString(2131165340), getResources().getString(2131165341), ADS_DOUBLECLICK);
        } else {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_LOCATION, ADS_DOUBLECLICK);
        }
    }

    private void showStoragePermission(Activity mActivity) {
        if (ContextCompat.checkSelfPermission(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            NextRadioApplication.getInstance().changeImageCache(false);
            try {
                if (!DateUtils.getCurrentDate().equals(PermissionUtil.getStoragePermissionStateDeniedDate(this))) {
                    requestStoragePermission(mActivity, getResources().getString(2131165344));
                    return;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        NextRadioApplication.getInstance().changeImageCache(true);
    }

    private void requestStoragePermission(Activity mActivity, String message) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            displayAlert(getResources().getString(2131165343), message, ADS_PINSIGHT);
        } else {
            ActivityCompat.requestPermissions(mActivity, PermissionUtil.PERMISSIONS_STORAGE, ADS_PINSIGHT);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ADS_DOUBLECLICK) {
            if (grantResults[0] == 0) {
                NextRadioSDKWrapperProvider.getInstance().resumeLocationUpdates();
                LocationReceiver.getInstance().scheduleAlarms(this);
                showStoragePermission(this);
                return;
            }
            this.doNotRefreshIfPermissionDenied = true;
            PermissionUtil.saveLocationPermissionState(this, true);
            Log.e(TAG, "Location doNotRefreshForPermissionDeny:" + this.doNotRefreshIfPermissionDenied);
            NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
            PermissionUtil.saveLocationPermissionDeniedDate(this, DateUtils.getCurrentDate());
            showStoragePermission(this);
        } else if (requestCode != ADS_PINSIGHT) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults[0] == 0) {
            NextRadioApplication.getInstance().changeImageCache(true);
            if (this.isSharePermissionRequested) {
                this.isSharePermissionRequested = false;
                NowPlayingBarFragment mDisplayFragment = (NowPlayingBarFragment) getFragmentManager().findFragmentById(2131689515);
                if (mDisplayFragment != null && mDisplayFragment.isInLayout()) {
                    mDisplayFragment.displayEventAction(true);
                    return;
                }
                return;
            }
            int fragmentIDToDisplay = PreferenceManager.getDefaultSharedPreferences(this).getInt(PREF_DISPLAYED_FRAGMENT, ADS_DOUBLECLICK);
            this.mCurrFragmentID = fragmentIDToDisplay;
            displayFragment(fragmentIDToDisplay);
        } else {
            PermissionUtil.saveStoragePermissionState(this, true);
            this.doNotRefreshIfPermissionDenied = true;
            NextRadioApplication.getInstance().changeImageCache(false);
            PermissionUtil.saveStoragePermissionDeniedDate(this, DateUtils.getCurrentDate());
        }
    }

    private void displayAlert(String title, String msg, int permissionType) {
        new AlertDialog.Builder(this, 2131427334).setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton(17039370, new C11968(permissionType)).setNegativeButton(17039369, new C11957(permissionType)).setIcon(17301543).show();
    }

    public void onPermissionRequired() {
        this.isSharePermissionRequested = true;
        requestStoragePermission(this, getResources().getString(2131165345));
    }
}
