package com.nextradioapp.androidSDK.actions;

import android.content.Intent;
import android.net.Uri;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;
import org.apache.activemq.transport.stomp.Stomp;

public class WebURLAction extends EventAction {
    private final String mActionText;
    IActivityManager myParent;
    String myUrl;

    public WebURLAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String url, String actionText) {
        super(dbAdapter, payload);
        this.myUrl = url;
        this.myParent = myParent;
        this.mType = "web";
        this.mActionText = actionText;
    }

    public void start_internal(boolean specialExecution) throws Exception {
        Intent mIntent = new Intent("android.intent.action.VIEW");
        mIntent.setType(Stomp.TEXT_PLAIN);
        mIntent.setData(Uri.parse(this.myUrl));
        if (NextRadioAndroid.getInstance().isTablet(this.myParent.getCurrentActivity())) {
            this.myParent.getCurrentActivity().setRequestedOrientation(0);
            this.myParent.getCurrentActivity().startActivity(mIntent);
            return;
        }
        this.myParent.getCurrentActivity().startActivity(mIntent);
    }

    public String getActionDescription() {
        if (this.mActionText == null || this.mActionText.length() <= 0) {
            return this.myParent.getCurrentApplication().getString(C1136R.string.actions_web);
        }
        return this.mActionText;
    }

    public int getReportingAction() {
        return 12;
    }
}
