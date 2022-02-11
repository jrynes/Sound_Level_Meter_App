package com.admarvel.android.util.p000a;

import com.admarvel.android.util.p000a.Reflection.Reflection;

/* renamed from: com.admarvel.android.util.a.a */
public class MethodBuilderFactory {
    protected static MethodBuilderFactory f961a;

    static {
        f961a = new MethodBuilderFactory();
    }

    public static Reflection m529a(Object obj, String str) {
        return f961a.m530b(obj, str);
    }

    protected Reflection m530b(Object obj, String str) {
        return new Reflection(obj, str);
    }
}
