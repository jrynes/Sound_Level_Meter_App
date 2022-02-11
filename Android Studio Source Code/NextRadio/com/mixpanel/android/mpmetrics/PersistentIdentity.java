package com.mixpanel.android.mpmetrics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"CommitPrefEdits"})
class PersistentIdentity {
    private static final String LOGTAG = "MixpanelAPI.PIdentity";
    private static boolean sReferrerPrefsDirty;
    private static final Object sReferrerPrefsLock;
    private String mEventsDistinctId;
    private boolean mIdentitiesLoaded;
    private final Future<SharedPreferences> mLoadReferrerPreferences;
    private final Future<SharedPreferences> mLoadStoredPreferences;
    private String mPeopleDistinctId;
    private final OnSharedPreferenceChangeListener mReferrerChangeListener;
    private Map<String, String> mReferrerPropertiesCache;
    private JSONObject mSuperPropertiesCache;
    private JSONArray mWaitingPeopleRecords;

    /* renamed from: com.mixpanel.android.mpmetrics.PersistentIdentity.1 */
    class C10971 implements OnSharedPreferenceChangeListener {
        C10971() {
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            synchronized (PersistentIdentity.sReferrerPrefsLock) {
                PersistentIdentity.this.readReferrerProperties();
                PersistentIdentity.sReferrerPrefsDirty = false;
            }
        }
    }

    public static JSONArray waitingPeopleRecordsForSending(SharedPreferences storedPreferences) {
        JSONArray ret = null;
        String peopleDistinctId = storedPreferences.getString("people_distinct_id", null);
        String waitingPeopleRecords = storedPreferences.getString("waiting_array", null);
        if (!(waitingPeopleRecords == null || peopleDistinctId == null)) {
            try {
                JSONArray waitingObjects = new JSONArray(waitingPeopleRecords);
                ret = new JSONArray();
                for (int i = 0; i < waitingObjects.length(); i++) {
                    try {
                        JSONObject ob = waitingObjects.getJSONObject(i);
                        ob.put("$distinct_id", peopleDistinctId);
                        ret.put(ob);
                    } catch (JSONException e) {
                        Log.e(LOGTAG, "Unparsable object found in waiting people records", e);
                    }
                }
                Editor editor = storedPreferences.edit();
                editor.remove("waiting_array");
                writeEdits(editor);
            } catch (JSONException e2) {
                Log.e(LOGTAG, "Waiting people records were unreadable.");
                return null;
            }
        }
        return ret;
    }

    public static void writeReferrerPrefs(Context context, String preferencesName, Map<String, String> properties) {
        synchronized (sReferrerPrefsLock) {
            Editor editor = context.getSharedPreferences(preferencesName, 0).edit();
            editor.clear();
            for (Entry<String, String> entry : properties.entrySet()) {
                editor.putString((String) entry.getKey(), (String) entry.getValue());
            }
            writeEdits(editor);
            sReferrerPrefsDirty = true;
        }
    }

    public PersistentIdentity(Future<SharedPreferences> referrerPreferences, Future<SharedPreferences> storedPreferences) {
        this.mLoadReferrerPreferences = referrerPreferences;
        this.mLoadStoredPreferences = storedPreferences;
        this.mSuperPropertiesCache = null;
        this.mReferrerPropertiesCache = null;
        this.mIdentitiesLoaded = false;
        this.mReferrerChangeListener = new C10971();
    }

    public synchronized void addSuperPropertiesToObject(JSONObject ob) {
        JSONObject superProperties = getSuperPropertiesCache();
        Iterator<?> superIter = superProperties.keys();
        while (superIter.hasNext()) {
            String key = (String) superIter.next();
            try {
                ob.put(key, superProperties.get(key));
            } catch (JSONException e) {
                Log.wtf(LOGTAG, "Object read from one JSON Object cannot be written to another", e);
            }
        }
    }

