package com.nextradioapp.core;

import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.dependencies.IDeviceDescriptor;
import com.nextradioapp.core.dependencies.ILocationAdapter;
import com.nextradioapp.core.dependencies.ILogger;
import com.nextradioapp.core.dependencies.INextRadioEventListener;
import com.nextradioapp.core.dependencies.IPreferenceStorage;
import com.nextradioapp.core.interfaces.IActionBuilder;
import com.nextradioapp.core.interfaces.IEventReceivedListener;
import com.nextradioapp.core.interfaces.IPostalCodeInfoListener;
import com.nextradioapp.core.interfaces.IRabbitMQWrapper;
import com.nextradioapp.core.interfaces.IStationProvider;
import com.nextradioapp.core.interfaces.ITagStationAPIClient;
import com.nextradioapp.core.messaging.RabbitMQWrapper;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.core.objects.DeviceState;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.Location;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.core.radioDNS.IRadioVISListener;
import com.nextradioapp.core.radioDNS.RadioVISReceiver;
import com.nextradioapp.core.reporting.ReportingTracker;
import com.nextradioapp.core.web.InitFailedException;
import com.nextradioapp.core.web.StationProvider;
import com.nextradioapp.core.web.TagStationAPIClient;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.activemq.transport.stomp.Stomp;

public class NextRadioCore {
    private static final String TAG = "NextRadioCore";
    private static IErrorHandler mErrorHandler;
    private IRadioVISListener VISListener;
    private IActionBuilder mActionBuilder;
    NextRadioEventInfo mCurrVisEvent;
    private Thread mCurrentEventThread;
    private Thread mCurrentPostalCodeThread;
    private Thread mCurrentRecentlyPlayedThread;
    private StationInfo mCurrentStationInfo;
    private IDatabaseAdapter mDBAdapter;
    private IDeviceDescriptor mDeviceDescriptor;
    private String mDeviceID;
    private DeviceRegistrationInfo mDeviceRegistrationInfo;
    private INextRadioEventListener mEventListener;
    private Thread mEventLookupThread;
    private boolean mHasInitialized;
    private boolean mIsNoDataMode;
    private ILocationAdapter mLocationAdapter;
    private IPreferenceStorage mPrefStorage;
    IRabbitMQWrapper mRabbitMQClient;
    IRabbitMQWrapper mRabbitMQClientSecondary;
    private ExecutorService mSecondaryEventExecutorService;
    private int[] mSecondaryStationIDs;
    private Thread mStationChangingThread;
    private IStationProvider mStationProvider;
    private ITagStationAPIClient mTagStationAPI;
    RadioVISReceiver mVISReceiver;

    public NextRadioCore() {
        this.mCurrVisEvent = new NextRadioEventInfo();
        this.VISListener = new 2(this);
        this.mVISReceiver = new RadioVISReceiver();
        this.mRabbitMQClient = new RabbitMQWrapper("primary", new 7(this));
        this.mRabbitMQClientSecondary = new RabbitMQWrapper("secondary", new 8(this));
        this.mStationProvider = new StationProvider();
        this.mIsNoDataMode = false;
    }

    public static void setErrorHandler(IErrorHandler handler) {
        mErrorHandler = handler;
    }

    public static void handleError(Exception ex, String[] logs) {
        Log.m1935e(TAG, "handleError");
        if (mErrorHandler != null) {
            ex.printStackTrace();
            if (logs != null && logs.length > 0) {
                for (String log : logs) {
                    Log.m1935e(TAG, log);
                }
            }
            mErrorHandler.handleException(ex, logs);
        }
    }

    public void init(ILocationAdapter locationAdapter) throws InitFailedException {
        Log.m1934d(TAG, "init()");
        if (this.mPrefStorage == null || getDeviceID() == null || getDeviceID().length() == 0) {
            throw new InitFailedException(InitFailedException.ERROR_CODE_NOT_REGISTERED);
        }
        if (this.mTagStationAPI == null) {
            this.mTagStationAPI = new TagStationAPIClient(this.mPrefStorage.getTagURL());
        }
        this.mTagStationAPI.withDeviceRegistrationInfo(getDeviceRegistrationInfo());
        this.mDeviceDescriptor.setDeviceID(getDeviceID());
        this.mLocationAdapter = locationAdapter;
        this.mStationProvider.setLocationAdapter(this.mLocationAdapter);
        this.mVISReceiver.setListener(this.VISListener);
        new 1(this).start();
    }

