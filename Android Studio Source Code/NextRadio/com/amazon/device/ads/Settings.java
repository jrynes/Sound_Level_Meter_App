package com.amazon.device.ads;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

class Settings {
    private static final String LOGTAG;
    private static final String PREFS_NAME = "AmazonMobileAds";
    public static final String SETTING_ENABLE_WEBVIEW_PAUSE_LOGIC = "shouldPauseWebViewTimersInWebViewRelatedActivities";
    protected static final String SETTING_TESTING_ENABLED = "testingEnabled";
    protected static final String SETTING_TLS_ENABLED = "tlsEnabled";
    private static Settings instance;
    private final ConcurrentHashMap<String, Value> cache;
    private ArrayList<SettingsListener> listeners;
    private final ReentrantLock listenersLock;
    private final MobileAdsLogger logger;
    private final CountDownLatch settingsLoadedLatch;
    private SharedPreferences sharedPreferences;
    private final ReentrantLock writeToSharedPreferencesLock;

    public interface SettingsListener {
        void settingsLoaded();
    }

    /* renamed from: com.amazon.device.ads.Settings.1 */
    class C03391 implements Runnable {
        final /* synthetic */ Context val$context;

        C03391(Context context) {
            this.val$context = context;
        }

        public void run() {
            Settings.this.fetchSharedPreferences(this.val$context);
        }
    }

    /* renamed from: com.amazon.device.ads.Settings.2 */
    class C03402 implements Runnable {
        final /* synthetic */ SharedPreferences val$sharedPreferences;

        C03402(SharedPreferences sharedPreferences) {
            this.val$sharedPreferences = sharedPreferences;
        }

        public void run() {
            Settings.this.writeToSharedPreferencesLock.lock();
            Editor editor = this.val$sharedPreferences.edit();
            editor.clear();
            for (Entry<String, Value> entry : Settings.this.cache.entrySet()) {
                Value value = (Value) entry.getValue();
                if (!value.isTransientData) {
                    if (value.clazz == String.class) {
                        editor.putString((String) entry.getKey(), (String) value.value);
                    } else if (value.clazz == Long.class) {
                        editor.putLong((String) entry.getKey(), ((Long) value.value).longValue());
                    } else if (value.clazz == Integer.class) {
                        editor.putInt((String) entry.getKey(), ((Integer) value.value).intValue());
                    } else if (value.clazz == Boolean.class) {
                        editor.putBoolean((String) entry.getKey(), ((Boolean) value.value).booleanValue());
                    }
                }
            }
            Settings.this.commit(editor);
            Settings.this.writeToSharedPreferencesLock.unlock();
        }
    }

    class Value {
        public Class<?> clazz;
        public boolean isTransientData;
        public Object value;

        public Value(Class<?> clazz, Object value) {
            this.clazz = clazz;
            this.value = value;
        }
    }

    class TransientValue extends Value {
        public TransientValue(Class<?> clazz, Object value) {
            super(clazz, value);
            this.isTransientData = true;
        }
    }

    static {
        LOGTAG = Settings.class.getSimpleName();
        instance = new Settings();
    }

