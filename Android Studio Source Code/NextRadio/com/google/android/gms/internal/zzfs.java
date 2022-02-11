package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Error;
import org.json.JSONObject;

@zzhb
public class zzfs {
    private final String zzDJ;
    private final zzjp zzpD;

    public zzfs(zzjp com_google_android_gms_internal_zzjp) {
        this(com_google_android_gms_internal_zzjp, Stomp.EMPTY);
    }

    public zzfs(zzjp com_google_android_gms_internal_zzjp, String str) {
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzDJ = str;
    }

    public void zza(int i, int i2, int i3, int i4, float f, int i5) {
        try {
            this.zzpD.zzb("onScreenInfoChanged", new JSONObject().put(SettingsJsonConstants.ICON_WIDTH_KEY, i).put(SettingsJsonConstants.ICON_HEIGHT_KEY, i2).put("maxSizeWidth", i3).put("maxSizeHeight", i4).put("density", (double) f).put("rotation", i5));
        } catch (Throwable e) {
            zzb.zzb("Error occured while obtaining screen information.", e);
        }
    }

    public void zzam(String str) {
        try {
            this.zzpD.zzb("onError", new JSONObject().put(Error.MESSAGE, str).put(locationTracking.action, this.zzDJ));
        } catch (Throwable e) {
            zzb.zzb("Error occurred while dispatching error event.", e);
        }
    }

    public void zzan(String str) {
        try {
            this.zzpD.zzb("onReadyEventReceived", new JSONObject().put("js", str));
        } catch (Throwable e) {
            zzb.zzb("Error occured while dispatching ready Event.", e);
        }
    }

    public void zzao(String str) {
        try {
            this.zzpD.zzb("onStateChanged", new JSONObject().put("state", str));
        } catch (Throwable e) {
            zzb.zzb("Error occured while dispatching state change.", e);
        }
    }

    public void zzb(int i, int i2, int i3, int i4) {
        try {
            this.zzpD.zzb("onSizeChanged", new JSONObject().put("x", i).put("y", i2).put(SettingsJsonConstants.ICON_WIDTH_KEY, i3).put(SettingsJsonConstants.ICON_HEIGHT_KEY, i4));
        } catch (Throwable e) {
            zzb.zzb("Error occured while dispatching size change.", e);
        }
    }

    public void zzc(int i, int i2, int i3, int i4) {
        try {
            this.zzpD.zzb("onDefaultPositionReceived", new JSONObject().put("x", i).put("y", i2).put(SettingsJsonConstants.ICON_WIDTH_KEY, i3).put(SettingsJsonConstants.ICON_HEIGHT_KEY, i4));
        } catch (Throwable e) {
            zzb.zzb("Error occured while dispatching default position.", e);
        }
    }
}
