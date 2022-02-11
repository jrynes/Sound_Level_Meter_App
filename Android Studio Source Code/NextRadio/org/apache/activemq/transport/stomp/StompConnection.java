package org.apache.activemq.transport.stomp;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.apache.activemq.transport.stomp.Stomp.Commands;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.activemq.transport.stomp.Stomp.Headers.Connect;
import org.apache.activemq.transport.stomp.Stomp.Headers.Message;
import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;
import org.apache.activemq.transport.stomp.Stomp.Headers.Unsubscribe;
import org.apache.activemq.transport.stomp.Stomp.Responses;

public class StompConnection {
    public static final long RECEIVE_TIMEOUT = 10000;
    private ByteArrayOutputStream inputBuffer;
    private Socket stompSocket;
    private String version;

    public StompConnection() {
        this.inputBuffer = new ByteArrayOutputStream();
        this.version = Stomp.V1_0;
    }

    public void open(String host, int port) throws IOException, UnknownHostException {
        open(new Socket(host, port));
    }

    public void open(Socket socket) {
        this.stompSocket = socket;
    }

    public void close() throws IOException {
        if (this.stompSocket != null) {
            this.stompSocket.close();
            this.stompSocket = null;
        }
    }

    public void sendFrame(String data) throws Exception {
        byte[] bytes = data.getBytes(HttpRequest.CHARSET_UTF8);
        OutputStream outputStream = this.stompSocket.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }

    public void sendFrame(String frame, byte[] data) throws Exception {
        byte[] bytes = frame.getBytes(HttpRequest.CHARSET_UTF8);
        OutputStream outputStream = this.stompSocket.getOutputStream();
        outputStream.write(bytes);
        outputStream.write(data);
        outputStream.flush();
    }

    public StompFrame receive() throws Exception {
        return receive(RECEIVE_TIMEOUT);
    }

    public StompFrame receive(long timeOut) throws Exception {
        this.stompSocket.setSoTimeout((int) timeOut);
        InputStream is = this.stompSocket.getInputStream();
        StompWireFormat wf = new StompWireFormat();
        wf.setStompVersion(this.version);
        return (StompFrame) wf.unmarshal(new DataInputStream(is));
    }

    public String receiveFrame() throws Exception {
        return receiveFrame(RECEIVE_TIMEOUT);
    }

    public String receiveFrame(long timeOut) throws Exception {
        this.stompSocket.setSoTimeout((int) timeOut);
        InputStream is = this.stompSocket.getInputStream();
        while (true) {
            int c = is.read();
            if (c < 0) {
                break;
            } else if (c == 0) {
                c = is.read();
                if (c == 10) {
                    return stringFromBuffer(this.inputBuffer);
                }
                this.inputBuffer.write(0);
                this.inputBuffer.write(c);
            } else {
                this.inputBuffer.write(c);
            }
        }
        throw new IOException("socket closed.");
    }

    private String stringFromBuffer(ByteArrayOutputStream inputBuffer) throws Exception {
        byte[] ba = inputBuffer.toByteArray();
        inputBuffer.reset();
        return new String(ba, HttpRequest.CHARSET_UTF8);
    }

    public Socket getStompSocket() {
        return this.stompSocket;
    }

    public void setStompSocket(Socket stompSocket) {
        this.stompSocket = stompSocket;
    }

    public void connect(String username, String password) throws Exception {
        connect(username, password, null);
    }

    public void connect(String username, String password, String client) throws Exception {
        HashMap<String, String> headers = new HashMap();
        headers.put(Connect.LOGIN, username);
        headers.put(Connect.PASSCODE, password);
        if (client != null) {
            headers.put(Connect.CLIENT_ID, client);
        }
        connect(headers);
    }

    public void connect(HashMap<String, String> headers) throws Exception {
        sendFrame(new StompFrame(Commands.CONNECT, headers).format());
        StompFrame connect = receive();
        if (!connect.getAction().equals(Responses.CONNECTED)) {
            throw new Exception("Not connected: " + connect.getBody());
        }
    }

