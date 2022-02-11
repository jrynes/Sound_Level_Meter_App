package com.nextradioapp.androidSDK.actions;

import android.widget.Toast;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;

public class SaveEventAction extends EventAction {
    private IActivityManager mContext;
    private String mUFID;

    public SaveEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager context) {
        super(dbAdapter, payload);
        this.mType = "thumbsup";
        this.mContext = context;
        this.mUFID = payload.mUFID;
    }

    public void start_internal(boolean specialExecution) throws Exception {
        NextRadioAndroid.getInstance().addSavedEvent(this.mUFID);
        Toast.makeText(this.mContext.getCurrentActivity(), this.mContext.getCurrentApplication().getString(C1136R.string.actions_saved_success), 1).show();
    }

    public String getActionDescription() {
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_save);
    }

    public int getReportingAction() {
        return 2;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Save this event?", null};
    }
}
