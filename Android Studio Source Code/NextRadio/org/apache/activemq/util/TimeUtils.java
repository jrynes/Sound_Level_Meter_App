package org.apache.activemq.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class TimeUtils {
    private TimeUtils() {
    }

    public static String printDuration(double uptime) {
        NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
        NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));
        uptime /= 1000.0d;
        if (uptime < 60.0d) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60.0d;
        if (uptime < 60.0d) {
            long minutes = (long) uptime;
            return fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
        }
        uptime /= 60.0d;
        String s;
        if (uptime < 24.0d) {
            long hours = (long) uptime;
            minutes = (long) ((uptime - ((double) hours)) * 60.0d);
            s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes == 0) {
                return s;
            }
            return s + " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
        }
        uptime /= 24.0d;
        long days = (long) uptime;
        hours = (long) ((uptime - ((double) days)) * 24.0d);
        s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours == 0) {
            return s;
        }
        return s + " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
    }
}
