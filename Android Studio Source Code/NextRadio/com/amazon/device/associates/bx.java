package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: Image */
class bx implements Creator<Image> {
    bx() {
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m932a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return m933a(i);
    }

    public Image m932a(Parcel parcel) {
        return new Image(null);
    }

    public Image[] m933a(int i) {
        return new Image[i];
    }
}
