package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class PositionGroup extends Position {
    private String name;
    private List<Position> position = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }

}
