package org.apache.activemq.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class AnyChildDestinationNode implements DestinationNode {
    private DestinationNode node;

    class 1 extends AnyChildDestinationNode {
        final /* synthetic */ Collection val$list;

        1(DestinationNode x0, Collection collection) {
            this.val$list = collection;
            super(x0);
        }

        protected Collection getChildNodes() {
            return this.val$list;
        }
    }

    public AnyChildDestinationNode(DestinationNode node) {
        this.node = node;
    }

    public void appendMatchingValues(Set answer, String[] paths, int startIndex) {
        for (DestinationNode child : getChildNodes()) {
            child.appendMatchingValues(answer, paths, startIndex);
        }
    }

    public void appendMatchingWildcards(Set answer, String[] paths, int startIndex) {
        for (DestinationNode child : getChildNodes()) {
            child.appendMatchingWildcards(answer, paths, startIndex);
        }
    }

    public void appendDescendantValues(Set answer) {
        for (DestinationNode child : getChildNodes()) {
            child.appendDescendantValues(answer);
        }
    }

    public DestinationNode getChild(String path) {
        Collection list = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            DestinationNode answer = child.getChild(path);
            if (answer != null) {
                list.add(answer);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return new 1(this, list);
    }

    public Collection getDesendentValues() {
        Collection answer = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            answer.addAll(child.getDesendentValues());
        }
        return answer;
    }

    public Collection getValues() {
        Collection answer = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            answer.addAll(child.getValues());
        }
        return answer;
    }

    public Collection getChildren() {
        Collection answer = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            answer.addAll(child.getChildren());
        }
        return answer;
    }

    public Collection removeDesendentValues() {
        Collection answer = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            answer.addAll(child.removeDesendentValues());
        }
        return answer;
    }

    public Collection removeValues() {
        Collection answer = new ArrayList();
        for (DestinationNode child : getChildNodes()) {
            answer.addAll(child.removeValues());
        }
        return answer;
    }

    protected Collection getChildNodes() {
        return this.node.getChildren();
    }
}
