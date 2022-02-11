package com.google.android.gms.internal;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.rabbitmq.client.AMQP;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class zzru implements zzrv {
    private HttpURLConnection zzbmy;

    zzru() {
    }

    private InputStream zzd(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == AMQP.REPLY_SUCCESS) {
            return httpURLConnection.getInputStream();
        }
        String str = "Bad response: " + responseCode;
        if (responseCode == AMQP.NOT_FOUND) {
            throw new FileNotFoundException(str);
        }
        throw new IOException(str);
    }

    private void zze(HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    public void close() {
        zze(this.zzbmy);
    }

    public InputStream zzgI(String str) throws IOException {
        this.zzbmy = zzgJ(str);
        return zzd(this.zzbmy);
    }

    HttpURLConnection zzgJ(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        httpURLConnection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        return httpURLConnection;
    }
}
