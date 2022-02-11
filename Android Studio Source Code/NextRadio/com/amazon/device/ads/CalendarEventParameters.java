package com.amazon.device.ads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class CalendarEventParameters {
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mmZZZZZ";
    public static final List<String> DATE_FORMATS;
    private String description;
    private Date end;
    private String location;
    private Date start;
    private String summary;

    /* renamed from: com.amazon.device.ads.CalendarEventParameters.1 */
    static class C02981 extends ArrayList<String> {
        C02981() {
            add(CalendarEventParameters.DATE_FORMAT);
            add("yyyy-MM-dd'T'HH:mmZ");
            add("yyyy-MM-dd'T'HH:mm");
            add("yyyy-MM-dd");
        }
    }

    static {
        DATE_FORMATS = Collections.unmodifiableList(new C02981());
    }

    public CalendarEventParameters(String description, String location, String summary, String start, String end) {
        if (StringUtils.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("No description for event");
        }
        this.description = description;
        this.location = location;
        this.summary = summary;
        if (StringUtils.isNullOrEmpty(start)) {
            throw new IllegalArgumentException("No start date for event");
        }
        this.start = convertToDate(start);
        if (StringUtils.isNullOrEmpty(end)) {
            this.end = null;
        } else {
            this.end = convertToDate(end);
        }
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getSummary() {
        return this.summary;
    }

    public Date getStart() {
        return this.start;
    }

    public Date getEnd() {
        return this.end;
    }

    private Date convertToDate(String dateTimeString) {
        Date dateTime = null;
        for (String format : DATE_FORMATS) {
            try {
                dateTime = new SimpleDateFormat(format, Locale.US).parse(dateTimeString);
                break;
            } catch (ParseException e) {
            }
        }
        if (dateTime != null) {
            return dateTime;
        }
        throw new IllegalArgumentException("Could not parse datetime string " + dateTimeString);
    }
}
