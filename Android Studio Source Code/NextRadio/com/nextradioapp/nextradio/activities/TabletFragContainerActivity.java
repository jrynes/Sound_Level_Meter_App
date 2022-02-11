package com.nextradioapp.nextradio.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest.Builder;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.tagmanager.DataLayer;
import com.nextradioapp.androidSDK.data.schema.Tables.impressionReporting;
import com.nextradioapp.androidSDK.utils.AppRater;
import com.nextradioapp.core.interfaces.IEventReceivedListener;
import com.nextradioapp.core.objects.ActionPayload;
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
import com.nextradioapp.nextradio.fragments.NestedPreferencesFragment;
import com.nextradioapp.nextradio.fragments.NowPlayingBarFragment;
import com.nextradioapp.nextradio.fragments.NowPlayingBarFragment.OnStorageRequestPermission;
import com.nextradioapp.nextradio.fragments.PrefFragmentTablet.Callback;
import com.nextradioapp.nextradio.fragments.PrefFragmentTablet_;
import com.nextradioapp.nextradio.fragments.RecentlyPlayedListFragment_;
import com.nextradioapp.nextradio.fragments.StationsGridFragment_;
import com.nextradioapp.nextradio.fragments.StationsListFragment_;
import com.nextradioapp.nextradio.mixpanel.MipEvents;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDialogEvent;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.nextradioapp.nextradio.receivers.LocationReceiver;
import com.nextradioapp.nextradio.services.HeadsetStateReceiver;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.nextradioapp.radioadapters.AdapterListing;
import com.nextradioapp.utils.DateUtils;
import com.nextradioapp.utils.HeadsetHelper;
import com.nextradioapp.utils.ImageLoadingWrapper;
import com.nextradioapp.utils.PermissionUtil;
import com.nextradioapp.utils.ToasterStrudle;
import com.onelouder.adlib.AdView;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
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

@EActivity(2130903042)
public class TabletFragContainerActivity extends FragmentActivity implements OnRequestPermissionsResultCallback, OnNoStationAvailListener, Callback, OnStorageRequestPermission {
    private static final int ADS_ATT = 3;
    private static final int ADS_DOUBLECLICK = 1;
    private static final int ADS_PINSIGHT = 2;
    private static final int ADS_UNINITIALIZED = -1;
    private static String DOUBLECLICK_ID = null;
    private static String PINSIGHT_PRODUCT_ID = null;
    public static final String PREF_DISPLAYED_FRAGMENT = "PREF_DISPLAYED_FRAGMENT";
    public static final String SHOW_MANUAL_TUNER = "manualTuner";
    public static final String TAG = "TabletActivity";
    private static final String TAG_NESTED = "TAG_NESTED";
    @ViewById
    public FrameLayout adviewHolder;
    private boolean bShowManualTuner;
    @ViewById
    public Button buttonEnhancedMode;
    private boolean doNotRefreshForPermissionDeny;
    @ViewById
    public DrawerLayout drawer_layout;
    @ViewById
    public FrameLayout fragment_container_guide;
    @ViewById
    public FrameLayout fragment_container_guide_no_data;
    private ImageView imageBtnPause;
    private ImageView imageBtnPlay;
    private ImageView imageButtonHeartOff;
    private ImageView imageButtonHeartOn;
    private ImageView imageViewSmall;
    private boolean isSharePermissionRequested;
    @ViewById
    public LinearLayout layout_with_data_mode;
    @ViewById
    public LinearLayout layout_with_no_data_mode;
    public AdView mAdview;
    private int mChosenActivity;
    public Fragment mCurrFragment;
    private int mCurrFragmentID;
    private Runnable mCurrRunnable;
    private NextRadioEventInfo mCurrentEventInfo;
    private DeviceRegistrationInfo mDeviceRegistrationInfo;
    private int mDoubleClick;
    private ActionBarDrawerToggle mDrawerToggle;
    private Handler mHandler;
    private String mLastUFID;
    Menu mMainMenu;
    @FragmentById(2131689520)
    public NavigationDrawerFragment mNavigationDrawerFragment;
    private boolean mNoDataMode;
    private Runnable mPinsightKillerRunnable;
    private PublisherAdView mPublisherAdview;
    private TextView menu_item_textView;
    private View nav_view;
    private ProgressBar progressBar;
    private HeadsetStateReceiver receiver;
    private TextView textViewSmallEvent;
    private TextView textViewSmallStation;
    private TextView textViewSmallStationPart2;
    ViewAnimator viewAnimatorNowPlaying;

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.1 */
    class C12051 implements Runnable {
        C12051() {
        }

