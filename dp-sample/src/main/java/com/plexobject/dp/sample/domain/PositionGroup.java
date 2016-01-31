package com.plexobject.dp.sample.domain;

import java.util.ArrayList;
import java.util.List;

public class PositionGroup extends Position {
    private long positionGroupId;
    private String name;
    private List<Position> positions = new ArrayList<>();

    public PositionGroup() {

    }

    public PositionGroup(long positionGroupId, String name,
            List<Position> positions) {
        this.positionGroupId = positionGroupId;
        this.name = name;
        this.positions = positions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public long getPositionGroupId() {
        return positionGroupId;
    }

    public void setPositionGroupId(long positionGroupId) {
        this.positionGroupId = positionGroupId;
    }

}
