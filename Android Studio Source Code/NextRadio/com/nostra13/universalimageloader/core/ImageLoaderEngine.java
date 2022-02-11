package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

class ImageLoaderEngine {
    private final Map<Integer, String> cacheKeysForImageAwares;
    final ImageLoaderConfiguration configuration;
    private final AtomicBoolean networkDenied;
    private final AtomicBoolean paused;
    private final AtomicBoolean slowNetwork;
    private ExecutorService taskDistributor;
    private Executor taskExecutor;
    private Executor taskExecutorForCachedImages;
    private final Map<String, ReentrantLock> uriLocks;

    /* renamed from: com.nostra13.universalimageloader.core.ImageLoaderEngine.1 */
    class C12601 implements Runnable {
        final /* synthetic */ LoadAndDisplayImageTask val$task;

        C12601(LoadAndDisplayImageTask loadAndDisplayImageTask) {
            this.val$task = loadAndDisplayImageTask;
        }

        public void run() {
            boolean isImageCachedOnDisc = ImageLoaderEngine.this.configuration.discCache.get(this.val$task.getLoadingUri()).exists();
            ImageLoaderEngine.this.initExecutorsIfNeed();
            if (isImageCachedOnDisc) {
                ImageLoaderEngine.this.taskExecutorForCachedImages.execute(this.val$task);
            } else {
                ImageLoaderEngine.this.taskExecutor.execute(this.val$task);
            }
        }
    }

    ImageLoaderEngine(ImageLoaderConfiguration configuration) {
        this.cacheKeysForImageAwares = Collections.synchronizedMap(new HashMap());
        this.uriLocks = new WeakHashMap();
        this.paused = new AtomicBoolean(false);
        this.networkDenied = new AtomicBoolean(false);
        this.slowNetwork = new AtomicBoolean(false);
        this.configuration = configuration;
        this.taskExecutor = configuration.taskExecutor;
        this.taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;
        this.taskDistributor = Executors.newCachedThreadPool();
    }

    void submit(LoadAndDisplayImageTask task) {
        this.taskDistributor.execute(new C12601(task));
    }

    void submit(ProcessAndDisplayImageTask task) {
        initExecutorsIfNeed();
        this.taskExecutorForCachedImages.execute(task);
    }

    private void initExecutorsIfNeed() {
        if (!this.configuration.customExecutor && ((ExecutorService) this.taskExecutor).isShutdown()) {
            this.taskExecutor = createTaskExecutor();
        }
        if (!this.configuration.customExecutorForCachedImages && ((ExecutorService) this.taskExecutorForCachedImages).isShutdown()) {
            this.taskExecutorForCachedImages = createTaskExecutor();
        }
    }

    private Executor createTaskExecutor() {
        return DefaultConfigurationFactory.createExecutor(this.configuration.threadPoolSize, this.configuration.threadPriority, this.configuration.tasksProcessingType);
    }

    String getLoadingUriForView(ImageAware imageAware) {
        return (String) this.cacheKeysForImageAwares.get(Integer.valueOf(imageAware.getId()));
    }

    void prepareDisplayTaskFor(ImageAware imageAware, String memoryCacheKey) {
        this.cacheKeysForImageAwares.put(Integer.valueOf(imageAware.getId()), memoryCacheKey);
    }

    void cancelDisplayTaskFor(ImageAware imageAware) {
        this.cacheKeysForImageAwares.remove(Integer.valueOf(imageAware.getId()));
    }

    void denyNetworkDownloads(boolean denyNetworkDownloads) {
        this.networkDenied.set(denyNetworkDownloads);
    }

    void handleSlowNetwork(boolean handleSlowNetwork) {
        this.slowNetwork.set(handleSlowNetwork);
    }

    void pause() {
        this.paused.set(true);
    }

    void resume() {
        synchronized (this.paused) {
            this.paused.set(false);
            this.paused.notifyAll();
        }
    }

    void stop() {
        if (!this.configuration.customExecutor) {
            ((ExecutorService) this.taskExecutor).shutdownNow();
        }
        if (!this.configuration.customExecutorForCachedImages) {
            ((ExecutorService) this.taskExecutorForCachedImages).shutdownNow();
        }
        this.cacheKeysForImageAwares.clear();
        this.uriLocks.clear();
    }

    ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = (ReentrantLock) this.uriLocks.get(uri);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        this.uriLocks.put(uri, lock);
        return lock;
    }

    AtomicBoolean getPause() {
        return this.paused;
    }

    boolean isNetworkDenied() {
        return this.networkDenied.get();
    }

    boolean isSlowNetwork() {
        return this.slowNetwork.get();
    }
}
