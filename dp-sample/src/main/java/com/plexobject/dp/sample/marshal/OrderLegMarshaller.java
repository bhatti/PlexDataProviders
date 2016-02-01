package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.OrderLeg;

public class OrderLegMarshaller implements DataRowSetMarshaller<OrderLeg> {
    private static MetaField orderLegId = MetaFieldFactory.create("orderLegId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField positionType = MetaFieldFactory.create(
            "orderLeg.positionType", MetaFieldType.SCALAR_TEXT);
    private static MetaField side = MetaFieldFactory.create("orderLeg.side",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField price = MetaFieldFactory.create("orderLeg.price",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField quantity = MetaFieldFactory.create(
            "orderLeg.quantity", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField fillPrice = MetaFieldFactory.create(
            "orderLeg.fillPrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField fillQuantity = MetaFieldFactory.create(
            "orderLeg.fillQuantity", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField trades = MetaFieldFactory.create(
            "orderLeg.trades", MetaFieldType.ROWSET);
    private static Metadata responseMeta = Metadata.from(orderLegId,
            positionType, side, price, quantity, fillPrice, fillQuantity,
            trades);

    private static TradeMarshaller tradeMarshaller = new TradeMarshaller();

    @Override
    public DataRowSet marshal(OrderLeg leg) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(leg, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<OrderLeg> legs) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (OrderLeg leg : legs) {
            marshal(leg, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(OrderLeg leg, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(orderLegId, leg.getId(), rowNum);
        rowset.addValueAtRow(positionType, leg.getPositionType().name(), rowNum);
        rowset.addValueAtRow(side, leg.getSide().name(), rowNum);
        rowset.addValueAtRow(price, leg.getPrice(), rowNum);
        rowset.addValueAtRow(quantity, leg.getQuantity(), rowNum);
        rowset.addValueAtRow(trades, tradeMarshaller.marshal(leg.getTrades()),
                rowNum);
        rowset.addValueAtRow(fillPrice, leg.getFillPrice(), rowNum);
        rowset.addValueAtRow(fillQuantity, leg.getFillQuantity(), rowNum);
    }

    @Override
    public OrderLeg unmarshal(DataRowSet rowset) {
        OrderLeg leg = new OrderLeg();
        return leg; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
