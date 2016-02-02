package com.plexobject.dp.sample.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Quote;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.marshal.QuoteMarshaller;

public class QuotesBySymbolsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static QuoteMarshaller marshaller = new QuoteMarshaller();

    public QuotesBySymbolsProvider() {
        super("QuotesBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(SharedMeta.symbol, i);
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("symbol", id);
            Collection<Quote> quotes = DaoLocator.quoteDao.query(criteria);
            if (quotes.size() > 0) {
                Quote quote = quotes.iterator().next();
                DataRowSet rowset = marshaller.marshal(quote);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
