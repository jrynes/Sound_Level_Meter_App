package com.onelouder.adlib;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.google.android.gms.analytics.ecommerce.Promotion;

public class AdOverlay extends FrameLayout {
    private boolean bViewTouched;
    private AdPlacement mAdPlacement;
    private int mCount;
    private Handler mHandler;

    /* renamed from: com.onelouder.adlib.AdOverlay.1 */
    class C12831 implements Runnable {
        C12831() {
        }

        public void run() {
            if (AdOverlay.this.mCount == 8) {
                if (Diagnostics.getInstance().isEnabled(4)) {
                    Diagnostics.getInstance().setLogLevel(0);
                } else {
                    Diagnostics.getInstance().setLogLevel(6);
                }
            }
            AdOverlay.this.mCount = 0;
        }
    }

    public AdOverlay(Context context) {
        super(context, null);
        this.mHandler = new Handler();
        this.bViewTouched = false;
        this.mCount = 0;
    }

    public AdOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new Handler();
        this.bViewTouched = false;
        this.mCount = 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event != null && event.getAction() == 0) {
            if (this.mAdPlacement != null) {
                if (this.mAdPlacement.getNetwork().equals("admarvel")) {
                    Preferences.setSimplePref(getContext(), "admarvel-clickcnt", Preferences.getSimplePref(getContext(), "admarvel-clickcnt", 0) + 1);
                }
                this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_CLICKED, null);
                SendAdUsage.trackEvent(getContext(), this.mAdPlacement, Promotion.ACTION_CLICK, null, null);
                this.bViewTouched = true;
            }
            if (this.mCount == 0) {
                this.mHandler.postDelayed(new C12831(), 2000);
            }
            this.mCount++;
        }
        return super.onTouchEvent(event);
    }

    public void setAdPlacement(AdPlacement placement) {
        this.mAdPlacement = placement;
    }

    public void resetTouch() {
        this.bViewTouched = false;
    }

    public boolean wasTouched() {
        return this.bViewTouched;
    }
}
