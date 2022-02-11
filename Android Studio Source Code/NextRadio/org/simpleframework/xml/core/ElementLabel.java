package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Format;

class ElementLabel extends TemplateLabel {
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private Class expect;
    private Format format;
    private Element label;
    private String name;
    private String override;
    private String path;
    private boolean required;
    private Class type;

    public ElementLabel(Contact contact, Element label, Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.override = label.name();
        this.expect = label.type();
        this.data = label.data();
        this.format = format;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Type getType(Class type) {
        Type contact = getContact();
        return this.expect == Void.TYPE ? contact : new OverrideType(contact, this.expect);
    }

    public Converter getConverter(Context context) throws Exception {
        Type type = getContact();
        if (context.isPrimitive(type)) {
            return new Primitive(context, type);
        }
        if (this.expect == Void.TYPE) {
            return new Composite(context, type);
        }
        return new Composite(context, type, this.expect);
    }

    public Object getEmpty(Context context) {
        return null;
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

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.override;
    }

    public Class getType() {
        if (this.expect == Void.TYPE) {
            return this.type;
        }
        return this.expect;
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
