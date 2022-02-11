package org.apache.activemq.jndi;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public abstract class LazyCreateContext extends ReadOnlyContext {
    protected abstract Object createEntry(String str);

    public Object lookup(String name) throws NamingException {
        Object lookup;
        try {
            lookup = super.lookup(name);
        } catch (NameNotFoundException e) {
            lookup = createEntry(name);
            if (lookup == null) {
                throw e;
            }
            internalBind(name, lookup);
        }
        return lookup;
    }
}
