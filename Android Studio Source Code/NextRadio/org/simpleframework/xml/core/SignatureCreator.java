package org.simpleframework.xml.core;

import java.util.List;

class SignatureCreator implements Creator {
    private final List<Parameter> list;
    private final Signature signature;
    private final Class type;

    public SignatureCreator(Signature signature) {
        this.type = signature.getType();
        this.list = signature.getAll();
        this.signature = signature;
    }

    public Class getType() {
        return this.type;
    }

    public Signature getSignature() {
        return this.signature;
    }

    public Object getInstance() throws Exception {
        return this.signature.create();
    }

    public Object getInstance(Criteria criteria) throws Exception {
        Object[] values = this.list.toArray();
        for (int i = 0; i < this.list.size(); i++) {
            values[i] = getVariable(criteria, i);
        }
        return this.signature.create(values);
    }

    private Object getVariable(Criteria criteria, int index) throws Exception {
        Variable variable = criteria.remove(((Parameter) this.list.get(index)).getKey());
        if (variable != null) {
            return variable.getValue();
        }
        return null;
    }

    public double getScore(Criteria criteria) throws Exception {
        Signature match = this.signature.copy();
        for (Object key : criteria) {
            Parameter parameter = match.get(key);
            Variable label = criteria.get(key);
            Contact contact = label.getContact();
            if (parameter != null && !Support.isAssignable(label.getValue().getClass(), parameter.getType())) {
                return -1.0d;
            }
            if (contact.isReadOnly() && parameter == null) {
                return -1.0d;
            }
        }
        return getPercentage(criteria);
    }

    private double getPercentage(Criteria criteria) throws Exception {
        double score = 0.0d;
        for (Parameter value : this.list) {
            if (criteria.get(value.getKey()) != null) {
                score += 1.0d;
            } else if (value.isRequired()) {
                return -1.0d;
            } else {
                if (value.isPrimitive()) {
                    return -1.0d;
                }
            }
        }
        return getAdjustment(score);
    }

    private double getAdjustment(double score) {
        double adjustment = ((double) this.list.size()) / 1000.0d;
        if (score > 0.0d) {
            return (score / ((double) this.list.size())) + adjustment;
        }
        return score / ((double) this.list.size());
    }

    public String toString() {
        return this.signature.toString();
    }
}
