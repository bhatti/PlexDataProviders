package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User implements Idable<Long> {
    private long id;
    private String username;
    private String name;
    private String email;
    private Address address;
    private Date dateOfBirth;
    private List<Role> roles = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Watchlist> watchlists = new ArrayList<>();
    private Portfolio portfolio = new Portfolio();

    public User() {

    }

    public User(long id, String username, String name, String email,
            Address address, Date dateOfBirth, List<Role> roles,
            List<Account> accounts) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.roles = roles;
        this.accounts = accounts;
    }

    public User(long id, String username, String name, String email,
            Address address, Date dateOfBirth, List<Role> roles,
            List<Account> accounts, List<Watchlist> watchlists,
            Portfolio portfolio) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.roles = roles;
        this.accounts = accounts;
        this.watchlists = watchlists;
        this.portfolio = portfolio;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<Watchlist> getWatchlists() {
        return watchlists;
    }

    public void setWatchlists(List<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }

}
