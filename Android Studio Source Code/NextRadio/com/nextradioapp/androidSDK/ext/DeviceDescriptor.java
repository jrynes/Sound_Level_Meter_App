package com.nextradioapp.androidSDK.ext;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.nextradioapp.core.dependencies.IDeviceDescriptor;
import com.nextradioapp.core.objects.DeviceState;
import java.io.IOException;
import org.apache.activemq.transport.stomp.Stomp;

public class DeviceDescriptor implements IDeviceDescriptor {
    Info adInfo;
    Context mContext;
    String mRadioSource;
    String mTSD;
    DeviceState returnVal;
    String versionName;

    public DeviceDescriptor(Context context, String radioSourceName) {
        this.mRadioSource = DeviceInfo.ORIENTATION_UNKNOWN;
        this.mTSD = Stomp.EMPTY;
        this.mRadioSource = radioSourceName;
        this.mContext = context;
    }

    public DeviceState getDeviceDescription() {
        if (this.returnVal != null) {
            return this.returnVal;
        }
        this.returnVal = new DeviceState();
        this.returnVal.isDisplayOn = true;
        this.returnVal.brand = Build.BRAND;
        this.returnVal.device = Build.DEVICE;
        this.returnVal.manufacturer = Build.MANUFACTURER;
        this.returnVal.model = Build.MODEL;
        this.returnVal.systemVersion = Build.FINGERPRINT;
        this.returnVal.fmAPI = this.mRadioSource;
        this.returnVal.androidID = Secure.getString(this.mContext.getContentResolver(), "android_id");
        this.returnVal.tagStationID = this.mTSD;
        String carrierName = DeviceInfo.ORIENTATION_UNKNOWN;
        try {
            carrierName = ((TelephonyManager) this.mContext.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception e) {
        }
        this.returnVal.carrier = carrierName;
        try {
            this.versionName = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName;
            this.returnVal.nextRadioVersion = this.versionName;
        } catch (NameNotFoundException e2) {
            e2.printStackTrace();
        }
        this.returnVal.adID = Diagnostics.error;
        try {
            this.adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.mContext);
            String id = this.adInfo.getId();
            boolean isLAT = this.adInfo.isLimitAdTrackingEnabled();
            this.returnVal.adID = id;
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e4) {
            e4.printStackTrace();
        } catch (IllegalStateException e5) {
            e5.printStackTrace();
        } catch (GooglePlayServicesRepairableException e6) {
            e6.printStackTrace();
        }
        return this.returnVal;
    }

    public void setDeviceID(String deviceID) {
        this.mTSD = deviceID;
    }

    public String getDeviceVersionCode() {
        try {
            return String.valueOf(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DeviceDescriptor() {
        this.mRadioSource = DeviceInfo.ORIENTATION_UNKNOWN;
        this.mTSD = Stomp.EMPTY;
    }
}
