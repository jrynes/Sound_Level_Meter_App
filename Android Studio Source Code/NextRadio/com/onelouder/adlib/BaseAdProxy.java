package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public abstract class BaseAdProxy implements I1LouderAdProxy {
    protected SoftReference<Activity> activityRef;
    protected boolean expanded;
    protected AdPlacement mAdPlacement;
    protected AdView mAdViewParent;
    private Context mContext;
    protected boolean mInterstitialRequested;

    /* renamed from: com.onelouder.adlib.BaseAdProxy.1 */
    class C12941 implements Runnable {
        C12941() {
        }

        public void run() {
            BaseAdProxy.this.mAdViewParent.requestAd(false);
        }
    }

    protected abstract String TAG();

    BaseAdProxy() {
        this.mInterstitialRequested = false;
        this.expanded = false;
    }

    public BaseAdProxy(Context context, AdPlacement placement, AdView parent) {
        this.mInterstitialRequested = false;
        this.expanded = false;
        this.mAdViewParent = parent;
        this.mAdPlacement = placement;
        this.mContext = context;
    }

    public BaseAdProxy(Activity activity, AdPlacement placement) {
        this.mInterstitialRequested = false;
        this.expanded = false;
        if (placement != null) {
            this.activityRef = new SoftReference(activity);
            this.mAdPlacement = placement;
        }
    }

    protected Context getContext() {
        if (this.mContext != null) {
            return this.mContext;
        }
        if (this.activityRef != null) {
            return (Context) this.activityRef.get();
        }
        if (this.mAdViewParent != null) {
            return this.mAdViewParent.getContext();
        }
        Diagnostics.m1952e(TAG(), "getContext() is null");
        return null;
    }

    public void setAdViewParent(AdView parent) {
        this.mAdViewParent = parent;
    }

    public void setAdPlacement(AdPlacement placement) {
        this.mAdPlacement = placement;
    }

    public void onAddedToListView() {
    }

    public void destroy() {
        Diagnostics.m1951d(TAG(), "destroy");
    }

    public void resume() {
        Diagnostics.m1951d(TAG(), "resume");
    }

    public void start() {
        Diagnostics.m1951d(TAG(), "start");
    }

    public void stop() {
        Diagnostics.m1951d(TAG(), "stop");
    }

    public void pause() {
        Diagnostics.m1951d(TAG(), "pause");
        if (this.mAdPlacement == null) {
            return;
        }
        if (this.mAdPlacement.isRecycleable() || this.mAdPlacement.isCloneable()) {
            this.mAdPlacement.setAdProxy(this);
        }
    }

    public boolean isFullScreen() {
        return false;
    }

    public void setVisibility(int visibility) {
        if (getProxiedView() != null) {
            getProxiedView().setVisibility(visibility);
        }
    }

    public int getVisibility() {
        if (getProxiedView() != null) {
            return getProxiedView().getVisibility();
        }
        return 8;
    }

    public int getHeight() {
        int height = Utils.getDIP(50.0d);
        if (this.mAdPlacement.isXLarge() || (this.mAdPlacement.isLarge() && this.mAdPlacement.isTvDensity())) {
            return Utils.getDIP(90.0d);
        }
        return height;
    }

    public int getAnimationDelay() {
        return 0;
    }

    public boolean isInterstitialRequested() {
        return this.mInterstitialRequested;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    protected void logTargetParams(HashMap<String, Object> targetParams) {
        for (String key : targetParams.keySet()) {
            Object item = targetParams.get(key);
            if (item != null) {
                Diagnostics.m1955i(TAG(), key + "=" + item.toString());
            }
        }
    }

    protected void logPermissionCheck(Context context) {
        if (!checkWriteExternalStoragePermission(context)) {
            Log.w(TAG(), "android.permission.WRITE_EXTERNAL_STORAGE is missing from the manifest");
        }
    }

    protected void onAdRequestFailed(int errorCode, String errorMessage) {
        try {
            Diagnostics.m1951d(TAG(), "onAdRequestFailed, errorCode=" + errorMessage);
            HashMap<String, Object> params = new HashMap();
            params.put(AdPlacement.EXTRA_1L_ERROR_MESSAGE, errorMessage);
            params.put(AdPlacement.EXTRA_1L_ERROR_CODE, Integer.toString(errorCode));
            this.mAdPlacement.sendBroadcast(getContext(), AdPlacement.ACTION_1L_ADVIEW_REQ_FAILED, params);
            if (this.mAdPlacement.getRolloverId() == null) {
                this.mAdViewParent.requestFailed(errorCode, errorMessage);
            } else if (!this.mAdPlacement.ispaused()) {
                AdView.getStaticHandler().post(new C12941());
            } else if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1952e(TAG(), "isPaused=true, do not request rollover ad");
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    protected static boolean checkWriteExternalStoragePermission(Context context) {
        if (context == null || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }
}
