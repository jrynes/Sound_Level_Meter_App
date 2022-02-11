package org.apache.activemq.broker.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import javax.jms.MessageFormatException;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

public class CronParser {
    private static final int DAY_OF_MONTH = 2;
    private static final int DAY_OF_WEEK = 4;
    private static final int HOURS = 1;
    private static final int MINUTES = 0;
    private static final int MONTH = 3;
    private static final int NUMBER_TOKENS = 5;

    static class CronEntry {
        List<Integer> currentWhen;
        final int end;
        final String name;
        final int start;
        final String token;

        CronEntry(String name, String token, int start, int end) {
            this.name = name;
            this.token = token;
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return this.name + Headers.SEPERATOR + this.token;
        }
    }

    public static long getNextScheduledTime(String cronEntry, long currentTime) throws MessageFormatException {
        if (cronEntry == null || cronEntry.length() == 0) {
            return 0;
        }
        if (cronEntry.startsWith("* * * * *")) {
            return ((currentTime + 60000) / 1000) * 1000;
        }
        List<CronEntry> entries = buildCronEntries(tokenize(cronEntry));
        Calendar working = Calendar.getInstance();
        working.setTimeInMillis(currentTime);
        working.set(13, MINUTES);
        CronEntry minutes = (CronEntry) entries.get(MINUTES);
        CronEntry hours = (CronEntry) entries.get(HOURS);
        CronEntry dayOfMonth = (CronEntry) entries.get(DAY_OF_MONTH);
        CronEntry month = (CronEntry) entries.get(MONTH);
        CronEntry dayOfWeek = (CronEntry) entries.get(DAY_OF_WEEK);
        working.add(13, 60 - working.get(13));
        int currentMinutes = working.get(12);
        if (!isCurrent(minutes, currentMinutes)) {
            working.add(12, getNext(minutes, currentMinutes));
        }
        int currentHours = working.get(11);
        if (!isCurrent(hours, currentHours)) {
            working.add(11, getNext(hours, currentHours));
        }
        doUpdateCurrentDay(working, dayOfMonth, dayOfWeek);
        doUpdateCurrentMonth(working, month);
        doUpdateCurrentDay(working, dayOfMonth, dayOfWeek);
        currentHours = working.get(11);
        if (!isCurrent(hours, currentHours)) {
            working.add(11, getNext(hours, currentHours));
        }
        currentMinutes = working.get(12);
        if (!isCurrent(minutes, currentMinutes)) {
            working.add(12, getNext(minutes, currentMinutes));
        }
        long result = working.getTimeInMillis();
        if (result > currentTime) {
            return result;
        }
        throw new ArithmeticException("Unable to compute next scheduled exection time.");
    }

    protected static long doUpdateCurrentMonth(Calendar working, CronEntry month) throws MessageFormatException {
        int currentMonth = working.get(DAY_OF_MONTH) + HOURS;
        if (isCurrent(month, currentMonth)) {
            return 0;
        }
        working.add(DAY_OF_MONTH, getNext(month, currentMonth));
        resetToStartOfDay(working, HOURS);
        return working.getTimeInMillis();
    }

    protected static long doUpdateCurrentDay(Calendar working, CronEntry dayOfMonth, CronEntry dayOfWeek) throws MessageFormatException {
        int currentDayOfWeek = working.get(7) - 1;
        int currentDayOfMonth = working.get(NUMBER_TOKENS);
        if (isCurrent(dayOfWeek, currentDayOfWeek) && isCurrent(dayOfMonth, currentDayOfMonth)) {
            return 0;
        }
        int nextWeekDay = Integer.MAX_VALUE;
        int nextCalendarDay = Integer.MAX_VALUE;
        if (!isCurrent(dayOfWeek, currentDayOfWeek)) {
            nextWeekDay = getNext(dayOfWeek, currentDayOfWeek);
        }
        if (!isCurrent(dayOfMonth, currentDayOfMonth)) {
            nextCalendarDay = getNext(dayOfMonth, currentDayOfMonth);
        }
        if (nextWeekDay < nextCalendarDay) {
            working.add(7, nextWeekDay);
        } else {
            working.add(NUMBER_TOKENS, nextCalendarDay);
        }
        resetToStartOfDay(working, working.get(NUMBER_TOKENS));
        return working.getTimeInMillis();
    }

    public static void validate(String cronEntry) throws MessageFormatException {
        for (CronEntry e : buildCronEntries(tokenize(cronEntry))) {
            validate(e);
        }
    }

    static void validate(CronEntry entry) throws MessageFormatException {
        List<Integer> list = entry.currentWhen;
        if (list.isEmpty() || ((Integer) list.get(MINUTES)).intValue() < entry.start || ((Integer) list.get(list.size() - 1)).intValue() > entry.end) {
            throw new MessageFormatException("Invalid token: " + entry);
        }
    }

