package com.mixpanel.android.mpmetrics;

import android.util.Log;
import com.mixpanel.android.viewcrawler.UpdatesFromMixpanel;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;

class DecideMessages {
    private static final String LOGTAG = "MixpanelAPI.DecideUpdts";
    private static final Set<Integer> mLoadedVariants;
    private String mDistinctId;
    private final OnNewResultsListener mListener;
    private final Set<Integer> mNotificationIds;
    private final Set<Integer> mSurveyIds;
    private final String mToken;
    private final List<InAppNotification> mUnseenNotifications;
    private final List<Survey> mUnseenSurveys;
    private final UpdatesFromMixpanel mUpdatesFromMixpanel;
    private JSONArray mVariants;

    public interface OnNewResultsListener {
        void onNewResults();
    }

    public DecideMessages(String token, OnNewResultsListener listener, UpdatesFromMixpanel updatesFromMixpanel) {
        this.mToken = token;
        this.mListener = listener;
        this.mUpdatesFromMixpanel = updatesFromMixpanel;
        this.mDistinctId = null;
        this.mUnseenSurveys = new LinkedList();
        this.mUnseenNotifications = new LinkedList();
        this.mSurveyIds = new HashSet();
        this.mNotificationIds = new HashSet();
    }

    public String getToken() {
        return this.mToken;
    }

    public synchronized void setDistinctId(String distinctId) {
        if (this.mDistinctId == null || !this.mDistinctId.equals(distinctId)) {
            this.mUnseenSurveys.clear();
            this.mUnseenNotifications.clear();
        }
        this.mDistinctId = distinctId;
    }

