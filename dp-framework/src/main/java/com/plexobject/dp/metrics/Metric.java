package com.plexobject.dp.metrics;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class maintains profiling metrics. This is thread safe class.
 * 
 * 
 */
public class Metric {
    private final String name;
    private final AtomicLong totalDuration;
    private final AtomicLong totalCalls;
    private final AtomicLong skipCalls;

    static int MINIMUM_METRICS = 2; // skip first two requests

    private static final Map<String, Metric> METRICS = new ConcurrentHashMap<String, Metric>();

    // This class can only be created through factory-method and it's using
    // package scope for testing.
    Metric(final String name) {
        this.name = name;
        this.totalCalls = new AtomicLong();
        this.skipCalls = new AtomicLong();
        this.totalDuration = new AtomicLong();
    }

    public static Collection<Metric> getMetrics() {
        return Collections.unmodifiableCollection(METRICS.values());
    }

    /**
     * This method finds or creates metrics
     * 
     * @param name
     *            - name of metrics
     * @return Metric instance
     */
    public static Metric getMetric(final String name) {
        Objects.requireNonNull(name);
        Metric metric = null;

        synchronized (name.intern()) {
            metric = METRICS.get(name);
            if (metric == null) {
                metric = new Metric(name);
                METRICS.put(name, metric);
            }
        }
        return metric;
    }

    /**
     * This method finds metric and creates a new timer for it. The caller must
     * call lapse or stop to add the results back to the metric.
     * 
     * @param name
     *            - name of metric
     * @return Timer instance
     */
    public static Timer newTimer(final String name) {
        return getMetric(name).newTimer();
    }

    /**
     * This method creates a new timer for it. The caller must call lapse or
     * stop to add the results back to the metric.
     * 
     * @return Timer instance
     */

    public Timer newTimer() {
        return new Timer(this);
    }

    /**
     * 
     * @return total duration in milli-secs
     */
    public long getTotalDurationInMilliSecs() {
        return totalDuration.get();
    }

    /**
     * 
     * @return average duration in milli-secs
     */
    public double getAverageDurationInMilliSecs() {
        return getTotalDurationInMilliSecs() / (double) getTotalCalls();
    }

    /**
     * 
     * @return number of calls invoked
     */
    public long getTotalCalls() {
        return totalCalls.get();
    }

    void finishedTimer(final int totalCalls, final long totalDuration) {
        if (skipCalls.getAndIncrement() > MINIMUM_METRICS) {
            this.totalCalls.addAndGet(totalCalls);
            this.totalDuration.addAndGet(totalDuration);
        }
    }

    @Override
    public String toString() {
        double avg = getAverageDurationInMilliSecs();
        String avgTime = avg >= 1000 ? String.format("%.2f secs", avg / 1000)
                : String.format("%.2f millis", avg);
        return "Metrics(" + name + ") calls " + getTotalCalls() + ", average "
                + avgTime;
    }
}
