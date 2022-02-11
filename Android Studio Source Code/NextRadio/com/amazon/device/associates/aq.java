package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: UserData */
class aq implements Creator<UserData> {
    aq() {
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m779a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return m780a(i);
    }

    public UserData m779a(Parcel parcel) {
        return new UserData(null);
    }

    public UserData[] m780a(int i) {
        return new UserData[i];
    }
}
