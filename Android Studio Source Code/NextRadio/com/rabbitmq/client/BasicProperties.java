package com.rabbitmq.client;

import java.util.Date;
import java.util.Map;

public interface BasicProperties {
    String getAppId();

    String getContentEncoding();

    String getContentType();

    String getCorrelationId();

    Integer getDeliveryMode();

    String getExpiration();

    Map<String, Object> getHeaders();

    String getMessageId();

    Integer getPriority();

    String getReplyTo();

    Date getTimestamp();

    String getType();

    String getUserId();

    @Deprecated
    void setAppId(String str);

    @Deprecated
    void setContentEncoding(String str);

    @Deprecated
    void setContentType(String str);

    @Deprecated
    void setCorrelationId(String str);

    @Deprecated
    void setDeliveryMode(Integer num);

    @Deprecated
    void setExpiration(String str);

    @Deprecated
    void setHeaders(Map<String, Object> map);

    @Deprecated
    void setMessageId(String str);

    @Deprecated
    void setPriority(Integer num);

    @Deprecated
    void setReplyTo(String str);

    @Deprecated
    void setTimestamp(Date date);

    @Deprecated
    void setType(String str);

    @Deprecated
    void setUserId(String str);
}
