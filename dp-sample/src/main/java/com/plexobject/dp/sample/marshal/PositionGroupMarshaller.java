package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.PositionGroup;

public class PositionGroupMarshaller implements
        DataRowSetMarshaller<PositionGroup> {
    private static MetaField positionGroupId = MetaFieldFactory.create(
            "positionGroupId", MetaFieldType.SCALAR_INTEGER);
    private static MetaField name = MetaFieldFactory.create(
            "positionGroup.name", MetaFieldType.SCALAR_TEXT);
    private static MetaField positions = MetaFieldFactory.create(
            "positionGroup.positions", MetaFieldType.VECTOR_OBJECT);
    private static Metadata responseMeta = Metadata.from(positionGroupId, name,
            positions);

    @Override
    public DataRowSet marshal(PositionGroup group) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(positionGroupId, group.getId(), 0);
        rowset.addValueAtRow(name, group.getName(), 0);
        rowset.addValueAtRow(positions, group.getPositions(), 0);
        return rowset;
    }

    @Override
    public PositionGroup unmarshal(DataRowSet rowset) {
        PositionGroup group = new PositionGroup();
        return group; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
