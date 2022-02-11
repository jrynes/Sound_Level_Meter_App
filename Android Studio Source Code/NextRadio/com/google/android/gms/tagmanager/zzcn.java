package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import com.google.android.gms.internal.zzaf.zzf;
import com.google.android.gms.internal.zzrq.zza;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrs.zzc;
import com.google.android.gms.internal.zzrs.zzg;
import com.google.android.gms.internal.zzst;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

class zzcn implements zzf {
    private final Context mContext;
    private final String zzbhM;
    private zzbf<zza> zzbkg;
    private final ExecutorService zzbkn;

    /* renamed from: com.google.android.gms.tagmanager.zzcn.1 */
    class C10401 implements Runnable {
        final /* synthetic */ zzcn zzbko;

        C10401(zzcn com_google_android_gms_tagmanager_zzcn) {
            this.zzbko = com_google_android_gms_tagmanager_zzcn;
        }

        public void run() {
            this.zzbko.zzHc();
        }
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcn.2 */
    class C10412 implements Runnable {
        final /* synthetic */ zzcn zzbko;
        final /* synthetic */ zza zzbkp;

        C10412(zzcn com_google_android_gms_tagmanager_zzcn, zza com_google_android_gms_internal_zzrq_zza) {
            this.zzbko = com_google_android_gms_tagmanager_zzcn;
            this.zzbkp = com_google_android_gms_internal_zzrq_zza;
        }

        public void run() {
            this.zzbko.zzc(this.zzbkp);
        }
    }

    zzcn(Context context, String str) {
        this.mContext = context;
        this.zzbhM = str;
        this.zzbkn = Executors.newSingleThreadExecutor();
    }

