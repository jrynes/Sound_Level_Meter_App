package com.amazon.device.associates;

import android.content.Intent;
import android.net.Uri;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: DirectShoppingHelper */
public class br {
    protected static String m907a(String str) {
        String str2 = null;
        if (!(str == null || Stomp.EMPTY.equals(str))) {
            try {
                str2 = URLEncoder.encode(str.replace(Stomp.NEWLINE, " ").trim(), HttpRequest.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return str2;
    }

    private static String m906a(bz bzVar, String str) {
        if (str != null) {
            String str2 = (String) bzVar.m940e().get(str);
            if (str2 != null) {
                return str2;
            }
        }
        return (String) bzVar.m940e().get("All");
    }

    private static String m909b(bz bzVar, String str) {
        if (str != null) {
            String str2 = (String) bzVar.m936a().get(str);
            if (str2 != null) {
                return str2;
            }
        }
        return Stomp.EMPTY;
    }

    public static String m910b(String str) {
        CharSequence a = m907a(str);
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        String a2 = a != null ? (retailURLTemplates == null || !retailURLTemplates.m974a().containsKey(RetailURLTemplates.f1342a)) ? null : (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1342a) : m904a();
        if (a2 == null) {
            a2 = (String) ((be) al.m752a(be.class)).m868a().m974a().get(RetailURLTemplates.f1342a);
        }
        if (!a2.startsWith("http://")) {
            a2 = "http://" + a2;
        }
        if (a2.contains("$ASIN")) {
            a2 = a2.replace("$ASIN", a);
        }
        return a2.replace("$SUBTAG", bp.m901b());
    }

    public static String m911c(String str) {
        CharSequence a = m907a(str);
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        String a2 = a != null ? (retailURLTemplates == null || !retailURLTemplates.m974a().containsKey(RetailURLTemplates.f1346e)) ? null : (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1346e) : m904a();
        if (a2 == null) {
            a2 = (String) ((be) al.m752a(be.class)).m868a().m974a().get(RetailURLTemplates.f1346e);
        }
        if (!a2.startsWith("http://")) {
            a2 = "http://" + a2;
        }
        if (a2.contains("$ASIN")) {
            a2 = a2.replace("$ASIN", a);
        }
        return a2.replace("$SUBTAG", bp.m901b());
    }

    public static String m905a(OpenSearchPageRequest openSearchPageRequest) {
        RetailURLTemplates a;
        String a2;
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        if (retailURLTemplates == null) {
            a = ((be) al.m752a(be.class)).m868a();
        } else {
            a = retailURLTemplates;
        }
        bz bzVar = (bz) ((AsyncGetCategorySearchDetailsCacheTask) al.m752a(AsyncGetCategorySearchDetailsCacheTask.class)).m778j();
        if (bzVar == null) {
            bzVar = ((AsyncGetCategorySearchDetailsCacheTask) al.m752a(AsyncGetCategorySearchDetailsCacheTask.class)).m957a();
        }
        CharSequence a3 = m907a(openSearchPageRequest.getSearchTerm());
        String a4 = m907a(openSearchPageRequest.getBrand());
        CharSequence a5 = m906a(bzVar, openSearchPageRequest.getCategory());
        CharSequence charSequence = Stomp.EMPTY;
        if (openSearchPageRequest.getSortType() != null) {
            charSequence = m909b(bzVar, openSearchPageRequest.getSortType().toString());
        }
        String str = (String) a.m974a().get(RetailURLTemplates.f1345d);
        if (str == null) {
            a2 = m904a();
        } else {
            a2 = str;
        }
        if (a5 == null) {
            a5 = Stomp.EMPTY;
        }
        if (a4 == null) {
            CharSequence charSequence2 = Stomp.EMPTY;
        } else {
            Object obj = a4;
        }
        if (!a2.startsWith("http://")) {
            a2 = "http://" + a2;
        }
        if (a2.contains("$SEARCH")) {
            a2 = a2.replace("$SEARCH", a3);
        }
        if (a2.contains("$CATEGORY")) {
            a2 = a2.replace("$CATEGORY", a5);
        }
        if (a2.contains("$BRAND")) {
            a2 = a2.replace("$BRAND", charSequence2);
        }
        if (a2.contains("$SORTTYPE")) {
            a2 = a2.replace("$SORTTYPE", charSequence);
        }
        return a2.replace("$SUBTAG", bp.m901b());
    }

    public static String m904a() {
        String str;
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        if (retailURLTemplates == null || !retailURLTemplates.m974a().containsKey(RetailURLTemplates.f1343b)) {
            str = null;
        } else {
            str = (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1343b);
        }
        if (str == null) {
            str = (String) ((be) al.m752a(be.class)).m868a().m974a().get(RetailURLTemplates.f1343b);
        }
        if (!str.startsWith("http://")) {
            str = "http://" + str;
        }
        return str.replace("$SUBTAG", bp.m901b());
    }

    static void m912d(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setFlags(268435456);
        bp.m899a().startActivity(intent);
    }

    public static String m913e(String str) {
        String substring;
        int indexOf = str.indexOf("#");
        String str2 = null;
        if (indexOf != -1) {
            str2 = str.substring(indexOf);
            str = str.substring(0, indexOf);
        }
        String str3 = Stomp.EMPTY;
        StringBuilder stringBuilder = new StringBuilder();
        int indexOf2 = str.indexOf("?");
        if (indexOf2 != -1) {
            String substring2 = str.substring(indexOf2 + 1);
            substring = str.substring(0, indexOf2 + 1);
            indexOf = 1;
            str = substring2;
        } else {
            substring = str3;
            indexOf = 0;
        }
        String[] split = str.split("&");
        int i = indexOf;
        indexOf = 0;
        while (indexOf < split.length) {
            if (!(split[indexOf].toLowerCase(Locale.getDefault()).startsWith("tag=") || split[indexOf].toLowerCase(Locale.getDefault()).startsWith("linkcode=") || split[indexOf].toLowerCase(Locale.getDefault()).startsWith("ascsubtag="))) {
                if (stringBuilder.length() == 0 || r4 != 0) {
                    stringBuilder.append(split[indexOf]);
                    i = 0;
                } else {
                    stringBuilder.append("&");
                    stringBuilder.append(split[indexOf]);
                }
            }
            indexOf++;
        }
        stringBuilder.insert(0, substring);
        if (str2 != null) {
            stringBuilder.append(str2);
        }
        Log.m1013a("DirectShoppingHelper", "Url after stripping off linkcode and tag params : " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String m908a(String str, String str2) {
        return Uri.parse(str).getQueryParameter(str2);
    }
}
