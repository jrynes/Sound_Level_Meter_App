package com.amazon.device.associates;

/* renamed from: com.amazon.device.associates.q */
class Log {
    private static boolean f1375a;
    private static boolean f1376b;

    Log() {
    }

    static {
        f1375a = true;
        f1376b = false;
    }

    public static boolean m1015a() {
        return f1375a;
    }

    public static void m1013a(String str, String str2) {
        if (Log.m1015a() && f1376b) {
            android.util.Log.d("AmazonMobileAssociates " + str, str2);
        }
    }

    public static void m1014a(String str, String str2, Object... objArr) {
        if (Log.m1015a() && f1376b) {
            android.util.Log.d("AmazonMobileAssociates " + str, String.format(str2, objArr));
        }
    }

    public static void m1016b(String str, String str2) {
        if (Log.m1015a()) {
            android.util.Log.e("AmazonMobileAssociates " + str, str2);
        }
    }

    public static void m1017b(String str, String str2, Object... objArr) {
        if (Log.m1015a()) {
            android.util.Log.e("AmazonMobileAssociates " + str, String.format(str2, objArr));
        }
    }

    public static void m1018c(String str, String str2) {
        if (Log.m1015a() && f1376b) {
            android.util.Log.i("AmazonMobileAssociates " + str, str2);
        }
    }

    public static void m1019c(String str, String str2, Object... objArr) {
        if (Log.m1015a() && f1376b) {
            android.util.Log.i("AmazonMobileAssociates " + str, String.format(str2, objArr));
        }
    }

    public static void m1020d(String str, String str2) {
        if (Log.m1015a()) {
            android.util.Log.w("AmazonMobileAssociates " + str, str2);
        }
    }

    public static void m1021d(String str, String str2, Object... objArr) {
        if (Log.m1015a()) {
            android.util.Log.w("AmazonMobileAssociates " + str, String.format(str2, objArr));
        }
    }
}
