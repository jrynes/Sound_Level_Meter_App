package com.nextradioapp.androidSDK.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.adapters.FeedBackEventAdapter;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import com.nextradioapp.androidSDK.utils.AppRater;
import com.nextradioapp.core.web.TagStationAPIClient;
import org.apache.activemq.transport.stomp.Stomp;

public class FeedBackDialogFragment extends DialogFragment {
    private String frequency;

    /* renamed from: com.nextradioapp.androidSDK.fragments.FeedBackDialogFragment.1 */
    class C11481 implements OnClickListener {
        C11481() {
        }

        public void onClick(View v) {
            FeedBackDialogFragment.this.dismiss();
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.fragments.FeedBackDialogFragment.2 */
    class C11492 implements OnItemClickListener {
        final /* synthetic */ String[] val$feedBackTypesList;

        C11492(String[] strArr) {
            this.val$feedBackTypesList = strArr;
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            if (position == 2) {
                try {
                    FeedBackDialogFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(TagStationAPIClient.supportUrl)));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            } else if (position != 3) {
                String emailId = this.val$feedBackTypesList[position].split(Stomp.COMMA)[0];
                String body = this.val$feedBackTypesList[position].split(Stomp.COMMA)[1];
                FeedBackDialogFragment.this.shareFeedback(emailId, this.val$feedBackTypesList[position].split(Stomp.COMMA)[2], body);
            } else if (FeedBackDialogFragment.this.isAdded() && FeedBackDialogFragment.this.getActivity() != null) {
                AppRater.showRateDialog(FeedBackDialogFragment.this.getActivity());
            }
            FeedBackDialogFragment.this.dismiss();
        }
    }

    public static FeedBackDialogFragment newInstance(String frequency) {
        FeedBackDialogFragment dialog = new FeedBackDialogFragment();
        Bundle args = new Bundle();
        args.putString(stations.frequency, frequency);
        dialog.setArguments(args);
        return dialog;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(2, C1136R.style.CustomDialogTheme);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            this.frequency = getArguments().getString(stations.frequency);
        }
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(C1136R.layout.feedback_dialog_options, container, false);
        ListView lv = (ListView) view.findViewById(C1136R.id.listview);
        ((ImageView) view.findViewById(C1136R.id.close_dialog_imageview)).setOnClickListener(new C11481());
        String[] feedBackTypesList = getActivity().getResources().getStringArray(C1136R.array.feed_back_types);
        lv.setAdapter(new FeedBackEventAdapter(getActivity(), 0, feedBackTypesList));
        lv.setOnItemClickListener(new C11492(feedBackTypesList));
        return view;
    }

    private void shareFeedback(String emailId, String subjectName, String body) throws NameNotFoundException {
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setPackage("com.google.android.gm");
        emailIntent.setType("plain/text");
        emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{emailId});
        emailIntent.putExtra("android.intent.extra.SUBJECT", subjectName);
        emailIntent.putExtra("android.intent.extra.TEXT", getResources().getString(C1136R.string.station_info) + ": " + this.frequency + "\n\n" + body);
        getActivity().setRequestedOrientation(0);
        getActivity().startActivity(Intent.createChooser(emailIntent, getResources().getString(C1136R.string.feedback_via) + "..."));
    }
}
