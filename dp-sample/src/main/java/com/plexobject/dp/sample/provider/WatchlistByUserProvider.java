package com.plexobject.dp.sample.provider;

import java.util.Arrays;
import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.domain.User;
import com.plexobject.dp.sample.domain.Watchlist;
import com.plexobject.dp.sample.marshal.WatchlistMarshaller;

public class WatchlistByUserProvider extends BaseProvider {
    private static MetaField watchlistName = MetaFieldFactory.createText(
            "watchlistName", Watchlist.class.getSimpleName(), false);
    private static Metadata parameterMeta = Metadata.from(SharedMeta.userId);
    private static Metadata optionalMeta = Metadata.from(SharedMeta.symbol,
            watchlistName);
    private static WatchlistMarshaller marshaller = new WatchlistMarshaller();

    public WatchlistByUserProvider() {
        super("WatchlistByUserProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            Long id = parameter.getValueAsLong(SharedMeta.userId, i);
            User user = DaoLocator.userDao.getById(id);
            if (user == null) {
                continue;
            }
            String filterWatchlistName = null;
            Collection<String> filterSymbols = null;
            if (parameter.hasFieldValue(watchlistName, i)) {
                filterWatchlistName = parameter
                        .getValueAsText(watchlistName, i);
            }
            if (parameter.hasFieldValue(SharedMeta.symbol, i)) {
                filterSymbols = Arrays.asList(parameter.getValueAsText(
                        SharedMeta.symbol, i));
            }
            Collection<Watchlist> watchLists = user.getWatchlists();
            for (Watchlist watchlist : watchLists) {
                boolean matched = true;
                if (filterWatchlistName != null
                        && !watchlist.getName().equals(filterWatchlistName)) {
                    matched = false;
                }
                if (filterSymbols != null
                        && !filterSymbols.containsAll(watchlist.getSymbols())) {
                    matched = false;
                }
                if (matched) {
                    DataRowSet rowset = marshaller.marshal(watchlist);
                    nextRow = addRowSet(response, rowset, nextRow);
                }
            }
        }
    }
}
