package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Quote;
import com.plexobject.dp.sample.domain.SharedMeta;

public class QuoteMarshaller implements DataRowSetMarshaller<Quote> {
    private static MetaField bidPrice = MetaFieldFactory.createDecimal(
            "quote.bidPrice", Quote.class.getSimpleName(), false);
    private static MetaField askPrice = MetaFieldFactory.createDecimal(
            "quote.askPrice", Quote.class.getSimpleName(), false);
    private static MetaField closePrice = MetaFieldFactory.createDecimal(
            "quote.closePrice", Quote.class.getSimpleName(), false);
    private static MetaField tradePrice = MetaFieldFactory.createDecimal(
            "quote.tradePrice", Quote.class.getSimpleName(), false);
    private static MetaField markPrice = MetaFieldFactory.createDecimal(
            "quote.markPrice", Quote.class.getSimpleName(), false);
    private static MetaField volume = MetaFieldFactory.createDecimal(
            "quote.volume", Quote.class.getSimpleName(), false);
    private static MetaField sales = MetaFieldFactory.createRowset(
            "quote.sales", Quote.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(SharedMeta.symbol,
            SharedMeta.marketSession, bidPrice, askPrice, closePrice,
            tradePrice, markPrice, volume, sales);

    private static final TimeOfSalesMarshaller TimeOfSalesMarshaller = new TimeOfSalesMarshaller();

    @Override
    public DataRowSet marshal(Quote quote) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(SharedMeta.symbol,
                quote.getSecurity().getSymbol(), 0);
        rowset.addValueAtRow(SharedMeta.marketSession, quote.getMarketSession()
                .name(), 0);
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
