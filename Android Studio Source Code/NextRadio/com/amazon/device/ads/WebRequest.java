package com.amazon.device.ads;

import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

abstract class WebRequest {
    private static final String CHARSET_KEY = "charset";
    public static final String CHARSET_UTF_16 = "UTF-16";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CONTENT_TYPE_CSS = "text/css";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_JAVASCRIPT = "application/javascript";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_PLAIN_TEXT = "text/plain";
    public static final int DEFAULT_PORT = -1;
    public static final int DEFAULT_TIMEOUT = 20000;
    private static final String HEADER_ACCEPT_KEY = "Accept";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String LOGTAG;
    String acceptContentType;
    String charset;
    String contentType;
    private boolean disconnectEnabled;
    protected final HashMap<String, String> headers;
    private HttpMethod httpMethod;
    boolean logRequestBodyEnabled;
    boolean logResponseEnabled;
    private String logTag;
    protected boolean logUrlEnabled;
    private final MobileAdsLogger logger;
    private MetricsCollector metricsCollector;
    private String nonSecureHost;
    private String path;
    private int port;
    protected HashMap<String, String> postParameters;
    protected QueryStringParameters queryStringParameters;
    String requestBody;
    protected boolean secure;
    private String secureHost;
    protected MetricType serviceCallLatencyMetric;
    private int timeout;
    private String urlString;

    public enum HttpMethod {
        GET(HttpRequest.METHOD_GET),
        POST(HttpRequest.METHOD_POST);
        
        private final String methodString;

        private HttpMethod(String methodString) {
            this.methodString = methodString;
        }

        public String toString() {
            return this.methodString;
        }
    }

    static class QueryStringParameters {
        private final HashMap<String, String> params;
        private String rawAppendage;

        QueryStringParameters() {
            this.params = new HashMap();
        }

        int size() {
            return this.params.size();
        }

        void setRawAppendage(String rawAppendage) {
            this.rawAppendage = rawAppendage;
        }

        void putUrlEncoded(String param, boolean value) {
            putUrlEncoded(param, Boolean.toString(value));
        }

        void putUrlEncodedIfNotNullOrEmpty(String param, String value) {
            putUrlEncodedIfTrue(param, value, !StringUtils.isNullOrEmpty(value));
        }

        void putUrlEncodedIfTrue(String param, String value, boolean check) {
            if (check) {
                putUrlEncoded(param, value);
            }
        }

        String get(String name) {
            if (!StringUtils.isNullOrWhiteSpace(name)) {
                return (String) this.params.get(name);
            }
            throw new IllegalArgumentException("The name must not be null or empty string.");
        }

        void putUrlEncoded(String name, String value) {
            if (StringUtils.isNullOrWhiteSpace(name)) {
                throw new IllegalArgumentException("The name must not be null or empty string.");
            } else if (value == null) {
                this.params.remove(name);
            } else {
                this.params.put(name, value);
            }
        }

        String putUnencoded(String name, String value) {
            WebUtils2 webUtils = new WebUtils2();
            String encodedName = webUtils.getURLEncodedString(name);
            putUrlEncoded(encodedName, webUtils.getURLEncodedString(value));
            return encodedName;
        }

