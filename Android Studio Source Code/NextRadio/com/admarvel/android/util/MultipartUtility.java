package com.admarvel.android.util;

import com.facebook.ads.AdError;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.util.m */
public class MultipartUtility {
    private final String f1051a;
    private HttpURLConnection f1052b;
    private String f1053c;
    private OutputStream f1054d;
    private PrintWriter f1055e;

    public MultipartUtility(String str, String str2) {
        this.f1053c = str2;
        this.f1051a = "===" + System.currentTimeMillis() + "===";
        this.f1052b = (HttpURLConnection) new URL(str).openConnection();
        this.f1052b.setUseCaches(false);
        this.f1052b.setDoOutput(true);
        this.f1052b.setDoInput(true);
        this.f1052b.setConnectTimeout(AdError.SERVER_ERROR_CODE);
        this.f1052b.setReadTimeout(AdError.SERVER_ERROR_CODE);
        this.f1052b.setRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, "multipart/form-data; boundary=" + this.f1051a);
        this.f1054d = this.f1052b.getOutputStream();
        this.f1055e = new PrintWriter(new OutputStreamWriter(this.f1054d, str2), true);
    }

    public List<String> m624a() {
        List<String> arrayList = new ArrayList();
        this.f1055e.append("\r\n").flush();
        this.f1055e.append("--" + this.f1051a + "--").append("\r\n");
        this.f1055e.close();
        int responseCode = this.f1052b.getResponseCode();
        if (responseCode == AMQP.REPLY_SUCCESS) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.f1052b.getInputStream()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    arrayList.add(readLine);
                } else {
                    bufferedReader.close();
                    this.f1052b.disconnect();
                    return arrayList;
                }
            }
        }
        throw new IOException("Server returned non-OK status: " + responseCode);
    }

    public void m625a(String str, File file) {
        String name = file.getName();
        this.f1055e.append("--" + this.f1051a).append("\r\n");
        this.f1055e.append("Content-Disposition: form-data; name=\"" + str + "\"; filename=\"" + name + "\"").append("\r\n");
        this.f1055e.append("Content-Type: " + URLConnection.guessContentTypeFromName(name)).append("\r\n");
        this.f1055e.append("Content-Transfer-Encoding: binary").append("\r\n");
        this.f1055e.append("\r\n");
        this.f1055e.flush();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[Flags.EXTEND];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read != -1) {
                this.f1054d.write(bArr, 0, read);
            } else {
                this.f1054d.flush();
                fileInputStream.close();
                this.f1055e.append("\r\n");
                this.f1055e.flush();
                return;
            }
        }
    }
}
