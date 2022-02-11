package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* compiled from: Product */
class by implements Creator<Product> {
    by() {
    }

    public /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m934a(parcel);
    }

    public /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return m935a(i);
    }

    public Product m934a(Parcel parcel) {
        return new Product(null);
    }

    public Product[] m935a(int i) {
        return new Product[i];
    }
}
