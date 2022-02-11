package com.nextradioapp.nextradio.fragments;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.ViewAnimator;
import com.google.android.gms.common.Scopes;
import com.nextradioapp.core.objects.Card;
import com.nextradioapp.core.objects.CardButton;
import com.nextradioapp.core.objects.CardData;
import com.nextradioapp.core.objects.CardPropertyMap;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.adapters.NowPlayingCardAdapter;
import com.nextradioapp.nextradio.mixpanel.MipEvents;
import com.nextradioapp.nextradio.mixpanel.MipProperties;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDrawerEvent;
import com.nextradioapp.nextradio.services.RadioAdapterService;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Iterator;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

@EFragment(2130903081)
public class CardsFragment extends Fragment {
    private static final String TAG = "NowPlayingBarFragment";
    private Runnable mCurrRunnable;
    private NextRadioEventInfo mCurrentEventInfo;
    private Handler mHandler;
    private String mLastUFID;
    private LayoutInflater mLayoutInflater;
    private NowPlayingCardAdapter mNowPlayingCardAdapter;
    private View mRootView;
    @ViewById
    ScrollView scrollView;
    @ViewById
    ViewAnimator viewAnimatorNowPlaying;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        this.mCurrentEventInfo = event.currentEvent;
        if (event != null && event.currentEvent != null) {
            updateViews(event.currentEvent);
        }
    }

    @UiThread
    public void updateCards(NextRadioEventInfo nextRadioEvent) {
        this.mNowPlayingCardAdapter.updateCards(nextRadioEvent, nextRadioEvent.cards, nextRadioEvent.stationInfo.publicStationID, nextRadioEvent.teID, nextRadioEvent.trackingID, nextRadioEvent.UFIDIdentifier);
        this.mRootView.invalidate();
        this.mHandler.postDelayed(new 1(this), 50);
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

    @UiThread
    public void updateViews(NextRadioEventInfo event) {
        if (isAdded()) {
            if (this.mCurrRunnable != null) {
                this.mHandler.removeCallbacks(this.mCurrRunnable);
            }
            this.mCurrRunnable = new 2(this, event);
            this.mHandler.postDelayed(this.mCurrRunnable, 800);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLayoutInflater = getActivity().getLayoutInflater();
    }

    private void recordMipListeningData() {
        Log.e(TAG, "RadioAdapterService.mRadioAdapter:" + RadioAdapterService.mRadioAdapter);
        if (RadioAdapterService.mRadioAdapter != null) {
            Log.e(TAG, "RadioAdapterService.mRadioAdapter:" + RadioAdapterService.mRadioAdapter.getIsPoweredOn());
            recordMIPListening(RadioAdapterService.mRadioAdapter.getIsPoweredOn());
            return;
        }
        recordMIPListening(false);
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

    @Subscribe
    public void onDrawerOpened(NRDrawerEvent drawerEvent) {
        if (drawerEvent.what == 1) {
            Rect scrollBounds = new Rect();
            this.scrollView.getHitRect(scrollBounds);
            this.mNowPlayingCardAdapter.testForViewVisible(scrollBounds);
        }
    }

    @AfterViews
    void afterViews() {
        this.mHandler = new Handler();
        this.mNowPlayingCardAdapter = new NowPlayingCardAdapter(getActivity(), getView());
        this.mRootView = getView();
        this.scrollView.getViewTreeObserver().addOnScrollChangedListener(new 3(this));
    }

    private NextRadioEventInfo displayFeedBackEvent(NextRadioEventInfo eventInfo) {
        try {
            Card cards = new Card();
            cards.title = getResources().getString(2131165350);
            cards.cardStyle = "large";
            CardData cardData = new CardData();
            cardData.title = getResources().getString(2131165285);
            cardData.subtitle = getResources().getString(2131165284);
            cardData.imageURL = "drawable://2130837657";
            cardData.action = null;
            CardButton cardButton = new CardButton();
            cardButton.label = getResources().getString(2131165283);
            cardButton.action = Scopes.EMAIL;
            cardButton.icon = Scopes.EMAIL;
            CardPropertyMap cardPropertyMap = new CardPropertyMap();
            if (eventInfo.stationInfo != null) {
                cardPropertyMap.getMap().put("Subject", eventInfo.stationInfo.frequencyAndCallLetters());
                cardPropertyMap.getMap().put("Link", "Feedback");
            }
            cardButton.data = cardPropertyMap;
            cards.data = new CardData[1];
            cards.data[0] = cardData;
            cards.buttons = new CardButton[1];
            cards.buttons[0] = cardButton;
            eventInfo.cards = new ArrayList();
            eventInfo.cards.add(cards);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventInfo;
    }

    public void onResume() {
        super.onResume();
        NextRadioApplication.registerWithBus(this);
    }

    public void onPause() {
        super.onPause();
        NextRadioApplication.unregisterWithBus(this);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
