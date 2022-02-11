package com.amazon.device.associates;

import java.util.HashMap;
import java.util.Map;

/* renamed from: com.amazon.device.associates.r */
final class SandboxHandlerRegistry implements ay {
    private static final Map<Class, Class> f1377a;

    SandboxHandlerRegistry() {
    }

    static {
        f1377a = new HashMap();
        f1377a.put(aw.class, at.class);
        f1377a.put(bj.class, SandboxResponseHandler.class);
    }

    public <T> Class<T> m1022a(Class<T> cls) {
        return (Class) f1377a.get(cls);
    }
}
