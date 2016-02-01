package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.Address;

public class AddressMarshaller implements DataRowSetMarshaller<Address> {
    private static MetaField addressId = MetaFieldFactory.create("addressId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField street = MetaFieldFactory.create("address.street",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField city = MetaFieldFactory.create("address.city",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField state = MetaFieldFactory.create("address.state",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField zip = MetaFieldFactory.create("address.zip",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField country = MetaFieldFactory.create(
            "address.country", MetaFieldType.SCALAR_TEXT);
    private static Metadata responseMeta = Metadata.from(addressId, street,
            city, state, zip, country);

    @Override
    public DataRowSet marshal(Address address) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(addressId, address.getId(), 0);
        rowset.addValueAtRow(street, address.getStreet(), 0);
        rowset.addValueAtRow(city, address.getCity(), 0);
        rowset.addValueAtRow(zip, address.getZip(), 0);
        rowset.addValueAtRow(country, address.getCountry(), 0);
        return rowset;
    }

    @Override
    public Address unmarshal(DataRowSet rowset) {
        Address account = new Address();
        return account;
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }

}
