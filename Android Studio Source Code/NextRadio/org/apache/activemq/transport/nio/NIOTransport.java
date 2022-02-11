package org.apache.activemq.transport.nio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import javax.net.SocketFactory;
import org.apache.activemq.transport.nio.SelectorManager.Listener;
import org.apache.activemq.transport.tcp.TcpTransport;
import org.apache.activemq.util.IOExceptionSupport;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.wireformat.WireFormat;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

public class NIOTransport extends TcpTransport {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected SocketChannel channel;
    protected ByteBuffer currentBuffer;
    protected ByteBuffer inputBuffer;
    protected int nextFrameSize;
    protected SelectorSelection selection;

    class 1 implements Listener {
        1() {
        }

        public void onSelect(SelectorSelection selection) {
            NIOTransport.this.serviceRead();
        }

        public void onError(SelectorSelection selection, Throwable error) {
            if (error instanceof IOException) {
                NIOTransport.this.onException((IOException) error);
            } else {
                NIOTransport.this.onException(IOExceptionSupport.create(error));
            }
        }
    }

    static {
        $assertionsDisabled = !NIOTransport.class.desiredAssertionStatus();
    }

    public NIOTransport(WireFormat wireFormat, SocketFactory socketFactory, URI remoteLocation, URI localLocation) throws UnknownHostException, IOException {
        super(wireFormat, socketFactory, remoteLocation, localLocation);
    }

    public NIOTransport(WireFormat wireFormat, Socket socket) throws IOException {
        super(wireFormat, socket);
    }

    protected void initializeStreams() throws IOException {
        this.channel = this.socket.getChannel();
        this.channel.configureBlocking(false);
        this.selection = SelectorManager.getInstance().register(this.channel, new 1());
        this.inputBuffer = ByteBuffer.allocate(Flags.FLAG2);
        this.currentBuffer = this.inputBuffer;
        this.nextFrameSize = -1;
        this.currentBuffer.limit(4);
        NIOOutputStream outPutStream = new NIOOutputStream(this.channel, KEYRecord.FLAG_NOCONF);
        this.dataOut = new DataOutputStream(outPutStream);
        this.buffOut = outPutStream;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void serviceRead() {
        /*
        r10 = this;
        r9 = -1;
    L_0x0001:
        r5 = r10.channel;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r4 = r5.read(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r4 != r9) goto L_0x0019;
    L_0x000b:
        r5 = new java.io.EOFException;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.<init>();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r10.onException(r5);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.selection;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.close();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
    L_0x0018:
        return;
    L_0x0019:
        if (r4 == 0) goto L_0x0018;
    L_0x001b:
        r5 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r5.hasRemaining();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 != 0) goto L_0x0001;
    L_0x0023:
        r5 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 != r9) goto L_0x00c1;
    L_0x0027:
        r5 = $assertionsDisabled;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 != 0) goto L_0x003c;
    L_0x002b:
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 == r6) goto L_0x003c;
    L_0x0031:
        r5 = new java.lang.AssertionError;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.<init>();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        throw r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
    L_0x0037:
        r1 = move-exception;
        r10.onException(r1);
        goto L_0x0018;
    L_0x003c:
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.flip();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r5.getInt();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r5 + 4;
        r10.nextFrameSize = r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.wireFormat;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r5 instanceof org.apache.activemq.openwire.OpenWireFormat;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 == 0) goto L_0x009d;
    L_0x0051:
        r5 = r10.wireFormat;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = (org.apache.activemq.openwire.OpenWireFormat) r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r2 = r5.getMaxFrameSize();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = (long) r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r5 <= 0) goto L_0x009d;
    L_0x0060:
        r5 = new java.io.IOException;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6.<init>();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7 = "Frame size of ";
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r8 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r7 = r7 / r8;
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7 = " MB larger than max allowed ";
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r8 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r8 = r2 / r8;
        r6 = r6.append(r8);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7 = " MB";
        r6 = r6.append(r7);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r6.toString();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.<init>(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        throw r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
    L_0x0093:
        r1 = move-exception;
        r5 = org.apache.activemq.util.IOExceptionSupport.create(r1);
        r10.onException(r5);
        goto L_0x0018;
    L_0x009d:
        r5 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r6.capacity();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        if (r5 <= r6) goto L_0x00b8;
    L_0x00a7:
        r5 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = java.nio.ByteBuffer.allocate(r5);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r10.currentBuffer = r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.putInt(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        goto L_0x0001;
    L_0x00b8:
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = r10.nextFrameSize;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.limit(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        goto L_0x0001;
    L_0x00c1:
        r5 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.flip();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.wireFormat;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = new java.io.DataInputStream;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7 = new org.apache.activemq.transport.nio.NIOInputStream;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r8 = r10.currentBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r7.<init>(r8);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6.<init>(r7);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r0 = r5.unmarshal(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r0 = (org.apache.activemq.command.Command) r0;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r10.doConsume(r0);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = -1;
        r10.nextFrameSize = r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5.clear();	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r6 = 4;
        r5.limit(r6);	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r5 = r10.inputBuffer;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        r10.currentBuffer = r5;	 Catch:{ IOException -> 0x0037, Throwable -> 0x0093 }
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.activemq.transport.nio.NIOTransport.serviceRead():void");
    }

    protected void doStart() throws Exception {
        connect();
        this.selection.setInterestOps(1);
        this.selection.enable();
    }

    protected void doStop(ServiceStopper stopper) throws Exception {
        if (this.selection != null) {
            this.selection.close();
            this.selection = null;
        }
        super.doStop(stopper);
    }
}
