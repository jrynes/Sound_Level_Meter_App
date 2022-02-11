package com.nextradioapp.androidSDK.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.media.TransportMediator;
import android.util.Log;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Service;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "applicationdata";
    private static final int DATABASE_VERSION = 133;
    private static String[] PatchesFrom119;
    private static String[] PatchesFrom122;
    private static String[] PatchesFrom123;
    private static String[] PatchesFrom124;
    private static String[] PatchesFrom125;
    private static String[] PatchesFrom126;
    private static String[] PatchesFrom127;
    private static String[] PatchesFrom128;
    private static String[] PatchesFrom129;
    private static String[] PatchesFrom130;
    private static String[] PatchesFrom131;
    private static String[] PatchesFrom132;
    private static String[] TableCreates;
    private static CustomSQLiteOpenHelper mInstance;

    public static CustomSQLiteOpenHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new CustomSQLiteOpenHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    static {
        PatchesFrom119 = new String[]{"create table stationReporting (_id integer primary key autoincrement, endTime datetime not null, isClosed int not null default 0, lastUFID text, publicStationID int not null, startTime datetime not null)"};
        PatchesFrom122 = new String[]{"create table impressionReporting (_id integer primary key autoincrement, source integer not null, ufid text, action integer not null, createTime datetime not null)"};
        PatchesFrom123 = new String[]{"ALTER TABLE activityEvents ADD COLUMN primaryAction text DEFAULT \"\";"};
        PatchesFrom124 = new String[]{"create table impressionVisualReporting (_id integer primary key autoincrement, source integer not null, trackingID text, createTime datetime not null)"};
        PatchesFrom125 = new String[]{"alter table activityEvents ADD COLUMN 'trackingID'"};
        PatchesFrom126 = new String[]{"alter table stations ADD COLUMN 'fromNoData' int default 0", "DROP INDEX IF EXISTS iStationLookup", "DROP INDEX IF EXISTS iPublicStationID"};
        PatchesFrom127 = new String[]{"CREATE UNIQUE INDEX iStationLookup ON stations(frequency, frequencySubChannel, callLetters, publicStationID, fromNoData);"};
        PatchesFrom128 = new String[]{"update listeningHistory set isFromNowPlaying = not isFromNowPlaying"};
        PatchesFrom129 = new String[]{"alter table impressionVisualReporting ADD COLUMN 'stationID'"};
        PatchesFrom130 = new String[]{"delete from impressionReporting", "alter table impressionReporting ADD COLUMN 'trackingID'", "alter table impressionReporting ADD COLUMN 'stationID'", "alter table stations ADD COLUMN 'trackingID'"};
        PatchesFrom131 = new String[]{"alter table activityEvents ADD COLUMN 'teID'", "ALTER TABLE impressionVisualReporting ADD COLUMN 'teID'", "ALTER TABLE impressionVisualReporting ADD COLUMN 'cardTrackingID'", "ALTER TABLE impressionReporting ADD COLUMN 'teID'", "ALTER TABLE impressionReporting ADD COLUMN 'cardTrackingID'", "ALTER TABLE impressionReporting ADD COLUMN 'latitude'", "ALTER TABLE impressionReporting ADD COLUMN 'longitude'", "create table LocationTracking (_id integer primary key autoincrement, source int not null default 0, action int not null default 0, longitude text, latitude text, createTime datetime not null)"};
        PatchesFrom132 = new String[]{"ALTER TABLE impressionVisualReporting ADD COLUMN 'batchID'", "ALTER TABLE stationReporting ADD COLUMN 'batchID'", "ALTER TABLE impressionReporting ADD COLUMN 'batchID'", "ALTER TABLE LocationTracking ADD COLUMN 'batchID'", "ALTER TABLE stations ADD COLUMN 'batchID'"};
        TableCreates = new String[]{"create table chipLog (_id integer primary key autoincrement, logEntry text, timestamp datetime not null);", "create table activityEvents (_id integer primary key autoincrement, UFIDIdentifier string, itemType integer, timestamp datetime not null, artist text, album  text, title text, description text, imageURL text, imageURLHiRes text, stationID integer not null); ", "create table listeningHistory (_id integer primary key autoincrement, lastheard datetime not null, savedDate datetime, eventID integer, isFromNowPlaying integer, isFavorite integer default 0);", "create table listeningActivityAds (_id integer primary key autoincrement, IsFavorite integer default 0, EventID integer not null, adType text not null, field0 text, field1  text, field2 text, field3  text, field4 text, field5 text);", "create table stations (_id integer primary key autoincrement, publicStationID int not null, frequency int not null, frequencySubChannel int, callLetters text not null, genre text, market text, endpoint text, lastListened bigint not null default 0, slogan text default '', imageURL text, imageURLHiRes text, headline text, headlineText text, piCode text, countryCode text, IsValid int not null default 1, IsFavorite int default 0, artistList text);", "CREATE UNIQUE INDEX iStationLookup ON stations(frequency, frequencySubChannel, callLetters);", "CREATE INDEX iPublicStationID ON stations(publicStationID);"};
    }

    public CustomSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("upgrade", "DATABASE_NAME:applicationdata");
        Log.e("upgrade", "DATABASE_VERSION:133");
    }

    public void onCreate(SQLiteDatabase database) {
        Log.e("upgrade", "onCreate:");
        try {
            for (String sql : TableCreates) {
                database.execSQL(sql);
            }
            for (String sql2 : PatchesFrom119) {
                database.execSQL(sql2);
            }
            for (String sql22 : PatchesFrom122) {
                database.execSQL(sql22);
            }
            for (String sql222 : PatchesFrom123) {
                database.execSQL(sql222);
            }
            for (String sql2222 : PatchesFrom124) {
                database.execSQL(sql2222);
            }
            for (String sql22222 : PatchesFrom125) {
                database.execSQL(sql22222);
            }
            for (String sql222222 : PatchesFrom126) {
                database.execSQL(sql222222);
            }
            for (String sql2222222 : PatchesFrom127) {
                database.execSQL(sql2222222);
            }
            for (String sql22222222 : PatchesFrom128) {
                database.execSQL(sql22222222);
            }
            for (String sql222222222 : PatchesFrom129) {
                database.execSQL(sql222222222);
            }
            for (String sql2222222222 : PatchesFrom130) {
                database.execSQL(sql2222222222);
            }
            for (String sql22222222222 : PatchesFrom131) {
                database.execSQL(sql22222222222);
            }
            for (String sql222222222222 : PatchesFrom132) {
                database.execSQL(sql222222222222);
            }
        } catch (Exception ex) {
            Log.e("HD_RADIO", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.e("upgrade", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        if (oldVersion < Service.NNTP) {
            database.execSQL("DROP TABLE IF EXISTS userSaved");
            database.execSQL("DROP TABLE IF EXISTS stations");
            database.execSQL("DROP TABLE IF EXISTS listeningActivityAds");
            database.execSQL("DROP TABLE IF EXISTS listeningHistory");
            database.execSQL("DROP TABLE IF EXISTS chipLog");
            database.execSQL("DROP TABLE IF EXISTS stations");
            database.execSQL("DROP TABLE IF EXISTS activityEvents");
            database.execSQL("DROP INDEX IF EXISTS iStationLookup");
            onCreate(database);
            return;
        }
        if (oldVersion <= Service.NNTP) {
            for (String sql : PatchesFrom119) {
                database.execSQL(sql);
            }
        }
        if (oldVersion <= 122) {
            for (String sql2 : PatchesFrom122) {
                database.execSQL(sql2);
            }
        }
        if (oldVersion <= Service.NTP) {
            for (String sql22 : PatchesFrom123) {
                database.execSQL(sql22);
            }
        }
        if (oldVersion <= 124) {
            for (String sql222 : PatchesFrom124) {
                database.execSQL(sql222);
            }
        }
        if (oldVersion <= Service.LOCUS_MAP) {
            for (String sql2222 : PatchesFrom125) {
                database.execSQL(sql2222);
            }
        }
        if (oldVersion <= TransportMediator.KEYCODE_MEDIA_PLAY) {
            for (String sql22222 : PatchesFrom126) {
                database.execSQL(sql22222);
            }
        }
        if (oldVersion <= Service.LOCUS_CON) {
            for (String sql222222 : PatchesFrom127) {
                database.execSQL(sql222222);
            }
        }
        if (oldVersion <= Flags.FLAG8) {
            for (String sql2222222 : PatchesFrom128) {
                database.execSQL(sql2222222);
            }
        }
        if (oldVersion <= Service.PWDGEN) {
            for (String sql22222222 : PatchesFrom129) {
                database.execSQL(sql22222222);
            }
        }
        if (oldVersion <= Service.CISCO_FNA) {
            for (String sql222222222 : PatchesFrom130) {
                database.execSQL(sql222222222);
            }
        }
        if (oldVersion <= Service.CISCO_TNA) {
            for (String sql2222222222 : PatchesFrom131) {
                database.execSQL(sql2222222222);
            }
        }
        if (oldVersion <= Service.CISCO_SYS) {
            for (String sql22222222222 : PatchesFrom132) {
                database.execSQL(sql22222222222);
            }
        }
    }

    public static void clearInstance() {
        mInstance = null;
    }
}
