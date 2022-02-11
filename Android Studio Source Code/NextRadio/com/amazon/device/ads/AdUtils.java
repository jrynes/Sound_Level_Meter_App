package com.amazon.device.ads;

import android.content.Context;
import android.content.pm.ActivityInfo;
import java.util.HashSet;

class AdUtils {
    public static final String REQUIRED_ACTIVITY = "com.amazon.device.ads.AdActivity";
    private static AdUtilsExecutor executor;

    static class AdUtilsExecutor {
        private boolean hasRequiredActivities;
        private final HashSet<String> requiredActivities;

        AdUtilsExecutor() {
            this.requiredActivities = new HashSet();
            this.hasRequiredActivities = false;
            this.requiredActivities.add(AdUtils.REQUIRED_ACTIVITY);
        }

        boolean checkDefinedActivities(Context context) {
            boolean z = true;
            if (!this.hasRequiredActivities) {
                HashSet<String> activities = new HashSet();
                try {
                    if (AndroidTargetUtils.isAtLeastAndroidAPI(8)) {
                        for (ActivityInfo a : context.getPackageManager().getPackageArchiveInfo(AndroidTargetUtils.getPackageCodePath(context), 1).activities) {
                            activities.add(a.name);
                        }
                        this.hasRequiredActivities = activities.containsAll(this.requiredActivities);
                        z = this.hasRequiredActivities;
                    }
                } catch (Exception e) {
                }
                this.hasRequiredActivities = true;
            }
            return z;
        }

        void setConnectionMetrics(ConnectionInfo connectionInfo, MetricsCollector metricsCollector) {
            if (connectionInfo != null) {
                if (connectionInfo.isWiFi()) {
                    metricsCollector.incrementMetric(MetricType.WIFI_PRESENT);
                } else {
                    metricsCollector.setMetricString(MetricType.CONNECTION_TYPE, connectionInfo.getConnectionType());
                }
            }
            DeviceInfo deviceInfo = MobileAdsInfoStore.getInstance().getDeviceInfo();
            if (deviceInfo.getCarrier() != null) {
                metricsCollector.setMetricString(MetricType.CARRIER_NAME, deviceInfo.getCarrier());
            }
        }

        double getViewportInitialScale(double defaultScale) {
            return AndroidTargetUtils.isAtLeastAndroidAPI(19) ? 1.0d : defaultScale;
        }

        double calculateScalingMultiplier(int absoluteAdWidth, int absoluteAdHeight, int absoluteWindowWidth, int absoluteWindowHeight) {
            double multiplier;
            double widthRatio = ((double) absoluteWindowWidth) / ((double) absoluteAdWidth);
            double heightRatio = ((double) absoluteWindowHeight) / ((double) absoluteAdHeight);
            if ((heightRatio < widthRatio || widthRatio == 0.0d) && heightRatio != 0.0d) {
                multiplier = heightRatio;
            } else {
                multiplier = widthRatio;
            }
            return multiplier == 0.0d ? 1.0d : multiplier;
        }

        int pixelToDeviceIndependentPixel(int px) {
            return (int) (((float) px) / getScalingFactorAsFloat());
        }

        int deviceIndependentPixelToPixel(int dp) {
            return (int) (((float) dp) * getScalingFactorAsFloat());
        }

        float getScalingFactorAsFloat() {
            return MobileAdsInfoStore.getInstance().getDeviceInfo().getScalingFactorAsFloat();
        }
    }

    static {
        executor = new AdUtilsExecutor();
    }

    private AdUtils() {
    }

    static boolean checkDefinedActivities(Context context) {
        return executor.checkDefinedActivities(context);
    }

    static void setConnectionMetrics(ConnectionInfo connectionInfo, MetricsCollector metricsCollector) {
        executor.setConnectionMetrics(connectionInfo, metricsCollector);
    }

    public static double getViewportInitialScale(double defaultScale) {
        return executor.getViewportInitialScale(defaultScale);
    }

    public static double calculateScalingMultiplier(int absoluteAdWidth, int absoluteAdHeight, int absoluteWindowWidth, int absoluteWindowHeight) {
        return executor.calculateScalingMultiplier(absoluteAdWidth, absoluteAdHeight, absoluteWindowWidth, absoluteWindowHeight);
    }

    public static int pixelToDeviceIndependentPixel(int px) {
        return executor.pixelToDeviceIndependentPixel(px);
    }

    public static int deviceIndependentPixelToPixel(int dp) {
        return executor.deviceIndependentPixelToPixel(dp);
    }

    public static float getScalingFactorAsFloat() {
        return executor.getScalingFactorAsFloat();
    }
}
