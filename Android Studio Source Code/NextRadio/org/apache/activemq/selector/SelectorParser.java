package org.apache.activemq.selector;

import com.rabbitmq.client.impl.AMQImpl.Queue.Delete;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.jms.InvalidSelectorException;
import org.apache.activemq.filter.ArithmeticExpression;
import org.apache.activemq.filter.BooleanExpression;
import org.apache.activemq.filter.ComparisonExpression;
import org.apache.activemq.filter.ConstantExpression;
import org.apache.activemq.filter.Expression;
import org.apache.activemq.filter.LogicExpression;
import org.apache.activemq.filter.PropertyExpression;
import org.apache.activemq.filter.UnaryExpression;
import org.apache.activemq.util.LRUCache;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class SelectorParser implements SelectorParserConstants {
    private static final String CONVERT_STRING_EXPRESSIONS_PREFIX = "convert_string_expressions:";
    private static final Map cache;
    SimpleCharStream jj_input_stream;
    private int jj_la;
    private Token jj_lastpos;
    private final LookaheadSuccess jj_ls;
    public Token jj_nt;
    private int jj_ntk;
    private Token jj_scanpos;
    private String sql;
    public Token token;
    public SelectorParserTokenManager token_source;

    private static final class LookaheadSuccess extends Error {
        private LookaheadSuccess() {
        }
    }

    static {
        cache = Collections.synchronizedMap(new LRUCache(100));
    }

    public static BooleanExpression parse(String sql) throws InvalidSelectorException {
        Object result = cache.get(sql);
        if (result instanceof InvalidSelectorException) {
            throw ((InvalidSelectorException) result);
        } else if (result instanceof BooleanExpression) {
            return (BooleanExpression) result;
        } else {
            boolean convertStringExpressions = false;
            if (sql.startsWith(CONVERT_STRING_EXPRESSIONS_PREFIX)) {
                convertStringExpressions = true;
                sql = sql.substring(CONVERT_STRING_EXPRESSIONS_PREFIX.length());
            }
            if (convertStringExpressions) {
                ComparisonExpression.CONVERT_STRING_EXPRESSIONS.set(Boolean.valueOf(true));
            }
            try {
                BooleanExpression e = new SelectorParser(sql).parse();
                cache.put(sql, e);
                if (convertStringExpressions) {
                    ComparisonExpression.CONVERT_STRING_EXPRESSIONS.remove();
                }
                return e;
            } catch (InvalidSelectorException t) {
                cache.put(sql, t);
                throw t;
            } catch (Throwable th) {
                if (convertStringExpressions) {
                    ComparisonExpression.CONVERT_STRING_EXPRESSIONS.remove();
                }
            }
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    protected SelectorParser(String sql) {
        this(new StringReader(sql));
        this.sql = sql;
    }

    protected BooleanExpression parse() throws InvalidSelectorException {
        try {
            return JmsSelector();
        } catch (Throwable e) {
            InvalidSelectorException invalidSelectorException = (InvalidSelectorException) new InvalidSelectorException(this.sql).initCause(e);
        }
    }

    private BooleanExpression asBooleanExpression(Expression value) throws ParseException {
        if (value instanceof BooleanExpression) {
            return (BooleanExpression) value;
        }
        if (value instanceof PropertyExpression) {
            return UnaryExpression.createBooleanCast(value);
        }
        throw new ParseException("Expression will not result in a boolean value: " + value);
    }

    public final BooleanExpression JmsSelector() throws ParseException {
        return asBooleanExpression(orExpression());
    }

    public final Expression orExpression() throws ParseException {
        Expression left = andExpression();
        while (true) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Protocol.BBN_RCC_MON /*10*/:
                    jj_consume_token(10);
                    left = LogicExpression.createOR(asBooleanExpression(left), asBooleanExpression(andExpression()));
                default:
                    return left;
            }
        }
    }

    public final Expression andExpression() throws ParseException {
        Expression left = equalityExpression();
        while (true) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Service.DISCARD /*9*/:
                    jj_consume_token(9);
                    left = LogicExpression.createAND(asBooleanExpression(left), asBooleanExpression(equalityExpression()));
                default:
                    return left;
            }
        }
    }

    public final Expression equalityExpression() throws ParseException {
        Expression left = comparisonExpression();
        while (true) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Protocol.XNET /*15*/:
                case Protocol.IRTP /*28*/:
                case Service.MSG_ICP /*29*/:
                    if (this.jj_ntk == -1) {
                        jj_ntk = jj_ntk();
                    } else {
                        jj_ntk = this.jj_ntk;
                    }
                    switch (jj_ntk) {
                        case Protocol.IRTP /*28*/:
                            jj_consume_token(28);
                            left = ComparisonExpression.createEqual(left, comparisonExpression());
                            break;
                        case Service.MSG_ICP /*29*/:
                            jj_consume_token(29);
                            left = ComparisonExpression.createNotEqual(left, comparisonExpression());
                            break;
                        default:
                            if (jj_2_1(2)) {
                                jj_consume_token(15);
                                jj_consume_token(18);
                                left = ComparisonExpression.createIsNull(left);
                                break;
                            }
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case Protocol.XNET /*15*/:
                                    jj_consume_token(15);
                                    jj_consume_token(8);
                                    jj_consume_token(18);
                                    left = ComparisonExpression.createIsNotNull(left);
                                    break;
                                default:
                                    jj_consume_token(-1);
                                    throw new ParseException();
                            }
                    }
                default:
                    return left;
            }
        }
    }

    public final Expression comparisonExpression() throws ParseException {
        Expression left = addExpression();
        while (true) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Protocol.EGP /*8*/:
                case Service.USERS /*11*/:
                case Protocol.PUP /*12*/:
                case Protocol.EMCON /*14*/:
                case Protocol.NETBLT /*30*/:
                case Service.MSG_AUTH /*31*/:
                case Protocol.MERIT_INP /*32*/:
                case Service.DSP /*33*/:
                    if (this.jj_ntk == -1) {
                        jj_ntk = jj_ntk();
                    } else {
                        jj_ntk = this.jj_ntk;
                    }
                    String u;
                    String t;
                    switch (jj_ntk) {
                        case Protocol.PUP /*12*/:
                            u = null;
                            jj_consume_token(12);
                            t = stringLitteral();
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case Service.DAYTIME /*13*/:
                                    jj_consume_token(13);
                                    u = stringLitteral();
                                    break;
                            }
                            left = ComparisonExpression.createLike(left, t, u);
                            break;
                        case Protocol.NETBLT /*30*/:
                            jj_consume_token(30);
                            left = ComparisonExpression.createGreaterThan(left, addExpression());
                            break;
                        case Service.MSG_AUTH /*31*/:
                            jj_consume_token(31);
                            left = ComparisonExpression.createGreaterThanEqual(left, addExpression());
                            break;
                        case Protocol.MERIT_INP /*32*/:
                            jj_consume_token(32);
                            left = ComparisonExpression.createLessThan(left, addExpression());
                            break;
                        case Service.DSP /*33*/:
                            jj_consume_token(33);
                            left = ComparisonExpression.createLessThanEqual(left, addExpression());
                            break;
                        default:
                            if (jj_2_2(2)) {
                                u = null;
                                jj_consume_token(8);
                                jj_consume_token(12);
                                t = stringLitteral();
                                switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                    case Service.DAYTIME /*13*/:
                                        jj_consume_token(13);
                                        u = stringLitteral();
                                        break;
                                }
                                left = ComparisonExpression.createNotLike(left, t, u);
                                break;
                            }
                            Expression low;
                            switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
                                case Service.USERS /*11*/:
                                    jj_consume_token(11);
                                    low = addExpression();
                                    jj_consume_token(9);
                                    left = ComparisonExpression.createBetween(left, low, addExpression());
                                    break;
                                default:
                                    if (jj_2_3(2)) {
                                        jj_consume_token(8);
                                        jj_consume_token(11);
                                        low = addExpression();
                                        jj_consume_token(9);
                                        left = ComparisonExpression.createNotBetween(left, low, addExpression());
                                        break;
                                    }
                                    if (this.jj_ntk == -1) {
                                        jj_ntk = jj_ntk();
                                    } else {
                                        jj_ntk = this.jj_ntk;
                                    }
                                    ArrayList list;
                                    switch (jj_ntk) {
                                        case Protocol.EMCON /*14*/:
                                            jj_consume_token(14);
                                            jj_consume_token(34);
                                            t = stringLitteral();
                                            list = new ArrayList();
                                            list.add(t);
                                            while (true) {
                                                if (this.jj_ntk == -1) {
                                                    jj_ntk = jj_ntk();
                                                } else {
                                                    jj_ntk = this.jj_ntk;
                                                }
                                                switch (jj_ntk) {
                                                    case Type.NAPTR /*35*/:
                                                        jj_consume_token(35);
                                                        list.add(stringLitteral());
                                                    default:
                                                        jj_consume_token(36);
                                                        left = ComparisonExpression.createInFilter(left, list);
                                                        break;
                                                }
                                            }
                                        default:
                                            if (jj_2_4(2)) {
                                                jj_consume_token(8);
                                                jj_consume_token(14);
                                                jj_consume_token(34);
                                                t = stringLitteral();
                                                list = new ArrayList();
                                                list.add(t);
                                                while (true) {
                                                    if (this.jj_ntk == -1) {
                                                        jj_ntk = jj_ntk();
                                                    } else {
                                                        jj_ntk = this.jj_ntk;
                                                    }
                                                    switch (jj_ntk) {
                                                        case Type.NAPTR /*35*/:
                                                            jj_consume_token(35);
                                                            list.add(stringLitteral());
                                                        default:
                                                            jj_consume_token(36);
                                                            left = ComparisonExpression.createNotInFilter(left, list);
                                                            break;
                                                    }
                                                }
                                            }
                                            jj_consume_token(-1);
                                            throw new ParseException();
                                    }
                            }
                    }
                default:
                    return left;
            }
        }
    }

    public final Expression addExpression() throws ParseException {
        Expression left = multExpr();
        while (jj_2_5(Integer.MAX_VALUE)) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Service.TIME /*37*/:
                    jj_consume_token(37);
                    left = ArithmeticExpression.createPlus(left, multExpr());
                    break;
                case Type.A6 /*38*/:
                    jj_consume_token(38);
                    left = ArithmeticExpression.createMinus(left, multExpr());
                    break;
                default:
                    jj_consume_token(-1);
                    throw new ParseException();
            }
        }
        return left;
    }

    public final Expression multExpr() throws ParseException {
        Expression left = unaryExpr();
        while (true) {
            int jj_ntk;
            if (this.jj_ntk == -1) {
                jj_ntk = jj_ntk();
            } else {
                jj_ntk = this.jj_ntk;
            }
            switch (jj_ntk) {
                case Service.RLP /*39*/:
                case Delete.INDEX /*40*/:
                case Service.GRAPHICS /*41*/:
                    if (this.jj_ntk == -1) {
                        jj_ntk = jj_ntk();
                    } else {
                        jj_ntk = this.jj_ntk;
                    }
                    switch (jj_ntk) {
                        case Service.RLP /*39*/:
                            jj_consume_token(39);
                            left = ArithmeticExpression.createMultiply(left, unaryExpr());
                            break;
                        case Delete.INDEX /*40*/:
                            jj_consume_token(40);
                            left = ArithmeticExpression.createDivide(left, unaryExpr());
                            break;
                        case Service.GRAPHICS /*41*/:
                            jj_consume_token(41);
                            left = ArithmeticExpression.createMod(left, unaryExpr());
                            break;
                        default:
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                default:
                    return left;
            }
        }
    }

    public final Expression unaryExpr() throws ParseException {
        if (jj_2_6(Integer.MAX_VALUE)) {
            jj_consume_token(37);
            return unaryExpr();
        }
        int jj_ntk;
        if (this.jj_ntk == -1) {
            jj_ntk = jj_ntk();
        } else {
            jj_ntk = this.jj_ntk;
        }
        switch (jj_ntk) {
            case Protocol.EGP /*8*/:
                jj_consume_token(8);
                return UnaryExpression.createNOT(asBooleanExpression(unaryExpr()));
            case Protocol.CHAOS /*16*/:
            case Service.QUOTE /*17*/:
            case Protocol.MUX /*18*/:
            case Service.FTP /*21*/:
            case Protocol.XNS_IDP /*22*/:
            case Service.TELNET /*23*/:
            case Protocol.TRUNK_2 /*24*/:
            case Protocol.LEAF_2 /*26*/:
            case Service.NSW_FE /*27*/:
            case Type.ATMA /*34*/:
                return primaryExpr();
            case Service.CHARGEN /*19*/:
                jj_consume_token(19);
                return UnaryExpression.createXPath(stringLitteral());
            case Service.FTP_DATA /*20*/:
                jj_consume_token(20);
                return UnaryExpression.createXQuery(stringLitteral());
            case Type.A6 /*38*/:
                jj_consume_token(38);
                return UnaryExpression.createNegate(unaryExpr());
            default:
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final Expression primaryExpr() throws ParseException {
        int jj_ntk;
        if (this.jj_ntk == -1) {
            jj_ntk = jj_ntk();
        } else {
            jj_ntk = this.jj_ntk;
        }
        switch (jj_ntk) {
            case Protocol.CHAOS /*16*/:
            case Service.QUOTE /*17*/:
            case Protocol.MUX /*18*/:
            case Service.FTP /*21*/:
            case Protocol.XNS_IDP /*22*/:
            case Service.TELNET /*23*/:
            case Protocol.TRUNK_2 /*24*/:
            case Protocol.LEAF_2 /*26*/:
                return literal();
            case Service.NSW_FE /*27*/:
                return variable();
            case Type.ATMA /*34*/:
                jj_consume_token(34);
                Expression left = orExpression();
                jj_consume_token(36);
                return left;
            default:
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final ConstantExpression literal() throws ParseException {
        int jj_ntk;
        if (this.jj_ntk == -1) {
            jj_ntk = jj_ntk();
        } else {
            jj_ntk = this.jj_ntk;
        }
        switch (jj_ntk) {
            case Protocol.CHAOS /*16*/:
                jj_consume_token(16);
                return ConstantExpression.TRUE;
            case Service.QUOTE /*17*/:
                jj_consume_token(17);
                return ConstantExpression.FALSE;
            case Protocol.MUX /*18*/:
                jj_consume_token(18);
                return ConstantExpression.NULL;
            case Service.FTP /*21*/:
                return ConstantExpression.createFromDecimal(jj_consume_token(21).image);
            case Protocol.XNS_IDP /*22*/:
                return ConstantExpression.createFromHex(jj_consume_token(22).image);
            case Service.TELNET /*23*/:
                return ConstantExpression.createFromOctal(jj_consume_token(23).image);
            case Protocol.TRUNK_2 /*24*/:
                return ConstantExpression.createFloat(jj_consume_token(24).image);
            case Protocol.LEAF_2 /*26*/:
                return new ConstantExpression(stringLitteral());
            default:
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final String stringLitteral() throws ParseException {
        StringBuffer rc = new StringBuffer();
        String image = jj_consume_token(26).image;
        int i = 1;
        while (i < image.length() - 1) {
            char c = image.charAt(i);
            if (c == '\'') {
                i++;
            }
            rc.append(c);
            i++;
        }
        return rc.toString();
    }

    public final PropertyExpression variable() throws ParseException {
        return new PropertyExpression(jj_consume_token(27).image);
    }

    private boolean jj_2_1(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_1()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_2_2(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_2()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_2_3(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_3()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_2_4(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_4()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_2_5(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_5()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_2_6(int xla) {
        this.jj_la = xla;
        Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            if (jj_3_6()) {
                return false;
            }
            return true;
        } catch (LookaheadSuccess e) {
            return true;
        }
    }

    private boolean jj_3_3() {
        if (jj_scan_token(8) || jj_scan_token(11) || jj_3R_43() || jj_scan_token(9) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_45() {
        if (jj_scan_token(28) || jj_3R_41()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_42() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_45()) {
            this.jj_scanpos = xsp;
            if (jj_3R_46()) {
                this.jj_scanpos = xsp;
                if (jj_3_1()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_47()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_33() {
        if (jj_scan_token(24)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_54() {
        if (jj_scan_token(11) || jj_3R_43() || jj_scan_token(9) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_58() {
        if (jj_scan_token(13) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_32() {
        if (jj_scan_token(23)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_20() {
        if (jj_scan_token(41) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_39() {
        if (jj_3R_41()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_42());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3_2() {
        if (jj_scan_token(8) || jj_scan_token(12) || jj_3R_21()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_59()) {
            this.jj_scanpos = xsp;
        }
        return false;
    }

    private boolean jj_3R_53() {
        if (jj_scan_token(12) || jj_3R_21()) {
            return true;
        }
        Token xsp = this.jj_scanpos;
        if (jj_3R_58()) {
            this.jj_scanpos = xsp;
        }
        return false;
    }

    private boolean jj_3R_31() {
        if (jj_scan_token(22)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_19() {
        if (jj_scan_token(40) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_18() {
        if (jj_scan_token(39) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_11() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_18()) {
            this.jj_scanpos = xsp;
            if (jj_3R_19()) {
                this.jj_scanpos = xsp;
                if (jj_3R_20()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean jj_3R_40() {
        if (jj_scan_token(9) || jj_3R_39()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_30() {
        if (jj_scan_token(21)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_9() {
        if (jj_3R_10()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_11());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_29() {
        if (jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_57() {
        if (jj_scan_token(38) || jj_3R_9()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_37() {
        if (jj_3R_39()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_40());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3_5() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(37)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(38)) {
                return true;
            }
        }
        if (jj_3R_9()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_26() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_29()) {
            this.jj_scanpos = xsp;
            if (jj_3R_30()) {
                this.jj_scanpos = xsp;
                if (jj_3R_31()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_32()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_33()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_34()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_35()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3R_36()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_52() {
        if (jj_scan_token(33) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_56() {
        if (jj_scan_token(37) || jj_3R_9()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_51() {
        if (jj_scan_token(32) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_27() {
        if (jj_scan_token(27)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_61() {
        if (jj_scan_token(35) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_48() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_56()) {
            this.jj_scanpos = xsp;
            if (jj_3R_57()) {
                return true;
            }
        }
        return false;
    }

    private boolean jj_3R_38() {
        if (jj_scan_token(10) || jj_3R_37()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_50() {
        if (jj_scan_token(31) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_25() {
        if (jj_scan_token(34) || jj_3R_28() || jj_scan_token(36)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_24() {
        if (jj_3R_27()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_49() {
        if (jj_scan_token(30) || jj_3R_43()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_44() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_49()) {
            this.jj_scanpos = xsp;
            if (jj_3R_50()) {
                this.jj_scanpos = xsp;
                if (jj_3R_51()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_52()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_53()) {
                            this.jj_scanpos = xsp;
                            if (jj_3_2()) {
                                this.jj_scanpos = xsp;
                                if (jj_3R_54()) {
                                    this.jj_scanpos = xsp;
                                    if (jj_3_3()) {
                                        this.jj_scanpos = xsp;
                                        if (jj_3R_55()) {
                                            this.jj_scanpos = xsp;
                                            if (jj_3_4()) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_23() {
        if (jj_3R_26()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_43() {
        if (jj_3R_9()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_48());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_28() {
        if (jj_3R_37()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_38());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_22() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_23()) {
            this.jj_scanpos = xsp;
            if (jj_3R_24()) {
                this.jj_scanpos = xsp;
                if (jj_3R_25()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean jj_3R_17() {
        if (jj_3R_22()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_21() {
        if (jj_scan_token(26)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_60() {
        if (jj_scan_token(35) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_16() {
        if (jj_scan_token(20) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_41() {
        if (jj_3R_43()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_44());
        this.jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3R_59() {
        if (jj_scan_token(13) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3_4() {
        if (jj_scan_token(8) || jj_scan_token(14) || jj_scan_token(34) || jj_3R_21()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_61());
        this.jj_scanpos = xsp;
        if (jj_scan_token(36)) {
            return true;
        }
        return false;
    }

    private boolean jj_3_6() {
        if (jj_scan_token(37) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_15() {
        if (jj_scan_token(19) || jj_3R_21()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_14() {
        if (jj_scan_token(8) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_36() {
        if (jj_scan_token(18)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_12() {
        if (jj_scan_token(37) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_55() {
        if (jj_scan_token(14) || jj_scan_token(34) || jj_3R_21()) {
            return true;
        }
        Token xsp;
        do {
            xsp = this.jj_scanpos;
        } while (!jj_3R_60());
        this.jj_scanpos = xsp;
        if (jj_scan_token(36)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_47() {
        if (jj_scan_token(15) || jj_scan_token(8) || jj_scan_token(18)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_13() {
        if (jj_scan_token(38) || jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_35() {
        if (jj_scan_token(17)) {
            return true;
        }
        return false;
    }

    private boolean jj_3_1() {
        if (jj_scan_token(15) || jj_scan_token(18)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_10() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_12()) {
            this.jj_scanpos = xsp;
            if (jj_3R_13()) {
                this.jj_scanpos = xsp;
                if (jj_3R_14()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_15()) {
                        this.jj_scanpos = xsp;
                        if (jj_3R_16()) {
                            this.jj_scanpos = xsp;
                            if (jj_3R_17()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_34() {
        if (jj_scan_token(16)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_46() {
        if (jj_scan_token(29) || jj_3R_41()) {
            return true;
        }
        return false;
    }

    public SelectorParser(InputStream stream) {
        this(stream, null);
    }

    public SelectorParser(InputStream stream, String encoding) {
        this.jj_ls = new LookaheadSuccess();
        try {
            this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
            this.token_source = new SelectorParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReInit(InputStream stream) {
        ReInit(stream, null);
    }

    public void ReInit(InputStream stream, String encoding) {
        try {
            this.jj_input_stream.ReInit(stream, encoding, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SelectorParser(Reader stream) {
        this.jj_ls = new LookaheadSuccess();
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new SelectorParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
    }

    public void ReInit(Reader stream) {
        this.jj_input_stream.ReInit(stream, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
    }

    public SelectorParser(SelectorParserTokenManager tm) {
        this.jj_ls = new LookaheadSuccess();
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
    }

    public void ReInit(SelectorParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if (oldToken.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            return this.token;
        }
        this.token = oldToken;
        throw generateParseException();
    }

    private boolean jj_scan_token(int kind) {
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            Token token;
            if (this.jj_scanpos.next == null) {
                token = this.jj_scanpos;
                Token nextToken = this.token_source.getNextToken();
                token.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            } else {
                token = this.jj_scanpos.next;
                this.jj_scanpos = token;
                this.jj_lastpos = token;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_scanpos.kind != kind) {
            return true;
        }
        if (this.jj_la != 0 || this.jj_scanpos != this.jj_lastpos) {
            return false;
        }
        throw this.jj_ls;
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            Token token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        return this.token;
    }

    public final Token getToken(int index) {
        int i = 0;
        Token t = this.token;
        while (i < index) {
            Token t2;
            if (t.next != null) {
                t2 = t.next;
            } else {
                t2 = this.token_source.getNextToken();
                t.next = t2;
            }
            i++;
            t = t2;
        }
        return t;
    }

    private int jj_ntk() {
        Token token = this.token.next;
        this.jj_nt = token;
        if (token == null) {
            token = this.token;
            Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            int i = nextToken.kind;
            this.jj_ntk = i;
            return i;
        }
        i = this.jj_nt.kind;
        this.jj_ntk = i;
        return i;
    }

    public ParseException generateParseException() {
        Token errortok = this.token.next;
        int line = errortok.beginLine;
        return new ParseException("Parse error at line " + line + ", column " + errortok.beginColumn + ".  Encountered: " + (errortok.kind == 0 ? tokenImage[0] : errortok.image));
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }
}
