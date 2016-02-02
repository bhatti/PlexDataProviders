package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.Position;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.marshal.PositionMarshaller;

public class PositionsBySymbolsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static PositionMarshaller marshaller = new PositionMarshaller();

    public PositionsBySymbolsProvider() {
        super("PositionsBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        //
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(SharedMeta.symbol, i);
            Collection<Position> positions = DaoLocator.positionDao
                    .filter(new Filter<Position>() {
                        @Override
                        public boolean accept(Position object) {
                            return object.getSecurity().getSymbol().equals(id);
                        }
                    });
            //
            if (positions.size() > 0) {
                Position position = positions.iterator().next();
                DataRowSet rowset = marshaller.marshal(position);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
