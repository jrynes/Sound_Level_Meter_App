package com.facebook.ads.internal.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.view.ViewCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.FlushCommand;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.util.MarshallingSupport;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Type;

/* renamed from: com.facebook.ads.internal.util.n */
class C0530n implements C0525j {
    private static final short[] f1936a;
    private static final byte[] f1937b;

    /* renamed from: com.facebook.ads.internal.util.n.a */
    private static class C0529a implements Callable<Void> {
        private final int[] f1929a;
        private final int f1930b;
        private final int f1931c;
        private final int f1932d;
        private final int f1933e;
        private final int f1934f;
        private final int f1935g;

        public C0529a(int[] iArr, int i, int i2, int i3, int i4, int i5, int i6) {
            this.f1929a = iArr;
            this.f1930b = i;
            this.f1931c = i2;
            this.f1932d = i3;
            this.f1933e = i4;
            this.f1934f = i5;
            this.f1935g = i6;
        }

        public Void m1561a() {
            C0530n.m1563b(this.f1929a, this.f1930b, this.f1931c, this.f1932d, this.f1933e, this.f1934f, this.f1935g);
            return null;
        }

        public /* synthetic */ Object call() {
            return m1561a();
        }
    }

    static {
        f1936a = new short[]{(short) 512, (short) 512, (short) 456, (short) 512, (short) 328, (short) 456, (short) 335, (short) 512, (short) 405, (short) 328, (short) 271, (short) 456, (short) 388, (short) 335, (short) 292, (short) 512, (short) 454, (short) 405, (short) 364, (short) 328, (short) 298, (short) 271, (short) 496, (short) 456, (short) 420, (short) 388, (short) 360, (short) 335, (short) 312, (short) 292, (short) 273, (short) 512, (short) 482, (short) 454, (short) 428, (short) 405, (short) 383, (short) 364, (short) 345, (short) 328, (short) 312, (short) 298, (short) 284, (short) 271, (short) 259, (short) 496, (short) 475, (short) 456, (short) 437, (short) 420, (short) 404, (short) 388, (short) 374, (short) 360, (short) 347, (short) 335, (short) 323, (short) 312, (short) 302, (short) 292, (short) 282, (short) 273, (short) 265, (short) 512, (short) 497, (short) 482, (short) 468, (short) 454, (short) 441, (short) 428, (short) 417, (short) 405, (short) 394, (short) 383, (short) 373, (short) 364, (short) 354, (short) 345, (short) 337, (short) 328, (short) 320, (short) 312, (short) 305, (short) 298, (short) 291, (short) 284, (short) 278, (short) 271, (short) 265, (short) 259, (short) 507, (short) 496, (short) 485, (short) 475, (short) 465, (short) 456, (short) 446, (short) 437, (short) 428, (short) 420, (short) 412, (short) 404, (short) 396, (short) 388, (short) 381, (short) 374, (short) 367, (short) 360, (short) 354, (short) 347, (short) 341, (short) 335, (short) 329, (short) 323, (short) 318, (short) 312, (short) 307, (short) 302, (short) 297, (short) 292, (short) 287, (short) 282, (short) 278, (short) 273, (short) 269, (short) 265, (short) 261, (short) 512, (short) 505, (short) 497, (short) 489, (short) 482, (short) 475, (short) 468, (short) 461, (short) 454, (short) 447, (short) 441, (short) 435, (short) 428, (short) 422, (short) 417, (short) 411, (short) 405, (short) 399, (short) 394, (short) 389, (short) 383, (short) 378, (short) 373, (short) 368, (short) 364, (short) 359, (short) 354, (short) 350, (short) 345, (short) 341, (short) 337, (short) 332, (short) 328, (short) 324, (short) 320, (short) 316, (short) 312, (short) 309, (short) 305, (short) 301, (short) 298, (short) 294, (short) 291, (short) 287, (short) 284, (short) 281, (short) 278, (short) 274, (short) 271, (short) 268, (short) 265, (short) 262, (short) 259, (short) 257, (short) 507, (short) 501, (short) 496, (short) 491, (short) 485, (short) 480, (short) 475, (short) 470, (short) 465, (short) 460, (short) 456, (short) 451, (short) 446, (short) 442, (short) 437, (short) 433, (short) 428, (short) 424, (short) 420, (short) 416, (short) 412, (short) 408, (short) 404, (short) 400, (short) 396, (short) 392, (short) 388, (short) 385, (short) 381, (short) 377, (short) 374, (short) 370, (short) 367, (short) 363, (short) 360, (short) 357, (short) 354, (short) 350, (short) 347, (short) 344, (short) 341, (short) 338, (short) 335, (short) 332, (short) 329, (short) 326, (short) 323, (short) 320, (short) 318, (short) 315, (short) 312, (short) 310, (short) 307, (short) 304, (short) 302, (short) 299, (short) 297, (short) 294, (short) 292, (short) 289, (short) 287, (short) 285, (short) 282, (short) 280, (short) 278, (short) 275, (short) 273, (short) 271, (short) 269, (short) 267, (short) 265, (short) 263, (short) 261, (short) 259};
        f1937b = new byte[]{(byte) 9, Flags.CD, MarshallingSupport.LIST_TYPE, MarshallingSupport.BIG_STRING_TYPE, MarshallingSupport.BIG_STRING_TYPE, ControlCommand.DATA_STRUCTURE_TYPE, ControlCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, FlushCommand.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConnectionError.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConsumerControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ConnectionControl.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, ProducerAck.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessagePull.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageDispatch.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, MessageAck.DATA_STRUCTURE_TYPE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE, CommandTypes.ACTIVEMQ_BYTES_MESSAGE};
    }

