package org.apache.activemq.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryFinder {
    private static ObjectFactory objectFactory;
    private final String path;

    public interface ObjectFactory {
        Object create(String str) throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException;
    }

    protected static class StandaloneObjectFactory implements ObjectFactory {
        final ConcurrentHashMap<String, Class> classMap;

        protected StandaloneObjectFactory() {
            this.classMap = new ConcurrentHashMap();
        }

        public Object create(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
            Class clazz = (Class) this.classMap.get(path);
            if (clazz == null) {
                clazz = loadClass(loadProperties(path));
                this.classMap.put(path, clazz);
            }
            return clazz.newInstance();
        }

        public static Class loadClass(Properties properties) throws ClassNotFoundException, IOException {
            String className = properties.getProperty(Name.LABEL);
            if (className == null) {
                throw new IOException("Expected property is missing: class");
            }
            Class clazz = null;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                try {
                    clazz = loader.loadClass(className);
                } catch (ClassNotFoundException e) {
                }
            }
            if (clazz == null) {
                return FactoryFinder.class.getClassLoader().loadClass(className);
            }
            return clazz;
        }

        public static Properties loadProperties(String uri) throws IOException {
            Throwable th;
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = StandaloneObjectFactory.class.getClassLoader();
            }
            InputStream in = classLoader.getResourceAsStream(uri);
            if (in == null) {
                in = FactoryFinder.class.getClassLoader().getResourceAsStream(uri);
                if (in == null) {
                    throw new IOException("Could not find factory class for resource: " + uri);
                }
            }
            BufferedInputStream reader = null;
            try {
                BufferedInputStream reader2 = new BufferedInputStream(in);
                try {
                    Properties properties = new Properties();
                    properties.load(reader2);
                    try {
                        reader2.close();
                    } catch (Exception e) {
                    }
                    return properties;
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                    try {
                        reader.close();
                    } catch (Exception e2) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                reader.close();
                throw th;
            }
        }
    }

    static {
        objectFactory = new StandaloneObjectFactory();
    }

    public static ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public static void setObjectFactory(ObjectFactory objectFactory) {
        objectFactory = objectFactory;
    }

    public FactoryFinder(String path) {
        this.path = path;
    }

    public Object newInstance(String key) throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        return objectFactory.create(this.path + key);
    }
}
