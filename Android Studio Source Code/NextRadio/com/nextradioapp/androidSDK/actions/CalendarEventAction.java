package com.nextradioapp.androidSDK.actions;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import com.nextradioapp.androidSDK.data.schema.Tables.stationReporting;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.DateTransform;
import com.nextradioapp.core.objects.Enhancement;
import com.nextradioapp.core.objects.EventAction;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Date;

public class CalendarEventAction extends EventAction {
    public static final String ACTION_CALENDAR = "calendar";
    public static final int DATA_FIELD_CALENDAR_DESCRIPTION = 2;
    public static final int DATA_FIELD_CALENDAR_ENDDATETIME = 4;
    public static final int DATA_FIELD_CALENDAR_LOCATION = 1;
    public static final int DATA_FIELD_CALENDAR_STARTDATETIME = 3;
    public static final int DATA_FIELD_CALENDAR_TITLE = 0;
    private Enhancement ad;
    Date endDate;
    private IActivityManager mContext;
    private String mDescription;
    private Intent mIntent;
    String mLocation;
    private String mTitle;
    Date startDate;

    public CalendarEventAction(IDatabaseAdapter dbAdapter, ActionPayload payload, IActivityManager context, String title, String description, String location, String starts, String ends) {
        super(dbAdapter, payload);
        this.mLocation = null;
        this.mTitle = title;
        this.mDescription = description;
        this.mContext = context;
        this.mLocation = location;
        this.mType = ACTION_CALENDAR;
        try {
            this.startDate = new DateTransform().read(starts);
            this.endDate = new DateTransform().read(ends);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start_internal(boolean isSpecialExecution) throws Exception {
        Long start;
        Long end;
        if (isSpecialExecution) {
            try {
                if (this.startDate == null || this.endDate == null) {
                    throw new Exception("either start or end date or both were not provided");
                }
                start = Long.valueOf(this.startDate.getTime());
                end = Long.valueOf(this.endDate.getTime());
                ContentValues event = new ContentValues();
                event.put("calendar_id", Integer.valueOf(DATA_FIELD_CALENDAR_LOCATION));
                event.put("mTitle", this.mTitle);
                event.put("mDescription", this.mDescription);
                event.put("eventLocation", this.mLocation);
                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("eventStatus", Integer.valueOf(DATA_FIELD_CALENDAR_LOCATION));
                event.put("eventTimezone", "UTC/GMT +2:00");
                event.put("hasAlarm", Integer.valueOf(DATA_FIELD_CALENDAR_LOCATION));
                Log.i("Calendar ", this.mContext.getCurrentActivity().getContentResolver().insert(Uri.parse("content://com.android.calendar/events"), event).getEncodedSchemeSpecificPart());
                Toast.makeText(this.mContext.getCurrentActivity(), C1136R.string.eventSaved, DATA_FIELD_CALENDAR_LOCATION).show();
            } catch (Exception e) {
                Log.i("CalendarEvent Exception:", e.getMessage());
            }
        } else if (this.startDate == null || this.endDate == null) {
            throw new Exception("either start or end date or both were not provided");
        } else {
            start = Long.valueOf(this.startDate.getTime());
            end = Long.valueOf(this.endDate.getTime());
            this.mIntent = new Intent("android.intent.action.EDIT");
            this.mIntent.setType("vnd.android.cursor.item/event");
            this.mIntent.putExtra(SettingsJsonConstants.PROMPT_TITLE_KEY, this.mTitle);
            this.mIntent.putExtra(activityEvents.description, this.mDescription);
            this.mIntent.putExtra("eventLocation", this.mLocation);
            this.mIntent.putExtra("beginTime", start);
            this.mIntent.putExtra(stationReporting.endTime, end);
            if (NextRadioAndroid.getInstance().isTablet(this.mContext.getCurrentActivity())) {
                this.mContext.getCurrentActivity().setRequestedOrientation(0);
                this.mContext.getCurrentActivity().startActivity(this.mIntent);
                return;
            }
            this.mContext.getCurrentActivity().startActivity(this.mIntent);
        }
    }

    public String getActionDescription() {
        return this.mContext.getCurrentApplication().getString(C1136R.string.actions_calendar);
    }

    public int getReportingAction() {
        return DATA_FIELD_CALENDAR_ENDDATETIME;
    }

    public String[] getDetailedMessages() {
        String[] detailedString = new String[DATA_FIELD_CALENDAR_DESCRIPTION];
        detailedString[0] = "Add event to";
        detailedString[DATA_FIELD_CALENDAR_LOCATION] = "Calendar?";
        return detailedString;
    }
}
