package com.nextradioapp.androidSDK.ext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import com.nextradioapp.androidSDK.data.CursorHelper;
import com.nextradioapp.androidSDK.data.CustomSQLiteOpenHelper;
import com.nextradioapp.androidSDK.data.schema.Queries;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import com.nextradioapp.androidSDK.data.schema.Tables.impressionReporting;
import com.nextradioapp.androidSDK.data.schema.Tables.impressionVisualReporting;
import com.nextradioapp.androidSDK.data.schema.Tables.listeningActivityAds;
import com.nextradioapp.androidSDK.data.schema.Tables.listeningHistory;
import com.nextradioapp.androidSDK.data.schema.Tables.locationTracking;
import com.nextradioapp.androidSDK.data.schema.Tables.stationReporting;
import com.nextradioapp.androidSDK.data.schema.Tables.stations;
import com.nextradioapp.core.dependencies.IDatabaseAdapter;
import com.nextradioapp.core.objects.ActionPayload;
import com.nextradioapp.core.objects.DateTransform;
import com.nextradioapp.core.objects.Enhancement;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import com.nextradioapp.core.objects.StationInfo;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.util.ThreadPoolUtils;

public class DatabaseAdapter implements IDatabaseAdapter {
    private static final String TAG = "DatabaseAdapter";
    private static final int VISUAL_IMPRESSION_INSERT_TIME_DIFF = 59;
    private SQLiteDatabase database;
    public Context mContext;

    /* renamed from: com.nextradioapp.androidSDK.ext.DatabaseAdapter.1 */
    class C11421 extends StationInfo {
        final /* synthetic */ DatabaseAdapter this$0;
        final /* synthetic */ CursorHelper val$ch;