    public synchronized void updateSuperProperties(SuperPropertyUpdate updates) {
        JSONObject oldPropCache = getSuperPropertiesCache();
        JSONObject copy = new JSONObject();
        try {
            Iterator<String> keys = oldPropCache.keys();
            while (keys.hasNext()) {
                String k = (String) keys.next();
                copy.put(k, oldPropCache.get(k));
            }
            JSONObject replacementCache = updates.update(copy);
            if (replacementCache == null) {
                Log.w(LOGTAG, "An update to Mixpanel's super properties returned null, and will have no effect.");
            } else {
                this.mSuperPropertiesCache = replacementCache;
                storeSuperProperties();
            }
        } catch (JSONException e) {
            Log.wtf(LOGTAG, "Can't copy from one JSONObject to another", e);
        }
    }

    public Map<String, String> getReferrerProperties() {
        synchronized (sReferrerPrefsLock) {
            if (sReferrerPrefsDirty || this.mReferrerPropertiesCache == null) {
                readReferrerProperties();
                sReferrerPrefsDirty = false;
            }
        }
        return this.mReferrerPropertiesCache;
    }

    public synchronized String getEventsDistinctId() {
        if (!this.mIdentitiesLoaded) {
            readIdentities();
        }
        return this.mEventsDistinctId;
    }

    public synchronized void setEventsDistinctId(String eventsDistinctId) {
        if (!this.mIdentitiesLoaded) {
            readIdentities();
        }
        this.mEventsDistinctId = eventsDistinctId;
        writeIdentities();
    }

    public synchronized String getPeopleDistinctId() {
        if (!this.mIdentitiesLoaded) {
            readIdentities();
        }
        return this.mPeopleDistinctId;
    }

    public synchronized void setPeopleDistinctId(String peopleDistinctId) {
        if (!this.mIdentitiesLoaded) {
            readIdentities();
        }
        this.mPeopleDistinctId = peopleDistinctId;
        writeIdentities();
    }

    public synchronized void storeWaitingPeopleRecord(JSONObject record) {
        if (!this.mIdentitiesLoaded) {
            readIdentities();
        }
        if (this.mWaitingPeopleRecords == null) {
            this.mWaitingPeopleRecords = new JSONArray();
        }
        this.mWaitingPeopleRecords.put(record);
        writeIdentities();
    }

    public synchronized JSONArray waitingPeopleRecordsForSending() {
        JSONArray ret;
        ret = null;
        try {
            ret = waitingPeopleRecordsForSending((SharedPreferences) this.mLoadStoredPreferences.get());
            readIdentities();
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Couldn't read waiting people records from shared preferences.", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Couldn't read waiting people records from shared preferences.", e2);
        }
        return ret;
    }

    public synchronized void clearPreferences() {
        try {
            Editor prefsEdit = ((SharedPreferences) this.mLoadStoredPreferences.get()).edit();
            prefsEdit.clear();
            writeEdits(prefsEdit);
            readSuperProperties();
            readIdentities();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2.getCause());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void registerSuperProperties(org.json.JSONObject r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r3 = r6.getSuperPropertiesCache();	 Catch:{ all -> 0x0026 }
        r1 = r7.keys();	 Catch:{ all -> 0x0026 }
    L_0x0009:
        r4 = r1.hasNext();	 Catch:{ all -> 0x0026 }
        if (r4 == 0) goto L_0x0029;
    L_0x000f:
        r2 = r1.next();	 Catch:{ all -> 0x0026 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x0026 }
        r4 = r7.get(r2);	 Catch:{ JSONException -> 0x001d }
        r3.put(r2, r4);	 Catch:{ JSONException -> 0x001d }
        goto L_0x0009;
    L_0x001d:
        r0 = move-exception;
        r4 = "MixpanelAPI.PIdentity";
        r5 = "Exception registering super property.";
        android.util.Log.e(r4, r5, r0);	 Catch:{ all -> 0x0026 }
        goto L_0x0009;
    L_0x0026:
        r4 = move-exception;
        monitor-exit(r6);
        throw r4;
    L_0x0029:
        r6.storeSuperProperties();	 Catch:{ all -> 0x0026 }
        monitor-exit(r6);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.PersistentIdentity.registerSuperProperties(org.json.JSONObject):void");
    }

    public synchronized void storePushId(String registrationId) {
        try {
            Editor editor = ((SharedPreferences) this.mLoadStoredPreferences.get()).edit();
            editor.putString("push_id", registrationId);
            writeEdits(editor);
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e2);
        }
    }

