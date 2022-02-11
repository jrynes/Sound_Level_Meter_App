package com.admarvel.android.ads.nativeads;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.admarvel.android.ads.Version;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.admarvel.android.ads.nativeads.e */
public class AsyncTaskExecutor {
    private static final int f719a;
    private static final int f720b;
    private static final int f721c;
    private static final TimeUnit f722d;
    private static final BlockingQueue<Runnable> f723e;
    private static final ThreadFactory f724f;
    private static final ThreadPoolExecutor f725g;

    /* renamed from: com.admarvel.android.ads.nativeads.e.1 */
    static class AsyncTaskExecutor implements Runnable {
        final /* synthetic */ AsyncTask f716a;
        final /* synthetic */ Object[] f717b;

        AsyncTaskExecutor(AsyncTask asyncTask, Object[] objArr) {
            this.f716a = asyncTask;
            this.f717b = objArr;
        }

        public void run() {
            this.f716a.executeOnExecutor(AsyncTaskExecutor.f725g, this.f717b);
        }
    }

    /* renamed from: com.admarvel.android.ads.nativeads.e.a */
    private static class AsyncTaskExecutor implements ThreadFactory {
        private final AtomicInteger f718a;

        private AsyncTaskExecutor() {
            this.f718a = new AtomicInteger(1);
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + this.f718a.getAndIncrement());
        }
    }

    static {
        f719a = 10;
        f720b = Flags.FLAG8;
        f721c = 1;
        f722d = TimeUnit.SECONDS;
        f723e = new LinkedBlockingQueue(20);
        f724f = new AsyncTaskExecutor();
        f725g = new ThreadPoolExecutor(f719a, f720b, (long) f721c, f722d, f723e, f724f);
    }

    @SuppressLint({"NewApi"})
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> m418a(AsyncTask<Params, Progress, Result> asyncTask, Params... paramsArr) {
        if (Version.getAndroidSDKVersion() >= 11) {
            new Thread(new AsyncTaskExecutor(asyncTask, paramsArr)).start();
        } else {
            asyncTask.execute(paramsArr);
        }
        return asyncTask;
    }
}