        public void run() {
            Log.d("TabletActivityAdview", "triggering adview killer");
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.2 */
    class C12062 extends ActionBarDrawerToggle {
        C12062(Activity x0, DrawerLayout x1, int x2, int x3, int x4) {
            super(x0, x1, x2, x3, x4);
        }

        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
        }

        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.3 */
    class C12073 implements OnClickListener {
        C12073() {
        }

        public void onClick(View v) {
            if (TabletFragContainerActivity.this.drawer_layout.isDrawerOpen(TabletFragContainerActivity.this.nav_view)) {
                TabletFragContainerActivity.this.drawer_layout.closeDrawer(TabletFragContainerActivity.this.nav_view);
            } else {
                TabletFragContainerActivity.this.drawer_layout.openDrawer(TabletFragContainerActivity.this.nav_view);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.4 */
    class C12084 implements Comparator<StationInfo> {
        C12084() {
        }

        public int compare(StationInfo arg0, StationInfo arg1) {
            if (null != null) {
                return 0;
            }
            if (arg0.frequencyHz > arg1.frequencyHz) {
                return TabletFragContainerActivity.ADS_DOUBLECLICK;
            }
            return TabletFragContainerActivity.ADS_UNINITIALIZED;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.5 */
    class C12095 implements IEventReceivedListener {
        final /* synthetic */ String val$what;

        C12095(String str) {
            this.val$what = str;
        }

        public void onEventReceived(NextRadioEventInfo eventInfo) {
            ArrayList<EventAction> actions = NextRadioSDKWrapperProvider.getInstance().getEventActions(eventInfo);
            if (actions == null) {
                return;
            }
            if (this.val$what.equals("share")) {
                NextRadioApplication.mCurrentActivity = TabletFragContainerActivity.this;
                NowPlayingBarFragment mDisplayFragment = (NowPlayingBarFragment) TabletFragContainerActivity.this.getFragmentManager().findFragmentById(2131689515);
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
                        Log.d(TabletFragContainerActivity.TAG, "starting action...");
                        eventAction.start(4);
                        RadioAdapterService.isBuyThisSongSelected = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.6 */
    class C12106 extends NRNavigationEvent {
        C12106() {
            this.chosenView = 6;
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.7 */
    class C12117 implements DialogInterface.OnClickListener {
        final /* synthetic */ int val$permissionType;

        C12117(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.val$permissionType == TabletFragContainerActivity.ADS_DOUBLECLICK) {
                NextRadioSDKWrapperProvider.getInstance().shutdownLocationServices();
                PermissionUtil.saveLocationPermissionDeniedDate(TabletFragContainerActivity.this, DateUtils.getCurrentDate());
            } else if (this.val$permissionType == TabletFragContainerActivity.ADS_PINSIGHT) {
                NextRadioApplication.getInstance().changeImageCache(false);
                PermissionUtil.saveStoragePermissionDeniedDate(TabletFragContainerActivity.this, DateUtils.getCurrentDate());
            }
            dialog.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.8 */
    class C12128 implements DialogInterface.OnClickListener {
        final /* synthetic */ int val$permissionType;

        C12128(int i) {
            this.val$permissionType = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            TabletFragContainerActivity.this.doNotRefreshForPermissionDeny = true;
            if (this.val$permissionType == TabletFragContainerActivity.ADS_DOUBLECLICK) {
                ActivityCompat.requestPermissions(TabletFragContainerActivity.this, PermissionUtil.PERMISSIONS_LOCATION, TabletFragContainerActivity.ADS_DOUBLECLICK);
            } else if (this.val$permissionType == TabletFragContainerActivity.ADS_PINSIGHT) {
                ActivityCompat.requestPermissions(TabletFragContainerActivity.this, PermissionUtil.PERMISSIONS_STORAGE, TabletFragContainerActivity.ADS_PINSIGHT);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity.9 */
    class C12139 implements Runnable {
        final /* synthetic */ NextRadioEventInfo val$event;

        C12139(NextRadioEventInfo nextRadioEventInfo) {
            this.val$event = nextRadioEventInfo;
        }

        public void run() {
            Animation inAnim = AnimationUtils.loadAnimation(TabletFragContainerActivity.this, 17432576);
            Animation outAnim = AnimationUtils.loadAnimation(TabletFragContainerActivity.this, 17432577);
            TabletFragContainerActivity.this.viewAnimatorNowPlaying.setInAnimation(inAnim);
            TabletFragContainerActivity.this.viewAnimatorNowPlaying.setOutAnimation(outAnim);
            TabletFragContainerActivity.this.textViewSmallStation.setText(this.val$event.stationInfo.frequencyAndCallLetters());
            if (this.val$event.stationInfo.slogan == null || this.val$event.stationInfo.slogan.length() == 0 || this.val$event.stationInfo.getStationType() == TabletFragContainerActivity.ADS_DOUBLECLICK) {
                TabletFragContainerActivity.this.textViewSmallStationPart2.setVisibility(8);
            } else {
                TabletFragContainerActivity.this.textViewSmallStationPart2.setVisibility(0);
                TabletFragContainerActivity.this.textViewSmallStationPart2.setText(" - " + this.val$event.stationInfo.slogan);
            }
            TabletFragContainerActivity.this.textViewSmallEvent.setVisibility(0);
            if (!this.val$event.getUILine1().equals(Stomp.EMPTY) && !this.val$event.getUILine2().equals(Stomp.EMPTY)) {
                TabletFragContainerActivity.this.textViewSmallEvent.setText(this.val$event.getUILine1() + " - " + this.val$event.getUILine2());
            } else if (this.val$event.getUILine1().equals(Stomp.EMPTY) || this.val$event.stationInfo.getStationType() == TabletFragContainerActivity.ADS_DOUBLECLICK) {
                TabletFragContainerActivity.this.textViewSmallEvent.setVisibility(8);
            } else {
                TabletFragContainerActivity.this.textViewSmallEvent.setText(this.val$event.getUILine1());
            }
            TabletFragContainerActivity.this.displayFavoriteButton(this.val$event.stationInfo.isFavorited);
            String stationImageURLLowRes = this.val$event.stationInfo.imageURL;
            ActionPayload payload = new ActionPayload(this.val$event.trackingID, this.val$event.teID, Stomp.EMPTY, this.val$event.UFIDIdentifier, this.val$event.stationInfo.publicStationID);
            new ImageLoadingWrapper(TabletFragContainerActivity.this.imageViewSmall, this.val$event.trackingID, this.val$event.stationInfo.publicStationID, TabletFragContainerActivity.ADS_DOUBLECLICK, TabletFragContainerActivity.ADS_DOUBLECLICK, this.val$event.getUILine1() + Stomp.EMPTY, payload, false).addImageURL(stationImageURLLowRes).onFailed(new 1(this)).display();
            TabletFragContainerActivity.this.viewAnimatorNowPlaying.showNext();
        }
    }

    private class SubButtonOnClickListener implements OnClickListener {
        private SubButtonOnClickListener() {
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case 2131689602:
                    if (TabletFragContainerActivity.this.mCurrentEventInfo != null && TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo != null) {
                        NextRadioSDKWrapperProvider.getInstance().setStationFavoriteStatus(TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.frequencyHz, TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.frequencySubChannel, TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.getStationType(), false);
                        TabletFragContainerActivity.this.displayFavoriteButton(false);
                        ToasterStrudle.makeText(TabletFragContainerActivity.this, TabletFragContainerActivity.this.getResources().getString(2131165395), 0);
                    }
                case 2131689603:
                    if (TabletFragContainerActivity.this.mCurrentEventInfo != null && TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo != null) {
                        MixPanelHelper.getInstance(TabletFragContainerActivity.this).recordMIPEvent(MipProperties.STATION_NAME, TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.frequencyAndCallLetters(), MipEvents.FAVORITED_STATION);
                        NextRadioSDKWrapperProvider.getInstance().setStationFavoriteStatus(TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.frequencyHz, TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.frequencySubChannel, TabletFragContainerActivity.this.mCurrentEventInfo.stationInfo.getStationType(), true);
                        TabletFragContainerActivity.this.displayFavoriteButton(true);
                        ToasterStrudle.makeText(TabletFragContainerActivity.this, TabletFragContainerActivity.this.getResources().getString(2131165247), 0);
                    }
                case 2131689604:
                    NextRadioApplication.postToBus(this, new 1(this));
                case 2131689605:
                    NextRadioApplication.postToBus(this, new 2(this));
                default:
            }
        }
    }

    public TabletFragContainerActivity() {
        this.mChosenActivity = ADS_UNINITIALIZED;
        this.mCurrFragmentID = 0;
        this.mDoubleClick = ADS_UNINITIALIZED;
        this.mNoDataMode = false;
        this.bShowManualTuner = false;
        this.mPinsightKillerRunnable = new C12051();
        this.mMainMenu = null;
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
            return;
        }
        displayFragment(event.chosenView);
    }

    @UiThread
    @Subscribe
    public void onNRDataMode(NRDataMode nrDataMode) {
        this.mNoDataMode = nrDataMode.mIsDataModeOff;
        NRNavigationEvent nrNavigationEvent;
        if (nrDataMode.mIsDataModeOff) {
            destroyAdBanner();
            this.layout_with_data_mode.setVisibility(8);
            this.layout_with_no_data_mode.setVisibility(0);
            if (nrDataMode.mIsFreshRequest) {
                nrNavigationEvent = new NRNavigationEvent();
                nrNavigationEvent.chosenView = 4;
                NextRadioApplication.postToBus(this, nrNavigationEvent);
                NextRadioSDKWrapperProvider.setDataModeReceived();
            }
        } else if (nrDataMode.mIsFreshRequest) {
            enableAdBanner();
            this.layout_with_no_data_mode.setVisibility(8);
            this.layout_with_data_mode.setVisibility(0);
            nrNavigationEvent = new NRNavigationEvent();
            nrNavigationEvent.chosenView = ADS_DOUBLECLICK;
            NextRadioApplication.postToBus(this, nrNavigationEvent);
            NextRadioSDKWrapperProvider.setDataModeReceived();
        }
        if (this.mNoDataMode) {
            displayFragment(4);
        }
    }

    @Click({2131689518})
    void restoreFULLPOWER() {
        NextRadioSDKWrapperProvider.setNoDataMode(false, this);
        startActivity(new Intent(this, AcquireLocationActivity_.class));
        finish();
    }

    private void setMenu(String activityText) {
        setMenuText(activityText);
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
        if (this.menu_item_textView != null) {
            this.menu_item_textView.setText(menuText);
        }
    }

    public void displayFragment(int fragmentID) {
        Log.d(TAG, "displayFragment " + fragmentID);
        Bundle bundle;
        FragmentTransaction ft;
        if (fragmentID == 0) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "My Favorites");
            trackScreenToGoogleAnalytics("My Favorites");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new StationsListFragment_();
            bundle = new Bundle();
            bundle.putInt("FRAGMENT_TYPE", ADS_ATT);
            this.mCurrFragment.setArguments(bundle);
            ft = getFragmentManager().beginTransaction();
            if (this.mNoDataMode) {
                enableContainer(0);
                ft.replace(2131689519, this.mCurrFragment).commit();
            } else {
                enableContainer(ADS_DOUBLECLICK);
                ft.replace(2131689513, this.mCurrFragment).commit();
            }
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
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
            getFragmentManager().beginTransaction().replace(2131689513, this.mCurrFragment).commit();
            this.mChosenActivity = 9;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165412));
            }
        } else if (fragmentID == 4) {
            showManualTuner();
        } else if (fragmentID == 5) {
            MixPanelHelper.getInstance(this).recordMIPEvent(MipProperties.VIEW, "Settings");
            trackScreenToGoogleAnalytics("Settings");
            this.mCurrFragmentID = fragmentID;
            saveFragmentAsLastViewed(fragmentID);
            this.mCurrFragment = new PrefFragmentTablet_();
            ft = getFragmentManager().beginTransaction();
            if (this.mNoDataMode) {
                enableContainer(0);
                ft.replace(2131689519, this.mCurrFragment).commit();
            } else {
                enableContainer(ADS_DOUBLECLICK);
                ft.replace(2131689513, this.mCurrFragment).commit();
            }
            this.mChosenActivity = 5;
            if (this.mMainMenu != null) {
                setMenuText(getString(2131165228));
            }
        }
    }

    public void onNestedPreferenceSelected(int key) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_NESTED);
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        if (this.mNoDataMode) {
            enableContainer(0);
            ft.replace(2131689519, NestedPreferencesFragment.newInstance(key), TAG_NESTED).addToBackStack(TAG_NESTED).commit();
            return;
        }
        enableContainer(ADS_DOUBLECLICK);
        ft.replace(2131689513, NestedPreferencesFragment.newInstance(key), TAG_NESTED).addToBackStack(TAG_NESTED).commit();
    }

    private void enableContainer(int val) {
        if (val == 0) {
            this.fragment_container_guide.setVisibility(8);
            this.fragment_container_guide_no_data.setVisibility(0);
            return;
        }
        this.fragment_container_guide_no_data.setVisibility(8);
        this.fragment_container_guide.setVisibility(0);
    }

    public void showManualTunerFromDialog() {
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (this.mNoDataMode) {
            ft.replace(2131689519, this.mCurrFragment).commit();
        } else {
            ft.replace(2131689513, this.mCurrFragment).commit();
        }
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
            Log.d("TabletActivityAdview", "adview.destroy()");
            this.mAdview.destroy();
            this.adviewHolder.removeAllViews();
            this.mAdview = null;
        }
        if (this.mPublisherAdview != null) {
            getHandler().removeCallbacks(this.mPinsightKillerRunnable);
            this.mPublisherAdview.removeAllViews();
            Log.d("TabletActivityDCAds", "DCAdview.destroy()");
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
                        this.adviewHolder.setVisibility(8);
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
                    this.adviewHolder.setVisibility(8);
                } else {
                    this.mAdview = new AdView(this);
                    this.mAdview.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    Log.d(TAG, "Pinsight ads initialized");
                    Log.d("TabletActivityAdview", "AdView.setProductInfo(this, PINSIGHT_PRODUCT_ID);");
                    AdView.setProductInfo(getApplicationContext(), PINSIGHT_PRODUCT_ID);
                    AdView.startActivity(this);
                    Log.d("TabletActivityAdview", "adview.setPlacementId(null)");
                    this.mAdview.setPlacementId(null);
                    params = new LayoutParams(ADS_UNINITIALIZED, -2);
                    params.addRule(13);
                    this.adviewHolder.addView(this.mAdview, params);
                    Log.d("TabletActivityAdview", "adview.resume()");
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
        this.mHandler = new Handler();
        this.mNavigationDrawerFragment.setUp(2131689520, this.drawer_layout);
        if (this.bShowManualTuner) {
            this.mNavigationDrawerFragment.preSelectItem(4);
        }
        setCustomNavigationActionBar();
        this.mDrawerToggle = new C12062(this, this.drawer_layout, 2130837668, 2131165411, 2131165410);
        int fragmentIDToDisplay = PreferenceManager.getDefaultSharedPreferences(this).getInt(PREF_DISPLAYED_FRAGMENT, ADS_DOUBLECLICK);
        if (this.bShowManualTuner) {
            fragmentIDToDisplay = 4;
            this.drawer_layout.closeDrawer((int) ADS_ATT);
        }
        this.mCurrFragmentID = fragmentIDToDisplay;
        displayFragment(fragmentIDToDisplay);
        toggleNavDrawer();
    }

    private void setCustomNavigationActionBar() {
        StationsActivity.stopLiveGuideVisualRecording = false;
        this.nav_view = findViewById(2131689520);
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(2130903067);
        actionBar.setDisplayOptions(16);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        View customActionBarView = actionBar.getCustomView();
        this.drawer_layout.setDrawerListener(this.mDrawerToggle);
        this.menu_item_textView = (TextView) customActionBarView.findViewById(2131689594);
        this.menu_item_textView.setSelected(true);
        this.textViewSmallStation = (TextView) customActionBarView.findViewById(2131689598);
        this.textViewSmallStationPart2 = (TextView) customActionBarView.findViewById(2131689599);
        this.textViewSmallEvent = (TextView) customActionBarView.findViewById(2131689600);
        this.imageViewSmall = (ImageView) customActionBarView.findViewById(2131689597);
        this.progressBar = (ProgressBar) customActionBarView.findViewById(2131689492);
        this.viewAnimatorNowPlaying = (ViewAnimator) customActionBarView.findViewById(2131689595);
        this.imageBtnPlay = (ImageView) customActionBarView.findViewById(2131689605);
        this.imageBtnPause = (ImageView) customActionBarView.findViewById(2131689604);
        this.imageButtonHeartOff = (ImageView) customActionBarView.findViewById(2131689603);
        this.imageButtonHeartOn = (ImageView) customActionBarView.findViewById(2131689602);
        this.imageBtnPlay.setOnClickListener(new SubButtonOnClickListener());
        this.imageBtnPause.setOnClickListener(new SubButtonOnClickListener());
        this.imageButtonHeartOff.setOnClickListener(new SubButtonOnClickListener());
        this.imageButtonHeartOn.setOnClickListener(new SubButtonOnClickListener());
        customActionBarView.findViewById(2131689593).setOnClickListener(new C12073());
    }

    protected void onStart() {
        super.onStart();
        Log.d("TabletActivityAdview", "AdView.startActivity(this)");
        AdView.startActivity(this);
        NextRadioApplication.registerWithBus(this);
    }

    protected void onStop() {
        super.onStop();
        Log.d("TabletActivityAdview", "AdView.stopActivity(this)");
        AdView.stopActivity(this);
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onBackPressed() {
        if (this.drawer_layout.isDrawerOpen((int) ADS_ATT)) {
            this.drawer_layout.closeDrawer((int) ADS_ATT);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
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
        if (stationListEvent != null && stationListEvent.errorCode != 4) {
            if (stationListEvent.errorCode != 0) {
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
                    if ((test == null || !test.isVisible()) && !NextRadioSDKWrapperProvider.getInstance().getNoDataMode()) {
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
                Collections.sort(stationListEvent.stationList, new C12084());
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
        AppRater.app_launched(this);
        AppRater.showRateDialog(this, getSharedPreferences("apprater", 0).edit());
        if (NextRadioSDKWrapperProvider.getInstance().hasInitialized()) {
            setDeviceRegistrationInfo();
        }
    }

    private void registerHeadPhoneStateReceiver() {
        IntentFilter receiverFilter = new IntentFilter("android.intent.action.HEADSET_PLUG");
        this.receiver = new HeadsetStateReceiver();
        registerReceiver(this.receiver, receiverFilter);
    }

    private void unRegisterHeadPhoneStateReceiver() {
        if (this.receiver != null) {
            unregisterReceiver(this.receiver);
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
        Log.d("TabletActivityAdview", "adview.pause()");
        destroyAdBanner();
        if (!isRadioPowerOn()) {
            NextRadioSDKWrapperProvider.getInstance().setLocationUpdateInterval(false);
        }
    }

    protected void onResume() {
        super.onResume();
        if (AcquireLocationActivity.noStationAvailable) {
            this.mChosenActivity = 4;
            displayFragment(this.mChosenActivity);
        } else {
            getSharedPreferences(RadioAdapterService.PREFS, 4).edit().putBoolean("useFavoritesForSeek", PreferenceManager.getDefaultSharedPreferences(this).getBoolean("useFavoritesForSeek", false)).commit();
            checkRegister();
            displayTutorial();
            RadioAdapterService.isPermissionRevoked = false;
            if (this.doNotRefreshForPermissionDeny) {
                this.doNotRefreshForPermissionDeny = false;
            } else {
                showLocation(this);
            }
            NextRadioSDKWrapperProvider.getInstance().setLocationUpdateInterval(true);
        }
        if (!this.mNoDataMode) {
            Log.d("TabletActivityAdview", "adview.resume()");
            enableAdBanner();
        }
        checkLanguageUpdate();
    }

    private void displayTutorial() {
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("didTutorial", false)) {
            startActivity(new Intent(this, NewTutorialActivity.class));
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

    private void toggleNavDrawer() {
        if (this.nav_view != null && this.drawer_layout.isDrawerOpen(this.nav_view)) {
            this.drawer_layout.closeDrawer(this.nav_view);
        }
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
            NextRadioSDKWrapperProvider.getInstance().getEvent(eventUFID, new C12095(what));
        }
    }

    private void checkLanguageUpdate() {
        String currentLang = Locale.getDefault().getLanguage();
        if (getCurrentPhoneLanguageCode().length() == 0) {
            saveCurrentPhoneLanguageCode(currentLang);
        } else if (!currentLang.equals(getCurrentPhoneLanguageCode())) {
            saveCurrentPhoneLanguageCode(currentLang);
            NextRadioApplication.postToBus(this, new C12106());
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
            this.doNotRefreshForPermissionDeny = true;
            PermissionUtil.saveLocationPermissionState(this, true);
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
            this.doNotRefreshForPermissionDeny = true;
            NextRadioApplication.getInstance().changeImageCache(false);
            PermissionUtil.saveStoragePermissionDeniedDate(this, DateUtils.getCurrentDate());
        }
    }

    private void displayAlert(String title, String msg, int permissionType) {
        new AlertDialog.Builder(this, 2131427334).setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton(17039370, new C12128(permissionType)).setNegativeButton(17039369, new C12117(permissionType)).setIcon(17301543).show();
    }

    @UiThread
    public void updateViews(NextRadioEventInfo event) {
        if (this.mCurrRunnable != null) {
            this.mHandler.removeCallbacks(this.mCurrRunnable);
        }
        this.mCurrRunnable = new C12139(event);
        this.mHandler.postDelayed(this.mCurrRunnable, 800);
    }

    @UiThread
    @Subscribe
    public void dataChange(NRDataMode dataMode) {
        if (dataMode.mIsDataModeOff) {
            this.textViewSmallStation.setText(Stomp.EMPTY);
            this.textViewSmallStationPart2.setText(Stomp.EMPTY);
            this.textViewSmallEvent.setText(Stomp.EMPTY);
            this.imageViewSmall.setImageResource(2130837651);
        }
    }

    @UiThread
    @Subscribe
    public void radioResult(NRRadioResult result) {
        if (result.action == ADS_PINSIGHT) {
            this.imageBtnPlay.setVisibility(0);
            this.imageBtnPause.setVisibility(8);
            this.progressBar.setVisibility(8);
        }
        if (result.action == ADS_ATT) {
            this.imageBtnPlay.setVisibility(8);
            this.imageBtnPause.setVisibility(0);
            this.progressBar.setVisibility(8);
        }
        if (result.action == 4) {
            this.mLastUFID = null;
            this.imageBtnPlay.setVisibility(8);
            this.imageBtnPause.setVisibility(8);
            this.progressBar.setVisibility(0);
        }
    }

    private void displayFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            this.imageButtonHeartOff.setVisibility(8);
            this.imageButtonHeartOn.setVisibility(0);
            return;
        }
        this.imageButtonHeartOff.setVisibility(0);
        this.imageButtonHeartOn.setVisibility(8);
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        this.mCurrentEventInfo = event.currentEvent;
        if (event == null) {
            Log.e(TAG, "NRCurrentEvent placed on BUS with null event, skipping");
        } else if (event.currentEvent == null) {
            Log.e(TAG, "NRCurrentEvent placed on BUS with null event.currentEvent, skipping");
        } else {
            Log.d(TAG, "onEventChanged - " + event.currentEvent.toString());
            updateViews(event.currentEvent);
        }
    }

    public void onPermissionRequired() {
        this.isSharePermissionRequested = true;
        requestStoragePermission(this, getResources().getString(2131165345));
    }
}
