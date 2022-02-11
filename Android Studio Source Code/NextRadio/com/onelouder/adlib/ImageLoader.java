package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Looper;
import android.widget.ImageView;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.WeakHashMap;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

class ImageLoader {
    private static final String TAG = "ImageLoader";
    static WeakHashMap<Object, LoadImageCallback> img_cbs;

    static class AbsLoadImageCallback extends LoadImageCallback {
        protected Activity activity;
        public volatile boolean cancelled;
        private String url;
        protected Object view;

        /* renamed from: com.onelouder.adlib.ImageLoader.AbsLoadImageCallback.1 */
        class C12961 implements Runnable {
            final /* synthetic */ Bitmap val$bitmap;

            C12961(Bitmap bitmap) {
                this.val$bitmap = bitmap;
            }

            public void run() {
                if (!AbsLoadImageCallback.this.cancelled) {
                    synchronized (AbsLoadImageCallback.this.view) {
                        if (AbsLoadImageCallback.this.equals(ImageLoader.img_cbs.get(AbsLoadImageCallback.this.view))) {
                            ImageLoader.img_cbs.remove(AbsLoadImageCallback.this.view);
                            AbsLoadImageCallback.this.onReady(this.val$bitmap);
                        }
                    }
                    AbsLoadImageCallback.this.view = null;
                }
            }
        }

        public AbsLoadImageCallback(String u, Activity a, Object v) {
            this.url = u;
            this.activity = a;
            this.view = v;
        }

        public Runnable getImageRunnable(String path) {
            return null;
        }

        protected void onExisting(Bitmap bitmap) {
            if (this.view instanceof ImageView) {
                ((ImageView) this.view).setImageBitmap(bitmap);
                ((ImageView) this.view).setVisibility(0);
            }
        }

        protected void onReady(Bitmap bitmap) {
            if (this.view instanceof ImageView) {
                ((ImageView) this.view).setImageBitmap(bitmap);
                ((ImageView) this.view).setVisibility(0);
            }
        }

        protected void onError(int errorcode) {
        }

        public String getUrl() {
            return this.url;
        }

        public void existing(Bitmap bitmap) {
            if (!this.cancelled) {
                synchronized (this.view) {
                    if (equals(ImageLoader.img_cbs.get(this.view))) {
                        ImageLoader.img_cbs.remove(this.view);
                        onExisting(bitmap);
                    }
                }
                this.view = null;
            }
        }

        public void ready(Bitmap bitmap) {
            if (!this.cancelled) {
                if (Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
                    if (equals(ImageLoader.img_cbs.get(this.view))) {
                        ImageLoader.img_cbs.remove(this.view);
                        onReady(bitmap);
                    }
                    this.activity = null;
                    this.view = null;
                    return;
                }
                if (this.activity != null && !this.activity.isFinishing() && bitmap != null) {
                    this.activity.runOnUiThread(new C12961(bitmap));
                } else if (this.view != null) {
                    synchronized (this.view) {
                        if (equals(ImageLoader.img_cbs.get(this.view))) {
                            ImageLoader.img_cbs.remove(this.view);
                        }
                    }
                    this.view = null;
                }
                this.activity = null;
            }
        }

        public void error(int errorcode) {
            if (!this.cancelled) {
                synchronized (this.view) {
                    if (equals(ImageLoader.img_cbs.get(this.view))) {
                        ImageLoader.img_cbs.remove(this.view);
                    }
                    onError(errorcode);
                }
                this.view = null;
                this.activity = null;
            }
        }

        public final synchronized void cancel() {
            this.cancelled = true;
            this.view = null;
            this.activity = null;
        }
    }

    static class HttpRequest extends ServerBase {
        private static final String TAG = "HttpRequest";

        public HttpRequest(String url) {
            super(url, null);
        }

        protected String TAG() {
            return TAG;
        }
    }

    static class ImageRunnable implements Runnable {
        LoadImageCallback callback;
        Context context;
        String path;

        public ImageRunnable(Context context, LoadImageCallback c, String path) {
            this.callback = c;
            this.path = path;
            this.context = context;
        }

        public ImageRunnable(LoadImageCallback c) {
            this.callback = c;
        }

