package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: RequestId */
class ai implements Creator<RequestId> {
    ai() {
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m728a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return m729a(i);
    }

    public RequestId m728a(Parcel parcel) {
        return new RequestId(null);
    }

    public RequestId[] m729a(int i) {
        return new RequestId[i];
    }
}
