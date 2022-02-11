package com.rabbitmq.client.impl;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.impl.AMQChannel.BlockingRpcContinuation;
import com.rabbitmq.client.impl.AMQImpl.Basic.ConsumeOk;

class ChannelN$1 extends BlockingRpcContinuation<String> {
    final /* synthetic */ ChannelN this$0;
    final /* synthetic */ Consumer val$callback;

    ChannelN$1(ChannelN channelN, Consumer consumer) {
        this.this$0 = channelN;
        this.val$callback = consumer;
    }

    public String transformReply(AMQCommand replyCommand) {
        String actualConsumerTag = ((ConsumeOk) replyCommand.getMethod()).getConsumerTag();
        ChannelN.access$000(this.this$0).put(actualConsumerTag, this.val$callback);
        ChannelN.access$100(this.this$0).handleConsumeOk(this.val$callback, actualConsumerTag);
        return actualConsumerTag;
    }
}
