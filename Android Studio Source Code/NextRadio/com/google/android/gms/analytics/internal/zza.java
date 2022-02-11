package com.google.android.gms.analytics.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Locale;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

public class zza extends zzd {
    public static boolean zzPV;
    private Info zzPW;
    private final zzaj zzPX;
    private String zzPY;
    private boolean zzPZ;
    private Object zzQa;

    zza(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
        this.zzPZ = false;
        this.zzQa = new Object();
        this.zzPX = new zzaj(com_google_android_gms_analytics_internal_zzf.zzjl());
    }

    private boolean zza(Info info, Info info2) {
        String str = null;
        String id = info2 == null ? null : info2.getId();
        if (TextUtils.isEmpty(id)) {
            return true;
        }
        String zzkk = zzjr().zzkk();
        synchronized (this.zzQa) {
            if (!this.zzPZ) {
                this.zzPY = zzjb();
                this.zzPZ = true;
            } else if (TextUtils.isEmpty(this.zzPY)) {
                if (info != null) {
                    str = info.getId();
                }
                if (str == null) {
                    boolean zzbc = zzbc(id + zzkk);
                    return zzbc;
                }
                this.zzPY = zzbb(str + zzkk);
            }
            Object zzbb = zzbb(id + zzkk);
            if (TextUtils.isEmpty(zzbb)) {
                return false;
            } else if (zzbb.equals(this.zzPY)) {
                return true;
            } else {
                if (TextUtils.isEmpty(this.zzPY)) {
                    str = zzkk;
                } else {
                    zzbd("Resetting the client id because Advertising Id changed.");
                    str = zzjr().zzkl();
                    zza("New client Id", str);
                }
                zzbc = zzbc(id + str);
                return zzbc;
            }
        }
    }

    private static String zzbb(String str) {
        if (zzam.zzbv(CommonUtils.MD5_INSTANCE) == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, zzam.zzbv(CommonUtils.MD5_INSTANCE).digest(str.getBytes()))});
    }

    private boolean zzbc(String str) {
        try {
            String zzbb = zzbb(str);
            zzbd("Storing hashed adid.");
            FileOutputStream openFileOutput = getContext().openFileOutput("gaClientIdData", 0);
            openFileOutput.write(zzbb.getBytes());
            openFileOutput.close();
            this.zzPY = zzbb;
            return true;
        } catch (IOException e) {
            zze("Error creating hash file", e);
            return false;
        }
    }

    private synchronized Info zziZ() {
        if (this.zzPX.zzv(1000)) {
            this.zzPX.start();
            Info zzja = zzja();
            if (zza(this.zzPW, zzja)) {
                this.zzPW = zzja;
            } else {
                zzbh("Failed to reset client id on adid change. Not using adid");
                this.zzPW = new Info(Stomp.EMPTY, false);
            }
        }
        return this.zzPW;
    }

    protected void zziJ() {
    }

    public boolean zziU() {
        zzjv();
        Info zziZ = zziZ();
        return (zziZ == null || zziZ.isLimitAdTrackingEnabled()) ? false : true;
    }

    public String zziY() {
        zzjv();
        Info zziZ = zziZ();
        CharSequence id = zziZ != null ? zziZ.getId() : null;
        return TextUtils.isEmpty(id) ? null : id;
    }

    protected Info zzja() {
        Info info = null;
        try {
            info = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
        } catch (IllegalStateException e) {
            zzbg("IllegalStateException getting Ad Id Info. If you would like to see Audience reports, please ensure that you have added '<meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />' to your application manifest file. See http://goo.gl/naFqQk for details.");
        } catch (Throwable th) {
            if (!zzPV) {
                zzPV = true;
                zzd("Error getting advertiser id", th);
            }
        }
        return info;
    }

    protected String zzjb() {
        Object obj;
        String str = null;
        try {
            FileInputStream openFileInput = getContext().openFileInput("gaClientIdData");
            byte[] bArr = new byte[Flags.FLAG8];
            int read = openFileInput.read(bArr, 0, Flags.FLAG8);
            if (openFileInput.available() > 0) {
                zzbg("Hash file seems corrupted, deleting it.");
                openFileInput.close();
                getContext().deleteFile("gaClientIdData");
                return null;
            } else if (read <= 0) {
                zzbd("Hash file is empty.");
                openFileInput.close();
                return null;
            } else {
                String str2 = new String(bArr, 0, read);
                try {
                    openFileInput.close();
                    return str2;
                } catch (FileNotFoundException e) {
                    return str2;
                } catch (IOException e2) {
                    IOException iOException = e2;
                    str = str2;
                    IOException iOException2 = iOException;
                    zzd("Error reading Hash file, deleting it", obj);
                    getContext().deleteFile("gaClientIdData");
                    return str;
                }
            }
        } catch (FileNotFoundException e3) {
            return null;
        } catch (IOException e4) {
            obj = e4;
            zzd("Error reading Hash file, deleting it", obj);
            getContext().deleteFile("gaClientIdData");
            return str;
        }
    }
}
