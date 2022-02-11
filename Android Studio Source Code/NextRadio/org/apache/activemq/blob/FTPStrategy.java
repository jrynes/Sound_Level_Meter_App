package org.apache.activemq.blob;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jms.JMSException;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.apache.commons.net.ftp.FTPClient;

public class FTPStrategy {
    protected String ftpPass;
    protected String ftpUser;
    protected BlobTransferPolicy transferPolicy;
    protected URL url;

    public FTPStrategy(BlobTransferPolicy transferPolicy) throws MalformedURLException {
        this.ftpUser = Stomp.EMPTY;
        this.ftpPass = Stomp.EMPTY;
        this.transferPolicy = transferPolicy;
        this.url = new URL(this.transferPolicy.getUploadUrl());
    }

    protected void setUserInformation(String userInfo) {
        if (userInfo != null) {
            String[] userPass = userInfo.split(Headers.SEPERATOR);
            if (userPass.length > 0) {
                this.ftpUser = userPass[0];
            }
            if (userPass.length > 1) {
                this.ftpPass = userPass[1];
                return;
            }
            return;
        }
        this.ftpUser = "anonymous";
        this.ftpPass = "anonymous";
    }

    protected FTPClient createFTP() throws IOException, JMSException {
        String connectUrl = this.url.getHost();
        setUserInformation(this.url.getUserInfo());
        int port = this.url.getPort() < 1 ? 21 : this.url.getPort();
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(connectUrl, port);
            if (ftp.login(this.ftpUser, this.ftpPass)) {
                return ftp;
            }
            ftp.quit();
            ftp.disconnect();
            throw new JMSException("Cant Authentificate to FTP-Server");
        } catch (ConnectException e) {
            throw new JMSException("Problem connecting the FTP-server");
        }
    }
}
