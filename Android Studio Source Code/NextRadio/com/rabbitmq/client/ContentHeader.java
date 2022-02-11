package com.rabbitmq.client;

public interface ContentHeader extends Cloneable {
    void appendPropertyDebugStringTo(StringBuilder stringBuilder);

    int getClassId();

    String getClassName();
}
