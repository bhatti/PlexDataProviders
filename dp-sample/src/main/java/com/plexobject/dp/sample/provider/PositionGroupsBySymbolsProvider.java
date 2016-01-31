package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.PositionGroup;
import com.plexobject.dp.sample.marshal.PositionMarshaller;

public class PositionGroupsBySymbolsProvider extends BaseProvider {
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static Metadata parameterMeta = Metadata.from(symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static PositionMarshaller marshaller = new PositionMarshaller();

    public PositionGroupsBySymbolsProvider() {
        super("PositionGroupsBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        //
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(symbol, i);
            Collection<PositionGroup> groups = DaoLocator.positionGroupDao
                    .filter(new Filter<PositionGroup>() {
                        @Override
                        public boolean accept(PositionGroup object) {
                            return object.getSecurity().getSymbol().equals(id);
                        }
                    });
            //
            if (groups.size() > 0) {
                PositionGroup group = groups.iterator().next();
                DataRowSet rowset = marshaller.marshal(group);
                for (MetaField field : response.getMetadata().getMetaFields()) {
                    response.addValueAtRow(field, rowset.getValue(field, 0),
                            nextRow);
                }
                nextRow++;
            }
        }
    }
}
