package com.plexobject.dp.sample.marshal;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.marshal.DataRowSetMarshaller;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.domain.User;

public class UserMarshaller implements DataRowSetMarshaller<User> {
    private static MetaField name = MetaFieldFactory.createText("user.name",
            User.class.getSimpleName(), false);
    private static MetaField email = MetaFieldFactory.createText("user.email",
            User.class.getSimpleName(), false);
    private static MetaField address = MetaFieldFactory.createRowset(
            "user.address", User.class.getSimpleName(), false);
    private static MetaField dateOfBirth = MetaFieldFactory.createDate(
            "user.dateOfBirth", User.class.getSimpleName(), false);
    private static MetaField roles = MetaFieldFactory.createVectorText(
            "user.roles", User.class.getSimpleName(), false);
    private static MetaField portfolio = MetaFieldFactory.createRowset(
            "user.portfolio", User.class.getSimpleName(), false);
    private static MetaField accounts = MetaFieldFactory.createRowset(
            "user.accounts", User.class.getSimpleName(), false);
    private static Metadata responseMeta = Metadata.from(SharedMeta.userId, name, email,
            accounts, address, dateOfBirth, roles, portfolio);
    private static AddressMarshaller addressMarshaller = new AddressMarshaller();
    private static final PortfolioMarshaller portfolioMarshaller = new PortfolioMarshaller();
    private static AccountMarshaller accountMarshaller = new AccountMarshaller();

    @Override
    public DataRowSet marshal(User user) {
        DataRowSet rowset = new DataRowSet(responseMeta);
        rowset.addValueAtRow(SharedMeta.userId, user.getId(), 0);
        rowset.addValueAtRow(name, user.getName(), 0);
        rowset.addValueAtRow(email, user.getEmail(), 0);
        rowset.addValueAtRow(address,
                addressMarshaller.marshal(user.getAddress()), 0);
        rowset.addValueAtRow(accounts,
                accountMarshaller.marshal(user.getAccounts()), 0);
        rowset.addValueAtRow(dateOfBirth, user.getDateOfBirth(), 0);
        rowset.addValueAtRow(roles, user.getRoles(), 0);
        rowset.addValueAtRow(portfolio,
                portfolioMarshaller.marshal(user.getPortfolio()), 0);
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
