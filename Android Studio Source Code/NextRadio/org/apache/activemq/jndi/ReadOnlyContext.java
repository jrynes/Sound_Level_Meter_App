package org.apache.activemq.jndi;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import org.apache.activemq.transport.stomp.Stomp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadOnlyContext implements Context, Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Logger LOG;
    protected static final NameParser NAME_PARSER;
    public static final String SEPARATOR = "/";
    private static final long serialVersionUID = -5754338187296859149L;
    protected final Map<String, Object> bindings;
    protected final Hashtable<String, Object> environment;
    private boolean frozen;
    private String nameInNamespace;
    protected final Map<String, Object> treeBindings;

    private abstract class LocalNamingEnumeration implements NamingEnumeration {
        private final Iterator i;

        private LocalNamingEnumeration() {
            this.i = ReadOnlyContext.this.bindings.entrySet().iterator();
        }

        public boolean hasMore() throws NamingException {
            return this.i.hasNext();
        }

        public boolean hasMoreElements() {
            return this.i.hasNext();
        }

        protected Entry getNext() {
            return (Entry) this.i.next();
        }

        public void close() throws NamingException {
        }
    }

    private class ListBindingEnumeration extends LocalNamingEnumeration {
        ListBindingEnumeration() {
            super(null);
        }

        public Object next() throws NamingException {
            return nextElement();
        }

        public Object nextElement() {
            Entry entry = getNext();
            return new Binding((String) entry.getKey(), entry.getValue());
        }
    }

    private class ListEnumeration extends LocalNamingEnumeration {
        ListEnumeration() {
            super(null);
        }

        public Object next() throws NamingException {
            return nextElement();
        }

        public Object nextElement() {
            Entry entry = getNext();
            return new NameClassPair((String) entry.getKey(), entry.getValue().getClass().getName());
        }
    }

    static {
        $assertionsDisabled = !ReadOnlyContext.class.desiredAssertionStatus() ? true : $assertionsDisabled;
        LOG = LoggerFactory.getLogger(ReadOnlyContext.class);
        NAME_PARSER = new NameParserImpl();
    }

    public ReadOnlyContext() {
        this.nameInNamespace = Stomp.EMPTY;
        this.environment = new Hashtable();
        this.bindings = new HashMap();
        this.treeBindings = new HashMap();
    }

    public ReadOnlyContext(Hashtable env) {
        this.nameInNamespace = Stomp.EMPTY;
        if (env == null) {
            this.environment = new Hashtable();
        } else {
            this.environment = new Hashtable(env);
        }
        this.bindings = Collections.EMPTY_MAP;
        this.treeBindings = Collections.EMPTY_MAP;
    }

    public ReadOnlyContext(Hashtable environment, Map<String, Object> bindings) {
        this.nameInNamespace = Stomp.EMPTY;
        if (environment == null) {
            this.environment = new Hashtable();
        } else {
            this.environment = new Hashtable(environment);
        }
        this.bindings = new HashMap();
        this.treeBindings = new HashMap();
        if (bindings != null) {
            for (Entry<String, Object> binding : bindings.entrySet()) {
                try {
                    internalBind((String) binding.getKey(), binding.getValue());
                } catch (Throwable e) {
                    LOG.error("Failed to bind " + ((String) binding.getKey()) + "=" + binding.getValue(), e);
                }
            }
        }
        this.frozen = true;
    }

    public ReadOnlyContext(Hashtable environment, Map bindings, String nameInNamespace) {
        this(environment, bindings);
        this.nameInNamespace = nameInNamespace;
    }

    protected ReadOnlyContext(ReadOnlyContext clone, Hashtable env) {
        this.nameInNamespace = Stomp.EMPTY;
        this.bindings = clone.bindings;
        this.treeBindings = clone.treeBindings;
        this.environment = new Hashtable(env);
    }

    protected ReadOnlyContext(ReadOnlyContext clone, Hashtable<String, Object> env, String nameInNamespace) {
        this(clone, (Hashtable) env);
        this.nameInNamespace = nameInNamespace;
    }

    public void freeze() {
        this.frozen = true;
    }

    boolean isFrozen() {
        return this.frozen;
    }

    protected Map<String, Object> internalBind(String name, Object value) throws NamingException {
        if (!$assertionsDisabled && (name == null || name.length() <= 0)) {
            throw new AssertionError();
        } else if ($assertionsDisabled || !this.frozen) {
            Map<String, Object> newBindings = new HashMap();
            int pos = name.indexOf(47);
            if (pos != -1) {
                String segment = name.substring(0, pos);
                if (!$assertionsDisabled && segment == null) {
                    throw new AssertionError();
                } else if ($assertionsDisabled || !segment.equals(Stomp.EMPTY)) {
                    ReadOnlyContext o = this.treeBindings.get(segment);
                    if (o == null) {
                        o = newContext();
                        this.treeBindings.put(segment, o);
                        this.bindings.put(segment, o);
                        newBindings.put(segment, o);
                    } else if (!(o instanceof ReadOnlyContext)) {
                        throw new NamingException("Something already bound where a subcontext should go");
                    }
                    for (Entry entry : o.internalBind(name.substring(pos + 1), value).entrySet()) {
                        String subName = segment + SEPARATOR + ((String) entry.getKey());
                        Object bound = entry.getValue();
                        this.treeBindings.put(subName, bound);
                        newBindings.put(subName, bound);
                    }
                } else {
                    throw new AssertionError();
                }
            } else if (this.treeBindings.put(name, value) != null) {
                throw new NamingException("Something already bound at " + name);
            } else {
                this.bindings.put(name, value);
                newBindings.put(name, value);
            }
            return newBindings;
        } else {
            throw new AssertionError();
        }
    }

    protected ReadOnlyContext newContext() {
        return new ReadOnlyContext();
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return this.environment.put(propName, propVal);
    }

    public Hashtable<String, Object> getEnvironment() throws NamingException {
        return (Hashtable) this.environment.clone();
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        return this.environment.remove(propName);
    }

    public Object lookup(String name) throws NamingException {
        if (name.length() == 0) {
            return this;
        }
        ReadOnlyContext result = this.treeBindings.get(name);
        if (result == null) {
            result = this.bindings.get(name);
        }
        if (result == null) {
            int pos = name.indexOf(58);
            if (pos > 0) {
                String scheme = name.substring(0, pos);
                Context ctx = NamingManager.getURLContext(scheme, this.environment);
                if (ctx != null) {
                    return ctx.lookup(name);
                }
                throw new NamingException("scheme " + scheme + " not recognized");
            }
            CompositeName path = new CompositeName(name);
            if (path.size() == 0) {
                return this;
            }
            ReadOnlyContext obj = this.bindings.get(path.get(0));
            if (obj == null) {
                throw new NameNotFoundException(name);
            }
            if ((obj instanceof Context) && path.size() > 1) {
                obj = ((Context) obj).lookup(path.getSuffix(1));
            }
            return obj;
        }
        if (result instanceof LinkRef) {
            result = lookup(((LinkRef) result).getLinkName());
        }
        if (result instanceof Reference) {
            try {
                result = NamingManager.getObjectInstance(result, null, null, this.environment);
            } catch (NamingException e) {
                throw e;
            } catch (Exception e2) {
                throw ((NamingException) new NamingException("could not look up : " + name).initCause(e2));
            }
        }
        if (result instanceof ReadOnlyContext) {
            String prefix = getNameInNamespace();
            if (prefix.length() > 0) {
                prefix = prefix + SEPARATOR;
            }
            result = new ReadOnlyContext(result, this.environment, prefix + name);
        }
        return result;
    }

    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    public Object lookupLink(String name) throws NamingException {
        return lookup(name);
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        Name result = (Name) prefix.clone();
        result.addAll(name);
        return result;
    }

    public String composeName(String name, String prefix) throws NamingException {
        CompositeName result = new CompositeName(prefix);
        result.addAll(new CompositeName(name));
        return result.toString();
    }

    public NamingEnumeration list(String name) throws NamingException {
        ReadOnlyContext o = lookup(name);
        if (o == this) {
            return new ListEnumeration();
        }
        if (o instanceof Context) {
            return ((Context) o).list(Stomp.EMPTY);
        }
        throw new NotContextException();
    }

    public NamingEnumeration listBindings(String name) throws NamingException {
        ReadOnlyContext o = lookup(name);
        if (o == this) {
            return new ListBindingEnumeration();
        }
        if (o instanceof Context) {
            return ((Context) o).listBindings(Stomp.EMPTY);
        }
        throw new NotContextException();
    }

    public Object lookupLink(Name name) throws NamingException {
        return lookupLink(name.toString());
    }

    public NamingEnumeration list(Name name) throws NamingException {
        return list(name.toString());
    }

    public NamingEnumeration listBindings(Name name) throws NamingException {
        return listBindings(name.toString());
    }

    public void bind(Name name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void bind(String name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void close() throws NamingException {
    }

    public Context createSubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public Context createSubcontext(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void destroySubcontext(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public String getNameInNamespace() throws NamingException {
        return this.nameInNamespace;
    }

    public NameParser getNameParser(Name name) throws NamingException {
        return NAME_PARSER;
    }

    public NameParser getNameParser(String name) throws NamingException {
        return NAME_PARSER;
    }

    public void rebind(Name name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rebind(String name, Object obj) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rename(Name oldName, Name newName) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void rename(String oldName, String newName) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void unbind(Name name) throws NamingException {
        throw new OperationNotSupportedException();
    }

    public void unbind(String name) throws NamingException {
        throw new OperationNotSupportedException();
    }
}
