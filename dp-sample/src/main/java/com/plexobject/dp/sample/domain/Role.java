package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class Role implements Idable<Long> {
    private long id;
    private String name;
    private List<Permission> permissions = new ArrayList<>();

    public Role() {
    }

    public Role(long id, String name, List<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(long roleId) {
        this.id = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}
