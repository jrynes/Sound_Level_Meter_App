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

public class MapEventAction extends EventAction {
    private IActivityManager mContext;
    private Intent mIntent;
    private String mSearchQuery;
    private String trackingID;

    public MapEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String searchString) {
        super(dbAdapter, payload);
        this.mSearchQuery = searchString;
        this.mContext = myParent;
        this.mType = "findnearby";
        this.trackingID = this.trackingID;
    }

    public void start_internal(boolean specialExecution) throws Exception {
        String URIString = "geo:0,0";
        if (!(this.mSearchQuery == null || this.mSearchQuery.equals(Stomp.EMPTY))) {
            URIString = URIString + "?q=" + this.mSearchQuery;
        }
        this.mIntent = new Intent("android.intent.action.VIEW", Uri.parse(URIString));
        if (NextRadioAndroid.getInstance().isTablet(this.mContext.getCurrentActivity())) {
            this.mContext.getCurrentActivity().setRequestedOrientation(0);
            this.mContext.getCurrentActivity().startActivity(this.mIntent);
            return;
        }
        this.mContext.getCurrentActivity().startActivity(this.mIntent);
    }

    public String getActionDescription() {
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_map);
    }

    public int getReportingAction() {
        return 8;
    }

    public String[] getDetailedMessages() {
        return new String[]{"Use phone", "to navigate"};
    }
}
