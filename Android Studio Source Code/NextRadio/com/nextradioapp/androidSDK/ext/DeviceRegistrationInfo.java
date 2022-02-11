package com.nextradioapp.androidSDK.ext;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.IOException;
import org.apache.activemq.transport.stomp.Stomp;

public class DeviceRegistrationInfo {
    public String AdID;
    public String AndroidID;
    public String Brand;
    public String Carrier;
    public String Device;
    public String FmAPI;
    public String Manufacturer;
    public String Model;
    public String NextRadioVersion;
    public String OtherInfo;
    public String SystemVersion;
    public String TagStationID;

    /* renamed from: com.nextradioapp.androidSDK.ext.DeviceRegistrationInfo.1 */
    static class C11461 extends DeviceRegistrationInfo {
        C11461() {
            this.Brand = Build.BRAND;
            this.Device = Build.DEVICE;
            this.Manufacturer = Build.MANUFACTURER;
            this.Model = Build.MODEL;
            this.SystemVersion = Build.FINGERPRINT;
            this.AndroidID = "android_id";
        }
    }

    public static DeviceRegistrationInfo createFromDevice() {
        return new C11461();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.Brand);
        sb.append(this.Device);
        sb.append(this.Manufacturer);
        sb.append(this.Model);
        sb.append(this.OtherInfo);
        sb.append(this.FmAPI);
        sb.append(this.AdID);
        sb.append(this.AndroidID);
        sb.append(this.NextRadioVersion);
        sb.append(this.SystemVersion);
        return sb.toString();
    }

    public DeviceRegistrationInfo withNextRadioVersionID(Context context) {
        try {
            this.NextRadioVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return this;
    }

    public DeviceRegistrationInfo withTagStationID(String id) {
        if (id == null) {
            id = Stomp.EMPTY;
        }
        this.TagStationID = id;
        return this;
    }

    public DeviceRegistrationInfo withOtherInfo(String info) {
        this.OtherInfo = info;
        return this;
    }

    public DeviceRegistrationInfo withCarrier(String carrier) {
        this.Carrier = carrier;
        return this;
    }

    public DeviceRegistrationInfo withSecureAndroidID(Context context) {
        this.AndroidID = Secure.getString(context.getContentResolver(), "android_id");
        return this;
    }

    public DeviceRegistrationInfo withAndroidAdvertisementID(Context context) {
        this.AdID = Diagnostics.error;
        try {
            Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            String id = adInfo.getId();
            boolean isLAT = adInfo.isLimitAdTrackingEnabled();
            this.AdID = id;
        } catch (IOException e) {
        } catch (GooglePlayServicesNotAvailableException e2) {
        } catch (IllegalStateException e3) {
        } catch (GooglePlayServicesRepairableException e4) {
        }
        return this;
    }

    public DeviceRegistrationInfo withFmAPI(String fmAPI) {
        this.FmAPI = fmAPI;
        return this;
    }
}
