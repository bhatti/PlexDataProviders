package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Company;

public class CompanyMarshaller implements DataRowSetMarshaller<Company> {
    private static MetaField companyId = MetaFieldFactory.create("companyId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField symbol = MetaFieldFactory.create("symbol",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField exchange = MetaFieldFactory.create("exchange",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField name = MetaFieldFactory.create("company.name",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField address = MetaFieldFactory.create(
            "company.address", MetaFieldType.SCALAR_OBJECT);
    private static Metadata responseMeta = Metadata.from(companyId, symbol,
            name, exchange, address);

    @Override
    public DataRowSet marshal(Company company) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(companyId, company.getId(), 0);
        rowset.addValueAtRow(symbol, company.getSymbol(), 0);
        rowset.addValueAtRow(name, company.getName(), 0);
        rowset.addValueAtRow(address, company.getAddress(), 0);
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
