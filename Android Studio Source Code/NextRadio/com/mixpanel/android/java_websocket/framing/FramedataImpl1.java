package com.mixpanel.android.java_websocket.framing;

import com.mixpanel.android.java_websocket.exceptions.InvalidDataException;
import com.mixpanel.android.java_websocket.exceptions.InvalidFrameException;
import com.mixpanel.android.java_websocket.framing.Framedata.Opcode;
import com.mixpanel.android.java_websocket.util.Charsetfunctions;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FramedataImpl1 implements FrameBuilder {
    protected static byte[] emptyarray;
    protected boolean fin;
    protected Opcode optcode;
    protected boolean transferemasked;
    private ByteBuffer unmaskedpayload;

    static {
        emptyarray = new byte[0];
    }

    public FramedataImpl1(Opcode op) {
        this.optcode = op;
        this.unmaskedpayload = ByteBuffer.wrap(emptyarray);
    }

    public FramedataImpl1(Framedata f) {
        this.fin = f.isFin();
        this.optcode = f.getOpcode();
        this.unmaskedpayload = f.getPayloadData();
        this.transferemasked = f.getTransfereMasked();
    }

    public boolean isFin() {
        return this.fin;
    }

    public Opcode getOpcode() {
        return this.optcode;
    }

    public boolean getTransfereMasked() {
        return this.transferemasked;
    }

    public ByteBuffer getPayloadData() {
        return this.unmaskedpayload;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public void setOptcode(Opcode optcode) {
        this.optcode = optcode;
    }

    public void setPayload(ByteBuffer payload) throws InvalidDataException {
        this.unmaskedpayload = payload;
    }

    public void setTransferemasked(boolean transferemasked) {
        this.transferemasked = transferemasked;
    }

    public void append(Framedata nextframe) throws InvalidFrameException {
        ByteBuffer b = nextframe.getPayloadData();
        if (this.unmaskedpayload == null) {
            this.unmaskedpayload = ByteBuffer.allocate(b.remaining());
            b.mark();
            this.unmaskedpayload.put(b);
            b.reset();
        } else {
            b.mark();
            this.unmaskedpayload.position(this.unmaskedpayload.limit());
            this.unmaskedpayload.limit(this.unmaskedpayload.capacity());
            if (b.remaining() > this.unmaskedpayload.remaining()) {
                ByteBuffer tmp = ByteBuffer.allocate(b.remaining() + this.unmaskedpayload.capacity());
                this.unmaskedpayload.flip();
                tmp.put(this.unmaskedpayload);
                tmp.put(b);
                this.unmaskedpayload = tmp;
            } else {
                this.unmaskedpayload.put(b);
            }
            this.unmaskedpayload.rewind();
            b.reset();
        }
        this.fin = nextframe.isFin();
    }

    public String toString() {
        return "Framedata{ optcode:" + getOpcode() + ", fin:" + isFin() + ", payloadlength:[pos:" + this.unmaskedpayload.position() + ", len:" + this.unmaskedpayload.remaining() + "], payload:" + Arrays.toString(Charsetfunctions.utf8Bytes(new String(this.unmaskedpayload.array()))) + "}";
    }
}
