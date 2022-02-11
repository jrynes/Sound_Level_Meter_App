package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import com.rabbitmq.client.AMQP;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

class zzcx implements zzac {
    private final Context mContext;
    private final zzb zzblh;
    private final zza zzbli;
    private final String zzzN;

    public interface zza {
        void zza(zzaq com_google_android_gms_tagmanager_zzaq);

        void zzb(zzaq com_google_android_gms_tagmanager_zzaq);

        void zzc(zzaq com_google_android_gms_tagmanager_zzaq);
    }

    interface zzb {
        HttpURLConnection zzd(URL url) throws IOException;
    }

    /* renamed from: com.google.android.gms.tagmanager.zzcx.1 */
    class C10501 implements zzb {
        C10501() {
        }

        public HttpURLConnection zzd(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }

    zzcx(Context context, zza com_google_android_gms_tagmanager_zzcx_zza) {
        this(new C10501(), context, com_google_android_gms_tagmanager_zzcx_zza);
    }

    zzcx(zzb com_google_android_gms_tagmanager_zzcx_zzb, Context context, zza com_google_android_gms_tagmanager_zzcx_zza) {
        this.zzblh = com_google_android_gms_tagmanager_zzcx_zzb;
        this.mContext = context.getApplicationContext();
        this.zzbli = com_google_android_gms_tagmanager_zzcx_zza;
        this.zzzN = zza("GoogleTagManager", "4.00", VERSION.RELEASE, zzc(Locale.getDefault()), Build.MODEL, Build.ID);
    }

    static String zzc(Locale locale) {
        if (locale == null || locale.getLanguage() == null || locale.getLanguage().length() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(locale.getLanguage().toLowerCase());
        if (!(locale.getCountry() == null || locale.getCountry().length() == 0)) {
            stringBuilder.append("-").append(locale.getCountry().toLowerCase());
        }
        return stringBuilder.toString();
    }

    public void zzE(List<zzaq> list) {
        Object obj;
        Object obj2;
        IOException iOException;
        int min = Math.min(list.size(), 40);
        Object obj3 = 1;
        int i = 0;
        while (i < min) {
            zzaq com_google_android_gms_tagmanager_zzaq = (zzaq) list.get(i);
            URL zzd = zzd(com_google_android_gms_tagmanager_zzaq);
            if (zzd == null) {
                zzbg.zzaK("No destination: discarding hit.");
                this.zzbli.zzb(com_google_android_gms_tagmanager_zzaq);
                obj = obj3;
            } else {
                try {
                    HttpURLConnection zzd2 = this.zzblh.zzd(zzd);
                    if (obj3 != null) {
                        try {
                            zzbl.zzbb(this.mContext);
                            obj3 = null;
                        } catch (IOException e) {
                            IOException iOException2 = e;
                            obj3 = obj2;
                            iOException = iOException2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            obj2 = obj3;
                            Throwable th3 = th2;
                            zzd2.disconnect();
                        }
                    }
                    zzd2.setRequestProperty(HttpRequest.HEADER_USER_AGENT, this.zzzN);
                    int responseCode = zzd2.getResponseCode();
                    if (responseCode != AMQP.REPLY_SUCCESS) {
                        zzbg.zzaK("Bad response: " + responseCode);
                        this.zzbli.zzc(com_google_android_gms_tagmanager_zzaq);
                    } else {
                        this.zzbli.zza(com_google_android_gms_tagmanager_zzaq);
                    }
                    zzd2.disconnect();
                    obj = obj3;
                } catch (IOException e2) {
                    iOException = e2;
                    zzbg.zzaK("Exception sending hit: " + iOException.getClass().getSimpleName());
                    zzbg.zzaK(iOException.getMessage());
                    this.zzbli.zzc(com_google_android_gms_tagmanager_zzaq);
                    obj = obj3;
                    i++;
                    obj3 = obj;
                }
            }
            i++;
            obj3 = obj;
        }
    }

    public boolean zzGw() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        zzbg.m1676v("...no network connectivity");
        return false;
    }

    String zza(String str, String str2, String str3, String str4, String str5, String str6) {
        return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", new Object[]{str, str2, str3, str4, str5, str6});
    }

    URL zzd(zzaq com_google_android_gms_tagmanager_zzaq) {
        try {
            return new URL(com_google_android_gms_tagmanager_zzaq.zzGF());
        } catch (MalformedURLException e) {
            zzbg.m1675e("Error trying to parse the GTM url.");
            return null;
        }
    }
}
