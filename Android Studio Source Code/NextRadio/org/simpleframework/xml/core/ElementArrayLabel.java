package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.Style;

class ElementArrayLabel extends TemplateLabel {
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private String entry;
    private Format format;
    private ElementArray label;
    private String name;
    private Expression path;
    private boolean required;
    private Class type;

    public ElementArrayLabel(Contact contact, ElementArray label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.entry = label.entry();
        this.data = label.data();
        this.name = label.name();
        this.format = format;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        Contact contact = getContact();
        String entry = getEntry();
        if (this.type.isArray()) {
            return getConverter(context, entry);
        }
        throw new InstantiationException("Type is not an array %s for %s", this.type, contact);
    }

    private Converter getConverter(Context context, String name) throws Exception {
        Type entry = getDependent();
        Type type = getContact();
        if (context.isPrimitive(entry)) {
            return new PrimitiveArray(context, type, entry, name);
        }
        return new CompositeArray(context, type, entry, name);
    }

    public Object getEmpty(Context context) throws Exception {
        Factory factory = new ArrayFactory(context, new ClassType(this.type));
        if (this.label.empty()) {
            return null;
        }
        return factory.getInstance();
    }

    public String getEntry() throws Exception {
        Style style = this.format.getStyle();
        if (this.detail.isEmpty(this.entry)) {
            this.entry = this.detail.getEntry();
        }
        return style.getElement(this.entry);
    }

    public String getName() throws Exception {
        return this.format.getStyle().getElement(this.detail.getName());
    }

    public String getPath() throws Exception {
        return getExpression().getElement(getName());
    }

    public Expression getExpression() throws Exception {
        if (this.path == null) {
            this.path = this.detail.getExpression();
        }
        return this.path;
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public Type getDependent() {
        Class entry = this.type.getComponentType();
        if (entry == null) {
            return new ClassType(this.type);
        }
        return new ClassType(entry);
    }

    public Class getType() {
        return this.type;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.name;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isData() {
        return this.data;
    }

    public String toString() {
        return this.detail.toString();
    }
}
