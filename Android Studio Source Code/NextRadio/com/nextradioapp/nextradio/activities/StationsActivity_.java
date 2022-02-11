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
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.nextradioapp.nextradio.fragments.NavigationDrawerFragment;
import com.nextradioapp.nextradio.ottos.NRDataMode;
import com.nextradioapp.nextradio.ottos.NRDialogEvent;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRNavigationEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.nextradioapp.nextradio.ottos.NRRadioAvailabilityEvent;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.apache.activemq.transport.stomp.Stomp;

public final class StationsActivity_ extends StationsActivity implements HasViews, OnViewChangedListener {
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.1 */
    class C11971 implements Runnable {
        final /* synthetic */ NRDataMode val$nrDataMode;

        C11971(NRDataMode nRDataMode) {
            this.val$nrDataMode = nRDataMode;
        }

        public void run() {
            super.onNRDataMode(this.val$nrDataMode);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.2 */
    class C11982 implements Runnable {
        final /* synthetic */ NRRadioAvailabilityEvent val$radioAvailabilityEvent;

        C11982(NRRadioAvailabilityEvent nRRadioAvailabilityEvent) {
            this.val$radioAvailabilityEvent = nRRadioAvailabilityEvent;
        }

        public void run() {
            super.headsetDisconnected(this.val$radioAvailabilityEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.3 */
    class C11993 implements Runnable {
        final /* synthetic */ NRDialogEvent val$dialogEvent;

        C11993(NRDialogEvent nRDialogEvent) {
            this.val$dialogEvent = nRDialogEvent;
        }

        public void run() {
            super.renderDialogFragment(this.val$dialogEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.4 */
    class C12004 implements Runnable {
        final /* synthetic */ NRNavigationEvent val$event;

        C12004(NRNavigationEvent nRNavigationEvent) {
            this.val$event = nRNavigationEvent;
        }

        public void run() {
            super.onNRNavigationEvent(this.val$event);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.5 */
    class C12015 implements Runnable {
        final /* synthetic */ NRStationList val$stationListEvent;

        C12015(NRStationList nRStationList) {
            this.val$stationListEvent = nRStationList;
        }

        public void run() {
            super.onStationsAutoUpdating(this.val$stationListEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.6 */
    class C12026 implements Runnable {
        final /* synthetic */ NRInitCompleted val$action;

        C12026(NRInitCompleted nRInitCompleted) {
            this.val$action = nRInitCompleted;
        }

        public void run() {
            super.onInitCompleted(this.val$action);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.7 */
    class C12037 extends Task {
        C12037(String x0, int x1, String x2) {
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

    /* renamed from: com.nextradioapp.nextradio.activities.StationsActivity_.8 */
    class C12048 extends Task {
        C12048(String x0, int x1, String x2) {
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

    public static class IntentBuilder_ {
        private Context context_;
        private Fragment fragmentSupport_;
        private android.app.Fragment fragment_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            this.context_ = context;
            this.intent_ = new Intent(context, StationsActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, StationsActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, StationsActivity_.class);
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

    public StationsActivity_() {
        this.onViewChangedNotifier_ = new OnViewChangedNotifier();
        this.handler_ = new Handler(Looper.getMainLooper());
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(2130903044);
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
        this.adviewHolder = (FrameLayout) hasViews.findViewById(2131689514);
        this.sliding_layout = (SlidingUpPanelLayout) hasViews.findViewById(2131689523);
        this.container = (LinearLayout) hasViews.findViewById(2131689524);
        this.drawer_layout = (DrawerLayout) hasViews.findViewById(2131689511);
        this.mNavigationDrawerFragment = (NavigationDrawerFragment) findNativeFragmentById(2131689520);
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
    public void onNRDataMode(NRDataMode nrDataMode) {
        this.handler_.post(new C11971(nrDataMode));
    }

    @Subscribe
    public void headsetDisconnected(NRRadioAvailabilityEvent radioAvailabilityEvent) {
        this.handler_.post(new C11982(radioAvailabilityEvent));
    }

    @Subscribe
    public void renderDialogFragment(NRDialogEvent dialogEvent) {
        this.handler_.post(new C11993(dialogEvent));
    }

    @Subscribe
    public void onNRNavigationEvent(NRNavigationEvent event) {
        this.handler_.post(new C12004(event));
    }

    @Subscribe
    public void onNRRadioAction(NRRadioAction tune) {
        super.onNRRadioAction(tune);
    }

    @Subscribe
    public void onStationsAutoUpdating(NRStationList stationListEvent) {
        this.handler_.post(new C12015(stationListEvent));
    }

    @Subscribe
    public void onInitCompleted(NRInitCompleted action) {
        this.handler_.post(new C12026(action));
    }

    public void doSDKInitInBackground() {
        BackgroundExecutor.execute(new C12037(Stomp.EMPTY, 0, Stomp.EMPTY));
    }

    public void checkRegister() {
        BackgroundExecutor.execute(new C12048(Stomp.EMPTY, 0, Stomp.EMPTY));
    }
}
