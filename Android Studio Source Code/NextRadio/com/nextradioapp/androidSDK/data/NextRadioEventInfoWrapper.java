package com.nextradioapp.androidSDK.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.nextradioapp.core.objects.Enhancement;
import com.nextradioapp.core.objects.NextRadioEventInfo;
import java.util.ArrayList;
import java.util.Date;

public class NextRadioEventInfoWrapper extends NextRadioEventInfo implements Parcelable {
    public static final Creator<NextRadioEventInfo> CREATOR;
    StationInfoWrapper stationInfo2;

    /* renamed from: com.nextradioapp.androidSDK.data.NextRadioEventInfoWrapper.1 */
    static class C11401 implements Creator<NextRadioEventInfo> {
        C11401() {
        }

        public NextRadioEventInfoWrapper createFromParcel(Parcel in) {
            return new NextRadioEventInfoWrapper(in);
        }

        public NextRadioEventInfo[] newArray(int size) {
            return new NextRadioEventInfo[size];
        }
    }

    static {
        CREATOR = new C11401();
    }

    public NextRadioEventInfoWrapper(NextRadioEventInfo in) {
        this.stationInfo = in.stationInfo;
        this.stationInfo2 = new StationInfoWrapper(this.stationInfo);
        this.timePlayed = in.timePlayed;
        this.album = in.album;
        this.artist = in.artist;
        this.UFIDIdentifier = in.UFIDIdentifier;
        this.imageURL = in.imageURL;
        this.imageURLHiRes = in.imageURLHiRes;
        this.primaryAction = in.primaryAction;
        this.title = in.title;
        this.eventID = in.eventID;
        this.historyID = in.historyID;
        this.itemType = in.itemType;
        this.timePlayed = in.timePlayed;
        this.category = in.category;
        this.trackingID = in.trackingID;
        this.enhancements = (ArrayList) in.enhancements.clone();
    }

    public NextRadioEventInfoWrapper(Parcel in) {
        this.stationInfo2 = (StationInfoWrapper) in.readParcelable(StationInfoWrapper.class.getClassLoader());
        this.stationInfo = this.stationInfo2;
        this.album = in.readString();
        this.artist = in.readString();
        this.UFIDIdentifier = in.readString();
        this.imageURL = in.readString();
        this.imageURLHiRes = in.readString();
        this.primaryAction = in.readString();
        this.title = in.readString();
        this.eventID = in.readLong();
        this.historyID = in.readLong();
        this.itemType = in.readInt();
        this.timePlayed = new Date(in.readLong());
        this.category = in.readString();
        this.trackingID = in.readString();
        int adSize = in.readInt();
        this.enhancements = new ArrayList();
        for (int i = 0; i < adSize; i++) {
            Enhancement ad = new Enhancement();
            ad.type = in.readString();
            int dataFieldCount = in.readInt();
            ad.datafields = new String[dataFieldCount];
            for (int j = 0; j < dataFieldCount; j++) {
                ad.datafields[j] = in.readString();
            }
            this.enhancements.add(ad);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.stationInfo2 = new StationInfoWrapper(this.stationInfo);
        dest.writeParcelable(this.stationInfo2, 0);
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeString(this.UFIDIdentifier);
        dest.writeString(this.imageURL);
        dest.writeString(this.imageURLHiRes);
        dest.writeString(this.primaryAction);
        dest.writeString(this.title);
        dest.writeLong(this.eventID);
        dest.writeLong(this.historyID);
        dest.writeInt(this.itemType);
        dest.writeLong(this.timePlayed.getTime());
        dest.writeString(this.category);
        dest.writeString(this.trackingID);
        dest.writeInt(this.enhancements.size());
        for (int i = 0; i < this.enhancements.size(); i++) {
            dest.writeString(((Enhancement) this.enhancements.get(i)).type);
            dest.writeInt(((Enhancement) this.enhancements.get(i)).datafields.length);
            for (String writeString : ((Enhancement) this.enhancements.get(i)).datafields) {
                dest.writeString(writeString);
            }
        }
    }
}
