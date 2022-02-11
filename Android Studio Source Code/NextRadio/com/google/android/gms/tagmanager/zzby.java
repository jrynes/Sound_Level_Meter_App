package com.google.android.gms.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.facebook.ads.AdError;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

class zzby implements zzau {
    private static final String zzQR;
    private final Context mContext;
    private final zzb zzbjE;
    private volatile zzac zzbjF;
    private final zzav zzbjG;
    private final String zzbjH;
    private long zzbjI;
    private final int zzbjJ;
    private zzmq zzqW;

    class zza implements com.google.android.gms.tagmanager.zzcx.zza {
        final /* synthetic */ zzby zzbjK;

        zza(zzby com_google_android_gms_tagmanager_zzby) {
            this.zzbjK = com_google_android_gms_tagmanager_zzby;
        }

        public void zza(zzaq com_google_android_gms_tagmanager_zzaq) {
            this.zzbjK.zzq(com_google_android_gms_tagmanager_zzaq.zzGD());
        }

        public void zzb(zzaq com_google_android_gms_tagmanager_zzaq) {
            this.zzbjK.zzq(com_google_android_gms_tagmanager_zzaq.zzGD());
            zzbg.m1676v("Permanent failure dispatching hitId: " + com_google_android_gms_tagmanager_zzaq.zzGD());
        }

        public void zzc(zzaq com_google_android_gms_tagmanager_zzaq) {
            long zzGE = com_google_android_gms_tagmanager_zzaq.zzGE();
            if (zzGE == 0) {
                this.zzbjK.zzd(com_google_android_gms_tagmanager_zzaq.zzGD(), this.zzbjK.zzqW.currentTimeMillis());
            } else if (zzGE + 14400000 < this.zzbjK.zzqW.currentTimeMillis()) {
                this.zzbjK.zzq(com_google_android_gms_tagmanager_zzaq.zzGD());
                zzbg.m1676v("Giving up on failed hitId: " + com_google_android_gms_tagmanager_zzaq.zzGD());
            }
        }
    }

    class zzb extends SQLiteOpenHelper {
        final /* synthetic */ zzby zzbjK;
        private boolean zzbjL;
        private long zzbjM;

