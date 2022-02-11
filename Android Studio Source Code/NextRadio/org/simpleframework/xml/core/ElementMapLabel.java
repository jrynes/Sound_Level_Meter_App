package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Style;

class ElementMapLabel extends TemplateLabel {
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private Entry entry;
    private Format format;
    private boolean inline;
    private Class[] items;
    private ElementMap label;
    private String name;
    private String override;
    private String parent;
    private String path;
    private boolean required;
    private Class type;

    public ElementMapLabel(Contact contact, ElementMap label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.entry = new Entry(contact, label);
        this.required = label.required();
        this.type = contact.getType();
        this.inline = label.inline();
        this.override = label.name();
        this.data = label.data();
        this.format = format;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        Type type = getMap();
        if (this.label.inline()) {
            return new CompositeInlineMap(context, this.entry, type);
        }
        return new CompositeMap(context, this.entry, type);
    }

    public Object getEmpty(Context context) throws Exception {
        Factory factory = new MapFactory(context, new ClassType(this.type));
        if (this.label.empty()) {
            return null;
        }
        return factory.getInstance();
    }

    public Type getDependent() throws Exception {
        Contact contact = getContact();
        if (this.items == null) {
            this.items = contact.getDependents();
        }
        if (this.items == null) {
            throw new ElementException("Unable to determine type for %s", contact);
        } else if (this.items.length == 0) {
            return new ClassType(Object.class);
        } else {
            return new ClassType(this.items[0]);
        }
    }

    public String getEntry() throws Exception {
        Style style = this.format.getStyle();
        if (this.detail.isEmpty(this.parent)) {
            this.parent = this.detail.getEntry();
        }
        return style.getElement(this.parent);
    }

    public String getName() throws Exception {
        if (this.name == null) {
            Style style = this.format.getStyle();
            String value = this.entry.getEntry();
            if (!this.label.inline()) {
                value = this.detail.getName();
            }
            this.name = style.getElement(value);
        }
        return this.name;
    }

    public String getPath() throws Exception {
        if (this.path == null) {
            this.path = getExpression().getElement(getName());
        }
        return this.path;
    }

    public Expression getExpression() throws Exception {
        if (this.cache == null) {
            this.cache = this.detail.getExpression();
        }
        return this.cache;
    }

    private Type getMap() {
        return new ClassType(this.type);
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public Class getType() {
        return this.type;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.override;
    }

    public boolean isData() {
        return this.data;
    }

    public boolean isCollection() {
        return true;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isInline() {
        return this.inline;
    }

    public String toString() {
        return this.detail.toString();
    }
}
