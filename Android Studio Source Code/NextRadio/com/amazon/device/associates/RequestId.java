package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class RequestId implements Parcelable {
    public static final Creator<RequestId> CREATOR;
    private final String f1129a;

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f1129a);
    }

    static {
        CREATOR = new ai();
    }

    private RequestId(Parcel parcel) {
        this.f1129a = parcel.readString();
    }

    RequestId() {
        this.f1129a = UUID.randomUUID().toString();
    }

    private RequestId(String str) {
        this.f1129a = str;
    }

    public String toString() {
        return this.f1129a;
    }

    static RequestId m671a(String str) {
        return new RequestId(str);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.f1129a.equals(((RequestId) obj).f1129a);
    }
}
