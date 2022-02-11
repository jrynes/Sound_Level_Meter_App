package com.mixpanel.android.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.activemq.transport.stomp.Stomp;

public class StringUtils {
    public static String inputStreamToString(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line != null) {
                sb.append(line + Stomp.NEWLINE);
            } else {
                br.close();
                return sb.toString();
            }
        }
    }
}