    public synchronized String getDistinctId() {
        return this.mDistinctId;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void reportResults(java.util.List<com.mixpanel.android.mpmetrics.Survey> r15, java.util.List<com.mixpanel.android.mpmetrics.InAppNotification> r16, org.json.JSONArray r17, org.json.JSONArray r18) {
        /*
        r14 = this;
        monitor-enter(r14);
        r7 = 0;
        r11 = r14.mUpdatesFromMixpanel;	 Catch:{ all -> 0x00f3 }
        r0 = r17;
        r11.setEventBindings(r0);	 Catch:{ all -> 0x00f3 }
        r4 = r15.iterator();	 Catch:{ all -> 0x00f3 }
    L_0x000d:
        r11 = r4.hasNext();	 Catch:{ all -> 0x00f3 }
        if (r11 == 0) goto L_0x0039;
    L_0x0013:
        r9 = r4.next();	 Catch:{ all -> 0x00f3 }
        r9 = (com.mixpanel.android.mpmetrics.Survey) r9;	 Catch:{ all -> 0x00f3 }
        r5 = r9.getId();	 Catch:{ all -> 0x00f3 }
        r11 = r14.mSurveyIds;	 Catch:{ all -> 0x00f3 }
        r12 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x00f3 }
        r11 = r11.contains(r12);	 Catch:{ all -> 0x00f3 }
        if (r11 != 0) goto L_0x000d;
    L_0x0029:
        r11 = r14.mSurveyIds;	 Catch:{ all -> 0x00f3 }
        r12 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x00f3 }
        r11.add(r12);	 Catch:{ all -> 0x00f3 }
        r11 = r14.mUnseenSurveys;	 Catch:{ all -> 0x00f3 }
        r11.add(r9);	 Catch:{ all -> 0x00f3 }
        r7 = 1;
        goto L_0x000d;
    L_0x0039:
        r4 = r16.iterator();	 Catch:{ all -> 0x00f3 }
    L_0x003d:
        r11 = r4.hasNext();	 Catch:{ all -> 0x00f3 }
        if (r11 == 0) goto L_0x0069;
    L_0x0043:
        r6 = r4.next();	 Catch:{ all -> 0x00f3 }
        r6 = (com.mixpanel.android.mpmetrics.InAppNotification) r6;	 Catch:{ all -> 0x00f3 }
        r5 = r6.getId();	 Catch:{ all -> 0x00f3 }
        r11 = r14.mNotificationIds;	 Catch:{ all -> 0x00f3 }
        r12 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x00f3 }
        r11 = r11.contains(r12);	 Catch:{ all -> 0x00f3 }
        if (r11 != 0) goto L_0x003d;
    L_0x0059:
        r11 = r14.mNotificationIds;	 Catch:{ all -> 0x00f3 }
        r12 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x00f3 }
        r11.add(r12);	 Catch:{ all -> 0x00f3 }
        r11 = r14.mUnseenNotifications;	 Catch:{ all -> 0x00f3 }
        r11.add(r6);	 Catch:{ all -> 0x00f3 }
        r7 = 1;
        goto L_0x003d;
    L_0x0069:
        r8 = r18.length();	 Catch:{ all -> 0x00f3 }
        r2 = 0;
        r3 = 0;
    L_0x006f:
        if (r3 >= r8) goto L_0x008f;
    L_0x0071:
        r0 = r18;
        r10 = r0.getJSONObject(r3);	 Catch:{ JSONException -> 0x00b1 }
        r11 = mLoadedVariants;	 Catch:{ JSONException -> 0x00b1 }
        r12 = "id";
        r12 = r10.getInt(r12);	 Catch:{ JSONException -> 0x00b1 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ JSONException -> 0x00b1 }
        r11 = r11.contains(r12);	 Catch:{ JSONException -> 0x00b1 }
        if (r11 != 0) goto L_0x00d0;
    L_0x0089:
        r0 = r18;
        r14.mVariants = r0;	 Catch:{ JSONException -> 0x00b1 }
        r7 = 1;
        r2 = 1;
    L_0x008f:
        if (r2 == 0) goto L_0x00f6;
    L_0x0091:
        r11 = mLoadedVariants;	 Catch:{ all -> 0x00f3 }
        r11.clear();	 Catch:{ all -> 0x00f3 }
        r3 = 0;
    L_0x0097:
        if (r3 >= r8) goto L_0x00f6;
    L_0x0099:
        r11 = r14.mVariants;	 Catch:{ JSONException -> 0x00d3 }
        r10 = r11.getJSONObject(r3);	 Catch:{ JSONException -> 0x00d3 }
        r11 = mLoadedVariants;	 Catch:{ JSONException -> 0x00d3 }
        r12 = "id";
        r12 = r10.getInt(r12);	 Catch:{ JSONException -> 0x00d3 }
        r12 = java.lang.Integer.valueOf(r12);	 Catch:{ JSONException -> 0x00d3 }
        r11.add(r12);	 Catch:{ JSONException -> 0x00d3 }
    L_0x00ae:
        r3 = r3 + 1;
        goto L_0x0097;
    L_0x00b1:
        r1 = move-exception;
        r11 = "MixpanelAPI.DecideUpdts";
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f3 }
        r12.<init>();	 Catch:{ all -> 0x00f3 }
        r13 = "Could not convert variants[";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r12 = r12.append(r3);	 Catch:{ all -> 0x00f3 }
        r13 = "] into a JSONObject while comparing the new variants";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r12 = r12.toString();	 Catch:{ all -> 0x00f3 }
        android.util.Log.e(r11, r12, r1);	 Catch:{ all -> 0x00f3 }
    L_0x00d0:
        r3 = r3 + 1;
        goto L_0x006f;
    L_0x00d3:
        r1 = move-exception;
        r11 = "MixpanelAPI.DecideUpdts";
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f3 }
        r12.<init>();	 Catch:{ all -> 0x00f3 }
        r13 = "Could not convert variants[";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r12 = r12.append(r3);	 Catch:{ all -> 0x00f3 }
        r13 = "] into a JSONObject while updating the map";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r12 = r12.toString();	 Catch:{ all -> 0x00f3 }
        android.util.Log.e(r11, r12, r1);	 Catch:{ all -> 0x00f3 }
        goto L_0x00ae;
    L_0x00f3:
        r11 = move-exception;
        monitor-exit(r14);
        throw r11;
    L_0x00f6:
        r11 = com.mixpanel.android.mpmetrics.MPConfig.DEBUG;	 Catch:{ all -> 0x00f3 }
        if (r11 == 0) goto L_0x0138;
    L_0x00fa:
        r11 = "MixpanelAPI.DecideUpdts";
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f3 }
        r12.<init>();	 Catch:{ all -> 0x00f3 }
        r13 = "New Decide content has become available. ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = r15.size();	 Catch:{ all -> 0x00f3 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = " surveys, ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = r16.size();	 Catch:{ all -> 0x00f3 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = " notifications and ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = r18.length();	 Catch:{ all -> 0x00f3 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r13 = " experiments have been added.";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f3 }
        r12 = r12.toString();	 Catch:{ all -> 0x00f3 }
        android.util.Log.v(r11, r12);	 Catch:{ all -> 0x00f3 }
    L_0x0138:
        if (r7 == 0) goto L_0x0149;
    L_0x013a:
        r11 = r14.hasUpdatesAvailable();	 Catch:{ all -> 0x00f3 }
        if (r11 == 0) goto L_0x0149;
    L_0x0140:
        r11 = r14.mListener;	 Catch:{ all -> 0x00f3 }
        if (r11 == 0) goto L_0x0149;
    L_0x0144:
        r11 = r14.mListener;	 Catch:{ all -> 0x00f3 }
        r11.onNewResults();	 Catch:{ all -> 0x00f3 }
    L_0x0149:
        monitor-exit(r14);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.DecideMessages.reportResults(java.util.List, java.util.List, org.json.JSONArray, org.json.JSONArray):void");
    }

    public synchronized Survey getSurvey(boolean replace) {
        Survey survey;
        if (this.mUnseenSurveys.isEmpty()) {
            survey = null;
        } else {
            survey = (Survey) this.mUnseenSurveys.remove(0);
            if (replace) {
                this.mUnseenSurveys.add(survey);
            }
        }
        return survey;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.mixpanel.android.mpmetrics.Survey getSurvey(int r5, boolean r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        r2 = 0;
        r1 = 0;
    L_0x0003:
        r3 = r4.mUnseenSurveys;	 Catch:{ all -> 0x002f }
        r3 = r3.size();	 Catch:{ all -> 0x002f }
        if (r1 >= r3) goto L_0x002a;
    L_0x000b:
        r3 = r4.mUnseenSurveys;	 Catch:{ all -> 0x002f }
        r3 = r3.get(r1);	 Catch:{ all -> 0x002f }
        r3 = (com.mixpanel.android.mpmetrics.Survey) r3;	 Catch:{ all -> 0x002f }
        r3 = r3.getId();	 Catch:{ all -> 0x002f }
        if (r3 != r5) goto L_0x002c;
    L_0x0019:
        r3 = r4.mUnseenSurveys;	 Catch:{ all -> 0x002f }
        r3 = r3.get(r1);	 Catch:{ all -> 0x002f }
        r0 = r3;
        r0 = (com.mixpanel.android.mpmetrics.Survey) r0;	 Catch:{ all -> 0x002f }
        r2 = r0;
        if (r6 != 0) goto L_0x002a;
    L_0x0025:
        r3 = r4.mUnseenSurveys;	 Catch:{ all -> 0x002f }
        r3.remove(r1);	 Catch:{ all -> 0x002f }
    L_0x002a:
        monitor-exit(r4);
        return r2;
    L_0x002c:
        r1 = r1 + 1;
        goto L_0x0003;
    L_0x002f:
        r3 = move-exception;
        monitor-exit(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.DecideMessages.getSurvey(int, boolean):com.mixpanel.android.mpmetrics.Survey");
    }

    public synchronized JSONArray getVariants() {
        return this.mVariants;
    }

    public synchronized InAppNotification getNotification(boolean replace) {
        InAppNotification inAppNotification;
        if (this.mUnseenNotifications.isEmpty()) {
            if (MPConfig.DEBUG) {
                Log.v(LOGTAG, "No unseen notifications exist, none will be returned.");
            }
            inAppNotification = null;
        } else {
            inAppNotification = (InAppNotification) this.mUnseenNotifications.remove(0);
            if (replace) {
                this.mUnseenNotifications.add(inAppNotification);
            } else if (MPConfig.DEBUG) {
                Log.v(LOGTAG, "Recording notification " + inAppNotification + " as seen.");
            }
        }
        return inAppNotification;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.mixpanel.android.mpmetrics.InAppNotification getNotification(int r5, boolean r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        r2 = 0;
        r1 = 0;
    L_0x0003:
        r3 = r4.mUnseenNotifications;	 Catch:{ all -> 0x002f }
        r3 = r3.size();	 Catch:{ all -> 0x002f }
        if (r1 >= r3) goto L_0x002a;
    L_0x000b:
        r3 = r4.mUnseenNotifications;	 Catch:{ all -> 0x002f }
        r3 = r3.get(r1);	 Catch:{ all -> 0x002f }
        r3 = (com.mixpanel.android.mpmetrics.InAppNotification) r3;	 Catch:{ all -> 0x002f }
        r3 = r3.getId();	 Catch:{ all -> 0x002f }
        if (r3 != r5) goto L_0x002c;
    L_0x0019:
        r3 = r4.mUnseenNotifications;	 Catch:{ all -> 0x002f }
        r3 = r3.get(r1);	 Catch:{ all -> 0x002f }
        r0 = r3;
        r0 = (com.mixpanel.android.mpmetrics.InAppNotification) r0;	 Catch:{ all -> 0x002f }
        r2 = r0;
        if (r6 != 0) goto L_0x002a;
    L_0x0025:
        r3 = r4.mUnseenNotifications;	 Catch:{ all -> 0x002f }
        r3.remove(r1);	 Catch:{ all -> 0x002f }
    L_0x002a:
        monitor-exit(r4);
        return r2;
    L_0x002c:
        r1 = r1 + 1;
        goto L_0x0003;
    L_0x002f:
        r3 = move-exception;
        monitor-exit(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.DecideMessages.getNotification(int, boolean):com.mixpanel.android.mpmetrics.InAppNotification");
    }

    public synchronized void markNotificationAsUnseen(InAppNotification notif) {
        if (!MPConfig.DEBUG) {
            this.mUnseenNotifications.add(notif);
        }
    }

    public synchronized boolean hasUpdatesAvailable() {
        boolean z;
        z = (this.mUnseenNotifications.isEmpty() && this.mUnseenSurveys.isEmpty() && this.mVariants == null) ? false : true;
        return z;
    }

    static {
        mLoadedVariants = new HashSet();
    }
}
