package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmr;
import java.util.Set;

public final class UserDataType implements SafeParcelable {
    public static final zzm CREATOR;
    public static final UserDataType zzaPX;
    public static final UserDataType zzaPY;
    public static final UserDataType zzaPZ;
    public static final Set<UserDataType> zzaQa;
    final int mVersionCode;
    final String zzJN;
    final int zzaQb;

    static {
        zzaPX = zzy("test_type", 1);
        zzaPY = zzy("labeled_place", 6);
        zzaPZ = zzy("here_content", 7);
        zzaQa = zzmr.zza(zzaPX, zzaPY, zzaPZ);
        CREATOR = new zzm();
    }

    UserDataType(int versionCode, String type, int enumValue) {
        zzx.zzcM(type);
        this.mVersionCode = versionCode;
        this.zzJN = type;
        this.zzaQb = enumValue;
    }

    private static UserDataType zzy(String str, int i) {
        return new UserDataType(0, str, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserDataType)) {
            return false;
        }
        UserDataType userDataType = (UserDataType) object;
        return this.zzJN.equals(userDataType.zzJN) && this.zzaQb == userDataType.zzaQb;
    }

    public int hashCode() {
        return this.zzJN.hashCode();
    }

    public String toString() {
        return this.zzJN;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzm.zza(this, parcel, flags);
    }
}
