package org.simpleframework.xml.transform;

class ClassTransform implements Transform<Class> {
    private static final String BOOLEAN = "boolean";
    private static final String BYTE = "byte";
    private static final String CHARACTER = "char";
    private static final String DOUBLE = "double";
    private static final String FLOAT = "float";
    private static final String INTEGER = "int";
    private static final String LONG = "long";
    private static final String SHORT = "short";
    private static final String VOID = "void";

    ClassTransform() {
    }

    public Class read(String target) throws Exception {
        Class type = readPrimitive(target);
        if (type != null) {
            return type;
        }
        ClassLoader loader = getClassLoader();
        if (loader == null) {
            loader = getCallerClassLoader();
        }
        return loader.loadClass(target);
    }

    private Class readPrimitive(String target) throws Exception {
        if (target.equals(BYTE)) {
            return Byte.TYPE;
        }
        if (target.equals(SHORT)) {
            return Short.TYPE;
        }
        if (target.equals(INTEGER)) {
            return Integer.TYPE;
        }
        if (target.equals(LONG)) {
            return Long.TYPE;
        }
        if (target.equals(CHARACTER)) {
            return Character.TYPE;
        }
        if (target.equals(FLOAT)) {
            return Float.TYPE;
        }
        if (target.equals(DOUBLE)) {
            return Double.TYPE;
        }
        if (target.equals(BOOLEAN)) {
            return Boolean.TYPE;
        }
        if (target.equals(VOID)) {
            return Void.TYPE;
        }
        return null;
    }

    public String write(Class target) throws Exception {
        return target.getName();
    }

    private ClassLoader getCallerClassLoader() {
        return getClass().getClassLoader();
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
