package com.nextradioapp.androidSDK.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nextradioapp.androidSDK.C1136R;
import org.apache.activemq.transport.stomp.Stomp;

public class FeedBackEventAdapter extends ArrayAdapter<String> {
    String[] feedBackTypes;
    private Context mContext;

    public FeedBackEventAdapter(Context context, int resource, String[] feedBackTypes) {
        super(context, resource);
        this.mContext = context;
        this.feedBackTypes = feedBackTypes;
    }

    public int getCount() {
        return this.feedBackTypes.length;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(C1136R.layout.feedback_action_row, null);
        }
        ((TextView) convertView.findViewById(C1136R.id.lblActionItem)).setText(this.feedBackTypes[position].split(Stomp.COMMA)[1]);
        return convertView;
    }
}