    static int getNext(CronEntry entry, int current) throws MessageFormatException {
        if (entry.currentWhen == null) {
            entry.currentWhen = calculateValues(entry);
        }
        List<Integer> list = entry.currentWhen;
        int next = -1;
        for (Integer i : list) {
            if (i.intValue() > current) {
                next = i.intValue();
                break;
            }
        }
        if (next != -1) {
            return next - current;
        }
        int result = ((entry.end + ((Integer) list.get(MINUTES)).intValue()) - entry.start) - current;
        if (entry.name.equals("DayOfWeek") || entry.name.equals("Month")) {
            return result + HOURS;
        }
        return result;
    }

    static boolean isCurrent(CronEntry entry, int current) throws MessageFormatException {
        return entry.currentWhen.contains(new Integer(current));
    }

    protected static void resetToStartOfDay(Calendar target, int day) {
        target.set(NUMBER_TOKENS, day);
        target.set(11, MINUTES);
        target.set(12, MINUTES);
        target.set(13, MINUTES);
    }

    static List<String> tokenize(String cron) throws IllegalArgumentException {
        StringTokenizer tokenize = new StringTokenizer(cron);
        List<String> result = new ArrayList();
        while (tokenize.hasMoreTokens()) {
            result.add(tokenize.nextToken());
        }
        if (result.size() == NUMBER_TOKENS) {
            return result;
        }
        throw new IllegalArgumentException("Not a valid cron entry - wrong number of tokens(" + result.size() + "): " + cron);
    }

    protected static List<Integer> calculateValues(CronEntry entry) {
        List<Integer> result = new ArrayList();
        int i;
        if (isAll(entry.token)) {
            for (i = entry.start; i <= entry.end; i += HOURS) {
                result.add(Integer.valueOf(i));
            }
        } else if (isAStep(entry.token)) {
            int denominator = getDenominator(entry.token);
            for (Integer i2 : calculateValues(new CronEntry(entry.name, getNumerator(entry.token), entry.start, entry.end))) {
                if (i2.intValue() % denominator == 0) {
                    result.add(i2);
                }
            }
        } else if (isAList(entry.token)) {
            StringTokenizer tokenizer = new StringTokenizer(entry.token, Stomp.COMMA);
            while (tokenizer.hasMoreTokens()) {
                result.addAll(calculateValues(new CronEntry(entry.name, tokenizer.nextToken(), entry.start, entry.end)));
            }
        } else if (isARange(entry.token)) {
            int index = entry.token.indexOf(45);
            int first = Integer.parseInt(entry.token.substring(MINUTES, index));
            int last = Integer.parseInt(entry.token.substring(index + HOURS));
            for (i = first; i <= last; i += HOURS) {
                result.add(Integer.valueOf(i));
            }
        } else {
            result.add(Integer.valueOf(Integer.parseInt(entry.token)));
        }
        Collections.sort(result);
        return result;
    }

    protected static boolean isARange(String token) {
        return token != null && token.indexOf(45) >= 0;
    }

    protected static boolean isAStep(String token) {
        return token != null && token.indexOf(47) >= 0;
    }

    protected static boolean isAList(String token) {
        return token != null && token.indexOf(44) >= 0;
    }

    protected static boolean isAll(String token) {
        return token != null && token.length() == HOURS && (token.charAt(MINUTES) == '*' || token.charAt(MINUTES) == '?');
    }

    protected static int getDenominator(String token) {
        return Integer.parseInt(token.substring(token.indexOf(47) + HOURS));
    }

    protected static String getNumerator(String token) {
        return token.substring(MINUTES, token.indexOf(47));
    }

    static List<CronEntry> buildCronEntries(List<String> tokens) {
        List<CronEntry> result = new ArrayList();
        CronEntry minutes = new CronEntry("Minutes", (String) tokens.get(MINUTES), MINUTES, 60);
        minutes.currentWhen = calculateValues(minutes);
        result.add(minutes);
        CronEntry hours = new CronEntry("Hours", (String) tokens.get(HOURS), MINUTES, 24);
        hours.currentWhen = calculateValues(hours);
        result.add(hours);
        CronEntry dayOfMonth = new CronEntry("DayOfMonth", (String) tokens.get(DAY_OF_MONTH), HOURS, 31);
        dayOfMonth.currentWhen = calculateValues(dayOfMonth);
        result.add(dayOfMonth);
        CronEntry month = new CronEntry("Month", (String) tokens.get(MONTH), HOURS, 12);
        month.currentWhen = calculateValues(month);
        result.add(month);
        CronEntry dayOfWeek = new CronEntry("DayOfWeek", (String) tokens.get(DAY_OF_WEEK), MINUTES, 6);
        dayOfWeek.currentWhen = calculateValues(dayOfWeek);
        result.add(dayOfWeek);
        return result;
    }
}
