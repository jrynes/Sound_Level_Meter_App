package org.simpleframework.xml.convert;

import org.simpleframework.xml.strategy.Value;

class Reference implements Value {
    private Class actual;
    private Object data;
    private Value value;

    public Reference(Value value, Object data, Class actual) {
        this.actual = actual;
        this.value = value;
        this.data = data;
    }

    public int getLength() {
        return 0;
    }

    public Class getType() {
        if (this.data != null) {
            return this.data.getClass();
        }
        return this.actual;
    }

    public Object getValue() {
        return this.data;
    }

    public boolean isReference() {
        return true;
    }

    public void setValue(Object data) {
        if (this.value != null) {
            this.value.setValue(data);
        }
        this.data = data;
    }
}
