package org.simpleframework.xml.stream;

class NodeReader {
    private final EventReader reader;
    private final InputStack stack;
    private final StringBuilder text;

    public NodeReader(EventReader reader) {
        this.text = new StringBuilder();
        this.stack = new InputStack();
        this.reader = reader;
    }

    public boolean isRoot(InputNode node) {
        return this.stack.bottom() == node;
    }

    public InputNode readRoot() throws Exception {
        InputNode node = null;
        if (this.stack.isEmpty()) {
            node = readElement(null);
            if (node == null) {
                throw new NodeException("Document has no root element");
            }
        }
        return node;
    }

    public InputNode readElement(InputNode from) throws Exception {
        if (!this.stack.isRelevant(from)) {
            return null;
        }
        EventNode event = this.reader.next();
        while (event != null) {
            if (event.isEnd()) {
                if (this.stack.pop() == from) {
                    return null;
                }
            } else if (event.isStart()) {
                return readStart(from, event);
            }
            event = this.reader.next();
        }
        return null;
    }

    public InputNode readElement(InputNode from, String name) throws Exception {
        if (!this.stack.isRelevant(from)) {
            return null;
        }
        EventNode event = this.reader.peek();
        while (event != null) {
            if (event.isText()) {
                fillText(from);
            } else if (event.isEnd()) {
                if (this.stack.top() == from) {
                    return null;
                }
                this.stack.pop();
            } else if (event.isStart()) {
                if (isName(event, name)) {
                    return readElement(from);
                }
                return null;
            }
            event = this.reader.next();
            event = this.reader.peek();
        }
        return null;
    }

    private InputNode readStart(InputNode from, EventNode event) throws Exception {
        InputElement input = new InputElement(from, this, event);
        if (this.text.length() > 0) {
            this.text.setLength(0);
        }
        if (event.isStart()) {
            return (InputNode) this.stack.push(input);
        }
        return input;
    }

    private boolean isName(EventNode node, String name) {
        String local = node.getName();
        if (local == null) {
            return false;
        }
        return local.equals(name);
    }

    public String readValue(InputNode from) throws Exception {
        if (!this.stack.isRelevant(from)) {
            return null;
        }
        if (this.text.length() <= 0 && this.reader.peek().isEnd()) {
            if (this.stack.top() == from) {
                return null;
            }
            this.stack.pop();
            this.reader.next();
        }
        return readText(from);
    }

    private String readText(InputNode from) throws Exception {
        EventNode event = this.reader.peek();
        while (this.stack.top() == from && event.isText()) {
            fillText(from);
            event = this.reader.next();
            event = this.reader.peek();
        }
        return readBuffer(from);
    }

    private String readBuffer(InputNode from) throws Exception {
        if (this.text.length() <= 0) {
            return null;
        }
        String value = this.text.toString();
        this.text.setLength(0);
        return value;
    }

    private void fillText(InputNode from) throws Exception {
        EventNode event = this.reader.peek();
        if (event.isText()) {
            this.text.append(event.getValue());
        }
    }

    public boolean isEmpty(InputNode from) throws Exception {
        if (this.stack.top() == from && this.reader.peek().isEnd()) {
            return true;
        }
        return false;
    }

    public void skipElement(InputNode from) throws Exception {
        do {
        } while (readElement(from) != null);
    }
}
