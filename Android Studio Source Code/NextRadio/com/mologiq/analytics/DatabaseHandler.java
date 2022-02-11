package com.mologiq.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mologiq.analytics.MeanListData.MeanListData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connected;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.mologiq.analytics.g */
final class DatabaseHandler extends SQLiteOpenHelper {
    private static final int f2131a;
    private static DatabaseHandler f2132b;
    private static SQLiteDatabase f2133c;

    /* renamed from: com.mologiq.analytics.g.1 */
    class DatabaseHandler implements Runnable {
        final /* synthetic */ DatabaseHandler f2128a;
        private final /* synthetic */ Context f2129b;
        private final /* synthetic */ int f2130c;

        DatabaseHandler(DatabaseHandler databaseHandler, Context context, int i) {
            this.f2128a = databaseHandler;
            this.f2129b = context;
            this.f2130c = i;
        }

        public final void run() {
            try {
                DatabaseHandler.f2133c = DatabaseHandler.m1724a();
                List<ApplicationInfo> installedApplications = this.f2129b.getPackageManager().getInstalledApplications(Flags.FLAG8);
                DatabaseHandler.f2133c.execSQL("DELETE FROM installapplist");
                for (ApplicationInfo applicationInfo : installedApplications) {
                    if (DatabaseHandler.f2133c != null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("packagename", applicationInfo.packageName);
                        DatabaseHandler.f2133c.insert("installapplist", null, contentValues);
                    }
                }
            } catch (Throwable th) {
                Utils.m1924a("===== Background Thread " + Utils.m1922a(th));
            }
            try {
                this.f2128a.m1736a(this.f2130c);
            } catch (Throwable th2) {
                Utils.m1924a("===== Background Thread " + Utils.m1922a(th2));
            }
        }
    }

    static {
        f2131a = Integer.parseInt(Version.VERSION.replaceAll("\\.", Stomp.EMPTY));
        f2133c = null;
    }

    private DatabaseHandler(Context context) {
        super(context, "mologiq", null, f2131a);
    }

    static SQLiteDatabase m1724a() {
        return f2133c == null ? f2132b.getWritableDatabase() : f2133c;
    }

    static synchronized DatabaseHandler m1725a(Context context) {
        DatabaseHandler databaseHandler;
        synchronized (DatabaseHandler.class) {
            try {
                if (f2132b == null) {
                    f2132b = new DatabaseHandler(context);
                    if (f2133c == null) {
                        f2133c = DatabaseHandler.m1724a();
                    }
                }
            } catch (Throwable th) {
                Utils.m1924a(Utils.m1922a(th));
            }
            databaseHandler = f2132b;
        }
        return databaseHandler;
    }

    private synchronized void m1727a(String str, String str2, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(str, str2);
        SQLiteDatabase a = DatabaseHandler.m1724a();
        f2133c = a;
        a.update("appRequestResponse", contentValues, "id= ?", new String[]{String.valueOf(i)});
    }

    private synchronized int m1728b(AppInfo appInfo) {
        ContentValues contentValues;
        f2133c = DatabaseHandler.m1724a();
        contentValues = new ContentValues();
        contentValues.put(Name.MARK, Integer.valueOf(appInfo.m1723c()));
        contentValues.put("name", appInfo.m1721b());
        contentValues.put("classification_id", Integer.valueOf(appInfo.m1718a()));
        return f2133c.update("applist", contentValues, "id = ?", new String[]{String.valueOf(appInfo.m1723c())});
    }

    private synchronized int m1729b(String str, String str2) {
        ContentValues contentValues;
        SQLiteDatabase a;
        contentValues = new ContentValues();
        contentValues.put(str, str2);
        a = DatabaseHandler.m1724a();
        f2133c = a;
        return (int) a.insert("appRequestResponse", null, contentValues);
    }

