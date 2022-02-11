package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TreeModel implements Model {
    private LabelMap attributes;
    private Detail detail;
    private LabelMap elements;
    private Expression expression;
    private int index;
    private Label list;
    private ModelMap models;
    private String name;
    private OrderList order;
    private Policy policy;
    private String prefix;
    private Label text;

    private static class OrderList extends ArrayList<String> {
    }

    public TreeModel(Policy policy, Detail detail) {
        this(policy, detail, null, null, 1);
    }

    public TreeModel(Policy policy, Detail detail, String name, String prefix, int index) {
        this.attributes = new LabelMap(policy);
        this.elements = new LabelMap(policy);
        this.models = new ModelMap(detail);
        this.order = new OrderList();
        this.detail = detail;
        this.policy = policy;
        this.prefix = prefix;
        this.index = index;
        this.name = name;
    }

    public Model lookup(Expression path) {
        Model model = lookup(path.getFirst(), path.getIndex());
        if (!path.isPath()) {
            return model;
        }
        path = path.getPath(1, 0);
        if (model != null) {
            return model.lookup(path);
        }
        return model;
    }

    public void registerElement(String name) throws Exception {
        if (!this.order.contains(name)) {
            this.order.add(name);
        }
        this.elements.put(name, null);
    }

    public void registerAttribute(String name) throws Exception {
        this.attributes.put(name, null);
    }

    public void registerText(Label label) throws Exception {
        if (this.text != null) {
            throw new TextException("Duplicate text annotation on %s", label);
        } else {
            this.text = label;
        }
    }

    public void registerAttribute(Label label) throws Exception {
        String name = label.getName();
        if (this.attributes.get(name) != null) {
            throw new AttributeException("Duplicate annotation of name '%s' on %s", name, label);
        } else {
            this.attributes.put(name, label);
        }
    }

    public void registerElement(Label label) throws Exception {
        String name = label.getName();
        if (this.elements.get(name) != null) {
            throw new ElementException("Duplicate annotation of name '%s' on %s", name, label);
        }
        if (!this.order.contains(name)) {
            this.order.add(name);
        }
        if (label.isTextList()) {
            this.list = label;
        }
        this.elements.put(name, label);
    }

    public ModelMap getModels() throws Exception {
        return this.models.getModels();
    }

    public LabelMap getAttributes() throws Exception {
        return this.attributes.getLabels();
    }

    public LabelMap getElements() throws Exception {
        return this.elements.getLabels();
    }

    public boolean isModel(String name) {
        return this.models.containsKey(name);
    }

    public boolean isElement(String name) {
        return this.elements.containsKey(name);
    }

    public boolean isAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public Iterator<String> iterator() {
        List<String> list = new ArrayList();
        Iterator i$ = this.order.iterator();
        while (i$.hasNext()) {
            list.add((String) i$.next());
        }
        return list.iterator();
    }

    public void validate(Class type) throws Exception {
        validateExpressions(type);
        validateAttributes(type);
        validateElements(type);
        validateModels(type);
        validateText(type);
    }

    private void validateText(Class type) throws Exception {
        if (this.text == null) {
            return;
        }
        if (!this.elements.isEmpty()) {
            throw new TextException("Text annotation %s used with elements in %s", this.text, type);
        } else if (isComposite()) {
            throw new TextException("Text annotation %s can not be used with paths in %s", this.text, type);
        }
    }

    private void validateExpressions(Class type) throws Exception {
        Iterator i$ = this.elements.iterator();
        while (i$.hasNext()) {
            Label label = (Label) i$.next();
            if (label != null) {
                validateExpression(label);
            }
        }
        i$ = this.attributes.iterator();
        while (i$.hasNext()) {
            label = (Label) i$.next();
            if (label != null) {
                validateExpression(label);
            }
        }
        if (this.text != null) {
            validateExpression(this.text);
        }
    }

    private void validateExpression(Label label) throws Exception {
        Expression location = label.getExpression();
        if (this.expression != null) {
            if (!this.expression.getPath().equals(location.getPath())) {
                throw new PathException("Path '%s' does not match '%s' in %s", this.expression.getPath(), location.getPath(), this.detail);
            }
            return;
        }
        this.expression = location;
    }

    private void validateModels(Class type) throws Exception {
        Iterator it = this.models.iterator();
        while (it.hasNext()) {
            int count = 1;
            Iterator i$ = ((ModelList) it.next()).iterator();
            while (i$.hasNext()) {
                Model model = (Model) i$.next();
                if (model != null) {
                    String name = model.getName();
                    int count2 = count + 1;
                    if (model.getIndex() != count) {
                        throw new ElementException("Path section '%s[%s]' is out of sequence in %s", name, Integer.valueOf(model.getIndex()), type);
                    } else {
                        model.validate(type);
                        count = count2;
                    }
                }
            }
        }
    }

    private void validateAttributes(Class type) throws Exception {
        for (String name : this.attributes.keySet()) {
            if (((Label) this.attributes.get(name)) == null) {
                throw new AttributeException("Ordered attribute '%s' does not exist in %s", name, type);
            } else if (this.expression != null) {
                this.expression.getAttribute(name);
            }
        }
    }

    private void validateElements(Class type) throws Exception {
        for (String name : this.elements.keySet()) {
            ModelList list = (ModelList) this.models.get(name);
            Label label = (Label) this.elements.get(name);
            if (list == null && label == null) {
                throw new ElementException("Ordered element '%s' does not exist in %s", name, type);
            } else if (list != null && label != null && !list.isEmpty()) {
                throw new ElementException("Element '%s' is also a path name in %s", name, type);
            } else if (this.expression != null) {
                this.expression.getElement(name);
            }
        }
    }

    public void register(Label label) throws Exception {
        if (label.isAttribute()) {
            registerAttribute(label);
        } else if (label.isText()) {
            registerText(label);
        } else {
            registerElement(label);
        }
    }

    public Model lookup(String name, int index) {
        return this.models.lookup(name, index);
    }

    public Model register(String name, String prefix, int index) throws Exception {
        Model model = this.models.lookup(name, index);
        if (model == null) {
            return create(name, prefix, index);
        }
        return model;
    }

    private Model create(String name, String prefix, int index) throws Exception {
        Model model = new TreeModel(this.policy, this.detail, name, prefix, index);
        if (name != null) {
            this.models.register(name, model);
            this.order.add(name);
        }
        return model;
    }

    public boolean isComposite() {
        Iterator it = this.models.iterator();
        while (it.hasNext()) {
            Iterator i$ = ((ModelList) it.next()).iterator();
            while (i$.hasNext()) {
                Model model = (Model) i$.next();
                if (model != null && !model.isEmpty()) {
                    return true;
                }
            }
        }
        if (this.models.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        if (this.text == null && this.elements.isEmpty() && this.attributes.isEmpty() && !isComposite()) {
            return true;
        }
        return false;
    }

    public Label getText() {
        if (this.list != null) {
            return this.list;
        }
        return this.text;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public String toString() {
        return String.format("model '%s[%s]'", new Object[]{this.name, Integer.valueOf(this.index)});
    }
}
