package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Position;

public class PositionMarshaller implements DataRowSetMarshaller<Position> {
    private static MetaField positionId = MetaFieldFactory.create("positionId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField accountId = MetaFieldFactory.create(
            "position.accountId", MetaFieldType.SCALAR_INTEGER);
    private static MetaField price = MetaFieldFactory.create("position.price",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField quantity = MetaFieldFactory.create(
            "position.quantity", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static Metadata responseMeta = Metadata.from(positionId, accountId,
            price, quantity, symbol);

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
        rowset.addValueAtRow(accountId, position.getAccount().getId(), rowNum);
        rowset.addValueAtRow(symbol, position.getSecurity().getSymbol(), rowNum);
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