    private synchronized AppInfo m1730b(int i) {
        AppInfo appInfo;
        Throwable th;
        Cursor cursor = null;
        synchronized (this) {
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                Cursor query = a.query("applist", new String[]{Name.MARK, "name", "classification_id"}, "id=?", new String[]{String.valueOf(i)}, null, null, null, null);
                if (query != null) {
                    try {
                        if (query.moveToFirst() && query.getCount() > 0) {
                            appInfo = new AppInfo();
                            appInfo.m1722b(Integer.parseInt(query.getString(0)));
                            appInfo.m1720a(query.getString(1));
                            appInfo.m1719a(Integer.parseInt(query.getString(2)));
                            if (query != null) {
                                query.close();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                appInfo = null;
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        return appInfo;
    }

    private synchronized MeanListData m1731c(int i) {
        MeanListData meanListData;
        Throwable th;
        Cursor cursor = null;
        synchronized (this) {
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                Cursor query = a.query("meanlist", new String[]{Name.MARK, "apps", "mean"}, "id=?", new String[]{String.valueOf(i)}, null, null, null, null);
                if (query != null) {
                    try {
                        if (query.moveToFirst() && query.getCount() > 0) {
                            MeanListData a2 = MeanListData.m1765a();
                            a2.getClass();
                            meanListData = new MeanListData(a2);
                            meanListData.m1754a(query.getInt(0));
                            String[] split = query.getString(1).split(Stomp.COMMA);
                            List arrayList = new ArrayList();
                            for (int i2 = 1; i2 < split.length - 1; i2++) {
                                arrayList.add(Integer.valueOf(Integer.parseInt(split[i2])));
                            }
                            meanListData.m1755a(arrayList);
                            meanListData.m1757b(query.getInt(2));
                            if (query != null) {
                                query.close();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                    }
                }
                if (query != null) {
                    query.close();
                }
                meanListData = null;
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        return meanListData;
    }

    private synchronized MeanListData m1733i() {
        MeanListData meanListData;
        Throwable th;
        MeanListData a = MeanListData.m1765a();
        a.getClass();
        meanListData = new MeanListData(a);
        List arrayList = new ArrayList();
        Cursor query;
        try {
            SQLiteDatabase a2 = DatabaseHandler.m1724a();
            f2133c = a2;
            query = a2.query("meanlist", null, null, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst() && query.getCount() > 0) {
                        do {
                            a = MeanListData.m1765a();
                            a.getClass();
                            MeanListData meanListData2 = new MeanListData(a);
                            meanListData2.m1754a(query.getInt(0));
                            String[] split = query.getString(1).split(Stomp.COMMA);
                            List arrayList2 = new ArrayList();
                            for (int i = 1; i < split.length - 1; i++) {
                                arrayList2.add(Integer.valueOf(Integer.parseInt(split[i])));
                            }
                            meanListData2.m1755a(arrayList2);
                            meanListData2.m1757b(query.getInt(2));
                            arrayList.add(meanListData2);
                        } while (query.moveToNext());
                        meanListData.m1762a(arrayList);
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (query != null) {
                query.close();
            }
            MeanListData e = m1745e();
            meanListData.m1761a(e.m1759a());
            meanListData.m1760a(e.m1763b());
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return meanListData;
    }

    private synchronized int m1734j() {
        int i;
        Cursor cursor = null;
        synchronized (this) {
            i = -1;
            try {
                f2133c = DatabaseHandler.m1724a();
                cursor = f2133c.rawQuery("SELECT id FROM appRequestResponse", null);
                if (cursor != null && cursor.moveToFirst()) {
                    i = cursor.getInt(cursor.getColumnIndex(Name.MARK));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return i;
    }

    final synchronized String m1735a(String str) {
        Throwable th;
        String str2 = null;
        synchronized (this) {
            Cursor rawQuery;
            try {
                f2133c = DatabaseHandler.m1724a();
                rawQuery = f2133c.rawQuery("SELECT " + str + " FROM appRequestResponse", null);
                if (rawQuery != null) {
                    try {
                        if (rawQuery.moveToFirst()) {
                            str2 = rawQuery.getString(rawQuery.getColumnIndex(str));
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        throw th;
                    }
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
            } catch (Throwable th3) {
                th = th3;
                rawQuery = null;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                throw th;
            }
        }
        return str2;
    }

    final synchronized void m1736a(int i) {
        Cursor rawQuery;
        Throwable th;
        Map map = null;
        synchronized (this) {
            MeanListData i2 = (i > m1744d() || i <= 0) ? null : m1733i();
            if (i2 != null) {
                try {
                    f2133c = DatabaseHandler.m1724a();
                    rawQuery = f2133c.rawQuery("Select * from applist, installapplist Where applist.name = installapplist.packagename", null);
                    if (rawQuery != null) {
                        Utils.m1924a("===========Cursor size =========== " + rawQuery.getCount());
                        if (rawQuery.moveToFirst()) {
                            do {
                                AppInfo appInfo = new AppInfo();
                                appInfo.m1722b(rawQuery.getInt(rawQuery.getColumnIndex(Name.MARK)));
                                appInfo.m1719a(rawQuery.getInt(rawQuery.getColumnIndex("classification_id")));
                                appInfo.m1720a(rawQuery.getString(rawQuery.getColumnIndex("name")));
                                if (appInfo.m1718a() > 0) {
                                    Object obj = null;
                                    for (MeanListData meanListData : i2.m1764c()) {
                                        try {
                                            if (appInfo.m1718a() == meanListData.m1753a()) {
                                                for (Integer intValue : meanListData.m1758c()) {
                                                    if (intValue.intValue() == appInfo.m1723c()) {
                                                        int i3 = 1;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (obj != null) {
                                                if (map == null) {
                                                    map = new HashMap();
                                                }
                                                if (map.containsKey(Integer.valueOf(appInfo.m1718a()))) {
                                                    map.put(Integer.valueOf(appInfo.m1718a()), Integer.valueOf(((Integer) map.get(Integer.valueOf(appInfo.m1718a()))).intValue() + 1));
                                                } else {
                                                    map.put(Integer.valueOf(appInfo.m1718a()), Integer.valueOf(1));
                                                }
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                }
                            } while (rawQuery.moveToNext());
                        }
                    }
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    if (!(i2 == null || map == null)) {
                        int b;
                        Map hashMap = new HashMap();
                        for (MeanListData meanListData2 : i2.m1764c()) {
                            int a = meanListData2.m1753a();
                            if (map.containsKey(Integer.valueOf(a))) {
                                int intValue2 = ((Integer) map.get(Integer.valueOf(meanListData2.m1753a()))).intValue();
                                b = meanListData2.m1756b();
                                int i4 = intValue2 - b;
                                Utils.m1924a(" Classification Id:= " + a + "count:=" + intValue2 + " mean:= " + b + " valueFromMean:= " + i4);
                                hashMap.put(Integer.valueOf(a), Integer.valueOf(i4));
                            }
                        }
                        Map a2 = Utils.m1923a(hashMap, Utils.f2273b);
                        SQLiteDatabase a3 = DatabaseHandler.m1724a();
                        f2133c = a3;
                        a3.execSQL("DELETE FROM audience");
                        for (Entry key : a2.entrySet()) {
                            b = ((Integer) key.getKey()).intValue();
                            Utils.m1924a(" Audience Id:= " + b);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Name.MARK, Integer.valueOf(b));
                            f2133c.insert("audience", null, contentValues);
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    rawQuery = null;
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    throw th;
                }
            }
        }
    }

    final synchronized void m1737a(Context context, int i) {
        new Thread(new DatabaseHandler(this, context, i)).start();
    }

    final synchronized void m1738a(AppInfo appInfo) {
        if (m1730b(appInfo.m1723c()) == null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Name.MARK, Integer.valueOf(appInfo.m1723c()));
            contentValues.put("name", appInfo.m1721b());
            contentValues.put("classification_id", Integer.valueOf(appInfo.m1718a()));
            SQLiteDatabase a = DatabaseHandler.m1724a();
            f2133c = a;
            a.insert("applist", null, contentValues);
        } else {
            m1728b(appInfo);
        }
    }

    final synchronized void m1739a(MeanListData meanListData) {
        if (m1731c(meanListData.m1753a()) == null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Name.MARK, Integer.valueOf(meanListData.m1753a()));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < meanListData.m1758c().size(); i++) {
                stringBuilder.append(meanListData.m1758c().get(i));
                if (i != meanListData.m1758c().size() - 1) {
                    stringBuilder.append(Stomp.COMMA);
                }
            }
            contentValues.put("apps", stringBuilder.toString());
            contentValues.put("mean", Integer.valueOf(meanListData.m1756b()));
            SQLiteDatabase a = DatabaseHandler.m1724a();
            f2133c = a;
            a.insert("meanlist", null, contentValues);
        }
    }

    final synchronized void m1740a(MeanListData meanListData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Connected.VERSION, meanListData.m1759a());
        contentValues.put("appcount", Integer.valueOf(meanListData.m1763b()));
        MeanListData e = m1745e();
        SQLiteDatabase a;
        if (e == null) {
            a = DatabaseHandler.m1724a();
            f2133c = a;
            a.insert("meanInfo", null, contentValues);
        } else if (!e.m1759a().equalsIgnoreCase(meanListData.m1759a())) {
            a = DatabaseHandler.m1724a();
            f2133c = a;
            a.execSQL("DELETE FROM meanlist");
            f2133c.update("meanInfo", contentValues, null, null);
        }
    }

    final synchronized void m1741a(String str, String str2) {
        try {
            int j = m1734j();
            if (j != -1) {
                m1727a(str, str2, j);
                Utils.m1924a("============Updated " + str);
            } else {
                m1729b(str, str2);
                Utils.m1924a("==========Inserted " + str);
            }
        } catch (Throwable e) {
            Utils.m1924a(Utils.m1922a(e));
        }
    }

    final synchronized int m1742b() {
        Cursor cursor = null;
        int i = 0;
        synchronized (this) {
            String str = "SELECT  * FROM applist WHERE id = (SELECT MAX(id) FROM applist)";
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                cursor = a.rawQuery(str, null);
                if (cursor != null && cursor.moveToFirst()) {
                    i = Integer.parseInt(cursor.getString(0));
                    if (cursor != null) {
                        cursor.close();
                    }
                } else if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return i;
    }

    final synchronized int m1743c() {
        int count;
        Cursor cursor = null;
        synchronized (this) {
            String str = "SELECT  * FROM applist";
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                cursor = a.rawQuery(str, null);
                count = cursor == null ? 0 : cursor.getCount();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return count;
    }

    final synchronized int m1744d() {
        int i;
        Throwable th;
        Cursor query;
        try {
            SQLiteDatabase a = DatabaseHandler.m1724a();
            f2133c = a;
            query = a.query("meanlist", null, null, null, null, null, null);
            if (query == null) {
                i = 0;
            } else {
                try {
                    i = query.getCount();
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return i;
    }

    final synchronized MeanListData m1745e() {
        MeanListData meanListData;
        Throwable th;
        Cursor cursor = null;
        synchronized (this) {
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                Cursor query = a.query("meanInfo", null, null, null, null, null, null);
                if (query != null) {
                    try {
                        if (query.moveToFirst() && query.getCount() > 0) {
                            MeanListData a2 = MeanListData.m1765a();
                            a2.getClass();
                            meanListData = new MeanListData(a2);
                            meanListData.m1761a(query.getString(query.getColumnIndex(Connected.VERSION)));
                            meanListData.m1760a(query.getInt(query.getColumnIndex("appcount")));
                            if (query != null) {
                                query.close();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                meanListData = null;
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        return meanListData;
    }

    final synchronized List<Integer> m1746f() {
        List<Integer> arrayList;
        Cursor cursor = null;
        synchronized (this) {
            String str = "Select * from applist, installapplist Where applist.name = installapplist.packagename";
            arrayList = new ArrayList();
            try {
                SQLiteDatabase a = DatabaseHandler.m1724a();
                f2133c = a;
                cursor = a.rawQuery(str, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    do {
                        int i = cursor.getInt(cursor.getColumnIndex(Name.MARK));
                        Utils.m1924a(" Install App Id:= " + i);
                        arrayList.add(Integer.valueOf(i));
                    } while (cursor.moveToNext());
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return arrayList;
    }

    final synchronized List<Integer> m1747g() {
        List<Integer> arrayList;
        Cursor query;
        Throwable th;
        arrayList = new ArrayList();
        try {
            SQLiteDatabase a = DatabaseHandler.m1724a();
            f2133c = a;
            query = a.query("audience", null, null, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        do {
                            arrayList.add(Integer.valueOf(query.getInt(query.getColumnIndex(Name.MARK))));
                        } while (query.moveToNext());
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            a = DatabaseHandler.m1724a();
            f2133c = a;
            a.execSQL("DELETE FROM audience");
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return arrayList;
    }

    public final void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE applist(id INTEGER,name TEXT,classification_id INTEGER)");
        db.execSQL("CREATE TABLE meanlist(id INTEGER,apps TEXT,mean INTEGER)");
        db.execSQL("CREATE TABLE meanInfo(version TEXT, appcount INTEGER)");
        db.execSQL("CREATE TABLE installapplist(packagename TEXT )");
        db.execSQL("CREATE TABLE audience(id INTEGER )");
        db.execSQL("CREATE TABLE appRequestResponse(id INTEGER PRIMARY KEY  AUTOINCREMENT, deviceEventRequest TEXT ,deviceEventResponse TEXT ,enhanceParamRequest TEXT )");
    }

    public final void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS applist");
        db.execSQL("DROP TABLE IF EXISTS meanlist");
        db.execSQL("DROP TABLE IF EXISTS meanInfo");
        db.execSQL("DROP TABLE IF EXISTS installapplist");
        db.execSQL("DROP TABLE IF EXISTS audience");
        db.execSQL("DROP TABLE IF EXISTS appRequestResponse");
        onCreate(db);
    }
}
