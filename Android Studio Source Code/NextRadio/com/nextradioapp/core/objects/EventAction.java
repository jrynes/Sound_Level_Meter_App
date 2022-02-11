package com.nextradioapp.core.objects;

import com.nextradioapp.core.Log;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import org.apache.activemq.transport.stomp.Stomp;

public abstract class EventAction {
    protected String[] datafields;
    private final IDatabaseAdapter mDatabaseAdapter;
    private ActionPayload mPayload;
    protected String mType;

    public abstract String getActionDescription();

    public abstract int getReportingAction();

    protected abstract void start_internal(boolean z) throws Exception;

    public EventAction(IDatabaseAdapter databaseAdapter, ActionPayload payload) {
        this.mDatabaseAdapter = databaseAdapter;
        this.mPayload = payload;
    }

    public EventAction() {
        this.mDatabaseAdapter = null;
        this.mPayload = null;
    }

    public String getType() {
        if (this.mType == null) {
            return Stomp.EMPTY;
        }
        return this.mType;
    }

    public String[] getDetailedMessages() {
        return null;
    }

    public void start(int source, boolean isSpecialExecute) {
        try {
            if (!(this.mPayload == null || this.mPayload.mTrackingID == null || this.mPayload.mTrackingID.length() <= 0 || this.mDatabaseAdapter == null)) {
                this.mDatabaseAdapter.recordActionImpression(this.mPayload, getReportingAction(), source);
            }
            start_internal(isSpecialExecute);
        } catch (Exception e) {
            Log.m1935e("Error", "e:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start(int source) {
        start(source, false);
    }
}
