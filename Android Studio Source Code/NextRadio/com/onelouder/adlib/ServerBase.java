package com.onelouder.adlib;

import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.UrlUtils;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

abstract class ServerBase implements Runnable {
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String GET = "GET";
    private static final String GZIP = "gzip";
    private static final int HTTP_CONNECT_TIMEOUT = 60000;
    private static final int HTTP_READ_TIMEOUT = 60000;
    private static final String POST = "POST";
    protected boolean bLogPostParams;
    public byte[] data;
    protected boolean isError;
    protected boolean mAppendClient;
    protected int mConnectTimeOut;
    protected String mContentType;
    protected boolean mDo_post;
    protected boolean mGzipEncoded;
    protected String mPostParams;
    protected int mReadTimeOut;
    protected String mResponse;
    protected int mResponseCode;
    protected boolean mResponseGzipped;
    protected int mRetryCount;
    protected int mTotalBytes;
    protected String mUrl;
    public boolean mUseSecureConnection;
    protected boolean mZeroBytesAllowed;

    private class Buffer {
        public byte[] buffer;
        public int bytes;

        private Buffer() {
            this.buffer = null;
            this.bytes = 0;
        }
    }

    protected abstract String TAG();

    public void addParam(StringBuilder sb, String key, String value) {
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '?') {
            sb.append("&");
        }
        sb.append(key);
        sb.append("=");
        try {
            sb.append(URLEncoder.encode(value, UrlUtils.UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void addParam(StringBuilder sb, String key, int value) {
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '?') {
            sb.append("&");
        }
        sb.append(key);
        sb.append("=");
        sb.append(value);
    }

    public ServerBase() {
        this.mReadTimeOut = HTTP_READ_TIMEOUT;
        this.mConnectTimeOut = HTTP_READ_TIMEOUT;
        this.mRetryCount = 0;
        this.mZeroBytesAllowed = true;
        this.mUseSecureConnection = false;
        this.mResponseCode = 0;
        this.mTotalBytes = 0;
        this.mDo_post = true;
        this.mAppendClient = true;
        this.mGzipEncoded = true;
        this.mResponseGzipped = false;
        this.mUrl = null;
        this.mPostParams = null;
        this.mContentType = "application/x-www-form-urlencoded;charset=utf-8";
        this.isError = false;
        this.mResponse = null;
        this.bLogPostParams = true;
    }

    public ServerBase(String url, String post) {
        this.mReadTimeOut = HTTP_READ_TIMEOUT;
        this.mConnectTimeOut = HTTP_READ_TIMEOUT;
        this.mRetryCount = 0;
        this.mZeroBytesAllowed = true;
        this.mUseSecureConnection = false;
        this.mResponseCode = 0;
        this.mTotalBytes = 0;
        this.mDo_post = true;
        this.mAppendClient = true;
        this.mGzipEncoded = true;
        this.mResponseGzipped = false;
        this.mUrl = null;
        this.mPostParams = null;
        this.mContentType = "application/x-www-form-urlencoded;charset=utf-8";
        this.isError = false;
        this.mResponse = null;
        this.bLogPostParams = true;
        this.mUrl = url;
        if (post == null || post.length() <= 0) {
            this.mDo_post = false;
            return;
        }
        this.mPostParams = post;
        this.mDo_post = true;
    }

    public void disableGzipEncoding() {
        this.mGzipEncoded = false;
    }

    public boolean isResponseError() {
        return this.isError;
    }

    public String getResponeString() {
        if (this.mResponse != null) {
            return this.mResponse;
        }
        return Stomp.EMPTY;
    }

    public int getResponseCode() {
        return this.mResponseCode;
    }

    public String key() {
        String ConstructURL = ConstructURL();
        this.mUrl = ConstructURL;
        StringBuilder stringBuilder = new StringBuilder(ConstructURL);
        ConstructURL = ConstructPOST();
        this.mPostParams = ConstructURL;
        return stringBuilder.append(ConstructURL).toString();
    }

    public int hashCode() {
        return key().hashCode();
    }

    public void run() {
        try {
            int tries = this.mRetryCount + 1;
            while (tries > 0 && !Process(this.mDo_post)) {
                if (this.mResponseCode == 408) {
                    Diagnostics.m1957w(TAG(), "Request timed out.");
                    tries--;
                } else if (this.mResponseCode == 204) {
                    Diagnostics.m1957w(TAG(), "No content returned.");
                    tries--;
                } else {
                    ProcessFailure();
                    return;
                }
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    public boolean ProcessResponse() throws EOFException {
        return false;
    }

    public void ProcessFailure() {
    }

    protected String ConstructURL() {
        if (this.mUrl == null || this.mUrl.length() <= 0) {
            return Stomp.EMPTY;
        }
        return this.mUrl;
    }

    protected String ConstructPOST() {
        return null;
    }

    public boolean Process(boolean do_post) throws EOFException {
        boolean bReturn = true;
        try {
            onStart();
            this.mTotalBytes = doRequest(ConstructURL(), do_post, ConstructPOST());
            Diagnostics.m1951d(TAG(), "bytes returned=" + this.mTotalBytes);
            if (this.mTotalBytes > 0) {
                ProcessResponse();
            } else if (!this.mZeroBytesAllowed) {
                if (this.mResponseCode <= 0 || this.mResponseCode == 400) {
                    this.mResponseCode = 204;
                }
                bReturn = false;
                this.isError = true;
            }
        } catch (SocketTimeoutException e) {
            this.mResponseCode = 408;
            Diagnostics.m1957w(TAG(), "mResponseCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT");
            bReturn = false;
            this.isError = true;
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            this.mResponseCode = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH;
            Diagnostics.m1957w(TAG(), "mResponseCode = HttpURLConnection.HTTP_INTERNAL_ERROR");
            bReturn = false;
            this.isError = true;
        } catch (Throwable e3) {
            this.mResponseCode = AMQP.COMMAND_INVALID;
            Diagnostics.m1957w(TAG(), "mResponseCode = HttpURLConnection.HTTP_UNAVAILABLE");
            Diagnostics.m1958w(TAG(), e3);
            bReturn = false;
            this.isError = true;
        } catch (VirtualMachineError e4) {
            Diagnostics.m1959w(TAG(), e4);
            bReturn = false;
            this.isError = true;
        } catch (Throwable e32) {
            Diagnostics.m1958w(TAG(), e32);
            bReturn = false;
            this.isError = true;
        }
        onFinished(this.mResponseCode);
        return bReturn;
    }

    public int doRequest(String strURL, boolean do_post, String post_params) throws MalformedURLException, IOException {
        HttpURLConnection httpConnection;
        BufferedInputStream in;
        Buffer buf;
        OutOfMemoryError e;
        Throwable th;
        int offset;
        int ib;
        int i;
        int offset2;
        int totalSize = 0;
        if (strURL != null && strURL.length() > 0) {
            this.mUrl = strURL;
        }
        if (this.mUrl.length() == 0) {
            return 0;
        }
        URL url;
        if (post_params != null) {
            this.mPostParams = post_params;
        } else {
            this.mPostParams = Stomp.EMPTY;
            do_post = false;
        }
        if (Diagnostics.getInstance().isEnabled(4)) {
            if (this.bLogPostParams) {
                String str = this.mUrl;
                Diagnostics.m1951d(TAG(), r0 + this.mPostParams);
            } else {
                Diagnostics.m1951d(TAG(), this.mUrl);
            }
        }
        if (do_post || post_params == null) {
            url = new URL(this.mUrl);
        } else {
            url = new URL(this.mUrl + post_params);
        }
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(this.mConnectTimeOut);
            httpConnection.setReadTimeout(this.mReadTimeOut);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            if (this.mGzipEncoded) {
                httpConnection.setRequestProperty(ACCEPT_ENCODING, GZIP);
            }
            if (this.mContentType.length() > 0) {
                httpConnection.setRequestProperty(CONTENT_TYPE, this.mContentType);
            }
            addCustomConnectionInfo(httpConnection);
            if (!do_post || post_params == null) {
                httpConnection.setDoOutput(false);
                httpConnection.setRequestMethod(GET);
            } else {
                httpConnection.setDoOutput(true);
                httpConnection.setRequestMethod(POST);
                httpConnection.getOutputStream().write(post_params.getBytes());
            }
            this.mResponseCode = httpConnection.getResponseCode();
            int i2 = this.mResponseCode;
            if (r0 == 200) {
                in = null;
                List<Buffer> list = null;
                processResponseHeaders(httpConnection.getHeaderFields());
                this.mResponseGzipped = GZIP.equalsIgnoreCase(httpConnection.getContentEncoding());
                try {
                    BufferedInputStream in2 = new BufferedInputStream(httpConnection.getInputStream(), Flags.FLAG2);
                    try {
                        List<Buffer> buffers = new ArrayList();
                        int length = Flags.FLAG2;
                        while (length != -1) {
                            try {
                                byte[] buffer = new byte[Flags.FLAG2];
                                length = in2.read(buffer, 0, Flags.FLAG2);
                                if (length > 0) {
                                    buf = new Buffer(null);
                                    buf.buffer = buffer;
                                    buf.bytes = length;
                                    totalSize += length;
                                    buffers.add(buf);
                                }
                            } catch (OutOfMemoryError e2) {
                                e = e2;
                                list = buffers;
                                in = in2;
                            } catch (Throwable th2) {
                                th = th2;
                                list = buffers;
                                in = in2;
                            }
                        }
                        if (in2 != null) {
                            in2.close();
                        }
                        httpConnection.disconnect();
                        list = buffers;
                        in = in2;
                    } catch (OutOfMemoryError e3) {
                        e = e3;
                        in = in2;
                        e.printStackTrace();
                        if (in != null) {
                            in.close();
                        }
                        httpConnection.disconnect();
                        try {
                            this.data = new byte[totalSize];
                            offset = 0;
                            ib = 0;
                            while (ib < list.size()) {
                                buf = (Buffer) list.get(ib);
                                i = 0;
                                offset2 = offset;
                                while (true) {
                                    i2 = buf.bytes;
                                    if (i < r0) {
                                        break;
                                    }
                                    break;
                                    buf.buffer = null;
                                    ib++;
                                    offset = offset2;
                                    i++;
                                    offset2 = offset;
                                }
                            }
                        } catch (OutOfMemoryError e4) {
                            e4.printStackTrace();
                        }
                        return totalSize;
                    } catch (Throwable th3) {
                        th = th3;
                        in = in2;
                        if (in != null) {
                            in.close();
                        }
                        httpConnection.disconnect();
                        throw th;
                    }
                } catch (OutOfMemoryError e5) {
                    e4 = e5;
                    e4.printStackTrace();
                    if (in != null) {
                        in.close();
                    }
                    httpConnection.disconnect();
                    this.data = new byte[totalSize];
                    offset = 0;
                    ib = 0;
                    while (ib < list.size()) {
                        buf = (Buffer) list.get(ib);
                        i = 0;
                        offset2 = offset;
                        while (true) {
                            i2 = buf.bytes;
                            if (i < r0) {
                                break;
                            }
                            break;
                            buf.buffer = null;
                            ib++;
                            offset = offset2;
                            i++;
                            offset2 = offset;
                        }
                    }
                    return totalSize;
                }
                if (totalSize > 0 && list != null) {
                    this.data = new byte[totalSize];
                    offset = 0;
                    ib = 0;
                    while (ib < list.size()) {
                        buf = (Buffer) list.get(ib);
                        i = 0;
                        offset2 = offset;
                        while (true) {
                            i2 = buf.bytes;
                            if (i < r0 && offset2 < totalSize) {
                                offset = offset2 + 1;
                                this.data[offset2] = buf.buffer[i];
                                i++;
                                offset2 = offset;
                            }
                        }
                        buf.buffer = null;
                        ib++;
                        offset = offset2;
                    }
                }
            } else {
                Diagnostics.m1957w(TAG(), "\tmResponseCode=" + this.mResponseCode);
            }
            return totalSize;
        } catch (Throwable th4) {
        }
    }

    protected void addCustomConnectionInfo(HttpURLConnection connection) {
    }

    protected void processResponseHeaders(Map<String, List<String>> map) {
    }

    protected void onStart() {
        this.mTotalBytes = 0;
        this.isError = false;
    }

    protected void onFinished(int completionCode) {
    }
}
