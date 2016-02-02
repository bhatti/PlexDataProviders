package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Company;
import com.plexobject.dp.sample.domain.SharedMeta;

public class CompanyMarshaller implements DataRowSetMarshaller<Company> {
    private static MetaField name = MetaFieldFactory.createText("company.name", Company.class.getSimpleName(), false);
    private static MetaField address = MetaFieldFactory
            .createRowset("company.address", Company.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(SharedMeta.symbol,
            name, SharedMeta.exchange, address);
    private static AddressMarshaller addressMarshaller = new AddressMarshaller();

    @Override
    public DataRowSet marshal(Company company) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(SharedMeta.symbol, company.getSymbol(), 0);
        rowset.addValueAtRow(SharedMeta.exchange, company.getExchange(), 0);
        rowset.addValueAtRow(name, company.getName(), 0);
        rowset.addValueAtRow(address,
                addressMarshaller.marshal(company.getAddress()), 0);
        return rowset;
    }

    @Override
    public Company unmarshal(DataRowSet rowset) {
        Company company = new Company();
        return company; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}
