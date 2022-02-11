package org.apache.activemq.openwire;

import java.util.Comparator;
import org.apache.activemq.command.Command;

public class CommandIdComparator implements Comparator<Command> {
    public int compare(Command c1, Command c2) {
        return c1.getCommandId() - c2.getCommandId();
    }
}
