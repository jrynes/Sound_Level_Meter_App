package com.rabbitmq.client;

public class Envelope {
    private final long _deliveryTag;
    private final String _exchange;
    private final boolean _redeliver;
    private final String _routingKey;

    public Envelope(long deliveryTag, boolean redeliver, String exchange, String routingKey) {
        this._deliveryTag = deliveryTag;
        this._redeliver = redeliver;
        this._exchange = exchange;
        this._routingKey = routingKey;
    }

    public long getDeliveryTag() {
        return this._deliveryTag;
    }

    public boolean isRedeliver() {
        return this._redeliver;
    }

    public String getExchange() {
        return this._exchange;
    }

    public String getRoutingKey() {
        return this._routingKey;
    }
}