    protected Settings() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.listeners = new ArrayList();
        this.listenersLock = new ReentrantLock();
        this.writeToSharedPreferencesLock = new ReentrantLock();
        this.settingsLoadedLatch = new CountDownLatch(1);
        this.cache = new ConcurrentHashMap();
    }

    public static Settings getInstance() {
        return instance;
    }

    void contextReceived(Context context) {
        if (context != null) {
            beginFetch(context);
        }
    }

    void beginFetch(Context context) {
        ThreadUtils.scheduleRunnable(new C03391(context));
    }

    public boolean isSettingsLoaded() {
        return this.sharedPreferences != null;
    }

    public void listenForSettings(SettingsListener listener) {
        this.listenersLock.lock();
        if (isSettingsLoaded()) {
            listener.settingsLoaded();
        } else {
            this.listeners.add(listener);
        }
        this.listenersLock.unlock();
    }

    SharedPreferences getSharedPreferencesFromContext(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    ConcurrentHashMap<String, Value> getCache() {
        return this.cache;
    }

    private void putSetting(String key, Value value) {
        if (value.value == null) {
            this.logger.m646w("Could not set null value for setting: %s", key);
            return;
        }
        putSettingWithNoFlush(key, value);
        if (!value.isTransientData && isSettingsLoaded()) {
            flush();
        }
    }

    private void putSettingWithNoFlush(String key, Value value) {
        if (value.value == null) {
            this.logger.m646w("Could not set null value for setting: %s", key);
            return;
        }
        this.cache.put(key, value);
    }

    void readSharedPreferencesIntoCache(SharedPreferences sharedPreferences) {
        cacheAdditionalEntries(sharedPreferences.getAll());
    }

    void cacheAdditionalEntries(Map<String, ?> entries) {
        for (Entry<String, ?> entry : entries.entrySet()) {
            String key = (String) entry.getKey();
            if (!(key == null || this.cache.containsKey(key))) {
                Object value = entry.getValue();
                if (value != null) {
                    this.cache.put(key, new Value(value.getClass(), value));
                } else {
                    this.logger.m646w("Could not cache null value for SharedPreferences setting: %s", key);
                }
            }
        }
    }

    private void writeCacheToSharedPreferences() {
        writeCacheToSharedPreferences(this.sharedPreferences);
    }

    void writeCacheToSharedPreferences(SharedPreferences sharedPreferences) {
        ThreadUtils.scheduleRunnable(new C03402(sharedPreferences));
    }

    void flush() {
        writeCacheToSharedPreferences();
    }

    public boolean containsKey(String key) {
        return this.cache.containsKey(key);
    }

    public String getString(String key, String defaultValue) {
        Value value = (Value) this.cache.get(key);
        return value == null ? defaultValue : (String) value.value;
    }

    void putString(String key, String value) {
        putSetting(key, new Value(String.class, value));
    }

    void putStringWithNoFlush(String key, String value) {
        putSettingWithNoFlush(key, new Value(String.class, value));
    }

    void putTransientString(String key, String value) {
        putSettingWithNoFlush(key, new TransientValue(String.class, value));
    }

    public String getWrittenString(String key, String defaultValue) {
        if (isSettingsLoaded()) {
            return this.sharedPreferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        Value value = (Value) this.cache.get(key);
        return value == null ? defaultValue : ((Integer) value.value).intValue();
    }

    void putInt(String key, int value) {
        putSetting(key, new Value(Integer.class, Integer.valueOf(value)));
    }

    void putIntWithNoFlush(String key, int value) {
        putSettingWithNoFlush(key, new Value(Integer.class, Integer.valueOf(value)));
    }

    void putTransientInt(String key, int value) {
        putSettingWithNoFlush(key, new TransientValue(Integer.class, Integer.valueOf(value)));
    }

    public int getWrittenInt(String key, int defaultValue) {
        if (isSettingsLoaded()) {
            return this.sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        Value value = (Value) this.cache.get(key);
        return value == null ? defaultValue : ((Long) value.value).longValue();
    }

    void putLong(String key, long value) {
        putSetting(key, new Value(Long.class, Long.valueOf(value)));
    }

    void putLongWithNoFlush(String key, long value) {
        putSettingWithNoFlush(key, new Value(Long.class, Long.valueOf(value)));
    }

    void putTransientLong(String key, long value) {
        putSettingWithNoFlush(key, new TransientValue(Long.class, Long.valueOf(value)));
    }

    public long getWrittenLong(String key, long defaultValue) {
        if (isSettingsLoaded()) {
            return this.sharedPreferences.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean value = getBoolean(key, null);
        return value == null ? defaultValue : value.booleanValue();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Value value = (Value) this.cache.get(key);
        return value == null ? defaultValue : (Boolean) value.value;
    }

    void putBoolean(String key, boolean value) {
        putSetting(key, new Value(Boolean.class, Boolean.valueOf(value)));
    }

    void putBooleanWithNoFlush(String key, boolean value) {
        putSettingWithNoFlush(key, new Value(Boolean.class, Boolean.valueOf(value)));
    }

    void putTransientBoolean(String key, boolean value) {
        putSettingWithNoFlush(key, new TransientValue(Boolean.class, Boolean.valueOf(value)));
    }

    public boolean getWrittenBoolean(String key, boolean defaultValue) {
        if (isSettingsLoaded()) {
            return this.sharedPreferences.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    void remove(String key) {
        Value value = (Value) this.cache.remove(key);
        if (value != null && !value.isTransientData && isSettingsLoaded()) {
            flush();
        }
    }

    void removeWithNoFlush(String key) {
        this.cache.remove(key);
    }

    private void commit(Editor editor) {
        if (ThreadUtils.isOnMainThread()) {
            this.logger.m639e("Committing settings must be executed on a background thread.");
        }
        if (AndroidTargetUtils.isAtLeastAndroidAPI(9)) {
            AndroidTargetUtils.editorApply(editor);
        } else {
            editor.commit();
        }
    }

    void notifySettingsListeners() {
        this.listenersLock.lock();
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            ((SettingsListener) i$.next()).settingsLoaded();
        }
        this.listeners.clear();
        this.listeners = null;
        this.listenersLock.unlock();
    }

    void fetchSharedPreferences(Context context) {
        if (!isSettingsLoaded()) {
            SharedPreferences sharedPreferences = getSharedPreferencesFromContext(context);
            readSharedPreferencesIntoCache(sharedPreferences);
            this.sharedPreferences = sharedPreferences;
            writeCacheToSharedPreferences(sharedPreferences);
        }
        this.settingsLoadedLatch.countDown();
        notifySettingsListeners();
    }
}
