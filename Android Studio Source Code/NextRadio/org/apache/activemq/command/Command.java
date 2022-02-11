package org.apache.activemq.command;

import org.apache.activemq.state.CommandVisitor;

public interface Command extends DataStructure {
    int getCommandId();

    Endpoint getFrom();

    Endpoint getTo();

    boolean isBrokerInfo();

    boolean isConnectionControl();

    boolean isMessage();

    boolean isMessageAck();

    boolean isMessageDispatch();

    boolean isMessageDispatchNotification();

    boolean isResponse();

    boolean isResponseRequired();

    boolean isShutdownInfo();

    boolean isWireFormatInfo();

    void setCommandId(int i);

    void setFrom(Endpoint endpoint);

    void setResponseRequired(boolean z);

    void setTo(Endpoint endpoint);

    Response visit(CommandVisitor commandVisitor) throws Exception;
}
