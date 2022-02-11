package com.nextradioapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date());
    }
}
