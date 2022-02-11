package com.crashlytics.android.answers;

import com.admarvel.android.ads.Constants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

class AnswersAttributes {
    final Map<String, Object> attributes;
    final AnswersEventValidator validator;

    public AnswersAttributes(AnswersEventValidator validator) {
        this.attributes = new ConcurrentHashMap();
        this.validator = validator;
    }

    void put(String key, String value) {
        if (!this.validator.isNull(key, Constants.NATIVE_AD_KEY_ELEMENT) && !this.validator.isNull(value, Constants.NATIVE_AD_VALUE_ELEMENT)) {
            putAttribute(this.validator.limitStringLength(key), this.validator.limitStringLength(value));
        }
    }

    void put(String key, Number value) {
        if (!this.validator.isNull(key, Constants.NATIVE_AD_KEY_ELEMENT) && !this.validator.isNull(value, Constants.NATIVE_AD_VALUE_ELEMENT)) {
            putAttribute(this.validator.limitStringLength(key), value);
        }
    }

    void putAttribute(String key, Object value) {
        if (!this.validator.isFullMap(this.attributes, key)) {
            this.attributes.put(key, value);
        }
    }

    public String toString() {
        return new JSONObject(this.attributes).toString();
    }
}
