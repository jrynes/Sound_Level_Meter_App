package org.apache.activemq.transport.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.transport.tcp.TcpTransportFactory;
import org.apache.activemq.transport.tcp.TcpTransportServer;
import org.apache.activemq.wireformat.WireFormat;

public class NIOTransportFactory extends TcpTransportFactory {

    class 1 extends TcpTransportServer {
        1(TcpTransportFactory x0, URI x1, ServerSocketFactory x2) {
            super(x0, x1, x2);
        }

        protected Transport createTransport(Socket socket, WireFormat format) throws IOException {
            return new NIOTransport(format, socket);
        }
    }

    class 2 extends ServerSocketFactory {
        2() {
        }

        public ServerSocket createServerSocket(int port) throws IOException {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            return serverSocketChannel.socket();
        }

        public ServerSocket createServerSocket(int port, int backlog) throws IOException {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port), backlog);
            return serverSocketChannel.socket();
        }

        public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(ifAddress, port), backlog);
            return serverSocketChannel.socket();
        }
    }

    class 3 extends SocketFactory {
        3() {
        }

        public Socket createSocket() throws IOException {
            return SocketChannel.open().socket();
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
            return channel.socket();
        }

        public Socket createSocket(InetAddress address, int port) throws IOException {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(address, port));
            return channel.socket();
        }

        public Socket createSocket(String address, int port, InetAddress localAddresss, int localPort) throws IOException, UnknownHostException {
            SocketChannel channel = SocketChannel.open();
            channel.socket().bind(new InetSocketAddress(localAddresss, localPort));
            channel.connect(new InetSocketAddress(address, port));
            return channel.socket();
        }

        public Socket createSocket(InetAddress address, int port, InetAddress localAddresss, int localPort) throws IOException {
            SocketChannel channel = SocketChannel.open();
            channel.socket().bind(new InetSocketAddress(localAddresss, localPort));
            channel.connect(new InetSocketAddress(address, port));
            return channel.socket();
        }
    }

    protected TcpTransportServer createTcpTransportServer(URI location, ServerSocketFactory serverSocketFactory) throws IOException, URISyntaxException {
        return new 1(this, location, serverSocketFactory);
    }

    protected TcpTransport createTcpTransport(WireFormat wf, SocketFactory socketFactory, URI location, URI localLocation) throws UnknownHostException, IOException {
        return new NIOTransport(wf, socketFactory, location, localLocation);
    }

    protected ServerSocketFactory createServerSocketFactory() {
        return new 2();
    }

    protected SocketFactory createSocketFactory() throws IOException {
        return new 3();
    }
}
