package com.amazon.device.ads;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectionInfo {
    private static final String LOGTAG;
    private static final String WIFI_NAME = "Wifi";
    private String connectionType;
    private ConnectivityManager connectivityManager;
    private final MobileAdsLogger logger;

    static {
        LOGTAG = ConnectionInfo.class.getSimpleName();
    }

    public ConnectionInfo(MobileAdsInfoStore infoStore) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        initialize((ConnectivityManager) infoStore.getApplicationContext().getSystemService("connectivity"));
    }

    ConnectionInfo(ConnectivityManager connectivityManager) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        initialize(connectivityManager);
    }

    private void initialize(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
        refresh();
    }

    public void refresh() {
        generateConnectionType();
    }

    private void generateConnectionType() {
        NetworkInfo networkInfo = null;
        try {
            if (this.connectivityManager != null) {
                networkInfo = this.connectivityManager.getActiveNetworkInfo();
            }
        } catch (SecurityException e) {
            this.logger.m638d("Unable to get active network information: %s", e);
        }
        if (networkInfo == null) {
            this.connectionType = Integer.toString(0);
        } else if (networkInfo.getType() == 1) {
            this.connectionType = WIFI_NAME;
        } else {
            this.connectionType = Integer.toString(networkInfo.getSubtype());
        }
    }

    public String getConnectionType() {
        return this.connectionType;
    }

    public boolean isWiFi() {
        return WIFI_NAME.equals(getConnectionType());
    }
}
