package com.amazon.device.ads;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.xbill.DNS.Zone;

class ThreadUtils {
    private static ThreadRunner threadRunner;

    static abstract class MobileAdsAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
        protected abstract Result doInBackground(Params... paramsArr);

        MobileAdsAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
        }
    }

    /* renamed from: com.amazon.device.ads.ThreadUtils.1 */
    static /* synthetic */ class C03431 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$ThreadUtils$ExecutionThread;

        static {
            $SwitchMap$com$amazon$device$ads$ThreadUtils$ExecutionThread = new int[ExecutionThread.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$ThreadUtils$ExecutionThread[ExecutionThread.MAIN_THREAD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$ThreadUtils$ExecutionThread[ExecutionThread.BACKGROUND_THREAD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public static abstract class RunnableExecutor {
        private final ExecutionStyle executionStyle;
        private final ExecutionThread executionThread;

        public abstract void execute(Runnable runnable);

        public RunnableExecutor(ExecutionStyle executionStyle, ExecutionThread executionThread) {
            this.executionStyle = executionStyle;
            this.executionThread = executionThread;
        }

        public ExecutionStyle getExecutionStyle() {
            return this.executionStyle;
        }

        public ExecutionThread getExecutionThread() {
            return this.executionThread;
        }
    }

    public static class ThreadExecutor extends RunnableExecutor {
        private final RunnableExecutor threadScheduler;
        private final ThreadVerify threadVerify;

        public ThreadExecutor(ThreadVerify threadVerify, RunnableExecutor threadScheduler) {
            super(ExecutionStyle.RUN_ASAP, threadScheduler.executionThread);
            this.threadVerify = threadVerify;
            this.threadScheduler = threadScheduler;
        }

        public void execute(Runnable proc) {
            boolean shouldSchedule;
            switch (C03431.$SwitchMap$com$amazon$device$ads$ThreadUtils$ExecutionThread[this.threadScheduler.getExecutionThread().ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    shouldSchedule = !this.threadVerify.isOnMainThread();
                    break;
                case Zone.SECONDARY /*2*/:
                    shouldSchedule = this.threadVerify.isOnMainThread();
                    break;
                default:
                    shouldSchedule = false;
                    break;
            }
            if (shouldSchedule) {
                this.threadScheduler.execute(proc);
            } else {
                proc.run();
            }
        }
    }

    public static class BackgroundThreadRunner extends ThreadExecutor {
        public BackgroundThreadRunner(ThreadVerify threadVerify) {
            super(threadVerify, new ThreadPoolScheduler());
        }
    }

    public enum ExecutionStyle {
        RUN_ASAP,
        SCHEDULE
    }

    public enum ExecutionThread {
        MAIN_THREAD,
        BACKGROUND_THREAD
    }

    public static class MainThreadRunner extends ThreadExecutor {
        public MainThreadRunner(ThreadVerify threadVerify) {
            super(threadVerify, new MainThreadScheduler());
        }
    }

    public static class MainThreadScheduler extends RunnableExecutor {
        public MainThreadScheduler() {
            super(ExecutionStyle.SCHEDULE, ExecutionThread.MAIN_THREAD);
        }

        public void execute(Runnable proc) {
            new Handler(Looper.getMainLooper()).post(proc);
        }
    }

    public static class SingleThreadScheduler extends RunnableExecutor {
        private ExecutorService executorService;

        public SingleThreadScheduler() {
            super(ExecutionStyle.SCHEDULE, ExecutionThread.BACKGROUND_THREAD);
            this.executorService = Executors.newSingleThreadExecutor();
        }

        public void execute(Runnable proc) {
            this.executorService.submit(proc);
        }
    }

    public static class ThreadPoolScheduler extends RunnableExecutor {
        private static final int keepAliveTimeSeconds = 30;
        private static final int maxNumberThreads = 3;
        private static final int numberThreads = 1;
        private final ExecutorService executorService;

        public ThreadPoolScheduler() {
            super(ExecutionStyle.SCHEDULE, ExecutionThread.BACKGROUND_THREAD);
            this.executorService = new ThreadPoolExecutor(numberThreads, maxNumberThreads, 30, TimeUnit.SECONDS, new LinkedBlockingQueue());
        }

        public void execute(Runnable proc) {
            this.executorService.submit(proc);
        }
    }

    public static class ThreadRunner {
        private static final String LOGTAG;
        private final HashMap<ExecutionStyle, HashMap<ExecutionThread, RunnableExecutor>> executors;
        private final MobileAdsLogger logger;

        /* renamed from: com.amazon.device.ads.ThreadUtils.ThreadRunner.1 */
        class C03441 implements Runnable {
            final /* synthetic */ Object[] val$params;
            final /* synthetic */ MobileAdsAsyncTask val$task;

            C03441(MobileAdsAsyncTask mobileAdsAsyncTask, Object[] objArr) {
                this.val$task = mobileAdsAsyncTask;
                this.val$params = objArr;
            }

            public void run() {
                AndroidTargetUtils.executeAsyncTask(this.val$task, this.val$params);
            }
        }

        static {
            LOGTAG = ThreadRunner.class.getSimpleName();
        }

        ThreadRunner() {
            this(new MobileAdsLoggerFactory());
            ThreadVerify threadVerify = new ThreadVerify();
            withExecutor(new ThreadPoolScheduler());
            withExecutor(new BackgroundThreadRunner(threadVerify));
            withExecutor(new MainThreadScheduler());
            withExecutor(new MainThreadRunner(threadVerify));
        }

        ThreadRunner(MobileAdsLoggerFactory loggerFactory) {
            this.executors = new HashMap();
            this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        }

        public ThreadRunner withExecutor(RunnableExecutor executor) {
            HashMap<ExecutionThread, RunnableExecutor> executorsForStyle = (HashMap) this.executors.get(executor.getExecutionStyle());
            if (executorsForStyle == null) {
                executorsForStyle = new HashMap();
                this.executors.put(executor.getExecutionStyle(), executorsForStyle);
            }
            executorsForStyle.put(executor.getExecutionThread(), executor);
            return this;
        }

        public void execute(Runnable proc, ExecutionStyle executionStyle, ExecutionThread executionThread) {
            HashMap<ExecutionThread, RunnableExecutor> executorsForStyle = (HashMap) this.executors.get(executionStyle);
            if (executorsForStyle == null) {
                this.logger.m640e("No executor available for %s execution style.", executionStyle);
                return;
            }
            RunnableExecutor executor = (RunnableExecutor) executorsForStyle.get(executionThread);
            if (executor == null) {
                this.logger.m640e("No executor available for %s execution style on % execution thread.", executionStyle, executionThread);
            }
            executor.execute(proc);
        }

        public <T> void executeAsyncTask(MobileAdsAsyncTask<T, ?, ?> task, T... params) {
            executeAsyncTask(ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD, task, params);
        }

        public <T> void executeAsyncTask(ExecutionStyle executionStyle, ExecutionThread executionThread, MobileAdsAsyncTask<T, ?, ?> task, T... params) {
            ThreadUtils.threadRunner.execute(new C03441(task, params), executionStyle, executionThread);
        }
    }

    static class ThreadVerify {
        private static ThreadVerify instance;

        ThreadVerify() {
        }

        static {
            instance = new ThreadVerify();
        }

        static ThreadVerify getInstance() {
            return instance;
        }

        boolean isOnMainThread() {
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        }
    }

    ThreadUtils() {
    }

    static {
        threadRunner = new ThreadRunner();
    }

    public static ThreadRunner getThreadRunner() {
        return threadRunner;
    }

    static void setThreadRunner(ThreadRunner threadRunner) {
        threadRunner = threadRunner;
    }

    public static boolean isOnMainThread() {
        return ThreadVerify.getInstance().isOnMainThread();
    }

    public static final <T> void executeAsyncTask(MobileAdsAsyncTask<T, ?, ?> task, T... params) {
        threadRunner.executeAsyncTask(ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD, task, params);
    }

    public static final <T> void executeAsyncTask(ThreadRunner threadRunner, ExecutionStyle executionStyle, ExecutionThread executionThread, MobileAdsAsyncTask<T, ?, ?> task, T... params) {
        threadRunner.executeAsyncTask(executionStyle, executionThread, task, params);
    }

    public static void scheduleRunnable(Runnable proc) {
        scheduleRunnable(proc, threadRunner);
    }

    public static void scheduleRunnable(Runnable proc, ThreadRunner threadRunner) {
        threadRunner.execute(proc, ExecutionStyle.SCHEDULE, ExecutionThread.BACKGROUND_THREAD);
    }

    public static void executeRunnableWithThreadCheck(Runnable proc) {
        executeRunnableWithThreadCheck(proc, threadRunner);
    }

    public static void executeRunnableWithThreadCheck(Runnable proc, ThreadRunner threadRunner) {
        threadRunner.execute(proc, ExecutionStyle.RUN_ASAP, ExecutionThread.BACKGROUND_THREAD);
    }

    public static void scheduleOnMainThread(Runnable proc) {
        scheduleOnMainThread(proc, threadRunner);
    }

    public static void scheduleOnMainThread(Runnable proc, ThreadRunner threadRunner) {
        threadRunner.execute(proc, ExecutionStyle.SCHEDULE, ExecutionThread.MAIN_THREAD);
    }

    public static void executeOnMainThread(Runnable proc) {
        executeOnMainThread(proc, threadRunner);
    }

    public static void executeOnMainThread(Runnable proc, ThreadRunner threadRunner) {
        threadRunner.execute(proc, ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
    }
}
