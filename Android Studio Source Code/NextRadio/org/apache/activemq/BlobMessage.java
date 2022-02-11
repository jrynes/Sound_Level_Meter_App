package org.apache.activemq;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jms.JMSException;

public interface BlobMessage extends Message {
    InputStream getInputStream() throws IOException, JMSException;

    String getMimeType();

    String getName();

    URL getURL() throws MalformedURLException, JMSException;

    void setMimeType(String str);

    void setName(String str);
}
