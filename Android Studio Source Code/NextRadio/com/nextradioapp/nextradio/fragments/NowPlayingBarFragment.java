package com.nextradioapp.nextradio.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.adapters.NowPlayingCardAdapter;
import com.nextradioapp.nextradio.mixpanel.MipEvents;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDrawerEvent;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.utils.ImageLoadingWrapper;
import com.nextradioapp.utils.PermissionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.squareup.otto.Subscribe;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONObject;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@EFragment(2130903086)
public class NowPlayingBarFragment extends Fragment implements com.nextradioapp.androidSDK.actions.ShareEventAction.OnStorageRequestPermission {
    private static final String TAG = "NowPlayingBarFragment";
    @ViewById
    View btnDislike;
    @ViewById
    View btnLike;
    @ViewById
    View btnShare;
    private int deviceWidth;
    private DisplayMetrics displayMetrics;
    private Handler handler;
    @ViewById
    ImageView imageBtnPause;
    @ViewById
    ImageView imageBtnPlay;
    @ViewById
    ImageView imageButtonHeartOff;
    @ViewById
    ImageView imageButtonHeartOn;
    @ViewById
    ImageView imageViewSmall;
    private Context mContext;
    private Runnable mCurrRunnable;
    private NextRadioEventInfo mCurrentEventInfo;
    private Handler mHandler;
    private String mLastUFID;
    private LayoutInflater mLayoutInflater;
    private NowPlayingCardAdapter mNowPlayingCardAdapter;
    private OnStorageRequestPermission mPermissionCallback;
    private View mRootView;
    @ViewById
    LinearLayout playing_bar_layout;
    @ViewById
    ProgressBar progressBar;
    private Runnable runnable;
    private int screenRatio;
    @ViewById
    ScrollView scrollView;
    @ViewById
    TextView textViewSmallEvent;
    @ViewById
    TextView textViewSmallStation;
    @ViewById
    TextView textViewSmallStationPart2;
    @ViewById
    ViewAnimator viewAnimatorNowPlaying;

    public interface OnStorageRequestPermission {
        void onPermissionRequired();
    }

    private ArrayList<EventAction> getEventActions() {
        return NextRadioSDKWrapperProvider.getInstance().getEventActions(this.mCurrentEventInfo);
    }

    private EventAction getEventActionForType(String actionType) {
        Iterator i$ = getEventActions().iterator();
        while (i$.hasNext()) {
            EventAction action = (EventAction) i$.next();
            if (action.getType().equals(actionType)) {
                return action;
            }
        }
        return null;
    }

    @Click({2131689604})
    void imageBtnPauseClick() {
        NextRadioApplication.postToBus(this, new 1(this));
    }

    @Click({2131689605})
    void imageBtnPlayClick() {
        NextRadioApplication.postToBus(this, new 2(this));
    }

