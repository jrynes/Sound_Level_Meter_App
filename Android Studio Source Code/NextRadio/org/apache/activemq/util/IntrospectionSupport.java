package org.apache.activemq.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.SSLServerSocket;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.TypeConversionSupport.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IntrospectionSupport {
    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(IntrospectionSupport.class);
    }

    private IntrospectionSupport() {
    }

    public static boolean getProperties(Object target, Map props, String optionPrefix) {
        boolean rc = false;
        if (target == null) {
            throw new IllegalArgumentException("target was null.");
        } else if (props == null) {
            throw new IllegalArgumentException("props was null.");
        } else {
            if (optionPrefix == null) {
                optionPrefix = Stomp.EMPTY;
            }
            for (Method method : target.getClass().getMethods()) {
                String name = method.getName();
                Class<?> type = method.getReturnType();
                Class<?>[] params = method.getParameterTypes();
                if ((name.startsWith("is") || name.startsWith("get")) && params.length == 0 && type != null) {
                    try {
                        Object value = method.invoke(target, new Object[0]);
                        if (value != null) {
                            String strValue = convertToString(value, type);
                            if (strValue != null) {
                                if (name.startsWith("get")) {
                                    name = name.substring(3, 4).toLowerCase(Locale.ENGLISH) + name.substring(4);
                                } else {
                                    name = name.substring(2, 3).toLowerCase(Locale.ENGLISH) + name.substring(3);
                                }
                                props.put(optionPrefix + name, strValue);
                                rc = true;
                            }
                        }
                    } catch (Throwable th) {
                    }
                }
            }
            return rc;
        }
    }

    public static boolean setProperties(Object target, Map<String, ?> props, String optionPrefix) {
        boolean rc = false;
        if (target == null) {
            throw new IllegalArgumentException("target was null.");
        } else if (props == null) {
            throw new IllegalArgumentException("props was null.");
        } else {
            Iterator<String> iter = props.keySet().iterator();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                if (name.startsWith(optionPrefix)) {
                    if (setProperty(target, name.substring(optionPrefix.length()), props.get(name))) {
                        iter.remove();
                        rc = true;
                    }
                }
            }
            return rc;
        }
    }

    public static Map<String, Object> extractProperties(Map props, String optionPrefix) {
        if (props == null) {
            throw new IllegalArgumentException("props was null.");
        }
        HashMap<String, Object> rc = new HashMap(props.size());
        Iterator<?> iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            if (name.startsWith(optionPrefix)) {
                rc.put(name.substring(optionPrefix.length()), props.get(name));
                iter.remove();
            }
        }
        return rc;
    }

    public static boolean setProperties(Object target, Map props) {
        boolean rc = false;
        if (target == null) {
            throw new IllegalArgumentException("target was null.");
        } else if (props == null) {
            throw new IllegalArgumentException("props was null.");
        } else {
            Iterator<?> iter = props.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<?, ?> entry = (Entry) iter.next();
                if (setProperty(target, (String) entry.getKey(), entry.getValue())) {
                    iter.remove();
                    rc = true;
                }
            }
            return rc;
        }
    }

    public static boolean setProperty(Object target, String name, Object value) {
        try {
            Class<?> clazz = target.getClass();
            if (target instanceof SSLServerSocket) {
                clazz = SSLServerSocket.class;
            }
            Method setter = findSetterMethod(clazz, name);
            if (setter == null) {
                return false;
            }
            if (value == null || value.getClass() == setter.getParameterTypes()[0]) {
                setter.invoke(target, new Object[]{value});
            } else {
                setter.invoke(target, new Object[]{convert(value, setter.getParameterTypes()[0])});
            }
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static Object convert(Object value, Class to) {
        if (value == null) {
            if (Boolean.TYPE.isAssignableFrom(to)) {
                return Boolean.FALSE;
            }
            return null;
        } else if (to.isAssignableFrom(value.getClass())) {
            return to.cast(value);
        } else {
            if (to.isAssignableFrom(String[].class)) {
                return StringArrayConverter.convertToStringArray(value);
            }
            if (value.getClass().equals(String.class) && to.equals(List.class)) {
                Object answer = StringToListOfActiveMQDestinationConverter.convertToActiveMQDestination(value);
                if (answer != null) {
                    return answer;
                }
            }
            Converter converter = TypeConversionSupport.lookupConverter(value.getClass(), to);
            if (converter != null) {
                return converter.convert(value);
            }
            throw new IllegalArgumentException("Cannot convert from " + value.getClass() + " to " + to + " with value " + value);
        }
    }

    public static String convertToString(Object value, Class to) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (String[].class.isInstance(value)) {
            return StringArrayConverter.convertToString((String[]) value);
        }
        if (List.class.isInstance(value)) {
            String answer = StringToListOfActiveMQDestinationConverter.convertFromActiveMQDestination(value);
            if (answer != null) {
                return answer;
            }
        }
        Converter converter = TypeConversionSupport.lookupConverter(value.getClass(), String.class);
        if (converter != null) {
            return (String) converter.convert(value);
        }
        throw new IllegalArgumentException("Cannot convert from " + value.getClass() + " to " + to + " with value " + value);
    }

    private static Method findSetterMethod(Class clazz, String name) {
        name = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method method : clazz.getMethods()) {
            Class<?>[] params = method.getParameterTypes();
            if (method.getName().equals(name) && params.length == 1) {
                return method;
            }
        }
        return null;
    }

    public static String toString(Object target) {
        return toString(target, Object.class, null);
    }

    public static String toString(Object target, Class stopClass) {
        return toString(target, stopClass, null);
    }

    public static String toString(Object target, Class stopClass, Map<String, Object> overrideFields) {
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        addFields(target, target.getClass(), stopClass, map);
        if (overrideFields != null) {
            for (String key : overrideFields.keySet()) {
                map.put(key, overrideFields.get(key));
            }
        }
        StringBuffer buffer = new StringBuffer(simpleName(target.getClass()));
        buffer.append(" {");
        boolean first = true;
        for (Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            Object key2 = entry.getKey();
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(key2);
            buffer.append(" = ");
            appendToString(buffer, key2, value);
        }
        buffer.append("}");
        return buffer.toString();
    }

    protected static void appendToString(StringBuffer buffer, Object key, Object value) {
        if (value instanceof ActiveMQDestination) {
            buffer.append(((ActiveMQDestination) value).getQualifiedName());
        } else if (key.toString().toLowerCase(Locale.ENGLISH).contains("password")) {
            buffer.append("*****");
        } else {
            buffer.append(value);
        }
    }

    public static String simpleName(Class clazz) {
        String name = clazz.getName();
        int p = name.lastIndexOf(ActiveMQDestination.PATH_SEPERATOR);
        if (p >= 0) {
            return name.substring(p + 1);
        }
        return name;
    }

    private static void addFields(Object target, Class startClass, Class<Object> stopClass, LinkedHashMap<String, Object> map) {
        if (startClass != stopClass) {
            addFields(target, startClass.getSuperclass(), stopClass, map);
        }
        for (Field field : startClass.getDeclaredFields()) {
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) || Modifier.isPrivate(field.getModifiers()))) {
                try {
                    field.setAccessible(true);
                    Object o = field.get(target);
                    if (o != null && o.getClass().isArray()) {
                        try {
                            o = Arrays.asList((Object[]) o);
                        } catch (Throwable th) {
                        }
                    }
                    map.put(field.getName(), o);
                } catch (Throwable e) {
                    LOG.debug("Error getting field " + field + " on class " + startClass + ". This exception is ignored.", e);
                }
            }
        }
    }
}
