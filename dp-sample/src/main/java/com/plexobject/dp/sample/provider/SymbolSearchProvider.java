package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.marshal.SecurityMarshaller;

public class SymbolSearchProvider extends BaseProvider {
    private static MetaField symbolQuery = MetaFieldFactory.createText(
            "symbolQuery", Security.class.getSimpleName(), false);
    private static Metadata parameterMeta = Metadata.from(symbolQuery);
    private static Metadata optionalMeta = Metadata.from();
    private static SecurityMarshaller marshaller = new SecurityMarshaller();

    public SymbolSearchProvider() {
        super("SymbolSearchProvider", parameterMeta, optionalMeta, marshaller
                .getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            QueryConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(symbolQuery, i)
                    .toUpperCase();
            Collection<Security> securities = DaoLocator.securityDao
                    .filter(new Filter<Security>() {
                        @Override
                        public boolean accept(Security security) {
                            return security.getSymbol().contains(id);
                        }
                    });
            //
            for (Security security : securities) {
                DataRowSet rowset = marshaller.marshal(security);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