    public void setDemoMode(boolean isDemoMode) {
        this.mStationProvider.setDemoMode(isDemoMode);
    }

    public String getUrlOverride() {
        return this.mPrefStorage.getTagURL();
    }

    public String getCountryCode() {
        if (this.mPrefStorage != null) {
            return this.mPrefStorage.getCountryString();
        }
        return null;
    }

    public void setCountryCode(String country) {
        this.mPrefStorage.setCountryString(country);
    }

    public int getStepValue() {
        String code = getCountryCode();
        if (code == null || !code.equals("CO")) {
            return 2;
        }
        return 1;
    }

    public boolean getNoDataMode() {
        return this.mIsNoDataMode;
    }

    public boolean overrideDefaultURL(String url) {
        if (url == null || this.mTagStationAPI == null) {
            return false;
        }
        try {
            try {
                new URL(url).toURI();
                Thread t = new Thread(new 3(this, url));
                t.setName("TS-Register");
                t.start();
                return true;
            } catch (URISyntaxException e) {
                return false;
            }
        } catch (MalformedURLException e2) {
            return false;
        }
    }

    public void testLocationChange(Location l) {
        this.mStationProvider.setFixedLocation(l);
    }

    public void deleteSavedEvent(String UFID, boolean isUpdateRequired) {
        this.mDBAdapter.deleteSavedEvent(UFID);
        if (isUpdateRequired && this.mEventListener != null) {
            this.mEventListener.onHistoryEventsUpdated(this.mDBAdapter.getListeningHistory(null, false, true, null, 100));
        }
    }

    private void handleEvent(NextRadioEventInfo nInfo, boolean isFromPlaying, boolean shouldDeliver) {
        if (nInfo != null) {
            if (nInfo.isSaveable()) {
                try {
                    nInfo.eventID = this.mDBAdapter.putListeningActivityEventRecord(nInfo);
                    nInfo.historyID = this.mDBAdapter.putListeningHistoryRecord(nInfo.eventID, isFromPlaying);
                } catch (Exception e) {
                    System.out.println("Exception in Core - handleEvent: " + e.getMessage());
                }
            }
            nInfo.timePlayed = new Date();
            if (shouldDeliver) {
                this.mEventListener.onEventChanged(nInfo);
                this.mEventListener.onHistoryEventsUpdated(this.mDBAdapter.getListeningHistory(null, false, true, null, 100));
            }
        }
    }

    public String getDeviceID() {
        if (this.mDeviceID != null) {
            return this.mDeviceID;
        }
        DeviceRegistrationInfo di = this.mPrefStorage.getDeviceRegistrationInfo();
        if (di == null) {
            return null;
        }
        this.mDeviceID = di.TSD;
        return this.mDeviceID;
    }

    public String getCachingID() {
        DeviceRegistrationInfo di = this.mPrefStorage.getDeviceRegistrationInfo();
        if (di != null) {
            return di.cachingGroup;
        }
        return null;
    }

    public String getAdGroupID() {
        DeviceRegistrationInfo di = this.mPrefStorage.getDeviceRegistrationInfo();
        if (di != null) {
            return di.adGroup;
        }
        return null;
    }

    public NextRadioCore withEventListener(INextRadioEventListener inrel) {
        this.mEventListener = inrel;
        this.mStationProvider.setEventListener(inrel);
        return this;
    }

    public ArrayList<NextRadioEventInfo> getRecentRadioEvents(int limit) {
        ArrayList<NextRadioEventInfo> nInfo = this.mDBAdapter.getListeningHistory(null, false, true, null, limit);
        if (nInfo == null || nInfo.size() == 0) {
            return null;
        }
        return nInfo;
    }

    public void requestEvent(String UFID, StationInfo stationInfo, IEventReceivedListener listener) {
        if (this.mEventLookupThread != null) {
            this.mEventLookupThread.interrupt();
        }
        this.mEventLookupThread = new 4(this, UFID, stationInfo, listener);
        this.mEventLookupThread.setName("TSGetEvent");
        this.mEventLookupThread.start();
    }

    public void requestEvent(String UFID, StationInfo stationInfo) {
        requestEvent(UFID, stationInfo, null);
    }

    public void requestEvent(String UFID, IEventReceivedListener listener) {
        requestEvent(UFID, this.mCurrentStationInfo, listener);
    }

