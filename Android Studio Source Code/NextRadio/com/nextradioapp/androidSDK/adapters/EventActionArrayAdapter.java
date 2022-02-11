package com.nextradioapp.androidSDK.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.actions.CalendarEventAction;
import com.nextradioapp.core.objects.EventAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EventActionArrayAdapter extends ArrayAdapter<EventAction> {
    private static final Map<String, Integer> imageMap;
    private Context mContext;

    static {
        Map<String, Integer> tempMap = new HashMap();
        tempMap.put("thumbsdown", Integer.valueOf(C1136R.drawable.action_icon_dislike));
        tempMap.put("thumbsup", Integer.valueOf(C1136R.drawable.action_icon_like));
        tempMap.put("phone", Integer.valueOf(C1136R.drawable.action_icon_call));
        tempMap.put("findnearby", Integer.valueOf(C1136R.drawable.action_icon_findnearby));
        tempMap.put(AdWebViewClient.SMS, Integer.valueOf(C1136R.drawable.action_icon_text));
        tempMap.put("mp3search", Integer.valueOf(C1136R.drawable.action_icon_buy));
        tempMap.put(CalendarEventAction.ACTION_CALENDAR, Integer.valueOf(C1136R.drawable.action_icon_remind));
        tempMap.put("share", Integer.valueOf(C1136R.drawable.share));
        tempMap.put("coupon", Integer.valueOf(C1136R.drawable.action_icon_coupon));
        tempMap.put("favoritestation", Integer.valueOf(C1136R.drawable.action_icon_favorite));
        tempMap.put("web", Integer.valueOf(C1136R.drawable.action_icon_website));
        imageMap = Collections.unmodifiableMap(tempMap);
    }

    public EventActionArrayAdapter(Context context, int resource, int textViewResourceId, EventAction[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    public EventActionArrayAdapter(Context context, int textViewResourceId, EventAction[] objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
    }

    public EventActionArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(C1136R.layout.overlay_action_row_dark_bottom, null);
        }
        ((TextView) convertView.findViewById(C1136R.id.lblActionItem)).setText(((EventAction) getItem(position)).getActionDescription());
        convertView.findViewById(C1136R.id.imgIcon).setVisibility(0);
        String actionType = ((EventAction) getItem(position)).getType();
        if (imageMap.containsKey(actionType)) {
            ((ImageView) convertView.findViewById(C1136R.id.imgIcon)).setImageResource(((Integer) imageMap.get(actionType)).intValue());
        } else {
            ((ImageView) convertView.findViewById(C1136R.id.imgIcon)).setImageResource(C1136R.drawable.action_icon_save);
        }
        return convertView;
    }
}
