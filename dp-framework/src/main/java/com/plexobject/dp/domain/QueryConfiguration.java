package com.plexobject.dp.domain;

import java.util.HashMap;
import java.util.Map;

import com.plexobject.dp.util.ObjectConversionUtils;

/**
 * This class defines configuration parameters for query
 * 
 * @author shahzad bhatti
 *
 */
public class QueryConfiguration {
    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String QUERY_TIMEOUT = "queryTimeoutMillis";
    private static final String ORDER_BY = "orderBy";
    private static final String GROUP_BY = "groupBy";
    private static final String ABORT_UPON_FAILURE = "abortUponPartialFailure";

    private int page;
    private int limit;
    private long queryTimeoutMillis;
    private String orderBy;
    private String groupBy;
    private int maxThreads;
    private boolean abortUponPartialFailure;
    private Map<String, Object> filterCriteria = new HashMap<>();

    public static QueryConfiguration from(final Map<String, Object> args) {
        final QueryConfiguration config = new QueryConfiguration();
        for (Map.Entry<String, Object> e : args.entrySet()) {
            if (PAGE.equals(e.getKey())) {
                config.setPage((int) ObjectConversionUtils.getAsLong(e
                        .getValue()));
            } else if (LIMIT.equals(e.getKey())) {
                config.setLimit((int) ObjectConversionUtils.getAsLong(e
                        .getValue()));
            } else if (QUERY_TIMEOUT.equals(e.getKey())) {
                config.setQueryTimeoutMillis(ObjectConversionUtils.getAsLong(e
                        .getValue()));
            } else if (ORDER_BY.equals(e.getKey())) {
                config.setOrderBy(ObjectConversionUtils.getAsText(e.getValue()));
            } else if (GROUP_BY.equals(e.getKey())) {
                config.setGroupBy(ObjectConversionUtils.getAsText(e.getValue()));
            } else if (ABORT_UPON_FAILURE.equals(e.getKey())) {
                config.setAbortUponPartialFailure(ObjectConversionUtils
                        .getAsBoolean(e.getValue()));
            }
        }
        return config;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getQueryTimeoutMillis() {
        return queryTimeoutMillis;
    }

    public void setQueryTimeoutMillis(long queryTimeoutMillis) {
        this.queryTimeoutMillis = queryTimeoutMillis;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void addFilterCriteria(String name, Object value) {
        filterCriteria.put(name, value);
    }

    public Map<String, Object> getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(Map<String, Object> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    public boolean isAbortUponPartialFailure() {
        return abortUponPartialFailure;
    }

    public void setAbortUponPartialFailure(boolean abortUponPartialFailure) {
        this.abortUponPartialFailure = abortUponPartialFailure;
    }

}
