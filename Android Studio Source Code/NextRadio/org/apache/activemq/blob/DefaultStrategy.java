package org.apache.activemq.blob;

import com.rabbitmq.client.AMQP;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.command.ActiveMQBlobMessage;

public class DefaultStrategy {
    protected BlobTransferPolicy transferPolicy;

    public DefaultStrategy(BlobTransferPolicy transferPolicy) {
        this.transferPolicy = transferPolicy;
    }

    protected boolean isSuccessfulCode(int responseCode) {
        return responseCode >= AMQP.REPLY_SUCCESS && responseCode < 300;
    }

    protected URL createMessageURL(ActiveMQBlobMessage message) throws JMSException, MalformedURLException {
        return new URL(this.transferPolicy.getUploadUrl() + message.getMessageId().toString());
    }
}
