package com.nextradioapp.androidSDK.actions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import org.apache.activemq.transport.stomp.Stomp;

public class PhoneCallEventAction extends EventAction {
    private String eventHeadline;
    int mRequestCode;
    private IActivityManager myParent;
    String phoneNumber;

    public PhoneCallEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String phoneNumber, String headline) {
        super(dbAdapter, payload);
        this.mRequestCode = 0;
        this.phoneNumber = phoneNumber;
        this.myParent = myParent;
        this.mType = "phone";
        this.eventHeadline = headline;
    }

    public String getActionDescription() {
        if (this.eventHeadline == null || this.eventHeadline.length() <= 0) {
            return this.myParent.getCurrentApplication().getString(C1136R.string.actions_phone_call);
        }
        return this.eventHeadline;
    }

    public void start_internal(boolean isSpecialExecute) {
        if (isSpecialExecute) {
            try {
                this.phoneNumber = "tel:" + this.phoneNumber.replace("-", Stomp.EMPTY).replace(" ", Stomp.EMPTY).replace("(", Stomp.EMPTY).replace(")", Stomp.EMPTY);
                this.myParent.getCurrentActivity().startActivity(new Intent("android.intent.action.CALL", Uri.parse(this.phoneNumber)));
                return;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this.myParent.getCurrentActivity(), this.myParent.getCurrentActivity().getString(C1136R.string.action_call_failed), 0).show();
                return;
            }
        }
        this.phoneNumber = "tel:" + this.phoneNumber.replace("-", Stomp.EMPTY).replace(" ", Stomp.EMPTY).replace("(", Stomp.EMPTY).replace(")", Stomp.EMPTY);
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse(this.phoneNumber));
        if (NextRadioAndroid.getInstance().isTablet(this.myParent.getCurrentActivity())) {
            this.myParent.getCurrentActivity().setRequestedOrientation(0);
            this.myParent.getCurrentActivity().startActivity(intent);
            return;
        }
        this.myParent.getCurrentActivity().startActivity(intent);
    }

    public int getReportingAction() {
        return 9;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Place a phone call for", this.eventHeadline + " ?"};
    }
}
