package org.apache.activemq.selector;

import com.google.android.gms.location.places.Place;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class TokenMgrError extends Error {
    static final int INVALID_LEXICAL_STATE = 2;
    static final int LEXICAL_ERROR = 0;
    static final int LOOP_DETECTED = 3;
    static final int STATIC_LEXER_ERROR = 1;
    private static final long serialVersionUID = 1;
    int errorCode;

    protected static final String addEscapes(String str) {
        StringBuffer retval = new StringBuffer();
        for (int i = LEXICAL_ERROR; i < str.length(); i += STATIC_LEXER_ERROR) {
            switch (str.charAt(i)) {
                case LEXICAL_ERROR /*0*/:
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

    protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar) {
        return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : "\"" + addEscapes(String.valueOf(curChar)) + "\"" + " (" + curChar + "), ") + "after : \"" + addEscapes(errorAfter) + "\"";
    }

    public String getMessage() {
        return super.getMessage();
    }

    public TokenMgrError(String message, int reason) {
        super(message);
        this.errorCode = reason;
    }

    public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
        this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
    }
}
