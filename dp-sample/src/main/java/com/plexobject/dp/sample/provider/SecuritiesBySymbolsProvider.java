package com.plexobject.dp.sample.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.marshal.SecurityMarshaller;

public class SecuritiesBySymbolsProvider extends BaseProvider {
    static final Logger logger = Logger
            .getLogger(SecuritiesBySymbolsProvider.class);

    private static Metadata parameterMeta = Metadata.from(SharedMeta.symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static SecurityMarshaller marshaller = new SecurityMarshaller();

    public SecuritiesBySymbolsProvider() {
        super("SecuritiesBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            QueryConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(SharedMeta.symbol, i);
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("symbol", id.toUpperCase());
            Collection<Security> securities = DaoLocator.securityDao
                    .query(criteria);
            if (securities.size() > 0) {
                Security security = securities.iterator().next();
                DataRowSet rowset = marshaller.marshal(security);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
