package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Watchlist;

public class WatchlistMarshaller implements DataRowSetMarshaller<Watchlist> {
    private static MetaField watchlistId = MetaFieldFactory.createInteger(
            "watchlistId", Watchlist.class.getSimpleName(), false);
    private static MetaField name = MetaFieldFactory.createText(
            "watchlist.name", Watchlist.class.getSimpleName(), false);
    private static MetaField securities = MetaFieldFactory.createRowset(
            "watchlist.securities", Watchlist.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(watchlistId, name,
            securities);
    private static final SecurityMarshaller securityMarshaller = new SecurityMarshaller();

    @Override
    public DataRowSet marshal(Watchlist watchlist) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(watchlistId, watchlist.getId(), 0);
        rowset.addValueAtRow(name, watchlist.getName(), 0);
        rowset.addValueAtRow(securities,
                securityMarshaller.marshal(watchlist.getSecurities()), 0);
        return rowset;
    }

    @Override
    public Watchlist unmarshal(DataRowSet rowset) {
        Watchlist watchlist = new Watchlist();
        return watchlist; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}
