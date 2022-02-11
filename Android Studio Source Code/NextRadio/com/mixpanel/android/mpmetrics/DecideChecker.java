package com.mixpanel.android.mpmetrics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.mixpanel.android.mpmetrics.InAppNotification.Type;
import com.mixpanel.android.util.ImageStore;
import com.mixpanel.android.util.ImageStore.CantGetImageException;
import com.mixpanel.android.util.RemoteService;
import com.mixpanel.android.util.RemoteService.ServiceUnavailableException;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class DecideChecker {
    private static final JSONArray EMPTY_JSON_ARRAY;
    private static final String LOGTAG = "MixpanelAPI.DChecker";
    private final List<DecideMessages> mChecks;
    private final MPConfig mConfig;
    private final Context mContext;
    private final ImageStore mImageStore;

    static class Result {
        public JSONArray eventBindings;
        public final List<InAppNotification> notifications;
        public final List<Survey> surveys;
        public JSONArray variants;

        public Result() {
            this.surveys = new ArrayList();
            this.notifications = new ArrayList();
            this.eventBindings = DecideChecker.EMPTY_JSON_ARRAY;
            this.variants = DecideChecker.EMPTY_JSON_ARRAY;
        }
    }

    static class UnintelligibleMessageException extends Exception {
        private static final long serialVersionUID = -6501269367559104957L;

        public UnintelligibleMessageException(String message, JSONException cause) {
            super(message, cause);
        }
    }

    public DecideChecker(Context context, MPConfig config) {
        this.mContext = context;
        this.mConfig = config;
        this.mChecks = new LinkedList();
        this.mImageStore = createImageStore(context);
    }

    protected ImageStore createImageStore(Context context) {
        return new ImageStore(context, "DecideChecker");
    }

    public void addDecideCheck(DecideMessages check) {
        this.mChecks.add(check);
    }

    public void runDecideChecks(RemoteService poster) throws ServiceUnavailableException {
        for (DecideMessages updates : this.mChecks) {
            try {
                Result result = runDecideCheck(updates.getToken(), updates.getDistinctId(), poster);
                updates.reportResults(result.surveys, result.notifications, result.eventBindings, result.variants);
            } catch (UnintelligibleMessageException e) {
                Log.e(LOGTAG, e.getMessage(), e);
            }
        }
    }

    private Result runDecideCheck(String token, String distinctId, RemoteService poster) throws ServiceUnavailableException, UnintelligibleMessageException {
        String responseString = getDecideResponseFromServer(token, distinctId, poster);
        if (MPConfig.DEBUG) {
            Log.v(LOGTAG, "Mixpanel decide server response was:\n" + responseString);
        }
        Result parsed = new Result();
        if (responseString != null) {
            parsed = parseDecideResponse(responseString);
        }
        Iterator<InAppNotification> notificationIterator = parsed.notifications.iterator();
        while (notificationIterator.hasNext()) {
            InAppNotification notification = (InAppNotification) notificationIterator.next();
            Bitmap image = getNotificationImage(notification, this.mContext, poster);
            if (image == null) {
                Log.i(LOGTAG, "Could not retrieve image for notification " + notification.getId() + ", will not show the notification.");
                notificationIterator.remove();
            } else {
                notification.setImage(image);
            }
        }
        return parsed;
    }

    static Result parseDecideResponse(String responseString) throws UnintelligibleMessageException {
        Result ret = new Result();
        try {
            int i;
            JSONObject response = new JSONObject(responseString);
            JSONArray surveys = null;
            if (response.has("surveys")) {
                try {
                    surveys = response.getJSONArray("surveys");
                } catch (JSONException e) {
                    Log.e(LOGTAG, "Mixpanel endpoint returned non-array JSON for surveys: " + response);
                }
            }
            if (surveys != null) {
                for (i = 0; i < surveys.length(); i++) {
                    try {
                        ret.surveys.add(new Survey(surveys.getJSONObject(i)));
                    } catch (JSONException e2) {
                        Log.e(LOGTAG, "Received a strange response from surveys service: " + surveys.toString());
                    } catch (BadDecideObjectException e3) {
                        Log.e(LOGTAG, "Received a strange response from surveys service: " + surveys.toString());
                    }
                }
            }
            JSONArray notifications = null;
            if (response.has(PreferenceStorage.NOTIFICATION_PREFERENCE)) {
                try {
                    notifications = response.getJSONArray(PreferenceStorage.NOTIFICATION_PREFERENCE);
                } catch (JSONException e4) {
                    Log.e(LOGTAG, "Mixpanel endpoint returned non-array JSON for notifications: " + response);
                }
            }
            if (notifications != null) {
                int notificationsToRead = Math.min(notifications.length(), 2);
                for (i = 0; i < notificationsToRead; i++) {
                    try {
                        ret.notifications.add(new InAppNotification(notifications.getJSONObject(i)));
                    } catch (JSONException e5) {
                        Log.e(LOGTAG, "Received a strange response from notifications service: " + notifications.toString(), e5);
                    } catch (BadDecideObjectException e6) {
                        Log.e(LOGTAG, "Received a strange response from notifications service: " + notifications.toString(), e6);
                    } catch (OutOfMemoryError e7) {
                        Log.e(LOGTAG, "Not enough memory to show load notification from package: " + notifications.toString(), e7);
                    }
                }
            }
            if (response.has("event_bindings")) {
                try {
                    ret.eventBindings = response.getJSONArray("event_bindings");
                } catch (JSONException e8) {
                    Log.e(LOGTAG, "Mixpanel endpoint returned non-array JSON for event bindings: " + response);
                }
            }
            if (response.has("variants")) {
                try {
                    ret.variants = response.getJSONArray("variants");
                } catch (JSONException e9) {
                    Log.e(LOGTAG, "Mixpanel endpoint returned non-array JSON for variants: " + response);
                }
            }
            return ret;
        } catch (JSONException e52) {
            throw new UnintelligibleMessageException("Mixpanel endpoint returned unparsable result:\n" + responseString, e52);
        }
    }

    private String getDecideResponseFromServer(String unescapedToken, String unescapedDistinctId, RemoteService poster) throws ServiceUnavailableException {
        try {
            String escapedId;
            String escapedToken = URLEncoder.encode(unescapedToken, "utf-8");
            if (unescapedDistinctId != null) {
                escapedId = URLEncoder.encode(unescapedDistinctId, "utf-8");
            } else {
                escapedId = null;
            }
            StringBuilder queryBuilder = new StringBuilder().append("?version=1&lib=android&token=").append(escapedToken);
            if (escapedId != null) {
                queryBuilder.append("&distinct_id=").append(escapedId);
            }
            String checkQuery = queryBuilder.toString();
            String[] urls = this.mConfig.getDisableFallback() ? new String[]{this.mConfig.getDecideEndpoint() + checkQuery} : new String[]{this.mConfig.getDecideEndpoint() + checkQuery, this.mConfig.getDecideFallbackEndpoint() + checkQuery};
            if (MPConfig.DEBUG) {
                Log.v(LOGTAG, "Querying decide server, urls:");
                for (String str : urls) {
                    Log.v(LOGTAG, "    >> " + str);
                }
            }
            byte[] response = getUrls(poster, this.mContext, urls);
            if (response == null) {
                return null;
            }
            try {
                return new String(response, HttpRequest.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UTF not supported on this platform?", e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Mixpanel library requires utf-8 string encoding to be available", e2);
        }
    }

    private Bitmap getNotificationImage(InAppNotification notification, Context context, RemoteService poster) throws ServiceUnavailableException {
        String[] urls = new String[]{notification.getImage2xUrl(), notification.getImageUrl()};
        int displayWidth = getDisplayWidth(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
        if (notification.getType() == Type.TAKEOVER && displayWidth >= 720) {
            urls = new String[]{notification.getImage4xUrl(), notification.getImage2xUrl(), notification.getImageUrl()};
        }
        String[] arr$ = urls;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String url = arr$[i$];
            try {
                return this.mImageStore.getImage(url);
            } catch (CantGetImageException e) {
                Log.v(LOGTAG, "Can't load image " + url + " for a notification", e);
                i$++;
            }
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    private static int getDisplayWidth(Display display) {
        if (VERSION.SDK_INT < 13) {
            return display.getWidth();
        }
        Point displaySize = new Point();
        display.getSize(displaySize);
        return displaySize.x;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] getUrls(com.mixpanel.android.util.RemoteService r11, android.content.Context r12, java.lang.String[] r13) throws com.mixpanel.android.util.RemoteService.ServiceUnavailableException {
        /*
        r5 = 0;
        r8 = r11.isOnline(r12);
        if (r8 != 0) goto L_0x0008;
    L_0x0007:
        return r5;
    L_0x0008:
        r5 = 0;
        r0 = r13;
        r4 = r0.length;
        r3 = 0;
    L_0x000c:
        if (r3 >= r4) goto L_0x0007;
    L_0x000e:
        r7 = r0[r3];
        r1 = com.mixpanel.android.mpmetrics.MPConfig.getInstance(r12);	 Catch:{ MalformedURLException -> 0x001e, FileNotFoundException -> 0x0040, IOException -> 0x0064, OutOfMemoryError -> 0x0088 }
        r6 = r1.getSSLSocketFactory();	 Catch:{ MalformedURLException -> 0x001e, FileNotFoundException -> 0x0040, IOException -> 0x0064, OutOfMemoryError -> 0x0088 }
        r8 = 0;
        r5 = r11.performRequest(r7, r8, r6);	 Catch:{ MalformedURLException -> 0x001e, FileNotFoundException -> 0x0040, IOException -> 0x0064, OutOfMemoryError -> 0x0088 }
        goto L_0x0007;
    L_0x001e:
        r2 = move-exception;
        r8 = "MixpanelAPI.DChecker";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Cannot interpret ";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r10 = " as a URL.";
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.e(r8, r9, r2);
    L_0x003d:
        r3 = r3 + 1;
        goto L_0x000c;
    L_0x0040:
        r2 = move-exception;
        r8 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;
        if (r8 == 0) goto L_0x003d;
    L_0x0045:
        r8 = "MixpanelAPI.DChecker";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Cannot get ";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r10 = ", file not found.";
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.v(r8, r9, r2);
        goto L_0x003d;
    L_0x0064:
        r2 = move-exception;
        r8 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;
        if (r8 == 0) goto L_0x003d;
    L_0x0069:
        r8 = "MixpanelAPI.DChecker";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Cannot get ";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r10 = ".";
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.v(r8, r9, r2);
        goto L_0x003d;
    L_0x0088:
        r2 = move-exception;
        r8 = "MixpanelAPI.DChecker";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Out of memory when getting to ";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r10 = ".";
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.e(r8, r9, r2);
        goto L_0x0007;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.DecideChecker.getUrls(com.mixpanel.android.util.RemoteService, android.content.Context, java.lang.String[]):byte[]");
    }

    static {
        EMPTY_JSON_ARRAY = new JSONArray();
    }
}
