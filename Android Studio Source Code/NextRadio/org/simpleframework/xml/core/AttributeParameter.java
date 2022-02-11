package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.stream.Format;

class AttributeParameter extends TemplateParameter {
    private final Contact contact;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final Label label;
    private final String name;
    private final String path;
    private final Class type;

    private static class Contact extends ParameterContact<Attribute> {
        public Contact(Attribute label, Constructor factory, int index) {
            super(label, factory, index);
        }

        public String getName() {
            return ((Attribute) this.label).name();
        }
    }

    public AttributeParameter(Constructor factory, Attribute value, Format format, int index) throws Exception {
        this.contact = new Contact(value, factory, index);
        this.label = new AttributeLabel(this.contact, value, format);
        this.expression = this.label.getExpression();
        this.path = this.label.getPath();
        this.type = this.label.getType();
        this.name = this.label.getName();
        this.key = this.label.getKey();
        this.index = index;
    }

    public Object getKey() {
        return this.key;
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public Class getType() {
        return this.type;
    }

    public Annotation getAnnotation() {
        return this.contact.getAnnotation();
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isRequired() {
        return this.label.isRequired();
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    public boolean isAttribute() {
        return true;
    }

    public String toString() {
        return this.contact.toString();
    }
}
