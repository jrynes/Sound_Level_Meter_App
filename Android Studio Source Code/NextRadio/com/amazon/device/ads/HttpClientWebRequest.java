package com.amazon.device.ads;

import com.amazon.device.ads.WebRequest.HttpMethod;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestStatus;
import com.amazon.device.ads.WebRequest.WebResponse;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xbill.DNS.Zone;

class HttpClientWebRequest extends WebRequest {
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String LOGTAG;
    private HttpClient client;

    /* renamed from: com.amazon.device.ads.HttpClientWebRequest.1 */
    static /* synthetic */ class C03021 {
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

    HttpClientWebRequest() {
    }

    static {
        LOGTAG = HttpClientWebRequest.class.getSimpleName();
    }

    protected WebResponse doHttpNetworkCall(URL url) throws WebRequestException {
        HttpRequestBase httpRequest = createHttpRequest(url);
        HttpParams httpParams = createHttpParams();
        if (this.client != null) {
            closeConnection();
        }
        this.client = new DefaultHttpClient(httpParams);
        try {
            return parseResponse(this.client.execute(httpRequest));
        } catch (ClientProtocolException e) {
            getLogger().m640e("Invalid client protocol: %s", e.getMessage());
            throw new WebRequestException(WebRequestStatus.INVALID_CLIENT_PROTOCOL, "Invalid client protocol", e);
        } catch (IOException e2) {
            getLogger().m640e("IOException during client execution: %s", e2.getMessage());
            throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "IOException during client execution", e2);
        }
    }

    protected void closeConnection() {
        if (this.client != null) {
            this.client.getConnectionManager().closeIdleConnections(0, TimeUnit.MILLISECONDS);
            this.client.getConnectionManager().closeExpiredConnections();
            this.client = null;
        }
    }

    protected HttpRequestBase createHttpRequest(URL url) throws WebRequestException {
        HttpRequestBase httpRequest = null;
        try {
            URI uri = createURI(url);
            switch (C03021.$SwitchMap$com$amazon$device$ads$WebRequest$HttpMethod[getHttpMethod().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    httpRequest = new HttpGet(uri);
                    break;
                case Zone.SECONDARY /*2*/:
                    httpRequest = new HttpPost(uri);
                    prepareRequestBody((HttpPost) httpRequest);
                    break;
            }
            for (Entry<String, String> header : this.headers.entrySet()) {
                if (!(header.getValue() == null || ((String) header.getValue()).equals(Stomp.EMPTY))) {
                    httpRequest.addHeader((String) header.getKey(), (String) header.getValue());
                }
            }
            logUrl(uri.toString());
            if (this.logRequestBodyEnabled && getRequestBody() != null) {
                getLogger().m638d("Request Body: %s", getRequestBody());
            }
            return httpRequest;
        } catch (URISyntaxException e) {
            getLogger().m640e("Problem with URI syntax: %s", e.getMessage());
            throw new WebRequestException(WebRequestStatus.MALFORMED_URL, "Problem with URI syntax", e);
        }
    }

    private void prepareRequestBody(HttpPost httpPost) throws WebRequestException {
        String charset = this.charset;
        if (charset == null) {
            charset = HttpRequest.CHARSET_UTF8;
        }
        String contentType = this.contentType;
        if (contentType == null) {
            contentType = Stomp.TEXT_PLAIN;
        }
        if (this.requestBody != null) {
            prepareStringRequestBody(httpPost, contentType, charset);
        } else {
            prepareFormRequestBody(httpPost, charset);
        }
    }

    private void prepareStringRequestBody(HttpPost requestBase, String contentType, String charset) throws WebRequestException {
        try {
            StringEntity entity = new StringEntity(this.requestBody, charset);
            entity.setContentType(contentType);
            requestBase.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            getLogger().m640e("Unsupported character encoding used while creating the request. ", e.getMessage());
            throw new WebRequestException(WebRequestStatus.UNSUPPORTED_ENCODING, "Unsupported character encoding used while creating the request.", e);
        }
    }

    private void prepareFormRequestBody(HttpPost httpPost, String charset) throws WebRequestException {
        List<NameValuePair> postParams = new ArrayList();
        for (Entry<String, String> param : this.postParameters.entrySet()) {
            postParams.add(new BasicNameValuePair((String) param.getKey(), (String) param.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, HttpRequest.CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            getLogger().m640e("Unsupported character encoding used while creating the request: %s", e.getMessage());
            throw new WebRequestException(WebRequestStatus.UNSUPPORTED_ENCODING, "Unsupported character encoding used while creating the request", e);
        }
    }

    protected HttpParams createHttpParams() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, getTimeout());
        HttpConnectionParams.setSoTimeout(httpParams, getTimeout());
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        return httpParams;
    }

    protected WebResponse parseResponse(HttpResponse response) throws WebRequestException {
        WebResponse webResponse = new WebResponse();
        webResponse.setHttpStatusCode(response.getStatusLine().getStatusCode());
        webResponse.setHttpStatus(response.getStatusLine().getReasonPhrase());
        if (webResponse.getHttpStatusCode() == AMQP.REPLY_SUCCESS) {
            HttpEntity entity = response.getEntity();
            if (!(entity == null || entity.getContentLength() == 0)) {
                try {
                    webResponse.setInputStream(entity.getContent());
                } catch (IOException e) {
                    getLogger().m640e("IOException while reading the input stream from response: %s", e.getMessage());
                    throw new WebRequestException(WebRequestStatus.NETWORK_FAILURE, "IOException while reading the input stream from response", e);
                }
            }
        }
        return webResponse;
    }

    protected String getSubLogTag() {
        return LOGTAG;
    }
}
