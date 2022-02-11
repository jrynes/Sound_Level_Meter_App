package com.nextradioapp.nextradio;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nextradioapp.nextradio.mixpanel.MixPanelHelper;
import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS;

    static {
        TOPICS = new String[]{"global"};
    }

    public RegistrationIntentService() {
        super(TAG);
    }

    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            String token = InstanceID.getInstance(this).getToken(getResources().getString(2131165463), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (!sharedPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false)) {
                Log.i(TAG, "GCM Registration Token: " + token);
                MixPanelHelper.getInstance(this).registerPush(token, this);
            }
            sendRegistrationToServer(token);
            subscribeTopics(token);
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(REGISTRATION_COMPLETE));
    }

    private void sendRegistrationToServer(String token) {
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
