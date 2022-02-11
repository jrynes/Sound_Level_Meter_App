package org.apache.activemq.filter;

import java.io.IOException;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.command.Message;

public class NonCachedMessageEvaluationContext extends MessageEvaluationContext {
    public Message getMessage() throws IOException {
        return this.messageReference != null ? this.messageReference.getMessage() : null;
    }

    public void setMessageReference(MessageReference messageReference) {
        this.messageReference = messageReference;
    }

    protected void clearMessageCache() {
    }
}
