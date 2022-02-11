package com.amazon.device.associates;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.math.BigDecimal;
import java.util.Currency;
import org.json.JSONException;
import org.json.JSONObject;

public class Price implements Parcelable {
    public static final Creator<Price> CREATOR;
    final Currency f1086a;
    final BigDecimal f1087b;
    final BigDecimal f1088c;
    final String f1089d;

    public int describeContents() {
        return hashCode();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.f1086a.toString(), this.f1087b.toString(), this.f1088c.toString(), this.f1089d});
    }

    static {
        CREATOR = new ao();
    }

    private Price(Parcel parcel) {
        this.f1086a = Currency.getInstance(parcel.readString());
        this.f1087b = new BigDecimal(parcel.readString());
        this.f1088c = new BigDecimal(parcel.readString());
        this.f1089d = parcel.readString();
    }

    Price(Currency currency, BigDecimal bigDecimal, BigDecimal bigDecimal2, String str) {
        ar.m782a((Object) currency, "currency");
        ar.m782a((Object) bigDecimal, "minValue");
        ar.m782a((Object) bigDecimal2, "maxValue");
        ar.m782a((Object) str, "formattedString");
        this.f1086a = currency;
        this.f1087b = bigDecimal;
        this.f1088c = bigDecimal2;
        this.f1089d = str;
    }

    JSONObject m653a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("currency", this.f1086a.toString());
            jSONObject.put("minValue", this.f1087b.doubleValue());
            jSONObject.put("maxValue", this.f1088c.doubleValue());
            jSONObject.put("formattedPrice", this.f1089d);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public Currency getCurrency() {
        return this.f1086a;
    }

    public BigDecimal getMinValue() {
        return this.f1087b;
    }

    public BigDecimal getMaxValue() {
        return this.f1088c;
    }

    public String getFormattedString() {
        return this.f1089d;
    }

    public boolean isRange() {
        return this.f1087b != this.f1088c;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Price) {
            Price price = (Price) obj;
            if (this.f1086a.equals(price.f1086a) && this.f1087b.equals(price.f1087b) && this.f1088c.equals(price.f1088c) && this.f1089d.equals(price.f1089d)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return getFormattedString();
    }
}
