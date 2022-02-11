package com.onelouder.adlib;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

abstract class DataCache {
    private static final Object csLock;
    protected long _lastUpdated;

    /* renamed from: com.onelouder.adlib.DataCache.1 */
    class C12951 implements Runnable {
        final /* synthetic */ byte[] val$bytes;
        final /* synthetic */ Context val$context;

        C12951(Context context, byte[] bArr) {
            this.val$context = context;
            this.val$bytes = bArr;
        }

        public void run() {
            synchronized (DataCache.csLock) {
                try {
                    long start = System.currentTimeMillis();
                    DataOutputStream writer = new DataOutputStream(this.val$context.openFileOutput(DataCache.this.getFilename(), 0));
                    writer.write(this.val$bytes);
                    writer.flush();
                    writer.close();
                    if (Diagnostics.getInstance().isEnabled(4)) {
                        Diagnostics.m1952e(DataCache.this.TAG(), "write finished in " + (System.currentTimeMillis() - start) + "ms");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract String TAG();

    protected abstract String getFilename();

    public abstract DefaultHandler getParser();

    public abstract void onContentParsed(Object obj);

    DataCache() {
        this._lastUpdated = 0;
    }

    static {
        csLock = new Object();
    }

    protected void onLoadException() {
    }

    protected void onSaveInstance() {
    }

    protected void onInstanceLoaded() {
    }

    protected void onParseLoadedBytes(byte[] bytes) throws ParserConfigurationException, SAXException, IOException {
        XMLReader xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xr.setContentHandler(getParser());
        xr.parse(new InputSource(decompressStream(new ByteArrayInputStream(bytes))));
    }

    protected InputStream decompressStream(InputStream input) throws IOException {
        PushbackInputStream pb = new PushbackInputStream(input, 2);
        byte[] signature = new byte[2];
        pb.read(signature);
        pb.unread(signature);
        if (signature[0] == 31 && signature[1] == -117) {
            return new GZIPInputStream(pb);
        }
        return pb;
    }

    public void LoadFromCache(Context context) {
        synchronized (csLock) {
            long start = System.currentTimeMillis();
            File file = context.getFileStreamPath(getFilename());
            if (file.exists()) {
                try {
                    int byte_count = (int) file.length();
                    byte[] bytes = new byte[byte_count];
                    Diagnostics.m1951d(TAG(), "byte_count=" + byte_count);
                    DataInputStream reader = new DataInputStream(new FileInputStream(file));
                    if (reader != null) {
                        reader.read(bytes);
                        reader.close();
                    }
                    onParseLoadedBytes(bytes);
                    onInstanceLoaded();
                } catch (VirtualMachineError e) {
                    Diagnostics.m1954e(TAG(), e);
                    onLoadException();
                } catch (Throwable e2) {
                    Diagnostics.m1953e(TAG(), e2);
                    onLoadException();
                }
            }
            if (Diagnostics.getInstance().isEnabled(4)) {
                Diagnostics.m1952e(TAG(), "finished in " + (System.currentTimeMillis() - start) + "ms");
            }
        }
    }

    public void save(Context context, byte[] bytes) {
        this._lastUpdated = System.currentTimeMillis();
        onSaveInstance();
        RunnableManager.getInstance().pushRequest(new C12951(context, bytes));
    }
}
