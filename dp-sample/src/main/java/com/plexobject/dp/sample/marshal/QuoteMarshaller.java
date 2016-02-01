package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Quote;

public class QuoteMarshaller implements DataRowSetMarshaller<Quote> {
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField marketSession = MetaFieldFactory.create(
            "quote.marketSession", MetaFieldType.SCALAR_TEXT);
    private static MetaField bidPrice = MetaFieldFactory.create(
            "quote.bidPrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField askPrice = MetaFieldFactory.create(
            "quote.askPrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField closePrice = MetaFieldFactory.create(
            "quote.closePrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField tradePrice = MetaFieldFactory.create(
            "quote.tradePrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField markPrice = MetaFieldFactory.create(
            "quote.markPrice", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField volume = MetaFieldFactory.create("quote.volume",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField sales = MetaFieldFactory.create("quote.sales",
            MetaFieldType.ROWSET);
    private static Metadata responseMeta = Metadata.from(symbol, marketSession,
            bidPrice, askPrice, closePrice, tradePrice, markPrice, volume,
            sales);

    private static final TimeOfSalesMarshaller TimeOfSalesMarshaller = new TimeOfSalesMarshaller();

    @Override
    public DataRowSet marshal(Quote quote) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(symbol, quote.getSecurity().getSymbol(), 0);
        rowset.addValueAtRow(marketSession, quote.getMarketSession().name(), 0);
        rowset.addValueAtRow(bidPrice, quote.getBidPrice(), 0);
        rowset.addValueAtRow(askPrice, quote.getAskPrice(), 0);
        rowset.addValueAtRow(closePrice, quote.getClosePrice(), 0);
        rowset.addValueAtRow(closePrice, quote.getClosePrice(), 0);
        rowset.addValueAtRow(tradePrice, quote.getClosePrice(), 0);
        rowset.addValueAtRow(markPrice, quote.getClosePrice(), 0);
        rowset.addValueAtRow(volume, quote.getVolume(), 0);
        rowset.addValueAtRow(sales,
                TimeOfSalesMarshaller.marshal(quote.getSales()), 0);
        return rowset;
    }

    @Override
    public Quote unmarshal(DataRowSet rowset) {
        Quote quote = new Quote();
        return quote; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}