    public void getCurrentEventSecondary(int stationID) {
        if (this.mSecondaryEventExecutorService != null) {
            this.mSecondaryEventExecutorService.execute(new 5(this, stationID));
        }
    }

    public void requestEvent(String UFID) {
        requestEvent(UFID, this.mCurrentStationInfo, null);
    }

    public void requestRecentlyPlayed(int freq, int subchannel) {
        Log.m1934d(TAG, "requestRecentlyPlayed");
        if (this.mCurrentRecentlyPlayedThread != null) {
            this.mCurrentRecentlyPlayedThread.interrupt();
        }
        this.mCurrentRecentlyPlayedThread = new 6(this, freq, subchannel);
        this.mCurrentRecentlyPlayedThread.setName("TSRecentlyPlayed");
        this.mCurrentRecentlyPlayedThread.start();
    }

    private NextRadioEventInfo getCurrentEvent(int freqHz, int subChannel, boolean includeCards) throws InvalidDeviceIDException {
        StationInfo stationInfo = this.mStationProvider.getStationInfo(freqHz, subChannel);
        this.mCurrentStationInfo = stationInfo;
        Log.m1934d(TAG, "getCurrentEvent(" + stationInfo.publicStationID + ")");
        if (getDeviceID() == null || getDeviceID().length() == 0) {
            Log.m1936w(TAG, "- InvalidDeviceIDException");
            throw new InvalidDeviceIDException();
        }
        NextRadioEventInfo nInfo;
        int publicStationID = stationInfo.publicStationID;
        if (includeCards) {
            nInfo = this.mTagStationAPI.getCurrentEventWithCards(publicStationID, this.mDeviceDescriptor.getDeviceDescription());
        } else {
            nInfo = this.mTagStationAPI.getCurrentEvent(publicStationID, this.mDeviceDescriptor.getDeviceDescription());
        }
        if (this.mCurrentStationInfo.publicStationID != publicStationID) {
            Log.m1934d(TAG, "getCurrentEvent - station mismatch, cancelling result");
            return null;
        } else if (nInfo == null) {
            return nInfo;
        } else {
            nInfo.stationInfo = this.mCurrentStationInfo;
            return nInfo;
        }
    }

