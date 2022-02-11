package com.admarvel.android.ads;

import java.util.Map;

public class AdMarvelReward {
    Map<String, String> metaDatas;
    String partnerId;
    String rewardName;
    String rewardValue;
    String siteId;
    boolean success;

    public Map<String, String> getMetaDatas() {
        return this.metaDatas;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public String getRewardName() {
        return this.rewardName;
    }

    public String getRewardValue() {
        return this.rewardValue;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public boolean isSuccess() {
        return this.success;
    }

    void setMetaDatas(Map<String, String> metaDatas) {
        this.metaDatas = metaDatas;
    }

    void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }
}
