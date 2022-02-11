package com.rabbitmq.client;

import com.rabbitmq.utility.SensibleClone;

public class ConsumerCancelledException extends RuntimeException implements SensibleClone<ConsumerCancelledException> {
    private static final long serialVersionUID = 1;

    public ConsumerCancelledException sensibleClone() {
        try {
            return (ConsumerCancelledException) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
