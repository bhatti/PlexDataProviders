package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.domain.Trade;

public class TradeMarshaller implements DataRowSetMarshaller<Trade> {
    private static MetaField tradeId = MetaFieldFactory.createInteger(
            "tradeId", Trade.class.getSimpleName(), false);
    private static MetaField date = MetaFieldFactory.createDate("trade.date",
            Trade.class.getSimpleName(), false);
    private static MetaField price = MetaFieldFactory.createDecimal(
            "trade.price", Trade.class.getSimpleName(), false);
    private static MetaField quantity = MetaFieldFactory.createDecimal(
            "trade.quantity", Trade.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(tradeId, date,
            SharedMeta.symbol, SharedMeta.exchange, price, quantity);

    @Override
    public DataRowSet marshal(Trade trade) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(trade, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<Trade> trades) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (Trade trade : trades) {
            marshal(trade, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(Trade trade, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(tradeId, trade.getId(), rowNum);
        rowset.addValueAtRow(SharedMeta.symbol,
                trade.getSecurity().getSymbol(), rowNum);
        rowset.addValueAtRow(SharedMeta.exchange, trade.getExchange(), rowNum);
        rowset.addValueAtRow(price, trade.getPrice(), rowNum);
        rowset.addValueAtRow(quantity, trade.getQuantity(), rowNum);
        rowset.addValueAtRow(date, trade.getDate(), rowNum);
    }

    @Override
    public Trade unmarshal(DataRowSet rowset) {
        Trade trade = new Trade();
        return trade; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
