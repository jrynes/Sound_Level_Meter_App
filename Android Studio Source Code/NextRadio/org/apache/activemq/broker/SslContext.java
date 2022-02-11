package org.apache.activemq.broker;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class SslContext {
    private static final ThreadLocal<SslContext> current;
    protected List<KeyManager> keyManagers;
    protected String protocol;
    protected String provider;
    protected SecureRandom secureRandom;
    private SSLContext sslContext;
    protected List<TrustManager> trustManagers;

    static {
        current = new ThreadLocal();
    }

    public SslContext() {
        this.protocol = "TLS";
        this.provider = null;
        this.keyManagers = new ArrayList();
        this.trustManagers = new ArrayList();
    }

    public SslContext(KeyManager[] km, TrustManager[] tm, SecureRandom random) {
        this.protocol = "TLS";
        this.provider = null;
        this.keyManagers = new ArrayList();
        this.trustManagers = new ArrayList();
        if (km != null) {
            setKeyManagers(Arrays.asList(km));
        }
        if (tm != null) {
            setTrustManagers(Arrays.asList(tm));
        }
        setSecureRandom(random);
    }

    public static void setCurrentSslContext(SslContext bs) {
        current.set(bs);
    }

    public static SslContext getCurrentSslContext() {
        return (SslContext) current.get();
    }

    public KeyManager[] getKeyManagersAsArray() {
        return (KeyManager[]) this.keyManagers.toArray(new KeyManager[this.keyManagers.size()]);
    }

    public TrustManager[] getTrustManagersAsArray() {
        return (TrustManager[]) this.trustManagers.toArray(new TrustManager[this.trustManagers.size()]);
    }

    public void addKeyManager(KeyManager km) {
        this.keyManagers.add(km);
    }

    public boolean removeKeyManager(KeyManager km) {
        return this.keyManagers.remove(km);
    }

    public void addTrustManager(TrustManager tm) {
        this.trustManagers.add(tm);
    }

    public boolean removeTrustManager(TrustManager tm) {
        return this.trustManagers.remove(tm);
    }

    public List<KeyManager> getKeyManagers() {
        return this.keyManagers;
    }

    public void setKeyManagers(List<KeyManager> keyManagers) {
        this.keyManagers = keyManagers;
    }

    public List<TrustManager> getTrustManagers() {
        return this.trustManagers;
    }

    public void setTrustManagers(List<TrustManager> trustManagers) {
        this.trustManagers = trustManagers;
    }

    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public SSLContext getSSLContext() throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException {
        if (this.sslContext == null) {
            if (this.provider == null) {
                this.sslContext = SSLContext.getInstance(this.protocol);
            } else {
                this.sslContext = SSLContext.getInstance(this.protocol, this.provider);
            }
            this.sslContext.init(getKeyManagersAsArray(), getTrustManagersAsArray(), getSecureRandom());
        }
        return this.sslContext;
    }

    public void setSSLContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}
