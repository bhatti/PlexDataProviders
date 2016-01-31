package com.plexobject.dp.sample.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Security;
import com.plexobject.dp.sample.marshal.SecurityMarshaller;

public class SecuritiesBySymbolsProvider extends BaseProvider {
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static Metadata parameterMeta = Metadata.from(symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static SecurityMarshaller marshaller = new SecurityMarshaller();

    public SecuritiesBySymbolsProvider() {
        super("SecuritiesBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            final String id = parameter.getValueAsText(symbol, i);
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("symbol", id);
            Collection<Security> securities = DaoLocator.securityDao
                    .query(criteria);
            if (securities.size() > 0) {
                Security security = securities.iterator().next();
                DataRowSet rowset = marshaller.marshal(security);
                for (MetaField field : response.getMetadata().getMetaFields()) {
                    response.addValueAtRow(field, rowset.getValue(field, 0),
                            nextRow);
                }
                nextRow++;
            }
        }
    }
}
