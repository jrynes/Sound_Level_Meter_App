package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.json.JSONException;
import org.json.JSONObject;

public class UserData implements Parcelable {
    public static final Creator<UserData> CREATOR;
    private final String f1151a;
    private final String f1152b;

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.f1151a, this.f1152b});
    }

    static {
        CREATOR = new aq();
    }

    private UserData(Parcel parcel) {
        this.f1151a = parcel.readString();
        this.f1152b = parcel.readString();
    }

    UserData(String str, String str2) {
        this.f1151a = str;
        this.f1152b = str2;
    }

    JSONObject m680a() {
        JSONObject jSONObject = new JSONObject();
        try {
            Object obj;
            JSONObject jSONObject2;
            String str = "userId";
            if (this.f1151a != null) {
                obj = this.f1151a;
            } else {
                jSONObject2 = (JSONObject) null;
            }
            jSONObject.put(str, obj);
            str = "marketplace";
            if (this.f1152b != null) {
                obj = this.f1152b;
            } else {
                jSONObject2 = (JSONObject) null;
            }
            jSONObject.put(str, obj);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m680a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public String getUserId() {
        return this.f1151a;
    }

    public String getMarketplace() {
        return this.f1152b;
    }
}
