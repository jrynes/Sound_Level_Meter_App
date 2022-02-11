package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Style;

class ElementListLabel extends TemplateLabel {
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private String entry;
    private Format format;
    private boolean inline;
    private Class item;
    private ElementList label;
    private String name;
    private String override;
    private String path;
    private boolean required;
    private Class type;

    public ElementListLabel(Contact contact, ElementList label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.override = label.name();
        this.inline = label.inline();
        this.entry = label.entry();
        this.data = label.data();
        this.item = label.type();
        this.format = format;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        String entry = getEntry();
        if (this.label.inline()) {
            return getInlineConverter(context, entry);
        }
        return getConverter(context, entry);
    }

    private Converter getConverter(Context context, String name) throws Exception {
        Type item = getDependent();
        Type type = getContact();
        if (context.isPrimitive(item)) {
            return new PrimitiveList(context, type, item, name);
        }
        return new CompositeList(context, type, item, name);
    }

    private Converter getInlineConverter(Context context, String name) throws Exception {
        Type item = getDependent();
        Type type = getContact();
        if (context.isPrimitive(item)) {
            return new PrimitiveInlineList(context, type, item, name);
        }
        return new CompositeInlineList(context, type, item, name);
    }

    public Object getEmpty(Context context) throws Exception {
        Factory factory = new CollectionFactory(context, new ClassType(this.type));
        if (this.label.empty()) {
            return null;
        }
        return factory.getInstance();
    }

    public Type getDependent() throws Exception {
        Contact contact = getContact();
        if (this.item == Void.TYPE) {
            this.item = contact.getDependent();
        }
        if (this.item != null) {
            return new ClassType(this.item);
        }
        throw new ElementException("Unable to determine generic type for %s", contact);
    }

    public String getEntry() throws Exception {
        Style style = this.format.getStyle();
        if (this.detail.isEmpty(this.entry)) {
            this.entry = this.detail.getEntry();
        }
        return style.getElement(this.entry);
    }

    public String getName() throws Exception {
        if (this.name == null) {
            this.name = this.format.getStyle().getElement(this.detail.getName());
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
