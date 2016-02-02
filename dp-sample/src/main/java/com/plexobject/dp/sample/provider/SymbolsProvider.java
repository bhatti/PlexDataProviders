package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.marshal.SecurityMarshaller;

public class SymbolsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from();
    private static Metadata optionalMeta = Metadata.from();
    private static SecurityMarshaller marshaller = new SecurityMarshaller();

    public SymbolsProvider() {
        super("SymbolsProvider", parameterMeta, optionalMeta, marshaller
                .getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {

        Collection<Security> securities = DaoLocator.securityDao.getAll();
        int nextRow = 0;
        for (Security security : securities) {
            DataRowSet rowset = marshaller.marshal(security);
            nextRow = addRowSet(response, rowset, nextRow);
        }
    }
}