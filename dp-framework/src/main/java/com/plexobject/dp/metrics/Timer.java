package com.plexobject.dp.metrics;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * This class collects timing data but it's not thread safe.
 * 
 * 
 */
public class Timer {
    private static final Logger LOGGER = Logger.getLogger(Timer.class);
    private static final long LOGGING_THRESHOLD_IN_MILLIS = 100;
    private final Metric metric;

    private static class TimingData {
        private long started;
        private long ended;

        private TimingData() {
            this.started = System.currentTimeMillis();
        }

        private void stop() {
            this.ended = System.currentTimeMillis();
        }

        private long getTotalDuration() {
            return ended - started;
        }
    }

    private TimingData currentTimingData;
    private Collection<TimingData> elapsedTimingData;

    // This class can only be created by Metric and it's using
    // package scope for testing.
    Timer(final Metric metric) {
        this.metric = metric;
        this.currentTimingData = new TimingData();
    }

    /**
     * stop - stops the timer
     * 
     * @throws IllegalStateException
     *             - if timer is used again after stop
     */
    public void stop() {
        stop("");
    }

    /**
     * stop - stops the timer
     * 
     * @throws IllegalStateException
     *             - if timer is used again after stop
     */
    public void stop(final String logMessage) {
        if (currentTimingData == null) {
            throw new IllegalStateException("Timer has already been stopped");
        }
        currentTimingData.stop();

        final int totalCalls = elapsedTimingData == null ? 1
                : elapsedTimingData.size() + 1;
        long duration = currentTimingData.getTotalDuration();

        if (elapsedTimingData != null) {
            for (TimingData data : elapsedTimingData) {
                duration += data.getTotalDuration();
            }
        }
        metric.finishedTimer(totalCalls, duration);

        if (duration > LOGGING_THRESHOLD_IN_MILLIS && LOGGER.isInfoEnabled()) {
            LOGGER.info("ended " + logMessage + " for metrics " + metric
                    + getSystemStats());
        }
        currentTimingData = null;

    }

    /**
     * lapse - marks current timing and starts another timer
     * 
     * @throws IllegalStateException
     *             - if timer is already stopped
     */
    public void lapse() {
        lapse("");
    }

    /**
     * lapse - marks current timing and starts another timer
     * 
     * @param logMessage
     *            - will print log message if duration exceeds logging threshold
     * @throws IllegalStateException
     *             - if timer is already stopped
     */
    public void lapse(final String logMessage) {
        if (currentTimingData == null) {
            throw new IllegalStateException("Timer has already been stopped");
        }
        currentTimingData.stop();
        getElapsedTimingData().add(currentTimingData);
        if (currentTimingData.getTotalDuration() > LOGGING_THRESHOLD_IN_MILLIS
                && LOGGER.isInfoEnabled()) {
            LOGGER.info("lapsed " + logMessage + " for metrics " + metric);
        }
        currentTimingData = new TimingData();
    }

    private Collection<TimingData> getElapsedTimingData() {
        if (elapsedTimingData == null) {
            elapsedTimingData = new ArrayList<TimingData>();
        }
        return elapsedTimingData;
    }

    public static String getSystemStats() {
        final StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        sb.append(", load: ").append(
                String.format("%.2f", ManagementFactory
                        .getOperatingSystemMXBean().getSystemLoadAverage()));
        sb.append(", memory: ").append(runtime.freeMemory() / 1024 / 1024)
                .append("M/").append(runtime.totalMemory() / 1024 / 1024 + "M");
        sb.append(", threads: ").append(Thread.activeCount());
        return sb.toString();
    }
}
