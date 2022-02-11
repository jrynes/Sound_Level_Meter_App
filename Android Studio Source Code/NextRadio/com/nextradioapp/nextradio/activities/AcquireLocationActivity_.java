package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.nextradioapp.androidSDK.C1136R;
import com.nextradioapp.core.objects.PostalCodeInfo;
import com.nextradioapp.nextradio.ottos.NRInitCompleted;
import com.nextradioapp.nextradio.ottos.NRStationList;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import org.apache.activemq.transport.stomp.Stomp;

public final class AcquireLocationActivity_ extends AcquireLocationActivity implements HasViews, OnViewChangedListener {
    private Handler handler_;
    private final OnViewChangedNotifier onViewChangedNotifier_;

    class 10 implements Runnable {
        10() {
        }

        public void run() {
            super.showNoStations();
        }
    }

    class 11 implements Runnable {
        11() {
        }

        public void run() {
            super.showManualEntry();
        }
    }

    class 12 extends Task {
        12(String x0, int x1, String x2) {
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

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.1 */
    class C11641 implements OnClickListener {
        C11641() {
        }

        public void onClick(View view) {
            AcquireLocationActivity_.this.setAcquireLocationCloseButton_click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.2 */
    class C11652 implements OnClickListener {
        C11652() {
        }

        public void onClick(View view) {
            AcquireLocationActivity_.this.btnNetworkTryAgain_click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.3 */
    class C11663 implements OnClickListener {
        C11663() {
        }

        public void onClick(View view) {
            AcquireLocationActivity_.this.basicTuner_Click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.4 */
    class C11674 implements OnClickListener {
        C11674() {
        }

        public void onClick(View view) {
            AcquireLocationActivity_.this.btnLocationZip_Click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.5 */
    class C11685 implements Runnable {
        final /* synthetic */ NRStationList val$stations;

        C11685(NRStationList nRStationList) {
            this.val$stations = nRStationList;
        }

        public void run() {
            super.onStationsUpdated(this.val$stations);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.6 */
    class C11696 implements Runnable {
        final /* synthetic */ NRInitCompleted val$initEvent;

        C11696(NRInitCompleted nRInitCompleted) {
            this.val$initEvent = nRInitCompleted;
        }

        public void run() {
            super.onInitCompleted(this.val$initEvent);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.7 */
    class C11707 implements Runnable {
        final /* synthetic */ PostalCodeInfo val$postalCodeInfo;

        C11707(PostalCodeInfo postalCodeInfo) {
            this.val$postalCodeInfo = postalCodeInfo;
        }

        public void run() {
            super.updateRadioButton(this.val$postalCodeInfo);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.8 */
    class C11718 implements Runnable {
        C11718() {
        }

        public void run() {
            super.showUpdating();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.AcquireLocationActivity_.9 */
    class C11729 implements Runnable {
        final /* synthetic */ String val$msg;

        C11729(String str) {
            this.val$msg = str;
        }

        public void run() {
            super.showErrorMessage(this.val$msg);
        }
    }

    public static class IntentBuilder_ {
        private Context context_;
        private Fragment fragmentSupport_;
        private android.app.Fragment fragment_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            this.context_ = context;
            this.intent_ = new Intent(context, AcquireLocationActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, AcquireLocationActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, AcquireLocationActivity_.class);
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

    public AcquireLocationActivity_() {
        this.onViewChangedNotifier_ = new OnViewChangedNotifier();
        this.handler_ = new Handler(Looper.getMainLooper());
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(2130903041);
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

    public void onViewChanged(HasViews hasViews) {
        this.dummyView = hasViews.findViewById(2131689493);
        this.txtErrorMessage = (TextView) hasViews.findViewById(2131689502);
        this.editText = (EditText) hasViews.findViewById(2131689497);
        this.txtCountryNotAvailable = (TextView) hasViews.findViewById(2131689499);
        this.txtNoStationListing = (TextView) hasViews.findViewById(2131689505);
        this.progressBar = (ProgressBar) hasViews.findViewById(2131689492);
        this.progressBarLoadingZip = (ProgressBar) hasViews.findViewById(2131689503);
        this.imageView = (ImageView) hasViews.findViewById(C1136R.id.imageView);
        this.textView = (TextView) hasViews.findViewById(C1136R.id.textView);
        this.acquireLocationCloseButton = (TextView) hasViews.findViewById(2131689510);
        this.acquiringLocationTextView = (TextView) hasViews.findViewById(2131689489);
        this.manualEntry = (LinearLayout) hasViews.findViewById(2131689494);
        this.btnBasicTuner = (Button) hasViews.findViewById(2131689506);
        this.linearLayoutUpdatingStations = (LinearLayout) hasViews.findViewById(2131689490);
        this.layoutNetworkConnectivity = (LinearLayout) hasViews.findViewById(2131689507);
        this.spinnerCountry = (Spinner) hasViews.findViewById(2131689496);
        this.btnGetLocationZip = (Button) hasViews.findViewById(2131689498);
        this.linearLayoutNoStations = (LinearLayout) hasViews.findViewById(2131689504);
        this.FrameLayout2 = (FrameLayout) hasViews.findViewById(2131689500);
        this.radioButton = (RadioButton) hasViews.findViewById(2131689501);
        View view = hasViews.findViewById(2131689510);
        if (view != null) {
            view.setOnClickListener(new C11641());
        }
        view = hasViews.findViewById(2131689509);
        if (view != null) {
            view.setOnClickListener(new C11652());
        }
        view = hasViews.findViewById(2131689506);
        if (view != null) {
            view.setOnClickListener(new C11663());
        }
        view = hasViews.findViewById(2131689498);
        if (view != null) {
            view.setOnClickListener(new C11674());
        }
        onAfterViews();
    }

    @Subscribe
    public void onStationsUpdated(NRStationList stations) {
        this.handler_.post(new C11685(stations));
    }

    @Subscribe
    public void onInitCompleted(NRInitCompleted initEvent) {
        this.handler_.post(new C11696(initEvent));
    }

    public void updateRadioButton(PostalCodeInfo postalCodeInfo) {
        this.handler_.post(new C11707(postalCodeInfo));
    }

    public void showUpdating() {
        this.handler_.post(new C11718());
    }

    public void showErrorMessage(String msg) {
        this.handler_.post(new C11729(msg));
    }

    public void showNoStations() {
        this.handler_.post(new 10());
    }

    public void showManualEntry() {
        this.handler_.post(new 11());
    }

    public void doSDKInitInBackground() {
        BackgroundExecutor.execute(new 12(Stomp.EMPTY, 0, Stomp.EMPTY));
    }
}
