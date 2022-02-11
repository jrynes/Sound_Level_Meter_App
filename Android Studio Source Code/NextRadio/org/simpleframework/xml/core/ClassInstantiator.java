package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.List;

class ClassInstantiator implements Instantiator {
    private final List<Creator> creators;
    private final Detail detail;
    private final Creator primary;
    private final ParameterMap registry;

    public ClassInstantiator(List<Creator> creators, Creator primary, ParameterMap registry, Detail detail) {
        this.creators = creators;
        this.registry = registry;
        this.primary = primary;
        this.detail = detail;
    }

    public boolean isDefault() {
        if (this.creators.size() > 1) {
            return false;
        }
        if (this.primary != null) {
            return true;
        }
        return false;
    }

    public Object getInstance() throws Exception {
        return this.primary.getInstance();
    }

    public Object getInstance(Criteria criteria) throws Exception {
        Creator creator = getCreator(criteria);
        if (creator != null) {
            return creator.getInstance(criteria);
        }
        throw new PersistenceException("Constructor not matched for %s", this.detail);
    }

    private Creator getCreator(Criteria criteria) throws Exception {
        Creator result = this.primary;
        double max = 0.0d;
        for (Creator instantiator : this.creators) {
            double score = instantiator.getScore(criteria);
            if (score > max) {
                result = instantiator;
                max = score;
            }
        }
        return result;
    }

    public Parameter getParameter(String name) {
        return (Parameter) this.registry.get(name);
    }

    public List<Parameter> getParameters() {
        return this.registry.getAll();
    }

    public List<Creator> getCreators() {
        return new ArrayList(this.creators);
    }

    public String toString() {
        return String.format("creator for %s", new Object[]{this.detail});
    }
}
