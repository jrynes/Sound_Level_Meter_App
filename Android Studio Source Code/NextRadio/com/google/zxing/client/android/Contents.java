package com.google.zxing.client.android;

import com.google.android.gms.common.Scopes;

public final class Contents {
    public static final String[] EMAIL_KEYS;
    public static final String[] EMAIL_TYPE_KEYS;
    public static final String NOTE_KEY = "NOTE_KEY";
    public static final String[] PHONE_KEYS;
    public static final String[] PHONE_TYPE_KEYS;
    public static final String URL_KEY = "URL_KEY";

    public static final class Type {
        public static final String CONTACT = "CONTACT_TYPE";
        public static final String EMAIL = "EMAIL_TYPE";
        public static final String LOCATION = "LOCATION_TYPE";
        public static final String PHONE = "PHONE_TYPE";
        public static final String SMS = "SMS_TYPE";
        public static final String TEXT = "TEXT_TYPE";

        private Type() {
        }
    }

    private Contents() {
    }

    static {
        PHONE_KEYS = new String[]{"phone", "secondary_phone", "tertiary_phone"};
        PHONE_TYPE_KEYS = new String[]{"phone_type", "secondary_phone_type", "tertiary_phone_type"};
        EMAIL_KEYS = new String[]{Scopes.EMAIL, "secondary_email", "tertiary_email"};
        EMAIL_TYPE_KEYS = new String[]{"email_type", "secondary_email_type", "tertiary_email_type"};
    }
}
