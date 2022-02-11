package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: com.amazon.device.associates.h */
class Receipt implements Creator<Receipt> {
    Receipt() {
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m960a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return m961a(i);
    }

    public Receipt m960a(Parcel parcel) {
        return new Receipt(null);
    }

    public Receipt[] m961a(int i) {
        return new Receipt[i];
    }
}
