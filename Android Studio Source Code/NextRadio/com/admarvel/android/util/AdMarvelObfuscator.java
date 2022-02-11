package com.admarvel.android.util;

import android.util.Log;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.filter.DestinationFilter;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;

/* renamed from: com.admarvel.android.util.e */
public class AdMarvelObfuscator {
    private String f988a;
    private Map<String, String[]> f989b;
    private int f990c;
    private String[] f991d;
    private String[] f992e;

    public AdMarvelObfuscator() {
        this.f988a = "fwug66MEqP6JSxtsHXJxH8xKm7u1xfctDoP626eTR0v3guDbSJUETigA3GFBW0EW~DtP6k4y~YFd~Fk91d0VjjUqyQXxJjNZUQ-1LP_bBiklBJ3tXcTIwrfSWkZkdjl5qrHOJv79OC32YUZitEvxQ2ZX7XQt41J0n7HelUErtKXwbbKAkf8pc62WgU-YS6fTqLWSGjoNyDaKFK49X2N7mJ2DVha3rf~I~0KaCR1NR1icrg~C9tXjj6UvrjnScGK9X99ErjuESJHoqJrpjAKe-hkMamvSufxSMKXIycTmY0qjhIKa-9MkwOKRrf205PFD2P4skcIzF07-Ev-nWYmOGrNj8SCwz0wp8bo1fjMmC5WHces7q4gbt2aXACOPYfYn4zjdpBBXU0wwtVI8yYgcQgEVeZ7qjppfJGrPDfcU5U9JZDxVaE2D1KRAS6ZVqmaRPrEr9H6VlaQQDBRDYgDl0whbyRSDsSR~oINlZpbQpF3pTvpMP7c8I1giYMibtdMHxV9EHtG2V-d8JwEz4HmooDs8MPgz3kaBW9aRidvd9NSTDXOlwrUXi2YlXtj__jctLcxDHpdYBXeuwYwxP9fJkNODDb_6aGhL6ULZKVv3KLi1oGRFcDNPNWrfBVpBl8bzp96vPFn28yJ1kQIP3p00Q8yPrn1gII6qD9orv4XHipzY72uGG9WOIxjtGSu~GAWKBajdI4qvMdty-do8JaAdxv1YUvxyTNkINET7xilWjYxb7-gr-x2Qp0DKjxkutJRDiz71sx83eOYyktTtpBvCnvIopUh4VJ4d~TPTHhBNpx6SpSjC99Q_XlQ2JF1QNpKvy2pusFZkqYu-0XmMFNfrEJXWNzXOJM0l~uwTxP-WBUeZ4CL_bBVknKDjVXhD1llttskL~vD48Ta3nLItby6tL69XgtHQs9vRZIHdF_~Z0~8MhQoOb3084MsYziD9bgPRsAlhKRVHkPNnieCB~s-AjGC7UA0DT8wMusQ2CR4iE7y2h1rU4qgfbePD98Qd4vhw";
        this.f990c = 1295;
        this.f991d = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR, "~"};
        this.f992e = new String[]{ReadOnlyContext.SEPARATOR, "?", Headers.SEPERATOR, "@", "=", "&", " ", "<", DestinationFilter.ANY_DESCENDENT, "\"", "#", "%", "{", "}", "|", "^", "[", "]", "`", ActiveMQDestination.PATH_SEPERATOR, "\\", "+"};
    }

    private void m575a() {
        this.f989b = new HashMap();
        for (Object put : this.f991d) {
            this.f989b.put(put, new String[]{Stomp.EMPTY, String.valueOf(r0)});
        }
        for (Object put2 : this.f992e) {
            this.f989b.put(put2, new String[]{ActiveMQDestination.PATH_SEPERATOR, String.valueOf(r0)});
        }
    }

    public String m576a(String str) {
        if (str == null) {
            return null;
        }
        try {
            m575a();
            int random = (int) (Math.random() * ((double) Math.min(this.f988a.length(), this.f990c)));
            String str2 = (random <= 0 || random >= this.f988a.length()) ? this.f988a : this.f988a.substring(random) + this.f988a.substring(0, random);
            StringBuilder stringBuilder = new StringBuilder();
            String num = Integer.toString(random, 36);
            if (num.length() == 1) {
                stringBuilder.append("0" + num);
            } else {
                stringBuilder.append(num);
            }
            for (random = 0; random < str.length(); random++) {
                num = str.substring(random, random + 1);
                String[] strArr = (String[]) this.f989b.get(num);
                if (strArr == null) {
                    stringBuilder.append(num);
                } else {
                    num = ((String[]) this.f989b.get(str2.substring(random % str2.length(), (random % str2.length()) + 1)))[1];
                    stringBuilder.append(strArr[0]);
                    stringBuilder.append(this.f991d[(Integer.parseInt(strArr[1]) + Integer.parseInt(num)) % this.f991d.length]);
                }
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return null;
        }
    }

    public String m577b(String str) {
        m575a();
        StringBuilder stringBuilder = new StringBuilder();
        int parseInt = Integer.parseInt(str.substring(0, 2), 36);
        String str2 = (parseInt <= 0 || parseInt >= this.f988a.length()) ? this.f988a : this.f988a.substring(parseInt) + this.f988a.substring(0, parseInt);
        String substring = str.substring(2);
        int i = 0;
        int i2;
        for (parseInt = 0; parseInt < substring.length(); parseInt = i2 + 1) {
            int i3;
            String substring2;
            String substring3 = substring.substring(parseInt, parseInt + 1);
            if (substring3.equals(ActiveMQDestination.PATH_SEPERATOR)) {
                i3 = i + 1;
                i = parseInt + 1;
                substring2 = substring.substring(i, i + 1);
                i2 = i;
                i = i3;
                i3 = 1;
            } else {
                i2 = parseInt;
                substring2 = substring3;
                i3 = 0;
            }
            if (this.f989b.get(substring2) == null) {
                stringBuilder.append(substring2);
            } else {
                parseInt = ((Integer.parseInt(((String[]) this.f989b.get(substring2))[1]) - Integer.parseInt(((String[]) this.f989b.get(str2.substring((i2 - i) % str2.length(), ((i2 - i) % str2.length()) + 1)))[1])) + this.f991d.length) % this.f991d.length;
                if (i3 != 0) {
                    stringBuilder.append(this.f992e[Math.abs(parseInt)]);
                } else {
                    stringBuilder.append(this.f991d[Math.abs(parseInt)]);
                }
            }
        }
        return stringBuilder.toString();
    }
}
