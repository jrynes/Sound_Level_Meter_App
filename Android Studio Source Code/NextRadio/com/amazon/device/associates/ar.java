package com.amazon.device.associates;

import java.util.Collection;

/* compiled from: Validator */
class ar {
    private ar() {
    }

    public static void m782a(Object obj, String str) {
        if (obj == null || ((obj instanceof String) && ((String) obj).length() == 0)) {
            throw new IllegalArgumentException(str + " must not be null or empty");
        }
    }

    public static void m781a(int i, String str) {
        if (i <= 0) {
            throw new IllegalArgumentException(str + " must be positive");
        }
    }

    public static void m783a(Collection<String> collection, String str) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(str + " set must not be null or empty");
        }
        for (String str2 : collection) {
            if (str2 != null) {
                if (str2.length() == 0) {
                }
            }
            throw new IllegalArgumentException(str + " must not contains an empty string");
        }
    }
}
