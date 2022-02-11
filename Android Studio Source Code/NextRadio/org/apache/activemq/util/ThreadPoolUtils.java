package org.apache.activemq.util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ThreadPoolUtils {
    public static final long DEFAULT_SHUTDOWN_AWAIT_TERMINATION = 10000;
    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(ThreadPoolUtils.class);
    }

    public static void shutdown(ExecutorService executorService) {
        doShutdown(executorService, 0);
    }

    public static List<Runnable> shutdownNow(ExecutorService executorService) {
        List<Runnable> answer = null;
        if (!executorService.isShutdown()) {
            LOG.debug("Forcing shutdown of ExecutorService: {}", executorService);
            answer = executorService.shutdownNow();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Shutdown of ExecutorService: {} is shutdown: {} and terminated: {}.", new Object[]{executorService, Boolean.valueOf(executorService.isShutdown()), Boolean.valueOf(executorService.isTerminated())});
            }
        }
        return answer;
    }

    public static void shutdownGraceful(ExecutorService executorService) {
        doShutdown(executorService, DEFAULT_SHUTDOWN_AWAIT_TERMINATION);
    }

    public static void shutdownGraceful(ExecutorService executorService, long shutdownAwaitTermination) {
        doShutdown(executorService, shutdownAwaitTermination);
    }

    private static void doShutdown(ExecutorService executorService, long shutdownAwaitTermination) {
        if (executorService != null && !executorService.isShutdown()) {
            boolean warned = false;
            StopWatch watch = new StopWatch();
            LOG.trace("Shutdown of ExecutorService: {} with await termination: {} millis", executorService, Long.valueOf(shutdownAwaitTermination));
            executorService.shutdown();
            if (shutdownAwaitTermination > 0) {
                try {
                    if (!awaitTermination(executorService, shutdownAwaitTermination)) {
                        warned = true;
                        LOG.warn("Forcing shutdown of ExecutorService: {} due first await termination elapsed.", executorService);
                        executorService.shutdownNow();
                        if (!awaitTermination(executorService, shutdownAwaitTermination)) {
                            LOG.warn("Cannot completely force shutdown of ExecutorService: {} due second await termination elapsed.", executorService);
                        }
                    }
                } catch (InterruptedException e) {
                    warned = true;
                    LOG.warn("Forcing shutdown of ExecutorService: {} due interrupted.", executorService);
                    executorService.shutdownNow();
                }
            }
            if (warned) {
                LOG.info("Shutdown of ExecutorService: {} is shutdown: {} and terminated: {} took: {}.", new Object[]{executorService, Boolean.valueOf(executorService.isShutdown()), Boolean.valueOf(executorService.isTerminated()), TimeUtils.printDuration((double) watch.taken())});
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("Shutdown of ExecutorService: {} is shutdown: {} and terminated: {} took: {}.", new Object[]{executorService, Boolean.valueOf(executorService.isShutdown()), Boolean.valueOf(executorService.isTerminated()), TimeUtils.printDuration((double) watch.taken())});
            }
        }
    }

    public static boolean awaitTermination(ExecutorService executorService, long shutdownAwaitTermination) throws InterruptedException {
        StopWatch watch = new StopWatch();
        long interval = Math.min(2000, shutdownAwaitTermination);
        boolean done = false;
        while (!done && interval > 0) {
            if (executorService.awaitTermination(interval, TimeUnit.MILLISECONDS)) {
                done = true;
            } else {
                LOG.info("Waited {} for ExecutorService: {} to terminate...", TimeUtils.printDuration((double) watch.taken()), executorService);
                interval = Math.min(2000, shutdownAwaitTermination - watch.taken());
            }
        }
        return done;
    }
}
