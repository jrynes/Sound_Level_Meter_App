package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.ViewGroup;
import com.amazon.device.ads.AdActivity.IAdActivityAdapter;
import com.amazon.device.ads.SDKEvent.SDKEventType;
import org.xbill.DNS.KEYRecord.Flags;

@SuppressLint({"NewApi"})
class InterstitialAdActivityAdapter implements IAdActivityAdapter {
    private static final String LOGTAG;
    private Activity activity;
    private AdController adController;
    private final MobileAdsLogger logger;

    class InterstitialAdSDKEventListener implements SDKEventListener {
        InterstitialAdSDKEventListener() {
        }

        public void onSDKEvent(SDKEvent sdkEvent, AdControlAccessor controller) {
            if (sdkEvent.getEventType().equals(SDKEventType.CLOSED) && !InterstitialAdActivityAdapter.this.activity.isFinishing()) {
                InterstitialAdActivityAdapter.this.adController = null;
                InterstitialAdActivityAdapter.this.activity.finish();
            }
        }
    }

    InterstitialAdActivityAdapter() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.activity = null;
    }

    static {
        LOGTAG = InterstitialAdActivityAdapter.class.getSimpleName();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void preOnCreate() {
        this.activity.requestWindowFeature(1);
        this.activity.getWindow().setFlags(Flags.FLAG5, Flags.FLAG5);
        AndroidTargetUtils.hideActionAndStatusBars(this.activity);
    }

    public void onCreate() {
        AndroidTargetUtils.enableHardwareAcceleration(this.activity.getWindow());
        this.adController = getAdController();
        if (this.adController == null) {
            this.logger.m639e("Failed to show interstitial ad due to an error in the Activity.");
            InterstitialAd.resetIsAdShowing();
            this.activity.finish();
            return;
        }
        this.adController.setAdActivity(this.activity);
        this.adController.addSDKEventListener(new InterstitialAdSDKEventListener());
        ViewGroup parent = (ViewGroup) this.adController.getView().getParent();
        if (parent != null) {
            parent.removeView(this.adController.getView());
        }
        this.activity.setContentView(this.adController.getView());
        this.adController.adShown();
    }

    AdController getAdController() {
        return AdControllerFactory.getCachedAdController();
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStop() {
        if (this.activity.isFinishing() && this.adController != null) {
            this.adController.closeAd();
        }
    }

    Activity getActivity() {
        return this.activity;
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public boolean onBackPressed() {
        if (this.adController != null) {
            return this.adController.onBackButtonPress();
        }
        return false;
    }
}
