package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Position;
import com.plexobject.dp.sample.domain.SharedMeta;

public class PositionMarshaller implements DataRowSetMarshaller<Position> {
    private static MetaField positionId = MetaFieldFactory.create("positionId",
            Position.class.getSimpleName(), MetaFieldType.SCALAR_INTEGER, true);
    private static MetaField price = MetaFieldFactory.createDecimal(
            "position.price", Position.class.getSimpleName(), false);
    private static MetaField quantity = MetaFieldFactory.createDecimal(
            "position.quantity", Position.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(positionId,
            SharedMeta.accountId, price, quantity, SharedMeta.symbol);

    @Override
    public DataRowSet marshal(Position position) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(rowset, position, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Position> positions) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Position position : positions) {
            marshal(rowset, position, rowset.size());
        }
        return rowset;
    }

    private void marshal(DataRowSet rowset, Position position, int rowNum) {
        rowset.addValueAtRow(positionId, position.getId(), rowNum);
        rowset.addValueAtRow(SharedMeta.accountId, position.getAccount()
                .getId(), rowNum);
        rowset.addValueAtRow(SharedMeta.symbol, position.getSecurity()
                .getSymbol(), rowNum);
        rowset.addValueAtRow(price, position.getPrice(), rowNum);
        rowset.addValueAtRow(quantity, position.getQuantity(), rowNum);
    }

    @Override
    public Position unmarshal(DataRowSet rowset) {
        Position position = new Position();
        return position; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
