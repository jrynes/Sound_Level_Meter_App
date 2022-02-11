package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Parcelable {
    public static final Creator<Product> CREATOR;
    private final String f1090a;
    private final String f1091b;
    private final String f1092c;
    private final Image f1093d;
    private final Price f1094e;
    private final String f1095f;
    private final double f1096g;

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.f1090a, this.f1091b, this.f1092c, this.f1095f});
        parcel.writeDouble(this.f1096g);
        parcel.writeParcelable(this.f1093d, i);
        parcel.writeParcelable(this.f1094e, i);
    }

    static {
        CREATOR = new by();
    }

    private Product(Parcel parcel) {
        this.f1090a = parcel.readString();
        this.f1091b = parcel.readString();
        this.f1092c = parcel.readString();
        this.f1095f = parcel.readString();
        this.f1096g = parcel.readDouble();
        this.f1093d = (Image) parcel.readParcelable(Image.class.getClassLoader());
        this.f1094e = (Price) parcel.readParcelable(Price.class.getClassLoader());
    }

    Product(String str, String str2, String str3, Image image, Price price, String str4, double d) {
        ar.m782a((Object) str, "productId");
        ar.m782a((Object) str2, SettingsJsonConstants.PROMPT_TITLE_KEY);
        ar.m782a((Object) str3, activityEvents.description);
        this.f1090a = str;
        this.f1091b = str2;
        this.f1092c = str3;
        this.f1093d = image;
        this.f1094e = price;
        this.f1096g = d;
        this.f1095f = str4;
    }

    JSONObject m654a() {
        JSONObject jSONObject = new JSONObject();
        try {
            Object a;
            JSONObject jSONObject2;
            jSONObject.put("productId", this.f1090a);
            jSONObject.put(SettingsJsonConstants.PROMPT_TITLE_KEY, this.f1091b);
            jSONObject.put(activityEvents.description, this.f1092c);
            String str = Constants.NATIVE_AD_IMAGE_ELEMENT;
            if (this.f1093d != null) {
                a = this.f1093d.m652a();
            } else {
                jSONObject2 = (JSONObject) null;
            }
            jSONObject.put(str, a);
            str = "price";
            if (this.f1094e != null) {
                a = this.f1094e.m653a();
            } else {
                jSONObject2 = (JSONObject) null;
            }
            jSONObject.put(str, a);
            jSONObject.put(AdMarvelNativeAd.FIELD_RATING, this.f1096g);
            jSONObject.put("brand", this.f1095f);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        String str = null;
        try {
            str = m654a().toString(4);
        } catch (JSONException e) {
        }
        return str;
    }

    public String getProductId() {
        return this.f1090a;
    }

    public String getTitle() {
        return this.f1091b;
    }

    public String getDescription() {
        return this.f1092c;
    }

    public Image getImage() {
        return this.f1093d;
    }

    public Price getPrice() {
        return this.f1094e;
    }

    public String getBrand() {
        return this.f1095f;
    }

    public double getRating() {
        return this.f1096g;
    }
}
