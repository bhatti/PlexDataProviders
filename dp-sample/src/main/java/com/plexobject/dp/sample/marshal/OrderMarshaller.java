package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Order;
import com.plexobject.dp.sample.domain.OrderStatus;
import com.plexobject.dp.sample.domain.SharedMeta;

public class OrderMarshaller implements DataRowSetMarshaller<Order> {
    private static MetaField orderId = MetaFieldFactory.create("orderId",
            Order.class.getSimpleName(), MetaFieldType.SCALAR_INTEGER, true);
    private static MetaField date = MetaFieldFactory.createDate("order.date",
            Order.class.getSimpleName(), false);
    private static MetaField fillDate = MetaFieldFactory.createDate(
            "order.fillDate", Order.class.getSimpleName(), false);
    private static MetaField priceType = MetaFieldFactory.createText(
            "order.priceType", Order.class.getSimpleName(), false);
    private static MetaField status = MetaFieldFactory.createText(
            "order.status", Order.class.getSimpleName(), false);
    private static MetaField price = MetaFieldFactory.createDecimal(
            "order.price", Order.class.getSimpleName(), false);
    private static MetaField quantity = MetaFieldFactory.createDecimal(
            "order.quantity", Order.class.getSimpleName(), false);
    private static MetaField fillPrice = MetaFieldFactory.createDecimal(
            "order.fillPrice", Order.class.getSimpleName(), false);
    private static MetaField fillQuantity = MetaFieldFactory.createDecimal(
            "order.fillQuantity", Order.class.getSimpleName(), false);
    private static MetaField orderLegs = MetaFieldFactory.createRowset(
            "order.orderLegs", Order.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(orderId,
            SharedMeta.accountId, date, fillDate, SharedMeta.marketSession,
            SharedMeta.symbol, SharedMeta.exchange, status, priceType, price,
            quantity, fillPrice, fillQuantity, orderLegs);
    private static final OrderLegMarshaller orderLegMarshaller = new OrderLegMarshaller();

    @Override
    public DataRowSet marshal(Order order) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(rowset, order, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Order> orders) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Order order : orders) {
            marshal(rowset, order, rowset.size());
        }
        return rowset;
    }

    public void marshal(DataRowSet rowset, Order order, int rowNum) {
        rowset.addValueAtRow(orderId, order.getId(), rowNum);
        rowset.addValueAtRow(SharedMeta.accountId, order.getAccount().getId(),
                rowNum);
        rowset.addValueAtRow(SharedMeta.marketSession, order.getMarketSession()
                .name(), rowNum);
        rowset.addValueAtRow(SharedMeta.symbol,
                order.getSecurity().getSymbol(), rowNum);
        rowset.addValueAtRow(SharedMeta.exchange, order.getExchange(), rowNum);
        rowset.addValueAtRow(status, order.getStatus().name(), rowNum);
        rowset.addValueAtRow(priceType, order.getPriceType().name(), rowNum);
        rowset.addValueAtRow(price, order.getPrice(), rowNum);
        rowset.addValueAtRow(quantity, order.getQuantity(), rowNum);
        rowset.addValueAtRow(date, order.getDate(), rowNum);
        if (order.getStatus() == OrderStatus.FILLED) {
            rowset.addValueAtRow(fillDate, order.getFillDate(), rowNum);
            rowset.addValueAtRow(fillPrice, order.getFillPrice(), rowNum);
            rowset.addValueAtRow(fillQuantity, order.getFillQuantity(), rowNum);
        }
        rowset.addValueAtRow(orderLegs,
                orderLegMarshaller.marshal(order.getOrderLegs()), rowNum);
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