    public void disconnect() throws Exception {
        disconnect(null);
    }

    public void disconnect(String receiptId) throws Exception {
        StompFrame frame = new StompFrame(Commands.DISCONNECT);
        if (!(receiptId == null || receiptId.isEmpty())) {
            frame.getHeaders().put(Headers.RECEIPT_REQUESTED, receiptId);
        }
        sendFrame(frame.format());
    }

    public void send(String destination, String message) throws Exception {
        send(destination, message, null, null);
    }

    public void send(String destination, String message, String transaction, HashMap<String, String> headers) throws Exception {
        if (headers == null) {
            headers = new HashMap();
        }
        headers.put(Unsubscribe.DESTINATION, destination);
        if (transaction != null) {
            headers.put(Headers.TRANSACTION, transaction);
        }
        sendFrame(new StompFrame(Commands.SEND, headers, message.getBytes()).format());
    }

    public void subscribe(String destination) throws Exception {
        subscribe(destination, null, null);
    }

    public void subscribe(String destination, String ack) throws Exception {
        subscribe(destination, ack, new HashMap());
    }

    public void subscribe(String destination, String ack, HashMap<String, String> headers) throws Exception {
        if (headers == null) {
            headers = new HashMap();
        }
        headers.put(Unsubscribe.DESTINATION, destination);
        if (ack != null) {
            headers.put(Subscribe.ACK_MODE, ack);
        }
        sendFrame(new StompFrame("SUBSCRIBE", headers).format());
    }

    public void unsubscribe(String destination) throws Exception {
        unsubscribe(destination, null);
    }

    public void unsubscribe(String destination, HashMap<String, String> headers) throws Exception {
        if (headers == null) {
            headers = new HashMap();
        }
        headers.put(Unsubscribe.DESTINATION, destination);
        sendFrame(new StompFrame("UNSUBSCRIBE", headers).format());
    }

    public void begin(String transaction) throws Exception {
        HashMap<String, String> headers = new HashMap();
        headers.put(Headers.TRANSACTION, transaction);
        sendFrame(new StompFrame(Commands.BEGIN_TRANSACTION, headers).format());
    }

    public void abort(String transaction) throws Exception {
        HashMap<String, String> headers = new HashMap();
        headers.put(Headers.TRANSACTION, transaction);
        sendFrame(new StompFrame(Commands.ABORT_TRANSACTION, headers).format());
    }

    public void commit(String transaction) throws Exception {
        HashMap<String, String> headers = new HashMap();
        headers.put(Headers.TRANSACTION, transaction);
        sendFrame(new StompFrame(Commands.COMMIT_TRANSACTION, headers).format());
    }

    public void ack(StompFrame frame) throws Exception {
        ack((String) frame.getHeaders().get(Message.MESSAGE_ID), null);
    }

    public void ack(StompFrame frame, String transaction) throws Exception {
        ack((String) frame.getHeaders().get(Message.MESSAGE_ID), transaction);
    }

    public void ack(String messageId) throws Exception {
        ack(messageId, null);
    }

    public void ack(String messageId, String transaction) throws Exception {
        HashMap<String, String> headers = new HashMap();
        headers.put(Message.MESSAGE_ID, messageId);
        if (transaction != null) {
            headers.put(Headers.TRANSACTION, transaction);
        }
        sendFrame(new StompFrame(Commands.ACK, headers).format());
    }

    public void keepAlive() throws Exception {
        OutputStream outputStream = this.stompSocket.getOutputStream();
        outputStream.write(10);
        outputStream.flush();
    }

    protected String appendHeaders(HashMap<String, Object> headers) {
        StringBuilder result = new StringBuilder();
        for (String key : headers.keySet()) {
            result.append(key + Headers.SEPERATOR + headers.get(key) + Stomp.NEWLINE);
        }
        result.append(Stomp.NEWLINE);
        return result.toString();
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
