package org.apache.activemq.util;

import java.util.Map;

public final class MapHelper {
    private MapHelper() {
    }

    public static String getString(Map map, String key) {
        Object answer = map.get(key);
        return answer != null ? answer.toString() : null;
    }

    public static int getInt(Map map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        return defaultValue;
    }
}
