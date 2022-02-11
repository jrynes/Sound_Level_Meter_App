package org.apache.activemq.util;

import com.rabbitmq.client.impl.AMQImpl.Queue.Delete;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

public class URISupport {

    public static class CompositeData {
        private URI[] components;
        private String fragment;
        private String host;
        private Map<String, String> parameters;
        private String path;
        private String scheme;

        public URI[] getComponents() {
            return this.components;
        }

        public String getFragment() {
            return this.fragment;
        }

        public Map<String, String> getParameters() {
            return this.parameters;
        }

        public String getScheme() {
            return this.scheme;
        }

        public String getPath() {
            return this.path;
        }

        public String getHost() {
            return this.host;
        }

        public URI toURI() throws URISyntaxException {
            StringBuffer sb = new StringBuffer();
            if (this.scheme != null) {
                sb.append(this.scheme);
                sb.append(':');
            }
            if (this.host == null || this.host.length() == 0) {
                sb.append('(');
                for (int i = 0; i < this.components.length; i++) {
                    if (i != 0) {
                        sb.append(ActiveMQDestination.COMPOSITE_SEPERATOR);
                    }
                    sb.append(this.components[i].toString());
                }
                sb.append(')');
            } else {
                sb.append(this.host);
            }
            if (this.path != null) {
                sb.append('/');
                sb.append(this.path);
            }
            if (!this.parameters.isEmpty()) {
                sb.append("?");
                sb.append(URISupport.createQueryString(this.parameters));
            }
            if (this.fragment != null) {
                sb.append("#");
                sb.append(this.fragment);
            }
            return new URI(sb.toString());
        }
    }

    public static Map<String, String> parseQuery(String uri) throws URISyntaxException {
        try {
            uri = uri.substring(uri.lastIndexOf("?") + 1);
            Map<String, String> rc = new HashMap();
            if (!(uri == null || uri.isEmpty())) {
                String[] parameters = uri.split("&");
                for (int i = 0; i < parameters.length; i++) {
                    int p = parameters[i].indexOf("=");
                    if (p >= 0) {
                        rc.put(URLDecoder.decode(parameters[i].substring(0, p), HttpRequest.CHARSET_UTF8), URLDecoder.decode(parameters[i].substring(p + 1), HttpRequest.CHARSET_UTF8));
                    } else {
                        rc.put(parameters[i], null);
                    }
                }
            }
            return rc;
        } catch (UnsupportedEncodingException e) {
            throw ((URISyntaxException) new URISyntaxException(e.toString(), "Invalid encoding").initCause(e));
        }
    }

    public static Map<String, String> parseParameters(URI uri) throws URISyntaxException {
        if (!isCompositeURI(uri)) {
            return uri.getQuery() == null ? emptyMap() : parseQuery(stripPrefix(uri.getQuery(), "?"));
        } else {
            CompositeData data = parseComposite(uri);
            Map<String, String> parameters = new HashMap();
            parameters.putAll(data.getParameters());
            if (parameters.isEmpty()) {
                parameters = emptyMap();
            }
            return parameters;
        }
    }

    public static URI applyParameters(URI uri, Map<String, String> queryParameters) throws URISyntaxException {
        return applyParameters(uri, queryParameters, Stomp.EMPTY);
    }

    public static URI applyParameters(URI uri, Map<String, String> queryParameters, String optionPrefix) throws URISyntaxException {
        if (queryParameters == null || queryParameters.isEmpty()) {
            return uri;
        }
        StringBuffer newQuery = uri.getRawQuery() != null ? new StringBuffer(uri.getRawQuery()) : new StringBuffer();
        for (Entry<String, String> param : queryParameters.entrySet()) {
            if (((String) param.getKey()).startsWith(optionPrefix)) {
                if (newQuery.length() != 0) {
                    newQuery.append('&');
                }
                newQuery.append(((String) param.getKey()).substring(optionPrefix.length())).append('=').append((String) param.getValue());
            }
        }
        return createURIWithQuery(uri, newQuery.toString());
    }

    private static Map<String, String> emptyMap() {
        return Collections.EMPTY_MAP;
    }

    public static URI removeQuery(URI uri) throws URISyntaxException {
        return createURIWithQuery(uri, null);
    }

    public static URI createURIWithQuery(URI uri, String query) throws URISyntaxException {
        String schemeSpecificPart = uri.getRawSchemeSpecificPart();
        int questionMark = schemeSpecificPart.lastIndexOf("?");
        if (questionMark < schemeSpecificPart.lastIndexOf(")")) {
            questionMark = -1;
        }
        if (questionMark > 0) {
            schemeSpecificPart = schemeSpecificPart.substring(0, questionMark);
        }
        if (query != null && query.length() > 0) {
            schemeSpecificPart = schemeSpecificPart + "?" + query;
        }
        return new URI(uri.getScheme(), schemeSpecificPart, uri.getFragment());
    }

    public static CompositeData parseComposite(URI uri) throws URISyntaxException {
        CompositeData rc = new CompositeData();
        rc.scheme = uri.getScheme();
        parseComposite(uri, rc, stripPrefix(uri.getRawSchemeSpecificPart().trim(), "//").trim());
        rc.fragment = uri.getFragment();
        return rc;
    }

