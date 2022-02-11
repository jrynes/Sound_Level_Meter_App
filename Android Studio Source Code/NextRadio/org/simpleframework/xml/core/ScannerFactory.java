package org.simpleframework.xml.core;

import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class ScannerFactory {
    private final Cache<Scanner> cache;
    private final Support support;

    public ScannerFactory(Support support) {
        this.cache = new ConcurrentCache();
        this.support = support;
    }

    public Scanner getInstance(Class type) throws Exception {
        Scanner schema = (Scanner) this.cache.fetch(type);
        if (schema == null) {
            Detail detail = this.support.getDetail(type);
            if (this.support.isPrimitive(type)) {
                schema = new PrimitiveScanner(detail);
            } else {
                schema = new ObjectScanner(detail, this.support);
                if (schema.isPrimitive() && !this.support.isContainer(type)) {
                    schema = new DefaultScanner(detail, this.support);
                }
            }
            this.cache.cache(type, schema);
        }
        return schema;
    }
}
