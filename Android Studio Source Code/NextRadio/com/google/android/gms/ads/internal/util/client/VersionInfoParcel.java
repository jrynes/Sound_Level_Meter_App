package com.google.android.gms.ads.internal.util.client;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;
import org.apache.activemq.command.ActiveMQDestination;

@zzhb
public final class VersionInfoParcel implements SafeParcelable {
    public static final zzc CREATOR;
    public String afmaVersion;
    public final int versionCode;
    public int zzMZ;
    public int zzNa;
    public boolean zzNb;

    static {
        CREATOR = new zzc();
    }

    public VersionInfoParcel(int buddyApkVersion, int clientJarVersion, boolean isClientJar) {
        this(1, "afma-sdk-a-v" + buddyApkVersion + ActiveMQDestination.PATH_SEPERATOR + clientJarVersion + ActiveMQDestination.PATH_SEPERATOR + (isClientJar ? "0" : "1"), buddyApkVersion, clientJarVersion, isClientJar);
    }

    VersionInfoParcel(int versionCode, String afmaVersion, int buddyApkVersion, int clientJarVersion, boolean isClientJar) {
        this.versionCode = versionCode;
        this.afmaVersion = afmaVersion;
        this.zzMZ = buddyApkVersion;
        this.zzNa = clientJarVersion;
        this.zzNb = isClientJar;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
