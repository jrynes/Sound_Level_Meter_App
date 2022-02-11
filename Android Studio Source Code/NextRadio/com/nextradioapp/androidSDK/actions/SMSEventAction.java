package com.nextradioapp.androidSDK.actions;

import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;

public class SMSEventAction extends EventAction {
    Intent mIntent;
    IActivityManager myParent;
    Uri smsNumber;
    String smsText;

    public SMSEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String text, String number) {
        super(dbAdapter, payload);
        this.smsText = text;
        this.smsNumber = Uri.parse("sms:" + number);
        this.myParent = myParent;
        this.mType = AdWebViewClient.SMS;
    }

    public void start_internal(boolean specialExecution) {
        if (specialExecution) {
            SmsManager.getDefault().sendTextMessage(this.smsNumber.getSchemeSpecificPart(), null, this.smsText, null, null);
            return;
        }
        this.mIntent = new Intent("android.intent.action.VIEW");
        this.mIntent.setData(this.smsNumber);
        this.mIntent.putExtra("sms_body", this.smsText);
        if (NextRadioAndroid.getInstance().isTablet(this.myParent.getCurrentActivity())) {
            this.myParent.getCurrentActivity().setRequestedOrientation(0);
            this.myParent.getCurrentActivity().startActivity(this.mIntent);
            return;
        }
        this.myParent.getCurrentActivity().startActivity(this.mIntent);
    }

    public String getActionDescription() {
        return this.myParent.getCurrentApplication().getString(C1136R.string.actions_sms);
    }

    public int getReportingAction() {
        return 10;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Text " + this.smsText, "to " + this.smsNumber + "?"};
    }
}
