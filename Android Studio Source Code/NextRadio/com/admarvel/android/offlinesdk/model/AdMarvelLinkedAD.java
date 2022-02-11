package com.admarvel.android.offlinesdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AdMarvelLinkedAD implements Serializable {
    private static final long serialVersionUID = 132198219871927192L;
    int adId;
    HashMap<String, ArrayList<String[]>> adTargetingOption;
    int adWeight;
    int campaignPriority;
    float deliveryPriority;
    HashMap<String, String> deliveryPriorityBySite;
    int deliveryWeight;
    long endDate;
    long startDate;

    public int getAdId() {
        return this.adId;
    }

    public HashMap<String, ArrayList<String[]>> getAdTargetingOption() {
        return this.adTargetingOption;
    }

    public int getAdWeight() {
        return this.adWeight;
    }

    public int getCampaignPriority() {
        return this.campaignPriority;
    }

    public float getDeliveryPriority() {
        return this.deliveryPriority;
    }

    public HashMap<String, String> getDeliveryPriorityBySite() {
        return this.deliveryPriorityBySite;
    }

    public int getDeliveryWeight() {
        return this.deliveryWeight;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setAdTargetingOption(HashMap<String, ArrayList<String[]>> adTargetingOption) {
        this.adTargetingOption = adTargetingOption;
    }

    public void setAdWeight(int adWeight) {
        this.adWeight = adWeight;
    }

    public void setCampaignPriority(int campaignPriority) {
        this.campaignPriority = campaignPriority;
    }

    public void setDeliveryPriority(float deliveryPriority) {
        this.deliveryPriority = deliveryPriority;
    }

    public void setDeliveryPriorityBySite(HashMap<String, String> deliveryPriorityBySite) {
        this.deliveryPriorityBySite = deliveryPriorityBySite;
    }

    public void setDeliveryWeight(int deliveryWeight) {
        this.deliveryWeight = deliveryWeight;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }
}
