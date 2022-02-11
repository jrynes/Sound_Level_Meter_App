package com.amazon.device.associates;

import java.util.HashMap;
import java.util.Map;

/* compiled from: CacheAgent */
public class al {
    private static final Map<Class<?>, ap<?>> f1185a;

    private al() {
    }

    static {
        f1185a = new HashMap();
    }

    public static void m753a() {
        f1185a.put(be.class, new be());
        f1185a.put(AsyncGetCategorySearchDetailsCacheTask.class, new AsyncGetCategorySearchDetailsCacheTask());
        f1185a.put(AsyncPopoverHtmlTemplateTask.class, new AsyncPopoverHtmlTemplateTask());
        f1185a.put(bv.class, new bv());
        for (ap j : f1185a.values()) {
            j.m778j();
        }
    }

    public static <T extends ap<? extends bl>> T m752a(Class<T> cls) {
        ap apVar = (ap) f1185a.get(cls);
        return apVar != null ? apVar : null;
    }
}
