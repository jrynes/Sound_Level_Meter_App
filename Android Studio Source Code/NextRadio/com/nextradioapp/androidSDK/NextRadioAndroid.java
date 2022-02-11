package com.nextradioapp.androidSDK;

import android.content.Context;
import android.util.Log;
import com.nextradioapp.androidSDK.actions.EventActionBuilder;
import com.nextradioapp.androidSDK.ext.DatabaseAdapter;
import com.nextradioapp.androidSDK.ext.DeviceDescriptor;
import com.nextradioapp.androidSDK.ext.LocationAdapter;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.androidSDK.interfaces.IActivityManager;
import com.nextradioapp.core.InvalidDeviceIDException;
import com.nextradioapp.core.NextRadioCore;
import com.nextradioapp.core.NextRadioCore.IErrorHandler;
import com.nextradioapp.core.dependencies.INextRadioEventListener;
import com.nextradioapp.core.interfaces.IEventReceivedListener;
import com.nextradioapp.core.interfaces.IPostalCodeInfoListener;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.DeviceRegistrationInfo;
import com.nextradioapp.core.objects.EventAction;
import com.nextradioapp.core.objects.Location;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import com.nextradioapp.core.web.InitFailedException;
import com.rabbitmq.client.impl.AMQConnection;
import java.util.ArrayList;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class NextRadioAndroid {
    private static final String TAG = "NextRadioAndroid";
    private static NextRadioAndroid instance;
    private static NextRadioCore mCore;
    private EventActionBuilder mActionBuilder;
    private IActivityManager mContext;
    private LocationAdapter mLocationAdapter;
    private String mRadioSourceName;

    private NextRadioAndroid() {
    }

    public static synchronized NextRadioAndroid getInstance() {
        NextRadioAndroid nextRadioAndroid;
        synchronized (NextRadioAndroid.class) {
            if (instance == null) {
                instance = new NextRadioAndroid();
                mCore = new NextRadioCore();
            }
            nextRadioAndroid = instance;
        }
        return nextRadioAndroid;
    }

    public void init(Context context) throws InitFailedException {
        Log.d(TAG, "init");
        if (this.mLocationAdapter == null) {
            this.mLocationAdapter = new LocationAdapter(context, AMQConnection.HANDSHAKE_TIMEOUT);
        }
        mCore.init(this.mLocationAdapter);
    }

    public void setEventListener(INextRadioEventListener eventListener) {
        mCore.withEventListener(eventListener);
    }

    public void requestStations(boolean forceNetwork) {
        register(this.mContext, this.mRadioSourceName);
        mCore.requestStations(forceNetwork);
    }

    public void startListeningToStation(int freqHz, int subChannel) {
        Log.d(TAG, "startListeningToStation " + freqHz + Headers.SEPERATOR + subChannel);
        try {
            mCore.setCurrentStation(freqHz, subChannel);
        } catch (InvalidDeviceIDException e) {
            e.printStackTrace();
        }
    }

    public void setStationFavoriteStatus(int freqHz, int subchannel, int stationType, boolean isFavorited) {
        mCore.setStationFavoriteStatus(freqHz, subchannel, stationType, isFavorited);
    }

    public boolean overrideDefaultURL(String URL) {
        return mCore.overrideDefaultURL(URL);
    }

    public void setNoDataMode(boolean isNoDataMode) {
        mCore.setNoDataMode(isNoDataMode);
    }

    public boolean getNoDataMode() {
        return mCore.getNoDataMode();
    }

    public String getUrlOverride() {
        return mCore.getUrlOverride();
    }

    public String getCountryCode() {
        return mCore.getCountryCode();
    }

    public void setCountryCode(String countryCode) {
        mCore.setCountryCode(countryCode);
    }

    public void addSavedEvent(String UFID) {
        mCore.addSavedEvent(UFID);
    }

    public void deleteSavedEvent(String UFID) {
        mCore.deleteSavedEvent(UFID, true);
    }

    public void stopListening() {
        Log.d(TAG, "<!> STOP LISTENING");
        mCore.stopListening();
    }

    public void requestRecentlyPlayed(int freqHz, int subChannel) {
        mCore.requestRecentlyPlayed(freqHz, subChannel);
    }

    public void getEvent(String UFID, StationInfo stationInfo) {
        mCore.requestEvent(UFID, stationInfo);
    }

    public void getEvent(String UFID) {
        mCore.requestEvent(UFID);
    }

    public ArrayList<NextRadioEventInfo> getLocalHistory(int limit) {
        return mCore.getRecentRadioEvents(limit);
    }

    public ArrayList<NextRadioEventInfo> getSavedEvents() {
        return mCore.getSavedItems();
    }

    public ArrayList<EventAction> getEventActions(NextRadioEventInfo eventInfo) {
        if (this.mActionBuilder == null) {
            return null;
        }
        return mCore.getEventActions(eventInfo);
    }

    public EventAction getPrimaryEventActions(NextRadioEventInfo eventInfo) {
        if (this.mActionBuilder == null) {
            return null;
        }
        return this.mActionBuilder.getPrimaryEventAction(eventInfo);
    }

    public void setLocation(Location location) {
        mCore.testLocationChange(location);
    }

    public void setDemoMode(boolean isDemoMode) {
        mCore.setDemoMode(isDemoMode);
    }

    public void getEvent(String UFID, IEventReceivedListener listener) {
        mCore.requestEvent(UFID, listener);
    }

    public void startSecondarySubscriptions(ArrayList<StationInfo> stations) {
        mCore.startSecondaryEventSubscription(stations);
    }

    public void getCurrentEventSecondary(int stationID) {
        mCore.getCurrentEventSecondary(stationID);
    }

    public void putLocationData(int source, int action, String latitude, String longitude) {
        mCore.putLocationData(source, action, latitude, longitude);
    }

    public void stopSecondaryEventSubscription() {
        mCore.stopSecondaryEventSubscription();
    }

    public void requestPostalCodeInfo(String postalCode, String countryCode, IPostalCodeInfoListener postalListener) {
        mCore.requestPostalCodeInfo(postalCode, countryCode, postalListener);
    }

    public EventAction getEventActionFromMap(NextRadioEventInfo nInfo, String type, ActionPayload payload, Map map) {
        return mCore.getEventActionFromMap(nInfo, type, payload, map);
    }

    public void recordVisualImpression(String trackingID, int trackingContext, int stationID, String cardTrackID, String teID) {
        mCore.recordVisualImpression(trackingID, trackingContext, stationID, cardTrackID, teID);
    }

    public NextRadioEventInfo getEventLocal(String UFID) {
        return mCore.getEventLocal(UFID);
    }

    public void register(IActivityManager context, String radioSourceName) {
        this.mContext = context;
        this.mRadioSourceName = radioSourceName;
        DatabaseAdapter db = new DatabaseAdapter(context.getCurrentApplication());
        this.mActionBuilder = new EventActionBuilder(db, context);
        mCore.registerDevice(db, new PreferenceStorage(context.getCurrentApplication()), new DeviceDescriptor(context.getCurrentApplication(), radioSourceName), this.mActionBuilder);
    }

    public boolean hasInitialized() {
        return mCore.hasIntialized();
    }

    public void setErrorHandler(IErrorHandler handler) {
        NextRadioCore nextRadioCore = mCore;
        NextRadioCore.setErrorHandler(handler);
    }

    public int getTuneStepValue() {
        if (mCore != null) {
            return mCore.getStepValue();
        }
        return 2;
    }

    public void setAdGroupID(String ID) {
        mCore.setAdGroupID(ID);
    }

    public void setCachingID(String ID) {
        mCore.setCachingID(ID);
    }

    public Location getCurrentLocation() {
        return mCore.getCurrentLocation();
    }

    public void setLocationUpdateInterval(boolean value) {
        mCore.setLocationUpdateInterval(value);
    }

    public void shutdownLocationServices() {
        mCore.shutdownLocationServices();
    }

    public void resumeLocationUpdates() {
        mCore.resumeLocationUpdates();
    }

    public DeviceRegistrationInfo getDeviceRegistrationInfo() {
        return mCore.getDeviceRegistrationInfo();
    }

    public boolean isTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & 15) >= 3) & context.getResources().getBoolean(C1136R.bool.isTablet);
    }
}
