package com.admarvel.android.ads;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.admarvel.android.util.Logging;
import com.facebook.ads.AdError;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQConnection;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

public class AdMarvelLocalFileContentProvider extends ContentProvider {

    /* renamed from: com.admarvel.android.ads.AdMarvelLocalFileContentProvider.a */
    private static class C0171a {
        public byte[] f209a;
        public int f210b;

        private C0171a() {
            this.f209a = null;
            this.f210b = 0;
        }
    }

    public int delete(Uri uri, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    public Uri insert(Uri uri, ContentValues contentvalues) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        int i = 0;
        String replace = uri.getPath().replace("content://" + getContext().getPackageName() + ".AdMarvelLocalFileContentProvider", Stomp.EMPTY);
        if (replace != null && (replace.equals("/mraid.js") || replace.equals("mraid.js"))) {
            File dir = getContext().getDir("adm_assets", 0);
            File file = (dir == null || !dir.isDirectory()) ? null : new File(dir.getAbsolutePath() + "/mraid.js");
            if (file == null || !file.exists()) {
                try {
                    String str = Stomp.EMPTY;
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constants.MRAID_JS_URL).openConnection();
                    httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_FORM);
                    httpURLConnection.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, "0");
                    httpURLConnection.setConnectTimeout(AdError.SERVER_ERROR_CODE);
                    httpURLConnection.setReadTimeout(AMQConnection.HANDSHAKE_TIMEOUT);
                    int responseCode = httpURLConnection.getResponseCode();
                    int contentLength = httpURLConnection.getContentLength();
                    Logging.log("Mraid Connection Status Code: " + responseCode);
                    Logging.log("Mraid Content Length: " + contentLength);
                    if (responseCode == AMQP.REPLY_SUCCESS) {
                        InputStream inputStream = (InputStream) httpURLConnection.getContent();
                        InputStream gZIPInputStream = (!HttpRequest.ENCODING_GZIP.equals(httpURLConnection.getContentEncoding()) || Version.getAndroidSDKVersion() >= 9) ? inputStream : new GZIPInputStream(inputStream);
                        List arrayList = new ArrayList();
                        int i2 = 0;
                        responseCode = Flags.FLAG2;
                        while (responseCode != -1) {
                            byte[] bArr = new byte[Flags.FLAG2];
                            responseCode = gZIPInputStream.read(bArr, 0, Flags.FLAG2);
                            if (responseCode > 0) {
                                C0171a c0171a = new C0171a();
                                c0171a.f209a = bArr;
                                c0171a.f210b = responseCode;
                                i2 += responseCode;
                                arrayList.add(c0171a);
                            }
                        }
                        gZIPInputStream.close();
                        if (i2 > 0) {
                            Object obj = new byte[i2];
                            for (responseCode = 0; responseCode < arrayList.size(); responseCode++) {
                                C0171a c0171a2 = (C0171a) arrayList.get(responseCode);
                                System.arraycopy(c0171a2.f209a, 0, obj, i, c0171a2.f210b);
                                i += c0171a2.f210b;
                            }
                            replace = new String(obj);
                        } else {
                            replace = str;
                        }
                        FileOutputStream openFileOutput = getContext().openFileOutput("admarvel_mraid.js", 0);
                        try {
                            openFileOutput.write(replace.getBytes());
                            openFileOutput.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return ParcelFileDescriptor.open(new File("/data/data/" + getContext().getPackageName() + "/files", "admarvel_mraid.js"), 268435456);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } else {
                try {
                    Logging.log("Mraid loading from client");
                    return ParcelFileDescriptor.open(file, 268435456);
                } catch (Throwable e3) {
                    Logging.log(Log.getStackTraceString(e3));
                }
            }
        }
        return null;
    }

    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }

    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {
        throw new UnsupportedOperationException("Not supported by this provider");
    }
}
