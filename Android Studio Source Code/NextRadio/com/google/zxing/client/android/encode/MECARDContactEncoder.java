package com.google.zxing.client.android.encode;

import android.telephony.PhoneNumberUtils;
import java.util.regex.Pattern;
import org.apache.activemq.transport.stomp.Stomp;

final class MECARDContactEncoder extends ContactEncoder {
    private static final Pattern COMMA;
    private static final Formatter MECARD_FIELD_FORMATTER;
    private static final Pattern NEWLINE;
    private static final Pattern RESERVED_MECARD_CHARS;
    private static final char TERMINATOR = ';';

    /* renamed from: com.google.zxing.client.android.encode.MECARDContactEncoder.1 */
    static class C10621 implements Formatter {
        C10621() {
        }

        public String format(String source) {
            return MECARDContactEncoder.NEWLINE.matcher(MECARDContactEncoder.RESERVED_MECARD_CHARS.matcher(source).replaceAll("\\\\$1")).replaceAll(Stomp.EMPTY);
        }
    }

    /* renamed from: com.google.zxing.client.android.encode.MECARDContactEncoder.2 */
    class C10632 implements Formatter {
        C10632() {
        }

        public String format(String source) {
            return source == null ? null : MECARDContactEncoder.COMMA.matcher(source).replaceAll(Stomp.EMPTY);
        }
    }

    /* renamed from: com.google.zxing.client.android.encode.MECARDContactEncoder.3 */
    class C10643 implements Formatter {
        C10643() {
        }

        public String format(String source) {
            return PhoneNumberUtils.formatNumber(source);
        }
    }

    MECARDContactEncoder() {
    }

    static {
        RESERVED_MECARD_CHARS = Pattern.compile("([\\\\:;])");
        NEWLINE = Pattern.compile("\\n");
        COMMA = Pattern.compile(Stomp.COMMA);
        MECARD_FIELD_FORMATTER = new C10621();
    }

    public String[] encode(Iterable<String> names, String organization, Iterable<String> addresses, Iterable<String> phones, Iterable<String> emails, String url, String note) {
        StringBuilder newContents = new StringBuilder(100);
        StringBuilder newDisplayContents = new StringBuilder(100);
        newContents.append("MECARD:");
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, new C10632());
        append(newContents, newDisplayContents, "ORG", organization);
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, null);
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE, new C10643());
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null);
        append(newContents, newDisplayContents, "URL", url);
        append(newContents, newDisplayContents, "NOTE", note);
        newContents.append(TERMINATOR);
        return new String[]{newContents.toString(), newDisplayContents.toString()};
    }

    private static void append(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, String value) {
        ContactEncoder.doAppend(newContents, newDisplayContents, prefix, value, MECARD_FIELD_FORMATTER, TERMINATOR);
    }

    private static void appendUpToUnique(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, Iterable<String> values, int max, Formatter formatter) {
        ContactEncoder.doAppendUpToUnique(newContents, newDisplayContents, prefix, values, max, formatter, MECARD_FIELD_FORMATTER, TERMINATOR);
    }
}
