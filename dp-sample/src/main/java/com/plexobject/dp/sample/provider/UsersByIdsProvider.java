package com.plexobject.dp.sample.provider;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.sample.dao.DaoLocator;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.domain.User;
import com.plexobject.dp.sample.marshal.UserMarshaller;

public class UsersByIdsProvider extends BaseProvider {
    private static Metadata parameterMeta = Metadata.from(SharedMeta.userId);
    private static Metadata optionalMeta = Metadata.from();
    private static UserMarshaller marshaller = new UserMarshaller();

    public UsersByIdsProvider() {
        super("UsersByIdsProvider", parameterMeta, optionalMeta, marshaller
                .getMetadata());
    }

    @Override
    public void produce(DataRowSet parameter, DataRowSet response,
            DataConfiguration config) throws DataProviderException {
        int nextRow = 0;
        for (int i = 0; i < parameter.size(); i++) {
            Long id = parameter.getValueAsLong(SharedMeta.userId, i);
            User user = DaoLocator.userDao.getById(id);
            if (user == null) {
                continue;
            }
            DataRowSet rowset = marshaller.marshal(user);
            nextRow = addRowSet(response, rowset, nextRow);
        }
    }
}
