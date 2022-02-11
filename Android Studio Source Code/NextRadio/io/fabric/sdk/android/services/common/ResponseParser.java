package io.fabric.sdk.android.services.common;

import com.rabbitmq.client.AMQP;
import org.apache.activemq.ActiveMQPrefetchPolicy;

public class ResponseParser {
    public static final int ResponseActionDiscard = 0;
    public static final int ResponseActionRetry = 1;

    public static int parse(int statusCode) {
        if (statusCode >= AMQP.REPLY_SUCCESS && statusCode <= 299) {
            return ResponseActionDiscard;
        }
        if (statusCode >= 300 && statusCode <= 399) {
            return ResponseActionRetry;
        }
        if (statusCode >= 400 && statusCode <= 499) {
            return ResponseActionDiscard;
        }
        if (statusCode >= ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH) {
            return ResponseActionRetry;
        }
        return ResponseActionRetry;
    }
}
