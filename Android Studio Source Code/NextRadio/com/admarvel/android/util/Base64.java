package com.admarvel.android.util;

import java.io.UnsupportedEncodingException;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.JournalQueueAck;
import org.apache.activemq.command.JournalTopicAck;
import org.apache.activemq.command.JournalTrace;
import org.apache.activemq.command.JournalTransaction;
import org.apache.activemq.command.LocalTransactionId;
import org.apache.activemq.command.MessageDispatchNotification;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ReplayCommand;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SubscriptionInfo;
import org.apache.activemq.command.XATransactionId;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

/* renamed from: com.admarvel.android.util.h */
public class Base64 {
    static final /* synthetic */ boolean f1035a;

    /* renamed from: com.admarvel.android.util.h.a */
    static abstract class Base64 {
        public byte[] f1023a;
        public int f1024b;

        Base64() {
        }
    }

    /* renamed from: com.admarvel.android.util.h.b */
    static class Base64 extends Base64 {
        static final /* synthetic */ boolean f1025g;
        private static final byte[] f1026h;
        private static final byte[] f1027i;
        int f1028c;
        public final boolean f1029d;
        public final boolean f1030e;
        public final boolean f1031f;
        private final byte[] f1032j;
        private int f1033k;
        private final byte[] f1034l;

