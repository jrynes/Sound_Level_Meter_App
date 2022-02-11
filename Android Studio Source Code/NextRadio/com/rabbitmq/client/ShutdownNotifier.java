package com.rabbitmq.client;

public interface ShutdownNotifier {
    void addShutdownListener(ShutdownListener shutdownListener);

    ShutdownSignalException getCloseReason();

    boolean isOpen();

    void notifyListeners();

    void removeShutdownListener(ShutdownListener shutdownListener);
}
