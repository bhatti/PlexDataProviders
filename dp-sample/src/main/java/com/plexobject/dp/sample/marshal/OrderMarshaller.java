package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Order;
import com.plexobject.dp.sample.domain.OrderStatus;

public class OrderMarshaller implements DataRowSetMarshaller<Order> {
    private static MetaField orderId = MetaFieldFactory.create("orderId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField accountId = MetaFieldFactory.create(
            "order.accountId", MetaFieldType.SCALAR_INTEGER);
    private static MetaField date = MetaFieldFactory.create("order.date",
            MetaFieldType.SCALAR_DATE);
    private static MetaField fillDate = MetaFieldFactory.create(
            "order.fillDate", MetaFieldType.SCALAR_DATE);
    private static MetaField marketSession = MetaFieldFactory.create(
            "marketSession", MetaFieldType.SCALAR_TEXT);
    private static MetaField priceType = MetaFieldFactory.create(
            "order.priceType", MetaFieldType.SCALAR_TEXT);
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField exchange = MetaFieldFactory.create("exchange",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField status = MetaFieldFactory.create("order.status",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField price = MetaFieldFactory.create("order.price",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField quantity = MetaFieldFactory.create(
            "order.quantity", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField fillPrice = MetaFieldFactory.create(
            "order.fillPrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField fillQuantity = MetaFieldFactory.create(
            "forder.illQuantity", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField orderLegs = MetaFieldFactory.create(
            "order.orderLegs", MetaFieldType.VECTOR_OBJECT);
    private static Metadata responseMeta = Metadata.from(orderId, accountId,
            date, fillDate, marketSession, symbol, exchange, status, priceType,
            price, quantity, fillPrice, fillQuantity, orderLegs);

    @Override
    public DataRowSet marshal(Order order) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(orderId, order.getId(), 0);
        rowset.addValueAtRow(accountId, order.getAccount().getId(), 0);
        rowset.addValueAtRow(marketSession, order.getMarketSession(), 0);
        rowset.addValueAtRow(symbol, order.getSecurity().getSymbol(), 0);
        rowset.addValueAtRow(exchange, order.getExchange(), 0);
        rowset.addValueAtRow(status, order.getStatus(), 0);
        rowset.addValueAtRow(priceType, order.getPriceType(), 0);
        rowset.addValueAtRow(price, order.getPrice(), 0);
        rowset.addValueAtRow(quantity, order.getQuantity(), 0);
        rowset.addValueAtRow(orderLegs, order.getOrderLegs(), 0);
        rowset.addValueAtRow(date, order.getDate(), 0);
        if (order.getStatus() == OrderStatus.FILLED) {
            rowset.addValueAtRow(fillDate, order.getFillDate(), 0);
            rowset.addValueAtRow(fillPrice, order.getFillPrice(), 0);
            rowset.addValueAtRow(fillQuantity, order.getFillQuantity(), 0);
        }
        return rowset;
    }

    @Override
    public Order unmarshal(DataRowSet rowset) {
        Order order = new Order();
        return order; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
