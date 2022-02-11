package org.apache.activemq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.jms.JMSException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.activemq.broker.SslContext;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.util.JMSExceptionSupport;
import org.xbill.DNS.KEYRecord;

public class ActiveMQSslConnectionFactory extends ActiveMQConnectionFactory {
    protected KeyManager[] keyManager;
    protected String keyStore;
    protected String keyStorePassword;
    protected SecureRandom secureRandom;
    protected TrustManager[] trustManager;
    protected String trustStore;
    protected String trustStorePassword;

    public ActiveMQSslConnectionFactory(String brokerURL) {
        super(brokerURL);
    }

    public ActiveMQSslConnectionFactory(URI brokerURL) {
        super(brokerURL);
    }

    public void setKeyAndTrustManagers(KeyManager[] km, TrustManager[] tm, SecureRandom random) {
        this.keyManager = km;
        this.trustManager = tm;
        this.secureRandom = random;
    }

    protected Transport createTransport() throws JMSException {
        SslContext existing = SslContext.getCurrentSslContext();
        try {
            if (!(this.keyStore == null && this.trustStore == null)) {
                this.keyManager = createKeyManager();
                this.trustManager = createTrustManager();
            }
            if (!(this.keyManager == null && this.trustManager == null)) {
                SslContext.setCurrentSslContext(new SslContext(this.keyManager, this.trustManager, this.secureRandom));
            }
            Transport createTransport = super.createTransport();
            SslContext.setCurrentSslContext(existing);
            return createTransport;
        } catch (Exception e) {
            throw JMSExceptionSupport.create("Could not create Transport. Reason: " + e, e);
        } catch (Throwable th) {
            SslContext.setCurrentSslContext(existing);
        }
    }

    protected TrustManager[] createTrustManager() throws Exception {
        KeyStore trustedCertStore = KeyStore.getInstance("jks");
        if (this.trustStore == null) {
            return null;
        }
        trustedCertStore.load(getInputStream(this.trustStore), this.trustStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustedCertStore);
        return tmf.getTrustManagers();
    }

    protected KeyManager[] createKeyManager() throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance("jks");
        if (this.keyStore == null) {
            return null;
        }
        byte[] sslCert = loadClientCredential(this.keyStore);
        if (sslCert == null || sslCert.length <= 0) {
            return null;
        }
        ks.load(new ByteArrayInputStream(sslCert), this.keyStorePassword.toCharArray());
        kmf.init(ks, this.keyStorePassword.toCharArray());
        return kmf.getKeyManagers();
    }

    protected byte[] loadClientCredential(String fileName) throws IOException {
        if (fileName == null) {
            return null;
        }
        InputStream in = getInputStream(fileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[KEYRecord.OWNER_HOST];
        for (int i = in.read(buf); i > 0; i = in.read(buf)) {
            out.write(buf, 0, i);
        }
        in.close();
        return out.toByteArray();
    }

    protected InputStream getInputStream(String urlOrResource) throws IOException {
        try {
            File ifile = new File(urlOrResource);
            if (ifile.exists()) {
                return new FileInputStream(ifile);
            }
        } catch (Exception e) {
        }
        InputStream ins = null;
        try {
            ins = new URL(urlOrResource).openStream();
            if (ins != null) {
                return ins;
            }
        } catch (MalformedURLException e2) {
        }
        if (ins == null) {
            ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(urlOrResource);
        }
        if (ins != null) {
            return ins;
        }
        throw new IOException("Could not load resource: " + urlOrResource);
    }

    public String getTrustStore() {
        return this.trustStore;
    }

    public void setTrustStore(String trustStore) throws Exception {
        this.trustStore = trustStore;
        this.trustManager = null;
    }

    public String getTrustStorePassword() {
        return this.trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStore() {
        return this.keyStore;
    }

    public void setKeyStore(String keyStore) throws Exception {
        this.keyStore = keyStore;
        this.keyManager = null;
    }

    public String getKeyStorePassword() {
        return this.keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }
}
