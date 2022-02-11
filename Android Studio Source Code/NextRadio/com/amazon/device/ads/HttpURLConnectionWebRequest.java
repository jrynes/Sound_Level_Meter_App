package com.amazon.device.ads;

import com.amazon.device.ads.WebRequest.HttpMethod;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestStatus;
import com.amazon.device.ads.WebRequest.WebResponse;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Zone;

class HttpURLConnectionWebRequest extends WebRequest {
    private static final String LOGTAG;
    private HttpURLConnection connection;

    /* renamed from: com.amazon.device.ads.HttpURLConnectionWebRequest.1 */
    static /* synthetic */ class C03031 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod;

        static {
            $SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod = new int[HttpMethod.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod[HttpMethod.GET.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod[HttpMethod.POST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    HttpURLConnectionWebRequest() {
    }

    static {
        LOGTAG = HttpURLConnectionWebRequest.class.getSimpleName();
    }

    protected WebResponse doHttpNetworkCall(URL url) throws WebRequestException {
        if (this.connection != null) {
            closeConnection();
        }
        try {
            this.connection = openConnection(url);
            setupRequestProperties(this.connection);
            try {
                this.connection.connect();
                return prepareResponse(this.connection);
            } catch (SocketTimeoutException e) {
                getLogger().m640e("Socket timed out while connecting to URL: %s", e.getMessage());
                throw new WebRequestException(WebRequestStatus.NETWORK_TIMEOUT, "Socket timed out while connecting to URL", e);
            } catch (Exception e2) {
                getLogger().m640e("Problem while connecting to URL: %s", e2.getMessage());
                throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Probem while connecting to URL", e2);
            }
        } catch (IOException e3) {
            getLogger().m640e("Problem while opening the URL connection: %s", e3.getMessage());
            throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Problem while opening the URL connection", e3);
        }
    }

    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    protected void closeConnection() {
        if (this.connection != null) {
            this.connection.disconnect();
            this.connection = null;
        }
    }

    protected void setupRequestProperties(HttpURLConnection connection) throws WebRequestException {
        try {
            connection.setRequestMethod(getHttpMethod().name());
            for (Entry<String, String> header : this.headers.entrySet()) {
                if (!(header.getValue() == null || ((String) header.getValue()).equals(Stomp.EMPTY))) {
                    connection.setRequestProperty((String) header.getKey(), (String) header.getValue());
                }
            }
            connection.setConnectTimeout(getTimeout());
            connection.setReadTimeout(getTimeout());
            logUrl(connection.getURL().toString());
            switch (C03031.$SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod[getHttpMethod().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    connection.setDoOutput(false);
                case Zone.SECONDARY /*2*/:
                    connection.setDoOutput(true);
                    writePostBody(connection);
                default:
            }
        } catch (ProtocolException e) {
            getLogger().m640e("Invalid client protocol: %s", e.getMessage());
            throw new WebRequestException(WebRequestStatus.INVALID_CLIENT_PROTOCOL, "Invalid client protocol", e);
        }
    }

    private void writePostBody(HttpURLConnection connection) throws WebRequestException {
        IOException e;
        Throwable th;
        StringBuilder postBody = new StringBuilder();
        if (this.requestBody != null) {
            postBody.append(this.requestBody);
        } else if (!(this.postParameters == null || this.postParameters.isEmpty())) {
            for (Entry<String, String> param : this.postParameters.entrySet()) {
                postBody.append((String) param.getKey()).append("=").append(WebUtils.getURLEncodedString((String) param.getValue())).append("&");
            }
            postBody.deleteCharAt(postBody.lastIndexOf("&"));
        }
        if (this.logRequestBodyEnabled && getRequestBody() != null) {
            getLogger().m638d("Request Body: %s", getRequestBody());
        }
        OutputStreamWriter out = null;
        try {
            OutputStreamWriter out2 = new OutputStreamWriter(connection.getOutputStream(), HttpRequest.CHARSET_UTF8);
            try {
                out2.write(postBody.toString());
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e2) {
                        getLogger().m640e("Problem while closing output stream writer for request body: %s", e2.getMessage());
                        throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Problem while closing output stream writer for request body", e2);
                    }
                }
            } catch (IOException e3) {
                e2 = e3;
                out = out2;
                try {
                    getLogger().m640e("Problem while creating output steam for request body: %s", e2.getMessage());
                    throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Problem while creating output steam for request body", e2);
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e22) {
                            getLogger().m640e("Problem while closing output stream writer for request body: %s", e22.getMessage());
                            throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Problem while closing output stream writer for request body", e22);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e22 = e4;
            getLogger().m640e("Problem while creating output steam for request body: %s", e22.getMessage());
            throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "Problem while creating output steam for request body", e22);
        }
    }

    protected WebResponse prepareResponse(HttpURLConnection connection) throws WebRequestException {
        WebResponse webResponse = new WebResponse();
        try {
            webResponse.setHttpStatusCode(connection.getResponseCode());
            webResponse.setHttpStatus(connection.getResponseMessage());
            if (webResponse.getHttpStatusCode() == AMQP.REPLY_SUCCESS) {
                try {
                    webResponse.setInputStream(connection.getInputStream());
                } catch (IOException e) {
                    getLogger().m640e("IOException while reading the input stream from response: %s", e.getMessage());
                    throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "IOException while reading the input stream from response", e);
                }
            }
            return webResponse;
        } catch (SocketTimeoutException e2) {
            getLogger().m640e("Socket Timeout while getting the response status code: %s", e2.getMessage());
            throw new WebRequestException(WebRequestStatus.NETWORK_TIMEOUT, "Socket Timeout while getting the response status code", e2);
        } catch (IOException e3) {
            getLogger().m640e("IOException while getting the response status code: %s", e3.getMessage());
            throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "IOException while getting the response status code", e3);
        } catch (IndexOutOfBoundsException e4) {
            getLogger().m640e("IndexOutOfBoundsException while getting the response status code: %s", e4.getMessage());
            throw new WebRequestException(WebRequestStatus.MALFORMED_URL, "IndexOutOfBoundsException while getting the response status code", e4);
        }
    }

    protected String getSubLogTag() {
        return LOGTAG;
    }
}
