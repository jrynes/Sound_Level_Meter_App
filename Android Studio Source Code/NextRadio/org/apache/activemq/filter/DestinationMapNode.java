package org.apache.activemq.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DestinationMapNode implements DestinationNode {
    protected static final String ANY_CHILD = "*";
    protected static final String ANY_DESCENDENT = ">";
    private Map<String, DestinationNode> childNodes;
    private DestinationMapNode parent;
    private String path;
    private int pathLength;
    private List<Object> values;

    public DestinationMapNode(DestinationMapNode parent) {
        this.values = new ArrayList();
        this.childNodes = new HashMap();
        this.path = "Root";
        this.parent = parent;
        if (parent == null) {
            this.pathLength = 0;
        } else {
            this.pathLength = parent.pathLength + 1;
        }
    }

    public DestinationNode getChild(String path) {
        return (DestinationNode) this.childNodes.get(path);
    }

    public Collection<DestinationNode> getChildren() {
        return this.childNodes.values();
    }

    public int getChildCount() {
        return this.childNodes.size();
    }

    public DestinationMapNode getChildOrCreate(String path) {
        DestinationMapNode answer = (DestinationMapNode) this.childNodes.get(path);
        if (answer != null) {
            return answer;
        }
        answer = createChildNode();
        answer.path = path;
        this.childNodes.put(path, answer);
        return answer;
    }

    public List getValues() {
        return this.values;
    }

    public List removeValues() {
        ArrayList v = new ArrayList(this.values);
        this.values.clear();
        pruneIfEmpty();
        return v;
    }

    public Set removeDesendentValues() {
        Set answer = new HashSet();
        removeDesendentValues(answer);
        return answer;
    }

    protected void removeDesendentValues(Set answer) {
        answer.addAll(removeValues());
    }

    public Set getDesendentValues() {
        Set answer = new HashSet();
        appendDescendantValues(answer);
        return answer;
    }

    public void add(String[] paths, int idx, Object value) {
        if (idx >= paths.length) {
            this.values.add(value);
        } else {
            getChildOrCreate(paths[idx]).add(paths, idx + 1, value);
        }
    }

    public void remove(String[] paths, int idx, Object value) {
        if (idx >= paths.length) {
            this.values.remove(value);
            pruneIfEmpty();
            return;
        }
        getChildOrCreate(paths[idx]).remove(paths, idx + 1, value);
    }

    public void removeAll(Set<DestinationNode> answer, String[] paths, int startIndex) {
        int size = paths.length;
        int i = startIndex;
        DestinationNode node = this;
        while (i < size && node != null) {
            String path = paths[i];
            if (path.equals(ANY_DESCENDENT)) {
                answer.addAll(node.removeDesendentValues());
                break;
            }
            DestinationNode node2;
            node.appendMatchingWildcards(answer, paths, i);
            if (path.equals(ANY_CHILD)) {
                node2 = new AnyChildDestinationNode(node);
            } else {
                node2 = node.getChild(path);
            }
            i++;
            node = node2;
        }
        if (node != null) {
            answer.addAll(node.removeValues());
        }
    }

    public void appendDescendantValues(Set answer) {
        answer.addAll(this.values);
        for (DestinationNode child : this.childNodes.values()) {
            child.appendDescendantValues(answer);
        }
    }

    protected DestinationMapNode createChildNode() {
        return new DestinationMapNode(this);
    }

    public void appendMatchingWildcards(Set answer, String[] paths, int idx) {
        if (idx - 1 <= this.pathLength) {
            DestinationNode wildCardNode = getChild(ANY_CHILD);
            if (wildCardNode != null) {
                wildCardNode.appendMatchingValues(answer, paths, idx + 1);
            }
            wildCardNode = getChild(ANY_DESCENDENT);
            if (wildCardNode != null) {
                answer.addAll(wildCardNode.getDesendentValues());
            }
        }
    }

    public void appendMatchingValues(Set<DestinationNode> answer, String[] paths, int startIndex) {
        boolean couldMatchAny = true;
        int size = paths.length;
        int i = startIndex;
        DestinationNode node = this;
        while (i < size && node != null) {
            String path = paths[i];
            if (path.equals(ANY_DESCENDENT)) {
                answer.addAll(node.getDesendentValues());
                couldMatchAny = false;
                break;
            }
            DestinationNode node2;
            node.appendMatchingWildcards(answer, paths, i);
            if (path.equals(ANY_CHILD)) {
                node2 = new AnyChildDestinationNode(node);
            } else {
                node2 = node.getChild(path);
            }
            i++;
            node = node2;
        }
        if (node != null) {
            answer.addAll(node.getValues());
            if (couldMatchAny) {
                DestinationNode child = node.getChild(ANY_DESCENDENT);
                if (child != null) {
                    answer.addAll(child.getValues());
                }
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    protected void pruneIfEmpty() {
        if (this.parent != null && this.childNodes.isEmpty() && this.values.isEmpty()) {
            this.parent.removeChild(this);
        }
    }

    protected void removeChild(DestinationMapNode node) {
        this.childNodes.remove(node.getPath());
        pruneIfEmpty();
    }
}
