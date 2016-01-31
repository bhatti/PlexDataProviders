package com.plexobject.dp.sample.domain;

import java.util.Date;

public class CompanyEvent implements Idable<Long> {
    public enum Type {
        DIVIDEND, EARNINGS
    }

    private long id;
    private Type type;
    private String name;
    private Date date;

    public CompanyEvent() {

    }

    public CompanyEvent(long id, Type type, String name, Date date) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(long eventId) {
        this.id = eventId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
