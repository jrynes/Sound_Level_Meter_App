package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.nextradio.fragments.NavigationDrawerFragment;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDialogEvent;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;
import com.nextradioapp.nextradio.ottos.NRRadioResult;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.apache.activemq.transport.stomp.Stomp;

public final class TabletFragContainerActivity_ extends TabletFragContainerActivity implements HasViews, OnViewChangedListener {
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    class 10 implements Runnable {
        final /* synthetic */ NextRadioEventInfo val$event;

        10(NextRadioEventInfo nextRadioEventInfo) {
            this.val$event = nextRadioEventInfo;
        }

        public void run() {
            super.updateViews(this.val$event);
        }
    }

    class 11 extends Task {
        11(String x0, int x1, String x2) {
            super(x0, x1, x2);
        }

        public void execute() {
            try {
                super.doSDKInitInBackground();
            } catch (Throwable e) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
    }

    class 12 extends Task {
        12(String x0, int x1, String x2) {
            super(x0, x1, x2);
        }

        public void execute() {
            try {
                super.checkRegister();
            } catch (Throwable e) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.1 */
    class C12141 implements OnClickListener {
        C12141() {
        }

        public void onClick(View view) {
            TabletFragContainerActivity_.this.restoreFULLPOWER();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.2 */
    class C12152 implements Runnable {
        final /* synthetic */ NRDialogEvent val$dialogEvent;

        C12152(NRDialogEvent nRDialogEvent) {
            this.val$dialogEvent = nRDialogEvent;
        }

        public void run() {
            super.renderDialogFragment(this.val$dialogEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.3 */
    class C12163 implements Runnable {
        final /* synthetic */ NRStationList val$stationListEvent;

        C12163(NRStationList nRStationList) {
            this.val$stationListEvent = nRStationList;
        }

        public void run() {
            super.onStationsAutoUpdating(this.val$stationListEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.4 */
    class C12174 implements Runnable {
        final /* synthetic */ NRRadioResult val$result;

        C12174(NRRadioResult nRRadioResult) {
            this.val$result = nRRadioResult;
        }

        public void run() {
            super.radioResult(this.val$result);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.5 */
    class C12185 implements Runnable {
        final /* synthetic */ NRDataMode val$dataMode;

        C12185(NRDataMode nRDataMode) {
            this.val$dataMode = nRDataMode;
        }

        public void run() {
            super.dataChange(this.val$dataMode);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.6 */
    class C12196 implements Runnable {
        final /* synthetic */ NRInitCompleted val$action;

        C12196(NRInitCompleted nRInitCompleted) {
            this.val$action = nRInitCompleted;
        }

        public void run() {
            super.onInitCompleted(this.val$action);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.7 */
    class C12207 implements Runnable {
        final /* synthetic */ NRNavigationEvent val$event;

        C12207(NRNavigationEvent nRNavigationEvent) {
            this.val$event = nRNavigationEvent;
        }

        public void run() {
            super.onNRNavigationEvent(this.val$event);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.8 */
    class C12218 implements Runnable {
        final /* synthetic */ NRRadioAvailabilityEvent val$radioAvailabilityEvent;

        C12218(NRRadioAvailabilityEvent nRRadioAvailabilityEvent) {
            this.val$radioAvailabilityEvent = nRRadioAvailabilityEvent;
        }

        public void run() {
            super.headsetDisconnected(this.val$radioAvailabilityEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.TabletFragContainerActivity_.9 */
    class C12229 implements Runnable {
        final /* synthetic */ NRDataMode val$nrDataMode;

        C12229(NRDataMode nRDataMode) {
            this.val$nrDataMode = nRDataMode;
        }

        public void run() {
            super.onNRDataMode(this.val$nrDataMode);
        }
    }

    public static class IntentBuilder_ {
        private Context context_;
        private Fragment fragmentSupport_;
        private android.app.Fragment fragment_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            this.context_ = context;
            this.intent_ = new Intent(context, TabletFragContainerActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, TabletFragContainerActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, TabletFragContainerActivity_.class);
        }

        public Intent get() {
            return this.intent_;
        }

        public IntentBuilder_ flags(int flags) {
            this.intent_.setFlags(flags);
            return this;
        }

        public void start() {
            this.context_.startActivity(this.intent_);
        }

        public void startForResult(int requestCode) {
            if (this.fragmentSupport_ != null) {
                this.fragmentSupport_.startActivityForResult(this.intent_, requestCode);
            } else if (this.fragment_ != null) {
                this.fragment_.startActivityForResult(this.intent_, requestCode);
            } else if (this.context_ instanceof Activity) {
                ((Activity) this.context_).startActivityForResult(this.intent_, requestCode);
            } else {
                this.context_.startActivity(this.intent_);
            }
        }
    }

    public TabletFragContainerActivity_() {
        this.onViewChangedNotifier_ = new OnViewChangedNotifier();
        this.handler_ = new Handler(Looper.getMainLooper());
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(2130903042);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public void setContentView(View view) {
        super.setContentView(view);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static IntentBuilder_ intent(Context context) {
        return new IntentBuilder_(context);
    }

    public static IntentBuilder_ intent(android.app.Fragment fragment) {
        return new IntentBuilder_(fragment);
    }

    public static IntentBuilder_ intent(Fragment supportFragment) {
        return new IntentBuilder_(supportFragment);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (SdkVersionHelper.getSdkInt() < 5 && keyCode == 4 && event.getRepeatCount() == 0) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onViewChanged(HasViews hasViews) {
        this.layout_with_data_mode = (LinearLayout) hasViews.findViewById(2131689512);
        this.layout_with_no_data_mode = (LinearLayout) hasViews.findViewById(2131689517);
        this.fragment_container_guide_no_data = (FrameLayout) hasViews.findViewById(2131689519);
        this.drawer_layout = (DrawerLayout) hasViews.findViewById(2131689511);
        this.fragment_container_guide = (FrameLayout) hasViews.findViewById(2131689513);
        this.buttonEnhancedMode = (Button) hasViews.findViewById(2131689518);
        this.adviewHolder = (FrameLayout) hasViews.findViewById(2131689514);
        this.mNavigationDrawerFragment = (NavigationDrawerFragment) findNativeFragmentById(2131689520);
        View view = hasViews.findViewById(2131689518);
        if (view != null) {
            view.setOnClickListener(new C12141());
        }
        afterViews();
    }

    private android.app.Fragment findNativeFragmentById(int id) {
        return getFragmentManager().findFragmentById(id);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() != 16908332) {
            return false;
        }
        onHomeMenuButtonPushed(item);
        return true;
    }

    @Subscribe
    public void renderDialogFragment(NRDialogEvent dialogEvent) {
        this.handler_.post(new C12152(dialogEvent));
    }

    @Subscribe
    public void onStationsAutoUpdating(NRStationList stationListEvent) {
        this.handler_.post(new C12163(stationListEvent));
    }

    @Subscribe
    public void radioResult(NRRadioResult result) {
        this.handler_.post(new C12174(result));
    }

    @Subscribe
    public void onEventChanged(NRCurrentEvent event) {
        super.onEventChanged(event);
    }

    @Subscribe
    public void dataChange(NRDataMode dataMode) {
        this.handler_.post(new C12185(dataMode));
    }

    @Subscribe
    public void onInitCompleted(NRInitCompleted action) {
        this.handler_.post(new C12196(action));
    }

    @Subscribe
    public void onNRNavigationEvent(NRNavigationEvent event) {
        this.handler_.post(new C12207(event));
    }

    @Subscribe
    public void headsetDisconnected(NRRadioAvailabilityEvent radioAvailabilityEvent) {
        this.handler_.post(new C12218(radioAvailabilityEvent));
    }

    @Subscribe
    public void onNRDataMode(NRDataMode nrDataMode) {
        this.handler_.post(new C12229(nrDataMode));
    }

    public void updateViews(NextRadioEventInfo event) {
        this.handler_.post(new 10(event));
    }

    public void doSDKInitInBackground() {
        BackgroundExecutor.execute(new 11(Stomp.EMPTY, 0, Stomp.EMPTY));
    }

    public void checkRegister() {
        BackgroundExecutor.execute(new 12(Stomp.EMPTY, 0, Stomp.EMPTY));
    }
}