        zzb(zzby com_google_android_gms_tagmanager_zzby, Context context, String str) {
            this.zzbjK = com_google_android_gms_tagmanager_zzby;
            super(context, str, null, 1);
            this.zzbjM = 0;
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
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM gtm_hits WHERE 0", null);
            Set hashSet = new HashSet();
            try {
                String[] columnNames = rawQuery.getColumnNames();
                for (Object add : columnNames) {
                    hashSet.add(add);
                }
                if (!hashSet.remove("hit_id") || !hashSet.remove("hit_url") || !hashSet.remove("hit_time") || !hashSet.remove("hit_first_send_time")) {
                    throw new SQLiteException("Database column missing");
                } else if (!hashSet.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                }
            } finally {
                rawQuery.close();
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            if (!this.zzbjL || this.zzbjM + 3600000 <= this.zzbjK.zzqW.currentTimeMillis()) {
                SQLiteDatabase sQLiteDatabase = null;
                this.zzbjL = true;
                this.zzbjM = this.zzbjK.zzqW.currentTimeMillis();
                try {
                    sQLiteDatabase = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    this.zzbjK.mContext.getDatabasePath(this.zzbjK.zzbjH).delete();
                }
                if (sQLiteDatabase == null) {
                    sQLiteDatabase = super.getWritableDatabase();
                }
                this.zzbjL = false;
                return sQLiteDatabase;
            }
            throw new SQLiteException("Database creation failed");
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
            if (zza("gtm_hits", db)) {
                zzc(db);
            } else {
                db.execSQL(zzby.zzQR);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    static {
        zzQR = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", new Object[]{"gtm_hits", "hit_id", "hit_time", "hit_url", "hit_first_send_time"});
    }

    zzby(zzav com_google_android_gms_tagmanager_zzav, Context context) {
        this(com_google_android_gms_tagmanager_zzav, context, "gtm_urls.db", AdError.SERVER_ERROR_CODE);
    }

    zzby(zzav com_google_android_gms_tagmanager_zzav, Context context, String str, int i) {
        this.mContext = context.getApplicationContext();
        this.zzbjH = str;
        this.zzbjG = com_google_android_gms_tagmanager_zzav;
        this.zzqW = zzmt.zzsc();
        this.zzbjE = new zzb(this, this.mContext, this.zzbjH);
        this.zzbjF = new zzcx(this.mContext, new zza(this));
        this.zzbjI = 0;
        this.zzbjJ = i;
    }

    private void zzGQ() {
        int zzGR = (zzGR() - this.zzbjJ) + 1;
        if (zzGR > 0) {
            List zzkl = zzkl(zzGR);
            zzbg.m1676v("Store full, deleting " + zzkl.size() + " hits to make room.");
            zzf((String[]) zzkl.toArray(new String[0]));
        }
    }

    private void zzd(long j, long j2) {
        SQLiteDatabase zzgb = zzgb("Error opening database for getNumStoredHits.");
        if (zzgb != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hit_first_send_time", Long.valueOf(j2));
            try {
                zzgb.update("gtm_hits", contentValues, "hit_id=?", new String[]{String.valueOf(j)});
            } catch (SQLiteException e) {
                zzbg.zzaK("Error setting HIT_FIRST_DISPATCH_TIME for hitId: " + j);
                zzq(j);
            }
        }
    }

    private SQLiteDatabase zzgb(String str) {
        try {
            return this.zzbjE.getWritableDatabase();
        } catch (SQLiteException e) {
            zzbg.zzaK(str);
            return null;
        }
    }

    private void zzh(long j, String str) {
        SQLiteDatabase zzgb = zzgb("Error opening database for putHit");
        if (zzgb != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hit_time", Long.valueOf(j));
            contentValues.put("hit_url", str);
            contentValues.put("hit_first_send_time", Integer.valueOf(0));
            try {
                zzgb.insert("gtm_hits", null, contentValues);
                this.zzbjG.zzax(false);
            } catch (SQLiteException e) {
                zzbg.zzaK("Error storing hit");
            }
        }
    }

    private void zzq(long j) {
        zzf(new String[]{String.valueOf(j)});
    }

    public void dispatch() {
        zzbg.m1676v("GTM Dispatch running...");
        if (this.zzbjF.zzGw()) {
            List zzkm = zzkm(40);
            if (zzkm.isEmpty()) {
                zzbg.m1676v("...nothing to dispatch");
                this.zzbjG.zzax(true);
                return;
            }
            this.zzbjF.zzE(zzkm);
            if (zzGS() > 0) {
                zzcu.zzHo().dispatch();
            }
        }
    }

    int zzGR() {
        Cursor cursor = null;
        int i = 0;
        SQLiteDatabase zzgb = zzgb("Error opening database for getNumStoredHits.");
        if (zzgb != null) {
            try {
                cursor = zzgb.rawQuery("SELECT COUNT(*) from gtm_hits", null);
                if (cursor.moveToFirst()) {
                    i = (int) cursor.getLong(0);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SQLiteException e) {
                zzbg.zzaK("Error getting numStoredHits");
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

    int zzGS() {
        int count;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        SQLiteDatabase zzgb = zzgb("Error opening database for getNumStoredHits.");
        if (zzgb == null) {
            return 0;
        }
        try {
            Cursor query = zzgb.query("gtm_hits", new String[]{"hit_id", "hit_first_send_time"}, "hit_first_send_time=0", null, null, null, null);
            try {
                count = query.getCount();
                if (query != null) {
                    query.close();
                }
            } catch (SQLiteException e) {
                cursor = query;
                try {
                    zzbg.zzaK("Error getting num untried hits");
                    if (cursor == null) {
                        count = 0;
                    } else {
                        cursor.close();
                        count = 0;
                    }
                    return count;
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
            zzbg.zzaK("Error getting num untried hits");
            if (cursor == null) {
                cursor.close();
                count = 0;
            } else {
                count = 0;
            }
            return count;
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
        return count;
    }

    void zzf(String[] strArr) {
        boolean z = true;
        if (strArr != null && strArr.length != 0) {
            SQLiteDatabase zzgb = zzgb("Error opening database for deleteHits.");
            if (zzgb != null) {
                try {
                    zzgb.delete("gtm_hits", String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(Stomp.COMMA, Collections.nCopies(strArr.length, "?"))}), strArr);
                    zzav com_google_android_gms_tagmanager_zzav = this.zzbjG;
                    if (zzGR() != 0) {
                        z = false;
                    }
                    com_google_android_gms_tagmanager_zzav.zzax(z);
                } catch (SQLiteException e) {
                    zzbg.zzaK("Error deleting hits");
                }
            }
        }
    }

    public void zzg(long j, String str) {
        zzjN();
        zzGQ();
        zzh(j, str);
    }

    int zzjN() {
        boolean z = true;
        long currentTimeMillis = this.zzqW.currentTimeMillis();
        if (currentTimeMillis <= this.zzbjI + 86400000) {
            return 0;
        }
        this.zzbjI = currentTimeMillis;
        SQLiteDatabase zzgb = zzgb("Error opening database for deleteStaleHits.");
        if (zzgb == null) {
            return 0;
        }
        int delete = zzgb.delete("gtm_hits", "HIT_TIME < ?", new String[]{Long.toString(this.zzqW.currentTimeMillis() - 2592000000L)});
        zzav com_google_android_gms_tagmanager_zzav = this.zzbjG;
        if (zzGR() != 0) {
            z = false;
        }
        com_google_android_gms_tagmanager_zzav.zzax(z);
        return delete;
    }

    List<String> zzkl(int i) {
        SQLiteException e;
        Throwable th;
        List<String> arrayList = new ArrayList();
        if (i <= 0) {
            zzbg.zzaK("Invalid maxHits specified. Skipping");
            return arrayList;
        }
        SQLiteDatabase zzgb = zzgb("Error opening database for peekHitIds.");
        if (zzgb == null) {
            return arrayList;
        }
        Cursor query;
        try {
            query = zzgb.query("gtm_hits", new String[]{"hit_id"}, null, null, null, null, String.format("%s ASC", new Object[]{"hit_id"}), Integer.toString(i));
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
                    zzbg.zzaK("Error in peekHits fetching hitIds: " + e.getMessage());
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
            zzbg.zzaK("Error in peekHits fetching hitIds: " + e.getMessage());
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.google.android.gms.tagmanager.zzaq> zzkm(int r17) {
        /*
        r16 = this;
        r11 = new java.util.ArrayList;
        r11.<init>();
        r2 = "Error opening database for peekHits";
        r0 = r16;
        r2 = r0.zzgb(r2);
        if (r2 != 0) goto L_0x0011;
    L_0x000f:
        r2 = r11;
    L_0x0010:
        return r2;
    L_0x0011:
        r12 = 0;
        r3 = "gtm_hits";
        r4 = 3;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r5 = 0;
        r6 = "hit_id";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r5 = 1;
        r6 = "hit_time";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r5 = 2;
        r6 = "hit_first_send_time";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = "%s ASC";
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r13 = 0;
        r14 = "hit_id";
        r10[r13] = r14;	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r9 = java.lang.String.format(r9, r10);	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r10 = java.lang.Integer.toString(r17);	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r13 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x00ca, all -> 0x00ef }
        r12 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x0171, all -> 0x016b }
        r12.<init>();	 Catch:{ SQLiteException -> 0x0171, all -> 0x016b }
        r3 = r13.moveToFirst();	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        if (r3 == 0) goto L_0x0068;
    L_0x004b:
        r3 = new com.google.android.gms.tagmanager.zzaq;	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r4 = 0;
        r4 = r13.getLong(r4);	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r6 = 1;
        r6 = r13.getLong(r6);	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r8 = 2;
        r8 = r13.getLong(r8);	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r3.<init>(r4, r6, r8);	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r12.add(r3);	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        r3 = r13.moveToNext();	 Catch:{ SQLiteException -> 0x0177, all -> 0x016b }
        if (r3 != 0) goto L_0x004b;
    L_0x0068:
        if (r13 == 0) goto L_0x006d;
    L_0x006a:
        r13.close();
    L_0x006d:
        r11 = 0;
        r3 = "gtm_hits";
        r4 = 2;
        r4 = new java.lang.String[r4];	 Catch:{ SQLiteException -> 0x0169 }
        r5 = 0;
        r6 = "hit_id";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x0169 }
        r5 = 1;
        r6 = "hit_url";
        r4[r5] = r6;	 Catch:{ SQLiteException -> 0x0169 }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = "%s ASC";
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ SQLiteException -> 0x0169 }
        r14 = 0;
        r15 = "hit_id";
        r10[r14] = r15;	 Catch:{ SQLiteException -> 0x0169 }
        r9 = java.lang.String.format(r9, r10);	 Catch:{ SQLiteException -> 0x0169 }
        r10 = java.lang.Integer.toString(r17);	 Catch:{ SQLiteException -> 0x0169 }
        r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10);	 Catch:{ SQLiteException -> 0x0169 }
        r2 = r3.moveToFirst();	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        if (r2 == 0) goto L_0x00c2;
    L_0x009d:
        r4 = r11;
    L_0x009e:
        r0 = r3;
        r0 = (android.database.sqlite.SQLiteCursor) r0;	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = r0;
        r2 = r2.getWindow();	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = r2.getNumRows();	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        if (r2 <= 0) goto L_0x00f6;
    L_0x00ac:
        r2 = r12.get(r4);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = (com.google.android.gms.tagmanager.zzaq) r2;	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r5 = 1;
        r5 = r3.getString(r5);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2.zzgf(r5);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
    L_0x00ba:
        r2 = r4 + 1;
        r4 = r3.moveToNext();	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        if (r4 != 0) goto L_0x017d;
    L_0x00c2:
        if (r3 == 0) goto L_0x00c7;
    L_0x00c4:
        r3.close();
    L_0x00c7:
        r2 = r12;
        goto L_0x0010;
    L_0x00ca:
        r2 = move-exception;
        r3 = r2;
        r4 = r12;
        r2 = r11;
    L_0x00ce:
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016e }
        r5.<init>();	 Catch:{ all -> 0x016e }
        r6 = "Error in peekHits fetching hitIds: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x016e }
        r3 = r3.getMessage();	 Catch:{ all -> 0x016e }
        r3 = r5.append(r3);	 Catch:{ all -> 0x016e }
        r3 = r3.toString();	 Catch:{ all -> 0x016e }
        com.google.android.gms.tagmanager.zzbg.zzaK(r3);	 Catch:{ all -> 0x016e }
        if (r4 == 0) goto L_0x0010;
    L_0x00ea:
        r4.close();
        goto L_0x0010;
    L_0x00ef:
        r2 = move-exception;
    L_0x00f0:
        if (r12 == 0) goto L_0x00f5;
    L_0x00f2:
        r12.close();
    L_0x00f5:
        throw r2;
    L_0x00f6:
        r5 = "HitString for hitId %d too large.  Hit will be deleted.";
        r2 = 1;
        r6 = new java.lang.Object[r2];	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r7 = 0;
        r2 = r12.get(r4);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = (com.google.android.gms.tagmanager.zzaq) r2;	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r8 = r2.zzGD();	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = java.lang.Long.valueOf(r8);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r6[r7] = r2;	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        r2 = java.lang.String.format(r5, r6);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        com.google.android.gms.tagmanager.zzbg.zzaK(r2);	 Catch:{ SQLiteException -> 0x0114, all -> 0x0166 }
        goto L_0x00ba;
    L_0x0114:
        r2 = move-exception;
        r13 = r3;
    L_0x0116:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x015f }
        r3.<init>();	 Catch:{ all -> 0x015f }
        r4 = "Error in peekHits fetching hit url: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x015f }
        r2 = r2.getMessage();	 Catch:{ all -> 0x015f }
        r2 = r3.append(r2);	 Catch:{ all -> 0x015f }
        r2 = r2.toString();	 Catch:{ all -> 0x015f }
        com.google.android.gms.tagmanager.zzbg.zzaK(r2);	 Catch:{ all -> 0x015f }
        r3 = new java.util.ArrayList;	 Catch:{ all -> 0x015f }
        r3.<init>();	 Catch:{ all -> 0x015f }
        r4 = 0;
        r5 = r12.iterator();	 Catch:{ all -> 0x015f }
    L_0x013a:
        r2 = r5.hasNext();	 Catch:{ all -> 0x015f }
        if (r2 == 0) goto L_0x0152;
    L_0x0140:
        r2 = r5.next();	 Catch:{ all -> 0x015f }
        r2 = (com.google.android.gms.tagmanager.zzaq) r2;	 Catch:{ all -> 0x015f }
        r6 = r2.zzGF();	 Catch:{ all -> 0x015f }
        r6 = android.text.TextUtils.isEmpty(r6);	 Catch:{ all -> 0x015f }
        if (r6 == 0) goto L_0x015b;
    L_0x0150:
        if (r4 == 0) goto L_0x015a;
    L_0x0152:
        if (r13 == 0) goto L_0x0157;
    L_0x0154:
        r13.close();
    L_0x0157:
        r2 = r3;
        goto L_0x0010;
    L_0x015a:
        r4 = 1;
    L_0x015b:
        r3.add(r2);	 Catch:{ all -> 0x015f }
        goto L_0x013a;
    L_0x015f:
        r2 = move-exception;
    L_0x0160:
        if (r13 == 0) goto L_0x0165;
    L_0x0162:
        r13.close();
    L_0x0165:
        throw r2;
    L_0x0166:
        r2 = move-exception;
        r13 = r3;
        goto L_0x0160;
    L_0x0169:
        r2 = move-exception;
        goto L_0x0116;
    L_0x016b:
        r2 = move-exception;
        r12 = r13;
        goto L_0x00f0;
    L_0x016e:
        r2 = move-exception;
        r12 = r4;
        goto L_0x00f0;
    L_0x0171:
        r2 = move-exception;
        r3 = r2;
        r4 = r13;
        r2 = r11;
        goto L_0x00ce;
    L_0x0177:
        r2 = move-exception;
        r3 = r2;
        r4 = r13;
        r2 = r12;
        goto L_0x00ce;
    L_0x017d:
        r4 = r2;
        goto L_0x009e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzby.zzkm(int):java.util.List<com.google.android.gms.tagmanager.zzaq>");
    }
}