    private NextRadioEventInfo getCurrentEventImmediate(int freqHz, int subchannel) throws InvalidDeviceIDException {
        StationInfo stationInfo = this.mStationProvider.getStationInfo(freqHz, subchannel);
        this.mCurrentStationInfo = stationInfo;
        NextRadioEventInfo event;
        if (stationInfo.getStationType() == 2 || stationInfo.getStationType() == 3 || stationInfo.getStationType() == 1) {
            event = new NextRadioEventInfo();
            event.stationInfo = stationInfo;
            if (stationInfo.headlineText == null || stationInfo.headline == null) {
                event.title = stationInfo.callLetters;
                event.artist = stationInfo.frequencyMHz();
            } else {
                event.title = stationInfo.headlineText;
                event.artist = stationInfo.headline;
                if (stationInfo.getStationType() == 1) {
                    event.artist = stationInfo.frequencyAndCallLetters();
                }
            }
            event.trackingID = stationInfo.trackingID;
            event.imageURL = stationInfo.imageURL;
            event.imageURLHiRes = stationInfo.imageURLHiRes;
            event.itemType = 3;
            try {
                String toEncode = stationInfo.toString();
                MessageDigest mdEnc = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE);
                mdEnc.update(toEncode.getBytes(), 0, toEncode.length());
                event.UFIDIdentifier = "F" + stationInfo.getCallLetters() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + new BigInteger(1, mdEnc.digest()).toString(16);
            } catch (NoSuchAlgorithmException e) {
                event.UFIDIdentifier = "F" + stationInfo.getCallLetters() + stationInfo.frequencyHz;
            }
            return event;
        }
        event = new NextRadioEventInfo();
        event.stationInfo = stationInfo;
        event.imageURL = stationInfo.imageURL;
        event.title = stationInfo.frequencyAndCallLetters();
        event.itemType = 3;
        return event;
    }

    public void setStationFavoriteStatus(int freqHz, int subchannel, int stationType, boolean isFavorite) {
        this.mStationProvider.setStationFavoriteStatus(freqHz, subchannel, stationType, isFavorite);
    }

    public void setCurrentStation(int freqHz, int subChannel) throws InvalidDeviceIDException {
        this.mCurrentStationInfo = this.mStationProvider.getStationInfo(freqHz, subChannel);
        if (this.mVISReceiver != null) {
            this.mVISReceiver.interrupt();
        }
        if (this.mRabbitMQClient != null) {
            this.mRabbitMQClient.interrupt();
        }
        if (this.mCurrentStationInfo.getStationType() != 0) {
            this.mCurrentStationInfo.lastListened = System.currentTimeMillis();
            this.mDBAdapter.updateStationLastListened(this.mCurrentStationInfo.publicStationID, this.mCurrentStationInfo.lastListened);
            requestStations(false);
            if (this.mStationChangingThread != null) {
                this.mStationChangingThread.interrupt();
            }
            try {
                this.mStationChangingThread = new 9(this);
                this.mStationChangingThread.start();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        NextRadioEventInfo nInfo = new NextRadioEventInfo();
        nInfo.stationInfo = this.mCurrentStationInfo;
        nInfo.title = this.mCurrentStationInfo.frequencyMHz() + Stomp.EMPTY;
        this.mEventListener.onEventChanged(nInfo);
        ReportingTracker.getInstance().clearReporting();
    }

    private void startMQConnection(String endpoint) {
        this.mRabbitMQClient.setTopic("#.station_" + this.mCurrentStationInfo.publicStationID);
        this.mRabbitMQClient.setExchange("radioevents");
        Log.m1934d(TAG, "- starting MQ client");
        this.mRabbitMQClient.begin(endpoint);
    }

    public void startSecondaryEventSubscription(ArrayList<StationInfo> stations) {
        ArrayList<StationInfo> stationsWithEndpoints = new ArrayList();
        String endpoint = null;
        Iterator i$ = stations.iterator();
        while (i$.hasNext()) {
            StationInfo station = (StationInfo) i$.next();
            if (station.endpoint != null && station.endpoint.length() > 0) {
                stationsWithEndpoints.add(station);
                endpoint = station.endpoint;
            }
        }
        int[] stationIDs = new int[stationsWithEndpoints.size()];
        int i = 0;
        while (i < stationsWithEndpoints.size()) {
            if (((StationInfo) stationsWithEndpoints.get(i)).endpoint != null && ((StationInfo) stationsWithEndpoints.get(i)).endpoint.length() > 0) {
                stationIDs[i] = ((StationInfo) stationsWithEndpoints.get(i)).publicStationID;
            }
            i++;
        }
        if (endpoint != null && !this.mIsNoDataMode && stationIDs != null && stationIDs.length >= 1) {
            if (this.mSecondaryEventExecutorService == null) {
                this.mSecondaryEventExecutorService = Executors.newFixedThreadPool(5);
            }
            String[] topics = new String[stationIDs.length];
            int index = 0;
            this.mSecondaryStationIDs = stationIDs;
            for (int station2 : stationIDs) {
                topics[index] = "#.station_" + station2;
                index++;
            }
            Log.m1934d(TAG, "- initializing Secondary MQ client");
            this.mRabbitMQClientSecondary.setSecondaryStationTopics(topics);
            this.mRabbitMQClientSecondary.setExchange("radioevents");
            Log.m1934d(TAG, "- starting Secondary MQ client");
            this.mRabbitMQClientSecondary.begin(endpoint);
        }
    }

    public void stopSecondaryEventSubscription() {
        if (this.mSecondaryEventExecutorService != null) {
            this.mSecondaryEventExecutorService.shutdownNow();
            this.mSecondaryEventExecutorService = null;
        }
        if (this.mRabbitMQClientSecondary != null) {
            this.mRabbitMQClientSecondary.interrupt();
        }
    }

    public void requestPostalCodeInfo(String postCode, String countryCode, IPostalCodeInfoListener postalListener) {
        if (postalListener != null) {
            if (postCode == null) {
                postalListener.onInvalidZip();
                return;
            }
            if (this.mCurrentPostalCodeThread != null) {
                this.mCurrentPostalCodeThread.interrupt();
            }
            this.mCurrentPostalCodeThread = new 10(this, postCode, countryCode, postalListener);
            this.mCurrentPostalCodeThread.setName("TSPostalCode");
            this.mCurrentPostalCodeThread.start();
        }
    }

    public void setStationLocation(Location location) {
        this.mStationProvider.setFixedLocation(location);
    }

    public void setStationLocationAutomatic() {
        this.mStationProvider.resumeLocationTracking();
    }

    public void stopListening() {
        new Thread(new 11(this)).start();
        Log.m1934d(TAG, "stopListening");
    }

    public NextRadioCore withAPIClient(ITagStationAPIClient webServiceClient) {
        this.mTagStationAPI = webServiceClient;
        return this;
    }

    public void requestStations(boolean forceNetwork) {
        if (this.mDeviceID != null && this.mDeviceID.length() > 0) {
            this.mStationProvider.getStations(forceNetwork, null);
        } else if (this.mEventListener != null) {
            this.mEventListener.onStationUpdateFailed(3, forceNetwork);
        }
    }

    private String registerDevice() {
        String lastDeviceRegistrationString = this.mPrefStorage.getDeviceString();
        DeviceState deviceState = this.mDeviceDescriptor.getDeviceDescription();
        deviceState.nextRadioVersion = this.mDeviceDescriptor.getDeviceVersionCode();
        String newDeviceRegistrationString = deviceState.getUpdateString();
        Log.m1934d("TSL", "returnVal.nextRadioVersion :" + deviceState.nextRadioVersion);
        Log.m1934d("TSL", "returnVal.adID :" + deviceState.adID);
        String adID = deviceState.adID;
        if (lastDeviceRegistrationString != null && adID != null && adID.equals(Diagnostics.error)) {
            Log.m1936w("TSL", "adID is an error");
        } else if (getDeviceID() == null || getDeviceID().length() == 0 || lastDeviceRegistrationString == null || !lastDeviceRegistrationString.equals(newDeviceRegistrationString)) {
            this.mTagStationAPI.withDeviceID(getDeviceID());
            DeviceRegistrationInfo deviceInfo = this.mTagStationAPI.registerDevice(this.mDeviceDescriptor.getDeviceDescription());
            if (deviceInfo != null && deviceInfo.TSD != null && deviceInfo.TSD.length() > 0) {
                this.mPrefStorage.setDeviceRegistration(deviceInfo);
                this.mTagStationAPI.withDeviceRegistrationInfo(deviceInfo);
                this.mDeviceID = deviceInfo.TSD;
                if (this.mEventListener != null) {
                    this.mEventListener.onRegisterCompleted();
                }
            } else if (this.mEventListener != null) {
                if (getDeviceID() == null || getDeviceID().length() == 0) {
                    Log.m1936w("TSL", "onRegisterFailed");
                    this.mEventListener.onRegisterFailed();
                } else {
                    Log.m1936w("TSL", "allowing this registration call to pass");
                    this.mEventListener.onRegisterCompleted();
                }
            }
            this.mPrefStorage.setDeviceString(newDeviceRegistrationString);
        } else {
            this.mTagStationAPI.withDeviceRegistrationInfo(getDeviceRegistrationInfo());
            if (this.mEventListener != null) {
                this.mEventListener.onRegisterCompleted();
            }
        }
        return this.mDeviceID;
    }

    @Deprecated
    public void setLogger(ILogger logger) {
        Log.instance = logger;
    }

    public void recordVisualImpression(String trackingID, int source, int stationID, String cardTrackID, String teID) {
        this.mDBAdapter.recordVisualImpression(trackingID, source, stationID, cardTrackID, teID);
    }

    public void putLocationData(int source, int action, String latitude, String longitude) {
        this.mDBAdapter.putLocationData(source, action, latitude, longitude);
    }

    public void recordActionImpression(ActionPayload payload, int action, int source) {
        this.mDBAdapter.recordActionImpression(payload, action, source);
    }

    @Deprecated
    public void withStationDownloader(IStationProvider stationDownloader) {
        this.mStationProvider = stationDownloader;
    }

    public void addSavedEvent(String UFID) {
        deleteSavedEvent(UFID, false);
        long record = this.mDBAdapter.getMostRecentEventFromHistory(UFID);
        if (record != -1) {
            try {
                this.mDBAdapter.updateListeningHistoryAsFavorite(record, true);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            if (this.mEventListener != null) {
                this.mEventListener.onHistoryEventsUpdated(this.mDBAdapter.getListeningHistory(null, false, true, null, 100));
            }
        }
    }

    public ArrayList<EventAction> getEventActions(NextRadioEventInfo nInfo) {
        if (nInfo == null) {
            return null;
        }
        if (nInfo.stationInfo == null) {
            nInfo.stationInfo = this.mCurrentStationInfo;
        }
        return this.mActionBuilder.getEventActions(nInfo);
    }

    public ArrayList<NextRadioEventInfo> getSavedItems() {
        return this.mDBAdapter.getListeningHistory(null, true, false, null, 100);
    }

    public void setNoDataMode(boolean isNoDataMode) {
        this.mIsNoDataMode = isNoDataMode;
        if (this.mIsNoDataMode) {
            stopListening();
            stopSecondaryEventSubscription();
        }
        this.mStationProvider.setNoDataMode(this.mIsNoDataMode);
        this.mPrefStorage.setNoDataMode(this.mIsNoDataMode);
    }

    public EventAction getEventActionFromMap(NextRadioEventInfo nInfo, String type, ActionPayload payload, Map map) {
        return this.mActionBuilder.getEventAction(nInfo, type, payload, map);
    }

    public void setDeviceID(String deviceID) {
        this.mPrefStorage.setDeviceID(deviceID);
        this.mDeviceID = deviceID;
    }

    public void registerDevice(IDatabaseAdapter dbAdapter, IPreferenceStorage prefStorage, IDeviceDescriptor deviceDescriptor, IActionBuilder actionBuilder) {
        this.mPrefStorage = prefStorage;
        this.mDBAdapter = dbAdapter;
        this.mDeviceDescriptor = deviceDescriptor;
        this.mActionBuilder = actionBuilder;
        this.mIsNoDataMode = this.mPrefStorage.getNoDataMode();
        if (this.mTagStationAPI == null) {
            this.mTagStationAPI = new TagStationAPIClient(this.mPrefStorage.getTagURL());
        }
        this.mStationProvider.init(this.mDBAdapter, this.mPrefStorage, this.mTagStationAPI, this.mEventListener, this.mIsNoDataMode);
        registerDevice();
    }

    public DeviceRegistrationInfo getDeviceRegistrationInfo() {
        return this.mPrefStorage.getDeviceRegistrationInfo();
    }

    private void requestNewRegisterDevice() {
        this.mTagStationAPI.withDeviceRegistrationInfo(getDeviceRegistrationInfo());
        DeviceRegistrationInfo deviceInfo = this.mTagStationAPI.registerDevice(this.mDeviceDescriptor.getDeviceDescription());
        if (deviceInfo != null && deviceInfo.TSD != null && deviceInfo.TSD.length() > 0) {
            this.mPrefStorage.setDeviceRegistration(deviceInfo);
            this.mTagStationAPI.withDeviceRegistrationInfo(deviceInfo);
            this.mDeviceID = deviceInfo.TSD;
            if (this.mEventListener != null) {
                this.mEventListener.onRegisterCompleted();
            }
            this.mStationProvider.getStations(true, null);
        } else if (this.mEventListener == null) {
        } else {
            if (getDeviceID() == null || getDeviceID().length() == 0) {
                Log.m1936w("TSL", "onRegisterFailed");
                this.mEventListener.onRegisterFailed();
                return;
            }
            Log.m1936w("TSL", "allowing this registration call to pass");
            this.mEventListener.onRegisterCompleted();
        }
    }

    public void setCachingID(String ID) {
        this.mPrefStorage.setCachingID(ID);
        this.mTagStationAPI = this.mTagStationAPI.withCachingID(ID);
    }

    public void setAdGroupID(String ID) {
        this.mPrefStorage.setAdGroupID(ID);
    }

    public NextRadioEventInfo getEventLocal(String ufid) {
        long index = this.mDBAdapter.getMostRecentEventFromHistory(ufid);
        if (index > 0) {
            return this.mDBAdapter.fetchEvent(index);
        }
        return null;
    }

    public boolean hasIntialized() {
        return this.mHasInitialized;
    }

    public Location getCurrentLocation() {
        return this.mStationProvider.getCurrentLocation();
    }

    public void setLocationUpdateInterval(boolean value) {
        this.mStationProvider.setLocationUpdateInterval(value);
    }

    public void shutdownLocationServices() {
        if (this.mLocationAdapter != null) {
            this.mLocationAdapter.shutdownLocationServices();
        }
    }

    public void resumeLocationUpdates() {
        if (this.mLocationAdapter != null) {
            this.mLocationAdapter.resumeLocationUpdates();
        }
    }
}
