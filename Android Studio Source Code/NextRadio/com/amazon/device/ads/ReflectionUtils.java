package com.amazon.device.ads;

class ReflectionUtils {
    ReflectionUtils() {
    }

    public boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
