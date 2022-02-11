package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.android.Intents.WifiConnect;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public final class VCardResultParser extends ResultParser {
    private static final Pattern BEGIN_VCARD;
    private static final Pattern CR_LF_SPACE_TAB;
    private static final Pattern EQUALS;
    private static final Pattern NEWLINE_ESCAPE;
    private static final Pattern SEMICOLON;
    private static final Pattern VCARD_ESCAPES;
    private static final Pattern VCARD_LIKE_DATE;

    static {
        BEGIN_VCARD = Pattern.compile("BEGIN:VCARD", 2);
        VCARD_LIKE_DATE = Pattern.compile("\\d{4}-?\\d{2}-?\\d{2}");
        CR_LF_SPACE_TAB = Pattern.compile("\r\n[ \t]");
        NEWLINE_ESCAPE = Pattern.compile("\\\\[nN]");
        VCARD_ESCAPES = Pattern.compile("\\\\([,;\\\\])");
        EQUALS = Pattern.compile("=");
        SEMICOLON = Pattern.compile(";");
    }

    public AddressBookParsedResult parse(Result result) {
        String rawText = result.getText();
        Matcher m = BEGIN_VCARD.matcher(rawText);
        if (!m.find() || m.start() != 0) {
            return null;
        }
        List<List<String>> names = matchVCardPrefixedField("FN", rawText, true);
        if (names == null) {
            names = matchVCardPrefixedField("N", rawText, true);
            formatNames(names);
        }
        List<List<String>> phoneNumbers = matchVCardPrefixedField("TEL", rawText, true);
        List<List<String>> emails = matchVCardPrefixedField("EMAIL", rawText, true);
        List<String> note = matchSingleVCardPrefixedField("NOTE", rawText, false);
        List<List<String>> addresses = matchVCardPrefixedField("ADR", rawText, true);
        if (addresses != null) {
            for (List<String> list : addresses) {
                list.set(0, list.get(0));
            }
        }
        List<String> org = matchSingleVCardPrefixedField("ORG", rawText, true);
        List<String> birthday = matchSingleVCardPrefixedField("BDAY", rawText, true);
        if (!(birthday == null || isLikeVCardDate((CharSequence) birthday.get(0)))) {
            birthday = null;
        }
        List<String> title = matchSingleVCardPrefixedField("TITLE", rawText, true);
        List<String> url = matchSingleVCardPrefixedField("URL", rawText, true);
        return new AddressBookParsedResult(toPrimaryValues(names), null, toPrimaryValues(phoneNumbers), toTypes(phoneNumbers), toPrimaryValues(emails), toTypes(emails), toPrimaryValue(matchSingleVCardPrefixedField("IMPP", rawText, true)), toPrimaryValue(note), toPrimaryValues(addresses), toTypes(addresses), toPrimaryValue(org), toPrimaryValue(birthday), toPrimaryValue(title), toPrimaryValue(url));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.List<java.util.List<java.lang.String>> matchVCardPrefixedField(java.lang.CharSequence r22, java.lang.String r23, boolean r24) {
        /*
        r11 = 0;
        r4 = 0;
        r12 = r23.length();
    L_0x0006:
        if (r4 >= r12) goto L_0x003d;
    L_0x0008:
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "(?:^|\n)";
        r20 = r20.append(r21);
        r0 = r20;
        r1 = r22;
        r20 = r0.append(r1);
        r21 = "(?:;([^:]*))?:";
        r20 = r20.append(r21);
        r20 = r20.toString();
        r21 = 2;
        r20 = java.util.regex.Pattern.compile(r20, r21);
        r0 = r20;
        r1 = r23;
        r10 = r0.matcher(r1);
        if (r4 <= 0) goto L_0x0037;
    L_0x0035:
        r4 = r4 + -1;
    L_0x0037:
        r20 = r10.find(r4);
        if (r20 != 0) goto L_0x003e;
    L_0x003d:
        return r11;
    L_0x003e:
        r20 = 0;
        r0 = r20;
        r4 = r10.end(r0);
        r20 = 1;
        r0 = r20;
        r14 = r10.group(r0);
        r13 = 0;
        r17 = 0;
        r18 = 0;
        if (r14 == 0) goto L_0x00ba;
    L_0x0055:
        r20 = SEMICOLON;
        r0 = r20;
        r2 = r0.split(r14);
        r7 = r2.length;
        r5 = 0;
    L_0x005f:
        if (r5 >= r7) goto L_0x00ba;
    L_0x0061:
        r15 = r2[r5];
        if (r13 != 0) goto L_0x006e;
    L_0x0065:
        r13 = new java.util.ArrayList;
        r20 = 1;
        r0 = r20;
        r13.<init>(r0);
    L_0x006e:
        r13.add(r15);
        r20 = EQUALS;
        r21 = 2;
        r0 = r20;
        r1 = r21;
        r16 = r0.split(r15, r1);
        r0 = r16;
        r0 = r0.length;
        r20 = r0;
        r21 = 1;
        r0 = r20;
        r1 = r21;
        if (r0 <= r1) goto L_0x00aa;
    L_0x008a:
        r20 = 0;
        r6 = r16[r20];
        r20 = 1;
        r19 = r16[r20];
        r20 = "ENCODING";
        r0 = r20;
        r20 = r0.equalsIgnoreCase(r6);
        if (r20 == 0) goto L_0x00ad;
    L_0x009c:
        r20 = "QUOTED-PRINTABLE";
        r0 = r20;
        r1 = r19;
        r20 = r0.equalsIgnoreCase(r1);
        if (r20 == 0) goto L_0x00ad;
    L_0x00a8:
        r17 = 1;
    L_0x00aa:
        r5 = r5 + 1;
        goto L_0x005f;
    L_0x00ad:
        r20 = "CHARSET";
        r0 = r20;
        r20 = r0.equalsIgnoreCase(r6);
        if (r20 == 0) goto L_0x00aa;
    L_0x00b7:
        r18 = r19;
        goto L_0x00aa;
    L_0x00ba:
        r9 = r4;
    L_0x00bb:
        r20 = 10;
        r0 = r23;
        r1 = r20;
        r4 = r0.indexOf(r1, r4);
        if (r4 < 0) goto L_0x0121;
    L_0x00c7:
        r20 = r23.length();
        r20 = r20 + -1;
        r0 = r20;
        if (r4 >= r0) goto L_0x00f8;
    L_0x00d1:
        r20 = r4 + 1;
        r0 = r23;
        r1 = r20;
        r20 = r0.charAt(r1);
        r21 = 32;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x00f5;
    L_0x00e3:
        r20 = r4 + 1;
        r0 = r23;
        r1 = r20;
        r20 = r0.charAt(r1);
        r21 = 9;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x00f8;
    L_0x00f5:
        r4 = r4 + 2;
        goto L_0x00bb;
    L_0x00f8:
        if (r17 == 0) goto L_0x0121;
    L_0x00fa:
        r20 = r4 + -1;
        r0 = r23;
        r1 = r20;
        r20 = r0.charAt(r1);
        r21 = 61;
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x011e;
    L_0x010c:
        r20 = r4 + -2;
        r0 = r23;
        r1 = r20;
        r20 = r0.charAt(r1);
        r21 = 61;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0121;
    L_0x011e:
        r4 = r4 + 1;
        goto L_0x00bb;
    L_0x0121:
        if (r4 >= 0) goto L_0x0126;
    L_0x0123:
        r4 = r12;
        goto L_0x0006;
    L_0x0126:
        if (r4 <= r9) goto L_0x01a6;
    L_0x0128:
        if (r11 != 0) goto L_0x0133;
    L_0x012a:
        r11 = new java.util.ArrayList;
        r20 = 1;
        r0 = r20;
        r11.<init>(r0);
    L_0x0133:
        r20 = r4 + -1;
        r0 = r23;
        r1 = r20;
        r20 = r0.charAt(r1);
        r21 = 13;
        r0 = r20;
        r1 = r21;
        if (r0 != r1) goto L_0x0147;
    L_0x0145:
        r4 = r4 + -1;
    L_0x0147:
        r0 = r23;
        r3 = r0.substring(r9, r4);
        if (r24 == 0) goto L_0x0153;
    L_0x014f:
        r3 = r3.trim();
    L_0x0153:
        if (r17 == 0) goto L_0x0170;
    L_0x0155:
        r0 = r18;
        r3 = decodeQuotedPrintable(r3, r0);
    L_0x015b:
        if (r13 != 0) goto L_0x019b;
    L_0x015d:
        r8 = new java.util.ArrayList;
        r20 = 1;
        r0 = r20;
        r8.<init>(r0);
        r8.add(r3);
        r11.add(r8);
    L_0x016c:
        r4 = r4 + 1;
        goto L_0x0006;
    L_0x0170:
        r20 = CR_LF_SPACE_TAB;
        r0 = r20;
        r20 = r0.matcher(r3);
        r21 = "";
        r3 = r20.replaceAll(r21);
        r20 = NEWLINE_ESCAPE;
        r0 = r20;
        r20 = r0.matcher(r3);
        r21 = "\n";
        r3 = r20.replaceAll(r21);
        r20 = VCARD_ESCAPES;
        r0 = r20;
        r20 = r0.matcher(r3);
        r21 = "$1";
        r3 = r20.replaceAll(r21);
        goto L_0x015b;
    L_0x019b:
        r20 = 0;
        r0 = r20;
        r13.add(r0, r3);
        r11.add(r13);
        goto L_0x016c;
    L_0x01a6:
        r4 = r4 + 1;
        goto L_0x0006;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.client.result.VCardResultParser.matchVCardPrefixedField(java.lang.CharSequence, java.lang.String, boolean):java.util.List<java.util.List<java.lang.String>>");
    }

    private static String decodeQuotedPrintable(CharSequence value, String charset) {
        int length = value.length();
        StringBuilder result = new StringBuilder(length);
        ByteArrayOutputStream fragmentBuffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < length) {
            char c = value.charAt(i);
            switch (c) {
                case Protocol.BBN_RCC_MON /*10*/:
                case Service.DAYTIME /*13*/:
                    break;
                case Service.NI_MAIL /*61*/:
                    if (i >= length - 2) {
                        break;
                    }
                    char nextChar = value.charAt(i + 1);
                    if (!(nextChar == '\r' || nextChar == '\n')) {
                        char nextNextChar = value.charAt(i + 2);
                        int firstDigit = ResultParser.parseHexDigit(nextChar);
                        int secondDigit = ResultParser.parseHexDigit(nextNextChar);
                        if (firstDigit >= 0 && secondDigit >= 0) {
                            fragmentBuffer.write((firstDigit << 4) + secondDigit);
                        }
                        i += 2;
                        break;
                    }
                default:
                    maybeAppendFragment(fragmentBuffer, charset, result);
                    result.append(c);
                    break;
            }
            i++;
        }
        maybeAppendFragment(fragmentBuffer, charset, result);
        return result.toString();
    }

    private static void maybeAppendFragment(ByteArrayOutputStream fragmentBuffer, String charset, StringBuilder result) {
        if (fragmentBuffer.size() > 0) {
            String fragment;
            byte[] fragmentBytes = fragmentBuffer.toByteArray();
            if (charset == null) {
                fragment = new String(fragmentBytes);
            } else {
                try {
                    fragment = new String(fragmentBytes, charset);
                } catch (UnsupportedEncodingException e) {
                    fragment = new String(fragmentBytes);
                }
            }
            fragmentBuffer.reset();
            result.append(fragment);
        }
    }

    static List<String> matchSingleVCardPrefixedField(CharSequence prefix, String rawText, boolean trim) {
        List<List<String>> values = matchVCardPrefixedField(prefix, rawText, trim);
        return (values == null || values.isEmpty()) ? null : (List) values.get(0);
    }

    private static String toPrimaryValue(List<String> list) {
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    private static String[] toPrimaryValues(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList(lists.size());
        for (List<String> list : lists) {
            result.add(list.get(0));
        }
        return (String[]) result.toArray(new String[lists.size()]);
    }

    private static String[] toTypes(Collection<List<String>> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList(lists.size());
        for (List<String> list : lists) {
            String type = null;
            int i = 1;
            while (i < list.size()) {
                String metadatum = (String) list.get(i);
                int equals = metadatum.indexOf(61);
                if (equals < 0) {
                    type = metadatum;
                    break;
                } else if (WifiConnect.TYPE.equalsIgnoreCase(metadatum.substring(0, equals))) {
                    type = metadatum.substring(equals + 1);
                    break;
                } else {
                    i++;
                }
            }
            result.add(type);
        }
        return (String[]) result.toArray(new String[lists.size()]);
    }

    private static boolean isLikeVCardDate(CharSequence value) {
        return value == null || VCARD_LIKE_DATE.matcher(value).matches();
    }

    private static void formatNames(Iterable<List<String>> names) {
        if (names != null) {
            for (List<String> list : names) {
                String name = (String) list.get(0);
                String[] components = new String[5];
                int start = 0;
                int componentIndex = 0;
                while (true) {
                    int end = name.indexOf(59, start);
                    if (end <= 0) {
                        break;
                    }
                    components[componentIndex] = name.substring(start, end);
                    componentIndex++;
                    start = end + 1;
                }
                components[componentIndex] = name.substring(start);
                StringBuilder newName = new StringBuilder(100);
                maybeAppendComponent(components, 3, newName);
                maybeAppendComponent(components, 1, newName);
                maybeAppendComponent(components, 2, newName);
                maybeAppendComponent(components, 0, newName);
                maybeAppendComponent(components, 4, newName);
                list.set(0, newName.toString().trim());
            }
        }
    }

    private static void maybeAppendComponent(String[] components, int i, StringBuilder newName) {
        if (components[i] != null) {
            newName.append(' ');
            newName.append(components[i]);
        }
    }
}