    public static boolean isCompositeURI(URI uri) {
        String ssp = stripPrefix(uri.getRawSchemeSpecificPart().trim(), "//").trim();
        if (ssp.indexOf(40) == 0 && checkParenthesis(ssp)) {
            return true;
        }
        return false;
    }

    public static int indexOfParenthesisMatch(String str, int first) throws URISyntaxException {
        if (first < 0 || first > str.length()) {
            throw new IllegalArgumentException("Invalid position for first parenthesis: " + first);
        } else if (str.charAt(first) != '(') {
            throw new IllegalArgumentException("character at indicated position is not a parenthesis");
        } else {
            int depth = 1;
            char[] array = str.toCharArray();
            int index = first + 1;
            while (index < array.length) {
                char current = array[index];
                if (current == '(') {
                    depth++;
                } else if (current == ')') {
                    depth--;
                    if (depth == 0) {
                        break;
                    }
                } else {
                    continue;
                }
                index++;
            }
            if (depth == 0) {
                return index;
            }
            throw new URISyntaxException(str, "URI did not contain a matching parenthesis.");
        }
    }

    private static void parseComposite(URI uri, CompositeData rc, String ssp) throws URISyntaxException {
        if (checkParenthesis(ssp)) {
            int p;
            String componentString;
            String params;
            int initialParen = ssp.indexOf("(");
            if (initialParen == 0) {
                rc.host = ssp.substring(0, initialParen);
                p = rc.host.indexOf(ReadOnlyContext.SEPARATOR);
                if (p >= 0) {
                    rc.path = rc.host.substring(p);
                    rc.host = rc.host.substring(0, p);
                }
                p = indexOfParenthesisMatch(ssp, initialParen);
                componentString = ssp.substring(initialParen + 1, p);
                params = ssp.substring(p + 1).trim();
            } else {
                componentString = ssp;
                params = Stomp.EMPTY;
            }
            String[] components = splitComponents(componentString);
            rc.components = new URI[components.length];
            for (int i = 0; i < components.length; i++) {
                rc.components[i] = new URI(components[i].trim());
            }
            p = params.indexOf("?");
            if (p >= 0) {
                if (p > 0) {
                    rc.path = stripPrefix(params.substring(0, p), ReadOnlyContext.SEPARATOR);
                }
                rc.parameters = parseQuery(params.substring(p + 1));
                return;
            }
            if (params.length() > 0) {
                rc.path = stripPrefix(params, ReadOnlyContext.SEPARATOR);
            }
            rc.parameters = emptyMap();
            return;
        }
        throw new URISyntaxException(uri.toString(), "Not a matching number of '(' and ')' parenthesis");
    }

    private static String[] splitComponents(String str) {
        List<String> l = new ArrayList();
        int last = 0;
        int depth = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case Delete.INDEX /*40*/:
                    depth++;
                    break;
                case Service.GRAPHICS /*41*/:
                    depth--;
                    break;
                case Service.MPM_FLAGS /*44*/:
                    if (depth != 0) {
                        break;
                    }
                    l.add(str.substring(last, i));
                    last = i + 1;
                    break;
                default:
                    break;
            }
        }
        String s = str.substring(last);
        if (s.length() != 0) {
            l.add(s);
        }
        String[] rc = new String[l.size()];
        l.toArray(rc);
        return rc;
    }

    public static String stripPrefix(String value, String prefix) {
        if (value.startsWith(prefix)) {
            return value.substring(prefix.length());
        }
        return value;
    }

    public static URI stripScheme(URI uri) throws URISyntaxException {
        return new URI(stripPrefix(uri.getSchemeSpecificPart().trim(), "//"));
    }

    public static String createQueryString(Map<String, String> options) throws URISyntaxException {
        try {
            if (options.size() <= 0) {
                return Stomp.EMPTY;
            }
            StringBuffer rc = new StringBuffer();
            boolean first = true;
            for (String key : options.keySet()) {
                if (first) {
                    first = false;
                } else {
                    rc.append("&");
                }
                String value = (String) options.get(key);
                rc.append(URLEncoder.encode(key, HttpRequest.CHARSET_UTF8));
                rc.append("=");
                rc.append(URLEncoder.encode(value, HttpRequest.CHARSET_UTF8));
            }
            return rc.toString();
        } catch (UnsupportedEncodingException e) {
            throw ((URISyntaxException) new URISyntaxException(e.toString(), "Invalid encoding").initCause(e));
        }
    }

    public static URI createRemainingURI(URI originalURI, Map<String, String> params) throws URISyntaxException {
        String s = createQueryString(params);
        if (s.length() == 0) {
            s = null;
        }
        return createURIWithQuery(originalURI, s);
    }

    public static URI changeScheme(URI bindAddr, String scheme) throws URISyntaxException {
        return new URI(scheme, bindAddr.getUserInfo(), bindAddr.getHost(), bindAddr.getPort(), bindAddr.getPath(), bindAddr.getQuery(), bindAddr.getFragment());
    }

    public static boolean checkParenthesis(String str) {
        if (str == null) {
            return true;
        }
        int open = 0;
        int closed = 0;
        int i = 0;
        while (true) {
            i = str.indexOf(40, i);
            if (i < 0) {
                break;
            }
            i++;
            open++;
        }
        i = 0;
        while (true) {
            i = str.indexOf(41, i);
            if (i < 0) {
                break;
            }
            i++;
            closed++;
        }
        return open == closed;
    }
}
