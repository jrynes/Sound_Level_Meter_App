package com.onelouder.adlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class RunnableManager {
    private static final String TAG = "adlib-RunnableManager";
    private static RunnableManager _SingleInstance;
    private Object csLock;
    private int mMaxThreads;
    private final HashMap<String, Boolean> mRequestMap;
    private final ArrayList<RunnableRequest> mRequests;
    private int mThreadCount;
    private int mThreadsWaiting;

    class RunnableProc implements Runnable {
        RunnableProc() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r8 = this;
            r3 = "adlib-RunnableManager";
            r4 = "---------- runnable proc ENTER ---------------";
            com.onelouder.adlib.Diagnostics.m1951d(r3, r4);	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
        L_0x0007:
            r2 = 0;
            r0 = 0;
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3 = r3.mThreadsWaiting;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            if (r3 <= 0) goto L_0x0016;
        L_0x0011:
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3.mThreadsWaiting = r3.mThreadsWaiting - 1;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
        L_0x0016:
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r2 = r3.popRequest();	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            if (r2 == 0) goto L_0x0041;
        L_0x001e:
            r2.run();	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r0 = 1;
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3 = r3.mRequestMap;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r4 = r2.toString();	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3.remove(r4);	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            goto L_0x0016;
        L_0x0030:
            r1 = move-exception;
            r1.printStackTrace();
        L_0x0034:
            r3 = "adlib-RunnableManager";
            r4 = "---------- runnable proc EXIT ---------------";
            com.onelouder.adlib.Diagnostics.m1951d(r3, r4);
            r3 = com.onelouder.adlib.RunnableManager.this;
            r3.mThreadCount = r3.mThreadCount - 1;
            return;
        L_0x0041:
            if (r0 == 0) goto L_0x0034;
        L_0x0043:
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3.mThreadsWaiting = r3.mThreadsWaiting + 1;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r4 = r3.csLock;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            monitor-enter(r4);	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
            r3 = com.onelouder.adlib.RunnableManager.this;	 Catch:{ all -> 0x005c }
            r3 = r3.csLock;	 Catch:{ all -> 0x005c }
            r6 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            r3.wait(r6);	 Catch:{ all -> 0x005c }
            monitor-exit(r4);	 Catch:{ all -> 0x005c }
            goto L_0x0007;
        L_0x005c:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x005c }
            throw r3;	 Catch:{ InterruptedException -> 0x0030, Exception -> 0x005f }
        L_0x005f:
            r1 = move-exception;
            r1.printStackTrace();
            goto L_0x0034;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.onelouder.adlib.RunnableManager.RunnableProc.run():void");
        }
    }

    class RunnableRequest {
        public Runnable mReq;
        public Object mTag;

        public RunnableRequest(Runnable req) {
            this.mReq = null;
            this.mTag = null;
            this.mReq = req;
        }

        public RunnableRequest(Runnable req, Object tag) {
            this.mReq = null;
            this.mTag = null;
            this.mReq = req;
            this.mTag = tag;
        }

        public String toString() {
            String str = this.mReq.toString();
            if (this.mTag != null) {
                return str + this.mTag.toString();
            }
            return str;
        }
    }

    static {
        _SingleInstance = null;
    }

    private RunnableManager() {
        this.mRequests = new ArrayList();
        this.mRequestMap = new HashMap();
        this.mThreadsWaiting = 0;
        this.mThreadCount = 0;
        this.mMaxThreads = 5;
        this.csLock = new Object();
    }

    public static RunnableManager getInstance() {
        if (_SingleInstance == null) {
            _SingleInstance = new RunnableManager();
        }
        return _SingleInstance;
    }

    public void pushRequestAtFront(Runnable item) {
        synchronized (this.mRequests) {
            String key = item.toString();
            if (!this.mRequestMap.containsKey(key)) {
                RunnableRequest req = new RunnableRequest(item);
                this.mRequestMap.put(key, Boolean.valueOf(true));
                this.mRequests.add(0, req);
                startNextThread();
            }
        }
    }

    public void pushRequest(Runnable item) {
        pushRequest(item, null);
    }

    public void pushRequest(Runnable item, Object tag) {
        synchronized (this.mRequests) {
            String key = item.toString();
            if (!this.mRequestMap.containsKey(key)) {
                RunnableRequest req = new RunnableRequest(item, tag);
                this.mRequestMap.put(key, Boolean.valueOf(true));
                this.mRequests.add(req);
                startNextThread();
            }
        }
    }

    public void removeRequestsWithTag(Object tag) {
        Diagnostics.m1951d(TAG, "removeRequestsWithTag tag=" + tag.toString());
        synchronized (this.mRequests) {
            ArrayList<RunnableRequest> toRemove = new ArrayList();
            Iterator i$ = this.mRequests.iterator();
            while (i$.hasNext()) {
                RunnableRequest req = (RunnableRequest) i$.next();
                if (req.mTag != null && req.mTag.equals(tag)) {
                    toRemove.add(req);
                }
            }
            i$ = toRemove.iterator();
            while (i$.hasNext()) {
                req = (RunnableRequest) i$.next();
                this.mRequestMap.remove(req.mReq.toString());
                this.mRequests.remove(req);
            }
        }
    }

    private Runnable popRequest() {
        Runnable item = null;
        synchronized (this.mRequests) {
            if (this.mRequests.size() > 0) {
                item = ((RunnableRequest) this.mRequests.remove(0)).mReq;
            }
        }
        return item;
    }

    private void startNextThread() {
        if (this.mThreadCount < this.mMaxThreads && this.mThreadsWaiting == 0) {
            this.mThreadCount++;
            Thread mImageThread = new Thread(new RunnableProc());
            mImageThread.setName("runnable " + this.mThreadCount);
            mImageThread.start();
        }
        synchronized (this.csLock) {
            this.csLock.notify();
        }
    }
}