        public void run() {
            try {
                if (this.callback.isCanceled()) {
                    this.callback = null;
                    return;
                }
                String key;
                Rect rc = this.callback.getScaledRect();
                if (rc != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.callback.getUrl());
                    sb.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
                    sb.append(rc.width());
                    sb.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
                    sb.append(rc.height());
                    key = sb.toString();
                } else {
                    key = this.callback.getUrl();
                }
                byte[] bytes = ImageCache.getInstance().getImage(key);
                if (bytes == null) {
                    HttpRequest ps = new HttpRequest(this.callback.getUrl());
                    int totalbytes = ps.doRequest(null, false, null);
                    if (ps.data != null) {
                        Diagnostics.m1951d(ImageLoader.TAG, "ImageRunnable.run(), bytes returned=" + totalbytes);
                        bytes = ps.data;
                    } else {
                        Diagnostics.m1957w(ImageLoader.TAG, "Invalid image returned. (null byte array)");
                        this.callback.error(ps.getResponseCode());
                        this.callback = null;
                        return;
                    }
                }
                if (this.callback.isCanceled()) {
                    this.callback = null;
                    return;
                }
                Bitmap bitmap = Utils.decodePurgeableByteArray(bytes);
                if (bitmap != null) {
                    ImageCache.getInstance().addImage(key, bytes, true);
                    this.callback.ready(bitmap);
                    if (this.path != null) {
                        this.path = this.path.replace("png", Stomp.EMPTY).replace("jpg", Stomp.EMPTY).replace("jpeg", Stomp.EMPTY);
                        try {
                            DataOutputStream writer = new DataOutputStream(new FileOutputStream(this.context.getFileStreamPath(this.path)));
                            writer.write(bytes);
                            writer.flush();
                            writer.close();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
                this.callback = null;
            } catch (Throwable e2) {
                Diagnostics.m1958w(ImageLoader.TAG, e2);
                this.callback.error(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH);
            } catch (Throwable th) {
                this.callback = null;
            }
        }
    }

    ImageLoader() {
    }

    static {
        img_cbs = new WeakHashMap();
    }

    public static void removeView(Object v) {
        synchronized (v) {
            LoadImageCallback ocb = (LoadImageCallback) img_cbs.get(v);
            if (ocb != null) {
                ocb.cancel();
                img_cbs.remove(v);
            }
        }
    }

    public static void displayCachedImage(Context context, LoadImageCallback xcb, Object v) {
        try {
            synchronized (v) {
                LoadImageCallback ocb = (LoadImageCallback) img_cbs.get(v);
                if (ocb != null) {
                    ocb.cancel();
                }
                img_cbs.put(v, xcb);
                loadCachedImage(context, xcb);
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG, e);
        }
    }

    public static void loadCachedImage(Context context, LoadImageCallback c) {
        String url = c.getUrl();
        if (url == null || url.length() <= 0) {
            c.error(ActiveMQPrefetchPolicy.DEFAULT_QUEUE_BROWSER_PREFETCH);
            return;
        }
        byte[] bytes = ImageCache.getInstance().getImage(url);
        Bitmap bitmap;
        if (bytes != null) {
            bitmap = Utils.decodePurgeableByteArray(bytes);
            if (bitmap != null) {
                c.ready(bitmap);
                return;
            }
            return;
        }
        String path = getKey(url);
        File sdFile = context.getFileStreamPath(path);
        if (sdFile.exists()) {
            bytes = new byte[((int) sdFile.length())];
            try {
                DataInputStream reader = new DataInputStream(new FileInputStream(sdFile));
                if (reader != null) {
                    reader.read(bytes);
                    reader.close();
                }
            } catch (Throwable e) {
                Diagnostics.m1958w(TAG, e);
            }
            bitmap = Utils.decodePurgeableByteArray(bytes);
            if (bitmap != null) {
                ImageCache.getInstance().addImage(url, bytes, true);
                c.ready(bitmap);
                return;
            }
            return;
        }
        Runnable runnable = c.getImageRunnable(path);
        if (runnable == null) {
            runnable = new ImageRunnable(context, c, path);
        }
        if (c.isHighPriority()) {
            RunnableManager.getInstance().pushRequestAtFront(runnable);
        } else {
            RunnableManager.getInstance().pushRequest(runnable);
        }
    }

    private static String getKey(String url) {
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
