package com.amazon.device.associates;

/* compiled from: Logger */
class aa {
    private static bn f1157a;

    aa() {
    }

    static {
        f1157a = null;
    }

    static void m690a(bn bnVar) {
        f1157a = bnVar;
    }

    static boolean m692a() {
        if (f1157a == null) {
            return false;
        }
        return f1157a.m755a();
    }

    static boolean m694b() {
        if (f1157a == null) {
            return false;
        }
        return f1157a.m757b();
    }

    static void m691a(String str, String str2) {
        if (f1157a != null && m694b()) {
            f1157a.m756b(str, str2);
        }
    }

    static void m693b(String str, String str2) {
        if (f1157a != null && m692a()) {
            f1157a.m754a(str, str2);
        }
    }
}
