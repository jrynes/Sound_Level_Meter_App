package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.location.places.Place;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public final class zznb {
    private static final Pattern zzaoi;
    private static final Pattern zzaoj;

    static {
        zzaoi = Pattern.compile("\\\\.");
        zzaoj = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }

    public static String zzcU(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = zzaoj.matcher(str);
        StringBuffer stringBuffer = null;
        while (matcher.find()) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            switch (matcher.group().charAt(0)) {
                case Protocol.EGP /*8*/:
                    matcher.appendReplacement(stringBuffer, "\\\\b");
                    break;
                case Service.DISCARD /*9*/:
                    matcher.appendReplacement(stringBuffer, "\\\\t");
                    break;
                case Protocol.BBN_RCC_MON /*10*/:
                    matcher.appendReplacement(stringBuffer, "\\\\n");
                    break;
                case Protocol.PUP /*12*/:
                    matcher.appendReplacement(stringBuffer, "\\\\f");
                    break;
                case Service.DAYTIME /*13*/:
                    matcher.appendReplacement(stringBuffer, "\\\\r");
                    break;
                case Type.ATMA /*34*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\"");
                    break;
                case Service.NI_FTP /*47*/:
                    matcher.appendReplacement(stringBuffer, "\\\\/");
                    break;
                case Place.TYPE_TRAIN_STATION /*92*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\\");
                    break;
                default:
                    break;
            }
        }
        if (stringBuffer == null) {
            return str;
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static boolean zze(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if ((obj instanceof JSONObject) && (obj2 instanceof JSONObject)) {
            JSONObject jSONObject = (JSONObject) obj;
            JSONObject jSONObject2 = (JSONObject) obj2;
            if (jSONObject.length() != jSONObject2.length()) {
                return false;
            }
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!jSONObject2.has(str)) {
                    return false;
                }
                try {
                    if (!zze(jSONObject.get(str), jSONObject2.get(str))) {
                        return false;
                    }
                } catch (JSONException e) {
                    return false;
                }
            }
            return true;
        } else if (!(obj instanceof JSONArray) || !(obj2 instanceof JSONArray)) {
            return obj.equals(obj2);
        } else {
            JSONArray jSONArray = (JSONArray) obj;
            JSONArray jSONArray2 = (JSONArray) obj2;
            if (jSONArray.length() != jSONArray2.length()) {
                return false;
            }
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    if (!zze(jSONArray.get(i), jSONArray2.get(i))) {
                        return false;
                    }
                    i++;
                } catch (JSONException e2) {
                    return false;
                }
            }
            return true;
        }
    }
}
