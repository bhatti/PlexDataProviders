package com.plexobject.dp.sample.provider;

import java.util.Collection;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.User;
import com.plexobject.dp.sample.marshal.UserMarshaller;

public class UsersProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.fromRaw();
    private static Metadata optionalMeta = Metadata.fromRaw();
    private static UserMarshaller marshaller = new UserMarshaller();

    public UsersProvider() {
        super("UsersProvider", parameterMeta, optionalMeta, marshaller
                .getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {

        Collection<User> users = DaoLocator.userDao.getAll();
        int nextRow = 0;
        for (User user : users) {
            DataRowSet rowset = marshaller.marshal(user);
            nextRow = addRowSet(response, rowset, nextRow);
        }
    }
}