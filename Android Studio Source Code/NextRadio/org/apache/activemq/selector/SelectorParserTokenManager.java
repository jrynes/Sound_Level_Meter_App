package org.apache.activemq.selector;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import com.google.android.gms.location.places.Place;
import com.rabbitmq.client.impl.AMQImpl.Basic;
import com.rabbitmq.client.impl.AMQImpl.Basic.Ack;
import com.rabbitmq.client.impl.AMQImpl.Basic.Get;
import com.rabbitmq.client.impl.AMQImpl.Basic.Nack;
import com.rabbitmq.client.impl.AMQImpl.Basic.Recover;
import com.rabbitmq.client.impl.AMQImpl.Confirm;
import com.rabbitmq.client.impl.AMQImpl.Queue.Delete;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.filter.DestinationFilter;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class SelectorParserTokenManager implements SelectorParserConstants {
    static final long[] jjbitVec0;
    static final long[] jjbitVec2;
    static final int[] jjnextStates;
    public static final String[] jjstrLiteralImages;
    static final long[] jjtoSkip;
    static final long[] jjtoSpecial;
    static final long[] jjtoToken;
    public static final String[] lexStateNames;
    protected char curChar;
    int curLexState;
    public PrintStream debugStream;
    int defaultLexState;
    protected SimpleCharStream input_stream;
    int jjmatchedKind;
    int jjmatchedPos;
    int jjnewStateCnt;
    int jjround;
    private final int[] jjrounds;
    private final int[] jjstateSet;

    public void setDebugStream(PrintStream ds) {
        this.debugStream = ds;
    }

    private int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case Service.DISCARD /*9*/:
                this.jjmatchedKind = 2;
                return jjMoveNfa_0(5, 0);
            case Protocol.BBN_RCC_MON /*10*/:
                this.jjmatchedKind = 3;
                return jjMoveNfa_0(5, 0);
            case Protocol.PUP /*12*/:
                this.jjmatchedKind = 5;
                return jjMoveNfa_0(5, 0);
            case Service.DAYTIME /*13*/:
                this.jjmatchedKind = 4;
                return jjMoveNfa_0(5, 0);
            case Protocol.MERIT_INP /*32*/:
                this.jjmatchedKind = 1;
                return jjMoveNfa_0(5, 0);
            case Service.TIME /*37*/:
                this.jjmatchedKind = 41;
                return jjMoveNfa_0(5, 0);
            case Delete.INDEX /*40*/:
                this.jjmatchedKind = 34;
                return jjMoveNfa_0(5, 0);
            case Service.GRAPHICS /*41*/:
                this.jjmatchedKind = 36;
                return jjMoveNfa_0(5, 0);
            case Service.NAMESERVER /*42*/:
                this.jjmatchedKind = 39;
                return jjMoveNfa_0(5, 0);
            case Service.NICNAME /*43*/:
                this.jjmatchedKind = 37;
                return jjMoveNfa_0(5, 0);
            case Service.MPM_FLAGS /*44*/:
                this.jjmatchedKind = 35;
                return jjMoveNfa_0(5, 0);
            case Service.MPM /*45*/:
                this.jjmatchedKind = 38;
                return jjMoveNfa_0(5, 0);
            case Service.NI_FTP /*47*/:
                this.jjmatchedKind = 40;
                return jjMoveNfa_0(5, 0);
            case Basic.INDEX /*60*/:
                this.jjmatchedKind = 32;
                return jjMoveStringLiteralDfa1_0(9126805504L);
            case Service.NI_MAIL /*61*/:
                this.jjmatchedKind = 28;
                return jjMoveNfa_0(5, 0);
            case Protocol.CFTP /*62*/:
                this.jjmatchedKind = 30;
                return jjMoveStringLiteralDfa1_0(2147483648L);
            case Service.TACACS_DS /*65*/:
                return jjMoveStringLiteralDfa1_0(512);
            case Protocol.RVD /*66*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
            case Service.TFTP /*69*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_URI);
            case Get.INDEX /*70*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
            case Service.NETRJS_3 /*73*/:
                return jjMoveStringLiteralDfa1_0(49152);
            case Protocol.BR_SAT_MON /*76*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
            case Protocol.WB_MON /*78*/:
                return jjMoveStringLiteralDfa1_0(262400);
            case Service.FINGER /*79*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            case Place.TYPE_SHOPPING_MALL /*84*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
            case Place.TYPE_STORE /*88*/:
                return jjMoveStringLiteralDfa1_0(1572864);
            case Service.SWIFT_RVF /*97*/:
                return jjMoveStringLiteralDfa1_0(512);
            case Service.TACNEWS /*98*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
            case Service.HOSTNAME /*101*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_URI);
            case Service.ISO_TSAP /*102*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
            case Service.CSNET_NS /*105*/:
                return jjMoveStringLiteralDfa1_0(49152);
            case 'l':
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
            case Recover.INDEX /*110*/:
                return jjMoveStringLiteralDfa1_0(262400);
            case Service.SUNRPC /*111*/:
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
            case 't':
                return jjMoveStringLiteralDfa1_0(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
            case Nack.INDEX /*120*/:
                return jjMoveStringLiteralDfa1_0(1572864);
            default:
                return jjMoveNfa_0(5, 0);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Service.NI_MAIL /*61*/:
                    if ((2147483648L & active0) == 0) {
                        if ((8589934592L & active0) != 0) {
                            this.jjmatchedKind = 33;
                            this.jjmatchedPos = 1;
                            break;
                        }
                    }
                    this.jjmatchedKind = 31;
                    this.jjmatchedPos = 1;
                    break;
                    break;
                case Protocol.CFTP /*62*/:
                    if ((536870912 & active0) != 0) {
                        this.jjmatchedKind = 29;
                        this.jjmatchedPos = 1;
                        break;
                    }
                    break;
                case Service.TACACS_DS /*65*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
                case Service.TFTP /*69*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.NETRJS_3 /*73*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
                case Protocol.WB_MON /*78*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE & active0) != 0) {
                        this.jjmatchedKind = 14;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 512);
                case Service.FINGER /*79*/:
                    return jjMoveStringLiteralDfa2_0(active0, 256);
                case Ack.INDEX /*80*/:
                    return jjMoveStringLiteralDfa2_0(active0, 524288);
                case Service.HOSTS2_NS /*81*/:
                    return jjMoveStringLiteralDfa2_0(active0, 1048576);
                case Place.TYPE_SCHOOL /*82*/:
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID & active0) != 0) {
                        this.jjmatchedKind = 10;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
                case Place.TYPE_SHOE_STORE /*83*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID & active0) != 0) {
                        this.jjmatchedKind = 15;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Confirm.INDEX /*85*/:
                    return jjMoveStringLiteralDfa2_0(active0, 262144);
                case Service.SWIFT_RVF /*97*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
                case Service.HOSTNAME /*101*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.CSNET_NS /*105*/:
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
                case Recover.INDEX /*110*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE & active0) != 0) {
                        this.jjmatchedKind = 14;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, 512);
                case Service.SUNRPC /*111*/:
                    return jjMoveStringLiteralDfa2_0(active0, 256);
                case 'p':
                    return jjMoveStringLiteralDfa2_0(active0, 524288);
                case Service.AUTH /*113*/:
                    return jjMoveStringLiteralDfa2_0(active0, 1048576);
                case 'r':
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID & active0) != 0) {
                        this.jjmatchedKind = 10;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
                case Service.SFTP /*115*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID & active0) != 0) {
                        this.jjmatchedKind = 15;
                        this.jjmatchedPos = 1;
                    }
                    return jjMoveStringLiteralDfa2_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Service.UUCP_PATH /*117*/:
                    return jjMoveStringLiteralDfa2_0(active0, 262144);
            }
            return jjMoveNfa_0(5, 1);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 0);
        }
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        active0 &= old0;
        if (active0 == 0) {
            return jjMoveNfa_0(5, 1);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Service.TACACS_DS /*65*/:
                    return jjMoveStringLiteralDfa3_0(active0, 524288);
                case Service.BOOTPS /*67*/:
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Service.BOOTPC /*68*/:
                    if ((512 & active0) != 0) {
                        this.jjmatchedKind = 9;
                        this.jjmatchedPos = 2;
                        break;
                    }
                    break;
                case Place.TYPE_PLUMBER /*75*/:
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
                case Protocol.BR_SAT_MON /*76*/:
                    return jjMoveStringLiteralDfa3_0(active0, 393216);
                case Place.TYPE_SHOPPING_MALL /*84*/:
                    if ((256 & active0) != 0) {
                        this.jjmatchedKind = 8;
                        this.jjmatchedPos = 2;
                    }
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Confirm.INDEX /*85*/:
                    return jjMoveStringLiteralDfa3_0(active0, 1114112);
                case Service.SWIFT_RVF /*97*/:
                    return jjMoveStringLiteralDfa3_0(active0, 524288);
                case Service.METAGRAM /*99*/:
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                    if ((512 & active0) != 0) {
                        this.jjmatchedKind = 9;
                        this.jjmatchedPos = 2;
                        break;
                    }
                    break;
                case Service.RTELNET /*107*/:
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM);
                case 'l':
                    return jjMoveStringLiteralDfa3_0(active0, 393216);
                case 't':
                    if ((256 & active0) != 0) {
                        this.jjmatchedKind = 8;
                        this.jjmatchedPos = 2;
                    }
                    return jjMoveStringLiteralDfa3_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.UUCP_PATH /*117*/:
                    return jjMoveStringLiteralDfa3_0(active0, 1114112);
            }
            return jjMoveNfa_0(5, 2);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 1);
        }
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        active0 &= old0;
        if (active0 == 0) {
            return jjMoveNfa_0(5, 2);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Service.TACACS_DS /*65*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Service.TFTP /*69*/:
                    if ((PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM & active0) != 0) {
                        this.jjmatchedKind = 12;
                        this.jjmatchedPos = 3;
                    } else if ((PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH & active0) != 0) {
                        this.jjmatchedKind = 16;
                        this.jjmatchedPos = 3;
                    }
                    return jjMoveStringLiteralDfa4_0(active0, 1048576);
                case Protocol.BR_SAT_MON /*76*/:
                    if ((262144 & active0) != 0) {
                        this.jjmatchedKind = 18;
                        this.jjmatchedPos = 3;
                        break;
                    }
                    break;
                case Place.TYPE_SHOE_STORE /*83*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
                case Place.TYPE_SHOPPING_MALL /*84*/:
                    return jjMoveStringLiteralDfa4_0(active0, 524288);
                case Place.TYPE_STORAGE /*87*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.SWIFT_RVF /*97*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Service.HOSTNAME /*101*/:
                    if ((PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM & active0) != 0) {
                        this.jjmatchedKind = 12;
                        this.jjmatchedPos = 3;
                    } else if ((PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH & active0) != 0) {
                        this.jjmatchedKind = 16;
                        this.jjmatchedPos = 3;
                    }
                    return jjMoveStringLiteralDfa4_0(active0, 1048576);
                case 'l':
                    if ((262144 & active0) != 0) {
                        this.jjmatchedKind = 18;
                        this.jjmatchedPos = 3;
                        break;
                    }
                    break;
                case Service.SFTP /*115*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PREPARE_FROM_URI);
                case 't':
                    return jjMoveStringLiteralDfa4_0(active0, 524288);
                case Service.NNTP /*119*/:
                    return jjMoveStringLiteralDfa4_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
            }
            return jjMoveNfa_0(5, 3);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 2);
        }
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        active0 &= old0;
        if (active0 == 0) {
            return jjMoveNfa_0(5, 3);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Service.TFTP /*69*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE_FROM_URI & active0) != 0) {
                        this.jjmatchedKind = 17;
                        this.jjmatchedPos = 4;
                    }
                    return jjMoveStringLiteralDfa5_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.NETRJS_2 /*72*/:
                    if ((524288 & active0) != 0) {
                        this.jjmatchedKind = 19;
                        this.jjmatchedPos = 4;
                        break;
                    }
                    break;
                case Ack.INDEX /*80*/:
                    return jjMoveStringLiteralDfa5_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case Place.TYPE_SCHOOL /*82*/:
                    return jjMoveStringLiteralDfa5_0(active0, 1048576);
                case Service.HOSTNAME /*101*/:
                    if ((PlaybackStateCompat.ACTION_PREPARE_FROM_URI & active0) != 0) {
                        this.jjmatchedKind = 17;
                        this.jjmatchedPos = 4;
                    }
                    return jjMoveStringLiteralDfa5_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.X400_SND /*104*/:
                    if ((524288 & active0) != 0) {
                        this.jjmatchedKind = 19;
                        this.jjmatchedPos = 4;
                        break;
                    }
                    break;
                case 'p':
                    return jjMoveStringLiteralDfa5_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
                case 'r':
                    return jjMoveStringLiteralDfa5_0(active0, 1048576);
            }
            return jjMoveNfa_0(5, 4);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 3);
        }
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        active0 &= old0;
        if (active0 == 0) {
            return jjMoveNfa_0(5, 4);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Service.TFTP /*69*/:
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_URI & active0) != 0) {
                        this.jjmatchedKind = 13;
                        this.jjmatchedPos = 5;
                    }
                    return jjMoveStringLiteralDfa6_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.SU_MIT_TG /*89*/:
                    if ((1048576 & active0) != 0) {
                        this.jjmatchedKind = 20;
                        this.jjmatchedPos = 5;
                        break;
                    }
                    break;
                case Service.HOSTNAME /*101*/:
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_URI & active0) != 0) {
                        this.jjmatchedKind = 13;
                        this.jjmatchedPos = 5;
                    }
                    return jjMoveStringLiteralDfa6_0(active0, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                case Service.ERPC /*121*/:
                    if ((1048576 & active0) != 0) {
                        this.jjmatchedKind = 20;
                        this.jjmatchedPos = 5;
                        break;
                    }
                    break;
            }
            return jjMoveNfa_0(5, 5);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 4);
        }
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        active0 &= old0;
        if (active0 == 0) {
            return jjMoveNfa_0(5, 5);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Protocol.WB_MON /*78*/:
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH & active0) != 0) {
                        this.jjmatchedKind = 11;
                        this.jjmatchedPos = 6;
                        break;
                    }
                    break;
                case Recover.INDEX /*110*/:
                    if ((PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH & active0) != 0) {
                        this.jjmatchedKind = 11;
                        this.jjmatchedPos = 6;
                        break;
                    }
                    break;
            }
            return jjMoveNfa_0(5, 6);
        } catch (IOException e) {
            return jjMoveNfa_0(5, 5);
        }
    }

    static {
        jjbitVec0 = new long[]{-2, -1, -1, -1};
        jjbitVec2 = new long[]{0, 0, -1, -1};
        jjnextStates = new int[]{29, 30, 35, 36, 23, 24, 25, 1, 2, 4, 8, 9, 11, 19, 20, 33, 34, 37, 38};
        jjstrLiteralImages = new String[]{Stomp.EMPTY, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "=", "<>", DestinationFilter.ANY_DESCENDENT, ">=", "<", "<=", "(", Stomp.COMMA, ")", "+", "-", DestinationFilter.ANY_CHILD, ReadOnlyContext.SEPARATOR, "%"};
        lexStateNames = new String[]{"DEFAULT"};
        jjtoToken = new long[]{4398012956417L};
        jjtoSkip = new long[]{254};
        jjtoSpecial = new long[]{62};
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int strKind = this.jjmatchedKind;
        int strPos = this.jjmatchedPos;
        int seenUpto = curPos + 1;
        this.input_stream.backup(seenUpto);
        try {
            this.curChar = this.input_stream.readChar();
            curPos = 0;
            int startsAt = 0;
            this.jjnewStateCnt = 43;
            int i = 1;
            this.jjstateSet[0] = startState;
            int kind = Integer.MAX_VALUE;
            while (true) {
                int i2 = this.jjround + 1;
                this.jjround = i2;
                if (i2 == Integer.MAX_VALUE) {
                    ReInitRounds();
                }
                char c = this.curChar;
                long l;
                if (r0 >= '@') {
                    c = this.curChar;
                    if (r0 >= '\u0080') {
                        int hiByte = this.curChar >> 8;
                        int i1 = hiByte >> 6;
                        long l1 = 1 << (hiByte & 63);
                        int i22 = (this.curChar & Type.ANY) >> 6;
                        long l2 = 1 << (this.curChar & 63);
                        do {
                            i--;
                            switch (this.jjstateSet[i]) {
                                case Zone.PRIMARY /*1*/:
                                    if (jjCanMove_0(hiByte, i1, i22, l1, l2)) {
                                        jjAddStates(7, 9);
                                        break;
                                    }
                                    break;
                                case Service.ECHO /*7*/:
                                    if (jjCanMove_0(hiByte, i1, i22, l1, l2)) {
                                        jjCheckNAddTwoStates(7, 8);
                                        break;
                                    }
                                    break;
                                case Service.DISCARD /*9*/:
                                case Protocol.BBN_RCC_MON /*10*/:
                                    if (jjCanMove_0(hiByte, i1, i22, l1, l2)) {
                                        jjCheckNAddTwoStates(10, 8);
                                        break;
                                    }
                                    break;
                                case Protocol.TRUNK_2 /*24*/:
                                    if (jjCanMove_0(hiByte, i1, i22, l1, l2)) {
                                        jjAddStates(4, 6);
                                        break;
                                    }
                                    break;
                            }
                        } while (i != startsAt);
                    }
                    l = 1 << (this.curChar & 63);
                    do {
                        i--;
                        switch (this.jjstateSet[i]) {
                            case Zone.PRIMARY /*1*/:
                                jjAddStates(7, 9);
                                break;
                            case Service.RJE /*5*/:
                            case Service.NSW_FE /*27*/:
                                if ((576460745995190270L & l) != 0) {
                                    if (kind > 27) {
                                        kind = 27;
                                    }
                                    jjCheckNAdd(27);
                                    break;
                                }
                                break;
                            case Service.ECHO /*7*/:
                                jjCheckNAddTwoStates(7, 8);
                                break;
                            case Service.DISCARD /*9*/:
                            case Protocol.BBN_RCC_MON /*10*/:
                                jjCheckNAddTwoStates(10, 8);
                                break;
                            case Protocol.XNET /*15*/:
                                if ((17592186048512L & l) != 0 && kind > 21) {
                                    kind = 21;
                                    break;
                                }
                            case Protocol.MUX /*18*/:
                                if ((137438953504L & l) != 0) {
                                    jjAddStates(13, 14);
                                    break;
                                }
                                break;
                            case Protocol.TRUNK_2 /*24*/:
                                jjAddStates(4, 6);
                                break;
                            case Protocol.MERIT_INP /*32*/:
                                if ((137438953504L & l) != 0) {
                                    jjAddStates(15, 16);
                                    break;
                                }
                                break;
                            case Type.KX /*36*/:
                                if ((137438953504L & l) != 0) {
                                    jjAddStates(17, 18);
                                    break;
                                }
                                break;
                            case Delete.INDEX /*40*/:
                                if ((72057594054705152L & l) != 0) {
                                    jjCheckNAdd(41);
                                    break;
                                }
                                break;
                            case Service.GRAPHICS /*41*/:
                                if ((541165879422L & l) != 0) {
                                    if (kind > 22) {
                                        kind = 22;
                                    }
                                    jjCheckNAdd(41);
                                    break;
                                }
                                break;
                        }
                    } while (i != startsAt);
                }
                l = 1 << this.curChar;
                do {
                    i--;
                    int[] iArr;
                    int i3;
                    switch (this.jjstateSet[i]) {
                        case Tokenizer.EOF /*0*/:
                            c = this.curChar;
                            if (r0 == '-') {
                                jjCheckNAddStates(7, 9);
                                break;
                            }
                            break;
                        case Zone.PRIMARY /*1*/:
                            if ((-9217 & l) != 0) {
                                jjCheckNAddStates(7, 9);
                                break;
                            }
                            break;
                        case Zone.SECONDARY /*2*/:
                            if ((9216 & l) != 0 && kind > 6) {
                                kind = 6;
                                break;
                            }
                        case Protocol.GGP /*3*/:
                            c = this.curChar;
                            if (r0 == '\n' && kind > 6) {
                                kind = 6;
                                break;
                            }
                        case Type.MF /*4*/:
                            c = this.curChar;
                            if (r0 == '\r') {
                                iArr = this.jjstateSet;
                                i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 3;
                                break;
                            }
                            break;
                        case Service.RJE /*5*/:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddStates(0, 3);
                            } else {
                                c = this.curChar;
                                if (r0 == '$') {
                                    if (kind > 27) {
                                        kind = 27;
                                    }
                                    jjCheckNAdd(27);
                                } else {
                                    c = this.curChar;
                                    if (r0 == '\'') {
                                        jjCheckNAddStates(4, 6);
                                    } else {
                                        c = this.curChar;
                                        if (r0 == '.') {
                                            jjCheckNAdd(17);
                                        } else {
                                            c = this.curChar;
                                            if (r0 == '/') {
                                                iArr = this.jjstateSet;
                                                i3 = this.jjnewStateCnt;
                                                this.jjnewStateCnt = i3 + 1;
                                                iArr[i3] = 6;
                                            } else {
                                                c = this.curChar;
                                                if (r0 == '-') {
                                                    iArr = this.jjstateSet;
                                                    i3 = this.jjnewStateCnt;
                                                    this.jjnewStateCnt = i3 + 1;
                                                    iArr[i3] = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if ((287667426198290432L & l) == 0) {
                                c = this.curChar;
                                if (r0 == '0') {
                                    if (kind > 23) {
                                        kind = 23;
                                    }
                                    jjCheckNAddTwoStates(40, 42);
                                    break;
                                }
                            }
                            if (kind > 21) {
                                kind = 21;
                            }
                            jjCheckNAddTwoStates(14, 15);
                            break;
                            break;
                        case Protocol.TCP /*6*/:
                            c = this.curChar;
                            if (r0 == '*') {
                                jjCheckNAddTwoStates(7, 8);
                                break;
                            }
                            break;
                        case Service.ECHO /*7*/:
                            if ((-4398046511105L & l) != 0) {
                                jjCheckNAddTwoStates(7, 8);
                                break;
                            }
                            break;
                        case Protocol.EGP /*8*/:
                            c = this.curChar;
                            if (r0 == '*') {
                                jjCheckNAddStates(10, 12);
                                break;
                            }
                            break;
                        case Service.DISCARD /*9*/:
                            if ((-145135534866433L & l) != 0) {
                                jjCheckNAddTwoStates(10, 8);
                                break;
                            }
                            break;
                        case Protocol.BBN_RCC_MON /*10*/:
                            if ((-4398046511105L & l) != 0) {
                                jjCheckNAddTwoStates(10, 8);
                                break;
                            }
                            break;
                        case Service.USERS /*11*/:
                            c = this.curChar;
                            if (r0 == '/' && kind > 7) {
                                kind = 7;
                                break;
                            }
                        case Protocol.PUP /*12*/:
                            c = this.curChar;
                            if (r0 == '/') {
                                iArr = this.jjstateSet;
                                i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 6;
                                break;
                            }
                            break;
                        case Service.DAYTIME /*13*/:
                            if ((287667426198290432L & l) != 0) {
                                if (kind > 21) {
                                    kind = 21;
                                }
                                jjCheckNAddTwoStates(14, 15);
                                break;
                            }
                            break;
                        case Protocol.EMCON /*14*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 21) {
                                    kind = 21;
                                }
                                jjCheckNAddTwoStates(14, 15);
                                break;
                            }
                            break;
                        case Protocol.CHAOS /*16*/:
                            c = this.curChar;
                            if (r0 == '.') {
                                jjCheckNAdd(17);
                                break;
                            }
                            break;
                        case Service.QUOTE /*17*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAddTwoStates(17, 18);
                                break;
                            }
                            break;
                        case Service.CHARGEN /*19*/:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(20);
                                break;
                            }
                            break;
                        case Service.FTP_DATA /*20*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAdd(20);
                                break;
                            }
                            break;
                        case Service.FTP /*21*/:
                        case Protocol.XNS_IDP /*22*/:
                            c = this.curChar;
                            if (r0 == '\'') {
                                jjCheckNAddStates(4, 6);
                                break;
                            }
                            break;
                        case Service.TELNET /*23*/:
                            c = this.curChar;
                            if (r0 == '\'') {
                                iArr = this.jjstateSet;
                                i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 22;
                                break;
                            }
                            break;
                        case Protocol.TRUNK_2 /*24*/:
                            if ((-549755813889L & l) != 0) {
                                jjCheckNAddStates(4, 6);
                                break;
                            }
                            break;
                        case Service.SMTP /*25*/:
                            c = this.curChar;
                            if (r0 == '\'' && kind > 26) {
                                kind = 26;
                                break;
                            }
                        case Protocol.LEAF_2 /*26*/:
                            c = this.curChar;
                            if (r0 == '$') {
                                if (kind > 27) {
                                    kind = 27;
                                }
                                jjCheckNAdd(27);
                                break;
                            }
                            break;
                        case Service.NSW_FE /*27*/:
                            if ((287948969894477824L & l) != 0) {
                                if (kind > 27) {
                                    kind = 27;
                                }
                                jjCheckNAdd(27);
                                break;
                            }
                            break;
                        case Protocol.IRTP /*28*/:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddStates(0, 3);
                                break;
                            }
                            break;
                        case Service.MSG_ICP /*29*/:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(29, 30);
                                break;
                            }
                            break;
                        case Protocol.NETBLT /*30*/:
                            c = this.curChar;
                            if (r0 == '.') {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAddTwoStates(31, 32);
                                break;
                            }
                            break;
                        case Service.MSG_AUTH /*31*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAddTwoStates(31, 32);
                                break;
                            }
                            break;
                        case Service.DSP /*33*/:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(34);
                                break;
                            }
                            break;
                        case Type.ATMA /*34*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAdd(34);
                                break;
                            }
                            break;
                        case Type.NAPTR /*35*/:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(35, 36);
                                break;
                            }
                            break;
                        case Service.TIME /*37*/:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(38);
                                break;
                            }
                            break;
                        case Type.A6 /*38*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 24) {
                                    kind = 24;
                                }
                                jjCheckNAdd(38);
                                break;
                            }
                            break;
                        case Service.RLP /*39*/:
                            c = this.curChar;
                            if (r0 == '0') {
                                if (kind > 23) {
                                    kind = 23;
                                }
                                jjCheckNAddTwoStates(40, 42);
                                break;
                            }
                            break;
                        case Service.GRAPHICS /*41*/:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 22) {
                                    kind = 22;
                                }
                                iArr = this.jjstateSet;
                                i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 41;
                                break;
                            }
                            break;
                        case Service.NAMESERVER /*42*/:
                            if ((71776119061217280L & l) != 0) {
                                if (kind > 23) {
                                    kind = 23;
                                }
                                jjCheckNAdd(42);
                                break;
                            }
                            break;
                    }
                } while (i != startsAt);
                if (kind != Integer.MAX_VALUE) {
                    this.jjmatchedKind = kind;
                    this.jjmatchedPos = curPos;
                    kind = Integer.MAX_VALUE;
                }
                curPos++;
                i = this.jjnewStateCnt;
                this.jjnewStateCnt = startsAt;
                startsAt = 43 - startsAt;
                if (i != startsAt) {
                    try {
                        this.curChar = this.input_stream.readChar();
                    } catch (IOException e) {
                    }
                }
                i2 = this.jjmatchedPos;
                if (r0 > strPos) {
                    return curPos;
                }
                int toRet = Math.max(curPos, seenUpto);
                if (curPos < toRet) {
                    int i4 = toRet - Math.min(curPos, seenUpto);
                    while (true) {
                        i = i4 - 1;
                        if (i4 > 0) {
                            try {
                                this.curChar = this.input_stream.readChar();
                                i4 = i;
                            } catch (IOException e2) {
                                throw new Error("Internal Error : Please send a bug report.");
                            }
                        }
                    }
                }
                i2 = this.jjmatchedPos;
                if (r0 < strPos) {
                    this.jjmatchedKind = strKind;
                    this.jjmatchedPos = strPos;
                } else {
                    i2 = this.jjmatchedPos;
                    if (r0 == strPos) {
                        i2 = this.jjmatchedKind;
                        if (r0 > strKind) {
                            this.jjmatchedKind = strKind;
                        }
                    }
                }
                return toRet;
            }
        } catch (IOException e3) {
            throw new Error("Internal Error");
        }
    }

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
        switch (hiByte) {
            case Tokenizer.EOF /*0*/:
                if ((jjbitVec2[i2] & l2) == 0) {
                    return false;
                }
                return true;
            default:
                if ((jjbitVec0[i1] & l1) != 0) {
                    return true;
                }
                return false;
        }
    }

    public SelectorParserTokenManager(SimpleCharStream stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[43];
        this.jjstateSet = new int[86];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = stream;
    }

    public SelectorParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        this.jjround = -2147483647;
        int i = 43;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                this.jjrounds[i2] = ExploreByTouchHelper.INVALID_ID;
                i = i2;
            } else {
                return;
            }
        }
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) {
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }

    protected Token jjFillToken() {
        String curTokenImage;
        String im = jjstrLiteralImages[this.jjmatchedKind];
        if (im == null) {
            curTokenImage = this.input_stream.GetImage();
        } else {
            curTokenImage = im;
        }
        int beginLine = this.input_stream.getBeginLine();
        int beginColumn = this.input_stream.getBeginColumn();
        int endLine = this.input_stream.getEndLine();
        int endColumn = this.input_stream.getEndColumn();
        Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    public Token getNextToken() {
        Token matchedToken;
        Token specialToken = null;
        while (true) {
            int curPos;
            try {
                this.curChar = this.input_stream.BeginToken();
                this.jjmatchedKind = Integer.MAX_VALUE;
                this.jjmatchedPos = 0;
                curPos = jjMoveStringLiteralDfa0_0();
                if (this.jjmatchedKind == Integer.MAX_VALUE) {
                    break;
                }
                if (this.jjmatchedPos + 1 < curPos) {
                    this.input_stream.backup((curPos - this.jjmatchedPos) - 1);
                }
                if ((jjtoToken[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                    matchedToken = jjFillToken();
                    matchedToken.specialToken = specialToken;
                    return matchedToken;
                } else if ((jjtoSpecial[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                    matchedToken = jjFillToken();
                    if (specialToken == null) {
                        specialToken = matchedToken;
                    } else {
                        matchedToken.specialToken = specialToken;
                        specialToken.next = matchedToken;
                        specialToken = matchedToken;
                    }
                }
            } catch (IOException e) {
                this.jjmatchedKind = 0;
                matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
        }
        int error_line = this.input_stream.getEndLine();
        int error_column = this.input_stream.getEndColumn();
        String error_after = null;
        boolean EOFSeen = false;
        try {
            this.input_stream.readChar();
            this.input_stream.backup(1);
        } catch (IOException e2) {
            EOFSeen = true;
            error_after = curPos <= 1 ? Stomp.EMPTY : this.input_stream.GetImage();
            if (this.curChar == '\n' || this.curChar == '\r') {
                error_line++;
                error_column = 0;
            } else {
                error_column++;
            }
        }
        if (!EOFSeen) {
            this.input_stream.backup(1);
            error_after = curPos <= 1 ? Stomp.EMPTY : this.input_stream.GetImage();
        }
        throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
    }

    private void jjCheckNAdd(int state) {
        if (this.jjrounds[state] != this.jjround) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        while (true) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = jjnextStates[start];
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        while (true) {
            jjCheckNAdd(jjnextStates[start]);
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }
}
