package org.simpleframework.xml.stream;

class InputStack extends Stack<InputNode> {
    public InputStack() {
        super(6);
    }

    public boolean isRelevant(InputNode value) {
        return contains(value) || isEmpty();
    }
}
