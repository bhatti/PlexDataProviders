package com.plexobject.dp.domain;

import java.util.HashMap;
import java.util.Map;

public class DataConfiguration {
    private int page;
    private int limit;
    private long queryTimeoutMillis;
    private String orderBy;
    private String groupBy;
    private int maxThreads;
    private boolean abortUponPartialFailure;
    private Map<String, Object> filterCriteria = new HashMap<>();

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
