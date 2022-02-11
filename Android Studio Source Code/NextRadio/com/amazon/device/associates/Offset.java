package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Offset implements Parcelable {
    public static final Offset BEGINNING;
    public static final Creator<Offset> CREATOR;
    private final String f1080a;

    static {
        BEGINNING = new Offset("0");
        CREATOR = new an();
    }

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f1080a);
    }

    private Offset(Parcel parcel) {
        this.f1080a = parcel.readString();
    }

    Offset(String str) {
        this.f1080a = str;
    }

    public static Offset fromString(String str) {
        ar.m782a((Object) str, "encodedOffset");
        return "0".equals(str) ? BEGINNING : new Offset(str);
    }

    public String toString() {
        return this.f1080a;
    }
}
