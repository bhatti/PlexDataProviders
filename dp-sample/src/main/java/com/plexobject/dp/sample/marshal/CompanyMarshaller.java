package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Company;

public class CompanyMarshaller implements DataRowSetMarshaller<Company> {
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField exchange = MetaFieldFactory.create("exchange",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField name = MetaFieldFactory.create("company.name",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField address = MetaFieldFactory.create(
            "company.address", MetaFieldType.ROWSET);
    private static Metadata responseMeta = Metadata.from(symbol, name,
            exchange, address);
    private static AddressMarshaller addressMarshaller = new AddressMarshaller();

    @Override
    public DataRowSet marshal(Company company) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(symbol, company.getSymbol(), 0);
        rowset.addValueAtRow(name, company.getName(), 0);
        rowset.addValueAtRow(exchange, company.getExchange(), 0);
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
