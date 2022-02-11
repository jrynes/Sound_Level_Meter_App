package com.facebook.ads;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import com.facebook.ads.internal.util.C0514b;
import com.facebook.ads.internal.util.C0515c;
import org.json.JSONObject;

public class NativeAdViewAttributes {
    private Typeface f1524a;
    private int f1525b;
    private int f1526c;
    private int f1527d;
    private int f1528e;
    private int f1529f;
    private int f1530g;
    private boolean f1531h;

    public NativeAdViewAttributes() {
        this.f1524a = Typeface.DEFAULT;
        this.f1525b = -1;
        this.f1526c = ViewCompat.MEASURED_STATE_MASK;
        this.f1527d = -11643291;
        this.f1528e = 0;
        this.f1529f = -12420889;
        this.f1530g = -12420889;
        this.f1531h = true;
    }

    public NativeAdViewAttributes(JSONObject jSONObject) {
        int i = 0;
        this.f1524a = Typeface.DEFAULT;
        this.f1525b = -1;
        this.f1526c = ViewCompat.MEASURED_STATE_MASK;
        this.f1527d = -11643291;
        this.f1528e = 0;
        this.f1529f = -12420889;
        this.f1530g = -12420889;
        this.f1531h = true;
        try {
            int parseColor = jSONObject.getBoolean("background_transparent") ? 0 : Color.parseColor(jSONObject.getString("background_color"));
            int parseColor2 = Color.parseColor(jSONObject.getString("title_text_color"));
            int parseColor3 = Color.parseColor(jSONObject.getString("description_text_color"));
            int parseColor4 = jSONObject.getBoolean("button_transparent") ? 0 : Color.parseColor(jSONObject.getString("button_color"));
            if (!jSONObject.getBoolean("button_border_transparent")) {
                i = Color.parseColor(jSONObject.getString("button_border_color"));
            }
            int parseColor5 = Color.parseColor(jSONObject.getString("button_text_color"));
            Typeface create = Typeface.create(jSONObject.getString("android_typeface"), 0);
            this.f1525b = parseColor;
            this.f1526c = parseColor2;
            this.f1527d = parseColor3;
            this.f1528e = parseColor4;
            this.f1530g = i;
            this.f1529f = parseColor5;
            this.f1524a = create;
        } catch (Throwable e) {
            C0515c.m1515a(C0514b.m1512a(e, "Error retrieving native ui configuration data"));
        }
    }

    public boolean getAutoplay() {
        return this.f1531h;
    }

    public int getBackgroundColor() {
        return this.f1525b;
    }

    public int getButtonBorderColor() {
        return this.f1530g;
    }

    public int getButtonColor() {
        return this.f1528e;
    }

    public int getButtonTextColor() {
        return this.f1529f;
    }

    public int getDescriptionTextColor() {
        return this.f1527d;
    }

    public int getDescriptionTextSize() {
        return 10;
    }

    public int getTitleTextColor() {
        return this.f1526c;
    }

    public int getTitleTextSize() {
        return 16;
    }

    public Typeface getTypeface() {
        return this.f1524a;
    }

    public NativeAdViewAttributes setAutoplay(boolean z) {
        this.f1531h = z;
        return this;
    }

    public NativeAdViewAttributes setBackgroundColor(int i) {
        this.f1525b = i;
        return this;
    }

    public NativeAdViewAttributes setButtonBorderColor(int i) {
        this.f1530g = i;
        return this;
    }

    public NativeAdViewAttributes setButtonColor(int i) {
        this.f1528e = i;
        return this;
    }

    public NativeAdViewAttributes setButtonTextColor(int i) {
        this.f1529f = i;
        return this;
    }

    public NativeAdViewAttributes setDescriptionTextColor(int i) {
        this.f1527d = i;
        return this;
    }

    public NativeAdViewAttributes setTitleTextColor(int i) {
        this.f1526c = i;
        return this;
    }

    public NativeAdViewAttributes setTypeface(Typeface typeface) {
        this.f1524a = typeface;
        return this;
    }
}
