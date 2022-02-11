package org.simpleframework.xml.convert;

import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class RegistryBinder {
    private final Cache<Class> cache;
    private final ConverterFactory factory;

    public RegistryBinder() {
        this.cache = new ConcurrentCache();
        this.factory = new ConverterFactory();
    }

    public Converter lookup(Class type) throws Exception {
        Class result = (Class) this.cache.fetch(type);
        if (result != null) {
            return create(result);
        }
        return null;
    }

    private Converter create(Class type) throws Exception {
        return this.factory.getInstance(type);
    }

    public void bind(Class type, Class converter) throws Exception {
        this.cache.cache(type, converter);
    }
}
