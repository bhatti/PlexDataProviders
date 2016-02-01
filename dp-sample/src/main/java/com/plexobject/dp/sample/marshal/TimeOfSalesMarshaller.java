package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.TimeOfSales;

public class TimeOfSalesMarshaller implements DataRowSetMarshaller<TimeOfSales> {
    private static MetaField date = MetaFieldFactory.create("timeOfSale.date",
            MetaFieldType.SCALAR_DATE);
    private static MetaField price = MetaFieldFactory.create(
            "timeOfSale.price", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField symbol = MetaFieldFactory.create(
            "timeOfSale.symbol", MetaFieldType.SCALAR_TEXT);
    private static MetaField volume = MetaFieldFactory.create("volume",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField exchange = MetaFieldFactory.create(
            "timeOfSale.exchange", MetaFieldType.SCALAR_TEXT);
    private static Metadata responseMeta = Metadata.from(volume, exchange,
            date, symbol, exchange, price);

    @Override
    public DataRowSet marshal(TimeOfSales sale) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        marshal(sale, rowset, 0);
        return rowset;
    }

    public DataRowSet marshal(Collection<TimeOfSales> sales) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        for (TimeOfSales sale : sales) {
            marshal(sale, rowset, rowset.size());
        }
        return rowset;
    }

    private void marshal(TimeOfSales sale, DataRowSet rowset, int rowNum) {
        rowset.addValueAtRow(symbol, sale.getSymbol(), rowNum);
        rowset.addValueAtRow(exchange, sale.getExchange(), rowNum);
        rowset.addValueAtRow(price, sale.getPrice(), rowNum);
        rowset.addValueAtRow(volume, sale.getVolume(), rowNum);
        rowset.addValueAtRow(date, sale.getDate(), rowNum);
    }

    @Override
    public TimeOfSales unmarshal(DataRowSet rowset) {
        TimeOfSales sale = new TimeOfSales();
        return sale; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
