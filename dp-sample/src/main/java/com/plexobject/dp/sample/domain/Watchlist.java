package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {
    private long watchlistId;
    private String name;
    private List<Security> securities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Security> getSecurities() {
        return securities;
    }

    public void setSecurities(List<Security> securities) {
        this.securities = securities;
    }

    public long getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(long watchlistId) {
        this.watchlistId = watchlistId;
    }

}
