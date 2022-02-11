package org.apache.activemq.state;

import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.FlushCommand;
import org.apache.activemq.command.KeepAliveInfo;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageDispatchNotification;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.RemoveSubscriptionInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SessionInfo;
import org.apache.activemq.command.ShutdownInfo;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.command.WireFormatInfo;

public interface CommandVisitor {
    Response processAddConnection(ConnectionInfo connectionInfo) throws Exception;

    Response processAddConsumer(ConsumerInfo consumerInfo) throws Exception;

    Response processAddDestination(DestinationInfo destinationInfo) throws Exception;

    Response processAddProducer(ProducerInfo producerInfo) throws Exception;

    Response processAddSession(SessionInfo sessionInfo) throws Exception;

    Response processBeginTransaction(TransactionInfo transactionInfo) throws Exception;

    Response processBrokerInfo(BrokerInfo brokerInfo) throws Exception;

    Response processCommitTransactionOnePhase(TransactionInfo transactionInfo) throws Exception;

    Response processCommitTransactionTwoPhase(TransactionInfo transactionInfo) throws Exception;

    Response processConnectionControl(ConnectionControl connectionControl) throws Exception;

    Response processConnectionError(ConnectionError connectionError) throws Exception;

    Response processConsumerControl(ConsumerControl consumerControl) throws Exception;

    Response processControlCommand(ControlCommand controlCommand) throws Exception;

    Response processEndTransaction(TransactionInfo transactionInfo) throws Exception;

    Response processFlush(FlushCommand flushCommand) throws Exception;

    Response processForgetTransaction(TransactionInfo transactionInfo) throws Exception;

    Response processKeepAlive(KeepAliveInfo keepAliveInfo) throws Exception;

    Response processMessage(Message message) throws Exception;

    Response processMessageAck(MessageAck messageAck) throws Exception;

    Response processMessageDispatch(MessageDispatch messageDispatch) throws Exception;

    Response processMessageDispatchNotification(MessageDispatchNotification messageDispatchNotification) throws Exception;

    Response processMessagePull(MessagePull messagePull) throws Exception;

    Response processPrepareTransaction(TransactionInfo transactionInfo) throws Exception;

    Response processProducerAck(ProducerAck producerAck) throws Exception;

    Response processRecoverTransactions(TransactionInfo transactionInfo) throws Exception;

    Response processRemoveConnection(ConnectionId connectionId, long j) throws Exception;

    Response processRemoveConsumer(ConsumerId consumerId, long j) throws Exception;

    Response processRemoveDestination(DestinationInfo destinationInfo) throws Exception;

    Response processRemoveProducer(ProducerId producerId) throws Exception;

    Response processRemoveSession(SessionId sessionId, long j) throws Exception;

    Response processRemoveSubscription(RemoveSubscriptionInfo removeSubscriptionInfo) throws Exception;

    Response processRollbackTransaction(TransactionInfo transactionInfo) throws Exception;

    Response processShutdown(ShutdownInfo shutdownInfo) throws Exception;

    Response processWireFormat(WireFormatInfo wireFormatInfo) throws Exception;
}