    C0530n() {
    }

    private static void m1563b(int[] iArr, int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i - 1;
        int i8 = i2 - 1;
        int i9 = (i3 * 2) + 1;
        short s = f1936a[i3];
        byte b = f1937b[i3];
        int[] iArr2 = new int[i9];
        int i10;
        int i11;
        long j;
        int i12;
        long j2;
        long j3;
        long j4;
        long j5;
        long j6;
        long j7;
        long j8;
        int i13;
        int i14;
        int i15;
        long j9;
        int i16;
        if (i6 == 1) {
            i10 = ((i5 + 1) * i2) / i4;
            for (i11 = (i5 * i2) / i4; i11 < i10; i11++) {
                j = 0;
                i12 = i * i11;
                j2 = 0;
                j3 = 0;
                j4 = 0;
                j5 = 0;
                j6 = 0;
                long j10 = 0;
                for (int i17 = 0; i17 <= i3; i17++) {
                    iArr2[i17] = iArr[i12];
                    j6 += (long) (((iArr[i12] >>> 16) & Type.ANY) * (i17 + 1));
                    j5 += (long) (((iArr[i12] >>> 8) & Type.ANY) * (i17 + 1));
                    j4 += (long) ((iArr[i12] & Type.ANY) * (i17 + 1));
                    j3 += (long) ((iArr[i12] >>> 16) & Type.ANY);
                    j2 += (long) ((iArr[i12] >>> 8) & Type.ANY);
                    j10 += (long) (iArr[i12] & Type.ANY);
                }
                j7 = 0;
                j8 = 0;
                for (int i18 = 1; i18 <= i3; i18++) {
                    if (i18 <= i7) {
                        i12++;
                    }
                    iArr2[i18 + i3] = iArr[i12];
                    j6 += (long) (((iArr[i12] >>> 16) & Type.ANY) * ((i3 + 1) - i18));
                    j5 += (long) (((iArr[i12] >>> 8) & Type.ANY) * ((i3 + 1) - i18));
                    j4 += (long) ((iArr[i12] & Type.ANY) * ((i3 + 1) - i18));
                    j7 += (long) ((iArr[i12] >>> 16) & Type.ANY);
                    j8 += (long) ((iArr[i12] >>> 8) & Type.ANY);
                    j += (long) (iArr[i12] & Type.ANY);
                }
                i12 = i3 > i7 ? i7 : i3;
                i13 = i3;
                i14 = 0;
                long j11 = j3;
                j3 = j2;
                j2 = j10;
                i8 = i12 + (i11 * i);
                i15 = i12;
                i12 = i11 * i;
                j9 = j6;
                j6 = j5;
                j5 = j4;
                j4 = j7;
                j7 = j8;
                j8 = j;
                j = j11;
                while (i14 < i) {
                    iArr[i12] = (int) (((((long) (iArr[i12] & ViewCompat.MEASURED_STATE_MASK)) | ((((((long) s) * j9) >>> b) & 255) << 16)) | ((((((long) s) * j6) >>> b) & 255) << 8)) | (((((long) s) * j5) >>> b) & 255));
                    i16 = i12 + 1;
                    j9 -= j;
                    j6 -= j3;
                    j5 -= j2;
                    i12 = (i13 + i9) - i3;
                    if (i12 >= i9) {
                        i12 -= i9;
                    }
                    j -= (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j3 -= (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j2 -= (long) (iArr2[i12] & Type.ANY);
                    if (i15 < i7) {
                        i8++;
                        i15++;
                    }
                    iArr2[i12] = iArr[i8];
                    j4 += (long) ((iArr[i8] >>> 16) & Type.ANY);
                    j7 += (long) ((iArr[i8] >>> 8) & Type.ANY);
                    j8 += (long) (iArr[i8] & Type.ANY);
                    j9 += j4;
                    j6 += j7;
                    j5 += j8;
                    i12 = i13 + 1;
                    if (i12 >= i9) {
                        i12 = 0;
                    }
                    j += (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j3 += (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j2 += (long) (iArr2[i12] & Type.ANY);
                    j4 -= (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j7 -= (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j8 -= (long) (iArr2[i12] & Type.ANY);
                    i14++;
                    i13 = i12;
                    i12 = i16;
                }
            }
        } else if (i6 == 2) {
            i10 = ((i5 + 1) * i) / i4;
            for (i16 = (i5 * i) / i4; i16 < i10; i16++) {
                j8 = 0;
                j2 = 0;
                j3 = 0;
                j = 0;
                j5 = 0;
                j6 = 0;
                j9 = 0;
                for (i12 = 0; i12 <= i3; i12++) {
                    iArr2[i12] = iArr[i16];
                    j9 += (long) (((iArr[i16] >>> 16) & Type.ANY) * (i12 + 1));
                    j6 += (long) (((iArr[i16] >>> 8) & Type.ANY) * (i12 + 1));
                    j5 += (long) ((iArr[i16] & Type.ANY) * (i12 + 1));
                    j += (long) ((iArr[i16] >>> 16) & Type.ANY);
                    j3 += (long) ((iArr[i16] >>> 8) & Type.ANY);
                    j2 += (long) (iArr[i16] & Type.ANY);
                }
                j7 = 0;
                j4 = 0;
                i12 = i16;
                for (i7 = 1; i7 <= i3; i7++) {
                    if (i7 <= i8) {
                        i12 += i;
                    }
                    iArr2[i7 + i3] = iArr[i12];
                    j9 += (long) (((iArr[i12] >>> 16) & Type.ANY) * ((i3 + 1) - i7));
                    j6 += (long) (((iArr[i12] >>> 8) & Type.ANY) * ((i3 + 1) - i7));
                    j5 += (long) ((iArr[i12] & Type.ANY) * ((i3 + 1) - i7));
                    j4 += (long) ((iArr[i12] >>> 16) & Type.ANY);
                    j7 += (long) ((iArr[i12] >>> 8) & Type.ANY);
                    j8 += (long) (iArr[i12] & Type.ANY);
                }
                i12 = i3 > i8 ? i8 : i3;
                i7 = (i12 * i) + i16;
                i14 = i3;
                i11 = 0;
                i15 = i12;
                i12 = i16;
                while (i11 < i2) {
                    iArr[i12] = (int) (((((long) (iArr[i12] & ViewCompat.MEASURED_STATE_MASK)) | ((((((long) s) * j9) >>> b) & 255) << 16)) | ((((((long) s) * j6) >>> b) & 255) << 8)) | (((((long) s) * j5) >>> b) & 255));
                    i13 = i12 + i;
                    j9 -= j;
                    j6 -= j3;
                    j5 -= j2;
                    i12 = (i14 + i9) - i3;
                    if (i12 >= i9) {
                        i12 -= i9;
                    }
                    j -= (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j3 -= (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j2 -= (long) (iArr2[i12] & Type.ANY);
                    if (i15 < i8) {
                        i7 += i;
                        i15++;
                    }
                    iArr2[i12] = iArr[i7];
                    j4 += (long) ((iArr[i7] >>> 16) & Type.ANY);
                    j7 += (long) ((iArr[i7] >>> 8) & Type.ANY);
                    j8 += (long) (iArr[i7] & Type.ANY);
                    j9 += j4;
                    j6 += j7;
                    j5 += j8;
                    i12 = i14 + 1;
                    if (i12 >= i9) {
                        i12 = 0;
                    }
                    j += (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j3 += (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j2 += (long) (iArr2[i12] & Type.ANY);
                    j4 -= (long) ((iArr2[i12] >>> 16) & Type.ANY);
                    j7 -= (long) ((iArr2[i12] >>> 8) & Type.ANY);
                    j8 -= (long) (iArr2[i12] & Type.ANY);
                    i11++;
                    i14 = i12;
                    i12 = i13;
                }
            }
        }
    }

    public Bitmap m1564a(Bitmap bitmap, float f) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = C0535r.f1946a;
        Collection arrayList = new ArrayList(i);
        Collection arrayList2 = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            arrayList.add(new C0529a(iArr, width, height, (int) f, i, i2, 1));
            arrayList2.add(new C0529a(iArr, width, height, (int) f, i, i2, 2));
        }
        try {
            C0535r.f1947b.invokeAll(arrayList);
            try {
                C0535r.f1947b.invokeAll(arrayList2);
                return Bitmap.createBitmap(iArr, width, height, Config.ARGB_8888);
            } catch (InterruptedException e) {
                return null;
            }
        } catch (InterruptedException e2) {
            return null;
        }
    }
}
