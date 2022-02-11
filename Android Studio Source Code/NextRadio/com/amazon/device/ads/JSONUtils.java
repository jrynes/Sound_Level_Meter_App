package com.amazon.device.ads;

import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONUtils {
    JSONUtils() {
    }

    public static boolean getBooleanFromJSON(JSONObject json, String key, boolean defaultValue) {
        if (json.isNull(key)) {
            return defaultValue;
        }
        return json.optBoolean(key, defaultValue);
    }

    public static String getStringFromJSON(JSONObject json, String key, String defaultValue) {
        if (json.isNull(key)) {
            return defaultValue;
        }
        return json.optString(key, defaultValue);
    }

    public static int getIntegerFromJSON(JSONObject json, String key, int defaultValue) {
        if (json.isNull(key)) {
            return defaultValue;
        }
        return json.optInt(key, defaultValue);
    }

    public static long getLongFromJSON(JSONObject json, String key, long defaultValue) {
        if (json.isNull(key)) {
            return defaultValue;
        }
        return json.optLong(key, defaultValue);
    }

    public static JSONArray getJSONArrayFromJSON(JSONObject json, String key) {
        if (json.isNull(key)) {
            return null;
        }
        return json.optJSONArray(key);
    }

    public static int getIntegerFromJSONArray(JSONArray jsonArray, int index, int defaultValue) {
        if (jsonArray.isNull(index)) {
            return defaultValue;
        }
        return jsonArray.optInt(index, defaultValue);
    }

    public static JSONObject getJSONObjectFromJSONArray(JSONArray jsonArray, int index) {
        JSONObject jSONObject = null;
        if (!jsonArray.isNull(index)) {
            try {
                jSONObject = jsonArray.getJSONObject(index);
            } catch (JSONException e) {
            }
        }
        return jSONObject;
    }

    public static void put(JSONObject json, String key, String value) {
        if (value != null && !value.equals(Stomp.EMPTY)) {
            try {
                json.put(key, value);
            } catch (JSONException e) {
            }
        }
    }

    public static void put(JSONObject json, String key, int value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
        }
    }

    public static void put(JSONObject json, String key, long value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
        }
    }

    public static void put(JSONObject json, String key, boolean value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
        }
    }

    public static JSONObject getJSONObjectFromString(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            return null;
        }
    }
}
