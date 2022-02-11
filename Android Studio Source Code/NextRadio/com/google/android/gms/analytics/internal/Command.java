package com.google.android.gms.analytics.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Command implements Parcelable {
    @Deprecated
    public static final Creator<Command> CREATOR;
    private String mValue;
    private String zzRt;
    private String zzyv;

    /* renamed from: com.google.android.gms.analytics.internal.Command.1 */
    static class C06511 implements Creator<Command> {
        C06511() {
        }

        @Deprecated
        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return zzq(parcel);
        }

        @Deprecated
        public /* synthetic */ Object[] newArray(int i) {
            return zzag(i);
        }

        @Deprecated
        public Command[] zzag(int i) {
            return new Command[i];
        }

        @Deprecated
        public Command zzq(Parcel parcel) {
            return new Command(parcel);
        }
    }

    static {
        CREATOR = new C06511();
    }

    @Deprecated
    Command(Parcel in) {
        readFromParcel(in);
    }

    @Deprecated
    private void readFromParcel(Parcel in) {
        this.zzyv = in.readString();
        this.zzRt = in.readString();
        this.mValue = in.readString();
    }

    @Deprecated
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.zzyv;
    }

    public String getValue() {
        return this.mValue;
    }

    @Deprecated
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.zzyv);
        out.writeString(this.zzRt);
        out.writeString(this.mValue);
    }
}
