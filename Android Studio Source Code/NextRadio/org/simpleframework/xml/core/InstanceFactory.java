package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class InstanceFactory {
    private final Cache<Constructor> cache;

    private class ClassInstance implements Instance {
        private Class type;
        private Object value;

        public ClassInstance(Class type) {
            this.type = type;
        }

        public Object getInstance() throws Exception {
            if (this.value == null) {
                this.value = InstanceFactory.this.getObject(this.type);
            }
            return this.value;
        }

        public Object setInstance(Object value) throws Exception {
            this.value = value;
            return value;
        }

        public Class getType() {
            return this.type;
        }

        public boolean isReference() {
            return false;
        }
    }

    private class ValueInstance implements Instance {
        private final Class type;
        private final Value value;

        public ValueInstance(Value value) {
            this.type = value.getType();
            this.value = value;
        }

        public Object getInstance() throws Exception {
            if (this.value.isReference()) {
                return this.value.getValue();
            }
            Object object = InstanceFactory.this.getObject(this.type);
            if (this.value == null) {
                return object;
            }
            this.value.setValue(object);
            return object;
        }

        public Object setInstance(Object object) {
            if (this.value != null) {
                this.value.setValue(object);
            }
            return object;
        }

        public boolean isReference() {
            return this.value.isReference();
        }

        public Class getType() {
            return this.type;
        }
    }

    public InstanceFactory() {
        this.cache = new ConcurrentCache();
    }

    public Instance getInstance(Value value) {
        return new ValueInstance(value);
    }

    public Instance getInstance(Class type) {
        return new ClassInstance(type);
    }

    protected Object getObject(Class type) throws Exception {
        Constructor method = (Constructor) this.cache.fetch(type);
        if (method == null) {
            method = type.getDeclaredConstructor(new Class[0]);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            this.cache.cache(type, method);
        }
        return method.newInstance(new Object[0]);
    }
}
