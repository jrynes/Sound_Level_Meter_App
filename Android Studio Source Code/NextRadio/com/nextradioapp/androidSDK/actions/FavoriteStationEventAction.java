package com.nextradioapp.androidSDK.actions;

import android.widget.Toast;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;

public class FavoriteStationEventAction extends EventAction {
    private static String CALL_LETTER_MARKERS;
    String callLetters;
    private IActivityManager mContext;

    static {
        CALL_LETTER_MARKERS = "^^^";
    }

    public FavoriteStationEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager context, String callLetters) {
        super(dbAdapter, payload);
        this.mContext = context;
        this.mType = "favoritestation";
    }

    public String getActionDescription() {
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_favorite_station);
    }

    public int getReportingAction() {
        return 13;
    }

    public void start_internal(boolean specialExecution) throws Exception {
        Toast t = Toast.makeText(this.mContext.getCurrentActivity(), this.mContext.getCurrentApplication().getString(C1136R.string.actions_favorited_call_letters).replace(CALL_LETTER_MARKERS, this.callLetters), 1);
    }
}
