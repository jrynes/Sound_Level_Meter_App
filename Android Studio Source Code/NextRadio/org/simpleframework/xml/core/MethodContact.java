package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

class MethodContact implements Contact {
    private MethodPart get;
    private Class item;
    private Class[] items;
    private Annotation label;
    private String name;
    private Class owner;
    private MethodPart set;
    private Class type;

    public MethodContact(MethodPart get) {
        this(get, null);
    }

    public MethodContact(MethodPart get, MethodPart set) {
        this.owner = get.getDeclaringClass();
        this.label = get.getAnnotation();
        this.items = get.getDependents();
        this.item = get.getDependent();
        this.type = get.getType();
        this.name = get.getName();
        this.set = set;
        this.get = get;
    }

    public boolean isReadOnly() {
        return this.set == null;
    }

    public MethodPart getRead() {
        return this.get;
    }

    public MethodPart getWrite() {
        return this.set;
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public <T extends Annotation> T getAnnotation(Class<T> type) {
        T result = this.get.getAnnotation(type);
        if (type == this.label.annotationType()) {
            return this.label;
        }
        if (result != null || this.set == null) {
            return result;
        }
        return this.set.getAnnotation(type);
    }

    public Class getType() {
        return this.type;
    }

    public Class getDependent() {
        return this.item;
    }

    public Class[] getDependents() {
        return this.items;
    }

    public Class getDeclaringClass() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public void set(Object source, Object value) throws Exception {
        Class type = this.get.getMethod().getDeclaringClass();
        if (this.set == null) {
            throw new MethodException("Property '%s' is read only in %s", this.name, type);
        }
        this.set.getMethod().invoke(source, new Object[]{value});
    }

    public Object get(Object source) throws Exception {
        return this.get.getMethod().invoke(source, new Object[0]);
    }

    public String toString() {
        return String.format("method '%s'", new Object[]{this.name});
    }
}