        static {
            f1025g = !Base64.class.desiredAssertionStatus();
            f1026h = new byte[]{ReplayCommand.DATA_STRUCTURE_TYPE, (byte) 66, (byte) 67, (byte) 68, (byte) 69, CommandTypes.BYTE_TYPE, CommandTypes.CHAR_TYPE, CommandTypes.SHORT_TYPE, CommandTypes.INTEGER_TYPE, CommandTypes.LONG_TYPE, CommandTypes.DOUBLE_TYPE, CommandTypes.FLOAT_TYPE, CommandTypes.STRING_TYPE, CommandTypes.BOOLEAN_TYPE, CommandTypes.BYTE_ARRAY_TYPE, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, MessageDispatchNotification.DATA_STRUCTURE_TYPE, (byte) 97, (byte) 98, (byte) 99, CommandTypes.ACTIVEMQ_QUEUE, CommandTypes.ACTIVEMQ_TOPIC, CommandTypes.ACTIVEMQ_TEMP_QUEUE, CommandTypes.ACTIVEMQ_TEMP_TOPIC, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, MessageId.DATA_STRUCTURE_TYPE, LocalTransactionId.DATA_STRUCTURE_TYPE, XATransactionId.DATA_STRUCTURE_TYPE, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, ConnectionId.DATA_STRUCTURE_TYPE, SessionId.DATA_STRUCTURE_TYPE, ConsumerId.DATA_STRUCTURE_TYPE, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
            f1027i = new byte[]{ReplayCommand.DATA_STRUCTURE_TYPE, (byte) 66, (byte) 67, (byte) 68, (byte) 69, CommandTypes.BYTE_TYPE, CommandTypes.CHAR_TYPE, CommandTypes.SHORT_TYPE, CommandTypes.INTEGER_TYPE, CommandTypes.LONG_TYPE, CommandTypes.DOUBLE_TYPE, CommandTypes.FLOAT_TYPE, CommandTypes.STRING_TYPE, CommandTypes.BOOLEAN_TYPE, CommandTypes.BYTE_ARRAY_TYPE, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, MessageDispatchNotification.DATA_STRUCTURE_TYPE, (byte) 97, (byte) 98, (byte) 99, CommandTypes.ACTIVEMQ_QUEUE, CommandTypes.ACTIVEMQ_TOPIC, CommandTypes.ACTIVEMQ_TEMP_QUEUE, CommandTypes.ACTIVEMQ_TEMP_TOPIC, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, MessageId.DATA_STRUCTURE_TYPE, LocalTransactionId.DATA_STRUCTURE_TYPE, XATransactionId.DATA_STRUCTURE_TYPE, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, ConnectionId.DATA_STRUCTURE_TYPE, SessionId.DATA_STRUCTURE_TYPE, ConsumerId.DATA_STRUCTURE_TYPE, (byte) 48, (byte) 49, JournalTopicAck.DATA_STRUCTURE_TYPE, (byte) 51, JournalQueueAck.DATA_STRUCTURE_TYPE, JournalTrace.DATA_STRUCTURE_TYPE, JournalTransaction.DATA_STRUCTURE_TYPE, SubscriptionInfo.DATA_STRUCTURE_TYPE, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        }

        public Base64(int i, byte[] bArr) {
            boolean z = true;
            this.a = bArr;
            this.f1029d = (i & 1) == 0;
            this.f1030e = (i & 2) == 0;
            if ((i & 4) == 0) {
                z = false;
            }
            this.f1031f = z;
            this.f1034l = (i & 8) == 0 ? f1026h : f1027i;
            this.f1032j = new byte[2];
            this.f1028c = 0;
            this.f1033k = this.f1030e ? 19 : -1;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean m599a(byte[] r12, int r13, int r14, boolean r15) {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:42)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:66)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r11 = this;
            r6 = r11.f1034l;
            r7 = r11.a;
            r1 = 0;
            r0 = r11.f1033k;
            r8 = r14 + r13;
            r2 = -1;
            r3 = r11.f1028c;
            switch(r3) {
                case 0: goto L_0x00a7;
                case 1: goto L_0x00aa;
                case 2: goto L_0x00cd;
                default: goto L_0x000f;
            };
        L_0x000f:
            r3 = r13;
        L_0x0010:
            r4 = -1;
            if (r2 == r4) goto L_0x023b;
        L_0x0013:
            r4 = 1;
            r5 = r2 >> 18;
            r5 = r5 & 63;
            r5 = r6[r5];
            r7[r1] = r5;
            r1 = 2;
            r5 = r2 >> 12;
            r5 = r5 & 63;
            r5 = r6[r5];
            r7[r4] = r5;
            r4 = 3;
            r5 = r2 >> 6;
            r5 = r5 & 63;
            r5 = r6[r5];
            r7[r1] = r5;
            r1 = 4;
            r2 = r2 & 63;
            r2 = r6[r2];
            r7[r4] = r2;
            r0 = r0 + -1;
            if (r0 != 0) goto L_0x023b;
        L_0x0039:
            r0 = r11.f1031f;
            if (r0 == 0) goto L_0x023f;
        L_0x003d:
            r0 = 5;
            r2 = 13;
            r7[r1] = r2;
        L_0x0042:
            r1 = r0 + 1;
            r2 = 10;
            r7[r0] = r2;
            r0 = 19;
            r5 = r0;
            r4 = r1;
        L_0x004c:
            r0 = r3 + 3;
            if (r0 > r8) goto L_0x00f0;
        L_0x0050:
            r0 = r12[r3];
            r0 = r0 & 255;
            r0 = r0 << 16;
            r1 = r3 + 1;
            r1 = r12[r1];
            r1 = r1 & 255;
            r1 = r1 << 8;
            r0 = r0 | r1;
            r1 = r3 + 2;
            r1 = r12[r1];
            r1 = r1 & 255;
            r0 = r0 | r1;
            r1 = r0 >> 18;
            r1 = r1 & 63;
            r1 = r6[r1];
            r7[r4] = r1;
            r1 = r4 + 1;
            r2 = r0 >> 12;
            r2 = r2 & 63;
            r2 = r6[r2];
            r7[r1] = r2;
            r1 = r4 + 2;
            r2 = r0 >> 6;
            r2 = r2 & 63;
            r2 = r6[r2];
            r7[r1] = r2;
            r1 = r4 + 3;
            r0 = r0 & 63;
            r0 = r6[r0];
            r7[r1] = r0;
            r3 = r3 + 3;
            r1 = r4 + 4;
            r0 = r5 + -1;
            if (r0 != 0) goto L_0x023b;
        L_0x0092:
            r0 = r11.f1031f;
            if (r0 == 0) goto L_0x0238;
        L_0x0096:
            r0 = r1 + 1;
            r2 = 13;
            r7[r1] = r2;
        L_0x009c:
            r1 = r0 + 1;
            r2 = 10;
            r7[r0] = r2;
            r0 = 19;
            r5 = r0;
            r4 = r1;
            goto L_0x004c;
        L_0x00a7:
            r3 = r13;
            goto L_0x0010;
        L_0x00aa:
            r3 = r13 + 2;
            if (r3 > r8) goto L_0x000f;
        L_0x00ae:
            r2 = r11.f1032j;
            r3 = 0;
            r2 = r2[r3];
            r2 = r2 & 255;
            r2 = r2 << 16;
            r3 = r13 + 1;
            r4 = r12[r13];
            r4 = r4 & 255;
            r4 = r4 << 8;
            r2 = r2 | r4;
            r13 = r3 + 1;
            r3 = r12[r3];
            r3 = r3 & 255;
            r2 = r2 | r3;
            r3 = 0;
            r11.f1028c = r3;
            r3 = r13;
            goto L_0x0010;
        L_0x00cd:
            r3 = r13 + 1;
            if (r3 > r8) goto L_0x000f;
        L_0x00d1:
            r2 = r11.f1032j;
            r3 = 0;
            r2 = r2[r3];
            r2 = r2 & 255;
            r2 = r2 << 16;
            r3 = r11.f1032j;
            r4 = 1;
            r3 = r3[r4];
            r3 = r3 & 255;
            r3 = r3 << 8;
            r2 = r2 | r3;
            r3 = r13 + 1;
            r4 = r12[r13];
            r4 = r4 & 255;
            r2 = r2 | r4;
            r4 = 0;
            r11.f1028c = r4;
            goto L_0x0010;
        L_0x00f0:
            if (r15 == 0) goto L_0x01fe;
        L_0x00f2:
            r0 = r11.f1028c;
            r0 = r3 - r0;
            r1 = r8 + -1;
            if (r0 != r1) goto L_0x015e;
        L_0x00fa:
            r2 = 0;
            r0 = r11.f1028c;
            if (r0 <= 0) goto L_0x0156;
        L_0x00ff:
            r0 = r11.f1032j;
            r1 = 1;
            r0 = r0[r2];
            r2 = r3;
        L_0x0105:
            r0 = r0 & 255;
            r3 = r0 << 4;
            r0 = r11.f1028c;
            r0 = r0 - r1;
            r11.f1028c = r0;
            r1 = r4 + 1;
            r0 = r3 >> 6;
            r0 = r0 & 63;
            r0 = r6[r0];
            r7[r4] = r0;
            r0 = r1 + 1;
            r3 = r3 & 63;
            r3 = r6[r3];
            r7[r1] = r3;
            r1 = r11.f1029d;
            if (r1 == 0) goto L_0x0130;
        L_0x0124:
            r1 = r0 + 1;
            r3 = 61;
            r7[r0] = r3;
            r0 = r1 + 1;
            r3 = 61;
            r7[r1] = r3;
        L_0x0130:
            r1 = r11.f1030e;
            if (r1 == 0) goto L_0x0146;
        L_0x0134:
            r1 = r11.f1031f;
            if (r1 == 0) goto L_0x013f;
        L_0x0138:
            r1 = r0 + 1;
            r3 = 13;
            r7[r0] = r3;
            r0 = r1;
        L_0x013f:
            r1 = r0 + 1;
            r3 = 10;
            r7[r0] = r3;
            r0 = r1;
        L_0x0146:
            r3 = r2;
            r4 = r0;
        L_0x0148:
            r0 = f1025g;
            if (r0 != 0) goto L_0x01f2;
        L_0x014c:
            r0 = r11.f1028c;
            if (r0 == 0) goto L_0x01f2;
        L_0x0150:
            r0 = new java.lang.AssertionError;
            r0.<init>();
            throw r0;
        L_0x0156:
            r1 = r3 + 1;
            r0 = r12[r3];
            r10 = r2;
            r2 = r1;
            r1 = r10;
            goto L_0x0105;
        L_0x015e:
            r0 = r11.f1028c;
            r0 = r3 - r0;
            r1 = r8 + -2;
            if (r0 != r1) goto L_0x01d6;
        L_0x0166:
            r2 = 0;
            r0 = r11.f1028c;
            r1 = 1;
            if (r0 <= r1) goto L_0x01c9;
        L_0x016c:
            r0 = r11.f1032j;
            r1 = 1;
            r0 = r0[r2];
        L_0x0171:
            r0 = r0 & 255;
            r9 = r0 << 10;
            r0 = r11.f1028c;
            if (r0 <= 0) goto L_0x01d0;
        L_0x0179:
            r0 = r11.f1032j;
            r2 = r1 + 1;
            r0 = r0[r1];
            r1 = r2;
        L_0x0180:
            r0 = r0 & 255;
            r0 = r0 << 2;
            r0 = r0 | r9;
            r2 = r11.f1028c;
            r1 = r2 - r1;
            r11.f1028c = r1;
            r1 = r4 + 1;
            r2 = r0 >> 12;
            r2 = r2 & 63;
            r2 = r6[r2];
            r7[r4] = r2;
            r2 = r1 + 1;
            r4 = r0 >> 6;
            r4 = r4 & 63;
            r4 = r6[r4];
            r7[r1] = r4;
            r1 = r2 + 1;
            r0 = r0 & 63;
            r0 = r6[r0];
            r7[r2] = r0;
            r0 = r11.f1029d;
            if (r0 == 0) goto L_0x0235;
        L_0x01ab:
            r0 = r1 + 1;
            r2 = 61;
            r7[r1] = r2;
        L_0x01b1:
            r1 = r11.f1030e;
            if (r1 == 0) goto L_0x01c7;
        L_0x01b5:
            r1 = r11.f1031f;
            if (r1 == 0) goto L_0x01c0;
        L_0x01b9:
            r1 = r0 + 1;
            r2 = 13;
            r7[r0] = r2;
            r0 = r1;
        L_0x01c0:
            r1 = r0 + 1;
            r2 = 10;
            r7[r0] = r2;
            r0 = r1;
        L_0x01c7:
            r4 = r0;
            goto L_0x0148;
        L_0x01c9:
            r1 = r3 + 1;
            r0 = r12[r3];
            r3 = r1;
            r1 = r2;
            goto L_0x0171;
        L_0x01d0:
            r2 = r3 + 1;
            r0 = r12[r3];
            r3 = r2;
            goto L_0x0180;
        L_0x01d6:
            r0 = r11.f1030e;
            if (r0 == 0) goto L_0x0148;
        L_0x01da:
            if (r4 <= 0) goto L_0x0148;
        L_0x01dc:
            r0 = 19;
            if (r5 == r0) goto L_0x0148;
        L_0x01e0:
            r0 = r11.f1031f;
            if (r0 == 0) goto L_0x0233;
        L_0x01e4:
            r0 = r4 + 1;
            r1 = 13;
            r7[r4] = r1;
        L_0x01ea:
            r4 = r0 + 1;
            r1 = 10;
            r7[r0] = r1;
            goto L_0x0148;
        L_0x01f2:
            r0 = f1025g;
            if (r0 != 0) goto L_0x020e;
        L_0x01f6:
            if (r3 == r8) goto L_0x020e;
        L_0x01f8:
            r0 = new java.lang.AssertionError;
            r0.<init>();
            throw r0;
        L_0x01fe:
            r0 = r8 + -1;
            if (r3 != r0) goto L_0x0214;
        L_0x0202:
            r0 = r11.f1032j;
            r1 = r11.f1028c;
            r2 = r1 + 1;
            r11.f1028c = r2;
            r2 = r12[r3];
            r0[r1] = r2;
        L_0x020e:
            r11.b = r4;
            r11.f1033k = r5;
            r0 = 1;
            return r0;
        L_0x0214:
            r0 = r8 + -2;
            if (r3 != r0) goto L_0x020e;
        L_0x0218:
            r0 = r11.f1032j;
            r1 = r11.f1028c;
            r2 = r1 + 1;
            r11.f1028c = r2;
            r2 = r12[r3];
            r0[r1] = r2;
            r0 = r11.f1032j;
            r1 = r11.f1028c;
            r2 = r1 + 1;
            r11.f1028c = r2;
            r2 = r3 + 1;
            r2 = r12[r2];
            r0[r1] = r2;
            goto L_0x020e;
        L_0x0233:
            r0 = r4;
            goto L_0x01ea;
        L_0x0235:
            r0 = r1;
            goto L_0x01b1;
        L_0x0238:
            r0 = r1;
            goto L_0x009c;
        L_0x023b:
            r5 = r0;
            r4 = r1;
            goto L_0x004c;
        L_0x023f:
            r0 = r1;
            goto L_0x0042;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.util.h.b.a(byte[], int, int, boolean):boolean");
        }
    }

    static {
        f1035a = !Base64.class.desiredAssertionStatus();
    }

    private Base64() {
    }

    public static String m600a(byte[] bArr, int i) {
        try {
            return new String(Base64.m602b(bArr, i), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] m601a(byte[] bArr, int i, int i2, int i3) {
        Base64 base64 = new Base64(i3, null);
        int i4 = (i2 / 3) * 4;
        if (!base64.f1029d) {
            switch (i2 % 3) {
                case Tokenizer.EOF /*0*/:
                    break;
                case Zone.PRIMARY /*1*/:
                    i4 += 2;
                    break;
                case Zone.SECONDARY /*2*/:
                    i4 += 3;
                    break;
                default:
                    break;
            }
        } else if (i2 % 3 > 0) {
            i4 += 4;
        }
        if (base64.f1030e && i2 > 0) {
            i4 += (base64.f1031f ? 2 : 1) * (((i2 - 1) / 57) + 1);
        }
        base64.a = new byte[i4];
        base64.m599a(bArr, i, i2, true);
        if (f1035a || base64.b == i4) {
            return base64.a;
        }
        throw new AssertionError();
    }

    public static byte[] m602b(byte[] bArr, int i) {
        return Base64.m601a(bArr, 0, bArr.length, i);
    }
}
