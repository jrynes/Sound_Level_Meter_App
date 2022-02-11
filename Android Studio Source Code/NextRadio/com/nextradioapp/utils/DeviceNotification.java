package com.nextradioapp.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import org.apache.activemq.transport.stomp.Stomp;

public class DeviceNotification {
    Context context;

    public DeviceNotification(Context c) {
        this.context = c;
    }

    public boolean isDeviceRestricted() {
        String deviceName = Build.MODEL;
        String deviceMan = Build.MANUFACTURER;
        String carrierName = ((TelephonyManager) this.context.getSystemService("phone")).getNetworkOperatorName();
        if ((deviceMan.equalsIgnoreCase("samsung") && (deviceName.contains("SPH-L720") || deviceName.contains("SGH-M919") || deviceName.contains("SGH-I337") || deviceName.contains("SCH-I545") || deviceName.contains("SCH-I545") || deviceName.contains("SM-T817P"))) || deviceName.contains("LS660")) {
            return true;
        }
        if (carrierName.equals("Verizon Wireless")) {
            if (deviceName.equals("m7") || deviceName.contains("htc_m7") || deviceName.contains("m7wlv") || deviceName.contains("htc_himauhl") || deviceName.contains("htc_himauhl") || deviceName.contains("htc_himawl")) {
                return true;
            }
        } else if (carrierName.equals("T-Mobile") && carrierName.equals("US Cellular")) {
            if (deviceName.contains("draconis")) {
                return true;
            }
        } else if (carrierName.equals("AT&T") || carrierName.equals("Cricket")) {
            if (deviceName.contains("cygni") || deviceName.contains("ZMax 2") || deviceName.contains("ZMax2")) {
                return true;
            }
            if (deviceName.contains("Z812")) {
                return true;
            }
        } else if (deviceName.toUpperCase().equals("SM-T217S")) {
            return true;
        }
        return false;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public String getBrandName() {
        return Build.BRAND;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return Stomp.EMPTY;
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }
}
