package com.nextradioapp.androidSDK.actions;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.EventAction;

public class ThumbEventAction extends EventAction {
    private static String CALL_LETTER_MARKERS;
    private String callLetters;
    private IActivityManager mContext;
    private boolean mIsThumbUp;
    private String mUFID;

    static {
        CALL_LETTER_MARKERS = "^^^";
    }

    public ThumbEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager myParent, String call, boolean isThumbUp) {
        super(dbAdapter, payload);
        this.mContext = myParent;
        this.mUFID = payload.mUFID;
        this.callLetters = call;
        if (isThumbUp) {
            this.mType = "thumbsup";
        } else {
            this.mType = "thumbsdown";
        }
        this.mIsThumbUp = isThumbUp;
    }

    public String getActionDescription() {
        if (this.mIsThumbUp) {
            return this.mContext.getCurrentApplication().getString(C1136R.string.actions_thumb_up);
        }
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_thumb_down);
    }

    public void start_internal(boolean specialExecution) throws Exception {
        if (this.mIsThumbUp) {
            NextRadioAndroid.getInstance().addSavedEvent(this.mUFID);
        }
        String temp = this.mContext.getCurrentApplication().getString(C1136R.string.actions_feedback_call_letters);
        CharSequence charSequence = "^^^";
        CALL_LETTER_MARKERS = charSequence;
        Toast t = Toast.makeText(this.mContext.getCurrentActivity(), temp.replace(charSequence, this.callLetters), 1);
        ((TextView) ((ViewGroup) t.getView()).getChildAt(0)).setTextSize(12.0f);
        t.show();
    }

    public int getReportingAction() {
        if (this.mIsThumbUp) {
            return 11;
        }
        return 14;
    }
}
