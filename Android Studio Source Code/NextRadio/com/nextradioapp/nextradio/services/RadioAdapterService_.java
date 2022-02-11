package com.nextradioapp.nextradio.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public final class RadioAdapterService_ extends RadioAdapterService {

    public static class IntentBuilder_ {
        private Context context_;
        private Fragment fragmentSupport_;
        private android.app.Fragment fragment_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            this.context_ = context;
            this.intent_ = new Intent(context, RadioAdapterService_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            this.fragment_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, RadioAdapterService_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            this.fragmentSupport_ = fragment;
            this.context_ = fragment.getActivity();
            this.intent_ = new Intent(this.context_, RadioAdapterService_.class);
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

    public static IntentBuilder_ intent(Context context) {
        return new IntentBuilder_(context);
    }

    public static IntentBuilder_ intent(android.app.Fragment fragment) {
        return new IntentBuilder_(fragment);
    }

    public static IntentBuilder_ intent(Fragment supportFragment) {
        return new IntentBuilder_(supportFragment);
    }
}
