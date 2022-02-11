package com.amazon.device.associates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: FileUtils */
class ab {
    ab() {
    }

    private static byte[] m698a(Object obj) {
        try {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.m1014a("FileUtils", "Converting object to byte array failed. Ex=", e);
            return null;
        }
    }

    private static Object m696a(byte[] bArr) {
        Object obj = null;
        if (bArr != null) {
            try {
                obj = new ObjectInputStream(new ByteArrayInputStream(bArr)).readObject();
            } catch (Exception e) {
                Log.m1014a("FileUtils", "Converting byte array to object failed. Ex=", e);
            }
        }
        return obj;
    }

    public static boolean m697a(Object obj, String str) {
        boolean z = true;
        if (obj == null || str == null || Stomp.EMPTY.equals(str)) {
            return false;
        }
        try {
            Object a = m698a(obj);
            if (a == null || Stomp.EMPTY.equals(a)) {
                return false;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            fileOutputStream.write(a);
            fileOutputStream.close();
            return z;
        } catch (Exception e) {
            Log.m1014a("FileUtils", "Saving file failed. Ex=", e);
            z = false;
        }
    }

    private static byte[] m700c(String str) throws IOException {
        if (!m701d(str)) {
            return null;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(str), "r");
        try {
            long length = randomAccessFile.length();
            int i = (int) length;
            if (((long) i) != length) {
                throw new IOException("File size >= 2 GB");
            }
            byte[] bArr = new byte[i];
            randomAccessFile.readFully(bArr);
            return bArr;
        } finally {
            randomAccessFile.close();
        }
    }

    public static Object m695a(String str) {
        Object obj = null;
        if (m701d(str)) {
            try {
                obj = m696a(m700c(str));
            } catch (Exception e) {
                Log.m1014a("FileUtils", "Loading Object failed. Ex=", e);
            }
        }
        return obj;
    }

    public static void m699b(String str) {
        try {
            if (m701d(str)) {
                new File(str).delete();
            }
        } catch (Exception e) {
        }
    }

    private static boolean m701d(String str) {
        if (str == null || Stomp.EMPTY.equals(str)) {
            return false;
        }
        return new File(str).exists();
    }
}
