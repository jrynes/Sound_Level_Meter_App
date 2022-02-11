package com.nextradioapp.androidSDK.data;

public interface IRabbitMQListener {
    void onConnectFailed();

    void onError(Exception exception);

    void onInterrupted();

    void onMQConnecting();

    void onMessageReceived(String str);
}
