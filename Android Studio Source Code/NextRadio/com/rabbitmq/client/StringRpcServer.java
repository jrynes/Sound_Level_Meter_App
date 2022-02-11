package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import org.apache.activemq.transport.stomp.Stomp;

public class StringRpcServer extends RpcServer {
    public static String STRING_ENCODING;

    public StringRpcServer(Channel channel) throws IOException {
        super(channel);
    }

    public StringRpcServer(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
    }

    static {
        STRING_ENCODING = HttpRequest.CHARSET_UTF8;
    }

    public byte[] handleCall(byte[] requestBody, BasicProperties replyProperties) {
        String request;
        try {
            request = new String(requestBody, STRING_ENCODING);
        } catch (IOException e) {
            request = new String(requestBody);
        }
        String reply = handleStringCall(request, replyProperties);
        try {
            return reply.getBytes(STRING_ENCODING);
        } catch (IOException e2) {
            return reply.getBytes();
        }
    }

    public String handleStringCall(String request, BasicProperties replyProperties) {
        return handleStringCall(request);
    }

    public String handleStringCall(String request) {
        return Stomp.EMPTY;
    }

    public void handleCast(byte[] requestBody) {
        try {
            handleStringCast(new String(requestBody, STRING_ENCODING));
        } catch (IOException e) {
            handleStringCast(new String(requestBody));
        }
    }

    public void handleStringCast(String requestBody) {
    }
}