    private zzc zza(ByteArrayOutputStream byteArrayOutputStream) {
        zzc com_google_android_gms_internal_zzrs_zzc = null;
        try {
            com_google_android_gms_internal_zzrs_zzc = zzaz.zzgi(byteArrayOutputStream.toString(HttpRequest.CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            zzbg.zzaI("Failed to convert binary resource to string for JSON parsing; the file format is not UTF-8 format.");
        } catch (JSONException e2) {
            zzbg.zzaK("Failed to extract the container from the resource file. Resource is a UTF-8 encoded string but doesn't contain a JSON container");
        }
        return com_google_android_gms_internal_zzrs_zzc;
    }

    private void zzd(zza com_google_android_gms_internal_zzrq_zza) throws IllegalArgumentException {
        if (com_google_android_gms_internal_zzrq_zza.zzju == null && com_google_android_gms_internal_zzrq_zza.zzbme == null) {
            throw new IllegalArgumentException("Resource and SupplementedResource are NULL.");
        }
    }

    private zzc zzx(byte[] bArr) {
        try {
            zzc zzb = zzrs.zzb(zzf.zzc(bArr));
            if (zzb == null) {
                return zzb;
            }
            zzbg.m1676v("The container was successfully loaded from the resource (using binary file)");
            return zzb;
        } catch (zzst e) {
            zzbg.m1675e("The resource file is corrupted. The container cannot be extracted from the binary file");
            return null;
        } catch (zzg e2) {
            zzbg.zzaK("The resource file is invalid. The container from the binary file is invalid");
            return null;
        }
    }

    public synchronized void release() {
        this.zzbkn.shutdown();
    }

    public void zzGl() {
        this.zzbkn.execute(new C10401(this));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void zzHc() {
        /*
        r3 = this;
        r0 = r3.zzbkg;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r0 = new java.lang.IllegalStateException;
        r1 = "Callback must be set before execute";
        r0.<init>(r1);
        throw r0;
    L_0x000c:
        r0 = r3.zzbkg;
        r0.zzGk();
        r0 = "Attempting to load resource from disk";
        com.google.android.gms.tagmanager.zzbg.m1676v(r0);
        r0 = com.google.android.gms.tagmanager.zzcb.zzGU();
        r0 = r0.zzGV();
        r1 = com.google.android.gms.tagmanager.zzcb.zza.CONTAINER;
        if (r0 == r1) goto L_0x002e;
    L_0x0022:
        r0 = com.google.android.gms.tagmanager.zzcb.zzGU();
        r0 = r0.zzGV();
        r1 = com.google.android.gms.tagmanager.zzcb.zza.CONTAINER_DEBUG;
        if (r0 != r1) goto L_0x0046;
    L_0x002e:
        r0 = r3.zzbhM;
        r1 = com.google.android.gms.tagmanager.zzcb.zzGU();
        r1 = r1.getContainerId();
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0046;
    L_0x003e:
        r0 = r3.zzbkg;
        r1 = com.google.android.gms.tagmanager.zzbf.zza.NOT_AVAILABLE;
        r0.zza(r1);
    L_0x0045:
        return;
    L_0x0046:
        r1 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0070 }
        r0 = r3.zzHd();	 Catch:{ FileNotFoundException -> 0x0070 }
        r1.<init>(r0);	 Catch:{ FileNotFoundException -> 0x0070 }
        r0 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r0.<init>();	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        com.google.android.gms.internal.zzrs.zzb(r1, r0);	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r0 = r0.toByteArray();	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r0 = com.google.android.gms.internal.zzrq.zza.zzy(r0);	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r3.zzd(r0);	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r2 = r3.zzbkg;	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r2.zzI(r0);	 Catch:{ IOException -> 0x0085, IllegalArgumentException -> 0x009d }
        r1.close();	 Catch:{ IOException -> 0x007e }
    L_0x006a:
        r0 = "The Disk resource was successfully read.";
        com.google.android.gms.tagmanager.zzbg.m1676v(r0);
        goto L_0x0045;
    L_0x0070:
        r0 = move-exception;
        r0 = "Failed to find the resource in the disk";
        com.google.android.gms.tagmanager.zzbg.zzaI(r0);
        r0 = r3.zzbkg;
        r1 = com.google.android.gms.tagmanager.zzbf.zza.NOT_AVAILABLE;
        r0.zza(r1);
        goto L_0x0045;
    L_0x007e:
        r0 = move-exception;
        r0 = "Error closing stream for reading resource from disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r0);
        goto L_0x006a;
    L_0x0085:
        r0 = move-exception;
        r0 = r3.zzbkg;	 Catch:{ all -> 0x00b5 }
        r2 = com.google.android.gms.tagmanager.zzbf.zza.IO_ERROR;	 Catch:{ all -> 0x00b5 }
        r0.zza(r2);	 Catch:{ all -> 0x00b5 }
        r0 = "Failed to read the resource from disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r0);	 Catch:{ all -> 0x00b5 }
        r1.close();	 Catch:{ IOException -> 0x0096 }
        goto L_0x006a;
    L_0x0096:
        r0 = move-exception;
        r0 = "Error closing stream for reading resource from disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r0);
        goto L_0x006a;
    L_0x009d:
        r0 = move-exception;
        r0 = r3.zzbkg;	 Catch:{ all -> 0x00b5 }
        r2 = com.google.android.gms.tagmanager.zzbf.zza.IO_ERROR;	 Catch:{ all -> 0x00b5 }
        r0.zza(r2);	 Catch:{ all -> 0x00b5 }
        r0 = "Failed to read the resource from disk. The resource is inconsistent";
        com.google.android.gms.tagmanager.zzbg.zzaK(r0);	 Catch:{ all -> 0x00b5 }
        r1.close();	 Catch:{ IOException -> 0x00ae }
        goto L_0x006a;
    L_0x00ae:
        r0 = move-exception;
        r0 = "Error closing stream for reading resource from disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r0);
        goto L_0x006a;
    L_0x00b5:
        r0 = move-exception;
        r1.close();	 Catch:{ IOException -> 0x00ba }
    L_0x00b9:
        throw r0;
    L_0x00ba:
        r1 = move-exception;
        r1 = "Error closing stream for reading resource from disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r1);
        goto L_0x00b9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzcn.zzHc():void");
    }

    File zzHd() {
        return new File(this.mContext.getDir("google_tagmanager", 0), "resource_" + this.zzbhM);
    }

    public void zza(zzbf<zza> com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzrq_zza) {
        this.zzbkg = com_google_android_gms_tagmanager_zzbf_com_google_android_gms_internal_zzrq_zza;
    }

    public void zzb(zza com_google_android_gms_internal_zzrq_zza) {
        this.zzbkn.execute(new C10412(this, com_google_android_gms_internal_zzrq_zza));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean zzc(com.google.android.gms.internal.zzrq.zza r5) {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.zzHd();
        r2 = new java.io.FileOutputStream;	 Catch:{ FileNotFoundException -> 0x0016 }
        r2.<init>(r1);	 Catch:{ FileNotFoundException -> 0x0016 }
        r3 = com.google.android.gms.internal.zzsu.toByteArray(r5);	 Catch:{ IOException -> 0x0024 }
        r2.write(r3);	 Catch:{ IOException -> 0x0024 }
        r0 = 1;
        r2.close();	 Catch:{ IOException -> 0x001d }
    L_0x0015:
        return r0;
    L_0x0016:
        r1 = move-exception;
        r1 = "Error opening resource file for writing";
        com.google.android.gms.tagmanager.zzbg.m1675e(r1);
        goto L_0x0015;
    L_0x001d:
        r1 = move-exception;
        r1 = "error closing stream for writing resource to disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r1);
        goto L_0x0015;
    L_0x0024:
        r3 = move-exception;
        r3 = "Error writing resource to disk. Removing resource from disk.";
        com.google.android.gms.tagmanager.zzbg.zzaK(r3);	 Catch:{ all -> 0x0038 }
        r1.delete();	 Catch:{ all -> 0x0038 }
        r2.close();	 Catch:{ IOException -> 0x0031 }
        goto L_0x0015;
    L_0x0031:
        r1 = move-exception;
        r1 = "error closing stream for writing resource to disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r1);
        goto L_0x0015;
    L_0x0038:
        r0 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x003d }
    L_0x003c:
        throw r0;
    L_0x003d:
        r1 = move-exception;
        r1 = "error closing stream for writing resource to disk";
        com.google.android.gms.tagmanager.zzbg.zzaK(r1);
        goto L_0x003c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzcn.zzc(com.google.android.gms.internal.zzrq$zza):boolean");
    }

    public zzc zzke(int i) {
        try {
            InputStream openRawResource = this.mContext.getResources().openRawResource(i);
            zzbg.m1676v("Attempting to load a container from the resource ID " + i + " (" + this.mContext.getResources().getResourceName(i) + ")");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zzrs.zzb(openRawResource, byteArrayOutputStream);
                zzc zza = zza(byteArrayOutputStream);
                if (zza == null) {
                    return zzx(byteArrayOutputStream.toByteArray());
                }
                zzbg.m1676v("The container was successfully loaded from the resource (using JSON file format)");
                return zza;
            } catch (IOException e) {
                zzbg.zzaK("Error reading the default container with resource ID " + i + " (" + this.mContext.getResources().getResourceName(i) + ")");
                return null;
            }
        } catch (NotFoundException e2) {
            zzbg.zzaK("Failed to load the container. No default container resource found with the resource ID " + i);
            return null;
        }
    }
}
