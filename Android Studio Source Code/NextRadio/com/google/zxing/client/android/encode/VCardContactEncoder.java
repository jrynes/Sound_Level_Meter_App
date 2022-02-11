package com.google.zxing.client.android.encode;

import android.telephony.PhoneNumberUtils;
import java.util.regex.Pattern;
import org.apache.activemq.transport.stomp.Stomp;

final class VCardContactEncoder extends ContactEncoder {
    private static final Pattern NEWLINE;
    private static final Pattern RESERVED_VCARD_CHARS;
    private static final char TERMINATOR = '\n';
    private static final Formatter VCARD_FIELD_FORMATTER;

    /* renamed from: com.google.zxing.client.android.encode.VCardContactEncoder.1 */
    static class C10651 implements Formatter {
        C10651() {
        }

        public String format(String source) {
            return VCardContactEncoder.NEWLINE.matcher(VCardContactEncoder.RESERVED_VCARD_CHARS.matcher(source).replaceAll("\\\\$1")).replaceAll(Stomp.EMPTY);
        }
    }

    /* renamed from: com.google.zxing.client.android.encode.VCardContactEncoder.2 */
    class C10662 implements Formatter {
        C10662() {
        }

        public String format(String source) {
            return PhoneNumberUtils.formatNumber(source);
        }
    }

    VCardContactEncoder() {
    }

    static {
        RESERVED_VCARD_CHARS = Pattern.compile("([\\\\,;])");
        NEWLINE = Pattern.compile("\\n");
        VCARD_FIELD_FORMATTER = new C10651();
    }

    public String[] encode(Iterable<String> names, String organization, Iterable<String> addresses, Iterable<String> phones, Iterable<String> emails, String url, String note) {
        StringBuilder newContents = new StringBuilder(100);
        StringBuilder newDisplayContents = new StringBuilder(100);
        newContents.append("BEGIN:VCARD").append(TERMINATOR);
        appendUpToUnique(newContents, newDisplayContents, "N", names, 1, null);
        append(newContents, newDisplayContents, "ORG", organization);
        appendUpToUnique(newContents, newDisplayContents, "ADR", addresses, 1, null);
        appendUpToUnique(newContents, newDisplayContents, "TEL", phones, Integer.MAX_VALUE, new C10662());
        appendUpToUnique(newContents, newDisplayContents, "EMAIL", emails, Integer.MAX_VALUE, null);
        append(newContents, newDisplayContents, "URL", url);
        append(newContents, newDisplayContents, "NOTE", note);
        newContents.append("END:VCARD").append(TERMINATOR);
        return new String[]{newContents.toString(), newDisplayContents.toString()};
    }

    private static void append(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, String value) {
        ContactEncoder.doAppend(newContents, newDisplayContents, prefix, value, VCARD_FIELD_FORMATTER, TERMINATOR);
    }

    private static void appendUpToUnique(StringBuilder newContents, StringBuilder newDisplayContents, String prefix, Iterable<String> values, int max, Formatter formatter) {
        ContactEncoder.doAppendUpToUnique(newContents, newDisplayContents, prefix, values, max, formatter, VCARD_FIELD_FORMATTER, TERMINATOR);
    }
}