    public synchronized void clearPushId() {
        try {
            Editor editor = ((SharedPreferences) this.mLoadStoredPreferences.get()).edit();
            editor.remove("push_id");
            writeEdits(editor);
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e2);
        }
    }

    public synchronized String getPushId() {
        String ret;
        ret = null;
        try {
            ret = ((SharedPreferences) this.mLoadStoredPreferences.get()).getString("push_id", null);
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Can't write push id to shared preferences", e2);
        }
        return ret;
    }

    public synchronized void unregisterSuperProperty(String superPropertyName) {
        getSuperPropertiesCache().remove(superPropertyName);
        storeSuperProperties();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void registerSuperPropertiesOnce(org.json.JSONObject r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r3 = r6.getSuperPropertiesCache();	 Catch:{ all -> 0x002c }
        r1 = r7.keys();	 Catch:{ all -> 0x002c }
    L_0x0009:
        r4 = r1.hasNext();	 Catch:{ all -> 0x002c }
        if (r4 == 0) goto L_0x002f;
    L_0x000f:
        r2 = r1.next();	 Catch:{ all -> 0x002c }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x002c }
        r4 = r3.has(r2);	 Catch:{ all -> 0x002c }
        if (r4 != 0) goto L_0x0009;
    L_0x001b:
        r4 = r7.get(r2);	 Catch:{ JSONException -> 0x0023 }
        r3.put(r2, r4);	 Catch:{ JSONException -> 0x0023 }
        goto L_0x0009;
    L_0x0023:
        r0 = move-exception;
        r4 = "MixpanelAPI.PIdentity";
        r5 = "Exception registering super property.";
        android.util.Log.e(r4, r5, r0);	 Catch:{ all -> 0x002c }
        goto L_0x0009;
    L_0x002c:
        r4 = move-exception;
        monitor-exit(r6);
        throw r4;
    L_0x002f:
        r6.storeSuperProperties();	 Catch:{ all -> 0x002c }
        monitor-exit(r6);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mixpanel.android.mpmetrics.PersistentIdentity.registerSuperPropertiesOnce(org.json.JSONObject):void");
    }

    public synchronized void clearSuperProperties() {
        this.mSuperPropertiesCache = new JSONObject();
        storeSuperProperties();
    }

    private JSONObject getSuperPropertiesCache() {
        if (this.mSuperPropertiesCache == null) {
            readSuperProperties();
        }
        return this.mSuperPropertiesCache;
    }

    private void readSuperProperties() {
        try {
            String props = ((SharedPreferences) this.mLoadStoredPreferences.get()).getString("super_properties", "{}");
            if (MPConfig.DEBUG) {
                Log.v(LOGTAG, "Loading Super Properties " + props);
            }
            this.mSuperPropertiesCache = new JSONObject(props);
            if (this.mSuperPropertiesCache == null) {
                this.mSuperPropertiesCache = new JSONObject();
            }
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Cannot load superProperties from SharedPreferences.", e.getCause());
            if (this.mSuperPropertiesCache == null) {
                this.mSuperPropertiesCache = new JSONObject();
            }
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Cannot load superProperties from SharedPreferences.", e2);
            if (this.mSuperPropertiesCache == null) {
                this.mSuperPropertiesCache = new JSONObject();
            }
        } catch (JSONException e3) {
            Log.e(LOGTAG, "Cannot parse stored superProperties");
            storeSuperProperties();
            if (this.mSuperPropertiesCache == null) {
                this.mSuperPropertiesCache = new JSONObject();
            }
        } catch (Throwable th) {
            if (this.mSuperPropertiesCache == null) {
                this.mSuperPropertiesCache = new JSONObject();
            }
        }
    }

    private void readReferrerProperties() {
        this.mReferrerPropertiesCache = new HashMap();
        try {
            SharedPreferences referrerPrefs = (SharedPreferences) this.mLoadReferrerPreferences.get();
            referrerPrefs.unregisterOnSharedPreferenceChangeListener(this.mReferrerChangeListener);
            referrerPrefs.registerOnSharedPreferenceChangeListener(this.mReferrerChangeListener);
            for (Entry<String, ?> entry : referrerPrefs.getAll().entrySet()) {
                this.mReferrerPropertiesCache.put((String) entry.getKey(), entry.getValue().toString());
            }
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Cannot load referrer properties from shared preferences.", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Cannot load referrer properties from shared preferences.", e2);
        }
    }

    private void storeSuperProperties() {
        if (this.mSuperPropertiesCache == null) {
            Log.e(LOGTAG, "storeSuperProperties should not be called with uninitialized superPropertiesCache.");
            return;
        }
        String props = this.mSuperPropertiesCache.toString();
        if (MPConfig.DEBUG) {
            Log.v(LOGTAG, "Storing Super Properties " + props);
        }
        try {
            Editor editor = ((SharedPreferences) this.mLoadStoredPreferences.get()).edit();
            editor.putString("super_properties", props);
            writeEdits(editor);
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Cannot store superProperties in shared preferences.", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Cannot store superProperties in shared preferences.", e2);
        }
    }

    private void readIdentities() {
        SharedPreferences prefs = null;
        try {
            prefs = (SharedPreferences) this.mLoadStoredPreferences.get();
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Cannot read distinct ids from sharedPreferences.", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Cannot read distinct ids from sharedPreferences.", e2);
        }
        if (prefs != null) {
            this.mEventsDistinctId = prefs.getString("events_distinct_id", null);
            this.mPeopleDistinctId = prefs.getString("people_distinct_id", null);
            this.mWaitingPeopleRecords = null;
            String storedWaitingRecord = prefs.getString("waiting_array", null);
            if (storedWaitingRecord != null) {
                try {
                    this.mWaitingPeopleRecords = new JSONArray(storedWaitingRecord);
                } catch (JSONException e3) {
                    Log.e(LOGTAG, "Could not interpret waiting people JSON record " + storedWaitingRecord);
                }
            }
            if (this.mEventsDistinctId == null) {
                this.mEventsDistinctId = UUID.randomUUID().toString();
                writeIdentities();
            }
            this.mIdentitiesLoaded = true;
        }
    }

    private void writeIdentities() {
        try {
            Editor prefsEditor = ((SharedPreferences) this.mLoadStoredPreferences.get()).edit();
            prefsEditor.putString("events_distinct_id", this.mEventsDistinctId);
            prefsEditor.putString("people_distinct_id", this.mPeopleDistinctId);
            if (this.mWaitingPeopleRecords == null) {
                prefsEditor.remove("waiting_array");
            } else {
                prefsEditor.putString("waiting_array", this.mWaitingPeopleRecords.toString());
            }
            writeEdits(prefsEditor);
        } catch (ExecutionException e) {
            Log.e(LOGTAG, "Can't write distinct ids to shared preferences.", e.getCause());
        } catch (InterruptedException e2) {
            Log.e(LOGTAG, "Can't write distinct ids to shared preferences.", e2);
        }
    }

    @TargetApi(9)
    private static void writeEdits(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    static {
        sReferrerPrefsDirty = true;
        sReferrerPrefsLock = new Object();
    }
}
