package com.nostra13.universalimageloader.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class BaseImageDownloader implements ImageDownloader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

    /* renamed from: com.nostra13.universalimageloader.core.download.BaseImageDownloader.1 */
    static /* synthetic */ class C12671 {
        static final /* synthetic */ int[] f2284x4730d1a3;

        static {
            f2284x4730d1a3 = new int[Scheme.values().length];
            try {
                f2284x4730d1a3[Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f2284x4730d1a3[Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f2284x4730d1a3[Scheme.FILE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f2284x4730d1a3[Scheme.CONTENT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f2284x4730d1a3[Scheme.ASSETS.ordinal()] = BaseImageDownloader.MAX_REDIRECT_COUNT;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f2284x4730d1a3[Scheme.DRAWABLE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f2284x4730d1a3[Scheme.UNKNOWN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public BaseImageDownloader(Context context) {
        this.context = context.getApplicationContext();
        this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
    }

    public BaseImageDownloader(Context context, int connectTimeout, int readTimeout) {
        this.context = context.getApplicationContext();
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch (C12671.f2284x4730d1a3[Scheme.ofUri(imageUri).ordinal()]) {
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
                return getStreamFromNetwork(imageUri, extra);
            case Protocol.GGP /*3*/:
                return getStreamFromFile(imageUri, extra);
            case Type.MF /*4*/:
                return getStreamFromContent(imageUri, extra);
            case MAX_REDIRECT_COUNT /*5*/:
                return getStreamFromAssets(imageUri, extra);
            case Protocol.TCP /*6*/:
                return getStreamFromDrawable(imageUri, extra);
            default:
                return getStreamFromOtherSource(imageUri, extra);
        }
    }

    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
        HttpURLConnection conn = createConnection(imageUri, extra);
        int redirectCount = 0;
        while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
            conn = createConnection(conn.getHeaderField(HttpRequest.HEADER_LOCATION), extra);
            redirectCount++;
        }
        return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
    }

    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(Uri.encode(url, ALLOWED_URI_CHARS)).openConnection();
        conn.setConnectTimeout(this.connectTimeout);
        conn.setReadTimeout(this.readTimeout);
        return conn;
    }

    protected InputStream getStreamFromFile(String imageUri, Object extra) throws IOException {
        return new BufferedInputStream(new FileInputStream(Scheme.FILE.crop(imageUri)), BUFFER_SIZE);
    }

    protected InputStream getStreamFromContent(String imageUri, Object extra) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(Uri.parse(imageUri));
    }

    protected InputStream getStreamFromAssets(String imageUri, Object extra) throws IOException {
        return this.context.getAssets().open(Scheme.ASSETS.crop(imageUri));
    }

    protected InputStream getStreamFromDrawable(String imageUri, Object extra) {
        Bitmap bitmap = ((BitmapDrawable) this.context.getResources().getDrawable(Integer.parseInt(Scheme.DRAWABLE.crop(imageUri)))).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, new Object[]{imageUri}));
    }
}
