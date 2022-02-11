package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;
import com.rabbitmq.client.impl.AMQConnection;

final class AI01320xDecoder extends AI013x0xDecoder {
    AI01320xDecoder(BitArray information) {
        super(information);
    }

    protected void addWeightCode(StringBuilder buf, int weight) {
        if (weight < AMQConnection.HANDSHAKE_TIMEOUT) {
            buf.append("(3202)");
        } else {
            buf.append("(3203)");
        }
    }

    protected int checkWeight(int weight) {
        return weight < AMQConnection.HANDSHAKE_TIMEOUT ? weight : weight - 10000;
    }
}