        C11421(DatabaseAdapter databaseAdapter, CursorHelper cursorHelper) {
            boolean z = true;
            this.this$0 = databaseAdapter;
            this.val$ch = cursorHelper;
            this.frequencyHz = this.val$ch.getInt(stations.frequency);
            this.frequencySubChannel = this.val$ch.getInt(stations.frequencySubChannel);
            this.publicStationID = this.val$ch.getInt(stations.publicStationID);
            this.callLetters = this.val$ch.getString(stations.callLetters);
            this.genre = this.val$ch.getString(stations.genre);
            this.endpoint = this.val$ch.getString(stations.endpoint);
            this.headline = this.val$ch.getString(stations.headline);
            this.headlineText = this.val$ch.getString(stations.headlineText);
            this.imageURL = this.val$ch.getString(stations.imageURL);
            this.imageURLHiRes = this.val$ch.getString(stations.imageURLHiRes);
            this.market = this.val$ch.getString(stations.market);
            this.description = this.val$ch.getString(stations.artistList);
            this.slogan = this.val$ch.getString(stations.slogan);
            this.imageURLHiRes = this.val$ch.getString(stations.imageURL);
            this.country = this.val$ch.getString(stations.countryCode);
            this.piCode = this.val$ch.getString(stations.piCode);
            this.isValid = this.val$ch.getInt(stations.IsValid) == 1;
            if (this.val$ch.getInt(stations.IsFavorite) != 1) {
                z = false;
            }
            this.isFavorited = z;
            this.lastListened = this.val$ch.getLong(stations.lastListened);
            this.trackingID = this.val$ch.getString(stations.trackingID);
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.ext.DatabaseAdapter.2 */
    class C11432 extends StationInfo {
        final /* synthetic */ DatabaseAdapter this$0;
        final /* synthetic */ CursorHelper val$ch;

        C11432(DatabaseAdapter databaseAdapter, CursorHelper cursorHelper) {
            boolean z;
            boolean z2 = true;
            this.this$0 = databaseAdapter;
            this.val$ch = cursorHelper;
            this.frequencyHz = this.val$ch.getInt(stations.frequency);
            this.frequencySubChannel = this.val$ch.getInt(stations.frequencySubChannel);
            this.publicStationID = this.val$ch.getInt(stations.publicStationID);
            this.callLetters = this.val$ch.getString(stations.callLetters);
            this.endpoint = this.val$ch.getString(stations.endpoint);
            this.headline = this.val$ch.getString(stations.headline);
            this.headlineText = this.val$ch.getString(stations.headlineText);
            this.imageURL = this.val$ch.getString(stations.imageURL);
            this.imageURLHiRes = this.val$ch.getString(stations.imageURLHiRes);
            this.market = this.val$ch.getString(stations.market);
            this.description = this.val$ch.getString(stations.artistList);
            this.slogan = this.val$ch.getString(stations.slogan);
            this.genre = this.val$ch.getString(stations.genre);
            this.country = this.val$ch.getString(stations.countryCode);
            this.piCode = this.val$ch.getString(stations.piCode);
            if (this.val$ch.getInt(stations.IsValid) == 1) {
                z = true;
            } else {
                z = false;
            }
            this.isValid = z;
            if (this.val$ch.getInt(stations.IsFavorite) != 1) {
                z2 = false;
            }
            this.isFavorited = z2;
            this.lastListened = this.val$ch.getLong(stations.lastListened);
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.ext.DatabaseAdapter.3 */
    class C11443 extends StationInfo {
        final /* synthetic */ DatabaseAdapter this$0;
        final /* synthetic */ Cursor val$c;

        C11443(DatabaseAdapter databaseAdapter, Cursor cursor) {
            boolean z = true;
            this.this$0 = databaseAdapter;
            this.val$c = cursor;
            this.frequencyHz = this.val$c.getInt(this.val$c.getColumnIndex(stations.frequency));
            this.frequencySubChannel = this.val$c.getInt(this.val$c.getColumnIndex(stations.frequencySubChannel));
            this.callLetters = this.val$c.getString(this.val$c.getColumnIndex(stations.callLetters));
            this.slogan = this.val$c.getString(this.val$c.getColumnIndex(stations.slogan));
            this.publicStationID = this.val$c.getInt(this.val$c.getColumnIndex(stations.publicStationID));
            this.imageURL = this.val$c.getString(this.val$c.getColumnIndex(stations.imageURL));
            this.imageURLHiRes = this.val$c.getString(this.val$c.getColumnIndex(stations.imageURLHiRes));
            this.headline = this.val$c.getString(this.val$c.getColumnIndex(stations.headline));
            this.headlineText = this.val$c.getString(this.val$c.getColumnIndex(stations.headlineText));
            this.genre = this.val$c.getString(this.val$c.getColumnIndex(stations.genre));
            this.market = this.val$c.getString(this.val$c.getColumnIndex(stations.market));
            this.endpoint = this.val$c.getString(this.val$c.getColumnIndex(stations.endpoint));
            this.description = this.val$c.getString(this.val$c.getColumnIndex(stations.artistList));
            this.isFavorited = this.val$c.getInt(this.val$c.getColumnIndex(stations.IsFavorite)) == 1;
            this.piCode = this.val$c.getString(this.val$c.getColumnIndex(stations.piCode));
            this.country = this.val$c.getString(this.val$c.getColumnIndex(stations.countryCode));
            if (this.val$c.getInt(this.val$c.getColumnIndex(stations.IsValid)) != 1) {
                z = false;
            }
            this.isValid = z;
            this.lastListened = this.val$c.getLong(this.val$c.getColumnIndex(stations.lastListened));
            this.trackingID = this.val$c.getString(this.val$c.getColumnIndex(stations.trackingID));
        }
    }

    /* renamed from: com.nextradioapp.androidSDK.ext.DatabaseAdapter.4 */
    class C11454 extends StationInfo {
        final /* synthetic */ DatabaseAdapter this$0;
        final /* synthetic */ Cursor val$c;

        C11454(DatabaseAdapter databaseAdapter, Cursor cursor) {
            boolean z = true;
            this.this$0 = databaseAdapter;
            this.val$c = cursor;
            this.frequencyHz = this.val$c.getInt(this.val$c.getColumnIndex(stations.frequency));
            this.frequencySubChannel = this.val$c.getInt(this.val$c.getColumnIndex(stations.frequencySubChannel));
            this.callLetters = this.val$c.getString(this.val$c.getColumnIndex(stations.callLetters));
            this.slogan = this.val$c.getString(this.val$c.getColumnIndex(stations.slogan));
            this.publicStationID = this.val$c.getInt(this.val$c.getColumnIndex(stations.publicStationID));
            this.imageURL = this.val$c.getString(this.val$c.getColumnIndex(stations.imageURL));
            this.imageURLHiRes = this.val$c.getString(this.val$c.getColumnIndex(stations.imageURLHiRes));
            this.headline = this.val$c.getString(this.val$c.getColumnIndex(stations.headline));
            this.headlineText = this.val$c.getString(this.val$c.getColumnIndex(stations.headlineText));
            this.genre = this.val$c.getString(this.val$c.getColumnIndex(stations.genre));
            this.market = this.val$c.getString(this.val$c.getColumnIndex(stations.market));
            this.endpoint = this.val$c.getString(this.val$c.getColumnIndex(stations.endpoint));
            this.description = this.val$c.getString(this.val$c.getColumnIndex(stations.artistList));
            this.isFavorited = this.val$c.getInt(this.val$c.getColumnIndex(stations.IsFavorite)) == 1;
            this.piCode = this.val$c.getString(this.val$c.getColumnIndex(stations.piCode));
            this.country = this.val$c.getString(this.val$c.getColumnIndex(stations.countryCode));
            if (this.val$c.getInt(this.val$c.getColumnIndex(stations.IsValid)) != 1) {
                z = false;
            }
            this.isValid = z;
            this.lastListened = this.val$c.getLong(this.val$c.getColumnIndex(stations.lastListened));
        }
    }

    public DatabaseAdapter(Context c) {
        this.database = CustomSQLiteOpenHelper.getInstance(c).getWritableDatabase();
        this.mContext = c;
    }

    public void updateStationReporting(int publicStationID, String lastUFID) {
        ContentValues cv = new ContentValues();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String currentUTCString = DateFormats.iso8601FormatUTC(cal.getTime());
        cv.put(stationReporting.endTime, currentUTCString);
        cv.put(stationReporting.lastUFID, checkNull(lastUFID));
        this.database.execSQL("DELETE FROM stationReporting WHERE _id NOT IN (SELECT _id from stationReporting ORDER BY startTime DESC LIMIT 900)");
        Cursor c = this.database.rawQuery("select _id from stationReporting order by endTime desc limit 1", null);
        int _id = 0;
        try {
            if (c.moveToFirst()) {
                _id = c.getInt(0);
            }
            int rowsAffected = this.database.update(stationReporting.tableName, cv, "publicStationID = " + publicStationID + " and isClosed = 0 and _id=" + _id, null);
            long sessionFrequencyTime;
            if (rowsAffected != 0) {
                if (rowsAffected == 0) {
                    sessionFrequencyTime = getSessionFrequencyDifference(currentUTCString, publicStationID);
                    if (sessionFrequencyTime <= 11 || sessionFrequencyTime == -1) {
                        cal.add(13, -5);
                        cv.put(stationReporting.startTime, DateFormats.iso8601FormatUTC(cal.getTime()));
                        cv.put(stations.publicStationID, Integer.valueOf(publicStationID));
                        cv.put(stationReporting.isClosed, Boolean.valueOf(false));
                        this.database.insert(stationReporting.tableName, null, cv);
                    } else {
                        this.database.update(stationReporting.tableName, cv, "publicStationID = " + publicStationID + " and _id = " + _id, null);
                    }
                }
                if (c != null) {
                    c.close();
                }
            }
            if (rowsAffected == 0) {
                sessionFrequencyTime = getSessionFrequencyDifference(currentUTCString, publicStationID);
                if (sessionFrequencyTime <= 11) {
                }
                cal.add(13, -5);
                cv.put(stationReporting.startTime, DateFormats.iso8601FormatUTC(cal.getTime()));
                cv.put(stations.publicStationID, Integer.valueOf(publicStationID));
                cv.put(stationReporting.isClosed, Boolean.valueOf(false));
                this.database.insert(stationReporting.tableName, null, cv);
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private long getSessionFrequencyDifference(String mEndTime, int stationID) {
        long differenceDates = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Cursor c = this.database.rawQuery("SELECT endTime, publicStationID FROM stationReporting ORDER BY _id DESC LIMIT 1", null);
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                String endTime = c.getString(0);
                if (c.getInt(1) == stationID) {
                    differenceDates = Math.abs(sdf.parse(mEndTime).getTime() - sdf.parse(endTime).getTime()) / ThreadPoolUtils.DEFAULT_SHUTDOWN_AWAIT_TERMINATION;
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        return differenceDates;
    }

    public void closeOpenStationReporting(boolean forceUpdateAll) {
        ContentValues cv = new ContentValues();
        cv.put(stationReporting.isClosed, Boolean.valueOf(true));
        if (forceUpdateAll) {
            this.database.update(stationReporting.tableName, cv, null, null);
            return;
        }
        Calendar cal10SecAgo = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal10SecAgo.add(13, -5);
        this.database.update(stationReporting.tableName, cv, "endTime < '" + DateFormats.iso8601FormatUTC(cal10SecAgo.getTime()) + "'", null);
    }

    public ArrayList<String[]> getStationReporting(String batchId) {
        ArrayList<String[]> returnVal = new ArrayList();
        Cursor c = this.database.rawQuery("select publicStationId, startTime, endTime, lastUFID from stationReporting where isClosed == 1 AND batchID = '" + batchId + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            try {
                String startString = DateTransform.msSqlDateFormat(DateTransform.iso8601Parse(c.getString(1))) + Stomp.EMPTY;
                String endString = DateTransform.msSqlDateFormat(DateTransform.iso8601Parse(c.getString(2))) + Stomp.EMPTY;
                returnVal.add(new String[]{c.getInt(0) + Stomp.EMPTY, startString, endString, checkNull(c.getString(3))});
            } catch (ParseException e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return returnVal;
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            c.moveToNext();
        }
        if (c != null) {
            c.close();
        }
        return returnVal;
    }

    public void clearImpressionData(String batchId) {
        this.database.delete(impressionReporting.tableName, "batchID = '" + batchId + "'", null);
    }

    public void clearVisualImpressionData(String batchId) {
        this.database.delete(impressionVisualReporting.tableName, "batchID = '" + batchId + "'", null);
    }

    public void clearClosedStationReporting(String batchId) {
        this.database.delete(stationReporting.tableName, "isClosed = 1 AND batchID = '" + batchId + "'", null);
    }

    public void clearLocationData(String batchId) {
        this.database.delete(locationTracking.tableName, "batchID = '" + batchId + "'", null);
    }

    public ArrayList<String[]> getImpressionData(String batchId) {
        ArrayList<String[]> returnVal = new ArrayList();
        Cursor c = this.database.rawQuery("select trackingID, action, source, createTime, stationID, teID, cardTrackingID,latitude,longitude from impressionReporting WHERE batchID = '" + batchId + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            try {
                String trackingID = checkNull(c.getString(0));
                int action = c.getInt(1);
                int source = c.getInt(2);
                String createTime = checkNull(c.getString(3));
                String stationID = checkNull(c.getString(4));
                String teID = checkNull(c.getString(5));
                String cardTrackingID = checkNull(c.getString(6));
                createTime = DateTransform.msSqlDateFormat(DateTransform.iso8601Parse(createTime)) + Stomp.EMPTY;
                if (cardTrackingID.length() > 0) {
                    returnVal.add(new String[]{cardTrackingID, action + Stomp.EMPTY, source + Stomp.EMPTY, createTime, stationID, teID, trackingID});
                } else {
                    returnVal.add(new String[]{trackingID, action + Stomp.EMPTY, source + Stomp.EMPTY, createTime, stationID, teID, cardTrackingID});
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                c.moveToNext();
            } catch (Exception e2) {
                e2.printStackTrace();
                return returnVal;
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (c != null) {
            c.close();
        }
        return returnVal;
    }

    public void updateBatchId(String tableName) {
        String batchId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(impressionVisualReporting.batchID, batchId);
        if (tableName.equals(stationReporting.tableName)) {
            this.database.update(tableName, cv, "isClosed == 1 AND batchID IS NULL", null);
        } else {
            this.database.update(tableName, cv, "batchID IS NULL", null);
        }
    }

    public String getBatchId(String tableName) {
        updateBatchId(tableName);
        String batchId = null;
        Cursor cursor = null;
        try {
            cursor = this.database.rawQuery("SELECT batchID FROM " + tableName + " GROUP BY batchID", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                batchId = cursor.getString(0);
                if (cursor != null) {
                    cursor.close();
                }
                return batchId;
            }
            if (cursor != null) {
                cursor.close();
            }
            if (null == null) {
                batchId = "0";
            }
            return batchId;
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<String[]> getVisualImpressionData(String batchID) {
        ArrayList<String[]> returnVal = new ArrayList();
        Cursor c = this.database.rawQuery("select trackingID, source, createTime, stationID, teID, cardTrackingID from impressionVisualReporting WHERE batchID = '" + batchID + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            try {
                String trackingID = checkNull(c.getString(0));
                int source = c.getInt(1);
                String createTime = DateTransform.msSqlDateFormat(DateTransform.iso8601Parse(checkNull(c.getString(2)))) + Stomp.EMPTY;
                int stationID = c.getInt(3);
                String teID = checkNull(c.getString(4));
                if (checkNull(c.getString(5)).length() > 0) {
                    returnVal.add(new String[]{checkNull(c.getString(5)), source + Stomp.EMPTY, createTime, stationID + Stomp.EMPTY, teID, trackingID});
                } else {
                    returnVal.add(new String[]{trackingID, source + Stomp.EMPTY, createTime, stationID + Stomp.EMPTY, teID, checkNull(c.getString(5))});
                }
            } catch (ParseException e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return returnVal;
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            c.moveToNext();
        }
        if (c != null) {
            c.close();
        }
        return returnVal;
    }

    private String getStationTrackID(String teID) {
        Cursor cursor = null;
        try {
            cursor = this.database.rawQuery("SELECT trackingID FROM activityEvents WHERE teID = '" + teID + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                String trackingID = cursor.getString(0);
                if (cursor != null) {
                    cursor.close();
                }
                return trackingID;
            }
            if (cursor != null) {
                cursor.close();
            }
            return checkNull(null);
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String checkNull(String value) {
        if (value == null) {
            return Stomp.EMPTY;
        }
        return value;
    }

    public ArrayList<String[]> getLocationTrackData(String batchId) {
        ArrayList<String[]> returnVal = new ArrayList();
        Cursor c = this.database.rawQuery("select createTime, latitude, longitude from LocationTracking WHERE batchID = '" + batchId + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            try {
                String createTime = DateTransform.msSqlDateFormat(DateTransform.iso8601Parse(c.getString(0))) + Stomp.EMPTY;
                String latitude = c.getString(1);
                String longitude = c.getString(2);
                returnVal.add(new String[]{createTime, latitude, longitude});
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return returnVal;
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            c.moveToNext();
        }
        if (c != null) {
            c.close();
        }
        return returnVal;
    }

    public StationInfo fetchStation(int freqHz, int subChannel) {
        StationInfo returnVal = null;
        Cursor c = this.database.rawQuery("SELECT * FROM stations WHERE frequency=" + freqHz + " AND" + "((0 = " + subChannel + " and " + stations.frequencySubChannel + " = -1) or " + stations.frequencySubChannel + "=" + subChannel + ")", null);
        try {
            c.moveToFirst();
            CursorHelper ch = new CursorHelper(c);
            if (!c.isAfterLast()) {
                returnVal = new C11421(this, ch);
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        return returnVal;
    }

    public ArrayList<StationInfo> fetchStations(boolean includeOnlyFavorites, String sortBy, boolean isNoDataMode) {
        String filterString = Stomp.EMPTY;
        ArrayList<StationInfo> data = new ArrayList();
        if (includeOnlyFavorites) {
            filterString = "IsFavorite = 1 and ";
        }
        Cursor stationCursor = this.database.query(stations.tableName, null, "fromNoData = " + (isNoDataMode ? 1 : 0) + " and " + filterString + "isValid = 1", null, null, null, sortBy + ", frequency, frequencySubChannel");
        if (stationCursor != null) {
            try {
                CursorHelper ch = new CursorHelper(stationCursor);
                stationCursor.moveToFirst();
                while (!stationCursor.isAfterLast()) {
                    data.add(new C11432(this, ch));
                    stationCursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return data;
            } finally {
                if (stationCursor != null) {
                    stationCursor.close();
                }
            }
        }
        if (stationCursor != null) {
            stationCursor.close();
        }
        return data;
    }

    public long putStationInfo(StationInfo info, int favoriteStatus, boolean isNoDataClause) {
        info.callLetters = info.callLetters.replace("-FM", Stomp.EMPTY);
        String Query = "select _id from stations where ";
        String whereClause = "callLetters = '" + info.callLetters + "' and " + stations.frequency + " = '" + info.frequencyHz + "' and " + stations.frequencySubChannel + "= '" + info.frequencySubChannel + "' and " + "fromNoData = " + (isNoDataClause ? 1 : 0) + Stomp.EMPTY;
        Cursor c = this.database.rawQuery(Query + whereClause, null);
        boolean stationExists = false;
        int stationID = 0;
        try {
            c.moveToFirst();
            stationExists = !c.isAfterLast() && c.getInt(0) > 0;
            if (stationExists) {
                stationID = c.getInt(0);
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        ContentValues values;
        if (info.publicStationID > 0 && stationExists) {
            values = new ContentValues();
            values.put(stations.artistList, info.description);
            values.put(stations.callLetters, info.callLetters);
            values.put(stations.frequency, Integer.valueOf(info.frequencyHz));
            values.put(stations.frequencySubChannel, Integer.valueOf(info.frequencySubChannel));
            values.put(stations.genre, info.genre);
            values.put(stations.imageURL, info.imageURL);
            values.put(stations.imageURLHiRes, info.imageURLHiRes);
            values.put(stations.headline, info.headline);
            values.put(stations.headlineText, info.headlineText);
            values.put(stations.piCode, info.piCode);
            values.put(stations.countryCode, info.country);
            values.put(stations.endpoint, info.endpoint);
            values.put(stations.market, info.market);
            values.put(stations.slogan, info.slogan);
            values.put("isValid", Boolean.valueOf(true));
            values.put(stations.fromNoData, Boolean.valueOf(info.fromNoData));
            values.put(stations.trackingID, info.trackingID);
            if (favoriteStatus == 0) {
                values.put(stations.IsFavorite, Boolean.valueOf(false));
            } else if (favoriteStatus == 1) {
                values.put(stations.IsFavorite, Boolean.valueOf(true));
            }
            this.database.update(stations.tableName, values, whereClause, null);
            c = this.database.rawQuery(String.format("select _id from stations where publicStationID = %0$s", new Object[]{Integer.valueOf(info.publicStationID)}), null);
            try {
                c.moveToFirst();
                Log.d("HD_RADIO_DB", ">>>> GOT STATION ID:" + c.getInt(0));
                int result = c.getInt(0);
                return (long) result;
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        } else if (stationExists) {
            return (long) stationID;
        } else {
            values = new ContentValues();
            values.put(stations.artistList, info.description);
            values.put(stations.callLetters, info.callLetters);
            values.put(stations.frequency, Integer.valueOf(info.frequencyHz));
            values.put(stations.frequencySubChannel, Integer.valueOf(info.frequencySubChannel));
            values.put(stations.genre, info.genre);
            values.put(stations.imageURL, info.imageURL);
            values.put(stations.imageURLHiRes, info.imageURLHiRes);
            values.put(stations.headline, info.headline);
            values.put(stations.headlineText, info.headlineText);
            values.put(stations.piCode, info.piCode);
            values.put(stations.countryCode, info.country);
            values.put(stations.endpoint, info.endpoint);
            values.put(stations.market, info.market);
            values.put(stations.publicStationID, Integer.valueOf(info.publicStationID));
            values.put("isValid", Boolean.valueOf(true));
            values.put(stations.trackingID, info.trackingID);
            if (favoriteStatus == 0) {
                values.put(stations.IsFavorite, Boolean.valueOf(false));
            } else if (favoriteStatus == 1) {
                values.put(stations.IsFavorite, Boolean.valueOf(true));
            }
            values.put(stations.fromNoData, Boolean.valueOf(info.fromNoData));
            values.put(stations.slogan, info.slogan);
            try {
                return this.database.insert(stations.tableName, null, values);
            } catch (Exception e2) {
                e2.printStackTrace();
                return -1;
            }
        }
    }

    public long putListeningActivityEventRecord(NextRadioEventInfo radioEventInfo) throws Exception {
        ContentValues initialValues = createContentValues(radioEventInfo);
        Cursor c = this.database.query(activityEvents.tableName, new String[]{stations._id}, "UFIDIdentifier = '" + radioEventInfo.UFIDIdentifier + "'", null, null, null, null);
        long eventID = 0;
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                eventID = c.getLong(c.getColumnIndex(stations._id));
            }
            if (c != null) {
                c.close();
            }
            if (eventID > 0) {
                return eventID;
            }
            eventID = this.database.insert(activityEvents.tableName, null, initialValues);
            if (radioEventInfo.getEnhancements() == null) {
                return eventID;
            }
            Iterator i$ = radioEventInfo.getEnhancements().iterator();
            while (i$.hasNext()) {
                this.database.insert(listeningActivityAds.tableName, null, createAdValues(eventID, (Enhancement) i$.next()));
            }
            return eventID;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private ContentValues createContentValues(NextRadioEventInfo radioEventInfo) throws Exception {
        ContentValues values = new ContentValues();
        long stationID = getStationID(radioEventInfo.stationInfo);
        if (radioEventInfo == null || radioEventInfo.stationInfo == null || stationID <= 0) {
            throw new Exception("Incomplete ContentValues for RadioEvent");
        }
        try {
            values.put(Message.TIMESTAMP, DateFormats.iso8601Format(radioEventInfo.timePlayed));
        } catch (Exception e) {
            values.put(Message.TIMESTAMP, DateFormats.iso8601Format(new Date()));
        }
        values.put(activityEvents.artist, radioEventInfo.artist);
        values.put(activityEvents.album, radioEventInfo.album);
        values.put(SettingsJsonConstants.PROMPT_TITLE_KEY, radioEventInfo.title);
        values.put(activityEvents.itemType, Integer.valueOf(radioEventInfo.itemType));
        values.put(impressionVisualReporting.stationID, Long.valueOf(stationID));
        values.put(impressionVisualReporting.teID, radioEventInfo.teID);
        values.put(stations.imageURL, radioEventInfo.imageURL);
        values.put(stations.imageURLHiRes, radioEventInfo.imageURLHiRes);
        values.put(activityEvents.UFIDIdentifier, radioEventInfo.UFIDIdentifier);
        values.put(activityEvents.primaryAction, radioEventInfo.primaryAction);
        values.put(stations.trackingID, radioEventInfo.trackingID);
        return values;
    }

    private long getStationID(StationInfo info) {
        Cursor c = this.database.rawQuery("select _id from stations where " + ("publicStationID  = " + info.publicStationID), null);
        int stationID = 0;
        try {
            c.moveToFirst();
            boolean stationExists = !c.isAfterLast() && c.getInt(0) > 0;
            if (stationExists) {
                stationID = c.getInt(0);
            }
            if (c != null) {
                c.close();
            }
            return (long) stationID;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private ContentValues createAdValues(long eventID, Enhancement ad) {
        ContentValues values = new ContentValues();
        values.put(listeningActivityAds.adType, ad.type);
        values.put(listeningHistory.eventID, Long.valueOf(eventID));
        if (ad.datafields.length > 0) {
            values.put(listeningActivityAds.field0, ad.datafields[0]);
        }
        if (ad.datafields.length > 1) {
            values.put(listeningActivityAds.field1, ad.datafields[1]);
        }
        if (ad.datafields.length > 2) {
            values.put(listeningActivityAds.field2, ad.datafields[2]);
        }
        if (ad.datafields.length > 3) {
            values.put(listeningActivityAds.field3, ad.datafields[3]);
        }
        if (ad.datafields.length > 4) {
            values.put(listeningActivityAds.field4, ad.datafields[4]);
        }
        if (ad.datafields.length > 5) {
            values.put(listeningActivityAds.field5, ad.datafields[5]);
        }
        return values;
    }

    public int updateListeningHistoryAsFavorite(long historyID, boolean isFavorite) throws Exception {
        ContentValues values = new ContentValues();
        values.put(listeningHistory.isFavorite, isFavorite ? "1" : "0");
        values.put(listeningHistory.savedDate, DateFormats.iso8601Format(new Date()));
        int rowsAffected = this.database.update(listeningHistory.tableName, values, "_id = " + historyID, null);
        if (rowsAffected != 0) {
            return rowsAffected;
        }
        throw new Exception("Row not found in listeningHistory for _id " + historyID);
    }

    public StationInfo fetchStationByID(int publicStationID) {
        StationInfo returnVal = null;
        Cursor c = this.database.rawQuery("SELECT * FROM stations WHERE publicStationID=" + publicStationID, null);
        try {
            c.moveToFirst();
            if (!c.isAfterLast()) {
                returnVal = new C11443(this, c);
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        return returnVal;
    }

    public int clearAllNonFavoritedStations() {
        ContentValues values = new ContentValues();
        values.put(stations.IsValid, Boolean.valueOf(false));
        return this.database.update(stations.tableName, values, "IsFavorite = 0", null);
    }

    public ArrayList<NextRadioEventInfo> getListeningHistory(Integer[] filters, boolean includeOnlyFavorites, boolean includeFromNowPlaying, String searchterm, int limit) {
        String filterString = Stomp.EMPTY;
        ArrayList<NextRadioEventInfo> data = new ArrayList();
        if (filters == null || filters.length == 0) {
            filterString = " WHERE 1 = 1";
        } else {
            boolean first = true;
            for (Integer intValue : filters) {
                int itemType = intValue.intValue();
                if (first) {
                    filterString = " WHERE (itemType = " + itemType;
                    first = false;
                } else {
                    filterString = filterString + " OR itemType = " + itemType;
                }
            }
            filterString = filterString + ")";
        }
        if (includeFromNowPlaying) {
            filterString = filterString + " AND listeningHistory.isFromNowPlaying = 1 ";
        }
        if (includeOnlyFavorites) {
            filterString = filterString + " AND listeningHistory.isFavorite = 1 ";
        }
        if (searchterm != null && searchterm.length() > 0) {
            searchterm = DatabaseUtils.sqlEscapeString("%" + searchterm + "%");
            filterString = filterString + " AND  (activityEvents.artist like " + searchterm + " or" + " activityEvents.album like " + searchterm + " or" + " activityEvents.title like " + searchterm + " or" + " activityEvents.description like " + searchterm + ")";
        }
        if (includeOnlyFavorites) {
            filterString = filterString + " ORDER BY listeningHistory.savedDate DESC LIMIT " + limit;
        } else {
            filterString = filterString + " ORDER BY listeningHistory._id DESC LIMIT " + limit;
        }
        Cursor eventCursor = this.database.rawQuery(Queries.LISTENINGHISTORY_STATIONS + filterString, null);
        if (eventCursor != null) {
            CursorHelper ch = new CursorHelper(eventCursor);
            eventCursor.moveToFirst();
            while (!eventCursor.isAfterLast()) {
                NextRadioEventInfo returnVal = new NextRadioEventInfo();
                returnVal.title = ch.getString(SettingsJsonConstants.PROMPT_TITLE_KEY);
                returnVal.artist = ch.getString(activityEvents.artist);
                returnVal.album = ch.getString(activityEvents.album);
                returnVal.itemType = ch.getInt(activityEvents.itemType);
                returnVal.trackingID = ch.getString(stations.trackingID);
                returnVal.UFIDIdentifier = ch.getString(activityEvents.UFIDIdentifier);
                returnVal.eventID = (long) ch.getInt("activityID");
                returnVal.historyID = (long) ch.getInt("listeningHistoryID");
                returnVal.imageURL = ch.getString(stations.imageURL);
                returnVal.imageURLHiRes = ch.getString(stations.imageURLHiRes);
                if (ch.getInt(listeningHistory.isFavorite) == 1) {
                    returnVal.isFavorite = true;
                } else {
                    try {
                        returnVal.isFavorite = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return data;
                    } finally {
                        if (eventCursor != null) {
                            eventCursor.close();
                        }
                    }
                }
                String datestring = ch.getString(listeningHistory.lastheard);
                returnVal.stationInfo = new StationInfo();
                returnVal.stationInfo.frequencyHz = ch.getInt(stations.frequency);
                returnVal.stationInfo.callLetters = ch.getString(stations.callLetters);
                returnVal.stationInfo.publicStationID = ch.getInt(stations.publicStationID);
                returnVal.stationInfo.slogan = ch.getString(stations.slogan);
                returnVal.stationInfo.frequencySubChannel = ch.getInt(stations.frequencySubChannel);
                returnVal.stationInfo.headline = ch.getString(stations.headline);
                returnVal.stationInfo.endpoint = ch.getString(stations.endpoint);
                returnVal.stationInfo.lastListened = ch.getLong(stations.lastListened);
                returnVal.stationInfo.imageURL = ch.getString(stations.imageURL);
                returnVal.stationInfo.imageURLHiRes = ch.getString(stations.imageURLHiRes);
                try {
                    returnVal.timePlayed = DateFormats.iso8601Parse(datestring);
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
                data.add(returnVal);
                eventCursor.moveToNext();
            }
        }
        if (eventCursor != null) {
            eventCursor.close();
        }
        return data;
    }

    public long putListeningHistoryRecord(long eventID, boolean isFromNowPlaying) {
        Cursor c = this.database.rawQuery("select eventID, _id from listeningHistory order by _id desc limit 1", null);
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                if (c.getLong(c.getColumnIndex(listeningHistory.eventID)) == eventID) {
                    long historyID = c.getLong(c.getColumnIndex(stations._id));
                    c.close();
                    if (c == null) {
                        return historyID;
                    }
                    c.close();
                    return historyID;
                }
            }
            if (c != null) {
                c.close();
            }
            ContentValues values = new ContentValues();
            values.put(listeningHistory.eventID, Long.valueOf(eventID));
            values.put(listeningHistory.isFromNowPlaying, Boolean.valueOf(isFromNowPlaying));
            try {
                values.put(listeningHistory.lastheard, DateFormats.iso8601Format(new Date()));
            } catch (Exception e) {
                values.put(listeningHistory.lastheard, DateFormats.iso8601Format(new Date()));
            }
            return this.database.insert(listeningHistory.tableName, null, values);
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    public NextRadioEventInfo fetchEvent(long historyID) {
        Throwable th;
        boolean z = true;
        Cursor cursor = this.database.rawQuery("SELECT activityEvents.*, stations.trackingID as stationTrackingID, stations.isValid, stations.imageURL as stationImageURL, stations.IsFavorite as stationIsFavorite, stations.imageURLHiRes as stationImageURLHiRes, stations.frequency, stations.publicStationID, stations.slogan, stations.callLetters, stations.lastListened, listeningHistory.isFromNowPlaying, listeningHistory.isFavorite, listeningHistory._id as historyID FROM activityEvents LEFT OUTER JOIN stations ON stations._id = activityEvents.stationID LEFT OUTER JOIN listeningActivityAds ON listeningActivityAds.eventID = activityEvents._id LEFT OUTER JOIN listeningHistory ON activityEvents._id = listeningHistory.eventID WHERE listeningHistory._id = " + historyID + " " + "ORDER BY [timestamp] DESC", null);
        CursorHelper ch = new CursorHelper(cursor);
        NextRadioEventInfo displayInfo = new NextRadioEventInfo();
        try {
            cursor.moveToFirst();
            try {
                displayInfo.timePlayed = DateFormats.iso8601Parse(ch.getString(Message.TIMESTAMP));
            } catch (ParseException e) {
            }
            displayInfo.eventID = ch.getLong(stations._id);
            displayInfo.UFIDIdentifier = ch.getString(activityEvents.UFIDIdentifier);
            displayInfo.historyID = ch.getLong("historyID");
            displayInfo.artist = ch.getString(activityEvents.artist);
            displayInfo.trackingID = ch.getString(stations.trackingID);
            displayInfo.teID = ch.getString(impressionVisualReporting.teID);
            displayInfo.stationInfo.callLetters = ch.getString(stations.callLetters);
            displayInfo.stationInfo.frequencyHz = ch.getInt(stations.frequency);
            displayInfo.stationInfo.publicStationID = ch.getInt(stations.publicStationID);
            displayInfo.stationInfo.slogan = ch.getString(stations.slogan);
            StationInfo stationInfo = displayInfo.stationInfo;
            if (ch.getInt(stations.IsValid) != 1) {
                z = false;
            }
            stationInfo.isValid = z;
            displayInfo.stationInfo.imageURLHiRes = ch.getString("stationImageURL");
            displayInfo.stationInfo.imageURL = ch.getString(stations.imageURLHiRes);
            displayInfo.stationInfo.lastListened = ch.getLong(stations.lastListened);
            displayInfo.stationInfo.isFavorited = ch.getBoolean("stationIsFavorite");
            displayInfo.stationInfo.trackingID = ch.getString("stationTrackingID");
            displayInfo.title = ch.getString(SettingsJsonConstants.PROMPT_TITLE_KEY);
            displayInfo.album = ch.getString(activityEvents.album);
            displayInfo.imageURL = ch.getString(stations.imageURL);
            displayInfo.imageURLHiRes = ch.getString(stations.imageURLHiRes);
            displayInfo.itemType = ch.getInt(activityEvents.itemType);
            displayInfo.isFavorite = ch.getBoolean(listeningHistory.isFavorite);
            displayInfo.isFromRecentlyPlayed = ch.getBoolean(listeningHistory.isFromNowPlaying);
            displayInfo.primaryAction = ch.getString(activityEvents.primaryAction);
            cursor = this.database.query(listeningActivityAds.tableName, null, "eventID = " + displayInfo.eventID, null, null, null, stations._id);
            try {
                cursor.moveToFirst();
                CursorHelper ch2 = new CursorHelper(cursor);
                try {
                    displayInfo.setEnhancements(new ArrayList());
                    while (!cursor.isAfterLast()) {
                        displayInfo.putEnhancement(ch2.getString(listeningActivityAds.adType), ch2.getString(listeningActivityAds.field0), ch2.getString(listeningActivityAds.field1), ch2.getString(listeningActivityAds.field2), ch2.getString(listeningActivityAds.field3), ch2.getString(listeningActivityAds.field4), ch2.getString(listeningActivityAds.field5));
                        cursor.moveToNext();
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    return displayInfo;
                } catch (Throwable th2) {
                    th = th2;
                    ch = ch2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void clearAllStations() {
        try {
            this.database.delete(stations.tableName, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int invalidateAllStations() {
        ContentValues values = new ContentValues();
        values.put(stations.IsValid, Integer.valueOf(0));
        return this.database.update(stations.tableName, values, "publicStationID > 0", null);
    }

    public int enableNoDataModeStations() {
        ContentValues values = new ContentValues();
        values.put(stations.IsValid, Boolean.valueOf(true));
        return this.database.update(stations.tableName, values, "fromNoData = 1", null);
    }

    public void putLocationData(int source, int action, String latitude, String longitude) {
        ContentValues cv = new ContentValues();
        cv.put(locationTracking.source, Integer.valueOf(source));
        cv.put(locationTracking.action, Integer.valueOf(action));
        String createTime = DateFormats.iso8601FormatUTC(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
        cv.put(locationTracking.createTime, createTime);
        cv.put(locationTracking.latitude, latitude);
        cv.put(locationTracking.longitude, longitude);
        if (isUniqueLocationData(createTime)) {
            this.database.insert(locationTracking.tableName, null, cv);
        }
        this.database.execSQL("DELETE FROM LocationTracking WHERE _id NOT IN (SELECT _id from LocationTracking ORDER BY createTime DESC LIMIT 900)");
    }

    private boolean isUniqueLocationData(String createTime) {
        boolean z = false;
        Cursor cursor = this.database.rawQuery("SELECT createTime FROM LocationTracking WHERE createTime = '" + createTime + "'", null);
        try {
            if (cursor.getCount() == 0) {
                z = true;
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return z;
    }

    public void clearAllEvents() {
        try {
            this.database.delete(activityEvents.tableName, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearListeningHistory() {
        try {
            this.database.delete(listeningHistory.tableName, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearStationReporting() {
        try {
            this.database.delete(stationReporting.tableName, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<StationInfo> fetchStationsFavoritedAsList() {
        ArrayList<StationInfo> presetList = new ArrayList();
        Cursor c = this.database.query(stations.tableName, null, "IsFavorite = 1 AND publicStationID > 0 AND IsValid= 1", null, null, null, "frequency, frequencySubChannel");
        if (c.getCount() > 0) {
            try {
                if (getLastFavouriteSentDate()) {
                    c.moveToFirst();
                    while (!c.isAfterLast()) {
                        presetList.add(new C11454(this, c));
                        c.moveToNext();
                    }
                }
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        } else if (c != null) {
            c.close();
        }
        return presetList;
    }

    public boolean getLastFavouriteSentDate() {
        String lastFavouriteSentDate = PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("stations_favourite_date", Stomp.EMPTY);
        String currentDateAndTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (currentDateAndTime.equals(lastFavouriteSentDate)) {
            return false;
        }
        PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putString("stations_favourite_date", currentDateAndTime).apply();
        return true;
    }

    public void updateStationLastListened(int publicID, long lastListenTime) {
        ContentValues values = new ContentValues();
        values.put(stations.lastListened, Long.valueOf(lastListenTime));
        this.database.update(stations.tableName, values, "publicStationID = '" + publicID + "'", null);
    }

    public int updateStationFavoriteStatus(int freqHz, int subChannel, int stationType, boolean isFavorite, boolean isNoDataMode) {
        int i = 1;
        ContentValues values = new ContentValues();
        values.put(stations.IsFavorite, Boolean.valueOf(isFavorite));
        if (stationType == 0) {
            int i2;
            SQLiteDatabase sQLiteDatabase = this.database;
            String str = stations.tableName;
            StringBuilder append = new StringBuilder().append("IsFavorite <> ");
            if (isFavorite) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            StringBuilder append2 = append.append(i2).append(" and ").append("fromNoData = ");
            if (!isNoDataMode) {
                i = 0;
            }
            return sQLiteDatabase.update(str, values, append2.append(i).append(" and ").append("publicStationID = 0 and ").append("isValid = 1 and frequency = ").append(freqHz).append(" and ").append("frequencySubChannel = ").append(subChannel).toString(), null);
        }
        sQLiteDatabase = this.database;
        str = stations.tableName;
        append2 = new StringBuilder().append("IsFavorite <> ").append(isFavorite ? 1 : 0).append(" and ").append("publicStationID > 0 and ").append("fromNoData = ");
        if (!isNoDataMode) {
            i = 0;
        }
        return sQLiteDatabase.update(str, values, append2.append(i).append(" and ").append("isValid = 1 and frequency = ").append(freqHz).append(" and ").append("((0 = ").append(subChannel).append(" and frequencySubChannel = -1) or ").append("frequencySubChannel = ").append(subChannel).append(")").toString(), null);
    }

    public void recordImpression(String trackingID, int action, int source, int publicStationID) {
        String[] trackAndTeId = getCardAndTeId(trackingID);
        if (action == 19) {
            Cursor c = this.database.rawQuery("select trackingID, action, source, createTime, stationID from impressionReporting where action=19 order by _id desc limit 1", null);
            try {
                c.moveToFirst();
                if (c.isAfterLast() || !c.getString(0).equals(trackingID)) {
                    c.close();
                } else {
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                c.close();
            }
        }
        ContentValues cv = new ContentValues();
        cv.put(locationTracking.action, Integer.valueOf(action));
        cv.put(locationTracking.createTime, DateFormats.iso8601FormatUTC(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
        cv.put(locationTracking.source, Integer.valueOf(source));
        cv.put(stations.trackingID, trackingID);
        cv.put(impressionVisualReporting.stationID, Integer.valueOf(publicStationID));
        cv.put(impressionVisualReporting.cardTrackingID, trackAndTeId[0]);
        cv.put(impressionVisualReporting.teID, trackAndTeId[1]);
        this.database.insert(impressionReporting.tableName, null, cv);
        this.database.execSQL("DELETE FROM impressionReporting WHERE _id NOT IN (SELECT _id from impressionReporting ORDER BY createTime DESC LIMIT 900)");
    }

    private String[] getCardAndTeId(String trackId) {
        String[] data = new String[2];
        Cursor cursor = null;
        try {
            cursor = this.database.rawQuery("SELECT cardTrackingID, teID FROM impressionVisualReporting WHERE trackingID = '" + trackId + "'", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                data[0] = cursor.getString(0);
                data[1] = cursor.getString(1);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public void recordActionImpression(ActionPayload payload, int action, int source) {
        ContentValues cv = new ContentValues();
        cv.put(locationTracking.action, Integer.valueOf(action));
        cv.put(locationTracking.createTime, DateFormats.iso8601FormatUTC(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()));
        cv.put(locationTracking.source, Integer.valueOf(source));
        cv.put(stations.trackingID, payload.mTrackingID);
        cv.put(impressionVisualReporting.stationID, Integer.valueOf(payload.mStationID));
        cv.put(impressionVisualReporting.cardTrackingID, payload.mCardTrackingID);
        cv.put(impressionVisualReporting.teID, payload.mTEID);
        this.database.insert(impressionReporting.tableName, null, cv);
        this.database.execSQL("DELETE FROM impressionReporting WHERE _id NOT IN (SELECT _id from impressionReporting ORDER BY createTime DESC LIMIT 900)");
    }

    public void recordVisualImpression(String trackingID, int source, int stationID, String cardTrackID, String teID) {
        ContentValues cv = new ContentValues();
        String createTime = DateFormats.iso8601FormatUTC(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
        cv.put(locationTracking.createTime, createTime);
        cv.put(locationTracking.source, Integer.valueOf(source));
        cv.put(stations.trackingID, trackingID);
        cv.put(impressionVisualReporting.stationID, Integer.valueOf(stationID));
        cv.put(impressionVisualReporting.cardTrackingID, cardTrackID);
        cv.put(impressionVisualReporting.teID, teID);
        if (isUniqueVisualImpression(source, trackingID, teID, cardTrackID, createTime)) {
            this.database.insert(impressionVisualReporting.tableName, null, cv);
        }
        this.database.execSQL("DELETE FROM impressionVisualReporting WHERE _id NOT IN (SELECT _id from impressionVisualReporting ORDER BY createTime DESC LIMIT 900)");
    }

    private boolean isUniqueVisualImpression(int source, String trackingId, String teID, String cardTrackID, String currentTime) {
        boolean z;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Cursor cursor = this.database.rawQuery("SELECT trackingID, createTime FROM impressionVisualReporting WHERE teID = '" + teID + "' AND trackingID = '" + trackingId + "' AND source = '" + source + "' AND cardTrackingID = '" + cardTrackID + "'", null);
        String createdTime = Stomp.EMPTY;
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                createdTime = cursor.getString(1);
                long differenceDates = Math.abs(sdf.parse(currentTime).getTime() - sdf.parse(createdTime).getTime()) / 1000;
                Log.d(TAG, "visual differenceDates:" + differenceDates);
                if (differenceDates > 59) {
                    z = true;
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    z = false;
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else {
                z = true;
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            z = false;
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return z;
    }

    public void deleteSavedEvent(String UFID) {
        try {
            this.database.execSQL("UPDATE listeningHistory SET isFavorite = 0 WHERE eventID IN (SELECT _id from activityEvents WHERE UFIDIdentifier = '" + UFID + "')");
        } catch (Exception e) {
        }
    }

    public long getMostRecentEventFromHistory(String UFID) {
        long j = -1;
        Cursor cursor = this.database.rawQuery("SELECT listeningHistory._id FROM listeningHistory LEFT OUTER JOIN activityEvents ON listeningHistory.eventID = activityEvents._id WHERE activityEvents.UFIDIdentifier = '" + UFID + "' ORDER BY [lastHeard] DESC LIMIT 1", null);
        try {
            if (cursor.moveToFirst()) {
                j = new CursorHelper(cursor).getLong(stations._id);
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return j;
    }
}
