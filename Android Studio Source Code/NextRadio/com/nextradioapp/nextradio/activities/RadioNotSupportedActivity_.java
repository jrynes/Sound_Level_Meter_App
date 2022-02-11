package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RadioNotSupportedActivity_ extends RadioNotSupportedActivity implements HasViews, OnViewChangedListener {
    private final OnViewChangedNotifier onViewChangedNotifier_;

    /* renamed from: com.nextradioapp.nextradio.activities.RadioNotSupportedActivity_.1 */
    class C11791 implements OnClickListener {
        C11791() {
        }

        public void onClick(View view) {
            RadioNotSupportedActivity_.this.btnRadioSkip_Click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.RadioNotSupportedActivity_.2 */
    class C11802 implements OnClickListener {
        C11802() {
        }

        public void onClick(View view) {
            RadioNotSupportedActivity_.this.btnOtherCountrySignUp_Click();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.RadioNotSupportedActivity_.3 */
    class C11813 implements OnClickListener {
        C11813() {
        }

        public void onClick(View view) {
            RadioNotSupportedActivity_.this.btnRadioLink_Click();
        }
    }

    public static class IntentBuilder_ {
        private Context context_;
        private Fragment fragmentSupport_;
        private android.app.Fragment fragment_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            this.context_ = context;
            this.intent_ = new Intent(context, RadioNotSupportedActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, RadioNotSupportedActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, RadioNotSupportedActivity_.class);
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

    public RadioNotSupportedActivity_() {
        this.onViewChangedNotifier_ = new OnViewChangedNotifier();
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(2130903108);
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
        this.signUpForUpdates = (Button) hasViews.findViewById(2131689773);
        this.otherCountryMsgLayout = (LinearLayout) hasViews.findViewById(2131689767);
        this.txtNoRadioDescription = (TextView) hasViews.findViewById(2131689771);
        this.txtNoRadioLink = (Button) hasViews.findViewById(2131689772);
        View view = hasViews.findViewById(2131689653);
        if (view != null) {
            view.setOnClickListener(new C11791());
        }
        view = hasViews.findViewById(2131689773);
        if (view != null) {
            view.setOnClickListener(new C11802());
        }
        view = hasViews.findViewById(2131689772);
        if (view != null) {
            view.setOnClickListener(new C11813());
        }
        afterViews();
    }
}
