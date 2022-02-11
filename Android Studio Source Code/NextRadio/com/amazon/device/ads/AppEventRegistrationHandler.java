package com.amazon.device.ads;

import com.amazon.device.ads.FileOutputHandler.WriteMethod;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class AppEventRegistrationHandler {
    protected static final String APP_EVENTS_FILE = "AppEventsJsonFile";
    protected static final long APP_EVENTS_FILE_MAX_SIZE = 1048576;
    protected static final String APP_EVENT_NAME_KEY = "evtName";
    protected static final String APP_EVENT_TIMESTAMP_KEY = "ts";
    protected static final String INSTALL_REFERRER_EVENT_NAME = "INSTALL_REFERRER";
    private static final String LOGTAG;
    protected static AppEventRegistrationHandler instance;
    protected final Object appEventsFileLock;
    protected final Set<String> eventsSent;
    private final FileHandlerFactory fileHandlerFactory;
    private FileInputHandler fileInputHandler;
    private FileOutputHandler fileOutputHandler;
    private final MobileAdsInfoStore infoStore;
    private final MobileAdsLogger logger;
    protected final Set<String> newEventsToSave;

    /* renamed from: com.amazon.device.ads.AppEventRegistrationHandler.1 */
    class C02971 implements Runnable {
        final /* synthetic */ AppEvent val$appEvent;

        C02971(AppEvent appEvent) {
            this.val$appEvent = appEvent;
        }

        public void run() {
            AppEventRegistrationHandler.this.appendAppEventToFile(this.val$appEvent);
            if (this.val$appEvent.getEventName().equals(AppEventRegistrationHandler.INSTALL_REFERRER_EVENT_NAME) && AppEventRegistrationHandler.this.infoStore.getRegistrationInfo().isRegisteredWithSIS()) {
                AppEventRegistrationHandler.this.infoStore.getSISRegistration().registerEvents();
            }
        }
    }

    static {
        LOGTAG = AppEventRegistrationHandler.class.getSimpleName();
        instance = new AppEventRegistrationHandler(MobileAdsInfoStore.getInstance(), new DefaultFileHandlerFactory());
    }

    protected AppEventRegistrationHandler(MobileAdsInfoStore infoStore, FileHandlerFactory fileHandlerFactory) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.infoStore = infoStore;
        this.fileHandlerFactory = fileHandlerFactory;
        this.appEventsFileLock = new Object();
        this.newEventsToSave = Collections.synchronizedSet(new HashSet());
        this.eventsSent = Collections.synchronizedSet(new HashSet());
    }

    public static AppEventRegistrationHandler getInstance() {
        return instance;
    }

    public void addEventToAppEventsCacheFile(AppEvent appEvent) {
        ThreadUtils.scheduleRunnable(new C02971(appEvent));
    }

    private boolean createFileOutputHandlerIfNeeded() {
        if (this.fileOutputHandler == null) {
            File fileDir = this.infoStore.getFilesDir();
            if (fileDir == null) {
                this.logger.m639e("No files directory has been set.");
                return false;
            }
            this.fileOutputHandler = this.fileHandlerFactory.createFileOutputHandler(fileDir, APP_EVENTS_FILE);
        }
        if (this.fileOutputHandler != null) {
            return true;
        }
        return false;
    }

    private boolean createFileInputHandlerIfNeeded() {
        if (this.fileInputHandler == null) {
            File fileDir = this.infoStore.getFilesDir();
            if (fileDir == null) {
                this.logger.m639e("No files directory has been set.");
                return false;
            }
            this.fileInputHandler = this.fileHandlerFactory.createFileInputHandler(fileDir, APP_EVENTS_FILE);
        }
        if (this.fileInputHandler != null) {
            return true;
        }
        return false;
    }

    protected void appendAppEventToFile(AppEvent appEvent) {
        if (createFileOutputHandlerIfNeeded()) {
            JSONObject jsonEvent = new JSONObject();
            try {
                jsonEvent.put(APP_EVENT_NAME_KEY, appEvent.getEventName());
                jsonEvent.put(APP_EVENT_TIMESTAMP_KEY, appEvent.getTimestamp());
                for (Entry<String, String> prop : appEvent.getPropertyEntries()) {
                    jsonEvent.put((String) prop.getKey(), prop.getValue());
                }
                this.newEventsToSave.add(jsonEvent.toString());
                synchronized (this.appEventsFileLock) {
                    String toAppend = jsonEvent.toString() + Stomp.NEWLINE;
                    if (this.fileOutputHandler.getFileLength() + ((long) toAppend.length()) > APP_EVENTS_FILE_MAX_SIZE) {
                        this.logger.m646w("Couldn't write the application event %s to the cache file. Maximum size limit reached.", appEvent.toString());
                        return;
                    }
                    if (this.fileOutputHandler.open(WriteMethod.APPEND)) {
                        try {
                            this.fileOutputHandler.write(toAppend);
                            this.logger.m638d("Added the application event %s to the cache file.", appEvent.toString());
                        } catch (IOException e) {
                            this.logger.m646w("Couldn't write the application event %s to the file.", appEvent.toString());
                        }
                    }
                    this.fileOutputHandler.close();
                    return;
                }
            } catch (JSONException e2) {
                this.logger.m646w("Internal error while persisting the application event %s.", appEvent.toString());
                return;
            }
        }
        this.logger.m639e("Error creating file output handler.");
    }

    public JSONArray getAppEventsJSONArray() {
        if (createFileInputHandlerIfNeeded()) {
            synchronized (this.appEventsFileLock) {
                if (!this.fileInputHandler.doesFileExist()) {
                    return null;
                } else if (this.fileInputHandler.open()) {
                    JSONArray jsonArray = new JSONArray();
                    while (true) {
                        String jsonEventString = this.fileInputHandler.readLine();
                        if (jsonEventString != null) {
                            JSONObject jsonEventObject = JSONUtils.getJSONObjectFromString(jsonEventString);
                            if (jsonEventObject == null) {
                                onAppEventsRegistered();
                                this.fileInputHandler.close();
                                return null;
                            }
                            jsonArray.put(jsonEventObject);
                            this.eventsSent.add(jsonEventObject.toString());
                        } else {
                            this.fileInputHandler.close();
                            if (jsonArray.length() > 0) {
                                return jsonArray;
                            }
                            return null;
                        }
                    }
                } else {
                    this.logger.m639e("App Events File could not be opened.");
                    return null;
                }
            }
        }
        this.logger.m639e("Error creating file input handler.");
        return null;
    }

    public void onAppEventsRegistered() {
        if (createFileOutputHandlerIfNeeded()) {
            synchronized (this.appEventsFileLock) {
                this.newEventsToSave.removeAll(this.eventsSent);
                if (this.newEventsToSave.isEmpty()) {
                    this.infoStore.getApplicationContext().deleteFile(APP_EVENTS_FILE);
                    this.eventsSent.clear();
                } else {
                    StringBuilder toWrite = new StringBuilder();
                    synchronized (this.newEventsToSave) {
                        for (String newEvent : this.newEventsToSave) {
                            toWrite.append(newEvent).append(Stomp.NEWLINE);
                        }
                    }
                    if (this.fileOutputHandler.open(WriteMethod.APPEND)) {
                        try {
                            this.fileOutputHandler.write(toWrite.toString());
                            this.newEventsToSave.clear();
                            this.eventsSent.clear();
                        } catch (IOException e) {
                            this.logger.m645w("Couldn't write the application event(s) to the file.");
                        }
                    }
                    this.fileOutputHandler.close();
                }
            }
            return;
        }
        this.logger.m639e("Error creating file output handler.");
    }
}
