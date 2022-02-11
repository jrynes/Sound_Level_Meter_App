package com.nextradioapp.androidSDK.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.nextradioapp.core.objects.StationInfo;

public class StationInfoWrapper extends StationInfo implements Parcelable {
    public static final Creator<StationInfoWrapper> CREATOR;

    /* renamed from: com.nextradioapp.androidSDK.data.StationInfoWrapper.1 */
    static class C11411 implements Creator<StationInfoWrapper> {
        C11411() {
        }

        public StationInfoWrapper createFromParcel(Parcel in) {
            return new StationInfoWrapper(in);
        }

        public StationInfoWrapper[] newArray(int size) {
            return new StationInfoWrapper[size];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeInt(this.publicStationID);
        dest.writeString(this.description);
        dest.writeString(this.callLetters);
        dest.writeString(this.country);
        dest.writeString(this.endpoint);
        dest.writeString(this.genre);
        dest.writeString(this.headline);
        dest.writeString(this.headlineText);
        dest.writeString(this.imageURL);
        dest.writeString(this.imageURLHiRes);
        dest.writeString(this.market);
        dest.writeString(this.piCode);
        dest.writeString(this.slogan);
        dest.writeInt(this.frequencyHz);
        dest.writeInt(this.frequencySubChannel);
        if (!this.isFavorited) {
            i = 0;
        }
        dest.writeInt(i);
    }

    public StationInfoWrapper(Parcel in) {
        boolean z = true;
        this.publicStationID = in.readInt();
        this.description = in.readString();
        this.callLetters = in.readString();
        this.country = in.readString();
        this.endpoint = in.readString();
        this.genre = in.readString();
        this.headline = in.readString();
        this.headlineText = in.readString();
        this.imageURL = in.readString();
        this.imageURLHiRes = in.readString();
        this.market = in.readString();
        this.piCode = in.readString();
        this.slogan = in.readString();
        this.frequencyHz = in.readInt();
        this.frequencySubChannel = in.readInt();
        if (in.readInt() != 1) {
            z = false;
        }
        this.isFavorited = z;
    }

    public StationInfoWrapper(StationInfo in) {
        this.publicStationID = in.publicStationID;
        this.description = in.description;
        this.callLetters = in.callLetters;
        this.country = in.country;
        this.endpoint = in.endpoint;
        this.genre = in.genre;
        this.headline = in.headline;
        this.headlineText = in.headlineText;
        this.imageURL = in.imageURL;
        this.imageURLHiRes = in.imageURLHiRes;
        this.market = in.market;
        this.piCode = in.piCode;
        this.slogan = in.slogan;
        this.frequencyHz = in.frequencyHz;
        this.frequencySubChannel = in.frequencySubChannel;
        this.isFavorited = in.isFavorited;
    }

    static {
        CREATOR = new C11411();
    }
}
