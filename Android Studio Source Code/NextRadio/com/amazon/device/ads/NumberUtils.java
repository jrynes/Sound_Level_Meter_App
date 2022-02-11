package com.amazon.device.ads;

class NumberUtils {
    private NumberUtils() {
    }

    public static int parseInt(String string, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(string);
        } catch (NumberFormatException e) {
        }
        return value;
    }

    public static final long convertToMillisecondsFromNanoseconds(long nanoseconds) {
        return nanoseconds / 1000000;
    }

    public static final long convertToMillisecondsFromSeconds(long seconds) {
        return 1000 * seconds;
    }
}
