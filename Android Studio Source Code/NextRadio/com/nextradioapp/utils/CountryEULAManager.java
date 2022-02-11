package com.nextradioapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;

public class CountryEULAManager {
    private static final String EULA_MX = "http://NextRadioApp.mx/eula/";
    private static final String EULA_PE = "http://nextradioapp.pe/eula/";
    private static final String EULA_US = "http://nextradioapp.com/eula/";
    private static final String FAQ_MX = "http://nextradioapp.com/faq/";
    private static final String FAQ_PE = "http://nextradioapp.com/faq/";
    private static final String FAQ_US = "http://nextradioapp.com/faq/";
    private static final String MEXICO = "MX";
    private static final String PERU = "PE";
    private static final String PRIVACY_MX = "http://NextRadioApp.mx/privacy/";
    private static final String PRIVACY_PE = "http://nextradioapp.pe/privacy/";
    private static final String PRIVACY_US = "http://nextradioapp.com/privacy/";
    private String currentCountry;

    public CountryEULAManager(Context myParent) {
        this.currentCountry = PreferenceManager.getDefaultSharedPreferences(myParent).getString(PreferenceStorage.SELECTED_COUNTRY, null);
        if (this.currentCountry == null) {
            this.currentCountry = "us";
        }
    }

    public String getFAQ() {
        if (this.currentCountry.equals(MEXICO)) {
            return FAQ_US;
        }
        if (this.currentCountry.equals(PERU)) {
            return FAQ_US;
        }
        return FAQ_US;
    }

    public String getPrivacy() {
        if (this.currentCountry.equals(MEXICO)) {
            return PRIVACY_MX;
        }
        if (this.currentCountry.equals(PERU)) {
            return PRIVACY_PE;
        }
        return PRIVACY_US;
    }

    public String getEULA() {
        if (this.currentCountry.equals(MEXICO)) {
            return EULA_MX;
        }
        if (this.currentCountry.equals(PERU)) {
            return EULA_PE;
        }
        return EULA_US;
    }

    public boolean renderTermsScreen() {
        if (this.currentCountry.equals(MEXICO)) {
            return true;
        }
        return false;
    }
}
