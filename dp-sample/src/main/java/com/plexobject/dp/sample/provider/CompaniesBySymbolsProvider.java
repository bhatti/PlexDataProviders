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
import com.plexobject.dp.sample.domain.Company;
import com.plexobject.dp.sample.marshal.CompanyMarshaller;

public class CompaniesBySymbolsProvider extends BaseProvider {
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static Metadata parameterMeta = Metadata.from(symbol);
    private static Metadata optionalMeta = Metadata.from();
    private static CompanyMarshaller marshaller = new CompanyMarshaller();

    public CompaniesBySymbolsProvider() {
        super("CompaniesBySymbolsProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {

        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            String id = parameter.getValueAsText(symbol, i);
            Map<String, Object> criteria = new HashMap<>();
            criteria.put("symbol", id.toUpperCase());
            Collection<Company> companies = DaoLocator.companyDao
                    .query(criteria);
            if (companies.size() > 0) {
                Company company = companies.iterator().next();
                DataRowSet rowset = marshaller.marshal(company);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
