package com.mixpanel.android.util;

import android.content.Context;
import java.io.IOException;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;

public interface RemoteService {

    public static class ServiceUnavailableException extends Exception {
        private final int mRetryAfter;

        public ServiceUnavailableException(String message, String strRetryAfter) {
            int retry;
            super(message);
            try {
                retry = Integer.parseInt(strRetryAfter);
            } catch (NumberFormatException e) {
                retry = 0;
            }
            this.mRetryAfter = retry;
        }

        public int getRetryAfter() {
            return this.mRetryAfter;
        }
    }

    void checkIsMixpanelBlocked();

    boolean isOnline(Context context);

    byte[] performRequest(String str, Map<String, Object> map, SSLSocketFactory sSLSocketFactory) throws ServiceUnavailableException, IOException;
}