        void append(StringBuilder builder) {
            if (size() != 0 || !StringUtils.isNullOrEmpty(this.rawAppendage)) {
                builder.append("?");
                boolean first = true;
                for (Entry<String, String> param : this.params.entrySet()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append("&");
                    }
                    builder.append((String) param.getKey());
                    builder.append("=");
                    builder.append((String) param.getValue());
                }
                if (this.rawAppendage != null && !this.rawAppendage.equals(Stomp.EMPTY)) {
                    if (size() != 0) {
                        builder.append("&");
                    }
                    builder.append(this.rawAppendage);
                }
            }
        }
    }

    public class WebRequestException extends Exception {
        private static final long serialVersionUID = -4980265484926465548L;
        private final WebRequestStatus status;

        protected WebRequestException(WebRequestStatus status, String message, Throwable cause) {
            super(message, cause);
            this.status = status;
        }

        protected WebRequestException(WebRequest webRequest, WebRequestStatus status, String message) {
            this(status, message, null);
        }

        public WebRequestStatus getStatus() {
            return this.status;
        }
    }

    public static class WebRequestFactory {
        private final AndroidBuildInfo androidBuildInfo;

        public WebRequestFactory() {
            this(new AndroidBuildInfo());
        }

        WebRequestFactory(AndroidBuildInfo androidBuildInfo) {
            this.androidBuildInfo = androidBuildInfo;
        }

        public WebRequest createWebRequest() {
            if (AndroidTargetUtils.isAtOrBelowAndroidAPI(this.androidBuildInfo, 7)) {
                return new HttpClientWebRequest();
            }
            return new HttpURLConnectionWebRequest();
        }

        public WebRequest createJSONGetWebRequest() {
            WebRequest request = createWebRequest();
            request.setHttpMethod(HttpMethod.GET);
            request.putHeader(WebRequest.HEADER_ACCEPT_KEY, WebRequest.CONTENT_TYPE_JSON);
            return request;
        }

        public WebRequest createJSONPostWebRequest() {
            WebRequest request = createWebRequest();
            request.convertToJSONPostRequest();
            return request;
        }
    }

    class WebRequestInputStream extends InputStream {
        private final InputStream decoratedStream;

        public WebRequestInputStream(InputStream decoratedStream) {
            this.decoratedStream = decoratedStream;
        }

        public int read() throws IOException {
            return this.decoratedStream.read();
        }

        public void close() throws IOException {
            this.decoratedStream.close();
            if (WebRequest.this.disconnectEnabled) {
                WebRequest.this.closeConnection();
            }
        }
    }

    public enum WebRequestStatus {
        NETWORK_FAILURE,
        NETWORK_TIMEOUT,
        MALFORMED_URL,
        INVALID_CLIENT_PROTOCOL,
        UNSUPPORTED_ENCODING
    }

    public class WebResponse {
        private String httpStatus;
        private int httpStatusCode;
        private WebRequestInputStream inputStream;

        protected WebResponse() {
        }

        protected void setInputStream(InputStream inputStream) {
            this.inputStream = new WebRequestInputStream(inputStream);
        }

        public ResponseReader getResponseReader() {
            ResponseReader reader = new ResponseReader(this.inputStream);
            reader.enableLog(WebRequest.this.logResponseEnabled);
            reader.setExternalLogTag(WebRequest.this.getLogTag());
            return reader;
        }

        public int getHttpStatusCode() {
            return this.httpStatusCode;
        }

        protected void setHttpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }

        public boolean isHttpStatusCodeOK() {
            return getHttpStatusCode() == AMQP.REPLY_SUCCESS;
        }

        public String getHttpStatus() {
            return this.httpStatus;
        }

        protected void setHttpStatus(String httpStatus) {
            this.httpStatus = httpStatus;
        }
    }

    protected abstract void closeConnection();

    protected abstract WebResponse doHttpNetworkCall(URL url) throws WebRequestException;

    protected abstract String getSubLogTag();

    static {
        LOGTAG = WebRequest.class.getSimpleName();
    }

    WebRequest() {
        this.requestBody = null;
        this.acceptContentType = null;
        this.contentType = null;
        this.charset = null;
        this.urlString = null;
        this.secureHost = null;
        this.nonSecureHost = null;
        this.path = null;
        this.port = DEFAULT_PORT;
        this.httpMethod = HttpMethod.GET;
        this.timeout = DEFAULT_TIMEOUT;
        this.logRequestBodyEnabled = false;
        this.logResponseEnabled = false;
        this.logUrlEnabled = false;
        this.secure = false;
        this.logTag = LOGTAG;
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(this.logTag);
        this.queryStringParameters = new QueryStringParameters();
        this.headers = new HashMap();
        this.postParameters = new HashMap();
        this.secure = DebugProperties.getInstance().getDebugPropertyAsBoolean(DebugProperties.DEBUG_TLS_ENABLED, Boolean.valueOf(Settings.getInstance().getBoolean("tlsEnabled", false))).booleanValue();
        this.disconnectEnabled = true;
    }

    public void convertToJSONPostRequest() {
        setHttpMethod(HttpMethod.POST);
        putHeader(HEADER_ACCEPT_KEY, CONTENT_TYPE_JSON);
        putHeader(HEADER_CONTENT_TYPE, "application/json; charset=UTF-8");
    }

    public WebResponse makeCall() throws WebRequestException {
        if (ThreadUtils.isOnMainThread()) {
            this.logger.m639e("The network request should not be performed on the main thread.");
        }
        setContentTypeHeaders();
        String urlString = getUrl();
        try {
            URL url = createURL(urlString);
            writeMetricStart(this.serviceCallLatencyMetric);
            try {
                WebResponse response = doHttpNetworkCall(url);
                writeMetricStop(this.serviceCallLatencyMetric);
                if (this.logResponseEnabled) {
                    this.logger.m638d("Response: %s %s", Integer.valueOf(response.getHttpStatusCode()), response.getHttpStatus());
                }
                return response;
            } catch (WebRequestException e) {
                throw e;
            } catch (Throwable th) {
                writeMetricStop(this.serviceCallLatencyMetric);
            }
        } catch (MalformedURLException e2) {
            this.logger.m640e("Problem with URI syntax: %s", e2.getMessage());
            throw new WebRequestException(WebRequestStatus.MALFORMED_URL, "Could not construct URL from String " + urlString, e2);
        }
    }

    public void enableLogUrl(boolean enabled) {
        this.logUrlEnabled = enabled;
    }

    public void enableLogRequestBody(boolean enabled) {
        this.logRequestBodyEnabled = enabled;
    }

    public void enableLogResponse(boolean enabled) {
        this.logResponseEnabled = enabled;
    }

    public void enableLog(boolean enabled) {
        enableLogUrl(enabled);
        enableLogRequestBody(enabled);
        enableLogResponse(enabled);
    }

    protected void logUrl(String url) {
        if (this.logUrlEnabled) {
            this.logger.m638d("%s %s", getHttpMethod(), url);
        }
    }

    public String getQueryParameter(String name) {
        return this.queryStringParameters.get(name);
    }

    public void putUrlEncodedQueryParameter(String name, String value) {
        this.queryStringParameters.putUrlEncoded(name, value);
    }

    public String putUnencodedQueryParameter(String name, String value) {
        return this.queryStringParameters.putUnencoded(name, value);
    }

    public void setQueryStringParameters(QueryStringParameters queryStringParameters) {
        this.queryStringParameters = queryStringParameters;
    }

    public String getPostParameter(String name) {
        if (!StringUtils.isNullOrWhiteSpace(name)) {
            return (String) this.postParameters.get(name);
        }
        throw new IllegalArgumentException("The name must not be null or empty string.");
    }

    public void putPostParameter(String name, String value) {
        if (StringUtils.isNullOrWhiteSpace(name)) {
            throw new IllegalArgumentException("The name must not be null or empty string.");
        } else if (value == null) {
            this.postParameters.remove(name);
        } else {
            this.postParameters.put(name, value);
        }
    }

    public String getHeader(String name) {
        if (!StringUtils.isNullOrWhiteSpace(name)) {
            return (String) this.headers.get(name);
        }
        throw new IllegalArgumentException("The name must not be null or empty string.");
    }

    public void putHeader(String name, String value) {
        if (StringUtils.isNullOrWhiteSpace(name)) {
            throw new IllegalArgumentException("The name must not be null or empty string.");
        }
        this.headers.put(name, value);
    }

    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        if (httpMethod == null) {
            throw new IllegalArgumentException("The httpMethod must not be null.");
        }
        this.httpMethod = httpMethod;
    }

    public String getHost() {
        return this.secure ? this.secureHost : this.nonSecureHost;
    }

    public void setHost(String host) {
        if (StringUtils.isNullOrWhiteSpace(host)) {
            throw new IllegalArgumentException("The host must not be null.");
        }
        this.secureHost = host;
        this.nonSecureHost = host;
    }

    public void setSecureHost(String secureHost) {
        if (StringUtils.isNullOrWhiteSpace(secureHost)) {
            throw new IllegalArgumentException("The host must not be null.");
        }
        this.secureHost = secureHost;
    }

    public void setNonSecureHost(String nonSecureHost) {
        if (StringUtils.isNullOrWhiteSpace(nonSecureHost)) {
            throw new IllegalArgumentException("The host must not be null.");
        }
        this.nonSecureHost = nonSecureHost;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getUseSecure() {
        return this.secure;
    }

    public void setUseSecure(boolean secure) {
        this.secure = secure;
    }

    public void setUrlString(String urlString) {
        if (urlString != null && this.secure && urlString.startsWith("http:")) {
            urlString = urlString.replaceFirst("http", "https");
        }
        this.urlString = urlString;
    }

    public String getUrlString() {
        return this.urlString;
    }

    public void setRequestBodyString(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestBodyString() {
        return this.requestBody;
    }

    public String getRequestBody() {
        if (getRequestBodyString() != null) {
            return getRequestBodyString();
        }
        if (this.postParameters.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> entry : this.postParameters.entrySet()) {
            builder.append((String) entry.getKey()).append('=').append((String) entry.getValue()).append(";\n");
        }
        return builder.toString();
    }

    public void setAcceptContentType(String acceptContentType) {
        this.acceptContentType = this.contentType;
    }

    public String getAcceptContentType() {
        return this.acceptContentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return this.charset;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setMetricsCollector(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    public void setServiceCallLatencyMetric(MetricType metric) {
        this.serviceCallLatencyMetric = metric;
    }

    public void setAdditionalQueryParamsString(String paramsString) {
        this.queryStringParameters.setRawAppendage(paramsString);
    }

    public void setExternalLogTag(String externalLogTag) {
        if (externalLogTag == null) {
            this.logTag = LOGTAG + " " + getSubLogTag();
        } else {
            this.logTag = externalLogTag + " " + LOGTAG + " " + getSubLogTag();
        }
        this.logger.withLogTag(this.logTag);
    }

    public boolean getDisconnectEnabled() {
        return this.disconnectEnabled;
    }

    public void setDisconnectEnabled(boolean disconnectEnabled) {
        this.disconnectEnabled = disconnectEnabled;
    }

    protected MobileAdsLogger getLogger() {
        return this.logger;
    }

    private String getLogTag() {
        return this.logTag;
    }

    protected void setContentTypeHeaders() {
        if (this.acceptContentType != null) {
            putHeader(HEADER_ACCEPT_KEY, this.contentType);
        }
        if (this.contentType != null) {
            String contentType = this.contentType;
            if (this.charset != null) {
                contentType = contentType + "; charset=" + this.charset;
            }
            putHeader(HEADER_CONTENT_TYPE, contentType);
        }
    }

    protected void writeMetricStart(MetricType metric) {
        if (metric != null && this.metricsCollector != null) {
            this.metricsCollector.startMetric(metric);
        }
    }

    protected void writeMetricStop(MetricType metric) {
        if (metric != null && this.metricsCollector != null) {
            this.metricsCollector.stopMetric(metric);
        }
    }

    protected URI createUri() throws URISyntaxException, MalformedURLException {
        return new URL(getUrlString()).toURI();
    }

    protected URI createURI(String url) throws MalformedURLException, URISyntaxException {
        return createURI(createURL(url));
    }

    protected URI createURI(URL url) throws URISyntaxException {
        return url.toURI();
    }

    protected URL createURL(String urlString) throws MalformedURLException {
        return new URL(urlString);
    }

    protected void appendQuery(StringBuilder builder) {
        this.queryStringParameters.append(builder);
    }

    protected String getScheme() {
        if (getUseSecure()) {
            return "https";
        }
        return "http";
    }

    public String toString() {
        return getUrl();
    }

    protected String getUrl() {
        if (this.urlString != null) {
            return this.urlString;
        }
        StringBuilder builder = new StringBuilder(getScheme());
        builder.append("://");
        builder.append(getHost());
        if (getPort() != DEFAULT_PORT) {
            builder.append(Headers.SEPERATOR);
            builder.append(getPort());
        }
        builder.append(getPath());
        appendQuery(builder);
        return builder.toString();
    }
}
