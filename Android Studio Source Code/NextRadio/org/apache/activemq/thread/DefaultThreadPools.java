package org.apache.activemq.thread;

@Deprecated
public final class DefaultThreadPools {
    private static final TaskRunnerFactory DEFAULT_TASK_RUNNER_FACTORY;

    static {
        DEFAULT_TASK_RUNNER_FACTORY = new TaskRunnerFactory();
    }

    private DefaultThreadPools() {
    }

    @Deprecated
    public static TaskRunnerFactory getDefaultTaskRunnerFactory() {
        return DEFAULT_TASK_RUNNER_FACTORY;
    }

    public static void shutdown() {
        DEFAULT_TASK_RUNNER_FACTORY.shutdown();
    }
}
