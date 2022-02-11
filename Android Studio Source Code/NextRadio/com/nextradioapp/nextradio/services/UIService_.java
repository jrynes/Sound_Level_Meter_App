package com.nextradioapp.nextradio.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import com.nextradioapp.nextradio.ottos.NRCurrentEvent;
import com.nextradioapp.nextradio.ottos.NRRadioAction;
import com.squareup.otto.Subscribe;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.apache.activemq.transport.stomp.Stomp;

public final class UIService_ extends UIService {
    private Handler handler_;

    /* renamed from: com.nextradioapp.nextradio.services.UIService_.1 */
    class C12351 implements Runnable {
        C12351() {
        }

        public void run() {
            super.crashTest();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.services.UIService_.2 */
    class C12362 extends Task {
        final /* synthetic */ int val$currentFrequency;

        C12362(String x0, int x1, String x2, int i) {
            this.val$currentFrequency = i;
            super(x0, x1, x2);
        }

        public void execute() {
            try {
                super.initSDKInBackground(this.val$currentFrequency);
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
            this.intent_ = new Intent(context, UIService_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, UIService_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, UIService_.class);
        }

        public Intent get() {
            return this.intent_;
        }

        public IntentBuilder_ flags(int flags) {
            this.intent_.setFlags(flags);
            return this;
        }

        public ComponentName start() {
            return this.context_.startService(this.intent_);
        }

        public boolean stop() {
            return this.context_.stopService(this.intent_);
        }
    }

    public UIService_() {
        this.handler_ = new Handler(Looper.getMainLooper());
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

    @Subscribe
    public void onRadioAction(NRRadioAction radioAction) {
        super.onRadioAction(radioAction);
    }

    @Subscribe
    public void onNewCurrentEvent(NRCurrentEvent event) {
        super.onNewCurrentEvent(event);
    }

    public void crashTest() {
        this.handler_.post(new C12351());
    }

    public void initSDKInBackground(int currentFrequency) {
        BackgroundExecutor.execute(new C12362(Stomp.EMPTY, 0, Stomp.EMPTY, currentFrequency));
    }
}
