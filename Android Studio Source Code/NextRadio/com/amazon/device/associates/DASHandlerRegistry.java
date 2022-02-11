package com.amazon.device.associates;

import java.util.HashMap;
import java.util.Map;

/* renamed from: com.amazon.device.associates.m */
final class DASHandlerRegistry implements ay {
    private static final Map<Class, Class> f1355a;

    DASHandlerRegistry() {
    }

    static {
        f1355a = new HashMap();
        f1355a.put(aw.class, aj.class);
        f1355a.put(bj.class, bk.class);
    }

    public <T> Class<T> m981a(Class<T> cls) {
        return (Class) f1355a.get(cls);
    }
}
