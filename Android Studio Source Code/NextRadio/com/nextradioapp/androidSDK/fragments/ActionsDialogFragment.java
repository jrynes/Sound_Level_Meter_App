package com.nextradioapp.androidSDK.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.androidSDK.NextRadioAndroid;
import com.nextradioapp.androidSDK.adapters.EventActionArrayAdapter;
import com.nextradioapp.androidSDK.data.NextRadioEventInfoWrapper;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.EventActionSet;
import com.nextradioapp.core.objects.NextRadioEventInfo;

public class ActionsDialogFragment extends DialogFragment {
    public static final String PARENT_TAG_KEY = "parentFragment";
    private static NextRadioEventInfo mEvent;
    private EventAction[] mActions;
    private String mParentTag;

    /* renamed from: com.nextradioapp.androidSDK.fragments.ActionsDialogFragment.1 */
    class C11471 implements OnItemClickListener {
        C11471() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            try {
                ActionsDialogFragment.this.mActions[position].start(2);
                ActionsDialogFragment.this.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ActionsDialogFragment newInstance(NextRadioEventInfo event) {
        ActionsDialogFragment dialog = new ActionsDialogFragment();
        mEvent = event;
        Bundle args = new Bundle();
        args.putParcelable("eventInfo", new NextRadioEventInfoWrapper(event));
        dialog.setArguments(args);
        return dialog;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = (NextRadioEventInfo) getArguments().getParcelable("eventInfo");
        this.mParentTag = getArguments().getString(PARENT_TAG_KEY);
        setStyle(2, C1136R.style.CustomDialogTheme);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        if (getArguments() != null && getArguments().containsKey("eventInfo")) {
            mEvent = (NextRadioEventInfoWrapper) getArguments().getParcelable("eventInfo");
            this.mParentTag = getArguments().getString(PARENT_TAG_KEY);
        }
        View view = inflater.inflate(C1136R.layout.dialog_options, container, false);
        ListView lv = (ListView) view.findViewById(C1136R.id.listview);
        EventActionSet theSet = new EventActionSet(mEvent, NextRadioAndroid.getInstance().getEventActions(mEvent));
        this.mActions = (EventAction[]) theSet.secondary.toArray(new EventAction[theSet.secondary.size()]);
        lv.setAdapter(new EventActionArrayAdapter(getActivity(), 0, this.mActions));
        lv.setOnItemClickListener(new C11471());
        return view;
    }
}
