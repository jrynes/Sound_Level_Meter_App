package com.onelouder.adlib;

import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.HashMap;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

public class ImageCache {
    private static ImageCache _SingleInstance;
    private HashMap<String, ImageItem> imageMap;

    class ImageItem {
        public byte[] ImageBytes;
        public long accessed;
        public String key;
        public boolean persist;

        public ImageItem(byte[] ImageBytes, boolean bPersist) {
            this.ImageBytes = null;
            this.persist = false;
            this.key = Stomp.EMPTY;
            this.accessed = 0;
            this.ImageBytes = ImageBytes;
            this.persist = bPersist;
        }
    }

    public ImageCache() {
        this.imageMap = new HashMap();
    }

    static {
        _SingleInstance = null;
    }

    public static ImageCache getInstance() {
        if (_SingleInstance == null) {
            _SingleInstance = new ImageCache();
        }
        return _SingleInstance;
    }

    public void addImage(String url, byte[] bytes, boolean persist) {
        addImage(url, bytes, persist, false);
    }

    public void addImage(String url, byte[] bytes, boolean persist, boolean replace) {
        synchronized (this.imageMap) {
            String key = getKey(url);
            if (this.imageMap.containsKey(key)) {
                if (((ImageItem) this.imageMap.get(key)).ImageBytes == null || replace) {
                    this.imageMap.remove(key);
                } else {
                    return;
                }
            }
            ImageItem item = new ImageItem(bytes, persist);
            this.imageMap.put(key, item);
            item.key = key;
            item.accessed = System.currentTimeMillis();
        }
    }

    public byte[] getImage(String url) {
        byte[] bytes = null;
        synchronized (this.imageMap) {
            String key = getKey(url);
            if (this.imageMap.containsKey(key)) {
                ImageItem item = (ImageItem) this.imageMap.get(key);
                item.accessed = System.currentTimeMillis();
                bytes = item.ImageBytes;
            }
        }
        return bytes;
    }

    private String getKey(String url) {
        int maxLen = url.length();
        int size = 16;
        if (16 > maxLen) {
            size = maxLen;
        }
        int lastSlash = url.lastIndexOf(ReadOnlyContext.SEPARATOR) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(url.hashCode()).append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
        sb.append(maxLen).append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
        sb.append(url.substring(Math.max(lastSlash, maxLen - size)).replace("?", "Q"));
        return sb.toString().replace(".png", Stomp.EMPTY).replace(".jpg", Stomp.EMPTY).replace(".jpeg", Stomp.EMPTY);
    }
}
