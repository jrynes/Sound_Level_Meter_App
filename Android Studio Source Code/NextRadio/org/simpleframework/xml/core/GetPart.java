package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class GetPart implements MethodPart {
    private final Cache<Annotation> cache;
    private final Annotation label;
    private final Annotation[] list;
    private final Method method;
    private final String name;
    private final MethodType type;

    public GetPart(MethodName method, Annotation label, Annotation[] list) {
        this.cache = new ConcurrentCache();
        this.method = method.getMethod();
        this.name = method.getName();
        this.type = method.getType();
        this.label = label;
        this.list = list;
    }

    public String getName() {
        return this.name;
    }

    public Class getType() {
        return this.method.getReturnType();
    }

    public Class getDependent() {
        return Reflector.getReturnDependent(this.method);
    }

    public Class[] getDependents() {
        return Reflector.getReturnDependents(this.method);
    }

    public Class getDeclaringClass() {
        return this.method.getDeclaringClass();
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public <T extends Annotation> T getAnnotation(Class<T> type) {
        if (this.cache.isEmpty()) {
            for (Annotation entry : this.list) {
                this.cache.cache(entry.annotationType(), entry);
            }
        }
        return (Annotation) this.cache.fetch(type);
    }

    public MethodType getMethodType() {
        return this.type;
    }

    public Method getMethod() {
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
        return this.method;
    }

    public String toString() {
        return this.method.toGenericString();
    }
}
