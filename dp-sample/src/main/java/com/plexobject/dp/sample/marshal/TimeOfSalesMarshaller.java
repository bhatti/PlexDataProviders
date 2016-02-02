package com.plexobject.dp.sample.marshal;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.domain.TimeOfSales;

public class TimeOfSalesMarshaller implements DataRowSetMarshaller<TimeOfSales> {
    private static MetaField date = MetaFieldFactory.createDate(
            "timeOfSale.date", TimeOfSales.class.getSimpleName(), false);
    private static MetaField price = MetaFieldFactory.createDecimal(
            "timeOfSale.price", TimeOfSales.class.getSimpleName(), false);
    private static MetaField volume = MetaFieldFactory.createInteger(
            "timeOfSale.volume", TimeOfSales.class.getSimpleName(), false);
    private static MetaField exchange = MetaFieldFactory.create(
            "timeOfSale.exchange", TimeOfSales.class.getSimpleName(),
            MetaFieldType.SCALAR_TEXT, true);
    private static Metadata responseMeta = Metadata.from(volume, exchange,
            date, SharedMeta.symbol, exchange, price);

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
        rowset.addValueAtRow(SharedMeta.symbol, sale.getSymbol(), rowNum);
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
