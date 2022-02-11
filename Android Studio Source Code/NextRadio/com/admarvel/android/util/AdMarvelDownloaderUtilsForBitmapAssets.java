package com.admarvel.android.util;

import android.util.Log;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.util.c */
public class AdMarvelDownloaderUtilsForBitmapAssets {
    private static boolean f978a;

    static {
        f978a = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void m561a(android.content.Context r10) {
        /*
        r0 = 0;
        r1 = "checkForAdMarvelBitmapAssets : checking For AdMarvel Bitmap Assets";
        com.admarvel.android.util.Logging.log(r1);
        if (r10 == 0) goto L_0x0061;
    L_0x0008:
        r1 = "adm_assets";
        r4 = r10.getDir(r1, r0);
        r2 = new java.io.File;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = r4.getAbsoluteFile();
        r1 = r1.append(r3);
        r3 = "/adm_bmp";
        r1 = r1.append(r3);
        r1 = r1.toString();
        r2.<init>(r1);
        r1 = 1;
        r3 = r2.isDirectory();
        if (r3 == 0) goto L_0x0062;
    L_0x0031:
        r3 = r2.list();
        r3 = r3.length;
        r5 = 11;
        if (r3 < r5) goto L_0x0062;
    L_0x003a:
        r3 = java.util.Calendar.getInstance();
        r6 = r3.getTimeInMillis();
        r2 = r2.lastModified();
        r8 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r5 != 0) goto L_0x004f;
    L_0x004d:
        r2 = 0;
    L_0x004f:
        r2 = r6 - r2;
        r6 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 > 0) goto L_0x0062;
    L_0x0058:
        if (r0 == 0) goto L_0x0061;
    L_0x005a:
        r0 = r4.getAbsolutePath();
        com.admarvel.android.util.AdMarvelDownloaderUtilsForBitmapAssets.m562a(r0);
    L_0x0061:
        return;
    L_0x0062:
        r0 = r1;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.admarvel.android.util.c.a(android.content.Context):void");
    }

    private static void m562a(String str) {
        Logging.log("downLoadAdMarvelBitmapAssets : AdMarvel Bitmap assets downloadeding.....");
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://admarvel.s3.amazonaws.com/sdk/assets/adm_bmp.zip").openConnection();
            InputStream inputStream = (InputStream) httpURLConnection.getContent();
            if (HttpRequest.ENCODING_GZIP.equals(httpURLConnection.getContentEncoding())) {
                inputStream = new GZIPInputStream(inputStream);
            }
            OutputStream fileOutputStream = new FileOutputStream(str + "/bmptmp.zip");
            byte[] bArr = new byte[Flags.EXTEND];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            if (AdMarvelDownloaderUtilsForBitmapAssets.m563a(str + "/bmptmp.zip", str)) {
                Logging.log("AdMarvel Bitmap assets downloaded");
                return;
            }
            AdMarvelDownloaderUtilsForBitmapAssets.m564b(str);
            Logging.log("Error in Download admarvel assets");
        } catch (MalformedURLException e) {
            AdMarvelDownloaderUtilsForBitmapAssets.m564b(str);
            Logging.log("Error in Download admarvel assets");
        } catch (IOException e2) {
            AdMarvelDownloaderUtilsForBitmapAssets.m564b(str);
            Logging.log("Error in Download admarvel assets");
        } catch (Exception e3) {
            AdMarvelDownloaderUtilsForBitmapAssets.m564b(str);
            Logging.log("Error in Download admarvel assets");
        }
    }

    private static boolean m563a(String str, String str2) {
        String str3;
        FileOutputStream fileOutputStream;
        try {
            str3 = Stomp.EMPTY;
            File file = new File(str2);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
                String str4 = str2 + ReadOnlyContext.SEPARATOR + nextEntry.getName();
                if (nextEntry.isDirectory()) {
                    File file2 = new File(str4);
                    if (!file2.isDirectory()) {
                        file2.mkdirs();
                    }
                } else {
                    String substring = nextEntry.getName().substring(nextEntry.getName().lastIndexOf(ActiveMQDestination.PATH_SEPERATOR) + 1, nextEntry.getName().length());
                    if (substring.length() > 0 && substring.equalsIgnoreCase("png")) {
                        String str5 = str4 + ".tmp";
                        str3 = str4;
                        str4 = str5;
                    }
                    fileOutputStream = new FileOutputStream(str4, false);
                    fileOutputStream.toString().length();
                    byte[] bArr = new byte[Flags.FLAG2];
                    while (true) {
                        int read = zipInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    zipInputStream.closeEntry();
                    if (str3.length() > 0) {
                        Runtime.getRuntime().exec("mv " + str3 + ".tmp" + " " + str3);
                    }
                    fileOutputStream.close();
                }
            }
            if (new File(str).delete()) {
                Logging.log("unzipAndDelete : zip file deleted ");
                return true;
            }
            throw new Exception("Error in deleting Zip file ");
        } catch (Throwable e) {
            Logging.log(Log.getStackTraceString(e));
            return false;
        } catch (Throwable th) {
            if (new File(str).delete()) {
                Logging.log("unzipAndDelete : zip file deleted ");
            } else {
                Exception exception = new Exception("Error in deleting Zip file ");
            }
        }
    }

    private static void m564b(String str) {
        if (f978a) {
            f978a = false;
            Logging.log("trying to Download admarvel assets one more time");
            AdMarvelDownloaderUtilsForBitmapAssets.m562a(str);
        }
    }
}
