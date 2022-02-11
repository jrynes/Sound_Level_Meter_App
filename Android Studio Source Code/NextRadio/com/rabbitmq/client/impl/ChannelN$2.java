package com.rabbitmq.client.impl;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.impl.AMQChannel.BlockingRpcContinuation;

class ChannelN$2 extends BlockingRpcContinuation<Consumer> {
    final /* synthetic */ ChannelN this$0;
    final /* synthetic */ String val$consumerTag;
    final /* synthetic */ Consumer val$originalConsumer;

    ChannelN$2(ChannelN channelN, String str, Consumer consumer) {
        this.this$0 = channelN;
        this.val$consumerTag = str;
        this.val$originalConsumer = consumer;
    }

    public Consumer transformReply(AMQCommand replyCommand) {
        replyCommand.getMethod();
        ChannelN.access$000(this.this$0).remove(this.val$consumerTag);
        ChannelN.access$100(this.this$0).handleCancelOk(this.val$originalConsumer, this.val$consumerTag);
        return this.val$originalConsumer;
    }
}
