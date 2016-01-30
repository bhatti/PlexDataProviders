package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String symbol;
    private String exchange;
    private String name;
    private Address address;
    private List<CompanyEvent> events = new ArrayList<>();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<CompanyEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CompanyEvent> events) {
        this.events = events;
    }

}
