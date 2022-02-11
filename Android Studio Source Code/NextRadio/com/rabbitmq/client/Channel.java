package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.Basic.RecoverOk;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Channel.FlowOk;
import com.rabbitmq.client.AMQP.Confirm.SelectOk;
import com.rabbitmq.client.AMQP.Exchange.BindOk;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;
import com.rabbitmq.client.AMQP.Exchange.DeleteOk;
import com.rabbitmq.client.AMQP.Exchange.UnbindOk;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.AMQP.Queue.PurgeOk;
import com.rabbitmq.client.AMQP.Tx;
import com.rabbitmq.client.AMQP.Tx.CommitOk;
import com.rabbitmq.client.AMQP.Tx.RollbackOk;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface Channel extends ShutdownNotifier {
    void abort() throws IOException;

    void abort(int i, String str) throws IOException;

    void addConfirmListener(ConfirmListener confirmListener);

    void addFlowListener(FlowListener flowListener);

    void addReturnListener(ReturnListener returnListener);

    void asyncRpc(Method method) throws IOException;

    void basicAck(long j, boolean z) throws IOException;

    void basicCancel(String str) throws IOException;

    String basicConsume(String str, Consumer consumer) throws IOException;

    String basicConsume(String str, boolean z, Consumer consumer) throws IOException;

    String basicConsume(String str, boolean z, String str2, Consumer consumer) throws IOException;

    String basicConsume(String str, boolean z, String str2, boolean z2, boolean z3, Map<String, Object> map, Consumer consumer) throws IOException;

    GetResponse basicGet(String str, boolean z) throws IOException;

    void basicNack(long j, boolean z, boolean z2) throws IOException;

    void basicPublish(String str, String str2, BasicProperties basicProperties, byte[] bArr) throws IOException;

    void basicPublish(String str, String str2, boolean z, boolean z2, BasicProperties basicProperties, byte[] bArr) throws IOException;

    void basicQos(int i) throws IOException;

    void basicQos(int i, int i2, boolean z) throws IOException;

    RecoverOk basicRecover() throws IOException;

    RecoverOk basicRecover(boolean z) throws IOException;

    @Deprecated
    void basicRecoverAsync(boolean z) throws IOException;

    void basicReject(long j, boolean z) throws IOException;

    void clearConfirmListeners();

    void clearFlowListeners();

    void clearReturnListeners();

    void close() throws IOException;

    void close(int i, String str) throws IOException;

    SelectOk confirmSelect() throws IOException;

    BindOk exchangeBind(String str, String str2, String str3) throws IOException;

    BindOk exchangeBind(String str, String str2, String str3, Map<String, Object> map) throws IOException;

    DeclareOk exchangeDeclare(String str, String str2) throws IOException;

    DeclareOk exchangeDeclare(String str, String str2, boolean z) throws IOException;

    DeclareOk exchangeDeclare(String str, String str2, boolean z, boolean z2, Map<String, Object> map) throws IOException;

    DeclareOk exchangeDeclare(String str, String str2, boolean z, boolean z2, boolean z3, Map<String, Object> map) throws IOException;

    DeclareOk exchangeDeclarePassive(String str) throws IOException;

    DeleteOk exchangeDelete(String str) throws IOException;

    DeleteOk exchangeDelete(String str, boolean z) throws IOException;

    UnbindOk exchangeUnbind(String str, String str2, String str3) throws IOException;

    UnbindOk exchangeUnbind(String str, String str2, String str3, Map<String, Object> map) throws IOException;

    FlowOk flow(boolean z) throws IOException;

    int getChannelNumber();

    Connection getConnection();

    Consumer getDefaultConsumer();

    FlowOk getFlow();

    long getNextPublishSeqNo();

    Queue.BindOk queueBind(String str, String str2, String str3) throws IOException;

    Queue.BindOk queueBind(String str, String str2, String str3, Map<String, Object> map) throws IOException;

    Queue.DeclareOk queueDeclare() throws IOException;

    Queue.DeclareOk queueDeclare(String str, boolean z, boolean z2, boolean z3, Map<String, Object> map) throws IOException;

    Queue.DeclareOk queueDeclarePassive(String str) throws IOException;

    Queue.DeleteOk queueDelete(String str) throws IOException;

    Queue.DeleteOk queueDelete(String str, boolean z, boolean z2) throws IOException;

    PurgeOk queuePurge(String str) throws IOException;

    Queue.UnbindOk queueUnbind(String str, String str2, String str3) throws IOException;

    Queue.UnbindOk queueUnbind(String str, String str2, String str3, Map<String, Object> map) throws IOException;

    boolean removeConfirmListener(ConfirmListener confirmListener);

    boolean removeFlowListener(FlowListener flowListener);

    boolean removeReturnListener(ReturnListener returnListener);

    Command rpc(Method method) throws IOException;

    void setDefaultConsumer(Consumer consumer);

    CommitOk txCommit() throws IOException;

    RollbackOk txRollback() throws IOException;

    Tx.SelectOk txSelect() throws IOException;

    boolean waitForConfirms() throws InterruptedException;

    boolean waitForConfirms(long j) throws InterruptedException, TimeoutException;

    void waitForConfirmsOrDie() throws IOException, InterruptedException;

    void waitForConfirmsOrDie(long j) throws IOException, InterruptedException, TimeoutException;
}
