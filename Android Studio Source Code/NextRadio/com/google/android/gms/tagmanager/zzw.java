package com.google.android.gms.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.AdError;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;

class zzw implements zzc {
    private static final String zzbiB;
    private final Context mContext;
    private final Executor zzbiC;
    private zza zzbiD;
    private int zzbiE;
    private zzmq zzqW;

    /* renamed from: com.google.android.gms.tagmanager.zzw.1 */
    class C10561 implements Runnable {
        final /* synthetic */ List zzbiF;
        final /* synthetic */ long zzbiG;
        final /* synthetic */ zzw zzbiH;

        C10561(zzw com_google_android_gms_tagmanager_zzw, List list, long j) {
            this.zzbiH = com_google_android_gms_tagmanager_zzw;
            this.zzbiF = list;
            this.zzbiG = j;
        }

        public void run() {
            this.zzbiH.zzb(this.zzbiF, this.zzbiG);
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzw.2 */
    class C10572 implements Runnable {
        final /* synthetic */ zzw zzbiH;
        final /* synthetic */ com.google.android.gms.tagmanager.DataLayer.zzc.zza zzbiI;

        C10572(zzw com_google_android_gms_tagmanager_zzw, com.google.android.gms.tagmanager.DataLayer.zzc.zza com_google_android_gms_tagmanager_DataLayer_zzc_zza) {
            this.zzbiH = com_google_android_gms_tagmanager_zzw;
            this.zzbiI = com_google_android_gms_tagmanager_DataLayer_zzc_zza;
        }

        public void run() {
            this.zzbiI.zzB(this.zzbiH.zzGr());
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzw.3 */
    class C10583 implements Runnable {
        final /* synthetic */ zzw zzbiH;
        final /* synthetic */ String zzbiJ;

        C10583(zzw com_google_android_gms_tagmanager_zzw, String str) {
            this.zzbiH = com_google_android_gms_tagmanager_zzw;
            this.zzbiJ = str;
        }

        public void run() {
            this.zzbiH.zzga(this.zzbiJ);
        }
    }

    class zza extends SQLiteOpenHelper {
        final /* synthetic */ zzw zzbiH;

        zza(zzw com_google_android_gms_tagmanager_zzw, Context context, String str) {
            this.zzbiH = com_google_android_gms_tagmanager_zzw;
            super(context, str, null, 1);
        }

        private boolean zza(String str, SQLiteDatabase sQLiteDatabase) {
            Cursor cursor;
            Throwable th;
            Cursor cursor2 = null;
            try {
                SQLiteDatabase sQLiteDatabase2 = sQLiteDatabase;
                Cursor query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
                try {
                    boolean moveToFirst = query.moveToFirst();
                    if (query == null) {
                        return moveToFirst;
                    }
                    query.close();
                    return moveToFirst;
                } catch (SQLiteException e) {
                    cursor = query;
                    try {
                        zzbg.zzaK("Error querying for table " + str);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return false;
                    } catch (Throwable th2) {
                        cursor2 = cursor;
                        th = th2;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e2) {
                cursor = null;
                zzbg.zzaK("Error querying for table " + str);
                if (cursor != null) {
                    cursor.close();
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th;
            }
        }

        private void zzc(SQLiteDatabase sQLiteDatabase) {
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM datalayer WHERE 0", null);
            Set hashSet = new HashSet();
            try {
                String[] columnNames = rawQuery.getColumnNames();
                for (Object add : columnNames) {
                    hashSet.add(add);
                }
                if (!hashSet.remove(Constants.NATIVE_AD_KEY_ELEMENT) || !hashSet.remove(Constants.NATIVE_AD_VALUE_ELEMENT) || !hashSet.remove("ID") || !hashSet.remove(Send.EXPIRATION_TIME)) {
                    throw new SQLiteException("Database column missing");
                } else if (!hashSet.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } finally {
                rawQuery.close();
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase sQLiteDatabase = null;
            try {
                sQLiteDatabase = super.getWritableDatabase();
            } catch (SQLiteException e) {
                this.zzbiH.mContext.getDatabasePath("google_tagmanager.db").delete();
            }
            return sQLiteDatabase == null ? super.getWritableDatabase() : sQLiteDatabase;
        }

        public void onCreate(SQLiteDatabase db) {
            zzal.zzbo(db.getPath());
        }

        public void onOpen(SQLiteDatabase db) {
            if (VERSION.SDK_INT < 15) {
                Cursor rawQuery = db.rawQuery("PRAGMA journal_mode=memory", null);
                try {
                    rawQuery.moveToFirst();
                } finally {
                    rawQuery.close();
                }
            }
            if (zza("datalayer", db)) {
                zzc(db);
            } else {
                db.execSQL(zzw.zzbiB);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private static class zzb {
        final byte[] zzbiK;
        final String zzvs;

        zzb(String str, byte[] bArr) {
            this.zzvs = str;
            this.zzbiK = bArr;
        }

        public String toString() {
            return "KeyAndSerialized: key = " + this.zzvs + " serialized hash = " + Arrays.hashCode(this.zzbiK);
        }
    }

    static {
        zzbiB = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' STRING NOT NULL, '%s' BLOB NOT NULL, '%s' INTEGER NOT NULL);", new Object[]{"datalayer", "ID", Constants.NATIVE_AD_KEY_ELEMENT, Constants.NATIVE_AD_VALUE_ELEMENT, Send.EXPIRATION_TIME});
    }

    public zzw(Context context) {
        this(context, zzmt.zzsc(), "google_tagmanager.db", AdError.SERVER_ERROR_CODE, Executors.newSingleThreadExecutor());
    }

    zzw(Context context, zzmq com_google_android_gms_internal_zzmq, String str, int i, Executor executor) {
        this.mContext = context;
        this.zzqW = com_google_android_gms_internal_zzmq;
        this.zzbiE = i;
        this.zzbiC = executor;
        this.zzbiD = new zza(this, this.mContext, str);
    }

    private List<zza> zzC(List<zzb> list) {
        List<zza> arrayList = new ArrayList();
        for (zzb com_google_android_gms_tagmanager_zzw_zzb : list) {
            arrayList.add(new zza(com_google_android_gms_tagmanager_zzw_zzb.zzvs, zzw(com_google_android_gms_tagmanager_zzw_zzb.zzbiK)));
        }
        return arrayList;
    }

    private List<zzb> zzD(List<zza> list) {
        List<zzb> arrayList = new ArrayList();
        for (zza com_google_android_gms_tagmanager_DataLayer_zza : list) {
            arrayList.add(new zzb(com_google_android_gms_tagmanager_DataLayer_zza.zzvs, zzJ(com_google_android_gms_tagmanager_DataLayer_zza.zzNc)));
        }
        return arrayList;
    }

    private List<zza> zzGr() {
        try {
            zzal(this.zzqW.currentTimeMillis());
            List<zza> zzC = zzC(zzGs());
            return zzC;
        } finally {
            zzGu();
        }
    }

    private List<zzb> zzGs() {
        SQLiteDatabase zzgb = zzgb("Error opening database for loadSerialized.");
        List<zzb> arrayList = new ArrayList();
        if (zzgb == null) {
            return arrayList;
        }
        Cursor query = zzgb.query("datalayer", new String[]{Constants.NATIVE_AD_KEY_ELEMENT, Constants.NATIVE_AD_VALUE_ELEMENT}, null, null, null, null, "ID", null);
        while (query.moveToNext()) {
            try {
                arrayList.add(new zzb(query.getString(0), query.getBlob(1)));
            } finally {
                query.close();
            }
        }
        return arrayList;
    }

    private int zzGt() {
        Cursor cursor = null;
        int i = 0;
        SQLiteDatabase zzgb = zzgb("Error opening database for getNumStoredEntries.");
        if (zzgb != null) {
            try {
                cursor = zzgb.rawQuery("SELECT COUNT(*) from datalayer", null);
                if (cursor.moveToFirst()) {
                    i = (int) cursor.getLong(0);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLiteException e) {
                zzbg.zzaK("Error getting numStoredEntries");
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

    private void zzGu() {
        try {
            this.zzbiD.close();
        } catch (SQLiteException e) {
        }
    }

    private byte[] zzJ(Object obj) {
        Throwable th;
        byte[] bArr = null;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(obj);
                bArr = byteArrayOutputStream.toByteArray();
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                    }
                }
                byteArrayOutputStream.close();
            } catch (IOException e2) {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
                byteArrayOutputStream.close();
                return bArr;
            } catch (Throwable th2) {
                th = th2;
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e4) {
                        throw th;
                    }
                }
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (IOException e5) {
            objectOutputStream = bArr;
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            byteArrayOutputStream.close();
            return bArr;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            objectOutputStream = bArr;
            th = th4;
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            byteArrayOutputStream.close();
            throw th;
        }
        return bArr;
    }

    private void zzal(long j) {
        SQLiteDatabase zzgb = zzgb("Error opening database for deleteOlderThan.");
        if (zzgb != null) {
            try {
                zzbg.m1676v("Deleted " + zzgb.delete("datalayer", "expires <= ?", new String[]{Long.toString(j)}) + " expired items");
            } catch (SQLiteException e) {
                zzbg.zzaK("Error deleting old entries.");
            }
        }
    }

    private synchronized void zzb(List<zzb> list, long j) {
        try {
            long currentTimeMillis = this.zzqW.currentTimeMillis();
            zzal(currentTimeMillis);
            zzkf(list.size());
            zzc(list, currentTimeMillis + j);
            zzGu();
        } catch (Throwable th) {
            zzGu();
        }
    }

    private void zzc(List<zzb> list, long j) {
        SQLiteDatabase zzgb = zzgb("Error opening database for writeEntryToDatabase.");
        if (zzgb != null) {
            for (zzb com_google_android_gms_tagmanager_zzw_zzb : list) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Send.EXPIRATION_TIME, Long.valueOf(j));
                contentValues.put(Constants.NATIVE_AD_KEY_ELEMENT, com_google_android_gms_tagmanager_zzw_zzb.zzvs);
                contentValues.put(Constants.NATIVE_AD_VALUE_ELEMENT, com_google_android_gms_tagmanager_zzw_zzb.zzbiK);
                zzgb.insert("datalayer", null, contentValues);
            }
        }
    }

    private void zze(String[] strArr) {
        if (strArr != null && strArr.length != 0) {
            SQLiteDatabase zzgb = zzgb("Error opening database for deleteEntries.");
            if (zzgb != null) {
                try {
                    zzgb.delete("datalayer", String.format("%s in (%s)", new Object[]{"ID", TextUtils.join(Stomp.COMMA, Collections.nCopies(strArr.length, "?"))}), strArr);
                } catch (SQLiteException e) {
                    zzbg.zzaK("Error deleting entries " + Arrays.toString(strArr));
                }
            }
        }
    }

    private void zzga(String str) {
        SQLiteDatabase zzgb = zzgb("Error opening database for clearKeysWithPrefix.");
        if (zzgb != null) {
            try {
                zzbg.m1676v("Cleared " + zzgb.delete("datalayer", "key = ? OR key LIKE ?", new String[]{str, str + ".%"}) + " items");
            } catch (SQLiteException e) {
                zzbg.zzaK("Error deleting entries with key prefix: " + str + " (" + e + ").");
            } finally {
                zzGu();
            }
        }
    }

    private SQLiteDatabase zzgb(String str) {
        try {
            return this.zzbiD.getWritableDatabase();
        } catch (SQLiteException e) {
            zzbg.zzaK(str);
            return null;
        }
    }

    private void zzkf(int i) {
        int zzGt = (zzGt() - this.zzbiE) + i;
        if (zzGt > 0) {
            List zzkg = zzkg(zzGt);
            zzbg.zzaJ("DataLayer store full, deleting " + zzkg.size() + " entries to make room.");
            zze((String[]) zzkg.toArray(new String[0]));
        }
    }

    private List<String> zzkg(int i) {
        Cursor query;
        SQLiteException e;
        Throwable th;
        List<String> arrayList = new ArrayList();
        if (i <= 0) {
            zzbg.zzaK("Invalid maxEntries specified. Skipping.");
            return arrayList;
        }
        SQLiteDatabase zzgb = zzgb("Error opening database for peekEntryIds.");
        if (zzgb == null) {
            return arrayList;
        }
        try {
            query = zzgb.query("datalayer", new String[]{"ID"}, null, null, null, null, String.format("%s ASC", new Object[]{"ID"}), Integer.toString(i));
            try {
                if (query.moveToFirst()) {
                    do {
                        arrayList.add(String.valueOf(query.getLong(0)));
                    } while (query.moveToNext());
                }
                if (query != null) {
                    query.close();
                }
            } catch (SQLiteException e2) {
                e = e2;
                try {
                    zzbg.zzaK("Error in peekEntries fetching entryIds: " + e.getMessage());
                    if (query != null) {
                        query.close();
                    }
                    return arrayList;
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
        } catch (SQLiteException e3) {
            e = e3;
            query = null;
            zzbg.zzaK("Error in peekEntries fetching entryIds: " + e.getMessage());
            if (query != null) {
                query.close();
            }
            return arrayList;
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

    private Object zzw(byte[] bArr) {
        ObjectInputStream objectInputStream;
        Object readObject;
        Throwable th;
        ObjectInputStream objectInputStream2 = null;
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                readObject = objectInputStream.readObject();
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                    }
                }
                byteArrayInputStream.close();
            } catch (IOException e2) {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e3) {
                    }
                }
                byteArrayInputStream.close();
                return readObject;
            } catch (ClassNotFoundException e4) {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e5) {
                    }
                }
                byteArrayInputStream.close();
                return readObject;
            } catch (Throwable th2) {
                th = th2;
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e6) {
                        throw th;
                    }
                }
                byteArrayInputStream.close();
                throw th;
            }
        } catch (IOException e7) {
            objectInputStream = objectInputStream2;
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            byteArrayInputStream.close();
            return readObject;
        } catch (ClassNotFoundException e8) {
            objectInputStream = objectInputStream2;
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            byteArrayInputStream.close();
            return readObject;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            objectInputStream = objectInputStream2;
            th = th4;
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            byteArrayInputStream.close();
            throw th;
        }
        return readObject;
    }

    public void zza(com.google.android.gms.tagmanager.DataLayer.zzc.zza com_google_android_gms_tagmanager_DataLayer_zzc_zza) {
        this.zzbiC.execute(new C10572(this, com_google_android_gms_tagmanager_DataLayer_zzc_zza));
    }

    public void zza(List<zza> list, long j) {
        this.zzbiC.execute(new C10561(this, zzD(list), j));
    }

    public void zzfZ(String str) {
        this.zzbiC.execute(new C10583(this, str));
    }
}
