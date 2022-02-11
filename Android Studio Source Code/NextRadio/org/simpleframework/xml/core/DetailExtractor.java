package org.simpleframework.xml.core;

import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class DetailExtractor {
    private final Cache<Detail> details;
    private final Cache<ContactList> fields;
    private final Cache<ContactList> methods;
    private final DefaultType override;
    private final Support support;

    public DetailExtractor(Support support) {
        this(support, null);
    }

    public DetailExtractor(Support support, DefaultType override) {
        this.methods = new ConcurrentCache();
        this.fields = new ConcurrentCache();
        this.details = new ConcurrentCache();
        this.override = override;
        this.support = support;
    }

    public Detail getDetail(Class type) {
        Detail detail = (Detail) this.details.fetch(type);
        if (detail != null) {
            return detail;
        }
        detail = new DetailScanner(type, this.override);
        this.details.cache(type, detail);
        return detail;
    }

    public ContactList getFields(Class type) throws Exception {
        ContactList list = (ContactList) this.fields.fetch(type);
        if (list != null) {
            return list;
        }
        Detail detail = getDetail(type);
        if (detail != null) {
            return getFields(type, detail);
        }
        return list;
    }

    private ContactList getFields(Class type, Detail detail) throws Exception {
        ContactList list = new FieldScanner(detail, this.support);
        if (detail != null) {
            this.fields.cache(type, list);
        }
        return list;
    }

    public ContactList getMethods(Class type) throws Exception {
        ContactList list = (ContactList) this.methods.fetch(type);
        if (list != null) {
            return list;
        }
        Detail detail = getDetail(type);
        if (detail != null) {
            return getMethods(type, detail);
        }
        return list;
    }

    private ContactList getMethods(Class type, Detail detail) throws Exception {
        ContactList list = new MethodScanner(detail, this.support);
        if (detail != null) {
            this.methods.cache(type, list);
        }
        return list;
    }
}
