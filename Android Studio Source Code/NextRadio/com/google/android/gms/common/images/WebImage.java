package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebImage implements SafeParcelable {
    public static final Creator<WebImage> CREATOR;
    private final int mVersionCode;
    private final Uri zzajZ;
    private final int zzoG;
    private final int zzoH;

    static {
        CREATOR = new zzb();
    }

    WebImage(int versionCode, Uri url, int width, int height) {
        this.mVersionCode = versionCode;
        this.zzajZ = url;
        this.zzoG = width;
        this.zzoH = height;
    }

    public WebImage(Uri url) throws IllegalArgumentException {
        this(url, 0, 0);
    }

    public WebImage(Uri url, int width, int height) throws IllegalArgumentException {
        this(1, url, width, height);
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        } else if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public WebImage(JSONObject json) throws IllegalArgumentException {
        this(zzj(json), json.optInt(SettingsJsonConstants.ICON_WIDTH_KEY, 0), json.optInt(SettingsJsonConstants.ICON_HEIGHT_KEY, 0));
    }

    private static Uri zzj(JSONObject jSONObject) {
        Uri uri = null;
        if (jSONObject.has(SettingsJsonConstants.APP_URL_KEY)) {
            try {
                uri = Uri.parse(jSONObject.getString(SettingsJsonConstants.APP_URL_KEY));
            } catch (JSONException e) {
            }
        }
        return uri;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) other;
        return zzw.equal(this.zzajZ, webImage.zzajZ) && this.zzoG == webImage.zzoG && this.zzoH == webImage.zzoH;
    }

    public int getHeight() {
        return this.zzoH;
    }

    public Uri getUrl() {
        return this.zzajZ;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public int getWidth() {
        return this.zzoG;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzajZ, Integer.valueOf(this.zzoG), Integer.valueOf(this.zzoH));
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SettingsJsonConstants.APP_URL_KEY, this.zzajZ.toString());
            jSONObject.put(SettingsJsonConstants.ICON_WIDTH_KEY, this.zzoG);
            jSONObject.put(SettingsJsonConstants.ICON_HEIGHT_KEY, this.zzoH);
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public String toString() {
        return String.format("Image %dx%d %s", new Object[]{Integer.valueOf(this.zzoG), Integer.valueOf(this.zzoH), this.zzajZ.toString()});
    }

    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
