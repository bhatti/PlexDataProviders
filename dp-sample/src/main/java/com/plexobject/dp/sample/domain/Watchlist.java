package com.plexobject.dp.sample.domain;

import java.util.Collection;
import java.util.HashSet;

public class Watchlist implements Idable<Long> {
    private long id;
    private String name;
    private Collection<Security> securities = new HashSet<>();

    public Watchlist() {

    }

    public Watchlist(long id, String name, Collection<Security> securities) {
        this.id = id;
        this.name = name;
        this.securities.addAll(securities);
    }

    public Collection<String> getSymbols() {
        Collection<String> symbols = new HashSet<>();
        for (Security security : securities) {
            symbols.add(security.getSymbol());
        }
        return symbols;
    }
    public Long getId() {
        return id;
    }

    public void setId(long watchlistId) {
        this.id = watchlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Security> getSecurities() {
        return securities;
    }

    public void setSecurities(Collection<Security> securities) {
        this.securities.clear();
        this.securities.addAll(securities);
    }

}
