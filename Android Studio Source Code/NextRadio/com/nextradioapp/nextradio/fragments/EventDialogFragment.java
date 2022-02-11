package com.nextradioapp.nextradio.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.EventActionSet;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import com.nextradioapp.nextradio.ottos.NRDialogEvent;
import com.nextradioapp.utils.ImageLoadingWrapper;
import java.util.ArrayList;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.activemq.transport.stomp.Stomp;

@EFragment(2130903079)
public class EventDialogFragment extends DialogFragment {
    private String TAG;
    private String UFID;
    @ViewById
    Button btnAction;
    @ViewById
    Button btnActionPrimary;
    @ViewById
    View btnCloseDialog;
    @ViewById
    ImageView imageView1;
    private NextRadioEventInfo mCurrentNREvent;
    private EventActionSet mEventActionSet;
    @ViewById
    ProgressBar progressBar1;
    @ViewById
    TextView text1;
    @ViewById
    TextView text2;

    public EventDialogFragment() {
        this.TAG = "EventDialogFragment";
    }

    public static EventDialogFragment getInstance(NRDialogEvent dialogEvent) {
        EventDialogFragment dialog = new EventDialogFragment_();
        Bundle b = new Bundle();
        b.putString("UFID", dialogEvent.UFID);
        dialog.setArguments(b);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @AfterViews
    public void afterViews() {
        this.btnCloseDialog.setOnClickListener(new 1(this));
    }

    private void setupActionButtons() {
        this.btnActionPrimary.setVisibility(8);
        this.btnAction.setVisibility(8);
        try {
            if (shouldShowButtons(this.mCurrentNREvent)) {
                this.btnActionPrimary.setVisibility(0);
                this.btnAction.setVisibility(0);
                ArrayList<EventAction> actions = NextRadioAndroid.getInstance().getEventActions(this.mCurrentNREvent);
                if (actions != null) {
                    EventAction eventAction = getPrimaryAction(this.mCurrentNREvent, actions);
                    if (eventAction != null) {
                        setPrimaryButton(this.btnActionPrimary, eventAction);
                        setSecondaryButtonStatus();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldShowButtons(NextRadioEventInfo info) {
        if (info.itemType == 0 && info.stationInfo != null && (info.stationInfo.getStationType() == 1 || info.stationInfo.getStationType() == 0)) {
            return false;
        }
        if (info.title != null && info.title.length() > 0) {
            return true;
        }
        if (info.UFIDIdentifier == null || info.UFIDIdentifier.length() <= 0) {
            return false;
        }
        return true;
    }

    private EventAction getPrimaryAction(NextRadioEventInfo info, ArrayList<EventAction> events) {
        this.mEventActionSet = new EventActionSet(info, events);
        return this.mEventActionSet.primary;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(2, 2131427336);
        this.UFID = getArguments().getString("UFID");
    }

    private void setPrimaryButton(Button btnActionPrimary, EventAction eventAction) {
        btnActionPrimary.setText(eventAction.getActionDescription().toLowerCase());
        btnActionPrimary.setOnClickListener(new 2(this, eventAction));
    }

    private void setSecondaryButtonStatus() {
        if (this.mEventActionSet == null || this.mEventActionSet.secondary == null) {
            this.btnAction.setEnabled(false);
        } else {
            this.btnAction.setOnClickListener(new 3(this));
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mCurrentNREvent != null) {
            handleRequestedEvent(this.mCurrentNREvent);
        } else {
            NextRadioSDKWrapperProvider.getInstance().getEvent(this.UFID, new 4(this));
        }
    }

    @UiThread
    public void handleRequestedEvent(NextRadioEventInfo event) {
        if (isAdded()) {
            this.mCurrentNREvent = event;
            if (this.mCurrentNREvent != null) {
                this.text1.setText(this.mCurrentNREvent.getUILine1());
                this.text1.setSelected(true);
                this.text2.setText(this.mCurrentNREvent.getUILine2().toString());
                String imageURLHighRes = this.mCurrentNREvent.imageURLHiRes;
                String imageURLLowRes = this.mCurrentNREvent.imageURL;
                String stationImageURLHighRes = this.mCurrentNREvent.stationInfo == null ? null : this.mCurrentNREvent.stationInfo.imageURLHiRes;
                new ImageLoadingWrapper(this.imageView1, this.mCurrentNREvent.trackingID, this.mCurrentNREvent.stationInfo.publicStationID, 1, 2, this.mCurrentNREvent.title, new ActionPayload(event.trackingID, event.teID, Stomp.EMPTY, event.UFIDIdentifier, event.stationInfo.publicStationID), false).addImageURL(imageURLHighRes).addImageURL(imageURLLowRes).addImageURL(stationImageURLHighRes).addImageURL(this.mCurrentNREvent.stationInfo == null ? null : this.mCurrentNREvent.stationInfo.imageURL).withProgressView(this.progressBar1).onFailed(new 5(this)).display();
                setupActionButtons();
            }
        }
    }
}
