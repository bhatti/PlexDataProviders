package com.plexobject.dp.sample.marshal;

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
    private static MetaField accountId = MetaFieldFactory.create("accountId",
            MetaFieldType.SCALAR_INTEGER);
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
        rowset.addValueAtRow(positionId, position.getId(), 0);
        rowset.addValueAtRow(accountId, position.getAccount().getId(), 0);
        rowset.addValueAtRow(symbol, position.getSecurity().getSymbol(), 0);
        rowset.addValueAtRow(price, position.getPrice(), 0);
        rowset.addValueAtRow(quantity, position.getQuantity(), 0);
        return rowset;
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
