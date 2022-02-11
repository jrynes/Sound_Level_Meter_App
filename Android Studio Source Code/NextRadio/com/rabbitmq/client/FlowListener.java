package com.rabbitmq.client;

import java.io.IOException;

public interface FlowListener {
    void handleFlow(boolean z) throws IOException;
}
