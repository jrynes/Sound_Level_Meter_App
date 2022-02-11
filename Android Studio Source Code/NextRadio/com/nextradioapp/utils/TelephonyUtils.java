package com.nextradioapp.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.nextradioapp.nextradio.NextRadioSDKWrapperProvider;
import java.util.Locale;

public class TelephonyUtils {
    private static final int OTHER_INDEX = 6;

    public static int getCountryCodeIndex(Context c) {
        String countryCode = NextRadioSDKWrapperProvider.getInstance().getCountryCode();
        String[] codes = c.getResources().getStringArray(2131230734);
        if (countryCode == null) {
            countryCode = ((TelephonyManager) c.getSystemService("phone")).getSimCountryIso();
        }
        int index = -1;
        boolean bFound = false;
        for (String s : codes) {
            index++;
            if (countryCode.toUpperCase().equals(s)) {
                bFound = true;
                break;
            }
        }
        if (!bFound) {
            index = -1;
            for (String s2 : codes) {
                index++;
                if (Locale.getDefault().getCountry().equals(s2)) {
                    bFound = true;
                    break;
                }
            }
        }
        if (!bFound) {
            index = OTHER_INDEX;
        }
        Log.d("Telephoney", "getCountry:" + Locale.getDefault().getCountry());
        return index;
    }
}
