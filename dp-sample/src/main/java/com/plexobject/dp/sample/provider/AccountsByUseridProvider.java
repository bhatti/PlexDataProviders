package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.Account;
import com.plexobject.dp.sample.domain.User;
import com.plexobject.dp.sample.marshal.AccountMarshaller;

public class AccountsByUseridProvider extends BaseProvider {
    private static MetaField userId = MetaFieldFactory.create("userId",
            MetaFieldType.SCALAR_INTEGER);
    private static Metadata parameterMeta = Metadata.from(userId);
    private static Metadata optionalMeta = Metadata.from();
    private static AccountMarshaller marshaller = new AccountMarshaller();

    public AccountsByUseridProvider() {
        super("AccountsByUseridProvider", parameterMeta, optionalMeta,
                marshaller.getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            Long id = parameter.getValueAsLong(userId, i);
            User user = DaoLocator.userDao.getById(id);
            if (user == null) {
                continue;
            }
            Collection<Account> accounts = user.getAccounts();
            for (Account account : accounts) {
                DataRowSet rowset = marshaller.marshal(account);
                nextRow = addRowSet(response, rowset, nextRow);
            }
        }
    }
}
