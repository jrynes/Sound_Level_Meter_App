package org.apache.activemq.selector;

import com.google.android.gms.location.places.Place;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class ParseException extends Exception {
    private static final long serialVersionUID = 1;
    public Token currentToken;
    protected String eol;
    public int[][] expectedTokenSequences;
    public String[] tokenImage;

    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
        this.eol = System.getProperty("line.separator", Stomp.NEWLINE);
        this.currentToken = currentTokenVal;
        this.expectedTokenSequences = expectedTokenSequencesVal;
        this.tokenImage = tokenImageVal;
    }

    public ParseException() {
        this.eol = System.getProperty("line.separator", Stomp.NEWLINE);
    }

    public ParseException(String message) {
        super(message);
        this.eol = System.getProperty("line.separator", Stomp.NEWLINE);
    }

    private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
        int i;
        String eol = System.getProperty("line.separator", Stomp.NEWLINE);
        StringBuffer expected = new StringBuffer();
        int maxSize = 0;
        for (i = 0; i < expectedTokenSequences.length; i++) {
            if (maxSize < expectedTokenSequences[i].length) {
                maxSize = expectedTokenSequences[i].length;
            }
            for (int i2 : expectedTokenSequences[i]) {
                expected.append(tokenImage[i2]).append(' ');
            }
            if (expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0) {
                expected.append("...");
            }
            expected.append(eol).append("    ");
        }
        String retval = "Encountered \"";
        Token tok = currentToken.next;
        for (i = 0; i < maxSize; i++) {
            if (i != 0) {
                retval = retval + " ";
            }
            if (tok.kind == 0) {
                retval = retval + tokenImage[0];
                break;
            }
            retval = (((retval + " " + tokenImage[tok.kind]) + " \"") + add_escapes(tok.image)) + " \"";
            tok = tok.next;
        }
        retval = (retval + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn) + ActiveMQDestination.PATH_SEPERATOR + eol;
        if (expectedTokenSequences.length == 1) {
            retval = retval + "Was expecting:" + eol + "    ";
        } else {
            retval = retval + "Was expecting one of:" + eol + "    ";
        }
        return retval + expected.toString();
    }

    static String add_escapes(String str) {
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case Tokenizer.EOF /*0*/:
                    break;
                case Protocol.EGP /*8*/:
                    retval.append("\\b");
                    break;
                case Service.DISCARD /*9*/:
                    retval.append("\\t");
                    break;
                case Protocol.BBN_RCC_MON /*10*/:
                    retval.append("\\n");
                    break;
                case Protocol.PUP /*12*/:
                    retval.append("\\f");
                    break;
                case Service.DAYTIME /*13*/:
                    retval.append("\\r");
                    break;
                case Type.ATMA /*34*/:
                    retval.append("\\\"");
                    break;
                case Service.RLP /*39*/:
                    retval.append("\\'");
                    break;
                case Place.TYPE_TRAIN_STATION /*92*/:
                    retval.append("\\\\");
                    break;
                default:
                    char ch = str.charAt(i);
                    if (ch >= ' ' && ch <= '~') {
                        retval.append(ch);
                        break;
                    }
                    String s = "0000" + Integer.toString(ch, 16);
                    retval.append("\\u" + s.substring(s.length() - 4, s.length()));
                    break;
                    break;
            }
        }
        return retval.toString();
    }
}
