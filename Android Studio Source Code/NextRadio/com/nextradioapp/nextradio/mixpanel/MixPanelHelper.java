package com.nextradioapp.nextradio.mixpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.mixpanel.android.mpmetrics.MixpanelAPI.People;
import com.nextradioapp.core.Log;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;

public class MixPanelHelper {
    private static final String TAG = "MixPanelHelper";
    public static MixPanelHelper mixPanelHelper;
    private Context mContext;
    private MixpanelAPI mixpanelAPI;

    static {
        mixPanelHelper = null;
    }

    public MixPanelHelper(Context context) {
        this.mixpanelAPI = null;
        this.mContext = context;
        getMixPanel(context);
    }

    public static MixPanelHelper getInstance(Context context) {
        if (mixPanelHelper == null) {
            mixPanelHelper = new MixPanelHelper(context);
        }
        return mixPanelHelper;
    }

    private MixpanelAPI getMixPanel(Context context) {
        if (this.mixpanelAPI == null) {
            this.mixpanelAPI = MixpanelAPI.getInstance(context, context.getResources().getString(2131165462));
        }
        return this.mixpanelAPI;
    }

    public void registerPush(String registrationId, Context context) {
        String trackingDistinctId = getTrackingDistinctId(context);
        this.mixpanelAPI.identify(trackingDistinctId);
        People people = this.mixpanelAPI.getPeople();
        people.identify(trackingDistinctId);
        people.setPushRegistrationId(registrationId);
    }

    public void recordMIPEvent(String eventProp, String eventValue) {
        try {
            JSONObject props = new JSONObject();
            props.put(eventProp, eventValue);
            this.mixpanelAPI.track(eventValue, props);
        } catch (JSONException e) {
            Log.m1935e(TAG, "Unable to add properties to JSONObject" + e);
        }
    }

    public void recordMIPEvent(String eventProp, String propertyValue, String eventName) {
        try {
            JSONObject props = new JSONObject();
            props.put(eventProp, propertyValue);
            this.mixpanelAPI.track(eventName, props);
        } catch (JSONException e) {
            Log.m1935e(TAG, "Unable to add properties to JSONObject" + e);
        }
    }

    public void flush() {
        if (this.mixpanelAPI != null) {
            this.mixpanelAPI.flush();
        }
    }

    public void recordMIPJsonObject(JSONObject props, String eventName) {
        this.mixpanelAPI.track(eventName, props);
    }

    public void firstTimeAppOpen(boolean status) {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean("first_open_mixpanel", status).apply();
    }

    public boolean isFirstTimeAppOpen() {
        return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("first_open_mixpanel", false);
    }

    private String getTrackingDistinctId(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String ret = prefs.getString("MIx_dist_id", null);
        if (ret != null) {
            return ret;
        }
        ret = generateDistinctId();
        prefs.edit().putString("MIx_dist_id", ret).apply();
        return ret;
    }

    private String generateDistinctId() {
        byte[] randomBytes = new byte[32];
        new Random().nextBytes(randomBytes);
        return Base64.encodeToString(randomBytes, 3);
    }
}
