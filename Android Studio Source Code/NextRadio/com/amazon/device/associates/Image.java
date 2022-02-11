package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class Image implements Parcelable {
    public static final Creator<Image> CREATOR;
    final String f1077a;
    final int f1078b;
    final int f1079c;

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeIntArray(new int[]{this.f1078b, this.f1079c});
        parcel.writeString(this.f1077a);
    }

    static {
        CREATOR = new bx();
    }

    private Image(Parcel parcel) {
        this.f1078b = parcel.readInt();
        this.f1079c = parcel.readInt();
        this.f1077a = parcel.readString();
    }

    Image(String str, int i, int i2) {
        this.f1077a = str;
        this.f1078b = i;
        this.f1079c = i2;
    }

    JSONObject m652a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SettingsJsonConstants.APP_URL_KEY, this.f1077a);
            jSONObject.put(SettingsJsonConstants.ICON_WIDTH_KEY, this.f1078b);
            jSONObject.put(SettingsJsonConstants.ICON_HEIGHT_KEY, this.f1079c);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m652a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public String getUrl() {
        return this.f1077a;
    }

    public int getWidth() {
        return this.f1078b;
    }

    public int getHeight() {
        return this.f1079c;
    }
}
