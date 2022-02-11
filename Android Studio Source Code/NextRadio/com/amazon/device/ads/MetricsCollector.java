package com.amazon.device.ads;

import com.amazon.device.ads.AdProperties.AdType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

class MetricsCollector {
    private static final String LOGTAG;
    private String adTypeMetricTag;
    private final MobileAdsLogger logger;
    protected Vector<MetricHit> metricHits;

    static class CompositeMetricsCollector extends MetricsCollector {
        private final ArrayList<MetricsCollector> collectors;

        public CompositeMetricsCollector(ArrayList<MetricsCollector> collectors) {
            this.collectors = collectors;
        }

        public void incrementMetric(MetricType metric) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).incrementMetric(metric);
            }
        }

        public void setMetricString(MetricType metric, String s) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).setMetricString(metric, s);
            }
        }

        public void publishMetricInMilliseconds(MetricType metric, long value) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).publishMetricInMilliseconds(metric, value);
            }
        }

        public void publishMetricInMillisecondsFromNanoseconds(MetricType metric, long value) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).publishMetricInMillisecondsFromNanoseconds(metric, value);
            }
        }

        public void startMetricInMillisecondsFromNanoseconds(MetricType metric, long startTime) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).startMetricInMillisecondsFromNanoseconds(metric, startTime);
            }
        }

        public void startMetric(MetricType metric) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).startMetric(metric);
            }
        }

        public void stopMetricInMillisecondsFromNanoseconds(MetricType metric, long stopTime) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).stopMetricInMillisecondsFromNanoseconds(metric, stopTime);
            }
        }

        public void stopMetric(MetricType metric) {
            Iterator i$ = this.collectors.iterator();
            while (i$.hasNext()) {
                ((MetricsCollector) i$.next()).stopMetric(metric);
            }
        }
    }

    static class MetricHit {
        public final MetricType metric;

        public MetricHit(MetricType metric) {
            this.metric = metric;
        }
    }

    static class MetricHitIncrement extends MetricHit {
        public final int increment;

        public MetricHitIncrement(MetricType metric, int increment) {
            super(metric);
            this.increment = increment;
        }
    }

    static class MetricHitStartTime extends MetricHit {
        public final long startTime;

        public MetricHitStartTime(MetricType metric, long startTime) {
            super(metric);
            this.startTime = startTime;
        }
    }

    static class MetricHitStopTime extends MetricHit {
        public final long stopTime;

        public MetricHitStopTime(MetricType metric, long stopTime) {
            super(metric);
            this.stopTime = stopTime;
        }
    }

    static class MetricHitString extends MetricHit {
        public final String text;

        public MetricHitString(MetricType metric, String text) {
            super(metric);
            this.text = text;
        }
    }

    static class MetricHitTotalTime extends MetricHit {
        public final long totalTime;

        public MetricHitTotalTime(MetricType metric, long totalTime) {
            super(metric);
            this.totalTime = totalTime;
        }
    }

    static {
        LOGTAG = MetricsCollector.class.getSimpleName();
    }

    public MetricsCollector() {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.metricHits = new Vector(60);
    }

    public Vector<MetricHit> getMetricHits() {
        return this.metricHits;
    }

    public void incrementMetric(MetricType metric) {
        this.logger.m637d("METRIC Increment " + metric.toString());
        this.metricHits.add(new MetricHitIncrement(metric, 1));
    }

    public void setMetricString(MetricType metric, String s) {
        this.logger.m637d("METRIC Set " + metric.toString() + ": " + s);
        this.metricHits.add(new MetricHitString(metric, s));
    }

    public void publishMetricInMilliseconds(MetricType metric, long value) {
        this.logger.m637d("METRIC Publish " + metric.toString());
        this.metricHits.add(new MetricHitTotalTime(metric, value));
    }

    public void publishMetricInMillisecondsFromNanoseconds(MetricType metric, long value) {
        publishMetricInMilliseconds(metric, NumberUtils.convertToMillisecondsFromNanoseconds(value));
    }

    public void startMetricInMillisecondsFromNanoseconds(MetricType metric, long startTime) {
        this.logger.m637d("METRIC Start " + metric.toString());
        this.metricHits.add(new MetricHitStartTime(metric, NumberUtils.convertToMillisecondsFromNanoseconds(startTime)));
    }

    public void startMetric(MetricType metric) {
        startMetricInMillisecondsFromNanoseconds(metric, System.nanoTime());
    }

    public void stopMetricInMillisecondsFromNanoseconds(MetricType metric, long stopTime) {
        this.logger.m637d("METRIC Stop " + metric.toString());
        this.metricHits.add(new MetricHitStopTime(metric, NumberUtils.convertToMillisecondsFromNanoseconds(stopTime)));
    }

    public void stopMetric(MetricType metric) {
        stopMetricInMillisecondsFromNanoseconds(metric, System.nanoTime());
    }

    public void setAdType(AdType adType) {
        this.adTypeMetricTag = adType.getAdTypeMetricTag();
    }

    public String getAdTypeMetricTag() {
        return this.adTypeMetricTag;
    }

    public boolean isMetricsCollectorEmpty() {
        return this.metricHits.isEmpty();
    }
}
