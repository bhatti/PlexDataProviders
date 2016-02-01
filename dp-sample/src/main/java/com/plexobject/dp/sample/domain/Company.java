package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class Company implements Idable<String> {
    private String symbol;
    private String exchange;
    private String name;
    private Address address;
    private List<CompanyEvent> events = new ArrayList<>();

    public Company() {

    }

    public Company(String symbol, String exchange, String name,
            Address address, List<CompanyEvent> events) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.name = name;
        this.address = address;
        this.events = events;
    }

    public String getId() {
        return symbol;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Company other = (Company) obj;
        if (symbol == null) {
            if (other.symbol != null)
                return false;
        } else if (!symbol.equals(other.symbol))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Company [symbol=" + symbol + ", exchange=" + exchange
                + ", name=" + name + ", address=" + address + ", events="
                + events + "]";
    }

}
