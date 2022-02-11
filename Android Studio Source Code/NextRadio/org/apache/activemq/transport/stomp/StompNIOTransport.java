package org.apache.activemq.transport.stomp;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import javax.net.SocketFactory;
import org.apache.activemq.transport.nio.NIOOutputStream;
import org.apache.activemq.transport.nio.SelectorManager;
import org.apache.activemq.transport.nio.SelectorManager.Listener;
import org.apache.activemq.transport.nio.SelectorSelection;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.wireformat.WireFormat;
import org.xbill.DNS.KEYRecord.Flags;

public class StompNIOTransport extends TcpTransport {
    private SocketChannel channel;
    StompCodec codec;
    private ByteBuffer inputBuffer;
    private SelectorSelection selection;

    class 1 implements Listener {
        1() {
        }

        public void onSelect(SelectorSelection selection) {
            StompNIOTransport.this.serviceRead();
        }

        public void onError(SelectorSelection selection, Throwable error) {
            if (error instanceof IOException) {
                StompNIOTransport.this.onException((IOException) error);
            } else {
                StompNIOTransport.this.onException(IOExceptionSupport.create(error));
            }
        }
    }

    public StompNIOTransport(WireFormat wireFormat, SocketFactory socketFactory, URI remoteLocation, URI localLocation) throws UnknownHostException, IOException {
        super(wireFormat, socketFactory, remoteLocation, localLocation);
    }

    public StompNIOTransport(WireFormat wireFormat, Socket socket) throws IOException {
        super(wireFormat, socket);
    }

    protected void initializeStreams() throws IOException {
        this.channel = this.socket.getChannel();
        this.channel.configureBlocking(false);
        this.selection = SelectorManager.getInstance().register(this.channel, new 1());
        this.inputBuffer = ByteBuffer.allocate(Flags.FLAG2);
        NIOOutputStream outPutStream = new NIOOutputStream(this.channel, Flags.FLAG2);
        this.dataOut = new DataOutputStream(outPutStream);
        this.buffOut = outPutStream;
        this.codec = new StompCodec(this);
    }

    private void serviceRead() {
        while (true) {
            try {
                int readSize = this.channel.read(this.inputBuffer);
                if (readSize == -1) {
                    onException(new EOFException());
                    this.selection.close();
                    return;
                } else if (readSize != 0) {
                    this.receiveCounter += readSize;
                    this.inputBuffer.flip();
                    this.codec.parse(new ByteArrayInputStream(this.inputBuffer.array()), readSize);
                    this.inputBuffer.clear();
                } else {
                    return;
                }
            } catch (IOException e) {
                onException(e);
                return;
            } catch (Throwable e2) {
                onException(IOExceptionSupport.create(e2));
                return;
            }
        }
    }

    protected void doStart() throws Exception {
        connect();
        this.selection.setInterestOps(1);
        this.selection.enable();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        try {
            if (this.selection != null) {
                this.selection.close();
            }
            super.doStop(stopper);
        } catch (Throwable th) {
            super.doStop(stopper);
        }
    }
}
