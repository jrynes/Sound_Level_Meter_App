package com.nextradioapp.core.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.simpleframework.xml.transform.Transform;

public class DateTransform implements Transform<Date> {
    private static SimpleDateFormat iso8601Formatter;
    private static SimpleDateFormat iso8601FormatterUTC;
    private static SimpleDateFormat msSqlDateFormatter;

    static {
        iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msSqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

    public Date read(String value) throws Exception {
        try {
            return iso8601Parse(value);
        } catch (Exception e) {
            try {
                return msSqlDateParse(value);
            } catch (Exception e2) {
                try {
                    return new Date(value);
                } catch (Exception e3) {
                    return null;
                }
            }
        }
    }

    public String write(Date arg0) throws Exception {
        return iso8601Format(arg0);
    }
}
