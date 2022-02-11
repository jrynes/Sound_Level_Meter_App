package com.nextradioapp.androidSDK.actions;

import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.fragments.FeedBackDialogFragment;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;

public class FeedBackEventAction extends EventAction {
    String link;
    IActivityManager myParent;
    String subject;

    public FeedBackEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String subject, String link) {
        super(dbAdapter, payload);
        this.subject = subject;
        this.link = link;
        this.myParent = myParent;
        this.mType = "share";
    }

    public void start_internal(boolean specialExecution) throws Exception {
        FeedBackDialogFragment.newInstance(this.subject).show(this.myParent.getCurrentActivity().getFragmentManager(), "FeedBackDialogFragment");
    }

    public String getActionDescription() {
        return this.myParent.getCurrentApplication().getString(C1136R.string.actions_share);
    }

    public int getReportingAction() {
        return 6;
    }
}
