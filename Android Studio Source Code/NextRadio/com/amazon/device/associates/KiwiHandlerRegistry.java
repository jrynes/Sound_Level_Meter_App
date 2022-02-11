package com.amazon.device.associates;

import java.util.HashMap;
import java.util.Map;

/* renamed from: com.amazon.device.associates.z */
public class KiwiHandlerRegistry implements ay {
    private static final Map<Class, Class> f1395a;

    static {
        f1395a = new HashMap();
        f1395a.put(aw.class, ak.class);
        f1395a.put(bj.class, KiwiResponseHandler.class);
    }

    public <T> Class<T> m1035a(Class<T> cls) {
        return (Class) f1395a.get(cls);
    }
}