    @Click({2131689603})
    void imageButtonHeartOffClick() {
        if (this.mCurrentEventInfo != null && this.mCurrentEventInfo.stationInfo != null) {
            MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.STATION_NAME, this.mCurrentEventInfo.stationInfo.frequencyAndCallLetters(), MipEvents.FAVORITED_STATION);
            int freqHz = this.mCurrentEventInfo.stationInfo.frequencyHz;
            int subChannel = this.mCurrentEventInfo.stationInfo.frequencySubChannel;
            int stationType = this.mCurrentEventInfo.stationInfo.getStationType();
            this.mCurrentEventInfo.stationInfo.setStationFavorite(true);
            NextRadioSDKWrapperProvider.getInstance().setStationFavoriteStatus(freqHz, subChannel, stationType, true);
            displayFavoriteButton(true);
        }
    }

    @Click({2131689602})
    void imageButtonHeartOnClick() {
        if (this.mCurrentEventInfo != null && this.mCurrentEventInfo.stationInfo != null) {
            int freqHz = this.mCurrentEventInfo.stationInfo.frequencyHz;
            int subChannel = this.mCurrentEventInfo.stationInfo.frequencySubChannel;
            int stationType = this.mCurrentEventInfo.stationInfo.getStationType();
            this.mCurrentEventInfo.stationInfo.setStationFavorite(false);
            NextRadioSDKWrapperProvider.getInstance().setStationFavoriteStatus(freqHz, subChannel, stationType, false);
            displayFavoriteButton(false);
        }
    }

    public NowPlayingBarFragment() {
        this.screenRatio = 85;
        this.handler = new Handler();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        try {
            this.mPermissionCallback = (OnStorageRequestPermission) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        this.mCurrentEventInfo = event.currentEvent;
        if (event != null && event.currentEvent != null) {
            updateViews(event.currentEvent);
            recordMipListeningData();
        }
    }

    @UiThread
    public void updateCards(NextRadioEventInfo nextRadioEvent) {
        if (this.mNowPlayingCardAdapter != null) {
            this.mNowPlayingCardAdapter.updateCards(nextRadioEvent, nextRadioEvent.cards, nextRadioEvent.stationInfo.publicStationID, nextRadioEvent.teID, nextRadioEvent.trackingID, nextRadioEvent.UFIDIdentifier);
            this.mRootView.invalidate();
            this.mHandler.postDelayed(new 3(this), 50);
        }
    }

    public void onPermissionRequired() {
        this.mPermissionCallback.onPermissionRequired();
    }

    public void displayEventAction(boolean val) {
        NextRadioApplication.mCurrentActivity = getActivity();
        if (NextRadioApplication.getInstance().getEventInfo() != null) {
            this.mCurrentEventInfo = NextRadioApplication.getInstance().getEventInfo();
        }
        try {
            getEventActionForType("share").start(1, val);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        loadDefaultStationLogoImages();
        NextRadioApplication.getInstance().setEventInfo(null);
    }

    @UiThread
    public void updateViews(NextRadioEventInfo event) {
        if (isAdded() && getActivity() != null) {
            if (this.mCurrRunnable != null) {
                this.mHandler.removeCallbacks(this.mCurrRunnable);
            }
            String imageURLHighRes = event.imageURLHiRes;
            String imageURLLowRes = event.imageURL;
            String stationImageURLHighRes = event.stationInfo.imageURLHiRes;
            String stationImageURLLowRes = event.stationInfo.imageURL;
            if (!NextRadioApplication.isTablet) {
                this.textViewSmallStation.setText(event.stationInfo.frequencyAndCallLetters());
                if (event.stationInfo.slogan == null || event.stationInfo.slogan.length() == 0 || event.stationInfo.getStationType() == 1) {
                    this.textViewSmallStationPart2.setVisibility(8);
                } else {
                    this.textViewSmallStationPart2.setVisibility(0);
                    this.textViewSmallStationPart2.setText(" - " + event.stationInfo.slogan);
                }
                this.textViewSmallEvent.setVisibility(0);
                if (!event.getUILine1().equals(Stomp.EMPTY) && !event.getUILine2().equals(Stomp.EMPTY)) {
                    this.textViewSmallEvent.setText(event.getUILine1() + " - " + event.getUILine2());
                } else if (event.getUILine1().equals(Stomp.EMPTY) || event.stationInfo.getStationType() == 1) {
                    this.textViewSmallEvent.setVisibility(8);
                } else {
                    this.textViewSmallEvent.setText(event.getUILine1());
                }
                displayFavoriteButton(event.stationInfo.isFavorited);
                ActionPayload payload = new ActionPayload(event.trackingID, event.teID, Stomp.EMPTY, event.UFIDIdentifier, event.stationInfo.publicStationID);
                new ImageLoadingWrapper(this.imageViewSmall, event.trackingID, event.stationInfo.publicStationID, 1, 1, event.getUILine1() + Stomp.EMPTY, payload, false).addImageURL(imageURLLowRes).addImageURL(imageURLHighRes).addImageURL(stationImageURLLowRes).addImageURL(stationImageURLHighRes).onFailed(new 4(this)).display();
            }
            this.mCurrRunnable = new 5(this, event, stationImageURLHighRes, imageURLHighRes, imageURLLowRes, stationImageURLLowRes);
            this.mHandler.postDelayed(this.mCurrRunnable, 800);
            loadDefaultStationLogoImages();
        }
    }

    private void loadDefaultStationLogoImages() {
        if (this.mCurrentEventInfo != null && this.mCurrentEventInfo.stationInfo != null) {
            File file = DiscCacheUtil.findInCache(this.mCurrentEventInfo.stationInfo.imageURLHiRes, ImageLoader.getInstance().getDiscCache());
            if (file == null || !file.exists()) {
                ImageLoader.getInstance().loadImage(this.mCurrentEventInfo.stationInfo.imageURLHiRes, NextRadioApplication.imageOptions, new 6(this));
            }
        }
    }

    private void recordMixPanelValues(String action, NextRadioEventInfo event) {
        Object obj = -1;
        switch (action.hashCode()) {
            case -1698449537:
                if (action.equals("thumbsdown")) {
                    obj = 1;
                    break;
                }
                break;
            case -411101032:
                if (action.equals("mp3search")) {
                    obj = 4;
                    break;
                }
                break;
            case 3522941:
                if (action.equals("save")) {
                    obj = 3;
                    break;
                }
                break;
            case 109400031:
                if (action.equals("share")) {
                    obj = 2;
                    break;
                }
                break;
            case 1566946488:
                if (action.equals("thumbsup")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case Tokenizer.EOF /*0*/:
                try {
                    if (event.getArtistName() == null || event.getArtistName().length() <= 0) {
                        MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "Like", MipEvents.NOWPLAYING);
                        return;
                    }
                    JSONObject props = new JSONObject();
                    props.put(MipProperties.TAP, "Like");
                    props.put(MipProperties.ARTIST_NAME, event.getArtistName().toString());
                    MixPanelHelper.getInstance(getActivity()).recordMIPJsonObject(props, MipEvents.LIKED_SONG);
                } catch (Exception e) {
                }
            case Zone.PRIMARY /*1*/:
                MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "Dislike", MipEvents.NOWPLAYING);
            case Zone.SECONDARY /*2*/:
                MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "Share This", MipEvents.NOWPLAYING);
            case Protocol.GGP /*3*/:
                MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "Save", MipEvents.NOWPLAYING);
            case Type.MF /*4*/:
                MixPanelHelper.getInstance(getActivity()).recordMIPEvent(MipProperties.TAP, "Buy Song", MipEvents.NOWPLAYING);
            default:
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLayoutInflater = getActivity().getLayoutInflater();
        this.displayMetrics = getActivity().getResources().getDisplayMetrics();
        this.deviceWidth = this.displayMetrics.widthPixels;
        try {
            this.screenRatio = convertDpToPixel((float) this.screenRatio, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordMipListeningData() {
        if (this.runnable != null) {
            this.handler.removeCallbacks(this.runnable);
        }
        this.runnable = new 7(this);
        this.handler.postDelayed(this.runnable, 500);
    }

    private void recordMIPListening(boolean listening) {
        JSONObject props = new JSONObject();
        try {
            props.put(MipProperties.VIEW, MipEvents.NOWPLAYING);
            props.put(MipProperties.LISTENING, listening);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MixPanelHelper.getInstance(getActivity()).recordMIPJsonObject(props, MipEvents.NOWPLAYING);
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
    public void onDrawerOpened(NRDrawerEvent drawerEvent) {
        if (isAdded() && drawerEvent.what == 1) {
            Rect scrollBounds = new Rect();
            this.scrollView.getHitRect(scrollBounds);
            if (this.mNowPlayingCardAdapter != null) {
                this.mNowPlayingCardAdapter.testForViewVisible(scrollBounds);
            }
        }
    }

    @UiThread
    @Subscribe
    public void radioResult(NRRadioResult result) {
        if (isAdded()) {
            PermissionUtil.saveRadioState(getActivity(), result.action);
            if (result.action == 2) {
                this.imageBtnPlay.setVisibility(0);
                this.imageBtnPause.setVisibility(8);
                this.progressBar.setVisibility(8);
            }
            if (result.action == 3) {
                this.imageBtnPlay.setVisibility(8);
                this.imageBtnPause.setVisibility(0);
                this.progressBar.setVisibility(8);
                this.scrollView.smoothScrollTo(0, 0);
            }
            if (result.action == 4) {
                this.mLastUFID = null;
                this.imageBtnPlay.setVisibility(8);
                this.imageBtnPause.setVisibility(8);
                this.progressBar.setVisibility(0);
            }
        }
    }

    @UiThread
    @Subscribe
    public void dataChange(NRDataMode dataMode) {
        if (isAdded() && dataMode.mIsDataModeOff) {
            this.textViewSmallStation.setText(Stomp.EMPTY);
            this.textViewSmallStationPart2.setText(Stomp.EMPTY);
            this.textViewSmallEvent.setText(Stomp.EMPTY);
            this.imageViewSmall.setImageResource(2130837651);
        }
    }

    @AfterViews
    void afterViews() {
        this.mHandler = new Handler();
        this.mContext = getActivity();
        this.mRootView = getView();
        if (NextRadioApplication.isTablet) {
            this.playing_bar_layout.setVisibility(8);
            return;
        }
        this.mNowPlayingCardAdapter = new NowPlayingCardAdapter(getActivity(), getView());
        this.scrollView.getViewTreeObserver().addOnScrollChangedListener(new 8(this));
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
        if (this.mCurrentEventInfo != null && this.mCurrentEventInfo.stationInfo != null) {
            displayFavoriteButton(this.mCurrentEventInfo.stationInfo.isFavorited);
        }
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    public int convertDpToPixel(float dp, Context context) {
        int px = (int) ((((float) this.displayMetrics.densityDpi) / 160.0f) * dp);
        if (px < Service.CISCO_FNA) {
            return px + 40;
        }
        return px;
    }
}
