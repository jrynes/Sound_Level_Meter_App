package org.simpleframework.xml.convert;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.util.ConcurrentCache;

class ScannerBuilder extends ConcurrentCache<Scanner> {

    private static class Entry extends ConcurrentCache<Annotation> implements Scanner {
        private final Class root;

        public Entry(Class root) {
            this.root = root;
        }

        public <T extends Annotation> T scan(Class<T> type) {
            if (!contains(type)) {
                T value = find(type);
                if (!(type == null || value == null)) {
                    put(type, value);
                }
            }
            return (Annotation) get(type);
        }

        private <T extends Annotation> T find(Class<T> label) {
            for (Class<?> type = this.root; type != null; type = type.getSuperclass()) {
                T value = type.getAnnotation(label);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }

    public Scanner build(Class<?> type) {
        Scanner scanner = (Scanner) get(type);
        if (scanner != null) {
            return scanner;
        }
        scanner = new Entry(type);
        put(type, scanner);
        return scanner;
    }
}
