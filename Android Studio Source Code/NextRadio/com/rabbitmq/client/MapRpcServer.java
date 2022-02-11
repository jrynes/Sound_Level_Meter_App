package com.rabbitmq.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.impl.MethodArgumentReader;
import com.rabbitmq.client.impl.MethodArgumentWriter;
import com.rabbitmq.client.impl.ValueReader;
import com.rabbitmq.client.impl.ValueWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapRpcServer extends RpcServer {
    public MapRpcServer(Channel channel) throws IOException {
        super(channel);
    }

    public MapRpcServer(Channel channel, String queueName) throws IOException {
        super(channel, queueName);
    }

    public byte[] handleCall(byte[] requestBody, BasicProperties replyProperties) {
        try {
            return encode(handleMapCall(decode(requestBody), replyProperties));
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public static Map<String, Object> decode(byte[] requestBody) throws IOException {
        return new MethodArgumentReader(new ValueReader(new DataInputStream(new ByteArrayInputStream(requestBody)))).readTable();
    }

    public static byte[] encode(Map<String, Object> reply) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        MethodArgumentWriter writer = new MethodArgumentWriter(new ValueWriter(new DataOutputStream(buffer)));
        writer.writeTable(reply);
        writer.flush();
        return buffer.toByteArray();
    }

    public Map<String, Object> handleMapCall(Map<String, Object> request, BasicProperties replyProperties) {
        return handleMapCall(request);
    }

    public Map<String, Object> handleMapCall(Map<String, Object> map) {
        return new HashMap();
    }

    public void handleCast(byte[] requestBody) {
        try {
            handleMapCast(decode(requestBody));
        } catch (IOException e) {
        }
    }

    public void handleMapCast(Map<String, Object> map) {
    }
}
