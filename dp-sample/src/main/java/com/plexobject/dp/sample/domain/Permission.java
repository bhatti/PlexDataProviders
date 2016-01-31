package com.plexobject.dp.sample.domain;

public class Permission implements Idable<Long> {
    private long id;
    private String resource;
    private String[] actions;

    public Permission() {

    }

    public Permission(long id, String resource, String[] actions) {
        this.id = id;
        this.resource = resource;
        this.actions = actions;
    }

    public Long getId() {
        return id;
    }

    public void setId(long permissionId) {
        this.id = permissionId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

}
