package com.amazon.device.associates;

import com.admarvel.android.ads.Constants;
import com.admarvel.android.ads.nativeads.AdMarvelNativeAd;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: JSONParser */
class ba {
    private static final String f1243a;

    static {
        f1243a = ba.class.getSimpleName();
    }

    private ba() {
    }

    static UserData m857a(JSONObject jSONObject) throws JSONException {
        return new UserData(jSONObject.getString("userId"), jSONObject.getString("marketplace"));
    }

    static Receipt m859b(JSONObject jSONObject) throws JSONException, ParseException {
        String string = jSONObject.getString("receiptId");
        String string2 = jSONObject.getString("productId");
        String str = null;
        if (jSONObject.has("parentProductId")) {
            str = jSONObject.getString("parentProductId");
        }
        return new Receipt(string, string2, str, jSONObject.getInt("quantity"), jSONObject.getString("purchaseToken"), new Date(jSONObject.getLong("purchaseDate")), jSONObject.getBoolean("isCanceled"));
    }

    static <C extends Collection<String>> C m858a(JSONArray jSONArray, C c) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                c.add(jSONArray.getString(i));
            } catch (JSONException e) {
                aa.m693b(f1243a, "error in parseStrings: " + e);
            }
        }
        return c;
    }

    static <C extends Collection<Receipt>> C m860b(JSONArray jSONArray, C c) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                c.add(m859b(jSONArray.getJSONObject(i)));
            } catch (Exception e) {
                aa.m693b(f1243a, "error in parseReceipts: " + e);
            }
        }
        return c;
    }

    static <C extends Collection<Product>> C m862c(JSONArray jSONArray, C c) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                c.add(new Product(jSONObject.getString("productId"), jSONObject.optString(SettingsJsonConstants.PROMPT_TITLE_KEY), jSONObject.optString(activityEvents.description), m861c(jSONObject.optJSONObject(Constants.NATIVE_AD_IMAGE_ELEMENT)), m863d(jSONObject.optJSONObject("price")), jSONObject.optString("brand"), jSONObject.optDouble(AdMarvelNativeAd.FIELD_RATING, 0.0d)));
            } catch (Exception e) {
                aa.m693b(f1243a, "error in parseProducts: " + e);
            }
        }
        return c;
    }

    static Image m861c(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                return new Image(jSONObject.getString(SettingsJsonConstants.APP_URL_KEY), jSONObject.getInt(SettingsJsonConstants.ICON_WIDTH_KEY), jSONObject.getInt(SettingsJsonConstants.ICON_HEIGHT_KEY));
            } catch (Exception e) {
                aa.m693b(f1243a, "error in parseImage. returning null: " + e);
            }
        }
        return null;
    }

    static Price m863d(JSONObject jSONObject) {
        if (jSONObject != null) {
            try {
                return new Price(Currency.getInstance(jSONObject.getString("currency")), new BigDecimal(jSONObject.getDouble("minValue")), new BigDecimal(jSONObject.getDouble("maxValue")), jSONObject.getString("formattedPrice"));
            } catch (Exception e) {
                aa.m693b(f1243a, "error in parsePrice. returning null: " + e);
            }
        }
        return null;
    }
}
