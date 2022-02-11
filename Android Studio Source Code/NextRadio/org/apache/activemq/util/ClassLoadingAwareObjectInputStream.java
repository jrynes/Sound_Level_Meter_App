package org.apache.activemq.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class ClassLoadingAwareObjectInputStream extends ObjectInputStream {
    private static final ClassLoader FALLBACK_CLASS_LOADER;
    private static final HashMap<String, Class> primClasses;
    private final ClassLoader inLoader;

    static {
        FALLBACK_CLASS_LOADER = ClassLoadingAwareObjectInputStream.class.getClassLoader();
        primClasses = new HashMap(8, 1.0f);
        primClasses.put("boolean", Boolean.TYPE);
        primClasses.put("byte", Byte.TYPE);
        primClasses.put("char", Character.TYPE);
        primClasses.put("short", Short.TYPE);
        primClasses.put("int", Integer.TYPE);
        primClasses.put("long", Long.TYPE);
        primClasses.put("float", Float.TYPE);
        primClasses.put("double", Double.TYPE);
        primClasses.put("void", Void.TYPE);
    }

    public ClassLoadingAwareObjectInputStream(InputStream in) throws IOException {
        super(in);
        this.inLoader = in.getClass().getClassLoader();
    }

    protected Class<?> resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return load(classDesc.getName(), cl, this.inLoader);
    }

    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        Class<?> proxyClass;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class[] cinterfaces = new Class[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            cinterfaces[i] = load(interfaces[i], cl);
        }
        try {
            proxyClass = Proxy.getProxyClass(cl, cinterfaces);
        } catch (IllegalArgumentException e) {
            try {
                proxyClass = Proxy.getProxyClass(this.inLoader, cinterfaces);
            } catch (IllegalArgumentException e2) {
                try {
                    proxyClass = Proxy.getProxyClass(FALLBACK_CLASS_LOADER, cinterfaces);
                } catch (IllegalArgumentException e3) {
                    throw new ClassNotFoundException(null, e);
                }
            }
        }
        return proxyClass;
    }

    private Class<?> load(String className, ClassLoader... cl) throws ClassNotFoundException {
        ClassLoader[] arr$ = cl;
        int i$ = 0;
        while (i$ < arr$.length) {
            try {
                return Class.forName(className, false, arr$[i$]);
            } catch (ClassNotFoundException e) {
                i$++;
            }
        }
        Class<?> clazz = (Class) primClasses.get(className);
        return clazz == null ? Class.forName(className, false, FALLBACK_CLASS_LOADER) : clazz;
    }
}
