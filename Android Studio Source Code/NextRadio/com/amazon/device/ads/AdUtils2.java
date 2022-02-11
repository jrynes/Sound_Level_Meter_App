package com.amazon.device.ads;

import android.content.Context;

/* compiled from: AdUtils */
class AdUtils2 {
    private final AdUtilsStatic adUtilsAdapter;

    /* compiled from: AdUtils */
    private static class AdUtilsStatic {
        private AdUtilsStatic() {
        }

        boolean checkDefinedActivities(Context context) {
            return AdUtils.checkDefinedActivities(context);
        }

        void setConnectionMetrics(ConnectionInfo connectionInfo, MetricsCollector metricsCollector) {
            AdUtils.setConnectionMetrics(connectionInfo, metricsCollector);
        }

        double getViewportInitialScale(double defaultScale) {
            return AdUtils.getViewportInitialScale(defaultScale);
        }

        double calculateScalingMultiplier(int absoluteAdWidth, int absoluteAdHeight, int absoluteWindowWidth, int absoluteWindowHeight) {
            return AdUtils.calculateScalingMultiplier(absoluteAdWidth, absoluteAdHeight, absoluteWindowWidth, absoluteWindowHeight);
        }

        int pixelToDeviceIndependentPixel(int px) {
            return AdUtils.pixelToDeviceIndependentPixel(px);
        }

        int deviceIndependentPixelToPixel(int dp) {
            return AdUtils.deviceIndependentPixelToPixel(dp);
        }

        float getScalingFactorAsFloat() {
            return AdUtils.getScalingFactorAsFloat();
        }
    }

    AdUtils2() {
        this.adUtilsAdapter = new AdUtilsStatic();
    }

    public boolean checkDefinedActivities(Context context) {
        return this.adUtilsAdapter.checkDefinedActivities(context);
    }

    public void setConnectionMetrics(ConnectionInfo connectionInfo, MetricsCollector metricsCollector) {
        this.adUtilsAdapter.setConnectionMetrics(connectionInfo, metricsCollector);
    }

    public double getViewportInitialScale(double defaultScale) {
        return this.adUtilsAdapter.getViewportInitialScale(defaultScale);
    }

    public double calculateScalingMultiplier(int absoluteAdWidth, int absoluteAdHeight, int absoluteWindowWidth, int absoluteWindowHeight) {
        return this.adUtilsAdapter.calculateScalingMultiplier(absoluteAdWidth, absoluteAdHeight, absoluteWindowWidth, absoluteWindowHeight);
    }

    public int pixelToDeviceIndependentPixel(int px) {
        return this.adUtilsAdapter.pixelToDeviceIndependentPixel(px);
    }

    public int deviceIndependentPixelToPixel(int dp) {
        return this.adUtilsAdapter.deviceIndependentPixelToPixel(dp);
    }

    public float getScalingFactorAsFloat() {
        return this.adUtilsAdapter.getScalingFactorAsFloat();
    }
}
