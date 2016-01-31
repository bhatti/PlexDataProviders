package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account implements Idable<Long> {
    private long id;
    private String accountName;
    private AccountType accountType;
    private List<Position> positions = new ArrayList<>();
    private Map<String, PositionGroup> positionGroups = new HashMap<>();
    private List<Order> orders = new ArrayList<>();

    public Account() {

    }

    public Account(long id, String accountName, AccountType accountType) {
        this.id = id;
        this.accountName = accountName;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Map<String, PositionGroup> getPositionGroups() {
        return positionGroups;
    }

    public void setPositionGroups(Map<String, PositionGroup> positionGroups) {
        this.positionGroups = positionGroups;
    }

  

}
