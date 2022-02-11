package com.admarvel.android.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.util.i */
public class Compress {
    private String[] f1036a;
    private String f1037b;

    public Compress(String[] strArr, String str) {
        this.f1036a = strArr;
        this.f1037b = str;
    }

    public void m603a() {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(this.f1037b)));
            byte[] bArr = new byte[Flags.FLAG4];
            for (int i = 0; i < this.f1036a.length; i++) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.f1036a[i]), Flags.FLAG4);
                zipOutputStream.putNextEntry(new ZipEntry(this.f1036a[i].substring(this.f1036a[i].lastIndexOf(ReadOnlyContext.SEPARATOR) + 1)));
                while (true) {
                    int read = bufferedInputStream.read(bArr, 0, Flags.FLAG4);
                    if (read == -1) {
                        break;
                    }
                    zipOutputStream.write(bArr, 0, read);
                }
                bufferedInputStream.close();
            }
            zipOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
