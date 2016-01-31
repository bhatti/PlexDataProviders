package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.User;

public class UserMarshaller implements DataRowSetMarshaller<User> {
    private static MetaField userId = MetaFieldFactory.create("userId",
            MetaFieldType.SCALAR_INTEGER);
    private static MetaField name = MetaFieldFactory.create("user.name",
            MetaFieldType.SCALAR_TEXT);
    private static MetaField email = MetaFieldFactory.create("user.email",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField address = MetaFieldFactory.create("user.address",
            MetaFieldType.SCALAR_OBJECT);
    private static MetaField dateOfBirth = MetaFieldFactory.create(
            "user.dateOfBirth", MetaFieldType.SCALAR_DECIMAL);
    private static MetaField roles = MetaFieldFactory.create("user.roles",
            MetaFieldType.SCALAR_DECIMAL);
    private static MetaField portfolio = MetaFieldFactory.create(
            "user.portfolio", MetaFieldType.SCALAR_OBJECT);
    private static Metadata responseMeta = Metadata.from(userId, name, email,
            address, dateOfBirth, roles, portfolio);

    @Override
    public DataRowSet marshal(User user) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(userId, user.getId(), 0);
        rowset.addValueAtRow(name, user.getName(), 0);
        rowset.addValueAtRow(email, user.getEmail(), 0);
        rowset.addValueAtRow(address, user.getAddress(), 0);
        rowset.addValueAtRow(dateOfBirth, user.getDateOfBirth(), 0);
        rowset.addValueAtRow(roles, user.getRoles(), 0);
        rowset.addValueAtRow(portfolio, user.getPortfolio(), 0);
        return rowset;
    }

    @Override
    public User unmarshal(DataRowSet rowset) {
        User user = new User();
        return user; // TODO fill in fields
    }

    @Override
    public Metadata getMetadata() {
        return responseMeta;
    }
}
