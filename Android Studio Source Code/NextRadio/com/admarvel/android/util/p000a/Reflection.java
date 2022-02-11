package com.admarvel.android.util.p000a;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.admarvel.android.util.a.c */
public class Reflection {

    /* renamed from: com.admarvel.android.util.a.c.a */
    public static class Reflection {
        private final Object f962a;
        private final String f963b;
        private Class<?> f964c;
        private List<Class<?>> f965d;
        private List<Object> f966e;
        private boolean f967f;
        private boolean f968g;

        public Reflection(Object obj, String str) {
            this.f962a = obj;
            this.f963b = str;
            this.f965d = new ArrayList();
            this.f966e = new ArrayList();
            this.f964c = obj != null ? obj.getClass() : null;
        }

        public Reflection m538a(Class<?> cls) {
            this.f968g = true;
            this.f964c = cls;
            return this;
        }

        public <T> Reflection m539a(Class<T> cls, T t) {
            this.f965d.add(cls);
            this.f966e.add(t);
            return this;
        }

        public Object m540a() {
            Method a = Reflection.m541a(this.f964c, this.f963b, (Class[]) this.f965d.toArray(new Class[this.f965d.size()]));
            if (this.f967f) {
                a.setAccessible(true);
            }
            Object[] toArray = this.f966e.toArray();
            return this.f968g ? a.invoke(null, toArray) : a.invoke(this.f962a, toArray);
        }
    }

    public static Method m541a(Class<?> cls, String str, Class<?>... clsArr) {
        Class superclass;
        while (superclass != null) {
            try {
                return superclass.getDeclaredMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                superclass = superclass.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
}
