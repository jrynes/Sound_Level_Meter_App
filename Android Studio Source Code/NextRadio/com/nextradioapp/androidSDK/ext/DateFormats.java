package com.nextradioapp.androidSDK.ext;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormats {
    public static DecimalFormat decimalFormat;
    private static SimpleDateFormat iso8601Formatter;
    private static SimpleDateFormat iso8601FormatterUTC;
    private static SimpleDateFormat msSqlDateFormatter;
    private static SimpleDateFormat uiFormatter;

    static {
        uiFormatter = new SimpleDateFormat("M/d/yy h:mma");
        iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msSqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        decimalFormat = new DecimalFormat("###.#");
    }

    public static String uiFormat(Date date) {
        String format;
        synchronized (uiFormatter) {
            format = uiFormatter.format(date);
        }
        return format;
    }

    public static String iso8601Format(Date date) {
        String format;
        synchronized (iso8601Formatter) {
            format = iso8601Formatter.format(date);
        }
        return format;
    }

    public static String iso8601FormatUTC(Date date) {
        String format;
        synchronized (iso8601FormatterUTC) {
            iso8601FormatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            format = iso8601FormatterUTC.format(date);
        }
        return format;
    }

    public static Date iso8601Parse(String dateString) throws ParseException {
        Date parse;
        synchronized (iso8601Formatter) {
            parse = iso8601Formatter.parse(dateString);
        }
        return parse;
    }

    public static String msSqlDateFormat(Date date) {
        String format;
        synchronized (msSqlDateFormatter) {
            format = msSqlDateFormatter.format(date);
        }
        return format;
    }

    public static Date msSqlDateParse(String dateString) throws ParseException {
        Date parse;
        synchronized (msSqlDateFormatter) {
            parse = msSqlDateFormatter.parse(dateString);
        }
        return parse;
    }
}
