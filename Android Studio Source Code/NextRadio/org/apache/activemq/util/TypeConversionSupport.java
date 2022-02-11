package org.apache.activemq.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.ActiveMQDestination;
import org.fusesource.hawtbuf.UTF8Buffer;

public final class TypeConversionSupport {
    private static final Map<ConversionKey, Converter> CONVERSION_MAP;
    private static final Converter IDENTITY_CONVERTER;

    public interface Converter {
        Object convert(Object obj);
    }

    static class 10 implements Converter {
        10() {
        }

        public Object convert(Object value) {
            return Long.valueOf(((Number) value).longValue());
        }
    }

    static class 11 implements Converter {
        11() {
        }

        public Object convert(Object value) {
            return Long.valueOf(((Date) value).getTime());
        }
    }

    static class 12 implements Converter {
        12() {
        }

        public Object convert(Object value) {
            return Integer.valueOf(((Number) value).intValue());
        }
    }

    static class 13 implements Converter {
        13() {
        }

        public Object convert(Object value) {
            return Short.valueOf(((Number) value).shortValue());
        }
    }

    static class 14 implements Converter {
        14() {
        }

        public Object convert(Object value) {
            return new Double(((Number) value).doubleValue());
        }
    }

    static class 15 implements Converter {
        15() {
        }

        public Object convert(Object value) {
            return ActiveMQDestination.createDestination((String) value, (byte) 1);
        }
    }

    static class 16 implements Converter {
        16() {
        }

        public Object convert(Object value) {
            try {
                return new URI(value.toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class 1 implements Converter {
        1() {
        }

        public Object convert(Object value) {
            return value;
        }
    }

    static class 2 implements Converter {
        2() {
        }

        public Object convert(Object value) {
            return value.toString();
        }
    }

    static class 3 implements Converter {
        3() {
        }

        public Object convert(Object value) {
            return Boolean.valueOf((String) value);
        }
    }

    static class 4 implements Converter {
        4() {
        }

        public Object convert(Object value) {
            return Byte.valueOf((String) value);
        }
    }

    static class 5 implements Converter {
        5() {
        }

        public Object convert(Object value) {
            return Short.valueOf((String) value);
        }
    }

    static class 6 implements Converter {
        6() {
        }

        public Object convert(Object value) {
            return Integer.valueOf((String) value);
        }
    }

    static class 7 implements Converter {
        7() {
        }

        public Object convert(Object value) {
            return Long.valueOf((String) value);
        }
    }

    static class 8 implements Converter {
        8() {
        }

        public Object convert(Object value) {
            return Float.valueOf((String) value);
        }
    }

    static class 9 implements Converter {
        9() {
        }

        public Object convert(Object value) {
            return Double.valueOf((String) value);
        }
    }

    private static class ConversionKey {
        final Class<?> from;
        final int hashCode;
        final Class<?> to;

        public ConversionKey(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
            this.hashCode = from.hashCode() ^ (to.hashCode() << 1);
        }

        public boolean equals(Object o) {
            ConversionKey x = (ConversionKey) o;
            return x.from == this.from && x.to == this.to;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    static {
        IDENTITY_CONVERTER = new 1();
        CONVERSION_MAP = new HashMap();
        Converter toStringConverter = new 2();
        CONVERSION_MAP.put(new ConversionKey(Boolean.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Byte.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Short.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Integer.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Long.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Float.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(Double.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(UTF8Buffer.class, String.class), toStringConverter);
        CONVERSION_MAP.put(new ConversionKey(String.class, Boolean.class), new 3());
        CONVERSION_MAP.put(new ConversionKey(String.class, Byte.class), new 4());
        CONVERSION_MAP.put(new ConversionKey(String.class, Short.class), new 5());
        CONVERSION_MAP.put(new ConversionKey(String.class, Integer.class), new 6());
        CONVERSION_MAP.put(new ConversionKey(String.class, Long.class), new 7());
        CONVERSION_MAP.put(new ConversionKey(String.class, Float.class), new 8());
        CONVERSION_MAP.put(new ConversionKey(String.class, Double.class), new 9());
        Converter longConverter = new 10();
        CONVERSION_MAP.put(new ConversionKey(Byte.class, Long.class), longConverter);
        CONVERSION_MAP.put(new ConversionKey(Short.class, Long.class), longConverter);
        CONVERSION_MAP.put(new ConversionKey(Integer.class, Long.class), longConverter);
        CONVERSION_MAP.put(new ConversionKey(Date.class, Long.class), new 11());
        Converter intConverter = new 12();
        CONVERSION_MAP.put(new ConversionKey(Byte.class, Integer.class), intConverter);
        CONVERSION_MAP.put(new ConversionKey(Short.class, Integer.class), intConverter);
        CONVERSION_MAP.put(new ConversionKey(Byte.class, Short.class), new 13());
        CONVERSION_MAP.put(new ConversionKey(Float.class, Double.class), new 14());
        CONVERSION_MAP.put(new ConversionKey(String.class, ActiveMQDestination.class), new 15());
        CONVERSION_MAP.put(new ConversionKey(String.class, URI.class), new 16());
    }

    private TypeConversionSupport() {
    }

    public static Object convert(Object value, Class<?> to) {
        if (value == null) {
            if (Boolean.TYPE.isAssignableFrom(to)) {
                return Boolean.FALSE;
            }
            return null;
        } else if (to.isInstance(value)) {
            return to.cast(value);
        } else {
            Converter c = lookupConverter(value.getClass(), to);
            if (c != null) {
                return c.convert(value);
            }
            return null;
        }
    }

    public static Converter lookupConverter(Class<?> from, Class<?> to) {
        if (from.isPrimitive()) {
            from = convertPrimitiveTypeToWrapperType(from);
        }
        if (to.isPrimitive()) {
            to = convertPrimitiveTypeToWrapperType(to);
        }
        if (from.equals(to)) {
            return IDENTITY_CONVERTER;
        }
        return (Converter) CONVERSION_MAP.get(new ConversionKey(from, to));
    }

    private static Class<?> convertPrimitiveTypeToWrapperType(Class<?> type) {
        Class<?> rc = type;
        if (!type.isPrimitive()) {
            return rc;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Double.TYPE) {
            return Double.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        return rc;
    }
}
