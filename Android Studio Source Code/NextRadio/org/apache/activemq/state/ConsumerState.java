package org.apache.activemq.state;

import org.apache.activemq.command.ConsumerInfo;

public class ConsumerState {
    final ConsumerInfo info;

    public ConsumerState(ConsumerInfo info) {
        this.info = info;
    }

    public String toString() {
        return this.info.toString();
    }

    public ConsumerInfo getInfo() {
        return this.info;
    }
}
